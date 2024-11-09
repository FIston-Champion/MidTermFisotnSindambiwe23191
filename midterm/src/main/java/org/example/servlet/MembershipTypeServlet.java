package org.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.DAO.MembershipTypeDAO;
import org.example.model.MembershipType;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/membershipType")
public class MembershipTypeServlet extends HttpServlet {

    private MembershipTypeDAO membershipTypeDAO;

    @Override
    public void init() throws ServletException {
        membershipTypeDAO = new MembershipTypeDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            listMembershipTypes(response);
        } else if (action.equals("view")) {
            viewMembershipType(request, response);
        } else if (action.equals("delete")) {
            deleteMembershipType(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action.equals("create")) {
            createMembershipType(request, response);
        } else if (action.equals("update")) {
            updateMembershipType(request, response);
        }
    }

    // List all membership types
    private void listMembershipTypes(HttpServletResponse response) throws IOException {
        List<MembershipType> membershipTypes = membershipTypeDAO.getAllMembershipTypes();
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<h1>Membership Types</h1>");
        for (MembershipType type : membershipTypes) {
            out.println("<p>ID: " + type.getId() + ", Name: " + type.getMembershipName() +
                    ", Max Books: " + type.getMaxBooks() + ", Daily Rate: " + type.getDailyRate() + "</p>");
        }
    }

    // View a specific membership type by ID
    private void viewMembershipType(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Integer id = Integer.parseInt(request.getParameter("id"));
        MembershipType membershipType = membershipTypeDAO.getMembershipTypeById(id);
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        if (membershipType != null) {
            out.println("<h1>Membership Type Details</h1>");
            out.println("<p>ID: " + membershipType.getId() + "</p>");
            out.println("<p>Name: " + membershipType.getMembershipName() + "</p>");
            out.println("<p>Max Books: " + membershipType.getMaxBooks() + "</p>");
            out.println("<p>Daily Rate: " + membershipType.getDailyRate() + "</p>");
        } else {
            out.println("<p>Membership type not found!</p>");
        }
    }

    // Create a new membership type
    private void createMembershipType(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("membershipName");
        Integer maxBooks = Integer.parseInt(request.getParameter("maxBooks"));
        Integer dailyRate = Integer.parseInt(request.getParameter("dailyRate"));

        MembershipType newMembershipType = new MembershipType(name, maxBooks, dailyRate);
        membershipTypeDAO.saveMembershipType(newMembershipType);

        response.sendRedirect("membershipType?action=list");
    }

    // Update an existing membership type
    private void updateMembershipType(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Integer id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("membershipName");
        Integer maxBooks = Integer.parseInt(request.getParameter("maxBooks"));
        Integer dailyRate = Integer.parseInt(request.getParameter("dailyRate"));

        MembershipType membershipType = membershipTypeDAO.getMembershipTypeById(id);
        if (membershipType != null) {
            membershipType.setMembershipName(name);
            membershipType.setMaxBooks(maxBooks);
            membershipType.setDailyRate(dailyRate);
            membershipTypeDAO.updateMembershipType(membershipType);
        }

        response.sendRedirect("membershipType?action=list");
    }

    // Delete a membership type by ID
    private void deleteMembershipType(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Integer id = Integer.parseInt(request.getParameter("id"));
        membershipTypeDAO.deleteMembershipType(id);

        response.sendRedirect("membershipType?action=list");
    }
}
