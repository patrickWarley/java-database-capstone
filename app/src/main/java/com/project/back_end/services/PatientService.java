package com.project.back_end.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.back_end.DTO.AppointmentDTO;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.PatientRepository;

@Service
public class PatientService {
  private final Logger logger = LoggerFactory.getLogger(PatientService.class);

  private final PatientRepository patientRepository;
  private final AppointmentRepository appointmentRepository;
  private final TokenService tokenService;

  @Autowired
  public PatientService(PatientRepository patientRepository, AppointmentRepository appointmentRepository,
      TokenService tokenService) {
    this.patientRepository = patientRepository;
    this.appointmentRepository = appointmentRepository;
    this.tokenService = tokenService;
  }

  @Transactional
  public int createPatient(Patient patient) {
    try {
      patientRepository.save(patient);
      return 1;
    } catch (Exception e) {
      logger.error("Error when saving the patient! - " + e.getMessage());
      return 0;
    }
  }

  public ResponseEntity<Map<String, Object>> getPatientAppointment(long patientId, String token, String role) {
    try {

      if (!tokenService.validateToken(role, token))
        return ResponseEntity.badRequest().body(Map.of("message", "Invalid token!"));

      List<Appointment> appointments = appointmentRepository.findByPatientIdOrderByStatus(patientId);

      return ResponseEntity.ok().body(Map.of("appointments", convertToDTO(appointments)));

    } catch (Exception e) {
      logger.error("An error occurred when trying to retrieve the patient" + e.getMessage());
      return ResponseEntity.internalServerError()
          .body(Map.of("message", "An error occurred when trying to retrieve the patient"));
    }
  }

  public ResponseEntity<Map<String, Object>> filterByCondition(long patienId, String condition) {
    logger.info(condition + " LOOOOOOOO");
    try {
      List<Appointment> appointments = appointmentRepository
          .findByPatient_IdAndStatusOrderByAppointmentTimeAsc(patienId, condition == "future" ? 0 : 1);

      return ResponseEntity.ok().body(Map.of("Appointments", convertToDTO(appointments)));
    } catch (Exception e) {
      logger.error("An error occurred when trying to retrieve the list of appointments" + e.getMessage());
      return ResponseEntity.internalServerError()
          .body(Map.of("message", "Error when trying to retrieve appointment data!"));
    }
  }

  public ResponseEntity<Map<String, Object>> filterByDoctor(String doctorName, long patientId) {
    try {
      List<Appointment> appointments = appointmentRepository.filterByDoctorNameAndPatientId(doctorName,
          patientId);

      return ResponseEntity.ok().body(Map.of("appointments", convertToDTO(appointments)));
    } catch (Exception e) {
      logger.error("An error occured when trying o retrieve the list of appointments!");
      return ResponseEntity.internalServerError()
          .body(Map.of("message", "An error ocurred when trying to retrieve the list of appointments!"));
    }
  }

  public ResponseEntity<Map<String, Object>> filterByDoctorAndCondition(String doctorName, String condition,
      long patienId) {
    // condition ={"past", "future"}
    try {
      List<Appointment> appointments = condition.equals("past")
          ? appointmentRepository.filterByDoctorNameAndPatientIdAndStatus(doctorName, patienId,
              1)
          : appointmentRepository.filterByDoctorNameAndPatientIdAndStatus(doctorName, patienId,
              0);

      return ResponseEntity.ok().body(Map.of("appointments", convertToDTO(appointments)));
    } catch (Exception e) {
      return ResponseEntity.internalServerError()
          .body(Map.of("message", "An error ocurred when trying to retrieve the list of appointments!"));
    }
  }

  public ResponseEntity<Map<String, Object>> getPatientDetails(String token) {
    try {
      logger.info(token);
      if (!tokenService.validateToken("patient", token))
        return ResponseEntity.badRequest().body(Map.of("message", "Invalid token"));

      String email = tokenService.extractEmail(token);
      Patient patient = patientRepository.findByEmail(email);

      return ResponseEntity.ok().body(Map.of("patient", patient));
    } catch (Exception e) {
      logger.error("An error ocurred when trying to retrieve patient data!" + e.getMessage());

      return ResponseEntity.internalServerError()
          .body(Map.of("message", "An error ocurred when trying to retrieve patient data"));
    }
  }

  private List<AppointmentDTO> convertToDTO(List<Appointment> aList) {
    return aList.stream().map(AppointmentDTO::new).collect(Collectors.toList());
  }

}
