package de.uniwue.dachs.fotolyrik_backend.DTO.previews;

import de.uniwue.dachs.fotolyrik_backend.DTO.FileDTO;
import de.uniwue.dachs.fotolyrik_backend.utils.enums.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class PhotopoemPreviewDTO {
    private Long id;
    private String title;
    private String altTitle;
    private String publicationDate;
    private Set<FileDTO> images = new HashSet<>();
    private AccessLevel imagesVisible = AccessLevel.INTERNAL;
}
