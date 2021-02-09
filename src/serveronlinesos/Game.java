/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serveronlinesos;

import java.util.ArrayList;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author user
 */
public class Game {

    String currentPlayer;
    short player1Points;
    short player2Points;
    String player1;
    String player2;
    short arr[][];
    short count;

    public Game(String player1, String player2) {
        this.player1 = player1;
        this.player2 = player2;
        currentPlayer = player1;
        arr = new short[7][7];
        player1Points = 0;
        player2Points = 0;
        count = 0;

    }

    public short getValueAt(int i, int j) {
        return arr[i][j];
    }

    public short getCount() {
        return count;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public short getPlayer1Points() {
        return player1Points;
    }

    public short getPlayer2Points() {
        return player2Points;
    }

    public String getPlayer1() {
        return player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public ArrayList<Location[]> changeState(int i, int j, short value) {
        arr[i][j] = value;
        count++;
        ArrayList<Location[]> strikedLocs = checkSuccess(i, j);
        if (strikedLocs.size() == 0) {
            currentPlayer = currentPlayer.equals(player1) ? player2 : player1;
        }
        return strikedLocs;
    }

    public void printArr() {
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                System.out.print(arr[i][j] + " ");
            }
            System.out.println();
        }

    }

    private ArrayList<Location[]> checkSuccess(int i, int j) {
        ArrayList<Location[]> strikedLocs = new ArrayList<>();
        if (arr[i][j] == 1)//1 is S
        {
            if (j >= 2 && arr[i][j - 1] == 2 && arr[i][j - 2] == 1) {
                System.out.println("if 1");
                Location loc[] = {new Location(i, j), new Location(i, j - 1), new Location(i, j - 2)};
                strikedLocs.add(loc);
                updatePoints();
            }
            if (i >= 2 && arr[i - 1][j] == 2 && arr[i - 2][j] == 1) {
                System.out.println("if 2");
                Location loc[] = {new Location(i, j), new Location(i - 1, j), new Location(i - 2, j)};
                strikedLocs.add(loc);
                updatePoints();
            }
            if (i >= 2 && j <= 4 && arr[i - 1][j + 1] == 2 && arr[i - 2][j + 2] == 1) {
                System.out.println("if 3");
                Location loc[] = {new Location(i, j), new Location(i - 1, j + 1), new Location(i - 2, j + 2)};
                strikedLocs.add(loc);
                updatePoints();
            }
            if (i <= 4 && arr[i + 1][j] == 2 && arr[i + 2][j] == 1) {
                System.out.println("if 4");
                Location loc[] = {new Location(i, j), new Location(i + 1, j), new Location(i + 2, j)};
                strikedLocs.add(loc);
                updatePoints();
            }
            if (i <= 4 && j <= 4 && arr[i + 1][j + 1] == 2 && arr[i + 2][j + 2] == 1) {
                System.out.println("if 5");
                Location loc[] = {new Location(i, j), new Location(i + 1, j + 1), new Location(i + 2, j + 2)};
                strikedLocs.add(loc);
                updatePoints();
            }
            if (i >= 2 && j >= 2 && arr[i - 1][j - 1] == 2 && arr[i - 2][j - 2] == 1) {
                System.out.println("if 6");
                Location loc[] = {new Location(i, j), new Location(i - 1, j - 1), new Location(i - 2, j - 2)};
                strikedLocs.add(loc);
                updatePoints();
            }
            if (j <= 4 && arr[i][j + 1] == 2 && arr[i][j + 2] == 1) {
                System.out.println("if 7");
                Location loc[] = {new Location(i, j), new Location(i, j + 1), new Location(i, j + 2)};
                strikedLocs.add(loc);
                updatePoints();
            }
            if (i <= 4 && j >= 2 && arr[i + 1][j - 1] == 2 && arr[i + 2][j - 2] == 1) {
                System.out.println("if 8");
                Location loc[] = {new Location(i, j), new Location(i + 1, j - 1), new Location(i + 2, j - 2)};
                strikedLocs.add(loc);
                updatePoints();
            }
        } else if (arr[i][j] == 2)// 2 is O
        {
            if (i >= 1 && i <= 5 && j >= 1 && j <= 5) {
                if (arr[i][j - 1] == 1 && arr[i][j + 1] == 1) {
                    Location loc[] = {new Location(i, j), new Location(i, j - 1), new Location(i, j + 1)};
                    strikedLocs.add(loc);
                    updatePoints();
                }
                if (arr[i - 1][j] == 1 && arr[i + 1][j] == 1) {
                    Location loc[] = {new Location(i, j), new Location(i - 1, j), new Location(i + 1, j)};
                    strikedLocs.add(loc);
                    updatePoints();
                }
                if (arr[i + 1][j - 1] == 1 && arr[i - 1][j + 1] == 1) {
                    Location loc[] = {new Location(i, j), new Location(i + 1, j - 1), new Location(i - 1, j + 1)};
                    strikedLocs.add(loc);
                    updatePoints();
                }
                if (arr[i - 1][j - 1] == 1 && arr[i + 1][j + 1] == 1) {
                    Location loc[] = {new Location(i, j), new Location(i - 1, j - 1), new Location(i + 1, j + 1)};
                    strikedLocs.add(loc);
                    updatePoints();
                }
            } else if ((i == 0 || i == 6) && j >= 1 && j <= 5) {
                if (arr[i][j - 1] == 1 && arr[i][j + 1] == 1) {
                    Location loc[] = {new Location(i, j), new Location(i, j - 1), new Location(i, j + 1)};
                    strikedLocs.add(loc);
                    updatePoints();
                }
            } else if ((j == 0 || j == 6) && i >= 1 && i <= 5) {
                if (arr[i - 1][j] == 1 && arr[i + 1][j] == 1) {
                    Location loc[] = {new Location(i, j), new Location(i - 1, j), new Location(i + 1, j)};
                    strikedLocs.add(loc);
                    updatePoints();
                }
            }
        }
        return strikedLocs;
    }

    private void updatePoints() {
        if (currentPlayer.equals(player1)) {
            player1Points++;
        } else {
            player2Points++;
        }

    }
//    public static void main(String arg[])
//    {
//        Game game = new Game("Mehta","Sinha");
//        boolean gameOver=false;
//        Scanner sc=new Scanner(System.in);
//        short val;
//        int i,j;
//        String currentUser;
//        game.printArr();
//        ArrayList<Location[]> strikedLocs;
//        while(!gameOver)
//        {
//            strikedLocs=new ArrayList<>();
//            System.out.println("Mehta Scored "+game.getPlayer1Points()+" Sinha Scored "+
//                game.getPlayer2Points());
//            currentUser=game.getCurrentPlayer();
//            System.out.println(game.getCurrentPlayer()+" turn");
//            i=sc.nextInt();
//            j=sc.nextInt();
//            val=sc.nextShort();
//            strikedLocs=game.changeState(i, j, val);
//            if(game.getCount()==49)
//                gameOver=true;
//            for(int k=0;k<strikedLocs.size();k++)
//            {
//                Location loc[]=strikedLocs.get(k);
//                for(int m=0;m<3;m++)
//                    System.out.println(loc[m]);
//                System.out.println();
//            }
//            game.printArr();
//            
//        }
//        System.out.println("Mehta Scored "+game.getPlayer1Points()+" Sinha Scored "+
//                game.getPlayer2Points());
//    }
}

class Location {

    int x, y;

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return x + y + "";
    }

}
