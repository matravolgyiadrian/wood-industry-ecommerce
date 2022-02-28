$(function(){
    initSorts();
});

var nameCount;
var priceCount;
var stockCount;

function initSorts(){
    nameCount = 0;
    $("#table-name").click(function(){
        checkOtherSorts("table-name");
        sortByName(nameCount);
        nameCount = cycleThrough(nameCount, $(this));
    });
    priceCount = 0;
    $("#table-price").click(function(){
        checkOtherSorts("table-price");
        sortByPrice(priceCount);
        priceCount = cycleThrough(priceCount, $(this));

    });
    stockCount = 0;
    $("#table-stock").click(function(){
        checkOtherSorts("table-stock")
        sortByStock(stockCount);
        stockCount = cycleThrough(stockCount, $(this));

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
            priceCount = 0;
            stockCount = 0;
            $(".table > thead i").remove();
            break;
        case "table-price":
            nameCount = 0;
            stockCount = 0;
            $(".table > thead i").remove();
            break;
        case "table-stock":
            nameCount = 0;
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
        sortDescProductByName(dataToSort);
    } else if(counter === 1){
        sortAscProductByName(dataToSort)
    } else {
        rebuildProductTable(dataToSort);
        return 0;
    }
}

function sortByPrice(counter){
    var dataToSort = getDataToSort();
    if(counter === 0){
        sortDescProductByPrice(dataToSort);
    } else if(counter === 1){
        sortAscProductByPrice(dataToSort)
    } else {
        rebuildProductTable(dataToSort);
        return 0;
    }
}

function sortByStock(counter){
    var dataToSort = getDataToSort();
    if(counter === 0){
        sortDescProductByStock(dataToSort);
    } else if(counter === 1){
        sortAscProductByStock(dataToSort)
    } else {
        rebuildProductTable(dataToSort);
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

function sortAscProductByName(data){
    data.sort(function(a, b){
        var x = a.name.toLowerCase();
        var y = b.name.toLowerCase();
        return x < y ? -1: x > y ? 1 : 0;
    });
    rebuildProductTable(data);
}

function sortDescProductByName(data){
    data.sort(function(a, b){
        var x = a.name.toLowerCase();
        var y = b.name.toLowerCase();
        return x > y ? -1: x < y ? 1 : 0;
    });
    rebuildProductTable(data);
}

function sortAscProductByPrice(data){
    data.sort(function(a, b){
        return a.price - b.price;
    });
    rebuildProductTable(data);
}
function sortDescProductByPrice(data){
    data.sort(function(a, b){
        return b.price - a.price;
    });
    rebuildProductTable(data);
}
function sortAscProductByStock(data){
    data.sort(function(a, b){
        return a.stock - b.stock;
    });
    rebuildProductTable(data);
}
function sortDescProductByStock(data){
    data.sort(function(a, b){
        return b.stock - a.stock;
    });
    rebuildProductTable(data);
}