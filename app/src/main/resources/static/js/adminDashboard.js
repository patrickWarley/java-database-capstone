 import { openModal } from "./components/modals.js";
 import { getDoctors, filterDoctors, saveDoctor } from "./services/doctorServices.js";
 import { createDoctorCard }  from "./components/doctorCard.js";
 
let adminDebounceTimer = {
  id:null
}

async function loadDoctorCards(){
  
  renderDoctorCards(await getDoctors());
  
  document.getElementById("adminSearchBar").addEventListener("input", filterDoctorsOnChange);
  document.getElementById("adminFilterTime").addEventListener("change", filterDoctorsOnChange);
  document.getElementById("adminFilterspecialty").addEventListener("change", filterDoctorsOnChange);
} 

async function debounce(fn, timer) {
  if (timer.id != null) clearTimeout(timer.id);
  timer.id = setTimeout(() => fn(), 300);
 }

async function filterDoctorsOnChange(evt) {
   let name = document.getElementById("adminSearchBar").value;
   let time = document.getElementById("adminFilterTime").value;
   let specialty = document.getElementById("adminFilterspecialty").value;
    
  debounce(async () => {
    let result = await filterDoctors(name, time, specialty);
    renderDoctorCards(result.doctors, "No doctors found with the given filters.");
  }, adminDebounceTimer);

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

document.addEventListener("DOMContentLoaded",() =>{
  document.getElementById("addDocBtn").addEventListener("click", () => openModal('addDoctor'));
  loadDoctorCards();
});