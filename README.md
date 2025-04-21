# 📱 Mobile Quiz Application

A full-stack **Mobile Quiz App** that allows users to **register**, **login**, and take quizzes based on different **categories and topics**.  
Each category contains multiple topics, and users can choose a specific topic within a category to attempt a quiz.

This application is built using **Android (Kotlin)** for the frontend and **Spring Boot** for the backend.  
It features secure **JWT Authentication**, a clean **MVVM architecture**, and robust **REST APIs** to deliver a seamless and secure quiz-taking experience.

---

## 🚀 Features

### 📲 Mobile App (Frontend - Android)
- ✅ User Authentication (Login/Signup) using JWT token  
- ✅ Fetch quiz questions from backend using **Retrofit**  
- ✅ Clean **MVVM Architecture**  
- ✅ State-safe navigation using **Navigation Graph**  
- ✅ Dependency Injection using **Hilt**  
- ✅ Background operations using **Coroutines**  
- ✅ Token storage using **SharedPreferences**  
- ✅ Smooth UI and performance optimized for responsiveness  

### 🖥️ Backend (Spring Boot)
- ✅ RESTful APIs developed using **Spring Boot**  
- ✅ Secure authentication using **Spring Security + JWT**  
- ✅ API documentation with **Swagger**  
- ✅ **MySQL** database integration for persistent storage  
- ✅ Role-based access control (User/Admin)  

---

## 🧑‍💼 Admin Panel (Backend API)
- ✅ Admins can create **categories**  
- ✅ Add multiple **topics** under each category  
- ✅ Add **quiz questions** for specific topics  
- ✅ Manage (Create/Update/Delete) quizzes from the backend  
- ✅ APIs are protected and accessible only with **admin credentials**

> 🔐 **Admin must first:**
> 1. Create a **Category**  
> 2. Add **Topics** to the category  
> 3. Add **Questions** under each topic  

---

## 🧭 Quiz Flow

1. User logs in or signs up.  
2. User selects a **category** (e.g., Java, Android, DSA).  
3. From within the category, user selects a **topic** (e.g., OOPs, Multithreading).  
4. Quiz questions for that topic are fetched from the backend.  
5. Timer starts for the **whole quiz**, with options to answer and continue.  
6. At the end, the app shows:
   - ✅ Total Score  
   - ✅ Correct Answers  
   - ✅ Skipped Questions  
   - ✅ Total Time Taken  

---

## 🔐 Authentication Flow

- User credentials are authenticated on login.  
- **JWT token** is issued and stored locally using **SharedPreferences**.  
- All secure API requests include the token in the **Authorization** header.  
- **Spring Security** validates and authorizes API access on the backend.  

---

## 📄 API Documentation

Access Swagger UI for detailed API specs:  
[http://localhost:8080/swagger-ui/swagger-ui/index.html#/](http://localhost:8080/swagger-ui/swagger-ui/index.html#/)

---
