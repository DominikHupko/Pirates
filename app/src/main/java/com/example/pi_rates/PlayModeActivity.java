package com.example.pi_rates;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PlayModeActivity extends AppCompatActivity {

    String userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_mode);

        Intent intent = getIntent();
        userName = intent.getStringExtra("USER_NAME");
    }
    public void easyMode (View view)
    {
        Intent intent = new Intent(PlayModeActivity.this, TaskGeneratorActivity.class);
        intent.putExtra("USER_NAME", userName);
        intent.putExtra("MODE", "easy");
        startActivity(intent);
        this.finish();
    }
    public void mediumMode (View view)
    {
        Intent intent = new Intent(PlayModeActivity.this, TaskGeneratorActivity.class);
        intent.putExtra("USER_NAME", userName);
        intent.putExtra("MODE", "medium");
        startActivity(intent);
        this.finish();
    }
    public void hardMode (View view)
    {
        Intent intent = new Intent(PlayModeActivity.this, TaskGeneratorActivity.class);
        intent.putExtra("USER_NAME", userName);
        intent.putExtra("MODE", "hard");
        startActivity(intent);
        this.finish();
    }
    public void back (View view)
    {
        this.finish();
    }
}