package de.uniwue.dachs.fotolyrik_backend.utils.mapper;

import de.uniwue.dachs.fotolyrik_backend.DTO.visualization.graph.AdjacencyProjection;
import de.uniwue.dachs.fotolyrik_backend.DTO.visualization.graph.GraphDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class GraphMapper {
    public GraphMapper(){}
    public GraphDTO fromAdjacencyList(List<AdjacencyProjection> adjacencyList){
        Map<Long, String> nodesMap = adjacencyList.stream()
                .collect(Collectors.toMap(
                        AdjacencyProjection::getId,
                        AdjacencyProjection::getName,
                        (existing, replacement) -> existing
                ));

        Map<Long, Set<Long>> edgesMap = adjacencyList.stream()
                .collect(Collectors.toMap(
                        AdjacencyProjection::getId,
                        AdjacencyProjection::getTargets
                ));

        GraphDTO graphDTO = new GraphDTO();
        graphDTO.setNodes(nodesMap);
        graphDTO.setEdges(edgesMap);

        return graphDTO;
    }
}
