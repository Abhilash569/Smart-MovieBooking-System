package com.smartbooking.dao;

import com.smartbooking.model.Theatre;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Theatre operations
 */
public class TheatreDAO {
    
    /**
     * Find all theatres
     */
    public List<Theatre> findAll() {
        List<Theatre> theatres = new ArrayList<>();
        String sql = "SELECT * FROM theatres";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                theatres.add(new Theatre(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("address"),
                    rs.getDouble("latitude"),
                    rs.getDouble("longitude")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return theatres;
    }
    
    /**
     * Find theatre by ID
     */
    public Theatre findById(int id) {
        String sql = "SELECT * FROM theatres WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new Theatre(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("address"),
                    rs.getDouble("latitude"),
                    rs.getDouble("longitude")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Find nearby theatres using Haversine formula
     * @param lat User's latitude
     * @param lng User's longitude
     * @param radius Search radius in kilometers
     */
    public List<Theatre> findNearby(double lat, double lng, double radius) {
        List<Theatre> theatres = new ArrayList<>();
        // Haversine formula in SQL to calculate distance
        String sql = "SELECT id, name, address, latitude, longitude, " +
                     "(6371 * acos(cos(radians(?)) * cos(radians(latitude)) * " +
                     "cos(radians(longitude) - radians(?)) + sin(radians(?)) * " +
                     "sin(radians(latitude)))) AS distance " +
                     "FROM theatres " +
                     "WHERE (6371 * acos(cos(radians(?)) * cos(radians(latitude)) * " +
                     "cos(radians(longitude) - radians(?)) + sin(radians(?)) * " +
                     "sin(radians(latitude)))) < ? " +
                     "ORDER BY distance";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDouble(1, lat);
            stmt.setDouble(2, lng);
            stmt.setDouble(3, lat);
            stmt.setDouble(4, lat);
            stmt.setDouble(5, lng);
            stmt.setDouble(6, lat);
            stmt.setDouble(7, radius);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                theatres.add(new Theatre(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("address"),
                    rs.getDouble("latitude"),
                    rs.getDouble("longitude")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return theatres;
    }
    
    /**
     * Create a new theatre
     */
    public boolean create(Theatre theatre) {
        String sql = "INSERT INTO theatres (name, address, latitude, longitude) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, theatre.getName());
            stmt.setString(2, theatre.getAddress());
            stmt.setDouble(3, theatre.getLatitude());
            stmt.setDouble(4, theatre.getLongitude());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
