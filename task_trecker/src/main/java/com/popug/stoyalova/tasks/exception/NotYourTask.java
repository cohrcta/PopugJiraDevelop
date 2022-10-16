package com.popug.stoyalova.tasks.exception;

public class NotYourTask extends RuntimeException {

    private static final long serialVersionUID = 9002193470853148139L;

    public NotYourTask(String message) {
        super(message);
    }
}
