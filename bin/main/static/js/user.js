let employeeList = [];
let isUpdate = false;
let user;

$(document).ready(function () {
    isUpdate = false;
});

function openTab(evt, tabName) {
    // hide all content boxes
    var i, tabContent, tabButtons;
    tabContent = document.getElementsByClassName("tab-content");
    for (i = 0; i < tabContent.length; i++) {
        tabContent[i].style.display = "none";
    }

    // remove active class from all buttons
    tabButtons = document.getElementsByClassName("tab-btn");
    for (i = 0; i < tabButtons.length; i++) {
        tabButtons[i].className = tabButtons[i].className.replace(" active", "");
    }

    // show clicked content box and add active class to clicked button
    document.getElementById(tabName).style.display = "block";
    if (evt) {
        evt.currentTarget.className += " active";
    }
}

// Default open tab
document.addEventListener("DOMContentLoaded", function () {
    var defaultBtn = document.querySelector(".tab-btn");
    if (defaultBtn) {
        defaultBtn.click();
    }
});

//get all employees
const getEmployeeList = () => {
    getHTTPService("/user/getemployeelist", "GET", "json").then((response) => {
        employeeList = response;
        console.log(employeeList);

        //create dynamic option for employee dropdown
        let employeeOptions = document.getElementById("userEmployee");
        employeeOptions.innerHTML = '<option value="">Select Employee</option>';
        employeeList.forEach(employee => {
            employeeOptions.innerHTML += `<option value="${employee.id}">${employee.fullname}</option>`;
        });

    })
}

const submitUser = (evt) => {
    // Prevent the default form submission (page reload)
    evt.preventDefault();
    const userformdata = new FormData(userFormData);
    const convertUserFormData = Object.fromEntries(userformdata.entries());

    // Convert roleids multiple check boxes into array
    convertUserFormData.roleIds = userformdata.getAll("roleIds").map(id => parseInt(id));

    if (!isUpdate) {
        Swal.fire({
            icon: "question",
            title: "Please Confirm the Add User",
            text: "Are you sure you want to add this user?",
            showCancelButton: true,
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33",
            confirmButtonText: "Yes, add it!"
        }).then((result) => {
            if (result.isConfirmed) {
                postHTTPService("/user/add/user", "POST", "json", convertUserFormData).then((response) => {
                    Swal.fire({
                        icon: "success",
                        title: "User added successfully",
                        text: response.message,
                        timer: 1500,
                        showConfirmButton: false,
                    })
                    console.log(response);
                }).catch((error) => {
                    Swal.fire({
                        icon: "error",
                        title: "Error adding user",
                        text: error.message,
                        timer: 1500,
                        showConfirmButton: false,
                    })
                    console.log(error);
                });
            }
        });
    } else {
        Swal.fire({
            icon: "question",
            title: "Please Confirm the Update User",
            text: "Are you sure you want to update this user?",
            showCancelButton: true,
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33",
            confirmButtonText: "Yes, update it!"
        }).then((result) => {
            if (result.isConfirmed) {
                postHTTPService(`update/user/${user?.id}`, "PUT", "json", convertUserFormData)
                    .then((response) => {
                        Swal.mixin({
                            toast: true,
                            position: "center",
                            showConfirmButton: false,
                            timer: 2000,
                            timerProgressBar: false,
                            didOpen: (toast) => {
                                toast.onmouseenter = Swal.stopTimer;
                                toast.onmouseleave = Swal.resumeTimer;
                            }
                        }).fire({
                            icon: "success",
                            title: "Update is successful"
                        });

                        isUpdate = false; // Reset flag
                    })
                    .catch((error) => {
                        Swal.fire({
                            icon: "error",
                            title: "Error updating user",
                            text: error.message,
                            timer: 1500,
                            showConfirmButton: false,
                        });
                    });
            }
        });
    }
}

const updateUser = (userid) => {
    console.log("button clicked");
    const id = userid;
    console.log(id);

    //change header text
    document.getElementById("addUserModalHeader").innerText = "Update User";

    //open modal
    isUpdate = true;
    if (isUpdate) {
        var model = new bootstrap.Modal(document.getElementById("addUserModal"));
        model.show();
    } else {
        var model = new bootstrap.Modal(document.getElementById("addUserModal"));
        model.hide();
    }
    user = null;

    //get user data 
    getHTTPService(`/user/getuserbyid/${id}`, "GET", "json").then((response) => {
        user = response;
        console.log("selected user" + user.employeeid.callingname);
        const id = user?.employeeid?.id;
        const label = user?.employeeid?.callingname;
        const userStatus = user?.status;

        const select = document.querySelector(`select[name="employeeid"]`);
        if (!select.querySelector(`option[value="${id}"]`)) {
            select.add(new Option(label, id, true, true));
        }
        select.value = id;
        
        const status = document.querySelector(`select[name="userStatus"]`);
        status.value = userStatus;

        const selectedRoleIds = user?.roles?.map(role => Number(role.id)) || [];
        document.querySelectorAll('input[name="roleIds"]').forEach(checkbox => {
            checkbox.checked = selectedRoleIds.includes(Number(checkbox.value));
        });

        document.getElementById("username").value = user?.username ?? "";
        // Only set password if backend provided one; otherwise clear to avoid showing browser autofill
        if (user?.password) {
            document.getElementById("userPassword").value = user.password;
        } else {
            document.getElementById("userPassword").value = "";
            document.getElementById("userPassword").setAttribute("autocomplete", "new-password");
        }
        document.getElementById("userEmail").value = user?.employeeid?.email ?? "";
        document.getElementById("userNote").value = user?.note ?? "";
        document.getElementById("userStatus").value = user.status;
    });
}

const resetForm = () => {
    window.location.reload();
    if (document.activeElement) {
        document.activeElement.blur();
    }
    document.getElementById("userFormData").reset();
    isUpdate = false;

    document.getElementById("submitButton").textContent = "Add User";
    document.getElementById("submitButton").style.display = "none";
    document.getElementById("submitButton").classList.remove("d-none");
    document.querySelector(".modal-title").textContent = "Add User";

    // Make all inputs editable again
    const inputs = document.querySelectorAll("#userFormData .form-control, #userFormData .form-select, #userFormData .form-check-input");
    inputs.forEach(input => {
        input.readOnly = false;
        input.disabled = false;
    });
}
