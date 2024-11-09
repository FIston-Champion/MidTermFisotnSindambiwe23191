package org.example.DAO;

import org.example.model.Person;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.example.model.Gender;


import java.util.List;

public class PersonDAO {
    private final SessionFactory sessionFactory;

    public PersonDAO() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    // Save a new Person
    public void savePerson(Person person) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.save(person);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Retrieve all persons
    public List<Person> getAllPersons() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Person", Person.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Retrieve a Person by ID
    public Person getPersonById(String personId) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Person.class, personId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Update Person details
    public void updatePerson(Person person) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.update(person);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Delete a Person by ID
    public void deletePerson(String personId) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Person person = session.get(Person.class, personId);
            if (person != null) {
                session.delete(person);
            }
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Retrieve persons by gender
    public List<Person> getPersonsByGender(Gender gender) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Person WHERE gender = :gender", Person.class)
                    .setParameter("gender", gender)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Retrieve persons by location ID
    public List<Person> getPersonsByLocationId(String locationId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Person WHERE location.id = :locationId", Person.class)
                    .setParameter("locationId", locationId)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
