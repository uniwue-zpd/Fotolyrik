package de.uniwue.dachs.fotolyrik_backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @CreatedBy
    private String createdBy;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    @LastModifiedBy
    protected String lastModifiedBy;

    @Lob
    private String internalNotes;

    @Lob
    private String generalNotes;

    public void updateBaseEntityNotes(BaseEntity source) {
        generalNotes = source.getGeneralNotes();
        internalNotes = source.getInternalNotes();
    }
    public void setBaseEntityFields(BaseEntity source) {
        createdDate = source.getCreatedDate();
        createdBy = source.getCreatedBy();
        lastModifiedDate = source.getLastModifiedDate();
        lastModifiedBy = source.getLastModifiedBy();
        generalNotes = source.getGeneralNotes();
        internalNotes = source.getInternalNotes();
    }
}
