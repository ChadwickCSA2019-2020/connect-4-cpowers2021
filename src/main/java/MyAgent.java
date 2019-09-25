import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;
/**
 * Describe your basic strategy here.
 * @author <your Github username>
 *
 */
public class MyAgent extends Agent {
    int[] fire = {1, 2, 3, 4, 5}; //taking a slice of an array
    int start = 1;
    int end = 2;
    int[] ice = Arrays.copyOfRange(fire, start, end+1);

    /**
     * A random number generator to randomly decide where to place a token.
     */
    private Random random;

    /**
     * Constructs a new agent, giving it the game and telling it whether it is Red or Yellow.
     *
     * @param game The game the agent will be playing.
     * @param iAmRed True if the agent is Red, False if the agent is Yellow.
     */
    public MyAgent(Connect4Game game, boolean iAmRed) {
        super(game, iAmRed);
        random = new Random();
    }

    /**
     * The move method is run every time it is this agent's turn in the game. You may assume that
     * when move() is called, the game has at least one open slot for a token, and the game has not
     * already been won.
     *
     * <p>By the end of the move method, the agent should have placed one token into the game at some
     * point.</p>
     *
     * <p>After the move() method is called, the game engine will check to make sure the move was
     * valid. A move might be invalid if:
     * - No token was place into the game.
     * - More than one token was placed into the game.
     * - A previous token was removed from the game.
     * - The color of a previous token was changed.
     * - There are empty spaces below where the token was placed.</p>
     *
     * <p>If an invalid move is made, the game engine will announce it and the game will be ended.</p>
     *
     */

    public void move(){
        if (this.iCanWin() != -1) {
            moveOnColumn(iCanWin());
        }
        else if (this.theyCanWin() != -1) {
            moveOnColumn(theyCanWin());
        }
        else {
            for (int i = 0; i < 6; i++) {
                Connect4Game gameCopy = new Connect4Game(myGame);
                MyAgent agentCopy = new MyAgent(gameCopy, iAmRed);
                agentCopy.moveOnColumn(i);
                if (agentCopy.theyCanWin() > -1) {
                    avoid = [i];
                }
            }
        }

        // First move is in the middle
        boolean firstMove = myGame.getRedPlayedFirst();
        if (!firstMove){
            System.out.println("Moving in middle column, first move");
            moveOnColumn(3);
            firstMove = true;
        }
    }

    public ArrayList<Integer> weDie() {
        ArrayList<Integer> avoid = new ArrayList<Integer>();
        Connect4Game gameCopy = new Connect4Game(myGame);
        MyAgent agentMeCopy = new MyAgent(gameCopy, iAmRed);
        MyAgent agentThemCopy = new MyAgent(gameCopy, !iAmRed);
        for (int i = 0; i < 7; i++) {
            agentMeCopy.moveOnColumn(i);
            agentThemCopy.moveOnColumn(i);
            if (agentMeCopy.theyCanWin() > -1) {
                avoid.add(i);
            }
        }
        return avoid;
    }
    // write move so that it chooses which of the viable columns to move on --> move on column
    
    // If you are going to win. Do it.
    // If the enemy is going to win. Block them.

    //                if (this.iCanWin() > -1) {
    //                this.moveOnColumn(this.iCanWin());
    //            }
    //            else if (this.theyCanWin() > -1) {
    //                this.moveOnColumn(this.theyCanWin());
    //            }
    //            else {
    //
    //                this.randomMove();
    //
    //            }
    //        }

    /**
     * Drops a token into a particular column so that it will fall to the bottom of the column.
     * If the column is already full, nothing will change.
     *
     * @param columnNumber The column into which to drop the token.
     */
    public void moveOnColumn(int columnNumber) {
        // Find the top empty slot in the column
        // If the column is full, lowestEmptySlot will be -1
        int lowestEmptySlotIndex = getLowestEmptyIndex(myGame.getColumn(columnNumber));//row number
        // if the column is not full
        if (lowestEmptySlotIndex > -1) {
            // get the slot in this column at this index
            Connect4Slot lowestEmptySlot = myGame.getColumn(columnNumber).getSlot(lowestEmptySlotIndex);
            // If the current agent is the Red player...
            if (iAmRed) {
                lowestEmptySlot.addRed(); // Place a red token into the empty slot
            } else {
                lowestEmptySlot.addYellow(); // Place a yellow token into the empty slot
            }
        }
    }

    /**
     * Returns the index of the top empty slot in a particular column.
     *
     * @param column The column to check.
     * @return
     *      the index of the top empty slot in a particular column;
     *      -1 if the column is already full.
     */
    public int getLowestEmptyIndex(Connect4Column column) {
        int lowestEmptySlot = -1;
        for  (int i = 0; i < column.getRowCount(); i++) {
            if (!column.getSlot(i).getIsFilled()) {
                lowestEmptySlot = i;
            }
        }
        return lowestEmptySlot;
    }

    /**
     * Returns a random valid move. If your agent doesn't know what to do, making a random move
     * can allow the game to go on anyway.
     *
     * @return a random valid move.
     */
    public int randomMove() {
        int i = random.nextInt(myGame.getColumnCount());
        while (getLowestEmptyIndex(myGame.getColumn(i)) == -1) {
            i = random.nextInt(myGame.getColumnCount());
        }
        return i;
    }

    /**
     * Returns the column that would allow the agent to win.
     *
     * <p>You might want your agent to check to see if it has a winning move available to it so that
     * it can go ahead and make that move. Implemen  t this method to return what column would
     * allow the agent to win.</p>
     *
     * @return the column that would allow the agent to win.
     */
    // i can win horizontal
    //  public int iCanWin() {
    //    // i = 0
    //
    //    int columns = myGame.getColumnCount();
    //    int rows = myGame.getRowCount();
    //    char myTeam = 'Y';
    //
    //    if (iAmRed) {
    //      myTeam = 'R';
    //    }
    //    // theres no winning spot
    //    int toReturn = -1;
    //
    //    for (int i = 0; i < rows; i++) {
    //      int lowestHorizontal = lowest(myGame.getBoardMatrix()[i]);
    //      if (lowestHorizontal == -1) {
    //        continue;
    //      }
    //
    //      if (myGame.getBoardMatrix()[i][lowestHorizontal] == myTeam &&
    //          myGame.getBoardMatrix()[i][lowestHorizontal + 1] == myTeam &&
    //          myGame.getBoardMatrix()[i][lowestHorizontal + 2] == myTeam) {
    //
    //        toReturn = lowestHorizontal + 3;
    //      }
    //
    //      // for loop for indices- (vertical)
    //    }
    //
    //    for (int i = 0; i < columns; i++) {
    //      int lowestVertical = lowest(myGame.getBoardMatrix()[i]);
    //      if (lowestVertical == -1) {
    //        continue;
    //      }
    //
    //      if (myGame.getBoardMatrix()[i][lowestVertical] == myTeam &&
    //          myGame.getBoardMatrix()[i][lowestVertical + 1] == myTeam &&
    //          myGame.getBoardMatrix()[i][lowestVertical + 2] == myTeam) {
    //
    //        toReturn = lowestVertical + 3;
    //      }
    //
    //    }
    //
    //    // for loop for columns+/- (horizontal)
    //    // for loop for index+/-1 and column+/- (diagonal)
    //    return toReturn;
    //  }

    // i can win vertical and horizontal
    public int iCanWin() {
        for (int i = 0; i < 6; i++) {
            // create a copy of the current game
            Connect4Game gameCopy = new Connect4Game(myGame);
            // need an agent to play our copied game
            MyAgent agentCopy = new MyAgent(gameCopy, iAmRed);
            agentCopy.moveOnColumn(i);
            if (iAmRed) {
                if (gameCopy.gameWon() == 'R') {
                    return i;
                }
            }
            else {
                if (gameCopy.gameWon() == 'Y') {
                    return i;
                }
            }
        }

        return -1;

    }





    /* private int lowest(char[] row) {
    char lol = 'Y';

    if (iAmRed) {
      lol = 'R';
    }

    int lowest = Integer.MAX_VALUE;
    for (int i = 0; i < row.length; i++) {
      if (row[i] == lol) {
        if (i < lowest) {
          lowest = i;
        }
      }
    }
    if (lowest == Integer.MAX_VALUE) {
      return -1;
    }
    return lowest;
  }  */

    /**
     * Returns the column that would allow the opponent to win.
     *
     * <p>You might want your agent to check to see if the opponent would have any winning moves
     * available so your agent can block them. Implement this method to return what column should
     * be blocked to prevent the opponent from winning.</p>
     *
     * @return the column that would allow the opponent to win.
     */
    public int theyCanWin() {
        for (int i = 0; i < 6; i++) {
            // create a copy of the current game
            Connect4Game gameCopy = new Connect4Game(myGame);
            // need an agent to play our copied game
            MyAgent agentCopy = new MyAgent(gameCopy, !iAmRed);
            agentCopy.moveOnColumn(i);
            if (iAmRed) {
                if (gameCopy.gameWon() == 'Y') {
                    return i;
                }
            }
            else {
                if (gameCopy.gameWon() == 'R') {
                    return i;
                }
            }
        }

        return -1;

    }

    /**
     * Returns the name of this agent.
     *
     * @return the agent's name
     */
    public String getName() {
        return "boiblaster";
    }
}
