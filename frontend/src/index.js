import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
    <React.StrictMode>
        <App/>
    </React.StrictMode>
);

(async () => {
    // fetch("/auth-api/registration", {
    //     method: "POST",
    //     headers: {
    //         "Content-Type": "application/json;charset=utf-8",
    //     },
    //     body: JSON.stringify({
    //         username: "yestai",
    //         password: "123",
    //         email: "igor.abdullin.95@mail.ru",
    //     }),
    // }).then((response) => {
    //     console.log(`Registration response from auth: ${response.status}`);
    //     console.log(response);
    // });

    fetch("/auth-api/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/json;charset=utf-8",
        },
        body: JSON.stringify({
            password: "123",
            email: "igor.abdullin.95@mail.ru",
        }),
    }).then((response) => {
        console.log(`Login response from auth: ${response.status}`);
        console.log(response);
    });
})();

reportWebVitals();
