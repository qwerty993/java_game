package characters;

import java.awt.Point;
import java.awt.image.BufferedImage;

public abstract class Character {
	public static final int SCREEN_WIDTH = 800;	// SVGA resolution 800x600
	public static final int SCREEN_HEIGHT = 600;
	
	public static final int IMG_WIDTH = 64;
	public static final int IMG_HEIGHT = 68;
	
	// double speed NE KORISTIM NIGDE, IMA DOSTA PREPRAVKE
	
	private int health;
	private double speed;
	private int numberOfLives;
	private boolean collision;
	private Point currentPosition;
    public BufferedImage image;
	
	public Character(Point startPosition, int health, double speed, int numberOfLives, boolean collision) {
		super();
		this.currentPosition = startPosition;
		this.health = health;
		this.speed = speed;
		this.numberOfLives = numberOfLives;
		this.collision = collision;
	}
	
	protected abstract void moveLeft();
	protected abstract void moveRight();

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public int getNumberOfLives() {
		return numberOfLives;
	}

	public void setNumberOfLives(int numberOfLives) {
		this.numberOfLives = numberOfLives;
	}

	public boolean isCollision() {
		return collision;
	}

	public void setCollision(boolean collision) {
		this.collision = collision;
	}

	public Point getCurrentPosition() {
		return currentPosition.getLocation();
	}

	public void setCurrentPosition(Point newCurrentPosition) {
		this.currentPosition.setLocation(newCurrentPosition);
	}
	
	public int getCurrentX(){
		return (int)currentPosition.getX();
	}
	
	public int getCurrentY(){
		return (int)currentPosition.getY();
	}
	
	public static int getWidth() {
		return SCREEN_WIDTH;
	}

	public static int getHeigt() {
		return SCREEN_HEIGHT;
	}

	public static int getImgWidth() {
		return IMG_WIDTH;
	}

	public static int getImgHeight() {
		return IMG_HEIGHT;
	}

	public abstract BufferedImage setImage(String image);
	
	public BufferedImage getImage() {
		return image;
	}
	
}
