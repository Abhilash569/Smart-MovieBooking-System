package com.smartbooking.dao;

import com.smartbooking.model.Booking;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Booking operations
 */
public class BookingDAO {
    
    /**
     * Create a new booking
     */
    public boolean create(Booking booking) {
        String sql = "INSERT INTO bookings (user_id, movie_id, theatre_id, seats, total_price, show_time, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, 'CONFIRMED')";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, booking.getUserId());
            stmt.setInt(2, booking.getMovieId());
            stmt.setInt(3, booking.getTheatreId());
            stmt.setString(4, booking.getSeats());
            stmt.setDouble(5, booking.getTotalPrice());
            stmt.setString(6, booking.getShowTime());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Find all bookings for a specific user
     */
    public List<Booking> findByUserId(int userId) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings WHERE user_id = ? ORDER BY booked_at DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                bookings.add(new Booking(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getInt("movie_id"),
                    rs.getInt("theatre_id"),
                    rs.getString("seats"),
                    rs.getDouble("total_price"),
                    rs.getTimestamp("booked_at"),
                    rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }
    
    /**
     * Get occupied seats for a specific movie and theatre (excluding cancelled bookings)
     */
    public List<String> getOccupiedSeats(int movieId, int theatreId) {
        List<String> occupiedSeats = new ArrayList<>();
        String sql = "SELECT seats FROM bookings WHERE movie_id = ? AND theatre_id = ? AND status != 'CANCELLED'";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, movieId);
            stmt.setInt(2, theatreId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                String seats = rs.getString("seats");
                if (seats != null && !seats.isEmpty()) {
                    String[] seatArray = seats.split(",");
                    for (String seat : seatArray) {
                        occupiedSeats.add(seat.trim());
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return occupiedSeats;
    }
    
    /**
     * Find booking by ID
     */
    public Booking findById(int id) {
        String sql = "SELECT * FROM bookings WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new Booking(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getInt("movie_id"),
                    rs.getInt("theatre_id"),
                    rs.getString("seats"),
                    rs.getDouble("total_price"),
                    rs.getTimestamp("booked_at"),
                    rs.getString("status")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Cancel a booking
     */
    public boolean cancel(int bookingId, int userId) {
        String sql = "UPDATE bookings SET status = 'CANCELLED' WHERE id = ? AND user_id = ? AND status = 'CONFIRMED'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, bookingId);
            stmt.setInt(2, userId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Modify booking seats
     */
    public boolean modifySeats(int bookingId, int userId, String newSeats, double newTotalPrice) {
        String sql = "UPDATE bookings SET seats = ?, total_price = ?, status = 'MODIFIED' WHERE id = ? AND user_id = ? AND status != 'CANCELLED'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, newSeats);
            stmt.setDouble(2, newTotalPrice);
            stmt.setInt(3, bookingId);
            stmt.setInt(4, userId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Change theatre for a booking
     */
    public boolean changeTheatre(int bookingId, int userId, int newTheatreId, String newShowTime) {
        String sql = "UPDATE bookings SET theatre_id = ?, show_time = ?, status = 'MODIFIED' WHERE id = ? AND user_id = ? AND status != 'CANCELLED'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, newTheatreId);
            stmt.setString(2, newShowTime);
            stmt.setInt(3, bookingId);
            stmt.setInt(4, userId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Change movie for a booking
     */
    public boolean changeMovie(int bookingId, int userId, int newMovieId) {
        String sql = "UPDATE bookings SET movie_id = ?, status = 'MODIFIED' WHERE id = ? AND user_id = ? AND status != 'CANCELLED'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, newMovieId);
            stmt.setInt(2, bookingId);
            stmt.setInt(3, userId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
