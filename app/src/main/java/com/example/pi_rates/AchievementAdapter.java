package com.example.pi_rates;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.pi_rates.Achievement;

import java.util.List;

public class AchievementAdapter extends ArrayAdapter<Achievement> {

    public AchievementAdapter(Context context, List<Achievement> achievements) {
        super(context, 0, achievements);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Achievement achievement = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(android.R.layout.simple_list_item_2, parent, false);
        }

        TextView title = convertView.findViewById(android.R.id.text1);
        TextView description = convertView.findViewById(android.R.id.text2);

        title.setText(achievement.getTitle());
        description.setText(achievement.getDescription());


        if (!achievement.isUnlocked()) {
            title.setTextColor(Color.GRAY);
            description.setTextColor(Color.GRAY);
        } else {
            title.setTextColor(Color.BLACK);
            description.setTextColor(Color.DKGRAY);
        }

        return convertView;
    }
}