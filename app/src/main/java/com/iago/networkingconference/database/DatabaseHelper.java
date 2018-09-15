package com.iago.networkingconference.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.iago.networkingconference.database.tags.Tags;
import com.iago.networkingconference.database.tags.TagsUsageTable;
import com.iago.networkingconference.models.Attendee;
import com.iago.networkingconference.models.CustomFields;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import timber.log.Timber;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    static final String FETCH_ALL_TAGS_DATA
            = "select tag, tagType from tags_data group by tagType, tag order by tagType, tag";

    static final String FETCH_ALL_WITH_TAGS = "SELECT \n" +
            "*\n" +
            " FROM \n" +
            "(select tags_data.attendee_id from tags_data %s group by tags_data.attendee_id) cond\n" +
            "INNER JOIN attendees_detail ON attendees_detail.id = cond.attendee_id\n";

    private static final String DATABASE_NAME = "attendees.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_ATTENDEES_DETAIL_VIEW_SQL
            = "CREATE VIEW attendees_detail\n" +
            "AS\n" +
            "SELECT \n" +
            "attendees.id, \n" +
            "attendees.isFeatured, \n" +
            "attendees.likes,\n" +
            "customFields.age,\n" +
            "customFields.city,\n" +
            "customFields.company,\n" +
            "customFields.companySize,\n" +
            "customFields.countryCode,\n" +
            "customFields.email,\n" +
            "customFields.fullName,\n" +
            "customFields.gender,\n" +
            "customFields.phone,\n" +
            "customFields.position,\n" +
            "customFields.publicEmail,\n" +
            "media.defaultUrl,\n" +
            "media.smallUrl,\n" +
            "media.originalUrl,\n" +
            "media.tinyUrl\n" +
            " FROM \n" +
            " attendees\n" +
            "INNER JOIN customFields ON attendees.customFields_id = customFields.id\n" +
            "INNER JOIN media ON attendees.media_id = media.id";

    private static final String CREATE_TAGS_DATA
            = "CREATE VIEW tags_data "
                + "AS SELECT tags.tag, tags_usage_table.attendee_id, tags_usage_table.tagType "
                + "from tags inner join tags_usage_table ON tags.id = tags_usage_table.tag_id";

    private Class[] tables = new Class[] {
            Attendee.class,
            CustomFields.class,
            DBMedia.class,

            Tags.class,
            TagsUsageTable.class
    };

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            createTables(connectionSource);
            database.execSQL(CREATE_ATTENDEES_DETAIL_VIEW_SQL);
            database.execSQL(CREATE_TAGS_DATA);
        } catch (SQLException e) {
            Timber.e(e, "error creating tables");
        }
    }

    private void createTables(ConnectionSource connectionSource) throws SQLException {
        for (Class clazz : tables) {
            TableUtils.createTable(connectionSource, clazz);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
           dropTables(connectionSource);
           createTables(connectionSource);
        } catch (SQLException e) {
            Timber.e(e, "error recreating database on upgrade");
        }
    }

    private void dropTables(ConnectionSource connectionSource) throws SQLException {
        for (Class clazz : tables) {
            TableUtils.dropTable(connectionSource, clazz, true);
        }
    }
}
