package com.example.pi_rates;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HighScoreActivity extends AppCompatActivity {

    private User[] users;
    ListView list;
    String userName;
    String url;
    ScoreDB db;
    TextView bronzeName;
    TextView bronzeScore;
    TextView silverName;
    TextView silverScore;
    TextView goldName;
    TextView goldScore;
    LinearLayout podium;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        Intent intent = getIntent();
        userName = intent.getStringExtra("USER_NAME");
        list =  findViewById(R.id.listview);
        url = Server.getURL() + "/highscore";
        bronzeName = findViewById(R.id.bronzeName);
        bronzeScore = findViewById(R.id.bronzeScore);
        silverName = findViewById(R.id.silverName);
        silverScore = findViewById(R.id.silverScore);
        goldName = findViewById(R.id.goldName);
        goldScore = findViewById(R.id.goldScore);
        podium = findViewById(R.id.podium);
        Log.d("asd1", "url");
        db = new ScoreDB(this);
        db.setUserName(userName);
        download(url);
    }
    private void download(String url) {
        try{
            Server server = new Server(this);
            Log.d("userName", "I don't know. The name is: " + userName);
            JSONObject jsonObject = db.localScorers(userName);
            Log.d("asd1Json",jsonObject.toString());
            server.getHighScore(url, jsonObject, new Server.GotScores() {
                @Override
                public void onSuccess(JSONObject response){
                    parseJson(response);
                }
            });
        }
        catch (Exception exception){
            Log.d("asd1", "Problem apeard during sernding data. ", exception);
        }
    }
    private void parseJson(JSONObject json){
        Log.d("asd1", "parseJson");
        int arraySize;
        try{
            JSONArray jsonArray = json.getJSONArray("Scores");
            arraySize =jsonArray.length();
            users = new User[arraySize-3];
            for(int i = 0; i < arraySize; i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.getString("USER_NAME");
                int score = jsonObject.getInt("Score");
                String date = jsonObject.getString("Date");
                if (i<3)
                {
                    switch (i)
                    {
                        case 0:
                            goldName.setText(name);
                            goldScore.setText(String.valueOf(score));
                            break;
                        case 1:
                            silverName.setText(name);
                            silverScore.setText(String.valueOf(score));
                            break;
                        case 2:
                            bronzeName.setText(name);
                            bronzeScore.setText(String.valueOf(score));
                            break;
                    }
                }
                else
                {
                    users[i - 3] = new User(name, score, date);
                }
            }
            Log.d("asd1", "fillView");
            fillView();
        }
        catch (JSONException exp)
        {
            Log.d("JSON error", "" + exp);
        }
    }
    private void fillView(){
        CustomAdapter adapter = new CustomAdapter( this, users);
        Log.d("asd1", "setAdapter");
        list.setAdapter(adapter);
        db.updatedScores();
    }
    public void Home(View view){
        Intent intent = new Intent(HighScoreActivity.this, MainActivity.class);
        intent.putExtra("USER_NAME", userName);
        startActivity(intent);
        finish();
    }
    public void LocalScore(View view){
        Log.d("ScoreDBLogD", "Db start");
        users = db.getScores("All");
        podium.setVisibility(View.INVISIBLE);
        if(users != null)
            fillView();
        else {
            Toast.makeText(this, "You don't have score yet.", Toast.LENGTH_SHORT).show();
        }
    }
    public void GlobalScore(View view){
        podium.setVisibility(View.VISIBLE);
        download(url);
    }
}