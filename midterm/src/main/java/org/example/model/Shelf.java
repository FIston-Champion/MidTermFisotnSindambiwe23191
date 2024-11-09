package org.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "shelves")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Shelf {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "shelf_id")
    private String id;

    @Column(name = "available_stock")
    private Integer availableStock;

    @Column(name = "book_category")
    private String bookCategory;

    @Column(name = "borrowed_counter")
    private Integer borrowedCounter;

    @Column(name = "total_stock")
    private Integer totalStock;

    @Column(name = "room_id")
    private String roomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", insertable = false, updatable = false)
    private Room room;

    @OneToMany(mappedBy = "shelf", cascade = CascadeType.ALL)
    private List<Book> books = new ArrayList<>();

    // Calculate available stock
    public void calculateAvailableStock() {
        this.availableStock = this.totalStock - this.borrowedCounter;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getAvailableStock() {
        return availableStock;
    }

    public void setAvailableStock(Integer availableStock) {
        this.availableStock = availableStock;
    }

    public String getBookCategory() {
        return bookCategory;
    }

    public void setBookCategory(String bookCategory) {
        this.bookCategory = bookCategory;
    }

    public Integer getBorrowedCounter() {
        return borrowedCounter;
    }

    public void setBorrowedCounter(Integer borrowedCounter) {
        this.borrowedCounter = borrowedCounter;
    }

    public Integer getTotalStock() {
        return totalStock;
    }

    public void setTotalStock(Integer totalStock) {
        this.totalStock = totalStock;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
