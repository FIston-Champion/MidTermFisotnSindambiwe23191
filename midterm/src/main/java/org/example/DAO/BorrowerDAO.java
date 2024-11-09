package org.example.DAO;

import org.example.model.Borrower;
import org.example.model.User;
import org.example.model.Membership;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.Date;
import java.util.List;

public class BorrowerDAO {
    private final SessionFactory sessionFactory;

    public BorrowerDAO() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    // Save a new borrowing record
    public void saveBorrowing(Borrower borrower) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.save(borrower);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw e;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Calculate late charges for a given Borrower ID
    public Integer calculateLateCharges(String borrowId) {
        try (Session session = sessionFactory.openSession()) {
            Borrower borrower = session.get(Borrower.class, borrowId);
            if (borrower != null && borrower.getDueDate().before(new Date())) {
                // Calculate days overdue
                long diff = new Date().getTime() - borrower.getDueDate().getTime();
                int daysLate = (int) (diff / (1000 * 60 * 60 * 24));

                // Get daily rate from user's membership
                User user = borrower.getUser();
                Membership membership = user.getMembership();
                int dailyRate = membership.getMembershipType().getDailyRate();

                return daysLate * dailyRate;
            }
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // Retrieve all overdue borrowings
    public List<Borrower> getOverdueBorrowings() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "FROM Borrower WHERE dueDate < :currentDate AND returnDate IS NULL",
                            Borrower.class)
                    .setParameter("currentDate", new Date())
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Find Borrowers by Reader (User) ID
    public List<Borrower> findByReaderId(String userId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "FROM Borrower WHERE readerId = :userId",
                            Borrower.class)
                    .setParameter("userId", userId)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Save a new borrower
    public void saveBorrower(Borrower borrower) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.save(borrower);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw e;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Find borrower by ID
    public Borrower findById(String borrowingId) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Borrower.class, borrowingId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Update borrower
    public void updateBorrower(Borrower borrower) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Borrower existingBorrower = session.get(Borrower.class, borrower.getId());
                if (existingBorrower != null) {
                    existingBorrower.setBookId(borrower.getBookId());
                    existingBorrower.setDueDate(borrower.getDueDate());
                    existingBorrower.setFine(borrower.getFine());
                    existingBorrower.setLateChargesFees(borrower.getLateChargesFees());
                    existingBorrower.setPickupDate(borrower.getPickupDate());
                    existingBorrower.setReaderId(borrower.getReaderId());
                    existingBorrower.setReturnDate(borrower.getReturnDate());

                    session.update(existingBorrower);
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

    // Additional utility method to check if a book is currently borrowed
    public boolean isBookBorrowed(String bookId) {
        try (Session session = sessionFactory.openSession()) {
            Long count = session.createQuery(
                            "SELECT COUNT(b) FROM Borrower b WHERE b.bookId = :bookId AND b.returnDate IS NULL",
                            Long.class)
                    .setParameter("bookId", bookId)
                    .uniqueResult();
            return count != null && count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get active borrowings count for a user
    public int getActiveBorrowingsCount(String userId) {
        try (Session session = sessionFactory.openSession()) {
            Long count = session.createQuery(
                            "SELECT COUNT(b) FROM Borrower b WHERE b.readerId = :userId AND b.returnDate IS NULL",
                            Long.class)
                    .setParameter("userId", userId)
                    .uniqueResult();
            return count != null ? count.intValue() : 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}