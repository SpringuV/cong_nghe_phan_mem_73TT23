document.addEventListener("DOMContentLoaded", () => {
    async function searchStudent() {
        const studentId = document.getElementById("search-input").value.trim();
        if (!studentId) {
            alert("Vui lòng nhập mã sinh viên!");
            return;
        }

        try {
            const response = await fetch(`http://localhost:8080/students/${studentId}`, {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                },
            });

            if (response.ok) {
                const data = await response.json();
                renderStudentData(data.result);
            } else {
                alert("Không tìm thấy thông tin sinh viên!");
                clearResult();
            }
        } catch (error) {
            console.error("Lỗi kết nối:", error);
            alert("Không thể kết nối tới server!");
        }
    }

    function renderStudentData(data) {
        // Hiển thị thông tin sinh viên
        const studentTable = document.getElementById("data-student");
        studentTable.innerHTML = `
            <tr><th>Tên sinh viên</th><td>${data.lastName} ${data.firstName}</td></tr>
            <tr><th>Mã sinh viên</th><td>${data.studentId}</td></tr>
            <tr><th>Ngành học</th><td>${data.departmentName || "Không có"}</td></tr>
            <tr><th>Trạng thái tốt nghiệp</th><td>${data.graduationStatus || "Không có"}</td></tr>
            <tr><th>Ngày sinh</th><td>${data.dob || "Không có"}</td></tr>
            <tr><th>Năm nhập học</th><td>${data.yearAdmission || "Không có"}</td></tr>
            <tr><th>Năm tốt nghiệp</th><td>${data.yearGraduation || "Không có"}</td></tr>
            <tr><th>Email</th><td>${data.email || "Không có"}</td></tr>
        `;

        // Hiển thị thông tin bằng tốt nghiệp
        const degreeTable = document.getElementById("data-degree");
        degreeTable.innerHTML = `<tr><th>Thông tin</th><th>Chi tiết</th></tr>`; // Reset bảng
        if (data.diplomas?.length) {
            data.diplomas.forEach((diploma, index) => {
                degreeTable.innerHTML += `
                <tr><td colspan="2" class="degree-title">Bằng số ${index + 1}</td></tr>
                <tr><td>Tên ngành học</td><td>${diploma.major || "Không có"}</td></tr>
                <tr><td>Năm cấp</td><td>${diploma.issueDate || "Không có"}</td></tr>
                <tr><td>Loại bằng</td><td>${diploma.degreeType || "Không có"}</td></tr>
            `;
            });
        } else {
            degreeTable.innerHTML += `<tr><td style="text-align: center;" colspan="2">Không có thông tin bằng cấp !</td></tr>`;
        }

        // Hiển thị thông tin chứng chỉ
        const certificateTable = document.getElementById("data-certificate");
        certificateTable.innerHTML = `<tr><th>Thông tin</th><th>Chi tiết</th></tr>`; // Reset bảng
        if (data.certificates?.length) {
            data.certificates.forEach((certificate, index) => {
                certificateTable.innerHTML += `
                    <tr><td colspan="2" class="certificate-title">Chứng chỉ số ${index + 1}</td></tr>
                    <tr><td>Tên chứng chỉ</td><td>${certificate.nameCertificate || "Không có"}</td></tr>
                    <tr><td>Ngày cấp</td><td>${certificate.issueDate || "Không có"}</td></tr>
                    <tr><td>Loại chứng chỉ</td><td>${certificate.certificateType || "Không có"}</td></tr>
                    <tr><td>Mô tả</td><td>${certificate.description || "Không có"}</td></tr>
                `;
            });
        } else {
            certificateTable.innerHTML += `<tr><td style="text-align: center;" colspan="2">Không có thông tin chứng chỉ !</td></tr>`;
        }
    }

    function clearResult() {
        document.getElementById("data-student").innerHTML = `<tr>
                                                                <th>Thông tin</th>
                                                                <th>Chi tiết</th>
                                                            </tr>`;
        document.getElementById("data-degree").innerHTML = `<tr>
                                                                <th>Thông tin</th>
                                                                <th>Chi tiết</th>
                                                            </tr>`;
        document.getElementById("data-certificate").innerHTML = `<tr>
                                                                <th>Thông tin</th>
                                                                <th>Chi tiết</th>
                                                            </tr>`;
    }

    // Sự kiện tìm kiếm
    document.getElementById("search-button").addEventListener("click", searchStudent);
    document.getElementById("search-input").addEventListener("keypress", (event) => {
        if (event.key === "Enter") searchStudent();
    });
});
