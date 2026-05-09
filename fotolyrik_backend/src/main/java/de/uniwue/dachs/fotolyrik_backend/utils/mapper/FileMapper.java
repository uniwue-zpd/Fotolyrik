package de.uniwue.dachs.fotolyrik_backend.utils.mapper;

import de.uniwue.dachs.fotolyrik_backend.DTO.FileDTO;
import de.uniwue.dachs.fotolyrik_backend.model.File;
import de.uniwue.dachs.fotolyrik_backend.repository.FileRepository;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
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

    public FileDTO FileToFileDTO(File file) {
        if (file == null) return null;
        FileDTO fileDTO = new FileDTO();
        fileDTO.setId(file.getId());
        fileDTO.setOriginalFilename(file.getOriginalFilename());
        fileDTO.setFilename(file.getFilename());
        fileDTO.setBaseEntityFields(file);
        return fileDTO;
    }

    public Set<File> FileDTOsToFiles(Set<FileDTO> fileDTOs) {
        return MapperUtils.mapSet(fileDTOs,this::FileDTOToFile);
    }

    public Set<FileDTO> FilesToFileDTOs(Set<File> files) {
        return MapperUtils.mapSet(files,this::FileToFileDTO);
    }
    public List<File> FileDTOsToFiles(List<FileDTO> fileDTOs) {
        return MapperUtils.mapList(fileDTOs,this::FileDTOToFile);
    }

    public List<FileDTO> FilesToFileDTOs(List<File> files) {
        return MapperUtils.mapList(files,this::FileToFileDTO);
    }
}
