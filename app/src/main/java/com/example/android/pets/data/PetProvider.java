package com.example.android.pets.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

public class PetProvider extends ContentProvider {
    private PetDbHelper mPetDbHelper;
    private static final int PETS = 100;
    private static final int PETS_ID = 101;
    private static UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    public static final String AUTHORITY = "com.example.android.pets";
    private static final String MULTIPLE_TYPE = "vnd.android.cursor.dir/com.example.android.pets/pets";
    private static final String ITEM_TYPE = "vnd.android.cursor.item/com.example.android.pets/pets";

    static {
        sUriMatcher.addURI(AUTHORITY, "pets", PETS);
        sUriMatcher.addURI(AUTHORITY, "pets/#", PETS_ID);
    }

    @Override
    public boolean onCreate() {
        mPetDbHelper = new PetDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Log.e("JJ", "Here");
        Cursor cursor;
        SQLiteDatabase db = mPetDbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        switch (match) {
            case (PETS):
                cursor = db.query(ShelterContract.PetsEntry.TABLE_NAME,
                        projection,
                        null,
                        null,
                        null,
                        null,
                        null);
                break;
            case (PETS_ID):
                selection = ShelterContract.PetsEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(ShelterContract.PetsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null);
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri format");
        }
        cursor.setNotificationUri(getContext().getContentResolver(), Uri.parse("content://" + AUTHORITY + "/pets/"));
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                return MULTIPLE_TYPE;
            case PETS_ID:
                return ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Error");
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                String name = values.getAsString(ShelterContract.PetsEntry.COLUMN_PET_NAME);
                if (name == null) {
                    throw new IllegalArgumentException("Pet requires a name");
                }
                Integer gender = values.getAsInteger(ShelterContract.PetsEntry.COLUMN_PET_GENDER);
                if (gender == null || !ShelterContract.PetsEntry.isValidGender(gender)) {
                    throw new IllegalArgumentException("Pet requires valid gender");
                }
                Integer weight = values.getAsInteger(ShelterContract.PetsEntry.COLUMN_PET_WEIGHT);
                if (weight != null && weight < 0) {
                    throw new IllegalArgumentException("Pet requires valid weight");
                }
                SQLiteDatabase db = mPetDbHelper.getWritableDatabase();
                long id = db.insert(ShelterContract.PetsEntry.TABLE_NAME,
                        null,
                        values);
                if (id == -1) {
                    Log.e("this", "Failed to insert row for " + uri);
                    return null;
                }
                Toast.makeText(getContext(), "Pet Saved !", Toast.LENGTH_LONG).show();
                getContext().getContentResolver().notifyChange(uri, null);
                return (Uri.parse("content://com.example.android.pets/pets/id"));
            default:
                throw new IllegalArgumentException("Pet cannot be saved");
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                return (deletePet(uri, selection, selectionArgs));
            case PETS_ID:
                selection = ShelterContract.PetsEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return (deletePet(uri, selection, selectionArgs));
            default:
                throw new IllegalArgumentException("Error Deleting");
        }
    }

    private int deletePet(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mPetDbHelper.getWritableDatabase();
        int rows= db.delete(ShelterContract.PetsEntry.TABLE_NAME,
                selection,
                selectionArgs);
        if(rows!=0)
            getContext().getContentResolver().notifyChange(uri,null);
        return rows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                return updatePet(uri, values, selection, selectionArgs);
            case PETS_ID:
                selection = ShelterContract.PetsEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updatePet(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Bad Update Operation");
        }
    }

    private int updatePet(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.containsKey(ShelterContract.PetsEntry.COLUMN_PET_NAME)) {
            String name = values.getAsString(ShelterContract.PetsEntry.COLUMN_PET_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Pet requires a name");
            }
        }
        if (values.containsKey(ShelterContract.PetsEntry.COLUMN_PET_GENDER)) {
            Integer gender = values.getAsInteger(ShelterContract.PetsEntry.COLUMN_PET_GENDER);
            if (gender == null || !ShelterContract.PetsEntry.isValidGender(gender)) {
                throw new IllegalArgumentException("Pet requires valid gender");
            }
        }
        if (values.containsKey(ShelterContract.PetsEntry.COLUMN_PET_WEIGHT)) {
            Integer weight = values.getAsInteger(ShelterContract.PetsEntry.COLUMN_PET_WEIGHT);
            if (weight != null && weight < 0) {
                throw new IllegalArgumentException("Pet requires valid weight");
            }
        }
        if (values.size() == 0) {
            return 0;
        }
        SQLiteDatabase db = mPetDbHelper.getWritableDatabase();
        int rows = db.update(ShelterContract.PetsEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
        if(rows!=0)
            getContext().getContentResolver().notifyChange(uri,null);
        return rows;
    }
}
