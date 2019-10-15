import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

public class RegEnemyProjectile implements Sprite {
	
	private static final String PROJECTILE_IMAGE = "assets/fireball.png";

	private BufferedImage image;
	private Point2D.Double currentPosition;
	private int currentHeight;
	private int imageWidth;
	private int imageHeight;
	private boolean movingForward;

	//not called
	boolean movingLeft;
	boolean movingRight;
	boolean movingBackward;

	public RegEnemyProjectile(int x, int y, int h) {
		currentPosition = new Point2D.Double(x, y);
		currentHeight = h;
		try {
			image = ImageIO.read(new File(PROJECTILE_IMAGE));
		} catch (IOException e) {
			System.out.println("ASSET LOADING PROBLEM");
		}

		image = resize(image, 90, 90);

		imageWidth = image.getWidth();
		imageHeight = image.getHeight();

		currentHeight = 0;

		movingForward = false;
		movingLeft = false;
		movingRight = false;
		movingBackward = true;
	}

	public static BufferedImage resize(BufferedImage img, int newW, int newH) {
		Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
		BufferedImage scaled = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2d = scaled.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();

		return scaled;
	}

	public Shape getBoundingShape() {
		Path2D.Double bound = new Path2D.Double();
		bound.moveTo(currentPosition.getX(), currentPosition.getY() + ((int) imageHeight / 3));
		bound.lineTo(currentPosition.getX() + ((2 * imageWidth) / 3), currentPosition.getY() + imageHeight);
		bound.lineTo(currentPosition.getX() + imageWidth, currentPosition.getY());
		bound.lineTo(currentPosition.getX(), currentPosition.getY() + ((int) imageHeight / 3));
		return bound;
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

	// not called//////////////
	public void movingLeft(boolean val) {
		movingLeft = val;
	}

	public void movingRight(boolean val) {
		movingRight = val;
	}

	public void movingBackward(boolean val) {
		movingBackward = val;
	}
	//////////////////////

	public boolean isMoving() {
		return movingBackward;
	}

	public void move() {
		if (movingBackward) {
			setX(currentPosition.getX() - 4);
			setY(currentPosition.getY() + 2);
		}
	}

	public void shoot() {
	}

	public boolean isColliding() {
		return false;
	}

}
