package com.smartbooking.controller;

import com.google.gson.Gson;
import com.smartbooking.dao.TheatreDAO;
import com.smartbooking.model.Theatre;
import com.smartbooking.service.GoogleMapsService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Theatre servlet - handles theatre-related requests
 */
@WebServlet("/theatres")
public class TheatreServlet extends HttpServlet {
    
    private TheatreDAO theatreDAO = new TheatreDAO();
    private GoogleMapsService googleMapsService = new GoogleMapsService();
    private Gson gson = new Gson();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try {
            List<Theatre> theatres = theatreDAO.findAll();
            String json = gson.toJson(theatres);
            response.getWriter().write(json);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Failed to fetch theatres\"}");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try {
            String latStr = request.getParameter("latitude");
            String lngStr = request.getParameter("longitude");
            String radiusStr = request.getParameter("radius");
            
            if (latStr != null && lngStr != null) {
                double latitude = Double.parseDouble(latStr);
                double longitude = Double.parseDouble(lngStr);
                int radius = radiusStr != null ? Integer.parseInt(radiusStr) : 10; // Default 10km
                
                List<Theatre> nearbyTheatres = googleMapsService.findNearbyTheatres(latitude, longitude, radius);
                String json = gson.toJson(nearbyTheatres);
                response.getWriter().write(json);
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"Latitude and longitude are required\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Failed to find nearby theatres\"}");
        }
    }
}
