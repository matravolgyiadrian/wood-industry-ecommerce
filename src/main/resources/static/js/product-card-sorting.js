$(function(){
    initSorts();
});

function initSorts(){
    $("#filter-name-az").click(function(){
        checkOtherSorts("filter-name-az");
        sortAscByName();
    });
    $("#filter-name-za").click(function(){
        checkOtherSorts("filter-name-za");
        sortDescByName();
    });
    $("#filter-price-lh").click(function(){
        checkOtherSorts("filter-price-lh");
        sortAscByPrice();
    });
    $("#filter-price-hl").click(function(){
        checkOtherSorts("filter-price-hl");
        sortDescByPrice();
    });
}

function checkOtherSorts(currentElement){
    if($(".dropdown-menu").find("a.active") != 0 && $(".dropdown-menu").find("a.active").attr("id") != currentElement) {
        clearOtherSorts(currentElement);
    }
}

function clearOtherSorts(currentElement){
    $(".dropdown-item").removeClass("active");
}

function getDataToSort(){
    if(filteredQuery.length != 0){
        return filteredQuery;
    } else {
        return queryResult.slice(0);
    }
}

function sortAscByName(){
    var dataToSort = getDataToSort();
    if($("#filter-name-az").hasClass("active")){
        $("#filter-name-az").removeClass("active");
        rebuildProductCards(dataToSort);
    } else {
        $("#filter-name-az").addClass("active");
        sortAscProductByName(dataToSort);
    }
}

function sortDescByName(){
    var dataToSort = getDataToSort();
    if($("#filter-name-za").hasClass("active")){
        $("#filter-name-za").removeClass("active");
        rebuildProductCards(dataToSort);
    } else {
        $("#filter-name-za").addClass("active");
        sortDescProductByName(dataToSort);
    }
}

function sortAscByPrice(){
    var dataToSort = getDataToSort();
    if($("#filter-price-lh").hasClass("active")){
        $("#filter-price-lh").removeClass("active");
        rebuildProductCards(dataToSort);
    } else {
        $("#filter-price-lh").addClass("active");
        sortAscProductByPrice(dataToSort);
    }
}

function sortDescByPrice(){
    var dataToSort = getDataToSort();
    if($("#filter-price-hl").hasClass("active")){
        $("#filter-price-hl").removeClass("active");
        rebuildProductCards(dataToSort);
    } else {
        $("#filter-price-hl").addClass("active");
        sortDescProductByPrice(dataToSort);
    }
}

function sortAscProductByName(data){
    data.sort(function(a, b){
        var x = a.name.toLowerCase();
        var y = b.name.toLowerCase();
        return x < y ? -1: x > y ? 1 : 0;
    });
    rebuildProductCards(data);
}

function sortDescProductByName(data){
    data.sort(function(a, b){
        var x = a.name.toLowerCase();
        var y = b.name.toLowerCase();
        return x > y ? -1: x < y ? 1 : 0;
    });
    rebuildProductCards(data);
}

function sortAscProductByPrice(data){
    data.sort(function(a, b){
        return a.price - b.price;
    });
    rebuildProductCards(data);
}
function sortDescProductByPrice(data){
    data.sort(function(a, b){
        return b.price - a.price;
    });
    rebuildProductCards(data);
}