package com.example.SmartBath.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.sql.Date;
import java.time.LocalDate;

public class DatabaseHandler extends SQLiteOpenHelper {

    // DB Version
    private static final int DATABASE_VERSION = 1;

    // DB Name
    private static final String DATABASE_NAME = "test.db";

    // Table Name
    private static final String TABLE_NAME_USER = "userdata";
    private static final String TABLE_NAME_LIGHT = "lightdata";
    private static final String TABLE_NAME_ADMIN = "admindata";


    // Table Fields
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_LOCATION = "location";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_ROLE = "role";
    private static final String COLUMN_BRIGHTNESS = "brightness";
    private static final String COLUMN_MODE = "mode";
    private static final String COLUMN_COLOR1 = "color1";
    private static final String COLUMN_COLOR2 = "color2";
    private static final String COLUMN_COLOR3 = "color3";



    SQLiteDatabase database;

    public DatabaseHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        database = getWritableDatabase();
    }



    @Override
    public void onCreate(SQLiteDatabase db) {


        db.execSQL("CREATE TABLE " + TABLE_NAME_USER + " ( " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT, " + COLUMN_PASSWORD + " TEXT, " + COLUMN_ROLE + " TEXT)");

//        db.execSQL("CREATE TABLE " + TABLE_NAME_PATIENT + " ( " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
//                + COLUMN_NAME + " TEXT, " + COLUMN_SURNAME + " TEXT, " + COLUMN_AGE + " INTEGER, " + COLUMN_ADDRESS
//                + " TEXT, " + COLUMN_PHONE + " TEXT, " + COLUMN_CONDITION + " TEXT, " + COLUMN_USERNAME +
//                " TEXT, FOREIGN KEY( " + COLUMN_USERNAME + ") REFERENCES " + TABLE_NAME_USER + " (" + COLUMN_USERNAME + "))");

        db.execSQL("CREATE TABLE " + TABLE_NAME_LIGHT + " ( " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " TEXT, " + COLUMN_MODE + " TEXT, " + COLUMN_BRIGHTNESS + " INTEGER, " + COLUMN_COLOR1 + " INTEGER, "
                + COLUMN_COLOR2 + " INTEGER, " + COLUMN_COLOR3 + " INTEGER, " + COLUMN_USERNAME +
                " TEXT, FOREIGN KEY( " + COLUMN_USERNAME + ") REFERENCES " + TABLE_NAME_USER + " (" + COLUMN_USERNAME + "))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_ADMIN);
        onCreate(db);
    }



    public void deleteAllUsers() {
        database = getWritableDatabase();
        database.execSQL("delete from " + TABLE_NAME_USER);
    }
    public void deleteAllLights(){
        database = getWritableDatabase();
        database.execSQL("delete from " + TABLE_NAME_LIGHT);
    }
    public void deleteAllAdmins() {
        database = getWritableDatabase();
        database.execSQL("delete from " + TABLE_NAME_ADMIN);
    }

    public void deleteAll(){
        deleteAllUsers();
        deleteAllLights();
    }



    public boolean insertLight(String name, String mode, int brightness, int color1, int color2, int color3, String username) {
        database = getWritableDatabase();
        ContentValues initial = new ContentValues();
        initial.put("name", name);
        initial.put("mode", mode);
        initial.put("brightness", brightness);
        initial.put("color1", color1);
        initial.put("color2", color2);
        initial.put("color3", color3);
        initial.put("username", username);
        long result = database.insert(TABLE_NAME_LIGHT, null, initial);
        if(result == -1)
            return false;
        else {
            return true;
        }

    }

    public boolean insertUser(String username, String password, String role){
        database = getWritableDatabase();
        ContentValues initial = new ContentValues();
        initial.put("username", username);
        initial.put("password", password);
        initial.put("role", role);
        long result = database.insert(TABLE_NAME_USER, null, initial);
        if(result == -1)
            return false;
        else {
            return true;
        }
    }

    public Cursor searchUserByUsername(String userName){
        database = getWritableDatabase();
        Cursor cursor = database.rawQuery("select * from userdata where username LIKE '" + userName + "'", null);
        return cursor;
    }


    public Cursor searchUserInLights(String username) {
        database = getWritableDatabase();
        Cursor cursor = database.rawQuery("select * from lightdata where username LIKE '" + username + "'", null);
        return cursor;
    }

    public void editLight(String name, String mode, int brightness, int color1, int color2, int color3, String username) {
        database = getWritableDatabase();
        ContentValues values =  new ContentValues();
        values.put("name", name);
        values.put("mode", mode);
        values.put("brightness", brightness);
        values.put("color1", color1);
        values.put("color2", color2);
        values.put("color3", color3);
        values.put("username", username);
        database.update(TABLE_NAME_LIGHT, values, "username='" + username + "'", null);
    }

    public Cursor getPatientIdByUsername(String username) {
        database = getWritableDatabase();
        Cursor cursor = database.rawQuery("select id from patientdata where username LIKE '" + username + "'", null);
        return cursor;
    }

    public Cursor checkAppointment(int id, LocalDate date){
        database = getWritableDatabase();
        Cursor cursor = database.rawQuery("select * from appointmentdata where date = '" + date + "' and idPatient = " + id, null);
        return cursor;
    }
}
