package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Membre;
import com.mycompany.myapp.repository.MembreRepository;
import com.mycompany.myapp.service.criteria.MembreCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Membre} entities in the database.
 * The main input is a {@link MembreCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Membre} or a {@link Page} of {@link Membre} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MembreQueryService extends QueryService<Membre> {

    private final Logger log = LoggerFactory.getLogger(MembreQueryService.class);

    private final MembreRepository membreRepository;

    public MembreQueryService(MembreRepository membreRepository) {
        this.membreRepository = membreRepository;
    }

    /**
     * Return a {@link List} of {@link Membre} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Membre> findByCriteria(MembreCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Membre> specification = createSpecification(criteria);
        return membreRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Membre} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Membre> findByCriteria(MembreCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Membre> specification = createSpecification(criteria);
        return membreRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MembreCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Membre> specification = createSpecification(criteria);
        return membreRepository.count(specification);
    }

    /**
     * Function to convert {@link MembreCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Membre> createSpecification(MembreCriteria criteria) {
        Specification<Membre> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Membre_.id));
            }
            if (criteria.getNom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNom(), Membre_.nom));
            }
            if (criteria.getPrenom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPrenom(), Membre_.prenom));
            }
            if (criteria.getFonctionInterne() != null) {
                specification = specification.and(buildSpecification(criteria.getFonctionInterne(), Membre_.fonctionInterne));
            }
            if (criteria.getFonctionExterne() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFonctionExterne(), Membre_.fonctionExterne));
            }
            if (criteria.getStructure() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStructure(), Membre_.structure));
            }
            if (criteria.getSituationMatrimoniale() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getSituationMatrimoniale(), Membre_.situationMatrimoniale));
            }
            if (criteria.getNumeroTelephone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNumeroTelephone(), Membre_.numeroTelephone));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Membre_.email));
            }
            if (criteria.getSectionsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getSectionsId(), root -> root.join(Membre_.sections, JoinType.LEFT).get(Section_.id))
                    );
            }
        }
        return specification;
    }
}
