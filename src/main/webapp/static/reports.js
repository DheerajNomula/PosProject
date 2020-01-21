function getBaseUrl(){
 	var baseUrl = $("meta[name=baseUrl]").attr("content")
 	return baseUrl;
 }
function getReportsUrl(){
    return getBaseUrl() + "/api/reports";
}
var dictionary={};
function resetForm(){
    $("#sales-form").trigger("reset");
    $("#sales-form input[name=brandName]").val("");
    $("#sales-form input[name=brandCategory]").val("");
}

function displayBrand(){
    resetForm();

    var baseUrl=getBaseUrl();
    var url=baseUrl+'/api/brand/';
    $.ajax({
        url:url,
        type:'GET',
        success:function(data){
            showAllbrands(data);
            //showAllCategories(date);
        },
        error:handleAjaxError
    });
}
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
    $("#selectBrandName").append('<option value="" selected>Select one of the Brand</option>');
    for(var key in dictionary){
        var row="<option>"+key+"</option>";
        $("#selectBrandName").append(row);
    }
    initSelectBrandCategory();
}
function initSelectBrandCategory(){
    $('#selectBrandCategory').empty();
    $("#selectBrandCategory").append('<option value="" selected>Select one of the Category</option>');
//    console.log(dictionary);
    var categories={};
    for(var key in dictionary){
            var brandCategories=dictionary[key];
            for(var category in brandCategories){
                if(!categories.hasOwnProperty(category))
                     categories[category]=1;
            }
    }
    //console.log(categories);
    for(var key in categories){
        var row="<option>"+key+"</option>";
        $("#selectBrandCategory").append(row);
    }
    $('#sales-modal').modal('toggle');
}
function generateSalesRepo(){
    var url=getReportsUrl()+'/'+'sales';

    var $form=$('#sales-form');

    var json=toJson($form);
    //console.log(json);

    $.ajax({
    	   url: url,
    	   type: 'POST',
    	   data: json,
    	   headers: {
           	'Content-Type': 'application/json'
           },
    	   success: function(response) {
    	   		printSales(response);//recursion - one by one sending by post
    	   },
    	   error: handleAjaxError
    	});
}
function printSales(data){
    var $thead=$('#show-table').find('thead');
    $thead.empty();
    var tableheader='<tr><th scope="col">Brand Category</th><th scope="col">Quantity</th><th scope="col">Revenue</th></tr>';
    $thead.append(tableheader);
    var $tbody=$('#show-table').find('tbody');
    $tbody.empty();

     for(var i in data){
        var row=data[i];
        var tablerow='<tr>'
                                		+ '<td>' + row.brandCategory + '</td>'
                                		+ '<td>'  + row.quantity + '</td>'
                                		+ '<td>'  + row.revenue + '</td>'
                                		+ '</tr>';
        $tbody.append(tablerow);
     }
     $('#sales-modal').modal("hide");

}
function showAllCategories(brand){
    //selectedBrand=brand;
    $('#selectBrandCategory').empty();
    var brandCategories=dictionary[brand];
    $("#selectBrandCategory").append('<option value="" selected>Select one of the Category</option>');
    for(var key in brandCategories){
        var row="<option>"+key+"</option>";
        $("#selectBrandCategory").append(row);
    }
}
function generateInventoryRepo(){
    var url=getReportsUrl()+'/'+'inventory';
    $.ajax({
            url:url,
            type:'GET',
            success:function(data){
                printInventory(data);
                //showAllCategories(date);
            },
            error:handleAjaxError
        });
}
function printInventory(data){
    var $thead=$('#show-table').find('thead');
        $thead.empty();
        var tableheader='<tr><th scope="col">Brand Name</th><th scope="col">Brand Category</th><th scope="col">Quantity</th></tr>';
        $thead.append(tableheader);
        var $tbody=$('#show-table').find('tbody');
        $tbody.empty();

         for(var i in data){
            var row=data[i];
            var tablerow='<tr>'
                                    		+ '<td>' + row.brandName + '</td>'
                                    		+ '<td>'  + row.brandCategory + '</td>'
                                    		+ '<td>'  + row.quantity + '</td>'
                                    		+ '</tr>';
            $tbody.append(tablerow);
         }
         //$('#sales-modal').modal("hide");
}
function generateBrandRepo(){
    var url=getReportsUrl()+'/'+'brand';
        $.ajax({
                url:url,
                type:'GET',
                success:function(data){
                    printBrand(data);
                    //showAllCategories(date);
                },
                error:handleAjaxError
            });
}
function printBrand(data){
    var $thead=$('#show-table').find('thead');
        $thead.empty();
        var tableheader='<tr><th scope="col">Brand Id</th><th scope="col">Brand Name</th><th scope="col">Brand Category</th></tr>';
        $thead.append(tableheader);
        var $tbody=$('#show-table').find('tbody');
        $tbody.empty();

         for(var i in data){
            var row=data[i];
            var tablerow='<tr>'
                                             + '<td>'  + row.id + '</td>'

                                    		+ '<td>' + row.brandName + '</td>'
                                    		+ '<td>'  + row.brandCategory + '</td>'
                                    		+ '</tr>';
            $tbody.append(tablerow);
         }
         //$('#sales-modal').modal("hide");
}
function init(){
    $('#selectBrandName').change(function(){
        var selectedBrand=$('#selectBrandName option:selected').val();
        $('#sales-form input[name=brandName]').val(selectedBrand);
        showAllCategories(selectedBrand);
        }
    );
    $('#selectBrandCategory').change(function(){
        var selectedCategory=$('#selectBrandCategory option:selected').val();
        $('#sales-form input[name=brandCategory]').val(selectedCategory);
    }
    );
    $('#salesReport').click(displayBrand);
    $('#generateSalesRepo').click(generateSalesRepo);
    $('#inventoryReport').click(generateInventoryRepo);
    $('#brandReport').click(generateBrandRepo);
    //$('#inventoryReport').click(generateInventoryRepo);
    //$('#brandReport').click(generateBrandRepo);
}
$(document).ready(init);
//$(document).ready(displayBrand);

