package org.example.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.DAO.BookDAO;
import org.example.model.Book;
import org.example.model.BookStatus;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/books")
public class BookServlet extends HttpServlet {

    private BookDAO bookDao;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        bookDao = new BookDAO();  // Initialize BookDAO
        objectMapper = new ObjectMapper();  // Initialize ObjectMapper for JSON serialization
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String bookId = request.getParameter("id");
        response.setContentType("application/json");

        try (PrintWriter out = response.getWriter()) {
            if (bookId == null) {
                // Fetch all books
                List<Book> books = bookDao.getAvailableBooks();
                String json = objectMapper.writeValueAsString(books);  // Convert list to JSON
                out.println(json);
            } else {
                // Fetch book by ID
                Book book = bookDao.findById(bookId);
                if (book != null) {
                    String json = objectMapper.writeValueAsString(book);  // Convert book to JSON
                    out.println(json);
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.println("{\"error\":\"Book not found\"}");
                }
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Book book = new Book();
        book.setTitle(request.getParameter("title"));
        book.setIsbn(request.getParameter("isbn"));
        book.setEdition(Integer.parseInt(request.getParameter("edition")));
        book.setPublicationYear(Integer.parseInt(request.getParameter("publicationYear")));
        book.setPublisherName(request.getParameter("publisherName"));
        book.setStatus(BookStatus.valueOf(request.getParameter("status")));

        bookDao.saveBook(book);

        response.setStatus(HttpServletResponse.SC_CREATED);
        response.getWriter().println("{\"message\":\"Book created successfully\"}");
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String bookId = request.getParameter("id");

        Book book = bookDao.findById(bookId);
        if (book != null) {
            book.setTitle(request.getParameter("title"));
            book.setIsbn(request.getParameter("isbn"));
            book.setEdition(Integer.parseInt(request.getParameter("edition")));
            book.setPublicationYear(Integer.parseInt(request.getParameter("publicationYear")));
            book.setPublisherName(request.getParameter("publisherName"));
            book.setStatus(BookStatus.valueOf(request.getParameter("status")));

            bookDao.updateBook(book);

            response.getWriter().println("{\"message\":\"Book updated successfully\"}");
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().println("{\"error\":\"Book not found\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String bookId = request.getParameter("id");

        Book book = bookDao.findById(bookId);
        if (book != null) {
            response.getWriter().println("{\"message\":\"Book deleted successfully\"}");
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().println("{\"error\":\"Book not found\"}");
        }
    }
}
