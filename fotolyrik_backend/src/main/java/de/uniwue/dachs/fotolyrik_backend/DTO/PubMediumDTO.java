package de.uniwue.dachs.fotolyrik_backend.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import de.uniwue.dachs.fotolyrik_backend.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.Year;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class PubMediumDTO extends BaseEntity {
    private String title;
    private String subtitle;
    private Set<PlacePreviewDTO> publicationPlaces = new HashSet<>();
    private PublisherDTO publisher;
    private Set<PublicationRhythmDTO> pubRhythms = new HashSet<>();
    private String editorialOffice;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy")
    private Year startYear;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy")
    private Year endYear;

    private Long amountVolumes;
    private Long amountIssues;
    private String zdbId;
    private String notes;
}
