package com.project.back_end.services;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.spi.ObjectThreadContextMap;
import org.aspectj.internal.lang.annotation.ajcDeclareAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.back_end.models.Prescription;
import com.project.back_end.repo.PrescriptionRepository;

@Service
public class PrescriptionService {
    
    private final PrescriptionRepository prescriptionRepository;

    @Autowired
    public PrescriptionService(PrescriptionRepository prescriptionRepository) {
        this.prescriptionRepository = prescriptionRepository;
    }

    @Transactional
    public ResponseEntity<Map<String, String>> savePrescription(Prescription aPrescription){
        try {
            List<Prescription> prescriptions = prescriptionRepository.findByAppointmentId(aPrescription.getAppointmentId());

            if(!prescriptions.isEmpty()) return ResponseEntity.badRequest().body(Map.of("message", "The prescriptions already exists!"));

            prescriptionRepository.save(aPrescription);
            
            return ResponseEntity.status(201).body(Map.of("message", "Prescription created with success!"));
        } catch (Exception e) {
            System.out.println("An error ocurred! - "+e.getMessage());
            return ResponseEntity.internalServerError().body(Map.of("message", "An error ocurred when trying to save the prescription."));
        }
    }
    
    public ResponseEntity<Map<String, Object>> getPrescription(Long appointmentId){
        try {
            List<Prescription> prescriptions = prescriptionRepository.findByAppointmentId(appointmentId);

            if(prescriptions.isEmpty()) return ResponseEntity.badRequest().body(Map.of("message", "No preescriptions found!"));

            return ResponseEntity.ok().body(Map.of("prescriptions", prescriptions));
        } catch (Exception e) {
            System.out.println("An error ocurred! - "+ e.getMessage());
            return ResponseEntity.internalServerError().body(Map.of("message", "An error ocurred! Please try again later."));
        }
    }
}
