package com.iago.networkingconference.database.tags;

import com.iago.networkingconference.models.Attendee;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "tags_usage_table")
public class TagsUsageTable {

    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField(foreign = true)
    private Tags tag;

    @DatabaseField(foreign = true)
    private Attendee attendee;

    @DatabaseField
    private String tagType;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Tags getTag() {
        return tag;
    }

    public void setTag(Tags tag) {
        this.tag = tag;
    }

    public Attendee getAttendee() {
        return attendee;
    }

    public void setAttendee(Attendee attendee) {
        this.attendee = attendee;
    }

    public String getTagType() {
        return tagType;
    }

    public void setTagType(String tagType) {
        this.tagType = tagType;
    }
}
