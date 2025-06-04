package com.example.pi_rates;

import static java.lang.System.out;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.JsonWriter;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ScoreDB  extends SQLiteOpenHelper {
    private static final int Version = 2;
    private static final String DataBaseName = "ScoresDataBase";
    private static final String TableName = "scores";
    private static final String KeyID = "id";
    private static final String NameCulomn = "name";
    private static final String ScoreColumn = "score";
    private static final String DateColumn = "date";
    private static final String UploadedColumn = "uploaded";
    private static String userName = "";
    public  void setUserName(String name){userName = name;}

    public ScoreDB (Context context){super(context, DataBaseName, null,Version);}
    @Override
    public void onCreate (SQLiteDatabase db){
        String CreateScoreTable = "CREATE TABLE scores (" +
                KeyID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                NameCulomn + " TEXT ," +
                ScoreColumn + " INTEGER , " +
                DateColumn + " TEXT , " +
                UploadedColumn + " TEXT );";

        db.execSQL(CreateScoreTable);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS scores");
        this.onCreate(db);
    }

    public void insertScore(int score){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ScoreColumn, score);
        SimpleDateFormat date = new SimpleDateFormat("dd.MM.yy", Locale.getDefault());
        cv.put(DateColumn, date.format(new Date()));
        cv.put(UploadedColumn, "FALSE");
        db.insert(TableName,
                null,
                cv);
        db.close();
        Log.d("ScoreDBLogD", "Saved score");
    }
    public User[] getScores(String method){
        Log.d("ScoreDBLogD", "Getting score started, method: " + method);
        int scores = scores();
        if(scores > 0){
            User[] users = new User[scores];
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "";
            if(method.equals("All")){
                query = "SELECT " + NameCulomn + ", " + ScoreColumn + ", " + DateColumn + " FROM " + TableName + " ORDER BY " + ScoreColumn + " ;";
            } else if (method.equals("only locals")) {
                query = "SELECT " + NameCulomn + ", " + ScoreColumn + ", " + DateColumn + " FROM " + TableName + " WHERE " + UploadedColumn + " = 'FALSE'";
            }
            Cursor cursor = db.rawQuery(query, null);
            User scoreData;
            int i = 0;
            Log.d("ScoreDBLogD","Start reading data");
            if(cursor.moveToFirst()){
                do{
                    String name = userName;
                    int score = cursor.getInt(1);
                    String date = cursor.getString(2);
                    Log.d("ScoreDBLogD", "Name: " + cursor.getString(0) + "; score: " + cursor.getString(1) + "; Date: " + cursor.getString(2));
                    scoreData = new User( name, score, date);
                    users[i] = scoreData;
                    i++;
                }while(cursor.moveToNext());
                cursor.close();
                db.close();
                return users;
            }
            else{
                Log.d("ScoreDBLogD", "Cursor was null");
                return null;
            }
        }
        else {
            return null;
        }
    }
    private int scores(){
        int games = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*)," + UploadedColumn + " FROM " + TableName + " ;";
        Log.d("ScoreDBLogD", "Created an query");
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            games = cursor.getInt(0);
            Log.d("ScoreDBLogD", "UploadColumn data: " + cursor.getString(1));
        }
        cursor.close();
        db.close();
        Log.d("ScoreDBLogD", "I got the numder of saves. The saves: " + games);
        return games;
    }
    public JSONObject localScorers(String userName){
        User[] users = getScores("only locals");
        JSONObject usersScore = new JSONObject();
        try{
            if(users != null) {
                JSONArray jsonArray = new JSONArray();
                for (User user : users) {
                    if (user == null) continue;
                    Log.d("asd1", "Building JSON");
                    JSONObject jsonObject = new JSONObject();
                    Log.d("asd1", " " + users.length);
                    jsonObject.put(NameCulomn, userName);
                    jsonObject.put(ScoreColumn, user.getScore());
                    Log.d("asd1", " " + user.getScore());
                    jsonObject.put(DateColumn, user.getDate());
                    Log.d("asd1", " " + user.getDate());

                    jsonArray.put(jsonObject);
                }
                usersScore.put("scores", jsonArray);
                usersScore.put("upload", "true");
            }
            else{
                usersScore.put("upload", "false");
            }
            usersScore.put("USER_NAME", userName);
        }catch (JSONException exception){
            Log.d("JSONError", " " + exception);
            try{
                usersScore.put("upload", "false");
            }catch (JSONException e){
                Log.d("JSONError", " " + e);
            }
        }
        return usersScore;
    }
    public void updatedScores(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TableName + " SET " + UploadedColumn + " = 'TRUE' WHERE " + UploadedColumn + " = 'FALSE'";
        try{
            db.execSQL(query);
            Log.d("ScoreDBLogD", "Uploaded");
        }catch (Exception e){
            Log.d("ScoreDBLogD", "Query problems in the update method");
        }finally {
            db.close();
        }
    }
}
