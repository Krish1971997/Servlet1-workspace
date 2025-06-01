function confirmDelete(userId) {
    if (confirm("Are you sure you want to delete this user?")) {
        window.location.href = "/admin/delete?userId=" + userId;
    }
}

function validateForm() {
    const password = document.getElementById("password").value;
    const confirmPassword = document.getElementById("confirmPassword").value;
    if (password !== confirmPassword) {
        alert("Passwords do not match!");
        return false;
    }
    return true;
}