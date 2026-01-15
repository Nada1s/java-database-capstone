import { getAllAppointments } from "./services/appointmentRecordService.js";
import { createPatientRow } from "./components/patientRows.js";

const tableBody = document.getElementById("patientTableBody");

const today = new Date().toISOString().split("T")[0];
let selectedDate = today;

const token = localStorage.getItem("token");
let patientName = null;

async function loadAppointments() {
  try {
    if (!tableBody) return;

    const appointments = await getAllAppointments(
      selectedDate,
      patientName,
      token
    );

    tableBody.innerHTML = "";

    if (!appointments || appointments.length === 0) {
      const row = document.createElement("tr");
      const cell = document.createElement("td");
      cell.colSpan = 4;
      cell.textContent = "No Appointments found for today.";
      row.appendChild(cell);
      tableBody.appendChild(row);
      return;
    }

    appointments.forEach((appointment) => {
      const patient = {
        id: appointment.patientId,
        name: appointment.patientName,
        phone: appointment.patientPhone,
        email: appointment.patientEmail,
      };

      const row = createPatientRow(patient, appointment);
      tableBody.appendChild(row);
    });
  } catch (error) {
    console.error("Error loading appointments:", error);

    if (!tableBody) return;

    tableBody.innerHTML = "";
    const row = document.createElement("tr");
    const cell = document.createElement("td");
    cell.colSpan = 4;
    cell.textContent = "Error loading appointments. Try again later.";
    row.appendChild(cell);
    tableBody.appendChild(row);
  }
}

document.addEventListener("DOMContentLoaded", () => {
  const searchBar = document.getElementById("searchBar");
  const todayButton = document.getElementById("todayButton");
  const datePicker = document.getElementById("datePicker");

  if (datePicker) {
    datePicker.value = today;
  }

  if (searchBar) {
    searchBar.addEventListener("input", () => {
      const value = searchBar.value.trim();
      patientName = value !== "" ? value : null;
      loadAppointments();
    });
  }

  if (todayButton) {
    todayButton.addEventListener("click", () => {
      selectedDate = today;
      if (datePicker) {
        datePicker.value = today;
      }
      loadAppointments();
    });
  }

  if (datePicker) {
    datePicker.addEventListener("change", () => {
      selectedDate = datePicker.value;
      loadAppointments();
    });
  }

  if (typeof renderContent === "function") {
    renderContent();
  }

  loadAppointments();
});
