package com.spring_project.exception;

public class PasswordsDoNotMatchException extends RuntimeException {

    public PasswordsDoNotMatchException(String message) {
        super(message);
    }
}
