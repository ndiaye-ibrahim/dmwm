package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Cellule;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Cellule entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CelluleRepository extends JpaRepository<Cellule, Long>, JpaSpecificationExecutor<Cellule> {}
