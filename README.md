# Digital Heroes Golf Charity Subscription Platform

A production-ready Spring Boot application designed to manage a charity-driven golf subscription service. This platform integrates golf score tracking with a subscription-based model to support charitable causes through lucky draws.

## 🚀 Technology Stack

- **Backend:** Java 17+, Spring Boot 3.x, Spring Security (RBAC)
- **Frontend:** Thymeleaf, CSS3, JavaScript
- **Database:** PostgreSQL (Supabase)
- **Payments:** Stripe API (Subscriptions & Webhooks)
- **Emails:** Brevo (formerly Sendinblue) SMTP
- **Deployment:** Docker & Render

---

## 🛠️ How It Works

The platform operates on a tiered access model where user capabilities are determined by their subscription status.

### 1. User Roles
- **PUBLIC_VISITOR:** Can register, login, and view public information but cannot enter scores or draws.
- **SUBSCRIBER:** Premium users who have an active Stripe subscription. They can enter golf scores and participate in charity draws.
- **ADMIN:** Full control over the system (User management, Draw creation, System logs).

### 2. Feature Modules
- **Subscription Engine:** Uses Stripe Checkout for secure payments and Stripe Webhooks to handle asynchronous events like successful payments, renewals, and cancellations.
- **Golf Score Tracker:** A "rolling" score system that keeps track of a user's 5 most recent golf entries.
- **Charity Lucky Draws:** A system for admins to publish draws and for subscribers to view winners and results.
- **Email System:** Automated transactional emails for registration, payment confirmations, and account updates.

---

## 🔄 Workflow

### **A. User Journey**
1. **Registration:** User creates an account and is assigned the `PUBLIC_VISITOR` role.
2. **Subscription:** The user chooses a plan (Monthly/Yearly) and is redirected to a **Stripe Checkout** page.
3. **Payment Hook:** Upon successful payment, Stripe sends a **webhook** (`customer.subscription.created`) to the application.
4. **Role Upgrade:** The application validates the webhook and automatically upgrades the user to the `SUBSCRIBER` role.
5. **Engagement:** The user can now enter scores and participate in active draws.

### **B. System Workflow (Background)**
1. **Renewal:** Stripe manages recurring billing.
2. **Access Control:** If a payment fails, Stripe sends a `customer.subscription.deleted` hook, and the application immediately downgrades the user back to `PUBLIC_VISITOR`.
3. **Score Management:** When a user enters a 6th score, the system automatically removes the oldest one to maintain a "Newest 5" rolling window.

---

## ⚙️ Setup & Deployment

### Environment Variables for Render
To run this project on Render, you must configure the following Environment Variables:

| Key | Value / Example |
| :--- | :--- |
| **`DB_URL`** | `jdbc:postgresql://[HOST]:6543/postgres?prepareThreshold=0` |
| **`DB_USER`** | `postgres.[YOUR_PROJECT_ID]` |
| **`DB_PASSWORD`** | `YourSupabasePassword` |
| **`STRIPE_SECRET_KEY`** | `sk_test_...` |
| **`STRIPE_WEBHOOK_SECRET`** | `whsec_...` |
| **`SPRING_MAIL_USERNAME`** | Your Brevo SMTP login |
| **`SPRING_MAIL_PASSWORD`** | Your Brevo SMTP password |
| **`BASE_URL`** | `https://your-app.onrender.com` |

### Local Development
1. Ensure you have Java 17 and Maven installed.
2. Configure your `application.yml` or set environment variables locally.
3. Run: `./mvnw spring-boot:run`

---

## 📜 License
This project is developed for the **Digital Heroes Golf Charity** initiative.
