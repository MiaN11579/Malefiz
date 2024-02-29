import java.awt.*;
import javax.swing.*;

public class Dice extends JLabel{
    public Dice() {
        setHorizontalAlignment(JLabel.CENTER);
    }

    public void updateDice(int diceResult) {
        ImageIcon imageIcon = new ImageIcon("Misc/dice" + diceResult + ".png"); // load the image to a imageIcon
        Image image = imageIcon.getImage(); // transform to an image
        Image newImage = image.getScaledInstance(75, 75,  java.awt.Image.SCALE_SMOOTH); // scale the image 
        imageIcon = new ImageIcon(newImage);  // transform back to imageIcon
        setIcon(imageIcon);
    }
    
    public void clearDice() {
        setIcon(null);
    }
}
