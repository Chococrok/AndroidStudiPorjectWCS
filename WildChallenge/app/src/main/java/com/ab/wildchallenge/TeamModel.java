package com.ab.wildchallenge;

/**
 * Created by apprenti on 19/07/17.
 */

public class TeamModel {
    private String name;
    private int score;
    private int meanScore;
    private int playerCount;

    public static final String DATABASE_TEAM_REF = "team";
    public static final String DATABASE_MEAN_SCORE_REF = "meanScore";

    private TeamModel(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getMeanScore() {
        return meanScore;
    }

    public void setMeanScore(int meanScore) {
        this.meanScore = meanScore;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }
}
