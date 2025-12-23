package com.project.back_end.repo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.project.back_end.models.Appointment;

@Repository
public interface AppointmentRepository  extends JpaRepository<Appointment, Long>{

   public List<Appointment> findByDoctorIdAndAppointmentTimeBetween(long doctorId, LocalDateTime start, LocalDateTime end);

   public List<Appointment> findByDoctorIdAndPatient_NameContainingIgnoreCaseAndAppointmentTimeBetween(long doctorId, String patientName, LocalDateTime start, LocalDateTime end);
   
   @Modifying
   @Transactional
   public void deleteAllByDoctorId(long doctorId);
   
   public List<Appointment> findByPatientId(long patientId);
   
   public List<Appointment> findByPatient_IdAndStatusOrderByAppointmentTimeAsc(long patientId, int status);
   
   @Query("SELECT a FROM Appointment a WHERE LOWER(a.doctor.name) LIKE LOWER(CONCAT('%',:doctorName, '%')) and a.patient.id = :patientId")
   public List<Appointment> filterByDoctorNameAndPatientId(String doctorName, long patientId);


   @Query("SELECT a FROM Appointment a WHERE LOWER(a.doctor.name) LIKE LOWER(CONCAT('%',:doctorName, '%')) and a.patient.id = :patientId and a.status = :status")
   public List<Appointment> filterByDoctorNameAndPatientIdAndStatus(String doctorName, long patientId, int status);
  
   @Modifying
   @Query("UPDATE Appointment a SET a.status = :status WHERE a.id = :id")
   public void updateStatus(int status, long id);     
}
