import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

/*
 *  Main Window for the game. Contains/Controls most of the components of the game. Starts
 *  on a main menu window with buttons to navigate to other game screens.
 *
 *  @author David, Nhu Nguyen, Andrew Harris
 */
public class MainMenu extends JFrame {
	
	/**
	 * The main color of all UI backgrounds.
	 */
	private static final Color BACKGROUND_COLOR = new Color(52, 152, 219);
	
	/**
	 * The horizontal width of the game window in pixels.
	 */
	private static final int WINDOWSIZE_X = 1500;
	
	/**
	 * The vertical width of the game window in pixels.
	 */
	private static final int WINDOWSIZE_Y = 950;
	
	/**
	 * The main panel for the menu. Contains the game logo, and all menu buttons.
	 */
	private JPanel mainPanel;
	
	/**
	 * Contains panels for every menu in the game.
	 */
	private CardLayout coreLayout;
	
	/**
	 * The configuration for the current game.
	 */
	private GameConfig gameConfig;

	/**
	 * The saving icon for save button.
	 */
    private ImageIcon imageIcon;

    /**
     * Create a new Malefiz main menu.
     */
    public MainMenu()
    {
        super("Malefiz Game // Group 7");
        getContentPane().setBackground(BACKGROUND_COLOR);

        gameConfig = new GameConfig();
        createComponents();
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WINDOWSIZE_X, WINDOWSIZE_Y);
        setVisible(true);
    }

    /**
     * Creates all components for the game.
     */
    private void createComponents() {
        // Setting up game icon
        ImageIcon iconImage = new ImageIcon("Misc/logo.png"); 
        JLabel logoLabel = new JLabel(iconImage, JLabel.CENTER);

        // Menu panel
        JPanel menuPanel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(menuPanel, BoxLayout.Y_AXIS);
        menuPanel.setBackground(BACKGROUND_COLOR);
        menuPanel.setLayout(boxLayout);
        menuPanel.setBorder(new EmptyBorder(new Insets(30, 440, 30, 440)));
        menuPanel.add(createButtonPanel());
        
        // Game Menus
        GameScreen gameScreen = new GameScreen(gameConfig, this);
        GameSetting gameSettingScreen = new GameSetting(gameConfig);
        PlayerSetting playerSettingScreen = new PlayerSetting(gameConfig);
        LoadScreen loadScreen = new LoadScreen(gameConfig);
        SaveScreen saveScreen = new SaveScreen(gameScreen, gameConfig);
        
        // Panel for back and save button / Back and save button setup
        JPanel backPanel = new JPanel();
        backPanel.setBackground(BACKGROUND_COLOR);
        backPanel.setLayout(new BorderLayout());
        backPanel.add(newMenuButton("Back", action -> showScreen("menu")), BorderLayout.WEST);
        backPanel.setVisible(false);

        JPanel gameBackPanel = new JPanel();
        gameBackPanel.setBackground(BACKGROUND_COLOR);
        gameBackPanel.setLayout(new BorderLayout());
        gameBackPanel.add(newMenuButton("Back", action -> showScreen("game")), BorderLayout.WEST);
        gameBackPanel.setVisible(false);

        imageIcon = new ImageIcon("Misc/floppyDisk.jpg");
        Image image = imageIcon.getImage(); // transform to an image
        Image newImage = image.getScaledInstance(38, 38,  java.awt.Image.SCALE_SMOOTH); // scale the image
        imageIcon = new ImageIcon(newImage);  // transform back to imageIcon

        JPanel savePanel = new JPanel();
        savePanel.setBackground(BACKGROUND_COLOR);
        savePanel.setLayout(new BorderLayout());
        savePanel.add(newMenuButton("Load Game ", action -> {showScreen("load");}), BorderLayout.EAST);
        savePanel.add(newMenuButton("Save Game ", action -> {
        	if (gameScreen.isTurnComplete()) {
        		showScreen("save");
        	} else {
        		JOptionPane.showMessageDialog(this, "Please complete the current turn before saving!");
        	}
        }), BorderLayout.WEST);
        savePanel.setVisible(false);

        JPanel topPanel = new JPanel();
        topPanel.setBackground(BACKGROUND_COLOR);
        topPanel.setLayout(new FlowLayout());
        topPanel.add(backPanel);
        topPanel.add(gameBackPanel);
        topPanel.add(savePanel);
        
        // Control panel
        JPanel controlPanel = new JPanel(new GridLayout(2,1));
        controlPanel.setBackground(BACKGROUND_COLOR);
        controlPanel.setBorder(new EmptyBorder(new Insets(0, 0, 100, 0)));
        controlPanel.add(logoLabel);
        controlPanel.add(menuPanel);
        controlPanel.addComponentListener(new ComponentAdapter() {
        	@Override
            public void componentHidden(ComponentEvent evt) {
                backPanel.setVisible(true);
            }
            @Override
            public void componentShown(ComponentEvent evt) {
            	backPanel.setVisible(false);
            }
        });

        // Setup gameScreen Menu
        gameScreen.addComponentListener(new ComponentAdapter() {
        	@Override
            public void componentHidden(ComponentEvent evt) {
                savePanel.setVisible(false);
            }
            @Override
            public void componentShown(ComponentEvent evt) {
            	savePanel.setVisible(true);
                backPanel.setVisible(true);
            }
        });

        // Setup saveScreen Menu
        saveScreen.addComponentListener(new ComponentAdapter() {
        	@Override
            public void componentHidden(ComponentEvent evt) {
                gameBackPanel.setVisible(false);
            }
            @Override
            public void componentShown(ComponentEvent evt) {
            	gameBackPanel.setVisible(true);
            	backPanel.setVisible(false);
            }
        });

        // Setup loadScreen Menu
        loadScreen.addComponentListener(new ComponentAdapter() {
        	@Override
            public void componentHidden(ComponentEvent evt) {
                gameBackPanel.setVisible(false);
            }
            @Override
            public void componentShown(ComponentEvent evt) {
            	gameBackPanel.setVisible(true);
            	backPanel.setVisible(false);
            	loadScreen.updateSlotTexts();
            }
        });
        
        coreLayout = new CardLayout();
        mainPanel = new JPanel(coreLayout);
        mainPanel.add(controlPanel, "menu");
        mainPanel.add(gameSettingScreen, "gameSetting");
        mainPanel.add(playerSettingScreen, "playerSetting");
        mainPanel.add(gameScreen, "game");
        mainPanel.add(loadScreen, "load");
        mainPanel.add(saveScreen, "save");
        
        add(topPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        pack();
        //setLocationByPlatform(true);
    }
    
    /**
     * Create a new menu button panel, and add buttons to navigate to each screen in the game.
     * 
     * @return The newly populated button panel.
     */
    private JPanel createButtonPanel() {
    	JPanel buttonPanel = new JPanel(new GridLayout(5,1,10,10));
        buttonPanel.setBackground(BACKGROUND_COLOR);
    	
    	// Menu buttons
        buttonPanel.add(newMenuButton("Start Game", action -> showScreen("game")));
        buttonPanel.add(newMenuButton("Game Settings", action -> showScreen("gameSetting")));
        buttonPanel.add(newMenuButton("Player Settings", action -> showScreen("playerSetting")));
        buttonPanel.add(newMenuButton("Exit", action -> exitGame()));
        
        return buttonPanel;
    }
    
    /**
     * Create a new menu button with given settings.
     * 
     * @param name The text on the button.
     * @param actionResponse The listener to respond to button inputs.
     * @return The newly created button.
     */
    private JButton newMenuButton(String text, ActionListener actionResponse) {
        JButton newButton = new JButton(text);
        if (text.equals("Save Game ")) {
            newButton.setIcon(imageIcon);
            newButton.setHorizontalTextPosition(SwingConstants.LEFT);
        }
    	newButton.setBackground(Color.white);
    	newButton.setFont(new Font("Verdana", Font.PLAIN, 30));
    	newButton.addActionListener(actionResponse);
    	return newButton;
    }
    
    /**
     * Show a game screen with the given name.
     * 
     * @param name The name of the screen to show.
     */
    private void showScreen(String name) {
    	coreLayout.show(mainPanel, name);
    }
    
    /**
     * Exit the game.
     */
    private void exitGame() {
    	System.exit(0);
    }

}
