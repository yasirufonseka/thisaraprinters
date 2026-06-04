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

//sample data
const sampleClients = [
  { id: 1, name: 'John Doe', email: 'john@example.com' },
  { id: 2, name: 'Jane Smith', email: 'jane@example.com' },
  { id: 3, name: 'Alice Johnson', email: 'alice@example.com' },
];

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
  const searchClientsName = document.getElementById('searchClientName').value;
  // console.log(searchClientsName);

  const showSearchedClientDiv = document.getElementById('showSearchedClient');

  // filter sample data
  const findClient = sampleClients.find(client => client.name.toLocaleLowerCase().includes(searchClientsName.toLocaleLowerCase()));
  // console.log(findClient);

  if (findClient) {
    // Store the full client object in the dataset for retrieval
    showSearchedClientDiv.dataset.client = JSON.stringify(findClient);

    //show searched client on a div
    showSearchedClientDiv.innerHTML = `
    <div class="col-md-12">
      <div class="card" style="cursor: pointer;">
        <div class="card-body">
          <h3 class="card-title">${findClient.name}</h3>
          <p class="card-text">${findClient.email}</p>
        </div>
      </div>
    </div>
    `;
  } else {
    // Handle case where no client is found
    delete showSearchedClientDiv.dataset.client;
    showSearchedClientDiv.innerHTML = '';
  }
}

function selectedClient() {
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
    bootstrap.Modal.getInstance(modal).hide();

    

  }
  else {
    alert('Please select a client');
  }
} 