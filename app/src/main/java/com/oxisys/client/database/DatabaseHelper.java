package com.oxisys.client.database;

/**
 * Created by joshu on 9/3/2016.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "credentialsManager";

    // Contacts table name
    public static final String TABLE_CREDENTIALS = "credentials";

    // Contacts Table Columns names
    public static final String KEY_ID = "id";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_PRIVILEGE = "privilege";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CREDENTIALS_TABLE = "CREATE TABLE " + TABLE_CREDENTIALS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_USERNAME + " TEXT,"
                + KEY_PASSWORD + " TEXT," + KEY_PRIVILEGE + " INTEGER " + ")";
        db.execSQL(CREATE_CREDENTIALS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CREDENTIALS);

        // Create tables again
        onCreate(db);
    }

    // Adding new contact
    public void addCredentials(Credentials credentials) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, credentials.getUsername()); // Contact Name
        values.put(KEY_PASSWORD, credentials.getPassword()); // Contact Phone Number
        values.put(KEY_PRIVILEGE, credentials.getPrivilege());

        // Inserting Row
        db.insert(TABLE_CREDENTIALS, null, values);
        db.close(); // Closing database connection
    }


    public Credentials getCredentials(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CREDENTIALS, new String[]{KEY_ID,
                        KEY_USERNAME, KEY_PASSWORD}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {


            Credentials credentials = new Credentials(Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1), cursor.getString(2), Integer.parseInt(cursor.getString(3)));
            return credentials;
        }
        return null;
    }

    public List<Credentials> getAllCredentials() {
        List<Credentials> credentials = new ArrayList<Credentials>();

        SQLiteDatabase db = this.getReadableDatabase();
        String[] allColumns = {KEY_USERNAME};
        String selecet = "SELECT " + KEY_USERNAME + " FROM " + TABLE_CREDENTIALS + " WHERE username=?";
        Cursor cursor = db.rawQuery(selecet, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Credentials credentials1 = cursorToCredential(cursor);
            credentials.add(credentials1);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return credentials;
    }

    private Credentials cursorToCredential(Cursor cursor) {
        Credentials credentials = new Credentials();
        credentials.setID(cursor.getInt(0));
        credentials.setUsername(cursor.getString(1));
        return credentials;
    }

    public Cursor getEntry(String[] columns) {
        return this.getReadableDatabase().query(TABLE_CREDENTIALS, columns, null, null, null, null, null);
    }

    // Getting contacts Count
    public int getCredentialsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CREDENTIALS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    // Updating single contact
    public int updateCredentials(Credentials credentials) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, credentials.getUsername());
        values.put(KEY_PASSWORD, credentials.getPassword());
        values.put(KEY_PRIVILEGE, credentials.getPrivilege());

        // updating row
        return db.update(TABLE_CREDENTIALS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(credentials.getID())});
    }

    // Deleting single contact
    public void deleteCredentials(Credentials credentials) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CREDENTIALS, KEY_USERNAME + " = ?",
                new String[]{credentials.getUsername()});
        db.close();
    }

    public boolean isAdmin(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] allColumns = {username};
        String selecet = "SELECT " + KEY_PRIVILEGE + " FROM " + TABLE_CREDENTIALS + " WHERE username=?";
        Cursor cursor = db.rawQuery(selecet, allColumns);
        if (cursor.getInt(0) != 1) {
            return false;
        }
        return true;
    }
}
