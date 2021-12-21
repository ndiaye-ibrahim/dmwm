package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.SousLocalite;
import com.mycompany.myapp.repository.SousLocaliteRepository;
import com.mycompany.myapp.service.SousLocaliteQueryService;
import com.mycompany.myapp.service.SousLocaliteService;
import com.mycompany.myapp.service.criteria.SousLocaliteCriteria;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.SousLocalite}.
 */
@RestController
@RequestMapping("/api")
public class SousLocaliteResource {

    private final Logger log = LoggerFactory.getLogger(SousLocaliteResource.class);

    private static final String ENTITY_NAME = "sousLocalite";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SousLocaliteService sousLocaliteService;

    private final SousLocaliteRepository sousLocaliteRepository;

    private final SousLocaliteQueryService sousLocaliteQueryService;

    public SousLocaliteResource(
        SousLocaliteService sousLocaliteService,
        SousLocaliteRepository sousLocaliteRepository,
        SousLocaliteQueryService sousLocaliteQueryService
    ) {
        this.sousLocaliteService = sousLocaliteService;
        this.sousLocaliteRepository = sousLocaliteRepository;
        this.sousLocaliteQueryService = sousLocaliteQueryService;
    }

    /**
     * {@code POST  /sous-localites} : Create a new sousLocalite.
     *
     * @param sousLocalite the sousLocalite to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sousLocalite, or with status {@code 400 (Bad Request)} if the sousLocalite has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sous-localites")
    public ResponseEntity<SousLocalite> createSousLocalite(@RequestBody SousLocalite sousLocalite) throws URISyntaxException {
        log.debug("REST request to save SousLocalite : {}", sousLocalite);
        if (sousLocalite.getId() != null) {
            throw new BadRequestAlertException("A new sousLocalite cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SousLocalite result = sousLocaliteService.save(sousLocalite);
        return ResponseEntity
            .created(new URI("/api/sous-localites/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sous-localites/:id} : Updates an existing sousLocalite.
     *
     * @param id the id of the sousLocalite to save.
     * @param sousLocalite the sousLocalite to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sousLocalite,
     * or with status {@code 400 (Bad Request)} if the sousLocalite is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sousLocalite couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sous-localites/{id}")
    public ResponseEntity<SousLocalite> updateSousLocalite(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SousLocalite sousLocalite
    ) throws URISyntaxException {
        log.debug("REST request to update SousLocalite : {}, {}", id, sousLocalite);
        if (sousLocalite.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sousLocalite.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sousLocaliteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SousLocalite result = sousLocaliteService.save(sousLocalite);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sousLocalite.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sous-localites/:id} : Partial updates given fields of an existing sousLocalite, field will ignore if it is null
     *
     * @param id the id of the sousLocalite to save.
     * @param sousLocalite the sousLocalite to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sousLocalite,
     * or with status {@code 400 (Bad Request)} if the sousLocalite is not valid,
     * or with status {@code 404 (Not Found)} if the sousLocalite is not found,
     * or with status {@code 500 (Internal Server Error)} if the sousLocalite couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/sous-localites/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SousLocalite> partialUpdateSousLocalite(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SousLocalite sousLocalite
    ) throws URISyntaxException {
        log.debug("REST request to partial update SousLocalite partially : {}, {}", id, sousLocalite);
        if (sousLocalite.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sousLocalite.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sousLocaliteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SousLocalite> result = sousLocaliteService.partialUpdate(sousLocalite);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sousLocalite.getId().toString())
        );
    }

    /**
     * {@code GET  /sous-localites} : get all the sousLocalites.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sousLocalites in body.
     */
    @GetMapping("/sous-localites")
    public ResponseEntity<List<SousLocalite>> getAllSousLocalites(SousLocaliteCriteria criteria, Pageable pageable) {
        log.debug("REST request to get SousLocalites by criteria: {}", criteria);
        Page<SousLocalite> page = sousLocaliteQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sous-localites/count} : count all the sousLocalites.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/sous-localites/count")
    public ResponseEntity<Long> countSousLocalites(SousLocaliteCriteria criteria) {
        log.debug("REST request to count SousLocalites by criteria: {}", criteria);
        return ResponseEntity.ok().body(sousLocaliteQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /sous-localites/:id} : get the "id" sousLocalite.
     *
     * @param id the id of the sousLocalite to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sousLocalite, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sous-localites/{id}")
    public ResponseEntity<SousLocalite> getSousLocalite(@PathVariable Long id) {
        log.debug("REST request to get SousLocalite : {}", id);
        Optional<SousLocalite> sousLocalite = sousLocaliteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sousLocalite);
    }

    /**
     * {@code DELETE  /sous-localites/:id} : delete the "id" sousLocalite.
     *
     * @param id the id of the sousLocalite to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sous-localites/{id}")
    public ResponseEntity<Void> deleteSousLocalite(@PathVariable Long id) {
        log.debug("REST request to delete SousLocalite : {}", id);
        sousLocaliteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
