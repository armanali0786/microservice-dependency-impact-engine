package com.dependencyimpact.graphservice.dto;

import java.util.List;
import java.util.UUID;

public record GraphResponse(UUID root, List<GraphNodeDto> nodes, List<GraphEdgeDto> edges) {
}
