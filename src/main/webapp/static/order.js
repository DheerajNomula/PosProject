function getOrderUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/order";
}

function getOrderItemDetails(){
    // barcode pass as param
    var barcode=$('#order-form input[name=barcode]').val();
    var url=getOrderUrl()+'/'+barcode;
    $.ajax({
        url:url,
        type:'GET',
        success:function(orderData){
            checkForQuantity(orderData);
        },
        error:handleAjaxError
    });
}
function checkPreviouslyBarcode(orderData){
    var requiredQuantity=Number($('#order-form input[name=quantity]').val());
    if(orderItemsTable.hasOwnProperty(orderData.barcode)){
    alert('Barcode already exists,updating the quantity');

        if(orderItemsTable[orderData.barcode].quantity+requiredQuantity<orderData.quantity){
        requiredQuantity+=orderItemsTable[orderData.barcode].quantity;
        }
     }
    return requiredQuantity;
}

function checkForQuantity(orderData){
    var requiredQuantity=checkPreviouslyBarcode(orderData);
    if(requiredQuantity>orderData.quantity)
    {
        alert('Entered Quantity : '+requiredQuantity+' but existing quantity: '+orderData.quantity);
        return false;
    }
    orderData.quantity=requiredQuantity;

    addToDisplayTable(orderData);
}
var orderItemsTable={};

function printTable(){

    var $tbody=$('#order-table').find('tbody');
    $tbody.empty();
    let serialNo=1;
    var totalAmount=0;
    for(let key in orderItemsTable){
        orderData=orderItemsTable[key];
        orderData.barcode.trim();
        var buttonHtml='<button class="btn btn-secondary"onclick="editOrderItem(\''+orderData.barcode+'\')">Edit</button>';
        buttonHtml+='<div class="divider"/>'
        buttonHtml+='<button class="btn btn-danger" onclick="deleteOrder(\''+orderData.barcode+'\') ">Delete</button>';
        var tablerow='<tr>'
                                + '<td>' + serialNo+ '</td>'
                                + '<td>'  + orderData.barcode + '</td>'
                                + '<td>'  + orderData.productName + '</td>'
                                + '<td>' + orderData.quantity + '</td>'
                                + '<td  class="price-value"> '  + orderData.mrp + '</td>'
                                + '<td  class="price-value">' + orderData.mrp*orderData.quantity + '</td>'
                                + '<td>'+ buttonHtml+'<td>'
                                + '</tr>';
        $tbody.append(tablerow);
        totalAmount+=orderData.mrp*orderData.quantity;
        serialNo++;
    }
    $('#totalAmount').val(totalAmount);
}

function addToDisplayTable(orderData){
    //console.log(orderData);// quantity should change
    orderItemsTable[orderData["barcode"]]=orderData;
    printTable();
    $('#order-form input[name=barcode]').val("");
    $('#order-form input[name=quantity]').val("");
}

function deleteOrder(barcode){
    console.log(barcode);
    delete orderItemsTable[barcode];

    printTable();
}

function editOrderItem(barcode){
    console.log(barcode);
    $('#orderItem-edit-form input[name=barcode]').val(barcode);
    $('#orderItem-edit-form input[name=productName]').val(orderItemsTable[barcode].productName);
    $('#orderItem-edit-form input[name=mrp]').val(orderItemsTable[barcode].mrp);
    $('#orderItem-edit-form input[name=quantity]').val(orderItemsTable[orderData.barcode].quantity)
    $('#edit-order-modal').modal('toggle');
}
function checkForQuantityAndDisplay(orderData){

    var availableQuantity=orderData.quantity;
        var requiredQuantity=$('#orderItem-edit-form input[name=quantity]').val();

        if(requiredQuantity>availableQuantity)
        {
            alert('Entered Quantity : '+requiredQuantity+' but existing quantity: '+orderData.quantity);
            return false;
        }
        orderData.quantity=requiredQuantity;


        var updatedQuantity=$('#orderItem-edit-form input[name=quantity]').val();
        var barcode=$('#orderItem-edit-form input[name=barcode]').val();
        orderItemsTable[barcode].quantity=updatedQuantity;

        //quantity.html(updatedQuantity);
        printTable();
        $('#edit-order-modal').modal("hide");
}

function allowAllfields(){
    $("#btnSubmit").attr("disabled", false);
    $("#add-order-item").attr("disabled",false);
    $('#add-order').attr("disabled",false);
    $('#order-form input[name=barcode]').attr("disabled",false);
    $('#order-form input[name=quantity]').attr("disabled",false);//add allow customer invoice
}

function addOrderItem(){

    var barcodeLength=$.trim($('#order-form input[name=barcode]').val()).length; // check for null
    var quantityLength=$.trim($('#order-form input[name=quantity]').val()).length;
    if(barcodeLength ==0 || quantityLength==0 || $('#order-form input[name=quantity]').val()==0)
    {
        alert('Please enter the values correctly');
        return false;
    }
    getOrderItemDetails();
}
function updateOrderItem(){
    var barcode=$('#orderItem-edit-form input[name=barcode]').val();
    var url=getOrderUrl()+'/'+barcode;
    $.ajax({
        url:url,
        type:'GET',
        success:function(orderData){
            checkForQuantityAndDisplay(orderData);

        },
        error:handleAjaxError
    });

}

function clearAll(){
    orderItemsTable={};
    $("#order-form").trigger("reset");
    var $tbody=$('#order-table').find('tbody');
    $tbody.empty();
    $('#totalAmount').val("");
    allowAllfields();
}

function completeOrder(){
    if(Object.keys(orderItemsTable).length ==0 ){
        alert("Please enter the order items");
        return false;
    }
    var form=[];

    for(let barcode in orderItemsTable){
        var orderItem=orderItemsTable[barcode];
        var item={};
        item["quantity"]=orderItem.quantity;
        item["barcode"]=orderItem.barcode;
        form.push(item);
    }
    var json=JSON.stringify(form);
    var url=getOrderUrl();
    $.ajax({
        url:url,
        type:'POST',
        data:json,
        headers:{
            'Content-Type':'application/json'
        },
        success:function(response){
            clearAll();
        },
        error:handleAjaxError
    })
}
function disableButtons(){
    $("#add-order").attr("disabled", true);
        $('#order-form input[name=barcode]').attr("disabled",true);
        $('#order-form input[name=quantity]').attr("disabled",true);
        $('#add-order-item').attr("disabled",true);
        //disable creting customer invoice

}
function showAllOrders(){//get call
    disableButtons();
    var url=getOrderUrl();

    $.ajax({
       url:url,
       type:'GET',
       success:function(data){
            displayAllOrders(data);
       },
       error:handleAjaxError
    });
}
function printTotalRow(totalAmount){
    var $tbody=$('#order-table').find('tbody');
    tablerow='<tr><td></td><td colspan="4">Total Amount</td><td colspan="2">Rs.'+totalAmount;
    $tbody.append(tablerow);
}
function printEmptyRow(){
     var $tbody=$('#order-table').find('tbody');
    tablerow='<tr class="blank_row"><td colspan="7"></td>';
        $tbody.append(tablerow);
}
function displayAllOrders(ordersData){
    var $tbody=$('#order-table').find('tbody');
    $tbody.empty();
    var orderNo=0;
    var serialNo=1;
    var buttonHtml;
    var totalAmount=0;
    var tablerow;
    for(let i in ordersData){
        console.log(ordersData[i]);
        order=ordersData[i];
        if(orderNo!=order.orderId){


            if(orderNo!=0){
            printTotalRow(totalAmount);
            totalAmount=0;
            printEmptyRow();
            }
            orderNo=order.orderId;
            //buttonHtml='<button class="btn btn-danger>Paid</button>';
            tablerow='<tr class="table-active">'
                            +'<td>ORDER ID: </td>'+'<td>'+orderNo+'</td>'+'<td></td><td></td><td></td><td></td><td></td>';
            serialNo=1;
            $tbody.append(tablerow);
        }

        buttonHtml='<button class="btn btn-danger disabled>Paid</button>';
        tablerow='<tr>'
                        + '<td>' + serialNo+ '</td>'
                        + '<td>'  + order.barcode + '</td>'
                        + '<td>'  + order.productName + '</td>'
                        + '<td>' + order.quantity + '</td>'
                        + '<td class="price-value">'  + order.mrp + '</td>'
                        + '<td>' + order.mrp*order.quantity + '</td>'
                        + '<td>'+ buttonHtml+'<td>'
                        + '</tr>';
        totalAmount+=order.mrp*order.quantity;
        serialNo++;

        $tbody.append(tablerow);

        }
        printTotalRow(totalAmount);
}

function init(){
    $('#add-order-item').click(addOrderItem);
    $('#update-orderItem').click(updateOrderItem);
    $('#create-new-order').click(clearAll);
    $('#add-order').click(completeOrder);
    $('#show-all-orders').click(showAllOrders);
}
$(document).ready(init);
//$(document).ready(getInventoryDa  ta);
