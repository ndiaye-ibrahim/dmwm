package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.SousLocalite;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SousLocalite entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SousLocaliteRepository extends JpaRepository<SousLocalite, Long>, JpaSpecificationExecutor<SousLocalite> {}
