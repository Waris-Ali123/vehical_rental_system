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



window.onload = async function(){
    

    await getBookingData();
    await fetchingUsers();
    await fetchingVehicles();
    await fetchingReviews();

    printingBookingsDataInTable();
    printingOverviewOnDashBoard();
 
}



function printingOverviewOnDashBoard(){

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
          icon: "reviews",
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

      let cardContainer = document.querySelector(".cardContainer");
      cardContainer.innerHTML = "";
      console.log(cardContainer);

      cardData.forEach((card)=>{
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

        topDiv.append(counts,iconSpan);

        let lable = document.createElement("div");
        lable.classList.add("label");
        lable.innerText = card.label;

        cardsDiv.append(topDiv,lable);
        cardContainer.appendChild(cardsDiv);
      });


}






function profileClick(){
    let user = JSON.parse(localStorage.getItem("admin"));
    printingProfile(user);
}



function printingDashBoardData(){
    
    
    printingOverviewOnDashBoard();
    printingBookingsDataInTable();
}


//used to print the complete booking table
function printingBookingsDataInTable(){
    
    let tablesContainer = document.querySelector(".tablesContainer");

    // Clear existing content before adding new data
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



    if (!allBookings || allBookings.length === 0) {
        console.log("No bookings available.");
        return;
    }

    allBookings.forEach(element => {
    

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
    // console.log(totalEarnings);

    let row = document.createElement("tr");

    row.append(bookId,userEmail,vehicleName,startDate,endDate,totalPrice);

    tbody.appendChild(row);

    });

    bookingTable.appendChild(thead);
    bookingTable.appendChild(tbody);


    tablesContainer.appendChild(heading);
    tablesContainer.appendChild(bookingTable);
}











//Get the bookingData
async function getBookingData(){

    try{

        let admin = JSON.parse(localStorage.getItem("admin"));
        let  adminEmail = admin.email;


        let response = await fetch(`http://localhost:8080/booking/getAllBookings?email=${adminEmail}`, {
            method: "GET"
            // headers: { "Content-Type": "application/json" }
        });

        if(response.ok){

            allBookings = await response.json();
            totalBookings = allBookings.length;
        }

        if (!response.ok) {
            throw new Error(`Error: ${response.status}`);
        }
        
    } catch(error) {
        console.error("Error fetching data:", error);
    }
    
}








//Fetching all users from backend
async function fetchingUsers() {
    try {
        let adminEmail = admin.email;
        let response = await fetch(`http://localhost:8080/auth/getAllUsers?email=${adminEmail}`, {
            method: "GET"
            // headers: { "Content-Type": "application/json" }
        });
        if(response.ok){
            let data = await response.json();
            allUsers = data;
            totalUsers = allUsers.length;

        }
        
        // printingUsersDataInTable();
    } catch (error) {
        console.log(error);
        
    }
}







//print all users in table
function printingUsersDataInTable(){

    let tablesContainer = document.querySelector(".tablesContainer");

    // Clear existing content before adding new data
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
            <th>Delete</th>


        </tr>
    `;


    let tbody = document.createElement("tbody");


    
    allUsers.forEach(element =>{
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
        if(element.role == "ADMIN")
            role.style.color = "var(--success)";


        let modifyColumn = document.createElement("td")
        let deleteBtn = document.createElement("span");
        deleteBtn.classList.add("Del-icon");
        deleteBtn.classList.add("material-symbols-outlined");
        deleteBtn.innerText = "delete";

        deleteBtn.addEventListener("click",()=>{
            deletingUser(element);
            console.log("user deleted successfully");
        });

        let updateBtn = document.createElement("span");
        updateBtn.classList.add("update-btn");
        updateBtn.classList.add("material-symbols-outlined");
        updateBtn.innerText = "edit_square";


        updateBtn.addEventListener("click",()=>{
            printingProfile(element);
        })


        

        modifyColumn.appendChild(deleteBtn);
        modifyColumn.appendChild(updateBtn);



        let newRow = document.createElement("tr");
        newRow.append(userId,name,email,contactNumber,role,modifyColumn);

        newRow.addEventListener("click",()=>{
            printingProfile(element,true);            
        })

        tbody.appendChild(newRow);
        
    });


    userTable.appendChild(thead);
    userTable.appendChild(tbody);


    tablesContainer.appendChild(heading);
    tablesContainer.appendChild(userTable);
}



async function deletingUser(userToDelete) {

    try {        
        let adminEmail = admin.email;
        let response = await fetch(
            `http://localhost:8080/auth/delete/${adminEmail}`,{
                method : 'DELETE',
                headers : {
                    "Content-type":"application/json"
                },
                body : JSON.stringify(userToDelete)
            }
        );

        if(response.ok){
            let result = await response.json();

            let index = allUsers.findIndex(user => user.user_id === userToDelete.user_id);
            if (index !== -1) {
                allUsers.remove(index);
            }

            printingUsersDataInTable();

        }
        else{
            console.error("Error updating user:", response.statusText);
        }
    } catch (error) {
        console.error(error);
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
        if(response.ok){
            let data = await response.json();
            allVehicles = data;
            totalVehicles = allVehicles.length;
            console.log(totalVehicles,"total Vehicels");
        }
        
        // printingUsersDataInTable();
    } catch (error) {
        console.log(error);
        
    }
}






//printing Vehicles Data
function printingVehiclesDataInTable(){

    

    let tablesContainer = document.querySelector(".tablesContainer");

    // Clear existing content before adding new data
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
        </tr>
    `;


    let tbody = document.createElement("tbody");


    
    allVehicles.forEach(element =>{
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
        
        let newRow = document.createElement("tr");
        newRow.append(vehicleId,name,type,model,registrationNumber,availability,pricePerDay);

        tbody.appendChild(newRow);
        
    });


    vehicleTable.appendChild(thead);
    vehicleTable.appendChild(tbody);


    tablesContainer.appendChild(heading);
    tablesContainer.appendChild(vehicleTable);
}



async function updatingUserInDB(user_id , updatedUser, fromUsersSection = false){

    try {        
        let response = await fetch(
            `http://localhost:8080/auth/update/${user_id}`,{
                method : 'PUT',
                headers : {
                    "Content-type":"application/json"
                },
                body : JSON.stringify(updatedUser)
            }
        );

        if(response.ok){
            let result = await response.json();

            let index = allUsers.findIndex(user => user.user_id === result.user_id);
            if (index !== -1) {
                allUsers[index] = result;  // Update user in the array
            }


            //Checking if the admin updating his own details
            if(user_id == admin.user_id){
                localStorage.setItem("admin",JSON.stringify(result));
                admin = result;
            }

            
            if(fromUsersSection){
                printingUsersDataInTable();
            }
            else{

                printingProfile(result);
            }

        }
        else{
            console.error("Error updating user:", response.statusText);
        }
    } catch (error) {
        console.error(error);
    }
        
}





//Printing a user profile
function printingProfile(element,fromUsersSection=false){

    let tablesContainer = document.querySelector(".tablesContainer");
    let cardContainer = document.querySelector(".cardContainer");

    tablesContainer.innerHTML = "";
    cardContainer.innerHTML = "";

    let heading = document.createElement("h2");
    heading.innerText = "User Details";
    heading.classList.add("heading");

    let userId =  element.user_id;
    let name = element.name;
    let email = element.email;
    let contactNumber = element.contact_number;
    let role = element.role;


    let userFormContainer = document.createElement("div");
    userFormContainer.classList.add("userFormContainer");

    let userForm = document.createElement("form");
    userForm.classList.add("userForm");


    let fields = [
        {label : "User ID",value : userId , id : "userId"},
        {label : "Name",value : name , id : "name"},
        {label : "Email",value : email , id : "email"},
        {label : "Mobile No",value : contactNumber , id : "contactNumber"},
        {label : "Role",value : role , id : "role"}
    ];

    fields.forEach(specificField =>{

        let inputContainer = document.createElement("div");
        inputContainer.classList.add("inputContainer");


        let label = document.createElement("label");
        label.for  = specificField.label;
        label.innerText = specificField.label;
        // label.id = specificField.id;

        let input = document.createElement("input");
        input.value = specificField.value;
        input.id = specificField.id;
        input.name = specificField.label;
        input.readOnly = true;
        if(input.id == "email")
            input.type = "email";
        else if(input.id == "userId" || input.id == "contactNumber")
            input.type = "number";
        else
            input.type = "text";

        inputContainer.append(label,input);
        userForm.appendChild(inputContainer);
    });
    
    
    let editBtn = document.createElement("button");
    editBtn.classList.add("editBtn");
    editBtn.innerText = "Update Details";

    
    
    editBtn.addEventListener("click",async ()=>{

        if(editBtn.innerText == "Save Changes"){

            let user_id  = document.getElementById("userId").value;

            if(isNaN(user_id)){
                console.log("Invalid user id or not a number");
                console.log(user_id);
                return ;
            }
            let updatedUser = {
                name  :  (document.getElementById("name").value),
                email  : (document.getElementById("email").value),
                contact_number  : String(document.getElementById("contactNumber").value),
                role  : (document.getElementById("role").value).toUpperCase()
            };

            await updatingUserInDB(user_id ,updatedUser,fromUsersSection);
        }


        let inputs  = document.querySelectorAll(".inputContainer input");
        // editBtn.classList.toggle("saveChanges");

        
        inputs.forEach((ele)=>{
            //userId and role cannot be updated
            if(ele.id != "userId" && ele.id != "role"){
                ele.readOnly = !ele.readOnly;
                ele.classList.toggle("editable");
            }
        });

        editBtn.innerText = inputs[2].readOnly ? "Update Details" : "Save Changes";

    });
    
    userFormContainer.appendChild(userForm);
    userFormContainer.appendChild(editBtn);
    tablesContainer.appendChild(heading);
    tablesContainer.appendChild(userFormContainer);




    // console.log(userId);
    // console.log(name);
    // console.log(email);
    // console.log(contactNumber);
    // console.log(role);
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
        if(response.ok){
            let data = await response.json();
            allReviews = data;
            totalReviews = allReviews.length;
        }
        
        // printingUsersDataInTable();
    } catch (error) {
        console.log(error);  
    }
}

//printing the table of reviews
function printingReviewsDataInTable(){

    

    let tablesContainer = document.querySelector(".tablesContainer");

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


    
    allReviews.forEach(element =>{
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
        newRow.append(reviewId,time,userMail,vehicle,rating,feedback);

        tbody.appendChild(newRow);
        
    });


    reviewTable.appendChild(thead);
    reviewTable.appendChild(tbody);


    tablesContainer.appendChild(heading);
    tablesContainer.appendChild(reviewTable);
}















//searching by keywords
//adding a new user and admin
//adding a new vehicle , updating the vehicle, removing a vehicle
//same for bookings....
//implementing logout.......