package com.example.pi_rates;

public class User {
    private String name;
    private int score;
    private String date;
    private int rank;
    private String avatar;
    public User(String name, int score, String date){
        this.name = name;
        this.score = score;
        this.date = date;
    }
    public User(int rank, String avatar, String name, int score, String date) {
        this.rank = rank;
        this.avatar = avatar;
        this.name = name;
        this.score = score;
        this.date = date;
    }
    public String getName(){return name;}
    public void setName(String name){this.name = name;}
    public int getScore(){return score;}
    public void setScore(int score){this.score = score;}
    public String getDate(){return date;}
    public void setDate(String date){this.date = date;}
    public int getrank(){return rank;}
    public void setRank(int rank){this.rank = rank;}
    public String getAvatar(){return avatar;}
    public void setAvatar(String avatar){this.avatar = avatar;}
}
