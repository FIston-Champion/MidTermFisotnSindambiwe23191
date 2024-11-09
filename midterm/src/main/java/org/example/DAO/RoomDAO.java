package org.example.DAO;

import org.example.model.Room;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import java.util.List;

public class RoomDAO {
    private final SessionFactory sessionFactory;

    public RoomDAO() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    // Rename getAllRooms to findAll and add proper generic type
    public List<Room> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Room", Room.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Rest of the methods remain the same
    public void saveRoom(Room room) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.save(room);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateRoom(Room room) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.update(room);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteRoom(String roomId) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Room room = session.get(Room.class, roomId);
            if (room != null) {
                session.delete(room);
                transaction.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Room getRoomById(String roomId) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Room.class, roomId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Room getRoomByCode(String roomCode) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Room WHERE roomCode = :roomCode", Room.class)
                    .setParameter("roomCode", roomCode)
                    .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Integer getTotalBooksInRoom(String roomId) {
        Room room = getRoomById(roomId);
        return (room != null) ? room.getTotalBooks() : null;
    }

    public Integer getAvailableBooksInRoom(String roomId) {
        Room room = getRoomById(roomId);
        return (room != null) ? room.getAvailableBooks() : null;
    }
}