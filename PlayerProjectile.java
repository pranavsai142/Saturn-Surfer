import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class PlayerProjectile implements Sprite {

	private static final String PROJECTILE_IMAGE = "assets/red_projectile.png";
	private static final double ORIGINAL_TO_HEIGHT_0_SCALAR = .2;
	private static final double ORIGINAL_TO_HEIGHT_1_SCALAR = .3;
	private static final double ORIGINAL_TO_HEIGHT_2_SCALAR = .6;
	private static final int TOP_LEFT_HEIGHT = 45;
	private static final int BOTTOM_WIDTH = 18;
	private static final int TOP_RIGHT_HEIGHT = 25;
	private static final int TOP_WIDTH = 85;
	
	private BufferedImage image;
	private BufferedImage originalImage;
	private Point2D.Double currentPosition;
	private int currentHeight;
	private int imageWidth;
	private int imageHeight;
	private int originalImageWidth;
	private int originalImageHeight;
	private boolean movingForward;

	//not called
	boolean movingLeft;
	boolean movingRight;
	boolean movingBackward;

	public PlayerProjectile(int x, int y, int h) {
		currentPosition = new Point2D.Double(x, y);
		currentHeight = h;
		try {
			originalImage = ImageIO.read(new File(PROJECTILE_IMAGE));
		} catch (IOException e) {
			System.out.println("ASSET LOADING PROBLEM");
		}
		initOriginalImageDimensions();

		currentHeight = h;

		movingForward = true;
		movingLeft = false;
		movingRight = false;
		movingBackward = false;
	}

	public static BufferedImage resize(BufferedImage img, int newW, int newH) {
		Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
		BufferedImage scaled = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2d = scaled.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();

		return scaled;
	}

	public void initImageDimensions() {
    	imageWidth = image.getWidth();
		imageHeight = image.getHeight();
    }

    public void initOriginalImageDimensions() {
    	originalImageWidth = originalImage.getWidth();
		originalImageHeight = originalImage.getHeight();
    }
    
	public Shape getBoundingShape() {
		Path2D.Double bound = new Path2D.Double();
		if(currentHeight == 0) {
			bound.moveTo(currentPosition.getX(),currentPosition.getY() + (TOP_LEFT_HEIGHT * ORIGINAL_TO_HEIGHT_0_SCALAR));
			bound.lineTo(currentPosition.getX() + (BOTTOM_WIDTH * ORIGINAL_TO_HEIGHT_0_SCALAR), currentPosition.getY() + (originalImageHeight * ORIGINAL_TO_HEIGHT_0_SCALAR));
			bound.lineTo(currentPosition.getX() + (originalImageWidth * ORIGINAL_TO_HEIGHT_0_SCALAR), currentPosition.getY() + (TOP_RIGHT_HEIGHT * ORIGINAL_TO_HEIGHT_0_SCALAR));
			bound.lineTo(currentPosition.getX() + (TOP_WIDTH * ORIGINAL_TO_HEIGHT_0_SCALAR), currentPosition.getY());
			bound.lineTo(currentPosition.getX(),currentPosition.getY() + (TOP_LEFT_HEIGHT * ORIGINAL_TO_HEIGHT_0_SCALAR));
		}
		else if(currentHeight == 1) {
			bound.moveTo(currentPosition.getX(),currentPosition.getY() + (TOP_LEFT_HEIGHT * ORIGINAL_TO_HEIGHT_1_SCALAR));
			bound.lineTo(currentPosition.getX() + (BOTTOM_WIDTH * ORIGINAL_TO_HEIGHT_1_SCALAR), currentPosition.getY() + (originalImageHeight * ORIGINAL_TO_HEIGHT_1_SCALAR));
			bound.lineTo(currentPosition.getX() + (originalImageWidth * ORIGINAL_TO_HEIGHT_1_SCALAR), currentPosition.getY() + (TOP_RIGHT_HEIGHT * ORIGINAL_TO_HEIGHT_1_SCALAR));
			bound.lineTo(currentPosition.getX() + (TOP_WIDTH * ORIGINAL_TO_HEIGHT_1_SCALAR), currentPosition.getY());
			bound.lineTo(currentPosition.getX(),currentPosition.getY() + (TOP_LEFT_HEIGHT * ORIGINAL_TO_HEIGHT_1_SCALAR));
		}
		else if(currentHeight == 2) {
			bound.moveTo(currentPosition.getX(),currentPosition.getY() + (TOP_LEFT_HEIGHT * ORIGINAL_TO_HEIGHT_2_SCALAR));
			bound.lineTo(currentPosition.getX() + (BOTTOM_WIDTH * ORIGINAL_TO_HEIGHT_2_SCALAR), currentPosition.getY() + (originalImageHeight * ORIGINAL_TO_HEIGHT_2_SCALAR));
			bound.lineTo(currentPosition.getX() + (originalImageWidth * ORIGINAL_TO_HEIGHT_2_SCALAR), currentPosition.getY() + (TOP_RIGHT_HEIGHT * ORIGINAL_TO_HEIGHT_2_SCALAR));
			bound.lineTo(currentPosition.getX() + (TOP_WIDTH * ORIGINAL_TO_HEIGHT_2_SCALAR), currentPosition.getY());
			bound.lineTo(currentPosition.getX(),currentPosition.getY() + (TOP_LEFT_HEIGHT * ORIGINAL_TO_HEIGHT_2_SCALAR));
		}
		return bound;
	}

	public Image getImage() {
		if(currentHeight == 0) {
			image = resize(originalImage, ((int)(originalImageWidth * ORIGINAL_TO_HEIGHT_0_SCALAR)), ((int)(originalImageHeight * ORIGINAL_TO_HEIGHT_0_SCALAR)));
		}
		if(currentHeight == 1) {
			image = resize(originalImage, ((int)(originalImageWidth * ORIGINAL_TO_HEIGHT_1_SCALAR)), ((int)(originalImageHeight * ORIGINAL_TO_HEIGHT_1_SCALAR)));
		}
		if(currentHeight == 2) {
			image = resize(originalImage, ((int)(originalImageWidth * ORIGINAL_TO_HEIGHT_2_SCALAR)), ((int)(originalImageHeight * ORIGINAL_TO_HEIGHT_2_SCALAR)));
		}
		initImageDimensions();
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
		return movingForward;
	}

	public void move() {
		if (movingForward) {
			setX(currentPosition.getX() + 4);
			setY(currentPosition.getY() - 2);
		}
	}


	public boolean isColliding() {
		return false;
	}

}
