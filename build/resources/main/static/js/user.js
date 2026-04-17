
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

const submitUser = (evt) => {
    // Prevent the default form submission (page reload)
    evt.preventDefault();
    document.getElementById("addUserModalHeader").innerText = "Add User";


    const userformdata = new FormData(userFormData);
    const convertUserFormData = Object.fromEntries(userformdata.entries());

    // Convert roleids multiple check boxes into array
    convertUserFormData.roleIds = userformdata.getAll("roleIds").map(id => parseInt(id));

    postHTTPService("/user/add/user", "POST", "json", convertUserFormData).then((response) => {
        swal.fire({
            icon: "success",
            title: "User added successfully",
            text: response.message,
            timer: 1500,
            showConfirmButton: false,
        })
        console.log(response);
    }).catch((error) => {
        swal.fire({
            icon: "error",
            title: "Error adding user",
            text: error.message,
            timer: 1500,
            showConfirmButton: false,
        })
        console.log(error);
    });
    document.getElementById("addUserModalHeader").innerText = "Add User";

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
        console.log("selected user" + user.username);

        document.getElementById("username").value = user?.username
        document.getElementById("userPassword").value = user?.password;
        document.getElementById("userEmail").value = user?.employeeid.email;
        //document.getElementById("userNote").value
        document.getElementById("userStatus").value = user.status;


    })




}

