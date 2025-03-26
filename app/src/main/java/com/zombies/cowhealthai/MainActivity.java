package com.zombies.cowhealthai;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDelegate;


public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main); // Ensure this layout exists


        //This code will be put in login section and then we will get accordingly if it is user or admin
        SharedPreferences.Editor editor = getSharedPreferences("UserPrefs",MODE_PRIVATE).edit();
        editor.putBoolean("isAdmin",true);  //Change when you are making for user
        editor.apply();
    }
}


