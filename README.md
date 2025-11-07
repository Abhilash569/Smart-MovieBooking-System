# Smart Movie Booking System

A full-stack web application for browsing movies, finding nearby theatres, and booking movie tickets online.

## Features

- 🎬 Browse popular movies with details from The MovieDB API
- 🗺️ Find nearby theatres using Google Maps integration
- 🎫 Interactive seat selection and booking
- 👤 User authentication (Login/Signup)
- 📱 Responsive design for mobile and desktop
- 💾 MySQL database for data persistence

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
- MySQL 8.0+
- JDBC for database connectivity

### External APIs
- The MovieDB API (for movie data)
- Google Maps API (for theatre locations)

## Prerequisites

Before running this application, ensure you have the following installed:

- **Java Development Kit (JDK) 17 or higher**
- **Apache Maven 3.8+**
- **MySQL 8.0+**
- **Apache Tomcat 10.1+**

## Setup Instructions

### 1. Clone the Repository

```bash
git clone <repository-url>
cd smart-movie-booking-system
```

### 2. Database Setup

1. Start your MySQL server

2. Run the initialization script:
```bash
mysql -u root -p < init.sql
```

This will:
- Create the `smartmoviebooking` database
- Create all required tables (users, movies, theatres, bookings)
- Insert sample theatre data
- Create a demo user account

### 3. Configure Database Connection

Update the database credentials in `src/main/java/com/smartbooking/dao/DBConnection.java`:

```java
private static final String URL = "jdbc:mysql://localhost:3306/smartmoviebooking";
private static final String USER = "root";
private static final String PASSWORD = "your_password"; // Update this
```

Or set the `DB_PASSWORD` environment variable:
```bash
export DB_PASSWORD=your_password
```

### 4. Configure API Keys

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

### 5. Build the Project

```bash
mvn clean package
```

This will create a WAR file at `target/smart-booking.war`

### 6. Deploy to Tomcat

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

### 7. Access the Application

Open your browser and navigate to:
```
http://localhost:8080/smart-booking
```

## Demo Credentials

Use these credentials to test the application:

- **Email:** demo@email.com
- **Password:** password123

## Project Structure

```
smart-movie-booking-system/
├── src/
│   ├── main/
│   │   ├── java/com/smartbooking/
│   │   │   ├── controller/      # Servlets
│   │   │   ├── model/           # Data models
│   │   │   ├── dao/             # Data Access Objects
│   │   │   └── service/         # External API services
│   │   └── webapp/
│   │       ├── WEB-INF/
│   │       │   └── web.xml      # Servlet configuration
│   │       ├── css/
│   │       │   └── style.css    # Custom styles
│   │       ├── js/
│   │       │   └── app.js       # Frontend JavaScript
│   │       └── index.jsp        # Main page
├── init.sql                     # Database initialization
├── pom.xml                      # Maven configuration
└── README.md                    # This file
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
- `POST /book` - Create booking (params: movieId, theatreId, seats)

## Database Schema

### users
- id, name, email (unique), password, created_at

### movies
- id, title, genre, rating, duration, language, description, poster_url, created_at

### theatres
- id, name, address, latitude, longitude, created_at

### bookings
- id, user_id, movie_id, theatre_id, seats, total_price, booked_at

## Troubleshooting

### Database Connection Issues
- Verify MySQL is running
- Check database credentials in DBConnection.java
- Ensure the database exists: `SHOW DATABASES;`

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
