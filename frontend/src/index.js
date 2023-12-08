import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './js/components/App';
import './css/StyleLogIn.css';
import './js/FormSwitch';
import './js/Initialization';

export function createErrorMessage(id, message){
    const root = ReactDOM.createRoot(document.getElementById(id));
    root.render(
        <App message={message}/>
    );
}


