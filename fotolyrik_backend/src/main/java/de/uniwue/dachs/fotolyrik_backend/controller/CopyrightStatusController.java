package de.uniwue.dachs.fotolyrik_backend.controller;

import de.uniwue.dachs.fotolyrik_backend.DTO.CopyrightStatusDTO;
import de.uniwue.dachs.fotolyrik_backend.model.CopyrightStatus;
import de.uniwue.dachs.fotolyrik_backend.service.CopyrightStatusService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/copyright_statuses")
public class CopyrightStatusController {
    private final CopyrightStatusService copyrightStatusService;

    public CopyrightStatusController(CopyrightStatusService copyrightStatusService) {
        this.copyrightStatusService = copyrightStatusService;
    }

    @GetMapping
    public ResponseEntity<List<CopyrightStatusDTO>> getAllCopyrightStatuses() {
        List<CopyrightStatusDTO> copyrightStatuses = copyrightStatusService.getAllCopyrightStatuses();
        return ResponseEntity.ok().body(copyrightStatuses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CopyrightStatusDTO> getCopyrightStatusById(@PathVariable Long id) {
        return copyrightStatusService.getCopyrightStatusById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).build());
    }

    @PostMapping
    public ResponseEntity<CopyrightStatusDTO> createCopyrightStatus(@RequestBody CopyrightStatusDTO copyrightStatusDTO) {
        CopyrightStatusDTO created = copyrightStatusService.createCopyrightStatus(copyrightStatusDTO);
        return ResponseEntity.status(201).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CopyrightStatusDTO> updateCopyrightStatus(@PathVariable Long id, @RequestBody CopyrightStatusDTO copyrightStatusDTO) {
        try {
            CopyrightStatusDTO updatedCopyrightStatus = copyrightStatusService.updateCopyrightStatus(id, copyrightStatusDTO);
            return ResponseEntity.status(201).body(updatedCopyrightStatus);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCopyrightStatus(@PathVariable Long id) {
        try {
            copyrightStatusService.deleteCopyrightStatusById(id);
            return ResponseEntity.status(204).build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).build();
        }
    }
}
