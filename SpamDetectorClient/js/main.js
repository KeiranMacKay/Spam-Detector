// TODO: onload function should retrieve the data needed to populate the UI
function openNav() {
  document.getElementById("side panel").style.width = "250px";
  document.getElementById("button").style.marginLeft = "250px";
  //document.getElementById("header").style.marginLeft = "250px";
}

function closeNav() {
  document.getElementById("side panel").style.width = "0";
  document.getElementById("button").style.marginLeft= "0";
  //document.getElementById("header").style.marginLeft= "0";
}


function add_record(tableId, data) {

  // this is an example on how to get the value of an `<input>` tag
  //   you do not have to use this layout

  //const input_name = document.getElementById("name");
  //const value_name = input_name.value;

  // TO DO ...

  let tableRef = document.getElementById(tableId);
  for (c in data.students)
  {
    let newRow = tableRef.insertRow(-1);
    let nameCell = newRow.insertCell(0);
    let idCell = newRow.insertCell(1);
    let gpaCell = newRow.insertCell(2);
    let nameText = document.createTextNode(data.students[c].file);
    let idText = document.createTextNode(data.students[c].SpamProbability);
    let gpaText = document.createTextNode(data.students[c].Class);

    nameCell.appendChild(file);
    idCell.appendChild(SpamProbability);
    gpaCell.appendChild(Class);

  }
}

let apiCallURL = "http://localhost:8080/spamDetector-1.0/api/spam";

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
