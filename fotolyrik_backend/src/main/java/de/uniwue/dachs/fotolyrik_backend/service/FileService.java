package de.uniwue.dachs.fotolyrik_backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import de.uniwue.dachs.fotolyrik_backend.model.File;
import de.uniwue.dachs.fotolyrik_backend.repository.FileRepository;

@Service
public class FileService {
    private final FileRepository fileRepository;

    @Value("${config.files.upload-dir}")
    private String uploadDirValue;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    // GET all files with pagination
    public Page<File> getAllFiles(Pageable pageable) {
        return fileRepository.findAll(pageable);
    }

    // GET all files without pagination
    public List<File> getAllFiles() {
        return fileRepository.findAll();
    }

    // GET file by ID
    public Optional<File> getFileById(Long id) {
        return fileRepository.findById(id);
    }

    // POST upload file(s)
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
            throw new IOException("Could not create target directory " + uploadDir.toString());
        }

        for (MultipartFile file : files) {
            if (file == null || file.isEmpty()) continue;
            
            String contentType = file.getContentType();
            if (contentType == null || !(contentType.startsWith("image/"))) {
                continue;
            }

            String uuid = UUID.randomUUID() + "-" + file.getOriginalFilename();
            Path targetPath = uploadDir.resolve(uuid);
            try {
                Files.write(targetPath, file.getBytes());
            } catch (IOException e) {
                //throw new IOException("Could not save file " + targetPath.toString());
                continue;
            }
            File savedFile = new File();
            savedFile.setFilename(file.getOriginalFilename());
            savedFile.setPath(targetPath.toString());
            savedFile.setType(contentType);
            savedFile.setSize(file.getSize());
            fileRepository.save(savedFile);
            savedFiles.add(savedFile);
        }

        return savedFiles;
    }

    // DELETE delete file(s)
    @Transactional
    public Map<String, List<Long>> deleteFiles(List<Long> ids) throws IllegalArgumentException {
        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("No files to delete");
        }

        List<Long> deletedFiles = new ArrayList<>();
        List<Long> failedFiles = new ArrayList<>();

        ids.forEach(id -> {
            fileRepository.findById(id).ifPresentOrElse(file -> {
                try {
                    Files.deleteIfExists(Paths.get(file.getPath()));
                    fileRepository.delete(file);
                    deletedFiles.add(id);
                } catch (IOException e) {
                    failedFiles.add(id);
                }
            }, () -> {
                deletedFiles.add(id);  // remove file from frontend if missing in db
            });
        });

        return Map.of("success", deletedFiles, "fail", failedFiles);
    }
}