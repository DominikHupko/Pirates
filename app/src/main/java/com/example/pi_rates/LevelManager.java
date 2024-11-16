package com.example.pi_rates;


public class LevelManager {

    private int correctAnswers = 0;
    private int currentLevel = 1;

    public void onAnswer(boolean isCorrect) {
        if (isCorrect) {
            correctAnswers++;
        }
        if (correctAnswers >= 5) {
            levelUp();
        }
    }

    private void levelUp() {
        currentLevel++;
        correctAnswers = 0;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }
}
