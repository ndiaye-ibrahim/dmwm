package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Membre;
import com.mycompany.myapp.domain.Section;
import com.mycompany.myapp.domain.enumeration.Rolemembre;
import com.mycompany.myapp.repository.MembreRepository;
import com.mycompany.myapp.service.criteria.MembreCriteria;
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
 * Integration tests for the {@link MembreResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MembreResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    private static final Rolemembre DEFAULT_FONCTION_INTERNE = Rolemembre.MembreSimple;
    private static final Rolemembre UPDATED_FONCTION_INTERNE = Rolemembre.President;

    private static final String DEFAULT_FONCTION_EXTERNE = "AAAAAAAAAA";
    private static final String UPDATED_FONCTION_EXTERNE = "BBBBBBBBBB";

    private static final String DEFAULT_STRUCTURE = "AAAAAAAAAA";
    private static final String UPDATED_STRUCTURE = "BBBBBBBBBB";

    private static final String DEFAULT_SITUATION_MATRIMONIALE = "AAAAAAAAAA";
    private static final String UPDATED_SITUATION_MATRIMONIALE = "BBBBBBBBBB";

    private static final String DEFAULT_NUMERO_TELEPHONE = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_TELEPHONE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/membres";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MembreRepository membreRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMembreMockMvc;

    private Membre membre;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Membre createEntity(EntityManager em) {
        Membre membre = new Membre()
            .nom(DEFAULT_NOM)
            .prenom(DEFAULT_PRENOM)
            .fonctionInterne(DEFAULT_FONCTION_INTERNE)
            .fonctionExterne(DEFAULT_FONCTION_EXTERNE)
            .structure(DEFAULT_STRUCTURE)
            .situationMatrimoniale(DEFAULT_SITUATION_MATRIMONIALE)
            .numeroTelephone(DEFAULT_NUMERO_TELEPHONE)
            .email(DEFAULT_EMAIL);
        return membre;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Membre createUpdatedEntity(EntityManager em) {
        Membre membre = new Membre()
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .fonctionInterne(UPDATED_FONCTION_INTERNE)
            .fonctionExterne(UPDATED_FONCTION_EXTERNE)
            .structure(UPDATED_STRUCTURE)
            .situationMatrimoniale(UPDATED_SITUATION_MATRIMONIALE)
            .numeroTelephone(UPDATED_NUMERO_TELEPHONE)
            .email(UPDATED_EMAIL);
        return membre;
    }

    @BeforeEach
    public void initTest() {
        membre = createEntity(em);
    }

    @Test
    @Transactional
    void createMembre() throws Exception {
        int databaseSizeBeforeCreate = membreRepository.findAll().size();
        // Create the Membre
        restMembreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(membre)))
            .andExpect(status().isCreated());

        // Validate the Membre in the database
        List<Membre> membreList = membreRepository.findAll();
        assertThat(membreList).hasSize(databaseSizeBeforeCreate + 1);
        Membre testMembre = membreList.get(membreList.size() - 1);
        assertThat(testMembre.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testMembre.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testMembre.getFonctionInterne()).isEqualTo(DEFAULT_FONCTION_INTERNE);
        assertThat(testMembre.getFonctionExterne()).isEqualTo(DEFAULT_FONCTION_EXTERNE);
        assertThat(testMembre.getStructure()).isEqualTo(DEFAULT_STRUCTURE);
        assertThat(testMembre.getSituationMatrimoniale()).isEqualTo(DEFAULT_SITUATION_MATRIMONIALE);
        assertThat(testMembre.getNumeroTelephone()).isEqualTo(DEFAULT_NUMERO_TELEPHONE);
        assertThat(testMembre.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void createMembreWithExistingId() throws Exception {
        // Create the Membre with an existing ID
        membre.setId(1L);

        int databaseSizeBeforeCreate = membreRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMembreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(membre)))
            .andExpect(status().isBadRequest());

        // Validate the Membre in the database
        List<Membre> membreList = membreRepository.findAll();
        assertThat(membreList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMembres() throws Exception {
        // Initialize the database
        membreRepository.saveAndFlush(membre);

        // Get all the membreList
        restMembreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(membre.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].fonctionInterne").value(hasItem(DEFAULT_FONCTION_INTERNE.toString())))
            .andExpect(jsonPath("$.[*].fonctionExterne").value(hasItem(DEFAULT_FONCTION_EXTERNE)))
            .andExpect(jsonPath("$.[*].structure").value(hasItem(DEFAULT_STRUCTURE)))
            .andExpect(jsonPath("$.[*].situationMatrimoniale").value(hasItem(DEFAULT_SITUATION_MATRIMONIALE)))
            .andExpect(jsonPath("$.[*].numeroTelephone").value(hasItem(DEFAULT_NUMERO_TELEPHONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));
    }

    @Test
    @Transactional
    void getMembre() throws Exception {
        // Initialize the database
        membreRepository.saveAndFlush(membre);

        // Get the membre
        restMembreMockMvc
            .perform(get(ENTITY_API_URL_ID, membre.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(membre.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM))
            .andExpect(jsonPath("$.fonctionInterne").value(DEFAULT_FONCTION_INTERNE.toString()))
            .andExpect(jsonPath("$.fonctionExterne").value(DEFAULT_FONCTION_EXTERNE))
            .andExpect(jsonPath("$.structure").value(DEFAULT_STRUCTURE))
            .andExpect(jsonPath("$.situationMatrimoniale").value(DEFAULT_SITUATION_MATRIMONIALE))
            .andExpect(jsonPath("$.numeroTelephone").value(DEFAULT_NUMERO_TELEPHONE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL));
    }

    @Test
    @Transactional
    void getMembresByIdFiltering() throws Exception {
        // Initialize the database
        membreRepository.saveAndFlush(membre);

        Long id = membre.getId();

        defaultMembreShouldBeFound("id.equals=" + id);
        defaultMembreShouldNotBeFound("id.notEquals=" + id);

        defaultMembreShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMembreShouldNotBeFound("id.greaterThan=" + id);

        defaultMembreShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMembreShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMembresByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        membreRepository.saveAndFlush(membre);

        // Get all the membreList where nom equals to DEFAULT_NOM
        defaultMembreShouldBeFound("nom.equals=" + DEFAULT_NOM);

        // Get all the membreList where nom equals to UPDATED_NOM
        defaultMembreShouldNotBeFound("nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllMembresByNomIsNotEqualToSomething() throws Exception {
        // Initialize the database
        membreRepository.saveAndFlush(membre);

        // Get all the membreList where nom not equals to DEFAULT_NOM
        defaultMembreShouldNotBeFound("nom.notEquals=" + DEFAULT_NOM);

        // Get all the membreList where nom not equals to UPDATED_NOM
        defaultMembreShouldBeFound("nom.notEquals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllMembresByNomIsInShouldWork() throws Exception {
        // Initialize the database
        membreRepository.saveAndFlush(membre);

        // Get all the membreList where nom in DEFAULT_NOM or UPDATED_NOM
        defaultMembreShouldBeFound("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM);

        // Get all the membreList where nom equals to UPDATED_NOM
        defaultMembreShouldNotBeFound("nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllMembresByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        membreRepository.saveAndFlush(membre);

        // Get all the membreList where nom is not null
        defaultMembreShouldBeFound("nom.specified=true");

        // Get all the membreList where nom is null
        defaultMembreShouldNotBeFound("nom.specified=false");
    }

    @Test
    @Transactional
    void getAllMembresByNomContainsSomething() throws Exception {
        // Initialize the database
        membreRepository.saveAndFlush(membre);

        // Get all the membreList where nom contains DEFAULT_NOM
        defaultMembreShouldBeFound("nom.contains=" + DEFAULT_NOM);

        // Get all the membreList where nom contains UPDATED_NOM
        defaultMembreShouldNotBeFound("nom.contains=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllMembresByNomNotContainsSomething() throws Exception {
        // Initialize the database
        membreRepository.saveAndFlush(membre);

        // Get all the membreList where nom does not contain DEFAULT_NOM
        defaultMembreShouldNotBeFound("nom.doesNotContain=" + DEFAULT_NOM);

        // Get all the membreList where nom does not contain UPDATED_NOM
        defaultMembreShouldBeFound("nom.doesNotContain=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllMembresByPrenomIsEqualToSomething() throws Exception {
        // Initialize the database
        membreRepository.saveAndFlush(membre);

        // Get all the membreList where prenom equals to DEFAULT_PRENOM
        defaultMembreShouldBeFound("prenom.equals=" + DEFAULT_PRENOM);

        // Get all the membreList where prenom equals to UPDATED_PRENOM
        defaultMembreShouldNotBeFound("prenom.equals=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllMembresByPrenomIsNotEqualToSomething() throws Exception {
        // Initialize the database
        membreRepository.saveAndFlush(membre);

        // Get all the membreList where prenom not equals to DEFAULT_PRENOM
        defaultMembreShouldNotBeFound("prenom.notEquals=" + DEFAULT_PRENOM);

        // Get all the membreList where prenom not equals to UPDATED_PRENOM
        defaultMembreShouldBeFound("prenom.notEquals=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllMembresByPrenomIsInShouldWork() throws Exception {
        // Initialize the database
        membreRepository.saveAndFlush(membre);

        // Get all the membreList where prenom in DEFAULT_PRENOM or UPDATED_PRENOM
        defaultMembreShouldBeFound("prenom.in=" + DEFAULT_PRENOM + "," + UPDATED_PRENOM);

        // Get all the membreList where prenom equals to UPDATED_PRENOM
        defaultMembreShouldNotBeFound("prenom.in=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllMembresByPrenomIsNullOrNotNull() throws Exception {
        // Initialize the database
        membreRepository.saveAndFlush(membre);

        // Get all the membreList where prenom is not null
        defaultMembreShouldBeFound("prenom.specified=true");

        // Get all the membreList where prenom is null
        defaultMembreShouldNotBeFound("prenom.specified=false");
    }

    @Test
    @Transactional
    void getAllMembresByPrenomContainsSomething() throws Exception {
        // Initialize the database
        membreRepository.saveAndFlush(membre);

        // Get all the membreList where prenom contains DEFAULT_PRENOM
        defaultMembreShouldBeFound("prenom.contains=" + DEFAULT_PRENOM);

        // Get all the membreList where prenom contains UPDATED_PRENOM
        defaultMembreShouldNotBeFound("prenom.contains=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllMembresByPrenomNotContainsSomething() throws Exception {
        // Initialize the database
        membreRepository.saveAndFlush(membre);

        // Get all the membreList where prenom does not contain DEFAULT_PRENOM
        defaultMembreShouldNotBeFound("prenom.doesNotContain=" + DEFAULT_PRENOM);

        // Get all the membreList where prenom does not contain UPDATED_PRENOM
        defaultMembreShouldBeFound("prenom.doesNotContain=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllMembresByFonctionInterneIsEqualToSomething() throws Exception {
        // Initialize the database
        membreRepository.saveAndFlush(membre);

        // Get all the membreList where fonctionInterne equals to DEFAULT_FONCTION_INTERNE
        defaultMembreShouldBeFound("fonctionInterne.equals=" + DEFAULT_FONCTION_INTERNE);

        // Get all the membreList where fonctionInterne equals to UPDATED_FONCTION_INTERNE
        defaultMembreShouldNotBeFound("fonctionInterne.equals=" + UPDATED_FONCTION_INTERNE);
    }

    @Test
    @Transactional
    void getAllMembresByFonctionInterneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        membreRepository.saveAndFlush(membre);

        // Get all the membreList where fonctionInterne not equals to DEFAULT_FONCTION_INTERNE
        defaultMembreShouldNotBeFound("fonctionInterne.notEquals=" + DEFAULT_FONCTION_INTERNE);

        // Get all the membreList where fonctionInterne not equals to UPDATED_FONCTION_INTERNE
        defaultMembreShouldBeFound("fonctionInterne.notEquals=" + UPDATED_FONCTION_INTERNE);
    }

    @Test
    @Transactional
    void getAllMembresByFonctionInterneIsInShouldWork() throws Exception {
        // Initialize the database
        membreRepository.saveAndFlush(membre);

        // Get all the membreList where fonctionInterne in DEFAULT_FONCTION_INTERNE or UPDATED_FONCTION_INTERNE
        defaultMembreShouldBeFound("fonctionInterne.in=" + DEFAULT_FONCTION_INTERNE + "," + UPDATED_FONCTION_INTERNE);

        // Get all the membreList where fonctionInterne equals to UPDATED_FONCTION_INTERNE
        defaultMembreShouldNotBeFound("fonctionInterne.in=" + UPDATED_FONCTION_INTERNE);
    }

    @Test
    @Transactional
    void getAllMembresByFonctionInterneIsNullOrNotNull() throws Exception {
        // Initialize the database
        membreRepository.saveAndFlush(membre);

        // Get all the membreList where fonctionInterne is not null
        defaultMembreShouldBeFound("fonctionInterne.specified=true");

        // Get all the membreList where fonctionInterne is null
        defaultMembreShouldNotBeFound("fonctionInterne.specified=false");
    }

    @Test
    @Transactional
    void getAllMembresByFonctionExterneIsEqualToSomething() throws Exception {
        // Initialize the database
        membreRepository.saveAndFlush(membre);

        // Get all the membreList where fonctionExterne equals to DEFAULT_FONCTION_EXTERNE
        defaultMembreShouldBeFound("fonctionExterne.equals=" + DEFAULT_FONCTION_EXTERNE);

        // Get all the membreList where fonctionExterne equals to UPDATED_FONCTION_EXTERNE
        defaultMembreShouldNotBeFound("fonctionExterne.equals=" + UPDATED_FONCTION_EXTERNE);
    }

    @Test
    @Transactional
    void getAllMembresByFonctionExterneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        membreRepository.saveAndFlush(membre);

        // Get all the membreList where fonctionExterne not equals to DEFAULT_FONCTION_EXTERNE
        defaultMembreShouldNotBeFound("fonctionExterne.notEquals=" + DEFAULT_FONCTION_EXTERNE);

        // Get all the membreList where fonctionExterne not equals to UPDATED_FONCTION_EXTERNE
        defaultMembreShouldBeFound("fonctionExterne.notEquals=" + UPDATED_FONCTION_EXTERNE);
    }

    @Test
    @Transactional
    void getAllMembresByFonctionExterneIsInShouldWork() throws Exception {
        // Initialize the database
        membreRepository.saveAndFlush(membre);

        // Get all the membreList where fonctionExterne in DEFAULT_FONCTION_EXTERNE or UPDATED_FONCTION_EXTERNE
        defaultMembreShouldBeFound("fonctionExterne.in=" + DEFAULT_FONCTION_EXTERNE + "," + UPDATED_FONCTION_EXTERNE);

        // Get all the membreList where fonctionExterne equals to UPDATED_FONCTION_EXTERNE
        defaultMembreShouldNotBeFound("fonctionExterne.in=" + UPDATED_FONCTION_EXTERNE);
    }

    @Test
    @Transactional
    void getAllMembresByFonctionExterneIsNullOrNotNull() throws Exception {
        // Initialize the database
        membreRepository.saveAndFlush(membre);

        // Get all the membreList where fonctionExterne is not null
        defaultMembreShouldBeFound("fonctionExterne.specified=true");

        // Get all the membreList where fonctionExterne is null
        defaultMembreShouldNotBeFound("fonctionExterne.specified=false");
    }

    @Test
    @Transactional
    void getAllMembresByFonctionExterneContainsSomething() throws Exception {
        // Initialize the database
        membreRepository.saveAndFlush(membre);

        // Get all the membreList where fonctionExterne contains DEFAULT_FONCTION_EXTERNE
        defaultMembreShouldBeFound("fonctionExterne.contains=" + DEFAULT_FONCTION_EXTERNE);

        // Get all the membreList where fonctionExterne contains UPDATED_FONCTION_EXTERNE
        defaultMembreShouldNotBeFound("fonctionExterne.contains=" + UPDATED_FONCTION_EXTERNE);
    }

    @Test
    @Transactional
    void getAllMembresByFonctionExterneNotContainsSomething() throws Exception {
        // Initialize the database
        membreRepository.saveAndFlush(membre);

        // Get all the membreList where fonctionExterne does not contain DEFAULT_FONCTION_EXTERNE
        defaultMembreShouldNotBeFound("fonctionExterne.doesNotContain=" + DEFAULT_FONCTION_EXTERNE);

        // Get all the membreList where fonctionExterne does not contain UPDATED_FONCTION_EXTERNE
        defaultMembreShouldBeFound("fonctionExterne.doesNotContain=" + UPDATED_FONCTION_EXTERNE);
    }

    @Test
    @Transactional
    void getAllMembresByStructureIsEqualToSomething() throws Exception {
        // Initialize the database
        membreRepository.saveAndFlush(membre);

        // Get all the membreList where structure equals to DEFAULT_STRUCTURE
        defaultMembreShouldBeFound("structure.equals=" + DEFAULT_STRUCTURE);

        // Get all the membreList where structure equals to UPDATED_STRUCTURE
        defaultMembreShouldNotBeFound("structure.equals=" + UPDATED_STRUCTURE);
    }

    @Test
    @Transactional
    void getAllMembresByStructureIsNotEqualToSomething() throws Exception {
        // Initialize the database
        membreRepository.saveAndFlush(membre);

        // Get all the membreList where structure not equals to DEFAULT_STRUCTURE
        defaultMembreShouldNotBeFound("structure.notEquals=" + DEFAULT_STRUCTURE);

        // Get all the membreList where structure not equals to UPDATED_STRUCTURE
        defaultMembreShouldBeFound("structure.notEquals=" + UPDATED_STRUCTURE);
    }

    @Test
    @Transactional
    void getAllMembresByStructureIsInShouldWork() throws Exception {
        // Initialize the database
        membreRepository.saveAndFlush(membre);

        // Get all the membreList where structure in DEFAULT_STRUCTURE or UPDATED_STRUCTURE
        defaultMembreShouldBeFound("structure.in=" + DEFAULT_STRUCTURE + "," + UPDATED_STRUCTURE);

        // Get all the membreList where structure equals to UPDATED_STRUCTURE
        defaultMembreShouldNotBeFound("structure.in=" + UPDATED_STRUCTURE);
    }

    @Test
    @Transactional
    void getAllMembresByStructureIsNullOrNotNull() throws Exception {
        // Initialize the database
        membreRepository.saveAndFlush(membre);

        // Get all the membreList where structure is not null
        defaultMembreShouldBeFound("structure.specified=true");

        // Get all the membreList where structure is null
        defaultMembreShouldNotBeFound("structure.specified=false");
    }

    @Test
    @Transactional
    void getAllMembresByStructureContainsSomething() throws Exception {
        // Initialize the database
        membreRepository.saveAndFlush(membre);

        // Get all the membreList where structure contains DEFAULT_STRUCTURE
        defaultMembreShouldBeFound("structure.contains=" + DEFAULT_STRUCTURE);

        // Get all the membreList where structure contains UPDATED_STRUCTURE
        defaultMembreShouldNotBeFound("structure.contains=" + UPDATED_STRUCTURE);
    }

    @Test
    @Transactional
    void getAllMembresByStructureNotContainsSomething() throws Exception {
        // Initialize the database
        membreRepository.saveAndFlush(membre);

        // Get all the membreList where structure does not contain DEFAULT_STRUCTURE
        defaultMembreShouldNotBeFound("structure.doesNotContain=" + DEFAULT_STRUCTURE);

        // Get all the membreList where structure does not contain UPDATED_STRUCTURE
        defaultMembreShouldBeFound("structure.doesNotContain=" + UPDATED_STRUCTURE);
    }

    @Test
    @Transactional
    void getAllMembresBySituationMatrimonialeIsEqualToSomething() throws Exception {
        // Initialize the database
        membreRepository.saveAndFlush(membre);

        // Get all the membreList where situationMatrimoniale equals to DEFAULT_SITUATION_MATRIMONIALE
        defaultMembreShouldBeFound("situationMatrimoniale.equals=" + DEFAULT_SITUATION_MATRIMONIALE);

        // Get all the membreList where situationMatrimoniale equals to UPDATED_SITUATION_MATRIMONIALE
        defaultMembreShouldNotBeFound("situationMatrimoniale.equals=" + UPDATED_SITUATION_MATRIMONIALE);
    }

    @Test
    @Transactional
    void getAllMembresBySituationMatrimonialeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        membreRepository.saveAndFlush(membre);

        // Get all the membreList where situationMatrimoniale not equals to DEFAULT_SITUATION_MATRIMONIALE
        defaultMembreShouldNotBeFound("situationMatrimoniale.notEquals=" + DEFAULT_SITUATION_MATRIMONIALE);

        // Get all the membreList where situationMatrimoniale not equals to UPDATED_SITUATION_MATRIMONIALE
        defaultMembreShouldBeFound("situationMatrimoniale.notEquals=" + UPDATED_SITUATION_MATRIMONIALE);
    }

    @Test
    @Transactional
    void getAllMembresBySituationMatrimonialeIsInShouldWork() throws Exception {
        // Initialize the database
        membreRepository.saveAndFlush(membre);

        // Get all the membreList where situationMatrimoniale in DEFAULT_SITUATION_MATRIMONIALE or UPDATED_SITUATION_MATRIMONIALE
        defaultMembreShouldBeFound("situationMatrimoniale.in=" + DEFAULT_SITUATION_MATRIMONIALE + "," + UPDATED_SITUATION_MATRIMONIALE);

        // Get all the membreList where situationMatrimoniale equals to UPDATED_SITUATION_MATRIMONIALE
        defaultMembreShouldNotBeFound("situationMatrimoniale.in=" + UPDATED_SITUATION_MATRIMONIALE);
    }

    @Test
    @Transactional
    void getAllMembresBySituationMatrimonialeIsNullOrNotNull() throws Exception {
        // Initialize the database
        membreRepository.saveAndFlush(membre);

        // Get all the membreList where situationMatrimoniale is not null
        defaultMembreShouldBeFound("situationMatrimoniale.specified=true");

        // Get all the membreList where situationMatrimoniale is null
        defaultMembreShouldNotBeFound("situationMatrimoniale.specified=false");
    }

    @Test
    @Transactional
    void getAllMembresBySituationMatrimonialeContainsSomething() throws Exception {
        // Initialize the database
        membreRepository.saveAndFlush(membre);

        // Get all the membreList where situationMatrimoniale contains DEFAULT_SITUATION_MATRIMONIALE
        defaultMembreShouldBeFound("situationMatrimoniale.contains=" + DEFAULT_SITUATION_MATRIMONIALE);

        // Get all the membreList where situationMatrimoniale contains UPDATED_SITUATION_MATRIMONIALE
        defaultMembreShouldNotBeFound("situationMatrimoniale.contains=" + UPDATED_SITUATION_MATRIMONIALE);
    }

    @Test
    @Transactional
    void getAllMembresBySituationMatrimonialeNotContainsSomething() throws Exception {
        // Initialize the database
        membreRepository.saveAndFlush(membre);

        // Get all the membreList where situationMatrimoniale does not contain DEFAULT_SITUATION_MATRIMONIALE
        defaultMembreShouldNotBeFound("situationMatrimoniale.doesNotContain=" + DEFAULT_SITUATION_MATRIMONIALE);

        // Get all the membreList where situationMatrimoniale does not contain UPDATED_SITUATION_MATRIMONIALE
        defaultMembreShouldBeFound("situationMatrimoniale.doesNotContain=" + UPDATED_SITUATION_MATRIMONIALE);
    }

    @Test
    @Transactional
    void getAllMembresByNumeroTelephoneIsEqualToSomething() throws Exception {
        // Initialize the database
        membreRepository.saveAndFlush(membre);

        // Get all the membreList where numeroTelephone equals to DEFAULT_NUMERO_TELEPHONE
        defaultMembreShouldBeFound("numeroTelephone.equals=" + DEFAULT_NUMERO_TELEPHONE);

        // Get all the membreList where numeroTelephone equals to UPDATED_NUMERO_TELEPHONE
        defaultMembreShouldNotBeFound("numeroTelephone.equals=" + UPDATED_NUMERO_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllMembresByNumeroTelephoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        membreRepository.saveAndFlush(membre);

        // Get all the membreList where numeroTelephone not equals to DEFAULT_NUMERO_TELEPHONE
        defaultMembreShouldNotBeFound("numeroTelephone.notEquals=" + DEFAULT_NUMERO_TELEPHONE);

        // Get all the membreList where numeroTelephone not equals to UPDATED_NUMERO_TELEPHONE
        defaultMembreShouldBeFound("numeroTelephone.notEquals=" + UPDATED_NUMERO_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllMembresByNumeroTelephoneIsInShouldWork() throws Exception {
        // Initialize the database
        membreRepository.saveAndFlush(membre);

        // Get all the membreList where numeroTelephone in DEFAULT_NUMERO_TELEPHONE or UPDATED_NUMERO_TELEPHONE
        defaultMembreShouldBeFound("numeroTelephone.in=" + DEFAULT_NUMERO_TELEPHONE + "," + UPDATED_NUMERO_TELEPHONE);

        // Get all the membreList where numeroTelephone equals to UPDATED_NUMERO_TELEPHONE
        defaultMembreShouldNotBeFound("numeroTelephone.in=" + UPDATED_NUMERO_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllMembresByNumeroTelephoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        membreRepository.saveAndFlush(membre);

        // Get all the membreList where numeroTelephone is not null
        defaultMembreShouldBeFound("numeroTelephone.specified=true");

        // Get all the membreList where numeroTelephone is null
        defaultMembreShouldNotBeFound("numeroTelephone.specified=false");
    }

    @Test
    @Transactional
    void getAllMembresByNumeroTelephoneContainsSomething() throws Exception {
        // Initialize the database
        membreRepository.saveAndFlush(membre);

        // Get all the membreList where numeroTelephone contains DEFAULT_NUMERO_TELEPHONE
        defaultMembreShouldBeFound("numeroTelephone.contains=" + DEFAULT_NUMERO_TELEPHONE);

        // Get all the membreList where numeroTelephone contains UPDATED_NUMERO_TELEPHONE
        defaultMembreShouldNotBeFound("numeroTelephone.contains=" + UPDATED_NUMERO_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllMembresByNumeroTelephoneNotContainsSomething() throws Exception {
        // Initialize the database
        membreRepository.saveAndFlush(membre);

        // Get all the membreList where numeroTelephone does not contain DEFAULT_NUMERO_TELEPHONE
        defaultMembreShouldNotBeFound("numeroTelephone.doesNotContain=" + DEFAULT_NUMERO_TELEPHONE);

        // Get all the membreList where numeroTelephone does not contain UPDATED_NUMERO_TELEPHONE
        defaultMembreShouldBeFound("numeroTelephone.doesNotContain=" + UPDATED_NUMERO_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllMembresByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        membreRepository.saveAndFlush(membre);

        // Get all the membreList where email equals to DEFAULT_EMAIL
        defaultMembreShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the membreList where email equals to UPDATED_EMAIL
        defaultMembreShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllMembresByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        membreRepository.saveAndFlush(membre);

        // Get all the membreList where email not equals to DEFAULT_EMAIL
        defaultMembreShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the membreList where email not equals to UPDATED_EMAIL
        defaultMembreShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllMembresByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        membreRepository.saveAndFlush(membre);

        // Get all the membreList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultMembreShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the membreList where email equals to UPDATED_EMAIL
        defaultMembreShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllMembresByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        membreRepository.saveAndFlush(membre);

        // Get all the membreList where email is not null
        defaultMembreShouldBeFound("email.specified=true");

        // Get all the membreList where email is null
        defaultMembreShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllMembresByEmailContainsSomething() throws Exception {
        // Initialize the database
        membreRepository.saveAndFlush(membre);

        // Get all the membreList where email contains DEFAULT_EMAIL
        defaultMembreShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the membreList where email contains UPDATED_EMAIL
        defaultMembreShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllMembresByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        membreRepository.saveAndFlush(membre);

        // Get all the membreList where email does not contain DEFAULT_EMAIL
        defaultMembreShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the membreList where email does not contain UPDATED_EMAIL
        defaultMembreShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllMembresBySectionsIsEqualToSomething() throws Exception {
        // Initialize the database
        membreRepository.saveAndFlush(membre);
        Section sections;
        if (TestUtil.findAll(em, Section.class).isEmpty()) {
            sections = SectionResourceIT.createEntity(em);
            em.persist(sections);
            em.flush();
        } else {
            sections = TestUtil.findAll(em, Section.class).get(0);
        }
        em.persist(sections);
        em.flush();
        membre.addSections(sections);
        membreRepository.saveAndFlush(membre);
        Long sectionsId = sections.getId();

        // Get all the membreList where sections equals to sectionsId
        defaultMembreShouldBeFound("sectionsId.equals=" + sectionsId);

        // Get all the membreList where sections equals to (sectionsId + 1)
        defaultMembreShouldNotBeFound("sectionsId.equals=" + (sectionsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMembreShouldBeFound(String filter) throws Exception {
        restMembreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(membre.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].fonctionInterne").value(hasItem(DEFAULT_FONCTION_INTERNE.toString())))
            .andExpect(jsonPath("$.[*].fonctionExterne").value(hasItem(DEFAULT_FONCTION_EXTERNE)))
            .andExpect(jsonPath("$.[*].structure").value(hasItem(DEFAULT_STRUCTURE)))
            .andExpect(jsonPath("$.[*].situationMatrimoniale").value(hasItem(DEFAULT_SITUATION_MATRIMONIALE)))
            .andExpect(jsonPath("$.[*].numeroTelephone").value(hasItem(DEFAULT_NUMERO_TELEPHONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));

        // Check, that the count call also returns 1
        restMembreMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMembreShouldNotBeFound(String filter) throws Exception {
        restMembreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMembreMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMembre() throws Exception {
        // Get the membre
        restMembreMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMembre() throws Exception {
        // Initialize the database
        membreRepository.saveAndFlush(membre);

        int databaseSizeBeforeUpdate = membreRepository.findAll().size();

        // Update the membre
        Membre updatedMembre = membreRepository.findById(membre.getId()).get();
        // Disconnect from session so that the updates on updatedMembre are not directly saved in db
        em.detach(updatedMembre);
        updatedMembre
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .fonctionInterne(UPDATED_FONCTION_INTERNE)
            .fonctionExterne(UPDATED_FONCTION_EXTERNE)
            .structure(UPDATED_STRUCTURE)
            .situationMatrimoniale(UPDATED_SITUATION_MATRIMONIALE)
            .numeroTelephone(UPDATED_NUMERO_TELEPHONE)
            .email(UPDATED_EMAIL);

        restMembreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMembre.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMembre))
            )
            .andExpect(status().isOk());

        // Validate the Membre in the database
        List<Membre> membreList = membreRepository.findAll();
        assertThat(membreList).hasSize(databaseSizeBeforeUpdate);
        Membre testMembre = membreList.get(membreList.size() - 1);
        assertThat(testMembre.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testMembre.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testMembre.getFonctionInterne()).isEqualTo(UPDATED_FONCTION_INTERNE);
        assertThat(testMembre.getFonctionExterne()).isEqualTo(UPDATED_FONCTION_EXTERNE);
        assertThat(testMembre.getStructure()).isEqualTo(UPDATED_STRUCTURE);
        assertThat(testMembre.getSituationMatrimoniale()).isEqualTo(UPDATED_SITUATION_MATRIMONIALE);
        assertThat(testMembre.getNumeroTelephone()).isEqualTo(UPDATED_NUMERO_TELEPHONE);
        assertThat(testMembre.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void putNonExistingMembre() throws Exception {
        int databaseSizeBeforeUpdate = membreRepository.findAll().size();
        membre.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMembreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, membre.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(membre))
            )
            .andExpect(status().isBadRequest());

        // Validate the Membre in the database
        List<Membre> membreList = membreRepository.findAll();
        assertThat(membreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMembre() throws Exception {
        int databaseSizeBeforeUpdate = membreRepository.findAll().size();
        membre.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMembreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(membre))
            )
            .andExpect(status().isBadRequest());

        // Validate the Membre in the database
        List<Membre> membreList = membreRepository.findAll();
        assertThat(membreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMembre() throws Exception {
        int databaseSizeBeforeUpdate = membreRepository.findAll().size();
        membre.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMembreMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(membre)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Membre in the database
        List<Membre> membreList = membreRepository.findAll();
        assertThat(membreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMembreWithPatch() throws Exception {
        // Initialize the database
        membreRepository.saveAndFlush(membre);

        int databaseSizeBeforeUpdate = membreRepository.findAll().size();

        // Update the membre using partial update
        Membre partialUpdatedMembre = new Membre();
        partialUpdatedMembre.setId(membre.getId());

        partialUpdatedMembre.nom(UPDATED_NOM).email(UPDATED_EMAIL);

        restMembreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMembre.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMembre))
            )
            .andExpect(status().isOk());

        // Validate the Membre in the database
        List<Membre> membreList = membreRepository.findAll();
        assertThat(membreList).hasSize(databaseSizeBeforeUpdate);
        Membre testMembre = membreList.get(membreList.size() - 1);
        assertThat(testMembre.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testMembre.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testMembre.getFonctionInterne()).isEqualTo(DEFAULT_FONCTION_INTERNE);
        assertThat(testMembre.getFonctionExterne()).isEqualTo(DEFAULT_FONCTION_EXTERNE);
        assertThat(testMembre.getStructure()).isEqualTo(DEFAULT_STRUCTURE);
        assertThat(testMembre.getSituationMatrimoniale()).isEqualTo(DEFAULT_SITUATION_MATRIMONIALE);
        assertThat(testMembre.getNumeroTelephone()).isEqualTo(DEFAULT_NUMERO_TELEPHONE);
        assertThat(testMembre.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void fullUpdateMembreWithPatch() throws Exception {
        // Initialize the database
        membreRepository.saveAndFlush(membre);

        int databaseSizeBeforeUpdate = membreRepository.findAll().size();

        // Update the membre using partial update
        Membre partialUpdatedMembre = new Membre();
        partialUpdatedMembre.setId(membre.getId());

        partialUpdatedMembre
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .fonctionInterne(UPDATED_FONCTION_INTERNE)
            .fonctionExterne(UPDATED_FONCTION_EXTERNE)
            .structure(UPDATED_STRUCTURE)
            .situationMatrimoniale(UPDATED_SITUATION_MATRIMONIALE)
            .numeroTelephone(UPDATED_NUMERO_TELEPHONE)
            .email(UPDATED_EMAIL);

        restMembreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMembre.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMembre))
            )
            .andExpect(status().isOk());

        // Validate the Membre in the database
        List<Membre> membreList = membreRepository.findAll();
        assertThat(membreList).hasSize(databaseSizeBeforeUpdate);
        Membre testMembre = membreList.get(membreList.size() - 1);
        assertThat(testMembre.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testMembre.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testMembre.getFonctionInterne()).isEqualTo(UPDATED_FONCTION_INTERNE);
        assertThat(testMembre.getFonctionExterne()).isEqualTo(UPDATED_FONCTION_EXTERNE);
        assertThat(testMembre.getStructure()).isEqualTo(UPDATED_STRUCTURE);
        assertThat(testMembre.getSituationMatrimoniale()).isEqualTo(UPDATED_SITUATION_MATRIMONIALE);
        assertThat(testMembre.getNumeroTelephone()).isEqualTo(UPDATED_NUMERO_TELEPHONE);
        assertThat(testMembre.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void patchNonExistingMembre() throws Exception {
        int databaseSizeBeforeUpdate = membreRepository.findAll().size();
        membre.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMembreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, membre.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(membre))
            )
            .andExpect(status().isBadRequest());

        // Validate the Membre in the database
        List<Membre> membreList = membreRepository.findAll();
        assertThat(membreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMembre() throws Exception {
        int databaseSizeBeforeUpdate = membreRepository.findAll().size();
        membre.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMembreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(membre))
            )
            .andExpect(status().isBadRequest());

        // Validate the Membre in the database
        List<Membre> membreList = membreRepository.findAll();
        assertThat(membreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMembre() throws Exception {
        int databaseSizeBeforeUpdate = membreRepository.findAll().size();
        membre.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMembreMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(membre)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Membre in the database
        List<Membre> membreList = membreRepository.findAll();
        assertThat(membreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMembre() throws Exception {
        // Initialize the database
        membreRepository.saveAndFlush(membre);

        int databaseSizeBeforeDelete = membreRepository.findAll().size();

        // Delete the membre
        restMembreMockMvc
            .perform(delete(ENTITY_API_URL_ID, membre.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Membre> membreList = membreRepository.findAll();
        assertThat(membreList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
