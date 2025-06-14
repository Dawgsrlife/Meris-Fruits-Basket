import javax.swing.JLabel;

/**
 * A wrapper class that pairs a {@link Bomb} object with its corresponding {@link JLabel} image.
 * <p>
 * This class is used to manage both the logical and visual components of a bomb in the game.
 * The {@code Bomb} handles game logic (position, type, state), while the {@code JLabel} displays
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
    Bomb bomb;
    JLabel bombImage;

    public BombWithImage(Bomb bomb, JLabel bombImage) {
        this.bomb = bomb;
        this.bombImage = bombImage;
    }
}