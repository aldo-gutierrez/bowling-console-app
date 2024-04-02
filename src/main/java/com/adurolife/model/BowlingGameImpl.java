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
        verifyState();
        if (pins < 0 || pins > MAX_PINS) {
            throw new IllegalArgumentException("a roll should be a positive number less or equal to " + MAX_PINS);
        }
        if (currentFrame == MAX_FRAMES - 1) { //last frame could allow three rolls
            if (currentDelivery == 0) {
                deliveries[currentDelivery][currentFrame] = pins;
                currentDelivery++;
            } else if (currentDelivery == 1) {
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
                    if (pins == MAX_PINS) {
                        currentDelivery++;
                    } else {
                        completeGame();
                    }
                }
            } else if (currentDelivery == 2) {
                deliveries[currentDelivery][currentFrame] = pins;
                completeGame();
            }
        } else {
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
                    throw new IllegalArgumentException("a frame should not exceed " + MAX_PINS);
                }
                deliveries[currentDelivery][currentFrame] = pins;
                currentDelivery = 0;
                currentFrame++;
            }
        }
    }

    private void completeGame() {
        finished = true;
        currentDelivery = 0;
        currentFrame = MAX_FRAMES;
    }

    private int getTotalFrame(int currentFrame) {
        int total = 0;
        for (int i = 0; i < MAX_DELIVERIES_LAST_FRAME; i++) {
            total += deliveries[i][currentFrame];
        }
        return Math.min(total, MAX_PINS);
    }

    private void verifyState() {
        if (finished) {
            throw new IllegalStateException("Game is finished, no more rolls are allowed");
        }
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
        return calculateScore();
    }

    private int calculateScore() {
        int[] frameScores = new int[MAX_FRAMES];
        int[] bonusPoints = new int[MAX_FRAMES];
        for (int i = 0; i < MAX_FRAMES; i++) {
            frameScores[i] = getTotalFrame(i);
        }
        for (int i = 0; i < MAX_FRAMES; i++) {
            char bonus = getBonus(i);
            if (bonus == '/' || bonus == 'X') {
                if (i + 1 < MAX_FRAMES) {
                    bonusPoints[i] += deliveries[0][i + 1];
                    if (bonus == 'X') {
                        if (deliveries[1][i + 1] > 0) {
                            bonusPoints[i] += deliveries[1][i + 1];
                        } else {
                            if (i + 2 < MAX_FRAMES) {
                                bonusPoints[i] += deliveries[0][i + 2];
                            }
                        }
                    }
                } else {
                    if (bonus == 'X') {
                        bonusPoints[i] += deliveries[1][i];
                        bonusPoints[i] += deliveries[2][i];
                    } else {
                        bonusPoints[i] += deliveries[2][i];
                    }
                }
            }
        }
        int score = 0;
        for (int i = 0; i < MAX_FRAMES; i++) {
            score += frameScores[i] + bonusPoints[i];
        }
        return score;
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
