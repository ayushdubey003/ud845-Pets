package com.example.android.pets.data;

import android.provider.BaseColumns;

public final class ShelterContract {
    private ShelterContract() {
    }

    public static final class PetsEntry implements BaseColumns {
        public final String TABLE = BaseColumns._ID;
        public final String ID = "_id";
        public final String NAME = "name";
        public final String GENDER = "gender";
        public final String BREED = "breed";
        public final String WEIGHT = "weight";
        public final int MALE = 1;
        public final int FEMALE = 2;
        public final int UNKNOWN = 0;
    }
}
