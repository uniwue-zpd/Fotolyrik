package de.uniwue.dachs.fotolyrik_backend.controller;

import de.uniwue.dachs.fotolyrik_backend.model.File;
import de.uniwue.dachs.fotolyrik_backend.service.FileService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/files")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping
    public ResponseEntity<Page<File>> getFiles(@PageableDefault(size = 20) Pageable pageable) {
        Page<File> files = fileService.getAllFiles(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(files);
    }

    @GetMapping("/{id}")
    public ResponseEntity<File> GetFileById(@PathVariable Long id) {
        return fileService.getFileById(id)
                .map(ResponseEntity.status(HttpStatus.OK)::body)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<List<File>> uploadFiles(@RequestParam("file") MultipartFile[] files) {
        try {
            List<File> uploadedFiles = fileService.uploadFiles(files);
            return ResponseEntity.status(HttpStatus.CREATED).body(uploadedFiles);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping ResponseEntity<Map<String, List<Long>>> deleteFiles (@RequestBody List<Long> ids) {
        try {
            Map<String, List<Long>> deletedFiles = fileService.deleteFiles(ids);
            return ResponseEntity.status(HttpStatus.OK).body(deletedFiles);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
