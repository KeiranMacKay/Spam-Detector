// TODO: onload function should retrieve the data needed to populate the UI

// Function to open side panel
function openNav() {
  document.getElementById("side panel").style.width = "250px";
  document.getElementById("button").style.marginLeft = "250px";
  //document.getElementById("header").style.marginLeft = "250px";
}

// Function to close side panel
function closeNav() {
  document.getElementById("side panel").style.width = "0";
  document.getElementById("button").style.marginLeft= "0";
  //document.getElementById("header").style.marginLeft= "0";
}

// Function to append data to table
function add_record(tableId, data) {

  // this is an example on how to get the value of an `<input>` tag
  //   you do not have to use this layout

  //const input_name = document.getElementById("name");
  //const value_name = input_name.value;

  // TO DO ...

  let tableRef = document.getElementById(tableId);
  for (c in data.spam)
  {
    let newRow = tableRef.insertRow(-1);
    let fileCell = newRow.insertCell(0);
    let spamProbabilityCell = newRow.insertCell(1);
    let actualClassCell = newRow.insertCell(2);
    let fileText = document.createTextNode(data.spam[c].filename);
    let spamProbabilityText = document.createTextNode(data.spam[c].spamProbability);
    let actualClassText = document.createTextNode(data.spam[c].actualClass);

    fileCell.appendChild(filename);
    spamProbabilityCell.appendChild(spamProbability);
    actualClassCell.appendChild(actualClass);

  }
}

let apiCallURL = "http://localhost:8080/spamDetector-1.0/api/spam/json";

/**
 * Function makes a HTTP request to an API
 * **/
function requestData(callURL){
  fetch(callURL, {
    method: 'GET',
    headers: {
      'Accept': 'application/json',
    },
  })
    .then(response => response.json())
    .then(response => add_record("chart", response));
}

(function () {
  // your page initialization code here
  // the DOM will be available here
  // console.log(customerData);
  // addDataToTable("customers");
  fetch(apiCallURL)
    /** fetching some data **/
    .catch((err) => {
      console.log("something went wrong: " + err);
    });
  requestData(apiCallURL);

})();
