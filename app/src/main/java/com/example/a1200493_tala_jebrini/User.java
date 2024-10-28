package com.example.a1200493_tala_jebrini;

public class User {
    private String Username;
    private int UserScore;


    public User(String username, int score) {
        this.Username = username;
        this.UserScore = score;
    }

    public String getUsername() {
        return Username;
    }

    public int getUserScore() {
        return UserScore;
    }


}
