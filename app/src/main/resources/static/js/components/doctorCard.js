import { showBookingOverlay } from "../loggedPatient.js";
import { deleteDoctor } from "../services/doctorServices.js";
import { getPatientData } from "../services/patientServices.js";

export function createDoctorCard(doctor) {
  const card = document.createElement("div");
  card.classList.add("doctor-card");

  const role = localStorage.getItem("userRole");

  const infoDiv = document.createElement("div");
  infoDiv.classList.add("doctor-info");

  const nameEl = document.createElement("h3");
  nameEl.textContent = doctor.name || "Unknown Doctor";

  const specializationEl = document.createElement("p");
  specializationEl.textContent = `Specialty: ${doctor.specialty || "N/A"}`;

  const emailEl = document.createElement("p");
  emailEl.textContent = `Email: ${doctor.email || "Not provided"}`;

  const availabilityEl = document.createElement("p");
  let availabilityText = "Not provided";
  if (Array.isArray(doctor.availableTimes)) {
    availabilityText = doctor.availableTimes.join(", ");
  } else if (typeof doctor.availableTimes === "string") {
    availabilityText = doctor.availableTimes;
  }
  availabilityEl.textContent = `Available: ${availabilityText}`;

  infoDiv.appendChild(nameEl);
  infoDiv.appendChild(specializationEl);
  infoDiv.appendChild(emailEl);
  infoDiv.appendChild(availabilityEl);

  const actionsDiv = document.createElement("div");
  actionsDiv.classList.add("card-actions");

  if (role === "admin") {
    const removeBtn = document.createElement("button");
    removeBtn.textContent = "Delete";
    removeBtn.classList.add("adminBtn");

    removeBtn.addEventListener("click", async () => {
      const confirmDelete = window.confirm(
        `Are you sure you want to delete Dr. ${doctor.name || ""}?`
      );
      if (!confirmDelete) return;

      const token = localStorage.getItem("token");
      if (!token) {
        alert("Not authorized. Please log in again.");
        window.location.href = "/";
        return;
      }

      try {
        const success = await deleteDoctor(doctor.id, token);
        if (success) {
          alert("Doctor deleted successfully.");
          card.remove();
        } else {
          alert("Failed to delete doctor. Please try again.");
        }
      } catch (err) {
        console.error("Error deleting doctor:", err);
        alert("An error occurred while deleting the doctor.");
      }
    });

    actionsDiv.appendChild(removeBtn);

  } else if (role === "patient") {
    const bookNow = document.createElement("button");
    bookNow.textContent = "Book Now";

    bookNow.addEventListener("click", () => {
      alert("Please log in to book an appointment.");
    });

    actionsDiv.appendChild(bookNow);

  } else if (role === "loggedPatient") {
    const bookNow = document.createElement("button");
    bookNow.textContent = "Book Now";

    bookNow.addEventListener("click", async (e) => {
      const token = localStorage.getItem("token");

      if (!token) {
        alert("Session expired. Please log in again.");
        window.location.href = "/";
        return;
      }

      try {
        const patientData = await getPatientData(token);
        showBookingOverlay(e, doctor, patientData);
      } catch (err) {
        console.error("Error fetching patient data:", err);
        alert("Unable to load your data for booking. Please try again.");
      }
    });

    actionsDiv.appendChild(bookNow);
  }

  card.appendChild(infoDiv);
  card.appendChild(actionsDiv);

  return card;
}
