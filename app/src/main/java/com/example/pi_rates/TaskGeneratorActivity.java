package com.example.pi_rates;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class TaskGeneratorActivity extends AppCompatActivity {
    private String userName;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 31000;
    private TextView option1, option2, option3;
    private TextView taskTextView, feedbackTextView, scoreTextView, levelTextView;
    private int correctAnswer, score = 0;
    private int taskCount = 0;
    private int correctAnswerCount = 0;
    private int level = 1;
    private TextView timerTextView;

    private int lives = 3;
    private ImageView life1, life2, life3;
    private AchievementManager achievementManager;
    private ScoreDB db;
    int correctStreak = 0;
    boolean madeMistake = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_generator);

        taskTextView = findViewById(R.id.taskTextView);
        option1 = findViewById(R.id.answerOption1);
        option2 = findViewById(R.id.answerOption2);
        option3 = findViewById(R.id.answerOption3);
        feedbackTextView = findViewById(R.id.feedbackTextView);
        scoreTextView = findViewById(R.id.scoreTextView);
        levelTextView = findViewById(R.id.levelTextView);
        timerTextView = findViewById(R.id.timerTextView);
        life1 = findViewById(R.id.life1);
        life2 = findViewById(R.id.life2);
        life3 = findViewById(R.id.life3);

        Intent intent = getIntent();
        userName =  intent.getStringExtra("USER_NAME");
        db = new ScoreDB(this);

        generateNewTask();

        option1.setOnClickListener(v -> checkAnswer(option1));
        option2.setOnClickListener(v -> checkAnswer(option2));
        option3.setOnClickListener(v -> checkAnswer(option3));
        level = intent.getIntExtra("START_LEVEL", 1);

        achievementManager = new AchievementManager(this);

        startCountDownTimer();
    }


    private void reduceLife() {
        if (lives == 3) {
            life3.setVisibility(View.INVISIBLE);
        } else if (lives == 2) {
            life2.setVisibility(View.INVISIBLE);
        } else if (lives == 1) {
            life1.setVisibility(View.INVISIBLE);
            showGameOverDialog();
        } else {
            showGameOverDialog();
        }

        lives--;
    }

    private void showAchievementDialog(String title, String description) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_achievement_unlocked, null);
        TextView titleView = dialogView.findViewById(R.id.achievementTitle);
        TextView descriptionView = dialogView.findViewById(R.id.achievementDescription);

        titleView.setText(title);
        descriptionView.setText(description);

        new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(true)
                .show();
    }
    private void generateNewTask() {
        if (correctAnswerCount % 10 == 0 && correctAnswerCount > 0) {
            level++;
            correctAnswerCount=0;
            showLevelUpPopup();
            addTimeToTimer(10000);
        }

        levelTextView.setText("Level: " + level);

        Task newTask = TaskGenerator.generateTask(level);

        taskTextView.setText(newTask.getQuestion());

        correctAnswer = newTask.getCorrectAnswer();
        ArrayList<Integer> answers = new ArrayList<>();
        answers.add(correctAnswer);

        Random random = new Random();
        int wrongAnswer1, wrongAnswer2;

        do {
            wrongAnswer1 = correctAnswer + (random.nextInt(16) - 8);
        } while (wrongAnswer1 ==0 || wrongAnswer1 == correctAnswer || wrongAnswer1>100 || wrongAnswer1<0);

        do {
            wrongAnswer2 = correctAnswer + (random.nextInt(16) - 8);
        } while (wrongAnswer2 ==0 || wrongAnswer2 == correctAnswer || wrongAnswer2 == wrongAnswer1 || wrongAnswer2>100 || wrongAnswer2<0);

        answers.add(wrongAnswer1);
        answers.add(wrongAnswer2);

        Collections.shuffle(answers);

        option1.setText(String.valueOf(answers.get(0)));
        option2.setText(String.valueOf(answers.get(1)));
        option3.setText(String.valueOf(answers.get(2)));

        option1.setBackgroundResource(R.drawable.option_background);
        option2.setBackgroundResource(R.drawable.option_background);
        option3.setBackgroundResource(R.drawable.option_background);

        taskCount++;
    }

    private void checkAnswer(TextView selectedOption) {
        int selectedAnswer = Integer.parseInt(selectedOption.getText().toString());

        if (selectedAnswer == correctAnswer) {
            feedbackTextView.setText("Correct!");
            feedbackTextView.setTextColor(getColor(R.color.green));
            selectedOption.setBackgroundResource(R.drawable.correct_answer_background);
            score += 10;
            correctAnswerCount++;
            if (score >= 50 && !achievementManager.isAchievementUnlocked("score_50")) {
                achievementManager.unlockAchievement("score_50");
                showAchievementDialog("Greenhorn Pirate", "You achieved 50 points!");
            }
            if (score >= 100 && !achievementManager.isAchievementUnlocked("score_100")) {
                achievementManager.unlockAchievement("score_100");
                showAchievementDialog("Pirate of Points", "You achieved 100 points!");
            }
            if (score >= 200 && !achievementManager.isAchievementUnlocked("score_200")) {
                achievementManager.unlockAchievement("score_200");
                showAchievementDialog("Point Captain", "You achieved 200 points!");
            }
            if (level >= 5 && !achievementManager.isAchievementUnlocked("level_5")) {
                achievementManager.unlockAchievement("level_5");
                showAchievementDialog("Experienced Player", "You achieved the 5th level!");
            }
            if (correctAnswerCount >= 10 && !achievementManager.isAchievementUnlocked("correct_10")) {
                achievementManager.unlockAchievement("correct_10");
                showAchievementDialog("10 Good Answers", "Give 10 Correct Answers!");
            }
            if (selectedAnswer == correctAnswer) {
                correctStreak++;
                if (correctStreak >= 5 && !achievementManager.isAchievementUnlocked("correct_streak_5")) {
                    achievementManager.unlockAchievement("correct_streak_5");
                    showAchievementDialog("Perfect!", "5 Perfect Answers In A Row!");
                }
                SharedPreferences prefs = getSharedPreferences("game_stats", MODE_PRIVATE);
                int gamesPlayed = prefs.getInt("games_played", 0) + 1;
                prefs.edit().putInt("games_played", gamesPlayed).apply();

                if (gamesPlayed >= 5 && !achievementManager.isAchievementUnlocked("play_5_games")) {
                    achievementManager.unlockAchievement("play_5_games");
                    showAchievementDialog("You Are Persistent!", "Play 5 Games!");
                }
                long answerTime = 31000 - timeLeftInMillis;
                if (answerTime <= 3000 && !achievementManager.isAchievementUnlocked("fast_answer")) {
                    achievementManager.unlockAchievement("fast_answer");
                    showAchievementDialog("Flash Answer!", "You answered under 3 seconds!");
                }
                if (selectedAnswer != correctAnswer) {
                    madeMistake = true;
                }
                if (gamesPlayed == 1 && !achievementManager.isAchievementUnlocked("first_game")) {
                    achievementManager.unlockAchievement("first_game");
                    showAchievementDialog("First Steps", "This was your fist game!");
                }
            } else {
                correctStreak = 0;
            }
        } else {
            feedbackTextView.setText("Incorrect, try again!");
            feedbackTextView.setTextColor(getColor(R.color.red));
            selectedOption.setBackgroundResource(R.drawable.incorrect_answer_background);
            reduceLife();
        }

        if (score < 0) {
            score = 0;
        }
        scoreTextView.setText("Score: " + score);

        generateNewTask();
    }

    private void showGameOverDialog() {

        if (!userName.equals("Geust"))
        {
            db.insertScore(score);
        }
        if (!madeMistake && !achievementManager.isAchievementUnlocked("no_mistakes")) {
            achievementManager.unlockAchievement("no_mistakes");
            showAchievementDialog("Perfect Game", "You had no errors!");
        }
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_game_over, null);

        TextView gameOverMessage = dialogView.findViewById(R.id.gameOverMessage);
        TextView finalScoreMessage = dialogView.findViewById(R.id.finalScoreMessage);
        Button backToMainMenuButton = dialogView.findViewById(R.id.backToMainMenuButton);

        finalScoreMessage.setText("Your final score: " + score);

        AlertDialog.Builder builder = new AlertDialog.Builder(TaskGeneratorActivity.this);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();

        backToMainMenuButton.setOnClickListener(v -> {
            Intent intent = new Intent(TaskGeneratorActivity.this, MainActivity.class);
            intent.putExtra("USER_NAME", userName);
            startActivity(intent);
            finish();
        });
    }

    @SuppressLint("SuspiciousIndentation")
    private void addTimeToTimer(long additionalTimeInMillis) {
        // Cancel the current timer
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        // Add time and restart the timer
        if(timeLeftInMillis + additionalTimeInMillis>31000)
            timeLeftInMillis = 31000;
        else
        timeLeftInMillis += additionalTimeInMillis;
        startCountDownTimer();
    }
    private void startCountDownTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished; // Update the remaining time
                int secondsRemaining = (int) (millisUntilFinished / 1000);
                timerTextView.setText("Time: " + secondsRemaining);

                if (secondsRemaining <= 10) {
                    timerTextView.setTextColor(getColor(R.color.red));
                } else {
                    timerTextView.setTextColor(getColor(R.color.black));
                }
            }

            @Override
            public void onFinish() {
                showTimeUpDialog();
            }
        };
        countDownTimer.start();
    }


    private void showTimeUpDialog() {
        countDownTimer.cancel();


        View dialogView = getLayoutInflater().inflate(R.layout.dialog_time_up, null);

        TextView timeUpMessage = dialogView.findViewById(R.id.timeUpMessage);
        TextView finalScoreMessage = dialogView.findViewById(R.id.finalScoreMessage);
        Button backToMainMenuButton = dialogView.findViewById(R.id.backToMainMenuButton);
        Button highScoreButton = dialogView.findViewById(R.id.highScoreButton);

        finalScoreMessage.setText("Your final score: " + score);

        AlertDialog.Builder builder = new AlertDialog.Builder(TaskGeneratorActivity.this);
        builder.setView(dialogView);


        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        if(!isFinishing()) {
            dialog.show();
        }

        backToMainMenuButton.setOnClickListener(v -> {
            Intent intent = new Intent(TaskGeneratorActivity.this, MainActivity.class);
            intent.putExtra("USER_NAME", userName);
            startActivity(intent);
            finish();
        });

        highScoreButton.setOnClickListener(v -> {
            Intent intent = new Intent(TaskGeneratorActivity.this, HighScoreActivity.class);
            intent.putExtra("USER_NAME", userName);
            startActivity(intent);
            finish();
        });
        if (!userName.equals("Geust"))
        {
            db.insertScore(score);
        }
    }

    private void showLevelUpPopup() {

        LevelUpPopupFragment popupFragment = new LevelUpPopupFragment();
        popupFragment.show(getSupportFragmentManager(), "LevelUpPopup");

    }

}
