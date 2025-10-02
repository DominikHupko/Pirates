package com.example.pi_rates;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class CustomAdapter extends ArrayAdapter<User> {

    HighScoreActivity highScoreActivity;
    User[] users;
    public CustomAdapter(HighScoreActivity highScoreActivity, User[] users){
        super(highScoreActivity, R.layout.score, users);
        this.highScoreActivity = highScoreActivity;
        this.users = users;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup){
        LayoutInflater inflater = highScoreActivity.getLayoutInflater();
        Log.d("asd1", "adapter1");
        View rowView = inflater.inflate(R.layout.score,null );
        Log.d("asd1", "adapter2");
        TextView rank = (TextView) rowView.findViewById(R.id.rankedID);
        ImageView avatar = (ImageView) rowView.findViewById(R.id.avaterImageView);
        TextView name = (TextView) rowView.findViewById(R.id.name);
        TextView score = (TextView) rowView.findViewById(R.id.score);
        TextView date = (TextView) rowView.findViewById(R.id.date);
        Log.d("asd1", "adapter3");
        name.setText(users[i].getName());
        score.setText(String.valueOf(users[i].getScore()));
        date.setText(users[i].getDate());
        if (users[i].getrank() == 0)
        {
            rank.setVisibility(View.GONE);
            avatar.setVisibility(View.GONE);
            return rowView;
        }
        else
        {
            rank.setVisibility(View.VISIBLE);
            avatar.setVisibility(View.VISIBLE);
        }
        rank.setText(String.valueOf(users[i].getrank()));
        if (users[i].getAvatar().equals("avatar2")) {
            avatar.setImageResource(R.drawable.avatar2);
        }
        else {
            avatar.setImageResource(R.drawable.avatar1);
        }
        Log.d("asd1", "adapter4");
        return rowView;
    }
}
