package com.andrewkravets.model;

public class State {
    private Integer downTimes = 0;

    private String errorMessage="";

    private void clearMessages() {
        errorMessage = "";
    }

    private void clearDownTimes() {
        downTimes = 0;
    }

    public Integer getDownTimes() {
        return downTimes;
    }

    public void increaseDownTime() {
        this.downTimes = this.downTimes + 1;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void clear() {
        clearDownTimes();
        clearMessages();
    }
}
