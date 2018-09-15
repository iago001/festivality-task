package com.iago.networkingconference.database;

import com.iago.networkingconference.database.tags.TagsUsageTable;
import com.iago.networkingconference.database.tags.Tags;
import com.iago.networkingconference.models.Attendee;
import com.iago.networkingconference.models.CustomFields;
import com.iago.networkingconference.network.APIResponse;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import timber.log.Timber;

public class DataStoreHelper {

    private DatabaseHelper databaseHelper;
    private ArrayList<Tags> tagsInDb;

    DataStoreHelper(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    /**
     * Method to store api response to local db
     * @param apiResponse response from server
     * @return true if save successful, otherwise false
     */
    public boolean saveAPIResponseToDB(APIResponse apiResponse) {
        if (apiResponse != null && apiResponse.getResponse() != null) {
            try {
                Timber.d("starting to save data");

                final Dao<Attendee, String> attendeeDao = databaseHelper.getDao(Attendee.class);

                Dao<Tags, Long> tagsDao = databaseHelper.getDao(Tags.class);
                tagsInDb = new ArrayList<>(tagsDao.queryForAll());

                final ArrayList<Attendee> attendees = apiResponse.getResponse();

                attendeeDao.callBatchTasks(new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        for (Attendee attendee : attendees) {
                            attendeeDao.createOrUpdate(attendee);
                        }
                        return null;
                    }
                });

                Timber.d("saved all attendees");

                final List<TagsUsageTable> tagsUsageList = new ArrayList<>();

                for (Attendee attendee : attendees) {

                    // handle tags
                    CustomFields customFields = attendee.getCustomFields();
                    if (customFields != null) {

                        // attendeeProviding
                        if (isSafe(customFields.getAttendeeProviding())) {
                            List<String> attendeeProvidingTags = customFields.getAttendeeProviding();
                            for (String tag : attendeeProvidingTags) {
                                Tags tagFromServer = createOrReuse(tag, tagsDao);

                                TagsUsageTable attendeeProviding = new TagsUsageTable();
                                setTagAndAttendee(attendeeProviding, tagFromServer, attendee, "attendeeProviding");
                                tagsUsageList.add(attendeeProviding);
                            }
                        }

                        // attendeeLookingFor
                        if (isSafe(customFields.getAttendeeLookingFor())) {
                            List<String> attendeeLookingForTags = customFields.getAttendeeLookingFor();
                            for (String tag : attendeeLookingForTags) {
                                Tags tagFromServer = createOrReuse(tag, tagsDao);

                                TagsUsageTable attendeeLookingFor = new TagsUsageTable();
                                setTagAndAttendee(attendeeLookingFor, tagFromServer, attendee, "attendeeLookingFor");
                                tagsUsageList.add(attendeeLookingFor);
                            }
                        }

                        // positionType
                        if (isSafe(customFields.getPositionType())) {
                            List<String> positionTypes = customFields.getPositionType();
                            for (String tag : positionTypes) {
                                Tags tagFromServer = createOrReuse(tag, tagsDao);

                                TagsUsageTable positionType = new TagsUsageTable();
                                setTagAndAttendee(positionType, tagFromServer, attendee, "positionType");
                                tagsUsageList.add(positionType);
                            }
                        }

                        // attendeeType
                        if (isSafe(customFields.getAttendeeType())) {
                            List<String> attendeeTypeTags = customFields.getAttendeeType();
                            for (String tag : attendeeTypeTags) {
                                Tags tagFromServer = createOrReuse(tag, tagsDao);

                                TagsUsageTable attendeeType = new TagsUsageTable();
                                setTagAndAttendee(attendeeType, tagFromServer, attendee, "attendeeType");
                                tagsUsageList.add(attendeeType);
                            }
                        }

                        // industry tags
                        if (isSafe(customFields.getIndustryTags())) {
                            List<String> industryTags = customFields.getIndustryTags();
                            for (String tag : industryTags) {
                                Tags tagFromServer = createOrReuse(tag, tagsDao);

                                TagsUsageTable industryTag = new TagsUsageTable();
                                setTagAndAttendee(industryTag, tagFromServer, attendee, "industryTags");
                                tagsUsageList.add(industryTag);
                            }
                        }

                        // industry complimentary tags
                        if (isSafe(customFields.getIndustryComplimentaryTags())) {
                            List<String> industryComplimentaryTags
                                    = customFields.getIndustryComplimentaryTags();
                            for (String tag : industryComplimentaryTags) {
                                Tags tagFromServer = createOrReuse(tag, tagsDao);

                                TagsUsageTable industryComplimentaryTag = new TagsUsageTable();
                                setTagAndAttendee(industryComplimentaryTag, tagFromServer,
                                        attendee, "industryComplimentaryTags");
                                tagsUsageList.add(industryComplimentaryTag);
                            }
                        }
                    }
                }

                Timber.d("all unique tags saved and list computed");

                final Dao<TagsUsageTable, Long> tagsUsageTableDao
                        = databaseHelper.getDao(TagsUsageTable.class);
                tagsUsageTableDao.callBatchTasks(new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        for (TagsUsageTable tagsUsage : tagsUsageList) {
                            tagsUsageTableDao.createOrUpdate(tagsUsage);
                        }
                        return null;
                    }
                });

                Timber.d("tags usage data saved");

                return true;
            } catch (Exception e) {
                Timber.e(e, "error saving data");
            }
        }
        return false;
    }

    private Tags createOrReuse(String tag, Dao<Tags, Long> tagsDao) throws SQLException {
        Tags tagFromServer = new Tags(tag);
        int index = tagsInDb.indexOf(tagFromServer);
        // if exists
        if (index >= 0) {
            return tagsInDb.get(index);
        } else {
            tagsDao.createOrUpdate(tagFromServer);
            // to avoid re-query
            tagsInDb.add(tagFromServer);
            return tagFromServer;
        }
    }

    private void setTagAndAttendee(TagsUsageTable tagsUsageTable, Tags tags, Attendee attendee, String tagType) {
        tagsUsageTable.setTag(tags);
        tagsUsageTable.setAttendee(attendee);
        tagsUsageTable.setTagType(tagType);
    }

    private boolean isSafe(List<String> tagList) {
        return tagList != null && !tagList.isEmpty();
    }
}
