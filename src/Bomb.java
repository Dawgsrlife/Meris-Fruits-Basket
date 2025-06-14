import java.awt.Color;

/*
 * This code is protected under the Gnu General Public License (Copyleft), 2005 by
 * IBM and the Computer Science Teachers of America organization. It may be freely
 * modified and redistributed under educational fair use.
 */

/**
 * Represents a bomb object in the FruitsBasket game.
 * <p>
 * Bombs fall from the top of the screen and negatively affect the player's score 
 * when caught. There are two types of bombs: regular bombs and score bombs, each 
 * with different penalties. Bombs are transparent and inherit size and position
 * handling from the {@code GameObject} superclass.
 * 
 * @author Meredith Meng
 * @version June 13, 2025
 */
public class Bomb extends GameObject {

	private final int DROP_SPEED = 12; // speed of drop
	private int bombType;
	private State currentState = State.DROPPING;
	private final int [] BOMB_VALUE = {-3, -5};
	
	public Bomb(int selectB, int width, int height){
		int selectBomb = selectB;
		setSize(width, height);
		
		// Assign bomb type
		if (selectBomb >= 1 && selectBomb <= 5){
			bombType = 0;
		}else{
			bombType = 1;
		}
		setColor(new Color(0, 0, 0, 0)); // Transparent background
	}
	
	/**
	 * Fill in this method with code that describes the behavior
	 * of a fruit from one moment to the next 
	 */
	public void act() {
		
		if (currentState == State.DROPPING){
			setY(getY() + DROP_SPEED); // moves bomb down
		}
		else if (currentState == State.LANDED){
		}
	}
	
	// Add any additional methods here
	
	/**
	 * Sets the current state of the bomb object.
	 * @param currentState - the new state to assign to the bomb
	 */
	public void setState(State currentState){
		this.currentState = currentState;
	}
	
	/**
	 * Returns the bomb's type
	 * @return an integer representing the bomb type
	 */
	public int getBombType(){
		return bombType;
	}
	
	/**
	 * Retrieves the value associated with the bomb's effect.
	 * @return the bomb's effect value from the BOMB_VALUE array, based on its type
	 */
	public int getBombValue(){
		return BOMB_VALUE[bombType];
	}
	
}