package org.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.DAO.ShelfDAO;
import org.example.model.Shelf;

import java.io.IOException;
import java.util.List;

@WebServlet("/shelves")
public class ShelfServlet extends HttpServlet {
    private ShelfDAO shelfDAO;

    @Override
    public void init() throws ServletException {
        shelfDAO = new ShelfDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        try {
            switch (action) {
                case "list":
                    listShelves(request, response);
                    break;
                case "view":
                    viewShelf(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                default:
                    listShelves(request, response);
                    break;
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        try {
            switch (action) {
                case "create":
                    createShelf(request, response);
                    break;
                case "update":
                    updateShelf(request, response);
                    break;
                case "delete":
                    deleteShelf(request, response);
                    break;
                default:
                    listShelves(request, response);
                    break;
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void listShelves(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Shelf> shelves = shelfDAO.getAllShelves();
        request.setAttribute("shelves", shelves);
        request.getRequestDispatcher("shelf-list.jsp").forward(request, response);
    }

    private void viewShelf(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        Shelf shelf = shelfDAO.getShelfById(id);
        request.setAttribute("shelf", shelf);
        request.getRequestDispatcher("shelf-view.jsp").forward(request, response);
    }

    private void createShelf(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String bookCategory = request.getParameter("bookCategory");
        int totalStock = Integer.parseInt(request.getParameter("totalStock"));
        int borrowedCounter = Integer.parseInt(request.getParameter("borrowedCounter"));
        String roomId = request.getParameter("roomId");

        Shelf shelf = new Shelf();
        shelf.setBookCategory(bookCategory);
        shelf.setTotalStock(totalStock);
        shelf.setBorrowedCounter(borrowedCounter);
        shelf.setRoomId(roomId);
        shelf.calculateAvailableStock();

        shelfDAO.saveShelf(shelf);
        response.sendRedirect("shelves?action=list");
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        Shelf existingShelf = shelfDAO.getShelfById(id);
        request.setAttribute("shelf", existingShelf);
        request.getRequestDispatcher("shelf-edit.jsp").forward(request, response);
    }

    private void updateShelf(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        String bookCategory = request.getParameter("bookCategory");
        int totalStock = Integer.parseInt(request.getParameter("totalStock"));
        int borrowedCounter = Integer.parseInt(request.getParameter("borrowedCounter"));
        String roomId = request.getParameter("roomId");

        Shelf shelf = new Shelf();
        shelf.setId(id);
        shelf.setBookCategory(bookCategory);
        shelf.setTotalStock(totalStock);
        shelf.setBorrowedCounter(borrowedCounter);
        shelf.setRoomId(roomId);
        shelf.calculateAvailableStock();

        shelfDAO.updateShelf(shelf);
        response.sendRedirect("shelves?action=list");
    }

    private void deleteShelf(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        shelfDAO.deleteShelf(id);
        response.sendRedirect("shelves?action=list");
    }
}
