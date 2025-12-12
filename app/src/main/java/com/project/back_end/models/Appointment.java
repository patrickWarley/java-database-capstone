package com.project.back_end.models;

import java.beans.Transient;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.hibernate.annotations.ManyToAny;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

@Entity
public class Appointment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @ManyToOne
  @NotNull
  private Doctor doctor;

  @ManyToOne
  @NotNull
  private Patient patient;

  @NotNull
  @Future(message = "Appointment time must be in the future!")
  private LocalDateTime appointmentTime;

  @NotNull
  private int status;

  @Transient
  private LocalDateTime getEndTime() {
	  return this.appointmentTime.plusHours(1);
  }

  @Transient
  private LocalDate getAppointmentDate() {
	return this.appointmentTime.toLocalDate();
  }

  @Transient
  private LocalTime getAppointmentTimeOnly() {
	return this.appointmentTime.toLocalTime();
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Doctor getDoctor() {
    return doctor;
  }

  public void setDoctor(Doctor doctor) {
    this.doctor = doctor;
  }

  public Patient getPatient() {
    return patient;
  }

  public void setPatient(Patient patient) {
    this.patient = patient;
  }

  public LocalDateTime getAppointmentTime() {
    return appointmentTime;
  }

  public void setAppointmentTime(LocalDateTime appointmentTime) {
    this.appointmentTime = appointmentTime;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

}

