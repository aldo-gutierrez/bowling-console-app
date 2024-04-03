package com.adurolife.console;

import com.adurolife.model.BowlingGame;
import com.adurolife.model.BowlingGameImpl;

import java.io.PrintStream;
import java.util.Scanner;

public class BowlingAppConsole {

    public void printMenu() {
        PrintStream out = System.out;
        out.println("1.-     Add Roll");
        out.println("2.-     Get current Frame");
        out.println("3.-     Get current Roll");
        out.println("4.-     Get current Score");
        out.println("5.-     Restart");
        out.println("6.-     Quit");
        out.println("ENTER.- Print this menu");
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        BowlingGame game = new BowlingGameImpl();
        System.out.println(game.getSmallDashboard());
        printMenu();
        int lastRoll = 0;
        while (true) {
            System.out.println();
            String input = scanner.nextLine();
            if (input == null || input.trim().isEmpty()) {
                System.out.println(game.getSmallDashboard());
                printMenu();
                continue;
            }
            Integer option;
            try {
                option = Integer.parseInt(input);
            } catch (Exception ex) {
                System.err.println(" Please select one of the menu options");
                continue;
            }
            if (option == 1) {
                Integer roll;
                while (true) {
                    System.out.print(" Input roll: ");
                    String rollS = scanner.nextLine();
                    rollS = rollS.trim().toLowerCase();
                    if (rollS.equals("x")) {
                        roll = 10;
                        break;
                    } else if (rollS.equals("/")) {
                        roll = 10 - lastRoll;
                        break;
                    } else if (rollS.equals("-")) {
                        roll = 0;
                        break;
                    } else {
                        try {
                            roll = Integer.parseInt(rollS);
                            break;
                        } catch (Exception ex) {
                            System.err.println(" Please introduce a valid roll \n");
                        }
                    }
                }
                try {
                    game.addRoll(roll);
                    lastRoll = roll;
                } catch (Exception ex) {
                    System.err.println(" Error " + ex.getMessage() + " \n");
                }
            } else if (option == 2) {
                System.out.println(" Current Frame: " + (game.getCurrentFrame() + 1));
            } else if (option == 3) {
                System.out.println(" Current Roll: " + (game.getCurrentRoll() + 1));
            } else if (option == 4) {
                System.out.println(" Current Score: " + (game.getCurrentScore()));
            } else if (option == 5) {
                game.resetGame();
                lastRoll = 0;
                System.out.println(" Game Restarted");
            } else {
                System.out.println(" Exiting.");
                System.exit(0);
            }
        }
    }

}
