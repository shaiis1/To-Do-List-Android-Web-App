package com.shaybar.todolist.services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.webkit.JavascriptInterface;

import com.shaybar.todolist.entities.Task;
import com.shaybar.todolist.entities.TaskForm;
import com.shaybar.todolist.utils.AppUtils;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TaskService {

    private Context context;

    private void addTask(Task task) {
        SQLiteDatabase db = DatabaseHelper.getWritableDatabase(getContext());
        ContentValues insertValues = new ContentValues();
        insertValues.put(Task.DESCRIPTION, task.getDescription());
        insertValues.put(Task.TAG, task.getTag());
        insertValues.put(Task.DUE_DATE, task.getDueDate());
        insertValues.put(Task.DATE, task.getDate());
        insertValues.put(Task.PRIORITY, task.getPriority());
        insertValues.put(Task.REPEAT_DAYS, task.getRepeatDays());
        insertValues.put(Task.DONE, task.isDone());


        db.insert(Task.TABLE_NAME, null, insertValues);
        db.close();
    }

    public TaskService(Context context) {
        this.context = context;
    }

    /**
     * getAllTasks
     * return all the tasks from the DB without filtering.
     */
    @JavascriptInterface
    public String getAllTasks() {
        SQLiteDatabase db = DatabaseHelper.getReadableDatabase(getContext());
        Cursor cursor = db.query(Task.TABLE_NAME, null, null, null, null, null, null);

        List<Task> tasks = DatabaseHelper.getOrm().listFromCursor(cursor, Task.class);
        Collections.sort(tasks, new Comparator<Task>() {
            @Override
            public int compare(Task task, Task t1) {
                if (task.getDueDate().compareTo(t1.getDueDate()) == 0) {
                    return task.getPriority().compareTo(t1.getPriority());
                }

                return task.getDueDate().compareTo(t1.getDueDate());
            }
        });

        cursor.close();
        db.close();
        return AppUtils.toJson(tasks);
    }

    private List<Task> getAllTasksByTag(String tagId) {
        SQLiteDatabase db = DatabaseHelper.getReadableDatabase(getContext());
        String[] args = {tagId};
        Cursor cursor = db.query(Task.TABLE_NAME, null, Task.TAG + "=?", args, null, null, null);
        List<Task> tasks = DatabaseHelper.getOrm().listFromCursor(cursor, Task.class);

        cursor.close();
        db.close();
        return tasks;
    }

    /**
     * delete task transaction records by the task id.
     * return SUCCESS OR FAILED message.
     */
    @JavascriptInterface
    public String deleteTask(String id) {
        SQLiteDatabase db = DatabaseHelper.getWritableDatabase(getContext());
        String[] vec = {id};
        int responseCode = db.delete(Task.TABLE_NAME, Task.ID + "=?", vec);

        db.close();
        if (responseCode > 0) {
            return AppUtils.toJson(new Message(MessageType.INFO, MessagesFactory.ACTION_COMPLETED, "delete"));
        } else {
            return AppUtils.toJson(new Message(MessageType.ERROR, MessagesFactory.ACTION_FAILED, "delete task"));
        }
    }

    /**
     * addTask
     * parsing the TaskForm (Html form) and parsing into java object,
     * then call addTask with the java object and create db record.
     */
    @JavascriptInterface
    public String addTask(String json) {
        try {
            Task task = new Task(AppUtils.fromJson(json, TaskForm.class));
            addTask(task);
            return AppUtils.toJson(task);
        } catch (Throwable e) {
            System.out.print(e.getMessage());
            return AppUtils.toJson(new Message(MessageType.ERROR, MessagesFactory.UNABLE_TO_ADD_TASK));
        }

    }

    /**
     * updateTask
     * parsing the TaskForm (Html form) and parsing into java object,
     * then update the db record
     */
    @JavascriptInterface
    public String updateTask(String taskId, String json) {
        SQLiteDatabase db = DatabaseHelper.getWritableDatabase(getContext());
        Task task = new Task(AppUtils.fromJson(json, TaskForm.class));
        ContentValues contentValues = new ContentValues();
        contentValues.put(Task.DESCRIPTION, task.getDescription());
        contentValues.put(Task.TAG, task.getTag());
        contentValues.put(Task.DUE_DATE, task.getDueDate());
        contentValues.put(Task.DATE, task.getDate());
        contentValues.put(Task.PRIORITY, task.getPriority());
        contentValues.put(Task.REPEAT_DAYS, task.getRepeatDays());
        contentValues.put(Task.DONE, task.isDone());
        String[] vec = {String.valueOf(taskId)};

        int responseCode = db.update(Task.TABLE_NAME, contentValues, Task.ID + " = ?", vec);

        db.close();

        if (responseCode > 0) {
            return AppUtils.toJson(new Message(MessageType.INFO, MessagesFactory.ACTION_COMPLETED, "update"));
        } else {
            return AppUtils.toJson(new Message(MessageType.ERROR, MessagesFactory.ACTION_FAILED, "update task"));
        }
    }

    /**
     * change task status complete or incomplete,
     * then return a message to user with feedback about his performance.
     */
    @JavascriptInterface
    public String changeTaskStatus(String currentTaskId, boolean done) {
        SQLiteDatabase db = DatabaseHelper.getWritableDatabase(getContext());
        String[] vec = {String.valueOf(currentTaskId)};
        ContentValues contentValues = new ContentValues();
        contentValues.put(Task.DONE, done);
        int responseCode = db.update(Task.TABLE_NAME, contentValues, Task.ID + " = ?", vec);
        Task lastTask = getTaskById(currentTaskId);
        long lastDueDate = lastTask.getDueDate();
        if (lastTask.getRepeatDays() != null && lastTask.getRepeatDays() > 0) {
            Task nextTask = getRepeatedTask(lastTask);
            addTask(nextTask);
        }

        db.close();

        if (responseCode > 0) {
            if (done) {
                Long currentDate = AppUtils.getCurrentDate();
                if (lastDueDate < currentDate) {
                    return AppUtils.toJson(new Message(MessageType.ERROR, MessagesFactory.JOB_FINISH_DELAYED));
                } else {
                    return AppUtils.toJson(new Message(MessageType.INFO, MessagesFactory.GOOD_JOB));
                }
            } else {
                return AppUtils.toJson(new Message(MessageType.INFO, MessagesFactory.ACTION_COMPLETED));
            }

        } else {
            return AppUtils.toJson(new Message(MessageType.ERROR, MessagesFactory.ACTION_FAILED, "update task"));
        }
    }


    private Task getRepeatedTask(Task currentTask) {
        Calendar dueDate = Calendar.getInstance();
        dueDate.setTimeInMillis(Math.max(System.currentTimeMillis(), currentTask.getDueDate()));

        Task nextTask = currentTask;

        boolean[] repeatDays = AppUtils.decimalTobits(currentTask.getRepeatDays());

        //nextTask initialize
        int nextTaskDayDiff = 1;
        dueDate.add(Calendar.DATE, 1);

        //iterate the next days
        while (nextTaskDayDiff < 7) {

            //return the first repeat day was found
            if (repeatDays[dueDate.get(Calendar.DAY_OF_WEEK) - 1]) {
                nextTask.setDate(System.currentTimeMillis());
                nextTask.setDueDate(dueDate.getTimeInMillis());
                nextTask.setDone(false);
                return nextTask;
            }

            dueDate.add(Calendar.DATE, 1);
            nextTaskDayDiff++;
        }

        return null;
    }

    /**
     * return json of task by given task id
     */
    @JavascriptInterface
    public String getTaskFormById(String id) {
        Task task = getTaskById(id);
        TaskForm taskForm = (task == null) ? null : new TaskForm(task);
        return AppUtils.toJson(taskForm);
    }

    private Task getTaskById(String id) {
        SQLiteDatabase db = DatabaseHelper.getWritableDatabase(getContext());
        Cursor cursor = db.query(Task.TABLE_NAME, null, "_id=" + id, null, null, null, null);
        List<Task> taskList = DatabaseHelper.getOrm().listFromCursor(cursor, Task.class);
        cursor.close();
        db.close();
        return (taskList.size() == 0) ? null : taskList.get(0);
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TaskService that = (TaskService) o;

        return context != null ? context.equals(that.context) : that.context == null;

    }

    @Override
    public int hashCode() {
        return context != null ? context.hashCode() : 0;
    }

}
