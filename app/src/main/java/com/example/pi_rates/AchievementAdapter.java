package com.example.pi_rates;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.core.graphics.drawable.DrawableCompat;

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
                    .inflate(R.layout.achievement_adapter, parent, false);
        }

        TextView title = convertView.findViewById(R.id.title);
        TextView description = convertView.findViewById(R.id.text);

        title.setText(achievement.getTitle());
        description.setText(achievement.getDescription());


        if (!achievement.isUnlocked()) {
            title.setTextColor(Color.GRAY);
            description.setTextColor(Color.GRAY);
            convertView.setAlpha(0.7f);
        } else {
            title.setTextColor(Color.BLACK);
            description.setTextColor(Color.DKGRAY);
            convertView.setAlpha(1f);
        }

        return convertView;
    }
}