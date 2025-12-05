
import { API_BASE_URL } from "../config/config";

const DOCTOR_API = API_BASE_URL + "/doctor";
const DEFAULT_ERROR_MESSAGE = "An error ocurred. Please try again later!";

export async function getDoctors() {
  try {
    let response = await fetch(DOCTOR_API, { method: "GET", headers: { "Content-type": "application/json" } });
    let data = await response.json();

    if (response.ok) return data.doctors;

    console.log("An error ocurred. Please review your information and try again!");
    return [];
  } catch (error) {
    console.log(DEFAULT_ERROR_MESSAGE);
    return [];
  }
}

export async function deleteDoctor(id, token) {
  try {

    let response = await fetch(DOCTOR_API + `/${id}/${token}`, {
      method: "DELETE",
    });

    let data = await response.json();

    if (response.ok) return {success:true, message:"Success!! the doctor was deleted!"};

    console.log("An error ocurred when trying to delete the doctor. Try again!");
  } catch (error) {
    console.log(DEFAULT_ERROR_MESSAGE, error);
  }
}

export async function saveDoctor(doctor, token) {
  try {
    let response = await fetch(DOCTOR_API+`${token}`, {
      method:"POST",
      headers:{
        "Content-type":"application/json",
      },
      body:JSON.stringify(doctor)
    });

    let data = await response.json();

    if(response.ok) return {success:true, message:"Doctor saved with success!"};
    
    return {success:false, message:DEFAULT_ERROR_MESSAGE} ;

  } catch (error) {
    console.log(DEFAULT_ERROR_MESSAGE, error);
  }
}
export async function filterDoctors(name, time, specialty) {
  try {
   let response = await fetch(`${DOCTOR_API}?name=${name}&time=${time}&specialty=${specialty}`);

   let data = response.json();

   if(response.ok) return data;
    console.log(DEFAULT_ERROR_MESSAGE);
    return {doctors:[]};
  } catch (error) {
    console.log(DEFAULT_ERROR_MESSAGE, error);
    return {doctors:[]};
  }
}

/*
  Import the base API URL from the config file
  Define a constant DOCTOR_API to hold the full endpoint for doctor-related actions


  Function: getDoctors
  Purpose: Fetch the list of all doctors from the API

   Use fetch() to send a GET request to the DOCTOR_API endpoint
   Convert the response to JSON
   Return the 'doctors' array from the response
   If there's an error (e.g., network issue), log it and return an empty array


  Function: deleteDoctor
  Purpose: Delete a specific doctor using their ID and an authentication token

   Use fetch() with the DELETE method
    - The URL includes the doctor ID and token as path parameters
   Convert the response to JSON
   Return an object with:
    - success: true if deletion was successful
    - message: message from the server
   If an error occurs, log it and return a default failure response


  Function: saveDoctor
  Purpose: Save (create) a new doctor using a POST request

   Use fetch() with the POST method
    - URL includes the token in the path
    - Set headers to specify JSON content type
    - Convert the doctor object to JSON in the request body

   Parse the JSON response and return:
    - success: whether the request succeeded
    - message: from the server

   Catch and log errors
    - Return a failure response if an error occurs


  Function: filterDoctors
  Purpose: Fetch doctors based on filtering criteria (name, time, and specialty)

   Use fetch() with the GET method
    - Include the name, time, and specialty as URL path parameters
   Check if the response is OK
    - If yes, parse and return the doctor data
    - If no, log the error and return an object with an empty 'doctors' array

   Catch any other errors, alert the user, and return a default empty result
*/
