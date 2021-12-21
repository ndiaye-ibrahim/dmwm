package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Membre;
import com.mycompany.myapp.domain.Section;
import com.mycompany.myapp.domain.SousLocalite;
import com.mycompany.myapp.repository.SectionRepository;
import com.mycompany.myapp.service.criteria.SectionCriteria;
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
 * Integration tests for the {@link SectionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SectionResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/sections";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSectionMockMvc;

    private Section section;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Section createEntity(EntityManager em) {
        Section section = new Section().nom(DEFAULT_NOM);
        return section;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Section createUpdatedEntity(EntityManager em) {
        Section section = new Section().nom(UPDATED_NOM);
        return section;
    }

    @BeforeEach
    public void initTest() {
        section = createEntity(em);
    }

    @Test
    @Transactional
    void createSection() throws Exception {
        int databaseSizeBeforeCreate = sectionRepository.findAll().size();
        // Create the Section
        restSectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(section)))
            .andExpect(status().isCreated());

        // Validate the Section in the database
        List<Section> sectionList = sectionRepository.findAll();
        assertThat(sectionList).hasSize(databaseSizeBeforeCreate + 1);
        Section testSection = sectionList.get(sectionList.size() - 1);
        assertThat(testSection.getNom()).isEqualTo(DEFAULT_NOM);
    }

    @Test
    @Transactional
    void createSectionWithExistingId() throws Exception {
        // Create the Section with an existing ID
        section.setId(1L);

        int databaseSizeBeforeCreate = sectionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(section)))
            .andExpect(status().isBadRequest());

        // Validate the Section in the database
        List<Section> sectionList = sectionRepository.findAll();
        assertThat(sectionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSections() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        // Get all the sectionList
        restSectionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(section.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)));
    }

    @Test
    @Transactional
    void getSection() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        // Get the section
        restSectionMockMvc
            .perform(get(ENTITY_API_URL_ID, section.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(section.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM));
    }

    @Test
    @Transactional
    void getSectionsByIdFiltering() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        Long id = section.getId();

        defaultSectionShouldBeFound("id.equals=" + id);
        defaultSectionShouldNotBeFound("id.notEquals=" + id);

        defaultSectionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSectionShouldNotBeFound("id.greaterThan=" + id);

        defaultSectionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSectionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSectionsByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        // Get all the sectionList where nom equals to DEFAULT_NOM
        defaultSectionShouldBeFound("nom.equals=" + DEFAULT_NOM);

        // Get all the sectionList where nom equals to UPDATED_NOM
        defaultSectionShouldNotBeFound("nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllSectionsByNomIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        // Get all the sectionList where nom not equals to DEFAULT_NOM
        defaultSectionShouldNotBeFound("nom.notEquals=" + DEFAULT_NOM);

        // Get all the sectionList where nom not equals to UPDATED_NOM
        defaultSectionShouldBeFound("nom.notEquals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllSectionsByNomIsInShouldWork() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        // Get all the sectionList where nom in DEFAULT_NOM or UPDATED_NOM
        defaultSectionShouldBeFound("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM);

        // Get all the sectionList where nom equals to UPDATED_NOM
        defaultSectionShouldNotBeFound("nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllSectionsByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        // Get all the sectionList where nom is not null
        defaultSectionShouldBeFound("nom.specified=true");

        // Get all the sectionList where nom is null
        defaultSectionShouldNotBeFound("nom.specified=false");
    }

    @Test
    @Transactional
    void getAllSectionsByNomContainsSomething() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        // Get all the sectionList where nom contains DEFAULT_NOM
        defaultSectionShouldBeFound("nom.contains=" + DEFAULT_NOM);

        // Get all the sectionList where nom contains UPDATED_NOM
        defaultSectionShouldNotBeFound("nom.contains=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllSectionsByNomNotContainsSomething() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        // Get all the sectionList where nom does not contain DEFAULT_NOM
        defaultSectionShouldNotBeFound("nom.doesNotContain=" + DEFAULT_NOM);

        // Get all the sectionList where nom does not contain UPDATED_NOM
        defaultSectionShouldBeFound("nom.doesNotContain=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllSectionsBySouslocaliteIsEqualToSomething() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);
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
        section.addSouslocalite(souslocalite);
        sectionRepository.saveAndFlush(section);
        Long souslocaliteId = souslocalite.getId();

        // Get all the sectionList where souslocalite equals to souslocaliteId
        defaultSectionShouldBeFound("souslocaliteId.equals=" + souslocaliteId);

        // Get all the sectionList where souslocalite equals to (souslocaliteId + 1)
        defaultSectionShouldNotBeFound("souslocaliteId.equals=" + (souslocaliteId + 1));
    }

    @Test
    @Transactional
    void getAllSectionsByMembreIsEqualToSomething() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);
        Membre membre;
        if (TestUtil.findAll(em, Membre.class).isEmpty()) {
            membre = MembreResourceIT.createEntity(em);
            em.persist(membre);
            em.flush();
        } else {
            membre = TestUtil.findAll(em, Membre.class).get(0);
        }
        em.persist(membre);
        em.flush();
        section.setMembre(membre);
        sectionRepository.saveAndFlush(section);
        Long membreId = membre.getId();

        // Get all the sectionList where membre equals to membreId
        defaultSectionShouldBeFound("membreId.equals=" + membreId);

        // Get all the sectionList where membre equals to (membreId + 1)
        defaultSectionShouldNotBeFound("membreId.equals=" + (membreId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSectionShouldBeFound(String filter) throws Exception {
        restSectionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(section.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)));

        // Check, that the count call also returns 1
        restSectionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSectionShouldNotBeFound(String filter) throws Exception {
        restSectionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSectionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSection() throws Exception {
        // Get the section
        restSectionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSection() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        int databaseSizeBeforeUpdate = sectionRepository.findAll().size();

        // Update the section
        Section updatedSection = sectionRepository.findById(section.getId()).get();
        // Disconnect from session so that the updates on updatedSection are not directly saved in db
        em.detach(updatedSection);
        updatedSection.nom(UPDATED_NOM);

        restSectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSection.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSection))
            )
            .andExpect(status().isOk());

        // Validate the Section in the database
        List<Section> sectionList = sectionRepository.findAll();
        assertThat(sectionList).hasSize(databaseSizeBeforeUpdate);
        Section testSection = sectionList.get(sectionList.size() - 1);
        assertThat(testSection.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    @Transactional
    void putNonExistingSection() throws Exception {
        int databaseSizeBeforeUpdate = sectionRepository.findAll().size();
        section.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, section.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(section))
            )
            .andExpect(status().isBadRequest());

        // Validate the Section in the database
        List<Section> sectionList = sectionRepository.findAll();
        assertThat(sectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSection() throws Exception {
        int databaseSizeBeforeUpdate = sectionRepository.findAll().size();
        section.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(section))
            )
            .andExpect(status().isBadRequest());

        // Validate the Section in the database
        List<Section> sectionList = sectionRepository.findAll();
        assertThat(sectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSection() throws Exception {
        int databaseSizeBeforeUpdate = sectionRepository.findAll().size();
        section.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSectionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(section)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Section in the database
        List<Section> sectionList = sectionRepository.findAll();
        assertThat(sectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSectionWithPatch() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        int databaseSizeBeforeUpdate = sectionRepository.findAll().size();

        // Update the section using partial update
        Section partialUpdatedSection = new Section();
        partialUpdatedSection.setId(section.getId());

        restSectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSection.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSection))
            )
            .andExpect(status().isOk());

        // Validate the Section in the database
        List<Section> sectionList = sectionRepository.findAll();
        assertThat(sectionList).hasSize(databaseSizeBeforeUpdate);
        Section testSection = sectionList.get(sectionList.size() - 1);
        assertThat(testSection.getNom()).isEqualTo(DEFAULT_NOM);
    }

    @Test
    @Transactional
    void fullUpdateSectionWithPatch() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        int databaseSizeBeforeUpdate = sectionRepository.findAll().size();

        // Update the section using partial update
        Section partialUpdatedSection = new Section();
        partialUpdatedSection.setId(section.getId());

        partialUpdatedSection.nom(UPDATED_NOM);

        restSectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSection.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSection))
            )
            .andExpect(status().isOk());

        // Validate the Section in the database
        List<Section> sectionList = sectionRepository.findAll();
        assertThat(sectionList).hasSize(databaseSizeBeforeUpdate);
        Section testSection = sectionList.get(sectionList.size() - 1);
        assertThat(testSection.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    @Transactional
    void patchNonExistingSection() throws Exception {
        int databaseSizeBeforeUpdate = sectionRepository.findAll().size();
        section.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, section.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(section))
            )
            .andExpect(status().isBadRequest());

        // Validate the Section in the database
        List<Section> sectionList = sectionRepository.findAll();
        assertThat(sectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSection() throws Exception {
        int databaseSizeBeforeUpdate = sectionRepository.findAll().size();
        section.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(section))
            )
            .andExpect(status().isBadRequest());

        // Validate the Section in the database
        List<Section> sectionList = sectionRepository.findAll();
        assertThat(sectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSection() throws Exception {
        int databaseSizeBeforeUpdate = sectionRepository.findAll().size();
        section.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSectionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(section)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Section in the database
        List<Section> sectionList = sectionRepository.findAll();
        assertThat(sectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSection() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        int databaseSizeBeforeDelete = sectionRepository.findAll().size();

        // Delete the section
        restSectionMockMvc
            .perform(delete(ENTITY_API_URL_ID, section.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Section> sectionList = sectionRepository.findAll();
        assertThat(sectionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
