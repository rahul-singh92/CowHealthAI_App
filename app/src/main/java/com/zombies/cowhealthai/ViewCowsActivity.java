package com.zombies.cowhealthai;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ViewCowsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CowAdapter cowAdapter;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cows);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseHelper = new DatabaseHelper(this);
        loadCowDetails();
    }

    private void loadCowDetails() {
        List<Cow> cowList = new ArrayList<>();
        Cursor cursor = databaseHelper.getAllCows();

        if (cursor == null) {
            Toast.makeText(this, "Error retrieving data from database!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(1);
                String breed = cursor.getString(2);
                String weight = cursor.getString(3);
                String age = cursor.getString(4);
                String milk = cursor.getString(5);
                String imageUri = cursor.getString(6);

                cowList.add(new Cow(name, breed, weight, age, milk, imageUri));
            } while (cursor.moveToNext());
        } else {
            Toast.makeText(this, "No cow details found!", Toast.LENGTH_SHORT).show();
        }

        cursor.close();
        cowAdapter = new CowAdapter(this, cowList);
        recyclerView.setAdapter(cowAdapter);
    }
}
