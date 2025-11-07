# Smart Movie Booking System - Project Structure

## Directory Tree

```
Smart-MovieBooking-System/
│
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── smartbooking/
│       │           ├── controller/          # Servlet Controllers
│       │           │   ├── BookingServlet.java
│       │           │   ├── HomeServlet.java
│       │           │   ├── LoginServlet.java
│       │           │   ├── MovieServlet.java
│       │           │   └── TheatreServlet.java
│       │           │
│       │           ├── dao/                 # Data Access Objects
│       │           │   ├── BookingDAO.java
│       │           │   ├── DBConnection.java
│       │           │   ├── MovieDAO.java
│       │           │   ├── TheatreDAO.java
│       │           │   └── UserDAO.java
│       │           │
│       │           ├── model/               # Data Models
│       │           │   ├── Booking.java
│       │           │   ├── Movie.java
│       │           │   ├── Theatre.java
│       │           │   └── User.java
│       │           │
│       │           └── service/             # External API Services
│       │               ├── GoogleMapsService.java
│       │               └── MovieDBService.java
│       │
│       └── webapp/                          # Web Application Files
│           ├── WEB-INF/
│           │   └── web.xml                  # Servlet Configuration
│           │
│           ├── css/
│           │   └── style.css                # Custom Styles
│           │
│           ├── js/
│           │   ├── app.js                   # Main JavaScript
│           │   └── maps-fix.js              # Google Maps Helper
│           │
│           └── index.jsp                    # Main Page
│
├── target/                                  # Build Output (excluded)
│   └── smart-booking.war                    # Deployable WAR file
│
├── .gitignore                               # Git Ignore Rules
├── pom.xml                                  # Maven Configuration
├── init.sql                                 # Database Schema
│
└── Documentation/
    ├── README.md                            # Project Overview
    ├── DOCUMENTATION.md                     # Complete Guide
    ├── BOOKING_MANAGEMENT.md                # Booking Features
    ├── STATUS.md                            # Current Status
    ├── GITHUB_UPDATE.md                     # Repository Info
    └── PROJECT_STRUCTURE.md                 # This File
```

## Component Details

### 1. Controllers (Servlets) - `/src/main/java/com/smartbooking/controller/`

| File | URL Mapping | Purpose |
|------|-------------|---------|
| **BookingServlet.java** | `/book` | Handle booking operations (create, view, modify, cancel) |
| **HomeServlet.java** | `/home` | Serve home page |
| **LoginServlet.java** | `/login` | User authentication (login/signup) |
| **MovieServlet.java** | `/movies` | Fetch movies from MovieDB API |
| **TheatreServlet.java** | `/theatres` | Manage theatre data and locations |

### 2. Data Access Objects (DAOs) - `/src/main/java/com/smartbooking/dao/`

| File | Purpose |
|------|---------|
| **BookingDAO.java** | CRUD operations for bookings, cancel/modify methods |
| **DBConnection.java** | H2 database connection and initialization |
| **MovieDAO.java** | Movie data persistence |
| **TheatreDAO.java** | Theatre data management, nearby search |
| **UserDAO.java** | User authentication and management |

### 3. Models - `/src/main/java/com/smartbooking/model/`

| File | Fields | Description |
|------|--------|-------------|
| **Booking.java** | id, userId, movieId, theatreId, seats, totalPrice, status, bookedAt | Booking entity with status tracking |
| **Movie.java** | id, title, genre, rating, duration, language, description, posterUrl | Movie information |
| **Theatre.java** | id, name, address, latitude, longitude | Theatre location data |
| **User.java** | id, name, email, password, createdAt | User account information |

### 4. Services - `/src/main/java/com/smartbooking/service/`

| File | Purpose | API Used |
|------|---------|----------|
| **GoogleMapsService.java** | Location services, nearby theatres | Google Maps API |
| **MovieDBService.java** | Fetch movie data (now playing, popular, etc.) | The MovieDB API |

### 5. Frontend - `/src/main/webapp/`

#### Main Files
- **index.jsp** - Single-page application with all sections
- **css/style.css** - Custom styling and responsive design
- **js/app.js** - Main application logic, AJAX calls, booking management
- **js/maps-fix.js** - Google Maps initialization helper

#### Sections in index.jsp
1. Home Section
2. Movies Section (with search and filters)
3. Theatres Section (with map)
4. My Bookings Section (NEW)
5. Login/Signup Modals
6. Movie Details Modal
7. Seat Selection Modal

### 6. Configuration Files

#### pom.xml - Maven Dependencies
```xml
- Jakarta Servlet API 6.0
- H2 Database 2.2.224
- Gson 2.10.1
- Maven WAR Plugin 3.4.0
- Maven Compiler Plugin 3.11.0
```

#### web.xml - Servlet Mappings
```xml
- Welcome file: index.jsp
- Servlet configurations
- Error pages
```

#### init.sql - Database Schema
```sql
- users table
- movies table
- theatres table (40+ pre-populated)
- bookings table (with status column)
```

## Database Schema

### Tables

#### 1. users
```sql
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### 2. movies
```sql
CREATE TABLE movies (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    genre VARCHAR(50),
    rating DECIMAL(3,1),
    duration INT,
    language VARCHAR(50),
    description TEXT,
    poster_url VARCHAR(500)
);
```

#### 3. theatres
```sql
CREATE TABLE theatres (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL
);
```

#### 4. bookings
```sql
CREATE TABLE bookings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    movie_id INT NOT NULL,
    theatre_id INT NOT NULL,
    seats VARCHAR(255) NOT NULL,
    total_price DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) DEFAULT 'CONFIRMED',
    booked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (movie_id) REFERENCES movies(id),
    FOREIGN KEY (theatre_id) REFERENCES theatres(id)
);
```

## API Endpoints

### Backend REST APIs

| Method | Endpoint | Parameters | Purpose |
|--------|----------|------------|---------|
| **GET** | `/movies` | type, language, page | Fetch movies from API |
| **GET** | `/theatres` | - | Get all theatres |
| **POST** | `/theatres` | latitude, longitude, radius | Find nearby theatres |
| **POST** | `/login` | email, password | User login |
| **POST** | `/login?action=signup` | name, email, password | User signup |
| **GET** | `/book` | movieId, theatreId | Get occupied seats |
| **GET** | `/book?action=myBookings` | - | Get user's bookings |
| **POST** | `/book` | movieId, theatreId, seats | Create booking |
| **PUT** | `/book` | bookingId, seats | Modify booking |
| **DELETE** | `/book` | bookingId | Cancel booking |

## Technology Stack

### Backend
- **Language:** Java 17
- **Framework:** Jakarta Servlet 6.0
- **Database:** H2 In-Memory Database
- **Build Tool:** Maven 3.9.6
- **Server:** Apache Tomcat 10.1.18
- **JSON Processing:** Gson 2.10.1

### Frontend
- **HTML5** - Structure
- **CSS3** - Styling
- **JavaScript (ES6)** - Logic
- **Bootstrap 5.3** - UI Framework
- **AJAX** - Asynchronous requests

### External APIs
- **The MovieDB API** - Movie data
- **Google Maps JavaScript API** - Location services

## Build Process

### Maven Build Lifecycle
```
1. clean    - Remove target directory
2. compile  - Compile Java source files
3. test     - Run unit tests (none currently)
4. package  - Create WAR file
5. deploy   - Copy to Tomcat webapps
```

### Build Command
```bash
mvn clean package
```

### Output
```
target/smart-booking.war
```

## Deployment Architecture

```
┌─────────────────────────────────────────┐
│         Apache Tomcat 10.1.18           │
│  ┌───────────────────────────────────┐  │
│  │   smart-booking.war               │  │
│  │  ┌─────────────────────────────┐  │  │
│  │  │  Servlets (Controllers)     │  │  │
│  │  │  - BookingServlet           │  │  │
│  │  │  - MovieServlet             │  │  │
│  │  │  - TheatreServlet           │  │  │
│  │  │  - LoginServlet             │  │  │
│  │  └─────────────────────────────┘  │  │
│  │  ┌─────────────────────────────┐  │  │
│  │  │  DAOs (Data Access)         │  │  │
│  │  │  - BookingDAO               │  │  │
│  │  │  - MovieDAO                 │  │  │
│  │  │  - TheatreDAO               │  │  │
│  │  │  - UserDAO                  │  │  │
│  │  └─────────────────────────────┘  │  │
│  │  ┌─────────────────────────────┐  │  │
│  │  │  H2 Database (In-Memory)    │  │  │
│  │  │  - users                    │  │  │
│  │  │  - movies                   │  │  │
│  │  │  - theatres                 │  │  │
│  │  │  - bookings                 │  │  │
│  │  └─────────────────────────────┘  │  │
│  │  ┌─────────────────────────────┐  │  │
│  │  │  Frontend (JSP/JS/CSS)      │  │  │
│  │  │  - index.jsp                │  │  │
│  │  │  - app.js                   │  │  │
│  │  │  - style.css                │  │  │
│  │  └─────────────────────────────┘  │  │
│  └───────────────────────────────────┘  │
└─────────────────────────────────────────┘
           ↓                    ↑
    HTTP Requests         HTTP Responses
           ↓                    ↑
┌─────────────────────────────────────────┐
│         Web Browser (Client)            │
│  - HTML/CSS Rendering                   │
│  - JavaScript Execution                 │
│  - AJAX Calls                           │
└─────────────────────────────────────────┘
           ↓                    ↑
    External API Calls    API Responses
           ↓                    ↑
┌─────────────────────────────────────────┐
│         External Services               │
│  - The MovieDB API                      │
│  - Google Maps API                      │
└─────────────────────────────────────────┘
```

## Data Flow

### 1. Movie Browsing Flow
```
User → Browser → MovieServlet → MovieDBService → MovieDB API
                                      ↓
                                  MovieDAO
                                      ↓
                                 H2 Database
                                      ↓
                                  Response → Browser
```

### 2. Booking Flow
```
User → Login → Select Movie → Select Theatre → Choose Seats
                                                      ↓
                                              BookingServlet
                                                      ↓
                                               BookingDAO
                                                      ↓
                                              H2 Database
                                                      ↓
                                            Confirmation → User
```

### 3. Booking Management Flow
```
User → My Bookings → View/Modify/Cancel
                            ↓
                     BookingServlet (PUT/DELETE)
                            ↓
                       BookingDAO
                            ↓
                      Update Status
                            ↓
                       H2 Database
                            ↓
                    Updated List → User
```

## File Size Summary

| Category | Files | Approx. Lines |
|----------|-------|---------------|
| Controllers | 5 | ~800 |
| DAOs | 5 | ~600 |
| Models | 4 | ~300 |
| Services | 2 | ~200 |
| Frontend (JSP) | 1 | ~350 |
| Frontend (JS) | 2 | ~700 |
| Frontend (CSS) | 1 | ~400 |
| Configuration | 3 | ~200 |
| **Total** | **23** | **~3,550** |

## Key Features by Component

### BookingServlet.java
- ✅ Create new bookings
- ✅ Get occupied seats
- ✅ View user bookings
- ✅ Modify seat selection
- ✅ Cancel bookings

### app.js
- ✅ Movie search and filtering
- ✅ Theatre map integration
- ✅ Seat selection UI
- ✅ Booking management UI
- ✅ AJAX communication

### DBConnection.java
- ✅ H2 database initialization
- ✅ Auto-create tables
- ✅ Pre-populate 40+ theatres
- ✅ Connection pooling

## Getting Started

### 1. Clone Repository
```bash
git clone https://github.com/Abhilash569/Smart-MovieBooking-System.git
cd Smart-MovieBooking-System
```

### 2. Build Project
```bash
mvn clean package
```

### 3. Deploy to Tomcat
```bash
cp target/smart-booking.war $TOMCAT_HOME/webapps/
```

### 4. Start Server
```bash
$TOMCAT_HOME/bin/startup.sh  # Linux/Mac
$TOMCAT_HOME/bin/startup.bat # Windows
```

### 5. Access Application
```
http://localhost:8080/smart-booking
```

---

**For detailed documentation, see:**
- [README.md](README.md) - Project overview
- [DOCUMENTATION.md](DOCUMENTATION.md) - Complete guide
- [BOOKING_MANAGEMENT.md](BOOKING_MANAGEMENT.md) - Booking features
