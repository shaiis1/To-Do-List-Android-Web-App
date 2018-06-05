
var NavigationUtils = {};

NavigationUtils.changeActiveTab = function (newTab, newTabHtml, targetDiv, params) {
    targetDiv.load(newTabHtml, params).trigger('create');
};

NavigationUtils.loadEvents = function () {
    $("#settings").click(function() {
        NavigationUtils.changeActiveTab($("#settings"), "addTag.html", $("#container"));
    });
    $("#addTask").click(function() {
        NavigationUtils.changeActiveTab($("#addTask"), "addTask.html", $("#container"));
    });
    $("#viewTasks").click(function() {
        NavigationUtils.changeActiveTab($("#viewTasks"), "viewTasks.html", $("#container"));
    });
    $("#home").click(function() {
        NavigationUtils.changeActiveTab($("#home"), "home.html", $("#container"));
    });
    $("#updateTask").click(function() {
        NavigationUtils.changeActiveTab($("#updateTask"), "updateTask.html", $("#container"));
    });

    $("#viewTasks").click();

  
};

window.onload = function () {
    NavigationUtils.loadEvents();
}




