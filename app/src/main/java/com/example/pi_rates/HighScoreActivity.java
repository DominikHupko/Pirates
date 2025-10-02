package com.example.pi_rates;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
    ImageView bronzeAvatar;
    TextView bronzeName;
    TextView bronzeScore;
    ImageView silverAvatar;
    TextView silverName;
    TextView silverScore;
    ImageView goldAvatar;
    TextView goldName;
    TextView goldScore;
    LinearLayout podium;
    ImageView podium_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        Intent intent = getIntent();
        userName = intent.getStringExtra("USER_NAME");
        list =  findViewById(R.id.listview);
        url = Server.getURL() + "/highscore";
        bronzeAvatar = findViewById(R.id.bronzeImageView);
        bronzeName = findViewById(R.id.bronzeName);
        bronzeScore = findViewById(R.id.bronzeScore);
        silverAvatar = findViewById(R.id.silverImageView);
        silverName = findViewById(R.id.silverName);
        silverScore = findViewById(R.id.silverScore);
        goldAvatar = findViewById(R.id.goldImageView);
        goldName = findViewById(R.id.goldName);
        goldScore = findViewById(R.id.goldScore);
        podium = findViewById(R.id.podium);
        podium_image = findViewById(R.id.podium_image);
        Log.d("asd1", "url");
        db = new ScoreDB(this);
        db.setUserName(userName);
        download(url);
    }
    private void download(String url) {
        try{
            Server server = new Server(this);
            Log.d("userName", "I don't know. The name is: " + userName);
            SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            String selectedAvatar = sharedPreferences.getString("SELECTED_AVATAR", "default_avatar");
            JSONObject jsonObject = db.localScorers(userName, selectedAvatar);
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
            Log.d("asd1", "Json Parsed");
            for(int i = 0; i < arraySize; i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.getString("USER_NAME");
                int score = jsonObject.getInt("Score");
                String date = jsonObject.getString("Date");
                String avatar =jsonObject.getString("AVATAR");
                if (avatar == null || avatar.isEmpty())
                {
                    avatar = "avatar";
                }
                if (i<3)
                {
                    switch (i)
                    {
                        case 0:
                            goldName.setText(name);
                            goldScore.setText(String.valueOf(score));
                            if (avatar.equals("avatar2")){
                                goldAvatar.setImageResource(R.drawable.avatar2_gold);
                            }
                            else {
                                goldAvatar.setImageResource(R.drawable.avatar1_gold);
                            }
                            break;
                        case 1:
                            silverName.setText(name);
                            silverScore.setText(String.valueOf(score));
                            if (avatar.equals("avatar2")){
                                silverAvatar.setImageResource(R.drawable.avatar2_silver);
                            }
                            else {
                                silverAvatar.setImageResource(R.drawable.avatar1_silver);
                            }
                            break;
                        case 2:
                            bronzeName.setText(name);
                            bronzeScore.setText(String.valueOf(score));
                            if (avatar.equals("avatar2")){
                                bronzeAvatar.setImageResource(R.drawable.avatar2_bronze);
                            }
                            else {
                                bronzeAvatar.setImageResource(R.drawable.avatar1_bronze);
                            }
                            break;
                    }
                }
                else
                {
                    Log.d("asd1", "Data added to users[" + i + "]");
                    users[i - 3] = new User( i, avatar, name, score, date);
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
        podium.setVisibility(View.GONE);
        podium_image.setVisibility(View.GONE);
        if(users != null)
            fillView();
        else {
            Toast.makeText(this, "You don't have score yet.", Toast.LENGTH_SHORT).show();
        }
    }
    public void GlobalScore(View view){
        podium.setVisibility(View.VISIBLE);
        podium_image.setVisibility(View.VISIBLE);
        download(url);
    }
}