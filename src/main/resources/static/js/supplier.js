let isUpdate = false;
let supplierid;
let categories = [];

//load functions on loading the web page
$(document).ready(function () {
    fetchMaterial();
})

//fetch material data from the backend
const fetchMaterial = ()=> {
    getHTTPService(`/supplier/get/materials`, "GET", "json").then((response) => {
        categories = response;
        console.log(response);
        renderCategoryOptions();
    }).catch((error) => {
        console.log(error);
    })
}

const getMaterialCategory = (item) => {
    if (!item) return "";
    if (typeof item.category === "string") return item.category;
    if (item.category && typeof item.category.name === "string") return item.category.name;
    if (item.categoryid && typeof item.categoryid === "string") return item.categoryid;
    if (item.categoryid && item.categoryid.name) return item.categoryid.name;
    return "";
}

const renderCategoryOptions = () => {
    const category = document.getElementById("category");
    const material = document.getElementById("material");
    if (!category || !material) return;

    const distinctCategories = [...new Set(categories.map(getMaterialCategory).filter(name => name))];

    category.innerHTML = `<option value="" selected disabled>Select Material</option>` +
        distinctCategories.map(name => `<option value="${name}">${name}</option>`).join("");
    material.innerHTML = `<option value="" selected disabled>Select Material</option>`;

    category.addEventListener("change", function() {
        const selectedCategory = this.value;
        if (selectedCategory) {
            const filteredMaterials = categories
                .filter(item => getMaterialCategory(item) === selectedCategory)
                .map(item => `<option value="${item.id}">${item.material}</option>`)
                .join("");
            material.innerHTML = `<option value="" selected disabled>Select Material</option>` + filteredMaterials;
        } else {
            material.innerHTML = `<option value="" selected disabled>Select Material</option>`;
        }
    });
}

//manage tab on the html page
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
    evt.currentTarget.className += " active";
}

// Default open tab (optional, can also be handled by HTML style="display:block")
document.addEventListener("DOMContentLoaded", function () {
    document.querySelector(".tab-btn").click();
});


const submitSupplier = (evt) => {
    evt.preventDefault();
    const supplierForm = evt.target;
    const formData = new FormData(supplierForm);
    const convertUserFormData = Object.fromEntries(formData.entries());
    const categories = formData.getAll("category");

    if (categories.length > 0) {
        // Map the array of string IDs to an array of objects expected by the backend DTO
        convertUserFormData.category = categories.map(id => ({ id: parseInt(id) }));
    }

    if (!isUpdate) {
        swal.fire({
            icon: "question",
            title: "Add Supplier",
            text: "Please confirm to add a new supplier.?",
            showCancelButton: true,
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33",
            confirmButtonText: "Save"
        }).then((result) => {
            if (result.isConfirmed) {
                postHTTPService(`/supplier/add/supplier`, "POST", "json", convertUserFormData)
                    .then((response) => {
                        swal.fire({
                            icon: "success",
                            title: "User added successfully",
                            text: response.message,
                            timer: 1500,
                            showConfirmButton: false,
                        }).then(() => {
                            window.location.reload();
                        });
                    })
                    .catch((error) => {
                        Swal.fire({
                            icon: "error",
                            title: "Error adding user",
                            text: error.message,
                            timer: 1500,
                            showConfirmButton: false,
                        });
                        console.log(error);
                    });
            }
        });
    } else {
        swal.fire({
            icon: "question",
            title: "Update Supplier",
            text: "Are you sure you want to Update this supplier?",
            showCancelButton: true,
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33",
            confirmButtonText: "Update"
        }).then((result) => {
            if (result.isConfirmed) {
                postHTTPService(`/supplier/update/${supplierid}`, "POST", "json", convertUserFormData)
                    .then((response) => {
                        swal.fire({
                            icon: "success",
                            title: "Supplier updated successfully",
                            text: response.message,
                            timer: 1500,
                            showConfirmButton: false,
                        }).then(() => {
                            window.location.reload();
                        });
                    })
                    .catch((error) => {
                        Swal.fire({
                            icon: "error",
                            title: "Error updating supplier",
                            text: error.message,
                            timer: 1500,
                            showConfirmButton: false,
                        });
                        console.log(error);
                    });
            }
        });

    }
};

const updateSupplier = (id) => {
    console.log("selected" + id);
    supplierid = id;

    isUpdate = true;
    const modalEl = document.getElementById("addSupplierModal");
    const modal = bootstrap.Modal.getOrCreateInstance(modalEl);
    modal.show();

    // Reset form first to clear old data
    document.getElementById("supplierForm").reset();

    getHTTPService(`/supplier/getsupplier/${id}`, "GET", "json").then((response) => {
        console.log(response);
        document.getElementById("companyname").value = response.companyname;
        document.getElementById("email").value = response.email;
        document.getElementById("contact").value = response.contact;
        document.getElementById("address").value = response.address;
        document.getElementById("contactperson").value = response.contactperson;
        document.getElementById("status").value = response.status;
        const checkBox = document.querySelectorAll('input[name="category"]');
        checkBox.forEach((checkbox) => {
            checkbox.checked = response.category && response.category.some(m => m.id === parseInt(checkbox.value));
        });

    });

}

let selectedPrSuppliers = [];

const addSupplierFromDropdown = (selectEl) => {
    if (!selectEl.value) return;

    const id = selectEl.value;
    const name = selectEl.options[selectEl.selectedIndex].text;

    // Check if already selected
    if (!selectedPrSuppliers.some(s => s.id === id)) {
        selectedPrSuppliers.push({ id, name });
        renderSelectedSuppliers();
    }

    // Reset dropdown selection
    selectEl.selectedIndex = 0;
};

const renderSelectedSuppliers = () => {
    const container = document.getElementById("prSelectedSuppliersContainer");
    if (!container) return;

    container.innerHTML = "";

    if (selectedPrSuppliers.length === 0) {
        container.innerHTML = `<span class="text-muted" id="prNoSuppliersText">No suppliers selected yet.</span>`;
        return;
    }

    selectedPrSuppliers.forEach(supplier => {
        const badge = document.createElement("span");
        badge.className = "badge bg-primary text-white d-flex align-items-center gap-1 p-2 m-1";
        badge.style.fontSize = "0.9rem";
        badge.innerHTML = `
            ${supplier.name}
            <button type="button" class="btn-close btn-close-white ms-1" style="font-size: 0.6rem; padding: 2px;" onclick="removeSupplierFromRequest('${supplier.id}')" aria-label="Remove"></button>
        `;
        container.appendChild(badge);
    });
};

const removeSupplierFromRequest = (supplierId) => {
    selectedPrSuppliers = selectedPrSuppliers.filter(s => s.id !== supplierId);
    renderSelectedSuppliers();
};

const submitPriceRequest = (evt) => {
    evt.preventDefault();

    if (selectedPrSuppliers.length === 0) {
        swal.fire({
            icon: "warning",
            title: "No Suppliers Selected",
            text: "Please select at least one supplier from the list to request prices.",
            confirmButtonColor: "#3085d6"
        });
        return;
    }

    const material = document.getElementById("prMaterial").value;
    const itemName = document.getElementById("prItemName").value;
    const quantity = document.getElementById("prQuantity").value;
    const deadline = document.getElementById("prDeadline").value;
    const message = document.getElementById("prMessage").value;

    const requestData = {
        supplierlist: selectedPrSuppliers.map(s => ({ id: parseInt(s.id) })),
        materialcategory: material,
        itemSpecification: itemName,
        quantity: quantity,
        deadline: deadline,
        message: message
    };

    swal.fire({
        icon: "question",
        title: "Send Price Request",
        text: `Are you sure you want to send a price request to ${selectedPrSuppliers.length} supplier(s)?`,
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Send"
    }).then((result) => {
        if (result.isConfirmed) {
            postHTTPService('/supplier/pricerequest', "POST", "json", requestData).then((response) => {
                console.log(response);
                swal.fire({
                    icon: "success",
                    title: "Send Price Request",
                    text: response.message,
                    timer: 1500,
                    showConfirmButton: false,
                }).then(() => {
                    location.reload();
                });
            }).catch((error) => {
                console.log(error);
                swal.fire({ icon: "error", title: "Failed to send request", text: error.message });
            })


        }
    });
};

const viewPriceRequestReplies = (priceRequestId) => {
    // Show the modal first
    const modalEl = document.getElementById("viewRepliesModal");
    const modal = bootstrap.Modal.getOrCreateInstance(modalEl);
    modal.show();

    // Fetch replies via AJAX
    getHTTPService(`/supplier/pricerequest/replies/${priceRequestId}`, "GET", "json").then((replies) => {
        const tableBody = document.getElementById("repliesTableBody");
        const table = document.getElementById("repliesTable");
        const noRepliesMsg = document.getElementById("noRepliesMessage");

        tableBody.innerHTML = ""; // Clear old rows

        if (!replies || replies.length === 0) {
            table.style.display = "none";
            noRepliesMsg.style.display = "block";
            return;
        }

        table.style.display = "table";
        noRepliesMsg.style.display = "none";

        replies.forEach(reply => {
            const tr = document.createElement("tr");
            tr.innerHTML = `
                <td>${reply.supplier ? reply.supplier.companyname : "Unknown"}</td>
                <td>${reply.quantity !== null && reply.quantity !== undefined ? reply.quantity : ""}</td>
                <td>Rs. ${reply.unitPrice.toFixed(2)}</td>
                <td>Rs. ${reply.deliveryCharge.toFixed(2)}</td>
                <td class="fw-bold">Rs. ${reply.totalAmount.toFixed(2)}</td>
                <td>${reply.deliveryDate}</td>
                <td>${reply.replyDate}</td>
            `;
            tableBody.appendChild(tr);
        });
    }).catch((error) => {
        console.error("Error fetching replies:", error);
    });
};

// --- Purchase Order Functions ---

const loadCompletedPriceRequests = () => {
    getHTTPService('/supplier/pricerequests/completed', 'GET', 'json').then((requests) => {
        const prSelect = document.getElementById("poPriceRequest");
        prSelect.innerHTML = '<option value="" selected disabled>Select Price Request</option>';
        
        if (requests && requests.length > 0) {
            requests.forEach(req => {
                const option = document.createElement("option");
                option.value = req.id;
                option.textContent = `PR-${req.id} - ${req.materialcategory} (${req.itemSpecification})`;
                prSelect.appendChild(option);
            });
        }
    }).catch(error => {
        console.error("Error loading completed price requests:", error);
    });
};

const onPriceRequestSelected = (priceRequestId) => {
    if (!priceRequestId) return;
    
    getHTTPService(`/supplier/pricerequest/replies/${priceRequestId}`, 'GET', 'json').then((replies) => {
        const supplierSelect = document.getElementById("poSupplier");
        supplierSelect.innerHTML = '<option value="" selected disabled>Select Supplier</option>';
        
        if (replies && replies.length > 0) {
            replies.forEach(reply => {
                const option = document.createElement("option");
                option.value = reply.supplier.id;
                option.textContent = `${reply.supplier.companyname} - Total: Rs. ${reply.totalAmount}`;
                supplierSelect.appendChild(option);
            });
        }
    }).catch(error => {
        console.error("Error fetching replies for price request:", error);
    });
};

const submitPurchaseOrder = (evt) => {
    evt.preventDefault();
    const form = evt.target;
    const formData = new FormData(form);
    const orderData = Object.fromEntries(formData.entries());
    
    // Convert IDs and numbers to correct types
    orderData.priceRequestId = parseInt(orderData.priceRequestId);
    orderData.supplierId = parseInt(orderData.supplierId);
    
    
    swal.fire({
        icon: "question",
        title: "Place Order",
        text: "Are you sure you want to place this purchase order?",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Yes, place order"
    }).then((result) => {
        if (result.isConfirmed) {
            postHTTPService('/supplier/purchaseorder', 'POST', 'json', orderData).then((response) => {
                swal.fire({
                    icon: "success",
                    title: "Success",
                    text: response.message,
                    timer: 1500,
                    showConfirmButton: false,
                }).then(() => {
                    const modal = bootstrap.Modal.getInstance(document.getElementById("addPurchaseOrderModal"));
                    if (modal) modal.hide();
                    form.reset();
                    loadPurchaseOrders();
                });
            }).catch(error => {
                swal.fire({ icon: "error", title: "Error", text: error.message });
            });
        }
    });
};

const loadPurchaseOrders = () => {
    getHTTPService('/supplier/purchaseorders', 'GET', 'json').then((orders) => {
        const tableBody = document.getElementById("purchaseOrderTableBody");
        if (!tableBody) return;
        
        tableBody.innerHTML = "";
        
        if (orders && orders.length > 0) {
            orders.forEach(order => {
                const tr = document.createElement("tr");
                const badgeClass = order.paymentStatus === 'Paid' ? 'bg-success' : 'bg-warning text-dark';
                tr.innerHTML = `
                    <td>PO-${order.id}</td>
                    <td>${order.supplier ? order.supplier.companyname : 'N/A'}</td>
                    <td>${order.orderDate}</td>
                    <td>${order.items}</td>
                    <td><span class="badge ${badgeClass}">${order.paymentStatus}</span></td>
                    <td class="d-flex flex-row justify-content-center">
                        <button class="btn btn-teal px-3 py-2 ms-2" onclick="openEditPaymentModal(${order.id}, '${order.paymentStatus}')">Edit</button>
                        <button class="btn btn-red px-3 py-2 ms-2" onclick="deletePurchaseOrder(${order.id})">Delete</button>
                    </td>
                `;
                tableBody.appendChild(tr);
            });
        } else {
            tableBody.innerHTML = '<tr><td colspan="6" class="text-center text-muted py-3">No Purchase Orders found.</td></tr>';
        }
    }).catch(error => {
        console.error("Error loading purchase orders:", error);
    });
};

const deletePurchaseOrder = (id) => {
    swal.fire({
        icon: "warning",
        title: "Delete Order",
        text: "Are you sure you want to delete this purchase order? This action cannot be undone.",
        showCancelButton: true,
        confirmButtonColor: "#d33",
        cancelButtonColor: "#3085d6",
        confirmButtonText: "Yes, delete it!"
    }).then((result) => {
        if (result.isConfirmed) {
            getHTTPService(`/supplier/purchaseorder/delete/${id}`, 'DELETE', 'json').then((response) => {
                swal.fire({
                    icon: "success",
                    title: "Deleted!",
                    text: response.message,
                    timer: 1500,
                    showConfirmButton: false,
                }).then(() => {
                    loadPurchaseOrders();
                });
            }).catch(error => {
                swal.fire({ icon: "error", title: "Error", text: error.message });
            });
        }
    });
};

const viewPurchaseOrder = (id) => {
    // Placeholder for view functionality
    swal.fire({
        icon: 'info',
        title: 'View Order',
        text: 'View details for PO-' + id + ' (To be implemented)'
    });
};

// --- Payment Editing Functions ---

const openEditPaymentModal = (orderId, currentStatus) => {
    document.getElementById("editPOId").value = orderId;
    document.getElementById("editPaymentStatus").value = currentStatus;
    document.getElementById("paymentProofFile").value = "";
    document.getElementById("paymentNotes").value = "";
    
    // Handle visibility of payment proof section
    handlePaymentStatusChange(currentStatus);
    
    const modalEl = document.getElementById("editPaymentModal");
    const modal = bootstrap.Modal.getOrCreateInstance(modalEl);
    modal.show();
};

const handlePaymentStatusChange = (status) => {
    const proofSection = document.getElementById("paymentProofSection");
    const detailsSection = document.getElementById("paymentDetailsSection");
    const proofInput = document.getElementById("paymentProofFile");
    const methodInput = document.getElementById("paymentMethod");
    const amountInput = document.getElementById("paidAmount");
    
    if (status === "Paid") {
        proofSection.style.display = "block";
        detailsSection.style.display = "block";
        proofInput.required = true;
        methodInput.required = true;
        amountInput.required = true;
    } else {
        proofSection.style.display = "none";
        detailsSection.style.display = "none";
        proofInput.required = false;
        methodInput.required = false;
        amountInput.required = false;
        proofInput.value = "";
        methodInput.value = "";
        amountInput.value = "";
    }
};

const submitPaymentUpdate = (evt) => {
    evt.preventDefault();
    
    const poId = document.getElementById("editPOId").value;
    const paymentStatus = document.getElementById("editPaymentStatus").value;
    const paymentProofFile = document.getElementById("paymentProofFile").files[0];
    const paymentNotes = document.getElementById("paymentNotes").value;
    const paymentMethod = document.getElementById("paymentMethod").value;
    const paidAmount = document.getElementById("paidAmount").value;
    
    // Validate file size if paid and file is selected
    if (paymentStatus === "Paid") {
        if (!paymentProofFile) {
            swal.fire({
                icon: "warning",
                title: "Payment Proof Required",
                text: "Please upload a payment proof file when marking as Paid.",
                confirmButtonColor: "#3085d6"
            });
            return;
        }
        
        if (!paymentMethod || !paidAmount) {
             swal.fire({
                icon: "warning",
                title: "Payment Details Required",
                text: "Please enter the payment method and paid amount.",
                confirmButtonColor: "#3085d6"
            });
            return;
        }
        
        const maxSize = 5 * 1024 * 1024; // 5MB
        if (paymentProofFile.size > maxSize) {
            swal.fire({
                icon: "error",
                title: "File Too Large",
                text: "Payment proof file must be less than 5MB.",
                confirmButtonColor: "#d33"
            });
            return;
        }
    }
    
    swal.fire({
        icon: "question",
        title: "Update Payment Status",
        text: `Are you sure you want to update the payment status to ${paymentStatus}?`,
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Update"
    }).then((result) => {
        if (result.isConfirmed) {
            // Use FormData to send file
            const formData = new FormData();
            formData.append("paymentStatus", paymentStatus);
            formData.append("paymentNotes", paymentNotes);
            if (paymentStatus === "Paid") {
                formData.append("paymentMethod", paymentMethod);
                formData.append("paidAmount", paidAmount);
            }
            if (paymentProofFile) {
                formData.append("paymentProof", paymentProofFile);
            }
            
            postHTTPServiceFormData(`/supplier/purchaseorder/${poId}/payment`, 'POST', formData).then((response) => {
                swal.fire({
                    icon: "success",
                    title: "Success",
                    text: response.message,
                    timer: 1500,
                    showConfirmButton: false,
                }).then(() => {
                    const modal = bootstrap.Modal.getInstance(document.getElementById("editPaymentModal"));
                    if (modal) modal.hide();
                    loadPurchaseOrders();
                });
            }).catch(error => {
                swal.fire({ 
                    icon: "error", 
                    title: "Error", 
                    text: error.message || "Failed to update payment status"
                });
            });
        }
    });
};

// Helper function to post FormData (for file upload)
const postHTTPServiceFormData = (url, method, formData) => {
    return fetch(url, {
        method: method,
        body: formData
    }).then(response => {
        if (!response.ok) {
            return response.json().then(err => {
                throw new Error(err.message || `HTTP error! status: ${response.status}`);
            });
        }
        return response.json();
    });
};

// Initial load for tables
document.addEventListener("DOMContentLoaded", () => {
    loadPurchaseOrders();
});

// --- Send Quotation Functions ---

const openSendQuotationModal = (priceRequestId) => {
    // Reset form
    document.getElementById("sendQuotationForm").reset();
    document.getElementById("sqTotalAmount").value = "";
    document.getElementById("sqPriceRequestId").value = priceRequestId;
    
    // Fetch price request details to get the list of suppliers sent to
    getHTTPService(`/supplier/pricerequest/${priceRequestId}`, 'GET', 'json').then((pr) => {
        document.getElementById("sqPriceRequestDisplay").value = `PR-${pr.id} - ${pr.materialcategory} (${pr.itemSpecification})`;
        
        // Extract numeric quantity if possible
        let parsedQty = "";
        if (pr.quantity) {
            const match = pr.quantity.match(/\d+/);
            if (match) {
                parsedQty = parseInt(match[0]);
            }
        }
        document.getElementById("sqQuantity").value = parsedQty;

        const supplierSelect = document.getElementById("sqSupplierSelect");
        supplierSelect.innerHTML = '<option value="" selected disabled>Choose Supplier</option>';
        
        if (pr.supplierlist && pr.supplierlist.length > 0) {
            pr.supplierlist.forEach(supplier => {
                const option = document.createElement("option");
                option.value = supplier.id;
                option.textContent = supplier.companyname;
                supplierSelect.appendChild(option);
            });
        }
        
        const modalEl = document.getElementById("sendQuotationModal");
        const modal = bootstrap.Modal.getOrCreateInstance(modalEl);
        modal.show();
    }).catch(error => {
        console.error("Error loading price request details:", error);
        swal.fire({ icon: "error", title: "Error", text: "Failed to load price request details" });
    });
};

const calculateSqTotal = () => {
    const unitPrice = parseFloat(document.getElementById("sqUnitPrice").value) || 0;
    const quantity = parseFloat(document.getElementById("sqQuantity").value) || 0;
    const deliveryCharge = parseFloat(document.getElementById("sqDeliveryCharge").value) || 0;
    const total = (unitPrice * quantity) + deliveryCharge;
    document.getElementById("sqTotalAmount").value = total > 0 ? total.toFixed(2) : "";
};

const submitSupplierQuotation = (evt) => {
    evt.preventDefault();
    const form = evt.target;
    
    const priceRequestId = document.getElementById("sqPriceRequestId").value;
    const supplierId = document.getElementById("sqSupplierSelect").value;
    const unitPrice = document.getElementById("sqUnitPrice").value;
    const quantity = document.getElementById("sqQuantity").value;
    const deliveryCharge = document.getElementById("sqDeliveryCharge").value;
    const deliveryDate = document.getElementById("sqDeliveryDate").value;
    const totalAmount = document.getElementById("sqTotalAmount").value;
    
    if (!supplierId || !unitPrice || !quantity || !deliveryCharge || !deliveryDate || !totalAmount) {
        swal.fire({
            icon: "warning",
            title: "Required Fields Missing",
            text: "Please fill all required fields.",
            confirmButtonColor: "#3085d6"
        });
        return;
    }
    
    const quotationData = {
        priceRequestId: parseInt(priceRequestId),
        supplierId: parseInt(supplierId),
        unitPrice: parseFloat(unitPrice),
        quantity: parseInt(quantity),
        deliveryCharge: parseFloat(deliveryCharge),
        deliveryDate: deliveryDate,
        totalAmount: parseFloat(totalAmount)
    };
    
    swal.fire({
        icon: "question",
        title: "Submit Quotation",
        text: "Are you sure you want to save this supplier quotation?",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Submit"
    }).then((result) => {
        if (result.isConfirmed) {
            postHTTPService('/supplier/pricerequest/reply', 'POST', 'json', quotationData).then((response) => {
                swal.fire({
                    icon: "success",
                    title: "Quotation Submitted",
                    text: response.message,
                    timer: 1500,
                    showConfirmButton: false,
                }).then(() => {
                    const modal = bootstrap.Modal.getInstance(document.getElementById("sendQuotationModal"));
                    if (modal) modal.hide();
                    location.reload();
                });
            }).catch(error => {
                swal.fire({ icon: "error", title: "Error", text: error.message });
            });
        }
    });
};


