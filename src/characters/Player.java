package characters;

import java.util.List;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import engine.Direction;
import bullet.Bullet;


public class Player extends Character{
	public static final int STEP = 64/2; 
	public static final int JUMP = 68;
	
	private static Point move;	// za kretanje: Left, Right, Up, Down
	private static int x;		
	private static int y;		
	private static Point moveBullet;	// za kretanje bullet-a
	private static int xBullet;
	private static int yBullet;
	
	private BufferedImage frontImg, leftImg, rightImg;
	private List<Bullet> firedBullets;
	
	public Player(Point startPosition, int health, double speed, int numberOfLives, boolean collision) {
		super(startPosition, health, speed, numberOfLives, collision);
		frontImg = setImage("player");
		leftImg = setImage("playerLeft");
		rightImg = setImage("playerRight");
		super.image = frontImg;
		firedBullets = new ArrayList<Bullet>();
	}	
		
	
	@Override
	public void moveLeft() {
		move = super.getCurrentPosition();
		x = (int)move.getX();
		y = (int)move.getY();
		
		if (x - STEP >= 0){
			move.setLocation(x - STEP , y);
		}
		super.setCurrentPosition(move);
	}

	@Override
	public void moveRight() {		
		move = super.getCurrentPosition();
		x = (int)move.getX();
		y = (int)move.getY();
		
		if (x + STEP + Player.IMG_WIDTH <= getWidth()){   
			move.setLocation(x + STEP , y);
		}
		
		super.setCurrentPosition(move);

	}
	
	public void moveUp() {
		move = super.getCurrentPosition();
		x = (int)move.getX();
		y = (int)move.getY();
		
		if (y - JUMP >= 0){  
			move.setLocation(x , y - JUMP);
		}
		
		super.setCurrentPosition(move);
	}
	
	public void moveDown() {
		move = super.getCurrentPosition();
		x = (int)move.getX();
		y = (int)move.getY();
		
		if (y + JUMP + getHeightOfPlayer() <= getHeigt() - 32){  
			move.setLocation(x , y + JUMP);
		}
		
		super.setCurrentPosition(move);
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
	
	public void setImageByString(String imageString){
		if (imageString == "player"){
			super.image = frontImg;
		}else if(imageString == "playerLeft"){
			super.image = leftImg;
		}else{
			super.image = rightImg;
		}
	}
	
	public void fire(Direction direction){
		Bullet bullet = new Bullet(this.getCurrentPosition());
		firedBullets.add(bullet);
	    bullet.spawnBullet(direction);
	}
	
	public void destroyBulletWhenHitObstacle(int indexOfBullet) {
		firedBullets.set(indexOfBullet, null);
	}
	
	public void destroyBullets(){
	/*
		Metoda koja sluzi da ukloni metak iz liste ispaljenih metkova ako taj metak izadje iz okvira ekrana (800 x 600)
	*/
		
		for (int i = 0; i < firedBullets.size(); i++) {
			if (firedBullets.get(i) != null){
				Bullet tmpBullet = firedBullets.get(i);
				if (tmpBullet.getBulletPosition().getX() >= Character.SCREEN_WIDTH || tmpBullet.getBulletPosition().getX() <= 0)
					firedBullets.remove(i);
			}
		}
	}
		
	public void moveBullets(){		
	/*
		Metoda koja sluzi da pomera sve metkove u odredjenom pravcu, koje je player ispalio.
	*/
		for (Bullet bullet : firedBullets) {
			if (bullet != null){
				moveBullet = new Point(bullet.getBulletPosition());
				xBullet = (int)moveBullet.getX();
				yBullet = (int)moveBullet.getY();
				
				if (bullet.isLeft() == true){ // kad metkovi idu u levo
					if (xBullet - Bullet.BULLET_SPEED >= 0 - Bullet.BULLET_WIDTH ){
						moveBullet.setLocation(xBullet - Bullet.BULLET_SPEED , yBullet);
					}else{
						bullet = null;
					}
				}else{	// kad metkovi idu u desno
					if (xBullet + Bullet.BULLET_SPEED - Bullet.BULLET_WIDTH <= getWidth()){  
						moveBullet.setLocation(xBullet + Bullet.BULLET_SPEED, yBullet);
					}else{
						bullet = null;
					}
				}
				
				if (bullet != null) bullet.getBulletPosition().setLocation(moveBullet);
			}
		}
	}
	
	public boolean collisionWithEnemy(boolean collisionHappend){
	/*
		Metoda koja sluzi da smanji player-u health nakon sto se desila kolizija 
		playera i nekog od enemy-ja. Takodje ova metoda vodi racuna i o tome koliko je zivota
		player-u  ostalo. Ako je broj zivota == 0 i health == 0, tada je kraj igre i metoda vraca
		true, u suprotnom igra se nastavlja i metoda vraca false
	*/
		boolean endOfGame = false;
		setHealth(getHealth() - (int)(getHealth() * 0.1) - 1);
		
		if (getHealth() <= 0){
			int numOfLives = getNumberOfLives();
			// DEBUG
			//System.out.println("Ostalo zivota: " + numOfLives);
			if ( numOfLives > 0){
				setHealth(100); // ponovo ima 100% health-a
				--numOfLives;
				setNumberOfLives(numOfLives); // smanjio mu se jedan zivot
			}else{
				endOfGame = true;
			}
		}
		
		return endOfGame;
	}
	
	public void hide(){
		/* Metoda hide() sluzi samo da promeni koordinate player-a i time skloni player-a sa ekrana */
		setCurrentPosition(new Point(1000, 1000));
	}
	
	public List<Bullet> getFiredBullets() {
		return firedBullets;
	}

	public static int getWidthOfPlayer() {
		return Player.IMG_WIDTH;
	}

	public static int getHeightOfPlayer() {
		return Player.IMG_HEIGHT;
	}
}
