import java.awt.image.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.geom.*;
import java.awt.*;
import java.io.*;
import javax.imageio.*;

public class Player implements Sprite {

	String PLAYER_IMAGE = "assets/player.png";
	String PLAYER_SHADOW_IMAGE = "assets/player_shadow.png";
	int SCALE_UP = 1;
	int STOP_SCALE_UP = 200;
	int SCALE_DOWN = -1;
	int STOP_SCALE_DOWN = -200;
	int NOT_SCALING = 0;
	double SCALAR_MULTIPLE = 1.002;
	double ORIGINAL_TO_HEIGHT_0_SCALAR = 0.2;
	double ORIGINAL_TO_HEIGHT_1_SCALAR = ORIGINAL_TO_HEIGHT_0_SCALAR * Math.pow(SCALAR_MULTIPLE, (STOP_SCALE_UP - 1));
	double ORIGINAL_TO_HEIGHT_2_SCALAR = ORIGINAL_TO_HEIGHT_1_SCALAR * Math.pow(SCALAR_MULTIPLE, (STOP_SCALE_UP - 1));
	
	double SHADOW_SCALAR_MULTIPLE = 1.002;
	double ORIGINAL_SHADOW_TO_HEIGHT_1_SCALAR = 0.65;
	double ORIGINAL_SHADOW_TO_HEIGHT_2_SCALAR = ORIGINAL_SHADOW_TO_HEIGHT_1_SCALAR * Math.pow(SHADOW_SCALAR_MULTIPLE, (STOP_SCALE_UP - 1));
	
	double CURRENT_HEIGHT_DECIMAL_INCREMENT = (1/(STOP_SCALE_UP * 1.0));
	
	BufferedImage image;
	BufferedImage originalImage;
	BufferedImage shadowImage;
	BufferedImage originalShadowImage;
	Point2D.Double currentPosition;
	int currentHeight;
	double currentHeightDecimal;
	int imageWidth;
	int imageHeight;
	int originalImageWidth;
	int originalImageHeight;
	int shadowImageWidth;
	int shadowImageHeight;
	int originalShadowImageWidth;
	int originalShadowImageHeight;
	boolean movingForward;
	boolean movingLeft;
	boolean movingRight;
	boolean movingBackward;
	boolean collidingTop;
	boolean collidingBottom;
	boolean collidingLeft;
	boolean collidingRight;
	int scaling;
	private List<PlayerProjectile> projectiles;
	
	public Player(int x, int y) {
		currentPosition = new Point2D.Double(x, y);
		try {
    		originalImage = ImageIO.read(new File(PLAYER_IMAGE));
		} catch (IOException e) {
			System.out.println("ASSET LOADING PROBLEM");
		}
	
		initOriginalImageDimensions();
		image = resize(originalImage, ((int) (ORIGINAL_TO_HEIGHT_0_SCALAR * originalImageWidth)), ((int) (ORIGINAL_TO_HEIGHT_0_SCALAR * originalImageHeight)));
		
		initImageDimensions();
		
		try {
			originalShadowImage = ImageIO.read(new File(PLAYER_SHADOW_IMAGE));
		} catch (IOException e) {
			System.out.println("ASSET LOADING PROBLEM");
		}
		
		initOriginalShadowImageDimensions();
		
		shadowImage = null;
		
		currentHeight = 0;
		currentHeightDecimal = 0.0;
		
		movingForward = false;
		movingLeft = false;
		movingRight = false;
		movingBackward = false;
		
		collidingTop = false;
		collidingBottom = false;
		collidingLeft = false;
		collidingRight = false;
		
		scaling = NOT_SCALING;
		
		projectiles = new ArrayList<>();
	}
	
	
	public Shape getBoundingShape() {
		initImageDimensions();
		Path2D.Double bound = new Path2D.Double();
		bound.moveTo(currentPosition.getX(),currentPosition.getY() + ((int) imageHeight/3));
		bound.lineTo(currentPosition.getX() + ((2 * imageWidth)/3), currentPosition.getY() + imageHeight);
		bound.lineTo(currentPosition.getX() + imageWidth, currentPosition.getY());
		bound.lineTo(currentPosition.getX(),currentPosition.getY() + ((int) imageHeight/3));
		return bound;
	}
	
	public Point2D.Double getTopLeftBoundPoint() {
		if(currentHeight == 0) {
			return new Point2D.Double(currentPosition.getX(),currentPosition.getY() + imageHeight/3);
		}
		if(currentHeight > 0) {
			Point2D.Double p = getShadowXY();
			return new Point2D.Double(p.getX(), p.getY() + shadowImageHeight/3);
		}
		return null;
	}
	
	public Point2D.Double getBottomBoundPoint() {
		if(currentHeight == 0) {
			return new Point2D.Double(currentPosition.getX() + ((2 * imageWidth)/3), currentPosition.getY() + imageHeight);
		}
		if(currentHeight > 0) {
			Point2D.Double p = getShadowXY();
			return new Point2D.Double(p.getX() + ((2 * shadowImageWidth)/3), p.getY() + shadowImageHeight);
		}
		return null;
	}
	
	public Image getImage() {
		if(scaling == STOP_SCALE_UP || scaling == STOP_SCALE_DOWN) {
			System.out.println("DONE");
			scaling = NOT_SCALING;
		}
		if(scaling > NOT_SCALING) {
			System.out.println("attempting scale up");
			scaleImageUp();
		}
		else if(scaling < NOT_SCALING) {
			System.out.println("attempting scale down");
			scaleImageDown();
		}
		return image;
	}
	
	public Image getShadowImage() {
		if(scaling > NOT_SCALING) {
			System.out.println("attempting to scale shadow up");
			scaleShadowUp();
		}
		else if(scaling < NOT_SCALING) {
			System.out.println("attempting to scale shadow down");
			scaleShadowDown();
		}
		return shadowImage;
	}
	
	public List<PlayerProjectile> getProjectiles() {
	    return projectiles;
	}
	
// 	public Image getShadow() {
// 		
// 	}
	
	public void scaleImageUp() {
		double scalar;
		if(currentHeight == 1) {
			scalar = (ORIGINAL_TO_HEIGHT_0_SCALAR * Math.pow(SCALAR_MULTIPLE, scaling));
		}
		else {
			scalar = (ORIGINAL_TO_HEIGHT_1_SCALAR * Math.pow(SCALAR_MULTIPLE, scaling));
		}
// 		System.out.println("SCALAR:" + scalar);
		image = resize(originalImage , ((int) (scalar * originalImageWidth)), ((int) (scalar * originalImageHeight)));
		initImageDimensions();
    	scaling++;
    	currentHeightDecimal += CURRENT_HEIGHT_DECIMAL_INCREMENT;
    }
    
	public void scaleImageDown() {
		double scalar;
		if(currentHeight == 1) {
			System.out.println("YIIIPPPE");
			scalar = (ORIGINAL_TO_HEIGHT_2_SCALAR * Math.pow((1/SCALAR_MULTIPLE), -scaling));
		}
		else {
			scalar = (ORIGINAL_TO_HEIGHT_1_SCALAR * Math.pow((1/SCALAR_MULTIPLE), -scaling));
		}
// 		System.out.println("SCALAR:" + scalar);
		image = resize(originalImage , ((int) (scalar * originalImageWidth)), ((int) (scalar * originalImageHeight)));
		initImageDimensions();
        scaling--;
        currentHeightDecimal -= CURRENT_HEIGHT_DECIMAL_INCREMENT;
    }
    
    public void scaleShadowUp() {
    	double scalar;
    	if(currentHeight == 1) {
//     		System.out.println("WOOPDEEDOODAH");
    		scalar = (ORIGINAL_SHADOW_TO_HEIGHT_1_SCALAR * Math.pow((1/SHADOW_SCALAR_MULTIPLE), (STOP_SCALE_UP - scaling)));
    	}
    	else {
    		scalar = (ORIGINAL_SHADOW_TO_HEIGHT_1_SCALAR * Math.pow(SHADOW_SCALAR_MULTIPLE, scaling));
    	}
// 		System.out.println("SCALAR:" + scalar);
		shadowImage = resize(originalShadowImage , ((int) (scalar * originalShadowImageWidth)), ((int) (scalar * originalShadowImageHeight)));
		initShadowImageDimensions();
    }
    
    public void scaleShadowDown() {
    	double scalar;
    	if(currentHeight == 1) {
//     		System.out.println("WOOPDEEDOODAH");
    		scalar = (ORIGINAL_SHADOW_TO_HEIGHT_2_SCALAR * Math.pow((1/SHADOW_SCALAR_MULTIPLE), -scaling));
    	}
    	else {
    		scalar = (ORIGINAL_SHADOW_TO_HEIGHT_1_SCALAR * Math.pow((1/SHADOW_SCALAR_MULTIPLE), (-scaling)));
    	}
// 		System.out.println("SCALAR:" + scalar);
		shadowImage = resize(originalShadowImage , ((int) (scalar * originalShadowImageWidth)), ((int) (scalar * originalShadowImageHeight)));
		initShadowImageDimensions();
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
		return new Point2D.Double(currentPosition.x, (currentPosition.y + (currentHeightDecimal * 100)));
	}
	
	public double getShadowX() {
		return currentPosition.x;
	}
	
	public double getShadowY() {
		return (currentPosition.y + (300 * (currentHeight - 1)));
	}
	
	public boolean hasShadow() {
		if(currentHeightDecimal > .1) {
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
		return (movingForward || movingLeft || movingRight || movingBackward);
	}
	
	public void move() {
		if(movingForward && !collidingRight) {
			setX(currentPosition.getX() + 2);
			setY(currentPosition.getY() - 1);
		}
		if(movingLeft && !collidingTop) {
			setX(currentPosition.getX() - 1);
			setY(currentPosition.getY() - 2);
		}
		if(movingRight && !collidingBottom) {
			setX(currentPosition.getX() + 1);
			setY(currentPosition.getY() + 2);
		}
		if(movingBackward && !collidingLeft) {
			setX(currentPosition.getX() - 2);
			setY(currentPosition.getY() + 1);
		}
	}
	
	public void shoot() {
		projectiles.add(new PlayerProjectile((int)currentPosition.getX() + 190, (int)currentPosition.getY() - 65));
		//System.out.println(projectiles.size());
	}

	
	public void collidingTop(boolean val) {
		collidingTop = val;
	}
	
	public boolean isCollidingTop() {
		return collidingTop;
	}
	
	public void collidingBottom(boolean val) {
		collidingBottom = val;
	}
	
	public boolean isCollidingBottom() {
		return collidingBottom;
	}

	public void collidingLeft(boolean val) {
		collidingLeft = val;
	}
	
	public boolean isCollidingLeft() {
		return collidingLeft;
	}
	
	public void collidingRight(boolean val) {
		collidingRight = val;
	}
	
	public boolean isCollidingRight() {
		return collidingRight;
	}
	
	

	
	public void loseLife() {
		System.out.println("OUCH");
	}
	
	public void moveUp() {
		if(currentHeight < 2 && scaling == NOT_SCALING) {
			currentHeight++;
			scaling = SCALE_UP;
		}
	}
	
	public void moveDown() {
		if(currentHeight > 0 && scaling == NOT_SCALING) {
			currentHeight--;
			scaling = SCALE_DOWN;
		}
	}
	
}