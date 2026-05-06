package de.uniwue.dachs.fotolyrik_backend.service;

import de.uniwue.dachs.fotolyrik_backend.DTO.FileDTO;
import de.uniwue.dachs.fotolyrik_backend.utils.mapper.FileMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
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
    private final FileMapper fileMapper;

    @Value("${config.files.upload-dir}")
    private String uploadDirValue;

    public FileService(FileRepository fileRepository, FileMapper fileMapper) {
        this.fileRepository = fileRepository;
        this.fileMapper = fileMapper;
    }

    /**
     * GET returns all file metadata entries in the database
     * @return {@link List} of all {@link File} entries in the database as {@link FileDTO}
     */
    public List<FileDTO> getFiles() {
        return fileMapper.FilesToFileDTOs(fileRepository.findAll());
    }

    /**
     * GET returns a paginated list of file metadata entries in the database
     * @param pageable pagination information (page number, page size, sorting)
     * @return {@link Page} of {@link File} represented as {@link FileDTO} entries in the database according to the pagination information
     */
    public Page<FileDTO> getFiles(Pageable pageable) {
        Page<File> filePage = fileRepository.findAll(pageable);
        return filePage.map(fileMapper::FileToFileDTO);
    }

    /**
     * GET returns the file metadata entry with the given id
     * @param id ID of the file metadata entry to return
     * @return {@link Optional} containing the {@link File} entry with the given id, or an empty {@link Optional} if no such entry exists
     */
    public Optional<FileDTO> getFileById(Long id) {
        return  fileRepository.findById(id).map(fileMapper::FileToFileDTO);
    }

    /**
     * POST uploads one or more files, saves them to the file system and creates corresponding metadata entries in the database. Only image files are accepted.
     * @param files array of files to upload
     * @return {@link List} of {@link File} entries corresponding to the uploaded files
     * @throws IllegalArgumentException if no files were passed or if any of the files is not an image
     * @throws IOException if there was an error saving any of the files to the file system
     */
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
            savedFile.setOriginalFilename(originalFilename);
            savedFile.setFilename(uuid);
            savedFile.setPath(targetPath.toString());
            savedFile.setType(contentType);
            savedFile.setSize(file.getSize());
            savedFiles.add(savedFile);
        }

        fileRepository.saveAll(savedFiles);
        return savedFiles;

    }

    /**
     * DELETE deletes the file metadata entry with the given id from the database and deletes the corresponding file from the file system
     * @param id ID of the file metadata entry to delete
     * @return the deleted {@link File} entry
     * @throws EntityNotFoundException if no file metadata entry with the given id exists
     * @throws IOException if there was an error deleting the file from the file system
     */
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

    /**
     * DELETE deletes the file metadata entries with the given ids from the database and deletes the corresponding files from the file system. If any of the given ids does not correspond to an existing file metadata entry, it is ignored. If there is an error deleting any of the files from the file system, the corresponding id is added to the list of failed deletions, but the method continues trying to delete the remaining files.
     * @param ids IDs of the file metadata entries to delete
     * @return a {@link Map} containing three entries: "success" with a list of the ids of the successfully deleted files, "fail" with a list of the ids of the files that could not be deleted from the file system, and "notFound" with a list of the ids that did not correspond to any existing file metadata entry
     * @throws IllegalArgumentException if no ids were passed
     */
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

    /**
     * GET returns the content of the file with the given id as a {@link Resource}
     * @param id ID of the file to return
     * @return a {@link Resource} containing the content of the file with the given id
     * @throws EntityNotFoundException if no file metadata entry with the given id exists
     * @throws RuntimeException if the file exists but cannot be read, or if the file path is invalid
     */
    public Resource getFileContent(Long id) {
        File file = fileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("File not found with id: " + id));
        try {
            Path filePath = Paths.get(file.getPath()).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                throw new RuntimeException("File exists but cannot be read: " + filePath);
            }
            return resource;
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid file path: " + file.getPath(), e);
        }
    }
}
