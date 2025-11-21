package de.uniwue.dachs.fotolyrik_backend.controller;

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
    public ResponseEntity<List<CopyrightStatus>> getAllCopyrightStatuses() {
        List<CopyrightStatus> copyrightStatuses = copyrightStatusService.getAllCopyrightStatuses();
        return ResponseEntity.ok().body(copyrightStatuses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CopyrightStatus> getCopyrightStatusById(@PathVariable Long id) {
        return copyrightStatusService.getCopyrightStatusById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).build());
    }

    @PostMapping
    public ResponseEntity<CopyrightStatus> createCopyrightStatus(@RequestBody CopyrightStatus copyrightStatus) {
        CopyrightStatus createdCopyrightStatus = copyrightStatusService.createCopyrightStatus(copyrightStatus);
        return ResponseEntity.status(201).body(createdCopyrightStatus);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CopyrightStatus> updateCopyrightStatus(@PathVariable Long id, @RequestBody CopyrightStatus copyrightStatus) {
        try {
            CopyrightStatus updatedCopyrightStatus = copyrightStatusService.updateCopyrightStatus(id, copyrightStatus);
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
