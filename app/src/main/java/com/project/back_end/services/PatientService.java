package com.project.back_end.services;

import com.project.back_end.DTO.AppointmentDTO;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.PatientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final TokenService tokenService;

    public PatientService(PatientRepository patientRepository,
                          AppointmentRepository appointmentRepository,
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
            e.printStackTrace();
            return 0;
        }
    }

    @Transactional
    public ResponseEntity<Map<String, Object>> getPatientAppointment(Long id, String token) {
        Map<String, Object> response = new HashMap<>();

        String emailFromToken = tokenService.extractIdentifier(token);
        Patient patient = patientRepository.findByEmail(emailFromToken);

        if (patient == null || !patient.getId().equals(id)) {
            response.put("message", "Unauthorized access");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        List<AppointmentDTO> appointments = appointmentRepository.findByPatientId(id)
                .stream()
                .map(AppointmentDTO::new)
                .collect(Collectors.toList());

        response.put("appointments", appointments);
        return ResponseEntity.ok(response);
    }

    @Transactional
    public ResponseEntity<Map<String, Object>> filterByCondition(String condition, Long patientId) {
        Map<String, Object> response = new HashMap<>();
        List<Appointment> allAppointments = appointmentRepository.findByPatientId(patientId);
        List<AppointmentDTO> filtered;
        LocalDateTime now = LocalDateTime.now();

        switch (condition.toLowerCase()) {
            case "past":
                filtered = allAppointments.stream()
                        .filter(a -> a.getAppointmentTime().isBefore(now))
                        .map(AppointmentDTO::new)
                        .collect(Collectors.toList());
                break;
            case "future":
                filtered = allAppointments.stream()
                        .filter(a -> a.getAppointmentTime().isAfter(now))
                        .map(AppointmentDTO::new)
                        .collect(Collectors.toList());
                break;
            default:
                response.put("message", "Invalid condition: use 'past' or 'future'");
                return ResponseEntity.badRequest().body(response);
        }

        response.put("appointments", filtered);
        return ResponseEntity.ok(response);
    }

    @Transactional
    public ResponseEntity<Map<String, Object>> filterByDoctor(String name, Long patientId) {
        Map<String, Object> response = new HashMap<>();

        List<AppointmentDTO> filtered = appointmentRepository.findByPatientId(patientId)
                .stream()
                .filter(a -> a.getDoctor().getName().toLowerCase().contains(name.toLowerCase()))
                .map(AppointmentDTO::new)
                .collect(Collectors.toList());

        response.put("appointments", filtered);
        return ResponseEntity.ok(response);
    }

    @Transactional
    public ResponseEntity<Map<String, Object>> filterByDoctorAndCondition(String condition,
                                                                          String name,
                                                                          long patientId) {
        Map<String, Object> response = new HashMap<>();
        List<Appointment> allAppointments = appointmentRepository.findByPatientId(patientId);
        LocalDateTime now = LocalDateTime.now();

        List<AppointmentDTO> filtered = allAppointments.stream()
                .filter(a -> a.getDoctor().getName().toLowerCase().contains(name.toLowerCase()))
                .filter(a -> {
                    if (condition.equalsIgnoreCase("past")) {
                        return a.getAppointmentTime().isBefore(now);
                    }
                    if (condition.equalsIgnoreCase("future")) {
                        return a.getAppointmentTime().isAfter(now);
                    }
                    return false;
                })
                .map(AppointmentDTO::new)
                .collect(Collectors.toList());

        response.put("appointments", filtered);
        return ResponseEntity.ok(response);
    }

    @Transactional
    public ResponseEntity<Map<String, Object>> getPatientDetails(String token) {
        Map<String, Object> response = new HashMap<>();
        String email = tokenService.extractIdentifier(token);
        Patient patient = patientRepository.findByEmail(email);

        if (patient == null) {
            response.put("message", "Patient not found");
            return ResponseEntity.badRequest().body(response);
        }

        response.put("patient", patient);
        return ResponseEntity.ok(response);
    }

    @Transactional
    public ResponseEntity<Map<String, Object>> filterByConditionOrDoctor(String condition,
                                                                         String name,
                                                                         String email) {
        Map<String, Object> response = new HashMap<>();
        Patient patient = patientRepository.findByEmail(email);

        if (patient == null) {
            response.put("message", "Patient not found");
            return ResponseEntity.badRequest().body(response);
        }

        Long patientId = patient.getId();

        if (condition != null && !condition.isBlank()
                && (name == null || name.isBlank())) {
            return filterByCondition(condition, patientId);
        } else if ((condition == null || condition.isBlank())
                && name != null && !name.isBlank()) {
            return filterByDoctor(name, patientId);
        } else if (condition != null && !condition.isBlank()
                && name != null && !name.isBlank()) {
            return filterByDoctorAndCondition(condition, name, patientId);
        } else {
            return getPatientAppointment(patientId, tokenService.generateToken(email));
        }
    }
}
