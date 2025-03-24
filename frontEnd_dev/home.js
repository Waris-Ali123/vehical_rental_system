let user = JSON.parse(localStorage.getItem("user"));
console.log(user);
if(user!=null)
    document.querySelector(".circularPhoto").innerText = user.name.charAt(0).toUpperCase()+user.name.charAt(1).toUpperCase();


let allBookings;
let totalBookings;
let totalEarnings = 0;
let allReviews ;
let allVehicles;
let totalVehicles = 0;
let totalReviews = 0 ;


//providing default date for fetching the availables initially
// Get today's date in YYYY-MM-DD format
let startDate = new Date().toISOString().split('T')[0];
let tomorrow = new Date();
tomorrow.setDate(tomorrow.getDate()+1);
let endDate = tomorrow.toISOString().split('T')[0];









// ==================START js used mainly for css manipulations========================
//table container that holds maximum outputs
let tablesContainer = document.querySelector(".tablesContainer");
//cards Container that is having cards
let cardContainer = document.querySelector(".cardContainer");
//filter container that is having all filters
let filterContainer = document.querySelector(".filterContainer");


// add hovered class to selected list item
let list = document.querySelectorAll(".listOfMenus .listItem");

function activeLink() {
    
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


// toggling the navbar
let toggle = document.querySelector(".toggle");
let navigation = document.querySelector(".navigation");
let main = document.querySelector(".main");

toggle.onclick = function () {
    navigation.classList.toggle("active");
    main.classList.toggle("active");
};

// ==================END js used mainly for css manipulations========================


window.onload = async function () {
 
   
    await Promise.all([fetchingBookings(), fetchingReviews(), fetchingVehiclesAvailable(),fetchingVehicles()]);

    printingBookingsDataInTable(allBookings);
   

}
// ======================================nav bar functions start ===============
function printingOnClickVehicleNav(){
    printingVehiclesDataInTable(allVehicles,true,true);

    printingFilters();
}

// ======================================nav bar functions end ===============





// ====================== Start fetching entities========================================


//Get the bookingData
async function fetchingBookings() {

    try {

        let user = JSON.parse(localStorage.getItem("user"));
        let userEmail = user.email;


        let response = await fetch(`http://localhost:8080/booking/getByEmail?email=${userEmail}`, {
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



//Fetching all reviews from backend
async function fetchingReviews() {
    try {
        let userEmail = user.email;
        console.log(userEmail)
        let response = await fetch(`http://localhost:8080/review/getReviewsByUser?email=${userEmail}`, {
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



//Fetching all vehicles from backend
async function fetchingVehiclesAvailableWithType(start=startDate,end=endDate,type = "BIKE") {
    try {
        
        let response = await fetch(`http://localhost:8080/vehicle/findingAvailable/${type}?startDate=${start}&endDate=${end}`, {
            method: "GET"
            // headers: { "Content-Type": "application/json" }
        });
        console.log("called the fetching vehicles");
        if (response.ok) {
            let data = await response.json();
            console.log(data);
            return data;
        }

        return null;

    } catch (error) {
        console.log(error);
        return null;

    }
}


//Fetching all vehicles from backend
async function fetchingVehiclesAvailable(start=startDate,end=endDate) {
    try {
        
        let response = await fetch(`http://localhost:8080/vehicle/findingAvailable?startDate=${start}&endDate=${end}`, {
            method: "GET"
            // headers: { "Content-Type": "application/json" }
        });
        console.log("called the fetching vehicles");
        if (response.ok) {
            let data = await response.json();
            console.log(data);
            return data;
        }

        return null;

    } catch (error) {
        console.log(error);
        return null;

    }
}



//Fetching all vehicles from backend
async function fetchingVehicles() {
    try {
        let response = await fetch(`http://localhost:8080/vehicle/getAllVehicles`, {
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





// ====================== Ending fetching entities========================================

function printingBookingsDataInTable(bookingsParam,eraseBefore = true,headingParam="Bookings History") {

    
    // Clear existing content before adding new data
    if(eraseBefore){
        tablesContainer.innerHTML = "";
        cardContainer.innerHTML = "";
        filterContainer.innerHTML = "";
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


//printing the table of reviews
function printingReviewsDataInTable(reviewsParam,eraseBefore=true) {

    
    // Clear existing content before adding new data
    if(eraseBefore){
        tablesContainer.innerHTML = "";
        cardContainer.innerHTML = "";
        showActiveNavItem("reviewNav");
        filterContainer.innerHTML = "";
    }
   
    let heading = document.createElement("h2");
    heading.innerText = "Review History";

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


//printing Vehicles Data
function printingVehiclesDataInTable(vehiclesParam,eraseBefore = true,eraseFilter = true) {

    
    // Clear existing content before adding new data
    if(eraseBefore){
        tablesContainer.innerHTML = "";
        cardContainer.innerHTML = "";
        if(eraseFilter){
            filterContainer.innerHTML="";
        }
        showActiveNavItem("vehicleNav");
    }

    let heading = document.createElement("h2");
    heading.innerText = "All Vehicles";


    

    //Vehicle table

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
            </tr>
            `;
            
            // <th>Edit</th>

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
        // let modifyColumn = document.createElement("td")
        // let deleteBtn = document.createElement("span");
        // deleteBtn.classList.add("Del-icon");
        // deleteBtn.classList.add("material-symbols-outlined");
        // deleteBtn.innerText = "delete";

        // deleteBtn.addEventListener("click", async () => {
        //     if (confirm("Are you sure you want to delete this Vehicle?")) {
        //         await deletingVehicleFromDB(element);
        //         console.log("Vehicle deleted successfully");

        //     }
        // });

        // let updateBtn = document.createElement("span");
        // updateBtn.classList.add("update-btn");
        // updateBtn.classList.add("material-symbols-outlined");
        // updateBtn.innerText = "edit_square";


        // updateBtn.addEventListener("click", () => {
        //     printingVehicleProfile(element, true);
        // })


        // modifyColumn.appendChild(deleteBtn);
        // modifyColumn.appendChild(updateBtn);

        let newRow = document.createElement("tr");
        // newRow.append(vehicleId, name, type, model, registrationNumber, availability, pricePerDay, modifyColumn);
        newRow.append(vehicleId, name, type, model, registrationNumber, availability, pricePerDay);

        tbody.appendChild(newRow);

    });


    vehicleTable.appendChild(thead);
    vehicleTable.appendChild(tbody);
    // vehicleTable.appendChild(addBtn);


    tablesContainer.appendChild(heading);
    tablesContainer.appendChild(vehicleTable);
}


function printingCardsForVehicle(vehiclesParam){
    tablesContainer.innerHTML = "";
    printingFilters();

    if(vehiclesParam.length==0){
        console.log("No Data Found");
    }
    else{

        vehiclesParam.forEach((vehicle)=>{
            // vehicle
        });











    }


}



//=============Putting filters in filter container=====

function printingFilters(){

    
    let fields = [
        { label: "Start Date",id: "startDate", type: "date"},
        { label: "End Date",id: "endDate", type: "date"},
        { label: "Type",id: "type", type: "select",options: ["ALL","CAR", "BIKE", "TRUCK"]}

    ];

    fields.forEach(element =>{

        let inputContainer = document.createElement("div");
        inputContainer.classList.add("inputContainer");

        let label = document.createElement("label");
        label.innerText = element.label;
        label.htmlFor = element.label;

        if (element.type == "select") {

            let inputSelect = document.createElement("select");
            inputSelect.id = element.id;

            element.options.forEach((ele) => {
                console.log(ele);
                let option = document.createElement("option");
                option.text = ele;
                option.value = ele;

                inputSelect.appendChild(option);
            });

            inputContainer.append(label,inputSelect);

        }else{
            let input = document.createElement("input");
            input.type = element.type;
            input.name = element.label
            input.id = element.id;

            inputContainer.append(label,input);
        }

            filterContainer.appendChild(inputContainer);
        
    });

    let filterBtn = document.createElement("div");
    filterBtn.innerText = "Apply filters";
    filterBtn.classList.add("filterBtn");
    filterBtn.addEventListener("click",async ()=>{
        let initial =  document.getElementById("startDate").value;
        let ending =  document.getElementById("endDate").value;
        let typeSelected = document.getElementById("type");
        let availableVehicles = null;
        if(typeSelected.value == "ALL"){
            availableVehicles = await fetchingVehiclesAvailable(initial,ending);
        }
        else{
            availableVehicles = await fetchingVehiclesAvailableWithType(initial,ending,typeSelected.value);
        }
        
        if(availableVehicles!=null){
                printingVehiclesDataInTable(availableVehicles,true,false);
                if(availableVehicles.length === 0){
                    tablesContainer.innerHTML = "<h2>No Content found</h2>"
                }
        }

    });


    filterContainer.appendChild(filterBtn);
}

//implementing logout.......

function logout(){
    localStorage.removeItem("user");
    window.location.href = "index.html";
}





// ======================================Ending Deleting entities===========================


