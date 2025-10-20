package com.example.pi_rates;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class YourAchievementsActivity extends AppCompatActivity {

    String userName;

    private ListView achievementListView;
    private AchievementManager achievementManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_achievements);

        Intent intent = getIntent();
        userName = intent.getStringExtra("USER_NAME");

        achievementListView = findViewById(R.id.achievementListView);
        achievementManager = new AchievementManager(this);

        List<Achievement> achievements = achievementManager.loadAchievements();

        AchievementAdapter adapter = new AchievementAdapter(this, achievements);
        achievementListView.setAdapter(adapter);
    }
    public void back (View view) {
        this.finish();
    }
}