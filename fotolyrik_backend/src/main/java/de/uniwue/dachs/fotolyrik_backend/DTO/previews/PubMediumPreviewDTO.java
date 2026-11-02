package de.uniwue.dachs.fotolyrik_backend.DTO.previews;

import java.time.Year;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PubMediumPreviewDTO {
    private Long id;
    private String title;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy")
    private Year startYear;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy")
    private Year endYear;
}
