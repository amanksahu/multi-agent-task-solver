# Multi-Agent Task Solver

## 📌 Design Decisions

1. **Agent Abstraction**  
   - Implemented a common `Agent` interface (`call()`, `getName()`, `setInput()`) so agents run in isolation.  
   - Each agent delegates work to a `Tool` to separate orchestration from business logic.

2. **Execution Graph**  
   - Used a lightweight in-memory DAG (`ExecutionGraph`) to model dependencies.  
   - Avoided external workflow engines to keep design simple and focused.

3. **Concurrency Handling**  
   - Used `ExecutorService` to run agents concurrently.  
   - Applied `Future.get(timeout, TimeUnit.SECONDS)` for timeouts.  
   - Added retry logic with a fixed number of attempts.

4. **API Layer**  
   - Exposed minimal REST endpoints with Spring Boot (`POST /tasks`, `GET /tasks/{id}`).  
   - Integrated Swagger/OpenAPI for documentation and interactive testing.

5. **Storage**  
   - Chose in-memory storage (`ConcurrentHashMap`) over a database to reduce setup and complexity.  
   - Suitable for a demo, but not persistent across restarts.

6. **Asynchronous Task Submission**  
   - `POST /tasks` returns a `taskId` immediately.  
   - Agents run in the background via `CompletableFuture`.  
   - `GET /tasks/{id}` shows results or execution status.

---

## ⚖️ Trade-offs Due to 24h Constraint

- **No Persistent DB**: Using in-memory repository instead of H2/MySQL; results vanish after restart.  
- **Simplified Retry Logic**: Fixed 3 retries, no exponential backoff.  
- **Static Graph for Demo**: Hardcoded `DataFetcher → ChartGenerator` dependency to save graph parsing time.  
- **Minimal Logging**: Basic console logs only; no structured log framework.  
- **Testing Coverage**: Added minimal tests instead of full suite.  
- **Security**: Endpoints are open; no authentication layer.

These choices allowed delivery of a working demo within 24 hours.

---

## 🚀 Running the System

### Prerequisites
- Java 17+ (tested with Java 21)  
- Maven 3.8+  

### Build and Run
```bash
mvn clean install
mvn spring-boot:run
