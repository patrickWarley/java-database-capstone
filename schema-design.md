## MySQL Database Design

### Table: patients
- id: INT, PRIMARY_KEY, AUTO INCREMENT
- name: VARCHAR, NOT NULL
- email: VARCHAR,UNIQUE, NOT NULL
- password: VARCHAR, NOT NULL
- birthday: DATE, NOT NULL

### Table: doctors
- id: INT, PRIMARY_KEY, AUTO INCREMENT
- name: VARCHAR, NOT NULL
- email: VARCHAR, UNIQUE, NOT NULL
- contact_info: VARCHAR, NOT NULL
- birthday: DATE, NOT NULL
- speciality: VARCHAR, NOT NULL

### Table: appointment 
- id: INT, Primary Key, Auto Increment
- doctor_id: INT, Foreign Key → doctor(id)
- patient_id: INT, Foreign Key → patients(id)
- appointment_time: DATETIME, Not Null
- status: INT (0 = Scheduled, 1 = Completed, 2 = Cancelled)

### Table: admin
- id: INT, PRIMARY_KEY, AUTO INCREMENT
- username: VARCHAR, NOT NULL,
- password: VARCHAR, NOT NULL

### Table: doctor_availability 
- id: INT, PRIMARY_KEY, AUTO INCREMENT
- doctor_id: INT, Foreign KEY -> doctors(id)
- startTime: TIME, NOT NULL,
- endTime: TIME, NOT NULL
- work_days: VARCHAR, NOT NULL

### Table: doctor_exceptions 
- id: INT, PRIMARY_KEY, AUTO INCREMENT
- doctor_id: INT, Foreign KEY -> doctors(id)
- startTime: TIME, NOT NULL,
- endTime: TIME, NOT NULL
- date: DATE, NOT NULL

### Table: clinic_locations
- id: INT, PRIMARY_KEY, AUTO INCREMENT
- address: VARCHAR, NOT NULL
- name: VARCHAR, NOT NULL

### Table: payments
- id: INT, PRIMARY_KEY, AUTO INCREMENT
- patient_id: INT, FOREIGN KEY -> patient(id)
- amount: FLOAT, NOT NULL
- status: VARCHAR, NOT NULL

## Mongodb Collection Design

### Collection: precriptions

```json
{
    "patient_id":"",
    "doctor_id":"",
    "_id": "ObjectId('64abc123456')",
    "patientName": "John Smith",
    "appointmentId": 51,
    "medication": "Paracetamol",
    "dosage": "500mg",
    "doctorNotes": "Take 1 tablet every 6 hours.",
    "refillCount": 2,
    "pharmacy": {
        "name": "Walgreens SF",
        "location": "Market Street"
    }
}
```