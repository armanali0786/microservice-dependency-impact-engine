package com.dependencyimpact.graphservice.dto;

import com.dependencyimpact.common.model.NodeType;

import java.util.UUID;

public record GraphNodeDto(UUID id, NodeType type, String name) {
}
