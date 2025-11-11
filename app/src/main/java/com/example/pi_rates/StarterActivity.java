package com.example.pi_rates;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class StarterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starter);

        Button loginAsPlayer = findViewById(R.id.login_as_player);
        Button registerUser = findViewById(R.id.registration);
        Button loginAsGuest = findViewById(R.id.login_as_guest);

        loginAsPlayer.setOnClickListener(v -> showNameDialog("login"));
        registerUser.setOnClickListener(V -> showNameDialog("registration"));
        loginAsGuest.setOnClickListener(v -> loginAsGuest());
    }

    private void showNameDialog(String event ) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_enter_name, null);
        builder.setView(dialogView);

        EditText nameInput = dialogView.findViewById(R.id.name_input);
        EditText passwordInput = dialogView.findViewById(R.id.password_input);
        Button positiveButton = dialogView.findViewById(R.id.positive_button);
        Button negativeButton = dialogView.findViewById(R.id.negative_button);

        dialogView.setScaleX(0.8f);
        dialogView.setScaleY(0.8f);
        dialogView.setAlpha(0f);

        dialogView.animate()
                .scaleX(1f)
                .scaleY(1f)
                .alpha(1f)
                .setDuration(600)
                .setInterpolator(new OvershootInterpolator())
                .start();

        if (event.equals("login")) {
            positiveButton.setText("Login");
        }
        else {
            positiveButton.setText("Registration");
        }
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        positiveButton.setOnClickListener(v -> {
            v.setAlpha(0.7f);
            positiveButton.setEnabled(false);
            negativeButton.setEnabled(false);
            String name = nameInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            if (!name.isEmpty() && !password.isEmpty()) {
                String link = Server.getURL() + "/"+ event;
                Server server = new Server(this);
                server.sendUserNamePassword(link, name, password,new Server.GotJson() {
                    @Override
                    public void onSuccess(JSONObject response){
                        String userName = parseJson(response);
                        if (userName.equals("error"))
                        {
                            Toast.makeText(StarterActivity.this, "Wrong name or password", Toast.LENGTH_SHORT).show();
                            positiveButton.setEnabled(true);
                            negativeButton.setEnabled(true);
                        }
                        else
                        {
                            dialog.dismiss();
                            goToMainMenu(userName);
                        }

                    }
                });
            } else {
                Toast.makeText(this, "Input cannot be empty", Toast.LENGTH_SHORT).show();
                positiveButton.setEnabled(true);
                negativeButton.setEnabled(true);
                v.setAlpha(1f);
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
    private String parseJson(JSONObject json){
        try{
            Log.d("Json receive", String.valueOf(json));
            String name = json.getString("username");
            return name;
        }catch (JSONException exp){
            Log.d("Json error","Error: " + exp);
            return "error";
        }
    }
    private void goToMainMenu (String userName)
    {
        Intent intent = new Intent(StarterActivity.this,MainActivity.class);
        intent.putExtra("USER_NAME",userName);
        SharedPreferences sharedPreferences = getSharedPreferences("UserLog", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("USER_NAME", userName);
        editor.apply();
        startActivity(intent);
        finish();
    }
}
