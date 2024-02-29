export function switchFormForAccount(event) {
    const tabs = document.querySelectorAll('.tabT');
    tabs.forEach(tab => tab.classList.remove('active'));
    event.target.classList.add('active');
    const tabForms = document.querySelectorAll('.tab-formT');
    tabForms.forEach(function (tabForm, index) {
        tabForm.classList.toggle('active', index === Array.from(tabs).indexOf(event.target));
    });
}
