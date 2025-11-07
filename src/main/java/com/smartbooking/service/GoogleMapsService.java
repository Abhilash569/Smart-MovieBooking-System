package com.smartbooking.service;

import com.smartbooking.dao.TheatreDAO;
import com.smartbooking.model.Theatre;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class for Google Maps related operations
 */
public class GoogleMapsService {
    
    private TheatreDAO theatreDAO = new TheatreDAO();
    
    /**
     * Calculate distance between two coordinates using Haversine formula
     * @param lat1 Latitude of first point
     * @param lng1 Longitude of first point
     * @param lat2 Latitude of second point
     * @param lng2 Longitude of second point
     * @return Distance in kilometers
     */
    public double calculateDistance(double lat1, double lng1, double lat2, double lng2) {
        final int EARTH_RADIUS = 6371; // Radius in kilometers
        
        double latDistance = Math.toRadians(lat2 - lat1);
        double lngDistance = Math.toRadians(lng2 - lng1);
        
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return EARTH_RADIUS * c;
    }
    
    /**
     * Find nearby theatres within specified radius
     * @param lat User's latitude
     * @param lng User's longitude
     * @param radius Search radius in kilometers
     * @return List of nearby theatres
     */
    public List<Theatre> findNearbyTheatres(double lat, double lng, int radius) {
        // Use TheatreDAO's findNearby method which uses SQL Haversine formula
        return theatreDAO.findNearby(lat, lng, radius);
    }
}
