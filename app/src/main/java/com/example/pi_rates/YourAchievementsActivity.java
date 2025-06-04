package com.example.pi_rates;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class YourAchievementsActivity extends AppCompatActivity {

    private ListView achievementListView;
    private AchievementManager achievementManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_achievements);

        achievementListView = findViewById(R.id.achievementListView);
        achievementManager = new AchievementManager(this);

        List<Achievement> achievements = achievementManager.loadAchievements();

        AchievementAdapter adapter = new AchievementAdapter(this, achievements);
        achievementListView.setAdapter(adapter);
    }
}