import {checkInputLogIn, checkInputSignUp} from "./InputValidator";
export async function sendRequestLogIn(body){
  fetch("/auth-api/login", {
    method: "POST",
    headers: {
      "Content-Type": "application/json;charset=utf-8",
    },
    body: JSON.stringify({
      password: body.password,
      email: body.email,
    }),
  }).then((response) => {
    console.log(`Login response from auth: ${response.status}`);
    console.log(response);
    if (!(response.ok)){
        checkInputLogIn(response.json());
    }else{
        window.open("checkMail.html");
    }
  });
}

export async function sendRequestSignUp(body){
  fetch("/auth-api/registration", {
      method: "POST",
      headers: {
          "Content-Type": "application/json;charset=utf-8",
      },
      body: JSON.stringify({
          username: body.username,
          password: body.password,
          email: body.email,
      }),
  }).then((response) => {
      console.log(`Registration response from auth: ${response.status}`);
      console.log(response);
      if (!(response.ok)){
          checkInputSignUp(response.json());
      }else{
          window.open("checkMail.html");
      }
  });
}

