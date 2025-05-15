package de.uniwue.dachs.fotolyrik_backend.controller;

import de.uniwue.dachs.fotolyrik_backend.model.File;
import de.uniwue.dachs.fotolyrik_backend.repository.FileRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/files")
public class FileController {
    private final FileRepository fileRepository;

    public FileController(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @GetMapping
    public ResponseEntity<List<File>> getFiles() {
        List<File> files = fileRepository.findAll();
        return ResponseEntity.ok(files);
    }

    @GetMapping("/{id}")
    public ResponseEntity<File> getFile(@PathVariable Long id) {
        return fileRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).build());
    }

    @PostMapping
    public ResponseEntity<File> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
            Path path = Paths.get("/uploads/", file.getOriginalFilename());
            Files.createDirectories(path.getParent());
            Files.write(path, file.getBytes());

            File savedFile = new File();
            savedFile.setFilename(file.getOriginalFilename());
            savedFile.setPath(path.toString());
            savedFile.setType(file.getContentType());
            savedFile.setSize(file.getSize());
            fileRepository.save(savedFile);
            return ResponseEntity.status(201).body(savedFile);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long id) {
        Optional<File> file = fileRepository.findById(id);
        if (file.isEmpty()) {
            return ResponseEntity.status(404).build();
        }
        File fileToDelete = file.get();
        Path filepath = Paths.get(fileToDelete.getPath());
        try {
            Files.deleteIfExists(filepath);
        } catch (IOException e) {
            return ResponseEntity.status(500).build();
        }
        fileRepository.deleteById(id);
        return ResponseEntity.status(204).build();
    }
}
