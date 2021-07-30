package com.chiayinfan.game.Scenes;

public class LeaderBoard implements Comparable<LeaderBoard>{
    private String name;
    private int score;

    public LeaderBoard(String name, int score) {
        this.name = name;
        this.score = score;
    }

    @Override
    public String toString() {
        return this.name + " " + this.score;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    @Override
    public int compareTo(LeaderBoard o) {
        return o.score-this.score;
    }
}
