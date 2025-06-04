package com.example.pi_rates;

public class Achievement {
    private String id;
    private String title;
    private String description;
    private boolean unlocked;

    public Achievement(String id, String title, String description, boolean unlocked) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.unlocked = unlocked;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public boolean isUnlocked() { return unlocked; }

    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
    }
}