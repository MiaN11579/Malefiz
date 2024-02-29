import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MoveCalculation {
    
	private ArrayList<Coordinate> possibleMoveList;
    private int numberOfSolution = 0;
	private RoundButton[][] gameBoard;
    private Color colorP, colorB, currentColor;
    private GameConfig gameConfig;

    public MoveCalculation(GameConfig gameConfig, RoundButton currentB, int diceResult, RoundButton[][] gameBoard) {
        this.gameBoard = gameBoard;
        this.gameConfig = gameConfig;
        // Get coordinate of current button
        Coordinate current = new Coordinate(currentB.getXcoord(), currentB.getYcoord());
        //colorCurrent = currentB.getBackground();
        colorP = new Color(36, 39, 40);
		colorB = new Color(121, 125, 127);
		currentColor = currentB.getBackground();
        possibleMoveList = new ArrayList<Coordinate>();
        cal(current,diceResult, 0);
    }
    private void cal(Coordinate current, int diceResult, int comingFrom) {
        
		Coordinate next;
		if (diceResult == 0) { // if we can't move anymore, we reached a legal destination location
		    if (!gameBoard[current.getX()][current.getY()].inBase() &&
		        !gameBoard[current.getX()][current.getY()].getBackground().equals(currentColor)) {
            	possibleMoveList.add(current);
    			numberOfSolution++; // One more solution !
		    }
            return;
    	}
        
        /* 
        * If comingFrom = 1 -> the previous square is on the bottom, 2 -> the previous square is on the left, 
        * 3 -> the previous square is on the top and 4 -> on the right. 0 means no previous square.
        */

        if (comingFrom != 1 && checkPosition(current.getX()+1,current.getY(), diceResult - 1)) { //if not from the bottom and the bottom button is available
        
            //diceResult = checkHomebase(current.getX()+1,current.getY(), diceResult);
            next = new Coordinate(current.getX()+1, current.getY()); //the next position is on the top
            if (checkHomebase(current.getX() + 1, current.getY())) {
                cal(next, diceResult, 3); //3 = next time is from the top
            } else {
                cal(next, diceResult - 1, 3); //3 = next time is from the top
            }
        } 
        
        if (comingFrom != 2 && checkPosition(current.getX(),current.getY()-1, diceResult - 1)) { ///if not from the left and the left button is available
        
            //diceResult = checkHomebase(current.getX(),current.getY()-1, diceResult);
            next = new Coordinate(current.getX(),current.getY()-1); //the next position is on the right
            if (checkHomebase(current.getX(), current.getY() - 1)) {
                cal(next, diceResult, 4); //4 = next time is from the right
            } else {
                cal(next, diceResult - 1, 4); //4 = next time is from the right
            }
        } 
        
        if (comingFrom != 3 && checkPosition(current.getX()-1,current.getY(), diceResult - 1)) { //if not from the top and the top button is available
        
            //diceResult = checkHomebase(current.getX()-1,current.getY(), diceResult);
            next = new Coordinate(current.getX()-1,current.getY()); //the next position is on the bottom
            if (checkHomebase(current.getX() - 1, current.getY())) {
                cal(next, diceResult, 1); //1 = next time is from the bottom
            } else {
                cal(next, diceResult - 1, 1); //1 = next time is from the bottom
            }
        }
        
        if (comingFrom != 4 && checkPosition(current.getX(),current.getY()+1, diceResult - 1)) { //if not from the right and the right button is available
        
            //diceResult = checkHomebase(current.getX(),current.getY()+1, diceResult);
            next = new Coordinate(current.getX(),current.getY()+1); //the next position is on the left
            if (checkHomebase(current.getX(), current.getY() + 1)) {
                cal(next, diceResult, 2); //2 = next time is from the left
            } else {
                cal(next, diceResult - 1, 2); //2 = next time is from the left
            }
        }
        
    }

    // Check the position of the current piece
    private boolean checkPosition(int x, int y, int diceResult) {
        if ((x >= 17) || (y >= 17) || (x < 0) || (y < 0)) {
            return false;
        }
        
        // If piece passes through or lands on an empty space
        if ((gameBoard[x][y].getBackground().equals(Color.orange))) {
            return true;
        }
        
        // If this position is the winning space
        if ((gameBoard[x][y].getBackground().equals(colorP))) {
            return true;
        }

        
        if ((diceResult == 0)) {
            if (gameBoard[x][y].getBackground().equals(colorB)) { // If piece lands on a barricade
                return true;
            }
        }
        
        // If piece passes through or lands on another player piece
        for (Color color : gameConfig.getColorList()) {      
            if (gameBoard[x][y].getBackground().equals(color)) {
                  return true;
            }
        }
        // Else
        return false;
    }
    
    // Check if position belongs to a base
    private boolean checkHomebase(int x, int y) {
        return gameBoard[x][y].inBase();
    }


    public ArrayList<Coordinate> getPossibleMoveList() {
        return possibleMoveList;
    }

    public int getNumberOfSolution() {
        return numberOfSolution;
    }
}
