package com.shaybar.todolist.entities;

import com.shaybar.todolist.utils.AppUtils;

import org.chalup.microorm.annotations.Column;

import java.io.Serializable;


public class Task implements Serializable {
    public static final String TABLE_NAME = "'task'";
    public static final String ID = "`_id`";
    public static final String DESCRIPTION = "`description`";
    public static final String TAG = "`tag`";
    public static final String DATE = "`date`";
    public static final String DUE_DATE = "`due_date`";
    public static final String PRIORITY = "`priority`";
    public static final String REPEAT_DAYS = "'repeat_days'";
    public static final String DONE = "'done'";

    @Column("_id")
    private Long id;

    @Column("description")
    private String description;

    @Column("tag")
    private String tag;

    @Column("date")
    private Long date;

    @Column("due_date")
    private Long dueDate;

    @Column("priority")
    private Integer priority;


    @Column("repeat_days")
    private Integer repeatDays;

    @Column("done")
    private boolean done;


    public Task(Long id, String description, String tag, Long date, Long dueDate, Integer priority, Integer repeatDays, boolean done) {
        this.id = id;
        this.description = description;
        this.tag = tag;
        this.date = date;
        this.dueDate = dueDate;
        this.priority = priority;
        this.repeatDays = repeatDays;
        this.done = done;

    }


    public Task(TaskForm taskForm) {
        this.description = taskForm.getDescription();
        this.tag = taskForm.getTag();
        this.date = System.currentTimeMillis();
        this.dueDate = taskForm.getDueDate();
        this.priority = taskForm.getPriority();
        this.repeatDays = AppUtils.bitsToDecimal(taskForm.getRepeatDays());
        this.done = taskForm.isDone();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Long getDueDate() {
        return dueDate;
    }

    public void setDueDate(Long dueDate) {
        this.dueDate = dueDate;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getRepeatDays() {
        return repeatDays;
    }

    public void setRepeatDays(Integer repeatDays) {
        this.repeatDays = repeatDays;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

}
