package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Localite;
import com.mycompany.myapp.repository.LocaliteRepository;
import com.mycompany.myapp.service.criteria.LocaliteCriteria;
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
 * Service for executing complex queries for {@link Localite} entities in the database.
 * The main input is a {@link LocaliteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Localite} or a {@link Page} of {@link Localite} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LocaliteQueryService extends QueryService<Localite> {

    private final Logger log = LoggerFactory.getLogger(LocaliteQueryService.class);

    private final LocaliteRepository localiteRepository;

    public LocaliteQueryService(LocaliteRepository localiteRepository) {
        this.localiteRepository = localiteRepository;
    }

    /**
     * Return a {@link List} of {@link Localite} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Localite> findByCriteria(LocaliteCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Localite> specification = createSpecification(criteria);
        return localiteRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Localite} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Localite> findByCriteria(LocaliteCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Localite> specification = createSpecification(criteria);
        return localiteRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LocaliteCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Localite> specification = createSpecification(criteria);
        return localiteRepository.count(specification);
    }

    /**
     * Function to convert {@link LocaliteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Localite> createSpecification(LocaliteCriteria criteria) {
        Specification<Localite> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Localite_.id));
            }
            if (criteria.getNom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNom(), Localite_.nom));
            }
            if (criteria.getSouslocaliteId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSouslocaliteId(),
                            root -> root.join(Localite_.souslocalite, JoinType.LEFT).get(SousLocalite_.id)
                        )
                    );
            }
            if (criteria.getCellulesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCellulesId(), root -> root.join(Localite_.cellules, JoinType.LEFT).get(Cellule_.id))
                    );
            }
        }
        return specification;
    }
}
