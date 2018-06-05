var AddTag = {};

AddTag.tagList;
AddTag.selectedValue;

AddTag.getAllTags = function (getFromDB){
    if (getFromDB) {
        AddTag.tagMap = JSON.parse(TagService.getAllTags());
    }
    return AddTag.tagMap;
};

AddTag.loadEvents = function () {
    $("#submit").click(function() {
        var result = AddTag.sendTagForm();
    });
    AddTag.loadForm();

};

AddTag.sendTagForm = function() {
    var result = TagService.saveTag(JSON.stringify($("#addTagForm").serializeJSON()), AddTag.selectedValue);
    console.log(JSON.parse(result));
    AddTag.loadForm();
    return result;

};

AddTag.loadForm = function() {
    AddTag.selectedValue = null;
    $('#addTagForm')[0].reset();
    $('#tagControlForm')[0].reset();
    var color = '#' + ("000000" + Math.random().toString(16).slice(2, 8).toUpperCase()).slice(-6);
    $('#addTagForm #color').val(color);
    AppUtils.populateTags($("#tagControlForm #tag"));

    $("#tagControlForm #tag").change(
        function() {
            AddTag.selectedValue = $('#tagControlForm option:selected')[0].value;
            $('#tagControlForm #id').val(AddTag.selectedValue);
            $('#addTagForm #name').val(AppUtils.tagsMap[AddTag.selectedValue].name);
            $('#addTagForm #color').val(AppUtils.tagsMap[AddTag.selectedValue].color);

        }
    );
    $(window).scrollTop(0);
};

$(document).ready( function () {
    console.log("init add_tag.js");
    AddTag.loadEvents();
});



