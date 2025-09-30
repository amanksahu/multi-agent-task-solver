package com.aman.multi_agent_task_solver.tools;

import com.aman.multi_agent_task_solver.core.Tool;

public class DummyDataFetcher implements Tool {
    @Override
    public String getName() { return "dummyDataFetcher"; }

    @Override
    public Object execute(Object input) {
        return "{ \"values\": [1, 2, 3, 4] }";
    }
}