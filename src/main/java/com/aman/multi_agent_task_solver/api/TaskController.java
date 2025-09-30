package com.aman.multi_agent_task_solver.api;


import com.aman.multi_agent_task_solver.agents.*;
import com.aman.multi_agent_task_solver.core.*;
import com.aman.multi_agent_task_solver.storage.TaskRepository;
import com.aman.multi_agent_task_solver.tools.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskRepository repo = new TaskRepository();

    @PostMapping
    public String createTask() {
        String taskId = UUID.randomUUID().toString();
        repo.save(taskId, Map.of("status", "RUNNING"));
        log.info("Task {} created. Status set to RUNNING", taskId);

        // Run orchestration in background
        CompletableFuture.runAsync(() -> {
            try {
                log.info("Task {} started execution", taskId);

                // Build execution graph: chartGenerator depends on dataFetcher
                ExecutionGraph graph = new ExecutionGraph();
                graph.addDependency("chartGenerator", "dataFetcher");

                // Create agents
                DataFetcherAgent fetcher = new DataFetcherAgent(new DummyDataFetcher());
                fetcher.setInput("query=demo");
                log.info("DataFetcherAgent initialized with input: query=demo");

                ChartGeneratorAgent chart = new ChartGeneratorAgent(new DummyChartGenerator());

                // Orchestrator
                TaskOrchestrator orchestrator = new TaskOrchestrator();
                orchestrator.registerAgent(fetcher);
                orchestrator.registerAgent(chart);

                // Run agents
                Map<String, Object> result = orchestrator.run(graph);

                // Pass DataFetcher output as input to ChartGenerator
                if (result.containsKey("dataFetcher")) {
                    Object fetchOutput = result.get("dataFetcher");
                    log.info("DataFetcher completed with output: {}", fetchOutput);

                    chart.setInput(fetchOutput);
                    Object chartResult = chart.call();
                    result.put("chartGenerator", chartResult);
                    log.info("ChartGenerator executed with input from DataFetcher. Output: {}", chartResult);
                }

                result.put("status", "COMPLETED");
                repo.save(taskId, result);
                log.info("Task {} completed successfully", taskId);
            } catch (Exception e) {
                repo.save(taskId, Map.of("status", "FAILED", "error", e.getMessage()));
                log.error("Task {} failed: {}", taskId, e.getMessage(), e);
            }
        });

        return taskId;
    }

    @GetMapping("/{id}")
    public Map<String, Object> getTask(@PathVariable String id) {
        Map<String, Object> task = repo.get(id);
        log.info("Fetching task {} -> {}", id, task);
        return task;
    }

    @GetMapping
    public Map<String, Map<String, Object>> getAllTasks() {
        return repo.getAll();
    }


}

