# MCP Server Testing Guide

## How to Verify Your MCP Server Works

### Method 1: Test with Windsurf Desktop (Easiest)

#### Step 1: Ensure MCP is Configured

Check your config file: `~/Library/Application Support/Windsurf/mcp_config.json`

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

#### Step 2: Build the JAR

```bash
cd /Users/yuzidong/Code/springboot-redbook
mvn clean package -DskipTests
```

Verify it exists:
```bash
ls -lh target/redbook-0.0.1-SNAPSHOT.jar
```

#### Step 3: Restart Windsurf

Close and reopen Windsurf Desktop completely.

#### Step 4: Test in Windsurf

Ask these questions in Windsurf:

**Test 1: Check if tools are available**
```
What MCP tools do you have available?
```

Expected: Should list your 6 tools (createPost, getPostById, getAllPosts, updatePost, deletePost, checkReadiness)

**Test 2: Use checkReadiness tool**
```
Check if the redbook application is ready
```

Expected: Should return status and post count

**Test 3: Create a test post**
```
Create a new post with title "Test Post", description "Testing MCP", and content "This is a test from Windsurf"
```

Expected: Should return the created post with an ID

**Test 4: Retrieve posts**
```
Show me all posts in the redbook
```

Expected: Should return a list of posts including the one just created

**Test 5: Get specific post**
```
Get the post with ID 1
```

Expected: Should return the post details or error if not found

---

### Method 2: Manual Testing with Java

Test the MCP server directly from command line:

#### Step 1: Start the Server Manually

```bash
cd /Users/yuzidong/Code/springboot-redbook
java -jar target/redbook-0.0.1-SNAPSHOT.jar
```

Watch for these logs:
```
✅ INFO: Registered tools: 6
✅ INFO: Enable tools capabilities, notification: true
✅ INFO: Tomcat started on port 8080
✅ INFO: Started RedbookApplication
```

#### Step 2: Check Health Endpoint

```bash
curl http://localhost:8080/actuator/health | python3 -m json.tool
```

Expected output:
```json
{
    "status": "UP",
    "components": {
        "db": {
            "status": "UP"
        }
    }
}
```

#### Step 3: Check Application is Running

```bash
# Check if port 8080 is in use
lsof -ti:8080

# Should return a process ID
```

---

### Method 3: Test with MCP Inspector (Advanced)

The MCP Inspector is a debugging tool for MCP servers.

#### Install MCP Inspector

```bash
npm install -g @modelcontextprotocol/inspector
```

#### Test Your Server

```bash
mcp-inspector java -jar /Users/yuzidong/Code/springboot-redbook/target/redbook-0.0.1-SNAPSHOT.jar
```

This will:
- ✅ List all available tools
- ✅ Show tool schemas
- ✅ Allow you to invoke tools manually
- ✅ Display request/response logs

---

### Method 4: Check Server Logs

When the application starts, look for these key indicators:

#### ✅ Good Signs:

```
INFO: Registered tools: 6
INFO: Enable tools capabilities, notification: true
INFO: Started RedbookApplication in X.XX seconds
Tomcat started on port 8080
```

#### ❌ Bad Signs:

```
ERROR: Failed to start bean 'webServerStartStop'
Port 8080 was already in use
IllegalStateException: Error processing condition
Connection refused
```

---

### Method 5: Database Connection Test

Since your MCP tools interact with MySQL, verify database connectivity:

```bash
# Test MySQL connection
mysql -h 192.168.5.13 -u chuwa -p -e "SELECT COUNT(*) FROM redbook.posts;"
# Password: chuwa
```

Expected: Should show the count of posts or error if table doesn't exist

---

## Common Issues and Solutions

### Issue 1: "No tools available"

**Cause:** MCP server not configured or JAR not found

**Fix:**
```bash
# Verify JAR exists
ls -lh /Users/yuzidong/Code/springboot-redbook/target/redbook-0.0.1-SNAPSHOT.jar

# Rebuild if missing
mvn clean package -DskipTests
```

### Issue 2: "Port 8080 already in use"

**Cause:** Another instance is running

**Fix:**
```bash
# Kill existing process
lsof -ti:8080 | xargs kill -9
```

### Issue 3: "Database connection failed"

**Cause:** MySQL server not accessible

**Fix:**
```bash
# Check MySQL server
mysql -h 192.168.5.13 -u chuwa -p
# Password: chuwa

# Or check application.properties
cat src/main/resources/application.properties
```

### Issue 4: "Tools not showing in Windsurf"

**Cause:** Configuration not loaded or Windsurf not restarted

**Fix:**
1. Verify config file exists: `~/Library/Application Support/Windsurf/mcp_config.json`
2. Check JSON syntax is valid (use `python3 -m json.tool < mcp_config.json`)
3. Completely quit and restart Windsurf Desktop
4. Check Windsurf Developer Console for errors

### Issue 5: "Java version mismatch"

**Cause:** Wrong Java version

**Fix:**
```bash
# Check Java version
java -version
# Should be Java 17 or higher

# If wrong version, set JAVA_HOME
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
```

---

## Automated Test Script

Create a test script to verify everything:

```bash
#!/bin/bash
# test-mcp.sh

echo "🔍 Testing MCP Server..."

# 1. Check JAR exists
if [ -f "target/redbook-0.0.1-SNAPSHOT.jar" ]; then
    echo "✅ JAR file exists"
else
    echo "❌ JAR file not found. Run: mvn clean package -DskipTests"
    exit 1
fi

# 2. Check Java version
JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
if [ "$JAVA_VERSION" -ge 17 ]; then
    echo "✅ Java version $JAVA_VERSION (>= 17)"
else
    echo "❌ Java version too old. Need Java 17+"
    exit 1
fi

# 3. Check MySQL connectivity
if mysql -h 192.168.5.13 -u chuwa -pchuwa -e "SELECT 1" &> /dev/null; then
    echo "✅ MySQL connection successful"
else
    echo "❌ MySQL connection failed"
fi

# 4. Check if port 8080 is free
if lsof -ti:8080 &> /dev/null; then
    echo "⚠️  Port 8080 is in use"
else
    echo "✅ Port 8080 is available"
fi

# 5. Check Windsurf config
CONFIG="$HOME/Library/Application Support/Windsurf/mcp_config.json"
if [ -f "$CONFIG" ]; then
    echo "✅ Windsurf MCP config exists"
    if python3 -m json.tool < "$CONFIG" &> /dev/null; then
        echo "✅ Config JSON is valid"
    else
        echo "❌ Config JSON is invalid"
    fi
else
    echo "❌ Windsurf MCP config not found"
fi

echo ""
echo "🎉 Pre-flight checks complete!"
```

Run it:
```bash
chmod +x test-mcp.sh
./test-mcp.sh
```

---

## Expected Tool Behavior

### createPost
- **Input:** title, description, content (all strings)
- **Output:** PostDto with ID, title, description, content
- **Test:** Creates a new post in database

### getPostById
- **Input:** id (number)
- **Output:** PostDto with post details
- **Test:** Retrieves existing post or throws error

### getAllPosts
- **Input:** None
- **Output:** List of PostDto objects
- **Test:** Returns all posts from database

### updatePost
- **Input:** id (number), title, description, content (all strings)
- **Output:** Updated PostDto
- **Test:** Updates existing post or throws error

### deletePost
- **Input:** id (number)
- **Output:** Success message with status
- **Test:** Deletes post from database

### checkReadiness
- **Input:** None
- **Output:** Status and sample post ID (or "none")
- **Test:** Verifies application and database connectivity

---

## Quick Verification Checklist

Before using MCP in Windsurf, verify:

- [ ] JAR file exists: `target/redbook-0.0.1-SNAPSHOT.jar`
- [ ] Java 17+ installed: `java -version`
- [ ] MySQL accessible: Can connect to 192.168.5.13:3306
- [ ] Port 8080 free: `lsof -ti:8080` returns nothing
- [ ] Windsurf config exists and valid JSON
- [ ] Windsurf restarted after config changes

---

## Success Indicators

When everything works correctly:

1. **In Windsurf:**
   - Asking "What tools are available?" lists 6 Redbook tools
   - Tools can be invoked and return data
   - No error messages in responses

2. **In Logs:**
   ```
   INFO: Registered tools: 6
   INFO: Started RedbookApplication in X seconds
   ```

3. **Health Check:**
   ```bash
   curl http://localhost:8080/actuator/health
   # Returns {"status":"UP"}
   ```

---

## Debugging Tips

### Enable Debug Logging

Add to `application.properties`:
```properties
logging.level.org.springframework.ai.mcp=DEBUG
logging.level.com.chuwa.redbook=DEBUG
```

### Check Windsurf Console

1. Open Windsurf
2. View → Toggle Developer Tools
3. Check Console tab for MCP-related errors

### Test Individual Components

```bash
# Test just Spring Boot (not MCP)
mvn spring-boot:run

# In another terminal
curl http://localhost:8080/actuator/health

# Then Ctrl+C to stop
```

---

## Next Steps After Verification

Once your MCP server is working:

1. **Create sample data** for testing
2. **Document your tools** for other users
3. **Add error handling** for edge cases
4. **Consider adding more tools** for other entities
5. **Set up monitoring** for production use

---

**Last Updated:** November 24, 2025
