function getBrandUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brand";
}

function addBrand(event){
    var $form=$("#brand-form");
    var json=toJson($form);
    var url=getBrandUrl();

    $.ajax({
        url:url,
        type:'POST',
        data:json,
        headers:{
            'Content-Type':'application/json'
        },
        success:function(response){
            getBrandList();
        },
        error:handleAjaxError
    });
    return false;
}

function getBrandList(){
    var url=getBrandUrl();
    $.ajax({
        url:url,
        type:'GET',
        success:function(brandsData){ //data - json data of all the brands
            //console.log(brandsData);
            displayBrandList(brandsData);
        },
        error:handleAjaxError
    });
}

function displayBrandList(brandsData){
    var $tbody=$('#brand-table').find('tbody');
    $tbody.empty();
    for(var i in brandsData){
        var brand=brandsData[i];
        var buttonHtml='<button onclick="displayEditBrand('+brand.id+')">edit</button>';
        //console.log(buttonHtml);
        var tablerow='<tr>'
                		+ '<td>' + brand.brandName + '</td>'
                		+ '<td>'  + brand.brandCategory + '</td>'
                		+ '<td>' + buttonHtml + '</td>'
                		+ '</tr>';
                        $tbody.append(tablerow);
    }
    $("#brand-form").trigger('reset');
}
//                		+ '<td>' + brand.id + '</td>'

function displayEditBrand(id){
    var url=getBrandUrl()+'/'+id;
    $.ajax({
        url:url,
        type:'GET',
        success:function(data){
            displayBrand(data);
        },
        error:handleAjaxError
    });
}

function displayBrand(data){
	$("#brand-edit-form input[name=brandName]").val(data.brandName);
	$("#brand-edit-form input[name=brandCategory]").val(data.brandCategory);
	$("#brand-edit-form input[name=updateId]").val(data.id);
	$('#edit-brand-modal').modal('toggle');
}


function updateBrand(event){
    //$('#edit-employee-modal').modal('toggle'); see why?
    var id=$('#brand-edit-form input[name=updateId]').val();
    var url=getBrandUrl()+'/'+id;

    var $form=$('#brand-edit-form');
    var json=toJson($form);
    $.ajax({
        url:url,
        type:'PUT',
        data:json,
        headers:{
            'Content-Type':'application/json'
        },
        success:function(response){
            $('#edit-brand-modal').modal("hide");
            getBrandList();
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
	$('#upload-brand-modal').modal('toggle');
}
function resetUploadDialog(){
	//Reset file name
	var $file = $('#brandFile');//choose file input id
	$file.val('');
	$('#brandFileName').html("Choose File");
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
	var file = $('#brandFile')[0].files[0];
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
	var url = getBrandUrl();

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
	var $file = $('#brandFile');
	var fileName = $file.val();
	$('#brandFileName').html(fileName);
}

function init(){
	$('#add-brand').click(addBrand);
	$('#update-brand').click(updateBrand); // for toggle window to update
	$('#refresh-data').click(getBrandList);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#brandFile').on('change', updateFileName);
    $('#cancel-inupload').click(getBrandList);
}
$(document).ready(init);
$(document).ready(getBrandList);
