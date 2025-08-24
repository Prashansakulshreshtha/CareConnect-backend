package com.careconnect.service;
import com.careconnect.dto.EMRDTO;
import com.careconnect.model.EMR;
import com.careconnect.repository.EMRRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class EMRService {
    @Autowired
    private EMRRepository emrRepository;
    public EMRDTO saveEMR(EMRDTO emrDTO) {
        EMR emr = new EMR();
        emr.setPatientId(emrDTO.getPatientId());
        emr.setDiagnosis(emrDTO.getDiagnosis());
        emr.setTreatment(emrDTO.getTreatment());
        emr.setNotes(emrDTO.getNotes());
        emr.setCreatedAt(emrDTO.getCreatedAt());
        EMR saved = emrRepository.save(emr);
        emrDTO.setId(saved.getId());
        return emrDTO;
    }
    public List<EMRDTO> getAllEMRs() {
        return emrRepository.findAll().stream().map(emr -> {
            EMRDTO dto = new EMRDTO();
            dto.setId(emr.getId());
            dto.setPatientId(emr.getPatientId());
            dto.setDiagnosis(emr.getDiagnosis());
            dto.setTreatment(emr.getTreatment());
            dto.setNotes(emr.getNotes());
            dto.setCreatedAt(emr.getCreatedAt());
            return dto;
        }).collect(Collectors.toList());
    }
    public EMRDTO getEMRById(Long id) {
        return emrRepository.findById(id).map(emr -> {
            EMRDTO dto = new EMRDTO();
            dto.setId(emr.getId());
            dto.setPatientId(emr.getPatientId());
            dto.setDiagnosis(emr.getDiagnosis());
            dto.setTreatment(emr.getTreatment());
            dto.setNotes(emr.getNotes());
            dto.setCreatedAt(emr.getCreatedAt());
            return dto;
        }).orElse(null);
    }
    public void deleteEMR(Long id) {
        emrRepository.deleteById(id);
    }
}
