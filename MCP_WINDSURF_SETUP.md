# MCP Setup for Windsurf Desktop

## Your MCP Tools Status ✅

**6 MCP Tools Ready:**
1. `createPost` - Create a new post with title, description, and content
2. `getPostById` - Lookup and retrieve a specific post by its ID
3. `getAllPosts` - Get all posts as a list
4. `updatePost` - Update an existing post by ID
5. `deletePost` - Delete a post by its ID permanently
6. `checkReadiness` - Check if the application is ready and healthy

---

## Windsurf Desktop MCP Configuration

### Configuration File Location

Windsurf Desktop MCP configuration is typically located at:

**macOS:**
```
~/Library/Application Support/Windsurf/mcp_config.json
```

**Linux:**
```
~/.config/Windsurf/mcp_config.json
```

**Windows:**
```
%APPDATA%\Windsurf\mcp_config.json
```

### Configuration Format

Add your Redbook MCP server to the configuration:

```json
{
  "mcpServers": {
    "redbook": {
      "command": "java",
      "args": [
        "-jar",
        "/Users/yuzidong/Code/springboot-redbook/target/redbook-0.0.1-SNAPSHOT.jar"
      ],
      "env": {}
    }
  }
}
```

### Alternative: Using Maven (Development)

If you're actively developing and want to use Maven instead of the JAR:

```json
{
  "mcpServers": {
    "redbook": {
      "command": "mvn",
      "args": [
        "spring-boot:run",
        "-f",
        "/Users/yuzidong/Code/springboot-redbook/pom.xml"
      ],
      "cwd": "/Users/yuzidong/Code/springboot-redbook",
      "env": {}
    }
  }
}
```

---

## Setup Steps

### 1. Build the Application JAR

```bash
cd /Users/yuzidong/Code/springboot-redbook
mvn clean package -DskipTests
```

This creates: `target/redbook-0.0.1-SNAPSHOT.jar`

### 2. Add Configuration to Windsurf

1. Create or edit the MCP config file:
   ```bash
   mkdir -p ~/Library/Application\ Support/Windsurf
   nano ~/Library/Application\ Support/Windsurf/mcp_config.json
   ```

2. Paste the configuration (see above)

3. Save and close

### 3. Restart Windsurf Desktop

Close and reopen Windsurf Desktop to load the new MCP server configuration.

### 4. Verify Connection

In Windsurf, the MCP tools should appear in the AI assistant's available tools. You can verify by asking:

```
"What MCP tools do you have available?"
```

You should see the 6 Redbook tools listed.

---

## Using MCP Tools in Windsurf

### Example Prompts

#### Create a Post
```
"Create a new post titled 'Getting Started with Spring Boot 3' 
with description 'A guide to upgrading' and content 'Spring Boot 3 
brings Jakarta EE support...'"
```

#### Get All Posts
```
"Show me all the posts in the redbook"
```

#### Get Specific Post
```
"Get the post with ID 1"
```

#### Update a Post
```
"Update post 1 with new title 'Updated Title', description 'Updated desc', 
and content 'Updated content'"
```

#### Delete a Post
```
"Delete post with ID 5"
```

#### Check System Health
```
"Check if the redbook application is ready"
```

---

## Troubleshooting

### Issue: Tools Not Appearing

**Check 1: Verify the JAR exists**
```bash
ls -lh /Users/yuzidong/Code/springboot-redbook/target/redbook-0.0.1-SNAPSHOT.jar
```

**Check 2: Test the application manually**
```bash
java -jar /Users/yuzidong/Code/springboot-redbook/target/redbook-0.0.1-SNAPSHOT.jar
```

**Check 3: Verify Java version**
```bash
java -version
# Should be Java 17 or higher
```

**Check 4: Check Windsurf logs**
Look in Windsurf's developer console for MCP connection errors.

### Issue: Port Already in Use

If the application fails to start because port 8080 is in use:

```bash
# Find and kill the process
lsof -ti:8080 | xargs kill -9
```

Or configure a different port in `application.properties`:
```properties
server.port=8081
```

### Issue: Database Connection Failed

Verify MySQL server is accessible:
```bash
mysql -h 192.168.5.13 -u chuwa -p
# Enter password: chuwa
```

---

## MCP Server Details

### Protocol
- **Type:** Standard IO (stdio)
- **Format:** JSON-RPC 2.0

### Capabilities
- ✅ Tools (6 tools registered)
- ✅ Resources
- ✅ Resource Templates  
- ✅ Prompts
- ✅ Completions

### Configuration
- **Stdio Mode:** Enabled (`spring.ai.mcp.server.stdio=true`)
- **Auto-configuration:** Fully enabled (Spring Boot 3.x compatible)

---

## Development Workflow

### Option 1: Run via JAR (Production-like)

```bash
# Build
mvn clean package -DskipTests

# Run
java -jar target/redbook-0.0.1-SNAPSHOT.jar
```

### Option 2: Run via Maven (Development)

```bash
mvn spring-boot:run
```

### Option 3: IDE Run (IntelliJ/Eclipse)

Run the main class: `com.chuwa.redbook.RedbookApplication`

---

## Architecture

```
┌─────────────────┐
│ Windsurf Desktop│
│   (MCP Client)  │
└────────┬────────┘
         │ JSON-RPC 2.0
         │ (stdio)
         ↓
┌─────────────────────┐
│  Spring Boot 3.2.0  │
│  MCP Server         │
│  ┌───────────────┐  │
│  │ ContentTools  │  │
│  │ @McpTool × 6  │  │
│  └───────┬───────┘  │
│          │          │
│  ┌───────▼───────┐  │
│  │ PostService   │  │
│  └───────┬───────┘  │
│          │          │
│  ┌───────▼───────┐  │
│  │ JPA/Hibernate │  │
│  └───────┬───────┘  │
└──────────┼──────────┘
           │
           ↓
    ┌──────────────┐
    │ MySQL Server │
    │ 192.168.5.13 │
    └──────────────┘
```

---

## Additional Configuration Options

### Custom Server Name

```json
{
  "mcpServers": {
    "my-redbook-server": {
      "command": "java",
      "args": ["-jar", "/path/to/redbook-0.0.1-SNAPSHOT.jar"]
    }
  }
}
```

### With JVM Options

```json
{
  "mcpServers": {
    "redbook": {
      "command": "java",
      "args": [
        "-Xmx512m",
        "-Dspring.profiles.active=prod",
        "-jar",
        "/Users/yuzidong/Code/springboot-redbook/target/redbook-0.0.1-SNAPSHOT.jar"
      ]
    }
  }
}
```

### Multiple MCP Servers

You can configure multiple MCP servers:

```json
{
  "mcpServers": {
    "redbook": {
      "command": "java",
      "args": ["-jar", "/path/to/redbook-0.0.1-SNAPSHOT.jar"]
    },
    "other-service": {
      "command": "node",
      "args": ["/path/to/other-mcp-server.js"]
    }
  }
}
```

---

## Quick Reference

### Start Application
```bash
cd /Users/yuzidong/Code/springboot-redbook
mvn spring-boot:run
```

### Health Check
```bash
curl http://localhost:8080/actuator/health
```

### Build JAR
```bash
mvn clean package -DskipTests
```

### View Logs
```bash
tail -f logs/spring.log  # if configured
# or check console output
```

---

## Support & Documentation

- **Spring AI MCP Docs:** https://docs.spring.io/spring-ai/reference/api/mcp/
- **MCP Specification:** https://modelcontextprotocol.io/
- **Windsurf Docs:** Check Windsurf's official documentation for MCP setup

---

**Last Updated:** November 24, 2025  
**MCP Status:** ✅ Fully Functional  
**Tools Available:** 6  
**Spring Boot Version:** 3.2.0
