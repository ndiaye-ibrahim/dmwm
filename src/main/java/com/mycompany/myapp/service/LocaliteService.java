package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Localite;
import com.mycompany.myapp.repository.LocaliteRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Localite}.
 */
@Service
@Transactional
public class LocaliteService {

    private final Logger log = LoggerFactory.getLogger(LocaliteService.class);

    private final LocaliteRepository localiteRepository;

    public LocaliteService(LocaliteRepository localiteRepository) {
        this.localiteRepository = localiteRepository;
    }

    /**
     * Save a localite.
     *
     * @param localite the entity to save.
     * @return the persisted entity.
     */
    public Localite save(Localite localite) {
        log.debug("Request to save Localite : {}", localite);
        return localiteRepository.save(localite);
    }

    /**
     * Partially update a localite.
     *
     * @param localite the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Localite> partialUpdate(Localite localite) {
        log.debug("Request to partially update Localite : {}", localite);

        return localiteRepository
            .findById(localite.getId())
            .map(existingLocalite -> {
                if (localite.getNom() != null) {
                    existingLocalite.setNom(localite.getNom());
                }

                return existingLocalite;
            })
            .map(localiteRepository::save);
    }

    /**
     * Get all the localites.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Localite> findAll(Pageable pageable) {
        log.debug("Request to get all Localites");
        return localiteRepository.findAll(pageable);
    }

    /**
     * Get one localite by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Localite> findOne(Long id) {
        log.debug("Request to get Localite : {}", id);
        return localiteRepository.findById(id);
    }

    /**
     * Delete the localite by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Localite : {}", id);
        localiteRepository.deleteById(id);
    }
}
