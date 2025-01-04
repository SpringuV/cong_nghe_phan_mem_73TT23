async function xulyDangNhap(event) {
    event.preventDefault(); // Ngăn form submit mặc định

    // Lấy giá trị từ form
    const username = document.getElementById("username").value.trim();
    const password = document.getElementById("password").value.trim();
    console.log(username);
    console.log(password);
    
    // Kiểm tra dữ liệu đầu vào
    if (!username || !password) {
      alert("Vui lòng nhập tên đăng nhập và mật khẩu!");
      return;
    }

    // Gửi yêu cầu đến backend
    try {
      const response = await fetch("http://localhost:8080/auth/token", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ username, password }), // Gửi dữ liệu dưới dạng JSON
      });

      if (response.ok) {
        const data = await response.json();
        console.log(data.result.token);

        // Lưu thông tin token hoặc người dùng (nếu cần)
        localStorage.setItem("token", data.result.token);

        // Chuyển hướng đến trang chủ
        window.location.href = "/pages/certificate.html";
      } else {
        // Nếu phản hồi không thành công
        const errorText = await response.text();
        alert(errorText || "Đăng nhập thất bại!");
      }
    } catch (error) {
      console.error("Lỗi trong quá trình đăng nhập:", error);
      alert("Đã xảy ra lỗi trong quá trình đăng nhập!");
    }

}