package com.project.back_end.controllers;

import com.project.back_end.models.Prescription;
import com.project.back_end.services.PrescriptionService;
import com.project.back_end.services.Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${api.path}prescriptions")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;
    private final Service service;

    public PrescriptionController(PrescriptionService prescriptionService, Service service) {
        this.prescriptionService = prescriptionService;
        this.service = service;
    }

    @PostMapping("/{token}")
    public ResponseEntity<?> createPrescription(
            @RequestBody Prescription prescription,
            @PathVariable String token
    ) {
        if (!service.validateToken(token, "doctor")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid or expired token"));
        }

        int result = prescriptionService.createPrescription(prescription);
        if (result == 1) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "Prescription created successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error creating prescription"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPrescriptionById(@PathVariable String id) {
        return prescriptionService.getPrescriptionById(id)
                .<ResponseEntity<?>>map(prescription -> ResponseEntity.ok(prescription))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Prescription not found")));
    }

    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<?> getPrescriptionsByAppointment(@PathVariable Long appointmentId) {
        List<Prescription> prescriptions =
                prescriptionService.getPrescriptionsByAppointmentId(appointmentId);
        return ResponseEntity.ok(Map.of("prescriptions", prescriptions));
    }

    @PutMapping("/{token}")
    public ResponseEntity<?> updatePrescription(
            @RequestBody Prescription prescription,
            @PathVariable String token
    ) {
        if (!service.validateToken(token, "doctor")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid or expired token"));
        }

        int result = prescriptionService.updatePrescription(prescription);
        if (result == 1) {
            return ResponseEntity.ok(Map.of("message", "Prescription updated successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error updating prescription"));
        }
    }
}
