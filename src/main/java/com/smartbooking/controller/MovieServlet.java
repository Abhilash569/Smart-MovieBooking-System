package com.smartbooking.controller;

import com.google.gson.Gson;
import com.smartbooking.dao.MovieDAO;
import com.smartbooking.model.Movie;
import com.smartbooking.service.MovieDBService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Movie servlet - handles movie-related requests
 */
@WebServlet("/movies")
public class MovieServlet extends HttpServlet {
    
    private MovieDAO movieDAO = new MovieDAO();
    private MovieDBService movieDBService = new MovieDBService();
    private Gson gson = new Gson();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try {
            // Get parameters
            String type = request.getParameter("type"); // popular, now_playing, upcoming, top_rated
            String language = request.getParameter("language"); // en, hi, ta, etc.
            String pageStr = request.getParameter("page");
            
            int page = 1;
            if (pageStr != null) {
                try {
                    page = Integer.parseInt(pageStr);
                } catch (NumberFormatException e) {
                    page = 1;
                }
            }
            
            List<Movie> movies;
            
            // If type is specified, fetch from API
            if (type != null && !type.isEmpty()) {
                movies = movieDBService.fetchMovies(type, page, language);
                // Cache in database
                if (!movies.isEmpty()) {
                    movieDAO.createBatch(movies);
                }
            } else {
                // Try to get from database first
                movies = movieDAO.findAll();
                
                // If no movies in database, fetch from API and cache
                if (movies.isEmpty()) {
                    movies = movieDBService.fetchPopularMovies(1);
                    if (!movies.isEmpty()) {
                        movieDAO.createBatch(movies);
                    }
                }
            }
            
            // Return movies as JSON
            String json = gson.toJson(movies);
            response.getWriter().write(json);
            
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Failed to fetch movies\"}");
        }
    }
}
