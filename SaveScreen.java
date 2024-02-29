import java.awt.*;
import java.awt.event.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.*;

import javax.swing.*;
import javax.swing.border.*;

import java.util.ArrayList;
/*
 *  Window for the save menu.
 *
 *  @author David Chicas, Nhu Nguyen, Andrew Harris
 */

public class SaveScreen extends JPanel { //JPanel instead of JDialog

    private GameScreen game_screen;
    private Color colorM;
    private JLabel saveGameLabel, saveLabel1, saveLabel2, saveLabel3;
    private JPanel mainPanel, savePanel1, savePanel2, savePanel3;
    public ArrayList<JPanel> panelList;
    private ArrayList<JLabel> labelList;
    private ArrayList<JButton> saveSlotButtons;

    private GameConfig gameConfig;

    public SaveScreen(GameScreen game_screen, GameConfig gameConfig) {

        this.gameConfig = gameConfig;
        this.game_screen = game_screen;
        
        // Setup menu color
        colorM = new Color(52, 152, 219);

        // Setup panel list
        savePanel1 = new JPanel(new BorderLayout());
        savePanel2 = new JPanel(new BorderLayout());
        savePanel3 = new JPanel(new BorderLayout());
        panelList = new ArrayList<JPanel>();
        panelList.add(savePanel1);
        panelList.add(savePanel2);
        panelList.add(savePanel3);

        // Setup label list
        saveLabel1 = new JLabel();
        saveLabel2 = new JLabel();
        saveLabel3 = new JLabel();
        labelList = new ArrayList<JLabel>();
        labelList.add(saveLabel1);
        labelList.add(saveLabel2);
        labelList.add(saveLabel3);

        // Setup main panel

        setBackground(colorM);
        //BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(new BorderLayout(0,30));
        //setLayout(boxLayout);
        setBorder(new EmptyBorder(new Insets(50, 350, 50, 350)));

        // Label for save game menu
        saveGameLabel = new JLabel("Save Game", JLabel.CENTER);
        saveGameLabel.setFont(new Font("Verdana", Font.PLAIN, 50));
        add(saveGameLabel, BorderLayout.NORTH);
        
        
        saveSlotButtons = new ArrayList<JButton>();
        saveSetup();

    }

    private void saveSetup() {
        // Panel setup
        mainPanel = new JPanel(new GridLayout(3,1,10,10));
        for (int i = 0; i < 3; i++) {
            // Panel for each save slot
            JPanel topPanel = new JPanel(new FlowLayout(1,30,1));
            panelList.get(i).setBackground(Color.white);

            // Label for each save slot
            int num = i + 1;
            labelList.get(i).setText("Save slot " + num);
            labelList.get(i).setFont(new Font("Verdana", Font.PLAIN, 20));

            // Button for each save slot
            JButton saveButton = new JButton("Empty Slot");
            saveButton.setBackground(Color.white);
            saveButton.setFont(new Font("Verdana", Font.PLAIN, 30));
            saveButton.addActionListener(button -> save(num));
            saveSlotButtons.add(saveButton);
            
            String emptyCheck = "";
            try {
                emptyCheck = Files.readString(Path.of("saveSlots/save" + num + ".txt"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            if (emptyCheck.length() != 0) {
            	saveButton.setText("Saved Game");
            }
            
            topPanel.add(labelList.get(i));
            panelList.get(i).add(topPanel, BorderLayout.NORTH);
            panelList.get(i).add(saveButton, BorderLayout.CENTER);
            mainPanel.add(panelList.get(i));
        }

        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private void save(int slotNum){
        Component[] comps = game_screen.getComp();
        String outputString = "";
        String emptyCheck = "";

        for (Component i : comps){
        	outputString += parseComponent(i);
        }
        
        outputString += game_screen.getCurrentTurn();

        try {
           emptyCheck = Files.readString(Path.of("saveSlots/save" + slotNum + ".txt"));
        } catch (IOException e) {
           e.printStackTrace();
        }

        if (emptyCheck.length() != 0){
            int response = JOptionPane.showConfirmDialog(this, "Selected slot already contains a game file, overwrite?", "Save Game", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.NO_OPTION) {
            	return;
            }
        }
        
        try{
        	Path filePath = Path.of("saveSlots/save" + slotNum + ".txt");
            String content = outputString;
            Files.writeString(filePath, content);
        } catch (IOException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Game save failed!");
        }
        JOptionPane.showMessageDialog(this, "Game save succeeded!");
        saveSlotButtons.get(slotNum-1).setText("Saved Game");
    }
    
    public String parseComponent(Component i) {
    	Color color_i = i.getBackground();
        Color colorP1 = gameConfig.getColorForPlayer(0);
        Color colorP2 = gameConfig.getColorForPlayer(1);
        Color colorP3 = gameConfig.getColorForPlayer(2);
        Color colorP4 = gameConfig.getColorForPlayer(3);
        Color colorbarr = new Color(121, 125, 127);
        Color blank = Color.white;
        Color win = Color.orange;
        Color empty = new Color(36, 39, 40);
    	
    	if (color_i.equals(colorP1)){
            return "1";
        } else if (color_i.equals(colorP2)){
        	return "2";
        } else if (color_i.equals(colorP3)){
        	return "3";
        } else if (color_i.equals(colorP4)){
        	return "4";
        } else if (color_i.equals(colorbarr)){
        	return "#";
        } else if (color_i.equals(blank)){
        	return "-";
        } else if (color_i.equals(win)){
        	return "W";
        } else if (color_i.equals(empty)){
        	return "o";
        }
    	return "";
    }
}
