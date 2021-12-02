var stompClient = null;

function connect() {
    var socket = new SockJS('/cart-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        console.log('Connected: '+ frame);
        stompClient.subscribe('/coupon/validation', function(coupon) {
            console.log("Subscription has something! coupon: " + coupon);
            if(coupon.body.valid) {
                showCoupon(JSON.parse(coupon.body));
                console.log("JSON coupon: "+ coupon.body)
            } else {
                $("#couponInput").prop("placeholder", "INVALID CODE");
                $("#couponInput").css("border-color", "red");
            }
        });
    });
}

function showCoupon(coupon) {
    var totalPrice = parseInt($("#totalPrice").text(), 10);
    var discount = totalPrice * coupon.discountMultiplier;

    $("#coupon").removeAttr('hidden');
    $("#coupon").html('<div class="text-success"><h6 class="my-0">Coupon code</h6><small>' + coupon.couponCode + '</small></div><span class="text-success">- '+ discount +'</span>');
    $("grandTotal").html(totalPrice - discount);
    $("hiddenDiscountMultiplier").html((100-coupon.discountAmount)/100);
}

function sendCoupon() {
    stompClient.send("/app/coupon", {}, JSON.stringify( {'code': $("#couponInput").val()} ));
}

$(function () {
    $("#couponForm").on('submit', function(e) {
        e.preventDefault();
    });
    $("#validateCoupon").click(function() { sendCoupon(); });
    $("#hiddenDiscountMultiplier").html("1");
    connect();
});
