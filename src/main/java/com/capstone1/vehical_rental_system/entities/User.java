package com.capstone1.vehical_rental_system.entities;

import java.util.ArrayList;
import java.util.List;

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

    // private static int idCounter = 1;

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

    // @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    // private List<Booking> bookings=new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Booking> bookings = new ArrayList<>();




    public User(){ 
        // this.user_id = idCounter++;
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


    public void addBooking(Booking b){
        bookings.add(b);
        System.out.println("book added in the list also in users");
        System.out.println(bookings.toString() + "in users");
    }

    public void removeBooking(Booking b){
        bookings.remove(b);
        System.out.println("Book removed in list in users ");
    }

    @Override
    public String toString() {
        return "User [name=" + name + ", email=" + email + ", password=" + password + ", contact_number="
                + contact_number + ", role=" + role + "]";
    }

    

    

    
}
