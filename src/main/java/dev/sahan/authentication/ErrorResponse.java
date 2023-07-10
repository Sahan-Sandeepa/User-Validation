package dev.sahan.authentication;

import java.util.List;

public class ErrorResponse {
    private int status;
    private String message;
    private List<String> errors;

    public ErrorResponse(){}

    public void setStatus(int value) {
        this.status = value;
    }
    public void setMessage(String string) {
        this.message = string;
    }
    public void setErrors(List<String> errorMessages) {
        this.errors = errorMessages;
    }

    // Constructors, getters, and setters
}

