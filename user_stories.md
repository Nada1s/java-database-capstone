# User Story Template

**Title:**
_As a [user role], I want [feature/goal], so that [reason]._

**Acceptance Criteria:**
1. [Criteria 1]
2. [Criteria 2]
3. [Criteria 3]

**Priority:** [High/Medium/Low]
**Story Points:** [Estimated Effort in Points]
**Notes:**
- [Additional information or edge cases]


## Admin User Stories

### User Story 1: Admin Login
**Title:** Admin login to the system

_As an Admin, I want to log into the portal using my username and password, so that I can securely manage the platform._

**Acceptance Criteria:**
1. The admin can enter a valid username and password.
2. The system authenticates the admin credentialas.
3. The admin is redirected to the main dashboard.

**Priority:** High
**Story Points:** 3

---

### User Story 2: Admin Logout
**Title:** Admin logout from the system

_As an Admin, I want to log out the portal, so that I can protect system access._

**Acceptance Criteria:**
1. The admin can log out from any page.
2. The system terminates the active session.
3. The admin is redirected to the login page after logout.

**Priority:** High
**Story Points:** 2

---

### User Story 3: Add Doctor to Portal
**Title:** Add doctor profile

_As an Admin, I want to add doctors to the portal, so that they can access and use the system._

**Acceptance Criteria:**
1. The admin can enter doctor details.
2. The system validates the entered information.
3. The doctor profile is successfully created and stored.

**Priority:** High
**Story Points:** 5

---

### User Story 4: Delete Doctor Profile
**Title:** Delete doctor profile

_As an Admin, I want to delete a doctor's profile from the portal, so that inactive or invalid accounts are removed from the system._

**Acceptance Criteria:**
1. The admin can select an existing doctor profile.
2. The system confirms the delete action.
3. The doctor profile is permanently removed from the system.

**Priority:** Medium
**Story Points:** 3

**Notes:**
-Consider handling existing appointments before deletion.

---

### User Story 5: View Monthly Appointment Statistics
**Title:** View appointment statistics per month

_As an Admin, I want to run a stored procedure in MySQL to retrieve the number of appointments per month, so that I can track system usage statistics._

**Acceptance Criteria:**
1. The stored procedure executes successfully in MySQL CLI.
2. The procedure returns appointment counts grouped by month.
3. The results can be used for reporting or analysis.

**Priority:** Medium
**Story Points:** 5

---

## Patient User Stories

### User Story 6: View Doctores Without Login
**Title:** View list of doctors as a guest

_As a patient, I want to view a list of doctors without logging in, so that I can explore options before registering._

**Acceptance Criteria:**
1. The system displays a list of available doctors without requiring authentication.
2. Doctor details such as name and specialization are visible.
3. The user is prompted to register when attempting restricted actions.

**Priority:** Medium
**Story Points:** 3

---

### User Story 7: Patient Sign Up
**Title:** Patient registeration

_As a patient, I want to sign up using my email and password, so that I can book appointments._

**Acceptance Criteria:**
1. The patient can register using a valid email and password.
2. The system validates email uniqueness.
3. The patient account is successfully created.
**Priority:** High
**Story Points:** 5

---

### User Story 8: Patient Login
**Title:** Patient login

_As an patient, I want to log into the portal, so that I can manage my bookings._

**Acceptance Criteria:**
1. The patient can enter valid login credentials.
2. The system authenticates the patient.
3. The patient is redirected to their dashboard after login.
**Priority:** High
**Story Points:** 3

---

### User Story 9: Patient Logout
**Title:** Patient logout

_As a patient, I want to log out of the portal, so that I can secure my account._

**Acceptance Criteria:**
1. The patient can log out from the portal.
2. The system ends the active session.
3. The patient is redirected to the login page.
**Priority:** Medium
**Story Points:** 2

---

### User Story 10: Book Appointment
**Title:** Book an appointment with a doctor

_As a patient, I want to log in and book an hour-long appointment with a doctor, so that I can consult with them._

**Acceptance Criteria:**
1. The patient must be logged in to book an appointment.
2. The patient can select a doctor and an available one-hour time slot.
3. The appointment is successfully saved and confirmed.
**Priority:** High
**Story Points:** 5

**Notes:**
-The system should prevent double booking.

---

### User Story 11: View Upcoming Appointments
**Title:** View upcoming appointments

_As a patient, I want to view my upcoming appointments, so that I can prepare accordingly._

**Acceptance Criteria:**
1. The patient can view a list of upcoming appointments.
2. Appointment details include date, time, and doctor.
3. Past appointments are not shown in the upcoming list.
**Priority:** Medium
**Story Points:** 3

---

## Doctor User Stories

### User Story 12: Doctor Login
**Title:** Doctor login to the system

_As a doctor, I want to Log into the portal, so that I can manage my appointments._

**Acceptance Criteria:**
1. The doctor can enter valid login credentials.
2. The system authenticates the doctor successfully.
3. The doctor is redirected to the doctor dashboard after login.

**Priority:** High
**Story Points:** 3

---

### User Story 13: Doctor Logout
**Title:** Doctor logout to the system

_As a doctor, I want to Log out of the portal, so that I can protect my data._

**Acceptance Criteria:**
1. The doctor can log out from any page.
2. The system terminates the active session.
3. The doctor is redirected to the login page.

**Priority:** Medium
**Story Points:** 2

---

### User Story 14: View Appointment Calendar
**Title:** View appointment calendar

_As a doctor, I want to view my appointment calendar, so that I can stay organized._

**Acceptance Criteria:**
1. The doctor can view all upcoming appointments in a calendar view.
2. Appointments display date, time, and patient name.
3. The calendar shows only the doctor's own appointments.

**Priority:** High
**Story Points:** 5

---

### User Story 15: Mark Unavailability
**Title:** Mark doctor unavailability

_As a doctor, I want to mark my unavailability, so that patients can only book available time slots._

**Acceptance Criteria:**
1. The doctor can mark specific dates or time slots as unavailable.
2. Unavailable slots cannot be booked by patients.
3. Changes to availability are saved immediately.

**Priority:** High
**Story Points:** 5

---

### User Story 16: Update Doctor Profile
**Title:** Update doctor profile information

_As a doctor, I want to update my specialization and contact information, so that patients have up-to-date information._

**Acceptance Criteria:**
1. The doctor can edit specialization and contact details.
2. The system validates the entered information.
3. Updated information is saved and displayed to patients.

**Priority:** Medium
**Story Points:** 3

---

### User Story 17: View Patient Details
**Title:** View patient details for appointments

_As a doctor, I want to view patient details for upcoming appointments, so that I can be prepared._

**Acceptance Criteria:**
1. The doctor can access patient details from an appointment.
2. Patient details include name and relevant information.
3. Only patients with scheduled appointments are visible.

**Priority:** High
**Story Points:** 5
