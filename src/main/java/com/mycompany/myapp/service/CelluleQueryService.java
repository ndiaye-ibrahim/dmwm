package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Cellule;
import com.mycompany.myapp.repository.CelluleRepository;
import com.mycompany.myapp.service.criteria.CelluleCriteria;
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
 * Service for executing complex queries for {@link Cellule} entities in the database.
 * The main input is a {@link CelluleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Cellule} or a {@link Page} of {@link Cellule} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CelluleQueryService extends QueryService<Cellule> {

    private final Logger log = LoggerFactory.getLogger(CelluleQueryService.class);

    private final CelluleRepository celluleRepository;

    public CelluleQueryService(CelluleRepository celluleRepository) {
        this.celluleRepository = celluleRepository;
    }

    /**
     * Return a {@link List} of {@link Cellule} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Cellule> findByCriteria(CelluleCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Cellule> specification = createSpecification(criteria);
        return celluleRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Cellule} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Cellule> findByCriteria(CelluleCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Cellule> specification = createSpecification(criteria);
        return celluleRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CelluleCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Cellule> specification = createSpecification(criteria);
        return celluleRepository.count(specification);
    }

    /**
     * Function to convert {@link CelluleCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Cellule> createSpecification(CelluleCriteria criteria) {
        Specification<Cellule> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Cellule_.id));
            }
            if (criteria.getNom() != null) {
                specification = specification.and(buildSpecification(criteria.getNom(), Cellule_.nom));
            }
            if (criteria.getLocalitesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLocalitesId(),
                            root -> root.join(Cellule_.localites, JoinType.LEFT).get(Localite_.id)
                        )
                    );
            }
            if (criteria.getSouslocaliteId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSouslocaliteId(),
                            root -> root.join(Cellule_.souslocalites, JoinType.LEFT).get(SousLocalite_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
