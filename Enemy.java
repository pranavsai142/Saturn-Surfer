import java.awt.image.*;
import java.awt.geom.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.*;

public class Enemy implements Sprite {
	
	String ENEMY_IMAGE = "assets/enemyColor.png";
	String ENEMY_SHADOW_IMAGE = "assets/enemy_shadow.png";
	int TOP_LEFT_HEIGHT = 259;
	int BOTTOM_WIDTH = 335;
	int TOP_RIGHT_HEIGHT = 176;

	
	double ORIGINAL_TO_HEIGHT_0_SCALAR = .2;
	double ORIGINAL_TO_HEIGHT_1_SCALAR = .3;
	double ORIGINAL_TO_HEIGHT_2_SCALAR = .5;
	
	double ORIGINAL_SHADOW_TO_HEIGHT_1_SCALAR = .75;
	double ORIGINAL_SHADOW_TO_HEIGHT_2_SCALAR = 1.35;
	
	BufferedImage image;
	BufferedImage originalImage;
	BufferedImage shadowImage;
	BufferedImage originalShadowImage;
	int imageWidth;
	int imageHeight;
	int originalImageWidth;
	int originalImageHeight;
	int shadowImageWidth;
	int shadowImageHeight;
	int originalShadowImageWidth;
	int originalShadowImageHeight;
	Point2D.Double currentPosition;
	int currentHeight;
	boolean movingForward;
	boolean movingLeft;
	boolean movingRight;
	boolean movingBackward;
	
	ArrayList<RegEnemyProjectile> regEnemyProjectiles;
	
	
	public Enemy(int x, int y, int h) {
		currentPosition = new Point2D.Double(x, y);
		try {
    		originalImage = ImageIO.read(new File(ENEMY_IMAGE));
		} catch (IOException e) {
			System.out.println("ASSET LOADING PROBLEM");
		}

		initOriginalImageDimensions();
		image = resize(originalImage, ((int) (ORIGINAL_TO_HEIGHT_0_SCALAR * originalImageWidth)), ((int) (ORIGINAL_TO_HEIGHT_0_SCALAR * originalImageHeight)));

		initImageDimensions();

		try {
			originalShadowImage = ImageIO.read(new File(ENEMY_SHADOW_IMAGE));
		} catch (IOException e) {
			System.out.println("ASSET LOADING PROBLEM");
		}

		initOriginalShadowImageDimensions();

		shadowImage = null;


		currentHeight = h;
	
		
		movingForward = false;
		movingLeft = false;
		movingRight = false;
		movingBackward = false;

		regEnemyProjectiles = new ArrayList<>();
	}
	
	public Shape getBoundingShape() {
		Path2D.Double bound = new Path2D.Double();
		if(currentHeight == 0) {
			bound.moveTo(currentPosition.getX(),currentPosition.getY() + (TOP_LEFT_HEIGHT * ORIGINAL_TO_HEIGHT_0_SCALAR));
			bound.lineTo(currentPosition.getX() + (BOTTOM_WIDTH * ORIGINAL_TO_HEIGHT_0_SCALAR), currentPosition.getY() + (originalImageHeight * ORIGINAL_TO_HEIGHT_0_SCALAR));
			bound.lineTo(currentPosition.getX() + (originalImageWidth * ORIGINAL_TO_HEIGHT_0_SCALAR), currentPosition.getY() + (TOP_RIGHT_HEIGHT * ORIGINAL_TO_HEIGHT_0_SCALAR));
			bound.lineTo(currentPosition.getX(),currentPosition.getY() + (TOP_LEFT_HEIGHT * ORIGINAL_TO_HEIGHT_0_SCALAR));
		}
		else if(currentHeight == 1) {
			bound.moveTo(currentPosition.getX(),currentPosition.getY() + (TOP_LEFT_HEIGHT * ORIGINAL_TO_HEIGHT_1_SCALAR));
			bound.lineTo(currentPosition.getX() + (BOTTOM_WIDTH * ORIGINAL_TO_HEIGHT_1_SCALAR), currentPosition.getY() + (originalImageHeight * ORIGINAL_TO_HEIGHT_1_SCALAR));
			bound.lineTo(currentPosition.getX() + (originalImageWidth * ORIGINAL_TO_HEIGHT_1_SCALAR), currentPosition.getY() + (TOP_RIGHT_HEIGHT * ORIGINAL_TO_HEIGHT_1_SCALAR));
			bound.lineTo(currentPosition.getX(),currentPosition.getY() + (TOP_LEFT_HEIGHT * ORIGINAL_TO_HEIGHT_1_SCALAR));
		}
		else if(currentHeight == 2) {
			bound.moveTo(currentPosition.getX(),currentPosition.getY() + (TOP_LEFT_HEIGHT * ORIGINAL_TO_HEIGHT_2_SCALAR));
			bound.lineTo(currentPosition.getX() + (BOTTOM_WIDTH * ORIGINAL_TO_HEIGHT_2_SCALAR), currentPosition.getY() + (originalImageHeight * ORIGINAL_TO_HEIGHT_2_SCALAR));
			bound.lineTo(currentPosition.getX() + (originalImageWidth * ORIGINAL_TO_HEIGHT_2_SCALAR), currentPosition.getY() + (TOP_RIGHT_HEIGHT * ORIGINAL_TO_HEIGHT_2_SCALAR));
			bound.lineTo(currentPosition.getX(),currentPosition.getY() + (TOP_LEFT_HEIGHT * ORIGINAL_TO_HEIGHT_2_SCALAR));
		}
		return bound;
	}

	public List<RegEnemyProjectile> getProjectiles() {
		return regEnemyProjectiles;
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
	
	public Image getShadowImage() {
		if(currentHeight == 0) {
			return null;
		}
		if(currentHeight == 1) {
			shadowImage = resize(originalShadowImage, ((int)(originalShadowImageWidth * ORIGINAL_SHADOW_TO_HEIGHT_1_SCALAR)), ((int)(originalShadowImageHeight * ORIGINAL_SHADOW_TO_HEIGHT_1_SCALAR)));
		}
		if(currentHeight == 2) {
			shadowImage = resize(originalShadowImage, ((int)(originalShadowImageWidth * ORIGINAL_SHADOW_TO_HEIGHT_2_SCALAR)), ((int)(originalShadowImageHeight * ORIGINAL_SHADOW_TO_HEIGHT_2_SCALAR)));
		}
		initShadowImageDimensions();
		return shadowImage;
	}
		
	public static BufferedImage resize(BufferedImage img, int newW, int newH) {
   		Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
   		BufferedImage newImg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

    	Graphics2D g2d = newImg.createGraphics();
    	g2d.drawImage(tmp, 0, 0, null);
    	g2d.dispose();

    	return newImg;
	}
	
    public void initImageDimensions() {
    	imageWidth = image.getWidth();
		imageHeight = image.getHeight();
    }

    public void initOriginalImageDimensions() {
    	originalImageWidth = originalImage.getWidth();
		originalImageHeight = originalImage.getHeight();
    }

	public void initShadowImageDimensions() {
    	shadowImageWidth = shadowImage.getWidth();
		shadowImageHeight = shadowImage.getHeight();
    }

    public void initOriginalShadowImageDimensions() {
    	originalShadowImageWidth = originalShadowImage.getWidth();
		originalShadowImageHeight = originalShadowImage.getHeight();
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

	public Point2D.Double getShadowXY() {
		return new Point2D.Double(currentPosition.x, (currentPosition.y + (currentHeight * 100)));
	}

	public double getShadowX() {
		return currentPosition.x;
	}

	public double getShadowY() {
		return (currentPosition.y + (currentPosition.y + (currentHeight * 100)));
	}

	public boolean hasShadow() {
		if(currentHeight > 0) {
			return true;
		}
		return false;
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
		// regEnemyProjectiles.add(new RegEnemyProjectile((int) currentPosition.getX() + 190, (int) currentPosition.getY() - 65));
	}
}