package com.mazenet.mzs119.skst.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mazenet.mzs119.skst.Model.FeedbackModel;

import java.util.ArrayList;

/**
 * Created by mzs119 on 12/22/2017.
 */

public class Databasefeedback extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "chit_feedback";

    private static final String TABLE_CONTACTS = "feedback";

    private static final String KEY_ID = "Id";


    private static final String KEY_Feed = "Name";

    private static final String KEY_Follow = "Follow";

    public Databasefeedback(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String AGENT_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_Feed + " TEXT," + KEY_Follow + " TEXT" + ")";
        sqLiteDatabase.execSQL(AGENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

        onCreate(sqLiteDatabase);
    }

    public void deletetable() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_CONTACTS, null, null);
        db.close();
    }

    public void addfeedback(ArrayList<FeedbackModel> sched) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < sched.size(); i++) {
            ContentValues values = new ContentValues();

            // Custmodel contact = sched.get(i);
            FeedbackModel agent = sched.get(i);
            values.put(KEY_Feed, agent.getName());
            values.put(KEY_Follow, agent.getFollowup());
            db.insert(TABLE_CONTACTS, null, values);
        }
        db.close();
    }

    public ArrayList<FeedbackModel> getAllfeed() {
        ArrayList<FeedbackModel> collectList = new ArrayList<FeedbackModel>();
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                FeedbackModel contact = new FeedbackModel();
                contact.setName(cursor.getString(1));
                contact.setFollowup(cursor.getString(2));
                collectList.add(contact);

            } while (cursor.moveToNext());
        }
        db.close();

        return collectList;
    }

    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int value = cursor.getCount();
        db.close();

        return value;
    }

}
