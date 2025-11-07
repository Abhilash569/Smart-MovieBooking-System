package com.smartbooking.model;

/**
 * Movie model representing a movie in the system
 */
public class Movie {
    private int id;
    private String title;
    private String genre;
    private double rating;
    private int duration;
    private String language;
    private String description;
    private String posterUrl;
    
    // Default constructor
    public Movie() {
    }
    
    // Constructor with all fields
    public Movie(int id, String title, String genre, double rating, int duration, 
                 String language, String description, String posterUrl) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.rating = rating;
        this.duration = duration;
        this.language = language;
        this.description = description;
        this.posterUrl = posterUrl;
    }
    
    // Constructor without id (for new movies)
    public Movie(String title, String genre, double rating, int duration, 
                 String language, String description, String posterUrl) {
        this.title = title;
        this.genre = genre;
        this.rating = rating;
        this.duration = duration;
        this.language = language;
        this.description = description;
        this.posterUrl = posterUrl;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getGenre() {
        return genre;
    }
    
    public void setGenre(String genre) {
        this.genre = genre;
    }
    
    public double getRating() {
        return rating;
    }
    
    public void setRating(double rating) {
        this.rating = rating;
    }
    
    public int getDuration() {
        return duration;
    }
    
    public void setDuration(int duration) {
        this.duration = duration;
    }
    
    public String getLanguage() {
        return language;
    }
    
    public void setLanguage(String language) {
        this.language = language;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getPosterUrl() {
        return posterUrl;
    }
    
    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }
}
