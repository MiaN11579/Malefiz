import java.awt.*;
import java.awt.event.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.*;

import javax.swing.*;
import javax.swing.border.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.List;

/*
 *  Window for the actual gameplay.
 *
 *  @author David Chicas, Nhu Nguyen, Andrew Harris
 */
public class GameScreen extends JPanel implements ActionListener {

	private RoundButton[][] gameBoard;
	private JButton resetButton, diceButton;
	private RoundButton currentB, prevB, startPiece;
	private JPanel namePanel, gamePanel, boardgame;
	private JLabel turnLabel, gameLabel;
	private int rows = 17, columns = 17, turn, diceResult, rollTurn;
	String nums, barricades, p;
	private Color currentCol, prevCol, colorP, colorB, colorM;
	private ArrayList<JLabel> labelList;
	private ArrayList<ArrayList<RoundButton>> buttonLists;

	private Dice dice;
	
	private List<Color> colorList;
	
	private GameConfig gameConfig;
	
	private ArrayList<ArrayList<Integer>> houseIndexes;

	private JFrame parent;

	public GameScreen(GameConfig gameConfig, JFrame parent) {
		this.gameConfig = gameConfig;
		this.parent = parent;
		
        // Setup label list
        labelList = new ArrayList<JLabel>();
        labelList.add(new JLabel("", JLabel.CENTER));
        labelList.add(new JLabel("", JLabel.CENTER));
        labelList.add(new JLabel("", JLabel.CENTER));
        labelList.add(new JLabel("", JLabel.CENTER));

        buttonLists = new ArrayList<ArrayList<RoundButton>>();
        houseIndexes = new ArrayList<ArrayList<Integer>>();
		for (int i = 0; i < 4; i++) {
			buttonLists.add(new ArrayList<RoundButton>());
			houseIndexes.add(new ArrayList<Integer>());
		}

		// Setup buttons
		try {
			nums = Files.readString(Path.of("Misc/barricades.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for (int i = 1; i <= 4; i++) {
			int index = nums.indexOf(String.valueOf(i));
			while (index >= 0) {
				houseIndexes.get(i-1).add(index);
				index = nums.indexOf(String.valueOf(i), index + 1);
			}
		}
		
        // Setup color
        colorM = new Color(52, 152, 219);
		colorP = new Color(36, 39, 40);
		colorB = new Color(121, 125, 127);
		prevCol = colorP;

		// Setup rollTurn, dice and dice button
		rollTurn = 1;
		dice = new Dice();
		diceButton = new JButton("Roll dice!");
		diceButton.setBackground(new Color(30, 227, 77));
		diceButton.setFont(new Font("Verdana", Font.PLAIN, 20));
		diceButton.addActionListener(this);

		// Setup reset button
		resetButton = new JButton("Reset game");
		resetButton.setBackground(new Color(30, 227, 77));
		resetButton.setFont(new Font("Verdana", Font.PLAIN, 20));
		resetButton.addActionListener(this);

		// Setup name panel
		namePanel = new JPanel(new GridLayout(1,4,10,0));
		namePanel.setBorder(new EmptyBorder(new Insets(0, 30, 0, 350)));
		namePanel.setBackground(colorM);

		for (int i = 0; i < 4; i++) {
			
            // Label for each player
			JLabel playerLabel = labelList.get(i);
            playerLabel.setText("Player " + (i + 1));
			playerLabel.setFont(new Font("Verdana", Font.BOLD, 20));
			playerLabel.setForeground(Color.WHITE);
			namePanel.add(playerLabel);
		}

		// Setup turn, turn label and game label
		turn = 0;
		diceResult = 0;
		turnLabel = new JLabel("Turn: " + labelList.get(turn).getText(), JLabel.CENTER);
		turnLabel.setFont(new Font("Verdana", Font.PLAIN, 20));

		gameLabel = new JLabel("MALEFIZ GAME", JLabel.CENTER);
		gameLabel.setFont(new Font("Verdana", Font.BOLD, 30));

		// Setup name panel
		namePanel = new JPanel(new GridLayout(1,4,10,0));
		namePanel.setBorder(new EmptyBorder(new Insets(0, 30, 0, 350)));
		namePanel.setBackground(colorM);

		for (int i = 0; i < 4; i++) {
			
            // Label for each player
			JLabel playerLabel = labelList.get(i);
            playerLabel.setText("Player " + (i + 1));
			playerLabel.setFont(new Font("Verdana", Font.BOLD, 20));
			playerLabel.setForeground(Color.WHITE);
			namePanel.add(playerLabel);
		}

		// Setup turn, turn label and game label
		turn = 0;
		diceResult = 0;
		turnLabel = new JLabel("Turn: " + labelList.get(turn).getText(), JLabel.CENTER);
		turnLabel.setFont(new Font("Verdana", Font.PLAIN, 20));

		gameLabel = new JLabel("MALEFIZ GAME", JLabel.CENTER);
		gameLabel.setFont(new Font("Verdana", Font.BOLD, 30));

        // Setup boardgame panel
		boardgame = new JPanel(new GridLayout(rows, columns));
		boardgame.setBorder(new EmptyBorder(new Insets(50, 100, 50, 100)));
		boardgame.setBackground(Color.white);
		boardgame.setSize(1250,850);

		// Setup game panel
		gamePanel = new JPanel(new GridLayout(6,1,0,10));
		gamePanel.setBorder(new EmptyBorder(new Insets(20, 30, 30, 30)));
		gamePanel.setBackground(Color.white);
		gamePanel.add(gameLabel);
		gamePanel.add(turnLabel);
		gamePanel.add(dice);
		gamePanel.add(diceButton);
		gamePanel.add(resetButton);
		
		// Create a game
		initGame();

		// Setup main panel
		setLayout(new BorderLayout(20,10));
		setBorder(new EmptyBorder(new Insets(50, 200, 50, 200)));
		setBackground(colorM);
		add(boardgame, BorderLayout.CENTER);
		add(namePanel, BorderLayout.SOUTH);
		add(gamePanel, BorderLayout.EAST);
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent evt) {
				updateColorList();
				updateNameList();
				updateBoardContents();
				
				if (gameConfig.shouldGameReset()) {
					reset();
					gameConfig.disableResetGame();
				}
			}
		});
	}

	public void initGame(){
		gameBoard = new RoundButton[rows][columns];
		int num = 0;
		char[] ch = new char[nums.length()];
		
		int i;
		for (i = 0; i < nums.length(); i++) { 
            ch[i] = nums.charAt(i); 
		}
		colorList = gameConfig.getColorList();
    
		for ( int x = 0; x < columns; x ++)
        {
            for ( int y = 0; y < rows; y ++)
            {
                gameBoard[x][y] = parseChar(ch[num], x, y);
				
				    for (int j = 0; j < 4; j++) {
					    if (houseIndexes.get(j).contains(num)) {
						    buttonLists.get(j).add(gameBoard[x][y]);
						    gameBoard[x][y].setInBase(true);
					    }
				    }

				    boardgame.add(gameBoard[x][y]);
				    boardgame.setBorder(BorderFactory.createEmptyBorder(0,10,10,10));
				
				    num++;
			}
		}
		
		if (nums.length() > num) { // this is a save file
			turn = Character.getNumericValue(ch[num]);
		}
		setHighlightColor(gameConfig.getColorForPlayer(turn));
	}

	private RoundButton parseChar(char c, int posX, int posY) {
		RoundButton button = new RoundButton(posX, posY);
		
		button.setText(Character.toString(c));

		button.setSize(30, 30);
		button.addActionListener(this);

		if (button.getText().matches(nums)){
		    button.setActive(true);
			button.setText("");
		}
		
		if (button.getText().matches("-")){
			button.setBackground(Color.WHITE);
			button.setActive(false);
			button.setText("");
		} else if(button.getText().matches("1")){
		    button.setActive(true);
			button.setBackground(colorList.get(0));
			button.setText("");
		} else if(button.getText().matches("2")){
		    button.setActive(true);
			button.setBackground(colorList.get(1));
			button.setText("");
		} else if(button.getText().matches("3")){
		    button.setActive(true);
			button.setBackground(colorList.get(2));
			button.setText("");
		} else if(button.getText().matches("4")){
		    button.setActive(true);
			button.setBackground(colorList.get(3));
			button.setText("");
		} else if(button.getText().matches("W")){
		    button.setActive(true);
			button.setBackground(Color.orange);
			button.setText("");
		}  else if(button.getText().matches("o")){
		    button.setActive(true);
			button.setBackground(colorP);
			button.setText("");
		}  else if(button.getText().matches("#")){
		    button.setActive(true);
			button.setBackground(colorB);
			button.setText("");
		}
		
		return button;
	}

	// Get the list of all possible moves.
	private ArrayList<Coordinate> getPossibleMoveList(RoundButton prevB, int diceResult) {

		MoveCalculation moveCalculation = new MoveCalculation(gameConfig, prevB, diceResult, gameBoard);
		return moveCalculation.getPossibleMoveList();
	}

	// Check if the selected move matches one of the possible moves.
	private boolean checkPossibleMove(RoundButton currentB, RoundButton prevB, int diceResult) {
		ArrayList<Coordinate> possibleMoveList = getPossibleMoveList(prevB, diceResult);
		if (possibleMoveList != null) {
			for (Coordinate coordinate : possibleMoveList) {
				int x = coordinate.getX();
				int y = coordinate.getY();
				if ((currentB.getXcoord() == x) && (currentB.getYcoord() == y)) {
					return true;
				}
			}
		}
		
		return false;
	}

	// Check if there is any possible move for the current player
	public int checkMoveForPlayer() { 
		int num = 0;
		for ( int x = 0; x < columns; x ++)
        {
            for ( int y = 0; y < rows; y ++)
            {
				if (gameBoard[x][y].getBackground() == colorList.get(turn)){
					if (!checkMoveForPiece(gameBoard[x][y], diceResult)) {
						num++;
					}
					if (num == 4) {
						updateTurn(); // If no moves can be made, skip turn
						return 0;
					}
				}
			}
		}
		return 0;
	}

	// Check if there is any possible move for a piece
	private boolean checkMoveForPiece(RoundButton prevB, int diceResult) {

		MoveCalculation moveCalculation = new MoveCalculation(gameConfig, prevB, diceResult, gameBoard);
		if (moveCalculation.getNumberOfSolution() == 0) {
			return false;
		}
		return true;
	}

	// Update color list
	public void updateColorList() { 
		// Update the current color of player pieces
		for ( int x = 0; x < columns; x ++)
        {
            for ( int y = 0; y < rows; y ++)
            {
                for (int p = 0; p < 4; p++) {
                    if (gameBoard[x][y].getBackground() == colorList.get(p)){
                        gameBoard[x][y].setBackground(gameConfig.getColorForPlayer(p));
                    }
                }
			}
		}
		
		for (int p = 0; p < 4; p++) {
            if (prevCol == colorList.get(p)){
                prevCol = gameConfig.getColorForPlayer(p);
            }
        }
		
		// Update current color list
		colorList = gameConfig.getColorList();
		setHighlightColor(gameConfig.getColorForPlayer(turn));
		
	}

    // Update list of player names
    public void updateNameList() {
        for (int i = 0; i < 4; i++) {
            labelList.get(i).setText(gameConfig.getNameForPlayer(i));
        }
    }
    
    public void updateBoardContents() {
    	if (gameConfig.getFileDirectory() == null) {
    		return;
    	}
    	
    	try {
    		nums = Files.readString(Path.of(gameConfig.getFileDirectory()));
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }

	// Update the turn
	public void updateTurn() {
	    resetHighlight();
		rollTurn = 1;
		turn = (turn + 1) % 4;
		turnLabel.setText("Turn: " + labelList.get(turn).getText());
		setHighlightColor(gameConfig.getColorForPlayer(turn));
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
        // Get the object that was selected in the gui
		Object selected = e.getSource();

		if (selected.equals(resetButton)) {
			try {
	    		nums = Files.readString(Path.of("Misc/barricades.txt"));
	    	} catch (IOException err) {
	    		err.printStackTrace();
	    	}
			
			reset();
		} else if (selected.equals(diceButton)) {
			if (isTurnComplete()) {
				rollDice();
			}
		} else if (selected instanceof RoundButton) {
			boardClicked((RoundButton) selected);
		}
	}
	
	/**
	 * Reset the current game with default board settings.
	 */
	private void reset() {
		// Remove the current board game
		Component[] components = boardgame.getComponents();

		for (Component component : components) {
			boardgame.remove(component);
		}
		boardgame.revalidate();
		boardgame.repaint();
		prevCol = colorP;
		
		buttonLists = new ArrayList<ArrayList<RoundButton>>();
		for (int i = 0; i < 4; i++) {
			buttonLists.add(new ArrayList<RoundButton>());
		}
	
		// Create a new one
		turn = 0;
		rollTurn = 1;
    	initGame();
		turnLabel.setText("Turn: " + labelList.get(turn).getText());
	}

	// Roll the dice, update dice result
	private void rollDice() {
		Random rand = new Random();
		diceResult = rand.nextInt(6) + 1;
		dice.updateDice(diceResult);
		rollTurn = 0;
		checkMoveForPlayer(); // Checks if any move is possible for current player
	}
	
	/**
	 * Responds to the action of clicking one of the buttons on the main game board.
	 * 
	 * @param roundButton The button that was pressed.
	 */
	private void boardClicked(RoundButton roundButton) {
		currentB = roundButton;
		currentCol = roundButton.getBackground();
		
		// Prevents player to use diceResult value from last turn
		if (rollTurn == 1) {
			return;
		}
		
		if (!currentCol.equals(colorList.get(turn))) { // A piece other than the player's pieces was clicked.
			if (prevCol.equals(colorList.get(turn))) { // A current player's piece was clicked before.
				if (checkPossibleMove(currentB, prevB, diceResult)) {
					// Check if the selected button belongs to a player's base
					for (ArrayList<RoundButton> buttons : buttonLists) {
						if (buttons.contains(roundButton)) { 
							return;
						} 
					}

					placePiece(roundButton, prevB, prevCol);
					prevB = roundButton;
				}
			} else if (currentCol.equals(colorP) && prevCol.equals(colorB)) { // A player needs to put down a barricade.
				setBarricade(roundButton);
			}
		} else if (prevCol.equals(colorP)){ // A piece owned by the player was clicked, and it is the start of a move.
			startPiece(roundButton);
		}
	}

	/**
	 * Set a piece down on the board at the location
	 * specified by a particular button.
	 * 
	 * @param roundButton The button to set a piece down on
	 * @param setColor The color to set the button to.
	 */
	private void placePiece(RoundButton roundButton,RoundButton prevB, Color setColor) {
		// An integer used for checking position of button
		int i = 1;
		prevB.setBackground(colorP);
		if (currentCol.equals(colorP) && (i==1)) {  // The piece is to be placed on an empty space, a normal move.
			roundButton.setBackground(setColor);
			prevCol = colorP;
			updateTurn();
		} else if (colorList.contains(currentCol) && (i==1)) { // The piece is to be placed down on another player's piece; a capture.
			roundButton.setBackground(setColor);
			returnToHome(colorList.indexOf(currentCol));
			prevCol = colorP;
			updateTurn();
		} else if (currentCol.equals(colorB) && (i==1)) { // The piece is to be placed on a barricade, initiate barricade move.
			resetHighlight();
			roundButton.setBackground(prevCol);
			Arrays.stream(gameBoard)
                .flatMap(Arrays::stream)
                .filter(button -> button.getBackground().equals(colorP))
                .filter(button -> !button.inBase())
			    .forEach(button -> button.setHighlighted(true));
			prevCol = colorB;
		} else if (currentCol.equals(Color.orange) && (i==1)){ // The piece is to be placed on the winning position; ending the game
			roundButton.setBackground(setColor);
			prevCol = Color.orange;
			winCondition(roundButton);
		}
	}
	
	private void startPiece(RoundButton roundButton) {
	    List<Coordinate> possibleMove = getPossibleMoveList(roundButton, diceResult);
	    if (possibleMove.size() == 0) {
	        return;
	    }
	    
	    resetHighlight();
		possibleMove.stream().forEach(c -> gameBoard[c.getX()][c.getY()].setHighlighted(true));
		
	    roundButton.setBackground(Color.lightGray);
		prevCol = currentCol;
		prevB = roundButton;
	}

	/**
	 * Clear a piece from a location on the board specified by a particular
	 * button.
	 * 
	 * @param roundButton The button to clear.
	 */
	private void clearPiece(RoundButton roundButton) {
		roundButton.setBackground(colorP);
		prevCol = currentCol;
	}

	/**
	 * Activates win condition
	 * 
	 * @param roundButton The button to clear.
	 */
	private void winCondition(RoundButton roundButton) {
		Color winner = roundButton.getBackground(); // get color of winning player
		for (int i = 0; i < 4; i++){ // for each player
			Color check = gameConfig.getColorForPlayer(i); // get color of each player
			String winnerS = gameConfig.getNameForPlayer(i); // get name of player
			if (check.equals(winner)){ // if the color of player equals color of winning piece
				winScreen dialog = new winScreen(parent,winnerS);
				if (dialog.checkResult()) {
				    try {
		                nums = Files.readString(Path.of("Misc/barricades.txt"));
		            } catch (IOException err) {
		                err.printStackTrace();
		            }
				    
					reset();
				}
				
				//System.out.println("Win condition? Winner: " + winnerS); used for testing
				//add(wScreen, BorderLayout.CENTER); <- need to figure out how to make winScreen appear in frame
			}
		}
    }
	
	/**
	 * Return a single piece back to the home of a specified player.
	 * 
	 * @param player The index number of the player home to return a piece to. (0-3 inclusive)
	 */
	private void returnToHome(int player) {
		for (RoundButton button : buttonLists.get(player)) {
			if (!button.getBackground().equals(currentCol)) {
				button.setBackground(currentCol);
				return;
			}
		}
	}
	
	/**
	 * Set down a barricade on a location on the board specified by a particular
	 * button.
	 * 
	 * @param roundButton The button to place a barricade on.
	 */
	private void setBarricade(RoundButton roundButton) {
		
		for (ArrayList<RoundButton> buttons : buttonLists) {
			if (buttons.contains(roundButton)) { // If the selected button belongs to a player's base
				return;
			} 
		}

		roundButton.setBackground(colorB);
		prevCol = colorP;
		updateTurn();
	}

	/**
	 * Get components of game board
	 * 
	 */
	public Component[] getComp() {
		Component [] components = boardgame.getComponents();
		return components;
	}

	public int getCurrentTurn() {
		return turn;
	}

	public boolean isTurnComplete() {
		return rollTurn == 1;
	}
	
	/**
	 * Reset the highlight value of all board buttons.
	 */
	public void resetHighlight() {
	    Arrays.stream(gameBoard)
	        .flatMap(Arrays::stream)
	        .forEach(button -> button.setHighlighted(false));
	}
	
	/**
     * Set the highlight color of all board buttons.
     * 
     * @param newColor The new highlight color.
     */
    public void setHighlightColor(Color newColor) {
        Arrays.stream(gameBoard)
            .flatMap(Arrays::stream)
            .forEach(button -> button.setHighlightColor(newColor));
    }
}
