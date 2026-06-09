let materialList = [];

// Show / hide the custom size height & width fields based on dropdown selection
function toggleCustomSize() {
  const select = document.getElementById('productSize');
  const wrap   = document.getElementById('customSizeWrap');
  if (!select || !wrap) return;

  if (select.value === 'custom') {
    wrap.style.display = 'block';
  } else {
    wrap.style.display = 'none';
    const heightInput = document.getElementById('customSizeHeight');
    const widthInput  = document.getElementById('customSizeWidth');
    if (heightInput) heightInput.value = '';
    if (widthInput)  widthInput.value  = '';
  }
}

//add tabs like structure for quatation and order
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

function toggleDeliveryFields() {
  const orderType = document.getElementById('orderType').value;
  const deliveryFields = document.querySelectorAll('.delivery-field');

  deliveryFields.forEach(field => {
    if (orderType === 'delivery') {
      field.style.display = 'block';
    } else {
      field.style.display = 'none';
    }
  });
}


//open customer search model
const openClientSearch = (event, modalId) => {
  event.preventDefault();
  const modal = document.getElementById(modalId);
  modal.style.display = 'block';
  modal.classList.add('show');
  modal.setAttribute('aria-hidden', 'false');
  modal.setAttribute('aria-modal', 'true');
  modal.setAttribute('role', 'dialog');
  modal.setAttribute('tabindex', '-1');

}
//close customer search  model
const closeSearchModel = (event, modelId) => {
  event.preventDefault();
  const modal = document.getElementById(modelId);
  modal.style.display = 'none';
  modal.classList.remove('show');
  //modal.setAttribute('aria-hidden', 'true');
  modal.setAttribute('aria-modal', 'false');
  modal.setAttribute('role', 'dialog');
  modal.setAttribute('tabindex', '0');
}

//search client from the client database
function searchClient() {

  const globalCustomerList = window.globalCustomer; //
  const searchClientsName = document.getElementById('clientName').value.trim();
  // console.log(searchClientsName);

  const showSearchedClientDiv = document.getElementById('showSearchedClient');

  // filter sample data
  const findClient = globalCustomerList.filter(cus =>cus.name.toLowerCase().includes(searchClientsName.toLowerCase()) )
  console.log(findClient);

  if (findClient.length >0) {
    showSearchedClientDiv.dataset.client = JSON.stringify(findClient[0]);

    // Store the full client object in the dataset for retrieval
    showSearchedClientDiv.innerHTML = findClient.map(client=>`

    <div class="col-md-12">
      <div class="card" style="cursor: pointer;">
        <div class="card-body">
          <h3 class="card-title">${client.name}</h3>
          <p class="card-text">${client.email}</p>
        </div>
      </div>
    </div>`).join("");

  } else {
    // Handle case where no client is found
    delete showSearchedClientDiv.dataset.client;
    showSearchedClientDiv.innerHTML = '<p class="text-muted">No client found.</p>';
  }

}

function selectedClient() {
  const model = document.getElementById('searchCustomerModal')
  //get the selected client from the showSearchedClient div
  const showSearchedClient = document.getElementById('showSearchedClient');

  // Check if we have a client stored (divs don't have .value)
  if (showSearchedClient.dataset.client) {
    const selectedClient = JSON.parse(showSearchedClient.dataset.client);
    console.log(selectedClient);

    // Populate the customer name field
    const customerNameInput = document.getElementById('customerName');
    if (customerNameInput) {
      customerNameInput.value = selectedClient.name;
    }

    // Optional: Close the modal after selection
    const modal = document.getElementById('searchCustomerModal');
    modal.style.display = 'none';
    modal.classList.remove('show');
  //  modal.setAttribute('aria-hidden', 'true');
    modal.setAttribute('aria-modal', 'false');
    modal.setAttribute('role', 'dialog');
    modal.setAttribute('tabindex', '0');

    //Using the existing close logic if accessible or bootstrap methods
      bootstrap.Modal.getOrCreateInstance(modal).hide();

    

  }
  else {
    alert('Please select a client');
  }
}

function makeMaterialList(){
  const select = document.getElementById('materials');
  const selectedValue = select.value;
  const selectedText = select.options[select.selectedIndex].text;
  if (!selectedValue) return;

  // Avoid duplicates
  if(materialList.some(m => m.id === selectedValue)) return;

  // Add to array with both id and name
  materialList.push({ id: selectedValue, name: selectedText });
  renderMaterialList();
}

function renderMaterialList() {
  const div = document.getElementById('selectedMaterial');
  div.innerHTML = materialList.map((m, index) => `
        <div data-id="${m.id}">
            ${m.name}
            <span onclick="removeMaterial(${index})" style="cursor:pointer">✕</span>
        </div>
    `).join('');
}

function removeMaterial(index) {
  materialList.splice(index, 1);
  renderMaterialList();
}

// Show cost-per-sheet input when a radio group has a selection (e.g. Binding)
function toggleCostInput(wrapId) {
  const wrap = document.getElementById(wrapId);
  if (wrap) {
    wrap.style.display = 'block';
  }
}

// Show cost-per-sheet input when at least one checkbox in a group is ticked (e.g. Cutting, Foiling)
function toggleCostInputByCheckboxGroup(wrapId, checkboxIds) {
  const anyChecked = checkboxIds.some(id => {
    const el = document.getElementById(id);
    return el && el.checked;
  });
  const wrap = document.getElementById(wrapId);
  if (wrap) {
    wrap.style.display = anyChecked ? 'block' : 'none';
    if (!anyChecked) {
      const input = wrap.querySelector('input[type="number"]');
      if (input) input.value = '';
    }
  }
}

// Show the correct lamination cost input based on which radio group (Thermal vs Normal) is selected
function toggleLaminationCost() {
  const thermalSelected = document.getElementById('laminationThermalGloss').checked ||
                          document.getElementById('laminationThermalMat').checked;
  const normalSelected  = document.getElementById('laminationNormalGloss').checked ||
                          document.getElementById('laminationNormalMat').checked;

  const thermalWrap = document.getElementById('laminationThermalCostWrap');
  const normalWrap  = document.getElementById('laminationNormalCostWrap');

  if (thermalWrap) {
    thermalWrap.style.display = thermalSelected ? 'block' : 'none';
    if (!thermalSelected) {
      const input = thermalWrap.querySelector('input[type="number"]');
      if (input) input.value = '';
    }
  }
  if (normalWrap) {
    normalWrap.style.display = normalSelected ? 'block' : 'none';
    if (!normalSelected) {
      const input = normalWrap.querySelector('input[type="number"]');
      if (input) input.value = '';
    }
  }
}