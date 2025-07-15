
const form = document.getElementById("payment-form");
const timerDisplay = document.getElementById("timer");
const captchaDisplay = document.getElementById("captcha-display");
const refreshCaptchaBtn = document.getElementById("refresh-captcha");
const submitButton = document.getElementById("submit-button");

let countdownInterval;
const urlParams = new URLSearchParams(window.location.search);
const customerId = urlParams.get('customerId');

function disableForm() {
    clearInterval(countdownInterval);
    form.querySelectorAll('input, button').forEach(el => {
        el.disabled = true;
    });
}

function validateFormData(formData) {
    if (!formData.get('card-number')) {
        alert("card number can not be empty.");
        return false;
    }
    if (!formData.get('cvv') || formData.get('cvv').length < 3) {
        alert("Please enter a valid CVV.");
        return false;
    }
    if (!formData.get('expiry-date')) {
        alert("Please enter expiry date")
        return false;
    }
    if (!formData.get('password')) {
        alert('Please enter password')
        return false
    }
    if (!formData.get('captcha-input')) {
        alert("Please enter the CAPTCHA text.");
        return false;
    }
    return true;
}

function startTimer(expiryTime) {
    clearInterval(countdownInterval);
    countdownInterval = setInterval(() => {
        const duration = Math.round((new Date(expiryTime) - new Date()) / 1000);

        if (duration <= 0) {
            timerDisplay.textContent = "00:00";
            alert('Time is up! Please refresh and try again.');
            disableForm();
            return;
        }
        const minutes = Math.floor(duration / 60);
        const seconds = duration % 60;
        timerDisplay.textContent = `${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}`;
    }, 1000);
}

async function startPayment() {
    if (!customerId) {
        alert('Customer id is missing from the URL.');
        disableForm();
        return;
    }
    try {
        const response = await fetch(`/api/payment/start/${customerId}`, { method: 'POST' });
        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message);
        }
        const data = await response.json();
        captchaDisplay.textContent = data.captchaText;
        startTimer(data.expiresAt);
    } catch (error) {
        alert(`Error: ${error.message}`);
        disableForm();
    }
}

form.addEventListener("submit", async (event) => {
    event.preventDefault();
    const formData = new FormData(form);
    if (!validateFormData(formData)) {
        return;
    }

    submitButton.disabled = true;
    submitButton.textContent = 'Processing...';

    const requestPayload = {
        customerId: parseFloat(customerId),
        amount: parseFloat(formData.get('amount')),
        cardNumber: formData.get('card-number'),
        password: formData.get('password'),
        expiryDate: formData.get('expiry-date'),
        cvv: formData.get('cvv'),
        captchaInput: formData.get('captcha-input')
    };


    try {
        const response = await fetch('/api/payment/process', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(requestPayload)
        });

        const data = await response.json();
        if (!response.ok) {
            throw new Error(data.message || 'Payment failed.');
        }

        alert("Payment successful! Thank you.");
        disableForm();

    } catch (error) {
        alert(`An error occurred: ${error.message}`);
        submitButton.disabled = false;
        submitButton.textContent = 'Pay Now';
        startPayment();
    }
});

refreshCaptchaBtn.addEventListener('click', startPayment);

startPayment();
