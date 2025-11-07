package com.smartbooking.model;

import java.sql.Timestamp;

/**
 * Booking model representing a movie ticket booking in the system
 */
public class Booking {
    private int id;
    private int userId;
    private int movieId;
    private int theatreId;
    private String seats;
    private double totalPrice;
    private Timestamp bookedAt;
    private String status; // CONFIRMED, CANCELLED, MODIFIED
    private String showTime; // Show time selected by user
    
    // Default constructor
    public Booking() {
    }
    
    // Constructor with all fields
    public Booking(int id, int userId, int movieId, int theatreId, 
                   String seats, double totalPrice, Timestamp bookedAt, String status) {
        this.id = id;
        this.userId = userId;
        this.movieId = movieId;
        this.theatreId = theatreId;
        this.seats = seats;
        this.totalPrice = totalPrice;
        this.bookedAt = bookedAt;
        this.status = status;
    }
    
    // Constructor without id (for new bookings)
    public Booking(int userId, int movieId, int theatreId, String seats, double totalPrice) {
        this.userId = userId;
        this.movieId = movieId;
        this.theatreId = theatreId;
        this.seats = seats;
        this.totalPrice = totalPrice;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public int getMovieId() {
        return movieId;
    }
    
    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }
    
    public int getTheatreId() {
        return theatreId;
    }
    
    public void setTheatreId(int theatreId) {
        this.theatreId = theatreId;
    }
    
    public String getSeats() {
        return seats;
    }
    
    public void setSeats(String seats) {
        this.seats = seats;
    }
    
    public double getTotalPrice() {
        return totalPrice;
    }
    
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
    
    public Timestamp getBookedAt() {
        return bookedAt;
    }
    
    public void setBookedAt(Timestamp bookedAt) {
        this.bookedAt = bookedAt;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getShowTime() {
        return showTime;
    }
    
    public void setShowTime(String showTime) {
        this.showTime = showTime;
    }
}
