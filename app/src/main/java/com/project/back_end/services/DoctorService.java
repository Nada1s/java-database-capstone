package com.project.back_end.services;

import com.project.back_end.DTO.Login;
import com.project.back_end.models.Doctor;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final TokenService tokenService;

    public DoctorService(DoctorRepository doctorRepository,
                         AppointmentRepository appointmentRepository,
                         TokenService tokenService) {
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.tokenService = tokenService;
    }

    public List<String> getDoctorAvailability(Long doctorId, LocalDate date) {
        List<String> allSlots = Arrays.asList(
                "09:00", "10:00", "11:00", "12:00",
                "13:00", "14:00", "15:00", "16:00"
        );

        List<LocalTime> booked = appointmentRepository
                .findByDoctorIdAndAppointmentTimeBetween(
                        doctorId,
                        date.atStartOfDay(),
                        date.atTime(23, 59)
                )
                .stream()
                .map(a -> a.getAppointmentTime().toLocalTime())
                .toList();

        List<String> available = new ArrayList<>();
        for (String slot : allSlots) {
            boolean occupied = booked.stream()
                    .anyMatch(t -> t.toString().startsWith(slot));
            if (!occupied) {
                available.add(slot);
            }
        }
        return available;
    }

    public int saveDoctor(Doctor doctor) {
        if (doctorRepository.findByEmail(doctor.getEmail()) != null) {
            return -1;
        }
        try {
            doctorRepository.save(doctor);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    public int updateDoctor(Doctor doctor) {
        Optional<Doctor> existing = doctorRepository.findById(doctor.getId());
        if (existing.isEmpty()) {
            return -1;
        }
        try {
            doctorRepository.save(doctor);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    public List<Doctor> getDoctors() {
        return doctorRepository.findAll();
    }

    public int deleteDoctor(Long id) {
        Optional<Doctor> existing = doctorRepository.findById(id);
        if (existing.isEmpty()) {
            return -1;
        }
        try {
            appointmentRepository.deleteAllByDoctorId(id);
            doctorRepository.deleteById(id);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    public ResponseEntity<Map<String, String>> validateDoctor(Login login) {
        Map<String, String> response = new HashMap<>();
        Doctor doctor = doctorRepository.findByEmail(login.getIdentifier());
        if (doctor == null || !doctor.getPassword().equals(login.getPassword())) {
            response.put("message", "Invalid credentials");
            return ResponseEntity.badRequest().body(response);
        }
        String token = tokenService.generateToken(doctor.getEmail());
        response.put("token", token);
        return ResponseEntity.ok(response);
    }

    public Map<String, Object> findDoctorByName(String name) {
        Map<String, Object> result = new HashMap<>();
        List<Doctor> doctors = doctorRepository.findByNameLike("%" + name + "%");
        result.put("doctors", doctors);
        return result;
    }

    public Map<String, Object> filterDoctorsByNameSpecialtyAndTime(
            String name,
            String specialty,
            String amOrPm
    ) {
        Map<String, Object> result = new HashMap<>();

        String safeName = name == null ? "" : name;
        String safeSpecialty = specialty == null ? "" : specialty;

        List<Doctor> filtered = doctorRepository
                .findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(
                        safeName, safeSpecialty
                );

        if (amOrPm != null && !amOrPm.isBlank()) {
            filtered = filterDoctorByTime(filtered, amOrPm);
        }

        result.put("doctors", filtered);
        return result;
    }

    private List<Doctor> filterDoctorByTime(List<Doctor> doctors, String amOrPm) {
        if (amOrPm == null || amOrPm.isBlank()) {
            return doctors;
        }
        LocalDate today = LocalDate.now();
        List<Doctor> result = new ArrayList<>();
        for (Doctor d : doctors) {
            List<String> slots = getDoctorAvailability(d.getId(), today);
            boolean match = false;
            if (amOrPm.equalsIgnoreCase("AM")) {
                match = slots.stream()
                        .anyMatch(s -> Integer.parseInt(s.split(":")[0]) < 12);
            } else if (amOrPm.equalsIgnoreCase("PM")) {
                match = slots.stream()
                        .anyMatch(s -> Integer.parseInt(s.split(":")[0]) >= 12);
            }
            if (match) {
                result.add(d);
            }
        }
        return result;
    }
}
