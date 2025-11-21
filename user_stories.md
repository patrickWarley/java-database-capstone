# User Stories

## Administrator user stories

### **Administrator access:**
_As a administrator, I want to login, so that i can access the admin dashboard and manage users and settings securely._

**Acceptance Criteria:**
1. The admin user can use his username and password to access the platform.
2. HTTPS enforced
3. After succesful login, the admin is redirected to the admin dashboard.

**Priority:** High
**Story Points:** 2

### **Administrator logout:**
_As an administrator, I want to logout of the platform, so that my session ends and my credentials cannot be misused.

**Acceptance Criteria:**

1. After the user logout, the platform revokes and deletes the user session
2. After the succesful logout the user will be redirected to the front page.
3. User Cannot access any protected page after logout.
4. Logout works from any page.

**Priority:** High
**Story Points:** 1
**Notes:**
- Logout should work even if the session is expired.

### **Add new doctors:**
_As an administrator, I want to add new doctors to the portal, so that they can access the platform and work._

**Acceptance Criteria:**

1. After submission, the new doctor is successfully added to the platform.
2. The platform creates the user credentials and the new doctor can access the platform.
3. The system shows the message "The \<\<doctor name\>\> was successfully added".
4. Duplicate messages are prevented, and mandatory fields are validated.

**Priority:** High
**Story Points:** 2
**Notes:**

### **Delete doctor profile:**
_As an administrator, I want to delete a doctor profile from the portal, so that the doctor no longer has access to the platform._

**Acceptance Criteria:**

1. After submission, the doctor profile is permanetely removed from the platform.
2. All active sessions and creditials for the doctor are revoked imediately.
3. All appointments of that doctor will also be removed.

**Priority:** High
**Story Points:** 2
**Notes:**

### **Run stored procedures:**
_As an administrator, I want to run a store procedure in the MySQL CLI to get the number of appointments per month and track usage statistics, so that I can monitor system performance and user engagement._

**Acceptance Criteria:**

1. The store procedure can be run from MySQL CLI without errors.
2. The store procedure returns the total number of appointments grouped by month.
3. The ouput format is clear and includes month names and appointment counts.

**Priority:** Medium
**Story Points:** 1
**Notes:**

## Patient user stories

### **List doctors:**
_As a patient, I want to access a list with all the doctors on the platform, so that I can choose the best option for my case._

**Acceptance Criteria:**

1. The patient has access to the list of doctors on the platform.
2. The patient can filter the list based on spefic parameters.
3. To access the list the patient does not need to be logged in.

**Priority:** High
**Story Points:** 1
**Notes:**

### **Patient Sinup:**
_As a patient, I want to Sign up, so that I can create an account and later book appointments.

**Acceptance Criteria:**

1. The patient can create an account using his email and password.
2. The system prevents duplicate email registration.
3. The system validates email format and enforces password complexity rules.

**Priority:** High
**Story Points:** 2
**Notes:**

### **Patient login:**
_As a patient, I want to login, so that I can manage my bookings._

**Acceptance Criteria:**

1. After a sucessful login, the patient should be redirected to the dashboard
2. The patient can log in using valid email and password credentials.
3. The system should show a message when the user enter the wrong credentials.

**Priority:** High
**Story Points:** 2
**Notes:**
    - HTTPs should be enforced.

### **Patient logout:**
_As an Patient, I want to logout of the platform, so that my session ends and my credentials cannot be misused.

**Acceptance Criteria:**

1. After the user logout, the platform revokes and deletes the user session
2. After the succesful logout the user will be redirected to the front page.
3. User Cannot access any protected page after logout.
4. Logout works from any page.

**Priority:** High
**Story Points:** 1
**Notes:**
- Logout should work even if the session is expired.

### **Book appointment:**
_As a patient, I want to book appointments, so that I can consult with a doctor._

**Acceptance Criteria:**

1. After the user is authenticated, the user can book an 1 hour appointment with any available doctor.
2. The system should allow to user to select the date and time slot for the appointment.
3. The system only shows the available doctors for the selected date and time.
4. The system prevents double booking and confirms the appointment to the patient.

**Priority:** High
**Story Points:** 3
**Notes:**

## Doctor user stories

### **Doctor login:**
_As a Doctor, I want to login, so that I can manage my appointments._

**Acceptance Criteria:**

1. After a sucessful login, the doctor should be redirected to the dashboard.
2. The doctor can log in using the valid credentials generated by the administrator.
3. The system should show a message when the user enter the wrong credentials.

**Priority:** High
**Story Points:** 2
**Notes:**
    - HTTPs should be enforced.

### **Doctor logout:**
_As an Doctor, I want to logout of the platform, so that my session ends and my credentials cannot be misused.

**Acceptance Criteria:**

1. After the user logout, the platform revokes and deletes the user session
2. After the succesful logout the user will be redirected to the front page.
3. User Cannot access any protected page after logout.
4. Logout works from any page.

**Priority:** High
**Story Points:** 1
**Notes:**
- Logout should work even if the session is expired.

### **View appointments calendar:**
_As a doctor, I want to view my appointments calendar, so that I can stay organized and provide the best service for my patients._

**Acceptance Criteria:**

1. After the doctor logs in, the doctor can access a calendar view of all scheduled appointments.
2. The default sorting order is ascending by date/time.
3. The system allows the doctor access to the information of the patient that made the appointment.

**Priority:** High
**Story Points:** 3
**Notes:**

### **Mark unavailability:**
_As a doctor, I want to mark the dates/times when I cannot consult, so that patient know when to find me._

**Acceptance Criteria:**

1. After sucessfull login, the doctor can select the dates and times when he'll be unavailable.
2. The system should allow partial unavailability.
3. The system should prevent patients from booking during marked unavailable times.

**Priority:** High
**Story Points:** 3
**Notes:**

### **Update profile:**
_As a Doctor, I want to update my profile, so that the patients always have the latest information about me._

**Acceptance Criteria:**

1. After successful login the doctor can manage his profile information.
2. The doctor can change his specialization and contact information.
3. Updated information is immediatelly available to patients.

**Priority:** Medium
**Story Points:** 2
**Notes:**


