
import { openModal } from "../components/modals.js";
import { API_BASE_URL } from "../config/config.js";
import { saveDoctor } from "./doctorServices.js";
import { patientLogin } from "./patientServices.js";
import { createAlert } from "../components/alert.js";
const DOCTOR_API = API_BASE_URL + "/doctor/login";
const ADMIN_API = API_BASE_URL + "/admin";

window.addEventListener("load", () => {
  let adminLogin = document.getElementById("adminLogin");
  let doctorLogin = document.getElementById("doctorLogin");
  let patientLogin = document.getElementById("patientLogin");
  
  if (adminLogin) {
    adminLogin.addEventListener('click', () => {
      openModal('adminLogin');
    });
  }

  if (doctorLogin) {
    doctorLogin.addEventListener('click', () => {
      openModal('doctorLogin');
    });
  }

  if (patientLogin)
    patientLogin.addEventListener('click', () => {
      openModal('patientLogin');
    })

});

export async function adminLoginHandler(){
  let username = document.getElementById('username').value;
  let password = document.getElementById('password').value;

  let admin = {username, password}
  
  try {
      let response = await fetch(ADMIN_API, {
        method:"POST",
        headers:{"Content-type":"application/json"},
        body:JSON.stringify(admin)
      });

    let json =await response.json();
    console.log(json);
    console.log(response.status)
    if (response.status == 200) { 
      localStorage.setItem("token", json.token);
      localStorage.setItem("role", "admin");
      selectRole('admin');
    }

    alert(json.message);
  } catch (error) {
    console.log(error);
    alert("An error ocurred! Please try again later!");
  }
}

export async function doctorLoginHandler(){
  let email = document.getElementById('username');
  let password = document.getElementById('password');

  let doctor = {email, password}
  try{
      let response = await fetch(DOCTOR_API, {
        method:"POST",
        headers:{"Content-type":"application/json"},
        body:JSON.stringify(doctor)
        });

      response = response.JSON();

      //What the backend returns?
      if(response.error) return alert("Error when trying to login. Please review your information and try again!");

      localStorage.setItem("token", response.token);

      selectRole('doctor');

  }catch(error){
    alert("An error ocurred! Please try again later!");
  }
}

export async function patientLoginHandler() { 
  let email = document.getElementById('username');
  let password = document.getElementById('password');

  let patient = { password, email }
  
  try {
    let request = await patientLogin(patient);

    if (response.error) return alert('Error when trying to login. Please review your information and try again!');

    localStorage.setItem("token", reponse.token);

    selectRole('patient');
  } catch (error) {
    alert("An error ocurred! Please try again later!")
  }
}

export async function adminAddDoctor(evt) {
  evt.preventDefault();
  
  let name = document.getElementById("doctorName").value;
  let specialty = document.getElementById("specialization").value;
  let email = document.getElementById("doctorEmail").value;
  let password = document.getElementById("doctorPassword").value;
  let phone = document.getElementById("doctorPhone").value;
  let availabilityNodes = document.getElementsByName("availability");

  let availableTimes = Array.from(availabilityNodes).filter(check => check.checked).map(check => check.value);
  
  let result = await saveDoctor({ name, specialty, email, password, phone, availableTimes }, localStorage.getItem("token"));

  if (!result.success) return createAlert(document.getElementById("messageContainer"), result.message, "danger");

  alert(result.message);
  window.location.reload();
}