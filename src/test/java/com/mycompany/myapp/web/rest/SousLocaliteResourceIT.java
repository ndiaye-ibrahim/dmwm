package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Cellule;
import com.mycompany.myapp.domain.Localite;
import com.mycompany.myapp.domain.Section;
import com.mycompany.myapp.domain.SousLocalite;
import com.mycompany.myapp.repository.SousLocaliteRepository;
import com.mycompany.myapp.service.criteria.SousLocaliteCriteria;
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
 * Integration tests for the {@link SousLocaliteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SousLocaliteResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/sous-localites";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SousLocaliteRepository sousLocaliteRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSousLocaliteMockMvc;

    private SousLocalite sousLocalite;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SousLocalite createEntity(EntityManager em) {
        SousLocalite sousLocalite = new SousLocalite().nom(DEFAULT_NOM);
        return sousLocalite;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SousLocalite createUpdatedEntity(EntityManager em) {
        SousLocalite sousLocalite = new SousLocalite().nom(UPDATED_NOM);
        return sousLocalite;
    }

    @BeforeEach
    public void initTest() {
        sousLocalite = createEntity(em);
    }

    @Test
    @Transactional
    void createSousLocalite() throws Exception {
        int databaseSizeBeforeCreate = sousLocaliteRepository.findAll().size();
        // Create the SousLocalite
        restSousLocaliteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sousLocalite)))
            .andExpect(status().isCreated());

        // Validate the SousLocalite in the database
        List<SousLocalite> sousLocaliteList = sousLocaliteRepository.findAll();
        assertThat(sousLocaliteList).hasSize(databaseSizeBeforeCreate + 1);
        SousLocalite testSousLocalite = sousLocaliteList.get(sousLocaliteList.size() - 1);
        assertThat(testSousLocalite.getNom()).isEqualTo(DEFAULT_NOM);
    }

    @Test
    @Transactional
    void createSousLocaliteWithExistingId() throws Exception {
        // Create the SousLocalite with an existing ID
        sousLocalite.setId(1L);

        int databaseSizeBeforeCreate = sousLocaliteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSousLocaliteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sousLocalite)))
            .andExpect(status().isBadRequest());

        // Validate the SousLocalite in the database
        List<SousLocalite> sousLocaliteList = sousLocaliteRepository.findAll();
        assertThat(sousLocaliteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSousLocalites() throws Exception {
        // Initialize the database
        sousLocaliteRepository.saveAndFlush(sousLocalite);

        // Get all the sousLocaliteList
        restSousLocaliteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sousLocalite.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)));
    }

    @Test
    @Transactional
    void getSousLocalite() throws Exception {
        // Initialize the database
        sousLocaliteRepository.saveAndFlush(sousLocalite);

        // Get the sousLocalite
        restSousLocaliteMockMvc
            .perform(get(ENTITY_API_URL_ID, sousLocalite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sousLocalite.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM));
    }

    @Test
    @Transactional
    void getSousLocalitesByIdFiltering() throws Exception {
        // Initialize the database
        sousLocaliteRepository.saveAndFlush(sousLocalite);

        Long id = sousLocalite.getId();

        defaultSousLocaliteShouldBeFound("id.equals=" + id);
        defaultSousLocaliteShouldNotBeFound("id.notEquals=" + id);

        defaultSousLocaliteShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSousLocaliteShouldNotBeFound("id.greaterThan=" + id);

        defaultSousLocaliteShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSousLocaliteShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSousLocalitesByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        sousLocaliteRepository.saveAndFlush(sousLocalite);

        // Get all the sousLocaliteList where nom equals to DEFAULT_NOM
        defaultSousLocaliteShouldBeFound("nom.equals=" + DEFAULT_NOM);

        // Get all the sousLocaliteList where nom equals to UPDATED_NOM
        defaultSousLocaliteShouldNotBeFound("nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllSousLocalitesByNomIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sousLocaliteRepository.saveAndFlush(sousLocalite);

        // Get all the sousLocaliteList where nom not equals to DEFAULT_NOM
        defaultSousLocaliteShouldNotBeFound("nom.notEquals=" + DEFAULT_NOM);

        // Get all the sousLocaliteList where nom not equals to UPDATED_NOM
        defaultSousLocaliteShouldBeFound("nom.notEquals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllSousLocalitesByNomIsInShouldWork() throws Exception {
        // Initialize the database
        sousLocaliteRepository.saveAndFlush(sousLocalite);

        // Get all the sousLocaliteList where nom in DEFAULT_NOM or UPDATED_NOM
        defaultSousLocaliteShouldBeFound("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM);

        // Get all the sousLocaliteList where nom equals to UPDATED_NOM
        defaultSousLocaliteShouldNotBeFound("nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllSousLocalitesByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        sousLocaliteRepository.saveAndFlush(sousLocalite);

        // Get all the sousLocaliteList where nom is not null
        defaultSousLocaliteShouldBeFound("nom.specified=true");

        // Get all the sousLocaliteList where nom is null
        defaultSousLocaliteShouldNotBeFound("nom.specified=false");
    }

    @Test
    @Transactional
    void getAllSousLocalitesByNomContainsSomething() throws Exception {
        // Initialize the database
        sousLocaliteRepository.saveAndFlush(sousLocalite);

        // Get all the sousLocaliteList where nom contains DEFAULT_NOM
        defaultSousLocaliteShouldBeFound("nom.contains=" + DEFAULT_NOM);

        // Get all the sousLocaliteList where nom contains UPDATED_NOM
        defaultSousLocaliteShouldNotBeFound("nom.contains=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllSousLocalitesByNomNotContainsSomething() throws Exception {
        // Initialize the database
        sousLocaliteRepository.saveAndFlush(sousLocalite);

        // Get all the sousLocaliteList where nom does not contain DEFAULT_NOM
        defaultSousLocaliteShouldNotBeFound("nom.doesNotContain=" + DEFAULT_NOM);

        // Get all the sousLocaliteList where nom does not contain UPDATED_NOM
        defaultSousLocaliteShouldBeFound("nom.doesNotContain=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllSousLocalitesByLocalitesIsEqualToSomething() throws Exception {
        // Initialize the database
        sousLocaliteRepository.saveAndFlush(sousLocalite);
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
        sousLocalite.addLocalites(localites);
        sousLocaliteRepository.saveAndFlush(sousLocalite);
        Long localitesId = localites.getId();

        // Get all the sousLocaliteList where localites equals to localitesId
        defaultSousLocaliteShouldBeFound("localitesId.equals=" + localitesId);

        // Get all the sousLocaliteList where localites equals to (localitesId + 1)
        defaultSousLocaliteShouldNotBeFound("localitesId.equals=" + (localitesId + 1));
    }

    @Test
    @Transactional
    void getAllSousLocalitesBySectionIsEqualToSomething() throws Exception {
        // Initialize the database
        sousLocaliteRepository.saveAndFlush(sousLocalite);
        Section section;
        if (TestUtil.findAll(em, Section.class).isEmpty()) {
            section = SectionResourceIT.createEntity(em);
            em.persist(section);
            em.flush();
        } else {
            section = TestUtil.findAll(em, Section.class).get(0);
        }
        em.persist(section);
        em.flush();
        sousLocalite.setSection(section);
        sousLocaliteRepository.saveAndFlush(sousLocalite);
        Long sectionId = section.getId();

        // Get all the sousLocaliteList where section equals to sectionId
        defaultSousLocaliteShouldBeFound("sectionId.equals=" + sectionId);

        // Get all the sousLocaliteList where section equals to (sectionId + 1)
        defaultSousLocaliteShouldNotBeFound("sectionId.equals=" + (sectionId + 1));
    }

    @Test
    @Transactional
    void getAllSousLocalitesByCelluleIsEqualToSomething() throws Exception {
        // Initialize the database
        sousLocaliteRepository.saveAndFlush(sousLocalite);
        Cellule cellule;
        if (TestUtil.findAll(em, Cellule.class).isEmpty()) {
            cellule = CelluleResourceIT.createEntity(em);
            em.persist(cellule);
            em.flush();
        } else {
            cellule = TestUtil.findAll(em, Cellule.class).get(0);
        }
        em.persist(cellule);
        em.flush();
        sousLocalite.setCellule(cellule);
        sousLocaliteRepository.saveAndFlush(sousLocalite);
        Long celluleId = cellule.getId();

        // Get all the sousLocaliteList where cellule equals to celluleId
        defaultSousLocaliteShouldBeFound("celluleId.equals=" + celluleId);

        // Get all the sousLocaliteList where cellule equals to (celluleId + 1)
        defaultSousLocaliteShouldNotBeFound("celluleId.equals=" + (celluleId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSousLocaliteShouldBeFound(String filter) throws Exception {
        restSousLocaliteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sousLocalite.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)));

        // Check, that the count call also returns 1
        restSousLocaliteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSousLocaliteShouldNotBeFound(String filter) throws Exception {
        restSousLocaliteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSousLocaliteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSousLocalite() throws Exception {
        // Get the sousLocalite
        restSousLocaliteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSousLocalite() throws Exception {
        // Initialize the database
        sousLocaliteRepository.saveAndFlush(sousLocalite);

        int databaseSizeBeforeUpdate = sousLocaliteRepository.findAll().size();

        // Update the sousLocalite
        SousLocalite updatedSousLocalite = sousLocaliteRepository.findById(sousLocalite.getId()).get();
        // Disconnect from session so that the updates on updatedSousLocalite are not directly saved in db
        em.detach(updatedSousLocalite);
        updatedSousLocalite.nom(UPDATED_NOM);

        restSousLocaliteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSousLocalite.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSousLocalite))
            )
            .andExpect(status().isOk());

        // Validate the SousLocalite in the database
        List<SousLocalite> sousLocaliteList = sousLocaliteRepository.findAll();
        assertThat(sousLocaliteList).hasSize(databaseSizeBeforeUpdate);
        SousLocalite testSousLocalite = sousLocaliteList.get(sousLocaliteList.size() - 1);
        assertThat(testSousLocalite.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    @Transactional
    void putNonExistingSousLocalite() throws Exception {
        int databaseSizeBeforeUpdate = sousLocaliteRepository.findAll().size();
        sousLocalite.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSousLocaliteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sousLocalite.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sousLocalite))
            )
            .andExpect(status().isBadRequest());

        // Validate the SousLocalite in the database
        List<SousLocalite> sousLocaliteList = sousLocaliteRepository.findAll();
        assertThat(sousLocaliteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSousLocalite() throws Exception {
        int databaseSizeBeforeUpdate = sousLocaliteRepository.findAll().size();
        sousLocalite.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSousLocaliteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sousLocalite))
            )
            .andExpect(status().isBadRequest());

        // Validate the SousLocalite in the database
        List<SousLocalite> sousLocaliteList = sousLocaliteRepository.findAll();
        assertThat(sousLocaliteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSousLocalite() throws Exception {
        int databaseSizeBeforeUpdate = sousLocaliteRepository.findAll().size();
        sousLocalite.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSousLocaliteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sousLocalite)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SousLocalite in the database
        List<SousLocalite> sousLocaliteList = sousLocaliteRepository.findAll();
        assertThat(sousLocaliteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSousLocaliteWithPatch() throws Exception {
        // Initialize the database
        sousLocaliteRepository.saveAndFlush(sousLocalite);

        int databaseSizeBeforeUpdate = sousLocaliteRepository.findAll().size();

        // Update the sousLocalite using partial update
        SousLocalite partialUpdatedSousLocalite = new SousLocalite();
        partialUpdatedSousLocalite.setId(sousLocalite.getId());

        restSousLocaliteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSousLocalite.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSousLocalite))
            )
            .andExpect(status().isOk());

        // Validate the SousLocalite in the database
        List<SousLocalite> sousLocaliteList = sousLocaliteRepository.findAll();
        assertThat(sousLocaliteList).hasSize(databaseSizeBeforeUpdate);
        SousLocalite testSousLocalite = sousLocaliteList.get(sousLocaliteList.size() - 1);
        assertThat(testSousLocalite.getNom()).isEqualTo(DEFAULT_NOM);
    }

    @Test
    @Transactional
    void fullUpdateSousLocaliteWithPatch() throws Exception {
        // Initialize the database
        sousLocaliteRepository.saveAndFlush(sousLocalite);

        int databaseSizeBeforeUpdate = sousLocaliteRepository.findAll().size();

        // Update the sousLocalite using partial update
        SousLocalite partialUpdatedSousLocalite = new SousLocalite();
        partialUpdatedSousLocalite.setId(sousLocalite.getId());

        partialUpdatedSousLocalite.nom(UPDATED_NOM);

        restSousLocaliteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSousLocalite.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSousLocalite))
            )
            .andExpect(status().isOk());

        // Validate the SousLocalite in the database
        List<SousLocalite> sousLocaliteList = sousLocaliteRepository.findAll();
        assertThat(sousLocaliteList).hasSize(databaseSizeBeforeUpdate);
        SousLocalite testSousLocalite = sousLocaliteList.get(sousLocaliteList.size() - 1);
        assertThat(testSousLocalite.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    @Transactional
    void patchNonExistingSousLocalite() throws Exception {
        int databaseSizeBeforeUpdate = sousLocaliteRepository.findAll().size();
        sousLocalite.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSousLocaliteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sousLocalite.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sousLocalite))
            )
            .andExpect(status().isBadRequest());

        // Validate the SousLocalite in the database
        List<SousLocalite> sousLocaliteList = sousLocaliteRepository.findAll();
        assertThat(sousLocaliteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSousLocalite() throws Exception {
        int databaseSizeBeforeUpdate = sousLocaliteRepository.findAll().size();
        sousLocalite.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSousLocaliteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sousLocalite))
            )
            .andExpect(status().isBadRequest());

        // Validate the SousLocalite in the database
        List<SousLocalite> sousLocaliteList = sousLocaliteRepository.findAll();
        assertThat(sousLocaliteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSousLocalite() throws Exception {
        int databaseSizeBeforeUpdate = sousLocaliteRepository.findAll().size();
        sousLocalite.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSousLocaliteMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(sousLocalite))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SousLocalite in the database
        List<SousLocalite> sousLocaliteList = sousLocaliteRepository.findAll();
        assertThat(sousLocaliteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSousLocalite() throws Exception {
        // Initialize the database
        sousLocaliteRepository.saveAndFlush(sousLocalite);

        int databaseSizeBeforeDelete = sousLocaliteRepository.findAll().size();

        // Delete the sousLocalite
        restSousLocaliteMockMvc
            .perform(delete(ENTITY_API_URL_ID, sousLocalite.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SousLocalite> sousLocaliteList = sousLocaliteRepository.findAll();
        assertThat(sousLocaliteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
