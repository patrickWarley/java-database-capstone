package com.project.back_end.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import com.project.back_end.models.Prescription;

public interface PrescriptionRepository extends MongoRepository<Prescription, String> {
    public List<Prescription> findByAppointmentId(Long appointmentId);
}

