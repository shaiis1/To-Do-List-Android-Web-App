var AddTask = {};
AddTask.loadEvents = function () {
    $("#submit").click(function() {
        var result = AddTask.sendTaskForm();
    });
    AddTask.resetForm();
};

AddTask.sendTaskForm = function() {
    var form = JSON.stringify($("#addTaskForm").serializeJSON());
    var result =  JSON.parse(TaskService.addTask(form));
    console.log(result);
    AddTask.resetForm();
    return result;

};

AddTask.resetForm = function() {
    $('#addTaskForm')[0].reset();
    AppUtils.populateTags($("#addTaskForm #tag"));
    document.getElementById('dueDate').valueAsDate = new Date();
    $(window).scrollTop(0);
};

$(document).ready( function () {
    AddTask.loadEvents();
});



