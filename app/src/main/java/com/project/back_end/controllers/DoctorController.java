package com.project.back_end.controllers;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.aspectj.internal.lang.annotation.ajcDeclareAnnotation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.back_end.DTO.LoginDTO;
import com.project.back_end.models.Doctor;
import com.project.back_end.services.BaseService;
import com.project.back_end.services.DoctorService;

import jakarta.transaction.Transactional;
import jakarta.websocket.server.PathParam;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("${api.path}doctor")
public class DoctorController {

  private final DoctorService doctorService;
  private final BaseService service;
  private final String INTERNAL_SERVER_ERROR_MESSAGE = "An error ocurred. Please try again later!";
  private final Logger logger = LoggerFactory.getLogger(DoctorController.class);

  @Autowired
  public DoctorController(DoctorService doctorService, BaseService service) {
    this.doctorService = doctorService;
    this.service = service;
  }

  @GetMapping("/availability/{user}/{doctorId}/{date}")
  public Map<String, Object> getDoctorAvailability(@PathVariable String availability, @PathVariable String user,
      @PathVariable Long doctorId, @PathVariable Date date, @RequestHeader("Authorization") String token) {

    ResponseEntity<Map<String, String>> tokenValidation = service.validateToken(token, user);

    if (tokenValidation.getStatusCode() != HttpStatusCode.valueOf(200))
      return Map.of("error", true, "message", tokenValidation.getBody().get("Message"));

    return Map.of("doctorAvailability", doctorService.getDoctorAvailability(doctorId,
        date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()));
  }

  @GetMapping
  public ResponseEntity<Map<String, Object>> getDoctors() {
    List<Doctor> doctors = doctorService.getDoctors();

    if (doctors == null)
      return ResponseEntity.internalServerError()
          .body(Map.of("message", INTERNAL_SERVER_ERROR_MESSAGE));

    return ResponseEntity.ok().body(Map.of("doctors", doctors));
  }

  @PostMapping
  public ResponseEntity<Map<String, String>> saveDoctor(@RequestBody Doctor doctor,
      @RequestHeader("Authorization") String token) {

    String tokenFormated = token.split(" ")[1];

    ResponseEntity<Map<String, String>> tokenValidation = service.validateToken(tokenFormated, "admin");
    if (tokenValidation.getStatusCode() != HttpStatusCode.valueOf(200))
      return tokenValidation;

    return buildReponseEntity(doctorService.saveDoctor(doctor), "Doctor created sucessfully",
        "The doctor already exists!");
  }

  @PostMapping("/login")
  public ResponseEntity<Map<String, Object>> postMethodName(@RequestBody LoginDTO entity) {
    return doctorService.validateDoctor(entity);
  }

  @PutMapping("/")
  public ResponseEntity<Map<String, String>> updateDoctor(@RequestBody Doctor doctor,
      @RequestHeader("Authorization") String token) {

    ResponseEntity<Map<String, String>> tokenValidation = service.validateToken(token, "admin");
    if (tokenValidation.getStatusCode() != HttpStatusCode.valueOf(200))
      return tokenValidation;

    return buildReponseEntity(doctorService.updateDoctor(doctor), "Doctor updated with success!",
        "Doctor not found!");
  }

  @DeleteMapping("/{doctorId}")
  public ResponseEntity<Map<String, String>> deleteDoctor(@PathVariable Long doctorId,
      @RequestHeader("Authorization") String token) {
    ResponseEntity<Map<String, String>> tokenValidation = service.validateToken(token, "admin");
    if (tokenValidation.getStatusCode() != HttpStatusCode.valueOf(200))
      return tokenValidation;

    return buildReponseEntity(doctorService.deleteDoctor(doctorId), "Doctor deleted successfully!",
        "Doctor not found!");
  }

  long test = 1;

  @GetMapping("/filter")
  public Map<String, Object> filterDoctorsByNameTimespecialty(@RequestParam String name, @RequestParam String time,
      @RequestParam String specialty) {

    return doctorService.findDoctorsByNamespecialtyandTime(name, time, specialty);
  }

  private ResponseEntity<Map<String, String>> buildReponseEntity(int result, String successMessage,
      String failMessage) {
    if (result == -1)
      return ResponseEntity.status(404).body(Map.of("message", failMessage));

    if (result == 1)
      return ResponseEntity.ok().body(Map.of("message", successMessage));

    return ResponseEntity.internalServerError()
        .body(Map.of("message", INTERNAL_SERVER_ERROR_MESSAGE));
  }

}
