/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.pets;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.pets.data.PetDbHelper;
import com.example.android.pets.data.PetProvider;
import com.example.android.pets.data.ShelterContract.PetsEntry;

/**
 * Displays list of pets that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
        displayDatabaseInfo();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void displayDatabaseInfo() {

        String projection[] = {PetsEntry._ID.toString(),
                PetsEntry.COLUMN_PET_NAME,
                PetsEntry.COLUMN_PET_BREED,
                PetsEntry.COLUMN_PET_GENDER,
                PetsEntry.COLUMN_PET_WEIGHT};
        ListView displayView = (ListView) findViewById(R.id.list);
        Cursor cursor = getContentResolver().query(PetsEntry.uri,
                projection,
                null,
                null,
                null,
                null);
        PetCursorAdapter adapter = new PetCursorAdapter(this, cursor, 0);
        View emptyView = findViewById(R.id.empty_view);
        displayView.setEmptyView(emptyView);
        displayView.setAdapter(adapter);
    }

    private String getGender(int gender) {
        if (gender == PetsEntry.GENDER_MALE)
            return "MALE";
        else if (gender == PetsEntry.GENDER_FEMALE)
            return "FEMALE";
        else
            return "UNKNOWN";
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_insert_dummy_data:
                insertData();
                return true;
            case R.id.action_delete_all_entries:
                deleteData();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteData() {
        getContentResolver().delete(Uri.parse("content://com.example.android.pets/pets/"),
                null,
                null);
        Toast.makeText(this, "Data deleted Succesfully", Toast.LENGTH_LONG).show();
        displayDatabaseInfo();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void insertData() {
        ContentValues values = new ContentValues();
        values.put(PetsEntry.COLUMN_PET_NAME, "Toto");
        values.put(PetsEntry.COLUMN_PET_BREED, "Terrier");
        values.put(PetsEntry.COLUMN_PET_GENDER, PetsEntry.GENDER_MALE);
        values.put(PetsEntry.COLUMN_PET_WEIGHT, 7);
        Uri uri = getContentResolver().insert(Uri.parse("content://com.example.android.pets/pets"),
                values);
        displayDatabaseInfo();
    }
}
