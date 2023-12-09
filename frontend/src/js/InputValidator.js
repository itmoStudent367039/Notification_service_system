import {createErrorMessage} from "../index";

export function checkInputSignUp(response) {
    response.then(result1 => {
        const password = result1.message.password;
        if (password) {
            createErrorMessage("appPasswordSignUp", password);
        }
        const email = result1.message.email;
        if (email){
            createErrorMessage("appMailSignUp", email);
        }
        const username = result1.message.username;
        if (username){
            createErrorMessage("appNameSignUp", username);
        }
        const error = result1.message.error;
        if (error){
            createErrorMessage("appPasswordSignUp", error);
        }
    });
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