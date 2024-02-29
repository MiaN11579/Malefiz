import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Specifies an alternate color setting for the game. There
 * are settings for three different kinds of color deficiency.
 * 
 * @author Andrew Harris
 */
public enum ColorSetting {
	
	/** No color adjustment. */
	REGULAR(Color.blue, Color.yellow, Color.green, Color.red),
	
	/** An adjustment for red-green color deficiency. */
	DEUTERANOMALY(new Color(40, 155, 241), new Color(188, 175, 145),
			new Color(255, 241, 87), new Color(113, 130, 235)),
	
	/** Another adjustment for red-green color deficiency. */
	PROTANOMALY(new Color(255, 160, 0), new Color(162, 176, 242),
			new Color(45, 110, 184), new Color(143, 102, 18)),
	
	/** An adjustment for blue-yellow color deficiency. */
	TRITANOMALY(new Color(255, 0, 67), new Color(142, 131, 178),
			new Color(81, 189, 129), new Color(0, 187, 183));

	/**
	 * The list of colors for a particular color configuration.
	 * Indexed in player order, from 0-3 inclusive.
	 */
	private List<Color> colorList;
	
	/**
	 * Create a new ColorSetting entry with a list of specified colors.
	 * 
	 * This isn't locked to any number of colors, so technically this could expand
	 * to more players or different pieces altogether.
	 * 
	 * @param playerColors The list of colors for each player. In player order, from 0-3 inclusive.
	 */
	ColorSetting(Color... playerColors) {
		colorList = new ArrayList<>();
		
		for (Color color : playerColors) {
			colorList.add(color);
		}
	}
	
	/**
	 * Get the list of all configuration colors.
	 * 
	 * Indexed in player order, from 0-3 inclusive.
	 * 
	 * @return The list of all player colors.
	 */
	public List<Color> getColorList() {
		return colorList;
	}
	
	/**
	 * Get the color for a specified player.
	 * 
	 * @param player The player number, with 0 indicating player one.
	 * @return The color for the specified player.
	 */
	public Color getColorForPlayer(int player) {
		Color playerColor = Color.white;
		
		try {
			playerColor = colorList.get(player);
		} catch (IndexOutOfBoundsException e) {
			System.out.println("Failed to get color for player " + player);
		}
		
		return playerColor;
	}
}
