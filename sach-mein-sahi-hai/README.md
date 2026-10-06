# Sach Mein Sahi Hai? (MVP skeleton)
Java 21 + Spring Boot 3 + H2. Open `pom.xml` in IntelliJ IDEA (File > Open > pom.xml > Open as Project), run `SachApplication`.
- UI: http://localhost:8080/  (static prototype, works standalone)
- API: POST /api/verify {"text":"..."}  |  GET/DELETE /api/history
- H2 console: http://localhost:8080/h2-console  (jdbc:h2:file:./data/sach)
- LLM keys via env vars LLM_PROVIDER, LLM_API_KEY (not used yet).
Next: JWT/Spring Security, Research/SourceValidation/Explanation agents, OCR, S3, rate limiting.
