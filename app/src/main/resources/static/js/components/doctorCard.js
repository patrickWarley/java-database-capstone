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
      ${doctor.availableTimes.reduce((curr, next) => curr += `<li>${next}</li>`, "")}
    </ul>
  `;

  doctorCardInfo.innerHTML = cardContent;

  let role = getRole();
  let cardActionContainer = document.createElement('div');
  cardActionContainer.classList.add("card-actions");

  if (role == "admin") cardActionContainer.appendChild(createDeleteButton());
  else if (role == "loggedPatient") cardActionContainer.appendChild(createBookNowButton(doctor));
  else if (role == "patient") cardActionContainer.appendChild(createLoginToBookNowButton())
  else console.log("I don't know what you trying to do");

  doctorCardDiv.appendChild(doctorCardInfo);
  doctorCardDiv.appendChild(cardActionContainer);

  return doctorCardDiv;
}

function createButton(textContent, eventListener) {
  let button = document.createElement('button');
  button.textContent = textContent;
  button.addEventListener("click", eventListener);

  return button;
}

function createDeleteButton() {
  return createButton("Delete", async () => {

    if (confirm("Are you sure you want to delete the doctor?")) {
      let response = await deleteDoctor(doctor.id);
      if (response.success) {
        alert('Doctor removed successfuly');

        doctorCardDiv.parentElement.removeChild(doctorCardDiv);

      } else alert("Something went wrong. Please try again later!");
    }
  });
}

function createBookNowButton(doctor) {
  return createButton("Book appointment", async (e) => {
    let token = localStorage.getItem("token");

    if (!token) {
      alert("Session expired or invalid login. Please log in again.");
      window.location.href = "/";
    }

    let response = await getPatientData(token);

    if (!response) alert("An error ocurred while fetching patient data. Please try again!");

    showBookingOverlay(e, doctor, response)
  });
}

function createLoginToBookNowButton() {
  return createButton("Book appointment", () => {
    alert("You need to log in to book an appointment!");
  });
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
