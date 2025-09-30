package com.aman.multi_agent_task_solver.core;

public interface Tool {
    String getName();
    Object execute(Object input);
}
