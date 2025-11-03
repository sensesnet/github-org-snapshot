# GitHub Org Snapshot – Backend

A simple Spring Boot backend that exposes REST APIs to:

- Check service health
- Fetch top repositories for a given GitHub organization

Used by the frontend app to display GitHub organization snapshots.

---

## Tech Stack

- Java 17+
- Spring Boot 3.x
- Maven 3.6.1 or higher
- REST API integration with [GitHub Public API](https://api.github.com)

---

## Project Structure

```bash
/backend
├── src/main/java/org/snapshot/github
│    ├── api/rest/                # REST controllers
│    ├── model/                   # DTOs and constants
│    ├── service/                 # GitHub API logic
│    └── BackendApplication.java  # Spring Boot entrypoint
├── pom.xml / build.gradle
└── README.md
```

## Endpoints

### Health Check

**Request**

```
GET /api/health
```

**Response**

```json
{
  "status": "OK"
}
```

### Fetch Repositories by Organization

**Request**

```
GET /api/org/{org}/repos?limit=5&sort=stars
```

**Parameter Type**

- organization (e.g. apple, spring-projects)
- limit (1–20)
- sort (stars or updated)

**Response Example**

```json
{
  "repos": [
    {
      "name": "foundationdb",
      "language": "C++",
      "description": "FoundationDB - the open source, distributed, transactional key-value store",
      "html_url": "https://github.com/apple/foundationdb",
      "stargazers_count": 15803,
      "forks_count": 1442,
      "updated_at": "2025-11-02T09:44:54Z"
    },
    {
      "name": "turicreate",
      "language": "C++",
      "description": "Turi Create simplifies the development of custom machine learning models.",
      "html_url": "https://github.com/apple/turicreate",
      "stargazers_count": 11193,
      "forks_count": 1140,
      "updated_at": "2025-10-28T18:17:08Z"
    },
    {
      "name": "darwin-xnu",
      "language": "C",
      "description": "Legacy mirror of Darwin Kernel. Replaced by https://github.com/apple-oss-distributions/xnu",
      "html_url": "https://github.com/apple/darwin-xnu",
      "stargazers_count": 11179,
      "forks_count": 1674,
      "updated_at": "2025-10-29T15:53:11Z"
    },
    {
      "name": "swift-nio",
      "language": "Swift",
      "description": "Event-driven network application framework for high performance protocol servers & clients, non-blocking.",
      "html_url": "https://github.com/apple/swift-nio",
      "stargazers_count": 8297,
      "forks_count": 712,
      "updated_at": "2025-11-03T00:08:54Z"
    },
    {
      "name": "swift-algorithms",
      "language": "Swift",
      "description": "Commonly used sequence and collection algorithms for Swift",
      "html_url": "https://github.com/apple/swift-algorithms",
      "stargazers_count": 6253,
      "forks_count": 467,
      "updated_at": "2025-11-02T14:00:38Z"
    }
  ]
}
```

## How to run locally

### Using Maven
```
mvn spring-boot:run
```
or
```
mvn clean package
java -jar target/backend-1.0.0.jar
```

## API Documentation (Swagger)

This project includes Swagger UI for interactive API documentation.

Accessing Swagger UI
Open a browser and go to:

http://localhost:8080/swagger-ui/index.html

You’ll see an interactive interface with all available endpoints, including:
```
GET /api/health → health check
GET /api/org/{org}/repos → fetch GitHub repositories
```

You can also test requests directly from the Swagger UI.