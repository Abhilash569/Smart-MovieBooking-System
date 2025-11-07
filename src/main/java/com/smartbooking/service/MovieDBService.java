package com.smartbooking.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.smartbooking.model.Movie;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for interacting with The MovieDB API
 */
public class MovieDBService {
    
    private static final String MOVIEDB_API_KEY = "78c9ef53b261445028575f6fe7b4552f";
    private static final String MOVIEDB_BASE_URL = "https://api.themoviedb.org/3";
    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";
    private static final String BEARER_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI3OGM5ZWY1M2IyNjE0NDUwMjg1NzVmNmZlN2I0NTUyZiIsIm5iZiI6MTc2MjQzMTg4Ni40MDIsInN1YiI6IjY5MGM5MzhlZTJkYWNiZjQ5YWY2ZDQ4NyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.ANXPpkyWXvmuEkSOwUgn87WqBROCqtYgG-wBbc0v0Dk";
    private static final int TIMEOUT = 10000;
    
    /**
     * Fetch movies from MovieDB API by type
     */
    public List<Movie> fetchMovies(String type, int page, String language) {
        List<Movie> movies = new ArrayList<>();
        
        try {
            String endpoint = "/movie/" + (type != null ? type : "popular");
            String langParam = (language != null && !language.isEmpty()) ? "&language=" + language : "";
            String urlString = MOVIEDB_BASE_URL + endpoint + "?api_key=" + MOVIEDB_API_KEY + "&page=" + page + langParam;
            
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + BEARER_TOKEN);
            conn.setRequestProperty("accept", "application/json");
            conn.setConnectTimeout(TIMEOUT);
            conn.setReadTimeout(TIMEOUT);
            
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                
                JsonObject jsonObject = JsonParser.parseString(response.toString()).getAsJsonObject();
                JsonArray results = jsonObject.getAsJsonArray("results");
                
                for (JsonElement element : results) {
                    JsonObject movieJson = element.getAsJsonObject();
                    
                    Movie movie = new Movie();
                    movie.setTitle(movieJson.get("title").getAsString());
                    movie.setDescription(movieJson.has("overview") ? movieJson.get("overview").getAsString() : "");
                    movie.setRating(movieJson.has("vote_average") ? movieJson.get("vote_average").getAsDouble() : 0.0);
                    movie.setLanguage(movieJson.has("original_language") ? movieJson.get("original_language").getAsString().toUpperCase() : "EN");
                    
                    if (movieJson.has("poster_path") && !movieJson.get("poster_path").isJsonNull()) {
                        String posterPath = movieJson.get("poster_path").getAsString();
                        movie.setPosterUrl(buildImageUrl(posterPath));
                    }
                    
                    if (movieJson.has("genre_ids") && movieJson.get("genre_ids").getAsJsonArray().size() > 0) {
                        movie.setGenre(getGenreName(movieJson.get("genre_ids").getAsJsonArray().get(0).getAsInt()));
                    } else {
                        movie.setGenre("Unknown");
                    }
                    
                    movie.setDuration(120);
                    movies.add(movie);
                }
            }
        } catch (Exception e) {
            System.err.println("Error fetching movies: " + e.getMessage());
            e.printStackTrace();
        }
        
        return movies;
    }
    
    /**
     * Fetch popular movies (backward compatibility)
     */
    public List<Movie> fetchPopularMovies(int page) {
        return fetchMovies("popular", page, null);
    }
    
    /**
     * Build full image URL from poster path
     */
    public String buildImageUrl(String posterPath) {
        if (posterPath == null || posterPath.isEmpty()) {
            return "";
        }
        return IMAGE_BASE_URL + posterPath;
    }
    
    /**
     * Get genre name from genre ID
     */
    private String getGenreName(int genreId) {
        switch (genreId) {
            case 28: return "Action";
            case 12: return "Adventure";
            case 16: return "Animation";
            case 35: return "Comedy";
            case 80: return "Crime";
            case 99: return "Documentary";
            case 18: return "Drama";
            case 10751: return "Family";
            case 14: return "Fantasy";
            case 36: return "History";
            case 27: return "Horror";
            case 10402: return "Music";
            case 9648: return "Mystery";
            case 10749: return "Romance";
            case 878: return "Science Fiction";
            case 10770: return "TV Movie";
            case 53: return "Thriller";
            case 10752: return "War";
            case 37: return "Western";
            default: return "Unknown";
        }
    }
}
