# How to Run Smart Movie Booking System

## Quick Start (5 Minutes)

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- Apache Tomcat 10.1+

### Steps

1. **Clone the Repository**
```bash
git clone https://github.com/Abhilash569/Smart-MovieBooking-System.git
cd Smart-MovieBooking-System
```

2. **Build the Project**
```bash
mvn clean package
```

3. **Deploy to Tomcat**
```bash
# Copy WAR file to Tomcat webapps directory
cp target/smart-booking.war $TOMCAT_HOME/webapps/

# Or on Windows
copy target\smart-booking.war %TOMCAT_HOME%\webapps\
```

4. **Start Tomcat**
```bash
# Linux/Mac
$TOMCAT_HOME/bin/startup.sh

# Windows
%TOMCAT_HOME%\bin\startup.bat
```

5. **Access Application**
```
http://localhost:8080/smart-booking
```

6. **Login**
```
Email: demo@email.com
Password: password123
```

---

## Detailed Setup Guide

### 1. Install Prerequisites

#### Java 17
**Check if installed:**
```bash
java -version
```

**Download:**
- Oracle JDK: https://www.oracle.com/java/technologies/downloads/
- OpenJDK: https://adoptium.net/

**Verify installation:**
```bash
java -version
# Should show: java version "17.x.x"
```

#### Maven 3.6+
**Check if installed:**
```bash
mvn -version
```

**Download:**
- https://maven.apache.org/download.cgi

**Installation:**
```bash
# Linux/Mac
tar -xzf apache-maven-3.9.x-bin.tar.gz
export PATH=/path/to/maven/bin:$PATH

# Windows
# Extract ZIP and add to PATH environment variable
```

**Verify installation:**
```bash
mvn -version
# Should show: Apache Maven 3.9.x
```

#### Apache Tomcat 10.1+
**Download:**
- https://tomcat.apache.org/download-10.cgi

**Installation:**
```bash
# Linux/Mac
tar -xzf apache-tomcat-10.1.x.tar.gz

# Windows
# Extract ZIP file
```

**Set CATALINA_HOME:**
```bash
# Linux/Mac
export CATALINA_HOME=/path/to/tomcat

# Windows
set CATALINA_HOME=C:\path\to\tomcat
```

---

### 2. Clone and Build

#### Clone Repository
```bash
git clone https://github.com/Abhilash569/Smart-MovieBooking-System.git
cd Smart-MovieBooking-System
```

#### Build with Maven
```bash
mvn clean package
```

**Expected Output:**
```
[INFO] BUILD SUCCESS
[INFO] Total time: 6-8 seconds
```

**Generated File:**
```
target/smart-booking.war
```

---

### 3. Deploy to Tomcat

#### Option 1: Copy WAR File
```bash
# Linux/Mac
cp target/smart-booking.war $TOMCAT_HOME/webapps/

# Windows
copy target\smart-booking.war %TOMCAT_HOME%\webapps\
```

#### Option 2: Manual Deployment
1. Navigate to Tomcat webapps directory
2. Copy `target/smart-booking.war` to `webapps/`
3. Tomcat will auto-deploy on startup

#### Option 3: Tomcat Manager (if enabled)
1. Access: http://localhost:8080/manager
2. Upload WAR file through web interface

---

### 4. Start Tomcat

#### Linux/Mac
```bash
# Start
$TOMCAT_HOME/bin/startup.sh

# Or run in foreground
$TOMCAT_HOME/bin/catalina.sh run

# Stop
$TOMCAT_HOME/bin/shutdown.sh
```

#### Windows
```cmd
# Start
%TOMCAT_HOME%\bin\startup.bat

# Or run in foreground
%TOMCAT_HOME%\bin\catalina.bat run

# Stop
%TOMCAT_HOME%\bin\shutdown.bat
```

#### Verify Tomcat is Running
```bash
# Check if port 8080 is listening
netstat -an | grep 8080

# Or on Windows
netstat -an | findstr 8080
```

---

### 5. Access Application

#### Open in Browser
```
http://localhost:8080/smart-booking
```

#### Default Credentials
```
Email: demo@email.com
Password: password123
```

---

## Configuration (Optional)

### API Keys

The application comes with working API keys, but you can use your own:

#### MovieDB API Key
1. Get API key from: https://www.themoviedb.org/settings/api
2. Update in: `src/main/java/com/smartbooking/service/MovieDBService.java`
```java
private static final String MOVIEDB_API_KEY = "YOUR_API_KEY";
```

#### Google Maps API Key
1. Get API key from: https://console.cloud.google.com/
2. Update in: `src/main/webapp/js/app.js`
```javascript
const GOOGLE_API_KEY = "YOUR_API_KEY";
```

### Port Configuration

If port 8080 is already in use:

1. Edit `$TOMCAT_HOME/conf/server.xml`
2. Change connector port:
```xml
<Connector port="8080" protocol="HTTP/1.1" .../>
```
3. Restart Tomcat

---

## Troubleshooting

### Build Issues

#### Maven Not Found
```bash
# Add Maven to PATH
export PATH=/path/to/maven/bin:$PATH
```

#### Compilation Errors
```bash
# Ensure Java 17 is being used
mvn -version
java -version

# Clean and rebuild
mvn clean install
```

### Deployment Issues

#### WAR Not Deploying
1. Check Tomcat logs:
```bash
tail -f $TOMCAT_HOME/logs/catalina.out
```

2. Verify WAR file exists:
```bash
ls -l target/smart-booking.war
```

3. Check Tomcat webapps directory:
```bash
ls -l $TOMCAT_HOME/webapps/
```

#### Port Already in Use
```bash
# Find process using port 8080
lsof -i :8080  # Linux/Mac
netstat -ano | findstr :8080  # Windows

# Kill the process or change Tomcat port
```

### Application Issues

#### Movies Not Loading
1. Check internet connection
2. Verify MovieDB API key is valid
3. Check browser console (F12) for errors
4. Check Tomcat logs for API errors

#### Google Maps Not Showing
1. Verify Google Maps API key
2. Check if API is enabled in Google Cloud Console
3. Check browser console for errors

#### Database Errors
- H2 database is in-memory and resets on restart
- Demo user is auto-created on startup
- No manual database setup required

---

## Development Mode

### Run with Maven (Development)
```bash
# Using Maven Tomcat plugin (if configured)
mvn tomcat7:run

# Or use your IDE's built-in server
```

### Hot Reload
1. Use IDE with hot reload (IntelliJ IDEA, Eclipse)
2. Or use JRebel for instant reload
3. Or redeploy WAR after changes

### Debug Mode
```bash
# Start Tomcat in debug mode
export JPDA_ADDRESS=8000
export JPDA_TRANSPORT=dt_socket
$TOMCAT_HOME/bin/catalina.sh jpda run

# Connect debugger to port 8000
```

---

## Production Deployment

### Recommendations

1. **Use Persistent Database**
   - Switch from H2 to MySQL/PostgreSQL
   - Update DBConnection.java with production credentials

2. **Secure API Keys**
   - Use environment variables
   - Don't commit keys to repository

3. **Enable HTTPS**
   - Configure SSL certificate in Tomcat
   - Redirect HTTP to HTTPS

4. **Configure Logging**
   - Set up proper log levels
   - Use log rotation

5. **Performance Tuning**
   - Increase Tomcat memory: `-Xmx2048m`
   - Enable connection pooling
   - Add caching layer

---

## Docker Deployment (Optional)

### Create Dockerfile
```dockerfile
FROM tomcat:10.1-jdk17
COPY target/smart-booking.war /usr/local/tomcat/webapps/
EXPOSE 8080
CMD ["catalina.sh", "run"]
```

### Build and Run
```bash
# Build image
docker build -t smart-booking .

# Run container
docker run -p 8080:8080 smart-booking
```

---

## Testing the Application

### 1. Browse Movies
- Navigate to Movies section
- Try different filters (language, genre)
- Search for movies
- View movie details

### 2. Find Theatres
- Navigate to Theatres section
- View theatres on map
- Click on markers for details

### 3. Book Tickets
- Select a movie
- Click "Book Now"
- Login if needed
- Select seats
- Confirm booking

### 4. Manage Bookings
- Go to "My Bookings"
- View all bookings
- Modify seat selection
- Cancel a booking

---

## Common Commands

### Build
```bash
mvn clean package
```

### Deploy
```bash
cp target/smart-booking.war $TOMCAT_HOME/webapps/
```

### Start Server
```bash
$TOMCAT_HOME/bin/startup.sh
```

### Stop Server
```bash
$TOMCAT_HOME/bin/shutdown.sh
```

### View Logs
```bash
tail -f $TOMCAT_HOME/logs/catalina.out
```

### Check Status
```bash
curl http://localhost:8080/smart-booking
```

---

## System Requirements

### Minimum
- CPU: 2 cores
- RAM: 2 GB
- Disk: 500 MB
- OS: Windows 10, macOS 10.14, Ubuntu 18.04

### Recommended
- CPU: 4 cores
- RAM: 4 GB
- Disk: 1 GB
- OS: Latest stable version

---

## Support

### Documentation
- [README.md](README.md) - Project overview
- [DOCUMENTATION.md](DOCUMENTATION.md) - Complete guide
- [PROJECT_STRUCTURE.md](PROJECT_STRUCTURE.md) - Architecture
- [BOOKING_MANAGEMENT.md](BOOKING_MANAGEMENT.md) - Booking features
- [BOOKING_FIX.md](BOOKING_FIX.md) - Recent fixes

### Logs
```bash
# Tomcat logs
$TOMCAT_HOME/logs/catalina.out
$TOMCAT_HOME/logs/localhost.log

# Application logs
Check browser console (F12)
```

### Common Issues
1. Port 8080 in use → Change Tomcat port
2. Java version mismatch → Use Java 17
3. Maven build fails → Check dependencies
4. Movies not loading → Check API key and internet

---

## Quick Reference

| Task | Command |
|------|---------|
| Clone | `git clone https://github.com/Abhilash569/Smart-MovieBooking-System.git` |
| Build | `mvn clean package` |
| Deploy | `cp target/smart-booking.war $TOMCAT_HOME/webapps/` |
| Start | `$TOMCAT_HOME/bin/startup.sh` |
| Stop | `$TOMCAT_HOME/bin/shutdown.sh` |
| Access | `http://localhost:8080/smart-booking` |
| Login | `demo@email.com / password123` |

---

**Ready to run!** Follow the Quick Start guide above to get started in 5 minutes. 🚀
