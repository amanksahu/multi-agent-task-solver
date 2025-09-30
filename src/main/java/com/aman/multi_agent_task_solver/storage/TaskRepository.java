package com.aman.multi_agent_task_solver.storage;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class TaskRepository {
    private final Map<String, Map<String, Object>> tasks = new ConcurrentHashMap<>();

    public void save(String taskId, Map<String, Object> result) {
        tasks.put(taskId, result);
    }

    public Map<String, Object> get(String taskId) {
        return tasks.getOrDefault(taskId, Map.of());
    }
}