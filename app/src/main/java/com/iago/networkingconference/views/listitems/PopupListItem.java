package com.iago.networkingconference.views.listitems;

import java.util.Objects;

public class PopupListItem extends PopupListHeader {

    private boolean isChecked;

    private String tagType;

    public PopupListItem(String label, String tagType, boolean isChecked) {
        super(label);
        this.tagType = tagType;
        this.isChecked = isChecked;
    }

    public int getViewType() {
        return 1;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getTagType() {
        return tagType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PopupListItem)) return false;
        PopupListItem listItem = (PopupListItem) o;
        return Objects.equals(getTagType(), listItem.getTagType()) &&
                Objects.equals(getLabel(), listItem.getLabel());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTagType(), getLabel());
    }
}
