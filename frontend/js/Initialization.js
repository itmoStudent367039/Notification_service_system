const btn = document.querySelector("#signUpBtn");

const requestURL = "https://jsonplaceholder.typicode.com/users";

btn.addEventListener("click", getInputRegistration);

function getInputRegistration() {
  const username = document.querySelector("#username2").value;
  const email = document.querySelector("#mail").value;
  const password = document.querySelector("#password2").value;
  const body = {
    username: username,
    email: email,
    password: password,
  };
  sendRequest("POST", requestURL, body)
    .then((data) => console.log(data))
    .catch((err) => console.log(err));
}

// import React from "react";

// document
//   .getElementById("signUpBtn")
//   .addEventListener("click", sendRegistrationForm);

// async function sendRegistrationForm() {
//   const email = "";
//   const password = "";
//   const username = "";
//   const response = await fetch("http://localhost:8080/auth/registration", {
//     method: "POST",
//     headers: {
//       "Content-type": "application/json",
//     },
//     body: {
//       username: username,
//       password: password,
//       email: email,
//     },
//   });
//   if (response.ok) {
//   } else {
//     await response.json().then((data) => {
//       data.errors.forEach((error) => {
//         document.getElementById(`${error.name}Er`).innerHTML(error.data);
//       });
//     });
//   }
// }
