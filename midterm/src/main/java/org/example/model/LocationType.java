package org.example.model;

public enum LocationType {
    PROVINCE("Province"),
    DISTRICT("District"),
    SECTOR("Sector"),
    CELL("Cell"),
    VILLAGE("Village");

    private final String displayName;

    LocationType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}