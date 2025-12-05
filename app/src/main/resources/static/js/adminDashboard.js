 import { openModal } from "./components/modals";
 import { getDoctors, filterDoctors, saveDoctor } from "./services/doctorServices";
 import { createDoctorCard }  from "./components/doctorCard";
 
async function loadDoctorCards(){
  /*Purpose: Fetch all doctors and display them as cards
    Call getDoctors() from the service layer
    Clear the current content area
    For each doctor returned:
    - Create a doctor card using createDoctorCard()
    - Append it to the content div

    Handle any fetch errors by logging them
    Attach 'input' and 'change' event listeners to the search bar and filter dropdowns
  On any input change, call filterDoctorsOnChange()
*/

  renderDoctorCards(await getDoctors());
  
  document.getElementById("searchBar").addEventListener("input", filterDoctorsOnChange);
  document.getElementById("filterTime").addEventListener("change", filterDoctorsOnChange);
  document.getElementById("filterSpecialty").addEventListener("change", filterDoctorsOnChange);c
} 

async function filterDoctorsOnChange(evt){
  let name=time=specialty=null;

  switch(evt.target.id){
    case "searchBar'":
     name = evt.target.value;
     break;
    case "filterTime":
      time=evt.target.value;
      break;
    case "filterSpecialty":
      specialty = evt.target.value;
      break;
  }

  let result = await filterDoctors(name, time, specialty);

  renderDoctorCards(result.doctors,"No doctors found with the given filters.");

  /*Purpose: Filter doctors based on name, available time, and specialty

    Read values from the search bar and filters
    Normalize empty values to null
    Call filterDoctors(name, time, specialty) from the service

    If doctors are found:
    - Render them using createDoctorCard()
    If no doctors match the filter:
    - Show a message: "No doctors found with the given filters."

    Catch and display any errors with an alert
*/
}

function renderDoctorCards(doctors, emptyListMessage="There are no doctors available!"){
  let content = document.getElementById('content');
  content.innerHTML = "";

  if(doctors.length == 0) content.innerHTML="<p>"+emptyListMessage+"</p>";
  else doctors.map(doctor => content.appendChild(createDoctorCard(doctor)));
 }

function adminAddDoctor(){
/*
Purpose: Collect form data and add a new doctor to the system

    Collect input values from the modal form
    - Includes name, email, phone, password, specialty, and available times

    Retrieve the authentication token from localStorage
    - If no token is found, show an alert and stop execution

    Build a doctor object with the form values

    Call saveDoctor(doctor, token) from the service

    If save is successful:
    - Show a success message
    - Close the modal and reload the page

    If saving fails, show an error message
*/
}

window.addEventListener("DOMContentLoaded",() =>{
  document.getElementById("addDocBTN").addEventListener("click", () => openModal('addDoctor'));
  loadDoctorCards();
});