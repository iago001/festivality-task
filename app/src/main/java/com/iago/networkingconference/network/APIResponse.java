package com.iago.networkingconference.network;

import com.iago.networkingconference.models.Attendee;

import java.util.ArrayList;

public class APIResponse {

    private String message;

    private int responseSize;

    ArrayList<Attendee> response = new ArrayList<>();

    public ArrayList<Attendee> getResponse() {
        return response;
    }

    public void setResponse(ArrayList<Attendee> response) {
        this.response = response;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getResponseSize() {
        return responseSize;
    }

    public void setResponseSize(int responseSize) {
        this.responseSize = responseSize;
    }
}
