package com.ab.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getContext;
import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.ab.sqlite", appContext.getPackageName());

    }

    @Test
    public void writeDataBase() throws Exception {

        DBHelper helper = new DBHelper(InstrumentationRegistry.getTargetContext());
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues user = new ContentValues();
        user.put(DatabaseContract.UserEntry.COLUMN_NAME_NAME, "Arnaud");
        user.put(DatabaseContract.UserEntry.COLUMN_NAME_EMAIL, "nono@mymail.com");



        long newUserId = db.insert(DatabaseContract.UserEntry.TABLE_NAME, null, user);
        assertNotEquals(-1, newUserId);

        for (int i = 0; i < 10; i++){
            ContentValues tweet = new ContentValues();
            tweet.put(DatabaseContract.TweetEntry.COLUMN_NAME_CONTENT, "content " + i);
            tweet.put(DatabaseContract.TweetEntry.COLUMN_NAME_USER_ID, (int) newUserId);
            long newTweetId = db.insert(DatabaseContract.TweetEntry.TABLE_NAME, null, tweet);
            assertNotEquals(-1, newTweetId);
        }

        ContentValues organization = new ContentValues();
        organization.put(DatabaseContract.OrganizationEntry.COLUMN_NAME_NAME, "WCS");
        organization.put(DatabaseContract.OrganizationEntry.COLUMN_NAME_WEBSITE_URL, "wcs.com");

        long newOrganizationId = db.insert(DatabaseContract.OrganizationEntry.TABLE_NAME, null, organization);
        assertNotEquals(-1, newOrganizationId);

        ContentValues belong = new ContentValues();
        belong.put(DatabaseContract.BelongEntry.COLUMN_NAME_USER_ID, (int) newUserId);
        belong.put(DatabaseContract.BelongEntry.COLUMN_NAME_ORGANIZATION_ID, (int) newOrganizationId);
        long newBelongId = db.insert(DatabaseContract.BelongEntry.TABLE_NAME, null, belong);
        assertNotEquals(-1, newBelongId);
    }

    @Test
    public void readDataBase() throws Exception {

        DBHelper helper = new DBHelper(InstrumentationRegistry.getTargetContext());
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursorUser = db.rawQuery("SELECT "
                + DatabaseContract.TweetEntry.COLUMN_NAME_USER_ID
                + " FROM "
                + DatabaseContract.TweetEntry.TABLE_NAME
                + " ORDER BY "
                + DatabaseContract.TweetEntry.COLUMN_NAME_USER_ID, null);

        cursorUser.moveToLast();
        int lastUserId = cursorUser.getInt(cursorUser.getColumnIndex(DatabaseContract.TweetEntry.COLUMN_NAME_USER_ID));

        Cursor cursorTweet = db.rawQuery("SELECT "
                + DatabaseContract.TweetEntry.COLUMN_NAME_CONTENT
                + " , "
                + DatabaseContract.TweetEntry.COLUMN_NAME_USER_ID
                + " FROM "
                + DatabaseContract.TweetEntry.TABLE_NAME
                + " WHERE "
                + DatabaseContract.TweetEntry.COLUMN_NAME_USER_ID
                + " = "
                + lastUserId, null);

        int userIdIndex = cursorTweet.getColumnIndex(DatabaseContract.TweetEntry.COLUMN_NAME_USER_ID);
        int tweetContentColumnIndex = cursorTweet.getColumnIndex(DatabaseContract.TweetEntry.COLUMN_NAME_CONTENT);

        while (cursorTweet.moveToNext()) {
            int userId = cursorTweet.getInt(userIdIndex);
            String s = cursorTweet.getString(tweetContentColumnIndex);
            assertEquals(true, s.contains("content"));
        }

        cursorTweet.close();

    }
}
