package com.project.back_end.services;

import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final TokenService tokenService;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              DoctorRepository doctorRepository,
                              TokenService tokenService) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.tokenService = tokenService;
    }

    public boolean bookAppointment(Appointment appointment) {
        try {
            appointmentRepository.save(appointment);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean updateAppointment(Appointment appointment) {
        Optional<Appointment> existing = appointmentRepository.findById(appointment.getId());
        if (existing.isEmpty()) {
            return false;
        }
        try {
            appointmentRepository.save(appointment);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean cancelAppointment(Long id) {
        Optional<Appointment> existing = appointmentRepository.findById(id);
        if (existing.isEmpty()) {
            return false;
        }
        try {
            appointmentRepository.delete(existing.get());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Map<String, Object> getAppointment(String patientName, LocalDate date, String token) {
        Map<String, Object> result = new HashMap<>();

        String email = tokenService.getEmailFromToken(token);
        if (email == null) {
            result.put("message", "Invalid token");
            return result;
        }

        Doctor doctor = doctorRepository.findByEmail(email);
        if (doctor == null) {
            result.put("message", "Doctor not found");
            return result;
        }

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59);

        List<Appointment> appointments;
        if (patientName != null && !patientName.isBlank()) {
            appointments = appointmentRepository
                    .findByDoctorIdAndPatient_NameContainingIgnoreCaseAndAppointmentTimeBetween(
                            doctor.getId(),
                            patientName,
                            start,
                            end
                    );
        } else {
            appointments = appointmentRepository
                    .findByDoctorIdAndAppointmentTimeBetween(
                            doctor.getId(),
                            start,
                            end
                    );
        }

        result.put("appointments", appointments);
        return result;
    }
}
