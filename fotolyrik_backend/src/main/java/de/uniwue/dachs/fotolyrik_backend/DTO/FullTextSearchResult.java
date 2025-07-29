package de.uniwue.dachs.fotolyrik_backend.DTO;

public interface FullTextSearchResult {
    Long getPhotopoemId();
    String getPhotopoemTitle();
    String getQueryResult();
}
