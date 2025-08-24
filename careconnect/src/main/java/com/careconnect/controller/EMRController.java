package com.careconnect.controller;

import com.careconnect.dto.EMRDTO;
import com.careconnect.service.EMRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/emrs")
@CrossOrigin
public class EMRController {

    @Autowired
    private EMRService emrService;

    @PostMapping
    public EMRDTO createEMR(@RequestBody EMRDTO emrDTO) {
        return emrService.saveEMR(emrDTO);
    }

    @GetMapping
    public List<EMRDTO> getAllEMRs() {
        return emrService.getAllEMRs();
    }

    @GetMapping("/{id}")
    public EMRDTO getEMRById(@PathVariable Long id) {
        return emrService.getEMRById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteEMR(@PathVariable Long id) {
        emrService.deleteEMR(id);
    }
}
