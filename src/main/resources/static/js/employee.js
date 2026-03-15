const inputs = document.querySelectorAll(".validetion");
let employee = [];
let isUpdate = false;
isView = false;


inputs.forEach(input => {
    input.addEventListener("blur", function () {
        // Find the error message element that belongs to this specific input
        const errorMsg = input.parentElement.querySelector(".errorMessage");

        if (input.value.trim() === "") {
            if (errorMsg) errorMsg.textContent = "Input field can't be empty";
            input.classList.add("input-error");
        } else {
            if (errorMsg) errorMsg.textContent = "";
            input.classList.remove("input-error");
        }
    });
});

//validate nic
const employeeNic = document.getElementById("employeeNIC");
function validateNIC() {
    const nicRegex = /^\d{12}$/;
    const oldNicRegex = /^\d{9}[vV]$/;
    const nic = employeeNic.value.trim();
    const errorMessage = employeeNic.parentElement.querySelector(".errorMessage");

    if (nic === "") return;

    if (!nicRegex.test(nic) && !oldNicRegex.test(nic)) {
        errorMessage.textContent = "Invalid NIC format. Please enter 9 digits with V/X or 12 digits.";
        employeeNic.classList.add("input-error");
    } else {
        errorMessage.textContent = "";
        employeeNic.classList.remove("input-error");
    }
    setBirthDay();
}

//set date of birth
const setDob = document.getElementById("employeeDOB");

function setBirthDay() {
    let nic = employeeNic.value.trim();
    if (nic.length !== 10 && nic.length !== 12) return;

    let year, dayValue;

    if (nic.length === 12) {
        year = parseInt(nic.substring(0, 4));
        dayValue = parseInt(nic.substring(4, 7));
    } else {
        year = parseInt("19" + nic.substring(0, 2));
        dayValue = parseInt(nic.substring(2, 5));
    }

    // check for female brithday
    if (dayValue > 500) {
        dayValue = dayValue - 500;
    }

    // Calculate the date from the year and day of the year
    let date = new Date(year, 0, 1);
    date.setDate(dayValue);

    const yyyy = date.getFullYear();
    const mm = String(date.getMonth() + 1).padStart(2, '0');
    const dd = String(date.getDate()).padStart(2, '0');

    const formattedDateForInput = `${yyyy}-${mm}-${dd}`;

    if (setDob) {
        setDob.value = formattedDateForInput;
    }
}

//validetion for email
const validateEmail = () => {
    const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
    const email = document.getElementById("employeeEmail").value.trim();
    const errorMessage = document.getElementById("employeeEmail").parentElement.querySelector(".errorMessage");
    if (email === "") return;
    if (!emailRegex.test(email)) {
        errorMessage.textContent = "Invalid email format. Please enter a valid email address.";
        document.getElementById("employeeEmail").classList.add("input-error");
    } else {
        errorMessage.textContent = "";
        document.getElementById("employeeEmail").classList.remove("input-error");
    }
}

//validate mobile no
const validateMobileNo = () => {
    const mobileNoRegex = /^[0-9]{10}$/;
    const mobileNo = document.getElementById("employeePhone").value.trim();
    const errorMsg = document.getElementById("employeePhone").parentElement.querySelector(".errorMessage");
    if (mobileNo === "") return;
    if (!mobileNoRegex.test(mobileNo)) {
        errorMsg.textContent = "Invalid mobile number format. Please enter 10 digits.";
        document.getElementById("employeePhone").classList.add("input-error");
    } else {
        errorMsg.textContent = "";
        document.getElementById("employeePhone").classList.remove("input-error");
    }

}

//set calling name
const setCallingName = () => {
    const fullName = document.getElementById("employeeFullName").value.trim();
    const callingName = document.getElementById("employeeName");
    if (fullName === "") return;
    const names = fullName.split(" ");
    callingName.value = names[0];
}

const refreshEmployeeData = () => {

    $.ajax({
        url: "/employees/get/alldata",
        type: "GET",
        dataType: "json",
        async: false,
    })
        .done(function (data, jqXHR) {

            employee = data;
            console.log("Employee data fetched successfully:", data);
            populateEmployeeTable();

        })
        .fail(function (jqXHR, textStatus, errorThrown) {
            console.error("Error fetching employee data:", textStatus, errorThrown);
            console.error("Response text:", jqXHR.responseText);
            console.error("Status code:", jqXHR.status);
            alert("Error: " + textStatus);
        })
        .always(function () {

            console.log("Request complete");
        });

}

// load employee data when the page is ready
$(document).ready(function () {
    refreshEmployeeData();
});

//add employee array data into the table after fetching from the server

const employeeTableBody = document.querySelector(".employeeTable tbody");

const populateEmployeeTable = (data = employee) => {
    employeeTableBody.innerHTML = "";
    data.forEach(emp => {
        const row = document.createElement("tr");
        row.innerHTML = `
            <td onclick="viewEmployee(${emp.id})">${emp.fullname}</td>
            <td onclick="viewEmployee(${emp.id})">${emp.callingname}</td>
            <td onclick="viewEmployee(${emp.id})">${emp.nic}</td>
            <td onclick="viewEmployee(${emp.id})">${emp.dob}</td>
            <td onclick="viewEmployee(${emp.id})">${emp.gender}</td>
            <td onclick="viewEmployee(${emp.id})">${emp.email}</td>
            <td onclick="viewEmployee(${emp.id})">${emp.phonenumber}</td>
            <td onclick="viewEmployee(${emp.id})">${emp.address}</td>
            <td onclick="viewEmployee(${emp.id})">${emp.position}</td>
            <td class="d-flex flex-row">
                <button class="btn btn-teal px-3 py-2 ms-2"  onclick="updateEmployee(${emp.id})">Edit</button>
                <button class="btn btn-red px-3 py-2 ms-2" onclick="deleteEmployee(${emp.id})">Delete</button>
            </td>
        `;
        employeeTableBody.appendChild(row);
    });
}

//update employee
const updateEmployee = (id) => {
    console.log(id);

    isUpdate = true;
    document.getElementById("submitButton").textContent = "Update Employee";
    document.querySelector(".modal-title").textContent = "Update Employee";

    //open employee form model
    const modal = new bootstrap.Modal(document.getElementById("employeeModal"));
    modal.show();

    //set employee data to the form
    const emp = employee.find(e => e.id === id);
    document.getElementById("employeeFullName").value = emp.fullname;
    document.getElementById("employeeName").value = emp.callingname;
    document.getElementById("employeeNIC").value = emp.nic;
    document.getElementById("employeeDOB").value = emp.dob;
    document.getElementById("employeeEmail").value = emp.email;
    document.getElementById("employeePhone").value = emp.phonenumber;
    document.querySelector(`input[name="gender"][value="${emp.gender}"]`).checked = true;
    document.querySelector(`textarea[name='address']`).value = emp.address;
    document.querySelector(`select[name="position"] option[value="${emp.position}"]`).selected = true;
    // Store the employee data 
    document.getElementById("employeeFormData").dataset.employeeId = id;


}


const employeeFormDataListener = (event) => {
    event.preventDefault();
    const formData = new FormData(employeeFormData);
    console.log("Form data collected:", Object.fromEntries(formData.entries()));
    const employeeId = document.getElementById("employeeFormData").dataset.employeeId;

    // Store isUpdate before resetting it
    const shouldUpdate = isUpdate;

    isUpdate = false;
    document.getElementById("submitButton").textContent = "Add Employee";
    document.querySelector(".modal-title").textContent = "Add Employee";

    if (shouldUpdate) {
        console.log("Updating employee with ID:", employeeId);

    }



    const url = shouldUpdate ? `/employees/update/${employeeId}` : "/employees/add/employee";
    const method = shouldUpdate ? "PUT" : "POST";


    $.ajax({
        url: url,
        type: method,
        data: new URLSearchParams(formData),
        contentType: "application/x-www-form-urlencoded",
        dataType: "json",
        async: false,
    })
        .done(function (data, jqXHR) {

            swal.fire({
                title: "Success",
                text: shouldUpdate ? "Employee updated successfully!" : "Employee added successfully!",
                icon: "success",
                showConfirmButton: true,
                confirmButtonText: "OK",
            }).then((result) => {
                console.log(shouldUpdate ? "Employee updated successfully:" : "Employee added successfully:", data);
                refreshEmployeeData();
                window.location.href = "/employees/getemployees";
                $('#employeeModal').modal('hide');


            });




        })
        .fail(function (jqXHR, textStatus, errorThrown) {
            console.error(shouldUpdate ? "Error updating employee:" : "Error adding employee:", textStatus, errorThrown);
            console.error("Response text:", jqXHR.responseText);
            console.error("Status code:", jqXHR.status);
            swal.fire({
                title: "Error",
                text: shouldUpdate ? "Failed to update employee. Please try again." : "Failed to add employee. Please try again.",
                icon: "error",
                confirmButtonText: "OK"
            });


        })
        .always(function () {
            console.log("Request complete");
        });

    $("#employeeModel").reset();
};


//employeeFormData.addEventListener("submit", employeeFormDataListener);

// search employee 

const searchEmployees = () => {
    const searchValue = document.getElementById("searchEmployee").value.trim().toLowerCase();
    console.log("Search value:", searchValue);
    const filterEmployee = employee.filter(emp => emp.fullname.toLowerCase().includes(searchValue) ||
        emp.callingname.toLowerCase().includes(searchValue) ||
        emp.nic.toLowerCase().includes(searchValue) ||
        emp.email.toLowerCase().includes(searchValue) ||
        emp.position.toLowerCase().includes(searchValue));
    populateEmployeeTable(filterEmployee);
}
const resetForm = () => {
    document.getElementById("employeeFormData").reset();
    isUpdate = false;
    isView = false;
    document.getElementById("submitButton").textContent = "Add Employee";
    document.getElementById("submitButton").style.display = "block";
    document.querySelector(".modal-title").textContent = "Add Employee";
    
    // Make all inputs editable again
    const inputs = document.querySelectorAll(".validetion");
    inputs.forEach(input => {
        input.readOnly = false;
    });
}

const viewEmployee = (id) => {

    isView = true;
    document.getElementById("submitButton").style.display = "none";
    document.querySelector(".modal-title").textContent = "Employee Details";

    //make inputs readonly
    const inputs = document.querySelectorAll(".validetion");
    inputs.forEach(input => {
        input.readOnly = true;
    });

    //open employee form model
    const modal = new bootstrap.Modal(document.getElementById("employeeModal"));
    modal.show();

    //set employee data to the form
    const emp = employee.find(e => e.id === id);
    document.getElementById("employeeFullName").value = emp.fullname;
    document.getElementById("employeeName").value = emp.callingname;
    document.getElementById("employeeNIC").value = emp.nic;
    document.getElementById("employeeDOB").value = emp.dob;
    document.getElementById("employeeEmail").value = emp.email;
    document.getElementById("employeePhone").value = emp.phonenumber;
    document.querySelector(`input[name="gender"][value="${emp.gender}"]`).checked = true;
    document.querySelector(`textarea[name='address']`).value = emp.address;
    document.querySelector(`select[name="position"] option[value="${emp.position}"]`).selected = true;



}


const deleteEmployee = (id) => {
    swal.fire({
        title: "Are you sure?",
        text: "You will not be able to recover this employee!",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Yes, delete it!"
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                url: `/employees/delete/${id}`,
                type: "DELETE",
                dataType: "json",
                async: false,
            })
                .done(function (data, jqXHR) {
                    swal.fire({
                        title: "Deleted!",
                        text: "Employee has been deleted.",
                        icon: "success",
                        showConfirmButton: true,
                        confirmButtonText: "OK",
                    }).then((result) => {
                        console.log("Employee deleted successfully:", data);
                        refreshEmployeeData();
                        window.location.href = "/employees/getemployees";
                    });
                })
                .fail(function (jqXHR, textStatus, errorThrown) {
                    console.error("Error deleting employee:", textStatus, errorThrown);
                    console.error("Response text:", jqXHR.responseText);
                    console.error("Status code:", jqXHR.status);
                    swal.fire({
                        title: "Error",
                        text: "Failed to delete employee. Please try again.",
                        icon: "error",
                        confirmButtonText: "OK"
                    });
                })
                .always(function () {
                    console.log("Request complete");
                });
        }
    });
}
