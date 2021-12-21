package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Cellule;
import com.mycompany.myapp.domain.Localite;
import com.mycompany.myapp.domain.SousLocalite;
import com.mycompany.myapp.domain.enumeration.Typecellule;
import com.mycompany.myapp.repository.CelluleRepository;
import com.mycompany.myapp.service.criteria.CelluleCriteria;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CelluleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CelluleResourceIT {

    private static final Typecellule DEFAULT_NOM = Typecellule.CA;
    private static final Typecellule UPDATED_NOM = Typecellule.CO;

    private static final String ENTITY_API_URL = "/api/cellules";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CelluleRepository celluleRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCelluleMockMvc;

    private Cellule cellule;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cellule createEntity(EntityManager em) {
        Cellule cellule = new Cellule().nom(DEFAULT_NOM);
        return cellule;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cellule createUpdatedEntity(EntityManager em) {
        Cellule cellule = new Cellule().nom(UPDATED_NOM);
        return cellule;
    }

    @BeforeEach
    public void initTest() {
        cellule = createEntity(em);
    }

    @Test
    @Transactional
    void createCellule() throws Exception {
        int databaseSizeBeforeCreate = celluleRepository.findAll().size();
        // Create the Cellule
        restCelluleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cellule)))
            .andExpect(status().isCreated());

        // Validate the Cellule in the database
        List<Cellule> celluleList = celluleRepository.findAll();
        assertThat(celluleList).hasSize(databaseSizeBeforeCreate + 1);
        Cellule testCellule = celluleList.get(celluleList.size() - 1);
        assertThat(testCellule.getNom()).isEqualTo(DEFAULT_NOM);
    }

    @Test
    @Transactional
    void createCelluleWithExistingId() throws Exception {
        // Create the Cellule with an existing ID
        cellule.setId(1L);

        int databaseSizeBeforeCreate = celluleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCelluleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cellule)))
            .andExpect(status().isBadRequest());

        // Validate the Cellule in the database
        List<Cellule> celluleList = celluleRepository.findAll();
        assertThat(celluleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCellules() throws Exception {
        // Initialize the database
        celluleRepository.saveAndFlush(cellule);

        // Get all the celluleList
        restCelluleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cellule.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())));
    }

    @Test
    @Transactional
    void getCellule() throws Exception {
        // Initialize the database
        celluleRepository.saveAndFlush(cellule);

        // Get the cellule
        restCelluleMockMvc
            .perform(get(ENTITY_API_URL_ID, cellule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cellule.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()));
    }

    @Test
    @Transactional
    void getCellulesByIdFiltering() throws Exception {
        // Initialize the database
        celluleRepository.saveAndFlush(cellule);

        Long id = cellule.getId();

        defaultCelluleShouldBeFound("id.equals=" + id);
        defaultCelluleShouldNotBeFound("id.notEquals=" + id);

        defaultCelluleShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCelluleShouldNotBeFound("id.greaterThan=" + id);

        defaultCelluleShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCelluleShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCellulesByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        celluleRepository.saveAndFlush(cellule);

        // Get all the celluleList where nom equals to DEFAULT_NOM
        defaultCelluleShouldBeFound("nom.equals=" + DEFAULT_NOM);

        // Get all the celluleList where nom equals to UPDATED_NOM
        defaultCelluleShouldNotBeFound("nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllCellulesByNomIsNotEqualToSomething() throws Exception {
        // Initialize the database
        celluleRepository.saveAndFlush(cellule);

        // Get all the celluleList where nom not equals to DEFAULT_NOM
        defaultCelluleShouldNotBeFound("nom.notEquals=" + DEFAULT_NOM);

        // Get all the celluleList where nom not equals to UPDATED_NOM
        defaultCelluleShouldBeFound("nom.notEquals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllCellulesByNomIsInShouldWork() throws Exception {
        // Initialize the database
        celluleRepository.saveAndFlush(cellule);

        // Get all the celluleList where nom in DEFAULT_NOM or UPDATED_NOM
        defaultCelluleShouldBeFound("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM);

        // Get all the celluleList where nom equals to UPDATED_NOM
        defaultCelluleShouldNotBeFound("nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllCellulesByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        celluleRepository.saveAndFlush(cellule);

        // Get all the celluleList where nom is not null
        defaultCelluleShouldBeFound("nom.specified=true");

        // Get all the celluleList where nom is null
        defaultCelluleShouldNotBeFound("nom.specified=false");
    }

    @Test
    @Transactional
    void getAllCellulesByLocalitesIsEqualToSomething() throws Exception {
        // Initialize the database
        celluleRepository.saveAndFlush(cellule);
        Localite localites;
        if (TestUtil.findAll(em, Localite.class).isEmpty()) {
            localites = LocaliteResourceIT.createEntity(em);
            em.persist(localites);
            em.flush();
        } else {
            localites = TestUtil.findAll(em, Localite.class).get(0);
        }
        em.persist(localites);
        em.flush();
        cellule.addLocalites(localites);
        celluleRepository.saveAndFlush(cellule);
        Long localitesId = localites.getId();

        // Get all the celluleList where localites equals to localitesId
        defaultCelluleShouldBeFound("localitesId.equals=" + localitesId);

        // Get all the celluleList where localites equals to (localitesId + 1)
        defaultCelluleShouldNotBeFound("localitesId.equals=" + (localitesId + 1));
    }

    @Test
    @Transactional
    void getAllCellulesBySouslocaliteIsEqualToSomething() throws Exception {
        // Initialize the database
        celluleRepository.saveAndFlush(cellule);
        SousLocalite souslocalite;
        if (TestUtil.findAll(em, SousLocalite.class).isEmpty()) {
            souslocalite = SousLocaliteResourceIT.createEntity(em);
            em.persist(souslocalite);
            em.flush();
        } else {
            souslocalite = TestUtil.findAll(em, SousLocalite.class).get(0);
        }
        em.persist(souslocalite);
        em.flush();
        cellule.addSouslocalite(souslocalite);
        celluleRepository.saveAndFlush(cellule);
        Long souslocaliteId = souslocalite.getId();

        // Get all the celluleList where souslocalite equals to souslocaliteId
        defaultCelluleShouldBeFound("souslocaliteId.equals=" + souslocaliteId);

        // Get all the celluleList where souslocalite equals to (souslocaliteId + 1)
        defaultCelluleShouldNotBeFound("souslocaliteId.equals=" + (souslocaliteId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCelluleShouldBeFound(String filter) throws Exception {
        restCelluleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cellule.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())));

        // Check, that the count call also returns 1
        restCelluleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCelluleShouldNotBeFound(String filter) throws Exception {
        restCelluleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCelluleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCellule() throws Exception {
        // Get the cellule
        restCelluleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCellule() throws Exception {
        // Initialize the database
        celluleRepository.saveAndFlush(cellule);

        int databaseSizeBeforeUpdate = celluleRepository.findAll().size();

        // Update the cellule
        Cellule updatedCellule = celluleRepository.findById(cellule.getId()).get();
        // Disconnect from session so that the updates on updatedCellule are not directly saved in db
        em.detach(updatedCellule);
        updatedCellule.nom(UPDATED_NOM);

        restCelluleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCellule.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCellule))
            )
            .andExpect(status().isOk());

        // Validate the Cellule in the database
        List<Cellule> celluleList = celluleRepository.findAll();
        assertThat(celluleList).hasSize(databaseSizeBeforeUpdate);
        Cellule testCellule = celluleList.get(celluleList.size() - 1);
        assertThat(testCellule.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    @Transactional
    void putNonExistingCellule() throws Exception {
        int databaseSizeBeforeUpdate = celluleRepository.findAll().size();
        cellule.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCelluleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cellule.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cellule))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cellule in the database
        List<Cellule> celluleList = celluleRepository.findAll();
        assertThat(celluleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCellule() throws Exception {
        int databaseSizeBeforeUpdate = celluleRepository.findAll().size();
        cellule.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCelluleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cellule))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cellule in the database
        List<Cellule> celluleList = celluleRepository.findAll();
        assertThat(celluleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCellule() throws Exception {
        int databaseSizeBeforeUpdate = celluleRepository.findAll().size();
        cellule.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCelluleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cellule)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cellule in the database
        List<Cellule> celluleList = celluleRepository.findAll();
        assertThat(celluleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCelluleWithPatch() throws Exception {
        // Initialize the database
        celluleRepository.saveAndFlush(cellule);

        int databaseSizeBeforeUpdate = celluleRepository.findAll().size();

        // Update the cellule using partial update
        Cellule partialUpdatedCellule = new Cellule();
        partialUpdatedCellule.setId(cellule.getId());

        restCelluleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCellule.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCellule))
            )
            .andExpect(status().isOk());

        // Validate the Cellule in the database
        List<Cellule> celluleList = celluleRepository.findAll();
        assertThat(celluleList).hasSize(databaseSizeBeforeUpdate);
        Cellule testCellule = celluleList.get(celluleList.size() - 1);
        assertThat(testCellule.getNom()).isEqualTo(DEFAULT_NOM);
    }

    @Test
    @Transactional
    void fullUpdateCelluleWithPatch() throws Exception {
        // Initialize the database
        celluleRepository.saveAndFlush(cellule);

        int databaseSizeBeforeUpdate = celluleRepository.findAll().size();

        // Update the cellule using partial update
        Cellule partialUpdatedCellule = new Cellule();
        partialUpdatedCellule.setId(cellule.getId());

        partialUpdatedCellule.nom(UPDATED_NOM);

        restCelluleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCellule.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCellule))
            )
            .andExpect(status().isOk());

        // Validate the Cellule in the database
        List<Cellule> celluleList = celluleRepository.findAll();
        assertThat(celluleList).hasSize(databaseSizeBeforeUpdate);
        Cellule testCellule = celluleList.get(celluleList.size() - 1);
        assertThat(testCellule.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    @Transactional
    void patchNonExistingCellule() throws Exception {
        int databaseSizeBeforeUpdate = celluleRepository.findAll().size();
        cellule.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCelluleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cellule.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cellule))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cellule in the database
        List<Cellule> celluleList = celluleRepository.findAll();
        assertThat(celluleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCellule() throws Exception {
        int databaseSizeBeforeUpdate = celluleRepository.findAll().size();
        cellule.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCelluleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cellule))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cellule in the database
        List<Cellule> celluleList = celluleRepository.findAll();
        assertThat(celluleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCellule() throws Exception {
        int databaseSizeBeforeUpdate = celluleRepository.findAll().size();
        cellule.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCelluleMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(cellule)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cellule in the database
        List<Cellule> celluleList = celluleRepository.findAll();
        assertThat(celluleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCellule() throws Exception {
        // Initialize the database
        celluleRepository.saveAndFlush(cellule);

        int databaseSizeBeforeDelete = celluleRepository.findAll().size();

        // Delete the cellule
        restCelluleMockMvc
            .perform(delete(ENTITY_API_URL_ID, cellule.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cellule> celluleList = celluleRepository.findAll();
        assertThat(celluleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
