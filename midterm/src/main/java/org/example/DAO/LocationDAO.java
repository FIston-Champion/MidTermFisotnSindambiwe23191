package org.example.DAO;

import org.example.model.Location;
import org.example.model.LocationType;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class LocationDAO {
    private final SessionFactory sessionFactory;

    public LocationDAO() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    // Create a new location
    public void createLocation(Location location) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                validateLocationHierarchy(location);
                session.save(location);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw e;
            }
        }
    }

    // Validate location hierarchy
    private void validateLocationHierarchy(Location location) throws IllegalArgumentException {
        if (location.getParentId() != null) {
            Location parent = findById(location.getParentId());
            if (parent == null) {
                throw new IllegalArgumentException("Parent location does not exist");
            }

            // Validate hierarchy level
            if (!isValidHierarchy(parent.getLocationType(), location.getLocationType())) {
                throw new IllegalArgumentException("Invalid location hierarchy");
            }
        } else if (location.getLocationType() != LocationType.PROVINCE) {
            throw new IllegalArgumentException("Only provinces can have no parent");
        }
    }

    // Check if the hierarchy is valid
    private boolean isValidHierarchy(LocationType parentType, LocationType childType) {
        return switch (parentType) {
            case PROVINCE -> childType == LocationType.DISTRICT;
            case DISTRICT -> childType == LocationType.SECTOR;
            case SECTOR -> childType == LocationType.CELL;
            case CELL -> childType == LocationType.VILLAGE;
            default -> false;
        };
    }

    // Find location by ID
    public Location findById(String locationId) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Location.class, locationId);
        }
    }

    // Get all locations
    public List<Location> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Location", Location.class).list();
        }
    }

    // Update location
    public void update(Location location) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.update(location);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw e;
            }
        }
    }

    // Delete location
    public void delete(String locationId) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Location location = session.get(Location.class, locationId);
                if (location != null) {
                    session.delete(location);
                    transaction.commit();
                }
            } catch (Exception e) {
                transaction.rollback();
                throw e;
            }
        }
    }

    // Get locations by type
    public List<Location> getLocationsByType(LocationType type) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Location WHERE locationType = :type", Location.class)
                    .setParameter("type", type)
                    .list();
        }
    }

    // Get child locations
    public List<Location> getChildLocations(String parentId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Location WHERE parentId = :parentId", Location.class)
                    .setParameter("parentId", parentId)
                    .list();
        }
    }
}
