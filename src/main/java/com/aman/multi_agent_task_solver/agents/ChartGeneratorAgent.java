package com.aman.multi_agent_task_solver.agents;

import com.aman.multi_agent_task_solver.core.Agent;
import com.aman.multi_agent_task_solver.core.Tool;

public class ChartGeneratorAgent implements Agent {
    private final Tool chartTool;
    private Object input;

    public ChartGeneratorAgent(Tool chartTool) {
        this.chartTool = chartTool;
    }

    @Override
    public String getName() { return "chartGenerator"; }

    @Override
    public void setInput(Object input) { this.input = input; }

    @Override
    public Object call() {
        return chartTool.execute(input);
    }
}