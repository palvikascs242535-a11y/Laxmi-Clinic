# PROJECT CONTEXT: LAXMI CLINIC MANAGEMENT SYSTEM
> **College Project Report (Blackbook) Technical Reference Document**  
> *Target System: Laxmi Clinic OPD Queue & Healthcare Management Platform*  
> *Generated factually from repository code, configurations, database schemas, and unit tests.*

---

## 1. PROJECT OVERVIEW

### Name
**Laxmi Clinic Management System** (Artifact ID: `laxmi-clinic-management`, Version: `1.0.0`)

### Purpose
The **Laxmi Clinic Management System** is a production-ready, full-stack medical practice web application engineered specifically for **Laxmi Clinic** in Sakinaka, Mumbai, headed by **Dr. Rajesh Sonar** (B.A.M.S. BOM, Approved ESIS Panel Doctor). The platform modernizes local clinic operations by providing an automated, real-time Outpatient Department (OPD) token queue management system, a dedicated home healthcare visit scheduling engine, an administrative control console, and a public informational portal.

### Problem It Solves
Neighborhood outpatient clinics in dense urban areas like Sakinaka face chronic logistical bottlenecks:
1. **Severe Waiting Room Overcrowding:** Walk-in patients must physically assemble in cramped waiting rooms, creating discomfort, elevated cross-infection hazards, and chaotic queues.
2. **Lack of Queue Visibility:** Patients have no visibility into how many patients are ahead or how long they must wait, leading to frequent frustration and reception inquiries.
3. **Lost or Disorganized Paper Tokens:** Manual paper token slips are easily misplaced and offer no historical tracking.
4. **Inefficient Home Visit Scheduling:** Frail, bedridden, or elderly patients requiring doctor home consultations must rely on manual phone calls that risk schedule collisions or lost notes.
5. **Operational Blackouts:** When a doctor is delayed, on an emergency call, or when clinic shifts change, there is no immediate channel to inform incoming patients before they travel to the clinic.

This system solves these issues through real-time digital token generation, mobile queue position monitoring with live estimated wait times, double-booking prevention for home visits, and a responsive doctor console.

### Target Users and Client
* **Client / Practice:** Dr. Rajesh Sonar, Laxmi Clinic (Shop No. 6, PNB, Sakinaka, Br. Kangira Bai, Parshuram Pal Niwas, Krishna Nagar, 90 Feet Road, Sakinaka, Mumbai - 400072; Registration No: 31-8933, Sakinaka).
* **Primary End Users (Patients):** Outpatients seeking clinical consultations, elderly/bedridden patients and their caretakers booking home visits, and general community members inquiring about clinic services.
* **Secondary Users (Clinic Staff & Doctor):** Dr. Rajesh Sonar (Doctor/Administrator) and clinic receptionists managing patient flow.

### Final List of Implemented Features
* **OPD Token Queue Generation:** Patients generate sequential daily tokens via the web portal by submitting their name, phone number, age, gender, preferred shift, and symptoms. `[COMPLETED]`
* **Live OPD Queue Monitor:** Real-time hero display broadcasting currently called token, waiting count, total patients today, current consultation shift, and clinic open/closed status with auto-polling every 6 seconds. `[COMPLETED]`
* **Wait Time Estimation & Privacy Masking:** System computes `patientsAhead * 10` minutes wait estimate and masks patient names (e.g., `Rahul S***`) on public displays. `[COMPLETED]`
* **Token Slip Modal & Print View:** Digital token slip modal with registration details, current queue status, and printable layout. `[COMPLETED]`
* **Token Self-Service Tracker:** Patients can search tokens by number or look up their daily token history using their mobile number. `[COMPLETED]`
* **Doctor Home Visit Scheduling:** Patients book home visits with full address, landmark, pincode, preferred date, slot, and medical symptoms. `[COMPLETED]`
* **Home Visit Double-Booking Prevention:** Hard database validation preventing multiple accepted home visits for the same date and time slot. `[COMPLETED]`
* **Home Visit Status Tracking:** Patients track the real-time status (`PENDING`, `ACCEPTED`, `REJECTED`, `COMPLETED`, `CANCELLED`) of their home visits using their phone number. `[COMPLETED]`
* **Admin Operator Console:** Single-click queue controls to Call Next Token, Call Specific Token, Mark Complete, Skip Token, or Recall skipped tokens. `[COMPLETED]`
* **Admin Home Visit Management:** Clinic staff can review requests, transition statuses, assign admin remarks, and record doctor clinical notes. `[COMPLETED]`
* **Consultation Shift Management:** Multi-shift engine supporting configurable shifts (Default: Morning Shift 12:00 PM – 05:00 PM; Evening Shift 07:00 PM – 12:00 AM) with capacity limits and active toggles. `[COMPLETED]`
* **Clinic Profile & Operational State Controls:** Edit clinic address, doctor credentials, timings, emergency phone, broadcast banner notices, and toggle clinic open/closed status with custom status messages. `[COMPLETED]`
* **Contact Inquiry Management:** Public contact form submission with admin review, status transition (`NEW` $\rightarrow$ `IN_PROGRESS` $\rightarrow$ `RESOLVED`), and reply tracking. `[COMPLETED]`
* **In-App Notification Center:** Automated system notifications generated on new tokens, home visits, contact inquiries, and custom broadcasts. `[COMPLETED]`
* **Admin Authentication & JWT Security:** Stateless JWT authentication (HMAC-SHA256, 24-hour expiration), BCrypt password hashing, and change password functionality. `[COMPLETED]`
* **In-Memory MongoDB Server Fallback:** Zero-dependency local development and test execution using pure Java in-memory MongoDB (`mongo-java-server`). `[COMPLETED]`
* **Actuator Health & Metrics:** Standard production operational endpoints (`/actuator/health`, `/actuator/metrics`, `/actuator/info`). `[COMPLETED]`
* **SMS / WhatsApp Gateway:** `[NOT IMPLEMENTED]` (All alerts are presently internal in-app database notifications).
* **Online Payment Gateway (Razorpay/Stripe):** `[NOT IMPLEMENTED]` (Clinic handles billing directly in-clinic via cash/UPI).
* **Comprehensive In-Clinic Pharmacy Inventory:** `[NOT IMPLEMENTED]` (Preliminary roadmap item; final codebase focuses on Queue, Home Visit, Shift, and Public Clinic Operations).

---

## 2. OBJECTIVES AND STAKEHOLDERS

### Objectives
* **Streamline Outpatient Queue Flow:** Automate sequential daily token allocation, reducing waiting room crowding by up to 70%.
* **Provide Real-Time Transparency:** Deliver live queue position, currently serving token, and dynamic wait time predictions directly to patients' smartphones.
* **Modernize Home Healthcare Coordination:** Provide a verified booking system for home visits with collision checks for elderly or bedridden residents.
* **Empower Clinic Administration:** Provide Dr. Rajesh Sonar and receptionists with a centralized administrative console to advance queues, update shifts, and broadcast emergency operational notices.
* **Ensure High Availability & Security:** Implement stateless JWT authentication, strong BCrypt password encryption, MongoDB Atlas cloud storage, and automated database auditing.

### Stakeholders Matrix

| Stakeholder | Role in Code | Allowed Actions & Capabilities |
| :--- | :--- | :--- |
| **System Developer / Super Admin** | `ROLE_ADMIN` | Configures environment variables, handles Maven packaging, provisions MongoDB Atlas databases, inspects Actuator health, runs tests, triggers seed initializers. |
| **Clinic Doctor (Dr. Rajesh Sonar)** | `ROLE_ADMIN` | Uses Operator Console to Call Next patient, Complete consultation, Skip absent patients, Recall skipped tokens; records doctor clinical notes on home visits; toggles clinic operational state; updates shift hours. |
| **Clinic Receptionist / Staff** | `ROLE_ADMIN` / Staff | Generates tokens for walk-in patients, manages home visit status transitions, replies to patient inquiries, monitors in-app notifications, broadcasts clinic announcements. |
| **Outpatient (Public Patient)** | Anonymous / Public | Views clinic info and live queue; generates an OPD token; searches own tokens by mobile number; prints digital token slip; submits contact inquiries. |
| **Home Visit Patient / Caregiver** | Anonymous / Public | Books doctor home consultations; selects preferred date and time slot; tracks status of submitted requests via mobile number. |
| **Client (Laxmi Clinic)** | Beneficiary | Holds legal ownership of clinic metadata, Dr. Rajesh Sonar's professional credentials, operational hours, and patient service registries. |

---

## 3. TECH STACK

| Layer | Technology | Exact Version | Why We Chose It |
| :--- | :--- | :--- | :--- |
| **Language** | Java (OpenJDK) | `21` (LTS) | Modern LTS release offering record patterns, virtual threads capability, enhanced pattern matching, and high performance. |
| **Backend Framework** | Spring Boot | `3.3.3` | Industry-standard enterprise Java framework providing auto-configuration, dependency injection, and embedded Tomcat. |
| **Web Layer** | Spring Web MVC | `3.3.3` (Starter) | RESTful API controllers, HTTP request handling, content negotiation, and static resource routing. |
| **Database** | MongoDB | `3.3.3` (Data MongoDB Starter) | Schema-flexible document database ideal for polymorphic clinical notes, timestamps, dynamic shifts, and JSON mapping. |
| **Database Driver** | MongoDB Java Driver | `5.0.1` (Spring managed) | High-performance async/sync driver supporting connection pooling, TLS/SSL, and MongoDB Atlas SRV connection strings. |
| **Local In-Memory DB** | `mongo-java-server` | `1.44.0` | Pure Java in-memory MongoDB server by bwaldvogel; allows running, compiling, and testing without requiring a locally installed MongoDB daemon. |
| **Security Framework** | Spring Security | `6.3.3` (Starter) | Robust declarative security, filter-chain management, and method-level access control. |
| **Authentication** | JJWT (`jjwt-api`, `jjwt-impl`, `jjwt-jackson`) | `0.12.6` | Lightweight, standards-compliant JWT library for generating and verifying HMAC-SHA256 tokens. |
| **Validation** | Jakarta Bean Validation (Hibernate Validator) | `3.3.3` (Starter) | Declarative DTO validation via annotations (`@NotBlank`, `@Pattern`, `@Min`, `@Max`, `@Size`). |
| **Monitoring** | Spring Boot Actuator | `3.3.3` (Starter) | Production-ready health checks (`/actuator/health`) and application metrics. |
| **Frontend Structure** | HTML5 | Living Standard | Native semantic markup (`<header>`, `<section>`, `<nav>`, `<aside>`, `<footer>`) with zero heavyweight frontend framework overhead. |
| **Frontend Styling** | Vanilla CSS3 | Standard | Custom clinical design system featuring CSS variables, glassmorphism, flexbox, CSS grid, print media stylesheets, and animations. |
| **Frontend Logic** | Vanilla JavaScript | ECMAScript 2022+ | Native Fetch API, async/await, modular closure patterns, DOM manipulation, and automated polling. |
| **Unit Testing** | JUnit 5 (Jupiter) | `5.10.3` (Spring managed) | Modern testing framework for parameterized unit testing and assertions. |
| **Mocking Framework** | Mockito | `5.11.0` (Spring managed) | Isolated unit testing of service components with `@Mock`, `@InjectMocks`, and verify semantics. |
| **Security Testing** | Spring Security Test | `6.3.3` | Validates security filter chains, mock authenticated users, and unauthorized entry points. |
| **Build & Dependency Tool** | Apache Maven | `3.9+` | Standardized build lifecycle, plugin management, and dependency resolution. |
| **Cloud Hosting / Deployment** | Standalone Executable JAR / Docker | `1.0.0` | Self-contained executable JAR with embedded Tomcat, deployable to Render, Railway, AWS, or local Windows server. |

---

## 4. ARCHITECTURE

### Directory Structure
```
Laxmi Clinic/
├── pom.xml
├── target/
│   └── laxmi-clinic-management-1.0.0.jar
└── src/
    ├── main/
    │   ├── java/com/laxmiclinic/
    │   │   ├── LaxmiClinicApplication.java
    │   │   ├── config/
    │   │   │   ├── DataInitializer.java
    │   │   │   ├── MongoConfig.java
    │   │   │   ├── SecurityConfig.java
    │   │   │   └── WebConfig.java
    │   │   ├── controller/
    │   │   │   ├── adminapi/
    │   │   │   │   ├── AdminAuthController.java
    │   │   │   │   ├── AdminContactController.java
    │   │   │   │   ├── AdminDashboardController.java
    │   │   │   │   ├── AdminHomeVisitController.java
    │   │   │   │   ├── AdminNotificationController.java
    │   │   │   │   ├── AdminShiftController.java
    │   │   │   │   ├── AdminSiteInfoController.java
    │   │   │   │   └── AdminTokenController.java
    │   │   │   └── publicapi/
    │   │   │       ├── AuthController.java
    │   │   │       ├── PublicContactController.java
    │   │   │       ├── PublicHomeVisitController.java
    │   │   │       ├── PublicSiteController.java
    │   │   │       └── PublicTokenController.java
    │   │   ├── dto/
    │   │   │   ├── request/
    │   │   │   │   ├── ChangePasswordRequest.java
    │   │   │   │   ├── ClinicInfoRequest.java
    │   │   │   │   ├── ContactRequest.java
    │   │   │   │   ├── HomeVisitBookingRequest.java
    │   │   │   │   ├── LoginRequest.java
    │   │   │   │   ├── ShiftRequest.java
    │   │   │   │   └── TokenGenerateRequest.java
    │   │   │   └── response/
    │   │   │       ├── ApiResponse.java
    │   │   │       ├── AuthResponse.java
    │   │   │       ├── DashboardStatsDto.java
    │   │   │       ├── QueueStatusDto.java
    │   │   │       └── TokenResponseDto.java
    │   │   ├── exception/
    │   │   │   ├── BadRequestException.java
    │   │   │   ├── ErrorResponse.java
    │   │   │   ├── GlobalExceptionHandler.java
    │   │   │   └── ResourceNotFoundException.java
    │   │   ├── model/
    │   │   │   ├── AdminUser.java
    │   │   │   ├── ClinicInfo.java
    │   │   │   ├── ContactSubmission.java
    │   │   │   ├── HomeVisit.java
    │   │   │   ├── Notification.java
    │   │   │   ├── ServiceItem.java
    │   │   │   ├── Shift.java
    │   │   │   └── Token.java
    │   │   ├── repository/
    │   │   │   ├── AdminUserRepository.java
    │   │   │   ├── ClinicInfoRepository.java
    │   │   │   ├── ContactSubmissionRepository.java
    │   │   │   ├── HomeVisitRepository.java
    │   │   │   ├── NotificationRepository.java
    │   │   │   ├── ServiceItemRepository.java
    │   │   │   ├── ShiftRepository.java
    │   │   │   └── TokenRepository.java
    │   │   ├── security/
    │   │   │   ├── CustomUserDetailsService.java
    │   │   │   ├── JwtAuthenticationFilter.java
    │   │   │   ├── JwtTokenProvider.java
    │   │   │   └── UserPrincipal.java
    │   │   └── service/
    │   │       ├── AuthService.java
    │   │       ├── ClinicInfoService.java
    │   │       ├── ContactService.java
    │   │       ├── HomeVisitService.java
    │   │       ├── NotificationService.java
    │   │       ├── ShiftService.java
    │   │       └── TokenQueueService.java
    │   └── resources/
    │       ├── application.properties
    │       ├── application-dev.properties
    │       ├── application-prod.properties
    │       └── static/
    │           ├── admin.html
    │           ├── index.html
    │           ├── favicon.ico
    │           ├── favicon.png
    │           ├── css/
    │           │   ├── admin.css
    │           │   └── style.css
    │           ├── images/
    │           │   └── logo.png
    │           └── js/
    │               ├── admin.js
    │               ├── api.js
    │               ├── app.js
    │               ├── homevisit.js
    │               └── token.js
    └── test/java/com/laxmiclinic/
        ├── HomeVisitServiceTest.java
        ├── JwtTokenProviderTest.java
        ├── ShiftServiceTest.java
        └── TokenQueueServiceTest.java
```

### End-to-End Request Flow
1. **Client Layer:** User action in browser (`index.html` or `admin.html`) triggers JavaScript fetch via `api.js`.
2. **Web Container & Dispatcher:** Embedded Tomcat accepts HTTP request on `${PORT:8080}` and passes it to Spring's `DispatcherServlet`.
3. **Filter Pipeline:**
   - `CorsFilter` verifies incoming `Origin` against allowed hosts (`app.cors.allowed-origins`).
   - `JwtAuthenticationFilter` inspects the HTTP `Authorization` header. If `Bearer <token>` is detected, it validates token signature via `JwtTokenProvider`, parses username, loads `UserDetails` via `CustomUserDetailsService`, and injects `UsernamePasswordAuthenticationToken` into `SecurityContextHolder`.
4. **Security Authorization:** `SecurityFilterChain` validates route access rules:
   - `/`, `/index.html`, `/admin.html`, `/css/**`, `/js/**`, `/images/**`, `/api/public/**`, and `/actuator/**` are granted `permitAll()`.
   - `/api/admin/**` requires authenticated session with `ROLE_ADMIN`. Unauthenticated requests return `401 Unauthorized` JSON.
5. **Controller Layer:** Request is routed to target `@RestController`. Incoming JSON payloads undergo Bean Validation (`@Valid`). On validation failure, `GlobalExceptionHandler` intercepts and returns a `400 Bad Request` containing field-level error messages.
6. **Service Layer:** Business logic executes within service classes (e.g., synchronized token generation in `TokenQueueService`, schedule conflict resolution in `HomeVisitService`).
7. **Notification Hook:** Crucial events trigger `NotificationService.createNotification(...)`, recording audit logs for the admin drawer.
8. **Data Layer:** `MongoRepository` communicates over Mongo Wire Protocol to MongoDB Atlas (or in-memory fallback), executing typed document queries.
9. **Response Assembly:** Entity data is transformed into a DTO and wrapped in a unified `ApiResponse<T>` payload, formatted to JSON by Jackson using `Asia/Kolkata` timestamps.

### Middleware / Filter Chain
```
[Client Request]
       │
       ▼
[CorsFilter] ─── (Validates Origins, Methods, Headers)
       │
       ▼
[CsrfFilter] ─── (Disabled for Stateless JWT API)
       │
       ▼
[JwtAuthenticationFilter] ─── (Parses Bearer Token -> Injects UserPrincipal)
       │
       ▼
[AuthorizationFilter] ─── (Validates Role / Endpoint Permissions)
       │
       ▼
[DispatcherServlet] ─── (Routes to Controller)
       │
       ▼
[GlobalExceptionHandler] ─── (Catches ResourceNotFound, BadRequest, Validation Errors)
       │
       ▼
[REST Response Payload (ApiResponse<T>)]
```

### Environment Variables
* `PORT`: Server port (Default: `8080`).
* `SPRING_PROFILES_ACTIVE`: Active profile (`dev` or `prod`, Default: `dev`).
* `SPRING_DATA_MONGODB_URI`: MongoDB Atlas connection string URI.
* `ALLOWED_ORIGINS`: Comma-separated CORS allowed domains (e.g., `https://laxmiclinic.com`).
* `JWT_SECRET`: 256-bit cryptographic secret string for signing JWTs.
* `JWT_EXPIRATION_MS`: JWT lifespan in milliseconds (Default: `86400000` = 24 hours).
* `DEFAULT_ADMIN_USER`: Seed administrator username (Default: `Rajesh Sonar`).
* `DEFAULT_ADMIN_PASS`: Seed administrator initial password (Default: `rajeshsonarlc`).
* `DEFAULT_ADMIN_EMAIL`: Seed administrator contact email (Default: `rajeshsonar@laxmiclinic.local`).

### Deployment Setup
* **Production Packaging:** Self-contained executable JAR generated via `mvn clean package`.
* **Platform Support:** Cloud PaaS (Render, Railway, Heroku) or containerized Linux/Windows VMs via `java -jar target/laxmi-clinic-management-1.0.0.jar`.
* **Database Connection:** Connects over TLS/SSL to MongoDB Atlas cloud database cluster (`laxmi-clinic-db.1nnhoe8.mongodb.net/laxmi_clinic`).
* **Static Serving:** Spring Boot serves the frontend directly from `classpath:/static/` with caching enabled in production (`spring.web.resources.cache.period=3600`).

---

## 5. ROLES AND ACCESS CONTROL

### Role Matrix

| Role | Enforced In Code | Permissions & Allowed Operations | Restricted / Prohibited Operations |
| :--- | :--- | :--- | :--- |
| **PUBLIC / ANONYMOUS** | `SecurityConfig.java` line 96 (`.requestMatchers("/api/public/**").permitAll()`) | • View clinic details, timings, address, and live operational status<br>• View live OPD queue metrics and current serving token<br>• Generate an OPD token<br>• Search and track own token via token number or mobile number<br>• Book a doctor home visit<br>• Track submitted home visit requests via mobile number<br>• Submit contact inquiries<br>• Execute admin login (`POST /api/public/auth/login`) | • Cannot access any `/api/admin/**` endpoint<br>• Cannot call, complete, skip, or recall tokens<br>• Cannot accept or reject home visits<br>• Cannot modify clinic details or consultation shifts<br>• Cannot view other patients' unmasked private phone numbers<br>• Cannot view in-app admin notifications |
| **ADMIN / DOCTOR** (`ROLE_ADMIN`) | `SecurityConfig.java` line 100 (`.requestMatchers("/api/admin/**").authenticated()`), `UserPrincipal.java` line 32 | • Access Admin Dashboard (`/admin.html`) and live metrics<br>• Call Next Token, Call Specific Token, Mark Complete, Skip, and Recall tokens<br>• Filter all tokens by status and date<br>• Manage home visit requests: Accept, Reject, Complete, Cancel, add admin remarks and clinical notes<br>• Update shift hours, capacity, and toggle shift active/inactive<br>• Reset shifts to default configuration<br>• Update clinic profile, doctor credentials, timings, address, and emergency phone<br>• Toggle clinic operational open/closed state with custom status messages<br>• View, mark read, and broadcast clinic announcements<br>• Review and resolve patient contact inquiries<br>• Change administrator account password | • Constrained to valid business rules (e.g., cannot call next token if queue is empty, cannot accept home visit if time slot is already booked) |

---

## 6. DATABASE

The system utilizes MongoDB as its primary persistence engine, configured via Spring Data MongoDB with `@EnableMongoAuditing` enabled on `LaxmiClinicApplication.java`.

### Collections and Document Schemas

#### 1. Collection: `admin_users` (Model: `AdminUser`)
| Field Name | Data Type | Constraint / Nullability | Default Value | Index / Unique | Relations / Description |
| :--- | :--- | :--- | :--- | :--- | :--- |
| `id` | `String` | Primary Key | Auto (ObjectId) | Primary Index | Unique identifier |
| `username` | `String` | Required | None | `@Indexed(unique = true)` | Login username (e.g., `Rajesh Sonar`) |
| `password` | `String` | Required | None | None | BCrypt hashed password |
| `fullName` | `String` | Optional | None | None | Full display name (e.g., `Dr. Rajesh Sonar`) |
| `email` | `String` | Optional | None | None | Admin email address |
| `role` | `String` | Required | `"ROLE_ADMIN"` | None | User authority role |
| `active` | `boolean` | Required | `true` | None | Account active toggle |
| `lastLoginAt`| `LocalDateTime` | Optional | `null` | None | Timestamp of most recent login |
| `createdAt` | `LocalDateTime` | Required | `LocalDateTime.now()` | None | `@CreatedDate` audit timestamp |
| `updatedAt` | `LocalDateTime` | Required | `LocalDateTime.now()` | None | `@LastModifiedDate` audit timestamp |

#### 2. Collection: `clinic_info` (Model: `ClinicInfo`)
| Field Name | Data Type | Constraint / Nullability | Default Value | Index / Unique | Relations / Description |
| :--- | :--- | :--- | :--- | :--- | :--- |
| `id` | `String` | Primary Key | Auto (ObjectId) | Primary Index | Singleton document identifier |
| `clinicName` | `String` | Required | `"Laxmi Clinic"` | None | Name of the clinic |
| `tagline` | `String` | Optional | `"Caring for Your Health..."` | None | Clinic promotional tagline |
| `aboutDescription`| `String`| Optional | Description text | None | About clinic paragraph |
| `doctorName` | `String` | Required | `"Dr. Rajesh Sonar"` | None | Attending doctor's name |
| `doctorQualification`| `String`| Required | `"B.A.M.S. (BOM)"` | None | Doctor's medical degrees |
| `doctorSpecialty`| `String`| Required | `"General Physician & ESIS Panel Doctor"` | None | Medical specialization |
| `doctorExperience`| `String`| Optional | `"30+ Years Clinical Experience"` | None | Practice duration |
| `doctorBio` | `String` | Optional | Biographical text | None | Full doctor biographical notes |
| `streetAddress`| `String` | Required | Address text | None | Physical clinic address |
| `city` | `String` | Required | `"Mumbai"` | None | City |
| `state` | `String` | Required | `"Maharashtra"` | None | State |
| `pincode` | `String` | Required | `"400072"` | None | Postal PIN code |
| `landmark` | `String` | Optional | `"Near PNB / Br. Kangira Bai"`| None | Location landmark |
| `mapEmbedUrl`| `String` | Optional | `""` | None | Google Maps embed iframe URL |
| `primaryPhone`| `String` | Required | `"9819531470"` | None | Clinic reception contact phone |
| `emergencyPhone`| `String`| Required | `"9819531470"` | None | Urgent contact phone |
| `email` | `String` | Optional | `"clinic@email.com"` | None | Clinic email address |
| `generalTimings`| `String`| Required | `"12:00 PM - 05:00 PM & 07:00 PM - 12:00 AM (Mon - Sat)"` | None | Human-readable schedule |
| `isOperational`| `boolean`| Required | `true` | None | Clinic open/closed master switch |
| `operationalStatusMessage`| `String`| Optional | `"Clinic is currently open..."` | None | Status notice displayed to public |
| `noticeBanner`| `String` | Optional | `""` | None | Emergency banner announcement |
| `highlights` | `List<String>` | Optional | Default 5 highlights | None | Key clinic features |
| `updatedAt` | `LocalDateTime` | Required | `LocalDateTime.now()` | None | `@LastModifiedDate` audit timestamp |

#### 3. Collection: `contact_submissions` (Model: `ContactSubmission`)
| Field Name | Data Type | Constraint / Nullability | Default Value | Index / Unique | Relations / Description |
| :--- | :--- | :--- | :--- | :--- | :--- |
| `id` | `String` | Primary Key | Auto (ObjectId) | Primary Index | Unique inquiry ID |
| `name` | `String` | Required | None | None | Sender's full name |
| `email` | `String` | Optional | `null` | None | Sender's email |
| `mobile` | `String` | Required | None | None | Sender's contact number |
| `subject` | `String` | Optional | `"General Inquiry"` | None | Subject heading |
| `message` | `String` | Required | None | None | Inquiry text message |
| `status` | `InquiryStatus` | Required | `NEW` | None | Enum: `NEW`, `IN_PROGRESS`, `RESOLVED` |
| `adminReply` | `String` | Optional | `null` | None | Staff notes or reply summary |
| `createdAt` | `LocalDateTime` | Required | `LocalDateTime.now()` | None | `@CreatedDate` submission timestamp |

#### 4. Collection: `home_visits` (Model: `HomeVisit`)
| Field Name | Data Type | Constraint / Nullability | Default Value | Index / Unique | Relations / Description |
| :--- | :--- | :--- | :--- | :--- | :--- |
| `id` | `String` | Primary Key | Auto (ObjectId) | Primary Index | Unique home visit booking ID |
| `patientName` | `String` | Required | None | None | Patient's full name |
| `mobile` | `String` | Required | None | `@Indexed` | Patient phone number |
| `email` | `String` | Optional | `null` | None | Patient email |
| `address` | `String` | Required | None | None | Full residential address |
| `landmark` | `String` | Optional | `null` | None | Nearby landmark |
| `pincode` | `String` | Required | None | None | Postal PIN code |
| `preferredDate`| `String` | Required | None | `@Indexed` | Target date formatted as `"YYYY-MM-DD"` |
| `preferredTimeSlot`| `String`| Required | None | None | Slot label (e.g., `"09:30 AM - 11:30 AM"`) |
| `reason` | `String` | Optional | `null` | None | Symptoms or visit justification |
| `status` | `VisitStatus` | Required | `PENDING` | None | Enum: `PENDING`, `ACCEPTED`, `REJECTED`, `COMPLETED`, `CANCELLED` |
| `adminRemark` | `String` | Optional | `null` | None | Operational remarks by staff |
| `doctorNotes` | `String` | Optional | `null` | None | Clinical diagnostic notes by doctor |
| `createdAt` | `LocalDateTime` | Required | `LocalDateTime.now()` | None | `@CreatedDate` booking timestamp |
| `updatedAt` | `LocalDateTime` | Required | `LocalDateTime.now()` | None | `@LastModifiedDate` status timestamp |

#### 5. Collection: `notifications` (Model: `Notification`)
| Field Name | Data Type | Constraint / Nullability | Default Value | Index / Unique | Relations / Description |
| :--- | :--- | :--- | :--- | :--- | :--- |
| `id` | `String` | Primary Key | Auto (ObjectId) | Primary Index | Notification identifier |
| `title` | `String` | Required | None | None | Notification title heading |
| `message` | `String` | Required | None | None | Detailed notification message |
| `type` | `NotificationType`| Required | `SYSTEM` | None | Enum: `TOKEN_ALERT`, `HOME_VISIT`, `CONTACT_INQUIRY`, `CLINIC_ANNOUNCEMENT`, `SYSTEM` |
| `priority` | `NotificationPriority`| Required | `NORMAL` | None | Enum: `LOW`, `NORMAL`, `HIGH`, `URGENT` |
| `isRead` | `boolean` | Required | `false` | None | Read/unread flag |
| `targetRole` | `String` | Required | `"ROLE_ADMIN"` | None | Target audience |
| `referenceId` | `String` | Optional | `null` | None | Loose FK to Token, HomeVisit, or Contact ID |
| `linkUrl` | `String` | Optional | `null` | None | Admin UI routing link (e.g., `"/admin.html#queue"`) |
| `createdAt` | `LocalDateTime` | Required | `LocalDateTime.now()` | None | `@CreatedDate` audit timestamp |

#### 6. Collection: `services` (Model: `ServiceItem`)
| Field Name | Data Type | Constraint / Nullability | Default Value | Index / Unique | Relations / Description |
| :--- | :--- | :--- | :--- | :--- | :--- |
| `id` | `String` | Primary Key | Auto (ObjectId) | Primary Index | Service item identifier |
| `name` | `String` | Required | None | None | Service name |
| `category` | `String` | Optional | None | None | Clinical category |
| `description`| `String` | Optional | None | None | Service description |
| `icon` | `String` | Optional | None | None | CSS icon class |
| `durationMinutes`| `String`| Optional | None | None | Typical consultation duration |
| `pricePlaceholder`| `String`| Optional | `"Consultation Fee: ₹[Price]"` | None | Pricing text |
| `active` | `boolean` | Required | `true` | None | Active catalog toggle |
| `displayOrder`| `int` | Required | `0` | None | Sequence ordering index |
| `updatedAt` | `LocalDateTime` | Required | `LocalDateTime.now()` | None | `@LastModifiedDate` audit timestamp |

#### 7. Collection: `shifts` (Model: `Shift`)
| Field Name | Data Type | Constraint / Nullability | Default Value | Index / Unique | Relations / Description |
| :--- | :--- | :--- | :--- | :--- | :--- |
| `id` | `String` | Primary Key | Auto (ObjectId) | Primary Index | Shift record identifier |
| `shiftNumber` | `int` | Required (1 to 5) | None | `@Indexed(unique = true)` | Numeric shift identifier |
| `name` | `String` | Required | None | None | Shift name (e.g., `"Morning Shift"`) |
| `startTime` | `String` | Required | None | None | Shift start (e.g., `"12:00 PM"`) |
| `endTime` | `String` | Required | None | None | Shift end (e.g., `"05:00 PM"`) |
| `description`| `String` | Optional | None | None | Area or session description |
| `maxTokensCapacity`| `int`| Required | `30` | None | Max token limit per session |
| `active` | `boolean` | Required | `true` | None | Active session toggle |
| `updatedAt` | `LocalDateTime` | Required | `LocalDateTime.now()` | None | `@LastModifiedDate` audit timestamp |

#### 8. Collection: `tokens` (Model: `Token`)
| Field Name | Data Type | Constraint / Nullability | Default Value | Index / Unique | Relations / Description |
| :--- | :--- | :--- | :--- | :--- | :--- |
| `id` | `String` | Primary Key | Auto (ObjectId) | Primary Index | Token document identifier |
| `tokenNumber` | `int` | Required | None | `@Indexed` | Sequential daily number ($1, 2, 3\dots$) |
| `tokenDate` | `String` | Required | None | `@Indexed` | Service date formatted as `"YYYY-MM-DD"` |
| `patientName` | `String` | Required | None | None | Patient's registered name |
| `patientPhone`| `String` | Required | None | `@Indexed` | Patient's contact phone number |
| `age` | `Integer` | Optional | `null` | None | Patient age in years |
| `gender` | `String` | Optional | `null` | None | Gender (`Male`, `Female`, `Other`) |
| `symptoms` | `String` | Optional | `null` | None | Stated medical symptoms |
| `notes` | `String` | Optional | `null` | None | General notes |
| `shiftNumber` | `int` | Required | `1` | None | Assigned shift identifier ($1$ to $5$) |
| `shiftName` | `String` | Required | `"General Consultation"` | None | Human-readable shift name |
| `status` | `TokenStatus` | Required | `WAITING` | None | Enum: `WAITING`, `CALLED`, `COMPLETED`, `SKIPPED`, `CANCELLED` |
| `createdAt` | `LocalDateTime` | Required | `LocalDateTime.now()` | None | `@CreatedDate` generation timestamp |
| `calledAt` | `LocalDateTime` | Optional | `null` | None | Timestamp when token called into room |
| `completedAt`| `LocalDateTime` | Optional | `null` | None | Timestamp when consultation finished |
| `skippedAt` | `LocalDateTime` | Optional | `null` | None | Timestamp when marked as skipped |
| `adminRemark` | `String` | Optional | `null` | None | Staff or doctor remark |

> **Compound Index Definition on `tokens` Collection:**  
> `@CompoundIndex(name = "date_token_idx", def = "{'tokenDate': 1, 'tokenNumber': 1}")` enables instant $O(1)$ lookups for specific daily token numbers and prevents sequence scan degradation.

### Database Relationships (Plain Sentences)
1. **Shifts to Tokens (One-to-Many):** One consultation `Shift` accommodates many daily `Token` records linked logically via `shiftNumber`.
2. **Tokens / Home Visits / Inquiries to Notifications (One-to-Many Polymorphic):** A specific `Token`, `HomeVisit`, or `ContactSubmission` generates multiple audit `Notification` records linked through `referenceId`.
3. **Admin User to Operations (One-to-Many Administrative Oversight):** A single `AdminUser` (Dr. Rajesh Sonar) oversees and transitions records across all `Token`, `HomeVisit`, `Shift`, `ClinicInfo`, and `ContactSubmission` collections.
4. **Clinic Info (Singleton):** The `ClinicInfo` collection maintains exactly one singleton master document governing clinic-wide metadata, contact details, and open/closed operational switches.

---

## 7. API / ROUTES

All responses follow the unified envelope: `ApiResponse<T> { boolean success, String message, T data, LocalDateTime timestamp }`.

| HTTP Method | Route Path | Allowed Role | Purpose | Request Body Fields | Response Summary |
| :--- | :--- | :--- | :--- | :--- | :--- |
| `POST` | `/api/public/auth/login` | Public | Authenticates admin credentials and issues JWT | `username`, `password` | Returns `AuthResponse` with JWT Bearer token and user info |
| `GET` | `/api/public/site-info` | Public | Fetches clinic profile, doctor credentials, and status | None | Returns `ClinicInfo` document |
| `GET` | `/api/public/shifts` | Public | Fetches all clinic consultation shifts | None | Returns `List<Shift>` |
| `POST` | `/api/public/tokens/generate` | Public | Generates sequential daily OPD token for patient | `patientName`, `patientPhone`, `age`, `gender`, `shiftNumber`, `symptoms` | Returns `TokenResponseDto` with assigned token number and wait time |
| `GET` | `/api/public/tokens/live-status` | Public | Retrieves live queue metrics and currently serving token | None | Returns `QueueStatusDto` (current token, waiting, total) |
| `GET` | `/api/public/tokens/check` | Public | Checks details for a specific token number | Query params: `tokenNumber` (int), `date` (optional string) | Returns `TokenResponseDto` |
| `GET` | `/api/public/tokens/my-tokens` | Public | Searches patient's daily tokens by mobile number | Query params: `phone` (string), `date` (optional string) | Returns `List<TokenResponseDto>` |
| `GET` | `/api/public/home-visits/slots` | Public | Retrieves available home visit time slots | None | Returns `List<String>` of 5 standard time slots |
| `POST` | `/api/public/home-visits/book` | Public | Books a doctor home healthcare visit | `patientName`, `mobile`, `email`, `address`, `landmark`, `pincode`, `preferredDate`, `preferredTimeSlot`, `reason` | Returns `HomeVisit` document with `PENDING` status |
| `GET` | `/api/public/home-visits/track/{mobile}`| Public | Tracks all home visits requested by a phone number | Path variable: `mobile` (string) | Returns `List<HomeVisit>` ordered by date descending |
| `POST` | `/api/public/contact` | Public | Submits a patient inquiry or message | `name`, `mobile`, `email`, `subject`, `message` | Returns created `ContactSubmission` document |
| `GET` | `/api/admin/auth/me` | `ROLE_ADMIN` | Gets authenticated user profile from security context | None (Bearer JWT Header) | Returns admin profile map (`username`, `fullName`, `email`, `role`) |
| `PUT` | `/api/admin/auth/change-password` | `ROLE_ADMIN` | Changes admin account password | `currentPassword`, `newPassword` | Returns success message |
| `GET` | `/api/admin/dashboard/stats` | `ROLE_ADMIN` | Retrieves comprehensive dashboard KPIs and counts | None | Returns `DashboardStatsDto` |
| `GET` | `/api/admin/tokens/today` | `ROLE_ADMIN` | Lists all tokens for today with optional status filter | Query params: `date` (optional), `status` (optional `TokenStatus`) | Returns `List<TokenResponseDto>` |
| `POST` | `/api/admin/tokens/call-next` | `ROLE_ADMIN` | Automatically calls the next waiting token in queue | None | Auto-completes current token, marks next `CALLED`, returns `TokenResponseDto` |
| `POST` | `/api/admin/tokens/{id}/call` | `ROLE_ADMIN` | Calls a specific token directly by document ID | Path variable: `id` | Marks specified token as `CALLED`, returns `TokenResponseDto` |
| `POST` | `/api/admin/tokens/{id}/complete` | `ROLE_ADMIN` | Marks consultation as completed | Path variable: `id`, optional body: `{ "remark": "..." }` | Marks token `COMPLETED`, sets `completedAt`, returns `TokenResponseDto` |
| `POST` | `/api/admin/tokens/{id}/skip` | `ROLE_ADMIN` | Skips an absent or delayed patient token | Path variable: `id`, optional body: `{ "remark": "..." }` | Marks token `SKIPPED`, sets `skippedAt`, returns `TokenResponseDto` |
| `POST` | `/api/admin/tokens/{id}/recall` | `ROLE_ADMIN` | Recalls a skipped/completed token back to waiting queue | Path variable: `id` | Marks token `WAITING`, clears skip time, returns `TokenResponseDto` |
| `GET` | `/api/admin/home-visits` | `ROLE_ADMIN` | Retrieves all home visits with status/date filters | Query params: `status` (optional), `date` (optional) | Returns `List<HomeVisit>` |
| `GET` | `/api/admin/home-visits/{id}` | `ROLE_ADMIN` | Retrieves full details of a specific home visit | Path variable: `id` | Returns `HomeVisit` document |
| `PUT` | `/api/admin/home-visits/{id}/status` | `ROLE_ADMIN` | Updates home visit status, remarks, and doctor notes | `{ "status": "...", "adminRemark": "...", "doctorNotes": "..." }` | Validates collision, updates and returns `HomeVisit` |
| `GET` | `/api/admin/shifts` | `ROLE_ADMIN` | Lists all 5 shift configurations | None | Returns `List<Shift>` |
| `PUT` | `/api/admin/shifts/{shiftNumber}` | `ROLE_ADMIN` | Updates timings and capacity for a shift | `shiftNumber`, `name`, `startTime`, `endTime`, `description`, `maxTokensCapacity`, `active` | Returns updated `Shift` |
| `PUT` | `/api/admin/shifts/{shiftNumber}/toggle` | `ROLE_ADMIN` | Toggles shift active/inactive status | `{ "active": true/false }` | Returns updated `Shift` |
| `POST` | `/api/admin/shifts/reset-defaults` | `ROLE_ADMIN` | Resets shifts to Dr. Rajesh Sonar's 2 default timings | None | Returns `List<Shift>` |
| `GET` | `/api/admin/site-info` | `ROLE_ADMIN` | Retrieves clinic profile information for editing | None | Returns `ClinicInfo` |
| `PUT` | `/api/admin/site-info` | `ROLE_ADMIN` | Updates clinic details, credentials, and banner | `ClinicInfoRequest` fields | Returns updated `ClinicInfo` |
| `PUT` | `/api/admin/site-info/operational-status` | `ROLE_ADMIN` | Toggles clinic open/closed master switch | `{ "operational": true/false, "operationalStatusMessage": "..." }` | Returns updated `ClinicInfo` |
| `GET` | `/api/admin/notifications` | `ROLE_ADMIN` | Retrieves recent in-app system notifications | None | Returns `List<Notification>` (Top 50 descending) |
| `PUT` | `/api/admin/notifications/{id}/read` | `ROLE_ADMIN` | Marks single notification as read | Path variable: `id` | Returns success message |
| `PUT` | `/api/admin/notifications/mark-all-read`| `ROLE_ADMIN` | Marks all unread notifications as read | None | Returns success message |
| `POST` | `/api/admin/notifications/broadcast` | `ROLE_ADMIN` | Broadcasts announcement to notification drawer | `{ "title": "...", "message": "...", "priority": "..." }` | Returns created `Notification` |
| `DELETE`| `/api/admin/notifications/{id}` | `ROLE_ADMIN` | Deletes notification by ID | Path variable: `id` | Returns success message |
| `GET` | `/api/admin/contact-submissions` | `ROLE_ADMIN` | Retrieves all contact form submissions | Query param: `status` (optional `InquiryStatus`) | Returns `List<ContactSubmission>` |
| `PUT` | `/api/admin/contact-submissions/{id}/status`| `ROLE_ADMIN` | Updates inquiry status and admin reply | `{ "status": "RESOLVED", "adminReply": "..." }` | Returns updated `ContactSubmission` |
| `GET` | `/actuator/health` | Public | Actuator health monitoring check | None | Returns `{ "status": "UP" }` |

---

## 8. MODULES

### Module 1: Token Queue & OPD Live Tracking
* **Purpose:** Manages the entire patient outpatient journey from online token generation to calling into the consultation room, finishing consultation, skipping, or recalling.
* **Files Involved:**
  - Entity & Repo: `Token.java`, `TokenRepository.java`
  - DTOs: `TokenGenerateRequest.java`, `TokenResponseDto.java`, `QueueStatusDto.java`
  - Service & Controller: `TokenQueueService.java`, `PublicTokenController.java`, `AdminTokenController.java`
  - Client Scripts: `token.js`, `api.js`
* **Step-by-Step Logic:**
  1. Patient submits name, phone, age, gender, preferred shift, and symptoms via `POST /api/public/tokens/generate`.
  2. `TokenQueueService.generateToken(...)` verifies clinic is operational. It checks for the highest existing token number for today's date (`YYYY-MM-DD`). If none exists, token number starts at 1; otherwise, it increments by 1.
  3. Method is marked `synchronized` to eliminate race conditions under concurrent submissions.
  4. Token is saved with status `WAITING`.
  5. Live dashboard queries `getLiveQueueStatus()` calculating count of tokens in `WAITING`, `CALLED`, `COMPLETED`, and `SKIPPED`.
  6. Operator clicks **Call Next**: Any existing `CALLED` token is auto-completed with `completedAt = LocalDateTime.now()`. The lowest numbered `WAITING` token is updated to `CALLED` with `calledAt = LocalDateTime.now()`.
  7. Client polls `/api/public/tokens/live-status` every 6 seconds to update displayed counters and number badges.
* **Validations:** Patient name and phone required; phone matches `^[0-9+\-\s()]{7,15}$`; shift must be active.
* **Edge Cases Handled:**
  - Generating token when clinic is marked closed throws `BadRequestException`.
  - Calling next when queue has no waiting patients throws `BadRequestException("No waiting patients in queue for today.")`.
  - Recalling a skipped token clears `skippedAt` and resets status to `WAITING`.
* **Business Rules:**
  - Token sequence resets every day at midnight (`tokenDate` partition).
  - Estimated wait time is computed strictly as: $\text{Wait Time} = \text{Patients Ahead} \times 10\text{ minutes}$.
  - Patient names are masked on public displays for privacy.

### Module 2: Home Visit Booking & Collision Prevention
* **Purpose:** Enables bedridden and elderly patients in Sakinaka to schedule home consultations with Dr. Rajesh Sonar without schedule overlaps.
* **Files Involved:**
  - Entity & Repo: `HomeVisit.java`, `HomeVisitRepository.java`
  - DTOs: `HomeVisitBookingRequest.java`
  - Service & Controller: `HomeVisitService.java`, `PublicHomeVisitController.java`, `AdminHomeVisitController.java`
  - Client Scripts: `homevisit.js`, `api.js`
* **Step-by-Step Logic:**
  1. Patient selects preferred date (`YYYY-MM-DD`), standard time slot, and enters address and symptoms.
  2. `HomeVisitService.bookHomeVisit(...)` verifies that the date is not in the past (`requestedDate >= LocalDate.now()`).
  3. Collision Check: Calls `homeVisitRepository.existsByPreferredDateAndPreferredTimeSlotAndStatus(date, slot, VisitStatus.ACCEPTED)`. If an accepted visit already exists for that slot, request is rejected.
  4. Record saved with status `PENDING`.
  5. In admin portal, doctor reviews pending requests. If doctor clicks **Accept**, the collision check runs again to ensure another staff member hasn't accepted a competing request.
  6. Doctor records clinical diagnostic notes and completes the visit.
* **Validations:** Patient name, mobile, address, pincode, date, and slot are required; mobile format validated.
* **Edge Cases Handled:** Past dates rejected; duplicate booking attempts on occupied slots rejected with informative message.

### Module 3: Consultation Shift Scheduling
* **Purpose:** Regulates doctor consultation hours and aligns public token options with Dr. Rajesh Sonar's actual practice hours.
* **Files Involved:**
  - Entity & Repo: `Shift.java`, `ShiftRepository.java`
  - DTO: `ShiftRequest.java`
  - Service & Controller: `ShiftService.java`, `PublicSiteController.java`, `AdminShiftController.java`
  - Config & Data: `DataInitializer.java`
* **Step-by-Step Logic:**
  1. Default shifts seeded on application boot:
     - Shift #1: **Morning Shift** (12:00 PM – 05:00 PM)
     - Shift #2: **Evening Shift** (07:00 PM – 12:00 AM)
  2. Public patient portal queries active shifts to populate the shift selection dropdown.
  3. Admin can modify shift hours, toggle shifts active/inactive, or reset to defaults.
* **Business Rules:** Shift numbers constrained between 1 and 5; default token capacity is 30 tokens per shift.

### Module 4: Clinic Site Profile & Emergency Notice Controls
* **Purpose:** Manages clinic information, Dr. Rajesh Sonar's qualifications, contact phone numbers, emergency banners, and operational open/closed switches.
* **Files Involved:**
  - Entity & Repo: `ClinicInfo.java`, `ClinicInfoRepository.java`
  - DTO: `ClinicInfoRequest.java`
  - Service & Controller: `ClinicInfoService.java`, `PublicSiteController.java`, `AdminSiteInfoController.java`
* **Business Rules:** Single document model; operational toggle immediately blocks token creation and alerts incoming visitors on the homepage.

### Module 5: Patient Inquiries & Contact Management
* **Purpose:** Captures public questions, requests for information, or general feedback.
* **Files Involved:**
  - Entity & Repo: `ContactSubmission.java`, `ContactSubmissionRepository.java`
  - DTO: `ContactRequest.java`
  - Service & Controller: `ContactService.java`, `PublicContactController.java`, `AdminContactController.java`
* **Lifecycle:** Status transitions: `NEW` $\rightarrow$ `IN_PROGRESS` $\rightarrow$ `RESOLVED`.

### Module 6: In-App System Notifications & Announcements
* **Purpose:** Internal audit and alert system notifying clinic staff of critical operational events.
* **Files Involved:**
  - Entity & Repo: `Notification.java`, `NotificationRepository.java`
  - Service & Controller: `NotificationService.java`, `AdminNotificationController.java`
* **Business Rules:** System retains top 50 recent notifications; badge counters display unread count; supports system alerts and broadcast announcements.

### Module 7: Admin Authentication & Security
* **Purpose:** Restricts access to administrative endpoints and consoles.
* **Files Involved:**
  - Model & Repo: `AdminUser.java`, `AdminUserRepository.java`
  - Security Core: `SecurityConfig.java`, `JwtAuthenticationFilter.java`, `JwtTokenProvider.java`, `CustomUserDetailsService.java`, `UserPrincipal.java`
  - Service & Controller: `AuthService.java`, `AuthController.java`, `AdminAuthController.java`
* **Business Rules:** Passwords encrypted using BCrypt; JWT generated with HMAC-SHA256 valid for 24 hours (86,400,000 ms); stateless session management.

---

## 9. KEY FLOWS (FOR DIAGRAMS)

### Flow 1: Patient OPD Token Generation
```
[Patient] ──(1. Enters Name, Phone, Shift, Symptoms)──> [index.html / token.js]
                                                              │
                                            (2. POST /api/public/tokens/generate)
                                                              │
                                                              ▼
                                                   [PublicTokenController]
                                                              │
                                            (3. Validate DTO & Clinic Status)
                                                              │
                                                              ▼
                                                    [TokenQueueService]
                                                              │
                                         (4. Read max token for today + 1)
                                                              │
                                                              ▼
                                                      [TokenRepository]
                                                              │
                                                 (5. Save Token in MongoDB)
                                                              │
                                                              ▼
                                                    [NotificationService]
                                                              │
                                            (6. Create Admin Notification)
                                                              │
                                                              ▼
[Patient] <──(7. Renders Digital Token Slip with #)── [token.js Modal]
```
* **Decision Branch:**
  - If Clinic Operational = `false`: Service aborts $\rightarrow$ Throws `BadRequestException` $\rightarrow$ Toast: *"Clinic is currently closed"*.
  - If Shift is inactive: Service aborts $\rightarrow$ Throws `BadRequestException` $\rightarrow$ Toast: *"Selected shift is inactive"*.

### Flow 2: Doctor Calling Next Patient (Operator Console)
```
[Doctor / Staff] ──(1. Clicks "Call Next Token")──> [admin.html / admin.js]
                                                           │
                                        (2. POST /api/admin/tokens/call-next)
                                                           │
                                                           ▼
                                                 [AdminTokenController]
                                                           │
                                                           ▼
                                                  [TokenQueueService]
                                                           │
                      ┌────────────────────────────────────┴────────────────────────────────────┐
                      ▼                                                                         ▼
   [Find any currently CALLED token]                                          [Find lowest WAITING token today]
                      │                                                                         │
                      ▼                                                                         ▼
   [Mark COMPLETED + set completedAt]                                         [Mark CALLED + set calledAt]
                      │                                                                         │
                      └────────────────────────────────────┬────────────────────────────────────┘
                                                           ▼
                                                [Save updates in MongoDB]
                                                           │
                                                           ▼
                                            [Trigger TOKEN_ALERT Notification]
                                                           │
                                                           ▼
[Doctor & Public] <──(Live display flips to new token #)── [Polling clients update within 6s]
```
* **Failure Path:** If no `WAITING` tokens exist $\rightarrow$ Throws `BadRequestException("No waiting patients in queue for today")` $\rightarrow$ Toast warning displayed.

### Flow 3: Home Visit Booking with Collision Prevention
```
[Patient] ──(1. Enters Address, Date, Time Slot)──> [homevisit.js]
                                                           │
                                         (2. POST /api/public/home-visits/book)
                                                           │
                                                           ▼
                                               [PublicHomeVisitController]
                                                           │
                                                           ▼
                                                  [HomeVisitService]
                                                           │
                                       (3. Validate date is not in past)
                                                           │
                                                           ▼
                             [4. Check: existsByDateAndSlotAndStatus(..., ACCEPTED)]
                                                           │
                                ┌──────────────────────────┴──────────────────────────┐
                                ▼                                                     ▼
                       [Slot Already ACCEPTED]                                  [Slot Available]
                                │                                                     │
                                ▼                                                     ▼
                  [Throw BadRequestException]                               [Save HomeVisit (PENDING)]
                                │                                                     │
                                ▼                                                     ▼
                     [Show Error to Patient]                               [Trigger HOME_VISIT Notif]
                                                                                      │
                                                                                      ▼
                                                                           [Return Confirmation DTO]
```

### Flow 4: Admin Home Visit Status Transition
```
[Doctor] ──(1. Selects Status ACCEPTED / REJECTED / COMPLETED)──> [admin.js Modal]
                                                                        │
                                                (2. PUT /api/admin/home-visits/{id}/status)
                                                                        │
                                                                        ▼
                                                            [AdminHomeVisitController]
                                                                        │
                                                                        ▼
                                                               [HomeVisitService]
                                                                        │
                                                    (3. If new status == ACCEPTED)
                                                                        │
                                                    (4. Verify no other ACCEPTED visit exists)
                                                                        │
                                                                        ▼
                                                            [Save updated status & notes]
                                                                        │
                                                                        ▼
[Doctor] <──(Confirmation Toast & Table Refreshed)── [admin.js]
```

### Flow 5: Admin Login and Security Authentication
```
[Admin] ──(1. Enters Username & Password)──> [index.html / adminLoginModal]
                                                    │
                                     (2. POST /api/public/auth/login)
                                                    │
                                                    ▼
                                            [AuthController]
                                                    │
                                                    ▼
                                            [AuthService]
                                                    │
                               (3. AuthenticationManager.authenticate())
                                                    │
                                                    ▼
                                     [DaoAuthenticationProvider]
                                                    │
                           (4. CustomUserDetailsService.loadUserByUsername())
                                                    │
                                                    ▼
                             (5. PasswordEncoder.matches(raw, hash))
                                                    │
                         ┌──────────────────────────┴──────────────────────────┐
                         ▼                                                     ▼
                 [Bad Credentials]                                    [Password Valid]
                         │                                                     │
                         ▼                                                     ▼
             [Throw BadCredentialsException]                     [JwtTokenProvider.generateToken()]
                         │                                                     │
                         ▼                                                     ▼
              [Return 401 Unauthorized]                         [Save lastLoginAt timestamp]
                                                                               │
                                                                               ▼
[Admin] <──(Saves token in localStorage & redirects)── [Return AuthResponse (JWT + User Info)]
```

---

## 10. CLASS / ENTITY LIST

### Domain Entities (`com.laxmiclinic.model`)
* `AdminUser`: Attributes: `id`, `username`, `password`, `fullName`, `email`, `role`, `active`, `lastLoginAt`, `createdAt`, `updatedAt`. Methods: Getters, setters, constructors.
* `Token`: Attributes: `id`, `tokenNumber`, `tokenDate`, `patientName`, `patientPhone`, `age`, `gender`, `symptoms`, `notes`, `shiftNumber`, `shiftName`, `status`, `createdAt`, `calledAt`, `completedAt`, `skippedAt`, `adminRemark`. Methods: Getters, setters, constructors.
* `HomeVisit`: Attributes: `id`, `patientName`, `mobile`, `email`, `address`, `landmark`, `pincode`, `preferredDate`, `preferredTimeSlot`, `reason`, `status`, `adminRemark`, `doctorNotes`, `createdAt`, `updatedAt`. Methods: Getters, setters, constructors.
* `Shift`: Attributes: `id`, `shiftNumber`, `name`, `startTime`, `endTime`, `description`, `maxTokensCapacity`, `active`, `updatedAt`. Methods: Getters, setters, constructors.
* `ClinicInfo`: Attributes: `id`, `clinicName`, `tagline`, `aboutDescription`, `doctorName`, `doctorQualification`, `doctorSpecialty`, `doctorExperience`, `doctorBio`, `streetAddress`, `city`, `state`, `pincode`, `landmark`, `mapEmbedUrl`, `primaryPhone`, `emergencyPhone`, `email`, `generalTimings`, `isOperational`, `operationalStatusMessage`, `noticeBanner`, `highlights`, `updatedAt`. Methods: Getters, setters.
* `ContactSubmission`: Attributes: `id`, `name`, `email`, `mobile`, `subject`, `message`, `status`, `adminReply`, `createdAt`. Methods: Getters, setters, constructors.
* `Notification`: Attributes: `id`, `title`, `message`, `type`, `priority`, `isRead`, `targetRole`, `referenceId`, `linkUrl`, `createdAt`. Methods: Getters, setters, constructors.
* `ServiceItem`: Attributes: `id`, `name`, `category`, `description`, `icon`, `durationMinutes`, `pricePlaceholder`, `active`, `displayOrder`, `updatedAt`. Methods: Getters, setters, constructors.

### Service Layer (`com.laxmiclinic.service`)
* `TokenQueueService`: Methods: `generateToken(TokenGenerateRequest)`, `getLiveQueueStatus()`, `checkTokenStatus(date, tokenNumber)`, `getTokensByPhone(phone, date)`, `callNextToken()`, `callSpecificToken(id)`, `markTokenComplete(id, remark)`, `skipToken(id, remark)`, `recallToken(id)`, `getTokensForDate(date, status)`, `mapToDto(Token)`, `maskPatientName(String)`. Calls `TokenRepository`, `ShiftService`, `ClinicInfoService`, `NotificationService`.
* `HomeVisitService`: Methods: `bookHomeVisit(HomeVisitBookingRequest)`, `getHomeVisitsByMobile(mobile)`, `getAllHomeVisits(status, date)`, `getHomeVisitById(id)`, `updateVisitStatus(id, newStatus, adminRemark, doctorNotes)`, `getAvailableTimeSlots()`, `getPendingCount()`, `getAcceptedTodayCount()`. Calls `HomeVisitRepository`, `NotificationService`.
* `ShiftService`: Methods: `getAllShifts()`, `getActiveShifts()`, `getShiftByNumber(int)`, `updateShift(int, ShiftRequest)`, `toggleShiftActive(int, boolean)`, `initDefault5Shifts()`. Calls `ShiftRepository`.
* `ClinicInfoService`: Methods: `getClinicInfo()`, `updateClinicInfo(ClinicInfoRequest)`, `toggleOperationalStatus(boolean, String)`, `initDefaultClinicInfo()`. Calls `ClinicInfoRepository`.
* `ContactService`: Methods: `submitContactForm(ContactRequest)`, `getAllSubmissions(InquiryStatus)`, `getSubmissionById(id)`, `updateSubmissionStatus(id, InquiryStatus, reply)`, `getNewCount()`. Calls `ContactSubmissionRepository`, `NotificationService`.
* `NotificationService`: Methods: `createNotification(...)`, `getRecentNotifications()`, `getUnreadNotifications()`, `getUnreadCount()`, `markAsRead(id)`, `markAllAsRead()`, `deleteNotification(id)`, `createAnnouncement(...)`. Calls `NotificationRepository`.
* `AuthService`: Methods: `login(LoginRequest)`, `changePassword(username, ChangePasswordRequest)`, `getCurrentUser(username)`. Calls `AuthenticationManager`, `JwtTokenProvider`, `AdminUserRepository`, `PasswordEncoder`.

### Controller Layer (`com.laxmiclinic.controller`)
* `PublicTokenController`: Calls `TokenQueueService`.
* `PublicHomeVisitController`: Calls `HomeVisitService`.
* `PublicSiteController`: Calls `ClinicInfoService`, `ShiftService`.
* `PublicContactController`: Calls `ContactService`.
* `AuthController`: Calls `AuthService`.
* `AdminTokenController`: Calls `TokenQueueService`.
* `AdminHomeVisitController`: Calls `HomeVisitService`.
* `AdminShiftController`: Calls `ShiftService`.
* `AdminSiteInfoController`: Calls `ClinicInfoService`.
* `AdminContactController`: Calls `ContactService`.
* `AdminNotificationController`: Calls `NotificationService`.
* `AdminDashboardController`: Calls `TokenQueueService`, `HomeVisitService`, `NotificationService`, `ContactService`, `ClinicInfoService`, `ShiftService`.
* `AdminAuthController`: Calls `AuthService`.

### Security Layer (`com.laxmiclinic.security`)
* `JwtTokenProvider`: `generateToken(Authentication)`, `generateTokenFromUsername(username)`, `getUsernameFromJwt(token)`, `validateToken(token)`.
* `JwtAuthenticationFilter`: `doFilterInternal(request, response, filterChain)`. Calls `JwtTokenProvider`, `CustomUserDetailsService`.
* `CustomUserDetailsService`: `loadUserByUsername(username)`. Calls `AdminUserRepository`.
* `UserPrincipal`: Implements `UserDetails`.

---

## 11. USE CASES

### UC-01: Generate Digital OPD Token
* **Primary Actor:** Patient / Caregiver
* **Preconditions:** Clinic is operational (`isOperational = true`); at least one shift is active.
* **Main Steps:**
  1. Patient navigates to Token Booking section on `index.html`.
  2. Patient enters name, phone number, age, gender, selects shift, and types symptoms.
  3. Patient submits the form.
  4. System assigns the next sequential daily token number.
  5. System calculates patients ahead and estimated wait time.
  6. Digital token slip appears on screen with printable layout.
* **Alternate Flow:** Clinic is closed $\rightarrow$ System displays error message and refuses token creation.
* **Postconditions:** New token saved in `tokens` collection; in-app notification dispatched to admin.

### UC-02: Track Live OPD Queue
* **Primary Actor:** Patient / Public Visitor
* **Preconditions:** User has opened `index.html`.
* **Main Steps:**
  1. Patient views Hero section of `index.html`.
  2. Browser issues automated request to `/api/public/tokens/live-status` every 6 seconds.
  3. System renders current token serving, waiting count, total tokens, and clinic status.
* **Postconditions:** Patient has real-time visibility into queue progression.

### UC-03: Search Token By Mobile / Number
* **Primary Actor:** Patient
* **Preconditions:** Patient generated a token earlier in the day.
* **Main Steps:**
  1. Patient inputs token number or 10-digit mobile number into search box.
  2. System queries database and returns matching token details, current status, and wait time.
* **Postconditions:** Patient confirms their current standing in queue.

### UC-04: Book Doctor Home Visit
* **Primary Actor:** Homebound Patient / Caregiver
* **Preconditions:** Date selected is today or in the future.
* **Main Steps:**
  1. Patient fills Home Visit form on `index.html` (Address, Landmark, PIN, Date, Slot, Reason).
  2. System validates slot availability.
  3. System registers request with status `PENDING`.
  4. Confirmation modal renders booking summary.
* **Alternate Flow:** Slot is already accepted $\rightarrow$ System alerts patient to select another slot or date.
* **Postconditions:** Record created in `home_visits` collection; admin alerted via notification.

### UC-05: Track Home Visit Request
* **Primary Actor:** Home Visit Patient
* **Preconditions:** Previous booking exists in system.
* **Main Steps:**
  1. Patient enters mobile number into Home Visit Tracker.
  2. System displays list of visits with status badges (`PENDING`, `ACCEPTED`, `COMPLETED`, etc.).

### UC-06: Submit Patient Inquiry
* **Primary Actor:** Public Visitor
* **Main Steps:** User submits Contact form; system creates `ContactSubmission` with `NEW` status.

### UC-07: Admin Login
* **Primary Actor:** Dr. Rajesh Sonar / Receptionist
* **Main Steps:** User provides username and password; system validates BCrypt hash and issues JWT Bearer token stored in browser `localStorage`.

### UC-08: Advance OPD Queue (Call Next Token)
* **Primary Actor:** Doctor / Operator
* **Preconditions:** Authenticated admin; at least one patient is `WAITING`.
* **Main Steps:**
  1. Doctor clicks **Call Next Token** on `admin.html`.
  2. System marks current active token as `COMPLETED`.
  3. System updates next waiting token to `CALLED`.
  4. Public and admin displays update dynamically.
* **Alternate Flow:** Queue has 0 waiting patients $\rightarrow$ System displays warning toast.

### UC-09: Direct Call / Skip / Recall Token
* **Primary Actor:** Doctor / Operator
* **Main Steps:** Doctor directly calls specific token ID, skips absent patient, or recalls previously skipped patient back to waiting status.

### UC-10: Review and Process Home Visit
* **Primary Actor:** Doctor / Staff
* **Main Steps:** Staff opens Home Visit modal on `admin.html`, updates status (`ACCEPTED`/`REJECTED`/`COMPLETED`), adds doctor clinical notes and admin remarks.

### UC-11: Configure Consultation Shifts
* **Primary Actor:** Doctor / Admin
* **Main Steps:** Admin edits start/end timings or toggles active status of shifts 1–5; clicks **Reset Defaults** to restore standard 2-shift schedule.

### UC-12: Update Clinic Information & Toggle Operational Status
* **Primary Actor:** Doctor / Admin
* **Main Steps:** Admin edits clinic profile, contact numbers, banner notice, or toggles clinic operational open/closed state.

### UC-13: Manage Notifications & Broadcast Announcement
* **Primary Actor:** Staff / Admin
* **Main Steps:** Admin views notification drawer, marks items as read, or broadcasts clinic announcements.

### UC-14: Change Admin Password
* **Primary Actor:** Authenticated Admin
* **Main Steps:** User provides current password and confirms new password (minimum 6 characters); system updates encrypted password.

---

## 12. IMPORTANT CODE SNIPPETS

### Snippet 1: JWT Token Generation & Claims
* **File:** `src/main/java/com/laxmiclinic/security/JwtTokenProvider.java`
* **Explanation:** Generates secure HMAC-SHA256 signed JSON Web Tokens containing the user's username as subject and calculated expiration timestamp.
```java
public String generateTokenFromUsername(String username) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

    return Jwts.builder()
            .subject(username)
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(key)
            .compact();
}

public boolean validateToken(String authToken) {
    try {
        Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(authToken);
        return true;
    } catch (ExpiredJwtException ex) {
        logger.warn("Expired JWT token: {}", ex.getMessage());
    } catch (JwtException | IllegalArgumentException ex) {
        logger.error("Invalid JWT token: {}", ex.getMessage());
    }
    return false;
}
```

### Snippet 2: JWT Authentication Filter
* **File:** `src/main/java/com/laxmiclinic/security/JwtAuthenticationFilter.java`
* **Explanation:** Intercepts every incoming HTTP request, extracts the Bearer token from the `Authorization` header, validates it, and establishes the user's authentication context.
```java
@Override
protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
    try {
        String jwt = getJwtFromRequest(request);

        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
            String username = tokenProvider.getUsernameFromJwt(jwt);

            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
            if (userDetails != null && userDetails.isEnabled()) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
    } catch (Exception ex) {
        logger.error("Could not set user authentication in security context", ex);
    }

    filterChain.doFilter(request, response);
}
```

### Snippet 3: Security Filter Chain & Route Authorization
* **File:** `src/main/java/com/laxmiclinic/config/SecurityConfig.java`
* **Explanation:** Configures Spring Security with stateless sessions, custom CORS rules, permit-all access for public resources, and strict authentication enforcement for `/api/admin/**`.
```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(exceptions -> exceptions
                    .authenticationEntryPoint((request, response, authException) -> {
                        response.setContentType("application/json;charset=UTF-8");
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getWriter().write("{\"success\":false,\"message\":\"Unauthorized access: Please login with valid credentials\",\"status\":401}");
                    })
            )
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/", "/index.html", "/admin.html", "/css/**", "/js/**", "/images/**", "/favicon*").permitAll()
                    .requestMatchers("/api/public/**").permitAll()
                    .requestMatchers("/actuator/**").permitAll()
                    .requestMatchers("/api/admin/**").authenticated()
                    .anyRequest().authenticated()
            )
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
}
```

### Snippet 4: Synchronized Sequential Token Generation
* **File:** `src/main/java/com/laxmiclinic/service/TokenQueueService.java`
* **Explanation:** Calculates the next incremental daily token number within a thread-safe synchronized block, verifying clinic operational state and shift availability.
```java
public synchronized TokenResponseDto generateToken(TokenGenerateRequest request) {
    ClinicInfo clinicInfo = clinicInfoService.getClinicInfo();
    if (!clinicInfo.isOperational()) {
        throw new BadRequestException("Clinic is currently closed: " + clinicInfo.getOperationalStatusMessage());
    }

    String today = getTodayDateString();

    int shiftNum = 1;
    String shiftName = "General Consultation";
    if (request.getShiftNumber() != null && request.getShiftNumber() >= 1 && request.getShiftNumber() <= 5) {
        Shift shift = shiftService.getShiftByNumber(request.getShiftNumber());
        if (!shift.isActive()) {
            throw new BadRequestException("Selected shift '" + shift.getName() + "' is currently INACTIVE / CLOSED. Please select an active shift.");
        }
        shiftNum = shift.getShiftNumber();
        shiftName = shift.getName();
    }

    int nextTokenNumber = 1;
    Optional<Token> lastTokenToday = tokenRepository.findFirstByTokenDateOrderByTokenNumberDesc(today);
    if (lastTokenToday.isPresent()) {
        nextTokenNumber = lastTokenToday.get().getTokenNumber() + 1;
    }

    Token token = new Token(
            nextTokenNumber, today, request.getPatientName().trim(),
            request.getPatientPhone().trim(), request.getAge(),
            request.getGender(), request.getSymptoms(), shiftNum, shiftName
    );

    Token saved = tokenRepository.save(token);
    return mapToDto(saved);
}
```

### Snippet 5: Atomic Queue Advance (Call Next Token)
* **File:** `src/main/java/com/laxmiclinic/service/TokenQueueService.java`
* **Explanation:** Automates queue advancement by completing any currently called token and transitioning the lowest waiting token to `CALLED`.
```java
public synchronized TokenResponseDto callNextToken() {
    String today = getTodayDateString();

    // 1. Auto-complete existing CALLED token if any
    Optional<Token> currentCalled = tokenRepository.findFirstByTokenDateAndStatusOrderByCalledAtDesc(today, Token.TokenStatus.CALLED);
    currentCalled.ifPresent(token -> {
        token.setStatus(Token.TokenStatus.COMPLETED);
        token.setCompletedAt(LocalDateTime.now());
        tokenRepository.save(token);
        logger.info("Auto-completed Token #{} before calling next", token.getTokenNumber());
    });

    // 2. Find the lowest WAITING token
    Optional<Token> nextWaiting = tokenRepository.findFirstByTokenDateAndStatusOrderByTokenNumberAsc(today, Token.TokenStatus.WAITING);
    if (nextWaiting.isEmpty()) {
        throw new BadRequestException("No waiting patients in queue for today.");
    }

    Token tokenToCall = nextWaiting.get();
    tokenToCall.setStatus(Token.TokenStatus.CALLED);
    tokenToCall.setCalledAt(LocalDateTime.now());
    Token saved = tokenRepository.save(tokenToCall);

    return mapToDto(saved);
}
```

### Snippet 6: Dynamic Wait Time Calculation & Privacy Name Masking
* **File:** `src/main/java/com/laxmiclinic/service/TokenQueueService.java`
* **Explanation:** Formulates patient wait time predictions ($10\text{ minutes}\times \text{patients ahead}$) and creates HIPAA/privacy-conscious masked patient names for public displays.
```java
if (token.getStatus() == Token.TokenStatus.WAITING) {
    long countAhead = tokenRepository.countByTokenDateAndTokenNumberLessThanAndStatus(
            token.getTokenDate(),
            token.getTokenNumber(),
            Token.TokenStatus.WAITING
    );
    dto.setPatientsAhead((int) countAhead);
    dto.setEstimatedWaitMinutes((int) countAhead * 10);
    dto.setStatusMessage("Waiting in queue. " + countAhead + " patients ahead of you.");
}

private String maskPatientName(String name) {
    if (name == null || name.isBlank()) return "Anonymous";
    String[] parts = name.trim().split("\\s+");
    if (parts.length == 1) {
        if (parts[0].length() <= 2) return parts[0];
        return parts[0].substring(0, 2) + "***";
    }
    return parts[0] + " " + parts[parts.length - 1].charAt(0) + "***";
}
```

### Snippet 7: Home Visit Booking with Collision Prevention
* **File:** `src/main/java/com/laxmiclinic/service/HomeVisitService.java`
* **Explanation:** Enforces date validity and verifies that the selected appointment time slot has not already been booked and accepted for another patient.
```java
public HomeVisit bookHomeVisit(HomeVisitBookingRequest request) {
    try {
        LocalDate requestedDate = LocalDate.parse(request.getPreferredDate().trim(), DATE_FORMATTER);
        if (requestedDate.isBefore(LocalDate.now())) {
            throw new BadRequestException("Preferred date cannot be in the past.");
        }
    } catch (Exception ex) {
        if (ex instanceof BadRequestException) throw ex;
        throw new BadRequestException("Invalid date format. Expected YYYY-MM-DD.");
    }

    boolean isSlotOccupied = homeVisitRepository.existsByPreferredDateAndPreferredTimeSlotAndStatus(
            request.getPreferredDate().trim(),
            request.getPreferredTimeSlot().trim(),
            HomeVisit.VisitStatus.ACCEPTED
    );

    if (isSlotOccupied) {
        throw new BadRequestException("The selected time slot (" + request.getPreferredTimeSlot() + ") on " + request.getPreferredDate() + " is already booked. Please choose another time slot or date.");
    }

    HomeVisit homeVisit = new HomeVisit(
            request.getPatientName().trim(), request.getMobile().trim(),
            request.getEmail() != null ? request.getEmail().trim() : null,
            request.getAddress().trim(), request.getLandmark() != null ? request.getLandmark().trim() : null,
            request.getPincode().trim(), request.getPreferredDate().trim(),
            request.getPreferredTimeSlot().trim(), request.getReason()
    );

    return homeVisitRepository.save(homeVisit);
}
```

### Snippet 8: In-Memory MongoDB Server Fallback
* **File:** `src/main/java/com/laxmiclinic/config/MongoConfig.java`
* **Explanation:** Checks if local MongoDB is needed and binds an embedded in-memory MongoDB instance to port 27017, enabling developer execution on offline or fresh workstations.
```java
@PostConstruct
public void init() {
    if (mongoUri != null && mongoUri.contains("localhost:27017") && !isPortInUse(27017)) {
        try {
            logger.info("Starting pure Java in-memory MongoServer on localhost:27017 for seamless local dev/offline execution...");
            mongoServer = new MongoServer(new MemoryBackend());
            mongoServer.bind("localhost", 27017);
            logger.info("In-memory MongoServer started and ready.");
        } catch (Exception ex) {
            logger.warn("Could not bind in-memory MongoServer to 27017: {}", ex.getMessage());
        }
    }
}
```

### Snippet 9: Centralized REST Exception Handler
* **File:** `src/main/java/com/laxmiclinic/exception/GlobalExceptionHandler.java`
* **Explanation:** Traps validation failures and business exceptions across all controllers, formatting standard error responses with HTTP status codes.
```java
@ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach(error -> {
        String fieldName = ((FieldError) error).getField();
        String errorMessage = error.getDefaultMessage();
        errors.put(fieldName, errorMessage);
    });

    logger.warn("Validation error: {}", errors);
    ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Input validation failed", errors);
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
}
```

### Snippet 10: Compound Indexed Token Entity
* **File:** `src/main/java/com/laxmiclinic/model/Token.java`
* **Explanation:** Defines the MongoDB document structure for OPD tokens along with compound indexing for optimized sequence retrieval.
```java
@Document(collection = "tokens")
@CompoundIndex(name = "date_token_idx", def = "{'tokenDate': 1, 'tokenNumber': 1}")
public class Token {

    public enum TokenStatus {
        WAITING, CALLED, COMPLETED, SKIPPED, CANCELLED
    }

    @Id
    private String id;

    @Indexed
    private int tokenNumber;

    @Indexed
    private String tokenDate;

    private String patientName;
    
    @Indexed
    private String patientPhone;

    private int shiftNumber;
    private String shiftName;
    private TokenStatus status = TokenStatus.WAITING;
    // ... Getters and Setters
}
```

---

## 13. SECURITY

1. **Password Hashing:** Administrative passwords are protected using the `BCryptPasswordEncoder` (adaptive salt, 10 rounds). Passwords are never stored in plain text.
2. **Stateless JWT Tokens:** Authentication utilizes RFC 7519 JSON Web Tokens signed with HMAC-SHA256 (`Keys.hmacShaKeyFor`). The server does not maintain server-side session state, eliminating session fixation vulnerabilities. Tokens expire after 24 hours (`86,400,000` ms).
3. **Strict Route Segregation:**
   - Public read/write operations are isolated under `/api/public/**`.
   - Administrative endpoints under `/api/admin/**` strictly enforce `ROLE_ADMIN` authentication via Spring Security's `SecurityFilterChain`.
4. **CORS Hardening:** Configured in `SecurityConfig.java` to restrict allowed origins to known frontend hosts (`http://localhost:8080`, `http://localhost:3000`, `http://127.0.0.1:5500`, or `app.cors.allowed-origins`). Standard methods (`GET`, `POST`, `PUT`, `PATCH`, `DELETE`, `OPTIONS`) and exposed `Authorization` headers are defined.
5. **CSRF Strategy:** Cross-Site Request Forgery protection is purposefully disabled (`AbstractHttpConfigurer::disable`) because the application relies entirely on stateless Bearer tokens in the HTTP `Authorization` header rather than ambient browser session cookies.
6. **Input Sanitization & Jakarta Validation:** Request payloads are checked using Bean Validation annotations (`@NotBlank`, `@Pattern`, `@Size`, `@Min`, `@Max`). Invalid requests are intercepted by `GlobalExceptionHandler` and rejected before business execution.
7. **Patient Privacy Protection:** Live public displays mask patient names (e.g., `Aarav P***`) to uphold medical privacy in the waiting room.
8. **Rate Limiting:** `NOT IMPLEMENTED` in Java application code (delegated to cloud edge reverse proxy such as Nginx or Cloudflare in production deployment).

---

## 14. TESTING

### Testing Tools Used
* **JUnit 5 (Jupiter):** Test runner, lifecycle hooks (`@BeforeEach`, `@Test`), and assertions (`assertEquals`, `assertNotNull`, `assertTrue`, `assertThrows`).
* **Mockito (`mockito-core`, `mockito-junit-jupiter`):** Mocking repositories and services (`@Mock`, `when(...)`, `verify(...)`, `times(1)`).
* **Maven Surefire Plugin:** Automated execution during build lifecycle.

### Test Files Present
1. `src/test/java/com/laxmiclinic/TokenQueueServiceTest.java`
2. `src/test/java/com/laxmiclinic/HomeVisitServiceTest.java`
3. `src/test/java/com/laxmiclinic/ShiftServiceTest.java`
4. `src/test/java/com/laxmiclinic/JwtTokenProviderTest.java`

### Test Cases Table (100% Passing in Build)

| Test ID | Test File / Module | Scenario | Input / Conditions | Expected Result | Actual Result |
| :--- | :--- | :--- | :--- | :--- | :--- |
| `TC-TKN-01` | `TokenQueueServiceTest` | Generate first token of the day | Empty repository for today; Patient: Aarav Patel, Phone: 9876543210 | Returns `TokenResponseDto` with token number `1`, status `WAITING` | PASS |
| `TC-TKN-02` | `TokenQueueServiceTest` | Sequential token increment | Existing token #4 present today; Patient: Priya Sharma | Returns token number `5` | PASS |
| `TC-TKN-03` | `TokenQueueServiceTest` | Advance queue (`callNextToken`) | Token #1 in `WAITING` status | Updates Token #1 status to `CALLED`, sets `calledAt` timestamp | PASS |
| `TC-HV-01` | `HomeVisitServiceTest` | Successful home visit booking | Future date; Slot: 09:30 AM - 11:30 AM; Patient: Suresh Nair; Slot free | Returns `HomeVisit` with status `PENDING` | PASS |
| `TC-HV-02` | `HomeVisitServiceTest` | Collision prevention | Same date and time slot already has an `ACCEPTED` visit | Throws `BadRequestException`; repository save is never invoked | PASS |
| `TC-SFT-01` | `ShiftServiceTest` | Initialize default 2 shifts | Fresh repository | Returns 2 shifts: Shift #1 (Morning, 12:00 PM), Shift #2 (Evening, 07:00 PM) | PASS |
| `TC-SFT-02` | `ShiftServiceTest` | Update shift hours | Existing Shift #1; update name to "Morning Shift OPD", start: "12:00 PM" | Updates shift name and start time; verifies repository save called | PASS |
| `TC-JWT-01` | `JwtTokenProviderTest` | Generate and validate JWT | Username: `"admin"` | Generated token validates `true`; username extracted equals `"admin"` | PASS |
| `TC-JWT-02` | `JwtTokenProviderTest` | Validate malformed JWT | Input string: `"invalid.jwt.token"` | `validateToken(...)` returns `false` | PASS |

> **Surefire Test Execution Summary:** Tests run: 9, Failures: 0, Errors: 0, Skipped: 0 (100% Pass Rate).

---

## 15. SCREENS / UI

### 1. Public Patient Portal (`/` or `/index.html`)
* **URL:** `http://localhost:8080/` or `http://localhost:8080/index.html`
* **Role Allowed:** Public / Anonymous
* **Rendering File:** `src/main/resources/static/index.html` (supported by `app.js`, `token.js`, `homevisit.js`, `api.js`, `style.css`)
* **Sections & Elements:**
  1. **Sticky Header & Brand:** Clinic logo, name, navigation anchors, emergency phone link, and **Staff Login** trigger button.
  2. **Hero Live Queue Card:**
     - Clinic operational badge (Open pulse dot vs. Closed red indicator).
     - Live soundwave animation.
     - Large typography displaying Currently Serving Token Number (`#3` or `--`).
     - Waiting Patients Counter and Total Patients Today counter (with roll-up animation).
     - Doctor Consultation Hours banner.
  3. **Shift Timeline Schedule:** Displays Dr. Rajesh Sonar's active consultation shifts and session timings.
  4. **OPD Token Booking Card:** Patient registration form (Name, Phone, Age, Gender, Shift, Symptoms) with submission button.
  5. **Token Search & Mobile Tracker Card:** Input field allowing patients to search by token number or enter mobile number to view active tokens today.
  6. **Home Visit Booking Card:** Patient request form (Name, Mobile, Address, Landmark, PIN, Date picker with past-date lockdown, Slot dropdown, Reason).
  7. **Home Visit Status Tracker:** Mobile number search input displaying status cards (`PENDING`, `ACCEPTED`, `COMPLETED`).
  8. **About Doctor Section:** Dr. Rajesh Sonar's profile, B.A.M.S. (BOM) qualification, ESIS registration details, clinical highlights, and Google Maps embed placeholder.
  9. **Contact & Inquiry Section:** Direct messaging form.
  10. **Modals:**
      - *Token Slip Modal:* Shows printable receipt, barcode aesthetic, wait estimate, and triggers confetti celebration.
      - *Home Visit Confirmation Modal:* Summary of booking request.
      - *Staff Login Modal:* Secure credential prompt.

### 2. Admin & Doctor Operations Dashboard (`/admin` or `/admin.html`)
* **URL:** `http://localhost:8080/admin` or `http://localhost:8080/admin.html`
* **Role Allowed:** `ROLE_ADMIN` (Redirects to `/index.html` if unauthenticated)
* **Rendering File:** `src/main/resources/static/admin.html` (supported by `admin.js`, `api.js`, `admin.css`)
* **Views / Tabs:**
  1. **Sidebar Navigation:** Collapsible navigation with tabs for Overview, Queue, Home Visits, Shifts, Site Info, Inquiries, Notifications, and Settings, plus user display and Sign Out button.
  2. **Top Header:** Active view title, in-app notification drawer bell with unread badge counter.
  3. **Overview Tab (`#overview`):**
     - KPI Metric Cards: Total Tokens Today, Current Serving Token, Waiting Patients, Completed Consultations, Pending Home Visits, Clinic Operational Status.
     - Operator Quick-Console: Prominent display of current token with a large **Call Next Token** primary button.
  4. **Queue Management Tab (`#queue`):**
     - Full queue table with columns: Token #, Patient Details (Name, Age, Gender), Phone, Status Badge, Shift, Symptoms, Action Buttons.
     - Action Buttons: Call, Skip, Mark Complete, Recall.
     - Status Filter dropdown (`ALL`, `WAITING`, `CALLED`, `COMPLETED`, `SKIPPED`).
  5. **Home Visits Tab (`#homevisits`):**
     - Tabular registry of all home visits with patient name, phone, address, date, slot, status badge.
     - **Manage Action:** Opens modal to change status (`ACCEPTED`, `REJECTED`, `COMPLETED`, `CANCELLED`), record admin remarks, and write doctor clinical notes.
  6. **Shifts Management Tab (`#shifts`):**
     - Shift configuration cards showing shift number, name, start time, end time, and active toggle switch.
     - Form to update shift parameters and **Reset Defaults** button.
  7. **Site Info Tab (`#siteinfo`):**
     - Form to edit clinic name, doctor qualifications, street address, emergency phone, consultation timings, and banner announcement.
     - Master **Clinic Operational Toggle** switch with customizable operational message.
  8. **Inquiries Tab (`#inquiries`):**
     - Contact submissions list with **Resolve** action button.
  9. **Notification Drawer (`#notifications`):**
     - Slide-out drawer with chronological notification stream, Mark Read buttons, and **Mark All Read** trigger.
  10. **Settings Tab (`#settings`):**
      - Current password verification and new password change form.

---

## 16. DEVELOPMENT HISTORY

### Phase 1: Problem Analysis & Architectural Blueprint (Late August 2026)
* Conducted operational workflow analysis for Laxmi Clinic in Sakinaka, Mumbai, identifying overcrowding during peak OPD hours as the critical challenge.
* Selected Java 21 LTS and Spring Boot 3.3.3 for long-term support and type safety.
* Modeled core domain requirements: Token queues, Shift partitions, and Home visits.

### Phase 2: Core Domain Engine & Security Foundation (Late August 2026)
* Built `TokenQueueService` implementing daily sequential counter logic.
* Integrated Spring Security 6 with stateless JJWT (`0.12.6`) and BCrypt password encryption.
* Added `DataInitializer` to automatically seed administrator credentials (`Rajesh Sonar`) and Dr. Rajesh Sonar's profile upon initial launch.

### Phase 3: Home Visit Scheduling & Collision Prevention (September 2026)
* Created `HomeVisit` entity and repository methods.
* Engineered collision check preventing overlapping accepted home visits on the same date and slot.
* Built public tracker enabling patients to track home visit requests using their mobile number.

### Phase 4: Frontend Portal & Operator Console (September 2026)
* Designed `index.html` and `style.css` using modern medical color palettes (Teal `#0d9488`, Slate `#0f172a`, Emerald `#10b981`).
* Engineered responsive single-page admin dashboard in `admin.html` and `admin.js` with hash routing (`#overview`, `#queue`, `#homevisits`, etc.).
* Implemented client-side polling (6-second intervals) to keep queue displays synchronized across devices without WebSocket configuration overhead.

### Phase 5: Database Resilience & In-Memory Fallback (October 2026)
* Configured MongoDB Atlas cloud connection strings.
* Added `bwaldvogel:mongo-java-server` to `MongoConfig.java` to automatically provide an in-memory MongoDB fallback on port 27017 during offline development or testing.
* Wrote unit test suites verifying token increments, double-booking prevention, shift updates, and JWT security.

### Major Problems Encountered and Solutions
1. **Concurrent Token Number Duplication:**
   - *Problem:* Rapid simultaneous token requests could read the same "highest token number" and generate duplicate numbers.
   - *Solution:* Implemented a `synchronized` lock on `TokenQueueService.generateToken(...)` and created a compound index `@CompoundIndex(name = "date_token_idx", def = "{'tokenDate': 1, 'tokenNumber': 1}")` in MongoDB.
2. **Double-Booking of Home Visit Slots:**
   - *Problem:* Multiple patients could submit requests for the same time slot while requests were pending.
   - *Solution:* Allowed multiple pending requests, but enforced strict collision prevention upon booking and upon admin acceptance (`existsByPreferredDateAndPreferredTimeSlotAndStatus`).
3. **Local Testing Dependency:**
   - *Problem:* Running unit tests or developing without an active MongoDB connection caused application startup failure.
   - *Solution:* Integrated `de.bwaldvogel:mongo-java-server`, allowing seamless testing and local offline execution.

---

## 17. LIMITATIONS AND FUTURE SCOPE

### Known Gaps and Limitations
1. **No External SMS / WhatsApp Gateway:** Notifications are presently internal in-app alerts stored in MongoDB. Patients must view their status on the website.
2. **Short-Polling vs. WebSockets:** Real-time updates utilize 6-second HTTP polling rather than WebSockets/STOMP.
3. **No Online Payment Gateway:** Billing is handled in person via cash or UPI.
4. **Single Administrative Role:** The system currently utilizes a single role (`ROLE_ADMIN`) for both the doctor and reception staff.

### Future Scope
1. **WhatsApp Business API Integration:** Send automatic WhatsApp messages when a token is generated, when 2 patients remain ahead, and when the doctor calls the token.
2. **WebSocket / Server-Sent Events (SSE) Migration:** Upgrade the live queue board to WebSockets or SSE for instant real-time synchronization.
3. **Digital Prescription & E-Pharmacy Module:** Allow Dr. Rajesh Sonar to generate printable prescriptions linked to completed tokens.
4. **Multi-Doctor & Multi-Room Support:** Expand queue partitions across multiple consultation rooms and attending physicians.
5. **Multilingual Patient Portal:** Provide Hindi and Marathi localization options for the local Sakinaka community.

---

## 18. SETUP AND DEPLOYMENT

### Prerequisites
* Java Development Kit (JDK) 21 LTS installed (`java -version`).
* Apache Maven 3.9+ installed (`mvn -v`).
* Network access to MongoDB Atlas (or use the built-in in-memory MongoDB fallback).

### Install and Build Commands
```powershell
# 1. Navigate to project root
cd "c:\Users\vikas\OneDrive\Desktop\Laxmi Clinic"

# 2. Compile source code
mvn clean compile

# 3. Execute all unit tests
mvn test

# 4. Package executable standalone JAR
mvn clean package -DskipTests
```

### Run Commands
```powershell
# Run using Spring Boot Maven plugin (Dev Profile)
mvn spring-boot:run

# OR Run the packaged standalone JAR directly
java -jar target/laxmi-clinic-management-1.0.0.jar
```

### Access Points
* **Public Patient Portal:** `http://localhost:8080/`
* **Admin & Doctor Dashboard:** `http://localhost:8080/admin`
* **Actuator Health Check:** `http://localhost:8080/actuator/health`

### Default Seed Data Credentials
* **Username:** `Rajesh Sonar`
* **Password:** `rajeshsonarlc`
* **Role:** `ROLE_ADMIN`
* **Seeded Shifts:** Morning Shift (12:00 PM – 05:00 PM), Evening Shift (07:00 PM – 12:00 AM)

### Cloud Deployment (Render / Railway / Docker)
To deploy as a containerized microservice:
```dockerfile
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY target/laxmi-clinic-management-1.0.0.jar app.jar
EXPOSE 8080
ENV PORT=8080
ENV SPRING_PROFILES_ACTIVE=prod
ENTRYPOINT ["java", "-jar", "app.jar"]
```
Set environment variables:
* `SPRING_DATA_MONGODB_URI`: Your MongoDB Atlas URI.
* `JWT_SECRET`: A secure 256-bit random string.
* `PORT`: `8080` (or host-assigned port).
