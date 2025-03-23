let allBookings;
let allUsers;
let allVehicles;
let allReviews;
let admin = JSON.parse(localStorage.getItem("admin"));
let totalUsers;
let totalBookings;
let totalReviews;
let totalVehicles;
let totalEarnings = 0;

//table container that holds maximum outputs
let tablesContainer = document.querySelector(".tablesContainer");
//cards Container that is having cards
let cardContainer = document.querySelector(".cardContainer");

// add hovered class to selected list item
let list = document.querySelectorAll(".listOfMenus .listItem");

function activeLink() {
    list.forEach((item) => {
        item.classList.remove("active");
    });
    this.classList.add("active");
}

list.forEach((item) => item.addEventListener("click", activeLink));

//toggling the navbar
let toggle = document.querySelector(".toggle");
let navigation = document.querySelector(".navigation");
let main = document.querySelector(".main");

toggle.onclick = function () {
    navigation.classList.toggle("active");
    main.classList.toggle("active");
};



window.onload = async function () {


    await Promise.all([fetchingBookings(), fetchingUsers(), fetchingVehicles(), fetchingReviews()]);

    printingBookingsDataInTable(allBookings);
    printingOverviewOnDashBoard();

}



function printingOverviewOnDashBoard() {

    const cardData = [
        {
            counts: totalUsers,
            icon: "group",
            label: "Total Users"
        },
        {
            counts: totalVehicles,
            icon: "directions_car",
            label: "Total Vehicles"
        },
        {
            counts: totalBookings,
            icon: "calendar_month",
            label: "Bookings"
        },
        {
            counts: totalReviews,
            icon:"hotel_class",
            label: "Total Reviews"
        },
        {
            counts: totalEarnings.toFixed(2),
            icon: "paid",
            label: "Total Earnings"
        },
        {
            counts: 1500,               //dummy value
            icon: "visibility",
            label: "Daily Views"
        }
    ];

    
    cardContainer.innerHTML = "";

    cardData.forEach((card) => {
        let cardsDiv = document.createElement("div");
        cardsDiv.classList.add("cards");

        let topDiv = document.createElement("div");
        topDiv.classList.add("top");

        let counts = document.createElement("div");
        counts.classList.add("counts");
        counts.innerText = card.counts;

        let iconSpan = document.createElement("span");
        iconSpan.classList.add("icon");
        iconSpan.classList.add("material-symbols-outlined");

        iconSpan.innerText = card.icon;

        topDiv.append(counts, iconSpan);

        let lable = document.createElement("div");
        lable.classList.add("label");
        lable.innerText = card.label;

        cardsDiv.append(topDiv, lable);
        cardContainer.appendChild(cardsDiv);
    });


}



function profileClick() {
    let user = JSON.parse(localStorage.getItem("admin"));
    printingProfile(user);
}



function printingDashBoardData() {
    printingOverviewOnDashBoard();
    printingBookingsDataInTable(allBookings);
}

// ====================== Start fetching entities========================================



//Fetching all users from backend
async function fetchingUsers() {
    try {
        let adminEmail = admin.email;
        let response = await fetch(`http://localhost:8080/auth/getAllUsers?email=${adminEmail}`, {
            method: "GET"
            // headers: { "Content-Type": "application/json" }
        });
        if (response.ok) {
            let data = await response.json();
            allUsers = data;
            totalUsers = allUsers.length;

        }

    } catch (error) {
        console.log(error);

    }
}



//Get the bookingData
async function fetchingBookings() {

    try {

        let admin = JSON.parse(localStorage.getItem("admin"));
        let adminEmail = admin.email;


        let response = await fetch(`http://localhost:8080/booking/getAllBookings?email=${adminEmail}`, {
            method: "GET"
            // headers: { "Content-Type": "application/json" }
        });

        if (response.ok) {

            allBookings = await response.json();
            totalBookings = allBookings.length;
        }

        if (!response.ok) {
            throw new Error(`Error: ${response.status}`);
        }

    } catch (error) {
        console.error("Error fetching data:", error);
    }

}




//Fetching all vehicles from backend
async function fetchingVehicles() {
    try {
        let adminEmail = admin.email;
        let response = await fetch(`http://localhost:8080/vehicle/getAllVehicles?email=${adminEmail}`, {
            method: "GET"
            // headers: { "Content-Type": "application/json" }
        });
        if (response.ok) {
            let data = await response.json();
            allVehicles = data;
            totalVehicles = allVehicles.length;
        }


    } catch (error) {
        console.log(error);

    }
}



//Fetching all reviews from backend
async function fetchingReviews() {
    try {
        let adminEmail = admin.email;
        console.log(adminEmail)
        let response = await fetch(`http://localhost:8080/review/getAllReviews?email=${adminEmail}`, {
            method: "GET"
            // headers: { "Content-Type": "application/json" }
        });
        if (response.ok) {
            let data = await response.json();
            allReviews = data;
            totalReviews = allReviews.length;
        }

    } catch (error) {
        console.log(error);
    }
}





// ====================== Ending fetching entities========================================

// ================================== start printing Tables=====================================

//used to print the complete booking table
function printingBookingsDataInTable(bookingsParam,eraseBefore = true) {

    

    // Clear existing content before adding new data
    if(eraseBefore)
        tablesContainer.innerHTML = "";

    let heading = document.createElement("h2");
    heading.innerText = "All Bookings";

    let bookingTable = document.createElement("table");
    bookingTable.classList.add("entityTable");


    let thead = document.createElement("thead");
    thead.innerHTML = `
        <tr>
            <th>ID</th>
            <th>User Email</th>
            <th>Vehicle Name</th>
            <th>Starting Date</th>
            <th>Ending Date</th>
            <th>Total Price</th>
        </tr>
    `;


    let tbody = document.createElement("tbody");



    if (!bookingsParam || bookingsParam.length === 0) {
        console.log("No bookings available.");
        return;
    }

    bookingsParam.forEach(element => {


        let bookId = document.createElement("td");
        bookId.innerText = element.booking_id;
        let startDate = document.createElement("td");
        startDate.innerText = element.startDate;
        let endDate = document.createElement("td");
        endDate.innerText = element.endDate;
        let userEmail = document.createElement("td");
        userEmail.innerText = element.user.email;
        let vehicleName = document.createElement("td");
        vehicleName.innerText = element.vehicle.name;
        let totalPrice = document.createElement("td");
        totalPrice.innerText = element.totalPrice.toFixed(2);

        totalEarnings += element.totalPrice;

        let row = document.createElement("tr");

        row.append(bookId, userEmail, vehicleName, startDate, endDate, totalPrice);

        tbody.appendChild(row);

    });

    bookingTable.appendChild(thead);
    bookingTable.appendChild(tbody);


    tablesContainer.appendChild(heading);
    tablesContainer.appendChild(bookingTable);
}

//printing Vehicles Data
function printingVehiclesDataInTable(vehiclesParam,eraseBefore = true) {



    

    // Clear existing content before adding new data
    if(eraseBefore)
        tablesContainer.innerHTML = "";

    let heading = document.createElement("h2");
    heading.innerText = "All Vehicles";

    let vehicleTable = document.createElement("table");
    vehicleTable.classList.add("entityTable");


    let thead = document.createElement("thead");
    thead.innerHTML = `
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Type</th>
            <th>Model</th>
            <th>Registration Number</th>
            <th>Availability</th>
            <th>Price per day</th>
            <th>Edit</th>

        </tr>
    `;


    let tbody = document.createElement("tbody");



    vehiclesParam.forEach(element => {
        let vehicleId = document.createElement("td");
        vehicleId.innerText = element.vehicle_id;
        let name = document.createElement("td");
        name.innerText = element.name;
        let type = document.createElement("td");
        type.innerText = element.type;
        let model = document.createElement("td");
        model.innerText = element.model;
        let registrationNumber = document.createElement("td");
        registrationNumber.innerText = element.registration_number;
        let availability = document.createElement("td");
        availability.innerText = element.availability;
        let pricePerDay = document.createElement("td");
        pricePerDay.innerText = element.price_per_day;


        //Providing to edit or delete the vehicles
        let modifyColumn = document.createElement("td")
        let deleteBtn = document.createElement("span");
        deleteBtn.classList.add("Del-icon");
        deleteBtn.classList.add("material-symbols-outlined");
        deleteBtn.innerText = "delete";

        deleteBtn.addEventListener("click", async () => {
            if (confirm("Are you sure you want to delete this Vehicle?")) {
                await deletingVehicleFromDB(element);
                console.log("Vehicle deleted successfully");

            }
        });

        let updateBtn = document.createElement("span");
        updateBtn.classList.add("update-btn");
        updateBtn.classList.add("material-symbols-outlined");
        updateBtn.innerText = "edit_square";


        updateBtn.addEventListener("click", () => {
            printingVehicleProfile(element, true);
        })


        modifyColumn.appendChild(deleteBtn);
        modifyColumn.appendChild(updateBtn);

        let newRow = document.createElement("tr");
        newRow.append(vehicleId, name, type, model, registrationNumber, availability, pricePerDay, modifyColumn);

        tbody.appendChild(newRow);

    });


    vehicleTable.appendChild(thead);
    vehicleTable.appendChild(tbody);


    tablesContainer.appendChild(heading);
    tablesContainer.appendChild(vehicleTable);
}

//print all users in table
function printingUsersDataInTable(usersParam,eraseBefore=true) {

    

    // Clear existing content before adding new data
    if(eraseBefore)
        tablesContainer.innerHTML = "";

    let heading = document.createElement("h2");
    heading.innerText = "All Users";

    let userTable = document.createElement("table");
    userTable.classList.add("entityTable");


    let thead = document.createElement("thead");
    thead.innerHTML = `
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Email</th>
            <th>Contact Number</th>
            <th>Role</th>
            <th>Edit</th>
        </tr>
    `;


    let tbody = document.createElement("tbody");



    usersParam.forEach(element => {
        let userId = document.createElement("td");
        userId.innerText = element.user_id;
        let name = document.createElement("td");
        name.innerText = element.name;
        let email = document.createElement("td");
        email.innerText = element.email;
        let contactNumber = document.createElement("td");
        contactNumber.innerText = element.contact_number;
        let role = document.createElement("td");
        role.innerText = element.role;
        if (element.role == "ADMIN")
            role.style.color = "var(--success)";


        let modifyColumn = document.createElement("td")
        let deleteBtn = document.createElement("span");
        deleteBtn.classList.add("Del-icon");
        deleteBtn.classList.add("material-symbols-outlined");
        deleteBtn.innerText = "delete";

        deleteBtn.addEventListener("click", async () => {
            if (confirm("Are you sure you want to delete this user?")) {
                await deletingUserFromDB(element);
                console.log("user deleted successfully");

            }
        });

        let updateBtn = document.createElement("span");
        updateBtn.classList.add("update-btn");
        updateBtn.classList.add("material-symbols-outlined");
        updateBtn.innerText = "edit_square";


        updateBtn.addEventListener("click", () => {
            printingProfile(element, true);
        })




        modifyColumn.appendChild(deleteBtn);
        modifyColumn.appendChild(updateBtn);



        let newRow = document.createElement("tr");
        newRow.append(userId, name, email, contactNumber, role, modifyColumn);

        tbody.appendChild(newRow);

    });


    userTable.appendChild(thead);
    userTable.appendChild(tbody);


    tablesContainer.appendChild(heading);
    tablesContainer.appendChild(userTable);
}



//printing the table of reviews
function printingReviewsDataInTable(reviewsParam,eraseBefore=true) {

    

    // Clear existing content before adding new data
    tablesContainer.innerHTML = "";

    let heading = document.createElement("h2");
    heading.innerText = "All Reviews";

    let reviewTable = document.createElement("table");
    reviewTable.classList.add("entityTable");


    let thead = document.createElement("thead");
    thead.innerHTML = `
        <tr>
            <th>ID</th>
            <th>Time</th>
            <th>User</th>
            <th>Vehicle </th>
            <th>Rating</th>
            <th>feedback</th>
        </tr>
    `;


    let tbody = document.createElement("tbody");



    reviewsParam.forEach(element => {
        let reviewId = document.createElement("td");
        reviewId.innerText = element.reviewId;
        let time = document.createElement("td");
        time.innerText = element.reviewTime;
        let userMail = document.createElement("td");
        userMail.innerText = element.user.email;
        let vehicle = document.createElement("td");
        vehicle.innerText = element.vehicle.name;
        let rating = document.createElement("td");
        rating.innerText = element.rating;
        let feedback = document.createElement("td");
        feedback.innerText = element.feedback;


        let newRow = document.createElement("tr");
        newRow.append(reviewId, time, userMail, vehicle, rating, feedback);

        tbody.appendChild(newRow);

    });


    reviewTable.appendChild(thead);
    reviewTable.appendChild(tbody);


    tablesContainer.appendChild(heading);
    tablesContainer.appendChild(reviewTable);
}


// =============================================Ending printing Tables =======================



















// ======================================Start Deleting entities===========================


//delete the user from db
async function deletingUserFromDB(userToDelete) {

    try {
        let adminEmail = admin.email;
        let response = await fetch(
            `http://localhost:8080/auth/delete/${adminEmail}`, {
            method: 'DELETE',
            headers: {
                "Content-type": "application/json"
            },
            body: JSON.stringify(userToDelete)
        }
        );

        if (response.ok) {
            // let result = await response.json();

            allUsers = allUsers.filter(user => user.user_id !== userToDelete.user_id);

            printingUsersDataInTable();

        }
        else {
            console.error("Error updating user:", response.statusText);
        }
    } catch (error) {
        console.error(error);
    }

}


//delete the vehicel from db
async function deletingVehicleFromDB(vehicleToDelete) {

    try {
        if (vehicleToDelete == null) {
            console.log("vehicle is null can't fetch the registration number to delete");
            return;
        }
        let vehicleRegistrationNumber = vehicleToDelete.registration_number;
        let adminEmail = admin.email;
        let response = await fetch(
            `http://localhost:8080/vehicle/delete/${vehicleRegistrationNumber}/${adminEmail}`, {
            method: 'DELETE',
            headers: {
                "Content-type": "application/json"
            }
        }
        );

        if (response.ok) {
            // let result = await response.json();

            allVehicles = allVehicles.filter(vehicle => vehicle.vehicle_id !== vehicleToDelete.vehicle_id);

            // console.log("Updated allVehicles :", allVehicles);

            printingVehiclesDataInTable();

        }
        else {
            console.error("Response status : ", response.status);
            console.error("Error updating user:", response.statusText);
        }
    } catch (error) {
        console.error(error);
    }

}


// ======================================Ending Deleting entities===========================













// =================================Start printing profiles=================================

//printing a layout for profile, making it generalize so that i dont have to create profile for each vehicle,rating,review etc.
//trying to do that here
function printingFormLayout(headingContent, fieldsComing, entityType, fromSection = false) {


    
    

    tablesContainer.innerHTML = "";
    cardContainer.innerHTML = "";

    //creating main heading of table
    let heading = document.createElement("h2");
    heading.innerText = headingContent;
    heading.classList.add("heading");

    //this will contain the user form
    let userFormContainer = document.createElement("div");
    userFormContainer.classList.add("userFormContainer");

    //This is a user form
    let userForm = document.createElement("form");
    userForm.classList.add("userForm");

    let fields = fieldsComing;

    fields.forEach(specificField => {

        let inputContainer = document.createElement("div");
        inputContainer.classList.add("inputContainer");


        let label = document.createElement("label");
        label.for = specificField.label;
        label.innerText = specificField.label;

        let input = document.createElement("input");
        input.value = specificField.value;
        if(input.value == null){
            input.value = "Not Specified yet";
        }
        input.id = specificField.id;
        input.name = specificField.label;
        input.readOnly = true;
        input.type = specificField.type;

        inputContainer.append(label, input);
        userForm.appendChild(inputContainer);
        // console.log(userForm);
    });

    //==================Edit btn starts===========
    let editBtn = document.createElement("button");
    editBtn.classList.add("editBtn");
    editBtn.innerText = "Update Details";

    // console.log("callled");


    editBtn.addEventListener("click", async () => {

        if (editBtn.innerText == "Save Changes") {

            let id = document.getElementById(fields[0].id).value;
            console.log(" id : ", id);

            if (isNaN(id)) {
                console.log("Invalid id or not a number");
                console.log(id);
                return;
            }


            if (entityType.toUpperCase() == "USER") {
                let updatedUser = {
                    name: (document.getElementById("name").value),
                    email: (document.getElementById("email").value),
                    contact_number: String(document.getElementById("contactNumber").value),
                    role: (document.getElementById("role").value).toUpperCase()
                };

                await updatingUserInDB(id, updatedUser, fromSection);
            }
            if (entityType.toUpperCase() == "VEHICLE") {
                let updatedVehicle = {
                    name: (document.getElementById("name").value),
                    type: (document.getElementById("type").value),
                    model: String(document.getElementById("model").value),
                    availability: (document.getElementById("availability").value).toUpperCase(),
                    price_per_day : parseFloat(document.getElementById("price_per_day").value),
                    registration_number : document.getElementById("registration_number").value
                };

                let registration_number = document.getElementById("registration_number").value;
                await updatingVehicleInDB(registration_number,updatedVehicle, fromSection);
            }
        }


        let inputs = document.querySelectorAll(".inputContainer input");


        inputs.forEach((ele) => {
            //userId and role cannot be updated
            if (ele.id != "userId" && ele.id != "vehicle_id" && ele.id != "role" && ele.id != "registration_number") {
                ele.readOnly = !ele.readOnly;
                ele.classList.toggle("editable");
            }
        });

        editBtn.innerText = inputs[2].readOnly ? "Update Details" : "Save Changes";

        // console.log(inputs);
    });



    userFormContainer.appendChild(userForm);
    userFormContainer.appendChild(editBtn);
    tablesContainer.appendChild(heading);
    tablesContainer.appendChild(userFormContainer);



}


//Printing a user profile
function printingProfile(element, fromUsersSection = false) {

    let userId = element.user_id;
    let name = element.name;
    let email = element.email;
    let contactNumber = element.contact_number;
    let role = element.role;


    let fields = [
        { label: "User ID", value: userId, id: "userId", type: "number" },
        { label: "Name", value: name, id: "name", type: "text" },
        { label: "Email", value: email, id: "email", type: "email" },
        { label: "Mobile No", value: contactNumber, id: "contactNumber", type: "number" },
        { label: "Role", value: role, id: "role", type: "text" }
    ];

    printingFormLayout("User Details", fields,"USER", fromUsersSection);
}


//printing a vehicle profile
function printingVehicleProfile(element,fromVehicleSection=false){

    let vehicleId = element.vehicle_id;
    let name = element.name;
    let type = element.type;
    let model = element.model;
    let availability = element.availability;
    let price_per_day = element.price_per_day;
    let registration_number = element.registration_number;


    let fields = [
        { label: "Vehicle ID", value: vehicleId, id: "vehicle_id", type: "number" },
        { label: "Name", value: name, id: "name", type: "text" },
        { label: "Type", value: type, id: "type", type: "text" },
        { label: "Model", value: model, id: "model", type: "text" },
        { label: "Availability Status", value: availability, id: "availability", type: "text"},
        { label: "Price per Day", value: price_per_day, id: "price_per_day", type: "number"},
        { label: "Registration Number", value: registration_number, id: "registration_number", type: "text"}
    ];

    printingFormLayout("Vehicle Details", fields,"VEHICLE", fromVehicleSection);

}

// =================================Ending printing profiles=================================










// ===================================START UPDATING IN DATABASE=======================================

async function updatingUserInDB(user_id, updatedUser, fromUsersSection = false) {

    try {
        let response = await fetch(
            `http://localhost:8080/auth/update/${user_id}`, {
            method: 'PUT',
            headers: {
                "Content-type": "application/json"
            },
            body: JSON.stringify(updatedUser)
        }
        );

        if (response.ok) {
            let result = await response.json();

            let index = allUsers.findIndex(user => user.user_id === result.user_id);
            if (index !== -1) {
                allUsers[index] = result;  // Update user in the array
            }


            //Checking if the admin updating his own details
            if (user_id == admin.user_id) {
                localStorage.setItem("admin", JSON.stringify(result));
                admin = result;
            }


            if (fromUsersSection) {
                printingUsersDataInTable();
            }
            else {

                printingProfile(result);
            }

        }
        else {
            console.error("Error updating user:", response.statusText);
        }
    } catch (error) {
        console.error(error);
    }

}



async function updatingVehicleInDB(registration_number,updatedVehicle,fromVehicleSection = false) {

    try {
        let adminEmail = admin.email;
        if(adminEmail == null ){
            console.log("admin email is null ");
            return;
        }
        let response = await fetch(
            `http://localhost:8080/vehicle/update/${registration_number}/${adminEmail}`, {
            method: 'PUT',
            headers: {
                "Content-type": "application/json"
            },
            body: JSON.stringify(updatedVehicle)
        }
        );

        if (response.ok) {
            let result = await response.json();

            let index = allVehicles.findIndex(vehicle => vehicle.vehicle_id === result.vehicle_id);
            if (index !== -1) {
                allVehicles[index] = result;  // Update user in the array
            }


            if (fromVehicleSection) {
                printingVehiclesDataInTable();
            }

        }
        else {
            console.error("Error updating user:", response.statusText);
        }
    } catch (error) {
        console.error(error);
    }

}


// ===================================END UPDATING IN DATABASE=======================================







// ==============================================Searching Starts======================================
//searching by keywords
async function searchingByVehicleKeywords() {
    let input = document.getElementById("searchBarId").value;
    console.log(input);

    tablesContainer.innerHTML = "";
    cardContainer.innerHTML = "";

    //Fetching all vehicles
    let outputVehicle = await fetchingVehiclesOnKeyword(input);
    console.log(outputVehicle);
    if(outputVehicle!=null)
        printingVehiclesDataInTable(outputVehicle);
    
    //Fetching all users
    let outputUser = await fetchingUsersOnKeyword(input);
    // console.log(outputUser);
    if(outputUser!=null)
        printingUsersDataInTable(outputUser,false);

    if(outputUser==null && outputVehicle==null){
        tablesContainer.innerHTML = "<h2> No Content found <h2>"
    }

}

//fetching the result for keyword from db
async function fetchingVehiclesOnKeyword(keyword) {
    try {
        let response = await fetch(`http://localhost:8080/vehicle/searching/${keyword}`, {
            method: "GET"
            // headers: { "Content-Type": "application/json" }
        });
        if (response.ok) {
            let data = await response.json();
            let searchedVehicles = data;
            console.log(searchedVehicles);
            return searchedVehicles;
            //total result found has to be implemented
        }


    } catch (error) {
        console.log(error);
        return null;

    }
}


//fetching the result for keyword from db
async function fetchingUsersOnKeyword(keyword) {
    try {
        let response = await fetch(`http://localhost:8080/auth/searching/${keyword}`, {
            method: "GET"
            // headers: { "Content-Type": "application/json" }
        });
        if (response.ok) {
            let data = await response.json();
            let searchedUsers = data;
            console.log(searchedUsers);
            return searchedUsers;
            //total result found has to be implemented
        }


    } catch (error) {
        console.log(error);
        return null;

    }
}



//adding a new user and admin
//adding a new vehicle , updating the vehicle, removing a vehicle
//same for bookings....
//implementing logout.......