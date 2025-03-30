let loginForm = document.getElementById("loginForm");
loginForm.addEventListener("submit", login);

async function login(event) {
  console.log("btn clicked");
  event.preventDefault();

  const email = document.getElementById("email").value;
  const password = document.getElementById("password").value;

  const response = await fetch(
    `http://localhost:8080/auth/login?email=${email}&password=${password}`,
    {
      method: "GET",
    }
  );

  if (response.ok) {
    const data = await response.json();
    console.log(data);

    if (data.role == "ADMIN") {
      localStorage.setItem("admin", JSON.stringify(data));
      window.location.href = "adminDashboard.html";
    } else {
      localStorage.setItem("user", JSON.stringify(data));
      window.location.href = "home.html";
    }
  } else {
    let isExisting = document.querySelector(".invalidMsg");
    if (!isExisting) {
      let invalidMsg = document.createElement("div");
      invalidMsg.innerText = "Invalid Username or Password";
      invalidMsg.classList.add("invalidMsg");

      loginForm.insertBefore(invalidMsg, loginForm.firstChild);
    } else {
      isExisting.innerHTML = "Invalid Username or Password";
    }
  }
}
