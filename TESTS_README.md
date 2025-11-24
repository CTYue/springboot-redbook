# Test Configuration

## Current Setup

Tests are configured to use **MySQL database** (same as production).

### Requirements
- ✅ MySQL server must be running on `localhost:3306`
- ✅ Database `redbook` must exist
- ✅ User `root` with password `chuwa` must have access

### Running Tests

**1. Start MySQL:**
```bash
# On macOS with Homebrew
brew services start mysql

# Or manually
mysql.server start
```

**2. Verify MySQL is running:**
```bash
mysql -u root -pchuwa -e "SHOW DATABASES;"
```

**3. Run tests:**
```bash
mvn test
```

## Test Configuration Details

**Location:** `src/test/resources/application.properties`

**Key Settings:**
- Uses MySQL at `localhost:3306/redbook`
- Disables MCP server auto-configuration (compatibility issue with Spring Boot 2.7.3)
- Uses same database as production (tests may modify data!)

## Alternative: Use H2 for Tests (Recommended)

If you prefer tests that don't require MySQL running:

1. **Add H2 dependency to `pom.xml`:**
```xml
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>test</scope>
</dependency>
```

2. **Update `src/test/resources/application.properties`:**
```properties
# Test database configuration with H2
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# Hibernate properties for H2
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop
```

**Benefits of H2:**
- ✅ No MySQL server needed
- ✅ Fast (in-memory)
- ✅ Isolated tests (won't affect production data)
- ✅ Automatic cleanup after each test run

**Drawback:**
- Different database than production (but tests still validate business logic)

## Current Test Status

- ✅ MCP auto-configuration disabled (fixes Spring Boot 2.7.3 compatibility)
- ✅ MySQL connector available
- ⚠️ **Tests will fail if MySQL is not running**

## Troubleshooting

### "Connection refused" error
- **Cause:** MySQL server is not running
- **Solution:** Start MySQL with `mysql.server start`

### "Access denied" error  
- **Cause:** Wrong username/password
- **Solution:** Update credentials in `src/test/resources/application.properties`

### "Unknown database 'redbook'" error
- **Cause:** Database doesn't exist
- **Solution:** Create it: `mysql -u root -pchuwa -e "CREATE DATABASE redbook;"`
