package org.example.DAO;

import org.example.model.MembershipType;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import java.util.List;

public class MembershipTypeDAO {
    private final SessionFactory sessionFactory;

    public MembershipTypeDAO() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    // Save a new MembershipType
    public void saveMembershipType(MembershipType membershipType) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.save(membershipType);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Retrieve all membership types
    public List<MembershipType> getAllMembershipTypes() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM MembershipType", MembershipType.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Retrieve a MembershipType by ID
    public MembershipType getMembershipTypeById(Integer id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(MembershipType.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Update MembershipType details
    public void updateMembershipType(MembershipType membershipType) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.update(membershipType);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Delete a MembershipType by ID
    public void deleteMembershipType(Integer id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            MembershipType membershipType = session.get(MembershipType.class, id);
            if (membershipType != null) {
                session.delete(membershipType);
            }
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Retrieve MembershipType by name
    public MembershipType getMembershipTypeByName(String membershipName) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM MembershipType WHERE membershipName = :membershipName", MembershipType.class)
                    .setParameter("membershipName", membershipName)
                    .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
