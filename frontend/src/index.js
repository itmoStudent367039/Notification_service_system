import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './js/App';
import CorrectInput from "./js/components/CorrectInput";
import './js/FormSwitch';
import './js/Initialization';
import ErrorMessage from "./js/components/ErrorMessage";
import {Router} from "./js/Router";
import CheckMailPage from "./js/pages/CheckMailPage";
import {UserAccountPage} from "./js/pages/UserAccountPage";

// export function createApp(){
//     const root = ReactDOM.createRoot(document.getElementById(""));
//     root.render(
//         <App/>
//     );
// }

export function createCorrectInput(id) {
    const root = ReactDOM.createRoot(document.getElementById(id));
    root.render(
        <CorrectInput/>
    );
}

export function createErrorMessage(id, message) {
    const root = ReactDOM.createRoot(document.getElementById(id));
    root.render(
        <ErrorMessage message={message}/>
    );
}

export function createCheckMailPage() {
    const root = ReactDOM.createRoot(document.getElementById("generalBlock"));
    root.render(
        <CheckMailPage/>
    );
}


const root = ReactDOM.createRoot(document.getElementById("container1000"));
root.render(
    <Router/>
);
