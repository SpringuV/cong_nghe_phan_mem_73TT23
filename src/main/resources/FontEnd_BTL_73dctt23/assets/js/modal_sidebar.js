
// Function to open the modal
function openModalCertificate(mode, data = null) {
  const modal = document.getElementById("myModal");
  const title = modal.querySelector("h2");

  if (mode === "create") {
    title.textContent = "Thêm mới chứng chỉ";
    document.getElementById("certificate-name").value = "";
    document.getElementById("certificate-type").value = "Ngoại ngữ";
    document.getElementById("issue-date").value = "";
    document.getElementById("student_id").value = "";
    document.getElementById("description").value = "";
    document.getElementById("certificate-id").value = "0"; // Đặt ID mặc định là 0
  } else if (mode === "edit" && data) {
    title.textContent = "Sửa chứng chỉ";
    document.getElementById("certificate-name").value = data.nameCertificate;
    document.getElementById("certificate-type").value = data.certificateType;
    document.getElementById("issue-date").value = formatDateToYMD(data.issueDate);
    document.getElementById("student_id").value = data.studentId;
    document.getElementById("description").value = data.description;
    document.getElementById("certificate-id").value = data.id;
  }

  modal.style.display = "block";
}

// Function to close the modal
function closeModal() {
  const modal = document.getElementById("myModal");
  modal.style.display = "none";
}
// end certificate modal

// start modal student
function openModalStudent(mode, student = null) {
  const modal = document.getElementById("studentModal");
  const title = document.getElementById("modal-title");

  if (mode === "create") {
    title.textContent = "Thêm sinh viên mới";
    document.getElementById("student-id").readOnly = false;
    document.getElementById("student-form").reset();
  } else if (mode === "edit" && student) {
    // đổ dữ liệu ra modal sửa
    title.textContent = "Sửa thông tin sinh viên";
    document.getElementById("student-id").value = student.studentId;
    document.getElementById("student-id").readOnly = true;
    document.getElementById("student-last-name").value = student.lastName;
    document.getElementById("student-first-name").value = student.firstName;
    document.getElementById("student-department").value = student.departmentName || "none";
    document.getElementById("student-year-admission").value = student.yearAdmission || "";
    document.getElementById("student-year-graduation").value = student.yearGraduation || "";
    document.getElementById("student-graduation-status").value = student.status || "none";
    document.getElementById("student-email").value = student.email || "";
    document.getElementById("dob").value = student.dob || "";
  }

  modal.style.display = "block";
}

// Sau khi đóng modal, đặt lại tất cả các trường về giá trị mặc định.
function closeStudentModal() {
  const modal = document.getElementById("studentModal");
  modal.style.display = "none";
  document.getElementById("student-form").reset();
  document.getElementById("student-id").readOnly = false;

}

function openDetailModal(student) {
  document.getElementById("detail-student-id").textContent = student.studentId || "Chưa có";
  document.getElementById("detail-student-name").textContent = `${student.lastName || ""} ${student.firstName || ""}`;
  document.getElementById("detail-student-department").textContent = student.departmentName || "Chưa rõ";
  document.getElementById("detail-student-status").textContent = student.graduationStatus || "Chưa rõ";
  document.getElementById("detail-student-graduation").textContent = student.yearGraduation || "Chưa rõ";
  document.getElementById("detail-student-admission").textContent = student.yearAdmission || "Chưa rõ";
  document.getElementById("detail-dob").textContent = student.dob || "Chưa rõ";
  document.getElementById("detail-email").textContent = student.email || "Chưa rõ";

  document.getElementById("detailModal").style.display = "block";
}

function editStudentFromDetail() {
  const student = {
    studentId: document.getElementById("detail-student-id").textContent || "",
    lastName: document.getElementById("detail-student-name").textContent.split(" ").slice(0, -1).join(" ") || "",
    firstName: document.getElementById("detail-student-name").textContent.split(" ").pop() || "",
    departmentName: document.getElementById("detail-student-department").textContent.trim() || "",
    status: document.getElementById("detail-student-status").textContent || "",
    yearAdmission: document.getElementById("detail-student-admission").textContent || "",
    yearGraduation: document.getElementById("detail-student-graduation").textContent || "",
    dob: formatDateToYMD(document.getElementById("detail-dob").textContent || ""),
    email: document.getElementById("detail-email").textContent || "",
  };

  closeDetailModal(); // Đóng modal chi tiết
  openModalStudent("edit", student); // Mở modal sửa
}

function formatDateToYMD(dateString) {
  const [year, month, day] = dateString.split("-");
  const fullYear = year.length === 2 ? `20${year}` : year; // Thêm "20" nếu năm chỉ có 2 chữ số
  return `${fullYear}-${month}-${day}`;
}



function closeDetailModal() {
  document.getElementById("detailModal").style.display = "none";
}

function openEditModal() {
  const studentId = document.getElementById("detail-student-id").textContent;
  const studentName = document.getElementById("detail-student-name").textContent.split(" ");
  const lastName = studentName.slice(0, -1).join(" ");
  const firstName = studentName[studentName.length - 1];

  document.getElementById("edit-student-id").value = studentId;
  document.getElementById("edit-student-last-name").value = lastName;
  document.getElementById("edit-student-first-name").value = firstName;
  document.getElementById("edit-student-department").value = document.getElementById("detail-student-department").textContent;
  document.getElementById("edit-student-status").value = document.getElementById("detail-student-status").textContent;
  document.getElementById("edit-email").value = document.getElementById("detail-email").textContent;
  document.getElementById("edit-dob").value = document.getElementById("detail-dob").textContent;

  closeDetailModal(); // Đóng modal chi tiết
  document.getElementById("editModal").style.display = "block";
}

function closeEditModal() {
  document.getElementById("editModal").style.display = "none";
}
// end student modal


// DIPLOMA

function openDegreeModal(mode, data = null) {
  console.log("Opening Degree Modal");
  const modal = document.getElementById("myModal");
  const title = document.getElementById("degree-modal-title");
  console.log("Mode:", mode, "Data:", data);

  if (mode === "create") {
    title.textContent = "Thêm mới Văn bằng";
    document.getElementById("degree-form").reset(); // Xóa dữ liệu cũ
    document.getElementById("degree-id").value = "0"; // Đặt ID = 0 cho chế độ tạo mới
  } else if (mode === "edit" && data) {
    title.textContent = "Cập nhật Văn bằng";
    document.getElementById("degree-id").value = degree.diplomaId || "0";
    document.getElementById("degree-major").value = degree.major || "none";
    document.getElementById("degree-type").value = degree.degreeType || "none";
    document.getElementById("issue-date").value = degree.issueDate || "";
    document.getElementById("student-id").value = degree.studentId || "";
  }
  console.log("Modal Content Set");
  modal.style.display = "block";
}

function openEditModal(degree) {
  document.getElementById("degree-id").value = degree.id || "0";
  document.getElementById("degree-major").value = degree.major || "";
  document.getElementById("degree-type").value = degree.degreeType || "none";
  document.getElementById("issue-date").value = degree.issueDate || "";
  document.getElementById("student-id").value = degree.studentId || "";

  document.getElementById("degree-modal-title").textContent = "Sửa Văn bằng";
  document.getElementById("degreeModal").style.display = "block";
}


// END DIPLOMA


// Function to toggle sidebar (collapse/expand)
function toggleSidebar() {
  const sidebar = document.getElementById("sidebar");
  sidebar.classList.toggle("collapsed");
}

// Close the modal if the user clicks outside of it
window.onclick = function (event) {
  const modals = document.querySelectorAll(".modal");
  modals.forEach(modal => {
    if (event.target === modal && modal.style.display === "block") {
      modal.style.display = "none";
    }
  });
};

// Tải sidebar.html và thêm vào #sidebar-container
fetch('/pages/fragment/sidebar.html')
  .then(response => response.text())
  .then(html => {
    document.getElementById('sidebar-container').innerHTML = html;
  })
  .catch(err => console.error('Lỗi khi tải sidebar:', err));
