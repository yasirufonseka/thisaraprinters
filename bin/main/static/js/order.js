//add tabs like structure for quotation and order
function openTab(evt, tabName) {
  // hide all content boxes
  const tabContent = document.getElementsByClassName("tab-content");
  for (let i = 0; i < tabContent.length; i++) {
    tabContent[i].style.display = "none";
  }

  // remove active class from all buttons
  const tabButtons = document.getElementsByClassName("tab-btn");
  for (let i = 0; i < tabButtons.length; i++) {
    tabButtons[i].classList.remove("active");
  }

  // show clicked content box and add active class to clicked button
  const selectedTab = document.getElementById(tabName);
  if (selectedTab) {
    selectedTab.style.display = "block";
    evt.currentTarget.classList.add("active");
  }
}
//show delivery fields based on the order type
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

//sample data
const sampleClients = [
  { id: 1, name: 'John Doe', email: 'john@example.com' },
  { id: 2, name: 'Jane Smith', email: 'jane@example.com' },
  { id: 3, name: 'Alice Johnson', email: 'alice@example.com' },
];

//open customer search model
const openClientSearch = (event, modalId) => {
  event.preventDefault();
  const modalElem = document.getElementById(modalId);
  if (modalElem) {
    const modalInstance = bootstrap.Modal.getOrCreateInstance(modalElem);
    modalInstance.show();
  }
}

//search client from the client database
function searchClient() {
  const searchClientsName = document.getElementById('searchClientName').value;
  const showSearchedClientDiv = document.getElementById('showSearchedClient');

  if (!searchClientsName) {
    showSearchedClientDiv.innerHTML = '';
    return;
  }

  // filter sample data
  const findClient = sampleClients.find(client => 
    client.name.toLowerCase().includes(searchClientsName.toLowerCase())
  );

  if (findClient) {
    // Store the full client object in the dataset for retrieval
    showSearchedClientDiv.dataset.client = JSON.stringify(findClient);

    //show searched client on a div
    showSearchedClientDiv.innerHTML = `
    <div class="col-md-12">
      <div class="card" style="cursor: pointer;">
        <div class="card-body">
          <h5 class="card-title">${findClient.name}</h5>
          <p class="card-text">${findClient.email}</p>
        </div>
      </div>
    </div>
    `;
  } else {
    // Handle case where no client is found
    delete showSearchedClientDiv.dataset.client;
    showSearchedClientDiv.innerHTML = '<p class="text-muted ms-3">No client found</p>';
  }
}

function selectedClient() {
  //get the selected client from the showSearchedClient div
  const showSearchedClientDiv = document.getElementById('showSearchedClient');

  // Check if we have a client stored
  if (showSearchedClientDiv.dataset.client) {
    const selectedClient = JSON.parse(showSearchedClientDiv.dataset.client);
    console.log("Selected Client:", selectedClient);

    // Populate the customer name field
    const customerNameInput = document.getElementById('customerName');
    if (customerNameInput) {
      customerNameInput.value = selectedClient.name;
    }

    // Close the search modal using Bootstrap API
    const modalElem = document.getElementById('searchCustomerModal');
    const modalInstance = bootstrap.Modal.getInstance(modalElem);
    if (modalInstance) {
      modalInstance.hide();
    }
  }
  else {
    alert('Please select a client');
  }
}

//get all quotations 
function getAllQuotations() {
  const url = '/quotations/get/all';
  const type = 'GET';
  const dataType = 'json';
  const data = getHTTPService(url, type, dataType);
  return data;
}

