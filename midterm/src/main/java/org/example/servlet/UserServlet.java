package org.example.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.DAO.UserDAO;
import org.example.model.User;
import org.example.model.User.RoleType;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/user")
public class UserServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userId = request.getParameter("id");
        String roleType = request.getParameter("roleType");

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        if (userId != null) {
            User user = userDAO.findById(userId);
            if (user != null) {
                out.write(objectMapper.writeValueAsString(user));
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.write("{\"error\": \"User not found\"}");
            }
        } else if (roleType != null) {
            try {
                RoleType role = RoleType.valueOf(roleType.toUpperCase());
                List<User> users = userDAO.findByRoleType(role);
                out.write(objectMapper.writeValueAsString(users));
            } catch (IllegalArgumentException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write("{\"error\": \"Invalid role type\"}");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("{\"error\": \"No user ID or role type provided\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        User user = objectMapper.readValue(request.getReader(), User.class);

        userDAO.saveUser(user);
        response.setStatus(HttpServletResponse.SC_CREATED);
        response.getWriter().write("{\"message\": \"User created successfully\"}");
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        User user = objectMapper.readValue(request.getReader(), User.class);

        if (userDAO.findById(user.getId()) != null) {
            userDAO.updateUser(user);
            response.getWriter().write("{\"message\": \"User updated successfully\"}");
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("{\"error\": \"User not found\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userId = request.getParameter("id");

        response.setContentType("application/json");
        if (userId != null && userDAO.findById(userId) != null) {
            userDAO.deleteUser(userId);
            response.getWriter().write("{\"message\": \"User deleted successfully\"}");
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("{\"error\": \"User not found\"}");
        }
    }
}
