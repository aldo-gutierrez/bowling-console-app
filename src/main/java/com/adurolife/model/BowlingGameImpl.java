package com.adurolife.model;

public class BowlingGameImpl implements BowlingGame {


    private static final int MAX_FRAMES = 10;
    private static final int MAX_DELIVERIES_LAST_FRAME = 3;
    public static final int MAX_PINS = 10;

    private int currentFrame = 0;

    /**
     * delivery, roll, shot
     */
    private int currentDelivery = 0;

    private int[][] deliveries;

    private boolean finished;

    public BowlingGameImpl() {
        resetGame();
    }

    @Override
    public void resetGame() {
        currentFrame = 0;
        currentDelivery = 0;
        deliveries = new int[MAX_DELIVERIES_LAST_FRAME][];
        for (int i = 0; i < MAX_DELIVERIES_LAST_FRAME; i++) {
            deliveries[i] = new int[MAX_FRAMES];
        }
        finished = false;
    }

    @Override
    public void addRoll(int pins) {
        if (finished) {
            throw new IllegalStateException("Game is finished, no more rolls are allowed");
        }
        if (pins < 0 || pins > MAX_PINS) {
            throw new IllegalArgumentException("a roll should be a positive number less or equal to " + MAX_PINS);
        }
        if (currentFrame == MAX_FRAMES - 1) { //last frame could allow three rolls
            addRollLastFrame(pins);
        } else {
            addRollNotLastFrame(pins);
        }
    }

    private void addRollNotLastFrame(int pins) {
        if (currentDelivery == 0) {
            deliveries[currentDelivery][currentFrame] = pins;
            if (pins == MAX_PINS) {
                currentFrame++;
            } else {
                currentDelivery++;
            }
        } else {
            int sumFrame = getTotalFrame(currentFrame);
            if (pins + sumFrame > MAX_PINS) {
                throw new IllegalArgumentException("A frame should not exceed " + MAX_PINS);
            }
            deliveries[currentDelivery][currentFrame] = pins;
            currentDelivery = 0;
            currentFrame++;
        }
    }

    private void addRollLastFrame(int pins) {
        switch (currentDelivery) {
            case 0:
                deliveries[currentDelivery][currentFrame] = pins;
                currentDelivery++;
                break;
            case 1:
                if (deliveries[0][currentFrame] < MAX_PINS) {
                    int sumFrame = getTotalFrame(currentFrame);
                    if (pins + sumFrame > MAX_PINS) {
                        throw new IllegalArgumentException("this roll should not exceed " + (MAX_PINS - sumFrame));
                    }
                    deliveries[currentDelivery][currentFrame] = pins;
                    sumFrame = getTotalFrame(currentFrame);
                    if (sumFrame == MAX_PINS) {
                        currentDelivery++;
                    } else {
                        completeGame();
                    }
                } else { //'X on first roll'
                    deliveries[currentDelivery][currentFrame] = pins;
                    currentDelivery++;
                }
                break;
            case 2:
                deliveries[currentDelivery][currentFrame] = pins;
                completeGame();
                break;
        }
    }

    private void completeGame() {
        finished = true;
        currentDelivery = 0;
        currentFrame = MAX_FRAMES;
    }

    private int getTotalFrame(int frame) {
        int total = 0;
        for (int i = 0; i < MAX_DELIVERIES_LAST_FRAME; i++) {
            total += deliveries[i][frame];
        }
        return Math.min(total, MAX_PINS);
    }

    @Override
    public int getCurrentFrame() {
        return currentFrame;
    }

    @Override
    public int getCurrentRoll() {
        return currentDelivery;
    }

    @Override
    public int getCurrentScore() {
        int[] frameScores = new int[MAX_FRAMES];
        int[] bonusPoints = new int[MAX_FRAMES];
        for (int i = 0; i < MAX_FRAMES; i++) {
            frameScores[i] = getTotalFrame(i);
            bonusPoints[i] += getBonusPoints(i);
        }
        int score = 0;
        for (int i = 0; i < MAX_FRAMES; i++) {
            score += frameScores[i] + bonusPoints[i];
        }
        return score;
    }


    public String getSmallDashboard() {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < MAX_FRAMES; i++) {
            res.append(" | ");
            char b = getBonus(i);
            if (b == 'X') {
                res.append(b);
            } else if (b == '/') {
                res.append(toString(deliveries[0][i]) + "" + '/');
            } else {
                if (i <= currentFrame) {
                    res.append(toString(deliveries[0][i]));
                    if (currentDelivery > 0) {
                        res.append(toString(deliveries[1][i]));
                    }
                } else {
                    res.append("  ");
                }
            }
        }
        if (currentFrame >= MAX_FRAMES - 1) {
            if (getBonus(MAX_FRAMES - 1) == 'X') {
                res.append(toString(deliveries[1][MAX_FRAMES - 1]) + " " + toString(deliveries[2][MAX_FRAMES - 1]));
            } else if (getBonus(MAX_FRAMES - 1) == '/') {
                res.append(toString(deliveries[2][MAX_FRAMES - 1]));
            }
        }
        res.append(" | ");
        return res.toString();
    }

    private char toString(int i) {
        if (i == 0) {
            return '-';
        }
        if (i == 10) {
            return 'X';
        }
        return (char) ((int) '0' + i);
    }

    private int getBonusPoints(int frame) {
        char bonus = getBonus(frame);
        int bonusPoints = 0;
        if (bonus == '/' || bonus == 'X') {
            if (frame == MAX_FRAMES - 1) {
                bonusPoints += deliveries[2][frame];
                if (bonus == 'X') {
                    bonusPoints += deliveries[1][frame];
                }
            } else {
                bonusPoints += deliveries[0][frame + 1];
                if (bonus == 'X') {
                    if (deliveries[1][frame + 1] > 0) {
                        bonusPoints += deliveries[1][frame + 1];
                    } else {
                        if (frame + 2 < MAX_FRAMES) {
                            bonusPoints += deliveries[0][frame + 2];
                        }
                    }
                }
            }
        }
        return bonusPoints;
    }

    private char getBonus(int frame) {
        if (deliveries[0][frame] == MAX_PINS) {
            return 'X';
        }
        if (deliveries[0][frame] + deliveries[1][frame] == MAX_PINS) {
            return '/';
        }
        return '-';
    }
}
