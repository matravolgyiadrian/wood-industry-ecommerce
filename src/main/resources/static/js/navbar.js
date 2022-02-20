function initProductTableSearch(){
    $("#keyword").on("keyup", function(){
            var value = $(this).val();
            var data = filterProducts(value, queryResult);
            rebuildProductTable(data);
        });
}

function initProductCardSearch(){
    $("#keyword").on("keyup", function(){
            var value = $(this).val();
            var data = filterProducts(value, queryResult);
            rebuildProductCards(data);
        });
}

function initOrderSearch(){
    $("#keyword").on("keyup", function(){
            var value = $(this).val();
            var data = filterOrders(value, queryResult);
            rebuildOrderTable(data);
        });
}

function initCouponSearch(){
    $("#keyword").on("keyup", function(){
            var value = $(this).val();
            var data = filterCoupons(value, queryResult);
            rebuildCouponTable(data);
        });
}


function filterProducts(value, data){
    var filteredData = [];
    for(var i = 0; i<data.length ; i++){
        value = value.toLowerCase();
        var name = data[i].name.toLowerCase();

        if(name.includes(value)){
            filteredData.push(data[i]);
        }
    }

    return filteredData;
}

function filterOrders(value, data){
    var filteredData = [];
    for(var i = 0; i<data.length ; i++){
        value = value.toLowerCase();
        var customer = data[i].customer.toLowerCase();
        var email = data[i].email.toLowerCase();

        if(customer.includes(value) || email.includes(value)){
            filteredData.push(data[i]);
        }
    }

    return filteredData;
}

function filterCoupons(value, data){
    var filteredData = [];
    for(var i = 0; i<data.length ; i++){
        value = value.toLowerCase();
        var couponCode = data[i].couponCode.toLowerCase();

        if(couponCode.includes(value)){
            filteredData.push(data[i]);
        }
    }

    return filteredData;
}

function rebuildProductCards(data){
    var wrapper = $("#product");
    wrapper.html('');
    if(data.length == 0 && $("#no-result").length == 0){
        $("h3").after('<div id="no-result" class="alert alert-info">There is no result</div>')
    }
    if(data.length != 0 && $("#no-result").length != 0){
        $("#no-result").remove();
    }
    for(var i = 0; i< data.length; i++){
        var card = `
                <div class="card shadow col-3">
                    <img src="${data[i].imageUrl}"
                         class="card-img-top" alt="..." onerror="this.src='https://thumbs.dreamstime.com/b/no-image-available-icon-flat-vector-no-image-available-icon-flat-vector-illustration-132482953.jpg'">
                    <div class="card-body text-center">
                        <h5 class="card-title">${data[i].name}</h5>`;
        if(data[i].stock>0){
            card += `<p class="mt-5"><span style="background-color: green;" class="indicator"></span> In Stock</p>`;
        } else {
            card += `<p class="mt-5"><span style="background-color: red;" class="indicator"></span> Out of Stock</p>`;
        }
        card += `   </div>
                    <form action="/cart/add" method="post" autocomplete="off" class="cart-wrapper">
                        <input type="hidden" name="id" value="${data[i].id}">
                        <input class="form-control" name="quantity" type="hidden" value="1">

                        <div class="">
                            <div class="h-bg">
                                <div class="h-bg-inner"></div>
                            </div>
                            <button class="cart" type="submit"
                                    style="border: none; padding: 0; font: inherit; outline:inherit; cursor: pointer; background: none;">
                                <span class="price">${toCurrency(data[i].price)}</span>
                                <span class="add-to-cart">
                          <span class="txt">Add to cart</span>
                        </span>
                            </button>
                        </div>
                    </form>
                </div>`;

        wrapper.append(card);
    }
}

function rebuildProductTable(products){
    var table = $("#productTable");
    table.html('');
    for(var i = 0; i < products.length; i++){
        var row = `<tr>
                            <td>${products[i].name}</td>
                            <td>${toCurrency(products[i].price)}</td>
                            <td>${products[i].stock}</td>
                            <td>
                                <a href="${'/product/edit/' + products[i].id}" class="btn btn-primary">Edit</a>
                            </td>
                            <td>
                                <form class="m-auto" style="width: min-content;"
                                action="${'/product/stop-order/' + products[i].id}" method="post">`;
        console.log(products[i].stopOrder);
        if(products[i].stopOrder){
            row += `<input class="btn btn-danger" type="submit" value="Stop Order" disabled="disabled"}>`;
        } else {
            row += `<input class="btn btn-danger" type="submit" value="Stop Order"}>`;
        }

        row +=`</form>
                            </td>
                            <td>
                                <form class="m-auto" style="width: min-content;"
                                action="${'/product/delete/' + products[i].id}" method="post">
                                    <input class="btn btn-danger" type="submit" value="Delete">
                                </form>
                            </td>
                      </tr>`;
        table.append(row);
    }
}

function rebuildOrderTable(orders){
    var table = $("#orderTable");
    table.html('');
    for(var i = 0; i < orders.length; i++){
        var row = `<tr th:each="order: ${orders}">
                       <td>${orders[i].customer}</td>
                       <td>${orders[i].email}</td>
                       <td>
                           <ul class="list-unstyled">`;
        for(var j = 0; j < orders[i].products.length; j++){
            row += `<li>${orders[i].products[j].product.name + ' x ' + orders[i].products[j].quantity}</li>`;
        }
        row +=`</ul>
                       </td>
                       <td>${orders[i].issuedOn.slice(0, 10)}</td>
                       <td>${orders[i].shippingAddress}</td>
                       <td>${toCurrency(orders[i].totalPrice)}</td>
                       <td>${orders[i].status}</td>
                       <td>
                            <a href=${'/order/status/' + orders[i].id} class="btn btn-outline-dark">Change Status</a>
                       </td>
                   </tr>`;
        table.append(row);
    }
}

function rebuildCouponTable(coupons){
    var table = $("#couponTable");
    table.html('');
    for(var i = 0; i < coupons.length; i++){
        var row = ` <tr>
                        <td>${coupons[i].couponCode}</td>
                        <td>${coupons[i].discountAmount}</td>`;
        if(coupons[i].expirationDate != null){
            row += `<td>${coupons[i].expirationDate}</td>`;
        } else {
            row += `<td>None</td>`;
        }

        row += `        <td><a href=${'/coupon/edit/'+ coupons[i].id} class="btn btn-outline-dark">Edit item</a></td>
                        <td>
                            <form action=${'/coupon/delete/' + coupons[i].id} method="post">
                                <input class="btn btn-outline-dark" type="submit" value="Delete">
                            </form>
                        </td>
                    </tr>`;
        table.append(row);
    }
}

function toCurrency(number) {
    return number.toFixed(2).replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1 ").replace(/\./g, ",")+" Ft";
}
