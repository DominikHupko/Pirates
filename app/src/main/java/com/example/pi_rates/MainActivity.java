package com.example.pi_rates;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public String userName;
    private static MediaPlayer mediaPlayer;
    private boolean isMusicPlaying = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Load music state
        SharedPreferences prefs = getSharedPreferences("MusicPrefs", MODE_PRIVATE);
        isMusicPlaying = prefs.getBoolean("IS_MUSIC_PLAYING", true);

        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.background_music);
            mediaPlayer.setLooping(true);
            if (isMusicPlaying) {
                mediaPlayer.start();
            }
        }
        Intent intent = getIntent();
        userName = intent.getStringExtra("USER_NAME");


        Button quitButton = findViewById(R.id.button3);
        

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
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
    public void Testing(View view) {
        /*AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

        dialog.show();*/
        Intent intent = new Intent(MainActivity.this, StarterActivity.class);
        startActivity(intent);
        this.finish();
    }

    private void showLevelInputDialog() {
        /*
        // Create a Dialog
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        // Inflate the custom layout for the dialog
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_level_input, null);
        dialogBuilder.setView(dialogView);

        // Find the EditText and Buttons in the custom dialog layout
        final EditText levelInput = dialogView.findViewById(R.id.dialogLevelInput);
        Button customStartButton = dialogView.findViewById(R.id.customStartButton);
        // Set the dialog buttons
        customStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String levelText = levelInput.getText().toString().trim();

                if (!levelText.isEmpty()) {
                    int startingLevel = Integer.parseInt(levelText);

                    if (startingLevel == 0)
                    {
                        startingLevel += 1;
                    }

                    // Send the starting level to the GameActivity
                    Intent intent = new Intent(MainActivity.this, TaskGeneratorActivity.class);
                    intent.putExtra("START_LEVEL", startingLevel);
                    intent.putExtra("USER_NAME", userName);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Please enter a valid level!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //dialogBuilder.setNegativeButton("Cancel", null); // Dismiss on Cancel
        AlertDialog dialog = dialogBuilder.create();
        if(!isFinishing()) {
            dialog.show();
        }*/
    }

    private void write(String response){
        Toast.makeText(this, "Server: " + response, Toast.LENGTH_SHORT).show();
    }
    public void playMode (View view) {
        Intent intent = new Intent(MainActivity.this, PlayModeActivity.class);
        intent.putExtra("USER_NAME", userName);
        startActivity(intent);
    }
    public void scoreBoard(View view){
        Intent intent = new Intent(MainActivity.this, HighScoreActivity.class);
        intent.putExtra("USER_NAME", userName);
        startActivity(intent);
    }
    public void openSettings(View view) {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        intent.putExtra("USER_NAME", userName);
        startActivity(intent); 
    }
    public void openAchievements(View view) {
        Intent intent = new Intent(MainActivity.this, YourAchievementsActivity.class);
        intent.putExtra("USER_NAME", userName);
        startActivity(intent);
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (mediaPlayer != null && isMusicPlaying && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
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

    /*public void toggleSound(View view) {
        ImageView soundToggle = findViewById(R.id.soundToggle);

        if (mediaPlayer != null) {
            if (isMusicPlaying) {
                mediaPlayer.pause();
                soundToggle.setImageResource(R.drawable.sound_off);
            } else {
                mediaPlayer.start();
                soundToggle.setImageResource(R.drawable.sound_on);
            }
            isMusicPlaying = !isMusicPlaying;

            // Save the state to SharedPreferences
            SharedPreferences prefs = getSharedPreferences("MusicPrefs", MODE_PRIVATE);
            prefs.edit().putBoolean("IS_MUSIC_PLAYING", isMusicPlaying).apply();
        }
    }*/
    @Override
    protected void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }
    public void settings (View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_settings, null);
        builder.setView(dialogView);
        Log.d("sound settings", "builder got view");

        // create button image
        ImageView imageView = dialogView.findViewById(R.id.close_settings);
        ImageView soundToggle = dialogView.findViewById(R.id.soundToggle);
        TextView logOut = dialogView.findViewById(R.id.log_out_button);
        Log.d("sound settings", "objects have initailed");

        //show dialog
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Log.d("sound settings", "dialog has shown");

        imageView.setOnClickListener(v -> {
            dialog.dismiss();
        });
        soundToggle.setOnClickListener(v -> {
            if (mediaPlayer != null) {
                if (isMusicPlaying) {
                    mediaPlayer.pause();
                    soundToggle.setImageResource(R.drawable.sound_off);
                } else {
                    mediaPlayer.start();
                    soundToggle.setImageResource(R.drawable.sound_on);
                }
                isMusicPlaying = !isMusicPlaying;

                // Save the state to SharedPreferences
                SharedPreferences prefs = getSharedPreferences("MusicPrefs", MODE_PRIVATE);
                prefs.edit().putBoolean("IS_MUSIC_PLAYING", isMusicPlaying).apply();
            }
        });
        logOut.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,StarterActivity.class);
            SharedPreferences sharedPreferences = getSharedPreferences("UserLog", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("USER_NAME", "Guest");
            editor.apply();
            startActivity(intent);
            finish();
        });
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}
