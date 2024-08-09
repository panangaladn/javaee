import { saveItem, getAllItems, updateItem, deleteItem } from '../model/ItemModel.js';

$(document).ready(function() {
    refresh(); // Load items when the page is loaded
});

document.querySelector('#ItemManage #ItemForm').addEventListener('submit', function(event) {
    event.preventDefault();
});

$('#ItemManage .saveBtn').click(function() {
    const item = {
        itemId: $('#ItemManage .itemId').val(),
        itemName: $('#ItemManage .itemName').val(),
        itemQty: $('#ItemManage .itemQty').val(),
        itemPrice: parseInt($('#ItemManage .itemPrice').val(), 10)
    };

    console.log('Saving item:', item); 

    if (validate(item)) {
        sendRequest('POST', 'http://localhost:8080/Pos_system_BackEnd/item', item, function(response) {
            alert('Item Saved');
            refresh(); 
        });
    }
});

$('#ItemManage .updateBtn').click(function() {
    const item = {
        itemId: $('#ItemManage .itemId').val(),
        itemName: $('#ItemManage .itemName').val(),
        itemQty: $('#ItemManage .itemQty').val(),
        itemPrice: parseInt($('#ItemManage .itemPrice').val(), 10)
    };

    console.log('Updating item:', item); 

    if (validate(item)) {
        sendRequest('PUT', 'http://localhost:8080/Pos_system_BackEnd/item', item, function(response) {
            alert('Item Updated');
            refresh(); 
        });
    }
});

$('#ItemManage .deleteBtn').click(function() {
    const id = $('#ItemManage .itemId').val();
    console.log('Deleting item with ID:', id); 
    if (!id) {
        alert('Item ID is required');
        return;
    }
    sendRequest('DELETE', `http://localhost:8080/Pos_system_BackEnd/item?id=${id}`, null, function(response) {
        alert('Item Deleted');
        refresh(); 
    });
});

$('#ItemManage .searchBtn').click(function() {
    const id = $('#ItemManage .itemId').val();
    console.log('Searching item with ID:', id); 
    if (!id) {
        alert('Item ID is required');
        return;
    }
    sendRequest('GET', `http://localhost:8080/Pos_system_BackEnd/item?id=${id}`, null, function(response) {
        const item = JSON.parse(response);
        if (item) {
            $('#ItemManage .itemName').val(item.itemName);
            $('#ItemManage .itemQty').val(item.itemQty);
            $('#ItemManage .itemPrice').val(item.itemPrice);
        } else {
            alert('Item Not Found');
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

function validate(item) {
    let valid = true;

    if (/^I0[0-9]+$/.test(item.itemId)) {
        $('#ItemManage .invalidCode').text('');
    } else {
        $('#ItemManage .invalidCode').text('Invalid Item Id');
        valid = false;
    }

    if (/^(?:[A-Z][a-z]*)(?: [A-Z][a-z]*)*$/.test(item.itemName)) {
        $('#ItemManage .invalidName').text('');
    } else {
        $('#ItemManage .invalidName').text('Invalid Item Name');
        valid = false;
    }

    if (item.itemQty != null && item.itemQty > 0) {
        $('#ItemManage .invalidQty').text('');
    } else {
        $('#ItemManage .invalidQty').text('Invalid Item Quantity');
        valid = false;
    }

    if (item.itemPrice != null && item.itemPrice > 0) {
        $('#ItemManage .invalidPrice').text('');
    } else {
        $('#ItemManage .invalidPrice').text('Invalid Item Price');
        valid = false;
    }

    return valid;
}

function loadTable(items) {
    $('#ItemManage .tableRow').empty();
    items.forEach(item => {
        $('#ItemManage .tableRow').append(
            `<tr>
                <td>${item.itemId}</td>
                <td>${item.itemName}</td>
                <td>${item.itemQty}</td>
                <td>${item.itemPrice}</td>
            </tr>`
        );
    });
}

function extractNumber(id) {
    var match = id.match(/I0(\d+)/);
    if (match && match.length > 1) {
        return parseInt(match[1]);
    }
    return null;
}

function createItemId() {
    let items = getAllItems();

    if (!items || items.length === 0) {
        return 'I01';
    } else {
        let lastItem = items[items.length - 1];
        let id = lastItem && lastItem.itemId ? lastItem.itemId : 'I00';

        let number = extractNumber(id);
        number++;
        let newId = 'I0' + number;
        console.log("Generated Item ID: ", newId);
        return newId;
    }
}

function refresh() {
    $('#ItemManage .itemId').val(createItemId());
    $('#ItemManage .itemName').val('');
    $('#ItemManage .itemQty').val('');
    $('#ItemManage .itemPrice').val('');
    $('#ItemManage .invalidCode').text('');
    $('#ItemManage .invalidName').text('');
    $('#ItemManage .invalidQty').text('');
    $('#ItemManage .invalidPrice').text('');

    sendRequest('GET', 'http://localhost:8080/Pos_system_BackEnd/item', null, function(response) {
        try {
            let items = JSON.parse(response);
            loadTable(items);
        } catch (e) {
            console.error("Error parsing JSON response: ", e);
        }
    });
}

$('#ItemManage .clearBtn').click(function() {
    refresh();
});

$('#ItemManage .tableRow').on('click', 'tr', function() {
    const id = $(this).children('td:eq(0)').text();
    const name = $(this).children('td:eq(1)').text();
    const qty = $(this).children('td:eq(2)').text();
    const price = $(this).children('td:eq(3)').text();

  
    $('#ItemManage .itemId').val(id);
    $('#ItemManage .itemName').val(name);
    $('#ItemManage .itemQty').val(qty);
    $('#ItemManage .itemPrice').val(price);
    
  
    $('#ItemManage .saveBtn').prop('disabled', true); 
    $('#ItemManage .updateBtn').prop('disabled', false); 
    $('#ItemManage .deleteBtn').prop('disabled', false); 
});
