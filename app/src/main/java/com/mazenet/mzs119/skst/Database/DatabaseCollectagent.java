package com.mazenet.mzs119.skst.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.mazenet.mzs119.skst.Model.CollectModel;

import java.util.ArrayList;

/**
 * Created by mzs119 on 12/22/2017.
 */

public class DatabaseCollectagent extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "chit_collectAgent.db";

    private static final String TABLE_CONTACTS = "collectagent";

    private static final String KEY_ID = "ids";

    private static final String AGENT_ID="Id";

    private static final  String AGENT_NAME="Name";

    private static final  String AGENT_CODE="Emp_Code";

    public DatabaseCollectagent(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String AGENT_TABLE="CREATE TABLE "+TABLE_CONTACTS+"("+KEY_ID+" INTEGER PRIMARY KEY,"+AGENT_ID+" INTEGER,"+AGENT_NAME+" TEXT,"+AGENT_CODE+" TEXT"+")";
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
    public void addCollectAgent(ArrayList<CollectModel> sched) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < sched.size(); i++) {
            ContentValues values = new ContentValues();

           // Custmodel contact = sched.get(i);
            CollectModel agent=sched.get(i);
            values.put(AGENT_ID, agent.getId());
            values.put(AGENT_NAME,agent.getName());
            values.put(AGENT_CODE,agent.getEmp_Code());
            db.insert(TABLE_CONTACTS, null, values);
        }
        db.close();
    }

    public ArrayList<CollectModel> getAllAgent() {
        ArrayList<CollectModel> collectList = new ArrayList<CollectModel>();
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                CollectModel contact = new CollectModel();
                contact.setId(cursor.getString(1));
                contact.setName(cursor.getString(2));
                contact.setEmp_Code(cursor.getString(3));
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
