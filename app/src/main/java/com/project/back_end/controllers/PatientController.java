package com.project.back_end.controllers;

import com.project.back_end.DTO.Login;
import com.project.back_end.models.Patient;
import com.project.back_end.services.PatientService;
import com.project.back_end.services.Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("${api.path}patient")
public class PatientController {

    private final PatientService patientService;
    private final Service service;

    public PatientController(PatientService patientService, Service service) {
        this.patientService = patientService;
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> createPatient(@RequestBody Patient patient) {
        boolean isNew = service.validatePatient(patient);
        if (!isNew) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Patient already exists with this email or phone"));
        }

        int result = patientService.createPatient(patient);
        if (result == 1) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "Patient created successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error creating patient"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> patientLogin(@RequestBody Login login) {
        return service.validatePatientLogin(login);
    }

    @GetMapping("/appointments/{id}")
    public ResponseEntity<?> getPatientAppointments(
            @PathVariable Long id,
            @RequestParam String token
    ) {
        return patientService.getPatientAppointment(id, token);
    }

    @GetMapping("/appointments/filter")
    public ResponseEntity<?> filterPatientAppointments(
            @RequestParam String condition,
            @RequestParam(required = false) String name,
            @RequestParam String token
    ) {
        return service.filterPatient(condition, name, token);
    }

    @GetMapping("/details")
    public ResponseEntity<?> getPatientDetails(@RequestParam String token) {
        return patientService.getPatientDetails(token);
    }
}
