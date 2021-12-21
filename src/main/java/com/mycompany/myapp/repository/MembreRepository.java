package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Membre;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Membre entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MembreRepository extends JpaRepository<Membre, Long>, JpaSpecificationExecutor<Membre> {}
