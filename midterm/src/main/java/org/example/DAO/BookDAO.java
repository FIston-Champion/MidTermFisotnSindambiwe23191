package org.example.DAO;

import org.example.model.Book;
import org.example.model.BookStatus;
import org.example.model.Shelf;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class BookDAO {
    private final SessionFactory sessionFactory;

    public BookDAO() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    public void saveBook(Book book) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(book);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void updateBook(Book book) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.update(book);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public Book findById(String id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Book.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void assignBookToShelf(String bookId, String shelfId) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            Book book = session.get(Book.class, bookId);
            Shelf newShelf = session.get(Shelf.class, shelfId);
            Shelf oldShelf = (book != null && book.getShelfId() != null) ? session.get(Shelf.class, book.getShelfId()) : null;

            if (book != null && newShelf != null) {
                // Update old shelf stock if the book was previously assigned to another shelf
                if (oldShelf != null && !oldShelf.equals(newShelf)) {
                    oldShelf.setAvailableStock(Math.max(0, oldShelf.getAvailableStock() - 1));
                    oldShelf.setTotalStock(Math.max(0, oldShelf.getTotalStock() - 1));
                    session.update(oldShelf);
                }

                // Update new shelf stock
                newShelf.setAvailableStock(newShelf.getAvailableStock() + 1);
                newShelf.setTotalStock(newShelf.getTotalStock() + 1);
                book.setShelfId(shelfId);

                // Persist updates
                session.update(book);
                session.update(newShelf);

                // Flush to ensure changes are saved
                session.flush();
            } else {
                System.out.println("Book or Shelf not found.");
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public List<Book> getAvailableBooks() {
        try (Session session = sessionFactory.openSession()) {
            Query<Book> query = session.createQuery("FROM Book WHERE status = :status", Book.class);
            query.setParameter("status", BookStatus.AVAILABLE);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
