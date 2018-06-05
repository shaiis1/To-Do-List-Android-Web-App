var Settings = {};


Settings.loadEvents = function () {
    $("#addTag").click(function() {
        NavigationUtils.changeActiveTab($("#addTag"), "addTag.html", $("#settingsContainer"));
    });
};

$(document).ready( function () {
    console.log("init add_seetings.js");
    Settings.loadEvents();
});



