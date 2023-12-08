import {sendRequestSignUp} from "./DataSender.js";

import {sendRequestLogIn} from "./DataSender.js";

const btnLI = document.querySelector("#logInBtn");

const btnSU = document.querySelector("#signUpBtn");

btnLI.addEventListener("click", getInputLogIn);

btnSU.addEventListener("click", getInputRegistration);

function getInputRegistration() {
  const username2 = document.querySelector("#username2").value;
  const email = document.querySelector("#mail").value;
  const password2 = document.querySelector("#password2").value;
  const body2 = {
    username: username2,
    email: email,
    password: password2,
  };
  sendRequestSignUp(body2)
    .then((data) => console.log(data))
    .catch((err) => console.log(err));
}

function getInputLogIn(){
  const email1 = document.querySelector("#email1").value;
  const password1 = document.querySelector("#password1").value;
  const body1 = {
    email: email1,
    password: password1,
  };
  sendRequestLogIn(body1)
      .then((data) => console.log(data))
      .catch((err) => console.log(err));
}