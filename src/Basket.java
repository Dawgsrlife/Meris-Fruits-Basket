import java.awt.Color;

/*
 * This code is protected under the Gnu General Public License (Copyleft), 2005 by
 * IBM and the Computer Science Teachers of America organization. It may be freely
 * modified and redistributed under educational fair use.
 */
/**
 * Represents the player's basket used to catch falling fruits in the FruitsBasket game.
 * <p>
 * The Basket is a transparent {@code GameObject} that moves horizontally across the screen 
 * based on player input. Movement is controlled using a shift speed and a direction enum.
 * </p>
 *
 * @author Meredith Meng
 * @version June 13, 2025
 */

public class Basket extends GameObject {

	private final int SHIFT_SPEED = 30; // speed of basket shift
	private BasketDirection currentDirection = BasketDirection.STOP;

	public Basket(int width, int height) {
		setSize(width, height);
		setColor(new Color(0, 0, 0, 0)); // Transparent background
	}

	/**
	 * Fill in this method with code that describes the behavior of a basket from
	 * one moment to the next
	 */

	public void act() {

		if (currentDirection == BasketDirection.LEFT) {
			setX(getX() - SHIFT_SPEED);
			currentDirection = BasketDirection.STOP; // stops basket after going left
		} else if (currentDirection == BasketDirection.RIGHT) {
			setX(getX() + SHIFT_SPEED);
			currentDirection = BasketDirection.STOP; // stops basket after going right
		}
	}

	// Add any additional methods here

	/**
	 * Sets the current movement direction of the basket
	 * 
	 * @param direction - the direction to set for the basket movement;
	 */
	public void setDirection(BasketDirection direction) {
		this.currentDirection = direction;
	}

	/**
	 * Retrieves the current movement direction of the basket.
	 * 
	 * @return the current BasketDirection of the basket
	 */
	public BasketDirection getDirection() {
		return currentDirection;
	}

}