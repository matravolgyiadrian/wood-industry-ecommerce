$(function () {
$(".quantity-control").click(e => handleQuantity(e));
});

function handleQuantity(event){
    var isIncrement = event.target.id.slice(0, 8) == "increase";
    var id = event.target.id.slice(9);

    $.post("/cart/item/quantity", {'increment': isIncrement, 'id': id}, function(data) {
        var qty = parseInt($("#qty-"+id).html());
        var subTotal = parseFromCurrency($("#subTotal").html());
        var productPrice = parseFromCurrency($("#productPrice-"+id).html());
        var productTotalPrice = parseFromCurrency($("#productTotalPrice-"+id).html());

        if(data == "++"){
            qty++;
            subTotal += productPrice;
            productTotalPrice += productPrice;
        } else if(data == "--"){
            if(qty == 1){
                $("#delete-"+id).submit();
            }
            qty--;
            subTotal -= productPrice;
            productTotalPrice -= productPrice;
        } else {
            var toastElList = [].slice.call(document.querySelectorAll('.toast'));
            var toastList = toastElList.map(function(toastEl) {
                return new bootstrap.Toast(toastEl)
            })
            toastList.forEach(toast => toast.show());
        }

        $("#qty-"+id).html(qty);
        $("#subTotal").html(toCurrency(subTotal));
        $("#productTotalPrice-"+id).html(toCurrency(productTotalPrice));
        });
}

function parseFromCurrency(input){
    return parseFloat(input.replace(/&nbsp;|Ft|\s/g, ""));
}

function toCurrency(number) {
    return number.toFixed(2).replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1 ").replace(/\./g, ",")+" Ft";
}