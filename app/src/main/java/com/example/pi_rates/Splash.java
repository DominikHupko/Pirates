package com.example.pi_rates;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SharedPreferences sharedPreferences = getSharedPreferences("UserLog", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("USER_NAME", "Guest");
        Intent intent;
        if (username.equals("Guest")){
            intent = new Intent(Splash.this, StarterActivity.class);
        }
        else{
            intent = new Intent(Splash.this, MainActivity.class);
            intent.putExtra("USER_NAME", username);
        }
        startActivity(intent);
        finish();
    }
}