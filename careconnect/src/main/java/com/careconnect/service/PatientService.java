package com.careconnect.service;

import com.careconnect.dto.PatientDTO;
import com.careconnect.model.Patient;
import com.careconnect.repository.PatientRepository;
import com.careconnect.exception.DuplicateResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    public PatientDTO savePatient(PatientDTO dto) {
        if (patientRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResource("email", "Email already exists");
        }
        if (patientRepository.existsByPhone(dto.getPhone())) {
            throw new DuplicateResource("phone", "Phone number already exists");
        }

        Patient patient = mapToEntity(dto);
        Patient saved = patientRepository.save(patient);
        return mapToDTO(saved);
    }

    public List<PatientDTO> getAllPatients() {
        return patientRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public PatientDTO getPatientById(Long id) {
        return patientRepository.findById(id)
                .map(this::mapToDTO)
                .orElse(null);
    }

    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }

    private PatientDTO mapToDTO(Patient patient) {
        PatientDTO dto = new PatientDTO();
        dto.setId(patient.getId());
        dto.setName(patient.getFullName());
        dto.setEmail(patient.getEmail());
        dto.setPhone(patient.getPhone());
        dto.setGender(patient.getGender());
        dto.setAge(patient.getAge());
        dto.setAddress(patient.getAddress());
        return dto;
    }

    private Patient mapToEntity(PatientDTO dto) {
        Patient patient = new Patient();
        patient.setId(dto.getId());
        patient.setFullName(dto.getName());
        patient.setEmail(dto.getEmail());
        patient.setPhone(dto.getPhone());
        patient.setGender(dto.getGender());
        patient.setAge(dto.getAge());
        patient.setAddress(dto.getAddress());
        return patient;
    }
}
