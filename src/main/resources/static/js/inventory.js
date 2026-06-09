// Add Material Form Submission
function submitMaterial(event) {
    event.preventDefault();

    const formData = {
        name: document.getElementById('materialName').value,
        status: document.getElementById('materialStatus').value
    };

    const response = postHTTPService('/inventory/api/materials/add', 'POST', 'json', formData);

    if (response.responseText && response.responseText.includes('Error')) {
        Swal.fire({
            icon: 'error',
            title: 'Error!',
            text: 'Failed to add material. Please try again.'
        });
    } else {
        Swal.fire({
            icon: 'success',
            title: 'Material Added!',
            text: 'Material has been added successfully.',
            timer: 2000,
            timerProgressBar: true,
            willClose: () => {
                location.reload();
            }
        });
    }

    const modal = bootstrap.Modal.getInstance(document.getElementById('addMaterialModal'));
    if (modal) modal.hide();
}

// Update Material
function updateMaterial(materialId) {
    const response = getHTTPService('/inventory/api/materials/' + materialId, 'GET', 'json');

    if (response.responseText && response.responseText.includes('Error')) {
        Swal.fire({
            icon: 'error',
            title: 'Error!',
            text: 'Failed to load material data.'
        });
        return;
    }

    const material = response.responseJSON;

    document.getElementById('materialName').value = material.name;
    document.getElementById('materialStatus').value = material.status;

    document.getElementById('addMaterialModalHeader').textContent = 'Edit Material';
    document.getElementById('materialFormData').onsubmit = function (event) {
        event.preventDefault();

        const updatedData = {
            id: materialId,
            name: document.getElementById('materialName').value,
            status: document.getElementById('materialStatus').value
        };

        const updateResponse = postHTTPService('/inventory/api/materials/' + materialId, 'PUT', 'json', updatedData);

        if (updateResponse.responseText && updateResponse.responseText.includes('Error')) {
            Swal.fire({
                icon: 'error',
                title: 'Error!',
                text: 'Failed to update material. Please try again.'
            });
        } else {
            Swal.fire({
                icon: 'success',
                title: 'Material Updated!',
                text: 'Material has been updated successfully.',
                timer: 2000,
                timerProgressBar: true,
                willClose: () => {
                    location.reload();
                }
            });
        }

        const modal = bootstrap.Modal.getInstance(document.getElementById('addMaterialModal'));
        if (modal) modal.hide();
    };

    const addMaterialModal = new bootstrap.Modal(document.getElementById('addMaterialModal'));
    addMaterialModal.show();
}

// Delete Material
function deleteMaterial(materialId) {
    Swal.fire({
        title: 'Are you sure?',
        text: 'This action cannot be undone.',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#3085d6',
        confirmButtonText: 'Yes, delete it!'
    }).then((result) => {
        if (result.isConfirmed) {
            const response = getHTTPService('/inventory/api/materials/' + materialId, 'DELETE', 'json');

            if (response.responseText && response.responseText.includes('Error')) {
                Swal.fire({
                    icon: 'error',
                    title: 'Error!',
                    text: 'Failed to delete material. Please try again.'
                });
            } else {
                Swal.fire({
                    icon: 'success',
                    title: 'Deleted!',
                    text: 'Material has been deleted successfully.',
                    timer: 2000,
                    timerProgressBar: true,
                    willClose: () => {
                        location.reload();
                    }
                });
            }
        }
    });
}

// Submit GRN Form
function submitGRN(event) {
    event.preventDefault();

    const grnData = {
        supplierInvoiceNo: document.getElementById('grnSupplierInvoice').value,
        batchNo: document.getElementById('grnBatchNo').value,
        receivedquantity: parseInt(document.getElementById('grnQty').value),
        units: document.getElementById('grnUnits').value,
        receivedDate: document.getElementById('grnDate').value || null,
        expiryDate: document.getElementById('grnExpiryDate').value || null,
        notes: document.getElementById('grnNotes').value || null,
        variantId: parseInt(document.getElementById('grnVariant').value),
        suppliers: { id: parseInt(document.getElementById('grnSupplier').value) },
        receivedByUser: { id: parseInt(document.getElementById('grnReceivedBy').value) }
    };

    const response = postHTTPService('/inventory/api/grn/save-full', 'POST', 'json', grnData);
    const serverMessage = response.responseJSON && response.responseJSON.message
        ? response.responseJSON.message
        : 'Goods receipt note has been saved successfully.';

    if (response.status >= 400 || (response.responseText && response.responseText.includes('Error'))) {
        Swal.fire({
            icon: 'error',
            title: 'Error!',
            text: serverMessage
        });
    } else {
        Swal.fire({
            icon: 'success',
            title: 'GRN Saved!',
            text: serverMessage,
            timer: 2000,
            timerProgressBar: true,
            willClose: () => {
                location.reload();
            }
        });
    }

    const modal = bootstrap.Modal.getInstance(document.getElementById('grnModal'));
    if (modal) modal.hide();
}

// Submit Usage Form
function submitUsage(event) {
    event.preventDefault();

    const usageData = {
        materialId: parseInt(document.getElementById('usageItem').value),
        quantityUsed: parseInt(document.getElementById('usageQty').value),
        purpose: document.getElementById('usageJob').value,
        dateUsed: document.getElementById('usageDate').value
    };

    const response = postHTTPService('/inventory/api/materials/usage', 'POST', 'json', usageData);

    if (response.responseText && response.responseText.includes('Error')) {
        Swal.fire({
            icon: 'error',
            title: 'Error!',
            text: 'Failed to record usage. Please try again.'
        });
    } else {
        Swal.fire({
            icon: 'success',
            title: 'Usage Recorded!',
            text: 'Material usage has been recorded successfully.',
            timer: 2000,
            timerProgressBar: true,
            willClose: () => {
                location.reload();
            }
        });
    }

    const modal = bootstrap.Modal.getInstance(document.getElementById('usageModal'));
    if (modal) modal.hide();
}

// Search functionality
$(document).ready(function () {
    $('#searchInventory').on('keyup', function () {
        const searchTerm = $(this).val().toLowerCase();

        $('#materialTable tbody tr').filter(function () {
            $(this).toggle($(this).text().toLowerCase().indexOf(searchTerm) > -1);
        });
    });

    $('#searchGRN').on('keyup', function () {
        const searchTerm = $(this).val().toLowerCase();

        $('#grnTable tbody tr').filter(function () {
            $(this).toggle($(this).text().toLowerCase().indexOf(searchTerm) > -1);
        });
    });

    // Reset form when modal is closed
    $('#addMaterialModal').on('hidden.bs.modal', function () {
        document.getElementById('materialFormData').reset();
        document.getElementById('addMaterialModalHeader').textContent = 'Add Material';
        document.getElementById('materialFormData').onsubmit = submitMaterial;
    });

    // Reset GRN form when modal is closed
    $('#grnModal').on('hidden.bs.modal', function () {
        document.getElementById('grnFormData').reset();
        document.getElementById('grnMaterial').value = '';
        document.getElementById('grnVariant').innerHTML = '<option selected disabled>Select Variant</option>';
        const today = new Date().toISOString().split('T')[0];
        document.getElementById('grnDate').value = today;
    });

    // Reset Usage form when modal is closed
    $('#usageModal').on('hidden.bs.modal', function () {
        document.getElementById('usageFormData').reset();
        const today = new Date().toISOString().split('T')[0];
        document.getElementById('usageDate').value = today;
    });
});

function openTab(evt, tabName) {
    var i, tabContent, tabButtons;
    tabContent = document.getElementsByClassName("tab-content");
    for (i = 0; i < tabContent.length; i++) {
        tabContent[i].style.display = "none";
    }

    tabButtons = document.getElementsByClassName("tab-btn");
    for (i = 0; i < tabButtons.length; i++) {
        tabButtons[i].className = tabButtons[i].className.replace(" active", "");
    }

    document.getElementById(tabName).style.display = "block";
    evt.currentTarget.className += " active";
}

// Default open tab
document.addEventListener("DOMContentLoaded", function () {
    const defaultTab = document.querySelector(".tab-btn");
    if (defaultTab) defaultTab.click();
});

