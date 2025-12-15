package com.project.back_end.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.misc.ObjectEqualityComparator;
import org.apache.logging.log4j.spi.ObjectThreadContextMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.back_end.DTO.Login;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;

@Service
public class DoctorService {

  private final DoctorRepository doctorRepository;
  private final AppointmentRepository appointmentRepository;
  private final TokenService tokenService;

  @Autowired
  public DoctorService(DoctorRepository doctorRepository, AppointmentRepository appointmentRepository,
      TokenService tokenService) {
    this.doctorRepository = doctorRepository;
    this.appointmentRepository = appointmentRepository;
    this.tokenService = tokenService;
  }

  // 4. **getDoctorAvailability Method**:
  // - Retrieves the available time slots for a specific doctor on a particular
  // date and filters out already booked slots.
  // - The method fetches all appointments for the doctor on the given date and
  // calculates the availability by comparing against booked slots.
  // - Instruction: Ensure that the time slots are properly formatted and the
  // available slots are correctly filtered.
  @Transactional
  public List<String> getDoctorAvailability(long doctorId, LocalDate date) {
    Optional<Doctor> doc = doctorRepository.findById(doctorId);

    if (!doc.isPresent())
      return null;

    Doctor doctor = doc.get();
    List<Appointment> appointments = appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(doctorId,
        date.atStartOfDay(), date.atTime(23, 59));

    if (appointments.isEmpty())
      return doctor.getAvailableTimes();

    return doctor.getAvailableTimes().stream().filter(time -> {
      for (Appointment appointment : appointments) {
        LocalDateTime start = appointment.getAppointmentTime();
        String aux = "" + start.getHour() + ":" + start.getMinute() + "-" + (start.getHour() + 1) + ":"
            + start.getMinute();

        if (time.equals(aux))
          return false;
      }

      return true;
    }).collect(Collectors.toList());
  }

  @Transactional
  public int saveDoctor(Doctor doctor) {
    try {
      Doctor sameEmailDoc = doctorRepository.findByEmail(doctor.getEmail());
      if (sameEmailDoc != null)
        return -1;

      doctorRepository.save(doctor);
      return 1;
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return 0;
    }
  }

  @Transactional
  public int updateDoctor(Doctor doctor) {
    try {
      Optional<Doctor> doctorDb = doctorRepository.findById(doctor.getId());

      if (!doctorDb.isPresent())
        return -1;

      doctorRepository.save(doctor);

      return 1;
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return 0;
    }
  }

  @Transactional
  public List<Doctor> geDoctors() {
    try {
      return doctorRepository.findAll();
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return null;
    }
  }

  @Transactional
  public int deleteDoctor(long doctorId) {
    try {
      Optional<Doctor> docToDelete = doctorRepository.findById(doctorId);

      if (!docToDelete.isPresent())
        return -1;

      doctorRepository.delete(docToDelete.get());
      appointmentRepository.deleteAllByDoctorId(doctorId);
      return 1;

    } catch (Exception e) {
      System.out.println(e.getMessage());
      return 0;
    }
  }

  public ResponseEntity<Map<String, Object>> validateDoctor(Login login) {
    try {
      Doctor doctor = doctorRepository.findByEmail(login.getIdentifier());
      Map<String, Object> invalid = Map.of("error", true, "token", null, "message", "Invalid credentials!");

      if (doctor == null)
        return ResponseEntity.badRequest().body(invalid);

      if (!login.getPassword().equals(doctor.getPassword()))
        return ResponseEntity.badRequest().body(invalid);

      String token = tokenService.generateToken(doctor.getEmail(), "doctor");
      return ResponseEntity.ok().body(Map.of("error", false, "token", token));

    } catch (Exception e) {
      System.out.println(e.getMessage());
      return ResponseEntity.badRequest()
          .body(Map.of("error", true, "token", null, "message", "An internal error ocurred. Try again later!"));
    }
  }

  @Transactional
  public List<Doctor> findDoctorByName(String name) {
    try {
      return doctorRepository.findByNameLike(name);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return null;
    }
  }

  public Map<String, Object> findDoctorsByNameSpecialtyandTime(String name, String specialty, String amPm) {
    try {
      List<Doctor> doctors = doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, specialty);

      return Map.of("doctors",
          doctors.stream().filter(doc -> doc.getAvailableTimes().stream().anyMatch(time -> time.contains(amPm)))
              .collect(Collectors.toList()));

    } catch (Exception e) {
      System.out.println(e.getMessage());
      return Map.of("error", null, "message", "An error ocurred try again later!");
    }
  }

  public Map<String, Object> filterDoctorByTime(String amPm) {
    try {
      List<Doctor> doctors = doctorRepository.findAll();
      return Map.of("doctors",
          doctors.stream().filter(doc -> doc.getAvailableTimes().stream().anyMatch(time -> time.contains(amPm)))
              .collect(Collectors.toList()));
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return Map.of("error", null, "message", "An error ocurred try again later!");
    }
  }

  public Map<String, Object> filterDoctorByNameAndTime(String name, String amPm) {
    List<Doctor> doctors = doctorRepository.findByNameLike(name);

    return Map.of("doctors",
        doctors.stream().filter(doc -> doc.getAvailableTimes().stream().anyMatch(time -> time.contains(amPm)))
            .collect(Collectors.toList()));
  }

  public Map<String, Object> filterDoctorByNameAndSpeciality(String name, String specialty) {
    try {
      return Map.of("Doctors", doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, specialty));
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return Map.of("error", null, "message", "An error ocurred try again later!");
    }
  }

  public Map<String, Object> filterDoctorByTimeAndSpecialty(String specialty, String amPm) {
    try {
      List<Doctor> doctors = doctorRepository.findAll();
      return Map.of("doctors",
          doctors.stream().filter(doc -> doc.getAvailableTimes().stream().anyMatch(time -> time.contains(amPm)))
              .collect(Collectors.toList()));
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return Map.of("error", null, "message", "An error ocurred try again later!");
    }
  }

  public Map<String, Object> filterDoctorBySpecialty(String specialty) {
    try {
      return Map.of("Doctors", doctorRepository.findBySpecialtyIgnoreCase(specialty));
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return Map.of("error", null, "message", "An error ocurred try again later!");
    }
  }
}
