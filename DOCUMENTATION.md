# Smart Movie Booking System - Complete Documentation

## Quick Start

### Running the Application
1. **Start Tomcat:**
   ```cmd
   apache-tomcat-10.1.18\bin\startup.bat
   ```

2. **Access Application:**
   - URL: http://localhost:8080/smart-booking
   - Demo Login: demo@email.com / password123

3. **Stop Tomcat:**
   ```cmd
   apache-tomcat-10.1.18\bin\shutdown.bat
   ```

### Building the Project
```cmd
apache-maven-3.9.6\bin\mvn.cmd clean package
copy target\smart-booking.war apache-tomcat-10.1.18\webapps\
```

## Features

### 1. Movie Browsing
- **Now Playing** movies (default view)
- Popular, Upcoming, and Top Rated movies
- Search by title
- Filter by language (English, Hindi, Tamil, Telugu, etc.)
- Filter by genre (Action, Comedy, Drama, etc.)
- Real-time data from MovieDB API

### 2. Theatre Management
- 40+ theatres across major Indian cities
- Google Maps integration for location visualization
- Find nearby theatres based on your location
- View theatre details and addresses

### 3. Booking System
- Interactive seat selection
- Real-time seat availability
- Price calculation (₹150 per seat)
- Booking confirmation

### 4. My Bookings (NEW)
- View all your bookings
- **Modify Bookings** - Change seat selection
- **Cancel Bookings** - Cancel unwanted bookings
- Status tracking (CONFIRMED, MODIFIED, CANCELLED)

### 5. User Authentication
- Login/Signup functionality
- Session management
- Secure booking operations

## API Configuration

### MovieDB API
- **API Key:** 78c9ef53b261445028575f6fe7b4552f
- **Location:** `src/main/java/com/smartbooking/service/MovieDBService.java`
- Fetches real-time movie data

### Google Maps API
- **API Key:** AIzaSyCdAOE-KDvjnttCz_VBSfRWipG0sdcCu7w
- **Location:** `src/main/webapp/js/app.js`
- Shows theatre locations on map

## Technology Stack

### Backend
- Java 17
- Jakarta Servlet 6.0
- H2 In-Memory Database
- Maven 3.9.6
- Apache Tomcat 10.1.18

### Frontend
- HTML5, CSS3, JavaScript
- Bootstrap 5.3
- Google Maps JavaScript API
- AJAX for async operations

### External APIs
- The MovieDB API (movies data)
- Google Maps API (location services)

## Database Schema

### Users Table
- id, name, email, password, created_at

### Movies Table
- id, title, genre, rating, duration, language, description, poster_url

### Theatres Table
- id, name, address, latitude, longitude

### Bookings Table
- id, user_id, movie_id, theatre_id, seats, total_price, status, booked_at

## Project Structure
```
smart-movie-booking-system/
├── src/main/
│   ├── java/com/smartbooking/
│   │   ├── controller/     # Servlets
│   │   ├── dao/            # Database access
│   │   ├── model/          # Data models
│   │   └── service/        # External API services
│   └── webapp/
│       ├── css/            # Stylesheets
│       ├── js/             # JavaScript files
│       └── index.jsp       # Main page
├── apache-maven-3.9.6/     # Maven installation
├── apache-tomcat-10.1.18/  # Tomcat server
└── pom.xml                 # Maven configuration
```

## Troubleshooting

### Movies Not Loading
- Check internet connection
- Verify MovieDB API key is valid
- Check browser console for errors

### Tomcat Not Starting
- Ensure port 8080 is not in use
- Check logs in `apache-tomcat-10.1.18/logs/`

### Database Issues
- H2 database is in-memory, resets on restart
- Demo user is auto-created on startup

## Demo Credentials
- **Email:** demo@email.com
- **Password:** password123

## Support
For issues or questions, check the logs in:
- Tomcat logs: `apache-tomcat-10.1.18/logs/catalina.out`
- Browser console (F12)
