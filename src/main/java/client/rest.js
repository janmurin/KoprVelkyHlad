/**
 * Created by Janco1 on 2. 2. 2016.
 */
var recepty = [];

$(document).ready(function () {
    console.log("jquery ready");
    reloadRecepty();

});


function editRecipe(id) {
    console.log('edit recipe ' + id);
    $('html body div#upravitReceptContainer.simpleContainer').attr('style', "");
    var recept = {};
    for (var i = 0; i < recepty.length; i++) {
        if (recepty[i].id == id) {
            recept = recepty[i];
            break;
        }
    }
    var text="";
    for(var i=0; i<recept.ingredients.length; i++){
        text+=recept.ingredients[i].quantity+"-"+recept.ingredients[i].ingredient+"\n";
    }
    $("html body div#upravitReceptContainer.simpleContainer div.id input").val(recept.id);
    $("html body div#upravitReceptContainer.simpleContainer div.name input").val(recept.name);
    $("html body div#upravitReceptContainer.simpleContainer div.author input").val(recept.author);
    $("html body div#upravitReceptContainer.simpleContainer div.ingredients textarea").val(text);
    $("html body div#upravitReceptContainer.simpleContainer div.guide textarea").val(recept.guide);
}

function editRecipeButtonClick() {
    console.log('editRecipeButtonClick()');
    $('html body div#upravitReceptContainer.simpleContainer').attr('style', "display:none;");
    var id = $("html body div#upravitReceptContainer.simpleContainer div.id input").val();
    var name = $("html body div#upravitReceptContainer.simpleContainer div.name input").val();
    var author = $("html body div#upravitReceptContainer.simpleContainer div.author input").val();
    var ingrs = $("html body div#upravitReceptContainer.simpleContainer div.ingredients textarea").val().trim();
    var guide = $("html body div#upravitReceptContainer.simpleContainer div.guide textarea").val();
    var novy = {};
    novy.id = id;
    novy.name = name;
    novy.author = author;
    var ingredients = [];
    ingrs = ingrs.split("\n");
    for (var i = 0; i < ingrs.length; i++) {
        var split = ingrs[i].split('-');
        ingredients.push({quantity: split[0], ingredient: split[1]});
    }
    console.log('naparsovane ingredients: ' + JSON.stringify(ingredients));
    novy.ingredients = ingredients;
    novy.guide = guide;
    console.log('posielma tento objekt: '+JSON.stringify(novy));
    $.ajax({
        type: "POST",
        url: "http://localhost:8080/velky.hlad.sk/upravRecept",
        data: JSON.stringify(novy),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (data, status, xhr) {
            if (xhr.statusText === "OK") {
                // uspesne pridanie do databazy
                alert('recept upraveny v databaze: ' + JSON.stringify(xhr.responseJSON));
                //console.log(JSON.stringify(xhr));
                // resetneme inputy
                $("html body div#upravitReceptContainer.simpleContainer div.id input").val('');
                $("html body div#upravitReceptContainer.simpleContainer div.name input").val('');
                $("html body div#upravitReceptContainer.simpleContainer div.author input").val('');
                $("html body div#upravitReceptContainer.simpleContainer div.ingredients textarea").val('');
                $("html body div#upravitReceptContainer.simpleContainer div.guide textarea").val('');
                reloadRecepty();
            } else {
                alert(xhr.statusText + ': ' + xhr.responseJSON);
            }
        },
        error: function (xhr, status, error) {
            //console.log(JSON.stringify(xhr));
            alert('nepodarilo sa upravit recept v databaze:\n' + xhr.responseJSON.error + ': ' + xhr.responseJSON.exception);
        }
    });
}

function removeRecipe(id) {
    console.log('remove recipe ' + id);
    $.get("http://localhost:8080/velky.hlad.sk/zmazatRecept/" + id, function (data, status, xhr) {
        if (xhr.status == 200) {
            console.log('uspesne zmazanie iniciujem refresh');
            // uspesne zmazanie, refreshnut tabulku
            var noveRecepty = [];
            for (var i = 0; i < recepty.length; i++) {
                if (recepty[i].id != id) {
                    noveRecepty.push(recepty[i]);
                }
            }
            recepty = noveRecepty;
            refreshReceptyTable(recepty);
        } else {
            console.log('neuspesne zmazanie');
            alert('neuspesne zmazanie: ' + JSON.stringify(data));
        }
    });
}

function addRecipe() {
    console.log('add recipe ');
    var name = $("html body div#pridatReceptContainer.simpleContainer div.name input").val();
    var author = $("html body div#pridatReceptContainer.simpleContainer div.author input").val();
    var ingrs = $("html body div#pridatReceptContainer.simpleContainer div.ingredients textarea").val().trim();
    var guide = $("html body div#pridatReceptContainer.simpleContainer div.guide textarea").val();
    var novy = {};
    novy.id = -1;
    novy.name = name;
    novy.author = author;
    var ingredients = [];
    ingrs = ingrs.split("\n");
    for (var i = 0; i < ingrs.length; i++) {
        var split = ingrs[i].split('-');
        ingredients.push({quantity: split[0], ingredient: split[1]});
    }
    console.log('naparsovane ingredients: ' + JSON.stringify(ingredients));
    novy.ingredients = ingredients;
    novy.guide = guide;
    console.log('odosielam: '+JSON.stringify(novy));
    $.ajax({
        type: "POST",
        url: "http://localhost:8080/velky.hlad.sk/pridajRecept",
        data: JSON.stringify(novy),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (data, status, xhr) {
            if (xhr.statusText === "Created") {
                // uspesne pridanie do databazy
                alert('recept pridany do databazy, idcko: ' + xhr.responseJSON.id);
                //console.log(JSON.stringify(xhr));
                // resetneme inputy
                $("html body div#pridatReceptContainer.simpleContainer div.name input").val('');
                $("html body div#pridatReceptContainer.simpleContainer div.author input").val('');
                $("html body div#pridatReceptContainer.simpleContainer div.ingredients textarea").val('');
                $("html body div#pridatReceptContainer.simpleContainer div.guide textarea").val('');
                reloadRecepty();
            } else {
                alert(xhr.statusText + ': ' + xhr.responseJSON);
            }
        },
        error: function (xhr, status, error) {
            //console.log(JSON.stringify(xhr));
            alert('nepodarilo sa pridat recept do databazy:\n' + xhr.responseJSON.error + ': ' + xhr.responseJSON.exception);
        }
    });
}

function reloadRecepty() {
    $.get("http://localhost:8080/velky.hlad.sk/vsetkyRecepty", function (data, status, xhr) {
        recepty = data;
        refreshReceptyTable(data);
    });
}

function findRecipes() {
    console.log('findRecipes ');
    var checked = $('html body div#hladatContainer.simpleContainer form input#ingrediencieRadio').is(':checked');
    //console.log('checked: '+checked);
    var text = $('html body div#hladatContainer.simpleContainer div.guide textarea').val().trim();
    var split = text.split('\n');
    console.log('split: ' + JSON.stringify(split));
    if (checked) {
        // chceme ingrediencie
        $.ajax({
            type: "POST",
            url: "http://localhost:8080/velky.hlad.sk/findWithIngredients",
            data: JSON.stringify(split),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (data, status, xhr) {
                if (xhr.statusText === "OK") {
                    // uspesne pridanie do databazy
                    alert('naslo sa ' + data.length + ' receptov');
                    refreshReceptyTable(data);
                } else {
                    alert(xhr.statusText + ': ' + xhr.responseJSON);
                }
            },
            error: function (xhr, status, error) {
                //console.log(JSON.stringify(xhr));
                alert('nepodarilo sa najst recepty:\n' + xhr.responseJSON.error + ': ' + xhr.responseJSON.exception);
            }
        });
    } else {
        // chceme keywordy
        $.ajax({
            type: "POST",
            url: "http://localhost:8080/velky.hlad.sk/findWithKeywords",
            data: JSON.stringify(split),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (data, status, xhr) {
                if (xhr.statusText === "OK") {
                    // uspesne pridanie do databazy
                    alert('naslo sa ' + data.length + ' receptov');
                    refreshReceptyTable(data);
                } else {
                    alert(xhr.statusText + ': ' + xhr.responseJSON);
                }
            },
            error: function (xhr, status, error) {
                //console.log(JSON.stringify(xhr));
                alert('nepodarilo sa najst recepty:\n' + xhr.responseJSON.error + ': ' + xhr.responseJSON.exception);
            }
        });
    }
}

function findExactRecipes() {
    console.log('findRecipes ');
    var text = $('html body div#hladatExactContainer.simpleContainer div.guide textarea').val().trim();
    var split = text.split('\n');
    console.log('split: ' + JSON.stringify(split));
    // chceme ingrediencie
    $.ajax({
        type: "POST",
        url: "http://localhost:8080/velky.hlad.sk/findWithExactIngredients",
        data: JSON.stringify(split),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (data, status, xhr) {
            if (xhr.statusText === "OK") {
                // uspesne pridanie do databazy
                alert('naslo sa ' + data.length + ' receptov');
                refreshReceptyTable(data);
            } else {
                alert(xhr.statusText + ': ' + xhr.responseJSON);
            }
        },
        error: function (xhr, status, error) {
            //console.log(JSON.stringify(xhr));
            alert('nepodarilo sa najst recepty:\n' + xhr.responseJSON.error + ': ' + xhr.responseJSON.exception);
        }
    });

}

function getIngredientsListNode(ingredients) {
    var ingredientsList = document.createElement("ul");
    for (var i = 0; i < ingredients.length; i++) {
        var li = document.createElement("li");
        li.appendChild(document.createTextNode(ingredients[i].quantity + " " + ingredients[i].ingredient));
        ingredientsList.appendChild(li);
    }
    return ingredientsList;
}



function refreshReceptyTable(polozkyTable) {
    console.log("refreshReceptyTable with polozky size: " + polozkyTable.length);
    var tbdy = document.createElement('tbody');
    var tr = document.createElement('tr');
    var th0 = document.createElement('th');
    th0.appendChild(document.createTextNode('id'));
    var th1 = document.createElement('th');
    th1.appendChild(document.createTextNode('name'));
    var th2 = document.createElement('th');
    th2.appendChild(document.createTextNode('author'));
    var th3 = document.createElement('th');
    th3.appendChild(document.createTextNode('ingredients'));
    var th4 = document.createElement('th');
    th4.appendChild(document.createTextNode('guide'));
    var th5 = document.createElement('th');
    th5.appendChild(document.createTextNode('actions'));
    tr.appendChild(th0);
    tr.appendChild(th1);
    tr.appendChild(th2);
    tr.appendChild(th3);
    tr.appendChild(th4);
    tr.appendChild(th5);
    tbdy.appendChild(tr);
    for (var i = 0; i < polozkyTable.length; i++) {
        //console.log("spracuvam polozku: "+polozkyTable[i]);
        var tr = document.createElement('tr');
        var td0 = document.createElement('td');
        td0.appendChild(document.createTextNode(polozkyTable[i].id + '.'));
        var td1 = document.createElement('td');
        td1.appendChild(document.createTextNode(polozkyTable[i].name));
        var td2 = document.createElement('td');
        td2.appendChild(document.createTextNode(polozkyTable[i].author));
        var td3 = document.createElement('td');
        td3.appendChild(getIngredientsListNode(polozkyTable[i].ingredients));
        var td4 = document.createElement('td');
        td4.appendChild(document.createTextNode(polozkyTable[i].guide));
        var td5 = document.createElement('td');
        var button = document.createElement('button');
        button.appendChild(document.createTextNode("upravit"));
        button.setAttribute("onClick", "editRecipe(" + polozkyTable[i].id + ")");
        var button2 = document.createElement('button');
        button2.appendChild(document.createTextNode("odstranit"));
        button2.setAttribute("onClick", "removeRecipe(" + polozkyTable[i].id + ")");
        td5.appendChild(button);
        td5.appendChild(button2);

        tr.appendChild(td0);
        tr.appendChild(td1);
        tr.appendChild(td2);
        tr.appendChild(td3);
        tr.appendChild(td4);
        tr.appendChild(td5);
        tbdy.appendChild(tr);
    }
    var table = document.getElementById('receptyTable');
    table.innerHTML = tbdy.innerHTML;
}
