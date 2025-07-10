package de.uniwue.dachs.fotolyrik_backend.DTO;

public interface FullTextSearchResult {
    Long getPhotopoem_id();
    String getPhotopoem_title();
    String getQuery_result();
}
