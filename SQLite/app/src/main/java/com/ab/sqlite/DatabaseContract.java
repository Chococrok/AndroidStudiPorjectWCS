package com.ab.sqlite;

import android.provider.BaseColumns;

/**
 * Created by apprenti on 10/07/17.
 */

public final class DatabaseContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private DatabaseContract() {}


    //CONSTANT

    public static class TweetEntry implements BaseColumns {
        public static final String TABLE_NAME = "tweet_table";
        public static final String COLUMN_NAME_ID = "tweet_id";
        public static final String COLUMN_NAME_CONTENT = "content";
        public static final String COLUMN_NAME_USER_ID = "user_id";
    }

    public static class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "user_table";
        public static final String COLUMN_NAME_ID = "user_id";
        public static final String COLUMN_NAME_NAME = "user_name";
        public static final String COLUMN_NAME_EMAIL = "email";
    }

    public static class OrganizationEntry implements BaseColumns {
        public static final String TABLE_NAME = "oraganization_table";
        public static final String COLUMN_NAME_ID = "oraganization_id";
        public static final String COLUMN_NAME_NAME = "oraganization_name";
        public static final String COLUMN_NAME_WEBSITE_URL = "website_url";
    }

    public static class BelongEntry implements BaseColumns {
        public static final String TABLE_NAME = "belong_table";
        public static final String COLUMN_NAME_ORGANIZATION_ID = "oraganization_id";
        public static final String COLUMN_NAME_USER_ID = "user_id";
    }

    //CREATE

    public static final String SQL_CREATE_TWEET =
            "CREATE TABLE " + TweetEntry.TABLE_NAME + " (" +
                    TweetEntry.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    TweetEntry.COLUMN_NAME_CONTENT + " TEXT," +
                    TweetEntry.COLUMN_NAME_USER_ID + " INTEGER);";

    public static final String SQL_CREATE_USER =
            "CREATE TABLE " + UserEntry.TABLE_NAME + " (" +
                    UserEntry.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    UserEntry.COLUMN_NAME_NAME + " VARCHAR(90)," +
                    UserEntry.COLUMN_NAME_EMAIL + " VARCHAR(90));";

    public static final String SQL_CREATE_ORGANIZATION =
            "CREATE TABLE " + OrganizationEntry.TABLE_NAME + " (" +
                    OrganizationEntry.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    OrganizationEntry.COLUMN_NAME_NAME + " VARCHAR(90)," +
                    OrganizationEntry.COLUMN_NAME_WEBSITE_URL + " VARCHAR(90));";

    public static final String SQL_CREATE_BELONG =
            "CREATE TABLE " + BelongEntry.TABLE_NAME + " (" +
                    BelongEntry.COLUMN_NAME_ORGANIZATION_ID + " INTEGER," +
                    BelongEntry.COLUMN_NAME_USER_ID + " INTEGER);";

    //DELETE

    public static final String SQL_DELETE_TWEET =
            "DROP TABLE IF EXISTS " + TweetEntry.TABLE_NAME;

    public static final String SQL_DELETE_USER =
            "DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME;

    public static final String SQL_DELETE_ORGANIZATION =
            "DROP TABLE IF EXISTS " + OrganizationEntry.TABLE_NAME;

    public static final String SQL_DELETE_BELONG =
            "DROP TABLE IF EXISTS " + BelongEntry.TABLE_NAME;
}

