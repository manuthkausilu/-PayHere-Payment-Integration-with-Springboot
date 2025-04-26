# 💳 PayHere Payment Integration Test Project

This project demonstrates a basic integration of the [PayHere](https://www.payhere.lk/) payment gateway using **Spring Boot (Java)** for the backend and **HTML, jQuery** for the frontend.

---

## 🛠️ Technologies Used

- Spring Boot
- Java
- jQuery
- HTML/CSS
- PayHere Sandbox
- Ngrok (for exposing `notify_url` to PayHere)

---

## 📂 Project Structure

payhere-test/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/example/payhere_test/
│       │       ├── controller/
│       │       │   └── PaymentController.java
│       │       ├── util/
│       │       │   └── PayHereHashUtil.java
│       │       └── PayhereTestApplication.java
│       └── resources/
│           ├── application.properties
│           └── static/
│               ├── index.html
│               ├── payment-success.html
│               └── payment-cancelled.html
├── README.md
└── pom.xml

---

## ⚙️ Configuration

Update your `src/main/resources/application.properties` with your **PayHere Sandbox merchant credentials**.

> 📝 **Note:** Replace the placeholders below (`YOUR_MERCHANT_ID`, `YOUR_MERCHANT_SECRET`) with your actual credentials. You can obtain these by creating a free [PayHere Sandbox account](https://sandbox.payhere.lk/).

```properties
# Application name
spring.application.name=payhere-test

# Server configuration
server.port=8080

# JPA configuration (for demonstration purposes, adjust as needed)
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# PayHere configuration
payhere.merchant.id=YOUR_MERCHANT_ID
payhere.merchant.secret=YOUR_MERCHANT_SECRET
payhere.app.mode=sandbox

# Logging (optional, for debugging)
logging.level.org.springframework=INFO
logging.level.com.example=DEBUG

🚀 How to Run

Follow these steps to get the project running:

1. Backend (Spring Boot)

Open your terminal and execute the following commands:

git clone [https://github.com/YOUR_USERNAME/payhere-test.git](https://github.com/YOUR_USERNAME/payhere-test.git)
cd payhere-test
./mvnw spring-boot:run
The Spring Boot backend will start and will be accessible at http://localhost:8080.

2. Frontend (Payment Form)
Simply open the index.html file located in the src/main/resources/static/ directory in your web browser. This file contains the HTML form that submits payment data to PayHere and uses AJAX to request the secure hash from your backend.

🌐 Exposing notify_url with Ngrok
PayHere requires a publicly accessible notify_url to send server-side payment confirmations. Since your local backend is not directly accessible from the internet, you can use Ngrok to create a secure tunnel.

Steps:

Download and Install Ngrok: Follow the instructions on the Ngrok website to download and install it on your system.

Run Ngrok: Open a new terminal window and execute the following command:

ngrok http 8080

Get Public URL: Ngrok will provide you with a temporary public HTTPS URL. For example:
Forwarding   [https://b675-175-157-41-80.ngrok-free.app](https://b675-175-157-41-80.ngrok-free.app) -> http://localhost:8080

Update notify_url in HTML: Open the src/main/resources/static/index.html file and update the value of the notify_url hidden input field with the HTTPS URL provided by Ngrok, ensuring you append the correct backend endpoint for the notify handler (likely /api/payment/notify):
<input type="hidden" name="notify_url" value="[https://b675-175-157-41-80.ngrok-free.app/api/payment/notify](https://b675-175-157-41-80.ngrok-free.app/api/payment/notify)">
⚠️ Important: This Ngrok URL changes every time you restart Ngrok. You will need to update the notify_url in your index.html accordingly for each testing session.

🔄 Payment Flow

Click Pay Now button.

Frontend sends POST request to /api/payment/hash.

Backend generates a secure hash and sends it back.

Form is auto-submitted to PayHere.

PayHere handles payment and responds with:

return_url (on success)

cancel_url (if cancelled)

notify_url (server-side confirmation)

✅ Result Pages
payment-success.html – This page is displayed to the user after a successful payment.
payment-cancelled.html – This page is displayed if the user cancels the payment process.

📒 Notes
Ensure that the payhere.app.mode property in your application.properties is set to sandbox for testing.
You can find test credit card information and other testing details in the PayHere Developer Documentation.

📜 License
This project is open-source and provided for learning and integration testing purposes. Feel free to use and modify it according to your needs.

🙌 Acknowledgements
The excellent documentation and resources provided by PayHere.
The invaluable service provided by Ngrok for local development and testing of webhooks.

End of README.md
