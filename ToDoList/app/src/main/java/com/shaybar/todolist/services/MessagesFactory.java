package com.shaybar.todolist.services;

/**
 * Project message template factory enum, used by Message object to build pretty message to front end.
 */
public enum MessagesFactory {
    INVALID_MAIL_ENTERED(400, "Invalid mail entered."),
    INVALID_VALUE_IN_FORM(400, "Invalid value entered on ['%s'] the required value is ['%s']."),
    ACTION_FAILED(400, "Action ['%s'] has been failed."),
    UNABLE_TO_ADD_TASK(400, "Add task failed due to system failure. Contact system administrator."),
    UNABLE_TO_SAVE_TAG(400, "Save tag failed due to system failure. Contact system administrator."),
    GOOD_JOB(200, "Good Job! you finished on time."),
    JOB_FINISH_DELAYED(200, "Job Finish Delayed! Try to be better next time."),
    ACTION_COMPLETED(200, "Action successfully completed.");


    int errorCode;
    String message;

    private MessagesFactory(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
