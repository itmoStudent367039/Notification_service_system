import {createErrorMessage} from "../index";

export function checkInputSignUp(response) {
    console.log(response);
}

export function checkInputLogIn(response) {
    response.then(result => {
        const password = result.message.password;
        if (password) {
            createErrorMessage("appPasswordLogIn", password);
        }
        const email = result.message.email;
        if (email){
            createErrorMessage("appEmailLogIn", email);
        }
        const error = result.message.error;
        if (error){
            createErrorMessage("appPasswordLogIn", error);
        }
    });
}