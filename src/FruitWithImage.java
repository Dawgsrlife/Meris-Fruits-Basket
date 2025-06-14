import javax.swing.ImageIcon;

/**
 * A wrapper class that associates a {@link Fruit} object with its corresponding {@link ImageIcon} image.
 * <p>
 * This class is used to manage both the logical and visual components of a fruit in the game.
 * The {@code Fruit} contains game-related logic and state, while the {@code ImageIcon} displays
 * the image of the fruit on the screen.
 * </p>
 *
 * <p>
 * This class simplifies bomb management in the game loop by keeping associated data together.
 * </p>
 * 
 * @author Meredith Meng
 * @version June 13, 2025
 */
public class FruitWithImage {
    // Defensive: Only draw if icon is not null in all usages.
    public Fruit fruit;
    public ImageIcon fruitIcon;
    public double vx = 0;
    public double vy = 0;

    public FruitWithImage(Fruit fruit, ImageIcon fruitIcon) {
        this.fruit = fruit;
        this.fruitIcon = fruitIcon;
        this.vx = (Math.random() - 0.5) * 0.1; // -0.05 to +0.05 px/frame
        this.vy = 0; // Start with no vertical velocity
    }
}