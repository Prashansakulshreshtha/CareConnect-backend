package com.careconnect.service;
import com.careconnect.dto.AppointmentDTO;
import com.careconnect.model.Appointment;
import com.careconnect.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    public AppointmentDTO saveAppointment(AppointmentDTO dto) {
        Appointment appointment;
        
        if (dto.getId() == null || dto.getId() == 0) {
            // New record
            appointment = convertToEntity(dto);
        } else {
            // Updating existing record: Check if it exists
            appointment = appointmentRepository.findById(dto.getId())
                    .map(existing -> {
                        // Update fields from DTO
                        existing.setAppointmentDate(dto.getAppointmentDate());
                        existing.setDoctorName(dto.getDoctorName());
                        existing.setPatientId(dto.getPatientId());
                        existing.setReason(dto.getReason());
                        return existing;
                    })
                    .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + dto.getId()));
        }

        return convertToDTO(appointmentRepository.save(appointment));
    }


    public List<AppointmentDTO> getAllAppointments() {
        return appointmentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public AppointmentDTO getAppointmentById(Long id) {
        return appointmentRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    public void deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }

    private Appointment convertToEntity(AppointmentDTO dto) {
        Appointment appt = new Appointment();
        appt.setId(dto.getId());
        appt.setPatientId(dto.getPatientId());
        appt.setAppointmentDate(dto.getAppointmentDate());
        appt.setDoctorName(dto.getDoctorName());
        appt.setReason(dto.getReason());
        return appt;
    }

    private AppointmentDTO convertToDTO(Appointment entity) {
        AppointmentDTO dto = new AppointmentDTO();
        dto.setId(entity.getId());
        dto.setPatientId(entity.getPatientId());
        dto.setAppointmentDate(entity.getAppointmentDate());
        dto.setDoctorName(entity.getDoctorName());
        dto.setReason(entity.getReason());
        return dto;
    }
}
