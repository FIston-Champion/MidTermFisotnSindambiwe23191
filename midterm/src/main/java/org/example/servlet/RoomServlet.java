package org.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.DAO.RoomDAO;
import org.example.model.Room;

import java.io.IOException;
import java.util.List;

@WebServlet("/room")
public class RoomServlet extends HttpServlet {
    private final RoomDAO roomDAO = new RoomDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "new":
                showNewForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "delete":
                deleteRoom(request, response);
                break;
            case "list":
            default:
                listRooms(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("insert".equals(action)) {
            insertRoom(request, response);
        } else if ("update".equals(action)) {
            updateRoom(request, response);
        }
    }

    private void listRooms(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Room> listRooms = roomDAO.findAll();
        request.setAttribute("listRooms", listRooms);
        request.getRequestDispatcher("/room-list.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/room-form.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String roomId = request.getParameter("roomId");
        Room existingRoom = roomDAO.getRoomById(roomId);
        request.setAttribute("room", existingRoom);
        request.getRequestDispatcher("/room-form.jsp").forward(request, response);
    }

    private void insertRoom(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String roomCode = request.getParameter("roomCode");
        String roomName = request.getParameter("roomName");
        Integer capacity = Integer.parseInt(request.getParameter("capacity"));

        Room newRoom = new Room(null, roomCode, roomName, capacity, null);
        roomDAO.saveRoom(newRoom);
        response.sendRedirect("room?action=list");
    }

    private void updateRoom(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String roomId = request.getParameter("roomId");
        String roomCode = request.getParameter("roomCode");
        String roomName = request.getParameter("roomName");
        Integer capacity = Integer.parseInt(request.getParameter("capacity"));

        Room room = new Room(roomId, roomCode, roomName, capacity, null);
        roomDAO.updateRoom(room);
        response.sendRedirect("room?action=list");
    }

    private void deleteRoom(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String roomId = request.getParameter("roomId");
        roomDAO.deleteRoom(roomId);
        response.sendRedirect("room?action=list");
    }
}
