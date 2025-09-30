package com.aman.multi_agent_task_solver.core;

import java.util.*;
import java.util.concurrent.*;


public class TaskOrchestrator {
    private final ExecutorService executor = Executors.newFixedThreadPool(4);
    private final Map<String, Agent> agentRegistry = new HashMap<>();
    private final int MAX_RETRIES = 3;

    public void registerAgent(Agent agent) {
        agentRegistry.put(agent.getName(), agent);
    }

    public Map<String, Object> run(ExecutionGraph graph) throws InterruptedException {
        Map<String, Object> results = new ConcurrentHashMap<>();
        Set<String> executed = ConcurrentHashMap.newKeySet();

        while (executed.size() < graph.getAllAgents().size()) {
            for (String agentName : graph.getAllAgents()) {
                if (executed.contains(agentName)) continue;
                List<String> deps = graph.getDependencies(agentName);
                if (!executed.containsAll(deps)) continue;

                Agent agent = agentRegistry.get(agentName);
                Future<Object> future = executor.submit(agent);

                try {
                    Object result = future.get(5, TimeUnit.SECONDS);
                    results.put(agentName, result);
                    executed.add(agentName);
                } catch (TimeoutException e) {
                    future.cancel(true);
                    retry(agent);
                } catch (Exception e) {
                    retry(agent);
                }
            }
        }
        return results;
    }

    private void retry(Agent agent) {
        for (int i = 0; i < MAX_RETRIES; i++) {
            try {
                agent.call();
                break;
            } catch (Exception ignored) {}
        }
    }
}
