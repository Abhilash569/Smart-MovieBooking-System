# Smart Movie Booking System

A full-stack web application for browsing movies, finding nearby theatres, and booking movie tickets online.

## Features

- 🎬 Browse movies (Now Playing, Popular, Upcoming, Top Rated) from The MovieDB API
- 🔍 Search and filter movies by language and genre
- 🗺️ Find nearby theatres using Google Maps integration (40+ theatres across India)
- 🎫 Interactive seat selection and booking
- 📋 **NEW:** My Bookings - View, modify, and cancel bookings
- 👤 User authentication (Login/Signup)
- 📱 Responsive design for mobile and desktop
- 💾 H2 in-memory database (no installation required!)

## Technology Stack

### Frontend
- HTML5, CSS3, Bootstrap 5.3
- JavaScript (ES6)
- Google Maps API

### Backend
- Java 17+
- Jakarta EE 10 (Servlets, JSP)
- Apache Tomcat 10.1+
- Maven 3.8+

### Database
- H2 In-Memory Database (no installation required!)
- JDBC for database connectivity

### External APIs
- The MovieDB API (for movie data)
- Google Maps API (for theatre locations)

## Prerequisites

Before running this application, ensure you have the following installed:

- **Java Development Kit (JDK) 17 or higher**
- **Apache Maven 3.6+**
- **Apache Tomcat 10.1+**

**Note:** No database installation required! The application uses H2 in-memory database.

## Setup Instructions

### 1. Clone the Repository

```bash
git clone <repository-url>
cd smart-movie-booking-system
```

### 2. Configure API Keys (Optional)

The application comes with working API keys, but you can use your own:

#### The MovieDB API Key

1. Get your API key from [The MovieDB](https://www.themoviedb.org/settings/api)
2. Set the environment variable:
```bash
export MOVIEDB_API_KEY=your_moviedb_api_key
```

Or update in `src/main/java/com/smartbooking/service/MovieDBService.java`:
```java
private static final String MOVIEDB_API_KEY = "YOUR_MOVIE_API_KEY";
```

#### Google Maps API Key

1. Get your API key from [Google Cloud Console](https://console.cloud.google.com/)
2. Update in `src/main/webapp/js/app.js`:
```javascript
const GOOGLE_API_KEY = "YOUR_GOOGLE_API_KEY";
```

### 3. Build the Project

```bash
mvn clean package
```

This will create a WAR file at `target/smart-booking.war`

### 4. Deploy to Tomcat

1. Copy the WAR file to Tomcat's webapps directory:
```bash
cp target/smart-booking.war /path/to/tomcat/webapps/
```

2. Start Tomcat:
```bash
/path/to/tomcat/bin/startup.sh  # Linux/Mac
/path/to/tomcat/bin/startup.bat # Windows
```

3. Wait for Tomcat to deploy the application

### 5. Access the Application

Open your browser and navigate to:
```
http://localhost:8080/smart-booking
```

## Demo Credentials

Use these credentials to test the application:

- **Email:** demo@email.com
- **Password:** password123

## Project Structure

For a detailed project structure with all files and components, see [PROJECT_STRUCTURE.md](PROJECT_STRUCTURE.md)

```
smart-movie-booking-system/
├── src/
│   ├── main/
│   │   ├── java/com/smartbooking/
│   │   │   ├── controller/      # 5 Servlets (Booking, Login, Movie, Theatre, Home)
│   │   │   ├── model/           # 4 Data models (User, Movie, Theatre, Booking)
│   │   │   ├── dao/             # 5 Data Access Objects + DBConnection
│   │   │   └── service/         # 2 External API services (MovieDB, Google Maps)
│   │   └── webapp/
│   │       ├── WEB-INF/
│   │       │   └── web.xml      # Servlet configuration
│   │       ├── css/
│   │       │   └── style.css    # Custom styles
│   │       ├── js/
│   │       │   ├── app.js       # Main JavaScript (with booking management)
│   │       │   └── maps-fix.js  # Google Maps helper
│   │       └── index.jsp        # Single-page application
├── init.sql                     # Database initialization (H2)
├── pom.xml                      # Maven configuration
├── README.md                    # This file
├── DOCUMENTATION.md             # Complete usage guide
├── BOOKING_MANAGEMENT.md        # Booking features documentation
├── PROJECT_STRUCTURE.md         # Detailed project structure
└── STATUS.md                    # Current status
```

## API Endpoints

### Movies
- `GET /movies` - Get all movies

### Theatres
- `GET /theatres` - Get all theatres
- `POST /theatres` - Find nearby theatres (params: latitude, longitude, radius)

### Authentication
- `POST /login` - Login or signup (params: action, email, password, name)

### Booking
- `GET /book` - Get occupied seats (params: movieId, theatreId)
- `GET /book?action=myBookings` - Get user's bookings
- `POST /book` - Create booking (params: movieId, theatreId, seats)
- `PUT /book` - Modify booking (params: bookingId, seats)
- `DELETE /book` - Cancel booking (params: bookingId)

## Database Schema

### users
- id, name, email (unique), password, created_at

### movies
- id, title, genre, rating, duration, language, description, poster_url, created_at

### theatres
- id, name, address, latitude, longitude, created_at

### bookings
- id, user_id, movie_id, theatre_id, seats, total_price, status, booked_at

## Troubleshooting

### Database Issues
- H2 database is in-memory and resets on server restart
- Demo user is auto-created on startup
- 40+ theatres are pre-populated automatically

### Tomcat Deployment Issues
- Check Tomcat logs: `tail -f /path/to/tomcat/logs/catalina.out`
- Verify Java version: `java -version` (should be 17+)
- Ensure port 8080 is not in use

### API Issues
- Verify API keys are correctly configured
- Check internet connectivity for external API calls
- Review browser console for JavaScript errors

## Future Enhancements

- Payment gateway integration
- Email notifications for bookings
- Admin panel for managing movies and theatres
- Movie showtimes and scheduling
- User reviews and ratings
- Social media sharing
- Mobile app (iOS/Android)
- Real-time seat updates using WebSockets

## License

This project is for educational purposes.

## Contact

For questions or support, please contact the development team.
