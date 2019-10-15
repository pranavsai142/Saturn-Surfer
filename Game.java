import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.io.*;
import javax.imageio.*;
import java.util.ArrayList;
import java.util.List;

public class Game extends JPanel implements KeyListener, ActionListener {

	private static final String BACKGROUND_IMAGE = "assets/backgroundSaturn.png";
	private static final String BACKGROUND_IMAGE_PLAYING = "assets/backgroundTransparent.png";
	private static final String SATURN_IMAGE = "assets/saturn.png";
	private static final String START_IMAGE = "assets/start_screen.png";
	private static final String EXPOSITION_IMAGE = "assets/exposition.png";
	private static final String GAME_OVER_IMAGE = "assets/game_over.png";
	
	private static final String HEART_IMAGE = "assets/heart.png";
	private static final int PANEL_WIDTH = 800;
	private static final int PANEL_HEIGHT = 800;
	private static final int BACKGROUND_DELAY = 30;
	private static final int SPRITE_DELAY = 10;
	private static final int MILLESECONDS_BETWEEN_LEVEL = 700;
	private static final long ROFcoolDownTime = 700;
	private static final long DamageCoolDownTime = 3000;
	private static final int GAME_OVER = 3;
	private static final int PLAY = 2;
	private static final int EXPOSITION = 1;
	private static final int START = 0;
	

	private BufferedImage fullBackground;
	private BufferedImage fullBackgroundPlaying;
	private BufferedImage saturnImage;
	private BufferedImage startImage;
	private BufferedImage expositionImage;
	private BufferedImage gameOverImage;
	private BufferedImage background;
	private BufferedImage heart1;
	private BufferedImage heart2;
	private BufferedImage heart3;
	private Point2D.Double offset;
	private Player player;
	private Timer backgroundTime;
	private Timer spriteTime;
	private int spriteCounter;
	private long lastAttack;
	private int hits;
	
	private int mode;

	private ArrayList<Enemy> enemies;
	private List<BufferedImage> hearts = new ArrayList<>();

// 	ArrayList<LifeCrystal> lifeCrystals;

	public Game() {
		try {
			fullBackground = ImageIO.read(new File(BACKGROUND_IMAGE));
			fullBackgroundPlaying = ImageIO.read(new File(BACKGROUND_IMAGE_PLAYING));
			saturnImage = ImageIO.read(new File(SATURN_IMAGE));
			startImage = ImageIO.read(new File(START_IMAGE));
			expositionImage = ImageIO.read(new File(EXPOSITION_IMAGE));
			gameOverImage = ImageIO.read(new File(GAME_OVER_IMAGE));
			heart1 = ImageIO.read(new File(HEART_IMAGE));
			heart1 = resize(heart1, 50, 50);
			heart2 = resize(heart1, 50, 50);
			heart3 = resize(heart1, 50, 50);
		} catch (IOException e) {
			System.out.println("ASSET LOADING PROBLEM");
		}
		

		hearts.add(heart1);
		hearts.add(heart2);
		hearts.add(heart3);

		hits = 0;
		lastAttack = 0;
		
		spriteCounter = 0;
		
		mode = 0;

		background = new BufferedImage(PANEL_WIDTH, PANEL_HEIGHT, BufferedImage.TYPE_INT_ARGB);

		offset = new Point2D.Double(0, (fullBackground.getHeight() - PANEL_HEIGHT) * -1.0);
		setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));

		addKeyListener(this);

		player = new Player(200, 400);

		enemies = new ArrayList<Enemy>();
// 		lifeCrystals = new ArrayList<lifeCrystals>();

		backgroundTime = new Timer(BACKGROUND_DELAY, this);
		backgroundTime.setActionCommand("BACKGROUND_TIMER");
		backgroundTime.start();

		spriteTime = new Timer(SPRITE_DELAY, this);
		spriteTime.setActionCommand("SPRITE_TIMER");
	}
	
	public void reInitGame() {
		hearts.add(heart1);
		hearts.add(heart2);
		hearts.add(heart3);
		spriteCounter = 0;
		
		hits = 0;
		
		lastAttack = 0;
		
		mode = 0;

		background = new BufferedImage(PANEL_WIDTH, PANEL_HEIGHT, BufferedImage.TYPE_INT_ARGB);

		offset = new Point2D.Double(0, (fullBackground.getHeight() - PANEL_HEIGHT) * -1.0);
		player = new Player(200, 400);

		enemies = new ArrayList<Enemy>();
		backgroundTime = new Timer(BACKGROUND_DELAY, this);
		backgroundTime.setActionCommand("BACKGROUND_TIMER");
		backgroundTime.start();

		spriteTime = new Timer(SPRITE_DELAY, this);
		spriteTime.setActionCommand("SPRITE_TIMER");
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		if(mode == START) {
			setOpaque(false);
			g2d.drawImage(background, 0, 0, this);
			g2d.drawImage(startImage, 0, 0, this);
		}
		
		if(mode == EXPOSITION) {
			setOpaque(false);
			g2d.drawImage(expositionImage, 0, 0, this);
		}	

		if(mode == PLAY || mode == GAME_OVER) {
			setOpaque(true);
			g2d.drawImage(background, 0, 0, this);


			// draw heart(s)
			int x = 5;
			for (BufferedImage heart : hearts) {
				g2d.drawImage(heart, x, 5, this);
				x += 57;
			}


			// draw player projectiles
			List<PlayerProjectile> projectiles = player.getProjectiles();
			for (PlayerProjectile projectile : projectiles) {
				g2d.drawImage(projectile.getImage(), ((int) projectile.getX()), ((int) projectile.getY()), this);
			}

//
// 		ArrayList<Image> lifeCrystalImages = new ArrayList<Image>();
// 		ArrayList<Shape> lifeCrystalBoundingShapes = new ArrayList<Image>();
// 		for(LifeCrystal lifeCrystal: lifeCrystals) {
// 			lifeCrystalImages.add(lifeCrystal.getImage());
// 		}

			if(player.hasShadow()) {
				Point2D.Double shadowXY = player.getShadowXY();
				g2d.drawImage(player.getShadowImage(), ((int) shadowXY.getX()), ((int) shadowXY.getY()), this);
			}
	
			g2d.drawImage(player.getImage(), ((int) player.getX()), ((int) player.getY()), this);
			
			// draw enemies and enemy projectiles
			for (Enemy enemy : enemies) {
				List<RegEnemyProjectile> regEnemyProjectiles = enemy.getProjectiles();
				if(enemy.hasShadow()) {
					Point2D.Double shadowXY = enemy.getShadowXY();
					g2d.drawImage(enemy.getShadowImage(), ((int) shadowXY.getX()), ((int) shadowXY.getY()), this);
				}
				g2d.drawImage(enemy.getImage(), ((int) enemy.getX()), ((int) enemy.getY()), this);
				for (RegEnemyProjectile regEnemyProjectile : regEnemyProjectiles) {
					g2d.drawImage(regEnemyProjectile.getImage(), ((int) regEnemyProjectile.getX()),
							((int) regEnemyProjectile.getY()), this);
				}
			}
		
			if (mode == GAME_OVER) {
				g2d.drawImage(gameOverImage, 0, 0, this);
			}
		}
	}

	public void recalculateBackground() {
		AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER);
		Graphics g = background.getGraphics();
		Graphics2D g2d = (Graphics2D) g;
		//if (draw) {
			if(mode == PLAY) {
				g2d.drawImage(saturnImage, 0, 0, this);
				g2d.drawImage(fullBackgroundPlaying, ((int) offset.getX()), ((int) offset.getY()), this);
				g2d.setComposite(ac);
				manageOffset();
			}
			else if(mode == EXPOSITION) {
				g2d.drawImage(expositionImage, 0, 0, this);
			}
			else {
				g2d.drawImage(fullBackground, ((int) offset.getX()), ((int) offset.getY()), this);
				manageOffset();
			}
	}
	
	public void manageOffset() {
		offset = new Point2D.Double(offset.getX() - 2, offset.getY() + 1);
		// repeat image
		if (offset.getX() <= -1220.0) {
			offset = new Point2D.Double(-696, -352);
		}
	}

	public void recalculateSpritePositions() {
		checkCollisions();
		manageEnemies();
		updateProjectiles();
		if (player.isMoving() == true) {
			player.move();
		}
		for (Enemy enemy : enemies) {
			if (enemy.isMoving() == true) {
				enemy.move();
			}
		}
	}

	public void manageEnemies() {
		if (spriteCounter % MILLESECONDS_BETWEEN_LEVEL == 0) {
			int height = (int) (Math.random() * 3);
			int y = (int) (Math.random() * 170);
			Enemy enemy = new Enemy(850, y, height);
			enemy.movingBackward(true);
			enemies.add(enemy);
		}
		// remove enemies off screen
		for (int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			if (e.getX() <= -200) {
				enemies.remove(i);
			}
		}
	}

	private void updateProjectiles() {

		List<PlayerProjectile> projectiles = player.getProjectiles();
		for (int i = 0; i < projectiles.size(); i++) {
			PlayerProjectile projectile = projectiles.get(i);

			// remove projectile off screen
			if (projectile.getX() >= 1900.0) {
				projectiles.remove(i);
			} else {
				projectile.move();
			}
		}

		for (Enemy enemy : enemies) {
			List<RegEnemyProjectile> regEnemyProjectiles = enemy.getProjectiles();
			for (int i = 0; i < regEnemyProjectiles.size(); i++) {
				RegEnemyProjectile regEnemyProjectile = regEnemyProjectiles.get(i);

				// remove projectile off screen
				if (regEnemyProjectile.getX() <= -200.0) {
					regEnemyProjectiles.remove(i);
				} else {
					regEnemyProjectile.move();
				}
			}
		}
	}

	public void checkCollisions() {
		boolean intersect = false;
		// Control player staying inside bounds
		if (playerIntersectsTopBound()) {
			player.collidingTop(true);
			player.movingLeft(false);
			intersect = true;
		} else if (playerIntersectsBottomBound()) {
			player.collidingBottom(true);
			player.movingRight(false);
			intersect = true;
		} else if (playerIntersectsLeftBound()) {
// 			System.out.println("leftleftleftleft");
			player.collidingLeft(true);
			player.movingBackward(false);
			intersect = true;
		} else if (playerIntersectsRightBound()) {
			player.collidingRight(true);
			player.movingForward(false);
			intersect = true;
		} else {
			player.collidingTop(false);
			player.collidingBottom(false);
			player.collidingLeft(false);
			player.collidingRight(false);
		}

		if (playerIntersectingEnemy() || playerIntersectingProjectile()) {
			long time = System.currentTimeMillis();
			if (time > lastAttack + DamageCoolDownTime) {
				hits++;
				heartCounter();
				player.loseLife();
				lastAttack = time;
			}
		}
		List<PlayerProjectile> projectiles = player.getProjectiles();
		for (int i = 0; i < projectiles.size(); i++) {
			PlayerProjectile projectile = projectiles.get(i);
			for(int j = 0; j < enemies.size(); j++) {
				Enemy enemy = enemies.get(j);
				if(projectile.getHeight() == enemy.getHeight()) {
					Shape projectileBound = projectile.getBoundingShape();
					Shape enemyBound = enemy.getBoundingShape();
					if(intersects(projectileBound, enemyBound)) {
						player.removeProjectile(i);
						enemies.remove(j);
// 						System.out.println("HIT HIT HIT");
					}
				}
			}
		}
		
	}

	// for visualizing amount of lives left
	public void heartCounter() {

		if (hits == 1) {
			hearts.remove(2);
		} else if (hits == 2) {
			hearts.remove(1);
		} else if (hits == 3) {
			hearts.remove(0);
			gameOver();
		}
	}

	public void gameOver() {
		backgroundTime.stop();
		spriteTime.stop();
		mode = GAME_OVER;
	}

//
// 		ArrayList<Enemy> destroyedEnemies = getDestroyedEnemies();
// 		if(destroyedEnemies.size() == 0) {
//
// 		}

	public boolean playerIntersectsTopBound() {
		Point2D.Double topLeftBoundPoint = player.getTopLeftBoundPoint();
		double y = 447 - (.5 * topLeftBoundPoint.getX());
		if (topLeftBoundPoint.getY() <= y) {
			return true;
		}
		return false;
	}

	public boolean playerIntersectsBottomBound() {
		Point2D.Double bottomBoundPoint = player.getBottomBoundPoint();
		double y = 960 - (.5 * bottomBoundPoint.getX());
		if (bottomBoundPoint.getY() >= y) {
			return true;
		}
		return false;
	}

	public boolean playerIntersectsLeftBound() {
		return (player.getX() <= -300);
	}

	public boolean playerIntersectsRightBound() {
		return (player.getX() >= 1000);
	}

	public boolean playerIntersectingEnemy() {
		Shape playerBound = player.getBoundingShape();
		for(Enemy enemy : enemies) {
			if(enemy.getHeight() == player.getHeight()) {
				Shape enemyBound = enemy.getBoundingShape();
				if(intersects(playerBound, enemyBound)) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean playerIntersectingProjectile() {
		return false;
	}

	public static boolean intersects(Shape shapeA, Shape shapeB) {
		Area areaA = new Area(shapeA);
		areaA.intersect(new Area(shapeB));
		return !areaA.isEmpty();
	}

	public static BufferedImage resize(BufferedImage img, int newW, int newH) {
		Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
		BufferedImage scaled = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2d = scaled.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();

		return scaled;
	}

	public void keyTyped(KeyEvent e) {

	}

	public void keyPressed(KeyEvent e) {
// 		System.out.println("KEY PRESSED");
		int id = e.getKeyCode();
		if (id == 87) {
			player.movingForward(true);
		}
		if (id == 65) {
			player.movingLeft(true);
		}
		if (id == 68) {
			player.movingRight(true);
		}
		if (id == 83) {
			player.movingBackward(true);
		}
		if (id == 69) {
			player.moveUp();
		}
		if (id == 81) {
			player.moveDown();
		}
		if (id == 32) {
			// limit rate of fire
		long time = System.currentTimeMillis();
			if (time > lastAttack + ROFcoolDownTime) {
				if(mode == START) {
					mode = EXPOSITION;
				}
				else if(mode == EXPOSITION) {
					offset = new Point2D.Double(0, (fullBackground.getHeight() - PANEL_HEIGHT) * -1.0);
					mode = PLAY;
					spriteTime.start();
				}
				else if(mode == PLAY) {
					player.shoot();
					for (Enemy enemy : enemies) {
						enemy.shoot();
					}
				}
				else if(mode == GAME_OVER) {
					reInitGame();
					recalculateBackground();
				}
				lastAttack = time;
			}
		}
	}

	public void keyReleased(KeyEvent e) {
// 		System.out.println("KEY RELEASED");
		int id = e.getKeyCode();
		if (id == 87) {
			player.movingForward(false);
		}
		if (id == 65) {
			player.movingLeft(false);
		}
		if (id == 68) {
			player.movingRight(false);
		}
		if (id == 83) {
			player.movingBackward(false);
		}
	}

	public void actionPerformed(ActionEvent e) {
		Timer t = (Timer) e.getSource();
		if (t.getActionCommand().equals("BACKGROUND_TIMER")) {
			recalculateBackground();
		} else if (t.getActionCommand().equals("SPRITE_TIMER")) {
			spriteCounter++;
			recalculateSpritePositions();
		}
		repaint();
	}

}
