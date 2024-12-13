package com.example.pi_rates;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class TaskGeneratorActivity extends AppCompatActivity {
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


        generateNewTask();

        option1.setOnClickListener(v -> checkAnswer(option1));
        option2.setOnClickListener(v -> checkAnswer(option2));
        option3.setOnClickListener(v -> checkAnswer(option3));


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
            startActivity(intent);
            finish();
        });
    }

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
        dialog.show();

        backToMainMenuButton.setOnClickListener(v -> {
            Intent intent = new Intent(TaskGeneratorActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        highScoreButton.setOnClickListener(v -> {
        });
    }

    private void showLevelUpPopup() {

        LevelUpPopupFragment popupFragment = new LevelUpPopupFragment();
        popupFragment.show(getSupportFragmentManager(), "LevelUpPopup");

    }

}
