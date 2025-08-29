# webhook-solver

Spring Boot app that on startup:
1. POSTs to a "generateWebhook" endpoint with {name, regNo, email}
2. Receives { webhook, accessToken }
3. Loads a final SQL query from src/main/resources/solution.sql
4. Stores the final SQL and metadata in H2
5. POSTs { finalQuery } to the returned webhook URL with Authorization header set to the JWT

## How to run

1. Edit `src/main/resources/application.yml` and set your values for:
   - app.name
   - app.regNo
   - app.email
   - external.generateWebhookUrl (for real endpoint)
2. Put your final SQL text in `src/main/resources/solution.sql`
3. Build and run:
   ```
   mvn clean package
   java -jar target/webhook-solver-0.0.1-SNAPSHOT.jar
   ```
4. For local testing, this repo includes a mock endpoint returning a test webhook and accessToken. To test with real endpoint, remove `MockController` and set `generateWebhookUrl` to the real URL.

H2 console: http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:testdb
User: sa
Password: (empty)
