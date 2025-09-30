package com.aman.multi_agent_task_solver.agents;

import com.aman.multi_agent_task_solver.core.Agent;
import com.aman.multi_agent_task_solver.core.Tool;

public class DataFetcherAgent implements Agent {
    private final Tool fetcher;
    private Object input;

    public DataFetcherAgent(Tool fetcher) {
        this.fetcher = fetcher;
    }

    @Override
    public String getName() { return "dataFetcher"; }

    @Override
    public void setInput(Object input) { this.input = input; }

    @Override
    public Object call() {
        return fetcher.execute(input);
    }
}
