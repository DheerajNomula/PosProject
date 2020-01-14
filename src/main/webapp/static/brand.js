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
        var buttonHtml='<button onclick="deleteBrand('+brand.id+')">delete</button>';
        buttonHtml+='<button onclick="displayEditBrand('+brand.id+')">edit</button>';
        //console.log(buttonHtml);
        var tablerow='<tr>'
                		+ '<td>' + brand.id + '</td>'
                		+ '<td>' + brand.brandName + '</td>'
                		+ '<td>'  + brand.brandCategory + '</td>'
                		+ '<td>' + buttonHtml + '</td>'
                		+ '</tr>';
                        $tbody.append(tablerow);
    }
}

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

function deleteBrand(id){
    var url=getBrandUrl()+'/'+id;
    $.ajax({
        url:url,
        type:'DELETE',
        success:function(data){
            getBrandList();
        },
        error:handleAjaxError
    });
}

function updateBrand(event){
    //$('#edit-employee-modal').modal('toggle'); see why?

    var id=$('#brand-edit-form input[name=updateId]').val();
    //var brandName=$('#brand-edit-form input[name=brandName]').val();
    var url=getBrandUrl()+'/'+id;

    var $form=$('#brand-edit-form');
    var json=toJson($form);
    //console.log(json);
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



function init(){
	$('#add-brand').click(addBrand);
	$('#update-brand').click(updateBrand); // for toggle window to update
	$('#refresh-data').click(getBrandList);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#employeeFile').on('change', updateFileName)
}
$(document).ready(init);
$(document).ready(getBrandList);
