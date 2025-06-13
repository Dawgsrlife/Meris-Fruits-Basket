/**
 * @author Meredith Meng
 * June 13, 2025
 * ICS4UE-2 Mr. Benum
 * Final Project
 * Research Sources: 
 * 
 */


import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/*
 * This code is protected under the Gnu General Public License (Copyleft), 2005 by
 * IBM and the Computer Science Teachers of America organization. It may be freely
 * modified and redistributed under educational fair use.
 */

public class FruitsBasket extends Game {

	// JOption Screen Icon
	private final static ImageIcon FB_ICON = new ImageIcon("Basket Icon.png");
	
	// Offscreen locations
	private final int X_OFFSCREEN = -1000;
	private final int Y_OFFSCREEN = -1000;

	// Tracking variables
	private int score = 0;
	private int seconds = 60;
	
	// Fruit Dimensions
	private final int FRUIT_WIDTH = (int)(Game.screenWidth * 0.05); // 5% of screen width
	private final int FRUIT_HEIGHT = (int)(Game.screenHeight * 0.08); // 8% of screen height
	
	// Basket Dimensions
	private final int BASKET_WIDTH = (int)(screenWidth * 0.105); // 10.5% of screen width
	private final int BASKET_HEIGHT = (int)(screenHeight * 0.045); // 4.5% of screen height
	
	// Basket Location
	private final int BASKET_X = (int)(screenWidth - BASKET_WIDTH)/2; // Center
	private final int BASKET_Y = (int)(screenHeight * 0.86); // Bottom area

	// Bomb Dimensions
	private final int BOMB_WIDTH = (int)(Game.screenWidth * 0.07); // 7% of screen width
    private final int BOMB_HEIGHT = (int)(Game.screenHeight * 0.11); // 11% of screen height

	// Game Labels
	JLabel timeCount;
	JLabel scoreCount;
	
	Timer gameTime;
	
	// For Basket
	private JLabel basketImage;
	private Basket basket;
	
	// ImageIcons 
	private ImageIcon basketIm = new ImageIcon("basket.png");
	private ImageIcon appleIm = new ImageIcon("apple.png");
	private ImageIcon orangeIm = new ImageIcon("orange.png");
	private ImageIcon watermelonIm = new ImageIcon("watermelon.png");
	private ImageIcon pearIm = new ImageIcon("pear.png");
	private ImageIcon pomegranateIm = new ImageIcon("pomegranate.png");
	private ImageIcon bombIm = new ImageIcon("bomb.png");
	private ImageIcon tntIm = new ImageIcon("tnt.png");
	
	// Scaled Images
	final ImageIcon scaledBasketIm = scaleImage(basketIm, BASKET_WIDTH, BASKET_HEIGHT + 15);
	final ImageIcon scaledApple = scaleImage(appleIm, FRUIT_WIDTH, FRUIT_HEIGHT);
	final ImageIcon scaledOrange = scaleImage(orangeIm, FRUIT_WIDTH, FRUIT_HEIGHT);
	final ImageIcon scaledWatermelon = scaleImage(watermelonIm, FRUIT_WIDTH, FRUIT_HEIGHT);
	final ImageIcon scaledPear = scaleImage(pearIm, FRUIT_WIDTH, FRUIT_HEIGHT);
	final ImageIcon scaledPomegranate = scaleImage(pomegranateIm, FRUIT_WIDTH, FRUIT_HEIGHT);
	final ImageIcon scaledBomb = scaleImage(bombIm, BOMB_WIDTH, BOMB_HEIGHT);
	final ImageIcon scaledTnt = scaleImage(tntIm, BOMB_WIDTH, BOMB_HEIGHT);
	
	// ArrayLists
	ArrayList<FruitWithImage> fruits = new ArrayList<>();
	ArrayList<BombWithImage> bombs = new ArrayList<>();
	
	/**
	 * Fill in this method with code that tells the game what to do before
	 * actual play begins
	 */

	public void setup() {
		
		showStartupMessage(); // Show Game Instructions

		createJLabels(); // Creates timer and score JLabels

		// Initializes the timer
		gameTime = new Timer(800, e -> updateTimer());
		gameTime.start(); // Start the timer

		makeBasket();
		makeFruit();
	}

	/**
	 * Fill in this method with code that tells the playing field what to do
	 * from one moment to the next
	 */
	
	public void act() {
		// Speeds Up Game
		if (seconds >= 40 && seconds <= 60) {
			setDelay(15);
		} else if (seconds >= 20 && seconds <= 39) {
			setDelay(12);
		} else if (seconds >= 0 && seconds <= 19) {
			setDelay(10);
		}

		// Collecting Fruits
		for (int i = 0; i < fruits.size(); i++) {
			Fruit currentFruit = fruits.get(i).fruit;
			JLabel currentFruitImage = fruits.get(i).fruitImage;
			
			// Checks for collison and places object off screen
		    if (currentFruit.collides(basket)) {
		        currentFruit.setState(State.LANDED);
		        currentFruit.setLocation(X_OFFSCREEN, Y_OFFSCREEN);
		        currentFruitImage.setLocation(X_OFFSCREEN, Y_OFFSCREEN);
		        updateScore(currentFruit);
		    }
		}
		// Collecting Bombs
		for (int i = 0; i < bombs.size(); i++) {
			Bomb currentBomb = bombs.get(i).bomb;
			JLabel currentBombImage = bombs.get(i).bombImage;
			
			// Checks for collison and places object off screen
			if (currentBomb.collides(basket)) {
				currentBomb.setState(State.LANDED);
				currentBomb.setLocation(X_OFFSCREEN, Y_OFFSCREEN);
				currentBombImage.setLocation(X_OFFSCREEN, Y_OFFSCREEN);
				bombEffect(currentBomb);
			}
		}

		// Bring fruit back to top if it goes out of bounds
		for (int i = 0; i < fruits.size(); i++) {
			Fruit currentFruit = fruits.get(i).fruit;

			// Check Y-value of current fruit
			if (currentFruit.getY() >= Game.screenHeight) {
				 resetFruit(currentFruit);
				 visualEffects(-currentFruit.getFruitPoint(), currentFruit);
				 
				 // Deduct score by the amount the dropped fruit is worth
				 if (score - currentFruit.getFruitPoint() >= 0){
					 score -= currentFruit.getFruitPoint();
				 }
				 else{ // Sets score to 0 if the deduction causes a negative score
					score = 0;
				 }
				 scoreCount.setText("Score: " + score); // Update Jlabel
			}
		}
		
		// Makes the JLabel Images move immedately with their corresponding object
		SwingUtilities.invokeLater(() -> {
			
			// Moves basket image with basket object immedately 
		    basketImage.setLocation(basket.getX(), basket.getY() - 12);
		    
		    // Moves fruit image with fruit object immedately 
		    for (int i = 0; i < fruits.size(); i++) {
			    Fruit currentFruit = fruits.get(i).fruit;
			    JLabel currentFruitImage = fruits.get(i).fruitImage;
			    currentFruitImage.setLocation(currentFruit.getX(), currentFruit.getY());
			}
		    
		    // Moves bomb image with bomb object immedately 
		    for (int i = 0; i < bombs.size(); i++){
		    	Bomb currentBomb = bombs.get(i).bomb;
				JLabel currentBombImage = bombs.get(i).bombImage;
				currentBombImage.setLocation(currentBomb.getX(), currentBomb.getY());
		    }
		});

		// Left and Right Keys for Basket movement
		if (ZKeyPressed() && basket.getX() >= 0) {
			basket.setDirection(BasketDirection.LEFT);
			System.out.println(basket.getDirection());
		}
		if (XKeyPressed() && basket.getX() + BASKET_WIDTH + 20 < Game.screenWidth) {
			basket.setDirection(BasketDirection.RIGHT);
			System.out.println(basket.getDirection());
		}
	}

	// Add any additional methods here

	private void visualEffects(int change, GameObject fruitOrBomb) {
		JLabel visualLabel = new JLabel();
		visualLabel.setFont(new Font("Sans-serif", Font.BOLD, 18));
		visualLabel.setForeground(change > 0 ? Color.GREEN : Color.RED);
		visualLabel.setText((change > 0 ? "+" : "") + (change == -5 ? change + " seconds" : change + " points"));
		
		visualLabel.setSize(150, 30);

		int xPos = fruitOrBomb.getX() + fruitOrBomb.getWidth() / 2;
//		int yPos = basket.getY() - 30;

//		int xPos = basket.getX() + basket.getWidth() / 2;
		int yPos = basket.getY() - 30;

		visualLabel.setLocation(xPos, yPos);
		
		add(visualLabel);
	    repaint();
	    revalidate();
	    
	    // Use Swing Timer to remove the label after 1 second
	    new javax.swing.Timer(1000, e -> {
	        remove(visualLabel);
	        repaint();
	        revalidate();
	        ((javax.swing.Timer)e.getSource()).stop();}).start();
	}
	
	/**
	 * Scales an ImageIcon to the specified width and height using smooth scaling
	 *
	 * @param icon - the original ImageIcon to be scaled
	 * @param width - the desired width of the scaled image
	 * @param height - the desired height of the scaled image
	 * @return a new ImageIcon containing the scaled image
	 */
	
	private ImageIcon scaleImage(ImageIcon icon, int width, int height) {
	    Image img = icon.getImage();
	    Image scaled = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
	    return new ImageIcon(scaled);
	}
	
	/**
	 * Creates and initializes the basket object along with its image label,
	 * sets their sizes and locations, and adds them to the game panel.
	 */
	
	private void makeBasket() {
        
        // Make basket object
		basket = new Basket(BASKET_WIDTH, BASKET_HEIGHT);
		basket.setLocation(BASKET_X, BASKET_Y);
	
		// Set up the JLabel containing the image of the basket
        basketImage = new JLabel(scaledBasketIm);
        basketImage.setSize(BASKET_WIDTH, BASKET_HEIGHT + 30);

        add(basketImage);
		add(basket);
	}

	/**
	 * Creates and initializes the JLabels for displaying the timer and score,
	 * sets their fonts, colors, and positions, then adds them to the game panel.
	 */
	
	private void createJLabels() {
		// Timer JLabel
		timeCount = new JLabel();
		timeCount.setForeground(Color.black);
		timeCount.setBounds(Game.screenWidth - 100, 5, 100,100); // Adjust size as needed
		timeCount.setFont(new Font("Sans-serif", Font.BOLD, (int) (Game.screenWidth * 0.02)));
		add(timeCount);
		repaint();

		// Score JLabel
		scoreCount = new JLabel();
		scoreCount.setForeground(Color.black);
		scoreCount.setBounds(50, 10, 500, 100); // Adjust size as needed
		scoreCount.setFont(new Font("Sans-serif", Font.BOLD, (int) (Game.screenWidth * 0.02)));
		scoreCount.setText("Score: 0");
		add(scoreCount);
		repaint();
	}

	/**
	 * Updates the game score by adding the point value of the collected fruit
	 * and refreshes the score display label.
	 *
	 * @param currentFruit - the Fruit object that was collected
	 */
	
	private void updateScore(Fruit currentFruit) {
		score += currentFruit.getFruitPoint();
		scoreCount.setText("Score: " + score);
		visualEffects(currentFruit.getFruitPoint(), currentFruit);
	}

	/**
	 * Applies the effect of a bomb on the game state by adjusting either the timer
	 * or the score depending on the bomb type. Ensures that time and score do not
	 * go below zero.
	 *
	 * @param currentBomb - the Bomb object whose effect is to be applied
	 */
	
	private void bombEffect(Bomb currentBomb) {

		if (currentBomb.getBombType() == Bomb.TIME_BOMB) { // time bomb
			if (seconds + currentBomb.getBombValue() < 0) { // checks that time doesn't go negative
				seconds = 0;
			} else {
				seconds += currentBomb.getBombValue();
				visualEffects(currentBomb.getBombValue(), null);
			}
			timeCount.setText(seconds + "");

		} else { // score bomb
			if (score + currentBomb.getBombValue() < 0) { // checks that score doesn't go negative
				score = 0;
			} else {
				score += currentBomb.getBombValue();
				visualEffects(currentBomb.getBombValue(), null);
			}
			scoreCount.setText("Score: " + score);
		}
	}

	/**
	 * Updates the countdown timer each second, refreshes the timer display,
	 * randomly triggers creation of fruits and bombs based on the remaining time,
	 * and stops the game when the timer reaches zero.
	 */
	
	private void updateTimer() {

		// Updates time left
		if (seconds > 0) {
			seconds--;
			timeCount.setText(seconds + "");
		}
		
		// Choose random times to generate fruit and bomb objects
		int randomFruitTime = (int) (Math.random() * 2 + 1);
		int randomBombTime = (int) (Math.random() * 5 + 3);

		if (seconds % randomFruitTime == 0) {
			makeFruit();
		}
		if (seconds % randomBombTime == 0) {
			makeBomb();
		}

		// Stops the timer when time is up and ends the game
		if (seconds <= 0) {
			gameTime.stop();
			endsGame();
		}

	}
	
	/**
	 * Creates a new Fruit object with a random type and horizontal position,
	 * sets up its corresponding scaled image JLabel, positions them at the top
	 * of the screen, adds both components to the game panel, and stores them
	 * in the fruits list for tracking
	 */
	
	private void makeFruit() {

		// Generates a random X coordinate
		Random r = new Random();
		int randomX = r.nextInt(Game.screenWidth - FRUIT_WIDTH);
		
		int rFruit = (int) (Math.random() * 100 + 1); // Choose a random fruit [1, 100]
		
		// Make Fruit Object
		Fruit randomFruit = new Fruit(rFruit, FRUIT_WIDTH, FRUIT_HEIGHT);
		randomFruit.setLocation(randomX, 0);
		
		// Get fruit type
		int fruitType = randomFruit.getFruitType();
		ImageIcon scaledFruit = null;
		
		// Select the scaled image
		switch (fruitType){
			case 0: scaledFruit = scaledApple; break;
			case 1: scaledFruit = scaledOrange; break;
			case 2: scaledFruit = scaledWatermelon; break;
			case 3: scaledFruit = scaledPear; break;
			case 4: scaledFruit = scaledPomegranate; break;
		}
		
		// Make fruit image jLabel
		JLabel fruitImage = new JLabel(scaledFruit);
		fruitImage.setSize(FRUIT_WIDTH, FRUIT_HEIGHT);
		fruitImage.setLocation(randomFruit.getX(), randomFruit.getY());
		
		// Add to game screen
		add(fruitImage);
		add(randomFruit);
		
	    fruits.add(new FruitWithImage(randomFruit, fruitImage)); // add to arraylist
	}

	/**
	 * Creates a new Bomb object with a random type and horizontal position,
	 * sets up its corresponding scaled image JLabel, positions them at the top
	 * of the screen, adds both components to the game panel, and stores them
	 * in the bombs list for tracking.
	 */
	
	private void makeBomb() {
		Random r = new Random();
		int randomX = r.nextInt(Game.screenWidth - BOMB_WIDTH);

		int rbomb = (int) (Math.random() * 10 + 1); // [1, 10]
		
		Bomb randomBomb = new Bomb(rbomb, BOMB_WIDTH, BOMB_HEIGHT);
		randomBomb.setLocation(randomX, 0);
		
		// Get bomb type
		int bombType = randomBomb.getBombType();
		ImageIcon scaledB = null;
		
		if (bombType == 0){
			scaledB = scaledBomb;
		} else if (bombType == 1){
			scaledB = scaledTnt;
		}
		
		// Make bomb image jLabel
		JLabel bombImage = new JLabel(scaledB);
		bombImage.setSize(BOMB_WIDTH, BOMB_HEIGHT);
		bombImage.setLocation(randomBomb.getX(), randomBomb.getY());
				
		add(bombImage);
		add(randomBomb);
		
		bombs.add(new BombWithImage(randomBomb, bombImage)); // add to arraylist
	}

	/**
	 * Resets the position of the given fruit to the top of the screen at a random horizontal (X) coordinate.
	 *
	 * @param currentFruit the Fruit object to be repositioned
	 */
	
	private void resetFruit(Fruit currentFruit) {
		int randomX;
		Random r = new Random();
		randomX = r.nextInt(Game.screenWidth - FRUIT_WIDTH); // generates a random X coordinate
		currentFruit.setLocation(randomX, 0);
	}
	
	/**
	 * Displays the startup instructions and game information in a centered,
	 * formatted JOptionPane dialog. The message includes how to play, scoring,
	 * penalties, and a warm welcome to the player. Offers "Enter" to start or
	 * "Exit" to quit the game.
	 * <p>
	 * If the user selects "Exit", the application will terminate.
	 */
	
	private void showStartupMessage() {

		String title = "Fruits Basket";
		String message = "<html><div style='text-align: center; width: 400px;'>"
				+ "<h1><u>Welcome to Fruits Basket!</u></h1>" + "<h2>How To Play</h2>"
				+ "<p>Random fruits will be falling from the top. Collect them with the basket below."
				+ "<br>Use the 'z' and 'x' keys to move the basket left and right. Avoid the bombs"
				+ "<br>that fall alongside the fruits. Uncollected fruits will be sent back to the"
				+ "<br>top and you will get a score penality. Try to finish the game without overwhelming"
				+ "<br>yourself in a mountain of falling fruits. ✧⁺⸜(･ ᗜ ･ )⸝⁺✧</p>"

				+ "<h2>Scoring & Penalties:</h2>" + "<table align='center'>"
				+ "<tr><td><b>TNT Bomb:</b> -5 points</td></tr>" + "<tr><td><b>Normal Bomb:</b> -3 seconds</td></tr>"
				+ "</table>"

				+ "<h2>Fruit Points:</h2>" + "<table align='center'>" + "<tr><td>Apple: 3 points</td></tr>"
				+ "<tr><td>Orange: 5 points</td></tr>" + "<tr><td>Watermelon: 7 points</td></tr>"
				+ "<tr><td>Pear: 12 points</td></tr>" + "<tr><td>Pomegranate:&nbsp;30&nbsp;points</td></tr>"
				+ "</table>" + "<br>" + "<p>Try to collect as many fruits as you can. "
				+ "You have 60 seconds, good luck! ( ๑ ˃̵ᴗ˂̵)و </p>" + "<br><b>Click Enter to Start the Game</b>"
				+ "</div></html>";

		int option = JOptionPane.showOptionDialog(null, message, title, JOptionPane.YES_NO_OPTION,
				JOptionPane.INFORMATION_MESSAGE, FB_ICON, new Object[] { "Enter", "Exit" }, "Yes");

		if (option == JOptionPane.NO_OPTION) {
			System.exit(0); // Handle the case where the user chooses not to start the game
		}
	}
		
	/**
	 * Ends the current game by stopping all fruit and bomb movement,
	 * removing the basket from the screen, and showing a "Game Over" dialog
	 * with the final score and an option to play again or exit.
	 * 
	 * If the player chooses to play again, the game is reset.
	 * If the player chooses to exit, the application terminates.
	 */
	
	private void endsGame() {
		
		// Stop movement
		for (int i = 0; i < fruits.size(); i++) {
			Fruit currentFruit = fruits.get(i).fruit;
			currentFruit.setState(State.LANDED);
		}

		for (int i = 0; i < bombs.size(); i++) {
			Bomb currentBomb = bombs.get(i).bomb;
			currentBomb.setState(State.LANDED);
		}

		remove(basket);
		remove(basketImage);

		String title = "Game Over!";
		String message = "<html><div style='text-align: center; width: 220px;'>" + "<h1>Your Score: " + score + "</h1>"
				+ "<br><br><h2>Play Again?</h2>" + "</div></html>";

		int option = JOptionPane.showOptionDialog(null, message, title, JOptionPane.YES_NO_OPTION,
				JOptionPane.INFORMATION_MESSAGE, FB_ICON, // Fruits Basket Image
				new Object[] { "Yes", "No" }, "Yes");

		if (option == JOptionPane.NO_OPTION) {
			System.exit(0); // Exists game
		} else if (option == JOptionPane.YES_OPTION) {
			resetGame(); // Prepares for new game
		}
	}
	
	/**
	 * Resets the game state to start a new game session.
	 */
	
	private void resetGame() {
		
		// Resets game variables
	    score = 0;
	    seconds = 8;
	    scoreCount.setText("Score: 0");
		disableZKey();
		disableXKey();
	    
	    // Remove all fruit and their Jlabels from the screen
	    for (FruitWithImage fwi : fruits) {
	        remove(fwi.fruit);
	        remove(fwi.fruitImage);
	    }
	    
	    fruits.clear(); // clear the array list

	    // Remove all bombs and their Jlabels from the screen
	    for (BombWithImage bwi : bombs) {
	        remove(bwi.bomb); 
	        remove(bwi.bombImage); 
	    }
	    
	    bombs.clear(); // clear the array list

	    // Refresh the screen after removing components
	    repaint();
	    revalidate();
	    
	    makeBasket();
		
	    // Restart the timer
	    gameTime.start();

	    // Create the initial fruit
	    makeFruit();

	}
	
	
	/**
	 * This code has been provided for you, and should not be modified
	 */
	public static void main(String[] args) {
		FruitsBasket p = new FruitsBasket();
		p.setVisible(true);
		p.initComponents();
	}
}