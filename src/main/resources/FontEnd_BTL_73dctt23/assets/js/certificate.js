document.addEventListener('DOMContentLoaded', () => {
    async function deleteCertificate(event) {
        checkSession(); // Đảm bảo phiên làm việc còn hiệu lực
        const btn = event.target;
        const certificateId = btn.getAttribute("data-id");
        const token = localStorage.getItem('token');
        if (!confirm("Bạn chắc chắn muốn xóa chứng chỉ này không ?")) {
            return;
        }
        try {
            const response = await fetch(`http://localhost:8080/certificates/${certificateId}`, {
                method: "DELETE",
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });
            if (response.ok) {
                alert("Xóa chứng chỉ thành công !");
                loadDataToTable(); // tải lại danh sách
            } else {
                const errorText = await response.text();
                console.error("Lỗi từ server:", errorText);
                alert("Xóa chứng chỉ thất bại!");
            }
        } catch (error) {
            console.error("Lỗi kết nối:", error);
            alert("Không thể kết nối đến server.");
        }
    }

    // luu certificate
    async function saveCertificate(event) {
        checkSession(); // Đảm bảo phiên làm việc còn hiệu lực
        event.preventDefault(); // Ngăn form reload trang
        const token = localStorage.getItem('token'); // Lấy JWT từ localStorage
        if (!token) {
            alert("Bạn cần đăng nhập để lưu thông tin!");
            return;
        }

        // Lấy dữ liệu từ form
        const nameCertificate = document.getElementById("certificate-name").value.trim();
        const certificateType = document.getElementById("certificate-type").value;
        const issueDate = document.getElementById("issue-date").value;
        const description = document.getElementById("description").value;
        const studentId = document.getElementById("student_id").value.trim();
        const certificateIdElement = document.getElementById("certificate-id");
        if (!certificateIdElement) {
            console.error("Không tìm thấy certificate-id trong DOM.");
            return;
        }
        const certificateId = certificateIdElement.value.trim();

        // Kiểm tra dữ liệu đầu vào
        if (!nameCertificate || !certificateType || !issueDate || !studentId) {
            alert("Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        // Tạo dữ liệu để gửi
        const requestData = { nameCertificate, certificateType, issueDate, description, studentId };

        // Xác định URL và phương thức
        const isEdit = certificateId && certificateId !== "0"; // Kiểm tra chế độ
        const url = isEdit
            ? `http://localhost:8080/certificates/${certificateId}` // Chỉnh sửa
            : `http://localhost:8080/certificates`; // Thêm mới
        const method = isEdit ? "PUT" : "POST";

        try {
            const response = await fetch(url, {
                method,
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${token}`,
                },
                body: JSON.stringify(requestData),
            });

            if (response.ok) {
                alert(`${isEdit ? "Cập nhật" : "Thêm mới"} chứng chỉ thành công!`);
                closeModal(); // Đóng modal
                loadDataToTable(); // Tải lại danh sách
            } else {
                const errorText = await response.text();
                console.error("Lỗi từ server:", errorText);
                alert(`${isEdit ? "Cập nhật" : "Thêm mới"} chứng chỉ thất bại!`);
            }
        } catch (error) {
            console.error("Lỗi kết nối:", error);
            alert("Không thể kết nối đến server.");
        }

    }


    // load data into table
    async function loadDataToTable() {
        const token = localStorage.getItem('token');
        if (!token) {
            alert("Bạn cần đăng nhập để xem danh sách chứng chỉ!");
            return;
        }
        // get data
        try {
            const response = await fetch('http://localhost:8080/certificates', {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${token}`,
                },
            });
            if (response.ok) {
                const data = await response.json();
                const tableBody = document.getElementById('certificate-table-body');

                // xoa noi dung cu
                tableBody.innerHTML = "";

                // duyet list va chen tung hang vao bang
                data.result.forEach(certificate => {
                    const row = document.createElement('tr');
                    row.innerHTML = `
                        <td>${certificate.id}</td>
                        <td>${certificate.nameCertificate}</td>
                        <td>${certificate.certificateType}</td>
                        <td>${formatDateToYMD(certificate.issueDate)}</td>
                        <td>${certificate.studentId}</td>
                        <td>${certificate.description}</td>
                        <td>
                            <button class="edit-btn" data-id="${certificate.id}">Sửa</button>
                            <button class="delete-btn" data-id="${certificate.id}">Xóa</button>
                        </td>
                    `;
                    tableBody.appendChild(row);
                    // Gắn sự kiện cho nút "Sửa"
                    row.querySelector(".edit-btn").addEventListener("click", () => openModalCertificate('edit', certificate));
                    // Gắn sự kiện cho nút "Xóa"
                    row.querySelector(".delete-btn").addEventListener("click", deleteCertificate);

                });
            } else {
                const errorText = await response.text();
                console.error("Lỗi từ server:", errorText);
                alert("Không thể tải danh sách chứng chỉ!");
            }
        } catch (error) {
            console.error("Lỗi kết nối:", error);
            alert("Không thể kết nối đến server.");
        }

    }

    function formatDateToYMD(dateString) {
        const [year, month, day] = dateString.split("-");
        const fullYear = year.length === 2 ? `20${year}` : year; // Thêm "20" nếu năm chỉ có 2 chữ số
        return `${fullYear}-${month}-${day}`;
    }

    // goi function load after page loaded
    loadDataToTable();

    // event
    document.getElementById("certificate-form").addEventListener("submit", saveCertificate);


});