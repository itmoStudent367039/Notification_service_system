import React from 'react';

const NewWindowComponent = () => {
    const openNewWindow = () => {
        // Открыть новое окно при клике на кнопку
        window.open('/checkMail.html', '_blank');
    };

    return (
        <div>
            <button onClick={openNewWindow}>Открыть новое окно</button>
        </div>
    );
};

export default NewWindowComponent;
