package org.example.DAO;

import org.example.model.Shelf;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class ShelfDAO {
    private final SessionFactory sessionFactory;

    public ShelfDAO() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    // Add the missing findByRoomId method
    public List<Shelf> findByRoomId(String roomId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Shelf WHERE roomId = :roomId", Shelf.class)
                    .setParameter("roomId", roomId)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Save a new Shelf
    public void saveShelf(Shelf shelf) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.save(shelf);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw e;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Retrieve all shelves
    public List<Shelf> getAllShelves() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Shelf", Shelf.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Retrieve shelves by Room ID
    public List<Shelf> getShelvesByRoomId(String roomId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Shelf WHERE roomId = :roomId", Shelf.class)
                    .setParameter("roomId", roomId)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Update Shelf stock
    public void updateShelfStock(String shelfId, int totalStock, int borrowedCounter) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Shelf shelf = session.get(Shelf.class, shelfId);
                if (shelf != null) {
                    shelf.setTotalStock(totalStock);
                    shelf.setBorrowedCounter(borrowedCounter);
                    shelf.calculateAvailableStock();
                    session.update(shelf);
                }
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw e;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Delete a Shelf by ID
    public void deleteShelf(String shelfId) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Shelf shelf = session.get(Shelf.class, shelfId);
                if (shelf != null) {
                    session.delete(shelf);
                }
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw e;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Retrieve a single Shelf by ID
    public Shelf getShelfById(String shelfId) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Shelf.class, shelfId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Find by ID (alternative implementation)
    public Shelf findById(String shelfId) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Shelf.class, shelfId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Update entire Shelf object
    public void updateShelf(Shelf shelf) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Shelf existingShelf = session.get(Shelf.class, shelf.getId());
                if (existingShelf != null) {
                    existingShelf.setAvailableStock(shelf.getAvailableStock());
                    existingShelf.setBookCategory(shelf.getBookCategory());
                    existingShelf.setBorrowedCounter(shelf.getBorrowedCounter());
                    existingShelf.setTotalStock(shelf.getTotalStock());
                    existingShelf.setRoomId(shelf.getRoomId());
                    existingShelf.calculateAvailableStock();

                    session.update(existingShelf);
                }
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw e;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}