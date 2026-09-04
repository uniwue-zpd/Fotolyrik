package de.uniwue.dachs.fotolyrik_backend.DTO.visualization.graph;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class GraphDTO {
    Map<Long, String> nodes;
    List<List<Long>> edges;
}
