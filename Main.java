//Import Section
import java.util.Random;
import java.util.Scanner;

/*
 * Provided in this class is the neccessary code to get started with your game's implementation
 * You will find a while loop that should take your minefield's gameOver() method as its conditional
 * Then you will prompt the user with input and manipulate the data as before in project 2
 * 
 * Things to Note:
 * 1. Think back to Project 1 when we asked our user to give a shape. In this project we will be asking the user to provide a mode. Then create a minefield accordingly
 * 2. You must implement a way to check if we are playing in debug mode or not.
 * 3. When working inside your while loop think about what happens each turn. We get input, user our methods, check their return values. repeat.
 * 4. Once while loop is complete figure out how to determine if the user won or lost. Print appropriate statement.
 */

public class Main {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        System.out.println("Choose a difficulty level: Easy,Medium or Hard");
        String level = s.nextLine();
        int rows = 0;
        int columns = 0;
        int flags = 0;
        if (level.equalsIgnoreCase("Easy")) {
            rows = 5;
            columns = 5;
            flags = 5;

        } else if ((level.equalsIgnoreCase("Medium"))) {
            rows = 9;
            columns = 9;
            flags = 12;

        } else if (level.equalsIgnoreCase("Hard")) {
            rows = 20;
            columns = 20;
            flags = 40;

        } else {
            System.out.println("Invalid Choice, Choose Again");
        }
        System.out.println("Debug Mode? Y/N");
        String db = s.nextLine();
        Minefield minefield = new Minefield(rows, columns, flags);
        System.out.println("Enter starting coordinates (x,y)");
        String coor = s.nextLine();
        String[] cord = coor.split(",");
        int xVal = Integer.parseInt(cord[0]);
        int yVal = Integer.parseInt(cord[1]);
        minefield.createMines(xVal, yVal, flags);
        minefield.evaluateField();
        minefield.revealStartingArea(xVal, yVal);

        while (!minefield.gameOver()) {
            if (db.equalsIgnoreCase("Y")) {
                minefield.debug();
            }
            System.out.println(minefield);
            System.out.println("Enter a coordinate and place a flag if you would like(x,y,Y/N, Remaining:" + minefield.getFlags());
            String choice = s.nextLine();
            String[] choi = choice.split(",");
            int newXval = Integer.parseInt(choi[0]);
            int newYval = Integer.parseInt(choi[1]);
            boolean flg = false;
            if (choi[2].equalsIgnoreCase("Y")){
                flg = true;
            }
            minefield.guess(newXval, newYval, flg);
            minefield.gameOver();
        }



        }
    }

