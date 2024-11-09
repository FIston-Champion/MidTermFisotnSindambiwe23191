package org.example.DAO;

import org.example.model.Borrower;
import org.example.model.Membership;
import org.example.model.MembershipType;
import org.example.model.User;
import org.example.model.MembershipLevel;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.Date;
import java.util.List;

public class MembershipDAO {
    private final SessionFactory sessionFactory;

    public MembershipDAO() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    public void saveMembership(Membership membership) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            // If the membership is new (no ID), generate a membership code
            if (membership.getId() == null) {
                // You might want to implement a more sophisticated code generation logic
                membership.setMembershipCode("MEM" + System.currentTimeMillis());
            }

            // If registration date is not set, set it to current date
            if (membership.getRegistrationDate() == null) {
                membership.setRegistrationDate(new Date());
            }

            // If status is not set, set it to PENDING
            if (membership.getStatus() == null) {
                membership.setStatus(Membership.Status.PENDING);
            }

            // Save or update the membership
            session.saveOrUpdate(membership);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void registerMembership(Membership membership) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            // Set registration date
            membership.setRegistrationDate(new Date());

            // Default status to PENDING
            membership.setStatus(Membership.Status.PENDING);

            session.save(membership);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public boolean validateBorrowingLimit(String userId) {
        try (Session session = sessionFactory.openSession()) {
            User user = session.get(User.class, userId);
            if (user != null && user.getMembership() != null) {
                MembershipType membershipType = user.getMembership().getMembershipType();
                List<Borrower> borrowings = user.getBorrowings();
                int currentBorrowings = borrowings != null ? borrowings.size() : 0;
                return currentBorrowings < membershipType.getMaxBooks();
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Membership> getExpiredMemberships() {
        try (Session session = sessionFactory.openSession()) {
            Query<Membership> query = session.createQuery(
                    "FROM Membership WHERE expiringTime < :currentDate AND status = :status",
                    Membership.class
            );
            query.setParameter("currentDate", new Date());
            query.setParameter("status", Membership.Status.APPROVED);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Membership getMembershipByUserId(String userId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Membership> query = session.createQuery(
                    "FROM Membership WHERE readerId = :userId",
                    Membership.class
            );
            query.setParameter("userId", userId);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean updateMembershipStatus(String membershipId, Membership.Status status) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            Membership membership = session.get(Membership.class, membershipId);
            if (membership != null) {
                membership.setStatus(status);
                session.update(membership);
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }

    public boolean renewMembership(String membershipId, Date newExpiryDate) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            Membership membership = session.get(Membership.class, membershipId);
            if (membership != null) {
                membership.setExpiringTime(newExpiryDate);
                session.update(membership);
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }

    public List<MembershipType> getAllMembershipTypes() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM MembershipType", MembershipType.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Optional: Add a method to delete a membership
    public boolean deleteMembership(String membershipId) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            Membership membership = session.get(Membership.class, membershipId);
            if (membership != null) {
                session.delete(membership);
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }

    // Optional: Add a method to get membership by membership code
    public Membership getMembershipByCode(String membershipCode) {
        try (Session session = sessionFactory.openSession()) {
            Query<Membership> query = session.createQuery(
                    "FROM Membership WHERE membershipCode = :code",
                    Membership.class
            );
            query.setParameter("code", membershipCode);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}