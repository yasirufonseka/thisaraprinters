
let employeeList = [];
let roleList = [];
let isUpdate = false;
let user;
let selectedUserId = null;

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

//show data on table 
// const showUserData = () => {
//     getHTTPService("/user/getallusers", "GET", "json").then((response) => {
//         console.log(response);
//     })
// }


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

//get all roles
const getRoleList = () => {
    getHTTPService("/user/getroles", "GET", "json").then((response) => {
        roleList = response;
        console.log(roleList);

        //create dynamic checkboxes for role
        const roleContainer = document.getElementById("userRoleContainer");
        roleContainer.innerHTML = '';
        roleList.forEach(role => {
            roleContainer.innerHTML += `
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" value="${role.id}" name="roleIds" id="role_${role.id}">
                    <label class="form-check-label" for="role_${role.id}">${role.name}</label>
                </div>`;
        });
    })
}

const prepareAddUser = () => {
    isUpdate = false;
    selectedUserId = null;
    document.getElementById("addUserModalHeader").innerText = "Add User";
    document.getElementById("userFormData").reset();
    getEmployeeList();
    getRoleList();
}

const submitUser = (evt) => {
    // Prevent the default form submission (page reload)
    evt.preventDefault();

    const userformdata = new FormData(userFormData);
    const convertUserFormData = Object.fromEntries(userformdata.entries());

    // Convert roleids multiple check boxes into array
    convertUserFormData.roleIds = userformdata.getAll("roleIds").map(id => parseInt(id));

    let url = "/user/add/user";
    if (isUpdate && selectedUserId) {
        url = `/user/update/user/${selectedUserId}`;
    }

    postHTTPService(url, "POST", "json", convertUserFormData).then((response) => {
        swal.fire({
            icon: "success",
            title: isUpdate ? "User updated successfully" : "User added successfully",
            text: response.message,
            timer: 1500,
            showConfirmButton: false,
        }).then(() => {
            location.reload(); // Reload to show changes in table
        });
        console.log(response);
    }).catch((error) => {
        swal.fire({
            icon: "error",
            title: isUpdate ? "Error updating user" : "Error adding user",
            text: error.message,
            timer: 1500,
            showConfirmButton: false,
        })
        console.log(error);
    });
}



const updateUser = (userid) => {
    console.log("button clicked");
    const id = userid;
    selectedUserId = id;
    console.log(id);

    //change header text
    document.getElementById("addUserModalHeader").innerText = "Update User";

    //open modal
    isUpdate = true;
    var model = new bootstrap.Modal(document.getElementById("addUserModal"));
    model.show();

    // Load dependencies first
    Promise.all([
        getHTTPService("/user/getemployeelist", "GET", "json"),
        getHTTPService("/user/getroles", "GET", "json")
    ]).then(([employees, roles]) => {
        // Populate employees
        let employeeOptions = document.getElementById("userEmployee");
        employeeOptions.innerHTML = '<option value="">Select Employee</option>';
        employees.forEach(employee => {
            employeeOptions.innerHTML += `<option value="${employee.id}">${employee.fullname}</option>`;
        });

        // Populate roles
        const roleContainer = document.getElementById("userRoleContainer");
        roleContainer.innerHTML = '';
        roles.forEach(role => {
            roleContainer.innerHTML += `
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" value="${role.id}" name="roleIds" id="role_${role.id}">
                    <label class="form-check-label" for="role_${role.id}">${role.name}</label>
                </div>`;
        });

        // Now fetch user data
        return getHTTPService(`/user/getuserbyid/${id}`, "GET", "json");
    }).then((response) => {
        user = response;
        console.log("selected user" + user.username);

        document.getElementById("username").value = user?.username;
        document.getElementById("userPassword").value = ""; // Keep password empty for updates for security
        document.getElementById("userEmail").value = user?.employeeid?.email;
        document.getElementById("userNote").value = user?.note || "";
        document.getElementById("userStatus").value = user.status;
        document.getElementById("userEmployee").value = user?.employeeid?.id;

        // Check assigned roles
        if (user.roles) {
            user.roles.forEach(role => {
                const checkbox = document.getElementById(`role_${role.id}`);
                if (checkbox) checkbox.checked = true;
            });
        }
    }).catch(error => {
        console.error("Error loading user data:", error);
    });
}

