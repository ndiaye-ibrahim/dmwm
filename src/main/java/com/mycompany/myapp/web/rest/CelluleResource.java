package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Cellule;
import com.mycompany.myapp.repository.CelluleRepository;
import com.mycompany.myapp.service.CelluleQueryService;
import com.mycompany.myapp.service.CelluleService;
import com.mycompany.myapp.service.criteria.CelluleCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Cellule}.
 */
@RestController
@RequestMapping("/api")
public class CelluleResource {

    private final Logger log = LoggerFactory.getLogger(CelluleResource.class);

    private static final String ENTITY_NAME = "cellule";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CelluleService celluleService;

    private final CelluleRepository celluleRepository;

    private final CelluleQueryService celluleQueryService;

    public CelluleResource(CelluleService celluleService, CelluleRepository celluleRepository, CelluleQueryService celluleQueryService) {
        this.celluleService = celluleService;
        this.celluleRepository = celluleRepository;
        this.celluleQueryService = celluleQueryService;
    }

    /**
     * {@code POST  /cellules} : Create a new cellule.
     *
     * @param cellule the cellule to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cellule, or with status {@code 400 (Bad Request)} if the cellule has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cellules")
    public ResponseEntity<Cellule> createCellule(@RequestBody Cellule cellule) throws URISyntaxException {
        log.debug("REST request to save Cellule : {}", cellule);
        if (cellule.getId() != null) {
            throw new BadRequestAlertException("A new cellule cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Cellule result = celluleService.save(cellule);
        return ResponseEntity
            .created(new URI("/api/cellules/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cellules/:id} : Updates an existing cellule.
     *
     * @param id the id of the cellule to save.
     * @param cellule the cellule to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cellule,
     * or with status {@code 400 (Bad Request)} if the cellule is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cellule couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cellules/{id}")
    public ResponseEntity<Cellule> updateCellule(@PathVariable(value = "id", required = false) final Long id, @RequestBody Cellule cellule)
        throws URISyntaxException {
        log.debug("REST request to update Cellule : {}, {}", id, cellule);
        if (cellule.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cellule.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!celluleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Cellule result = celluleService.save(cellule);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cellule.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cellules/:id} : Partial updates given fields of an existing cellule, field will ignore if it is null
     *
     * @param id the id of the cellule to save.
     * @param cellule the cellule to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cellule,
     * or with status {@code 400 (Bad Request)} if the cellule is not valid,
     * or with status {@code 404 (Not Found)} if the cellule is not found,
     * or with status {@code 500 (Internal Server Error)} if the cellule couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cellules/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Cellule> partialUpdateCellule(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Cellule cellule
    ) throws URISyntaxException {
        log.debug("REST request to partial update Cellule partially : {}, {}", id, cellule);
        if (cellule.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cellule.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!celluleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Cellule> result = celluleService.partialUpdate(cellule);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cellule.getId().toString())
        );
    }

    /**
     * {@code GET  /cellules} : get all the cellules.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cellules in body.
     */
    @GetMapping("/cellules")
    public ResponseEntity<List<Cellule>> getAllCellules(CelluleCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Cellules by criteria: {}", criteria);
        Page<Cellule> page = celluleQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cellules/count} : count all the cellules.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/cellules/count")
    public ResponseEntity<Long> countCellules(CelluleCriteria criteria) {
        log.debug("REST request to count Cellules by criteria: {}", criteria);
        return ResponseEntity.ok().body(celluleQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cellules/:id} : get the "id" cellule.
     *
     * @param id the id of the cellule to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cellule, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cellules/{id}")
    public ResponseEntity<Cellule> getCellule(@PathVariable Long id) {
        log.debug("REST request to get Cellule : {}", id);
        Optional<Cellule> cellule = celluleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cellule);
    }

    /**
     * {@code DELETE  /cellules/:id} : delete the "id" cellule.
     *
     * @param id the id of the cellule to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cellules/{id}")
    public ResponseEntity<Void> deleteCellule(@PathVariable Long id) {
        log.debug("REST request to delete Cellule : {}", id);
        celluleService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
