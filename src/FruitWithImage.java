import javax.swing.JLabel;

/**
 * A wrapper class that associates a {@link Fruit} object with its corresponding {@link JLabel} image.
 * <p>
 * This class is used to manage both the logical and visual components of a fruit in the game.
 * The {@code Fruit} contains game-related logic and state, while the {@code JLabel} displays
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
    Fruit fruit;
    JLabel fruitImage;

    FruitWithImage(Fruit fruit, JLabel fruitImage) {
        this.fruit = fruit;
        this.fruitImage = fruitImage;
    }
}