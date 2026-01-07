# Schema Design

## MySQL Database Design

### Table: patients
- id: INT, Primary Key, Auto Increment
- full_name: VARCHAR(100), Not Null
- email: VARCHAR(255), Not Null, Unique
- password_hash: VARCHAR(255), Not Null
- phone: VARCHAR(20), Null, Unique
- created_at: DATETIME, Not Null, Default CURRENT_TIMESTAMP

---

### Table: doctors
- id: INT, Primary Key, Auto Increment
- full_name: VARCHAR(100), Not Null
- specialization: VARCHAR(100), NOT NULL
- email: VARCHAR(255), Not Null, Unique
- password_hash: VARCHAR(255), Not Null
- phone: VARCHAR(20), Null, Unique
- bio: TEXT, Null
- is_active: BOOLEAN, NOT NULL, Default TRUE
- created_at: DATETIME, Not Null, Default CURRENT_TIMESTAMP

---

### Table: admin
- id: INT, Primary Key, Auto Increment
- username: VARCHAR(50), Not Null, Unique
- password_hash: VARCHAR(255), Not Null
- created_at: DATETIME, Not Null, Default CURRENT_TIMESTAMP

---

### Table: appointments
- id: INT, Primary Key, Auto Increment
- doctor_id: INT, Foreign Key → doctors(id)
- patient_id: INT, Foreign Key → patients(id)
- appointment_time: DATETIME, Not Null
- status: INT, Not Null (0 = Scheduled, 1 = Completed, 2 = Cancelled)
- created_at: DATETIME, Not Null, Default CURRENT_TIMESTAMP

---

## MongoDB Collection Design

### Collection: prescriptions

```json
{
  "_id": "ObjectId('64abc123456')",
  "patientName": "John Smith",
  "appointmentId": 51,
  "patientId": 7,
  "doctorId": 3,
  "medication": "Paracetamol",
  "dosage": "500mg",
  "doctorNotes": "Take 1 tablet every 6 hours.",
  "refillCount": 2,
  "pharmacy": {
    "name": "Walgreens SF",
    "location": "Market Street"
  }
}
