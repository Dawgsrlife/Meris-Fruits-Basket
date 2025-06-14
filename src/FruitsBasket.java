
/**
 * @author Meredith Meng
 * June 13, 2025
 * ICS4UE-2 Mr. Benum
 * Final Project
 * Sources: 
 * https://www.youtube.com/watch?v=JjkgKAE8FDU&t=20s
 * https://www.youtube.com/watch?v=4PfDdJ8GFHI
 * https://stackoverflow.com/questions/21375255/jpanel-positions-and-sizes-changes-according-to-screensize
 * https://stackoverflow.com/questions/5921175/how-to-set-jpanels-width-and-height
 * https://stackoverflow.com/questions/5731850/how-to-create-wrapper-class-for-any-user-defined-class
 * https://docs.google.com/document/d/1yA7i-QDWTSI3xue3K9XEvzlCRnHBcVZ8PRto2FkacJk/edit?tab=t.0#heading=h.yq5pdfj61b6j
 * https://htmlcolorcodes.com/color-picker/
 * https://stackoverflow.com/questions/12020597/java-convert-image-to-icon-imageicon
 * https://stackoverflow.com/questions/1097366/java-swing-revalidate-vs-repaint
 * https://www.w3schools.com/html/html_formatting.asp
 * https://www.youtube.com/watch?v=PLvIyoWPfmM
 * https://stackoverflow.com/questions/6567870/what-does-swingutilities-invokelater-do
 * https://www.youtube.com/watch?v=LRzf2JChudM
 * https://stackoverflow.com/questions/60952610/toolkit-getdefaulttoolkit-getscreensize-doesnt-get-correct-values-of-screen
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;
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

/**
 * The FruitsBasket class represents the main game logic for a timed fruit-catching game.
 * 
 * <p>Players use a basket to catch falling fruits while avoiding bombs.
 * The game rewards players with points for collecting different types of fruit,
 * and penalizes them for missing fruits or catching bombs.
 * 
 * <p>This class manages the initialization of game assets, user input, game state updates,
 * collision detection, scorekeeping, and the visual effects shown to the player.
 * 
 * <p>Gameplay involves:
 * <ul>
 *   <li>Moving the basket left and right with 'z' and 'x' keys.</li>
 *   <li>Catching fruits for points.</li>
 *   <li>Avoiding bombs which either deduct time or points.</li>
 *   <li>A countdown timer that ends the game at 0 seconds.</li>
 * </ul>
 * 
 * <p>Features:
 * <ul>
 *   <li>Dynamic screen scaling for consistent UI across screen sizes.</li>
 *   <li>Visual feedback for collected/missed items and time changes.</li>
 *   <li>Reset and replay functionality.</li>
 * </ul>
 *
 * @author Meredith Meng
 * @version 1.0
 * @since June 13, 2025
 */
public class FruitsBasket extends Game {

	// JOption Screen Icons
	private ImageIcon startScreenImage, endScreenImage;

	// Offscreen locations
	private final int X_OFFSCREEN = -1000;
	private final int Y_OFFSCREEN = -1000;

	// Tracking variables
	private int score = 0;
	private int seconds = 60;

	// Fruit Dimensions
	private final int FRUIT_WIDTH = (int) (Game.screenWidth * 0.05); // 5% of screen width
	private final int FRUIT_HEIGHT = (int) (Game.screenHeight * 0.08); // 8% of screen height

	// Basket Dimensions
	private final int BASKET_WIDTH = (int) (screenWidth * 0.105); // 10.5% of screen width
	private final int BASKET_HEIGHT = (int) (screenHeight * 0.045); // 4.5% of screen height

	// Basket Location
	private final int BASKET_X = (int) (screenWidth - BASKET_WIDTH) / 2; // Center
	private final int BASKET_Y = (int) (screenHeight * 0.86); // Bottom area

	// Bomb Dimensions
	private final int BOMB_WIDTH = (int) (Game.screenWidth * 0.07); // 7% of screen width
	private final int BOMB_HEIGHT = (int) (Game.screenHeight * 0.11); // 11% of screen height

	// Game Labels
	JLabel timeCount;
	JLabel scoreCount;
	
	private final int TYPE_TIME_BOMB = 1;

	// Game timer
	Timer gameTime;

	// For Basket
	private JLabel basketImage;
	private Basket basket;

	// ImageIcons
	private Image basketIm;
	private Image appleIm;
	private Image orangeIm;
	private Image watermelonIm;
	private Image pearIm;
	private Image pomegranateIm;
	private Image bombIm;
	private Image tntIm;

	// Scaled Images
	ImageIcon scaledBasketIm;
	ImageIcon scaledApple;
	ImageIcon scaledOrange;
	ImageIcon scaledWatermelon;
	ImageIcon scaledPear;
	ImageIcon scaledPomegranate;
	ImageIcon scaledBomb;
	ImageIcon scaledTnt;

	// ArrayLists
	private ArrayList<FruitWithImage> fruits = new ArrayList<>();
	private ArrayList<BombWithImage> bombs = new ArrayList<>();


	public void setupImage() throws IOException {

		// Gets images from the Image Resources folder
		basketIm = ImageIO.read(getClass().getResourceAsStream("basket.png"));
		appleIm = ImageIO.read(getClass().getResourceAsStream("apple.png"));
		orangeIm = ImageIO.read(getClass().getResourceAsStream("orange.png"));
		watermelonIm = ImageIO.read(getClass().getResourceAsStream("watermelon.png"));
		pearIm = ImageIO.read(getClass().getResourceAsStream("pear.png"));
		pomegranateIm = ImageIO.read(getClass().getResourceAsStream("pomegranate.png"));
		bombIm = ImageIO.read(getClass().getResourceAsStream("bomb.png"));
		tntIm = ImageIO.read(getClass().getResourceAsStream("tnt.png"));

		startScreenImage = new ImageIcon(getClass().getResource("/Basket Icon.png"));
		endScreenImage = new ImageIcon(getClass().getResource("/bomb friends.png"));

		// Scales images to appropriate sizes
		scaledBasketIm = scaleImage(basketIm, BASKET_WIDTH, BASKET_HEIGHT + 15);
		scaledApple = scaleImage(appleIm, FRUIT_WIDTH, FRUIT_HEIGHT);
		scaledOrange = scaleImage(orangeIm, FRUIT_WIDTH, FRUIT_HEIGHT);
		scaledWatermelon = scaleImage(watermelonIm, FRUIT_WIDTH, FRUIT_HEIGHT);
		scaledPear = scaleImage(pearIm, FRUIT_WIDTH, FRUIT_HEIGHT);
		scaledPomegranate = scaleImage(pomegranateIm, FRUIT_WIDTH, FRUIT_HEIGHT);
		scaledBomb = scaleImage(bombIm, BOMB_WIDTH, BOMB_HEIGHT);
		scaledTnt = scaleImage(tntIm, BOMB_WIDTH, BOMB_HEIGHT);
	}

	/**
	 * Fill in this method with code that tells the game what to do before actual
	 * play begins
	 */

	public void setup() {
		
		// Get images set up
		try {
			setupImage();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		setDelay(15);
		showStartupMessage(); // Show Game Instructions
		createJLabels(); // Creates timer and score JLabels

		// Initializes the timer
		gameTime = new Timer(800, e -> updateTimer());
		gameTime.start(); // Start the timer

		makeBasket();
		makeFruit();
	}

	/**
	 * Fill in this method with code that tells the playing field what to do from
	 * one moment to the next
	 */

	public void act() {

		// Speeds Up Game
		if (seconds >= 50 && seconds <= 60) {
			setDelay(14);
		} else if (seconds >= 20 && seconds <= 49) {
			setDelay(12);
		} else if (seconds >= 0 && seconds <= 19) {
			setDelay(8);
		}
		
		// For Collecting Fruits
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
		
		// For Collecting Bombs
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

		// Brings fruit back to top if it goes out of bounds
		for (int i = 0; i < fruits.size(); i++) {
			Fruit currentFruit = fruits.get(i).fruit;

			// Checks Y-value of current fruit
			if (currentFruit.getY() >= Game.screenHeight) {
				resetFruit(currentFruit); // sends currentFruit back to the top
				
				// Displays a point deduction of the amount the dropped fruit is worth
				visualEffects(-currentFruit.getFruitPoint());

				// Deduct score by the amount the dropped fruit is worth
				if (score - currentFruit.getFruitPoint() >= 0) {
					score -= currentFruit.getFruitPoint();
				} else { // Sets score to 0 if the deduction causes a negative score
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
			for (int i = 0; i < bombs.size(); i++) {
				Bomb currentBomb = bombs.get(i).bomb;
				JLabel currentBombImage = bombs.get(i).bombImage;
				currentBombImage.setLocation(currentBomb.getX(), currentBomb.getY());
			}
		});

		// Left and Right Keys for Basket movement contained within the game screen
		if (ZKeyPressed() && basket.getX() >= 0) {
			basket.setDirection(BasketDirection.LEFT);
		}
		if (XKeyPressed() && basket.getX() + BASKET_WIDTH + 20 < Game.screenWidth) {
			basket.setDirection(BasketDirection.RIGHT);
		}
	}

	// Add any additional methods here

	/**
	 * Displays a temporary visual effect on the screen to indicate a change in
	 * score or time after collecting a fruit or hitting a bomb
	 *
	 * <p>
	 * The label appears near the basket with a green "+X pts" or red "-X pts"/"-X
	 * secs" depending on the nature of the change. It fades away after a short
	 * duration.
	 * </p>
	 *
	 * @param change the value indicating the change:
	 *               <ul>
	 *               <li>Positive values represent score increases/li>
	 *               <li>Negative values of -5 represent time loss</li>
	 *               <li>Other negative values represent score deductions</li>
	 *               </ul>
	 */
	private void visualEffects(int change) {
		JLabel visualLabel = new JLabel();
		visualLabel.setFont(new Font("Sans-serif", Font.BOLD, 18));
		// Sets text to green if change is pos and red if change is neg
		visualLabel.setForeground(change > 0 ? Color.GREEN : Color.RED);
		// Adds to text if the label applies to points or seconds
		visualLabel.setText((change > 0 ? "+" : "") + (change == -5 ? change + " secs" : change + " pts"));

		// Set the location of the label to where the basket was
		int xPos = basket.getX() + basket.getWidth() / 2;
		int yPos = basket.getY() - 30;

		visualLabel.setLocation(xPos, yPos);
		visualLabel.setSize(150, 30);
		add(visualLabel);
		
		repaint();
		revalidate();

		// Remove the label after 0.5 seconds
		new javax.swing.Timer(500, e -> {
			remove(visualLabel);
			repaint();
			revalidate();
			((javax.swing.Timer) e.getSource()).stop();
		}).start();
	}

	/**
	 * Scales an Image to the specified width and height using smooth scaling
	 *
	 * @param image  - the original image to be scaled
	 * @param width  - the desired width of the scaled image
	 * @param height - the desired height of the scaled image
	 * @return a new ImageIcon containing the scaled image
	 */
	private ImageIcon scaleImage(Image image, int width, int height) {
		Image scaled = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		return new ImageIcon(scaled);
	}

	/**
	 * Creates and initializes the basket object along with its image label, sets
	 * their sizes and locations, and adds them to the game panel.
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
	 * Creates and initializes the JLabels for displaying the timer and score, sets
	 * their fonts, colors, and positions, then adds them to the game panel.
	 */

	private void createJLabels() {
		// Timer JLabel
		timeCount = new JLabel();
		timeCount.setForeground(Color.black);
		timeCount.setBounds(Game.screenWidth - 100, 5, 100, 100);
		timeCount.setFont(new Font("Sans-serif", Font.BOLD, (int) (Game.screenWidth * 0.02)));
		add(timeCount);
		repaint();

		// Score JLabel
		scoreCount = new JLabel();
		scoreCount.setForeground(Color.black);
		scoreCount.setBounds(50, 10, 500, 100);
		scoreCount.setFont(new Font("Sans-serif", Font.BOLD, (int) (Game.screenWidth * 0.02)));
		scoreCount.setText("Score: 0");
		add(scoreCount);
		repaint();
	}

	/**
	 * Updates the game score by adding the point value of the collected fruit,
	 * refreshes the score display label, and shows the visual text of that.
	 *
	 * @param currentFruit - the Fruit object that was collected
	 */
	private void updateScore(Fruit currentFruit) {
		score += currentFruit.getFruitPoint();
		scoreCount.setText("Score: " + score);
		visualEffects(currentFruit.getFruitPoint());
	}

	/**
	 * Applies the effect of a bomb on the game state by adjusting either the timer
	 * or the score depending on the bomb type. Ensures that time and score do not
	 * go below zero.
	 *
	 * @param currentBomb - the Bomb object whose effect is to be applied
	 */
	private void bombEffect(Bomb currentBomb) {

		// Note: bomb values are stored as negative numbers
		
		if (currentBomb.getBombType() == TYPE_TIME_BOMB) { // time bomb
			if (seconds + currentBomb.getBombValue() < 0) { // checks that time doesn't go negative
				seconds = 0;
			} else {
				seconds += currentBomb.getBombValue();
				visualEffects(currentBomb.getBombValue()); // shows visual text
			}
			timeCount.setText(seconds + "");

		} else { // score bomb
			if (score + currentBomb.getBombValue() < 0) { // checks that score doesn't go negative
				score = 0;
			} else {
				score += currentBomb.getBombValue();
				visualEffects(currentBomb.getBombValue()); // shows visual text
			}
			scoreCount.setText("Score: " + score);
		}
	}

	/**
	 * Updates the countdown timer each second, refreshes the timer display,
	 * triggers creation of fruits and bombs based on the remaining time,
	 * and stops the game when the timer reaches zero.
	 */
	private void updateTimer() {

		// Updates time left
		if (seconds > 0) {
			seconds--;
			timeCount.setText(seconds + "");
		}
		
		// Makes bomb every second
		makeFruit();
		
		// Choose random times to generate bomb objects
		int randomBombTime = (int) (Math.random() * 3 + 2);
		
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
	 * Creates a new Fruit object with a random fruit type and horizontal position, sets
	 * up its corresponding scaled image JLabel, positions them at the top of the
	 * screen, adds both components to the game panel, and stores them in the fruits
	 * list for tracking
	 */
	private void makeFruit() {

		// Generates a random X coordinate
		Random r = new Random();
		int randomX = r.nextInt(Game.screenWidth - FRUIT_WIDTH);

		int rFruit = (int) (Math.random() * 100 + 1); // Chooses a random fruit [1, 100]

		// Make Fruit Object
		Fruit randomFruit = new Fruit(rFruit, FRUIT_WIDTH, FRUIT_HEIGHT);
		randomFruit.setLocation(randomX, 0);

		// Get fruit type
		int fruitType = randomFruit.getFruitType();
		ImageIcon scaledFruit = null;

		// Select the scaled image
		switch (fruitType) {
		case 0: scaledFruit = scaledApple; break;
		case 1: scaledFruit = scaledOrange; break;
		case 2: scaledFruit = scaledWatermelon; break;
		case 3:scaledFruit = scaledPear; break;
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
	 * Creates a new Bomb object with a random bomb type and horizontal position, sets up
	 * its corresponding scaled image JLabel, positions them at the top of the
	 * screen, adds both components to the game panel, and stores them in the bombs
	 * list for tracking.
	 */

	private void makeBomb() {
		
		// Generates a random X coordinate
		Random r = new Random();
		int randomX = r.nextInt(Game.screenWidth - BOMB_WIDTH);

		int rbomb = (int) (Math.random() * 10 + 1); // Chooses a random bomb [1, 10]

		// Make Bomb Object
		Bomb randomBomb = new Bomb(rbomb, BOMB_WIDTH, BOMB_HEIGHT);
		randomBomb.setLocation(randomX, 0);

		// Get bomb type
		int bombType = randomBomb.getBombType();
		ImageIcon scaledB = null;

		if (bombType == 0) {
			scaledB = scaledBomb;
		} else if (bombType == 1) {
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
	 * Resets the position of the given fruit to the top of the screen at a random
	 * horizontal (X) coordinate.
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
	 * Displays the startup instructions and game information about how to play, scoring,
	 * and penalties.
	 * 
	 * <p>
	 * If the user selects "Exit", the application will terminate.
	 */

	private void showStartupMessage() {

		String title = "Fruits Basket";
		String message = "<html><div style='text-align: center; width: 400px;'>"
				+ "<h1><u>Welcome to Fruits Basket!</u></h1>" 
				+ "<h2>How To Play</h2>"
				+ "<p>Random fruits will be falling from the top. Collect them with the basket below."
				+ "<br>Use the 'z' and 'x' keys to move the basket left and right. Avoid the bombs"
				+ "<br>that fall alongside the fruits. Uncollected fruits will be sent back to the"
				+ "<br>top and you will get a score penality. Try to finish the game without overwhelming"
				+ "<br>yourself in a mountain of falling fruits. </p>"

				+ "<h2>Scoring & Penalties:</h2>" + "<table align='center'>"
				+ "<tr><td><b>TNT Bomb:</b> -5 seconds</td></tr>" + "<tr><td><b>Normal Bomb:</b> -3 points</td></tr>"
				+ "</table>"

				+ "<h2>Fruit Points:</h2>" + "<table align='center'>" + "<tr><td>Apple: 3 points</td></tr>"
				+ "<tr><td>Orange: 4 points</td></tr>" + "<tr><td>Watermelon: 7 points</td></tr>"
				+ "<tr><td>Pear: 12 points</td></tr>" + "<tr><td>Pomegranate:&nbsp;30&nbsp;points</td></tr>"
				+ "</table>" + "<br>" + "<p>Try to collect as many fruits as you can. "
				+ "You have 60 seconds, good luck! </p>" + "<br><b>Click Enter to Start the Game</b>" + "</div></html>";

		int option = JOptionPane.showOptionDialog(null, message, title, JOptionPane.YES_NO_OPTION,
				JOptionPane.INFORMATION_MESSAGE, startScreenImage, new Object[] { "Enter", "Exit" }, "Yes");

		if (option == JOptionPane.NO_OPTION) {
			System.exit(0); // Handle the case where the user chooses not to start the game
		}
	}

	/**
	 * Ends the current game by stopping all fruit and bomb movement, removing the
	 * basket from the screen, showing the final score, and an option to play again 
	 * or exit.
	 * 
	 * If the player chooses to play again, the game is reset. If the player chooses
	 * to exit, the application terminates.
	 */

	private void endsGame() {

		// Stop movements of fruit
		for (int i = 0; i < fruits.size(); i++) {
			Fruit currentFruit = fruits.get(i).fruit;
			currentFruit.setState(State.LANDED);
		}

		// Stop movements of bombs
		for (int i = 0; i < bombs.size(); i++) {
			Bomb currentBomb = bombs.get(i).bomb;
			currentBomb.setState(State.LANDED);
		}

		// removes basket components
		remove(basket);
		remove(basketImage);

		// Game Over Message
		String title = "Game Over!";
		String message = "<html><div style='text-align: center; width: 220px;'>"
		        + "<h1><u>Game Over</u></h1>"
		        + "<h2>Your Score: " + score + "</h2>"
		        + "<br><br><h2>Play Again?</h2>"
		        + "</div></html>";

		int option = JOptionPane.showOptionDialog(null, message, title, JOptionPane.YES_NO_OPTION,
				JOptionPane.INFORMATION_MESSAGE, endScreenImage, // Fruits Basket Image
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
		seconds = 60;
		scoreCount.setText("Score: 0");
		
		// Disables basket movement
		disableZKey();
		disableXKey();

		// Remove all fruits and their Jlabels from the screen
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

		// Refreshes the screen after removing components
		repaint();
		revalidate();

		// Make new basket
		makeBasket();

		// Restarts the timer
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