package org.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @Column(name = "book_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id; // Changed from bookId to id to match the view usage

    @Enumerated(EnumType.STRING)
    @Column(name = "book_status")
    private BookStatus status;

    @Column(name = "edition")
    private Integer edition;

    @Column(name = "isbn_code", unique = true)
    private String isbn; // Changed from isbnCode to isbn to match the view usage

    @Column(name = "publication_year")
    private Integer publicationYear; // Changed from Date to Integer to match the view usage

    @Column(name = "publisher_name")
    private String publisherName;

    @Column(name = "shelf_id")
    private String shelfId; // Added to match the view usage

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "shelf_id", insertable = false, updatable = false)
    private Shelf shelf;

    @Column(name = "title")
    private String title;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Borrower> borrowers = new ArrayList<>();
}