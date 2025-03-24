let user = JSON.parse(localStorage.getItem("user"));
console.log(user);
if(user!=null)
    document.querySelector(".circularPhoto").innerText = user.name.charAt(0).toUpperCase()+user.name.charAt(1).toUpperCase();


let allBookings;
let totalBookings;
let totalEarnings = 0;
let allReviews ;
let totalReviews = 0 ;






// ==================START js used mainly for css manipulations========================
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


    // await Promise.all([fetchingBookings(), fetchingUsers(), fetchingVehicles(), fetchingReviews()]);
    await Promise.all([fetchingBookings(), fetchingReviews()]);

    printingBookingsDataInTable(allBookings);
   

}






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

// ====================== Ending fetching entities========================================

function printingBookingsDataInTable(bookingsParam,eraseBefore = true,headingParam="Bookings History") {

    
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



//implementing logout.......

function logout(){
    localStorage.removeItem("user");
    window.location.href = "index.html";
}





// ======================================Ending Deleting entities===========================


