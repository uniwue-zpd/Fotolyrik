package de.uniwue.dachs.fotolyrik_backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "file")
@Getter
@Setter
public class File extends BaseEntity {
    @Column(name = "filename")
    private String filename;

    @Column(name = "path")
    private String path;

    @Column(name = "type")
    private String type;

    @Column(name = "size")
    private Long size;
}
