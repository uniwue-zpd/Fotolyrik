package de.uniwue.dachs.fotolyrik_backend.DTO.visualization.graph;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
public class GraphDTO {
    Map<Long, String> nodes;
    Map<Long, Set<Long>> edges;
}
