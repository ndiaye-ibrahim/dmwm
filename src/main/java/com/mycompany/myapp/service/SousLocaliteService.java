package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.SousLocalite;
import com.mycompany.myapp.repository.SousLocaliteRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SousLocalite}.
 */
@Service
@Transactional
public class SousLocaliteService {

    private final Logger log = LoggerFactory.getLogger(SousLocaliteService.class);

    private final SousLocaliteRepository sousLocaliteRepository;

    public SousLocaliteService(SousLocaliteRepository sousLocaliteRepository) {
        this.sousLocaliteRepository = sousLocaliteRepository;
    }

    /**
     * Save a sousLocalite.
     *
     * @param sousLocalite the entity to save.
     * @return the persisted entity.
     */
    public SousLocalite save(SousLocalite sousLocalite) {
        log.debug("Request to save SousLocalite : {}", sousLocalite);
        return sousLocaliteRepository.save(sousLocalite);
    }

    /**
     * Partially update a sousLocalite.
     *
     * @param sousLocalite the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SousLocalite> partialUpdate(SousLocalite sousLocalite) {
        log.debug("Request to partially update SousLocalite : {}", sousLocalite);

        return sousLocaliteRepository
            .findById(sousLocalite.getId())
            .map(existingSousLocalite -> {
                if (sousLocalite.getNom() != null) {
                    existingSousLocalite.setNom(sousLocalite.getNom());
                }

                return existingSousLocalite;
            })
            .map(sousLocaliteRepository::save);
    }

    /**
     * Get all the sousLocalites.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SousLocalite> findAll(Pageable pageable) {
        log.debug("Request to get all SousLocalites");
        return sousLocaliteRepository.findAll(pageable);
    }

    /**
     * Get one sousLocalite by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SousLocalite> findOne(Long id) {
        log.debug("Request to get SousLocalite : {}", id);
        return sousLocaliteRepository.findById(id);
    }

    /**
     * Delete the sousLocalite by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SousLocalite : {}", id);
        sousLocaliteRepository.deleteById(id);
    }
}
