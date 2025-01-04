document.addEventListener('DOMContentLoaded', () => {

    // luu certificate
    async function saveDegree(event) {
        checkSession(); // Đảm bảo phiên làm việc còn hiệu lực
        event.preventDefault(); // Ngăn form reload trang
        const token = localStorage.getItem('token'); // Lấy JWT từ localStorage
        if (!token) {
            alert("Bạn cần đăng nhập để lưu thông tin!");
            return;
        }

        // Lấy dữ liệu từ form
        const degreeId = document.getElementById("")
        const major = document.getElementById("degree-major").value.trim();
        const degreeType = document.getElementById("degree-type").value;
        const issueDate = document.getElementById("issue-date").value;
        const studentId = document.getElementById("student-id").value.trim();

        console.log(major);
        console.log(degreeType);
        console.log(issueDate);
        console.log(studentId);
        // Kiểm tra dữ liệu đầu vào
        if (!major || !degreeType || !issueDate || !studentId) {
            alert("Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        // Tạo dữ liệu để gửi
        const requestData = { major, degreeType, issueDate, studentId };

        // Xác định URL và phương thức
        const isEdit = degreeId && degreeId !== "0"; // Kiểm tra chế độ
        const url = isEdit
            ? `http://localhost:8080/diplomas/${degreeId}` // Chỉnh sửa
            : `http://localhost:8080/diplomas`; // Thêm mới
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
                alert(`${isEdit ? "Cập nhật" : "Thêm mới"} văn bằng thành công!`);
                closeModal(); // Đóng modal
                loadDegreesTable(); // Tải lại danh sách
            } else {
                const errorText = await response.text();
                console.error("Lỗi từ server:", errorText);
                alert(`${isEdit ? "Cập nhật" : "Thêm mới"} văn bằng thất bại!`);
            }
        } catch (error) {
            console.error("Lỗi kết nối:", error);
            alert("Không thể kết nối đến server.");
        }

    }

    // Load Degrees Table
    async function loadDegreesTable() {
        const token = localStorage.getItem('token');
        if (!token) {
            alert("Bạn cần đăng nhập để xem danh sách chứng chỉ!");
            return;
        }
        // get data
        try {
            const response = await fetch("http://localhost:8080/diplomas", {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${localStorage.getItem("token")}`,
                },
            });

            if (response.ok) {
                const data = await response.json();
                console.log(data);
                const tableBody = document.getElementById('degree-table-body');
                tableBody.innerHTML = ""; // Xóa nội dung cũ

                if (Array.isArray(data.result) && data.result.length > 0) {
                    data.result.forEach(degree => {
                        const row = document.createElement("tr");
                        row.innerHTML = `
                            <td>${degree.major}</td>
                            <td>${degree.degreeType}</td>
                            <td>${degree.issueDate}</td>
                            <td>${degree.studentId}</td>
                            <td>
                                <button class="edit-btn" data-id="${degree.diplomaId}">Sửa</button>
                                <button class="delete-btn" data-id="${degree.diplomaId}">Xóa</button>
                            </td>
                        `;
                        tableBody.appendChild(row);
                        // Gắn sự kiện cho nút "Sửa"
                        row.querySelector(".edit-btn").addEventListener("click", () => openModalCertificate('edit', degree));
                        // Gắn sự kiện cho nút "Xóa"
                        row.querySelector(".delete-btn").addEventListener("click", deleteDegree);
                    });
                } else {
                    // hiển thị thông báo nếu không có văn bằng
                    const noDataRow = document.createElement('tr');
                    noDataRow.innerHTML = `
                        <td colspan="5" style="text-align: center;">Không có dữ liệu văn bằng.</td>
                    `;
                    tableBody.appendChild(noDataRow);
                }
            } else {
                console.error("Không thể tải danh sách văn bằng.");
            }
        } catch (error) {
            console.error("Lỗi kết nối:", error);
        }
    }


    // Delete Degree
    async function deleteDegree(id) {
        checkSession(); // Đảm bảo phiên làm việc còn hiệu lực
        if (!confirm("Bạn có chắc chắn muốn xóa văn bằng này không?")) return;

        try {
            const response = await fetch(`http://localhost:8080/degrees/${id}`, {
                method: "DELETE",
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("token")}`,
                },
            });

            if (response.ok) {
                alert("Xóa văn bằng thành công!");
                loadDegreesTable();
            } else {
                console.error("Không thể xóa văn bằng.");
            }
        } catch (error) {
            console.error("Lỗi kết nối:", error);
        }
    }

    // tự động load data lên
    loadDegreesTable();

    // Gọi hàm load bảng sau khi trang được tải
    document.getElementById("degree-form").addEventListener("submit", saveDegree);

});