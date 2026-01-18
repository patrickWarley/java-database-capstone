
import { getAllAppointments } from "./services/appointmentRecordService.js";
import { createPatientRow } from "./components/patientRows.js";

let patientName = null;
let selectedDate = null;
const token = localStorage.getItem('token');
let datePicker, todayButton, searchBar, tableBody;


async function loadAppointments() {

  try {
    let appointments = (await getAllAppointments(selectedDate, patientName, token)).appointments;


    tableBody.innerHTML = '';

    if (appointments.length == 0) {
      let aux = document.createElement('tr');
      aux.classList.add("noPatientRecord");
      aux.innerHTML = "<td colspan='5'>No Appointments found.</td>";
      return tableBody.appendChild(aux);
    }

    appointments.forEach(appointment => {
      let patient = appointment.patient;
      tableBody.appendChild(createPatientRow(patient, appointment.id, appointment.doctor.id));
    });

  } catch (error) {
    console.log(error)
    alert("Error loading appointments!. Try again later.");
  }
}

window.addEventListener('DOMContentLoaded', () => {
  datePicker = document.getElementById("datePicker");
  todayButton = document.getElementById('todayButton');
  searchBar = document.getElementById('searchBar');
  tableBody = document.getElementById('patientTableBody');

  searchBar.addEventListener('change', (evt) => {
    let value = evt.target.value.trim();
    patientName = value == "" ? null : value;
    loadAppointments();
  });


  todayButton.addEventListener('click', () => {
    let today = new Date();
    selectedDate = `${today.getFullYear()}-${String(today.getMonth() + 1).padStart(2, '0')}-${String(today.getDate()).padStart(2, '0')}`;
    datePicker.value = selectedDate;
    loadAppointments();
  });

  datePicker.addEventListener('change', (evt) => {
    selectedDate = evt.target.value;
    loadAppointments();
  });

  loadAppointments();
});