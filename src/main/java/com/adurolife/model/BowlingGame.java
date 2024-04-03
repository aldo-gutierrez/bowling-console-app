package com.adurolife.model;

public interface BowlingGame {

    void resetGame();

    void addRoll(int pins);

    int getCurrentFrame();

    int getCurrentRoll();

    int getCurrentScore();

    String getSmallDashboard();

}
