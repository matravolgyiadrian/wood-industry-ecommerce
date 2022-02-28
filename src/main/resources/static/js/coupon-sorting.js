$(function(){
    initSorts();
});

var codeCount;
var discountCount;
var expirationCount;

function initSorts(){
    codeCount = 0;
    $("#table-code").click(function(){
        checkOtherSorts("table-code");
        sortByCode(codeCount);
        codeCount = cycleThrough(codeCount, $(this));
    });
    discountCount = 0;
    $("#table-discount").click(function(){
        checkOtherSorts("table-discount");
        sortByDiscount(discountCount);
        discountCount = cycleThrough(discountCount, $(this));
    });
    expirationCount = 0;
    $("#table-expiration").click(function(){
        checkOtherSorts("table-expiration");
        sortByExpiration(expirationCount);
        expirationCount = cycleThrough(expirationCount, $(this));
    });
}

function checkOtherSorts(currentElement){
    if($(".table > thead i").length != 0 && $(".table > thead i").parent().attr("id") != currentElement) {
        clearOtherSorts(currentElement);
    }
}

function clearOtherSorts(currentElement){
    switch(currentElement){
        case "table-code":
            discountCount = 0;
            expirationCount = 0;
            $(".table > thead i").remove();
            break;
        case "table-discount":
            codeCount = 0;
            expirationCount = 0;
            $(".table > thead i").remove();
            break;
        case "table-expiration":
            codeCount = 0;
            discountCount = 0;
            $(".table > thead i").remove();
            break;
    }
}

function cycleThrough(counter, htmlElement){
    if(counter === 0){
        htmlElement.append('<i class="fas fa-sort-down fa-x text-black">');
        return ++counter;
    } else if(counter === 1){
        htmlElement.children().removeClass("fa-sort-down").addClass("fa-sort-up");
        return ++counter;
    } else {
        htmlElement.children().remove();
        return 0;
    }
}

function sortByCode(counter){
    var dataToSort = getDataToSort();
    if(counter === 0){
        sortDescCouponByCode(dataToSort);
    } else if(counter === 1){
        sortAscCouponByCode(dataToSort)
    } else {
        rebuildCouponTable(dataToSort);
        return 0;
    }
}

function sortByDiscount(counter){
    var dataToSort = getDataToSort();
    if(counter === 0){
        sortDescCouponByDiscount(dataToSort);
    } else if(counter === 1){
        sortAscCouponByDiscount(dataToSort)
    } else {
        rebuildCouponTable(dataToSort);
        return 0;
    }
}

function sortByExpiration(counter){
    var dataToSort = getDataToSort();
    if(counter === 0){
        sortDescCouponByExpiration(dataToSort);
    } else if(counter === 1){
        sortAscCouponByExpiration(dataToSort)
    } else {
        rebuildCouponTable(dataToSort);
        return 0;
    }
}

function getDataToSort(){
    if(filteredQuery.length != 0){
        return filteredQuery;
    } else {
        return queryResult.slice(0);
    }
}

function sortAscCouponByCode(data){
    data.sort(function(a, b){
        var x = a.couponCode.toLowerCase();
        var y = b.couponCode.toLowerCase();
        return x < y ? -1: x > y ? 1 : 0;
    });
    rebuildCouponTable(data);
}

function sortDescCouponByCode(data){
    data.sort(function(a, b){
        var x = a.couponCode.toLowerCase();
        var y = b.couponCode.toLowerCase();
        return x > y ? -1: x < y ? 1 : 0;
    });
    rebuildCouponTable(data);
}


function sortAscCouponByDiscount(data){
    data.sort(function(a, b){
        return a.discountAmount - b.discountAmount;
    });
    rebuildCouponTable(data);
}

function sortDescCouponByDiscount(data){
    data.sort(function(a, b){
        return b.discountAmount - a.discountAmount;
    });
    rebuildCouponTable(data);
}

function sortAscCouponByExpiration(data){
    data.sort(function(a, b){
        x = a.expirationDate;
        y = b.expirationDate;
        return x < y ? -1: x > y ? 1 : 0;

    });
    rebuildCouponTable(data);
}

function sortDescCouponByExpiration(data){
    data.sort(function(a, b){
        x = a.expirationDate;
        y = b.expirationDate;
        return x > y ? -1: x < y ? 1 : 0;
    });
    rebuildCouponTable(data);
}