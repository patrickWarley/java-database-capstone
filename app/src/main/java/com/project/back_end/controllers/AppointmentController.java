package com.project.back_end.controllers;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.back_end.models.Appointment;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.BaseService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

  private final AppointmentService appointmentService;
  private final BaseService baseService;
  private final Logger logger = LoggerFactory.getLogger(AppointmentController.class);

  @Autowired
  public AppointmentController(AppointmentService appointmentService, BaseService baseService) {
    this.appointmentService = appointmentService;
    this.baseService = baseService;
  }

  @GetMapping
  public ResponseEntity<Map<String, Object>> getAppointments(@RequestParam(required = false) String patientName,
      @RequestParam(required = false) String date,
      @RequestHeader("Authorization") String token) {

    String formatedToken = token.substring(7);

    ResponseEntity<Map<String, String>> formatedTokenValidation = baseService.validateToken(token.substring(7),
        "doctor");

    if (formatedTokenValidation.getStatusCode() != HttpStatusCode.valueOf(200)) {

      return ResponseEntity.status(formatedTokenValidation.getStatusCode())
          .body(Map.of("message", formatedTokenValidation.getBody().get("message")));
    }

    LocalDate convertedDate = date != null ? LocalDate.parse(date) : null;

    logger.info(patientName + " - " + convertedDate + " - " +
        formatedToken);

    List<Appointment> appointments = (List<Appointment>) appointmentService
        .getAppointments(patientName, convertedDate,
            formatedToken)
        .get("appointments");

    return ResponseEntity.ok().body(Map.of("appointments", appointments));
  }

  @PostMapping
  public ResponseEntity<Map<String, String>> bookAppointment(@RequestBody Appointment appointment,
      @RequestHeader("Authorization") String token) {

    ResponseEntity<Map<String, String>> tokenValidation = baseService.validateToken(token.substring(7), "patient");
    if (tokenValidation.getStatusCode() != HttpStatusCode.valueOf(200))
      return tokenValidation;

    int valideResult = baseService.validateAppointment(appointment);
    if (valideResult == 1) {
      if (appointmentService.bookAppointment(appointment) == 1)
        return ResponseEntity.status(201).body(Map.of("message", "Appointment booked!"));
    } else if (valideResult == 0) {
      return ResponseEntity.badRequest().body(Map.of("message", "The requested time is already booked."));
    }

    return ResponseEntity.internalServerError().body(Map.of("message", "An error ocurred. Please try again later"));
  }

  @PutMapping
  public ResponseEntity<Map<String, String>> updateAppointment(@RequestBody Appointment appointment,
      @RequestHeader("Authorization") String token) {
    ResponseEntity<Map<String, String>> tokenValidation = baseService.validateToken(token.substring(7), "patient");

    if (tokenValidation.getStatusCode() != HttpStatusCode.valueOf(200))
      return tokenValidation;

    return appointmentService.updateAppointment(appointment);
  }

  @DeleteMapping("/{appointmentId}")
  public ResponseEntity<Map<String, String>> cancelAppointment(@PathVariable Long appointmentId,
      @RequestHeader("Authorization") String token) {
    ResponseEntity<Map<String, String>> tokenValidation = baseService.validateToken(token.substring(7), "patient");

    if (tokenValidation.getStatusCode() != HttpStatusCode.valueOf(200))
      return tokenValidation;

    return appointmentService.cancelAppointment(appointmentId, token);
  }
}
