# Vehicle Rental System

## 📚 Project Overview

The **Vehicle Rental System (VRS)** is a web-based platform designed to facilitate seamless vehicle rentals for users while providing admins with comprehensive tools to manage vehicles, bookings, users, and reviews effectively. The system offers a secure and intuitive booking process, ensuring a user-friendly experience with a responsive and dynamic interface.

## 🚀 Features

### 🧑‍💻 User Features

- **User Registration & Login:** The login is provided to users, while the signup functionality is restricted to the admin, who can create new user accounts.
- **Browse Vehicles:** View available vehicles with detailed specifications.
- **Search & Filter:** Filter vehicles by start date, end date, and type only. The search bar allows searching for vehicles by name, type, and price range (500 less or more than the entered amount).
- **Booking System:** Select start and end dates for booking.
- **Add Reviews:** Users can add reviews for the vehicles they have booked.
- **Review History:** Users can view their review history.
- **Booking History:** View past and current bookings.
- **Profile Management:** Users can update their email, name, and contact number.
- **Cancel Booking:** Users can cancel their booking if needed.
- **Homepage Content:** The homepage contains top reviews, recently added cars, and recently added bikes fetched dynamically from the backend.

### 🛠️ Admin Features

- **Admin Dashboard:** Manage vehicle inventory, bookings, and users.
- **Add/Edit/Delete Vehicles:** CRUD operations for vehicle management.
- **View Bookings:** Admin can view booking details.
- **User Management:** Admin can add, update, and delete registered users.
- **Profile Management:** Admin can update their own profile information.
- **Review Monitoring:** Admin can monitor reviews added by users.
- **Admin Panel Search:** Search functionality is provided to search vehicles, users, bookings, and reviews.
- **Statistics Overview:** Admin dashboard displays total count of vehicles, users, bookings, reviews, and total earnings.

## 📝 Technologies Used

### 🌐 Frontend

- **HTML, CSS, JavaScript** - Core frontend technologies.

### 🖥️ Backend

- **Java Spring Boot** - Backend framework for handling business logic.
- **PostgreSQL** - Database to store users, vehicles, reviews and booking information.

### 🎨 Design & Styling

- **CSS Grid/Flexbox** - For smooth layout management.
- **Material Icons** - To enhance UI.

## 📄 Folder Structure

```
/vehicle-rental-system
├── /src
│   ├── /main
│   │   ├── /java/com/capstone1/vehical_rental_system
│   │   │   ├── /configurations
│   │   │   ├── /controllers
│   │   │   ├── /entities
│   │   │   ├── /repositories
│   │   │   ├── /services
│   │   │   └── VehicalRentalSytemApplication.java
│   │   ├── /resources
│   │   │   ├── /templates
│   │   │   ├── /static
│   │   │   │   ├── /css
│   │   │   │   ├── /js
│   │   │   │   └── /images
├── /public
│   └── /uploads
└── /frontEnd_dev
    ├── index.html
    ├── index.css
    ├── index.js
    ├── adminDashboard.html
    ├── adminDashboard.css
    ├── adminDashboard.js
    ├── home.html
    ├── home.css
    └── home.js
```

## 🎯 How to Run

### 🛠️ Prerequisites

- **Java JDK 11+**
- **PostgreSQL Database**
- **Spring Boot CLI**

### 📦 Installation

1. **Clone the repository:**

```bash
git clone https://github.com/Waris-Ali123/vehical_rental_system.git
```

2. **Navigate to the project directory:**

```bash
cd vehicle-rental-system
```

3. **Configure Database:**

- Open `application.properties` and set your database credentials.

4. **Run the backend:**

```bash
mvn spring-boot:run
```

## 🧪 Usage

- Navigate to the homepage.
- Browse and select a vehicle.
- Provide booking details and confirm.
- View top reviews, recently added cars, and recently added bikes.
- Add reviews after booking.
- View review and booking history.
- Admin can log in to manage the system, vehicles, bookings, users, and reviews.

## 📧 Contact

For inquiries, contact:

- **Email:** [gulamwarissheikh786@gmail.com](mailto:gulamwarissheikh786@gmail.com)
- **GitHub:** [GitHub Repository](https://github.com/Waris-Ali123/vehical_rental_system)

---

Happy Coding! 🚗✨

