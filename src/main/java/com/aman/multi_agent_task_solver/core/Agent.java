package com.aman.multi_agent_task_solver.core;

import java.util.concurrent.Callable;

public interface Agent extends Callable<Object> {
    String getName();
    void setInput(Object input);
}