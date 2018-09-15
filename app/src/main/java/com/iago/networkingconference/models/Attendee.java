package com.iago.networkingconference.models;

import com.iago.networkingconference.database.DBMedia;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "attendees")
public class Attendee {

    @DatabaseField(id = true)
    private String id;

    @DatabaseField
    private int isFeatured;

    @DatabaseField
    private int likes;

    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private CustomFields customFields;

    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private DBMedia media;

    public DBMedia getMedia() {
        return media;
    }

    public void setMedia(DBMedia media) {
        this.media = media;
    }

    public CustomFields getCustomFields() {
        return customFields;
    }

    public void setCustomFields(CustomFields customFields) {
        this.customFields = customFields;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIsFeatured() {
        return isFeatured;
    }

    public void setIsFeatured(int isFeatured) {
        this.isFeatured = isFeatured;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }
}
