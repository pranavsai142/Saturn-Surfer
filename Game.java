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

	private static final String BACKGROUND_IMAGE = "assets/background2.png";
	private static final int PANEL_WIDTH = 800;
	private static final int PANEL_HEIGHT = 800;
	private static final int BACKGROUND_DELAY = 30;
	private static final int SPRITE_DELAY = 10;
	private static final int MILLESECONDS_BETWEEN_LEVEL = 1666;
	private static final long coolDownTime = 700;

	private BufferedImage fullBackground;
	private BufferedImage background;
	private Point2D.Double offset;
	private Player player;
	private Timer backgroundTime;
	private Timer spriteTime;
	private int spriteCounter;
	private long lastAttack = 0;

	private ArrayList<Enemy> enemies;

// 	ArrayList<LifeCrystal> lifeCrystals;

	public Game() {
		try {
    		fullBackground = ImageIO.read(new File(BACKGROUND_IMAGE));
		} catch (IOException e) {
			System.out.println("ASSET LOADING PROBLEM");
		}
		
		spriteCounter = 0;
		
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
		spriteTime.start();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;


		
		
		g2d.drawImage(background, 0, 0, this);


		for (Enemy enemy : enemies) {
			List<RegEnemyProjectile> regEnemyProjectiles = enemy.getProjectiles();
			g2d.drawImage(enemy.getImage(), ((int) enemy.getX()), ((int) enemy.getY()), this);
			for (RegEnemyProjectile regEnemyProjectile : regEnemyProjectiles) {
				g2d.drawImage(regEnemyProjectile.getImage(), ((int) regEnemyProjectile.getX()),
						((int) regEnemyProjectile.getY()), this);
			}

		}

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
	}
	
			
	public void recalculateBackground() {
		Graphics g = background.getGraphics();
		g.drawImage(fullBackground, ((int) offset.getX()), ((int) offset.getY()), null);
		offset = new Point2D.Double(offset.getX() - 2, offset.getY() + 1);
		// repeat image
		if (offset.getX() <= -1220.0) {
			offset = new Point2D.Double(-696, -352);
		}
//		System.out.println("X: " + offset.getX());
//		System.out.println("Y: " + offset.getY());
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
			Enemy enemy = new Enemy(800, 200);
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
		//Control player staying inside bounds
		if(playerIntersectsTopBound()) {
			player.collidingTop(true);
			player.movingLeft(false);
			intersect = true;
		}
		else if(playerIntersectsBottomBound()) {
			player.collidingBottom(true);
			player.movingRight(false);
			intersect = true;
		}
		else if(playerIntersectsLeftBound()) {
// 			System.out.println("leftleftleftleft");
			player.collidingLeft(true); 
			player.movingBackward(false);
			intersect = true;
		}
		else if(playerIntersectsRightBound()) {
			player.collidingRight(true);
			player.movingForward(false);
			intersect = true;
		}
		else {
			player.collidingTop(false);
			player.collidingBottom(false);
			player.collidingLeft(false);
			player.collidingRight(false);
		}
		
		if(playerIntersectingEnemy() || playerIntersectingProjectile()) {
			player.loseLife();
		}	
// 		
// 		ArrayList<Enemy> destroyedEnemies = getDestroyedEnemies();
// 		if(destroyedEnemies.size() == 0) {
// 			
// 		}
		
	}
	
	public boolean playerIntersectsTopBound() {
		Point2D.Double topLeftBoundPoint = player.getTopLeftBoundPoint();
		double y = 447 - (.5 * topLeftBoundPoint.getX());
		if(topLeftBoundPoint.getY() <= y) {
			System.out.println("TOP TOP TOP");
			return true;
		}
		return false;
	}
	
	public boolean playerIntersectsBottomBound() {
		Point2D.Double bottomBoundPoint = player.getBottomBoundPoint();
		double y = 960 - (.5 * bottomBoundPoint.getX());
		if(bottomBoundPoint.getY() >= y) {
			System.out.println("BOTTOM BOTTOM BOTTOM");
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
		
	public void keyTyped(KeyEvent e) {
	
	}
	
	public void keyPressed(KeyEvent e) {
		System.out.println("KEY PRESSED");
		int id = e.getKeyCode();
		if(id == 87) {
			player.movingForward(true);
		}
		if(id == 65) {
			player.movingLeft(true);
		}
		if(id == 68) {
			player.movingRight(true);
		}
		if(id == 83) {
			player.movingBackward(true);
		}
		if(id == 69) {
			player.moveUp();
		}
		if(id == 81) {
			player.moveDown();
		}
		if (id == 32) {
			// limit rate of fire
			long time = System.currentTimeMillis();
			if (time > lastAttack + coolDownTime) {
				player.shoot();
				for (Enemy enemy : enemies) {
					enemy.shoot();
				}
				lastAttack = time;
			}
		}
	}
	
	public void keyReleased(KeyEvent e) {
// 		System.out.println("KEY RELEASED");
		int id = e.getKeyCode();
		if(id == 87) {
			player.movingForward(false);
		}
		if(id == 65) {
			player.movingLeft(false);
		}
		if(id == 68) {
			player.movingRight(false);
		}
		if(id == 83) {
			player.movingBackward(false);
		}
	}

	public void actionPerformed(ActionEvent e) {
		Timer t = (Timer) e.getSource();
		if(t.getActionCommand().equals("BACKGROUND_TIMER")) {
			recalculateBackground();
		}
		else if(t.getActionCommand().equals("SPRITE_TIMER")) {
			spriteCounter++;
			recalculateSpritePositions();
		}
		repaint();
	}
	
}