function getInventoryUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/inventory";
}

function getInventoryData(){
    var url=getInventoryUrl();
    $.ajax({
        url:url,
        type:'GET',
        success:function(inventoryData){
            displayInventoryData(inventoryData);
        },
        error:handleAjaxError
    });
}

function displayInventoryData(inventoryData){
    var $tbody=$('#inventory-table').find('tbody');
    $tbody.empty();
//    console.log(inventoryData);
    for(let i in inventoryData){
        var inventory=inventoryData[i];
        var buttonHtml='<button onclick="displayEditInventory('+inventory.id+')">Update</button>';
        var tablerow='<tr>'
                        		+ '<td>' + inventory.barcode + '</td>'
                        		+ '<td>'  + inventory.brandName + '</td>'
                                + '<td>'  + inventory.brandCategory + '</td>'
                        		+ '<td>' + inventory.productName + '</td>'
                        		+ '<td>'  + inventory.quantity + '</td>'
                        		+ '<td>' + buttonHtml + '</td>'
                        		+ '</tr>';
        $tbody.append(tablerow);
    }
}
function displayEditInventory(id){
    var url=getInventoryUrl()+'/'+id;
    $.ajax({
        url:url,
        type:'GET',
        success:function(data){
            editInventory(data,id);
        },
        error:handleAjaxError
    });
}

function editInventory(inventoryData,id){
    $('#inventory-edit-form input[name=quantity]').val(inventoryData.quantity);
    $('#inventory-edit-form input[name=id]').val(id);
    $('#inventory-edit-form input[name=id]').prop('readonly', true);
    $('#inventory-edit-form input[name=barcode]').val(inventoryData.barcode);

    $('#edit-inventory-modal').modal('toggle');
}
function updateInventory(event){
    var id=$('#inventory-edit-form input[name=id]').val();
    var url=getInventoryUrl()+'/'+id;
    var qty=Number($('inventory-edit-form inputp[name=quantity').val());
    if(qty<0){alert('Quantity cannot be negative');return;}
    var $form=$('#inventory-edit-form');
    var json=toJson($form);
    $.ajax({
        url:url,
        type:'PUT',
        data:json,
        headers:{
            'Content-Type':'application/json'
        },
        success:function(response){
            $('#edit-inventory-modal').modal("hide");
            getInventoryData();
        },
        error:handleAjaxError
    });
    return false;
}
// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;

function displayUploadData(){
 	resetUploadDialog();
	$('#upload-inventory-modal').modal('toggle');
}
function resetUploadDialog(){
	//Reset file name
	var $file = $('#inventoryFile');//choose file input id
	$file.val('');
	$('#inventoryFileName').html("Choose File");
	//Reset various counts
	processCount = 0;
	fileData = [];
	errorData = [];
	//Update counts
	updateUploadDialog();
}

function updateUploadDialog(){
	$('#rowCount').html("" + fileData.length);
	$('#processCount').html("" + processCount);
	$('#errorCount').html("" + errorData.length);
}

//when file is uploaded
function processData(){
	var file = $('#inventoryFile')[0].files[0];
	//console.log(file)
	readFileData(file, readFileDataCallback); // one is dedault function , we are passing function as a param
}

function readFileDataCallback(results){
	fileData = results.data;
	//console.log(results.data);
	uploadRows();
}

function uploadRows(){
	//Update progress
	updateUploadDialog();
	//If everything processed then return
	if(processCount==fileData.length){
		return;
	}

	//Process next row
	var row = fileData[processCount];
	processCount++;

	var json = JSON.stringify(row);
	var url = getInventoryUrl();

	//Make ajax call
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		uploadRows();   //recursion - one by one sending by post
	   },
	   error: function(response){
	   		row.error=response.responseText
	   		errorData.push(row);
	   		uploadRows();
	   }
	});

}

function downloadErrors(){
	writeFileData(errorData);
}

function updateFileName(){
	var $file = $('#inventoryFile');
	var fileName = $file.val();
	$('#inventoryFileName').html(fileName);
}

function init(){
    $('#refresh-data').click(getInventoryData);
    $('#update-inventory').click(updateInventory);
    $('#upload-data').click(displayUploadData);//for file
    $('#process-data').click(processData);
    $('#download-errors').click(downloadErrors);
    $('#inventoryFile').on('change', updateFileName);
    $('#cancel-inupload').click(getInventoryData);
}
$(document).ready(init);
$(document).ready(getInventoryData);
