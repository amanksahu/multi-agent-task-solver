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

        for (String agentName : graph.getAllAgents()) {
            Agent agent = agentRegistry.get(agentName);
            if (agent == null) {
                results.put(agentName, "FAILED: Agent not registered");
                executed.add(agentName);
                continue;
            }

            Future<Object> future = executor.submit(agent);

            try {
                // Wait max 5s for agent to finish
                Object result = future.get(5, TimeUnit.SECONDS);
                results.put(agentName, result);
                executed.add(agentName);
            } catch (TimeoutException e) {
                future.cancel(true);
                results.put(agentName, "TIMEOUT");
                executed.add(agentName);
            } catch (Exception e) {
                results.put(agentName, "FAILED: " + e.getMessage());
                executed.add(agentName);
            }
        }

        return results;
    }

    private void retry(Agent agent) {
        for (int i = 0; i < MAX_RETRIES; i++) {
            try {
                agent.call();
                break;
            } catch (Exception ignored) {
            }
        }
    }
}
