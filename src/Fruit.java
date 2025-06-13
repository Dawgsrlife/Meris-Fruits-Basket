import java.awt.Color;

/*
 * This code is protected under the Gnu General Public License (Copyleft), 2005 by
 * IBM and the Computer Science Teachers of America organization. It may be freely
 * modified and redistributed under educational fair use.
 */


public class Fruit extends GameObject {
	// Add any state variables here
	private final int DROP_SPEED = 10; // speed of drop
	
	private State currentState = State.DROPPING;
	private final int [] FRUIT_POINTS = {3, 5, 7, 12, 30};
	private int selectFruit;
	private int fruitType;

	// Fruit Constructor
	public Fruit(int selectFruit, int width, int height){
		this.selectFruit = selectFruit;
		setSize(width, height); // sets the size of fruits
		assignFruitType();
		setColor(new Color(0, 0, 0, 0)); // Transparent background
	}
	/**
	 * Fill in this method with code that describes the behavior
	 * of a fruit from one moment to the next 
	 */
	public void act() {
		
		if (currentState == State.DROPPING){
			setY(getY() + DROP_SPEED); // moves fruit down
		}
		else if (currentState == State.LANDED){
			
		} 
	}
	
	// Add any additional methods here
	
	public void setState(State currentState){
		this.currentState = currentState;
	}
	
	public int getFruitPoint(){
		return FRUIT_POINTS[fruitType];
	}
	
	private void assignFruitType(){
		
		if (selectFruit >= 1 && selectFruit <= 5){
			fruitType = 4; // pomogranate
		}
		else if (selectFruit >= 6 && selectFruit <= 20){
			fruitType = 3; // pear
		}
		else if (selectFruit >= 21 && selectFruit <= 40){
			fruitType = 2; // watermelon
		}
		else if (selectFruit >= 41 && selectFruit <= 65){
			fruitType = 1; // orange
		}
		else if (selectFruit >= 66 && selectFruit <= 100){
			fruitType = 0; // apple
		}
	}
	
	public int getFruitType() {
		return fruitType;
	}
	
	
	
}