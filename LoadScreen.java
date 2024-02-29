import java.awt.*;
import java.awt.event.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.*;

import javax.swing.*;
import javax.swing.border.*;

import java.util.ArrayList;
import java.util.Random;
import java.util.List;

/*
 *  Window for the load menu.
 *
 *  @author David Chicas, Nhu Nguyen, Andrew Harris
 */
public class LoadScreen extends JPanel {

    private Color colorM;
    private JLabel loadGameLabel, loadLabel1, loadLabel2, loadLabel3;
    private JPanel mainPanel, loadPanel1, loadPanel2, loadPanel3;
    public ArrayList<JPanel> panelList;
    private ArrayList<JLabel> labelList;
    private ArrayList<JButton> buttonList;
    
    private GameConfig gameConfig;

    public LoadScreen(GameConfig gameConfig) {
    	this.gameConfig = gameConfig;
    	
        // Setup menu color
        colorM = new Color(52, 152, 219);

        // Setup panel list
        loadPanel1 = new JPanel(new BorderLayout());
        loadPanel2 = new JPanel(new BorderLayout());
        loadPanel3 = new JPanel(new BorderLayout());
        panelList = new ArrayList<JPanel>();
        panelList.add(loadPanel1);
        panelList.add(loadPanel2);
        panelList.add(loadPanel3);

        // Setup label list
        loadLabel1 = new JLabel();
        loadLabel2 = new JLabel();
        loadLabel3 = new JLabel();
        labelList = new ArrayList<JLabel>();
        labelList.add(loadLabel1);
        labelList.add(loadLabel2);
        labelList.add(loadLabel3);

        // Setup main panel

        setBackground(colorM);
        setLayout(new BorderLayout(0,30));
        setBorder(new EmptyBorder(new Insets(50, 350, 50, 350)));

        // Label for load game menu
        loadGameLabel = new JLabel("Load Game", JLabel.CENTER);
        loadGameLabel.setFont(new Font("Verdana", Font.PLAIN, 50));
        add(loadGameLabel, BorderLayout.NORTH);

        loadSetup();
    }

    private void loadSetup() {
        // Panel setup
        mainPanel = new JPanel(new GridLayout(3,1,10,10));
        buttonList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            // Panel for each save slot
            JPanel topPanel = new JPanel(new FlowLayout(1,30,1));
            panelList.get(i).setBackground(Color.white);

            // Label for each save slot
            int num = i + 1;
            labelList.get(i).setText("Save slot " + num);
            labelList.get(i).setFont(new Font("Verdana", Font.PLAIN, 20));

            // Button for each save slot
            JButton loadButton = new JButton("Empty slot");
            loadButton.setBackground(Color.white);
            loadButton.setFont(new Font("Verdana", Font.PLAIN, 30));
            loadButton.addActionListener(button -> load(num));
            buttonList.add(loadButton);
            
            topPanel.add(labelList.get(i));
            panelList.get(i).add(topPanel, BorderLayout.NORTH);
            panelList.get(i).add(loadButton, BorderLayout.CENTER);
            mainPanel.add(panelList.get(i));
        }

        updateSlotTexts();
        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);
    }
    
    private void load(int slot) {
    	String emptyCheck = "";
        try {
            emptyCheck = Files.readString(Path.of("saveSlots/save" + slot + ".txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        if (emptyCheck.length() != 0) {
        	gameConfig.setFileDirectory("saveSlots/save" + slot + ".txt");
        	gameConfig.requestGameReset();
        	JOptionPane.showMessageDialog(this, "Game file loaded, go back to play!");
        } else {
        	JOptionPane.showMessageDialog(this, "Game loading failed, cannot load empty file!");
        }
        
        updateSlotTexts();
    }
    
    public void updateSlotTexts() {
        for (int i = 1; i <= 3; i++) {
            String emptyCheck = "";
            try {
                emptyCheck = Files.readString(Path.of("saveSlots/save" + i + ".txt"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            if (emptyCheck.length() != 0) {
                buttonList.get(i - 1).setText("Saved Game");
            }
        }
    }
}