package de.uniwue.dachs.fotolyrik_backend.DTO.visualization.graph;

import java.util.Set;

public interface AdjacencyProjection {
    Long getId();
    String getName();
    Set<Long> getTargets();
}
