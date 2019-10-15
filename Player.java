import java.awt.image.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.geom.*;
import java.awt.*;
import java.io.*;
import javax.imageio.*;

public class Player implements Sprite {

	private static final String PLAYER_IMAGE = "assets/player.png";
	private static final int SCALE_UP = 1;
	private static final int STOP_SCALE_UP = 200;
	private static final int SCALE_DOWN = -1;
	private static final int STOP_SCALE_DOWN = -200;
	private static final int NOT_SCALING = 0;
	private static final double SCALAR_MULTIPLE = 1.002;
	private static final double ORIGINAL_TO_HEIGHT_0_SCALAR = 0.2;
	private static final double ORIGINAL_TO_HEIGHT_1_SCALAR = ORIGINAL_TO_HEIGHT_0_SCALAR * Math.pow(SCALAR_MULTIPLE, (STOP_SCALE_UP - 1));
	private static final double ORIGINAL_TO_HEIGHT_2_SCALAR = ORIGINAL_TO_HEIGHT_1_SCALAR * Math.pow(SCALAR_MULTIPLE, (STOP_SCALE_UP - 1));
	
	private BufferedImage image;
	private BufferedImage originalImage;
	private BufferedImage shadow;
	private BufferedImage originalShadow;
	private Point2D.Double currentPosition;
	private int currentHeight;
	private int imageWidth;
	private	int imageHeight;
	private int originalImageWidth;
	private int originalImageHeight;
	private boolean movingForward;
	private boolean movingLeft;
	private boolean movingRight;
	private boolean movingBackward;
	private boolean collidingTop;
	private boolean collidingBottom;
	private boolean collidingLeft;
	private boolean collidingRight;
	private int scaling;
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
		
		currentHeight = 0;
		
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
		Path2D.Double bound = new Path2D.Double();
		bound.moveTo(currentPosition.getX(),currentPosition.getY() + ((int) imageHeight/3));
		bound.lineTo(currentPosition.getX() + ((2 * imageWidth)/3), currentPosition.getY() + imageHeight);
		bound.lineTo(currentPosition.getX() + imageWidth, currentPosition.getY());
		bound.lineTo(currentPosition.getX(),currentPosition.getY() + ((int) imageHeight/3));
		return bound;
	}
	
	public Point2D.Double getTopLeftBoundPoint() {
		return new Point2D.Double(currentPosition.getX(),currentPosition.getY() + imageHeight/3);
	}
	
	public Point2D.Double getBottomBoundPoint() {
		return new Point2D.Double(currentPosition.getX() + ((2 * imageWidth)/3), currentPosition.getY() + imageHeight);
	}
	
	public Image getImage() {
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
	
	public List<PlayerProjectile> getProjectiles() {
	    return projectiles;
	}
	
// 	public Image getShadow() {
// 		
// 	}
	
	public void scaleImageUp() {
		if(scaling == STOP_SCALE_UP) {
			scaling = NOT_SCALING;
		}
		else {
			double scalar;
			if(currentHeight == 1) {
				scalar = (ORIGINAL_TO_HEIGHT_0_SCALAR * Math.pow(SCALAR_MULTIPLE, scaling));
			}
			else {
				scalar = (ORIGINAL_TO_HEIGHT_1_SCALAR * Math.pow(SCALAR_MULTIPLE, scaling));
			}
			System.out.println("SCALAR:" + scalar);
			image = resize(originalImage , ((int) (scalar * originalImageWidth)), ((int) (scalar * originalImageHeight)));
			initImageDimensions();
        	scaling++;
		}
    }
    
	public void scaleImageDown() {
		if(scaling == STOP_SCALE_DOWN) {
			scaling = NOT_SCALING;
		}
		else {
			double scalar;
			if(currentHeight == 1) {
				System.out.println("YIIIPPPE");
				scalar = (ORIGINAL_TO_HEIGHT_2_SCALAR * Math.pow((1/SCALAR_MULTIPLE), -scaling));
			}
			else {
				scalar = (ORIGINAL_TO_HEIGHT_1_SCALAR * Math.pow((1/SCALAR_MULTIPLE), -scaling));
			}
			System.out.println("SCALAR:" + scalar);
			image = resize(originalImage , ((int) (scalar * originalImageWidth)), ((int) (scalar * originalImageHeight)));
			initImageDimensions();
        	scaling--;
		}
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
    
    public BufferedImage getScaledUpBufferedImage() {
    	return new BufferedImage((int) (imageWidth * 2), (int) (imageHeight * 2), BufferedImage.TYPE_INT_RGB);
    }
    
    public BufferedImage getScaledDownBufferedImage() {
    	return new BufferedImage((int) (imageWidth * (1/2)), (int) (imageHeight * (1/2)), BufferedImage.TYPE_INT_RGB);
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