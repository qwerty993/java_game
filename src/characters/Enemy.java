package characters;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;


//import Sound.Sounds;
import bullet.Bullet;

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
	
	private int borderLeft;
	private int borderRight;
	
	//private volatile boolean enemyAlive; // PROBA
	
	
	// obrisi posle, SLUZI SAMO ZA PROVERU
	static int brojEnemija = 0;
	int rbrEnemija;
	// ---------------------------------
	
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
    	
    	
    	//DEBUG
    	rbrEnemija = ++brojEnemija;
	}

	@Override
	public void run() {
		while (!Thread.currentThread().isInterrupted() && enemyAlive) {
			
		//while (enemyAlive == true){
		//while (!Thread.currentThread().isInterrupted()) {
		
		// random kretanje levo desno

			if (r.nextInt(2) == 0){
				try {
					//Thread.sleep(r.nextInt((SPEED * (int)getSpeed()) * 2));
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				moveLeft();
			}else{
				try {
					//Thread.sleep(r.nextInt((SPEED * (int)getSpeed()) * 3));
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				moveRight();
			}
		
		}
		
		//DEBUG
		System.out.println("ENEMY number: " + rbrEnemija + " --> DEAD");
		// ---------------------------------
	}
		

	
	// KOLIZIJA ENEMIJA I BULLETA NE RADI KAKO TREBA, NEKAD JAVI NULL_POINTER_EXCEPTION
	
	public void collisonWithBullet(boolean collisionBetweenEnemyAndBullet){
		if (collisionBetweenEnemyAndBullet == true){
			this.setCollision(true);
			int currentHealth = getHealth() - Bullet.BULLET_DAMAGE - r.nextInt(Bullet.BULLET_DAMAGE);
			
			// DEBUG
			System.out.println("bullet HIT ENEMY num: " + rbrEnemija);
			System.out.println("Enemy (" + rbrEnemija + ") current HEALTH after HIT : " + currentHealth);
			
			
			if (currentHealth > 0){
				setHealth(currentHealth);
				setCollision(false);
				enemyAlive = true;	// this enemy je ziv
			}else{
				// MORA PRVO ZVUK DA SE SREDI
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
		
	
	
	public void setEnemyMoveBorders(int leftX, int rightX){
		borderLeft = leftX;
		borderRight = rightX;
	}
	
	public int getBorderLeft() {
		return borderLeft;
	}

	public int getBorderRight() {
		return borderRight;
	}

	
	@Override
	public void moveLeft() {
		Point moveLeft = new Point(super.getCurrentPosition());
		int x = (int)moveLeft.getX();
		int y = (int)moveLeft.getY();
		
		if (x - STEP >= borderLeft + 10){ 		// >= 0
 			moveLeft.setLocation(x - STEP , y);
		}
		
		super.setCurrentPosition(moveLeft);
	}

	@Override
	public void moveRight() {
		Point moveRight = new Point(super.getCurrentPosition());
		int x = (int)moveRight.getX();
		int y = (int)moveRight.getY();
		
	
		if (x + STEP <= borderRight - 10){  // <= getWidth()
			moveRight.setLocation(x + STEP, y);
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
