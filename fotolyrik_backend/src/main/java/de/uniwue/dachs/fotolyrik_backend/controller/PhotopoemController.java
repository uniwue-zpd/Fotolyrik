package de.uniwue.dachs.fotolyrik_backend.controller;

import de.uniwue.dachs.fotolyrik_backend.DTO.FullTextDTO;
import de.uniwue.dachs.fotolyrik_backend.DTO.PhotopoemDTO;
import de.uniwue.dachs.fotolyrik_backend.model.FullText;
import de.uniwue.dachs.fotolyrik_backend.service.FullTextService;
import de.uniwue.dachs.fotolyrik_backend.service.PhotopoemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@RestController
@RequestMapping("/photopoems")
public class PhotopoemController {
    private final PhotopoemService photopoemService;
    private final FullTextService fullTextService;

    public PhotopoemController(PhotopoemService photopoemService, FullTextService fullTextService) {
        this.photopoemService = photopoemService;
        this.fullTextService = fullTextService;
    }

    @GetMapping
    public ResponseEntity<Page<PhotopoemDTO>> getPaginatedPhotopoems(
            Pageable pageable,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String subtitle,
            @RequestParam(required = false, value="alttitle") String altTitle,
            @RequestParam(required = false) String series,
            @RequestParam(required = false) Long volume,
            @RequestParam(required = false) Long issue,
            @RequestParam(required = false, value = "publication-date") String publicationDate,
            @RequestParam(required = false, value = "pub-medium-id") Long pubMediumId,
            @RequestParam(required = false, value = "pub-place-id") Long pubPlaceId,
            @RequestParam(required = false, value = "location-id") Long locationId,
            @RequestParam(required = false, value = "author-id") Long authorId,
            @RequestParam(required = false, value = "photographer-id") Long photographerId,
            @RequestParam(required = false, value = "depicted-person-id") Long depictedPersonId,
            @RequestParam(required = false, value = "contributor-id") Long contributorId,
            @RequestParam(required = false, value = "theme-id") Long themeId,
            @RequestParam(required = false, value = "image-motif-id") Long imageMotifId,
            @RequestParam(required = false, value = "copyright-image-id") Long copyrightStatusImageId,
            @RequestParam(required = false, value = "copyright-text-id") Long copyrightStatusTextId,
            @RequestParam(required = false, value = "language-id") Long languageId
    ) {
        Page<PhotopoemDTO> photopoems = photopoemService.getPaginatedPhotopoems(
                pageable, title, subtitle, altTitle, series, volume, issue, publicationDate,
                pubMediumId, pubPlaceId, locationId, authorId, photographerId,
                depictedPersonId, contributorId, themeId, imageMotifId, copyrightStatusImageId,
                copyrightStatusTextId, languageId
        );
        return ResponseEntity.ok(photopoems);
    }

    @GetMapping("/all")
    public ResponseEntity<List<PhotopoemDTO>> getAllPhotopoems() {
        List<PhotopoemDTO> photopoems = photopoemService.getAllPhotopoems();
        return ResponseEntity.ok(photopoems);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PhotopoemDTO> getPhotopoemById(@PathVariable Long id) {
        return photopoemService.getPhotopoemById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).build());
    }

    @GetMapping("/{id}/fulltext")
    public ResponseEntity<FullTextDTO> getFullTextByPhotopoemId(@PathVariable Long id) {
        return fullTextService.getFullTextByPhotopoemId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).build());
    }

    @GetMapping("/filter")
    public ResponseEntity<List<PhotopoemDTO>> searchPhotopoems(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String subtitle,
            @RequestParam(required = false, value = "alt-title") String altTitle,
            @RequestParam(required = false) String series,
            @RequestParam(required = false) Long volume,
            @RequestParam(required = false) Long issue,
            @RequestParam(required = false, value = "publication-date") String publicationDate,
            @RequestParam(required = false, value = "pubmedium-id") Long pubMediumId,
            @RequestParam(required = false, value = "pubmedium") String pubMedium,
            @RequestParam(required = false, value = "pubplace-id") Long pubPlaceId,
            @RequestParam(required = false, value = "location-id") Long locationId,
            @RequestParam(required = false, value = "author-id") Long authorId,
            @RequestParam(required = false) String author,
            @RequestParam(required = false, value = "photographer-id") Long photographerId,
            @RequestParam(required = false) String photographer,
            @RequestParam(required = false, value = "depicted-person-id") Long depictedPersonId,
            @RequestParam(required = false, value= "depicted-person") String depictedPerson,
            @RequestParam(required = false, value = "participant-id") Long participantId,
            @RequestParam(required = false, value = "participant") String participant,
            @RequestParam(required = false, value = "other-contributor-id") Long otherContributorId,
            @RequestParam(required = false, value = "other-contributor") String otherContributor,
            @RequestParam(required = false, value = "theme-id") Long themeId,
            @RequestParam(required = false) String theme,
            @RequestParam(required = false, value = "image-motif-id") Long imageMotifId,
            @RequestParam(required = false, value = "image-motif") String imageMotif,
            @RequestParam(required = false, value = "copyright-status-image-id") Long copyrightStatusImageId,
            @RequestParam(required = false, value = "copyright-status-image") String copyrightStatusImage,
            @RequestParam(required = false, value = "copyright-status-text-id") Long copyrightStatusTextId,
            @RequestParam(required = false, value = "copyright-status-text") String copyrightStatusText,
            @RequestParam(required = false, value = "language-id") Long languageId,
            @RequestParam(required = false) String language
    ) {
        List<Object> paramsCount = Stream.<Object>of(
                title, subtitle, altTitle, series, volume, issue, publicationDate,
                pubMediumId, pubMedium, pubPlaceId, locationId, authorId, author, photographerId,
                photographer,depictedPersonId, depictedPerson, participantId, participant, otherContributorId,
                otherContributor, themeId, theme, imageMotifId, imageMotif,
                copyrightStatusImageId, copyrightStatusImage, copyrightStatusTextId,
                copyrightStatusText, languageId, language
        )
                .filter(Objects::nonNull)
                .toList();
        if (paramsCount.isEmpty()) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(
                photopoemService.filterPhotopoems(
                        title, subtitle, altTitle, series, volume, issue, publicationDate,
                        pubMediumId, pubMedium, pubPlaceId, locationId, authorId, author, photographerId,
                        photographer,depictedPersonId, depictedPerson, participantId, participant, otherContributorId,
                        otherContributor, themeId, theme, imageMotifId, imageMotif,
                        copyrightStatusImageId, copyrightStatusImage, copyrightStatusTextId,
                        copyrightStatusText, languageId, language
                )
        );
    }

    @GetMapping("/highlight")
    public ResponseEntity<PhotopoemDTO> getMonthlyHighlight() {
        return photopoemService.getMonthlyHighlight()
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.status(404)::build);
    }

    @PostMapping
    public ResponseEntity<PhotopoemDTO> savePhotopoem(@RequestBody PhotopoemDTO photopoem) {
        PhotopoemDTO savedPhotopoem = photopoemService.createPhotopoem(photopoem);
        return ResponseEntity.status(201).body(savedPhotopoem);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PhotopoemDTO> updatePhotopoem(@PathVariable Long id, @RequestBody PhotopoemDTO photopoem) {
        try {
            PhotopoemDTO updatedPhotopoem = photopoemService.updatePhotopoem(id, photopoem);
            return ResponseEntity.ok(updatedPhotopoem);
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }

    @PutMapping("/{id}/fulltext")
    public ResponseEntity<FullText> updateFullTextByPhotopoem(@PathVariable Long id, @RequestBody FullText fullText) {
        try {
            FullText updatedFullText = fullTextService.updateFullTextByPhotopoemId(id, fullText.getFullText());
            return ResponseEntity.ok(updatedFullText);
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePhotopoem(@PathVariable Long id) {
        try {
            photopoemService.deletePhotopoem(id);
            return ResponseEntity.status(204).build();
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }
}
