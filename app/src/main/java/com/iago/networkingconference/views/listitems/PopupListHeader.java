package com.iago.networkingconference.views.listitems;

public class PopupListHeader {

    private String label;

    public PopupListHeader(String label) {
        this.label = label;
    }

    public int getViewType() {
        return 0;
    }

    public String getLabel() {
        return label;
    }
}
