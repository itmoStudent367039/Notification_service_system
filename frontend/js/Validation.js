import React from "react";

const App = ({initButtonText}, {initClassText}) => {
    const [buttonText, setButtonText] = React.useState(initButtonText);
    const [classList, setClassList] = React.useState(initClassText);
    const onButtonClick = () => {
        setButtonText('hello, react');
        setClassList('greenBtn');
    };
    return (
        <div className="app">
            <button className={classList} onClick={onButtonClick}>{buttonText}</button>
        </div>
    );
}

document.querySelector('').addEventListener(sendRegistrationForm());

async function sendRegistrationForm() {
    const email = '';
    const password = '';
    const username = '';
    const response = await fetch('http://localhost:8080/auth/registration', {
        method: 'POST',
        headers: {
            'Content-type': 'application/json'
        },
        body: {
            username: username,
            password: password,
            email: email
        }
    });
    if (response.ok) {

    } else {
        await response.json().then(data => {
            data.errors.forEach(error => {
                document.getElementById(`${error.name}Er`).innerHTML(error.data);
            });
        })
    }
}

const container = document.getElementById('app');
const root = ReactDOM.createRoot(container);
root.render(<App initButtonText="" initClassText=""/>);