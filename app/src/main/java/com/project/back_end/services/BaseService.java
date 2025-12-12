package com.project.back_end.services;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.project.back_end.models.Admin;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AdminRepository;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;

@Service
public class BaseService {
    private final TokenService tokenService;
    private final AdminRepository adminRepository;
    private final DoctorService doctorService;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final PatientService patientService;
    private final AppointmentRepository appointmentRepository;

    @Autowired
    public BaseService(TokenService tokenService, AdminRepository adminRepository, DoctorService doctorService,
            DoctorRepository doctorRepository, PatientRepository patientRepository, PatientService patientService,
            AppointmentRepository appointmentRepository, String internalErrorMessage) {
        this.tokenService = tokenService;
        this.adminRepository = adminRepository;
        this.doctorService = doctorService;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.patientService = patientService;
        this.appointmentRepository = appointmentRepository;
        this.internalErrorMessage = internalErrorMessage;
    }

    private String internalErrorMessage = "An error ocurred. Please try again later!";

    public ResponseEntity<Map<String, String>> validateToken(String token, String user){
        try {
            if(tokenService.validateToken(user, token)) return ResponseEntity.ok().body(Map.of("message", "Token is valid!"));

            return ResponseEntity.status(401).body(Map.of("message", "Invalid token!"));
        } catch (Exception e) {
            System.out.println("Error when validating token! - "+e.getMessage());
            return ResponseEntity.internalServerError().body(Map.of("message", internalErrorMessage));
        }
    }

    public ResponseEntity<Map<String, String>> validateAdmin(Admin receivedAdmin){
        try {
            Admin admin = adminRepository.findByUsername(receivedAdmin.getUsername());

            if(admin != null && receivedAdmin.getPassword().equals(admin.getPassword())){
                    return ResponseEntity.ok().body(Map.of("token", tokenService.generateToken(receivedAdmin.getUsername())));
            }

            return ResponseEntity.status(401).body(Map.of("message", "Invalid credentials!"));
            
        } catch (Exception e) {
            System.out.println("Error when validating admin! - "+e.getMessage());
            return ResponseEntity.internalServerError().body(Map.of("message", internalErrorMessage));
        }
    }

    public Map<String, Object> filterDoctor(String name, String specialty, String time){
        if(name == null) name ="";

        if(!specialty.equals("") && specialty != null){
            if(time.equals("") && time != null) return Map.of("doctors", doctorService.findDoctorsByNameSpecialtyandTime(name, specialty, time));
        }else{
            if(time.equals("") && time != null) return Map.of("doctors", doctorService.filterDoctorByNameAndTime(name, time));
        }

        return Map.of("doctors",doctorService.geDoctors());   
    }

    public int validateAppointment(Appointment aAppointment){
        try {
            Optional<Doctor> doc = doctorRepository.findById(aAppointment.getDoctor().getId());

            if(!doc.isPresent()) return -1;

            return doc.get().getAvailableTimes()
                .stream()
                .anyMatch(time -> time.contains(""+aAppointment.getAppointmentTime().getHour())) ?1:0;

        } catch (Exception e) {
            System.out.println("An error ocurred when trying to validate appointment!");
            return -1;
        }
    }

    public boolean validatePatient(Patient patient){
        try {
            Patient dbPatient = patientRepository.findByEmailOrPhone(patient.getEmail(), patient.getPhone());
            
            return dbPatient !=null? false : true;
        } catch (Exception e) {
            System.out.println("An error ocurred when trying to validate the patient"+e.getMessage());
            return false;
        }
    }

    public ResponseEntity<Map<String, String>> validatePatientLogin(Patient patient){
        try {
            Patient dbPatient = patientRepository.findByEmail(patient.getEmail());

            if(dbPatient != null && dbPatient.getPassword().equals(patient.getPassword())){
                    return ResponseEntity.ok().body(Map.of("token", tokenService.generateToken(patient.getEmail())));
            }

            return ResponseEntity.status(401).body(Map.of("message", "Invalid credentials!"));
            
        } catch (Exception e) {
            System.out.println("Error when validating Patient! - "+e.getMessage());
            return ResponseEntity.internalServerError().body(Map.of("message", internalErrorMessage));
        }
    }

    public ResponseEntity<Map<String, Object>> filterPatient(String token, String condition, String doctorName){

        if(!tokenService.validateToken(token, "patient")) return ResponseEntity.status(401).body(Map.of("message", "Invalid token"));

        Patient patient = patientRepository.findByEmail(tokenService.extractEmail(token));

        if(!doctorName.equals("") && doctorName != null){
            if(!condition.equals("") && condition!= null)
                return ResponseEntity.ok().body(Map.of("appointments", patientService.filterByDoctorAndCondition(doctorName, condition, patient.getId())));
            else  return ResponseEntity.ok().body(Map.of("appointments", patientService.filterByDoctor(doctorName, patient.getId())));

        }

        return ResponseEntity.ok().body(Map.of("appointments", appointmentRepository.findByPatientId(patient.getId())));
    }
}
