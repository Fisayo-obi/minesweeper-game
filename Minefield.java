// Import Section
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

public class Minefield {

    public static final String ANSI_YELLOW_BRIGHT = "\u001B[33;1m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE_BRIGHT = "\u001b[34;1m";
    public static final String ANSI_BLUE = "\u001b[34m";
    public static final String ANSI_RED_BRIGHT = "\u001b[31;1m";
    public static final String ANSI_RED = "\u001b[31m";
    public static final String ANSI_GREEN = "\u001b[32m";
    public static final String ANSI_PURPLE = "\u001b[35m";
    public static final String ANSI_CYAN = "\u001b[36m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001b[47m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001b[45m";
    public static final String ANSI_GREY_BACKGROUND = "\u001b[0m";

    private int rows;
    private int columns;
    private int flags;
    private Cell[][] cell;

    public Minefield(int rows, int columns, int flags) {
        this.rows = rows;
        this.columns  = columns;
        this.flags = flags;
        cell = new Cell[rows][columns];
        for(int i = 0; i< cell.length; i++){
            for(int k = 0; k< cell[0].length; k++){
                cell[i][k] = new Cell(false,"0");
            }
        }
    }
    public int getFlags(){
        return this.flags;
    }
    public void evaluateField() {
        for (int i = 0; i < cell.length; i++) {
            for (int k = 0; k < cell[0].length; k++) {
                if (cell[i][k].getStatus().equals("M")) {
                    for (int j = -1; j <= 1; j++) {
                        for (int h = -1; h <= 1; h++) {
                            if (j == 0 && h == 0)
                                continue;
                            int newRow = i + j;
                            int newCol = k + h;

                            if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < columns) {
                                if (cell[newRow][newCol].getStatus().equals("M")) {
                                    continue;
                                }
                                        int num;
                                        String status = cell[newRow][newCol].getStatus();
                                        if (status.equals("")) {
                                            num = 0;
                                        } else {
                                            num = Integer.parseInt(status);
                                        }
                                        num++;
                                        cell[newRow][newCol].setStatus((Integer.toString(num)));



                            }
                        }
                    }
                }
            }
        }
    }





    public void createMines(int x, int y, int mines) {
    Random rand = new Random();
    for (int i = 0; i < mines; i++){
       int  newX = rand.nextInt(rows);
        int newY = rand.nextInt(columns);
        if (cell[newX][newY].getStatus().equals("M") || newX == x && newY == y){
            i--;
            continue;

        }
        cell[newX][newY].setStatus("M");

    }
    }

    public boolean guess(int x, int y, boolean flag) {
        if (x < 0 || x >= rows || y < 0 || y >= columns) {
            System.out.println("Invalid guess");
            return false;
        }
        if (flag == true) {
            if (cell[x][y].getStatus().equals("F")) {
                cell[x][y].setStatus("-");
                flags++;
                return false;
            } else if (!cell[x][y].getStatus().equals("F")) {
                if (flags > 0) {
                    cell[x][y].setStatus("F");
                    flags--;
                    return false;

                } else {
                    System.out.println("Not enough flags");
                    return false;
                }
            }
        }
        if (flag == false){
            if(cell[x][y].getStatus().equals("M")){
                cell[x][y].setRevealed(true);
                gameOver();
                return true;
            }else if(cell[x][y].getStatus().equals("0")) {
                revealZeroes(x, y);
                cell[x][y].setRevealed(true);
                return false;
            }else{
                cell[x][y].setRevealed(true);
                return false;
            }

        }
        return false;

    }


    public boolean gameOver() {
        for (int x = 0; x < cell.length; x++) {
            for (int y = 0; y < cell[0].length; y++) {
                if (cell[x][y].getStatus().equals("M") && cell[x][y].getRevealed() == true) {
                    for (int j = 0; j < cell.length; j++) {
                        for (int k = 0; k < cell[0].length; k++) {
                            cell[j][k].setRevealed(true);
                        }
                    }
                    System.out.println("You have lost");
                    return true;
                }
            }
        }

        int count = 0;
        int safeCnt = (rows * columns) - flags;
        for (int x = 0; x < cell.length; x++) {
            for (int y = 0; y < cell[0].length; y++) {
                if (!cell[x][y].getStatus().equals("M") && cell[x][y].getRevealed() == true) {
                   count++;

                    }

                }
            if (count == safeCnt){
                System.out.println("You have won!");
                return true;
            }
        }
        return false;
    }

    public void revealZeroes(int x, int y) {
        Stack<int[]> mystack = new Stack<>();
        mystack.push(new int[]{x, y});
        while (!mystack.isEmpty()) {
            int[] current = mystack.pop();
            cell[current[0]][current[1]].setRevealed(true);
            for (int j = -1; j <= 1; j++) {
                for (int h = -1; h <= 1; h++) {
                    if (j == 0 && h == 0)
                        continue;
                    int newRow = current[0] + j;
                    int newCol = current[1] + h;

                    if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < columns) {
                        if (cell[newRow][newCol].getRevealed() == false && cell[newRow][newCol].getStatus().equals("0")){
                            mystack.push(new int[]{newRow,newCol});
                        }
                    }
                }
            }
        }
    }

    public void revealStartingArea(int x, int y) {
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{x,y});
        while(!queue.isEmpty()) {
            int[] current = queue.remove();
            if (cell[current[0]][current[1]].getStatus().equals("M")) {
                break;
            } else {
                cell[current[0]][current[1]].setRevealed(true);
            }
            for (int j = -1; j <= 1; j++) {
                for (int h = -1; h <= 1; h++) {
                    if (j == 0 && h == 0)
                        continue;
                    int newRow = current[0] + j;
                    int newCol = current[1] + h;

                    if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < columns) {
                        if (cell[newRow][newCol].getRevealed()== false){
                            queue.add(new int[]{newRow,newCol});
                        }
                    }
                }
            }
        }
    }


    public void debug() {

        for (int i = 0; i < cell.length; i++) {
            for (int k = 0; k < cell[0].length; k++) {
                if (cell[i][k].getStatus().equals("M")){
                    System.out.print(ANSI_RED +"M" + " " + ANSI_GREY_BACKGROUND);
                }
                if (cell[i][k].getStatus().equals("1")) {
                    System.out.print(ANSI_YELLOW + "1" + " " + ANSI_GREY_BACKGROUND);
                }
                if (cell[i][k].getStatus().equals("2")) {
                    System.out.print(ANSI_BLUE + "2" + " " + ANSI_GREY_BACKGROUND);
                }
                if (cell[i][k].getStatus().equals("3")) {
                    System.out.print(ANSI_CYAN + "3" + " " + ANSI_GREY_BACKGROUND);
                }
                if (cell[i][k].getStatus().equals("4")) {
                    System.out.print(ANSI_GREEN + "4" + " " + ANSI_GREY_BACKGROUND);
                }
                if (cell[i][k].getStatus().equals("5")) {
                    System.out.print(ANSI_PURPLE + "5" + " " + ANSI_GREY_BACKGROUND);
                }
                if (cell[i][k].getStatus().equals("6")) {
                    System.out.print(ANSI_RED_BRIGHT + "6" + " " + ANSI_GREY_BACKGROUND);
                }
                if (cell[i][k].getStatus().equals("0")) {
                    System.out.print(ANSI_BLUE_BRIGHT + "0" + " " + ANSI_GREY_BACKGROUND);
                }
                if (cell[i][k].getStatus().equals("8")) {
                    System.out.print(ANSI_PURPLE_BACKGROUND + "8" + " " + ANSI_GREY_BACKGROUND);
                }
                if (cell[i][k].getStatus().equals("7")) {
                    System.out.print(ANSI_YELLOW_BRIGHT + "7" + " " + ANSI_GREY_BACKGROUND);
                }
                if (cell[i][k].getStatus().equals("F")) {
                    System.out.print("F"+ " ");
                }
            }
            System.out.println();
        }

    }

    @Override
    public String toString() {
        String str ="";
        for (int i = 0; i < cell.length; i++) {
            for (int k = 0; k < cell[0].length; k++) {
                if (cell[i][k].getRevealed()== false){
                    str = str + "-" + " ";
                }
                if (cell[i][k].getRevealed()== true){
                     if (cell[i][k].getStatus().equals("M"))  {
                          str = str + ANSI_RED +"M" + " " + ANSI_GREY_BACKGROUND;
                      }
                      if (cell[i][k].getStatus().equals("0"))  {
                          str = str + ANSI_BLUE_BRIGHT + "0" + " " + ANSI_GREY_BACKGROUND;
                        }
                      if (cell[i][k].getStatus().equals("1"))  {
                            str = str + ANSI_YELLOW + "1" + " " + ANSI_GREY_BACKGROUND;
                      }
                      if (cell[i][k].getStatus().equals("2"))  {
                            str = str + ANSI_BLUE + "2" + " " + ANSI_GREY_BACKGROUND;
                        }
                      if (cell[i][k].getStatus().equals("3"))  {
                            str = str + ANSI_CYAN + "3" + " " + ANSI_GREY_BACKGROUND;
                        }
                      if (cell[i][k].getStatus().equals("4"))  {
                            str = str + ANSI_GREEN + "4" + " " + ANSI_GREY_BACKGROUND;
                        }
                      if (cell[i][k].getStatus().equals("5"))  {
                            str = str + ANSI_PURPLE + "5" + " " + ANSI_GREY_BACKGROUND;
                        }
                      if (cell[i][k].getStatus().equals("6"))  {
                            str = str + ANSI_RED_BRIGHT + "6" + " " + ANSI_GREY_BACKGROUND;
                        }
                      if (cell[i][k].getStatus().equals("7"))  {
                            str = str + ANSI_YELLOW_BRIGHT + "7" + " " + ANSI_GREY_BACKGROUND;
                        }
                      if (cell[i][k].getStatus().equals("8"))  {
                            str = str + ANSI_PURPLE_BACKGROUND + "8" + " " + ANSI_GREY_BACKGROUND;
                        }
                      if (cell[i][k].getStatus().equals("F")) {
                          str = str + "F" + " ";
                      }
                    }
            }
            str = str + "\n";
            }
        return str;
    }
}
