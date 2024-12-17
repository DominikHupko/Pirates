package com.example.pi_rates;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {

    public String userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Intent intent = getIntent();
        userName = intent.getStringExtra("USER_NAME");


        Button startButton = findViewById(R.id.button);
        Button quitButton = findViewById(R.id.button3);


        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TaskGeneratorActivity.class);
                intent.putExtra("USER_NAME", userName);
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
    public void Testing(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_server_connection, null);
        builder.setView(dialogView);

        EditText link_text = dialogView.findViewById(R.id.connection_link);
        Button connection_button = dialogView.findViewById(R.id.connection_button);
        AlertDialog dialog = builder.create();

        connection_button.setOnClickListener(v -> {
            String link = Server.getURL() + "/connection";
            if (!link.isEmpty()) {
                Server server = new Server(this);
                server.connection(link, new Server.ConnectionChecked() {
                    @Override
                    public void onSuccess(String response) {
                        dialog.dismiss();
                        write(response); // Handle response in your activity
                    }
                });
            } else {
                Toast.makeText(this, "Please enter a valid link", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void write(String response){
        Toast.makeText(this, "Server: " + response, Toast.LENGTH_SHORT).show();
    }
    public void scoreBoard(View view){
        Intent intent = new Intent(MainActivity.this, HighScoreActivity.class);
        intent.putExtra("USER_NAME", userName);
        startActivity(intent);
        finish();
    }
    public void openSettings(View view) {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        intent.putExtra("USER_NAME", userName);
        startActivity(intent);
    }
    @Override
    protected void onResume() {
        super.onResume();
        updateAvatar();
    }

    private void updateAvatar() {

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String selectedAvatar = sharedPreferences.getString("SELECTED_AVATAR", "default_avatar");


        ImageView avatarImageView = findViewById(R.id.imageViewAvatar);

        if (selectedAvatar.equals("avatar1")) {
            avatarImageView.setImageResource(R.drawable.avatar1);
        } else if (selectedAvatar.equals("avatar2")) {
            avatarImageView.setImageResource(R.drawable.avatar2);
        } else {
            avatarImageView.setImageResource(R.drawable.avatar);
        }
    }
}
