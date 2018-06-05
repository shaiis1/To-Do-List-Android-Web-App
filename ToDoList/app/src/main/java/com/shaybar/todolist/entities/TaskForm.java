package com.shaybar.todolist.entities;

import com.shaybar.todolist.services.ExceptionMessage;
import com.shaybar.todolist.utils.AppUtils;

import org.chalup.microorm.annotations.Column;

public class TaskForm {

    @Column("_id")
    private String id;

    @Column("description")
    private String description;

    @Column("tag")
    private String tag;

    @Column("date")
    private String date;

    @Column("due_date")
    private String dueDate;

    @Column("priority")
    private Integer priority;

    @Column("repeatDays")
    private boolean[] repeatDays = new boolean[7];

    private boolean done;

    public TaskForm() {

    }

    public TaskForm(Task task) {
        this.description = task.getDescription();
        this.tag = task.getTag();
        this.date = String.valueOf(task.getDate());
        this.dueDate = String.valueOf(task.getDueDate());
        this.priority = task.getPriority();
        this.repeatDays = AppUtils.decimalTobits(task.getRepeatDays());
        this.done = task.isDone();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getDueDate() {
        try {
            return AppUtils.parseDate(dueDate, "yyyy-MM-dd");
        } catch (ExceptionMessage e) {
            e.toString();
        }

        return null;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }


    public boolean[] getRepeatDays() {
        return repeatDays;
    }

    public void setSunday(Boolean sunday) {
        this.repeatDays[0] = sunday;
    }

    public void setMonday(Boolean monday) {
        this.repeatDays[1] = monday;
    }

    public void setTuesday(Boolean tuesday) {
        this.repeatDays[2] = tuesday;
    }

    public void setWednesday(Boolean wednesday) {
        this.repeatDays[3] = wednesday;
    }

    public void setThursday(Boolean thursday) {
        this.repeatDays[4] = thursday;
    }

    public void setFriday(Boolean friday) {
        this.repeatDays[5] = friday;
    }

    public void setSaturday(Boolean saturday) {
        this.repeatDays[6] = saturday;
    }

    public boolean getSunday() {
        return this.repeatDays[0];
    }

    public boolean getMonday() {
        return this.repeatDays[1];
    }

    public boolean getTuesday() {
        return this.repeatDays[2];
    }

    public boolean getWednesday() {
        return this.repeatDays[3];
    }

    public boolean getThursday() {
        return this.repeatDays[4];
    }

    public boolean getFriday() {
        return this.repeatDays[5];
    }

    public boolean getSaturday() {
        return this.repeatDays[6];
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
