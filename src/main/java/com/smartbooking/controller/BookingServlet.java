package com.smartbooking.controller;

import com.google.gson.Gson;
import com.smartbooking.dao.BookingDAO;
import com.smartbooking.model.Booking;
import com.smartbooking.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Booking servlet - handles movie ticket bookings
 */
@WebServlet("/book")
public class BookingServlet extends HttpServlet {
    
    private BookingDAO bookingDAO = new BookingDAO();
    private Gson gson = new Gson();
    private static final double PRICE_PER_SEAT = 150.00;
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // Check authentication
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("user") == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                result.put("success", false);
                result.put("message", "Please login to book tickets");
                response.getWriter().write(gson.toJson(result));
                return;
            }
            
            User user = (User) session.getAttribute("user");
            int userId = user.getId();
            
            // Get booking parameters
            String movieIdStr = request.getParameter("movieId");
            String theatreIdStr = request.getParameter("theatreId");
            String seats = request.getParameter("seats");
            String showTime = request.getParameter("showTime");
            
            if (movieIdStr == null || theatreIdStr == null || seats == null || seats.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                result.put("success", false);
                result.put("message", "Missing required parameters");
                response.getWriter().write(gson.toJson(result));
                return;
            }
            
            int movieId = Integer.parseInt(movieIdStr);
            int theatreId = Integer.parseInt(theatreIdStr);
            
            // Check seat availability
            List<String> occupiedSeats = bookingDAO.getOccupiedSeats(movieId, theatreId);
            String[] requestedSeats = seats.split(",");
            
            for (String seat : requestedSeats) {
                if (occupiedSeats.contains(seat.trim())) {
                    result.put("success", false);
                    result.put("message", "Seat " + seat.trim() + " is already booked");
                    response.getWriter().write(gson.toJson(result));
                    return;
                }
            }
            
            // Calculate total price
            double totalPrice = requestedSeats.length * PRICE_PER_SEAT;
            
            // Create booking
            Booking booking = new Booking(userId, movieId, theatreId, seats, totalPrice);
            booking.setShowTime(showTime);
            
            if (bookingDAO.create(booking)) {
                result.put("success", true);
                result.put("totalPrice", totalPrice);
                result.put("message", "Booking confirmed successfully!");
            } else {
                result.put("success", false);
                result.put("message", "Failed to create booking");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.put("success", false);
            result.put("message", "An error occurred while processing your booking");
        }
        
        response.getWriter().write(gson.toJson(result));
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try {
            String action = request.getParameter("action");
            
            if ("myBookings".equals(action)) {
                // Get user's bookings
                HttpSession session = request.getSession(false);
                if (session == null || session.getAttribute("user") == null) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("{\"error\": \"Not authenticated\"}");
                    return;
                }
                
                User user = (User) session.getAttribute("user");
                List<Booking> bookings = bookingDAO.findByUserId(user.getId());
                response.getWriter().write(gson.toJson(bookings));
                
            } else {
                // Get occupied seats
                String movieIdStr = request.getParameter("movieId");
                String theatreIdStr = request.getParameter("theatreId");
                
                if (movieIdStr != null && theatreIdStr != null) {
                    int movieId = Integer.parseInt(movieIdStr);
                    int theatreId = Integer.parseInt(theatreIdStr);
                    
                    List<String> occupiedSeats = bookingDAO.getOccupiedSeats(movieId, theatreId);
                    response.getWriter().write(gson.toJson(occupiedSeats));
                } else {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("{\"error\": \"Missing parameters\"}");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Failed to process request\"}");
        }
    }
    
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // Check authentication
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("user") == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                result.put("success", false);
                result.put("message", "Please login to modify bookings");
                response.getWriter().write(gson.toJson(result));
                return;
            }
            
            User user = (User) session.getAttribute("user");
            
            String bookingIdStr = request.getParameter("bookingId");
            String newSeats = request.getParameter("seats");
            
            if (bookingIdStr == null || newSeats == null || newSeats.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                result.put("success", false);
                result.put("message", "Missing required parameters");
                response.getWriter().write(gson.toJson(result));
                return;
            }
            
            int bookingId = Integer.parseInt(bookingIdStr);
            
            // Get existing booking
            Booking booking = bookingDAO.findById(bookingId);
            if (booking == null || booking.getUserId() != user.getId()) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                result.put("success", false);
                result.put("message", "Booking not found or unauthorized");
                response.getWriter().write(gson.toJson(result));
                return;
            }
            
            // Check new seat availability
            List<String> occupiedSeats = bookingDAO.getOccupiedSeats(booking.getMovieId(), booking.getTheatreId());
            String[] requestedSeats = newSeats.split(",");
            String[] oldSeats = booking.getSeats().split(",");
            
            for (String seat : requestedSeats) {
                String trimmedSeat = seat.trim();
                boolean isOldSeat = false;
                for (String oldSeat : oldSeats) {
                    if (oldSeat.trim().equals(trimmedSeat)) {
                        isOldSeat = true;
                        break;
                    }
                }
                if (!isOldSeat && occupiedSeats.contains(trimmedSeat)) {
                    result.put("success", false);
                    result.put("message", "Seat " + trimmedSeat + " is already booked");
                    response.getWriter().write(gson.toJson(result));
                    return;
                }
            }
            
            // Calculate new price
            double newTotalPrice = requestedSeats.length * PRICE_PER_SEAT;
            
            if (bookingDAO.modifySeats(bookingId, user.getId(), newSeats, newTotalPrice)) {
                result.put("success", true);
                result.put("totalPrice", newTotalPrice);
                result.put("message", "Booking modified successfully!");
            } else {
                result.put("success", false);
                result.put("message", "Failed to modify booking");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.put("success", false);
            result.put("message", "An error occurred while modifying your booking");
        }
        
        response.getWriter().write(gson.toJson(result));
    }
    
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // Check authentication
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("user") == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                result.put("success", false);
                result.put("message", "Please login to cancel bookings");
                response.getWriter().write(gson.toJson(result));
                return;
            }
            
            User user = (User) session.getAttribute("user");
            String bookingIdStr = request.getParameter("bookingId");
            
            if (bookingIdStr == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                result.put("success", false);
                result.put("message", "Missing booking ID");
                response.getWriter().write(gson.toJson(result));
                return;
            }
            
            int bookingId = Integer.parseInt(bookingIdStr);
            
            if (bookingDAO.cancel(bookingId, user.getId())) {
                result.put("success", true);
                result.put("message", "Booking cancelled successfully!");
            } else {
                result.put("success", false);
                result.put("message", "Failed to cancel booking or booking already cancelled");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.put("success", false);
            result.put("message", "An error occurred while cancelling your booking");
        }
        
        response.getWriter().write(gson.toJson(result));
    }
}
