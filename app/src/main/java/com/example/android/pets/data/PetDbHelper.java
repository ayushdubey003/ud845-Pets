package com.example.android.pets.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.android.pets.data.ShelterContract.PetsEntry;

public class PetDbHelper extends SQLiteOpenHelper {
    public static String DATABASE_NAME = "shelter.db";
    public static int DATABASE_VERSION = 1;

    public PetDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_PETS_TABLE = "CREATE TABLE " + PetsEntry.TABLE_NAME + "(" +
                PetsEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                +PetsEntry.COLUMN_PET_NAME+" TEXT NOT NULL,"
                +PetsEntry.COLUMN_PET_BREED+" TEXT,"
                +PetsEntry.COLUMN_PET_GENDER+" INTEGER DEFAULT 0,"
                +PetsEntry.COLUMN_PET_WEIGHT+" INTEGER NOT NULL DEFAULT 0);";
        Log.e("this",SQL_CREATE_PETS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_PETS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //do nothing
    }
}
