package com.iago.networkingconference.models;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.iago.networkingconference.database.DBMedia;

import java.lang.reflect.Type;

public class AttendeeAdapter implements JsonDeserializer<Attendee> {

    private static final String CUSTOM_FIELDS = "customFields";

    private Gson gson;

    public void setGson(Gson gson) {
        this.gson = gson;
    }

    @Override
    public Attendee deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Attendee attendee = new Attendee();
        JsonObject jsonObject = json.getAsJsonObject();

        attendee.setId(jsonObject.get("id").getAsString());
        attendee.setIsFeatured(jsonObject.get("isFeatured").getAsInt());
        attendee.setLikes(jsonObject.get("likes").getAsInt());

        Media[] mediaArr = gson.fromJson(jsonObject.get("media"), Media[].class);
        if (mediaArr != null && mediaArr.length > 0) {
            DBMedia dbMedia = new DBMedia();
            Media media = mediaArr[0];
            dbMedia.setType(media.getType());
            dbMedia.setLabel(media.getLabel());

            if (media.getFiles() != null) {
                Media.Files files = media.getFiles();
                dbMedia.setDefaultUrl(files.getDefaultURL());

                if (files.getVariations() != null) {
                    Media.Files.Variations variations = files.getVariations();
                    dbMedia.setOriginalUrl(variations.getOriginal());
                    dbMedia.setTinyUrl(variations.getTiny());
                    dbMedia.setSmallUrl(variations.getSmall());
                }
            }
            attendee.setMedia(dbMedia);
        }

        // parse customFields only if it is a proper object
        if (jsonObject.has(CUSTOM_FIELDS) && jsonObject.get(CUSTOM_FIELDS).isJsonObject()) {
            attendee.setCustomFields(gson.fromJson(jsonObject.get(CUSTOM_FIELDS), CustomFields.class));
        }

        return attendee;
    }
}
