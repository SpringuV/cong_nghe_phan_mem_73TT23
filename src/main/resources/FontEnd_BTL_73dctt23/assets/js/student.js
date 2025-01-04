document.addEventListener("DOMContentLoaded", () => {
    async function saveStudent(event) {
        event.preventDefault();
        checkSession(); // Đảm bảo phiên làm việc còn hiệu lực
        const token = localStorage.getItem("token");
        if (!token) {
            alert("Bạn cần đăng nhập để thực hiện chức năng này.");
            return;
        }

        const studentId = document.getElementById("student-id").value.trim();
        const lastName = document.getElementById("student-last-name").value.trim();
        const firstName = document.getElementById("student-first-name").value.trim();
        const departmentName = document.getElementById("student-department").value;
        const yearGraduation = document.getElementById("student-year-graduation").value;
        const yearAdmission = document.getElementById("student-year-admission").value;
        const graduationStatus = document.getElementById("student-graduation-status").value;
        const email = document.getElementById("student-email").value.trim();
        const dob = document.getElementById("dob").value;

        // Lấy danh sách chứng chỉ
        const certificateCount = parseInt(document.getElementById("certificate-count").value, 10);
        const certificateList = [];

        for (let i = 0; i < certificateCount; i++) {
            const nameCertificate = document.getElementById(`certificate-name-${i}`).value.trim();
            const certificateType = document.getElementById(`certificate-type-${i}`).value;
            const issueDate = document.getElementById(`issue-date-${i}`).value;
            const description = document.getElementById(`description-${i}`).value.trim();

            certificateList.push({ nameCertificate, issueDate, description, certificateType, });
        }

        // lấy danh sách tốt nghiệp
        const degreeCount = parseInt(document.getElementById("degree-count").value, 10);
        const diplomaList = [];

        for (let i = 0; i < degreeCount; i++) {
            const major = document.getElementById(`degree-major-${i}`).value;
            const degreeType = document.getElementById(`degree-type-${i}`).value;
            const issueDate = document.getElementById(`degree-year-${i}`).value.trim();

            diplomaList.push({ major, degreeType, issueDate });
        }

        if (!studentId || !lastName || !firstName || !email || !dob || !yearGraduation) {
            alert("Vui lòng nhập đầy đủ thông tin.");
            return;
        }

        const requestData = { graduationStatus, lastName, firstName, departmentName, yearAdmission, yearGraduation, email, dob, certificateList, diplomaList };
        // Xác định URL và phương thức
        const modalTitle = document.getElementById("modal-title").textContent;
        const isEdit = modalTitle.includes("Sửa"); // Kiểm tra tiêu đề modal có chứa từ "Sửa" không

        const url = isEdit
            ? `http://localhost:8080/students/update/${studentId}` // API chỉnh sửa
            : `http://localhost:8080/students`; // API thêm mới
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
                alert(`${isEdit ? "Cập nhật" : "Thêm mới"} sinh viên thành công!`);
                closeStudentModal();
                loadStudentToTable(); // Tải lại danh sách sinh viên
            } else {
                const errorText = await response.text();
                console.error("Lỗi từ server:", errorText);
                alert(`${isEdit ? "Cập nhật" : "Thêm mới"} thất bại!`);
            }
        } catch (error) {
            console.error("Lỗi kết nối:", error);
            alert("Không thể kết nối đến server.");
        }
    }

    async function loadStudentToTable() {
        // check token
        const token = localStorage.getItem('token');
        if (!token) {
            alert("Bạn cần đăng nhập để xem danh sách chứng chỉ!");
            return;
        }
        try {
            const response = await fetch('http://localhost:8080/students', {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${token}`,
                },
            });

            if (response.ok) {
                const data = await response.json();
                const tableBody = document.getElementById('student-table-body');
                // xoa noi dung cu
                tableBody.innerHTML = "";

                // duyet list va chen tung hang vao bang
                if (Array.isArray(data.result) && data.result.length > 0) {
                    data.result.forEach(student => {
                        const row = document.createElement('tr');
                        row.innerHTML = `
                            <td>${student.studentId}</td>
                            <td>${student.lastName} ${student.firstName}</td>
                            <td>${student.departmentName}</td>
                            <td>${student.graduationStatus}</td>
                            <td>
                                <button class="detail-btn">Chi tiết</button>
                            </td>
                        `;
                        tableBody.appendChild(row);
                        // Gắn sự kiện cho nút "Detail"
                        row.querySelector(".detail-btn").addEventListener("click", () => openDetailModal(student));

                    });
                } else {
                    // hiển thị thông báo nếu không có sinh viên
                    const noDataRow = document.createElement('tr');
                    noDataRow.innerHTML = `
                        <td colspan="5" style="text-align: center;">Không có dữ liệu sinh viên.</td>
                    `;
                    tableBody.appendChild(noDataRow);
                }
            } else {
                const errorText = await response.text();
                console.error("Lỗi từ server:", errorText);
                alert("Không thể tải danh sách sinh viên!");
            }
        } catch (error) {
            console.error("Lỗi kết nối:", error);
            alert("Không thể kết nối đến server.");
        }
    }


    async function deleteStudentFromDetail() {
        checkSession(); // Đảm bảo phiên làm việc còn hiệu lực
        const studentId = document.getElementById("detail-student-id").textContent;

        if (!confirm("Bạn có chắc chắn muốn xóa sinh viên này không?")) return;

        const token = localStorage.getItem("token");
        if (!token) {
            alert("Bạn cần đăng nhập để thực hiện chức năng này.");
            return;
        }

        try {
            const response = await fetch(`http://localhost:8080/students/${studentId}`, {
                method: "DELETE",
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });

            if (response.ok) {
                alert("Xóa sinh viên thành công!");
                closeDetailModal();
                loadStudentToTable(); // Tải lại danh sách sinh viên
            } else {
                const errorText = await response.text();
                console.error("Lỗi từ server:", errorText);
                alert("Không thể xóa sinh viên!");
            }
        } catch (error) {
            console.error("Lỗi kết nối:", error);
            alert("Không thể kết nối đến server.");
        }
    }

    function generateCertificateInputs() {
        checkSession(); // Đảm bảo phiên làm việc còn hiệu lực
        const count = parseInt(document.getElementById("certificate-count").value, 10);
        const container = document.getElementById("certificate-inputs-container");

        if (isNaN(count) || count < 1) {
            alert("Vui lòng nhập số lượng chứng chỉ hợp lệ!");
            return;
        }

        container.innerHTML = ""; // Xóa các mẫu cũ
        for (let i = 0; i < count; i++) {
            const certificateHtml = `
                <div class="certificate-entry" id="certificate-entry-${i}">
                    <h4>Chứng chỉ ${i + 1}</h4>
                    <div>
                        <label for="certificate-name-${i}">Tên chứng chỉ:</label>
                        <select id="certificate-name-${i}">
                            <option value = "none">Chọn tên chứng chỉ</option>
                            <option value="MOS">MOS (Microsoft Office Specialist)</option>
                            <option value="IC3">IC3 (Internet and Computing Core Certification)</option>
                            <option value="CCNA">CCNA (Cisco Certified Network Associate)</option>
                            <option value="CompTIA-A+">CompTIA A+</option>
                            <option value="CompTIA-Network+">CompTIA Network+</option>
                            <option value="ECDL">ECDL (European Computer Driving License)</option>
                            <option value="CEH">CEH (Certified Ethical Hacker)</option>
                            <option value="Google-IT-Support">Google IT Support Certificate</option>
                            <option value="AWS-Solutions-Architect">AWS Certified Solutions Architect</optio
                            <option value="CISA">CISA (Certified Information Systems Auditor)</option>
                            <option value="Microsoft-Certified-Azure">Microsoft Certified: Azure Fundamentals</option>
                            <option value="Microsoft-Certified-Developer">Microsoft Certified: Developer Associate</option>
                            <option value="Google-Professional-Cloud">Google Professional Cloud Architect</option>
                            <option value="AWS-Certified-Developer">AWS Certified Developer</option>
                            <option value="AWS-SysOps-Administrator">AWS Certified SysOps Administrator</option>
                            <option value="Oracle-Certified-Java">Oracle Certified Professional, Java SE Programmer</option>
                            <option value="Red-Hat-Certified-System-Admin">Red Hat Certified System Administrator (RHCSA)</option>
                            <option value="Red-Hat-Certified-Engineer">Red Hat Certified Engineer (RHCE)</option>
                            <option value="Linux-Professional-Institute-Certification">Linux Professional Institute Certification (LPIC)</option>
                            <option value="CISSP">CISSP (Certified Information Systems Security Professional)</option>
                            <option value="ITIL-Foundation">ITIL Foundation Certification</option>
                            <option value="IELTS">IELTS (International English Language Testing System)</opt
                            <option value="TOEFL">TOEFL (Test of English as a Foreign Language)</option>
                            <option value="TOEIC">TOEIC (Test of English for International Communication)</o
                            <option value="Cambridge-KET">Cambridge English: KET</option>
                            <option value="Cambridge-PET">Cambridge English: PET</option>
                            <option value="Cambridge-FCE">Cambridge English: FCE</option>
                            <option value="Cambridge-CAE">Cambridge English: CAE</option>
                            <option value="Cambridge-CPE">Cambridge English: CPE</option>
                            <option value="PTE">PTE (Pearson Test of English)</option>
                            <option value="BULATS">BULATS (Business Language Testing Service)</option>
                            <option value="OPI">OPI (Oral Proficiency Interview)</option>
                            <option value="OPIc">OPIc (Oral Proficiency Interview - Computerized)</option>
                            <option value="Duolingo">Duolingo English Test</option>
                            <option value="EF-SET">EF SET (English First Standardized English Test)</option>
                            <option value="ESOL">ESOL (English for Speakers of Other Languages)</option>
                            <option value="CELPIP">CELPIP (Canadian English Language Proficiency Index Program)</option>
                            <option value="BEC-Preliminary">BEC Preliminary (Business English Certificate)</option>
                            <option value="BEC-Vantage">BEC Vantage (Business English Certificate)</option>
                            <option value="BEC-Higher">BEC Higher (Business English Certificate)</option>
                            <option value="SELT">SELT (Secure English Language Test)</option>
                            <option value="TOEFL-iBT">TOEFL iBT (Internet-Based Test)</option>
                            <option value="TOEFL-ITP">TOEFL ITP (Institutional Testing Program)</option>
                            <option value="IELTS-General-Training">IELTS General Training</option>
                            <option value="IELTS-Academic">IELTS Academic</option>
                            <option value="TOLES">TOLES (Test of Legal English Skills)</option>
                            <option value="Trinity-GESE">Trinity GESE (Graded Examinations in Spoken English)</option>
                        </select>
                    </div>
                    <label for="certificate-type-${i}">Loại chứng chỉ:</label>
                    <select id="certificate-type-${i}">
                        <option value="none">Chọn loại chứng chỉ</option>
                        <option value="Ngoại ngữ">Ngoại ngữ</option>
                        <option value="Tin học">Tin học</option>
                    </select>
                    
                    <label for="issue-date-${i}">Ngày cấp:</label>
                    <input type="date" id="issue-date-${i}" required />
                    
                    <label for="description-${i}">Mô tả:</label>
                    <textarea id="description-${i}" placeholder="Mô tả (nếu có)"></textarea>
                    <button type="button" onclick="removeCertificate(${i})">Xóa</button>
                </div>
            `;
            container.innerHTML += certificateHtml;
        }
    }

    window.removeCertificate = function removeCertificate(index) {
        const entry = document.getElementById(`certificate-entry-${index}`);
        if (entry) {
            entry.remove();
        }
    }

    // diploma
    // Hàm này xóa một bằng tốt nghiệp khỏi danh sách
    window.removeDegree = function removeDegree(index) {
        const entry = document.getElementById(`degree-entry-${index}`);
        console.log(entry);
        if (entry) {
            entry.remove();
        }
    }

    function generateDegreeInputs() {
        checkSession(); // Đảm bảo phiên làm việc còn hiệu lực
        const count = parseInt(document.getElementById("degree-count").value, 10);
        const container = document.getElementById("degree-inputs-container");

        if (isNaN(count) || count < 1) {
            alert("Vui lòng nhập số lượng bằng tốt nghiệp hợp lệ!");
            return;
        }

        container.innerHTML = ""; // Xóa các mẫu cũ
        for (let i = 0; i < count; i++) {
            const degreeHtml = `
                <div class="degree-entry" id="degree-entry-${i}">
                    <div>
                        <h4>Bằng tốt nghiệp ${i + 1}</h4>
                        <label for="degree-major-${i}">Ngành học:</label>
                        <select id="degree-major-${i}">
                            <option value="none">Chọn ngành học</option>
                            <option value="Công nghệ thông tin">Công nghệ thông tin</option>
                            <option value="Khoa học máy tính">Khoa học máy tính</option>
                            <option value="Hệ thống thông tin">Hệ thống thông tin</option>
                            <option value="Kỹ thuật phần mềm">Kỹ thuật phần mềm</option>
                            <option value="Trí tuệ nhân tạo">Trí tuệ nhân tạo</option>
                            <option value="An ninh mạng">An ninh mạng</option>
                            <option value="Kỹ thuật máy tính">Kỹ thuật máy tính</option>
                            <option value="Công nghệ robot và tự động hóa">Công nghệ robot và tự động hóa</option>
                            <option value="Điện tử viễn thông">Điện tử viễn thông</option>
                            <option value="Kỹ thuật điều khiển và tự động hóa">Kỹ thuật điều khiển và tự động hóa</option>

                            <!-- Ngành kinh tế và quản trị -->
                            <option value="Kinh tế học">Kinh tế học</option>
                            <option value="Quản trị kinh doanh">Quản trị kinh doanh</option>
                            <option value="Tài chính - Ngân hàng">Tài chính - Ngân hàng</option>
                            <option value="Kế toán">Kế toán</option>
                            <option value="Marketing">Marketing</option>
                            <option value="Thương mại điện tử">Thương mại điện tử</option>
                            <option value="Quản trị nhân sự">Quản trị nhân sự</option>
                            <option value="Logistics và quản lý chuỗi cung ứng">Logistics và quản lý chuỗi cung ứng</option>
                            <option value="Kinh doanh quốc tế">Kinh doanh quốc tế</option>
                            <option value="Quản trị du lịch và khách sạn">Quản trị du lịch và khách sạn</option>

                            <!-- Ngành khoa học xã hội và nhân văn -->
                            <option value="Ngôn ngữ Anh">Ngôn ngữ Anh</option>
                            <option value="Ngôn ngữ Nhật">Ngôn ngữ Nhật</option>
                            <option value="Ngôn ngữ Trung">Ngôn ngữ Trung</option>
                            <option value="Quan hệ quốc tế">Quan hệ quốc tế</option>
                            <option value="Truyền thông đa phương tiện">Truyền thông đa phương tiện</option>
                            <option value="Báo chí">Báo chí</option>
                            <option value="Luật kinh tế">Luật kinh tế</option>
                            <option value="Luật quốc tế">Luật quốc tế</option>
                            <option value="Công tác xã hội">Công tác xã hội</option>
                            <option value="Văn học">Văn học</option>

                            <!-- Ngành khoa học tự nhiên và ứng dụng -->
                            <option value="Toán học">Toán học</option>
                            <option value="Vật lý">Vật lý</option>
                            <option value="Hóa học">Hóa học</option>
                            <option value="Sinh học">Sinh học</option>
                            <option value="Khoa học môi trường">Khoa học môi trường</option>
                            <option value="Địa lý học">Địa lý học</option>
                            <option value="Công nghệ sinh học">Công nghệ sinh học</option>
                            <option value="Nông nghiệp và phát triển nông thôn">Nông nghiệp và phát triển nông thôn</option>
                            <option value="Khoa học vật liệu">Khoa học vật liệu</option>
                            <option value="Thủy sản và công nghệ chế biến">Thủy sản và công nghệ chế biến</option>

                            <!-- Ngành y dược -->
                            <option value="Y đa khoa">Y đa khoa</option>
                            <option value="Răng - Hàm - Mặt">Răng - Hàm - Mặt</option>
                            <option value="Dược học">Dược học</option>
                            <option value="Điều dưỡng">Điều dưỡng</option>
                            <option value="Y học cổ truyền">Y học cổ truyền</option>
                            <option value="Kỹ thuật xét nghiệm y học">Kỹ thuật xét nghiệm y học</option>
                            <option value="Công nghệ sinh học y học">Công nghệ sinh học y học</option>
                            <option value="Kỹ thuật phục hồi chức năng">Kỹ thuật phục hồi chức năng</option>
                            <option value="Khoa học dinh dưỡng">Khoa học dinh dưỡng</option>

                            <!-- Ngành nghệ thuật và thiết kế -->
                            <option value="Thiết kế đồ họa">Thiết kế đồ họa</option>
                            <option value="Thiết kế thời trang">Thiết kế thời trang</option>
                            <option value="Nghệ thuật sân khấu và điện ảnh">Nghệ thuật sân khấu và điện ảnh</option>
                            <option value="Mỹ thuật công nghiệp">Mỹ thuật công nghiệp</option>
                            <option value="Âm nhạc học">Âm nhạc học</option>
                            <option value="Diễn viên kịch và điện ảnh">Diễn viên kịch và điện ảnh</option>
                            <option value="Đạo diễn điện ảnh và truyền hình">Đạo diễn điện ảnh và truyền hình</option>
                            <option value="Nhiếp ảnh">Nhiếp ảnh</option>
                            <option value="Hội họa">Hội họa</option>

                            <!-- Ngành kỹ thuật và xây dựng -->
                            <option value="Kỹ thuật cơ khí">Kỹ thuật cơ khí</option>
                            <option value="Kỹ thuật xây dựng">Kỹ thuật xây dựng</option>
                            <option value="Kỹ thuật điện">Kỹ thuật điện</option>
                            <option value="Kỹ thuật công trình thủy">Kỹ thuật công trình thủy</option>
                            <option value="Kỹ thuật giao thông">Kỹ thuật giao thông</option>
                            <option value="Kỹ thuật năng lượng">Kỹ thuật năng lượng</option>
                            <option value="Công nghệ kỹ thuật ô tô">Công nghệ kỹ thuật ô tô</option>
                            <option value="Công nghệ chế tạo máy">Công nghệ chế tạo máy</option>
                            <option value="Vật liệu xây dựng">Vật liệu xây dựng</option>
                            <option value="Cơ khí chế tạo">Cơ khí chế tạo</option>

                            <!-- Ngành sư phạm -->
                            <option value="Sư phạm Toán">Sư phạm Toán</option>
                            <option value="Sư phạm Ngữ văn">Sư phạm Ngữ văn</option>
                            <option value="Sư phạm Vật lý">Sư phạm Vật lý</option>
                            <option value="Sư phạm Hóa học">Sư phạm Hóa học</option>
                            <option value="Sư phạm Sinh học">Sư phạm Sinh học</option>
                            <option value="Sư phạm Lịch sử">Sư phạm Lịch sử</option>
                            <option value="Sư phạm Địa lý">Sư phạm Địa lý</option>
                            <option value="Sư phạm Tiếng Anh">Sư phạm Tiếng Anh</option>
                            <option value="Sư phạm Mầm non">Sư phạm Mầm non</option>
                            <option value="Sư phạm Giáo dục thể chất">Sư phạm Giáo dục thể chất</option>
                        </select>
                    </div>
    
                    <div>
                        <label for="degree-type-${i}">Loại bằng:</label>
                        <select id="degree-type-${i}">
                            <option value="none">Chọn loại bằng</option>
                            <option value="Xuất sắc">Xuất sắc</option>
                            <option value="Giỏi">Giỏi</option>
                            <option value="Khá">Khá</option>
                        </select>
                    </div>

                    <div>
                        <label for="degree-year-${i}">Năm cấp bằng:</label>
                        <input type="text" id="degree-year-${i}" placeholder="Nhập năm cấp bằng" required />
                    </div>

    
                    <button type="button" onclick="removeDegree(${i})">Xóa</button>
                </div>
            `;
            container.innerHTML += degreeHtml;
        }
    }

    // goi function load after page loaded
    loadStudentToTable();
    // event
    document.getElementById("certificate-count").addEventListener("change", generateCertificateInputs);
    document.getElementById("degree-count").addEventListener("change", generateDegreeInputs);
    document.getElementById("student-form").addEventListener("submit", saveStudent);
});