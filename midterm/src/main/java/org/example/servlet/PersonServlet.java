package org.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.DAO.PersonDAO;
import org.example.DAO.LocationDAO;
import org.example.model.Gender;
import org.example.model.Person;
import org.example.model.Location;

import java.io.IOException;
import java.util.List;

@WebServlet("/person")
public class PersonServlet extends HttpServlet {

    private static final long serialVersionUID = 1L; // Added for serialization purposes

    private final PersonDAO personDAO = new PersonDAO();
    private final LocationDAO locationDAO = new LocationDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        try {
            switch (action) {
                case "new":
                    showNewForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "delete":
                    deletePerson(request, response);
                    break;
                case "list":
                default:
                    listPersons(request, response);
                    break;
            }
        } catch (Exception e) {
            throw new ServletException("Error in PersonServlet doGet", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if ("insert".equals(action)) {
                insertPerson(request, response);
            } else if ("update".equals(action)) {
                updatePerson(request, response);
            }
        } catch (Exception e) {
            throw new ServletException("Error in PersonServlet doPost", e);
        }
    }

    private void listPersons(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Person> listPersons = personDAO.getAllPersons();
        request.setAttribute("listPersons", listPersons);
        request.getRequestDispatcher("/person-list.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/person-form.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String personId = request.getParameter("personId");
        Person existingPerson = personDAO.getPersonById(personId);
        request.setAttribute("person", existingPerson);
        request.getRequestDispatcher("/person-form.jsp").forward(request, response);
    }

    private void insertPerson(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String personId = request.getParameter("personId");
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            Gender gender = Gender.valueOf(request.getParameter("gender").toUpperCase()); // Ensure case consistency
            String phoneNumber = request.getParameter("phoneNumber");
            String locationId = request.getParameter("locationId");

            Location location = locationDAO.findById(locationId);

            if (location != null) {
                Person newPerson = new Person(personId, firstName, lastName, gender, phoneNumber, location);
                personDAO.savePerson(newPerson);
                response.sendRedirect("person?action=list");
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid location ID");
            }
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid gender or location ID format");
        }
    }

    private void updatePerson(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String personId = request.getParameter("personId");
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            Gender gender = Gender.valueOf(request.getParameter("gender").toUpperCase()); // Ensure case consistency
            String phoneNumber = request.getParameter("phoneNumber");
            String locationId = request.getParameter("locationId");

            Location location = locationDAO.findById(locationId);

            if (location != null) {
                Person person = new Person(personId, firstName, lastName, gender, phoneNumber, location);
                personDAO.updatePerson(person);
                response.sendRedirect("person?action=list");
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid location ID");
            }
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid gender or location ID format");
        }
    }

    private void deletePerson(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String personId = request.getParameter("personId");
        personDAO.deletePerson(personId);
        response.sendRedirect("person?action=list");
    }
}
