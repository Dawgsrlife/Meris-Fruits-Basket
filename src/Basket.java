import java.awt.Color;

/*
 * This code is protected under the Gnu General Public License (Copyleft), 2005 by
 * IBM and the Computer Science Teachers of America organization. It may be freely
 * modified and redistributed under educational fair use.
 */


public class Basket extends GameObject {
	// Add any state variables here
	private final int SHIFT_SPEED = 30; // speed of shift
	private BasketDirection currentDirection = BasketDirection.STOP;
	private boolean gameState = true;
	
    public Basket(int width, int height) {
    	setSize(width, height);
    	setColor(new Color(78, 235, 40)); // Transparent background
    	}
    
	/**
	 * Fill in this method with code that describes the behavior
	 * of a basket from one moment to the next 
	 */
	public void act() {

		if (gameState) {
			if (currentDirection == BasketDirection.LEFT) {
				setX(getX() - SHIFT_SPEED);
				currentDirection = BasketDirection.STOP;
			} else if (currentDirection == BasketDirection.RIGHT) {
				setX(getX() + SHIFT_SPEED);
				currentDirection = BasketDirection.STOP;
			}
		}
		
	}
	
	// Add any additional methods here
	public void setDirection(BasketDirection direction) {
	        this.currentDirection = direction;

	}
	
	public BasketDirection getDirection() {
		return currentDirection;
	}
	
	public void setBasketState(boolean gameState) {
		this.gameState = gameState;
	}
	
}