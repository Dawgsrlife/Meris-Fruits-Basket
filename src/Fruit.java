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
	private State currentState = State.FALLING;
	private final int[] FRUIT_POINTS = { 3, 4, 7, 12, 30 };
	private int selectFruit;
	private int fruitType;
	private int speed = DROP_SPEED;

	// Fruit Constructor
	public Fruit(int selectFruit, int width, int height) {
		this.selectFruit = selectFruit;
		setSize(width, height); // sets the size of fruits
		assignFruitType();
		setColor(new Color(0, 0, 0, 0)); // Transparent background
	}
	
	// Default constructor for boss fruit
	public Fruit() {
		this.selectFruit = 1; // Default to pomegranate
		setSize(50, 50); // Default size
		assignFruitType();
		setColor(new Color(0, 0, 0, 0));
	}

	/**
	 * Fill in this method with code that describes the behavior of a fruit from one
	 * moment to the next
	 */
	public void act() {
		// Movement handled by FruitsBasket.updateGame()
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
	 * Gets the current state of the fruit.
	 *
	 * @return the current state of the fruit
	 */
	public State getState() {
		return currentState;
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
	 * Gets the points value of the fruit.
	 *
	 * @return the points value
	 */
	public int getPoints() {
		return getFruitPoint();
	}
	
	/**
	 * Gets the current speed of the fruit.
	 *
	 * @return the current speed
	 */
	public int getSpeed() {
		return speed;
	}
	
	/**
	 * Sets the speed of the fruit.
	 *
	 * @param speed the new speed value
	 */
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	/**
	 * Sets the width of the fruit.
	 *
	 * @param width the new width
	 */
	public void setWidth(int width) {
		setSize(width, getHeight());
	}
	
	/**
	 * Sets the height of the fruit.
	 *
	 * @param height the new height
	 */
	public void setHeight(int height) {
		setSize(getWidth(), height);
	}
	
	/**
	 * Determines the fruit type based on the randomly selected number
	 * passed into the constructor, and assigns it to the fruitType field.
	 */
	
	private void assignFruitType() {
		if (selectFruit >= 1 && selectFruit <= 5) {
			fruitType = 4; // pomegranate
		} else if (selectFruit >= 6 && selectFruit <= 20) {
			fruitType = 3; // pear
		} else if (selectFruit >= 21 && selectFruit <= 40) {
			fruitType = 2; // watermelon
		} else if (selectFruit >= 41 && selectFruit <= 65) {
			fruitType = 1; // orange
		} else if (selectFruit >= 66 && selectFruit <= 100) {
			fruitType = 0; // apple
		} else {
			fruitType = 0; // default to apple if out of range
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