import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.*;
import javax.swing.border.*;

import java.util.ArrayList;

/*
 *  Window for the game setting menu. 
 *
 *  @author David Chicas, Nhu Nguyen
 */
public class GameSetting extends JPanel {
    private JLabel settingLabel, playerLabel1, playerLabel2, playerLabel3, playerLabel4;
    private Color colorM;
    private JPanel mainPanel, playerPanel1, playerPanel2, playerPanel3, playerPanel4;
	private JComboBox<String> aiCombo;
    public ArrayList<JPanel> panelList;
    private ArrayList<JLabel> labelList;
    
    /**
     * The configuration for the current game. This screen currently doesn't alter any
     * part of the configuration, but it uses the color list & player name list to display
     * the game players.
     */
    private GameConfig gameConfig;
    
    public GameSetting(GameConfig gameConfig) {
    	
    	this.gameConfig = gameConfig;
    	
        // Setup menu color
        colorM = new Color(52, 152, 219);

        // Setup panel list
        playerPanel1 = new JPanel(new BorderLayout());
        playerPanel2 = new JPanel(new BorderLayout());
        playerPanel3 = new JPanel(new BorderLayout());
        playerPanel4 = new JPanel(new BorderLayout());
        panelList = new ArrayList<JPanel>();
        panelList.add(playerPanel1);
        panelList.add(playerPanel2);
        panelList.add(playerPanel3);
        panelList.add(playerPanel4);

        // Setup label list
        playerLabel1 = new JLabel();
        playerLabel2 = new JLabel();
        playerLabel3 = new JLabel();
        playerLabel4 = new JLabel();
        labelList = new ArrayList<JLabel>();
        labelList.add(playerLabel1);
        labelList.add(playerLabel2);
        labelList.add(playerLabel3);
        labelList.add(playerLabel4);

        // Setup main panel
        setBackground(colorM);
        setLayout(new BorderLayout(0,30));
        setBorder(new EmptyBorder(new Insets(50, 150, 50, 150)));

        // Label for game setting
        settingLabel = new JLabel("Game Settings", JLabel.CENTER);
        settingLabel.setFont(new Font("Verdana", Font.PLAIN, 50));
        add(settingLabel, BorderLayout.NORTH);
        addComponentListener(new ComponentAdapter() {
			@Override
			public void componentHidden(ComponentEvent evt) {
				
			}
			@Override
			public void componentShown(ComponentEvent evt) {
				setColor();
				setName();
			}
		});
        
        playerSetup();
    }

    private void playerSetup() {
        // Panel setup
        mainPanel = new JPanel(new GridLayout(2,2,10,10));
        for (int i = 1; i < 5; i++) {
            // Panel for each player
            JPanel topPanel = new JPanel(new FlowLayout(1,30,1));
            panelList.get(i-1).setBackground(getColor(i));

            // Label for each player
            labelList.get(i-1).setText("Player " + i);
            labelList.get(i-1).setFont(new Font("Verdana", Font.PLAIN, 20));
            
            topPanel.add(labelList.get(i-1));
            topPanel.add(getAiBox());
            panelList.get(i-1).add(topPanel, BorderLayout.NORTH);
            mainPanel.add(panelList.get(i-1));
        }

        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);
    }
    
    // Get the desired color from the list
    public Color getColor(int i) {
        return gameConfig.getColorList().get(i-1);
    }

    // Combobox for human/computer player
    private JComboBox getAiBox() {
        final DefaultComboBoxModel<String> panelName = new DefaultComboBoxModel<String>(new String[] {
            "Human Player",
            "Computer Player",
            "Computer Player(H)"
        });

        //Combobox for color setup
        aiCombo = new JComboBox<String>(panelName);
        aiCombo.setFont(new Font("Verdana", Font.PLAIN, 20));
        aiCombo.setSelectedIndex(0);
        return aiCombo;
    }

    // Update list of colors
    private void setColor() {
        for (int i = 1; i < 5; i++) {
            panelList.get(i-1).setBackground(getColor(i));
        }
    }

    // Update list of player names
    private void setName() {
        for (int i = 0; i < 4; i++) {
            labelList.get(i).setText(gameConfig.getNameForPlayer(i));
        }
    }

}
