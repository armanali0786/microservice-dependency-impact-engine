package com.dependencyimpact.graphservice.traversal;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "graph.traversal")
public class GraphTraversalProperties {

    private int defaultDepth = 2;
    private int maxDepth = 5;
    private int maxNodes = 500;

    public int getDefaultDepth() {
        return defaultDepth;
    }

    public void setDefaultDepth(int defaultDepth) {
        this.defaultDepth = defaultDepth;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public void setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    public int getMaxNodes() {
        return maxNodes;
    }

    public void setMaxNodes(int maxNodes) {
        this.maxNodes = maxNodes;
    }
}
