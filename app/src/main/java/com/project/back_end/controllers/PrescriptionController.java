package com.project.back_end.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.project.back_end.models.Prescription;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.BaseService;
import com.project.back_end.services.PrescriptionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
@RequestMapping("${api.path}prescription")
public class PrescriptionController {
    
    private final PrescriptionService prescriptionService;
    private final BaseService service;
    private final AppointmentService appointmentService;

    @Autowired
    public PrescriptionController(PrescriptionService prescriptionService, BaseService service,
            AppointmentService appointmentService) {
        this.prescriptionService = prescriptionService;
        this.service = service;
        this.appointmentService = appointmentService;
    }

   @PostMapping("/")
   public ResponseEntity<Map<String, String>> savePrescription(@RequestBody Prescription aPrescription, @RequestHeader("Authorization") String token) {
        ResponseEntity<Map<String, String>> testToken = service.validateToken(token, "doctor");

        if(testToken.getStatusCode().value() != 200)
            return testToken;

        return prescriptionService.savePrescription(aPrescription);
   }
 
   @GetMapping("/{appointmentId}")
   public ResponseEntity<Map<String, Object>> getPrescriptiion(@PathVariable Long appointmentId, @RequestParam("Authorization") String token) {
      ResponseEntity<Map<String, String>> testToken = service.validateToken(token, "doctor");

        if(testToken.getStatusCode().value() != 200)
            return ResponseEntity.badRequest().body(Map.of("message", "An Error ocurred when validating the token"));

        return prescriptionService.getPrescription(appointmentId);
   }
}
