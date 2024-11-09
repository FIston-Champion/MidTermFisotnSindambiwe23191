package org.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Location;
import org.example.model.LocationType;
import org.example.DAO.LocationDAO;

import java.io.IOException;
import java.util.List;
@WebServlet("/location/*")
public class LocationServlet extends HttpServlet {
    private LocationDAO locationDAO;

    @Override
    public void init() throws ServletException {
        locationDAO = new LocationDAO(); // Initialize your DAO
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            // List all locations
            List<Location> locations = locationDAO.findAll();
            request.setAttribute("locations", locations);
            request.getRequestDispatcher("/WEB-INF/views/location/list.jsp")
                    .forward(request, response);
        } else if (pathInfo.equals("/create")) {
            // Show creation form
            request.setAttribute("locationTypes", LocationType.values());
            request.getRequestDispatcher("/WEB-INF/views/location/form.jsp")
                    .forward(request, response);
        } else {
            // Get specific location
            String locationId = pathInfo.substring(1);
            Location location = locationDAO.findById(locationId);
            if (location != null) {
                request.setAttribute("location", location);
                request.getRequestDispatcher("/WEB-INF/views/location/view.jsp")
                        .forward(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("create".equals(action)) {
            // Create new location
            Location location = new Location();
            location.setLocationCode(request.getParameter("locationCode"));
            location.setLocationName(request.getParameter("locationName"));
            location.setLocationType(LocationType.valueOf(request.getParameter("locationType")));
            location.setParentId(request.getParameter("parentId"));

            locationDAO.createLocation(location); // Fixed error here

            response.sendRedirect(request.getContextPath() + "/location");
        } else if ("update".equals(action)) {
            // Update existing location
            String locationId = request.getParameter("locationId");
            Location location = locationDAO.findById(locationId);

            if (location != null) {
                location.setLocationCode(request.getParameter("locationCode"));
                location.setLocationName(request.getParameter("locationName"));
                location.setLocationType(LocationType.valueOf(request.getParameter("locationType")));
                location.setParentId(request.getParameter("parentId"));

                locationDAO.update(location);
            }

            response.sendRedirect(request.getContextPath() + "/location");
        } else if ("delete".equals(action)) {
            // Delete location
            String locationId = request.getParameter("locationId");
            locationDAO.delete(locationId);

            response.sendRedirect(request.getContextPath() + "/location");
        }
    }

    // Helper method to get location by phone number (Requirement 3)
    private String getProvinceByPhoneNumber(String phoneNumber) {
        // Adjust logic for getting location
        return null; // Adjust or remove based on your logic
    }
}
