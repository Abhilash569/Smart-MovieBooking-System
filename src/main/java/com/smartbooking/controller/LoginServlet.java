package com.smartbooking.controller;

import com.google.gson.Gson;
import com.smartbooking.dao.UserDAO;
import com.smartbooking.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Login servlet - handles user authentication
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    
    private UserDAO userDAO = new UserDAO();
    private Gson gson = new Gson();
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        String action = request.getParameter("action");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            if ("login".equals(action)) {
                // Handle login
                if (userDAO.authenticate(email, password)) {
                    User user = userDAO.findByEmail(email);
                    if (user != null) {
                        // Create session
                        HttpSession session = request.getSession();
                        session.setAttribute("user", user);
                        session.setAttribute("userId", user.getId());
                        
                        // Prepare response
                        Map<String, Object> userData = new HashMap<>();
                        userData.put("id", user.getId());
                        userData.put("name", user.getName());
                        userData.put("email", user.getEmail());
                        
                        result.put("success", true);
                        result.put("user", userData);
                    }
                } else {
                    result.put("success", false);
                    result.put("message", "Invalid email or password");
                }
            } else if ("signup".equals(action)) {
                // Handle signup
                String name = request.getParameter("name");
                
                // Check if user already exists
                if (userDAO.findByEmail(email) != null) {
                    result.put("success", false);
                    result.put("message", "Email already registered");
                } else {
                    User newUser = new User(name, email, password);
                    if (userDAO.create(newUser)) {
                        // Get the created user
                        User user = userDAO.findByEmail(email);
                        
                        // Create session
                        HttpSession session = request.getSession();
                        session.setAttribute("user", user);
                        session.setAttribute("userId", user.getId());
                        
                        // Prepare response
                        Map<String, Object> userData = new HashMap<>();
                        userData.put("id", user.getId());
                        userData.put("name", user.getName());
                        userData.put("email", user.getEmail());
                        
                        result.put("success", true);
                        result.put("user", userData);
                    } else {
                        result.put("success", false);
                        result.put("message", "Failed to create account");
                    }
                }
            } else {
                result.put("success", false);
                result.put("message", "Invalid action");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "An error occurred");
        }
        
        response.getWriter().write(gson.toJson(result));
    }
}
