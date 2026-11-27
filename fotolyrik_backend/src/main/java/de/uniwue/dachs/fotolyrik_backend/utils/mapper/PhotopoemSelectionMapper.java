package de.uniwue.dachs.fotolyrik_backend.utils.mapper;

import de.uniwue.dachs.fotolyrik_backend.DTO.PhotopoemSelectionDTO;
import de.uniwue.dachs.fotolyrik_backend.model.PhotopoemSelection;
import org.springframework.stereotype.Component;

@Component
public class PhotopoemSelectionMapper {
    public PhotopoemSelectionDTO PhotopoemSelectionToPhotopoemSelectionDTO(PhotopoemSelection selection) {
        PhotopoemSelectionDTO photopoemSelectionDTO = new PhotopoemSelectionDTO();
        photopoemSelectionDTO.setId(selection.getId());
        photopoemSelectionDTO.setPhotopoemId(selection.getPhotopoem().getId());
        photopoemSelectionDTO.setType(selection.getType());
        return photopoemSelectionDTO;
    }
}
