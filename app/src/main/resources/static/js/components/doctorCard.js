import { showBookingOverlay } from "../loggedPatient.js";
import { deleteDoctor } from "../services/doctorServices.js";
import { getPatientData } from "../services/patientServices.js";


export function createDoctorCard(doctor) {
  let doctorCardDiv = document.createElement('div');
  doctorCardDiv.classList.add("doctor-card");

  let doctorCardInfo = document.createElement('div');
  doctorCardInfo.classList.add("doctor-info");

  let cardContent = `
    <h3>${doctor.name}</h3>
    <p>${doctor.specialty}</p>
    <p>${doctor.email}</p>
    <ul class="appointments-list">
      ${doctor.availableTimes.reduce((curr, next) => curr += `<li>${next}</li>`,"")}
    </ul>
  `;

  doctorCardInfo.innerHTML = cardContent;

  let role = localStorage.getItem("role");
  let cardActionContainer = document.createElement('div');
  cardActionContainer.classList.add("card-actions");

  if (role == "admin") {
    let deleteButton = document.createElement('button');
    deleteButton.textContent = "Delete"
    deleteButton.addEventListener('click', () => {
      let token = localStorage.getItem('token');

      let response = deleteDoctor(doctor.id, token);

      if (response.success) {
        alert('Doctor removed successfuly');
        document.removeChild(doctorCardDiv);
      } else alert("Something went wrong. Please try again later!");

    });

    cardActionContainer.appendChild(deleteButton);

  } else if (role == "loggedPatient") {
    let bookNowButton = document.createElement("button");
    bookNowButton.addEventListener("click",async () =>{
      let token = localStorage.getItem("token");

      if(!token){
        alert("Session expired or invalid login. Please log in again.");
        window.location.href = "/";
      }

      let response = await getPatientData(token);

      if(!response) alert("An error ocurred while fetching patient data. Please try again!");
      
      showBookingOverlay(bookNowButton, doctor, response)
    });

  } else if (role == "patient") {
    let bookNowButton = document.createElement("button");
    bookNowButton.addEventListener("click", () => {
      alert("You need to log in to book an appointment!");
    });

    cardActionContainer.appendChild(bookNowButton);

  } else {
    console.log("I don't know what you trying to do");
  }

  doctorCardDiv.appendChild(doctorCardInfo);
  doctorCardDiv.appendChild(cardActionContainer); 

  return doctorCardDiv;
}

/*
Import the overlay function for booking appointments from loggedPatient.js

  Import the deleteDoctor API function to remove doctors (admin role) from docotrServices.js

  Import function to fetch patient details (used during booking) from patientServices.js

  Function to create and return a DOM element for a single doctor card
    Create the main container for the doctor card
    Retrieve the current user role from localStorage
    Create a div to hold doctor information
    Create and set the doctor’s name
    Create and set the doctor's specialization
    Create and set the doctor's email
    Create and list available appointment times
    Append all info elements to the doctor info container
    Create a container for card action buttons
    === ADMIN ROLE ACTIONS ===
      Create a delete button
      Add click handler for delete button
     Get the admin token from localStorage
        Call API to delete the doctor
        Show result and remove card if successful
      Add delete button to actions container
   
    === PATIENT (NOT LOGGED-IN) ROLE ACTIONS ===
      Create a book now button
      Alert patient to log in before booking
      Add button to actions container
  
    === LOGGED-IN PATIENT ROLE ACTIONS === 
      Create a book now button
      Handle booking logic for logged-in patient   
        Redirect if token not available
        Fetch patient data with token
        Show booking overlay UI with doctor and patient info
      Add button to actions container
   
  Append doctor info and action buttons to the car
  Return the complete doctor card element
*/
