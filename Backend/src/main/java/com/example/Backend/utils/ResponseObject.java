package com.example.Backend.utils;

/**
 * This ResponseObject is created in order to send information to Frontend about CRUD operations like the status code
 * but with special reference to possible warning messages or error messages
 * @param <T>
 */
public class ResponseObject<T> {
    private int statuscode;
    private String okmessage;
    private String warningmessage;
    private String errormessage;
    private T payload;

    public ResponseObject(int statuscode, String okmessage, String warningmessage, String errormessage, T payload) {
        this.statuscode = statuscode;
        this.warningmessage = warningmessage;
        this.okmessage = okmessage;
        this.errormessage = errormessage;
        this.payload = payload;
    }

    public int getStatuscode() {
        return statuscode;
    }

    public void setStatuscode(int statuscode) {
        this.statuscode = statuscode;
    }

    public String getWarningmessage() {
        return warningmessage;
    }

    public void setWarningmessage(String warningmessage) {
        this.warningmessage = warningmessage;
    }

    public String getErrormessage() {
        return errormessage;
    }

    public void setErrormessage(String errormessage) {
        this.errormessage = errormessage;
    }

    public String getOkmessage() {
        return okmessage;
    }

    public void setOkmessage(String okmessage) {
        this.okmessage = okmessage;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }
}
