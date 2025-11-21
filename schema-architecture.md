This is a Spring Boot application that uses both REST and Thymeleaf controllers. The thymeleaf controllers are linked to the admin and doctor dashboards, while REST controllers handle appointments, patients dashboard, and patient records.All controllers route requests through a common service layer.
The Application will have two databases:

- A relational database (**MySQL**) to store doctor, ptient, appointment, and admin information.
- A NOSQl database(MongoDB) to store prescriptions.

### Flow of data and control

1- The user access one of the interfaces

- Admin, doctor, or patient dashboard.
- Appointments
- Patient record

2- A request is made to a Thymeleaf or REST controller

3- The service layer is called by the controller

4- The service requests the information it needs from the repository layer

5- The repository layer access one the databases depending on what the service needs.

6- The repository then returns the data to the service

7- The service process the data and returns it to the controller

8- The controller format the result and send it to the user.
