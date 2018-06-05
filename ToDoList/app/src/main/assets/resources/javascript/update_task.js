var UpdateTask = {};

UpdateTask.loadEvents = function () {
    ViewTask.selectedTask = JSON.parse(TaskService.getTaskFormById(ViewTask.selectedTaskId));
    $("#submit").click(function() {
        var result = UpdateTask.sendTaskForm();
    });

    UpdateTask.loadForm();
};

UpdateTask.sendTaskForm = function() {
    var form = JSON.stringify($("#updateTaskForm").serializeJSON());
    var result = JSON.parse(TaskService.updateTask(ViewTask.selectedTaskId, form));
    NavigationUtils.changeActiveTab($("#viewTasks"), "viewTasks.html", $("#container"));

};

UpdateTask.loadForm = function() {
    AppUtils.populateTags($("#updateTaskForm #tag"));
    $('#updateTaskForm #description').val(ViewTask.selectedTask.description);
    $("#tag").val(ViewTask.selectedTask.tag).change();
    $("#priority").val(ViewTask.selectedTask.priority).change();

    var daysCheckboxes = [$('#sunday'), $('#monday'), $('#tuesday'), $('#wednesday'), $('#thursday'), $('#friday'), $('#saturday')] ;
    var dueDate = new Date(parseInt(ViewTask.selectedTask.dueDate));
    AppUtils.setDate(dueDate, $('#dueDate'));

    for (i=0; i<7 ;i++) {
        if (ViewTask.selectedTask.repeatDays[i] == true) {
            $(daysCheckboxes[i]).click();
        }
    }

};

$(document).ready( function () {
    UpdateTask.loadEvents();
});



