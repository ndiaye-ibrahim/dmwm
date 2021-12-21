package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Membre;
import com.mycompany.myapp.repository.MembreRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Membre}.
 */
@Service
@Transactional
public class MembreService {

    private final Logger log = LoggerFactory.getLogger(MembreService.class);

    private final MembreRepository membreRepository;

    public MembreService(MembreRepository membreRepository) {
        this.membreRepository = membreRepository;
    }

    /**
     * Save a membre.
     *
     * @param membre the entity to save.
     * @return the persisted entity.
     */
    public Membre save(Membre membre) {
        log.debug("Request to save Membre : {}", membre);
        return membreRepository.save(membre);
    }

    /**
     * Partially update a membre.
     *
     * @param membre the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Membre> partialUpdate(Membre membre) {
        log.debug("Request to partially update Membre : {}", membre);

        return membreRepository
            .findById(membre.getId())
            .map(existingMembre -> {
                if (membre.getNom() != null) {
                    existingMembre.setNom(membre.getNom());
                }
                if (membre.getPrenom() != null) {
                    existingMembre.setPrenom(membre.getPrenom());
                }
                if (membre.getFonctionInterne() != null) {
                    existingMembre.setFonctionInterne(membre.getFonctionInterne());
                }
                if (membre.getFonctionExterne() != null) {
                    existingMembre.setFonctionExterne(membre.getFonctionExterne());
                }
                if (membre.getStructure() != null) {
                    existingMembre.setStructure(membre.getStructure());
                }
                if (membre.getSituationMatrimoniale() != null) {
                    existingMembre.setSituationMatrimoniale(membre.getSituationMatrimoniale());
                }
                if (membre.getNumeroTelephone() != null) {
                    existingMembre.setNumeroTelephone(membre.getNumeroTelephone());
                }
                if (membre.getEmail() != null) {
                    existingMembre.setEmail(membre.getEmail());
                }

                return existingMembre;
            })
            .map(membreRepository::save);
    }

    /**
     * Get all the membres.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Membre> findAll(Pageable pageable) {
        log.debug("Request to get all Membres");
        return membreRepository.findAll(pageable);
    }

    /**
     * Get one membre by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Membre> findOne(Long id) {
        log.debug("Request to get Membre : {}", id);
        return membreRepository.findById(id);
    }

    /**
     * Delete the membre by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Membre : {}", id);
        membreRepository.deleteById(id);
    }
}
