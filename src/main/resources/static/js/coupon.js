var stompClient = null;

function connect() {
    var socket = new SockJS('/cart-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        console.log('Connected: '+ frame);
        stompClient.subscribe('/coupon/validation', function(coupon) {
            console.log("Subscription has something! coupon: " + coupon);
            console.log("Is the coupon valid? " + coupon.body.valid);
            if(coupon.body.valid) {
                showCoupon(JSON.parse(coupon.body));
                console.log("JSON coupon: "+ coupon.body)
            } else {
                $("#coupon").removeClass("d-flex").addClass("d-none");
                $("#couponInput").prop("placeholder", "INVALID CODE");
                $("#couponForm").css("border", "2px solid red");
                $("#couponInput").val("");
            }
        });
    });
}

function showCoupon(coupon) {
    var totalPrice = parseFloat($("#totalPrice").text(), 10);
    var discount = parseFloat(totalPrice * coupon.multiplier);

    $("#coupon").removeClass("d-none").addClass("d-flex");
    $("#coupon").html('<div class="text-success"><h6 class="my-0">Coupon code</h6><small>' + coupon.couponCode + '</small></div><span class="text-success">- '+ discount +'</span>');
    $("grandTotal").html(totalPrice * coupon.discountMultiplier);
    $("hiddenDiscountMultiplier").html(parseFloat((100-coupon.discountAmount)/100));

    $("#couponInput").prop("placeholder", "COUPON CODE");
    $("#couponForm").css("border", "1px solid rgba(0,0,0,.125)");
    $("#couponInput").val("");

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
