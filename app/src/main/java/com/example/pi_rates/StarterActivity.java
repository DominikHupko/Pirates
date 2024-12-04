package com.example.pi_rates;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
                String link = Server.url + "/sendUserName";
                Server server = new Server(this);
                server.sendUserName(link, name,new Server.GotJson() {
                    @Override
                    public void onSuccess(JSONObject response){
                        dialog.dismiss();
                        parseJson(response);
                    }
                });
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
    private void parseJson(JSONObject json){
        try{
            String name = json.getString("username");
            Intent intent = new Intent(StarterActivity.this,MainActivity.class);
            intent.putExtra("USER_NAME",name);
            SharedPreferences sharedPreferences = getSharedPreferences("UserLog", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("USER_NAME", name);
            editor.apply();
            startActivity(intent);
            finish();
        }catch (JSONException exp){
            Log.d("Json error","Error: " + exp);
        }
    }
}
