package com.project.back_end.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.back_end.models.Patient;

@Repository
public interface PatientRepository extends JpaRepository{

    public Patient findByEmail(String email);

    public Patient findByEmailOrPhone(String email, String phone);
   }

