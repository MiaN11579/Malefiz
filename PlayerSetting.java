import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

import java.util.ArrayList;

/*
 *  The main window of the PlayerSettings
 *  Notice that it extends JPanel - so we can add our own components.
 *  Notice that it implements ActionListener - so we can handle user input.
 *  @author ztiwari, Nhu Nguyen
 */
public class PlayerSetting extends JPanel implements ActionListener{
	
	private JPanel buttonPanel, topPanel, gridPanel, midPanel;
	private JLabel playerSettingsLabel, colorLabel, preferredName; 
	private JTextField piece1, piece2, piece3, piece4;
	private Color colorM;
	private JComboBox <String> myCombo;
	private DefaultComboBoxModel<String> panelName;
	
	/**
	 * The configuration for the current game. This screen alters the player names &
	 * color list settings.
	 */
	private GameConfig gameConfig;
	
	public PlayerSetting(GameConfig gameConfig) {
		
		this.gameConfig = gameConfig;
		
		colorM = new Color(52, 152, 219);

		//setSize(1200,800);
        setBackground(colorM);
        setBorder(new EmptyBorder(new Insets(50, 150, 50, 150)));
		setLayout(new GridLayout(3,1,10,20));

		// Button panel setup
		buttonPanel = new JPanel();
		buttonPanel.setLayout( new FlowLayout());
		buttonPanel.setPreferredSize(new Dimension (600, 300));
		buttonPanel.setBackground(colorM);

		// Middle panel setup
		midPanel = new JPanel();
		midPanel.setLayout( new FlowLayout());
		midPanel.setBackground(colorM);
		
		// Second panel setup
		topPanel = new JPanel();
		topPanel.setLayout(new GridLayout(3,1,10,10));
		topPanel.setBackground(colorM);

		// Grid panel setup
		gridPanel = new JPanel(new GridLayout(3,1,10,10));
		gridPanel.setBackground(colorM);
		
		//button for player 1
		piece1 = new JTextField(" Player 1 ");
		piece1.setFont(new Font("Verdana", Font.PLAIN, 30));
		buttonPanel.add(piece1);

		//button for player 2
		piece2 = new JTextField(" Player 2 ");
		piece2.setFont(new Font("Verdana", Font.PLAIN, 30));
		buttonPanel.add(piece2);

		//button for player 3
		piece3 = new JTextField(" Player 3 ");
		piece3.setFont(new Font("Verdana", Font.PLAIN, 30));
		buttonPanel.add(piece3);

		//button for player 4
		piece4 = new JTextField(" Player 4 ");
		piece4.setFont(new Font("Verdana", Font.PLAIN, 30));
		buttonPanel.add(piece4);
		
		piece1.setBackground(Color.blue);
		piece2.setBackground(Color.yellow);
		piece3.setBackground(Color.green);
		piece4.setBackground(Color.red);

		initList();

		topPanel.add(playerSettingsLabel);
		topPanel.add(colorLabel);
		topPanel.add(myCombo);

		midPanel.add(preferredName);
		
		gridPanel.add(midPanel);
		gridPanel.add(buttonPanel);

		add(topPanel);
		add(gridPanel);
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentHidden(ComponentEvent evt) {
				updatePlayerNames();
			}
			@Override
			public void componentShown(ComponentEvent evt) {
				
			}
		});
        setVisible(true);
	}
	
	public void initList() {
		// Color setting label
		colorLabel = new JLabel("Color mode:");
		colorLabel.setFont(new Font("Verdana", Font.PLAIN, 30));

		// Player setting label
		playerSettingsLabel = new JLabel("Player Settings", JLabel.CENTER);
		playerSettingsLabel.setFont(new Font("Verdana", Font.BOLD, 50));
		//add(playerSettingsLabel, BorderLayout.NORTH);
		
		// Setup name of selected player
		preferredName = new JLabel("Enter name for each player:");
		preferredName.setFont(new Font("Verdana", Font.PLAIN, 30));

		// Setup Color box
		panelName = new DefaultComboBoxModel<String>(new String[] {
			"Retro Colors",
			"Night Colors",
			"Forest Colors",
			"Sunset Colors"
		});

		myCombo = new JComboBox<>();
		myCombo.setModel(panelName);
		myCombo.setFont(new Font("Verdana", Font.PLAIN, 20));
		myCombo.setBounds(150,150,150,150);
		myCombo.addActionListener(this);
		
	}
	
	private void changeColorSetting(String i){
		ColorSetting newColorSetting;
		
		if (i ==  "Night Colors"){
			newColorSetting = ColorSetting.DEUTERANOMALY;
		}
		else if (i ==  "Forest Colors"){
			newColorSetting = ColorSetting.PROTANOMALY;
		}
		else if (i ==  "Sunset Colors"){
			newColorSetting = ColorSetting.TRITANOMALY;
		} 
		else {
			newColorSetting = ColorSetting.REGULAR;
		}
		
		piece1.setBackground(newColorSetting.getColorForPlayer(0));
		piece2.setBackground(newColorSetting.getColorForPlayer(1));
		piece3.setBackground(newColorSetting.getColorForPlayer(2));
		piece4.setBackground(newColorSetting.getColorForPlayer(3));
		
		gameConfig.setColorSetting(newColorSetting);
	}
	
	private void updatePlayerNames() {
		ArrayList<String> playerNames = new ArrayList<String>();
		playerNames.add(piece1.getText());
		playerNames.add(piece2.getText());
		playerNames.add(piece3.getText());
		playerNames.add(piece4.getText());
		gameConfig.setPlayerNames(playerNames);
	}

	@Override
	public void actionPerformed(ActionEvent aevt) {
		String t = (String) myCombo.getSelectedItem();
		changeColorSetting(t);
	}
   
}

