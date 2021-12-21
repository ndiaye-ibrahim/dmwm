package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.SousLocalite;
import com.mycompany.myapp.repository.SousLocaliteRepository;
import com.mycompany.myapp.service.criteria.SousLocaliteCriteria;
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
 * Service for executing complex queries for {@link SousLocalite} entities in the database.
 * The main input is a {@link SousLocaliteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SousLocalite} or a {@link Page} of {@link SousLocalite} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SousLocaliteQueryService extends QueryService<SousLocalite> {

    private final Logger log = LoggerFactory.getLogger(SousLocaliteQueryService.class);

    private final SousLocaliteRepository sousLocaliteRepository;

    public SousLocaliteQueryService(SousLocaliteRepository sousLocaliteRepository) {
        this.sousLocaliteRepository = sousLocaliteRepository;
    }

    /**
     * Return a {@link List} of {@link SousLocalite} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SousLocalite> findByCriteria(SousLocaliteCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SousLocalite> specification = createSpecification(criteria);
        return sousLocaliteRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link SousLocalite} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SousLocalite> findByCriteria(SousLocaliteCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SousLocalite> specification = createSpecification(criteria);
        return sousLocaliteRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SousLocaliteCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SousLocalite> specification = createSpecification(criteria);
        return sousLocaliteRepository.count(specification);
    }

    /**
     * Function to convert {@link SousLocaliteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SousLocalite> createSpecification(SousLocaliteCriteria criteria) {
        Specification<SousLocalite> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SousLocalite_.id));
            }
            if (criteria.getNom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNom(), SousLocalite_.nom));
            }
            if (criteria.getLocalitesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLocalitesId(),
                            root -> root.join(SousLocalite_.localites, JoinType.LEFT).get(Localite_.id)
                        )
                    );
            }
            if (criteria.getSectionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSectionId(),
                            root -> root.join(SousLocalite_.section, JoinType.LEFT).get(Section_.id)
                        )
                    );
            }
            if (criteria.getCelluleId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCelluleId(),
                            root -> root.join(SousLocalite_.cellule, JoinType.LEFT).get(Cellule_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
