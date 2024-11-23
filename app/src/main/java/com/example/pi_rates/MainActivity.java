package com.example.pi_rates;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Intent intent = getIntent();
        String userName = intent.getStringExtra("USER_NAME");


        Button startButton = findViewById(R.id.button);
        Button quitButton = findViewById(R.id.button3);


        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TaskGeneratorActivity.class);
                startActivity(intent);
            }
        });


        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });


        showWelcomeDialog(userName);
    }


    private void showWelcomeDialog(String userName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);


        View dialogView = getLayoutInflater().inflate(R.layout.dialog_welcome, null);
        builder.setView(dialogView);


        TextView welcomeMessage = dialogView.findViewById(R.id.welcome_message);
        welcomeMessage.setText("Welcome, " + userName + "!");


        Button continueButton = dialogView.findViewById(R.id.continue_button);
        AlertDialog dialog = builder.create();

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
