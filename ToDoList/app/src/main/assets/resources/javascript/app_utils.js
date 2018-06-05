
var AppUtils = {};

AppUtils.setDate = function(date, targetDateInput) {
    var dd = date.getDate();
    var mm = date.getMonth()+1;
    mm = mm < 10  ? '0'+mm : mm;
    dd = dd < 10  ? '0'+dd : dd;
    var yyyy = date.getFullYear();

    $(targetDateInput).val(yyyy+'-'+mm+'-'+dd);
};

AppUtils.populateTags = function(dropdownList) {
    $(dropdownList).html($('<option></option>').text("Select Tag"));
    AppUtils.loadTagsMap();
    for(tag in AppUtils.tagsMap) {
        var option=$('<option value=' + AppUtils.tagsMap[tag].id + '>' + AppUtils.tagsMap[tag].name + '</option>');
        $(dropdownList).append(option);
    }
};

AppUtils.loadTagsMap = function(useCache) {
    AppUtils.tagsMap = JSON.parse(TagService.getAllTags());
};

AppUtils.showPopup = function (result) {

    var popupMessage = $('#popupMessage');
    var popupHeader = $('#popupHeader');
    $(popupHeader).removeClass();
    if (result["messageType"] == "ERROR") {
        $(popupHeader).addClass("red_background");
        $(popupHeader).text("Warning");
    } else {
        $(popupHeader).addClass("green_background");
        $(popupHeader).text("Info");
    }

    $(popupMessage).html(result["formatedMessage"]);


    var popup = $('#popup');
    $(popup).popup();
    $(popup).popup("open");

};