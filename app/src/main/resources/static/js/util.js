// util.js
function setRole(role) {
  localStorage.setItem("role", role);
}

function getRole() {
  return localStorage.getItem("role");
}

function clearRole() {
  localStorage.removeItem("role");
}

//receive a string representing the time and returns it 
function formatTimeHourMinutes(time) {
  let matchHourMinutes = /[0-1]{1}[0-9]{1}\:[0-5]{1}[0-9]{1}/;

  return time.match(matchHourMinutes)[0];
}