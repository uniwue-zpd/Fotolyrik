package de.uniwue.dachs.fotolyrik_backend.utils.mapper;

import de.uniwue.dachs.fotolyrik_backend.DTO.FileDTO;
import de.uniwue.dachs.fotolyrik_backend.model.File;
import de.uniwue.dachs.fotolyrik_backend.repository.FileRepository;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class FileMapper {
    private final FileRepository fileRepository;

    public FileMapper(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public File FileDTOToFile(FileDTO fileDTO) {
        if (fileDTO == null || fileDTO.getId() == null) return null;
        return fileRepository.findById(fileDTO.getId()).orElse(null);
    }

    public Set<File> FileDTOsToFiles(Set<FileDTO> fileDTOs) {
        if (fileDTOs.isEmpty()) return Collections.emptySet();
        return fileDTOs.stream().map(this::FileDTOToFile).collect(Collectors.toSet());
    }

    public FileDTO FileToFileDTO(File file) {
        FileDTO fileDTO = new FileDTO();
        fileDTO.setId(file.getId());
        fileDTO.setFileName(file.getFilename());
        return fileDTO;
    }

    public Set<FileDTO> FilesToFileDTOs(Set<File> files) {
        if (files.isEmpty()) return Collections.emptySet();
        return files.stream().map(this::FileToFileDTO).collect(Collectors.toSet());
    }
}
