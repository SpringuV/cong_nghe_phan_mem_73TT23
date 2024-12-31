// Function to open the modal
function openModal() {
    const modal = document.getElementById("myModal");
    modal.style.display = "block";
  }
  
  // Function to close the modal
  function closeModal() {
    const modal = document.getElementById("myModal");
    modal.style.display = "none";
  }
  
  // Close the modal if the user clicks outside of it
  window.onclick = function (event) {
    const modal = document.getElementById("myModal");
    if (event.target === modal) {
      modal.style.display = "none";
    }
  };
  
  // Function to toggle sidebar (collapse/expand)
  function toggleSidebar() {
    const sidebar = document.getElementById("sidebar");
    sidebar.classList.toggle("collapsed");
  }

  // Tải sidebar.html và thêm vào #sidebar-container
  fetch('/fontend_QLVBCC/pages/fragment/sidebar.html')
  .then(response => response.text())
  .then(html => {
    document.getElementById('sidebar-container').innerHTML = html;
  })
  .catch(err => console.error('Lỗi khi tải sidebar:', err));
