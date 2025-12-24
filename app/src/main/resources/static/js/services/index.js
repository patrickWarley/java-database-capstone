
import { openModal } from "../components/modals.js";
import { API_BASE_URL } from "../config/config.js";
import { patientLogin } from "./patientServices.js";

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
/*

  Use the window.onload event to ensure DOM elements are available after page load
  Inside this function:
    - Select the "adminLogin" and "doctorLogin" buttons using getElementById
    - If the admin login button exists:
        - Add a click event listener that calls openModal('adminLogin') to show the admin login modal
    - If the doctor login button exists:
        - Add a click event listener that calls openModal('doctorLogin') to show the doctor login modal

  Define a function named adminLoginHandler on the global window object
  This function will be triggered when the admin submits their login credentials

  Step 1: Get the entered username and password from the input fields
  Step 2: Create an admin object with these credentials

  Step 3: Use fetch() to send a POST request to the ADMIN_API endpoint
    - Set method to POST
    - Add headers with 'Content-Type: application/json'
    - Convert the admin object to JSON and send in the body

  Step 4: If the response is successful:
    - Parse the JSON response to get the token
    - Store the token in localStorage
    - Call selectRole('admin') to proceed with admin-specific behavior

  Step 5: If login fails or credentials are invalid:
    - Show an alert with an error message

  Step 6: Wrap everything in a try-catch to handle network or server errors
    - Show a generic error message if something goes wrong


  Define a function named doctorLoginHandler on the global window object
  This function will be triggered when a doctor submits their login credentials

  Step 1: Get the entered email and password from the input fields
  Step 2: Create a doctor object with these credentials

  Step 3: Use fetch() to send a POST request to the DOCTOR_API endpoint
    - Include headers and request body similar to admin login

  Step 4: If login is successful:
    - Parse the JSON response to get the token
    - Store the token in localStorage
    - Call selectRole('doctor') to proceed with doctor-specific behavior

  Step 5: If login fails:
    - Show an alert for invalid credentials

  Step 6: Wrap in a try-catch block to handle errors gracefully
    - Log the error to the console
    - Show a generic error message
*/
