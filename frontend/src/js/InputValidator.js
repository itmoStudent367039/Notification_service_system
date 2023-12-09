import {createCorrectInput, createErrorMessage} from "../index";

export function checkInputSignUp(response) {
    response.then(result1 => {
        const password = result1.message.password;
        if (password) {
            createErrorMessage("appPasswordSignUp", password);
        } else {
            createCorrectInput("appPasswordSignUp");
        }
        const email = result1.message.email;
        if (email) {
            createErrorMessage("appMailSignUp", email);
        } else {
            createCorrectInput("appMailSignUp");
        }
        const username = result1.message.username;
        if (username) {
            createErrorMessage("appNameSignUp", username);
        } else {
            createCorrectInput("appNameSignUp");
        }
        const error = result1.message.error;
        if (error) {
            createErrorMessage("appPasswordSignUp", error);
        }
    });
}

export function checkInputLogIn(response) {
    response.then(result => {
        const password = result.message.password;
        if (password) {
            createErrorMessage("appPasswordLogIn", password);
        } else {
            createCorrectInput("appPasswordLogIn")
        }
        const email = result.message.email;
        if (email) {
            createErrorMessage("appEmailLogIn", email);
        } else {
            createCorrectInput("appEmailLogIn")
        }
        const error = result.message.error;
        if (error) {
            createErrorMessage("appPasswordLogIn", error);
        }
    });
}