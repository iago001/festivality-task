package com.iago.networkingconference.database;

import android.database.Cursor;
import android.text.TextUtils;

import com.iago.networkingconference.models.Attendee;
import com.iago.networkingconference.models.CustomFields;
import com.iago.networkingconference.views.listitems.PopupListHeader;
import com.iago.networkingconference.views.listitems.PopupListItem;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.Observer;
import timber.log.Timber;

import static com.iago.networkingconference.database.DatabaseHelper.FETCH_ALL_TAGS_DATA;
import static com.iago.networkingconference.database.DatabaseHelper.FETCH_ALL_WITH_TAGS;

public class DataFetchHelper {

    private DatabaseHelper databaseHelper;

    private Dao<Attendee, String> attendeesDao;

    private Dao<CustomFields, Integer> customFieldsDao;

    DataFetchHelper(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
        try {
            attendeesDao = databaseHelper.getDao(Attendee.class);
            customFieldsDao = databaseHelper.getDao(CustomFields.class);
        } catch (SQLException e) {
            Timber.e(e, "error creating attendee dao");
        }
    }

    public Observable<List<Attendee>> fetchByTags(final String orderBy,
                                                  final String defaultOrderBy,
                                                  final String searchStr,
                                                  final Set<PopupListItem> tagSet) {
        return new Observable<List<Attendee>>() {
            @Override
            protected void subscribeActual(Observer<? super List<Attendee>> observer) {
                String sql = "";
                if (!tagSet.isEmpty()) {
                    StringBuilder tagsWhereClauseBuilder = new StringBuilder();
                    for (PopupListItem listItem : tagSet) {
                        tagsWhereClauseBuilder.append("(tags_data.tag = \"" + listItem.getLabel() + "\" AND ");
                        tagsWhereClauseBuilder.append("tags_data.tagType = \"" + listItem.getTagType() + "\")");
                        tagsWhereClauseBuilder.append(" OR ");
                    }
                    String whereClause = tagsWhereClauseBuilder.substring(0, tagsWhereClauseBuilder.length() - 4);

                    sql = String.format(FETCH_ALL_WITH_TAGS, "where " + whereClause);
                } else {
                    sql = String.format(FETCH_ALL_WITH_TAGS, "");
                }

                if (!TextUtils.isEmpty(searchStr)) {
                    sql += " WHERE attendees_detail.fullName like \"%" + searchStr +"%\"";
                }

                sql += " ORDER BY ";
                if (orderBy != null) {
                    sql += "attendees_detail." + orderBy + ", ";
                }
                sql += "attendees_detail." + defaultOrderBy;

                List<Attendee> attendeeList = new ArrayList<>();

                Cursor cursor = databaseHelper.getReadableDatabase().rawQuery(sql, null);
                if (cursor.moveToFirst()) {
                    do {
                        Attendee attendee = new Attendee();
                        CustomFields customFields = new CustomFields();
                        DBMedia media = new DBMedia();
                        attendee.setCustomFields(customFields);
                        attendee.setMedia(media);

                        attendee.setId(cursor.getString(0));
                        customFields.setCompany(cursor.getString(6));
                        customFields.setFullName(cursor.getString(10));
                        customFields.setPosition(cursor.getString(13));

                        media.setTinyUrl(cursor.getString(18));
                        attendeeList.add(attendee);
                    } while (cursor.moveToNext());
                }
                cursor.close();

                observer.onNext(attendeeList);
                observer.onComplete();
            }
        };
    }

    public Observable<List<PopupListHeader>> fetchTagsForFilter() {

        return new Observable<List<PopupListHeader>>() {
            @Override
            protected void subscribeActual(Observer<? super List<PopupListHeader>> observer) {
                List<PopupListHeader> tagsList = new ArrayList<>();

                String currentTagType = null;
                Cursor cursor = databaseHelper.getReadableDatabase().rawQuery(FETCH_ALL_TAGS_DATA, null);
                if (cursor.moveToFirst()) {
                    do {
                        String tagType = cursor.getString(cursor.getColumnIndex("tagType"));
                        String tag = cursor.getString(cursor.getColumnIndex("tag"));

                        if (currentTagType == null || !currentTagType.equals(tagType)) {
                            currentTagType = tagType;
                            PopupListHeader popupListHeader = new PopupListHeader(currentTagType);
                            tagsList.add(popupListHeader);
                        }

                        PopupListItem listItem = new PopupListItem(tag, currentTagType, false);
                        tagsList.add(listItem);
                    } while(cursor.moveToNext());
                }
                cursor.close();

                observer.onNext(tagsList);
                observer.onComplete();
            }
        };

    }

    public Observable<Attendee> fetchAttendeeDetails(final String attendeeId) {

        return new Observable<Attendee>() {
            @Override
            protected void subscribeActual(Observer<? super Attendee> observer) {
                try {
                    observer.onNext(attendeesDao.queryForId(attendeeId));
                    observer.onComplete();
                } catch (SQLException e) {
                    observer.onError(e);
                }
            }
        };
    }
}
