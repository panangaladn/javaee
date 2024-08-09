import { saveCustomer, getAllCustomers, updateCustomer, deleteCustomer } from '../model/CustomerModel.js';

$(document).ready(function() {
    refresh(); // Load customers when the page is loaded
});

document.querySelector('#CustomerManage #customerForm').addEventListener('submit', function(event) {
    event.preventDefault();
});




$('#CustomerManage .saveBtn').click(function() {
    const customer = {
        id: $('#CustomerManage .custId').val(),
        name: $('#CustomerManage .custName').val(),
        address: $('#CustomerManage .custAddress').val(),
        salary: parseInt($('#CustomerManage .custSalary').val(), 10)
    };

    if (validate(customer)) {
        sendRequest('POST', 'http://localhost:8080/Pos_system_BackEnd/customer', customer, function(response) {
            alert('Customer Saved');
            refresh(); // Refresh table after saving a customer
        });
    }
});




$('#CustomerManage .updateBtn').click(function() {
    const customer = {
        id: $('#CustomerManage .custId').val(),
        name: $('#CustomerManage .custName').val(),
        address: $('#CustomerManage .custAddress').val(),
        salary: parseInt($('#CustomerManage .custSalary').val(), 10)
    };

    if (validate(customer)) {
        sendRequest('PUT', `http://localhost:8080/Pos_system_BackEnd/customer`, customer, function(response) {
            alert('Customer Updated');
            refresh(); // Refresh table after updating a customer
        });
    }
});

$('#CustomerManage .removeBtn').click(function() {
    let id = $('#CustomerManage .custId').val();
    if (!id) {
        alert('Customer ID is required');
        return;
    }
    sendRequest('DELETE', `http://localhost:8080/Pos_system_BackEnd/customer?id=${id}`, null, function(response) {
        alert('Customer Deleted');
        refresh(); // Refresh table after deleting a customer
    });
});

$('#CustomerManage .searchBtn').click(function() {
    let id = $('#CustomerManage .custId').val();
    if (!id) {
        alert('Customer ID is required');
        return;
    }
    sendRequest('GET', `http://localhost:8080/Pos_system_BackEnd/customer?id=${id}`, null, function(response) {
        let customer = JSON.parse(response);
        if (customer) {
            $('#CustomerManage .custName').val(customer.name);
            $('#CustomerManage .custAddress').val(customer.address);
            $('#CustomerManage .custSalary').val(customer.salary);
        } else {
            alert('Customer Not Found');
        }
    });
});

function sendRequest(method, url, data, callback) {
    const http = new XMLHttpRequest();
    http.onreadystatechange = function() {
        if (http.readyState === 4) {
            console.log(`Response received: ${http.responseText}`);
            if (http.status === 200 || http.status === 201 || http.status === 204) {
                callback(http.responseText);
            } else {
                alert('Operation Failed: ' + http.status);
            }
        }
    };

    http.open(method, url, true);
    if (method === 'POST' || method === 'PUT') {
        http.setRequestHeader('Content-Type', 'application/json');
        http.send(JSON.stringify(data));
    } else {
        http.send();
    }
}

function validate(customer) {
    let valid = true;

    if (/^C0[0-9]+$/.test(customer.id)) {
        $('#CustomerManage .invalidCustId').text('');
    } else {
        $('#CustomerManage .invalidCustId').text('Invalid Customer Id');
        valid = false;
    }

    if (/^(?:[A-Z][a-z]*)(?: [A-Z][a-z]*)*$/.test(customer.name)) {
        $('#CustomerManage .invalidCustName').text('');
    } else {
        $('#CustomerManage .invalidCustName').text('Invalid Customer Name');
        valid = false;
    }

    if (/^[A-Z][a-z, ]+$/.test(customer.address)) {
        $('#CustomerManage .invalidCustAddress').text('');
    } else {
        $('#CustomerManage .invalidCustAddress').text('Invalid Customer Address');
        valid = false;
    }

    if (customer.salary && customer.salary > 0) {
        $('#CustomerManage .invalidCustSalary').text('');
    } else {
        $('#CustomerManage .invalidCustSalary').text('Invalid Customer Salary');
        valid = false;
    }

    let customers = getAllCustomers();
    for (let i = 0; i < customers.length; i++) {
        if (customers[i].id === customer.id) {
            $('#CustomerManage .invalidCustId').text('Customer Id Already Exists');
            valid = false;
            break;
        }
    }

    return valid;
}

function loadTable(customers) {
    $('#CustomerManage .tableRow').empty();
    customers.forEach(customer => {
        $('#CustomerManage .tableRow').append(
            `<tr>
                <td>${customer.id}</td>
                <td>${customer.name}</td>
                <td>${customer.address}</td>
                <td>${customer.salary}</td>
            </tr>`
        );
    });
}

function extractNumber(id) {
    var match = id.match(/C0(\d+)/);
    if (match && match.length > 1) {
        return parseInt(match[1]);
    }
    return null;
}

function createCustomerId() {
    let customers = getAllCustomers();

    if (!customers || customers.length === 0) {
        return 'C01';
    } else {
        let lastCustomer = customers[customers.length - 1];
        let id = lastCustomer && lastCustomer.id ? lastCustomer.id : 'C00';

        let number = extractNumber(id);
        number++;
        let newId = 'C0' + number;
        console.log("Generated Customer ID: ", newId);
        return newId;
    }
}

function refresh() {
    $('#CustomerManage .custId').val(createCustomerId());
    $('#CustomerManage .custName').val('');
    $('#CustomerManage .custAddress').val('');
    $('#CustomerManage .custSalary').val('');
    $('#CustomerManage .invalidCustId').text('');
    $('#CustomerManage .invalidCustName').text('');
    $('#CustomerManage .invalidCustAddress').text('');
    $('#CustomerManage .invalidCustSalary').text('');

    sendRequest('GET', 'http://localhost:8080/Pos_system_BackEnd/customer', null, function(response) {
        try {
            let customers = JSON.parse(response);
            loadTable(customers);
        } catch (e) {
            console.error("Error parsing JSON response: ", e);
        }
    });
}



$('#CustomerManage .clearBtn').click(function() {
    refresh();
});

$('#CustomerManage .tableRow').on('click', 'tr', function() {
    let id = $(this).children('td:eq(0)').text();
    let name = $(this).children('td:eq(1)').text();
    let address = $(this).children('td:eq(2)').text();
    let salary = $(this).children('td:eq(3)').text();

    $('#CustomerManage .custId').val(id);
    $('#CustomerManage .custName').val(name);
    $('#CustomerManage .custAddress').val(address);
    $('#CustomerManage .custSalary').val(salary);
});
