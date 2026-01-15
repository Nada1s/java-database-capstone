import { openModal } from "./components/modals.js";
import { createDoctorCard } from "./components/doctorCard.js";
import { getDoctors, filterDoctors, saveDoctor } from "./services/doctorServices.js";

function renderDoctorCards(doctors) {
  const contentDiv = document.getElementById("content");
  if (!contentDiv) return;

  contentDiv.innerHTML = "";

  if (!doctors || doctors.length === 0) {
    const msg = document.createElement("p");
    msg.textContent = "No doctors found.";
    contentDiv.appendChild(msg);
    return;
  }

  doctors.forEach((doctor) => {
    const card = createDoctorCard(doctor);
    contentDiv.appendChild(card);
  });
}

async function loadDoctorCards() {
  try {
    const doctors = await getDoctors();
    renderDoctorCards(doctors);
  } catch (error) {
    console.error("Error loading doctors:", error);
    alert("Failed to load doctors. Please try again later.");
  }
}

async function filterDoctorsOnChange() {
  try {
    const nameInput = document.getElementById("searchBar");
    const timeSelect = document.getElementById("filterTime");
    const specialtySelect = document.getElementById("filterSpecialty");

    const name = nameInput ? nameInput.value.trim() : "";
    const time = timeSelect ? timeSelect.value : "";
    const specialty = specialtySelect ? specialtySelect.value : "";

    const nameFilter = name || null;
    const timeFilter = time || null;
    const specialtyFilter = specialty || null;

    const doctors = await filterDoctors(
      nameFilter,
      timeFilter,
      specialtyFilter
    );

    const contentDiv = document.getElementById("content");
    if (!contentDiv) return;

    contentDiv.innerHTML = "";

    if (doctors && doctors.length > 0) {
      renderDoctorCards(doctors);
    } else {
      const msg = document.createElement("p");
      msg.textContent = "No doctors found with the given filters.";
      contentDiv.appendChild(msg);
    }
  } catch (error) {
    console.error("Error filtering doctors:", error);
    alert("An error occurred while filtering doctors. Please try again.");
  }
}

async function adminAddDoctor(event) {
  if (event && event.preventDefault) {
    event.preventDefault();
  }

  try {
    const nameInput = document.getElementById("doctorName");
    const emailInput = document.getElementById("doctorEmail");
    const phoneInput = document.getElementById("doctorPhone");
    const passwordInput = document.getElementById("doctorPassword");
    const specialtyInput = document.getElementById("doctorSpecialty");

    const name = nameInput ? nameInput.value.trim() : "";
    const email = emailInput ? emailInput.value.trim() : "";
    const phone = phoneInput ? phoneInput.value.trim() : "";
    const password = passwordInput ? passwordInput.value.trim() : "";
    const specialty = specialtyInput ? specialtyInput.value.trim() : "";

    const availabilityCheckboxes = document.querySelectorAll(
      ".availability-checkbox"
    );
    const availableTimes = Array.from(availabilityCheckboxes)
      .filter((cb) => cb.checked)
      .map((cb) => cb.value);

    if (!name || !email || !phone || !password || !specialty) {
      alert("Please fill in all required fields.");
      return;
    }

    const token = localStorage.getItem("token");
    if (!token) {
      alert("You are not authenticated. Please log in as admin again.");
      return;
    }

    const doctor = {
      name,
      email,
      phone,
      password,
      specialty,
      availableTimes,
    };

    const savedDoctor = await saveDoctor(doctor, token);

    if (savedDoctor) {
      alert("Doctor added successfully!");

      if (typeof closeModal === "function") {
        closeModal();
      } else {
        const modal = document.getElementById("modal");
        if (modal) {
          modal.setAttribute("hidden", "true");
        }
      }

      await loadDoctorCards();
    } else {
      alert("Failed to save doctor. Please try again.");
    }
  } catch (error) {
    console.error("Error adding doctor:", error);
    alert("An error occurred while adding the doctor. Please try again.");
  }
}

window.adminAddDoctor = adminAddDoctor;

document.addEventListener("DOMContentLoaded", () => {
  const addDoctorBtn = document.getElementById("addDocBtn");
  if (addDoctorBtn) {
    addDoctorBtn.addEventListener("click", () => {
      openModal("addDoctor");
    });
  }

  loadDoctorCards();

  const searchBar = document.getElementById("searchBar");
  const filterTime = document.getElementById("filterTime");
  const filterSpecialty = document.getElementById("filterSpecialty");

  if (searchBar) {
    searchBar.addEventListener("input", filterDoctorsOnChange);
  }
  if (filterTime) {
    filterTime.addEventListener("change", filterDoctorsOnChange);
  }
  if (filterSpecialty) {
    filterSpecialty.addEventListener("change", filterDoctorsOnChange);
  }
});
