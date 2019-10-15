import java.awt.*;
import java.awt.geom.*;

public interface Sprite {
	
	public Shape getBoundingShape();
	
	public Image getImage();
	
	public Point2D.Double getXY();
	
	public double getX();
	
	public double getY();
	
	public int getHeight();
	
	public void setXY(Point2D.Double p);
	
	public void setX(double v);
	
	public void setY(double v);
	
	public void setHeight(int height);
	
	public void movingForward(boolean val);
	
	public void movingLeft(boolean val);
	
	public void movingRight(boolean val);
	
	public void movingBackward(boolean val);
	
	public boolean isMoving();
	
	public void move();
}