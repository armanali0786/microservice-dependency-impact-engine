package com.dependencyimpact.impactanalysis.client;

import com.dependencyimpact.common.model.NodeType;

import java.util.UUID;

// Mirrors dependency-graph-service's GraphNodeDto - the JSON shape returned by
// GET /api/v1/graph/services/{id}/downstream. Duplicated deliberately rather than
// shared: this is an HTTP contract between two independently deployable services,
// not something that should force them to release in lockstep.
public record GraphNodeDto(UUID id, NodeType type, String name) {
}
