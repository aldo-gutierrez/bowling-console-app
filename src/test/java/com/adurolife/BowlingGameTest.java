package com.adurolife;


import com.adurolife.model.BowlingGameImpl;
import com.adurolife.model.BowlingGame;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class BowlingGameTest {

    @Test
    void testCurrentVariables() {
        BowlingGame b = new BowlingGameImpl();
        assertEquals(0, b.getCurrentFrame());
        assertEquals(0, b.getCurrentRoll());
        b.addRoll(5);
        assertEquals(0, b.getCurrentFrame());
        assertEquals(1, b.getCurrentRoll());
        b.addRoll(3);
        assertEquals(1, b.getCurrentFrame());
        assertEquals(0, b.getCurrentRoll());
        assertEquals(8, b.getCurrentScore());
    }

    @Test
    void testValidation() {
        BowlingGame b = new BowlingGameImpl();
        try {
            b.addRoll(11);
            fail("fail to validate max roll value");
        } catch (Exception ex) {

        }
        b.addRoll(5);
        try {
            b.addRoll(6);
            fail("fail to validate max pin value");
        } catch (Exception ex) {

        }
    }

    @Test
    void testScores() {
        BowlingGame b = new BowlingGameImpl();
        assertEquals(90, evaluate(b, "9- 9- 9- 9- 9- 9- 9- 9- 9- 9-"));
        assertEquals(300, evaluate(b, "X X X X X X X X X X X X"));
        assertEquals(150, evaluate(b, "5/ 5/ 5/ 5/ 5/ 5/ 5/ 5/ 5/ 5/5"));
        assertEquals(248, evaluate(b, "9/ X X X 9/ X X X 9/ XXX"));
        assertEquals(221, evaluate(b, "9/ X X X 9/ X X X 9/ 45"));
        try {
            b.addRoll(5);
            fail("Game should be finished");
        } catch (Exception ex) {

        }
    }

    private int evaluate(BowlingGame bg, String s) {
        bg.resetGame();
        List<Integer> rolls = convert(s);
        rolls.forEach(bg::addRoll);
        return bg.getCurrentScore();
    }

    private List<Integer> convert(String string) {
        List<Integer> result = new ArrayList<>();
        for (char c : string.toCharArray()) {
            if (c == 'X') {
                result.add(10);
            } else if (c == '-') {
                result.add(0);
            } else if (c == '/') {
                result.add(10 - result.get(result.size() - 1));
            } else if (c != ' ') {
                result.add(c - '0');
            }
        }
        return result;
    }

}
