//Global Variables
let allBookings;
let totalBookings;
// let totalEarnings = 0;
let allReviews;
let totalReviews = 0;
let allVehicles;
let totalVehicles = 0;
let haveFilter = false;

//Initially fetching the user from locatStorage whoever logged in
let user = JSON.parse(localStorage.getItem("user"));

//Showing the user name in an profile icon on the top right corner of screeen
if (user != null){
  document.querySelector(".circularPhoto").innerText =
      user.name.charAt(0).toUpperCase() + user.name.charAt(1).toUpperCase();
}

//providing default date for fetching the availables initially
// Get today's date in YYYY-MM-DD format
let today = new Date().toISOString().split("T")[0];
let startDate = today;
let tomorrow = new Date();
tomorrow.setDate(tomorrow.getDate() + 1);
let endDate = tomorrow.toISOString().split("T")[0];

// =============================================Event Listeners=========================
let searchBar = document.getElementById("searchBarId");
searchBar.addEventListener("input", searchBarClick);

// =======================paging starts==================
let page1 = document.getElementById("page1");
let page2 = document.getElementById("page2");
let page3 = document.getElementById("page3");
let page4 = document.getElementById("page4");
let page0 = document.getElementById("page0");

function showPage0() {
  page0.classList.remove("invisible");
  page1.classList.add("invisible");
  page2.classList.add("invisible");
  page3.classList.add("invisible");
  page4.classList.add("invisible");
}

function showPage2() {
  page2.classList.remove("invisible");

  page1.classList.add("invisible");
  page3.classList.add("invisible");
  page4.classList.add("invisible");
  page0.classList.add("invisible");
}

function showPage1() {
  page1.classList.remove("invisible");

  page2.classList.add("invisible");
  page3.classList.add("invisible");
  page4.classList.add("invisible");
  page0.classList.add("invisible");
}

function showPage3() {
  page3.classList.remove("invisible");

  page1.classList.add("invisible");
  page2.classList.add("invisible");
  page4.classList.add("invisible");

  page0.classList.add("invisible");
}

function showPage4() {
  page4.classList.remove("invisible");

  page1.classList.add("invisible");
  page2.classList.add("invisible");
  page3.classList.add("invisible");
  page0.classList.add("invisible");
}

// ==================START js used mainly for css manipulations========================
//table container that holds maximum outputs
let tablesContainer = document.querySelector(".tablesContainer");
//cards Container that is having cards
let cardContainer = document.querySelector(".cardContainer");
//filter container that is having all filters
let filterContainer = document.querySelector(".filterContainer");
//Vehicle Container that is having the filter and card for vehicle as well as the specific vehicle info
let vehicleContainer = document.querySelector(".vehicleContainer");
//Form container that is used to have all kind of forms
let formContainer = document.querySelector(".formContainer");
//Profile container that is used to display the profile
let profileContainer = document.querySelector(".profileContainer");
//Review Section that will contain the top reviews given by users;
let reviewSection = document.querySelector(".reviewSection");
//Review Row that will have all the dynamic reviews;
let reviewRow = document.querySelector(".reviewRow");
//Newly added car section in home page that contains the cars that are newly added or modified;
let newlyAddedCarRow = document.querySelector(".newlyAddedCarRow");
//Newly added car section in home page that contains the cars that are newly added or modified;
let newlyAddedBikeRow = document.querySelector(".newlyAddedBikeRow");
// add hovered class to selected list item
let list = document.querySelectorAll(".listOfMenus .listItem");
list.forEach((item) => item.addEventListener("click", activeLink));

//Function that is used to highlight the active link in nav bar links
function activeLink() {
  list.forEach((item) => {
    item.classList.remove("active");
  });
  this.classList.add("active");
}

// toggling the navbar i.e. showing and hiding the navbar based on clicking on menu icon
let toggle = document.querySelector(".toggle");
let navigation = document.querySelector(".navigation");
let main = document.querySelector(".main");

toggle.onclick = function () {
  navigation.classList.toggle("active");
  main.classList.toggle("active");
};

// ==================END js used mainly for css manipulations========================

window.onload = async function () {
  //Showing the homepage to the user
  showPage0();

  //Fetching the bookings , reviews given by the user as well as the vehicles that can be booked
  await Promise.all([
    fetchingBookings(),
    fetchingReviews(),
    fetchingVehicles(),
  ]);

  //Since we are first showing the homepage thus fetching and printing the top reveiws in home page
  let topReviews = await fetchingTopReviews();
  topReviews = topReviews.filter((review) => review.feedback != "");
  printingReviewsOnCard(topReviews.slice(-5));

  // NewlyAdded section on homepage that will contain the vehicles that are recently newlyAddedBikeRow..

  //Initially fetching only recently added cars
  let newlyAddedCarVehicles = getNewlyAddedVehicles("CAR");

  //printing only the first four
  printingCardsForVehicle(
    newlyAddedCarVehicles.slice(0, Math.min(allVehicles.length, 4)),
    newlyAddedCarRow
  );

  //fetching only recently added bikes here and printing them on home page
  let newlyAddedBikeVehicles = getNewlyAddedVehicles("BIKE");
  //printing only the first four
  printingCardsForVehicle(
    newlyAddedBikeVehicles.slice(0, Math.min(allVehicles.length, 4)),
    newlyAddedBikeRow
  );
};

// ----------------getting newly added vehicles --------------
function getNewlyAddedVehicles(type) {
  let newlyAddedVehicles = allVehicles
    .filter((vehicle) => {
      return vehicle.type == type;
    })
    .sort((a, b) => b.vehicle_id - a.vehicle_id);

  return newlyAddedVehicles;
}

// ======================================NAVBAR functions start ===============
function printingOnClickVehicleNav() {
  //Showing the page which contains the vehicles
  showPage1();
  //Printing the filters once only then directly showing the page later, enhancing the productivity.
  if (!haveFilter) {
    printingFilters();
    haveFilter = true;
  }
  printingCardsForVehicle(allVehicles);
  //console("called in vehicle nav");
}

function printingOnClickBookingNav() {
  //Since the page 2 needs tablesContainer and vehicleContainers we are first making them empty to write our new content.
  showPage2();

  //Tables Container is used to show the bookings and reviews whether in specific vehicle page or the pages containing history.
  tablesContainer.innerHTML = "";
  //While the vehicle container refers to dom element that will contain and display the details of specific vehicle only.
  vehicleContainer.innerHTML = "";

  //Since the user clicked on the bookings tab to see his history of bookings, we only print the tablesContainer containing bookings history.
  printingBookingsDataInTable(allBookings, true);
}

function printingOnClickReviewNav() {
  //Since the page 2 needs tablesContainer and vehicleContainers we are first making them empty to write our new content.
  showPage2();
  //Tables Container is used to show the bookings and reviews whether in specific vehicle page or the pages containing history.
  tablesContainer.innerHTML = "";
  //While the vehicle container refers to dom element that will contain and display the details of specific vehicle only.
  vehicleContainer.innerHTML = "";

  //Since the user clicked on the review tab to see his history of reviews, we only print the tablesContainer containing reviews history.
  printingReviewsDataInTable(allReviews);
}

function printingOnClickProfileNav() {
  // page 4 is only used to display the user profile and to update it
  showPage4();
  printingProfile(user);
}

function printingOnClickHomeNav() {
  //Since our home page is made while the window loaded thus we simply hiding everything and showing it
  showPage0();
}

// ====================== Start fetching entities========================================

//Get the bookingData
async function fetchingBookings() {
  try {
    let userEmail = user.email;

    let response = await fetch(
      `http://localhost:8080/booking/getByEmail?email=${userEmail}`,
      {
        method: "GET",
      }
    );

    if (response.ok) {
      let data = await response.json();
      //Inorder to make it more user friendly, we reversed the list coming from backend thus we will be having recent bookings first.
      allBookings = data.reverse();
      //Calculating total bookings.
      totalBookings = allBookings.length;
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
    let response = await fetch(
      `http://localhost:8080/review/getReviewsByUser?email=${userEmail}`,
      {
        method: "GET",
      }
    );
    if (response.ok) {
      let data = await response.json();
      //Inorder to make it more user friendly, we reversed the list coming from backend thus we will be having recent reviews first.
      allReviews = data.reverse();
      totalReviews = allReviews.length;
    }
  } catch (error) {
    console.log(error);
  }
}

//Fetching all available vehicles based on type, startdate and end date from backend
async function fetchingVehiclesAvailableWithType(
  start = startDate,
  end = endDate,
  type = "BIKE"
) {
  try {
    //If the start date coming here is different we will modify our start date to hold the current updated value to use it later while the user clicks on book now to show the bookings dates as searched by the user previously again enhancing user experience.
    startDate = start;
    endDate = end;
    let response = await fetch(
      `http://localhost:8080/vehicle/findingAvailable/${type}?startDate=${start}&endDate=${end}`,
      {
        method: "GET",
      }
    );
    //console("called the fetching vehicles");
    if (response.ok) {
      let data = await response.json();

      return data;
    }

    return null;
  } catch (error) {
    console.log(error);
    return null;
  }
}

//Fetching all available vehicles based on start date and end date from backend
async function fetchingVehiclesAvailable(start = startDate, end = endDate) {
  try {
    startDate = start;
    endDate = end;

    let response = await fetch(
      `http://localhost:8080/vehicle/findingAvailable?startDate=${start}&endDate=${end}`,
      {
        method: "GET",
      }
    );

    if (response.ok) {
      let data = await response.json();
      // Since the backend is already eleminating the under maintenance vehicle, we dont need to eliminate it.
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
      method: "GET",
    });
    if (response.ok) {
      let data = await response.json();

      //removing the vehicles that are under maintenance
      data = data.filter(
        (vehicle) => vehicle.availability != "UNDER_MAINTENANCE"
      );

      allVehicles = data;
      totalVehicles = allVehicles.length;
    }
  } catch (error) {
    console.log(error);
  }
}

// -----fetching bookings using registration number from backend for specific vehicle
async function fetchingBookingsByVehicle(registration_number) {
  try {
    let response = await fetch(
      `http://localhost:8080/booking/getByRegistrationNumber?registration_number=${registration_number}`,
      {
        method: "GET",
      }
    );
    if (response.ok) {
      let data = await response.json();

      return data;
    }
    else{
    let errorMsg = await response.text();
    alert(errorMsg);
    }
  } catch (error) {
    alert("something went wrong in frontEnd while fetching bookings");
    console.log(error);
    return null;
  }
}

// -----fetching reviews using vehicle registration number from backend for specific vehicle
async function fetchingReviewsByVehicle(registration_number) {
  try {
    let response = await fetch(
      `http://localhost:8080/review/getReviewsByVehicle?registration_number=${registration_number}`,
      {
        method: "GET",
      }
    );
    if (response.ok) {
      let data = await response.json();

      return data;
    }
    else{
        let errorMsg = await response.text();
        alert(errorMsg);
    }
  } catch (error) {
    alert("something went wrong in frontEnd while fetching reviews");
    console.log(error);
    return null;
  }
}

//Fetching top reviews from backend used in home page.
async function fetchingTopReviews() {
  try {
    let response = await fetch(`http://localhost:8080/review/getTopReviews`, {
      method: "GET",
    });
    if (response.ok) {
      let data = await response.json();
      return data;
    }
  } catch (error) {
    console.log(error);
  }
}

// ==========================Storing in DB starts  =================================

//Storing a new booking in the db. this function is used to book a vehicle.
async function storingBookingInDB(
  email,
  registration_number,
  starting,
  ending
) {
  try {
    let response = await fetch(
      `http://localhost:8080/booking/add?email=${email}&registration_number=${registration_number}&startDate=${starting}&endDate=${ending}`,
      {
        method: "POST",
      }
    );

    if (response.ok) {
      let data = await response.text();
      alert(data);
    } else {
      let msg = await response.text();
      alert("Failed to book bcz ' " + msg + " ' ");
      console.log("error msg", response.status);
      console.log("error msg", response.statusText);
    }
  } catch (error) {
    console.error(error);
  }
}

async function storingReviewInDB(email, registration_number, rating, feedback) {
  try {
    let response = await fetch(
      `http://localhost:8080/review/add?email=${email}&registration_number=${registration_number}&rating=${rating}&feedback=${feedback}`,
      {
        method: "POST",
      }
    );

    if (response.ok) {
      let review = await response.json();
      alert("Review Added successfully... ");

      allReviews.push(review);
      printingReviewsDataInTable(allReviews);
    } else {
        let errorMsg = await response.text();
      alert(errorMsg);
      console.log("error msg", errorMsg);
    }
  } catch (error) {
    console.error(error);
  }
}

// ================================Updating the DB=================
async function updatingUserInDB(userId, updatedUser) {
  try {
    let response = await fetch(`http://localhost:8080/auth/update/${userId}`, {
      method: "PUT",
      headers: {
        "Content-type": "application/json",
      },
      body: JSON.stringify(updatedUser),
    });

    if (response.ok) {
      let result = await response.json();

      //Checking if the admin updating his own details
      if (userId == user.userId) {
        localStorage.setItem("user", JSON.stringify(result));
        user = result;
      }

      alert("User Update Succesfully");
    } else {
    let errorMsg = await response.text();
      console.error("Error updating user:",errorMsg);
      alert(errorMsg);
    }
  } catch (error) {
    console.error(error);
  }
}

// ---------fun to cancel the booking-------------
async function updatingBookingStatusInDB(booking) {
  try {
    let password = prompt("Enter your password please : ");
    let response = await fetch(
      `http://localhost:8080/booking/cancelBooking/${password}`,
      {
        method: "PUT",
        headers: {
          "Content-type": "application/json",
        },
        body : JSON.stringify(booking)
      }
    );

    if (response.ok) {
    let successMsg = await response.text();

      alert(successMsg);
      let index = allBookings.findIndex(
        (elem) => elem == booking
      );
      if (index != -1) {
        allBookings[index].booking_status = "CANCELED";
        printingBookingsDataInTable(allBookings,true);
      }
    } else {
    let errorMsg = await response.text();
      alert(errorMsg);
      console.log(errorMsg);
    }
  } catch (error) {
    console.error("Error Msg : ", error);
  }
}

// ===============================Printing starts===========================================

function printingBookingsDataInTable(
  bookingsParam,
  isCancelBtnRequired = false,
  eraseBefore = true,
  headingParam = "Bookings History"
) {
  // // Clear existing content before adding new data
  if (eraseBefore) {
    tablesContainer.innerHTML = "";
  }

  let heading = document.createElement("h2");
  heading.innerText = headingParam;

  //table booking is being implemented here

  let bookingTable = document.createElement("table");
  bookingTable.classList.add("entityTable");

  let thead = document.createElement("thead");
  thead.innerHTML = `
        <tr id='bookingtableColumns'>
            <th>ID</th>
            <th>User Email</th>
            <th>Vehicle Name</th>
            <th>Starting Date</th>
            <th>Ending Date</th>
            <th>Total Price</th>
            <th>Status</th>
        </tr>
    `;

  // used to check whether the Cancle name column heading is created later or not . And if not created but required will create it once else no more repetative cancle heading generated if required more than one time .
  let IsCancelHeadingCreated = false;

  let tbody = document.createElement("tbody");

  if (!bookingsParam || bookingsParam.length === 0) {
    console.log("No bookings available.");
    let noContentFound = document.createElement("h2");
    noContentFound.innerText = "No Bookings Found";
    tablesContainer.appendChild(noContentFound);
    return;
  }

  bookingsParam.forEach((element) => {
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
    totalPrice.innerText = element.totalPrice.toFixed(0);

    let statusBox = document.createElement("td");
    statusBox.classList.add("statusBox");
    let status = document.createElement("span");
    status.innerText = element.booking_status;
    if (element.booking_status == "CONFIRMED") status.classList.add("success");
    else status.classList.add("danger");

    statusBox.appendChild(status);

    let row = document.createElement("tr");

    // -----------providing cancel btn if needed as per function param isCancelRequired-------------

    if (isCancelBtnRequired) {
      //checking if the user is same one who booked it...
      //Always creating a td for occupying the space for better looking
      let cancelCell = document.createElement("td");
      if (
        element.user.email == user.email &&
        element.booking_status == "CONFIRMED" &&
        element.startDate > today
      ) {
        let cancelBtn = document.createElement("button");
        cancelBtn.classList.add("material-symbols-outlined");
        cancelBtn.classList.add("cancelBtn");
        cancelBtn.innerText = "cancel";
        cancelBtn.title = "Cancel Booking";

        cancelBtn.addEventListener("click", async () => {
          if (confirm("Are you sure you want to cancel the booking ? ")){
            await updatingBookingStatusInDB(element);
          }
        });

        cancelCell.appendChild(cancelBtn);

        if (!IsCancelHeadingCreated) {
          thead.innerHTML = `
                    <tr id='bookingtableColumns'>
                        <th>ID</th>
                        <th>User Email</th>
                        <th>Vehicle Name</th>
                        <th>Starting Date</th>
                        <th>Ending Date</th>
                        <th>Total Price</th>
                        <th>Status</th>
                        <th>Cancel</th>
                        </tr>
                        `;
          IsCancelHeadingCreated = true;
        }
      }

      row.append(
        bookId,
        userEmail,
        vehicleName,
        startDate,
        endDate,
        totalPrice,
        statusBox
      );
      row.appendChild(cancelCell); //appending cancle cell btn
      tbody.appendChild(row);
    } else {
      //Making the booking row with cancel status not to appear in the booking table in specific product page. thus if the isCanclebtn is false it means we are showing the booking table in the specific vehicle page thus we will check first whether the status is confirm or not , if it is not confirm we will not show that

      if (element.booking_status == "CONFIRMED") {
        row.append(
          bookId,
          userEmail,
          vehicleName,
          startDate,
          endDate,
          totalPrice,
          statusBox
        );
        tbody.appendChild(row);
      }
    }
  });

  bookingTable.appendChild(thead);
  bookingTable.appendChild(tbody);

  tablesContainer.appendChild(heading);
  tablesContainer.appendChild(bookingTable);
}

//printing the table of reviews
function printingReviewsDataInTable(reviewsParam, eraseBefore = true) {
  // Clear existing content before adding new data
  if (eraseBefore) {
    tablesContainer.innerHTML = "";
    vehicleContainer.innerHTML = "";
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

  if (reviewsParam == null || reviewsParam.length == 0) {
    let noDataFound = document.createElement("h2");
    noDataFound.innerText = "No Review Found";
    tablesContainer.appendChild(noDataFound);
    return;
  }

  reviewsParam.forEach((element) => {
    let reviewId = document.createElement("td");
    reviewId.innerText = element.reviewId;
    let reviewDate = document.createElement("td");
    reviewDate.innerText = element.reviewTime.split("T")[0];
    let userMail = document.createElement("td");
    userMail.innerText = element.user.email;
    let vehicle = document.createElement("td");
    vehicle.innerText = element.vehicle.name;
    let rating = document.createElement("td");
    rating.innerText = element.rating;
    let feedback = document.createElement("td");
    feedback.classList.add("feedbackInsideTable");
    feedback.innerText = element.feedback;

    let newRow = document.createElement("tr");
    newRow.append(reviewId, reviewDate, userMail, vehicle, rating, feedback);

    tbody.appendChild(newRow);
  });

  reviewTable.appendChild(thead);
  reviewTable.appendChild(tbody);

  tablesContainer.appendChild(heading);
  tablesContainer.appendChild(reviewTable);
}

//printing the cards for all vehicles
function printingCardsForVehicle(
  vehiclesParam,
  containerToPrint = cardContainer
) {
  containerToPrint.innerHTML = "";

  if (vehiclesParam.length == 0) {
    console.log("No Data Found");
  } else {
    vehiclesParam.forEach((vehicle) => {
      let mainVehicleCard = document.createElement("div");
      mainVehicleCard.classList.add("mainVehicleCard");

      let image = document.createElement("img");
      image.src = `${vehicle.vehicleImage}`;
      image.classList.add("vehicleImage");

      let detailsContainer = document.createElement("div");
      detailsContainer.classList.add("detailsContainer");

      // ---------------name block----------------------

      let nameBlock = document.createElement("div");
      nameBlock.classList.add("nameBlock");

      let name = document.createElement("h3");
      name.innerText = vehicle.name;

      let model = document.createElement("h5");
      model.innerText = vehicle.model;

      nameBlock.append(name, model);

      // -------------icons block----------
      let iconBlock = document.createElement("div");
      iconBlock.classList.add("iconBlock");

      let subVehicles = [
        { attribute: vehicle.fuelType, icon: "local_gas_station" },
        { attribute: vehicle.type, icon: "commute" },
        { attribute: vehicle.seatingCapacity, icon: "event_seat" },
        { attribute: vehicle.mileage, icon: "oil_barrel" },
        { attribute: vehicle.color, icon: "palette" },
      ];

      subVehicles.forEach((subVehicle) => {
        let iconInnerBox = document.createElement("div");
        iconInnerBox.classList.add("iconInnerBox");

        let icon = document.createElement("span");
        icon.classList.add("material-symbols-outlined");
        icon.innerText = subVehicle.icon;

        let iconValue = document.createElement("div");
        iconValue.classList.add("iconValue");
        iconValue.innerText = subVehicle.attribute;

        iconInnerBox.append(icon, iconValue);

        iconBlock.appendChild(iconInnerBox);
      });

      // ----------------price------------------

      let lowerBox = document.createElement("div");
      lowerBox.classList.add("lowerBox");

      let pricePerDay = document.createElement("h3");
      pricePerDay.classList.add("pricePerDay");
      pricePerDay.innerText = vehicle.price_per_day.toFixed(0) + "/-Rs";

      let selectBtn = document.createElement("div");
      selectBtn.innerText = "BOOK NOW";
      selectBtn.classList.add("selectBtn");
      selectBtn.addEventListener("click", () => {
        scheduleBookingForVehicle(vehicle);
      });

      lowerBox.append(pricePerDay, selectBtn);
      // -----------------------appending all----------------

      detailsContainer.append(nameBlock, iconBlock, lowerBox);
      mainVehicleCard.append(image, detailsContainer);

      mainVehicleCard.addEventListener("click", async () => {
        showPage2();
        tablesContainer.innerHTML = ""; //removing whatever is inside the table container before.
        printingCompleteVehicleDetails(vehicle);
      });

      containerToPrint.appendChild(mainVehicleCard);
    });
  }
}

//this is used to display the complete vehicle details i.e. specific vehicle page containing reveiws and already booked details too.
function printingCompleteVehicleDetails(vehicle) {
  vehicleContainer.innerHTML = "";

  let mainDetailsContainer = document.createElement("div");
  mainDetailsContainer.classList.add("mainDetailsContainer");

  let sideImg = document.createElement("img");
  sideImg.classList.add("vehicleImg");
  sideImg.src = vehicle.vehicleImage;

  // --------------------side details container ------------------
  let sideDetails = document.createElement("vehicleInfo");
  sideDetails.classList.add("sideDetails");

  // -----------name block----------------
  let nameBlock = document.createElement("div");

  let name = document.createElement("div");
  name.classList.add("vehicleName");
  name.innerText = vehicle.name;

  let model = document.createElement("div");
  model.classList.add("vehicleModel");
  model.innerText = vehicle.model;

  nameBlock.append(name, model);

  // --------------icons details block-------------------

  let iconBlock = document.createElement("div");
  iconBlock.classList.add("vehicleIconBlock");

  let subVehicles = [
    { attribute: vehicle.fuelType, icon: "local_gas_station" },
    { attribute: vehicle.type, icon: "commute" },
    { attribute: vehicle.seatingCapacity, icon: "event_seat" },
    { attribute: vehicle.mileage, icon: "oil_barrel" },
    { attribute: vehicle.color, icon: "palette" },
  ];

  subVehicles.forEach((subVehicle) => {
    let iconInnerBox = document.createElement("div");
    iconInnerBox.classList.add("iconInnerBox");

    let icon = document.createElement("span");
    icon.classList.add("material-symbols-outlined");
    icon.innerText = subVehicle.icon;

    let iconValue = document.createElement("div");
    iconValue.classList.add("iconValue");
    iconValue.innerText = subVehicle.attribute;

    iconInnerBox.append(icon, iconValue);

    iconBlock.appendChild(iconInnerBox);
  });

  // ----------price per day --------------

  let price_per_day = document.createElement("div");
  price_per_day.innerHTML =
    vehicle.price_per_day.toFixed(0) + "/-Rs" + "<span> per day</span>";
  price_per_day.classList.add("vehiclePrice");

  // --------------------Adding booking and review btn --------------
  let btnContainer = document.createElement("div");
  btnContainer.classList.add("btnContainer");

  // ------------Book Btn--------------------
  let bookingBtn = document.createElement("button");
  bookingBtn.classList.add("bookingBtn");
  bookingBtn.addEventListener("click", () => {
    showPage3();
    scheduleBookingForVehicle(vehicle);
  });
  bookingBtn.innerText = "Book Now";

  let addReviewBtn = document.createElement("button");
  addReviewBtn.innerHTML = "Add Review";
  addReviewBtn.classList.add("reviewBtn");
  addReviewBtn.addEventListener("click", () => {
    showPage3();
    addingReviewForVehicle(vehicle);
  });

  btnContainer.append(addReviewBtn, bookingBtn);
  // ----------------appending All --------------------
  sideDetails.append(nameBlock, iconBlock, price_per_day, btnContainer);
  mainDetailsContainer.append(sideImg, sideDetails);

  vehicleContainer.appendChild(mainDetailsContainer);

  // ---showing associated reviews------------

  printingReviewsBasedOnVehicle(vehicle);

  printingBookingsBasedOnVehicle(vehicle);

  // -----------showing associated bookings----------
}

//used in printingCompleteVehicleDetails for printing the reviews for that specific vehicle
async function printingReviewsBasedOnVehicle(vehicle) {
  let reviews = await fetchingReviewsByVehicle(vehicle.registration_number);

  if (reviews != null) printingReviewsDataInTable(reviews.reverse(), false);
}

//used in printingCompleteVehicleDetails for printing the bookings for that specific vehicle
//The bookings will only be shown that are for upcoming days Only or the one that has not ended yet.
async function printingBookingsBasedOnVehicle(vehicle) {
  let bookings = await fetchingBookingsByVehicle(vehicle.registration_number);
  bookings = bookings.filter((booking) => {
    return booking.endDate >= today;
  });

  if (bookings != null) {
    printingBookingsDataInTable(
      bookings.reverse(),
      false,
      false,   //Passing false here bcz we dont want to remove the review table generated for the same vehicle.
      "Already Booked for"
    );
  }
}

function printingReviewsOnCard(reviewsParam) {
  reviewsParam.forEach((review) => {
    let cardReview = document.createElement("div");
    cardReview.classList.add("cardReview");
    // -------ratings block-----------
    let rating = review.rating;

    let starsBlock = document.createElement("div");
    starsBlock.classList.add("starsBlock");

    for (let i = 0; i < rating; i++) {
      let star = document.createElement("span");
      star.classList.add("ratingStars");
      star.classList.add("material-symbols-rounded");
      star.innerText = "star";

      starsBlock.appendChild(star);
    }

    // --------feedback having----------------
    let feedback = document.createElement("div");
    feedback.classList.add("feedback");
    feedback.innerText = review.feedback;

    // -------user name that has given review-----------
    let reviewerName = document.createElement("div");
    reviewerName.classList.add("reviewerName");
    reviewerName.innerText = "~" + review.user.name;

    // ------appeding all in the card---------------
    cardReview.append(starsBlock, feedback, reviewerName);

    reviewRow.appendChild(cardReview);
  });
}

// -------------------Printing Profile-----------------------
function printingProfile(element) {
  let userId = element.userId;
  let name = element.name;
  let email = element.email;
  let contactNumber = element.contactNumber;
  let role = element.role;

  //Always remember our first field will be the id only.
  let fields = [
    { label: "User ID", value: userId, id: "userId", type: "number" },
    { label: "Name", value: name, id: "name", type: "text" },
    { label: "Email", value: email, id: "email", type: "email" },
    {
      label: "Mobile No",
      value: contactNumber,
      id: "contactNumber",
      type: "number",
    },
    { label: "Role", value: role, id: "role", type: "text" },
  ];

  printingFormLayout("User Details", fields, "USER");
}

// ------------layout that actually prints the profile--------------
function printingFormLayout(headingContent, fieldsComing, entityType) {
  showPage4();
  profileContainer.innerHTML = "";

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

  fields.forEach((specificField) => {
    let inputContainer = document.createElement("div");
    inputContainer.classList.add("inputContainer");

    let label = document.createElement("label");
    label.for = specificField.label;
    label.innerText = specificField.label;

    let input = document.createElement("input");

    input.id = specificField.id;
    input.name = specificField.label;
    input.value = specificField.value;
    input.readOnly = true;

    input.type = specificField.type;

    inputContainer.append(label, input);

    userForm.appendChild(inputContainer);
  });

  //==================Edit btn starts===========
  let editBtn = document.createElement("button");
  editBtn.classList.add("editBtn");

  editBtn.innerText = "Update Details";

  editBtn.addEventListener("click", async () => {
    if (!validateForm(fields)) {
      return; // Stop if form is invalid
    } else {
      if (editBtn.innerText == "Save Changes") {   //It means we have to save the changes made by user.
        let id = document.getElementById(fields[0].id).value;

        if (isNaN(id)) {
          console.log("Invalid id or not a number");
          console.log(id);
          return;
        }

        if (entityType.toUpperCase() == "USER") {
          let updatedUser = {
            name: document.getElementById("name").value,
            email: document.getElementById("email").value,
            contactNumber: String(
              document.getElementById("contactNumber").value
            ),
            role: "USER",
          };

          await updatingUserInDB(id, updatedUser);
        }
      }

      let inputs = document.querySelectorAll(".userForm .inputContainer input");

      //Making The fields toggling b/w readonly and vice versa.
      inputs.forEach((ele) => {
        //userId and role cannot be updated
        if (ele.id != "userId" && ele.id != "role") {
          ele.readOnly = !ele.readOnly;
          ele.classList.toggle("editable");
        }
      });

      editBtn.innerText = inputs[1].readOnly
        ? "Update Details"
        : "Save Changes";
    }
  });

  userFormContainer.appendChild(userForm);
  userFormContainer.appendChild(editBtn);
  profileContainer.appendChild(heading);
  profileContainer.appendChild(userFormContainer);
}

//=----------------------Putting filters in filter container--------

function printingFilters() {
  let fields = [
    { label: "Start Date", id: "startDate", type: "date" },
    { label: "End Date", id: "endDate", type: "date" },
    {
      label: "Type",
      id: "type",
      type: "select",
      options: ["ALL", "CAR", "BIKE", "TRUCK"],
    },
  ];

  fields.forEach((element) => {
    let inputContainer = document.createElement("div");
    inputContainer.classList.add("inputContainer");

    let label = document.createElement("label");
    label.innerText = element.label;
    label.htmlFor = element.label;

    if (element.type == "select") {
      let inputSelect = document.createElement("select");
      inputSelect.id = element.id;

      element.options.forEach((ele) => {
        let option = document.createElement("option");
        option.text = ele;
        option.value = ele;

        inputSelect.appendChild(option);
      });

      inputContainer.append(label, inputSelect);
    } else {
      let input = document.createElement("input");
      input.type = element.type;
      input.name = element.label;
      input.id = element.id;

      if (element.type == "date") {
        input.min = today;
      }

      inputContainer.append(label, input);
    }

    filterContainer.appendChild(inputContainer);
  });

  let filterBtn = document.createElement("div");
  filterBtn.innerText = "Apply filters";
  filterBtn.classList.add("filterBtn");
  filterBtn.addEventListener("click", async () => {
    let initial = document.getElementById("startDate").value;
    let ending = document.getElementById("endDate").value;
    if (initial > ending) {
      alert("Start Date must be smaller than or equal to End Date ");
      return;
    }
    let typeSelected = document.getElementById("type");

    let availableVehicles = null;

    if (initial && ending) {
      if (typeSelected.value == "ALL") {
        availableVehicles = await fetchingVehiclesAvailable(initial, ending);
      } else {
        availableVehicles = await fetchingVehiclesAvailableWithType(
          initial,
          ending,
          typeSelected.value
        );
      }
    } else {
      if (typeSelected.value != "ALL") {
        availableVehicles = searchingVehiclesForKeyword(typeSelected.value); //Using our search bar for reusability;
      } else {
        availableVehicles = allVehicles;
      }
    }

    //Available vehicles having already available vehicles thus no need to remove them from here.
    if (availableVehicles != null) {
      if (availableVehicles.length === 0) {
        alert("No Vehicle Found");
        return;
      }
      showPage1();
      printingCardsForVehicle(availableVehicles);
    }
  });

  filterContainer.appendChild(filterBtn);
}

// ===============================Printing filters container ENDS===========================================

// ---------------schedule Booking and book the vehicle section ------------------
function scheduleBookingForVehicle(vehicle) {
  formContainer.innerHTML = "";

  let fields = [
    {
      label: "User Name",
      value: user.name,
      readOnly: true,
      type: "text",
      id: "b_UserName",
    },
    {
      label: "Email",
      value: user.email,
      readOnly: true,
      type: "email",
      id: "b_UserEmail",
    },
    {
      label: "Contact No",
      value: user.contactNumber,
      readOnly: true,
      type: "number",
      id: "b_UserContactNumber",
    },
    {
      label: "Vehicle Name",
      value: vehicle.name,
      readOnly: true,
      type: "text",
      id: "b_VehicleName",
    },
    {
      label: "Model",
      value: vehicle.model,
      readOnly: true,
      type: "text",
      id: "b_VehicleModel",
    },
    {
      label: "Type",
      value: vehicle.type,
      readOnly: true,
      type: "text",
      id: "b_VehicleType",
    },
    {
      label: "Fuel Type",
      value: vehicle.fuelType,
      readOnly: true,
      type: "text",
      id: "b_VehicleFuelType",
    },
    {
      label: "Registration Number",
      value: vehicle.registration_number,
      readOnly: true,
      type: "text",
      id: "b_VehicleRegistrationNumber",
    },
    {
      label: "Availability Status",
      value: vehicle.availability,
      readOnly: true,
      type: "text",
      id: "b_VehicleAvailability",
    },
    {
      label: "Price/Day (In Rupees)",
      value: vehicle.price_per_day,
      readOnly: true,
      type: "text",
      id: "b_VehiclePricePerDay",
    },
    {
      label: "Start Date",
      value: startDate,
      readOnly: false,
      type: "date",
      id: "b_StartDate",
    },
    {
      label: "End Date",
      value: endDate,
      readOnly: false,
      type: "date",
      id: "b_EndDate",
    },
  ];

  let bookingForm = document.createElement("form");
  bookingForm.classList.add("bookingForm");

  let heading = document.createElement("h1");
  heading.classList.add("heading");
  heading.innerText = "Let's Book it now";

  formContainer.appendChild(heading);

  fields.forEach((field) => {
    let inputBox = document.createElement("div");
    inputBox.classList.add("inputBox");

    let label = document.createElement("label");
    label.innerText = field.label;

    let input = document.createElement("input");
    input.value = field.value;
    input.readOnly = field.readOnly;
    input.type = field.type;
    input.id = field.id;

    if (input.type == "date") {
      if (field.id == "b_StartDate") {
        input.min = startDate;
      } else if (field.id == "b_EndDate") {
        input.min = endDate;
      }

      input.onchange = () => {
        let initial = document.getElementById("b_StartDate");
        let final = document.getElementById("b_EndDate");
        let totalPriceBooking = document.querySelector(".totalPriceBooking");

        if (initial.value > final.value) {
          alert("Start Date must be smaller or equal to end date");
          return;
        }

        //Also changing global startDate and endDate
        startDate = initial.value;
        endDate = final.value;

        let finalPrice =
          vehicle.price_per_day * getDateDifference(initial.value, final.value);
        totalPriceBooking.innerText = finalPrice.toFixed(0);
      };
    }

    inputBox.append(label, input);
    bookingForm.appendChild(inputBox);
  });

  // ------------------creating a book btn and total Pricing tag---------------
  let lowerBlock = document.createElement("div");
  lowerBlock.classList.add("lowerBlock");

  // -----Calculating total Price---

  let totalPriceBlock = document.createElement("div");
  totalPriceBlock.classList.add("totalPriceBlock");

  let textTotal = document.createElement("h3");
  textTotal.innerText = "Total Price : ";

  let totalPrice = document.createElement("div");
  totalPrice.classList.add("totalPriceBooking");
  let differenceBetweenDates = 1;
  totalPrice.innerText =
    vehicle.price_per_day * getDateDifference(startDate, endDate);
  formContainer.appendChild(bookingForm);

  totalPriceBlock.append(textTotal, totalPrice);

  // ------Book it Btn to store directly into the database ---------------
  let bookbtn = document.createElement("button");
  bookbtn.classList.add("finalBookBtn");
  bookbtn.innerText = "Book Now";

  bookbtn.addEventListener("click", async () => {
    let userEmail = user.email;
    let registration_number = vehicle.registration_number;
    // let startDate = startDate;
    if (startDate > endDate) {
      alert("The Start Date must be smaller than or equal to the end date");
      return;
    }

    await storingBookingInDB(
      userEmail,
      registration_number,
      startDate,
      endDate
    );
  });

  // ---
  lowerBlock.append(totalPriceBlock, bookbtn);

  formContainer.appendChild(lowerBlock);
}

// ----------------------------adding the rating of user -----------------------------
function addingReviewForVehicle(vehicle) {
  formContainer.innerHTML = "";

  let fields = [
    {
      label: "User Name",
      value: user.name,
      readOnly: true,
      type: "text",
      id: "b_UserName",
    },
    {
      label: "Email",
      value: user.email,
      readOnly: true,
      type: "email",
      id: "b_UserEmail",
    },
    {
      label: "Vehicle Name",
      value: vehicle.name,
      readOnly: true,
      type: "text",
      id: "b_VehicleName",
    },
    {
      label: "Type",
      value: vehicle.type,
      readOnly: true,
      type: "text",
      id: "b_VehicleType",
    },
    {
      label: "Registration Number",
      value: vehicle.registration_number,
      readOnly: true,
      type: "text",
      id: "b_VehicleRegistrationNumber",
    },
    {
      label: "Rating",
      value: "",
      readOnly: false,
      type: "select",
      id: "b_Rating",
      options: [1, 2, 3, 4, 5],
    },
    {
      label: "Feedback",
      value: "",
      readOnly: false,
      type: "text",
      id: "b_Feedback",
    },
  ];

  let bookingForm = document.createElement("form");
  bookingForm.classList.add("bookingForm");

  let heading = document.createElement("h1");
  heading.classList.add("heading");
  heading.innerText = "Add your review";

  formContainer.appendChild(heading);

  fields.forEach((field) => {
    let inputBox = document.createElement("div");
    inputBox.classList.add("inputBox");

    let label = document.createElement("label");
    label.innerText = field.label;

    if (field.type == "select") {
      let selectInput = document.createElement("select");
      selectInput.id = field.id;
      field.options.forEach((op) => {
        let option = document.createElement("option");
        option.value = op;
        option.innerText = op;
        selectInput.appendChild(option);
      });
      inputBox.append(label, selectInput);
    } else {
      let input = document.createElement("input");
      input.value = field.value;
      input.readOnly = field.readOnly;
      input.type = field.type;
      input.id = field.id;
      inputBox.append(label, input);
    }

    bookingForm.appendChild(inputBox);
  });

  // ------------------creating a add btn---------------

  // ------Book it Btn to store directly into the database ---------------
  let addReveiwBtn = document.createElement("button");
  addReveiwBtn.classList.add("finalReviewBtn");
  addReveiwBtn.innerText = "Submit";

  addReveiwBtn.addEventListener("click", async () => {
    let userEmail = user.email;
    let registration_number = vehicle.registration_number;
    let ratingGiven = document.getElementById("b_Rating").value;
    let feedbackGiven = document.getElementById("b_Feedback").value;
    // let startDate = startDate;

    await storingReviewInDB(
      userEmail,
      registration_number,
      ratingGiven,
      feedbackGiven
    );

    showPage2();
    printingReviewsDataInTable(allReviews);
  });

  formContainer.append(bookingForm, addReveiwBtn);
}

// ========================================================Search Bar Implementation starts ==========================================
function searchBarClick() {
  let keyword = searchBar.value.toLowerCase();

  let targetVehicles = searchingVehiclesForKeyword(keyword);

  showPage1();
  printingCardsForVehicle(targetVehicles);
}

function searchingVehiclesForKeyword(keyword) {
  keyword = keyword.toLowerCase();

  let targetVehicles = [];

  allVehicles.forEach((vehicle) => {
    let name = vehicle.name.toLowerCase();
    let type = vehicle.type.toLowerCase();
    let fuelType = vehicle.fuelType.toLowerCase();
    let model = vehicle.model.toLowerCase();
    let price_per_day = vehicle.price_per_day;

    if (
      name.includes(keyword) ||
      type.includes(keyword) ||
      fuelType.includes(keyword) ||
      model.includes(keyword) ||
      (price_per_day < keyword && price_per_day > keyword - 500) //filtering based on price with a range to enhance the user experience
    ) {
      targetVehicles.push(vehicle);
    }
  });

  return targetVehicles;
}

//=============================================================implementing logout=====================================================

function logout() {
  localStorage.removeItem("user");
  window.location.href = "index.html";
}

// ==============================================================helpers=====================================================================

// ==================calculating dates differences =====================
function getDateDifference(startDateStr, endDateStr) {
  const start = new Date(startDateStr);
  const end = new Date(endDateStr);

  // Calculate the difference in milliseconds
  const diffMs = end - start;

  // Convert milliseconds to days
  const millisecondsPerDay = 1000 * 3600 * 24;
  const diffDays = diffMs / millisecondsPerDay;

  return diffDays;
}

// ----validating entries-------
function validateForm(fields) {
  let isValid = true;
  fields.forEach((field) => {
    let inputElement = document.getElementById(field.id);
    if (inputElement && inputElement.hasAttribute("required")) {
      if (!inputElement.value || inputElement.value.trim() === "") {
        alert(`Please fill in the required field: ${field.label}`);
        isValid = false;
        inputElement.focus();
        return false;
      }
    }

    if (field.id === "contactNumber" && inputElement.value.length !== 10) {
      alert("Mobile No must be exactly 10 digits!");
      isValid = false;
      return false;
    }

    if (inputElement && inputElement.type == "email") {
      if (!inputElement.checkValidity()) {
        alert(`Invalid value in ${inputElement.name}`);
        isValid = false;
        return false;
      }
    }
  });
  return isValid;
}
