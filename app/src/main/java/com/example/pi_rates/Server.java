package com.example.pi_rates;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Server {
<<<<<<< HEAD
    public static String getURL() {return"https://6f99-109-245-32-48.ngrok-free.app"; }
=======
    public static String getURL() {return"https://2d08-80-245-99-81.ngrok-free.app"; }
>>>>>>> f85376d0548ba94dc213e29feb86780bef00faf6
    private Context context;
    public Server(Context context){
        this.context = context;
    }
    public void connection(String link, ConnectionChecked callback){
        if (!link.isEmpty()) {
            RequestQueue queue = Volley.newRequestQueue(context);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, link,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            callback.onSuccess(response); // Pass the response back
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Server", "Error fetching data: " + error.getMessage());
                            Toast.makeText(context, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                        }
                    });

            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        } else {
            Toast.makeText(context, "Please provide a valid link", Toast.LENGTH_SHORT).show();
        }
    }
    public void sendUserName(String link, String name, GotJson gotJson){
        if(!name.isEmpty()){
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            JSONObject jsonObject = new JSONObject();
            try{
                jsonObject.put("USER_NAME", name);
            }
            catch (JSONException exp){
                Log.d("JSONERROR","Error: " + exp);
            }
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, link, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            gotJson.onSuccess(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Server ads1", "Doesn't got data from the server");
                        }
                    }
            );
            requestQueue.add(request);
        }
        else {
            Toast.makeText(context, "Enter user name", Toast.LENGTH_SHORT).show();
        }
    }
    public void sendScore(String link, String userName,int score, Updated updated) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject json = new JSONObject();
        try {
            json.put("Score", score);
            json.put("USER_NAME", userName);
        } catch (JSONException exp) {
            Log.d("JSONERROR", "" + exp);
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, link, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        updated.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Server ads1", "Doesn't got data from the server");
                    }
                });
        requestQueue.add(request);
    }
    public void getHighScore(String link, String name, GotScores gotScores){
        if(!name.isEmpty()){
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            JSONObject jsonObject = new JSONObject();
            try{
                jsonObject.put("USER_NAME", name);
            }
            catch (JSONException exp){
                Log.d("JSONERROR","Error: " + exp);
            }
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, link, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            gotScores.onSuccess(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Server ads1", "Doesn't got data from the server");
                        }
                    }
            );
            requestQueue.add(request);
        }
        else {
            Toast.makeText(context, "Enter user name", Toast.LENGTH_SHORT).show();
        }
    }
    public interface ConnectionChecked{
        void onSuccess(String response);
    }
    public interface GotJson{
        void onSuccess(JSONObject jsonObject);
    }
    public interface Updated{
        void onSuccess(JSONObject jsonObject);
    }
    public interface GotScores{
        void onSuccess(JSONObject jsonObject);
    }
}
