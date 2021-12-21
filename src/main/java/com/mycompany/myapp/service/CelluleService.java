package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Cellule;
import com.mycompany.myapp.repository.CelluleRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Cellule}.
 */
@Service
@Transactional
public class CelluleService {

    private final Logger log = LoggerFactory.getLogger(CelluleService.class);

    private final CelluleRepository celluleRepository;

    public CelluleService(CelluleRepository celluleRepository) {
        this.celluleRepository = celluleRepository;
    }

    /**
     * Save a cellule.
     *
     * @param cellule the entity to save.
     * @return the persisted entity.
     */
    public Cellule save(Cellule cellule) {
        log.debug("Request to save Cellule : {}", cellule);
        return celluleRepository.save(cellule);
    }

    /**
     * Partially update a cellule.
     *
     * @param cellule the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Cellule> partialUpdate(Cellule cellule) {
        log.debug("Request to partially update Cellule : {}", cellule);

        return celluleRepository
            .findById(cellule.getId())
            .map(existingCellule -> {
                if (cellule.getNom() != null) {
                    existingCellule.setNom(cellule.getNom());
                }

                return existingCellule;
            })
            .map(celluleRepository::save);
    }

    /**
     * Get all the cellules.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Cellule> findAll(Pageable pageable) {
        log.debug("Request to get all Cellules");
        return celluleRepository.findAll(pageable);
    }

    /**
     * Get one cellule by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Cellule> findOne(Long id) {
        log.debug("Request to get Cellule : {}", id);
        return celluleRepository.findById(id);
    }

    /**
     * Delete the cellule by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Cellule : {}", id);
        celluleRepository.deleteById(id);
    }
}
