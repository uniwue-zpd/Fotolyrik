package de.uniwue.dachs.fotolyrik_backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "file")
@Getter
@Setter
public class File extends BaseEntity {
    private String filename;

    private String path;

    private String type;

    private Long size;
}
