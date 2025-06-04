package com.example.pi_rates;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

public class AchievementManager {
    private static final String PREFS_NAME = "Achievements";
    private SharedPreferences sharedPreferences;

    public AchievementManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public boolean isAchievementUnlocked(String id) {
        return sharedPreferences.getBoolean(id, false);
    }

    public void unlockAchievement(String id) {
        sharedPreferences.edit().putBoolean(id, true).apply();
    }


    public List<Achievement> loadAchievements() {
        List<Achievement> achievements = new ArrayList<>();

        achievements.add(new Achievement("score_50", "Greenhorn Pirate", "You achieved 50 points!", false));
        achievements.add(new Achievement("score_100", "Pirate of Points", "You achieved 100 points!", false));
        achievements.add(new Achievement("score_200", "Point Captain", "You achieved 200 points!", false));
        achievements.add(new Achievement("level_5", "Experienced Player", "You achieved the 5th level!", false));
        achievements.add(new Achievement("correct_10", "10 Good Answers", "Give 10 Correct Answers!", false));
        achievements.add(new Achievement("correct_streak_5", "Perfect!", "5 Perfect Answers In A Row!", false));
        achievements.add(new Achievement("play_5_games", "You Are Persistent!", "Play 5 Games!", false));
        achievements.add(new Achievement("fast_answer", "Flash Answer!", "You answered under 3 seconds!", false));
        achievements.add(new Achievement("first_game", "First Steps", "This was your first game!", false));

        for (Achievement achievement : achievements) {
            boolean unlocked = isAchievementUnlocked(achievement.getId());
            achievement.setUnlocked(unlocked);
        }
        return achievements;
    }
}
