package com.smartbooking.dao;

import com.smartbooking.model.Movie;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Movie operations
 */
public class MovieDAO {
    
    /**
     * Find all movies
     */
    public List<Movie> findAll() {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT * FROM movies";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                movies.add(new Movie(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("genre"),
                    rs.getDouble("rating"),
                    rs.getInt("duration"),
                    rs.getString("language"),
                    rs.getString("description"),
                    rs.getString("poster_url")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movies;
    }
    
    /**
     * Find movie by ID
     */
    public Movie findById(int id) {
        String sql = "SELECT * FROM movies WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new Movie(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("genre"),
                    rs.getDouble("rating"),
                    rs.getInt("duration"),
                    rs.getString("language"),
                    rs.getString("description"),
                    rs.getString("poster_url")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Create a new movie
     */
    public boolean create(Movie movie) {
        String sql = "INSERT INTO movies (title, genre, rating, duration, language, description, poster_url) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, movie.getTitle());
            stmt.setString(2, movie.getGenre());
            stmt.setDouble(3, movie.getRating());
            stmt.setInt(4, movie.getDuration());
            stmt.setString(5, movie.getLanguage());
            stmt.setString(6, movie.getDescription());
            stmt.setString(7, movie.getPosterUrl());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Bulk insert movies using batch processing
     */
    public boolean createBatch(List<Movie> movies) {
        String sql = "MERGE INTO movies (id, title, genre, rating, duration, language, description, poster_url) " +
                     "KEY(id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            conn.setAutoCommit(false);
            
            for (Movie movie : movies) {
                stmt.setInt(1, movie.getId());
                stmt.setString(2, movie.getTitle());
                stmt.setString(3, movie.getGenre());
                stmt.setDouble(4, movie.getRating());
                stmt.setInt(5, movie.getDuration());
                stmt.setString(6, movie.getLanguage());
                stmt.setString(7, movie.getDescription());
                stmt.setString(8, movie.getPosterUrl());
                stmt.addBatch();
            }
            
            stmt.executeBatch();
            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Create or update a movie with specific ID
     */
    public boolean createOrUpdate(Movie movie) {
        String sql = "MERGE INTO movies (id, title, genre, rating, duration, language, description, poster_url) " +
                     "KEY(id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, movie.getId());
            stmt.setString(2, movie.getTitle());
            stmt.setString(3, movie.getGenre());
            stmt.setDouble(4, movie.getRating());
            stmt.setInt(5, movie.getDuration());
            stmt.setString(6, movie.getLanguage());
            stmt.setString(7, movie.getDescription());
            stmt.setString(8, movie.getPosterUrl());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
