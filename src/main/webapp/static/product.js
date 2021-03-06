function getProductUrl(){
 	var baseUrl = $("meta[name=baseUrl]").attr("content")
 	return baseUrl + "/api/product";
 }
function getBaseUrl(){
 	var baseUrl = $("meta[name=baseUrl]").attr("content")
 	return baseUrl;
 }
function refreshDataForm(){
    $('#product-form')[0].reset();
    displayBrand();
}
function addProduct(event){
    var $form=$('#product-form');

    var brandId=$('#product-form input[name=brandId]').val();

    var json=toJson($form);
    var url=getProductUrl();
    if(Number.isNaN(brandId) || brandId<=0){
        alert('Enter correct details !');
        return;
    }
    if(!correctMrp($('#product-form input[name=mrp]').val()))
    {
        return false;
    }
    $.ajax({
        url:url,
        type:'POST',
        data:json,
        headers:{
            'Content-Type':'application/json'
            },
         success:function(response){
            refreshDataForm();
            getProductList();
         },
         error:handleAjaxError
    });
    return false;
}

function getProductList(){
    var url=getProductUrl();
    $.ajax({
        url:url,
        type:'GET',
        success:function(productsData){
            displayProductList(productsData);
        },
        error:handleAjaxError
    });
}

function displayProductList(productsData){
    var $tbody=$('#product-table').find('tbody');
    $tbody.empty();
    for(var i in productsData){
        var product =productsData[i];
        var buttonHtml='<button onclick="displayEditProduct('+product.id+')">Edit</button>';

        var tablerow='<tr>'
                        		+ '<td>' + product.id + '</td>'
                        		+ '<td>'  + product.productName + '</td>'
                        		+ '<td>' + product.brandName + '</td>'
                        		+ '<td>' + product.brandCategory + '</td>'
                        		+ '<td>'  + product.barcode + '</td>'
                        		+ '<td>'  + product.mrp + '</td>'
                        		+ '<td>' + buttonHtml + '</td>'
                        		+ '</tr>';
        $tbody.append(tablerow);
    }
}
function displayEditProduct(id){
    var url=getProductUrl()+'/'+id;

    $.ajax({
        url:url,
        type:'GET',
        success:function(data){
            displayProduct(data);//displays the update form with existing values
        }
    });
}
function displayProduct(data){
    console.log('in edit(id) sub fun');
    console.log(data);

     $("#product-edit-form input[name=productName]").val(data.productName);
     $("#product-edit-form input[name=barcode]").val(data.barcode);
     $("#product-edit-form input[name=mrp]").val(data.mrp);
     $('#product-edit-form input[name=updateId]').val(data.id);
     $('#product-edit-form input[name=brandId]').val(dictionary[data.brandName][data.brandCategory]);
     //originalBrandIdEdit=data.brandId;
      selectedProductsBrandCategory=data.brandCategory;
      selectedProductsBrandName=data.brandName;
     displayBrandInEdit();
     $('#edit-product-modal').modal('toggle');//to open the pop up window
}

///////////////////////////////////////////////////////////
var selectedProductsBrandCategory;
var selectedProductsBrandName;
function displayBrandInEdit(){
    var baseUrl=getBaseUrl();
    var url=baseUrl+'/api/brand/';
    $.ajax({
        url:url,
        type:'GET',
        success:function(data){
            showAllbrandsInEdit(data);

        },
        error:handleAjaxError
    });
}
function showAllbrandsInEdit(data){
    createDictionary(data);
    $('#updateBrandName').empty();
    $("#updateBrandName").append('<option selected>'+selectedProductsBrandName+'</option>');
    showAllCategoriesInEdit(selectedProductsBrandName,0)
    for(var key in dictionary){
        if(key==selectedProductsBrandName)continue;
        var row="<option>"+key+"</option>";
        $("#updateBrandName").append(row);
    }
}

function showAllCategoriesInEdit(brand,freshUpdate){
    selectedBrand=brand;
    var brandCategories=dictionary[brand];
    $('#updateBrandCategory').empty();
    if(freshUpdate==0)
    $("#updateBrandCategory").append('<option selected >'+selectedProductsBrandCategory+'</option>');
    else{
    $('#product-edit-form input[name=brandId]').val(NaN);
    $("#updateBrandCategory").append('<option value="" selected disabled hidden> Select one of the Category</option>');}
    for(var key in brandCategories){
        if(key==selectedProductsBrandCategory)continue;
        var row="<option>"+key+"</option>";
        $("#updateBrandCategory").append(row);
    }
}
///////////////////////////////////////////////////////////////////
function displayBrand(){
    $("#product-form").trigger("reset");
    $("#product-edit-form").trigger("reset");
    var baseUrl=getBaseUrl();
    var url=baseUrl+'/api/brand/';
    $.ajax({
        url:url,
        type:'GET',
        success:function(data){
            showAllbrands(data);
            getProductList();
        },
        error:handleAjaxError
    });
}
var dictionary={};
function createDictionary(data){
    for(var i in data){
            var brand=data[i];
            if(!dictionary.hasOwnProperty(brand.brandName))
                dictionary[brand.brandName]={};
            dictionary[brand.brandName][brand.brandCategory]=brand.id;
        }
}
function showAllbrands(data){
    createDictionary(data);
    $('#selectBrandName').empty();
    $("#selectBrandName").append('<option value="" selected disabled hidden>Select one of the Brand</option>');
    for(var key in dictionary){
        var row="<option>"+key+"</option>";
        $("#selectBrandName").append(row);
    }
    initSelectBrandCategory();
}
function initSelectBrandCategory(){
    $('#selectBrandCategory').empty();
    $("#selectBrandCategory").append('<option value="" selected disabled hidden>Select one of the Category</option>');
}
function showAllCategories(brand){
    selectedBrand=brand;
    var brandCategories=dictionary[brand];
    initSelectBrandCategory();
    for(var key in brandCategories){
        var row="<option>"+key+"</option>";
        $("#selectBrandCategory").append(row);
    }
}
///////////////////////////////////////
function correctMrp(val){
    //console.log(newMrp);
    //console.log(newMrp.match( re ));
        /*var re = /^[+]?[0-9]+\.[0-9]+$/;
        if(!newMrp.match(re)){
            alert('Enter valid mrp in front ');
            return false;
         }
        return true;*/
        var floatRegex = /^[+]?[0-9]+\.?[0-9]+$/;
            if (!floatRegex.test(val)){
                alert('Please enter correct mrp');
                return false;
                }

            val = parseFloat(val);
            if (isNaN(val)){
                alert('Please enter correct mrp');
                return false;}
            return true;
}
function updateProduct(event){
    var id=$('#product-edit-form input[name=updateId]').val();
    var brandId=$('#product-edit-form input[name=brandId]').val();
    var url=getProductUrl()+'/'+id;
    var $form=$('#product-edit-form');
    var json=toJson($form);
    if(Number.isNaN(Number(brandId))){
        alert('Enter correct brand details');
        return false;
    }
    if(!correctMrp($('#product-edit-form input[name=mrp]').val())){
        return false;
    }
    $.ajax({
        url:url,
        type:'PUT',
        data:json,
        headers:{
            'Content-Type':'application/json'
        },
        success:function(response){
            $('#edit-product-modal').modal("hide");
            getProductList();
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
	$('#upload-product-modal').modal('toggle');
}
function resetUploadDialog(){
	//Reset file name
	var $file = $('#productFile');//choose file input id
	$file.val('');
	$('#productFileName').html("Choose File");
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
	var file = $('#productFile')[0].files[0];
	readFileData(file, readFileDataCallback); // one is dedault function , we are passing function as a param
}
function readFileDataCallback(results){
	fileData = results.data;
	uploadRows();
}
function uploadRows(){
	updateUploadDialog();
	console.log("start");
	console.log(processCount);
	if(processCount==fileData.length){
    		return;
    }

	var row = fileData[processCount];
	processCount++;
	//createDictionary();
	console.log(row);
	console.log(fileData.length);
	var error=0;
	if(!dictionary.hasOwnProperty(row["brandName"])){
	    row.error="brand name does not exists";
        error++;
	}
	if(error==0 && !dictionary[row["brandName"]].hasOwnProperty(row["brandCategory"])){
    	    row.error="brand category does not exist";
            error++;
    }

    if(error!=0){
        errorData.push(row);
        uploadRows();
        return;
    }

//	console.log(row["brandName"],row["brandCategory"],dictionary[row["brandName"]][row["brandCategory"]]);
	row["brandId"]=dictionary[row["brandName"]][row["brandCategory"]];
//	console.log(row,typeof(row),typeof(dictionary[row["brandName"]][row["brandCategory"]]));
	delete row["brandName"];
    delete row["brandCategory"];


	var json = JSON.stringify(row);
	var url = getProductUrl();

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
	var $file = $('#productFile');
	var fileName = $file.val();
	$('#productFileName').html(fileName);
}

function init(){
    $('#selectBrandName').change(function(){
        var selectedBrand=$('#selectBrandName option:selected').val();
        showAllCategories(selectedBrand);
    }
    );
    $('#selectBrandCategory').change(function(){
            var selectedCategory=$('#selectBrandCategory option:selected').val();
            var selectedId=dictionary[selectedBrand][selectedCategory];
            $('#product-form input[name=brandId]').val(Number(selectedId));
        }
    );
    $('#add-product').click(addProduct);
    $('#refresh-data').click(getProductList);

    $('#updateBrandName').change(function(){
            var selectedBrand=$('#updateBrandName option:selected').val();
            showAllCategoriesInEdit(selectedBrand,1);
        }
    );
    $('#updateBrandCategory').change(function(){
                var selectedCategory=$('#updateBrandCategory option:selected').val();
                var selectedId=dictionary[selectedBrand][selectedCategory];
                $('#product-edit-form input[name=brandId]').val(Number(selectedId));

            }
        );
     $('#update-product').click(updateProduct);
     $('#upload-data').click(displayUploadData);
     $('#process-data').click(processData);
     $('#download-errors').click(downloadErrors);
     $('#productFile').on('change', updateFileName);
     $('#cancel-inupload').click(getProductList);
}

$(document).ready(init);
$(document).ready(displayBrand);
//$(document).ready(getProductList);