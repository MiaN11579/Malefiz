import java.awt.*;
import javax.swing.*;

public class RoundButton extends JButton {

  private int xcoord, ycoord;
  
  /**
   * The active status of this button. When a button is active, the border is
   * black and the button can be pressed. An inactive button has a white border
   * and cannot be pressed.
   */
  private boolean active;
  
  /**
   * The highlight status of this button. The button is highlighted with a
   * green ring.
   */
  private boolean highlighted;
  
  /**
   * True if this button is part of the player houses, false otherwise.
   */
  private boolean base;
  
  /**
   * The color of the ring around this button when highlighted. Green by default.
   */
  private Color highlightColor;
  

  public RoundButton(int xcoord, int ycoord) {

    // Round the button
    Dimension size = getPreferredSize();
    size.width = size.height = Math.max(size.width,size.height);
    setPreferredSize(size);
    setForeground(Color.white);

    this.setSize(30,30);
    this.xcoord = xcoord;
    this.ycoord = ycoord;

    // Delete the border
    setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
    setContentAreaFilled(false);
    
    highlightColor = Color.GREEN;
  }

  // Color the button
  protected void paintComponent(Graphics g) 
  {
    Graphics2D g2 = (Graphics2D) g;
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    if (getModel().isRollover()) {
      g2.setColor(Color.lightGray);;
    } else {
      g2.setColor(getBackground());
    }
    
    g2.fillOval(0, 0, getSize().width-3, getSize().height-3);

    super.paintComponent(g2);
  }

  // Color the border
  protected void paintBorder(Graphics g) {
    Graphics2D g2 = (Graphics2D) g;
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setColor(getForeground());
    g2.setStroke(new BasicStroke(2));
    g2.drawOval(0, 0, getSize().width-3, getSize().height-3);
  }
  
  /**
   * Set the active value of this button. An active button is clickable and has
   * a black or highlighted border. An inactive button is completely white.
   * 
   * @param value The new active value of this button.
   */
  public void setActive(boolean value) {
      if (value) {
          if (highlighted) {
              setForeground(highlightColor);
          } else {
              setForeground(Color.BLACK);
          }
          
          setEnabled(true);
      } else {
          setForeground(Color.WHITE);
          setEnabled(false);
      }
      this.active = value;
  }
  
  /**
   * Sets the highlighted value of this buton. A highlighted button has a colored
   * border when active, a non-highlighted button has a black border when active.
   * 
   * @param value The new highlighted value of this button.
   */
  public void setHighlighted(boolean value) {
      if (active) {
          if (value) {
              setForeground(highlightColor);
          } else {
              setForeground(Color.BLACK);
          }
      }
      this.highlighted = value;
  }
  
  /**
   * Sets the color of this button's border when highlighted.
   * 
   * @param color The new highlight color.
   */
  public void setHighlightColor(Color color) {
      this.highlightColor = color;
      if (highlighted) {
          setForeground(highlightColor);
      }
  }
  
  /**
   * 
   */
  public void setInBase(boolean value) {
      this.base = value;
  }
  
  /**
   * 
   * @return
   */
  public boolean inBase() {
      return base;
  }

  /**
   * Get if this button is active or not.
   * 
   * @return True if this button is currently active, false otherwise.
   */
  public boolean isActive() {
      return this.active;
  }
  
  public int getXcoord() {
    return xcoord;
  }

  public int getYcoord() {
    return ycoord;
  }
}