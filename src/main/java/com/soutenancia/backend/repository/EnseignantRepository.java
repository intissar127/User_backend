package com.soutenancia.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.soutenancia.backend.models.Enseignant;

@Repository
public interface EnseignantRepository extends JpaRepository<Enseignant, Long> {
}
