package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Cellule;
import com.mycompany.myapp.domain.Localite;
import com.mycompany.myapp.domain.SousLocalite;
import com.mycompany.myapp.repository.LocaliteRepository;
import com.mycompany.myapp.service.criteria.LocaliteCriteria;
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
 * Integration tests for the {@link LocaliteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LocaliteResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/localites";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LocaliteRepository localiteRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLocaliteMockMvc;

    private Localite localite;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Localite createEntity(EntityManager em) {
        Localite localite = new Localite().nom(DEFAULT_NOM);
        return localite;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Localite createUpdatedEntity(EntityManager em) {
        Localite localite = new Localite().nom(UPDATED_NOM);
        return localite;
    }

    @BeforeEach
    public void initTest() {
        localite = createEntity(em);
    }

    @Test
    @Transactional
    void createLocalite() throws Exception {
        int databaseSizeBeforeCreate = localiteRepository.findAll().size();
        // Create the Localite
        restLocaliteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(localite)))
            .andExpect(status().isCreated());

        // Validate the Localite in the database
        List<Localite> localiteList = localiteRepository.findAll();
        assertThat(localiteList).hasSize(databaseSizeBeforeCreate + 1);
        Localite testLocalite = localiteList.get(localiteList.size() - 1);
        assertThat(testLocalite.getNom()).isEqualTo(DEFAULT_NOM);
    }

    @Test
    @Transactional
    void createLocaliteWithExistingId() throws Exception {
        // Create the Localite with an existing ID
        localite.setId(1L);

        int databaseSizeBeforeCreate = localiteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLocaliteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(localite)))
            .andExpect(status().isBadRequest());

        // Validate the Localite in the database
        List<Localite> localiteList = localiteRepository.findAll();
        assertThat(localiteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLocalites() throws Exception {
        // Initialize the database
        localiteRepository.saveAndFlush(localite);

        // Get all the localiteList
        restLocaliteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(localite.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)));
    }

    @Test
    @Transactional
    void getLocalite() throws Exception {
        // Initialize the database
        localiteRepository.saveAndFlush(localite);

        // Get the localite
        restLocaliteMockMvc
            .perform(get(ENTITY_API_URL_ID, localite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(localite.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM));
    }

    @Test
    @Transactional
    void getLocalitesByIdFiltering() throws Exception {
        // Initialize the database
        localiteRepository.saveAndFlush(localite);

        Long id = localite.getId();

        defaultLocaliteShouldBeFound("id.equals=" + id);
        defaultLocaliteShouldNotBeFound("id.notEquals=" + id);

        defaultLocaliteShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLocaliteShouldNotBeFound("id.greaterThan=" + id);

        defaultLocaliteShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLocaliteShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLocalitesByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        localiteRepository.saveAndFlush(localite);

        // Get all the localiteList where nom equals to DEFAULT_NOM
        defaultLocaliteShouldBeFound("nom.equals=" + DEFAULT_NOM);

        // Get all the localiteList where nom equals to UPDATED_NOM
        defaultLocaliteShouldNotBeFound("nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllLocalitesByNomIsNotEqualToSomething() throws Exception {
        // Initialize the database
        localiteRepository.saveAndFlush(localite);

        // Get all the localiteList where nom not equals to DEFAULT_NOM
        defaultLocaliteShouldNotBeFound("nom.notEquals=" + DEFAULT_NOM);

        // Get all the localiteList where nom not equals to UPDATED_NOM
        defaultLocaliteShouldBeFound("nom.notEquals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllLocalitesByNomIsInShouldWork() throws Exception {
        // Initialize the database
        localiteRepository.saveAndFlush(localite);

        // Get all the localiteList where nom in DEFAULT_NOM or UPDATED_NOM
        defaultLocaliteShouldBeFound("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM);

        // Get all the localiteList where nom equals to UPDATED_NOM
        defaultLocaliteShouldNotBeFound("nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllLocalitesByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        localiteRepository.saveAndFlush(localite);

        // Get all the localiteList where nom is not null
        defaultLocaliteShouldBeFound("nom.specified=true");

        // Get all the localiteList where nom is null
        defaultLocaliteShouldNotBeFound("nom.specified=false");
    }

    @Test
    @Transactional
    void getAllLocalitesByNomContainsSomething() throws Exception {
        // Initialize the database
        localiteRepository.saveAndFlush(localite);

        // Get all the localiteList where nom contains DEFAULT_NOM
        defaultLocaliteShouldBeFound("nom.contains=" + DEFAULT_NOM);

        // Get all the localiteList where nom contains UPDATED_NOM
        defaultLocaliteShouldNotBeFound("nom.contains=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllLocalitesByNomNotContainsSomething() throws Exception {
        // Initialize the database
        localiteRepository.saveAndFlush(localite);

        // Get all the localiteList where nom does not contain DEFAULT_NOM
        defaultLocaliteShouldNotBeFound("nom.doesNotContain=" + DEFAULT_NOM);

        // Get all the localiteList where nom does not contain UPDATED_NOM
        defaultLocaliteShouldBeFound("nom.doesNotContain=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllLocalitesBySouslocaliteIsEqualToSomething() throws Exception {
        // Initialize the database
        localiteRepository.saveAndFlush(localite);
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
        localite.setSouslocalite(souslocalite);
        localiteRepository.saveAndFlush(localite);
        Long souslocaliteId = souslocalite.getId();

        // Get all the localiteList where souslocalite equals to souslocaliteId
        defaultLocaliteShouldBeFound("souslocaliteId.equals=" + souslocaliteId);

        // Get all the localiteList where souslocalite equals to (souslocaliteId + 1)
        defaultLocaliteShouldNotBeFound("souslocaliteId.equals=" + (souslocaliteId + 1));
    }

    @Test
    @Transactional
    void getAllLocalitesByCellulesIsEqualToSomething() throws Exception {
        // Initialize the database
        localiteRepository.saveAndFlush(localite);
        Cellule cellules;
        if (TestUtil.findAll(em, Cellule.class).isEmpty()) {
            cellules = CelluleResourceIT.createEntity(em);
            em.persist(cellules);
            em.flush();
        } else {
            cellules = TestUtil.findAll(em, Cellule.class).get(0);
        }
        em.persist(cellules);
        em.flush();
        localite.setCellules(cellules);
        localiteRepository.saveAndFlush(localite);
        Long cellulesId = cellules.getId();

        // Get all the localiteList where cellules equals to cellulesId
        defaultLocaliteShouldBeFound("cellulesId.equals=" + cellulesId);

        // Get all the localiteList where cellules equals to (cellulesId + 1)
        defaultLocaliteShouldNotBeFound("cellulesId.equals=" + (cellulesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLocaliteShouldBeFound(String filter) throws Exception {
        restLocaliteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(localite.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)));

        // Check, that the count call also returns 1
        restLocaliteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLocaliteShouldNotBeFound(String filter) throws Exception {
        restLocaliteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLocaliteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLocalite() throws Exception {
        // Get the localite
        restLocaliteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLocalite() throws Exception {
        // Initialize the database
        localiteRepository.saveAndFlush(localite);

        int databaseSizeBeforeUpdate = localiteRepository.findAll().size();

        // Update the localite
        Localite updatedLocalite = localiteRepository.findById(localite.getId()).get();
        // Disconnect from session so that the updates on updatedLocalite are not directly saved in db
        em.detach(updatedLocalite);
        updatedLocalite.nom(UPDATED_NOM);

        restLocaliteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLocalite.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLocalite))
            )
            .andExpect(status().isOk());

        // Validate the Localite in the database
        List<Localite> localiteList = localiteRepository.findAll();
        assertThat(localiteList).hasSize(databaseSizeBeforeUpdate);
        Localite testLocalite = localiteList.get(localiteList.size() - 1);
        assertThat(testLocalite.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    @Transactional
    void putNonExistingLocalite() throws Exception {
        int databaseSizeBeforeUpdate = localiteRepository.findAll().size();
        localite.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocaliteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, localite.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(localite))
            )
            .andExpect(status().isBadRequest());

        // Validate the Localite in the database
        List<Localite> localiteList = localiteRepository.findAll();
        assertThat(localiteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLocalite() throws Exception {
        int databaseSizeBeforeUpdate = localiteRepository.findAll().size();
        localite.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocaliteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(localite))
            )
            .andExpect(status().isBadRequest());

        // Validate the Localite in the database
        List<Localite> localiteList = localiteRepository.findAll();
        assertThat(localiteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLocalite() throws Exception {
        int databaseSizeBeforeUpdate = localiteRepository.findAll().size();
        localite.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocaliteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(localite)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Localite in the database
        List<Localite> localiteList = localiteRepository.findAll();
        assertThat(localiteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLocaliteWithPatch() throws Exception {
        // Initialize the database
        localiteRepository.saveAndFlush(localite);

        int databaseSizeBeforeUpdate = localiteRepository.findAll().size();

        // Update the localite using partial update
        Localite partialUpdatedLocalite = new Localite();
        partialUpdatedLocalite.setId(localite.getId());

        partialUpdatedLocalite.nom(UPDATED_NOM);

        restLocaliteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocalite.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLocalite))
            )
            .andExpect(status().isOk());

        // Validate the Localite in the database
        List<Localite> localiteList = localiteRepository.findAll();
        assertThat(localiteList).hasSize(databaseSizeBeforeUpdate);
        Localite testLocalite = localiteList.get(localiteList.size() - 1);
        assertThat(testLocalite.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    @Transactional
    void fullUpdateLocaliteWithPatch() throws Exception {
        // Initialize the database
        localiteRepository.saveAndFlush(localite);

        int databaseSizeBeforeUpdate = localiteRepository.findAll().size();

        // Update the localite using partial update
        Localite partialUpdatedLocalite = new Localite();
        partialUpdatedLocalite.setId(localite.getId());

        partialUpdatedLocalite.nom(UPDATED_NOM);

        restLocaliteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocalite.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLocalite))
            )
            .andExpect(status().isOk());

        // Validate the Localite in the database
        List<Localite> localiteList = localiteRepository.findAll();
        assertThat(localiteList).hasSize(databaseSizeBeforeUpdate);
        Localite testLocalite = localiteList.get(localiteList.size() - 1);
        assertThat(testLocalite.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    @Transactional
    void patchNonExistingLocalite() throws Exception {
        int databaseSizeBeforeUpdate = localiteRepository.findAll().size();
        localite.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocaliteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, localite.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(localite))
            )
            .andExpect(status().isBadRequest());

        // Validate the Localite in the database
        List<Localite> localiteList = localiteRepository.findAll();
        assertThat(localiteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLocalite() throws Exception {
        int databaseSizeBeforeUpdate = localiteRepository.findAll().size();
        localite.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocaliteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(localite))
            )
            .andExpect(status().isBadRequest());

        // Validate the Localite in the database
        List<Localite> localiteList = localiteRepository.findAll();
        assertThat(localiteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLocalite() throws Exception {
        int databaseSizeBeforeUpdate = localiteRepository.findAll().size();
        localite.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocaliteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(localite)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Localite in the database
        List<Localite> localiteList = localiteRepository.findAll();
        assertThat(localiteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLocalite() throws Exception {
        // Initialize the database
        localiteRepository.saveAndFlush(localite);

        int databaseSizeBeforeDelete = localiteRepository.findAll().size();

        // Delete the localite
        restLocaliteMockMvc
            .perform(delete(ENTITY_API_URL_ID, localite.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Localite> localiteList = localiteRepository.findAll();
        assertThat(localiteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
