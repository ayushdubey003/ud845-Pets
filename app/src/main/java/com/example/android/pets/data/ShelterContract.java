package com.example.android.pets.data;

import android.net.Uri;
import android.provider.BaseColumns;

public final class ShelterContract {
    private ShelterContract() {
    }

    public static final class PetsEntry implements BaseColumns {
        public static final Uri BASE_URI = Uri.parse("content://com.example.android.pets");
        public static final Uri uri = Uri.withAppendedPath(BASE_URI, "pets/");
        public static final String TABLE_NAME = "pets";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_PET_NAME = "name";
        public static final String COLUMN_PET_GENDER = "gender";
        public static final String COLUMN_PET_BREED = "breed";
        public static final String COLUMN_PET_WEIGHT = "weight";
        public static final int GENDER_MALE = 1;
        public static final int GENDER_FEMALE = 2;
        public static final int GENDER_UNKNOWN = 0;

        public static boolean isValidGender(Integer gender) {
            if (gender == PetsEntry.GENDER_MALE || gender == PetsEntry.GENDER_FEMALE || gender == PetsEntry.GENDER_UNKNOWN)
                return true;
            else
                return false;
        }
    }
}
