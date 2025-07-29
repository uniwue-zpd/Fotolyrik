package de.uniwue.dachs.fotolyrik_backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import de.uniwue.dachs.fotolyrik_backend.model.File;
import de.uniwue.dachs.fotolyrik_backend.repository.FileRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class FileService {
    private final FileRepository fileRepository;

    @Value("${config.files.upload-dir}")
    private String uploadDirValue;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    // GET
    public List<File> getFiles() {
        return fileRepository.findAll();
    }

    public Page<File> getFiles(Pageable pageable) {
        return fileRepository.findAll(pageable);
    }

    public Optional<File> getFileById(Long id) {
        return fileRepository.findById(id);
    }

    // POST
    @Transactional
    public List<File> uploadFiles(MultipartFile[] files) throws IllegalArgumentException, IOException {
        if (files == null || files.length == 0) {
            throw new IllegalArgumentException("No files where passed");
        }

        List<File> savedFiles = new ArrayList<>();
        Path uploadDir = Paths.get(uploadDirValue);

        try {
            Files.createDirectories(uploadDir);
        } catch (IOException e) {
            throw new IOException("Could not create target directory " + uploadDir, e);
        }

        for (MultipartFile file : files) {
            if (file == null || file.isEmpty()) continue;

            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) continue;

            String originalFilename = file.getOriginalFilename();
            String uuid = UUID.randomUUID() + "-" + originalFilename;
            Path targetPath = uploadDir.resolve(uuid);

            try (InputStream in = file.getInputStream()) {
                Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                continue;
            }

            File savedFile = new File();
            savedFile.setFilename(originalFilename);
            savedFile.setPath(targetPath.toString());
            savedFile.setType(contentType);
            savedFile.setSize(file.getSize());
            savedFiles.add(savedFile);
        }

        fileRepository.saveAll(savedFiles);
        return savedFiles;

    }

    // DELETE
    @Transactional
    public File deleteFileById(Long id) throws EntityNotFoundException, IOException {
        File file = fileRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("File not found with id: " + id));
        
        Path filePath = Paths.get(file.getPath());
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new IOException("Could not delete file from disk: " + filePath, e);
        }

        fileRepository.delete(file);
        return file;
    }

    @Transactional
    public Map<String, List<Long>> deleteFiles(Set<Long> ids) throws IllegalArgumentException {
        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("No files to delete");
        }

        List<Long> deletedFiles = new ArrayList<>();
        List<Long> failedFiles = new ArrayList<>();
        List<Long> notFoundFiles = new ArrayList<>();

        for (Long id : ids) {
            Optional<File> optionalFile = fileRepository.findById(id);
            if (optionalFile.isPresent()) {
                File file = optionalFile.get();
                try {
                    Files.deleteIfExists(Paths.get(file.getPath()));
                    fileRepository.delete(file);
                    deletedFiles.add(id);
                } catch (IOException e) {
                    failedFiles.add(id);
                }
            } else {
                notFoundFiles.add(id);
            }
        }

        return Map.of(
            "success", deletedFiles,
            "fail", failedFiles,
            "notFound", notFoundFiles
        );
    }
}
