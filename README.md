# Redbook - MCP Server for AI-Powered Blog Management

An **MCP (Model Context Protocol) server** built with **Spring Boot 3.2.0** that enables AI assistants to manage blog posts through natural language interactions.

## 💡 What is MCP?

**Model Context Protocol (MCP)** is an open protocol that enables AI assistants to interact with external tools and data sources. Instead of traditional REST APIs that require HTTP clients, MCP servers expose **tools** that AI assistants can invoke directly through natural language.

### Why MCP Instead of REST?

- **Natural Language Interface:** Users interact via AI assistants (Windsurf, Claude) using plain English
- **No API Client Needed:** AI assistants handle all tool invocations automatically
- **Simplified Integration:** AI assistants discover and use tools without manual API documentation
- **Rich Context:** AI can intelligently chain multiple tool calls to accomplish complex tasks

### Example Workflow

**Traditional REST:**
```bash
curl -X POST http://localhost:8080/api/posts \
  -H "Content-Type: application/json" \
  -d '{"title":"Hello","description":"World","content":"..."}'
```

**With MCP:**
```
You: "Create a blog post about Spring Boot 3 migration"
AI: [Automatically calls createPost tool with generated content]
```

## 🚀 Features

- ✅ **6 AI-Ready MCP Tools** for blog post management
- ✅ **Spring AI MCP Server** with stdio transport
- ✅ **Automated Tool Discovery** via `@McpTool` annotations
- ✅ **MySQL Database** with JPA/Hibernate for data persistence
- ✅ **Spring Boot Actuator** for monitoring and health checks
- ✅ **No REST API** - Designed specifically for AI assistant integration

---

## 📋 Tech Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| Spring Boot | 3.2.0 | Application framework |
| Java | 17+ (tested with 21) | Programming language |
| Spring Data JPA | 3.2.0 | Data access layer |
| Hibernate | 6.3.1 | ORM |
| MySQL | 8.x | Database |
| Spring AI | 1.1.0 | MCP server support |
| Maven | 3.x | Build tool |

---

## 🛠️ Prerequisites

Before you begin, ensure you have:

- **Java 17 or higher** (tested with Java 21.0.3)
- **Maven 3.x**
- **MySQL 8.x** server
- **Git**
- (Optional) **Windsurf Desktop** or **Claude Desktop** for MCP features

### Verify Prerequisites

```bash
# Check Java version (should be 17+)
java -version

# Check Maven
mvn -version

# Check MySQL connectivity
mysql -h 192.168.5.13 -u chuwa -p
```

---

## ⚙️ Configuration

### Database Configuration

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://192.168.5.13:3306/redbook?allowPublicKeyRetrieval=true&useSSL=false
spring.datasource.username=chuwa
spring.datasource.password=chuwa
```

### MCP Configuration

MCP is enabled by default:
```properties
spring.ai.mcp.server.stdio=true
```

To disable MCP (e.g., for testing):
```properties
spring.ai.mcp.server.stdio=false
```

---

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone <repository-url>
cd springboot-redbook
```

### 2. Build the Project

```bash
mvn clean install
```

### 3. Run Tests

```bash
mvn test
```

### 4. Run the Application

**Option A: Using Maven**
```bash
mvn spring-boot:run
```

**Option B: Using JAR**
```bash
mvn clean package -DskipTests
java -jar target/redbook-0.0.1-SNAPSHOT.jar
```

### 5. Verify Application is Running

```bash
curl http://localhost:8080/actuator/health
```

Expected response:
```json
{
  "status": "UP"
}
```

---

## 🤖 MCP Tools for AI Assistants

This application provides **6 MCP tools** that AI assistants can use:

| Tool | Description | Parameters |
|------|-------------|------------|
| `createPost` | Create a new blog post | title, description, content |
| `getPostById` | Retrieve a specific post | id |
| `getAllPosts` | List all posts | none |
| `updatePost` | Update an existing post | id, title, description, content |
| `deletePost` | Delete a post | id |
| `checkReadiness` | Health check | none |

### MCP Setup for Windsurf Desktop

See **[MCP_WINDSURF_SETUP.md](MCP_WINDSURF_SETUP.md)** for detailed instructions.

**Quick Setup:**

1. Build the JAR:
   ```bash
   mvn clean package -DskipTests
   ```

2. Configure Windsurf (`~/Library/Application Support/Windsurf/mcp_config.json`):
   ```json
   {
     "mcpServers": {
       "redbook": {
         "command": "java",
         "args": [
           "-jar",
           "/Users/yuzidong/Code/springboot-redbook/target/redbook-0.0.1-SNAPSHOT.jar"
         ]
       }
     }
   }
   ```

3. Restart Windsurf Desktop

4. Ask Windsurf: `"What MCP tools are available?"`

---

## 🔌 Available Endpoints

### Actuator Endpoints (Management Only)

| Endpoint | Description |
|----------|-------------|
| `/actuator/health` | Application health status |
| `/actuator/metrics` | Application metrics |
| `/actuator/info` | Application information |
| `/actuator/prometheus` | Prometheus metrics |

**Note:** This application does **not expose REST APIs** for blog posts. All blog post operations are performed through MCP tools via AI assistants like Windsurf or Claude Desktop.

---

## 📦 Project Structure

```
springboot-redbook/
├── src/
│   ├── main/
│   │   ├── java/com/chuwa/redbook/
│   │   │   ├── tools/           # MCP tools (AI interface)
│   │   │   ├── service/         # Business logic
│   │   │   ├── dao/             # Data repositories
│   │   │   ├── entity/          # JPA entities
│   │   │   ├── payload/         # DTOs
│   │   │   ├── exception/       # Custom exceptions
│   │   │   └── util/            # Utilities
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       ├── java/                # Unit tests
│       └── resources/
│           └── application.properties
├── pom.xml
├── README.md
├── MCP_WINDSURF_SETUP.md       # MCP configuration guide
├── MCP_TESTING_GUIDE.md        # Testing MCP tools
└── SPRING_BOOT_3_UPGRADE_SUMMARY.md  # Upgrade notes

```

**Key Components:**
- **`tools/ContentTools.java`** - MCP tools with `@McpTool` annotations for AI assistants
- **`service/PostService.java`** - Business logic for blog post operations
- **`dao/PostRepository.java`** - JPA repository for database access
- **`entity/Post.java`** - JPA entity representing a blog post

---

## 🧪 Testing

### Run All Tests

```bash
mvn test
```

### Run Specific Test

```bash
mvn test -Dtest=RedbookApplicationTests
```

### Test Coverage

```bash
mvn clean test jacoco:report
```

View report: `target/site/jacoco/index.html`

---

## 🐛 Troubleshooting

### Common Issues

#### Port 8080 Already in Use

```bash
# Kill existing process
lsof -ti:8080 | xargs kill -9
```

#### Database Connection Failed

```bash
# Verify MySQL is running
mysql -h 192.168.5.13 -u chuwa -p

# Check application.properties configuration
```

#### MCP Tools Not Working

See **[MCP_TESTING_GUIDE.md](MCP_TESTING_GUIDE.md)** for comprehensive debugging steps.

---

## 📚 Documentation

- **[MCP_WINDSURF_SETUP.md](MCP_WINDSURF_SETUP.md)** - Complete MCP integration guide
- **[MCP_TESTING_GUIDE.md](MCP_TESTING_GUIDE.md)** - How to test MCP tools
- **[SPRING_BOOT_3_UPGRADE_SUMMARY.md](SPRING_BOOT_3_UPGRADE_SUMMARY.md)** - Upgrade details

---

## 🔄 Migration from Spring Boot 2.x

This project has been upgraded from Spring Boot 2.7.3 to 3.2.0. Key changes:

- ✅ Java 11 → Java 17
- ✅ `javax.*` → `jakarta.*` packages
- ✅ MySQL Connector: `mysql-connector-java` → `mysql-connector-j`
- ✅ Hibernate Dialect: `MySQL5InnoDBDialect` → `MySQLDialect`
- ✅ MCP support enabled

See **[SPRING_BOOT_3_UPGRADE_SUMMARY.md](SPRING_BOOT_3_UPGRADE_SUMMARY.md)** for details.

---

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

---

## 📝 Development Guidelines

### Code Style
- Follow Java naming conventions
- Use meaningful variable and method names
- Add JavaDoc for public methods
- Keep methods focused and small

### Git Workflow
- Write clear commit messages
- Keep commits atomic
- Reference issues in commit messages

---

## 🎯 Roadmap

### MCP Enhancements
- [ ] Add more MCP tools (search posts, filter by date, etc.)
- [ ] Implement MCP prompts for common workflows
- [ ] Add MCP resources for blog post templates
- [ ] Support for tags and categories via MCP tools
- [ ] Add image upload/attachment support via MCP

### Infrastructure
- [ ] Add authentication for MCP connections
- [ ] Implement rate limiting for MCP tools
- [ ] Enhanced logging and monitoring
- [ ] Docker containerization improvements
- [ ] Add integration tests for MCP tools

### Optional: REST API Layer
- [ ] Add REST API endpoints (for traditional web/mobile clients)
- [ ] Implement Swagger/OpenAPI documentation
- [ ] Add authentication and authorization

---

## 📄 License

[Add your license here]

---

## 👥 Authors

- **Chuwa Team** - Initial work

---

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- Spring AI team for MCP support
- Contributors and maintainers

---

## 📞 Support

For issues and questions:
- Open an issue in the repository
- Check existing documentation
- Review troubleshooting guides

---

**Built with ❤️ using Spring Boot 3.2.0**