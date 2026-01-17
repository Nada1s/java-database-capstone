package com.project.back_end.services;

import com.project.back_end.models.Prescription;
import com.project.back_end.repo.PrescriptionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;

    public PrescriptionService(PrescriptionRepository prescriptionRepository) {
        this.prescriptionRepository = prescriptionRepository;
    }

    public int createPrescription(Prescription prescription) {
        try {
            prescriptionRepository.save(prescription);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    public Optional<Prescription> getPrescriptionById(String id) {
        return prescriptionRepository.findById(id);
    }

    public List<Prescription> getPrescriptionsByAppointmentId(Long appointmentId) {
        return prescriptionRepository.findByAppointmentId(appointmentId);
    }

    public int updatePrescription(Prescription prescription) {
        if (prescription.getId() == null) {
            return 0;
        }
        try {
            prescriptionRepository.save(prescription);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }
}
