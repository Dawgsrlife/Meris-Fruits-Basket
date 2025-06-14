import java.awt.Color;

/*
 * This code is protected under the Gnu General Public License (Copyleft), 2005 by
 * IBM and the Computer Science Teachers of America organization. It may be freely
 * modified and redistributed under educational fair use.
 */

/**
 * Represents a falling fruit object in the game. Each fruit has a specific type,
 * associated point value, and drops at a constant speed. This class extends {@link GameObject}
 * and handles the fruit's state and type assignment.
 * <p>
 * Fruit types are assigned based on a selection integer and are associated with a fixed array of point values.
 * </p>
 * @author Meredith Meng
 * @version June 13, 2025
 */
public class Fruit extends GameObject {

	private final int DROP_SPEED = 8; // speed of drop
	private State currentState = State.DROPPING;
	private final int[] FRUIT_POINTS = { 3, 4, 7, 12, 30 };
	private int selectFruit;
	private int fruitType;

	// Fruit Constructor
	public Fruit(int selectFruit, int width, int height) {
		this.selectFruit = selectFruit;
		setSize(width, height); // sets the size of fruits
		assignFruitType();
		setColor(new Color(0, 0, 0, 0)); // Transparent background
	}

	/**
	 * Fill in this method with code that describes the behavior of a fruit from one
	 * moment to the next
	 */
	public void act() {

		if (currentState == State.DROPPING) {
			setY(getY() + DROP_SPEED); // moves fruit down
		} else if (currentState == State.LANDED) {
		}
	}

	// Add any additional methods here
	
	/**
	 * Sets the current state of the fruit.
	 *
	 * @param currentState - the new state to assign to the fruit
	 */
	public void setState(State currentState) {
		this.currentState = currentState;
	}

	/**
	 * Returns the point value associated with the current fruit type.
	 *
	 * @return the number of points awarded when this fruit is collected
	 */
	public int getFruitPoint() {
		return FRUIT_POINTS[fruitType];
	}
	
	/**
	 * Determines the fruit type based on the randomly selected number
	 * passed into the constructor, and assigns it to the fruitType field.
	 */
	
	private void assignFruitType() {

		if (selectFruit >= 1 && selectFruit <= 5) {
			fruitType = 4; // pomogranate
		} else if (selectFruit >= 6 && selectFruit <= 20) {
			fruitType = 3; // pear
		} else if (selectFruit >= 21 && selectFruit <= 40) {
			fruitType = 2; // watermelon
		} else if (selectFruit >= 41 && selectFruit <= 65) {
			fruitType = 1; // orange
		} else if (selectFruit >= 66 && selectFruit <= 100) {
			fruitType = 0; // apple
		}
	}
	
	/**
	 * Returns the type of the fruit.
	 *
	 * @return an integer representing the fruit type
	 */
	public int getFruitType() {
		return fruitType;
	}

}