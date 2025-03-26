package com.zombies.cowhealthai;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.UUID;

public class AdminActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private ImageView cowImage;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Set up the custom toolbar
        Toolbar toolbar = findViewById(R.id.admin_toolbar);
        setSupportActionBar(toolbar);

        // Initialize Database Helper
        databaseHelper = new DatabaseHelper(this);

        //Ask AI button
        Button adminButton = findViewById(R.id.admin_button);
        adminButton.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, ChatbotActivity.class);
            startActivity(intent);
        });
        // Find the button and set click listener
        Button addCowButton = findViewById(R.id.admin_button2);
        addCowButton.setOnClickListener(v -> showAddCowPopup());

        // Retrieve Button
        Button viewCowsButton = findViewById(R.id.admin_button3);
        viewCowsButton.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, ViewCowsActivity.class);
            startActivity(intent);
        });

        //Educational Content
        Button educationalButton = findViewById(R.id.admin_button4);
        educationalButton.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, EducationalContentActivity.class);
            startActivity(intent);
        });

    }

    private void showAddCowPopup() {
        // Inflate the custom layout for the popup
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_cow_details, null);

        // Find UI elements inside the popup
        cowImage = dialogView.findViewById(R.id.cow_image);
        Button uploadImageButton = dialogView.findViewById(R.id.upload_image_button);
        ImageView closeDialog = dialogView.findViewById(R.id.close_dialog);
        EditText cowName = dialogView.findViewById(R.id.cow_name);
        EditText cowBreed = dialogView.findViewById(R.id.cow_breed);
        EditText cowWeight = dialogView.findViewById(R.id.cow_weight);
        EditText cowAge = dialogView.findViewById(R.id.cow_age);
        EditText milkStored = dialogView.findViewById(R.id.cow_milk);
        Button submitButton = dialogView.findViewById(R.id.submit_button);

        // Create AlertDialog
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();

        // Handle image selection when clicking "Upload Image"
        uploadImageButton.setOnClickListener(v -> openImagePicker());

        // Close dialog when X icon is clicked
        closeDialog.setOnClickListener(v -> dialog.dismiss());

        // Handle Submit button click
        submitButton.setOnClickListener(v -> {
            String name = cowName.getText().toString().trim();
            String breed = cowBreed.getText().toString().trim();
            String weight = cowWeight.getText().toString().trim();
            String age = cowAge.getText().toString().trim();
            String milk = milkStored.getText().toString().trim();

            if (name.isEmpty() || breed.isEmpty() || weight.isEmpty() || age.isEmpty() || milk.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Save the image to internal storage and get the file path
            String imagePath = imageUri != null ? saveImageToInternalStorage(imageUri) : "";

            // Store Data in Database
            boolean inserted = databaseHelper.insertCow(name, breed, weight, age, milk, imagePath);

            if (inserted) {
                Toast.makeText(this, "Cow details saved!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to save details", Toast.LENGTH_SHORT).show();
            }

            dialog.dismiss();
        });

        // Show the custom dialog
        dialog.show();
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            if (cowImage != null) {
                cowImage.setImageURI(imageUri);
            }
        }
    }

    private String saveImageToInternalStorage(Uri uri) {
        try {
            // Generate a unique file name
            String fileName = "cow_" + UUID.randomUUID().toString() + ".jpg";

            // Open InputStream from the selected image URI
            ContentResolver resolver = getContentResolver();
            InputStream inputStream = resolver.openInputStream(uri);

            // Create a file in the internal storage
            File file = new File(getFilesDir(), fileName);
            FileOutputStream outputStream = new FileOutputStream(file);

            // Read image into a Bitmap
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);

            // Close streams
            outputStream.flush();
            outputStream.close();
            inputStream.close();

            return file.getAbsolutePath(); // Return the file path
        } catch (Exception e) {
            Log.e("SaveImage", "Error saving image", e);
            return "";
        }
    }
}
