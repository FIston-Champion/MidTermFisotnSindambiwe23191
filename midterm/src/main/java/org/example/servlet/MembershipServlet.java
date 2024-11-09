package org.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.DAO.MembershipDAO;
import org.example.model.Membership;
import org.example.model.Membership.Status;
import org.example.model.MembershipType;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@WebServlet("/membership")
public class MembershipServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final MembershipDAO membershipDAO = new MembershipDAO();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Action parameter is required.");
            return;
        }

        switch (action) {
            case "list":
                listMemberships(request, response);
                break;
            case "get":
                getMembership(request, response);
                break;
            case "types":
                listMembershipTypes(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action.");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Action parameter is required.");
            return;
        }

        switch (action) {
            case "add":
                addMembership(request, response);
                break;
            case "update":
                updateMembership(request, response);
                break;
            case "delete":
                deleteMembership(request, response);
                break;
            case "renew":
                renewMembership(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action.");
                break;
        }
    }

    private void listMemberships(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Membership> memberships = membershipDAO.getExpiredMemberships();
        request.setAttribute("memberships", memberships);
        // Forward to JSP or write JSON response
    }

    private void getMembership(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userId = request.getParameter("userId");
        if (userId == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "User ID is required.");
            return;
        }

        Membership membership = membershipDAO.getMembershipByUserId(userId);
        if (membership == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Membership not found.");
        } else {
            request.setAttribute("membership", membership);
            // Forward to JSP or write JSON response
        }
    }

    private void listMembershipTypes(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<MembershipType> types = membershipDAO.getAllMembershipTypes();
        request.setAttribute("types", types);
        // Forward to JSP or write JSON response
    }

    private void addMembership(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Membership membership = new Membership();
            membership.setReaderId(request.getParameter("readerId"));
            membership.setMembershipName(request.getParameter("membershipName"));
            membership.setMembershipTypeId(Integer.parseInt(request.getParameter("membershipTypeId")));
            membership.setExpiringTime(parseDate(request.getParameter("expiringTime")));

            membershipDAO.registerMembership(membership);
            response.sendRedirect("membership?action=list");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error adding membership.");
        }
    }

    private void updateMembership(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String id = request.getParameter("id");
            Membership membership = membershipDAO.getMembershipByCode(id);

            if (membership != null) {
                membership.setMembershipName(request.getParameter("membershipName"));
                membership.setMembershipTypeId(Integer.parseInt(request.getParameter("membershipTypeId")));
                membership.setStatus(Status.valueOf(request.getParameter("status").toUpperCase()));
                membership.setExpiringTime(parseDate(request.getParameter("expiringTime")));

                membershipDAO.saveMembership(membership);
                response.sendRedirect("membership?action=list");
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Membership not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error updating membership.");
        }
    }

    private void deleteMembership(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        if (id == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Membership ID is required.");
            return;
        }

        if (membershipDAO.deleteMembership(id)) {
            response.sendRedirect("membership?action=list");
        } else {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error deleting membership.");
        }
    }

    private void renewMembership(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String id = request.getParameter("id");
            Date newExpiryDate = parseDate(request.getParameter("newExpiringTime"));

            if (membershipDAO.renewMembership(id, newExpiryDate)) {
                response.sendRedirect("membership?action=list");
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Membership not found or failed to renew.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error renewing membership.");
        }
    }

    private Date parseDate(String dateStr) throws ParseException {
        return dateStr != null ? dateFormat.parse(dateStr) : null;
    }
}
