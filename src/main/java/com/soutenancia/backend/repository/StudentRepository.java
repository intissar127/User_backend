package com.soutenancia.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.soutenancia.backend.models.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {}