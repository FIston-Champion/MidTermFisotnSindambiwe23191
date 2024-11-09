package org.example.servlet;

import org.example.DAO.BorrowerDAO;
import org.example.model.Borrower;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/borrower")
public class BorrowerServlet extends HttpServlet {

    private BorrowerDAO borrowerDAO;

    @Override
    public void init() throws ServletException {
        borrowerDAO = new BorrowerDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action != null) {
            switch (action) {
                case "save":
                    saveBorrower(request, response);
                    break;
                case "calculateLateCharges":
                    calculateLateCharges(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action != null) {
            switch (action) {
                case "getOverdueBorrowings":
                    getOverdueBorrowings(response);
                    break;
                case "findByReaderId":
                    findByReaderId(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
            }
        }
    }

    private void saveBorrower(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String bookId = request.getParameter("bookId");
        String readerId = request.getParameter("readerId");
        String dueDateStr = request.getParameter("dueDate");

        // Additional fields can be extracted from request here
        Borrower borrower = new Borrower();
        borrower.setBookId(bookId);
        borrower.setReaderId(readerId);
        borrower.setDueDate(java.sql.Date.valueOf(dueDateStr)); // Convert dueDate to Date

        borrowerDAO.saveBorrowing(borrower);

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<h3>Borrower record saved successfully!</h3>");
    }

    private void calculateLateCharges(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String borrowId = request.getParameter("borrowId");

        Integer lateCharges = borrowerDAO.calculateLateCharges(borrowId);

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<h3>Late Charges: " + lateCharges + "</h3>");
    }

    private void getOverdueBorrowings(HttpServletResponse response) throws IOException {
        List<Borrower> overdueBorrowings = borrowerDAO.getOverdueBorrowings();

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<h3>Overdue Borrowings:</h3>");
        for (Borrower borrower : overdueBorrowings) {
            out.println("<p>" + borrower.toString() + "</p>");
        }
    }

    private void findByReaderId(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String readerId = request.getParameter("readerId");

        List<Borrower> borrowings = borrowerDAO.findByReaderId(readerId);

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<h3>Borrowings for Reader ID: " + readerId + "</h3>");
        for (Borrower borrower : borrowings) {
            out.println("<p>" + borrower.toString() + "</p>");
        }
    }

    @Override
    public void destroy() {
        borrowerDAO = null;
    }
}
