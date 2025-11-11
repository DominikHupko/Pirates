package com.example.pi_rates;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private String selectedAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        // Avatar ImageView-k inicializálása
        ImageView avatar1 = findViewById(R.id.avatar1);
        ImageView avatar2 = findViewById(R.id.avatar2);
        //ImageView avatar3 = findViewById(R.id.avatar3);

        avatar1.setOnClickListener(view -> selectAvatar("avatar1"));
        avatar2.setOnClickListener(view -> selectAvatar("avatar2"));
        //avatar3.setOnClickListener(view -> selectAvatar("avatar3"));
    }

    private void selectAvatar(String avatar) {
        selectedAvatar = avatar;

        MediaPlayer sound = MediaPlayer.create(this,R.raw.select_sound);
        sound.start();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("SELECTED_AVATAR", avatar);
        editor.apply();

        Toast.makeText(this, "Avatar selected: " + avatar, Toast.LENGTH_SHORT).show();
    }
    public void back (View view) {
        this.finish();
    }
}
