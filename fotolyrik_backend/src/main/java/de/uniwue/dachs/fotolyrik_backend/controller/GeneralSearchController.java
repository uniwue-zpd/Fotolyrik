package de.uniwue.dachs.fotolyrik_backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.uniwue.dachs.fotolyrik_backend.DTO.GeneralSearchDTO;
import de.uniwue.dachs.fotolyrik_backend.service.GeneralSearchService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/search")
public class GeneralSearchController {
    private final GeneralSearchService generalSearchService;

    public GeneralSearchController(GeneralSearchService generalSearchService) {
        this.generalSearchService = generalSearchService;
    }

    @GetMapping
    public ResponseEntity<List<GeneralSearchDTO>> search(@RequestParam String query) {
        if (query == null || query.isEmpty() || query.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        List<GeneralSearchDTO> result = generalSearchService.search(query);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
