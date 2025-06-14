import javax.swing.ImageIcon;

/**
 * A wrapper class that pairs a {@link Bomb} object with its corresponding {@link ImageIcon} image.
 * <p>
 * This class is used to manage both the logical and visual components of a bomb in the game.
 * The {@code Bomb} handles game logic (position, type, state), while the {@code ImageIcon} displays
 * the image of the bomb on the screen.
 * </p>
 * 
 * <p>
 * This class simplifies bomb management in the game loop by keeping associated data together.
 * </p>
 * 
 * @author Meredith Meng
 * @version June 13, 2025
 */
public class BombWithImage {
    public Bomb bomb;
    public ImageIcon bombIcon;
    public double vx = 0;
    public double vy = 0;

    public BombWithImage(Bomb bomb, ImageIcon bombIcon) {
        this.bomb = bomb;
        this.bombIcon = bombIcon;
        this.vx = (Math.random() - 0.5) * 0.1; // -0.05 to +0.05 px/frame
        this.vy = 0; // Start with no vertical velocity
    }
}