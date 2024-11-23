package com.example.pi_rates;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class TaskGeneratorActivity extends AppCompatActivity {

    private TextView option1, option2, option3;
    private TextView taskTextView, feedbackTextView, scoreTextView, levelTextView;
    private int correctAnswer, score = 0;
    private int taskCount = 0;
    private int correctAnswerCount = 0;
    private int level = 1;

    private CountDownTimer countDownTimer;

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

        generateNewTask();

        option1.setOnClickListener(v -> checkAnswer(option1));
        option2.setOnClickListener(v -> checkAnswer(option2));
        option3.setOnClickListener(v -> checkAnswer(option3));


        startCountDownTimer();
    }

    private void generateNewTask() {
        if (correctAnswerCount % 10 == 0 && correctAnswerCount > 0) {
            level++;
            correctAnswerCount=0;
            showLevelUpPopup();
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
        }
        if (score < 0) {
            score = 0;
        }
        scoreTextView.setText("Score: " + score);

        generateNewTask();
    }

    private void startCountDownTimer() {
        countDownTimer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                
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
