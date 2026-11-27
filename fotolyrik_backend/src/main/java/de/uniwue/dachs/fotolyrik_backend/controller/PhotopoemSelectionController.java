package de.uniwue.dachs.fotolyrik_backend.controller;

import de.uniwue.dachs.fotolyrik_backend.DTO.PhotopoemSelectionDTO;
import de.uniwue.dachs.fotolyrik_backend.DTO.PhotopoemSelectionRequest;
import de.uniwue.dachs.fotolyrik_backend.model.PhotopoemSelectionType;
import de.uniwue.dachs.fotolyrik_backend.service.PhotopoemSelectionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/photopoem_selection")
public class PhotopoemSelectionController {
    private final PhotopoemSelectionService photopoemSelectionService;

    public PhotopoemSelectionController(PhotopoemSelectionService photopoemSelectionService) {
        this.photopoemSelectionService = photopoemSelectionService;
    }

    @GetMapping
    public List<PhotopoemSelectionDTO> getPhotopoemSelections(@RequestParam(required = false) PhotopoemSelectionType type) {
        return type == null ? photopoemSelectionService.getAllPhotopoemSelections()
                : photopoemSelectionService.getPhotopoemSelectionsByType(type);
    }

    @GetMapping("/{id}")
    public PhotopoemSelectionDTO getPhotopoemSelectionById(@PathVariable Long id) {
        return photopoemSelectionService.getPhotopoemSelectionById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Photopoem selection not found: " + id));
    }

    @PostMapping
    public ResponseEntity<PhotopoemSelectionDTO> createPhotopoemSelection(@RequestBody PhotopoemSelectionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(photopoemSelectionService.createPhotopoemSelection(request));
    }

    @PutMapping("/{id}")
    public PhotopoemSelectionDTO updatePhotopoemSelection(@PathVariable Long id, @RequestBody PhotopoemSelectionRequest request) {
        return photopoemSelectionService.updatePhotopoemSelection(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePhotopoemSelection(@PathVariable Long id) {
        photopoemSelectionService.deletePhotopoemSelection(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ProblemDetail handleStatus(ResponseStatusException exception) {
        return ProblemDetail.forStatusAndDetail(exception.getStatusCode(), exception.getReason());
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class,
            MethodArgumentNotValidException.class})
    public ProblemDetail handleInvalidInput(Exception exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Invalid selection request or type");
    }
}
