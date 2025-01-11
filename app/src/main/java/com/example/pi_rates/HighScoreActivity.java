package com.example.pi_rates;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

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

public class HighScoreActivity extends AppCompatActivity {

    private User[] users;
    ListView list;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        Intent intent = getIntent();
        userName = intent.getStringExtra("USER_NAME");
        list =  findViewById(R.id.listview);
        String url = Server.getURL() + "/highscore";
        Log.d("asd1", "url");
        download(url);
    }
    private void download(String url) {
        Server server = new Server(this);
        server.getHighScore(url, userName, new Server.GotScores() {
            @Override
            public void onSuccess(JSONObject response){
                parseJson(response);
            }
        });
    }
    private void parseJson(JSONObject json){
        Log.d("asd1", "parseJson");
        int arraySize;
        try{
            JSONArray jsonArray = json.getJSONArray("Scores");
            arraySize =jsonArray.length();
            users = new User[arraySize];
            for(int i = 0; i < arraySize; i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.getString("USER_NAME");
                int score = jsonObject.getInt("Score");
                String date = jsonObject.getString("Date");
                users[i] = new User(name, score, date);
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
    }
    public void Home(View view){
        Intent intent = new Intent(HighScoreActivity.this, MainActivity.class);
        intent.putExtra("USER_NAME", userName);
        startActivity(intent);
        finish();
    }
}