package Characters;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import Sound.Sounds;
import Bullet.Bullet;

public class Enemy extends Character implements Runnable{
	public static final int STEP = 8;
	public static final int SPEED = 10;
	
	private Random r;
	private BufferedImage enemyImgFirst;
	private BufferedImage enemyImgSecond;
	private boolean displayedFirst;
	private int heightOfEnemy;
	private int widthOfEnemy;
	private boolean enemyAlive;
	

	
	public Enemy(Point startPosition) {
		this(startPosition, Difficulty.EASY.getDiffLevel(), 4f, 1, false);
	}
	
	public Enemy(Point startPosition, int health, double speed, int numberOfLives, boolean collision) {
		this(startPosition, health, speed, numberOfLives, collision, "enemy11", "enemy12");
	}
	
	public Enemy(Point startPosition, int health, double speed, int numberOfLives, boolean collision, String firstIMG, String secondIMG) {
		super(startPosition, health, speed, numberOfLives, collision);
		r = new Random();
		enemyImgFirst = setImage(firstIMG);
		enemyImgSecond = setImage(secondIMG);
		super.image = enemyImgFirst;
		displayedFirst = true;
		enemyAlive = true;
		
		heightOfEnemy = Character.IMG_HEIGHT;
    	widthOfEnemy = Character.IMG_WIDTH;
	}

	@Override
	public void run() {
		while (enemyAlive == true){
			if (r.nextInt(2) == 0){
				try {
					Thread.sleep(r.nextInt((SPEED * (int)getSpeed()) * 2));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				moveLeft();
			}else{
				try {
					Thread.sleep(r.nextInt((SPEED * (int)getSpeed()) * 3));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				moveRight();
			}
		}
		
	}
	
	public void collisonWithBullet(boolean collisionBetweenEnemyAndBullet){
		if (collisionBetweenEnemyAndBullet == true){
			this.setCollision(true);
			int currentHealth = getHealth() - Bullet.BULLET_DAMAGE - r.nextInt(Bullet.BULLET_DAMAGE);
			
			/* DEBUG
			System.out.println("Sudario se metak sa enemijem");
			System.out.println("currentHEALTHfromBULLET: " + currentHealth);
			*/
			
			if (currentHealth > 0){
				setHealth(currentHealth);
				setCollision(false);
				enemyAlive = true;	// this enemy je ziv
			}else{
				//Sounds.EXPLOSION.play();
				setHealth(0);
				setNumberOfLives(0);
				enemyAlive = false;	// this enemy je mrtav
			}
		}
	}
	
	public void collisonWithPlayer(boolean collisionBetweenPlayerAndEnemy){ 
		if (collisionBetweenPlayerAndEnemy == true){
			this.setCollision(true);
			int currentHealth = getHealth() - (int)(getHealth() * 0.15) - 1;
			
			/* DEBUG
			System.out.println("Sudario se PLEJER sa enemijem");
			System.out.println("currentHEALTH: " + currentHealth);
			*/
			
			if (currentHealth > 0){
				setHealth(currentHealth);
				setCollision(false);
				enemyAlive = true;	// this enemy je ziv
			}else{
				setHealth(0);
				setNumberOfLives(0);
				enemyAlive = false;	// this enemy je mrtav
			}
		}
	}
		
	@Override
	public void moveLeft() {
		Point moveLeft = new Point(super.getCurrentPosition());
		int x = (int)moveLeft.getX();
		int y = (int)moveLeft.getY();
		
		if (x - STEP * SPEED >= 0){
			moveLeft.setLocation(x - STEP , y);
		}
		
		super.setCurrentPosition(moveLeft);
	}

	@Override
	public void moveRight() {
		Point moveRight = new Point(super.getCurrentPosition());
		int x = (int)moveRight.getX();
		int y = (int)moveRight.getY();
		
		if (x + STEP * SPEED/2 + widthOfEnemy <= getWidth()){ 
			moveRight.setLocation(x + STEP , y);
		}
		
		super.setCurrentPosition(moveRight);

	}
	
	@Override
	public BufferedImage setImage(String imageStr) {
		try {
	    	// Working Directory - complete absolute path from where your application was initialized 
	    	// System.out.println("Working Directory: " + System.getProperty("user.dir"));
			BufferedImage image;
	    	String path = System.getProperty("user.dir") + "/bin/Resources/Images/" + imageStr + ".png";
	    	File file = new File(path);
	    	image = ImageIO.read(file);
	   
	    	return image;
	    	
	    } catch (IOException ioe) {
	    	ioe.printStackTrace();
	    }	
		return null;
	}
	
	public void setImage(boolean isFirst){
		if (isFirst == true)
			super.image = enemyImgFirst;
		else
			super.image = enemyImgSecond;
	}

	public BufferedImage getEnemyImg() {
		return super.getImage();
	}

	public void setEnemyImg(BufferedImage enemyImg) {
		super.image = enemyImg;
	}

	public boolean isDisplayedFirst() {
		return displayedFirst;
	}

	public void setDisplayedFirst(boolean displayedFirst) {
		this.displayedFirst = displayedFirst;
	}

	public int getHeightOfEnemy() {
		return heightOfEnemy;
	}

	public int getWidthOfEnemy() {
		return widthOfEnemy;
	}
		
	public boolean isEnemyAlive() {
		return enemyAlive;
	}

	public boolean isEnemyKilled(){
		if (this.isEnemyAlive() == true) return false;
		return true;
	}
	
	public void setEnemyAlive(boolean enemyAlive) {
		this.enemyAlive = enemyAlive;
	}
	
}
