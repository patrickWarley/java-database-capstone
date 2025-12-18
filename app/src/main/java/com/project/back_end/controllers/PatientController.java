package com.project.back_end.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.back_end.DTO.LoginDTO;
import com.project.back_end.models.Patient;
import com.project.back_end.services.BaseService;
import com.project.back_end.services.PatientService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/patient")
public class PatientController {


    private final PatientService patientService;
    private final BaseService service;

    @Autowired
    public PatientController(PatientService patientService, BaseService service) {
        this.patientService = patientService;
        this.service = service;
    }

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> getPatient(@RequestHeader("Authorization") String token) {
        return patientService.getPatientDetails(token);
    }
    
    @PostMapping("/")
    public ResponseEntity<Map<String, String>> postMethodName(@RequestBody Patient patient) {
        int validationResult = service.validatePatient(patient);

        if(validationResult == 1) {
            if(patientService.createPatient(patient) == 1) return ResponseEntity.status(201).body(Map.of("message", "patient created successfully!"));
            
            validationResult = 0;
        }

        if(validationResult == -1) return ResponseEntity.status(401).body(Map.of("message", "Patien already existss"));

        return ResponseEntity.internalServerError().body(Map.of("message", "An error ocurred. Please try again later!"));
    }
    
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> postMethodName(@RequestBody LoginDTO login) {   
        return service.validatePatientLogin(login);
    }
    
    @PostMapping("/{patientId}/{role}")
    public ResponseEntity<Map<String, Object>> getPatientAppointment(@PathVariable Long patientId, @PathVariable String role, @RequestHeader("Authorization") String token) {
        return patientService.getPatientAppointment(patientId, token);
    }
    
    @GetMapping("/filter/{name}/{token}")
    public ResponseEntity<Map<String, Object>> getMethodName(@PathVariable String name, @PathVariable String condition, @RequestHeader("Authorization") String token) {
      return service.filterPatient(token, condition, name);
    }
}


