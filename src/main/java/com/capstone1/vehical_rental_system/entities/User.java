package com.capstone1.vehical_rental_system.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    public enum Role {
        ADMIN, USER
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "email", unique = true, nullable = false, length = 150)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(unique = true, nullable = false, length = 15) // Ensure unique and non-null
    private String contact_number;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    Role role = User.Role.USER;

    @JsonIgnore
    @JsonBackReference(value = "booking-user")
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private final List<Booking> bookings = new ArrayList<>();

    @JsonIgnore
    @JsonBackReference(value = "review-user")
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private final List<Review> reviews = new ArrayList<>();

    public User() {
    }

    public User(String name, String email, String password) {
        this(name, email, password, null);
    }

    public User(String name, String email, String password, String contact_number) {
        this(name, email, password, contact_number, Role.USER);
    }

    public User(String name, String email, String password, String contact_number, Role role) {
        this();
        this.name = name;
        this.email = email;
        this.password = encodePassword(password); // Secure password storage
        this.contact_number = contact_number;
        this.role = role;
    }

    public int getUserId() { // Changed from getuserId()
        return userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = encodePassword(password); // Secure password hashing
    }

    public String getContactNumber() { // Changed from getContact_number()
        return contact_number;
    }

    public void setContactNumber(String contact_number) {
        this.contact_number = contact_number;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void addBooking(Booking b) {
        bookings.add(b);
    }

    public void removeBooking(Booking b) {
        bookings.remove(b);
    }

    public void addReview(Review b) {
        reviews.add(b);
    }

    public void removeReview(Review b) {
        reviews.remove(b);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        User user = (User) obj;
        return userId == user.userId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(userId);
    }

    @Override
    public String toString() {
        return "User [userId = " + userId + ", name=" + name + " ,password= " + password + ", email=" + email + ", contact_number=" +
                contact_number + ", role=" + role + "]";
    }

    private String encodePassword(String password) { // Helper method for secure password storage
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }
}
