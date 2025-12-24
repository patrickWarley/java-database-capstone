package com.project.back_end.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.objenesis.ObjenesisException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonSerializable.Base;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;
import com.project.back_end.services.BaseService;

@Service
public class AppointmentService {
  private final Logger logger = LoggerFactory.getLogger(AppointmentService.class);

  private final AppointmentRepository appointmentRepository;
  private final BaseService service;
  private final TokenService tokenService;
  private final PatientRepository patientRepository;
  private final DoctorRepository doctorRepository;

  @Autowired
  public AppointmentService(AppointmentRepository appointmentRepository, BaseService service,
      TokenService tokenService,
      PatientRepository patientRepository, DoctorRepository doctorRepository) {
    this.appointmentRepository = appointmentRepository;
    this.service = service;
    this.tokenService = tokenService;
    this.patientRepository = patientRepository;
    this.doctorRepository = doctorRepository;
  }

  @Transactional
  public int bookAppointment(Appointment appointment) {
    try {
      appointmentRepository.save(appointment);
      return 1;
    } catch (Exception e) {
      logger.error(e.getMessage());
      return 0;
    }
  }

  @Transactional
  public ResponseEntity<Map<String, String>> updateAppointment(Appointment appointment) {
    Optional<Appointment> appointmentCheck = appointmentRepository.findById(appointment.getId());

    // if the appointment exist we can to the next tests
    if (appointmentCheck.isPresent()) {
      Appointment old = appointmentCheck.get();

      if (old.getPatient().getId() != appointment.getPatient().getId()) {
        Map body = Map.of("error", "true", "message", "You need to keep the same patient!");

        return ResponseEntity.badRequest().body(body);
      }

      int validAppointment = service.validateAppointment(appointment);

      if (validAppointment > 0) {
        return ResponseEntity.ok(Map.of("error", "false", "message", "Appointment saved with success!"));
      }

      if (validAppointment == 0) {
        return ResponseEntity.badRequest().body(Map.of("error", "true", "message", "Not a valid appointment!"));
      }
      return ResponseEntity.badRequest().body(Map.of("error", "true", "message", "The doctor does not exist!"));
    }

    return ResponseEntity.badRequest().body(Map.of("error", "true", "message", "The appointment does not exist!"));
  }

  @Transactional
  public ResponseEntity<Map<String, String>> cancelAppointment(long id, String token) {
    if (tokenService.validateToken("patient", token)) {
      String email = tokenService.extractEmail(token);
      Optional<Appointment> result = appointmentRepository.findById(id);

      if (!result.isPresent())
        return ResponseEntity.badRequest()
            .body(Map.of("error", "true", "message", "The appointment does not exit!"));

      Appointment appointment = result.get();
      Patient patient = patientRepository.findByEmail(email);

      if (!(patient.getId() == appointment.getPatient().getId()))
        return ResponseEntity.badRequest().body(
            Map.of("error", "true", "message", "The loggedIn patient does not match the appointment!"));

      appointmentRepository.delete(appointment);
      return ResponseEntity.ok(Map.of("error", "false", "message", "The appointment was deleted!"));
    }

    return ResponseEntity.badRequest().body(Map.of("error", "true", "message", "Error validating the patient!"));
  }

  public Map<String, List<Appointment>> getAppointments(String pName, LocalDate date, String token) {
    if (!tokenService.validateToken("doctor", token))
      Map.of("appointments", null);

    String email = tokenService.extractEmail(token);

    Doctor doc = doctorRepository.findByEmail(email);
    List<Appointment> result = null;

    if (pName == null || pName.equals("")) {
      result = appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(doc.getId(), date.atStartOfDay(),
          date.atTime(23, 59));
    } else {
      result = appointmentRepository.findByDoctorIdAndPatient_NameContainingIgnoreCaseAndAppointmentTimeBetween(
          doc.getId(), pName, date.atStartOfDay(), date.atTime(23, 59));
    }

    return Map.of("appointments", result);
  }

  @Transactional
  public boolean changeStatus(Appointment appointment) {
    // I'm assuming that I receiving the appointment with the status changed
    // So I just save it
    try {
      appointmentRepository.save(appointment);
      return true;
    } catch (Exception e) {
      logger.error(e.getMessage());
      return false;
    }
  }
}
