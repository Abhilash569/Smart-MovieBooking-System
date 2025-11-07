# How to Run Smart Movie Booking System

## Requirements

- **Java 17** or higher
- **Maven 3.6+**
- **Apache Tomcat 10.1+**

## Steps to Run

### 1. Clone the Repository
```bash
git clone https://github.com/Abhilash569/Smart-MovieBooking-System.git
cd Smart-MovieBooking-System
```

### 2. Build the Project
```bash
mvn clean package
```

### 3. Deploy to Tomcat
```bash
# Linux/Mac
cp target/smart-booking.war $TOMCAT_HOME/webapps/

# Windows
copy target\smart-booking.war %TOMCAT_HOME%\webapps\
```

### 4. Start Tomcat
```bash
# Linux/Mac
$TOMCAT_HOME/bin/startup.sh

# Windows
%TOMCAT_HOME%\bin\startup.bat
```

### 5. Access Application
```
http://localhost:8080/smart-booking
```

### 6. Login
```
Email: demo@email.com
Password: password123
```

---

## Quick Commands

| Action | Command |
|--------|---------|
| Build | `mvn clean package` |
| Deploy | `cp target/smart-booking.war $TOMCAT_HOME/webapps/` |
| Start Server | `$TOMCAT_HOME/bin/startup.sh` |
| Stop Server | `$TOMCAT_HOME/bin/shutdown.sh` |
| View Logs | `tail -f $TOMCAT_HOME/logs/catalina.out` |

---

## Notes

- **No database setup required** - Uses H2 in-memory database
- **API keys included** - MovieDB and Google Maps APIs pre-configured
- **Demo data** - 40+ theatres and demo user auto-created on startup
- **Port 8080** - Default Tomcat port, change in `server.xml` if needed

---

## Troubleshooting

**Port 8080 already in use:**
```bash
# Find and kill process
lsof -i :8080  # Linux/Mac
netstat -ano | findstr :8080  # Windows
```

**Build fails:**
```bash
# Check Java version
java -version  # Should be 17+

# Check Maven version
mvn -version  # Should be 3.6+
```

**Application not loading:**
```bash
# Check Tomcat logs
tail -f $TOMCAT_HOME/logs/catalina.out
```

---

That's it! The application should be running at http://localhost:8080/smart-booking 🚀
