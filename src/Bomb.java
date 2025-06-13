import java.awt.Color;

/*
 * This code is protected under the Gnu General Public License (Copyleft), 2005 by
 * IBM and the Computer Science Teachers of America organization. It may be freely
 * modified and redistributed under educational fair use.
 */


public class Bomb extends GameObject {
	// Add any state variables here
	private final int DROP_SPEED = 18; // speed of drop
	private int bombType;
	private State currentState = State.DROPPING;
	private final int [] BOMB_VALUE = {-3, -5};
	public static final int TIME_BOMB = 0;
	
	
	public Bomb(int selectB, int width, int height){
		int selectBomb = selectB;
		setSize(width, height);
		
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
			setY(getY() + DROP_SPEED); // moves fruit down
		}
		else if (currentState == State.LANDED){
			
		}
	}
	
	// Add any additional methods here
	
	public void setState(State currentState){
		this.currentState = currentState;
	}
	
	public int getBombType(){
		return bombType;
	}
	
	public int getBombValue(){
		return BOMB_VALUE[bombType];
	}
	
	
}