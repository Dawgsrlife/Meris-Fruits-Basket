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
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.util.HashMap;
import java.awt.GradientPaint;
import java.awt.RenderingHints;
import javax.swing.border.EmptyBorder;
import java.awt.FontMetrics;
import java.awt.geom.AffineTransform;
import java.awt.Graphics;
import javax.swing.JPanel;
import java.util.concurrent.CopyOnWriteArrayList;
import java.io.BufferedInputStream;
import java.io.InputStream;

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
public class FruitsBasket extends Game implements Runnable {

	// JOption Screen Icons
	private ImageIcon startScreenImage, endScreenImage;

	// Offscreen locations
	private final int X_OFFSCREEN = -1000;
	private final int Y_OFFSCREEN = -1000;

	// Tracking variables
	private int score = 0;
	private int seconds = 60;
	private int lastDelay = 15;
	private Thread gameLoopThread;
	private volatile boolean running = true;
	private int fruitSpeed = 4;
	private int bombSpeed = 4;
	private int paddleSpeed = 30;
	private float gameSpeedMultiplier = 1.0f;

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
	// private JLabel basketImage;
	// private JLabel timeCount;
	// private JLabel scoreCount;
	
	private final int TYPE_TIME_BOMB = 1;
	private boolean gameOver = false;

	// Game timer
	Timer gameTime;

	// For Basket
	// private JLabel basketImage;
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

	// ArrayLists (changed to CopyOnWriteArrayList for thread-safety)
	private List<FruitWithImage> fruits = new CopyOnWriteArrayList<>();
	private List<BombWithImage> bombs = new CopyOnWriteArrayList<>();

	// Power-up states
	private boolean isShieldActive = false;
	private boolean isDoublePoints = false;
	private boolean isSlowMotion = false;
	private boolean isFruitMagnet = false;
	private boolean isTimeFreeze = false;
	private int powerUpDuration = 0;
	
	// Combo system
	private int comboCount = 0;
	private int maxCombo = 0;
	private Timer comboResetTimer;
	
	// High scores
	private List<Integer> highScores = new ArrayList<>();
	private static final int MAX_HIGH_SCORES = 5;
	
	// Sound effects
	private Clip catchSound;
	private Clip bombSound;
	private Clip powerUpSound;
	private Clip gameOverSound;
	private Clip backgroundMusic;
	private Clip comboSound;
	private Clip shieldSound;
	private Clip fruitMissSound;
	
	// Sound volume control
	private float masterVolume = 0.7f;
	private float musicVolume = 0.2f;
	private float sfxVolume = 0.2f;
	
	// Sound state
	private boolean soundEnabled = true;
	private boolean musicEnabled = true;
	
	// Particle effects
	private List<Particle> particles = new CopyOnWriteArrayList<>();
	
	// Difficulty levels
	private enum Difficulty { EASY, MEDIUM, HARD }
	private Difficulty currentDifficulty = Difficulty.MEDIUM;
	
	// Power-up types
	private enum PowerUpType { 
		SHIELD, 
		DOUBLE_POINTS, 
		SLOW_MOTION,
		FRUIT_MAGNET,
		TIME_FREEZE
	}
	private PowerUpType currentPowerUp = null;

	// Boss wave system
	private boolean isBossWave = false;
	private int bossHealth = 0;
	private int bossWaveTimer = 0;
	private static final int BOSS_WAVE_INTERVAL = 30; // Every 30 seconds
	private int lastBossWaveTriggeredSecond = -1; // To prevent multiple boss wave triggers in the same second
	private boolean bossWaveActive = false;
	private FruitWithImage bossFruitFwi = null; // Declare boss fruit here
	
	// Active power-up display
	private AnimatedLabel activePowerUpDisplay = null;

	// Achievement system
	private Set<String> achievements = new HashSet<>();
	private Map<String, Integer> achievementProgress = new HashMap<>();
	
	// Visual effects
	private List<AnimatedLabel> animatedLabels = new CopyOnWriteArrayList<>();
	private int screenShakeAmount = 0;
	private Color backgroundColor = Color.WHITE;
	private float comboHue = 0.0f;
	private boolean comboBackgroundActive = false;
	
	// Combo multiplier
	private float comboMultiplier = 1.0f;
	private static final float MAX_MULTIPLIER = 5.0f;
	private static final float MULTIPLIER_INCREASE = 0.1f;
	private static final float MULTIPLIER_DECREASE = 0.05f;
	
	// Achievement types
	private enum Achievement {
		FRUIT_MASTER("Fruit Master", "Catch 100 fruits"),
		BOMB_DODGER("Bomb Dodger", "Survive 5 minutes without hitting bombs"),
		COMBO_KING("Combo King", "Achieve a 10x combo"),
		SPEED_DEMON("Speed Demon", "Catch 5 fruits in 3 seconds"),
		BOSS_SLAYER("Boss Slayer", "Defeat a boss wave");
		
		private final String title;
		private final String description;
		
		Achievement(String title, String description) {
			this.title = title;
			this.description = description;
		}
	}
	
	// Modern color palette
	private static final Color PRIMARY_BG = new Color(34, 40, 49);
	private static final Color ACCENT = new Color(0, 173, 181);
	private static final Color LABEL_BG = new Color(57, 62, 70, 200);
	private static final Color LABEL_TEXT = new Color(238, 238, 238);
	private static final Color HIGHLIGHT = new Color(255, 211, 105);
	private static final Color ERROR = new Color(255, 87, 87);
	private static final Color SUCCESS = new Color(0, 255, 136);
	private static final Color VIBRANT_GREEN = new Color(0, 255, 64);
	private static final Color VIBRANT_RED = new Color(255, 64, 64);
	
	// Helper to create beautiful labels
	private JLabel createBeautifulLabel(String text, int fontSize, Color fg, Color bg, int padding) {
		JLabel label = new JLabel(text);
		label.setFont(new Font("Segoe UI", Font.BOLD, fontSize));
		label.setForeground(fg);
		label.setOpaque(true);
		label.setBackground(bg);
		label.setBorder(new EmptyBorder(padding, padding * 2, padding, padding * 2));
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setVerticalAlignment(JLabel.CENTER);
		label.setFocusable(false);
		return label;
	}
	
	// Add new class for animated labels
	private class AnimatedLabel {
		private String text;
		private int x, y;
		private float alpha;
		private Color color;
		private float scale;
		private int duration;
		private boolean isDead;
		private Font font;
		private boolean bounce;
		
		public AnimatedLabel(String text, int x, int y, Color color) {
			this(text, x, y, color, false);
		}
		public AnimatedLabel(String text, int x, int y, Color color, boolean bounce) {
			this.text = text;
			this.x = x;
			this.y = y;
			this.color = color;
			this.alpha = 1.0f;
			this.scale = 0.5f;
			this.duration = 60; // 1 second at 60 FPS
			this.isDead = false;
			this.font = new Font("Arial Black", Font.BOLD, 32);
			this.bounce = bounce;
		}
		public void update() {
			if (isDead) return;
			duration--;
			if (duration <= 0) {
				isDead = true;
				return;
			}
			// Fade out in last 20 frames
			if (duration < 20) {
				alpha = duration / 20.0f;
			}
			// Bounce/scale effect
			if (bounce && scale < 1.2f) {
				scale += 0.08f;
			} else if (!bounce && scale < 1.0f) {
				scale += 0.05f;
			}
			if (scale > 1.2f) scale = 1.2f;
			// Float upward
			y -= 1;
		}
		public void draw(Graphics2D g) {
			if (isDead) return;
			if (text == null || text.trim().isEmpty()) return; // Only draw if text is not empty
			AffineTransform originalTransform = g.getTransform();
			g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			Color labelColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(alpha * 255));
			g.setFont(font.deriveFont((float)(32 * scale)));
			FontMetrics fm = g.getFontMetrics();
			int textWidth = fm.stringWidth(text);
			int textHeight = fm.getHeight();
			int minWidth = 60;
			int bgWidth = Math.max(textWidth + 24, minWidth);
			int bgHeight = textHeight + 16;
			// More transparent background
			g.setColor(new Color(0, 0, 0, (int)(alpha * 80)));
			g.fillRoundRect(x - bgWidth/2, y - bgHeight/2, bgWidth, bgHeight, 16, 16);
			g.setColor(labelColor);
			g.drawString(text, x - textWidth/2, y + textHeight/4);
			g.setTransform(originalTransform);
		}
	}

	// SpeedupLabel: appears when speed increases
	private AnimatedLabel speedupLabel = null;
	private void showSpeedupLabel(String text) {
		speedupLabel = new AnimatedLabel(text, Game.screenWidth/2, 120, HIGHLIGHT, true);
		speedupLabel.duration = 45;
	}

	private GamePanel gamePanel;

	public FruitsBasket() {
		super();
		gamePanel = new GamePanel();
		setContentPane(gamePanel);
		gamePanel.setLayout(null);

		// Diagnostic: Print list types to confirm CopyOnWriteArrayList is used
		System.out.println("Fruits list class: " + fruits.getClass().getName());
		System.out.println("Bombs list class: " + bombs.getClass().getName());
		System.out.println("AnimatedLabels list class: " + animatedLabels.getClass().getName());
		System.out.println("Particles list class: " + particles.getClass().getName());
	}

	public void setupImage() throws IOException {
		try {
			// Gets images from the Image Resources folder
			basketIm = ImageIO.read(getClass().getResourceAsStream("/Image Resources/basket.png"));
			appleIm = ImageIO.read(getClass().getResourceAsStream("/Image Resources/apple.png"));
			orangeIm = ImageIO.read(getClass().getResourceAsStream("/Image Resources/orange.png"));
			watermelonIm = ImageIO.read(getClass().getResourceAsStream("/Image Resources/watermelon.png"));
			pearIm = ImageIO.read(getClass().getResourceAsStream("/Image Resources/pear.png"));
			pomegranateIm = ImageIO.read(getClass().getResourceAsStream("/Image Resources/pomegranate.png"));
			bombIm = ImageIO.read(getClass().getResourceAsStream("/Image Resources/bomb.png"));
			tntIm = ImageIO.read(getClass().getResourceAsStream("/Image Resources/tnt.png"));

			startScreenImage = new ImageIcon(getClass().getResource("/Image Resources/Basket Icon.png"));
			endScreenImage = new ImageIcon(getClass().getResource("/Image Resources/bomb friends.png"));

			// Scales images to appropriate sizes
			scaledBasketIm = scaleImage(basketIm, BASKET_WIDTH, BASKET_HEIGHT + 15);
			scaledApple = scaleImage(appleIm, FRUIT_WIDTH, FRUIT_HEIGHT);
			scaledOrange = scaleImage(orangeIm, FRUIT_WIDTH, FRUIT_HEIGHT);
			scaledWatermelon = scaleImage(watermelonIm, FRUIT_WIDTH, FRUIT_HEIGHT);
			scaledPear = scaleImage(pearIm, FRUIT_WIDTH, FRUIT_HEIGHT);
			scaledPomegranate = scaleImage(pomegranateIm, FRUIT_WIDTH, FRUIT_HEIGHT);
			scaledBomb = scaleImage(bombIm, BOMB_WIDTH, BOMB_HEIGHT);
			scaledTnt = scaleImage(tntIm, BOMB_WIDTH, BOMB_HEIGHT);
		} catch (Exception e) {
			System.err.println("Error loading image resources: " + e.getMessage());
			e.printStackTrace();
			throw new IOException("Failed to load image resources.", e);
		}
	}

	/**
	 * Fill in this method with code that tells the game what to do before actual
	 * play begins
	 */

	public void setup() {
		
		// Get images set up
		try {
			setupImage();
			setupSounds();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		showStartupMessage(); // Show Game Instructions
		setupComboSystem();

		// Initialize the game timer (for spawning and progression)
		gameTime = new Timer(800, e -> updateTimer());
		gameTime.start();

		// Start the animation loop (for smooth visuals)
		running = true;
		gameLoopThread = new Thread(() -> {
			while (running) {
				updateGame();
				gamePanel.repaint();
				try {
					Thread.sleep(1000 / 60); // 60 FPS for smooth animation
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		gameLoopThread.start();

		makeBasket();
		makeFruit();

		// Initialize lastBossWaveTriggeredSecond to prevent immediate trigger
		lastBossWaveTriggeredSecond = seconds;
	}

	/**
	 * Fill in this method with code that tells the playing field what to do from
	 * one moment to the next
	 */

	@Override
	public void act() {
		// Unused: Game loop handled by custom thread in this version.
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
		// Implementation of visualEffects method
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
	}

	/**
	 * Updates the game score by adding the point value of the collected fruit,
	 * refreshes the score display label, and shows the visual text of that.
	 *
	 * @param currentFruit - the Fruit object that was collected
	 */
	private void updateScore(Fruit currentFruit) {
		int points = currentFruit.getFruitPoint();
		if (isDoublePoints) {
			points *= 2;
		}
		points = Math.round(points * comboMultiplier);
		
		comboCount++;
		if (comboCount > maxCombo) {
			maxCombo = comboCount;
		}
		
		score = Math.max(0, score + points); // Ensure score never goes negative
		
		// Add animated label at fruit location
		addVisualEffect("+" + points, 
			currentFruit.getX() + currentFruit.getWidth()/2,
			currentFruit.getY(),
			VIBRANT_GREEN, true);
		
		playSound(catchSound);
		createParticleEffect(currentFruit.getX(), currentFruit.getY(), VIBRANT_GREEN);
		updateComboDisplay();
		comboResetTimer.restart();
		
		updateAchievements();
	}

	/**
	 * Applies the effect of a bomb on the game state by adjusting either the timer
	 * or the score depending on the bomb type. Ensures that time and score do not
	 * go below zero.
	 *
	 * @param currentBomb - the Bomb object whose effect is to be applied
	 */
	private void bombEffect(Bomb currentBomb) {
		if (isShieldActive) {
			playSound(shieldSound);
			addVisualEffect("Shield!", 
				currentBomb.getX() + currentBomb.getWidth()/2,
				currentBomb.getY(),
				HIGHLIGHT, true);
			isShieldActive = false; // Shield is one-time use
			return;
		}
		
		playSound(bombSound);
		createParticleEffect(currentBomb.getX(), currentBomb.getY(), VIBRANT_RED);
		
		if (currentBomb.getBombType() == TYPE_TIME_BOMB) {
			seconds = Math.max(0, seconds + currentBomb.getBombValue()); // Ensure time never goes negative
			addVisualEffect(currentBomb.getBombValue() + " secs",
				currentBomb.getX() + currentBomb.getWidth()/2,
				currentBomb.getY(),
				VIBRANT_RED, true);
			} else {
			score = Math.max(0, score + currentBomb.getBombValue()); // Ensure score never goes negative
			addVisualEffect(currentBomb.getBombValue() + " pts",
				currentBomb.getX() + currentBomb.getWidth()/2,
				currentBomb.getY(),
				VIBRANT_RED, true);
		}
		
		comboCount = 0;
		updateComboDisplay();
	}

	/**
	 * Updates the countdown timer each second, refreshes the timer display,
	 * triggers creation of fruits and bombs based on the remaining time,
	 * and stops the game when the timer reaches zero.
	 */
	private void updateTimer() {
		if (seconds > 0) {
			seconds--;
			// Speedup logic
			int newDelay = 15;
			if (seconds >= 50 && seconds <= 60) {
				newDelay = 14;
			} else if (seconds >= 20 && seconds <= 49) {
				newDelay = 12;
			} else if (seconds >= 0 && seconds <= 19) {
				newDelay = 8;
			}
			if (newDelay != lastDelay) {
				showSpeedupLabel("Speed Up!");
				addVisualEffect("Speed Up!", Game.screenWidth/2, 120, HIGHLIGHT, true);
				lastDelay = newDelay;
				gameTime.setDelay(newDelay * 50); // Convert to milliseconds
			}
			// Spawn new fruits and bombs
		makeFruit();
			if (Math.random() < 0.3) {
			makeBomb();
		}
		} else {
			gameOver = true;
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
		randomFruit.setState(State.FALLING); // Ensure state is set
		// Get fruit type
		int fruitType = randomFruit.getFruitType();
		ImageIcon scaledFruit = null;

		// Select the scaled image
		switch (fruitType) {
		case 0: scaledFruit = scaledApple; break;
		case 1: scaledFruit = scaledOrange; break;
		case 2: scaledFruit = scaledWatermelon; break;
		case 3: scaledFruit = scaledPear; break;
		case 4: scaledFruit = scaledPomegranate; break;
		}

		// Create with proper physics values
		FruitWithImage fwi = new FruitWithImage(randomFruit, scaledFruit);
		fwi.vx = (Math.random() - 0.5) * 0.2; // -0.1 to +0.1 px/frame
		fwi.vy = 0; // Start with no vertical velocity
		fruits.add(fwi);
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
		Bomb randomBomb;
		if (rbomb <= 5) {
			randomBomb = new TNTBomb(BOMB_WIDTH, BOMB_HEIGHT);
		} else {
			randomBomb = new Bomb(rbomb, BOMB_WIDTH, BOMB_HEIGHT);
		}
		randomBomb.setLocation(randomX, 0);

		// Create with proper physics values
		BombWithImage bwi = new BombWithImage(randomBomb, scaledBomb);
		bwi.vx = (Math.random() - 0.5) * 0.2; // -0.1 to +0.1 px/frame
		bwi.vy = 0; // Start with no vertical velocity
		bombs.add(bwi);
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
				+ "<br>top and you will get a score penalty. Try to finish the game without overwhelming"
				+ "<br>yourself in a mountain of falling fruits.</p>"
				
				+ "<h2>New Features:</h2>"
				+ "<ul>"
				+ "<li>Power-ups: Shield, Double Points, Slow Motion, Fruit Magnet, Time Freeze</li>"
				+ "<li>Combo System: Chain catches for bonus points and multipliers</li>"
				+ "<li>Boss Waves: Defeat giant fruits for massive points</li>"
				+ "<li>Achievements: Unlock special rewards</li>"
				+ "<li>Visual Effects: Screen shake, particles, and more</li>"
				+ "</ul>"
				
				+ "<h2>Scoring & Penalties:</h2>" 
				+ "<table align='center'>"
				+ "<tr><td><b>TNT Bomb:</b> -5 seconds</td></tr>" 
				+ "<tr><td><b>Normal Bomb:</b> -3 points</td></tr>"
				+ "</table>"

				+ "<h2>Fruit Points:</h2>" 
				+ "<table align='center'>" 
				+ "<tr><td>Apple: 3 points</td></tr>"
				+ "<tr><td>Orange: 4 points</td></tr>" 
				+ "<tr><td>Watermelon: 7 points</td></tr>"
				+ "<tr><td>Pear: 12 points</td></tr>" 
				+ "<tr><td>Pomegranate: 30 points</td></tr>"
				+ "</table>" 
				+ "<br>" 
				+ "<p>Try to collect as many fruits as you can. "
				+ "You have 60 seconds, good luck!</p>" 
				+ "<br><b>Click Enter to Start the Game</b>" 
				+ "</div></html>";

		int option = JOptionPane.showOptionDialog(null, message, title, JOptionPane.YES_NO_OPTION,
				JOptionPane.INFORMATION_MESSAGE, startScreenImage, new Object[] { "Enter", "Exit" }, "Yes");

		if (option == JOptionPane.NO_OPTION) {
			System.exit(0);
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
		running = false;
		if (gameLoopThread != null) {
			try { gameLoopThread.join(100); } catch (Exception e) {}
		}
		// Stop background music
		if (backgroundMusic != null) {
			backgroundMusic.stop();
		}
		
		// Play game over sound
		playSound(gameOverSound);
		
		// Stop movements
		for (FruitWithImage fwi : fruits) {
			fwi.fruit.setState(State.LANDED);
		}
		for (BombWithImage bwi : bombs) {
			bwi.bomb.setState(State.LANDED);
		}
		
		updateHighScores();
		
		// Show game over message
		String gameOverMessage = "<html><div style='text-align: center; width: 400px;'>" +
				"<h1><u>Game Over!</u></h1>" +
				"<h2>Final Score: " + score + "</h2>" +
				"<h3>High Scores:</h3>" +
				getHighScoresHTML() +
				"<br><b>Play Again?</b></div></html>";
		
		int option = JOptionPane.showOptionDialog(null, gameOverMessage, "Game Over", JOptionPane.YES_NO_OPTION,
				JOptionPane.INFORMATION_MESSAGE, endScreenImage, new Object[] { "Play Again", "Exit" }, "Play Again");
		
		if (option == JOptionPane.YES_OPTION) {
			resetGame();
		} else {
			System.exit(0);
		}
	}

	/**
	 * Resets the game state to start a new game session.
	 */

	private void resetGame() {
		// Stop current game loop
		running = false;
		if (gameLoopThread != null) {
			try { gameLoopThread.join(100); } catch (Exception e) {}
		}
		
		// Stop current timer
		if (gameTime != null) {
			gameTime.stop();
		}
		
		// Restart background music
		if (musicEnabled) {
			playBackgroundMusic();
		}
		
		// Reset game state
		score = 0;
		seconds = 60;
		comboCount = 0;
		maxCombo = 0;
		gameOver = false;
		lastBossWaveTriggeredSecond = 60;
		gameSpeedMultiplier = 1.0f;
		comboBackgroundActive = false;
		paddleSpeed = 30; // Reset paddle speed
		
		disableZKey();
		disableXKey();
		
		// Clear all game objects
		fruits.clear();
		bombs.clear();
		
		particles.clear();
		animatedLabels.clear();
		
		// Explicitly reset power-up states
		isShieldActive = false;
		isDoublePoints = false;
		isSlowMotion = false;
		isFruitMagnet = false;
		isTimeFreeze = false;
		currentPowerUp = null;
		powerUpDuration = 0;
		activePowerUpDisplay = null;
		
		// Reset boss state
		isBossWave = false;
		bossHealth = 0;
		bossWaveTimer = 0;
		bossWaveActive = false;
		bossFruitFwi = null; // Clear boss fruit

		// Restart the game timer
		gameTime = new Timer(800, e -> updateTimer());
		gameTime.start();
		
		// Restart the animation loop
		running = true;
		gameLoopThread = new Thread(() -> {
			while (running) {
				updateGame();
				gamePanel.repaint();
				try {
					Thread.sleep(1000 / 60);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		gameLoopThread.start();
		
		makeBasket();
		makeFruit();
	}

	private AudioInputStream loadAudio(String path) throws Exception {
		InputStream is = getClass().getResourceAsStream(path);
		if (is == null) {
			throw new IOException("Could not find sound file: " + path);
		}
		BufferedInputStream bis = new BufferedInputStream(is);
		return AudioSystem.getAudioInputStream(bis);
	}

	private void setupSounds() {
		try {
			// Load sound effects using getResourceAsStream with buffering
			AudioInputStream audioIn;
			
			// Catch sound
			audioIn = loadAudio("/sounds/catch.wav");
			catchSound = AudioSystem.getClip();
			catchSound.open(audioIn);
			
			// Bomb sound
			audioIn = loadAudio("/sounds/bomb.wav");
			bombSound = AudioSystem.getClip();
			bombSound.open(audioIn);
			
			// Power-up sound
			audioIn = loadAudio("/sounds/powerup.wav");
			powerUpSound = AudioSystem.getClip();
			powerUpSound.open(audioIn);
			
			// Game over sound
			audioIn = loadAudio("/sounds/gameover.wav");
			gameOverSound = AudioSystem.getClip();
			gameOverSound.open(audioIn);
			
			// Background music
			audioIn = loadAudio("/sounds/background.wav");
			backgroundMusic = AudioSystem.getClip();
			backgroundMusic.open(audioIn);
			
			// Combo sound
			audioIn = loadAudio("/sounds/combo.wav");
			comboSound = AudioSystem.getClip();
			comboSound.open(audioIn);
			
			// Shield sound
			audioIn = loadAudio("/sounds/shield.wav");
			shieldSound = AudioSystem.getClip();
			shieldSound.open(audioIn);
			
			// Fruit miss sound
			try {
				audioIn = loadAudio("/sounds/miss.wav");
			} catch (Exception e) {
				// Try alternate filename if miss.wav is missing
				audioIn = loadAudio("/sounds/fruitmiss.wav");
			}
			fruitMissSound = AudioSystem.getClip();
			fruitMissSound.open(audioIn);
			
			// Set initial volumes
			setupVolumeControls();
			
			// Start background music
			playBackgroundMusic();
			
		} catch (Exception e) {
			System.err.println("Error loading sound resources: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void setupVolumeControls() {
		setVolume(backgroundMusic, musicVolume);
		setVolume(catchSound, sfxVolume);
		setVolume(bombSound, sfxVolume);
		setVolume(powerUpSound, sfxVolume);
		setVolume(gameOverSound, sfxVolume);
		setVolume(comboSound, sfxVolume);
		setVolume(shieldSound, sfxVolume);
		setVolume(fruitMissSound, sfxVolume);
	}
	
	private void setVolume(Clip clip, float volume) {
		if (clip == null) return;
		try {
			FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			float dB = (float) (Math.log10(Math.max(volume, 0.0001)) * 20.0);
			gainControl.setValue(dB);
		} catch (Exception e) {
			// Some clips may not support volume control; ignore
		}
	}
	
	private void playBackgroundMusic() {
		if (backgroundMusic != null && musicEnabled) {
			backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
		}
	}
	
	private void playSound(Clip clip) {
		if (clip != null && soundEnabled) {
			clip.setFramePosition(0);
			clip.start();
		}
	}
	
	private void setupComboSystem() {
		comboResetTimer = new Timer(2000, e -> {
			if (comboCount > 0) {
				comboCount = 0;
				updateComboDisplay();
			}
		});
		comboResetTimer.setRepeats(false);
	}
	
	private void updateComboDisplay() {
		if (comboCount > 1) {
			addVisualEffect(comboCount + "x COMBO!", Game.screenWidth / 2, 80, Color.YELLOW, true);
			playSound(comboSound);
		}
	}
	
	private void spawnPowerUp() {
		if (Math.random() < 0.1) { // 10% chance to spawn power-up
			PowerUpType[] types = PowerUpType.values();
			currentPowerUp = types[(int)(Math.random() * types.length)];
			powerUpDuration = 10 * 60; // 10 seconds duration (converted to frames)
			
			// Clear existing power-up effects before applying new ones
			deactivatePowerUp();
			
			// Apply power-up effects immediately
			switch (currentPowerUp) {
				case SHIELD:
					isShieldActive = true;
					addVisualEffect("SHIELD ACTIVATED!", Game.screenWidth / 2, 120, Color.CYAN, true);
					addVisualEffect("Blocks next bomb hit", Game.screenWidth / 2, 160, Color.CYAN, true);
					activePowerUpDisplay = new AnimatedLabel("SHIELD", Game.screenWidth - 100, 90, Color.CYAN, false);
					activePowerUpDisplay.duration = Integer.MAX_VALUE; // Make it persist
					activePowerUpDisplay.font = new Font("Arial Black", Font.BOLD, 24); // Smaller font
					break;
				case DOUBLE_POINTS:
					isDoublePoints = true;
					addVisualEffect("DOUBLE POINTS!", Game.screenWidth / 2, 120, Color.YELLOW, true);
					addVisualEffect("2x score for 10 seconds", Game.screenWidth / 2, 160, Color.YELLOW, true);
					activePowerUpDisplay = new AnimatedLabel("2X POINTS", Game.screenWidth - 100, 90, Color.YELLOW, false);
					activePowerUpDisplay.duration = Integer.MAX_VALUE;
					activePowerUpDisplay.font = new Font("Arial Black", Font.BOLD, 24);
					break;
				case SLOW_MOTION:
					isSlowMotion = true;
					gameSpeedMultiplier = 0.5f;
					addVisualEffect("SLOW MOTION!", Game.screenWidth / 2, 120, Color.MAGENTA, true);
					addVisualEffect("Half speed for 10 seconds", Game.screenWidth / 2, 160, Color.MAGENTA, true);
					activePowerUpDisplay = new AnimatedLabel("SLOW MOTION", Game.screenWidth - 100, 90, Color.MAGENTA, false);
					activePowerUpDisplay.duration = Integer.MAX_VALUE;
					activePowerUpDisplay.font = new Font("Arial Black", Font.BOLD, 24);
					break;
				case FRUIT_MAGNET:
					isFruitMagnet = true;
					addVisualEffect("FRUIT MAGNET!", Game.screenWidth / 2, 120, Color.GREEN, true);
					addVisualEffect("Attracts fruits for 10 seconds", Game.screenWidth / 2, 160, Color.GREEN, true);
					activePowerUpDisplay = new AnimatedLabel("MAGNET", Game.screenWidth - 100, 90, Color.GREEN, false);
					activePowerUpDisplay.duration = Integer.MAX_VALUE;
					activePowerUpDisplay.font = new Font("Arial Black", Font.BOLD, 24);
					break;
				case TIME_FREEZE:
					isTimeFreeze = true;
					addVisualEffect("TIME FREEZE!", Game.screenWidth / 2, 120, Color.BLUE, true);
					addVisualEffect("Timer paused for 10 seconds", Game.screenWidth / 2, 160, Color.BLUE, true);
					activePowerUpDisplay = new AnimatedLabel("TIME FREEZE", Game.screenWidth - 100, 90, Color.BLUE, false);
					activePowerUpDisplay.duration = Integer.MAX_VALUE;
					activePowerUpDisplay.font = new Font("Arial Black", Font.BOLD, 24);
					break;
			}
			
			playSound(powerUpSound);
		}
	}
	
	private void createParticleEffect(int x, int y, Color color) {
		for (int i = 0; i < 20; i++) {
			particles.add(new Particle(x, y, color));
		}
	}
	
	private void updateParticles() {
		List<Particle> particlesToRemove = new ArrayList<>();
		for (Particle p : particles) {
			p.update();
			if (p.isDead()) {
				particlesToRemove.add(p);
			}
		}
		particles.removeAll(particlesToRemove);
	}
	
	private void deactivatePowerUp() {
		// Store previous power-up for transition message
		PowerUpType previousPowerUp = currentPowerUp;
		
		// Reset all power-up states
		isShieldActive = false;
		isDoublePoints = false;
		isSlowMotion = false;
		isFruitMagnet = false;
		isTimeFreeze = false;
		currentPowerUp = null;
		gameSpeedMultiplier = 1.0f;
		
		// Show transition message
		if (previousPowerUp != null) {
			addVisualEffect(previousPowerUp.name() + " ended!", Game.screenWidth / 2, Game.screenHeight / 2, Color.RED, true);
		}
		
		// Ensure music continues playing
		if (musicEnabled && backgroundMusic != null && !backgroundMusic.isRunning()) {
			playBackgroundMusic();
		}
	}
	
	private void updateHighScores() {
		highScores.add(score);
		Collections.sort(highScores, Collections.reverseOrder());
		if (highScores.size() > MAX_HIGH_SCORES) {
			highScores = highScores.subList(0, MAX_HIGH_SCORES);
		}
	}
	
	private String getHighScoresHTML() {
		StringBuilder sb = new StringBuilder("<table align='center'>");
		for (int i = 0; i < highScores.size(); i++) {
			sb.append("<tr><td>").append(i + 1).append(". ").append(highScores.get(i)).append("</td></tr>");
		}
		sb.append("</table>");
		return sb.toString();
	}

	private void updateAchievements() {
		// Fruit Master
		if (score >= 100 && !achievements.contains(Achievement.FRUIT_MASTER.name())) {
			unlockAchievement(Achievement.FRUIT_MASTER);
		}
		
		// Combo King
		if (comboCount >= 10 && !achievements.contains(Achievement.COMBO_KING.name())) {
			unlockAchievement(Achievement.COMBO_KING);
		}
		
		// Boss Slayer
		if (bossHealth <= 0 && !achievements.contains(Achievement.BOSS_SLAYER.name())) {
			unlockAchievement(Achievement.BOSS_SLAYER);
		}
	}
	
	private void unlockAchievement(Achievement achievement) {
		achievements.add(achievement.name());
		addVisualEffect("Achievement Unlocked: " + achievement.title, Game.screenWidth / 2, Game.screenHeight / 2, Color.YELLOW, true);
		playSound(powerUpSound);
	}
	
	private void startBossWave() {
		isBossWave = true;
		bossHealth = 100;
		bossWaveTimer = BOSS_WAVE_INTERVAL * 60; // 30 seconds to defeat boss (converted to frames)
		bossWaveActive = true;
		
		// Create a giant fruit as the boss
		Fruit bossFruit = new Fruit();
		bossFruit.setWidth(FRUIT_WIDTH * 3);
		bossFruit.setHeight(FRUIT_HEIGHT * 3);
		bossFruit.setLocation(Game.screenWidth / 2, 50);
		
		// Add visual effects
		addVisualEffect("BOSS WAVE INCOMING!", Game.screenWidth / 2 - 100, 100, Color.RED, true);
		addVisualEffect("Defeat the giant fruit!", Game.screenWidth / 2 - 100, 140, Color.RED, true);
		
		// Screen shake
		screenShakeAmount = 10;
		
		// Change background color
		backgroundColor = new Color(255, 200, 200);
	}
	
	private void updateBossWave() {
		if (isBossWave) {
			bossWaveTimer--;
			if (bossWaveTimer <= 0) {
				endBossWave(false);
				bossWaveActive = false;
			}
		} else if (!isBossWave && !bossWaveActive && seconds > 0 && seconds % BOSS_WAVE_INTERVAL == 0 && seconds != lastBossWaveTriggeredSecond) {
			// Only trigger boss wave if we're past the initial game setup
			if (seconds < 60) { // Prevent boss wave in first minute
				lastBossWaveTriggeredSecond = seconds;
				return;
			}
			startBossWave();
			lastBossWaveTriggeredSecond = seconds;
		}
	}

	private void endBossWave(boolean victory) {
		isBossWave = false;
		bossWaveActive = false;
		backgroundColor = Color.WHITE;
		if (victory) {
			score += 1000;
			addVisualEffect("BOSS DEFEATED! +1000 points", Game.screenWidth / 2 - 100, Game.screenHeight / 2, Color.GREEN);
		} else {
			addVisualEffect("BOSS ESCAPED!", Game.screenWidth / 2 - 100, Game.screenHeight / 2, Color.RED);
		}
		if (musicEnabled && backgroundMusic != null) {
			playBackgroundMusic();
		}
	}
	
	private void updateComboMultiplier() {
		if (comboCount > 0) {
			comboMultiplier = Math.min(MAX_MULTIPLIER, 
				comboMultiplier + MULTIPLIER_INCREASE);
			if (comboCount >= 5) {
				comboBackgroundActive = true;
				comboHue = (comboHue + 0.005f) % 1.0f;
			}
		} else {
			comboMultiplier = Math.max(1.0f, 
				comboMultiplier - MULTIPLIER_DECREASE);
			comboBackgroundActive = false;
		}
	}
	
	private void updateVisualEffects() {
		List<AnimatedLabel> labelsToRemove = new ArrayList<>();
		for (AnimatedLabel label : animatedLabels) {
			label.update();
			if (label.isDead) {
				labelsToRemove.add(label);
			}
		}
		animatedLabels.removeAll(labelsToRemove);

		if (speedupLabel != null) {
			speedupLabel.update();
			if (speedupLabel.isDead) speedupLabel = null;
		}
	}

	/**
	 * This code has been provided for you, and should not be modified
	 */
	public static void main(String[] args) {
		FruitsBasket p = new FruitsBasket();
		p.setVisible(true);
		p.initComponents();
	}

	public void run() {
		showStartupMessage();
		playBackgroundMusic();
		
		while (true) {
			updateGame();
			gamePanel.repaint();
			try {
				Thread.sleep(1000 / 60); // 60 FPS
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void updateGame() {
		if (gameOver) {
			endsGame();
			return;
		}
		
		// Update power-up timers
		if (powerUpDuration > 0) {
			powerUpDuration--;
			if (powerUpDuration <= 0) {
				deactivatePowerUp();
			} else {
				// Apply power-up effects
				if (currentPowerUp == PowerUpType.SLOW_MOTION) {
					gameSpeedMultiplier = 0.5f;
				} else if (currentPowerUp == PowerUpType.TIME_FREEZE) {
					seconds++; // Counteract the normal timer decrement
				}
			}
		}
		
		// Update boss wave
		updateBossWave();
		
		// Update combo multiplier
		updateComboMultiplier();
		
		// Update visual effects
		updateVisualEffects();
		
		// Update particles
		updateParticles();
		
		// Update achievements
		updateAchievements();
		
		// Update screen shake
		if (screenShakeAmount > 0) {
			screenShakeAmount = (int)Math.max(0, screenShakeAmount - 0.5f);
		}
		
		// Update fruits and bombs (physics only)
		List<FruitWithImage> fruitsToRemove = new ArrayList<>();
		for (FruitWithImage fwi : fruits) {
			if (fwi.fruit.getState() == State.FALLING) {
				// Apply gravity with terminal velocity, scaled by gameSpeedMultiplier
				fwi.vy = Math.min(fwi.vy + (GRAVITY * gameSpeedMultiplier), TERMINAL_VELOCITY * gameSpeedMultiplier);
				
				// Apply fruit magnet effect
				if (isFruitMagnet) {
					double dx = basket.getX() + BASKET_WIDTH/2 - (fwi.fruit.getX() + fwi.fruit.getWidth()/2);
					double dy = basket.getY() - (fwi.fruit.getY() + fwi.fruit.getHeight());
					double dist = Math.sqrt(dx*dx + dy*dy);
					if (dist > 0) {
						fwi.vx += (dx/dist) * 0.2;
						fwi.vy += (dy/dist) * 0.2;
					}
				}
				
				// Wall bounce
				int nextX = (int)(fwi.fruit.getLocation().x + (fwi.vx * gameSpeedMultiplier));
				if (nextX < 0) {
					fwi.vx = -fwi.vx * BOUNCE_DAMPING;
					nextX = 0;
				} else if (nextX + fwi.fruit.getWidth() > Game.screenWidth) {
					fwi.vx = -fwi.vx * BOUNCE_DAMPING;
					nextX = Game.screenWidth - fwi.fruit.getWidth();
				}
				// Update position
				fwi.fruit.setLocation(
					nextX,
					(int)(fwi.fruit.getLocation().y + (fwi.vy * gameSpeedMultiplier))
				);
				// Check for collision with basket
				if (fwi.fruit.collides(basket)) {
					fwi.fruit.setState(State.CAUGHT);
					updateScore(fwi.fruit);
				}
				// Check if out of bounds (bottom only)
				if (fwi.fruit.getLocation().y > Game.screenHeight) {
					fwi.fruit.setState(State.LANDED);
					score = Math.max(0, score - 2); // Ensure score never goes negative
					comboCount = 0;
					addVisualEffect("-2", fwi.fruit.getX(), Game.screenHeight - 50, ERROR, true);
				}
			}
			// Mark fruit for removal if not falling (CAUGHT or LANDED)
			if (fwi.fruit.getState() != State.FALLING) {
				fruitsToRemove.add(fwi);
			}
		}
		fruits.removeAll(fruitsToRemove);

		List<BombWithImage> bombsToRemove = new ArrayList<>();
		for (BombWithImage bwi : bombs) {
			if (bwi.bomb.getState() == State.FALLING) {
				// Apply gravity with terminal velocity, scaled by gameSpeedMultiplier
				bwi.vy = Math.min(bwi.vy + (GRAVITY * gameSpeedMultiplier), TERMINAL_VELOCITY * gameSpeedMultiplier);
				// Wall bounce
				int nextX = (int)(bwi.bomb.getLocation().x + (bwi.vx * gameSpeedMultiplier));
				if (nextX < 0) {
					bwi.vx = -bwi.vx * BOUNCE_DAMPING;
					nextX = 0;
				} else if (nextX + bwi.bomb.getWidth() > Game.screenWidth) {
					bwi.vx = -bwi.vx * BOUNCE_DAMPING;
					nextX = Game.screenWidth - bwi.bomb.getWidth();
				}
				bwi.bomb.setLocation(
					nextX,
					(int)(bwi.bomb.getLocation().y + (bwi.vy * gameSpeedMultiplier))
				);
				// Check for collision with basket
				if (bwi.bomb.collides(basket)) {
					bwi.bomb.setState(State.EXPLODED);
					bombEffect(bwi.bomb);
				}
				// Check if out of bounds (bottom only)
				if (bwi.bomb.getLocation().y > Game.screenHeight) {
					bwi.bomb.setState(State.LANDED);
				}
			}
			// Mark bomb for removal if not falling (LANDED or EXPLODED)
			if (bwi.bomb.getState() != State.FALLING) {
				bombsToRemove.add(bwi);
			}
		}
		bombs.removeAll(bombsToRemove);
		
		// Handle basket movement, scaled by gameSpeedMultiplier
		if (ZKeyPressed() && basket.getX() >= 0) {
			basket.setLocation(Math.max(0, basket.getX() - (int)(paddleSpeed * gameSpeedMultiplier)), basket.getY());
		}
		if (XKeyPressed() && basket.getX() + BASKET_WIDTH + 20 < Game.screenWidth) {
			basket.setLocation(Math.min(Game.screenWidth - BASKET_WIDTH - 20, basket.getX() + (int)(paddleSpeed * gameSpeedMultiplier)), basket.getY());
		}
	}

	private class GamePanel extends JPanel {
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			// Draw background gradient
			Color topColor, bottomColor;
			if (comboBackgroundActive) {
				topColor = Color.getHSBColor(comboHue, 0.8f, 1.0f);
				bottomColor = Color.getHSBColor((comboHue + 0.1f) % 1.0f, 0.8f, 0.8f);
			} else {
				topColor = PRIMARY_BG;
				bottomColor = ACCENT;
			}
			GradientPaint gp = new GradientPaint(0, 0, topColor, 0, getHeight(), bottomColor);
			g2d.setPaint(gp);
			g2d.fillRect(0, 0, getWidth(), getHeight());
			// Draw all fruits
			for (FruitWithImage fwi : fruits) {
				Fruit f = fwi.fruit;
				ImageIcon icon = getFruitIcon(f.getFruitType());
				if (icon != null && f.getState() == State.FALLING) {
					g2d.drawImage(icon.getImage(), f.getX(), f.getY(), f.getWidth(), f.getHeight(), null);
				}
			}
			// Draw all bombs
			for (BombWithImage bwi : bombs) {
				Bomb b = bwi.bomb;
				ImageIcon icon = getBombIcon(b.getBombType());
				if (icon != null && b.getState() == State.FALLING) {
					g2d.drawImage(icon.getImage(), b.getX(), b.getY(), b.getWidth(), b.getHeight(), null);
				}
			}
			// Draw basket
			if (basket != null && scaledBasketIm != null) {
				g2d.drawImage(scaledBasketIm.getImage(), basket.getX(), basket.getY() - 12, BASKET_WIDTH, BASKET_HEIGHT + 15, null);
			}
			// Draw score and timer
			g2d.setFont(new Font("Segoe UI", Font.BOLD, 32));
			g2d.setColor(LABEL_BG);
			g2d.fillRoundRect(20, 20, 180, 50, 20, 20);
			g2d.setColor(LABEL_TEXT);
			g2d.drawString("Score: " + score, 40, 55);
			g2d.setColor(LABEL_BG);
			g2d.fillRoundRect(getWidth() - 120, 20, 100, 50, 20, 20);
			g2d.setColor(LABEL_TEXT);
			g2d.drawString(String.valueOf(seconds), getWidth() - 90, 55);
			// Draw animated labels
			for (AnimatedLabel label : animatedLabels) {
				label.draw(g2d);
			}
			if (speedupLabel != null && !speedupLabel.isDead) {
				speedupLabel.draw(g2d);
			}
			// Draw particles
			for (Particle p : particles) {
				p.draw(g2d);
			}
		}
	}

	private ImageIcon getFruitIcon(int fruitType) {
		switch (fruitType) {
			case 0: return scaledApple;
			case 1: return scaledOrange;
			case 2: return scaledWatermelon;
			case 3: return scaledPear;
			case 4: return scaledPomegranate;
			default: return null;
		}
	}
	private ImageIcon getBombIcon(int bombType) {
		if (bombType == 0) return scaledBomb;
		if (bombType == 1) return scaledTnt;
		return null;
	}

	// Add these fields to FruitsBasket (not inside GamePanel):
	private static final double GRAVITY = 0.2; // Reasonable falling speed
	private static final double TERMINAL_VELOCITY = 8.0; // Reasonable max speed
	private static final double BOUNCE_DAMPING = 0.8;

	private void addVisualEffect(String text, int x, int y, Color color) {
		addVisualEffect(text, x, y, color, false);
	}
	private void addVisualEffect(String text, int x, int y, Color color, boolean bounce) {
		animatedLabels.add(new AnimatedLabel(text, x, y, color, bounce));
	}

	// Add constants for magic numbers
	private static final double BOMB_SPAWN_CHANCE = 0.3;
	private static final int BOSS_TIMER_SECONDS = 30;
}

class Particle {
	private double x, y;
	private double vx, vy;
	private double life;
	private Color color;
	
	public Particle(int x, int y, Color color) {
		this.x = x;
		this.y = y;
		this.color = color;
		this.life = 1.0;
		
		// More obvious particles: larger burst, distinct initial velocity
		this.vx = (Math.random() - 0.5) * 10; // Random horizontal velocity between -5 and 5
		this.vy = -(Math.random() * 8 + 5); // Random upward velocity between -5 and -13 (stronger initial kick)
	}
	
	public void update() {
		x += vx;
		y += vy;
		vy += 0.1; // gravity
		life -= 0.025; // Slower decay rate for a noticeable fade (approx 40 frames)
	}
	
	public void draw(Graphics2D g) {
		int alpha = (int)(life * 255);
		Color particleColor = new Color(
			color.getRed(),
			color.getGreen(),
			color.getBlue(),
			alpha
		);
		g.setColor(particleColor);
		g.fillOval((int)x, (int)y, 8, 8); // Larger particle size for more obvious effect
	}
	
	public boolean isDead() {
		return life <= 0;
	}
}