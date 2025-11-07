package com.smartbooking.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Database connection utility class for managing H2 in-memory database
 */
public class DBConnection {
    
    // Using H2 in-memory database (no installation required!)
    private static final String URL = "jdbc:h2:mem:smartmoviebooking;DB_CLOSE_DELAY=-1;MODE=MySQL";
    private static final String USER = "sa";
    private static final String PASSWORD = "";
    private static final String DRIVER = "org.h2.Driver";
    
    private static boolean initialized = false;
    
    static {
        try {
            // Load H2 JDBC driver
            Class.forName(DRIVER);
            // Initialize database schema
            initializeDatabase();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("H2 JDBC Driver not found", e);
        }
    }
    
    /**
     * Initialize database schema and sample data
     */
    private static void initializeDatabase() {
        if (initialized) return;
        
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {
            
            // Create tables
            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "name VARCHAR(100) NOT NULL, " +
                    "email VARCHAR(100) UNIQUE NOT NULL, " +
                    "password VARCHAR(100) NOT NULL, " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
            
            stmt.execute("CREATE TABLE IF NOT EXISTS movies (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "title VARCHAR(255) NOT NULL, " +
                    "genre VARCHAR(50), " +
                    "rating DECIMAL(3,1), " +
                    "duration INT, " +
                    "language VARCHAR(50), " +
                    "description TEXT, " +
                    "poster_url VARCHAR(500))");
            
            stmt.execute("CREATE TABLE IF NOT EXISTS theatres (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "name VARCHAR(255) NOT NULL, " +
                    "address VARCHAR(255) NOT NULL, " +
                    "latitude DOUBLE NOT NULL, " +
                    "longitude DOUBLE NOT NULL)");
            
            stmt.execute("CREATE TABLE IF NOT EXISTS bookings (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "user_id INT NOT NULL, " +
                    "movie_id INT NOT NULL, " +
                    "theatre_id INT NOT NULL, " +
                    "seats VARCHAR(255) NOT NULL, " +
                    "total_price DECIMAL(10,2) NOT NULL, " +
                    "status VARCHAR(20) DEFAULT 'CONFIRMED', " +
                    "show_time VARCHAR(20), " +
                    "booked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY (user_id) REFERENCES users(id), " +
                    "FOREIGN KEY (movie_id) REFERENCES movies(id), " +
                    "FOREIGN KEY (theatre_id) REFERENCES theatres(id))");
            
            // Insert demo user
            stmt.execute("INSERT INTO users (name, email, password) VALUES " +
                    "('Demo User', 'demo@email.com', 'password123')");
            
            // Insert sample theatres across India
            stmt.execute("INSERT INTO theatres (name, address, latitude, longitude) VALUES " +
                    // Mumbai
                    "('PVR Cinemas Phoenix', 'Phoenix Marketcity, Mumbai', 19.0896, 72.8869), " +
                    "('INOX Megaplex', 'Inorbit Mall, Mumbai', 19.1758, 72.9476), " +
                    "('Cinepolis Fun Republic', 'Andheri West, Mumbai', 19.1368, 72.8261), " +
                    "('Carnival Cinemas', 'Wadala, Mumbai', 19.0176, 72.8561), " +
                    "('MovieMax Multiplex', 'Thane West, Mumbai', 19.2183, 72.9781), " +
                    // Delhi NCR
                    "('PVR Rivoli', 'Connaught Place, Delhi', 28.7041, 77.1025), " +
                    "('INOX Insignia', 'DLF Mall, Noida', 28.5355, 77.3910), " +
                    "('Cinepolis DLF', 'Cyber City, Gurgaon', 28.4595, 77.0266), " +
                    "('PVR Select City Walk', 'Saket, Delhi', 28.6139, 77.2090), " +
                    "('Wave Cinemas', 'Ghaziabad', 28.6692, 77.4538), " +
                    // Bangalore
                    "('PVR Forum Mall', 'Koramangala, Bangalore', 12.9352, 77.6245), " +
                    "('INOX Garuda Mall', 'MG Road, Bangalore', 12.9716, 77.5946), " +
                    "('Cinepolis Nexus', 'Whitefield, Bangalore', 12.9698, 77.7500), " +
                    "('PVR Orion Mall', 'Rajajinagar, Bangalore', 13.0358, 77.5970), " +
                    "('Cinepolis Central', 'Electronic City, Bangalore', 12.9899, 77.7273), " +
                    // Hyderabad
                    "('PVR Inorbit', 'HITEC City, Hyderabad', 17.4400, 78.3489), " +
                    "('AMB Cinemas', 'Gachibowli, Hyderabad', 17.3850, 78.4867), " +
                    "('Cinepolis Mantra', 'Banjara Hills, Hyderabad', 17.4239, 78.4738), " +
                    "('PVR Next Galleria', 'Punjagutta, Hyderabad', 17.4065, 78.4772), " +
                    // Hyderabad - Bachupally, Kukatpally, KPHB, Pragathi Nagar
                    "('PVR Kukatpally', 'Kukatpally, Hyderabad', 17.4849, 78.3915), " +
                    "('INOX Kukatpally', 'Kukatpally Housing Board, Hyderabad', 17.4945, 78.3869), " +
                    "('Asian Cinemas', 'KPHB Colony, Hyderabad', 17.4920, 78.3910), " +
                    "('Cinepolis Bachupally', 'Bachupally, Hyderabad', 17.5450, 78.3850), " +
                    "('Prasads Multiplex', 'KPHB, Hyderabad', 17.4890, 78.3920), " +
                    "('Miraj Cinemas', 'Kukatpally, Hyderabad', 17.4870, 78.3895), " +
                    "('PVR KPHB', 'KPHB Phase 1, Hyderabad', 17.4910, 78.3905), " +
                    "('Cinepolis Miyapur', 'Miyapur, Hyderabad', 17.4950, 78.3580), " +
                    "('Cinepolis Pragathi Nagar', 'Pragathi Nagar, Hyderabad', 17.5380, 78.3920), " +
                    "('PVR Pragathi Nagar', 'Pragathi Nagar, Hyderabad', 17.5400, 78.3900), " +
                    "('INOX Bachupally', 'Bachupally X Roads, Hyderabad', 17.5420, 78.3880), " +
                    // Chennai
                    "('PVR Grand Galada', 'Pallavaram, Chennai', 13.0827, 80.2707), " +
                    "('INOX Escape', 'Express Avenue, Chennai', 13.0569, 80.2425), " +
                    "('AGS Cinemas', 'Velachery, Chennai', 12.9822, 80.2210), " +
                    "('Sathyam Cinemas', 'Royapettah, Chennai', 13.0475, 80.2400), " +
                    // Pune
                    "('PVR Phoenix Market', 'Viman Nagar, Pune', 18.5679, 73.9143), " +
                    "('INOX Bund Garden', 'Bund Garden Road, Pune', 18.5204, 73.8567), " +
                    "('Cinepolis Seasons', 'Magarpatta, Pune', 18.5362, 73.9264), " +
                    "('E-Square Multiplex', 'Kothrud, Pune', 18.5089, 73.8055), " +
                    // Kolkata
                    "('PVR South City', 'Prince Anwar Shah Road, Kolkata', 22.5726, 88.3639), " +
                    "('INOX Quest Mall', 'Park Circus, Kolkata', 22.5355, 88.3634), " +
                    "('Cinepolis Lake Mall', 'Salt Lake, Kolkata', 22.5726, 88.4309), " +
                    "('PVR Mani Square', 'EM Bypass, Kolkata', 22.5448, 88.3426), " +
                    // Ahmedabad
                    "('PVR Acropolis', 'Thaltej, Ahmedabad', 23.0395, 72.5066), " +
                    "('Cinepolis Ahmedabad One', 'Vastrapur, Ahmedabad', 23.0732, 72.5310), " +
                    "('Rajhans Cinemas', 'Satellite, Ahmedabad', 23.0225, 72.5714), " +
                    // Other Cities
                    "('Raj Mandir Cinema', 'MI Road, Jaipur', 26.9124, 75.7873), " +
                    "('PVR Treasure Island', 'Indora, Nagpur', 21.1458, 79.0882), " +
                    "('INOX City Centre', 'MP Nagar, Bhopal', 23.2599, 77.4126), " +
                    "('Elante Mall PVR', 'Industrial Area, Chandigarh', 30.7333, 76.7794)");
            
            initialized = true;
            System.out.println("✅ H2 Database initialized successfully!");
            
        } catch (SQLException e) {
            System.err.println("❌ Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Get a database connection
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
    /**
     * Close database connection and release resources
     * @param connection Connection to close
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
