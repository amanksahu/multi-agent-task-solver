package com.aman.multi_agent_task_solver.tools;

import com.aman.multi_agent_task_solver.core.Tool;

public class DummyChartGenerator implements Tool {
    @Override
    public String getName() { return "dummyChartGenerator"; }

    @Override
    public Object execute(Object input) {
        return "BASE64_IMAGE_PLACEHOLDER";
    }
}