package com.capstone1.vehical_rental_system.entities;

import java.util.ArrayList;
import java.util.List;

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

@Entity
@Table(name = "users")
public class User {

    public enum Role{
        ADMIN,USER;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private int user_id;

    @Column(name = "name", nullable = false,length = 100)
    private String name;

    @Column(name = "email", unique = true, nullable = false,length = 150)
    private String email;

    @Column(nullable = false,length = 255)
    private String password;

    @Column(unique = true)
    private String contact_number ;

    
    @Enumerated(EnumType.STRING)    // Without @Enumerated(EnumType.STRING), JPA uses EnumType.ORDINAL
    @Column(nullable = false)
    Role role = User.Role.USER; //first user is class name and second is value of enum;


    @JsonIgnore
    @JsonBackReference(value = "booking-user")
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Booking> bookings = new ArrayList<>();
    
    @JsonIgnore
    @JsonBackReference(value = "review-user")
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();



    public User(){ 
    }
    


    public User(String name, String email, String password) {
        this(name,email,password,null);
    }

    public User(String name, String email, String password, String contact_number) {
        this(name,email,password,contact_number,Role.USER);
    }

    public User(String name, String email, String password, String contact_number, Role role) {
        this();
        this.name = name;
        this.email = email;
        this.password = password;
        this.contact_number = contact_number;
        this.role = role;
    }

    public int getUser_id() {
        return user_id;
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
        this.password = password;
    }


    public String getContact_number() {
        return contact_number;
    }


    public void setContact_number(String contact_number) {
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

    public void addBooking(Booking b){
        bookings.add(b);
    }

    public void removeBooking(Booking b){
        bookings.remove(b);
    }
    
    public void addReview(Review b){
        reviews.add(b);
    }

    public void removeReview(Review b){
        reviews.remove(b);
    }

    
    @Override
    public String toString() {
        return "User [name=" + name + ", email=" + email + ", password=" + password + ", contact_number="
                + contact_number + ", role=" + role + "]";
    }

    

    

    
}
