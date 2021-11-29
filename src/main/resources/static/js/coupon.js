var stompClient = null;

function connect() {
    var socket = new SockJS('/cart-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        console.log('Connected: '+ frame);
        stompClient.subscribe('/coupon/validation', function(coupon) {
            if(coupon !== null) {
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
    var totalPrice = $("#totalPrice").text();

    $("#coupon").show();
    $("#coupon").html('<div class="text-success"><h6 class="my-0">Coupon code</h6><small>' + coupon.couponCode + '</small></div><span class="text-success">- '+ totalPrice * coupon.discountAmount/100 +'</span>');
}

function sendCoupon() {
    stompClient.send("/app/coupon", {}, JSON.stringify( {'code': $("#couponInput").val()} ));
}

$(function () {
    $("#couponForm").on('submit', function(e) {
        e.preventDefault();
    });
    $("#validateCoupon").click(function() { sendCoupon(); });

});
