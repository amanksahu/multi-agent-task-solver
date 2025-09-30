package com.aman.multi_agent_task_solver.core;

import java.util.*;

public class ExecutionGraph {
    private final Map<String, List<String>> dependencies = new HashMap<>();

    public void addDependency(String agent, String dependsOn) {
        dependencies.computeIfAbsent(agent, k -> new ArrayList<>()).add(dependsOn);
    }

    public List<String> getDependencies(String agent) {
        return dependencies.getOrDefault(agent, Collections.emptyList());
    }

    public Set<String> getAllAgents() {
        return dependencies.keySet();
    }
}