package com.example.android.pets.data;

public final class ShelterContract {
    public static final class PetsEntry {
        public final String TABLE = "pets";
        public final String ID = "_id";
        public final String NAME = "name";
        public final String GENDER = "gender";
        public final String BREED = "breed";
        public final String WEIGHT = "weight";
        public final int MALE=1;
        public final int FEMALE=2;
        public final int UNKNOWN=0;
    }
}
