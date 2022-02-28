$(function(){
    initSorts();
});
var nameCount;
var emailCount;
var issuedOnCount;
var addressCount;
var priceCount;
var statusCount;
function initSorts(){
    nameCount = 0;
    $("#table-name").click(function(){
        checkOtherSorts("table-name");
        sortByName(nameCount);
        nameCount = cycleThrough(nameCount, $(this));
    });
    emailCount = 0;
    $("#table-email").click(function(){
        checkOtherSorts("table-email");
        sortByEmail(emailCount);
        emailCount = cycleThrough(emailCount, $(this));
    });
    issuedOnCount = 0;
    $("#table-issuedOn").click(function(){
        checkOtherSorts("table-issuedOn");
        sortByIssuedOn(issuedOnCount);
        issuedOnCount = cycleThrough(issuedOnCount, $(this));
    });
    addressCount = 0;
    $("#table-address").click(function(){
        checkOtherSorts("table-address");
        sortByAddress(addressCount);
        addressCount = cycleThrough(addressCount, $(this));
    });
    priceCount = 0;
    $("#table-price").click(function(){
        checkOtherSorts("table-price");
        sortByPrice(priceCount);
        priceCount = cycleThrough(priceCount, $(this));
    });
    statusCount = 0;
    $("#table-status").click(function(){
        checkOtherSorts("table-status");
        sortByStatus(statusCount);
        statusCount = cycleThrough(statusCount, $(this));
    });
}

function checkOtherSorts(currentElement){
    if($(".table > thead i").length != 0 && $(".table > thead i").parent().attr("id") != currentElement) {
        clearOtherSorts(currentElement);
    }
}

function clearOtherSorts(currentElement){
    switch(currentElement){
        case "table-name":
            emailCount = 0;
            issuedOnCount = 0;
            addressCount = 0;
            priceCount = 0;
            statusCount = 0;
            $(".table > thead i").remove();
            break;
        case "table-email":
            nameCount = 0;
            issuedOnCount = 0;
            addressCount = 0;
            priceCount = 0;
            statusCount = 0;
            $(".table > thead i").remove();
            break;
        case "table-issuedOn":
            nameCount = 0;
            emailCount = 0;
            addressCount = 0;
            priceCount = 0;
            statusCount = 0;
            $(".table > thead i").remove();
            break;
        case "table-address":
            nameCount = 0;
            issuedOnCount = 0;
            emailCount = 0;
            priceCount = 0;
            statusCount = 0;
            $(".table > thead i").remove();
            break;
        case "table-price":
            nameCount = 0;
            issuedOnCount = 0;
            addressCount = 0;
            emailCount = 0;
            statusCount = 0;
            $(".table > thead i").remove();
            break;
        case "table-status":
            nameCount = 0;
            issuedOnCount = 0;
            addressCount = 0;
            emailCount = 0;
            priceCount = 0;
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

function sortByName(counter){
    var dataToSort = getDataToSort();
    if(counter === 0){
        sortDescOrderByName(dataToSort);
    } else if(counter === 1){
        sortAscOrderByName(dataToSort)
    } else {
        rebuildOrderTable(dataToSort);
        return 0;
    }
}

function sortByEmail(counter){
    var dataToSort = getDataToSort();
    if(counter === 0){
        sortDescOrderByEmail(dataToSort);
    } else if(counter === 1){
        sortAscOrderByEmail(dataToSort)
    } else {
        rebuildOrderTable(dataToSort);
        return 0;
    }
}

function sortByIssuedOn(counter){
    var dataToSort = getDataToSort();
    if(counter === 0){
        sortDescOrderByIssuedOn(dataToSort);
    } else if(counter === 1){
        sortAscOrderByIssuedOn(dataToSort)
    } else {
        rebuildOrderTable(dataToSort);
        return 0;
    }
}

function sortByAddress(counter){
    var dataToSort = getDataToSort();
    if(counter === 0){
        sortDescOrderByAddress(dataToSort);
    } else if(counter === 1){
        sortAscOrderByAddress(dataToSort)
    } else {
        rebuildOrderTable(dataToSort);
        return 0;
    }
}

function sortByPrice(counter){
    var dataToSort = getDataToSort();
    if(counter === 0){
        sortDescOrderByPrice(dataToSort);
    } else if(counter === 1){
        sortAscOrderByPrice(dataToSort)
    } else {
        rebuildOrderTable(dataToSort);
        return 0;
    }
}

function sortByStatus(counter){
    var dataToSort = getDataToSort();
    if(counter === 0){
        sortDescOrderByStatus(dataToSort);
    } else if(counter === 1){
        sortAscOrderByStatus(dataToSort)
    } else {
        rebuildOrderTable(dataToSort);
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

function sortAscOrderByName(data){
    data.sort(function(a, b){
        var x = a.customer.toLowerCase();
        var y = b.customer.toLowerCase();
        return x < y ? -1: x > y ? 1 : 0;
    });
    rebuildOrderTable(data);
}

function sortDescOrderByName(data){
    data.sort(function(a, b){
        var x = a.customer.toLowerCase();
        var y = b.customer.toLowerCase();
        return x > y ? -1: x < y ? 1 : 0;
    });
    rebuildOrderTable(data);
}

function sortAscOrderByEmail(data){
    data.sort(function(a, b){
        var x = a.email.toLowerCase();
        var y = b.email.toLowerCase();
        return x < y ? -1: x > y ? 1 : 0;
    });
    rebuildOrderTable(data);
}

function sortDescOrderByEmail(data){
    data.sort(function(a, b){
        var x = a.email.toLowerCase();
        var y = b.email.toLowerCase();
        return x > y ? -1: x < y ? 1 : 0;
    });
    rebuildOrderTable(data);
}

function sortAscOrderByIssuedOn(data){
    data.sort(function(a, b){
        return a.issuedOn - b.issuedOn;
    });
    rebuildOrderTable(data);
}

function sortDescOrderByIssuedOn(data){
    data.sort(function(a, b){
        return b.issuedOn - a.issuedOn;
    });
    rebuildOrderTable(data);
}

function sortAscOrderByAddress(data){
    data.sort(function(a, b){
        var x = a.shippingAddress.toLowerCase();
        var y = b.shippingAddress.toLowerCase();
        return x < y ? -1: x > y ? 1 : 0;
    });
    rebuildOrderTable(data);
}

function sortDescOrderByAddress(data){
    data.sort(function(a, b){
        var x = a.shippingAddress.toLowerCase();
        var y = b.shippingAddress.toLowerCase();
        return x > y ? -1: x < y ? 1 : 0;
    });
    rebuildOrderTable(data);
}

function sortAscOrderByPrice(data){
    data.sort(function(a, b){
        return a.totalPrice - b.totalPrice;
    });
    rebuildOrderTable(data);
}

function sortDescOrderByPrice(data){
    data.sort(function(a, b){
        return b.totalPrice - a.totalPrice;
    });
    rebuildOrderTable(data);
}

function sortAscOrderByStatus(data){
    data.sort(function(a, b){
        var x = a.status.toLowerCase();
        var y = b.status.toLowerCase();
        return x < y ? -1: x > y ? 1 : 0;
    });
    rebuildOrderTable(data);
}

function sortDescOrderByStatus(data){
    data.sort(function(a, b){
        var x = a.status.toLowerCase();
        var y = b.status.toLowerCase();
        return x > y ? -1: x < y ? 1 : 0;
    });
    rebuildOrderTable(data);
}