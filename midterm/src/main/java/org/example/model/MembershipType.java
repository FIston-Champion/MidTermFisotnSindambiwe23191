package org.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "membership_types")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MembershipType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "membership_name", nullable = false)
    private String membershipName;

    @Column(name = "max_books", nullable = false)
    private Integer maxBooks;

    @Column(name = "daily_rate", nullable = false)
    private Integer dailyRate;

    // Constructor with required fields
    public MembershipType(String membershipName, Integer maxBooks, Integer dailyRate) {
        this.membershipName = membershipName;
        this.maxBooks = maxBooks;
        this.dailyRate = dailyRate;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMembershipName() {
        return membershipName;
    }

    public void setMembershipName(String membershipName) {
        this.membershipName = membershipName;
    }

    public Integer getMaxBooks() {
        return maxBooks;
    }

    public void setMaxBooks(Integer maxBooks) {
        this.maxBooks = maxBooks;
    }

    public Integer getDailyRate() {
        return dailyRate;
    }

    public void setDailyRate(Integer dailyRate) {
        this.dailyRate = dailyRate;
    }

    // Helper method to calculate fees for a given number of days
    public Integer calculateFees(int numberOfDays) {
        return dailyRate * numberOfDays;
    }

    // Factory methods for predefined membership types
    public static MembershipType createGoldMembership() {
        return new MembershipType("Gold", 5, 50);
    }

    public static MembershipType createSilverMembership() {
        return new MembershipType("Silver", 3, 30);
    }

    public static MembershipType createStriverMembership() {
        return new MembershipType("Striver", 2, 10);
    }
}