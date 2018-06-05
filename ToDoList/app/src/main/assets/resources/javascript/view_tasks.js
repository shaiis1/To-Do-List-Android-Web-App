var ViewTask = {};
ViewTask.filterBy = {};
ViewTask.pieGraphData =[];
ViewTask.events = [];
ViewTask.columns = [     new Column(1, "tag", "Tag", function(id) {return AppUtils.tagsMap[id].name;}, "data-mini=\"true\" class=\"small tag\"")
                        ,new Column(1, "description", "Task", null, "data-mini=\"true\" class=\"small description\"")
                        ,new Column(1, "dueDate", "Date", function (date) { return timeSolver.getString(new Date(date), "YYYY-MM-DD");}, "data-mini=\"true\" class=\"small date\"")
                        ,new Column(1, "id", "id", null, "class=\"small id hide\"")];

ViewTask.getTaskList = function (getFromDB) {
    if (getFromDB) {
        ViewTask.taskList = JSON.parse(TaskService.getAllTasks());
    }

    ViewTask.filteredTaskList = ViewTask.taskList;
    ViewTask.filteredTaskList = DateFilter(ViewTask.filteredTaskList, ViewTask.filterBy.applyDateFilter);
    ViewTask.filteredTaskList = taskStatusFilter(ViewTask.filteredTaskList, ViewTask.filterBy.applyOnlyIncompletedTasks);

    ViewTask.createTable(ViewTask.filteredTaskList, ViewTask.columns);
    $("#view_tasks_table").table("refresh");

     ViewTask.events.onEdit();
     ViewTask.events.onDelete();
     ViewTask.events.onChecked();
};

ViewTask.createTable = function(json, columns) {
    ViewTask.addRows(json, columns);
};

ViewTask.addRows = function(jsonArr, columns) {
   var rows= "";
    var tBody = $('#view_tasks_table tbody');
    var jsonRow;
    for (i in jsonArr) {
            jsonRow = jsonArr[i];
            rows += ViewTask.formatRow(columns, jsonRow);
    }

    tBody.html(rows);
};

ViewTask.formatRow = function(columns, jsonRow){
   var row ="";

    //Initialize task list
    var isDone = (jsonRow['done'] == true);
    var trClass ="class =\" task-tr";
    trClass += isDone ? " line-on-text" : "";
    trClass += "\"";
    row+= "<tr "+ trClass + ">";

    var tag = AppUtils.tagsMap[jsonRow[columns[0].name]];

    //Buttons
    row += "<td>";
    var checked = isDone ? "checked" : "";
    row += "<label class=\"check\">";
    row += "<input type=\"checkbox\" class=\"onChecked\" " +checked + "/>";
    row += "<div class=\"box\"></div>";
    row += "</label>";
    row += "</td>";

    //Tag color
    row+= "<td style=\"width:0.3em; background-color:" + tag.color + ";\">" + "</td>";

    //Tag
    //Task
    var column = columns[1];
    var value = tag.name + " : " + jsonRow[column.name];
    row += "<td " +column.attributes +"> <value>" + value;
    row += "</br>";

    //Due date
    column = columns[2];
    var dateString = timeSolver.getString(new Date(jsonRow[column.name]), "YYYY-MM-DD");

    var priority = jsonRow["priority"];
    switch (priority) {
        case 1:
            row += "<div class=\"high-priority\"> High ";
            break;
        case 2:
            row += "<div class=\"medium-priority\"> Medium ";
            break;
        case 3:
            row += "<div class=\"low-priority\"> Low ";
            break;

        default:
            break;
    }
    row += dateString;
    row += "</div>";
    row += "</value>";

    row += "</td>";

    //id
    column = columns[3];
    var value = jsonRow[column.name];
    row += "<td " +column.attributes +"> <value>" + value + "</value></td>";
    row += "</value></td>";

    //edit button
    row += "<td><a class=\"task-update ui-btn ui-btn-inline ui-icon-edit ui-btn-icon-notext ui-corner-all ui-shadow view_tasks_ui-btn\"></td>";

    //delete button
    row += "<td><a class=\"table-remove ui-btn ui-btn-inline ui-icon-delete ui-btn-icon-notext ui-corner-all ui-shadow view_tasks_ui-btn\"></a></td>";

    row+="</tr>";
    return row;
};

ViewTask.events.onEdit = function() {
   $('.task-update').click(function () {
       var row = $(this).closest('tr')[0];
       ViewTask.selectedTaskId = $(row).find(".id")[0].lastChild.innerText;
       NavigationUtils.changeActiveTab($("#updateTask"), "updateTask.html", $("#container"));
       console.log( ViewTask.selectedTask);
    });
};


ViewTask.events.onDelete = function () {
    $('.table-remove').click(function () {
        var row = $(this).closest('tr')[0];
        ViewTask.selectedTaskId = $(row).find(".id")[0].lastChild.innerText;
        var response = TaskService.deleteTask(ViewTask.selectedTaskId);
        $(row).fadeOut(500, function () { ViewTask.getTaskList(true);});

    });
};


ViewTask.events.onChecked = function () {
    $('.onChecked').click(function () {
        var row = $(this).closest('tr')[0];

        var taskId = $(row).find(".id")[0].lastChild.innerText;
        var result = JSON.parse(TaskService.changeTaskStatus(taskId, this.checked));

        if (this.checked) {
            $(row).addClass("line-on-text");
        } else {
            $(row).removeClass("line-on-text");
        }

        if (this.checked && ViewTask.filterBy.applyOnlyIncompletedTasks) {
            $(row).fadeOut(500, function () { ViewTask.getTaskList(true);});
        } else {
            ViewTask.getTaskList(true);
        }

        AppUtils.showPopup(result);

    });
};

ViewTask.events.onDateFilter = function () {
    $('#view_tasks_filter').click(function () {
        ViewTask.filterBy.applyDateFilter = true;
        ViewTask.getTaskList(false);
    });
};

ViewTask.events.onDateUnfiltered = function () {
    $('#view_tasks_unfiltered').click(function () {
        ViewTask.filterBy.applyDateFilter = false;
        $("#startDate").val('');
        $("#endDate").val('');
        ViewTask.getTaskList(false);
    });
};

ViewTask.events.onTaskStatusFilter = function () {
    $('#filter_by_task_status').click(function () {
        ViewTask.filterBy.applyOnlyIncompletedTasks = !ViewTask.filterBy.applyOnlyIncompletedTasks;
        console.log("applyOnlyIncompletedTasks = " + ViewTask.filterBy.applyOnlyIncompletedTasks);
        var button = $(this).closest('div');
        $(this).val(ViewTask.filterBy.applyOnlyIncompletedTasks ? "Show Completed Tasks" : "Hide Completed Tasks").button('refresh');
        ViewTask.getTaskList(false);

    });
};




ViewTask.loadEvents = function () {
    ViewTask.filterBy.applyDateFilter = false;
    ViewTask.filterBy.applyOnlyIncompletedTasks = true;
    ViewTask.filterBy.startDate = new Date();
    ViewTask.filterBy.endDate = new Date();

    ViewTask.filterBy.endDate.setMonth(ViewTask.filterBy.endDate.getMonth() + 3);
    AppUtils.setDate(ViewTask.filterBy.startDate,  $("#startDate"));
    AppUtils.setDate(ViewTask.filterBy.endDate,  $("#endDate"));
    AppUtils.loadTagsMap();

    ViewTask.getTaskList(true);

    ViewTask.events.onDateFilter();
    ViewTask.events.onDateUnfiltered();
    ViewTask.events.onTaskStatusFilter();
};

function DateFilter(taskList, applyDateFilter) {
    if (ViewTask.filterBy.applyDateFilter) {
        //apply date range filter
        var startDate = new Date($("#startDate")[0].value).getTime();
        console.log(startDate);
        var endDate = new Date($("#endDate")[0].value).getTime();
        console.log(endDate);

        //filter by dates
        taskList = taskList.filter(function (task, index) {
            return task.dueDate >= startDate && task.dueDate <= endDate;
        });
    }

    return taskList;
}

function taskStatusFilter(taskList, onlyIncompletedTasks) {
    if (onlyIncompletedTasks) {
        taskList = taskList.filter(function (task, index) {
            return task.done == false;
        });
    }

    return taskList;
}
$(document).ready(function () {
    ViewTask.loadEvents();
});

