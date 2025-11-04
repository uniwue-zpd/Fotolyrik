package de.uniwue.dachs.fotolyrik_backend.utils.mapper;

import de.uniwue.dachs.fotolyrik_backend.DTO.FullTextDTO;
import de.uniwue.dachs.fotolyrik_backend.model.FullText;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class FullTextMapper {
    private final PhotopoemMapper photopoemMapper;

    public FullTextMapper(PhotopoemMapper photopoemMapper) {
        this.photopoemMapper = photopoemMapper;
    }

    public FullText FullTextDTOToFullText(FullTextDTO fullTextDTO) {
        if (fullTextDTO.getPhotopoem() == null) {
            throw new EntityNotFoundException("Fulltext must be associated with an existing photopoem");
        }
        FullText fullText = new FullText();
        fullText.setPhotopoem(photopoemMapper.PhotopoemPreviewDTOToPhotopoem(fullTextDTO.getPhotopoem()));
        fullText.setFullText(fullTextDTO.getFullText());
        return fullText;
    }

    public FullTextDTO FulltextToFullTextDTO(FullText fullText) {
        FullTextDTO fullTextDTO = new FullTextDTO();
        fullTextDTO.setId(fullText.getId());
        fullTextDTO.setPhotopoem(photopoemMapper.PhotopoemToPreviewDTO(fullText.getPhotopoem()));
        fullTextDTO.setFullText(fullText.getFullText());
        return fullTextDTO;
    }
}
