import java.awt.*;
import javax.swing.*;


class winScreen extends JDialog {

    private static final int RESET_OPTION = 0;
    private static final int BACK_OPTION = 1;

    private int result = -1;

    private Color colorM;
    private JLabel winner;
    private JPanel mainPanel, btnPanel;

    public winScreen(JFrame parent,String winnerName) {
      super(parent, true);

      // Setup menu color
      colorM = new Color(52, 152, 219);

      // Setup dialog
      setBackground(colorM);
      setLayout(new BorderLayout(3, 3));

      // Disable size and close button
      setSize(700,550);
      setResizable(false);
      setDefaultCloseOperation(0);
      
      winSetup(winnerName);
    }

    private void winSetup(String winnerName) {
        // Setup main panel
        ImageIcon iconImage = new ImageIcon("Misc/congrats.png"); 
        JLabel winLabel = new JLabel(iconImage, JLabel.CENTER);

        mainPanel = new JPanel(new BorderLayout(0,0));
        winner = new JLabel(winnerName + " wins!");
        winner.setHorizontalAlignment(JLabel.CENTER);
        winner.setFont(new Font("Verdana", Font.BOLD, 32));
        winner.setForeground(Color.white);

        // Setup button panel: contains reset and back button
        btnPanel = new JPanel(new FlowLayout());

        // Setup reset button
        JButton reset = new JButton("Reset Game");
        reset.setBackground(Color.white);
        reset.setFont(new Font("Verdana", Font.PLAIN, 25));
        btnPanel.add(reset);
        reset.addActionListener(e->{
            result = RESET_OPTION;
            setVisible(false);
        });

        // Setup back button
        JButton back = new JButton("Go Back");
        back.setBackground(Color.white);
        back.setFont(new Font("Verdana", Font.PLAIN, 25));
        btnPanel.add(back);
        back.addActionListener(e->{
            result = BACK_OPTION;
            setVisible(false);
        });

        mainPanel.add(winLabel, BorderLayout.NORTH);
        mainPanel.add(winner, BorderLayout.CENTER);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);
        mainPanel.setBackground(colorM);
        add(mainPanel, BorderLayout.CENTER);
        setLocationRelativeTo(getParent());
        setTitle("Malefiz Game");
        setVisible(true);
    }

    // Checks the returned result
    public boolean checkResult() {
        if (result == RESET_OPTION) {
            return true;
        }
        return false;
    }
  }
  
