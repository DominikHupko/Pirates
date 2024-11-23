package com.example.pi_rates;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;

public class StarterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starter);

        Button loginAsPlayer = findViewById(R.id.login_as_player);
        Button loginAsGuest = findViewById(R.id.login_as_guest);

        loginAsPlayer.setOnClickListener(v -> showNameDialog());
        loginAsGuest.setOnClickListener(v -> loginAsGuest());
    }

    private void showNameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_enter_name, null);
        builder.setView(dialogView);

        EditText nameInput = dialogView.findViewById(R.id.name_input);
        Button positiveButton = dialogView.findViewById(R.id.positive_button);
        Button negativeButton = dialogView.findViewById(R.id.negative_button);

        AlertDialog dialog = builder.create();


        positiveButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();
            if (!name.isEmpty()) {
                String playerName = name + "#" + generateRandomNumber();
                Intent intent = new Intent(StarterActivity.this, MainActivity.class);
                intent.putExtra("USER_NAME", playerName);
                startActivity(intent);
                finish();
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });


        negativeButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void loginAsGuest() {
        Intent intent = new Intent(StarterActivity.this, MainActivity.class);
        intent.putExtra("USER_NAME", "Guest");
        startActivity(intent);
        finish();
    }

    private String generateRandomNumber() {
        Random random = new Random();
        int number = random.nextInt(9000) + 1000;
        return String.valueOf(number);
    }
}
