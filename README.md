📌 Overview

This project implements a multi-agent orchestration layer where agents run in isolation but can share results, handle concurrency, retries, and timeouts.
It provides:

Modular agent architecture with dynamic execution graph (DAG).

Pluggable tools (e.g., data fetcher, chart generator).

REST API for submitting tasks and retrieving results.

In-memory task storage with status tracking (RUNNING, COMPLETED, FAILED)
