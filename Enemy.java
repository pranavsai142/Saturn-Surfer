import java.awt.image.*;
import java.awt.geom.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.*;

public class Enemy implements Sprite {
	
	String ENEMY_IMAGE = "assets/enemy.png";
	int TOP_LEFT_HEIGHT = 62;
	int BOTTOM_WIDTH = 80;
	int TOP_RIGHT_HEIGHT = 42;
	
	BufferedImage image;
	Point2D.Double currentPosition;
	int currentHeight;
	int imageWidth;
	int imageHeight;
	boolean movingForward;
	boolean movingLeft;
	boolean movingRight;
	boolean movingBackward;
	
	ArrayList<RegEnemyProjectile> regEnemyProjectiles;
	
	
	public Enemy(int x, int y) {
		currentPosition = new Point2D.Double(x, y);
		try {
    		image = ImageIO.read(new File(ENEMY_IMAGE));
		} catch (IOException e) {
			System.out.println("ASSET LOADING PROBLEM");
		}
		
		imageWidth = image.getWidth();
		imageHeight = image.getHeight();
		
		currentHeight = 0;
		
		movingForward = false;
		movingLeft = false;
		movingRight = false;
		movingBackward = false;

		regEnemyProjectiles = new ArrayList<>();
	}
	
	public Shape getBoundingShape() {
		Path2D.Double bound = new Path2D.Double();
		bound.moveTo(currentPosition.getX(),currentPosition.getY() + TOP_LEFT_HEIGHT);
		bound.lineTo(currentPosition.getX() + BOTTOM_WIDTH, currentPosition.getY() + imageHeight);
		bound.lineTo(currentPosition.getX() + imageWidth, currentPosition.getY() + TOP_RIGHT_HEIGHT);
		bound.lineTo(currentPosition.getX(),currentPosition.getY() + TOP_LEFT_HEIGHT);
		return bound;
	}

	public List<RegEnemyProjectile> getProjectiles() {
		return regEnemyProjectiles;
	}
	
	public Image getImage() {
		return image;
	}
	
	public Point2D.Double getXY() {
		return currentPosition;
	}
	
	public double getX() {
		return currentPosition.x;
	}
	
	public double getY() {
		return currentPosition.y;
	}
	
	public int getHeight() {
		return currentHeight;
	}
	
	public void setXY(Point2D.Double p) {
		currentPosition = p;
	}
	
	public void setX(double v) {
		currentPosition = new Point2D.Double(v, currentPosition.getY());
	}
	
	public void setY(double v) {
		currentPosition = new Point2D.Double(currentPosition.getX(), v);
	}
	
	public void setHeight(int height) {
		currentHeight = height;
	}
	
	public void movingForward(boolean val) {
		movingForward = val;
	}
	
	public void movingLeft(boolean val) {
		movingLeft = val;
	}

	public void movingRight(boolean val) { 
		movingRight = val;
	}
	
	public void movingBackward(boolean val) {
		movingBackward = val;
	}
	
	public boolean isMoving() {
		return movingBackward;
	}
	
	public void move() {
		if (movingBackward) {
			setX(currentPosition.getX() - 2);
			setY(currentPosition.getY() + 1);
		}
	}

	public void shoot() {
		regEnemyProjectiles.add(new RegEnemyProjectile((int) currentPosition.getX() + 190, (int) currentPosition.getY() - 65));
	}
}