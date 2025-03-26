let allBookings;
let allUsers;
let allVehicles;
let allReviews;
let admin = JSON.parse(localStorage.getItem("admin"));
if(admin!=null)
    document.querySelector(".circularPhoto").innerText = admin.name.charAt(0).toUpperCase()+admin.name.charAt(1).toUpperCase();
let totalUsers;
let totalBookings;
let totalReviews;
let totalVehicles;
let totalEarnings = 0;



// ==================START js used mainly for css manipulations========================
//table container that holds maximum outputs
let tablesContainer = document.querySelector(".tablesContainer");
//cards Container that is having cards
let cardContainer = document.querySelector(".cardContainer");

// add hovered class to selected list item
let list = document.querySelectorAll(".listOfMenus .listItem");

function activeLink() {
    console.log(list);
    list.forEach((item) => {
        item.classList.remove("active");
    });
    this.classList.add("active");
}

list.forEach((item) => item.addEventListener("click", activeLink));


// ==making the nav item active if there respective method is called===
function showActiveNavItem(navId){
    let list = document.querySelectorAll(".listOfMenus .listItem");

    list.forEach((item)=> item.classList.remove("active"));

    document.getElementById(navId).classList.add("active");

}


//toggling the navbar
let toggle = document.querySelector(".toggle");
let navigation = document.querySelector(".navigation");
let main = document.querySelector(".main");

toggle.onclick = function () {
    navigation.classList.toggle("active");
    main.classList.toggle("active");
};

// ==================START js used mainly for css manipulations========================


window.onload = async function () {


    await Promise.all([fetchingBookings(), fetchingUsers(), fetchingVehicles(), fetchingReviews()]);

    printingDashBoardData();

}



function printingOverviewOnDashBoard() {

    const cardData = [
        {
            counts: totalUsers,
            icon: "group",
            label: "Total Users",
            funToCall : () => printingUsersDataInTable(allUsers)
        },
        {
            counts: totalVehicles,
            icon: "directions_car",
            label: "Total Vehicles",
            funToCall : () => printingVehiclesDataInTable(allVehicles)
        },
        {
            counts: totalBookings,
            icon: "calendar_month",
            label: "Bookings",
            funToCall : () => printingBookingsDataInTable(allBookings)
        },
        {
            counts: totalReviews,
            icon:"hotel_class",
            label: "Total Reviews",
            funToCall : () => printingReviewsDataInTable(allReviews)
        },
        {
            counts: totalEarnings.toFixed(2),
            icon: "paid",
            label: "Total Earnings",
            funToCall : () => "printingDashBoardData()"
        },
        {
            counts: 1500,               //dummy value
            icon: "visibility",
            label: "Daily Views",
            funToCall : () => "printingDashBoardData()"
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
        cardsDiv.addEventListener("click",()=>{card.funToCall()});
        cardContainer.appendChild(cardsDiv);
    });


}



function profileClick() {
    showActiveNavItem("profileNav");
    let user = JSON.parse(localStorage.getItem("admin"));
    printingProfile(user);
}



function printingDashBoardData() {
    // let firstTenUsers = allUsers.slice(0, Math.min(10, allUsers.length));
    let mostRecentTenBookings = allBookings.slice(-Math.min(10, allBookings.length));
    printingBookingsDataInTable(mostRecentTenBookings.reverse(),true,"Recent Bookings");
    // printingUsersDataInTable(firstTenUsers,false);
    printingOverviewOnDashBoard();
    showActiveNavItem("dashboardNav");
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

            allBookings.forEach((element)=>{
                if(element.booking_status=="CONFIRMED"){
                    totalEarnings += element.totalPrice;
                }
            });
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
function printingBookingsDataInTable(bookingsParam,eraseBefore = true,headingParam="All Bookings") {

    
    // Clear existing content before adding new data
    if(eraseBefore){
        tablesContainer.innerHTML = "";
        cardContainer.innerHTML = "";
        showActiveNavItem("bookingNav");
    }

    let heading = document.createElement("h2");
    heading.innerText = headingParam;


    //table booking is being implemented here

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
            <th>Status</th>
        </tr>
    `;


    let tbody = document.createElement("tbody");



    if (!bookingsParam || bookingsParam.length === 0) {
        console.log("No bookings available.");
        return;
    }

    //resetting the totalEarning before calculating again
    // totalEarnings = 0;
    bookingsParam.forEach(element => {
        console.log(element);


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

       

        let statusBox = document.createElement("td");
        statusBox.classList.add("statusBox");
        let status = document.createElement("span");
        status.innerText = element.booking_status;
        if(element.booking_status=="CONFIRMED")
            status.classList.add("success");
        else
            status.classList.add("danger");

        statusBox.appendChild(status);

        let row = document.createElement("tr");

        row.append(bookId, userEmail, vehicleName, startDate, endDate, totalPrice,statusBox);

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
    if(eraseBefore){
        tablesContainer.innerHTML = "";
        cardContainer.innerHTML = "";
        showActiveNavItem("vehicleNav");
    }

    let heading = document.createElement("h2");
    heading.innerText = "All Vehicles";


    // ==========================Add button starts=============
    
    let addBtn = document.createElement("span");
    addBtn.classList.add("material-symbols-outlined");
    addBtn.innerText = "add";
    addBtn.classList.add("addBtn");
    addBtn.title = "Add Vehicle";
    
    
    
    addBtn.addEventListener("click",async ()=>{
        
        let fields = [  
    { label: "Name", value: "", id: "name", type: "text", required : true },
    { label: "Type", value: "", id: "type", type: "select",options:["CAR","BIKE","TRUCK"], required : true },
    { label: "Model", value: "", id: "model", type: "text", required : true },
    { label: "Availability Status", value: "", id: "availability", type: "select",options : ["AVAILABLE","UNDER_MAINTENANCE"], required : true },
    { label: "Price per Day", value: "", id: "price_per_day", type: "number", required : true },
    { label: "Registration Number", value: "", id: "registration_number", type: "text", required : true },
    { label: "Color", value: "", id: "color", type: "text", required : true },
    { label: "Vehicle Image URL", value: "", id: "vehicleImage", type: "text", required : true },
    { label: "Fuel Type", value: "", id: "fuelType", type: "select", options : ["PETROL", "DIESEL", "ELECTRIC","HYBRID","CNG"], required : true },
    { label: "Mileage (km/l)", value: "", id: "mileage", type: "number", required : true },
    { label: "Seating Capacity", value: "", id: "seatingCapacity", type: "number", required : true }
];
        
        printingFormLayout("Enter New Vehicle Details",fields,"VEHICLE", true, true);
    });
    
    
    // ==========================Add button ends=============


    //Vehicle table

    let vehicleTable = document.createElement("table");
    vehicleTable.classList.add("entityTable");


    let thead = document.createElement("thead");
    thead.innerHTML = `
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Type</th>
            <th>Fuel Type</th>
            <th>Capacity</th>
            <th>Mileage</th>
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
        let fuelType = document.createElement("td");
        fuelType.innerText = element.fuelType;
        let capacity = document.createElement("td");
        capacity.innerText = element.seatingCapacity;
        let mileage = document.createElement("td");
        mileage.innerText = element.mileage;
        
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
        newRow.append(vehicleId, name, type, fuelType, capacity,mileage, availability, pricePerDay, modifyColumn);

        tbody.appendChild(newRow);

    });


    vehicleTable.appendChild(thead);
    vehicleTable.appendChild(tbody);
    vehicleTable.appendChild(addBtn);


    tablesContainer.appendChild(heading);
    tablesContainer.appendChild(vehicleTable);
}

//print all users in table
function printingUsersDataInTable(usersParam,eraseBefore=true) {

    
    // Clear existing content before adding new data
    if(eraseBefore){
        tablesContainer.innerHTML = "";
        cardContainer.innerHTML = "";
        showActiveNavItem("userNav");
    }

    let heading = document.createElement("h2");
    heading.innerText = "All Users";

    // =============== Add user btn ==================
    let addBtn = document.createElement("span");
    addBtn.classList.add("material-symbols-outlined");
    addBtn.innerText = "add";
    addBtn.classList.add("addBtn");
    addBtn.title = "Add User"



    addBtn.addEventListener("click",async ()=>{
        console.log("add btn clicked");

        let fields = [
            { label: "Name", value: "", id: "name", type: "text",required : true },
            { label: "Password", value: "", id: "password", type: "password",required : true },
            { label: "Email", value: "", id: "email", type: "email",required : true },
            { label: "Mobile No", value: "", id: "contactNumber", type: "number",required : false },
            { label: "Role", value: "USER", id: "role", type: "text",required : true }
        ];

        printingFormLayout("Enter New User Details",fields,"USER", true, true);
    });

    // Add User btn ends===============


    //= User table creation starts

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
            if(element.role == "ADMIN"){
                alert("You cannot delete ADMIN");
                return;
            }
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
    userTable.appendChild(addBtn);

    // ==============================User table creation ends ===============================


    tablesContainer.appendChild(heading);
    tablesContainer.appendChild(userTable);
}



//printing the table of reviews
function printingReviewsDataInTable(reviewsParam,eraseBefore=true) {

    
    // Clear existing content before adding new data
    if(eraseBefore){
        tablesContainer.innerHTML = "";
        cardContainer.innerHTML = "";
        showActiveNavItem("reviewNav");
    }

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
            totalUsers--;

            printingUsersDataInTable(allUsers);

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
            totalVehicles--;

            // console.log("Updated allVehicles :", allVehicles);

            printingVehiclesDataInTable(allVehicles);

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
function printingFormLayout(headingContent, fieldsComing, entityType,isAdding=false , fromSection = false) {

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

        if (specificField.type == "select") {

            let inputSelect = document.createElement("select");
            inputSelect.id = specificField.id;
            
            specificField.options.forEach((ele) => {
                console.log(ele);
                let option = document.createElement("option");
                option.text = ele;
                option.value = ele;
                

                inputSelect.appendChild(option);
            });
            inputSelect.value = specificField.value;
            
            if(!isAdding || specificField.id=="role"){
                inputSelect.classList.add("read-only-select");
            }
            if(isAdding)
                inputSelect.required = specificField.required;

            inputContainer.append(label,inputSelect);

        }else{

            
            let input = document.createElement("input");
            // if(input.value == null){
                //     input.value = "Not Specified yet";
                // }
                    input.id = specificField.id;
            input.name = specificField.label;
            input.value = specificField.value;
            if(!isAdding || specificField.id=="role"){
                input.readOnly = true;
            }
            if(isAdding)
                input.required = specificField.required;
            
            input.type = specificField.type;
            
            inputContainer.append(label, input);
        }   
        
        userForm.appendChild(inputContainer);
        // console.log(userForm);
    });

    //==================Edit btn starts===========
    let editBtn = document.createElement("button");
    editBtn.classList.add("editBtn");
    if(isAdding && entityType=="USER"){
        editBtn.innerText = "Create User";
    }
    else if(isAdding && entityType=="VEHICLE"){
        editBtn.innerText = "Create Vehicle";
    }
    else{
        editBtn.innerText = "Update Details";
    }

    // console.log("callled");

    editBtn.addEventListener("click", async () => {

        if (!validateForm(fields)) {
            return; // Stop if form is invalid
        }
        if(isAdding){

            if (entityType.toUpperCase() == "USER") {
                let newUser = {
                    name: (document.getElementById("name").value),
                    email: (document.getElementById("email").value),
                    contact_number: String(document.getElementById("contactNumber").value),
                    role: (document.getElementById("role").value).toUpperCase(),
                    password : (document.getElementById("password").value)
                };

                await storingNewUserInDB(newUser);
            }
            if (entityType.toUpperCase() == "VEHICLE") {
                let newVehicle = {
                    name: document.getElementById("name").value,
                    type: document.getElementById("type").value.toUpperCase(),
                    model: String(document.getElementById("model").value),
                    availability: document.getElementById("availability").value.toUpperCase(),
                    price_per_day: parseFloat(document.getElementById("price_per_day").value),
                    registration_number: document.getElementById("registration_number").value,
                    color: document.getElementById("color").value,
                    vehicleImage: document.getElementById("vehicleImage").value,
                    fuelType: document.getElementById("fuelType").value.toUpperCase(),
                    mileage: parseFloat(document.getElementById("mileage").value),
                    seatingCapacity: parseInt(document.getElementById("seatingCapacity").value)
                };
                
                await storingNewVehicleInDB(newVehicle);
            }
        }
        else{

       
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
                    role: (document.getElementById("role").value).toUpperCase(),
                };

                await updatingUserInDB(id, updatedUser, fromSection);
            }
            if (entityType.toUpperCase() == "VEHICLE") {
                let updatedVehicle = {
                    name: document.getElementById("name").value,
                    type: document.getElementById("type").value.toUpperCase(),
                    model: String(document.getElementById("model").value),
                    availability: document.getElementById("availability").value.toUpperCase(),
                    price_per_day: parseFloat(document.getElementById("price_per_day").value),
                    registration_number: document.getElementById("registration_number").value,
                    color: document.getElementById("color").value,
                    vehicleImage: document.getElementById("vehicleImage").value,
                    fuelType: document.getElementById("fuelType").value.toUpperCase(),
                    mileage: parseFloat(document.getElementById("mileage").value),
                    seatingCapacity: parseInt(document.getElementById("seatingCapacity").value)
                };

                console.log("updated vehicle : ", updatedVehicle);

                let registration_number = document.getElementById("registration_number").value;
                await updatingVehicleInDB(registration_number,updatedVehicle, fromSection);
                console.log("updatingVehicleInDB called");
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


        let selects = document.querySelectorAll(".inputContainer select");


        selects.forEach((ele) => {
            //userId and role cannot be updated
            if ( ele.id != "role") {
                    ele.classList.remove("read-only-select");
            }
        });

        // console.log(inputs);

        if(inputs!=null && inputs.length!=0)
            editBtn.innerText = inputs[2].readOnly ? "Update Details" : "Save Changes";

    }
        // console.log(inputs);
    });



    userFormContainer.appendChild(userForm);
    userFormContainer.appendChild(editBtn);
    tablesContainer.appendChild(heading);
    tablesContainer.appendChild(userFormContainer);



}

//////validing
function validateForm(fields) {
    let isValid = true;
    fields.forEach(field => {
        let inputElement = document.getElementById(field.id);
        if (inputElement && inputElement.hasAttribute("required")) {
            if (!inputElement.value || inputElement.value.trim() === "") {
                alert(`Please fill in the required field: ${field.label}`);
                isValid = false;
                inputElement.focus();
                return false;
            }
        }
    });
    return isValid;
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

    printingFormLayout("User Details", fields,"USER",false,fromUsersSection);
}


//printing a vehicle profile
function printingVehicleProfile(element,fromVehicleSection=false){

    console.log(element);

    let vehicleId = element.vehicle_id;
let name = element.name;
let type = element.type;
let model = element.model;
let availability = element.availability;
let price_per_day = element.price_per_day;
let registration_number = element.registration_number;
let color = element.color;
let vehicleImage = element.vehicleImage;
let fuelType = element.fuelType;
let mileage = element.mileage;
let seatingCapacity = element.seatingCapacity;

let fields = [
    { label: "Vehicle ID", value: vehicleId, id: "vehicle_id", type: "number" },
    { label: "Name", value: name, id: "name", type: "text" },
    { label: "Type", value: type, id: "type", type: "select",options:["CAR","BIKE","TRUCK"] },
    { label: "Model", value: model, id: "model", type: "text" },
    { label: "Availability Status", value: availability, id: "availability",  type: "select",options : ["AVAILABLE","UNDER_MAINTENANCE"]},
    { label: "Price per Day", value: price_per_day, id: "price_per_day", type: "number" },
    { label: "Registration Number", value: registration_number, id: "registration_number", type: "text" },
    { label: "Color", value: color, id: "color", type: "text" },
    { label: "Vehicle Image URL", value: vehicleImage, id: "vehicleImage", type: "text" },
    { label: "Fuel Type", value: fuelType, id: "fuelType", type: "select", options : ["PETROL", "DIESEL", "ELECTRIC","HYBRID","CNG"] },
    { label: "Mileage (km/l)", value: mileage, id: "mileage", type: "number" },
    { label: "Seating Capacity", value: seatingCapacity, id: "seatingCapacity", type: "number" }
];

printingFormLayout("Vehicle Details", fields, "VEHICLE", false, fromVehicleSection);


}

// =================================Ending printing profiles=================================



// ==================================== Storing In Databases =================================
// async function storingNewUserInDB(newUser){
//     try {
//         let adminEmail = admin.email;
//         let response = await fetch(
//             `http://localhost:8080/auth/createAdmin?alreadyAdminEmail=${adminEmail}`, {
//             method: 'POST',
//             headers: {
//                 "Content-type": "application/json"
//             },
//             body: JSON.stringify(newUser)
//         }
//         );

//         if (response.ok) {
//             let result = await response.json();

//             allUsers.push(result);
//             totalUsers++;


//             alert("Admin added succesfully");
//             printingUsersDataInTable(allUsers);


//         }
//         else {
//             console.error("Error updating user:", response.statusText);
//         }
//     } catch (error) {
//         console.error(error);
//     }

// }

async function storingNewUserInDB(newUser){
    try {
        let response = await fetch(
            `http://localhost:8080/auth/signUp`, {
            method: 'POST',
            headers: {
                "Content-type": "application/json"
            },
            body: JSON.stringify(newUser)
        }
        );

        if (response.ok) {
            let result = await response.json();

            allUsers.push(result);
            totalUsers++;


            alert("User added succesfully");
            printingUsersDataInTable(allUsers);


        }
        else {
            console.error("Error updating user:", response.statusText);
        }
    } catch (error) {
        console.error(error);
    }

}



async function storingNewVehicleInDB(newVehicle){
    try {
        let adminEmail = admin.email;
        let response = await fetch(
            `http://localhost:8080/vehicle/add?email=${adminEmail}`, {
            method: 'POST',
            headers: {
                "Content-type": "application/json"
            },
            body: JSON.stringify(newVehicle)
        }
        );

        if (response.ok) {
            let result = await response.json();

            allVehicles.push(result);
            totalVehicles++;

            alert("vehicle added successfully");

            printingVehiclesDataInTable(allVehicles);

        }
        else {
            alert("Error updating user : ", response.status);
            console.error("Error updating user:", response.statusText);
        }
    } catch (error) {
        console.error(error);
    }

}







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
                printingUsersDataInTable(allUsers);
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

            alert("vehicle updated successfully as" + result);


            if (fromVehicleSection) {
                printingVehiclesDataInTable(allVehicles);
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
    if(outputUser!=null)
        printingUsersDataInTable(outputUser,false);
    
    //Fetching all Reviews
    let outputReview = await fetchingReviewsOnKeywords(input);
    if(outputReview!=null)
        printingReviewsDataInTable(outputReview,false);
    
    //Fetching all Bookings
    let outputBooking = await fetchingBookingsOnKeywords(input);
    if(outputBooking!=null)
        printingBookingsDataInTable(outputBooking,false);
    

    if(outputUser==null && outputVehicle==null&&outputReview==null&&outputBooking==null){
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

//fetching the booking for the keyword form db
async function fetchingReviewsOnKeywords(keyword) {
    try {
        let response = await fetch(`http://localhost:8080/review/searching/${keyword}`, {
            method: "GET"
            // headers: { "Content-Type": "application/json" }
        });
        if (response.ok) {
            let data = await response.json();
            let searchedReviews = data;
            console.log(searchedReviews);
            return searchedReviews;
            //total result found has to be implemented
        }


    } catch (error) {
        console.log(error);
        return null;

    }
    
}
//fetching the booking for the keyword form db
async function fetchingBookingsOnKeywords(keyword) {
    try {
        let response = await fetch(`http://localhost:8080/booking/searching/${keyword}`, {
            method: "GET"
            // headers: { "Content-Type": "application/json" }
        });
        if (response.ok) {
            let data = await response.json();
            console.log(data);
            return data;
            //total result found has to be implemented
        }


    } catch (error) {
        console.log(error);
        return null;

    }
    
}




//implementing logout.......

function logout(){
    localStorage.removeItem("admin");
    window.location.href = "index.html";
}