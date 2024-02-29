import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains the user-selected settings configuration for a new Malefiz game.
 * Altered in <@link PlayerSetting> and <@link LoadScreen>, and used in both 
 * <@link GameScreen> and <@link GameSetting>.
 * 
 * @author Andrew Harris
 */
public class GameConfig {
	
	/**
	 * The color setting currently used across the game.
	 */
	private ColorSetting colorSetting;
	
	/**
	 * The list of all player names. Indexed in player order, from 0-3 inclusive.
	 */
	private List<String> playerNames;
	
	/**
	 * The file directory of the game to load, if one has been selected. Null indicates no
	 * file has been loaded.
	 */
	private String fileDir;
	
	/**
	 * Flag indicating if the game should be reset upon entry to StartGame screen.
	 */
	private boolean resetGame;
	
	/**
	 * Create a new GameConfig with default settings.
	 */
	public GameConfig() {
		List<String> defaultNameList = new ArrayList<>();
		
		for (int i = 0; i < 4; i++) {
			defaultNameList.add("Player " + i);
		}
		
		setColorSetting(ColorSetting.REGULAR);
		setPlayerNames(defaultNameList);
	}
	
	public void requestGameReset() {
		resetGame = true;
	}
	
	public void disableResetGame() {
		resetGame = false;
	}
	
	public boolean shouldGameReset() {
		return resetGame;
	}
	
	public void setFileDirectory(String fileName) {
		this.fileDir = fileName;
	}
	
	public String getFileDirectory() {
		return fileDir;
	}

	/**
	 * Get the color that should be used for a particular player.
	 * 
	 * @param player The player number, with 0 indicating player one.
	 * @return The color for the specified player.
	 */
	public Color getColorForPlayer(int player) {
		return colorSetting.getColorForPlayer(player);
	}

	/**
	 * Set the universal color deficiency setting for the game.
	 * 
	 * @param colorSetting The new color setting to use.
	 */
	public void setColorSetting(ColorSetting colorSetting) {
		this.colorSetting = colorSetting;
	}
	
	/**
	 * Get a list of all player colors to use according to the current color
	 * deficiency setting.
	 * 
	 * The list will be indexed in player order, from 0-3 inclusive.
	 * 
	 * @return The list of all current player colors.
	 */
	public List<Color> getColorList() {
		return colorSetting.getColorList();
	}
	
	/**
	 * Get the name that should be used for a particular player.
	 * 
	 * @param player The player number, with 0 indicating player one.
	 * @return The name of the specified player.
	 */
	public String getNameForPlayer(int player) {
		String playerName = "Player " + player;
		
		try {
			playerName = playerNames.get(player);
		} catch (IndexOutOfBoundsException e) {
			System.out.println("Failed to get name for player " + player);
		}
		
		return playerName;
	}

	/**
	 * Set the list of all player names to the provided list.
	 * 
	 * The list should be indexed in player order.
	 * 
	 * @param playerNames The new list of player names to use.
	 */
	public void setPlayerNames(List<String> playerNames) {
		this.playerNames = playerNames;
	}
	
}
