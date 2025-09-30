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

        // Run orchestration in background
        CompletableFuture.runAsync(() -> {
            try {
                ExecutionGraph graph = new ExecutionGraph();
                graph.addDependency("chartGenerator", "dataFetcher");

                TaskOrchestrator orchestrator = new TaskOrchestrator();
                orchestrator.registerAgent(new DataFetcherAgent(new DummyDataFetcher()));
                orchestrator.registerAgent(new ChartGeneratorAgent(new DummyChartGenerator()));

                Map<String, Object> result = orchestrator.run(graph);
                result.put("status", "COMPLETED");
                repo.save(taskId, result);
            } catch (Exception e) {
                repo.save(taskId, Map.of("status", "FAILED", "error", e.getMessage()));
            }
        });

        return taskId;
    }


    @GetMapping("/{id}")
    public Map<String, Object> getTask(@PathVariable String id) {
        return repo.get(id);
    }

}

