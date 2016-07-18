package Characters;

import java.util.List;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Bullet.Bullet;


public class Player extends Character{
	public static final int STEP = 10;
	public static final int JUMP = STEP * 5;
	
	private int widthOfPlayer;
	private int heightOfPlayer;
	private BufferedImage frontImg, leftImg, rightImg;
	
	private List<Bullet> firedBullets = new ArrayList<Bullet>();
	
	public Player(Point startPosition, int health, double speed, int numberOfLives, boolean collision) {
		super(startPosition, health, speed, numberOfLives, collision);
		frontImg = setImage("player");
		leftImg = setImage("playerLeft");
		rightImg = setImage("playerRight");
		super.image = frontImg;
		
		heightOfPlayer = Character.IMG_HEIGHT;
    	widthOfPlayer = Character.IMG_WIDTH;
	}

	
	@Override
	public void moveLeft() {
		Point moveLeft = new Point(super.getCurrentPosition());
		int x = (int)moveLeft.getX();
		int y = (int)moveLeft.getY();
		
		if (x - STEP >= 0){
			moveLeft.setLocation(x - STEP , y);
		}
		
		super.setCurrentPosition(moveLeft);
	}

	@Override
	public void moveRight() {
		Point moveRight = new Point(super.getCurrentPosition());
		int x = (int)moveRight.getX();
		int y = (int)moveRight.getY();
		
		if (x + STEP + widthOfPlayer<= getWidth()){   // TREBA X + STEP + SIRINA_SLIKE <= GETWIDTH()
			moveRight.setLocation(x + STEP , y);
		}
		
		super.setCurrentPosition(moveRight);

	}

	public void moveUp() {
		Point moveUp = new Point(super.getCurrentPosition());
		int x = (int)moveUp.getX();
		int y = (int)moveUp.getY();
		
		if (y - JUMP >= 0){  
			moveUp.setLocation(x , y - JUMP);
		}
		
		super.setCurrentPosition(moveUp);
	}
	
	public void moveDown() {
		Point moveDown = new Point(super.getCurrentPosition());
		int x = (int)moveDown.getX();
		int y = (int)moveDown.getY();
		
		if (y + JUMP + getHeightOfPlayer() <= getHeigt() - 32){  
			//System.out.println(y + JUMP + getHeightOfPlayer()); // treba da ide do 568
			moveDown.setLocation(x , y + JUMP);
		}
		
		super.setCurrentPosition(moveDown);
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
	    	/*
	    	heightOfPlayer = image.getHeight();
	    	widthOfPlayer = image.getWidth(); 
	    	*/
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
	
	public void fire(String direction){
		Bullet bullet = new Bullet(this.getCurrentPosition());
		firedBullets.add(bullet);
	    bullet.spawnBullet(direction);
	}

	public Bullet destroyBullet(Bullet bullet) {
		boolean flag = false;
		if ((int)bullet.getBulletPosition().getX() + Bullet.BULLET_WIDTH + Bullet.BULLET_SPEED >= Character.SCREEN_WIDTH){
			bullet.getBulletPosition().setLocation(1000, 1000);
			flag = true;
		}
		if (flag) return null;
		return bullet;
	}
	
	public void moveBullets(){
		Point move;
		int x;
		int y;
		
		for (Bullet bullet : firedBullets) {
			
			// PROVERITI DA LI RADI U LEVO, POSTO NECE METKOVI DA SE KRECU U LEVO!!!
			if (bullet != null){
				if (bullet.isLeft() == true){ // kad metkovi idu u levo
					move = new Point(bullet.getBulletPosition());
					x = (int)move.getX();
					y = (int)move.getY();
					
					if (x - Bullet.BULLET_SPEED >= 0){
						move.setLocation(x - Bullet.BULLET_SPEED , y);
					}else{
						bullet = null;
					}
				}else{	// kad metkovi idu u desno
					
					move = new Point(bullet.getBulletPosition());
					x = (int)move.getX();
					y = (int)move.getY();
					
					if (x + Bullet.BULLET_SPEED + Bullet.BULLET_WIDTH <= getWidth()){   
						move.setLocation(x + Bullet.BULLET_SPEED, y);
					}else{
						bullet = null;
					}
				}
				
				if (bullet != null) bullet.getBulletPosition().setLocation(move);
			}
		}
	}
	
	
	
	/*
	public void moveBullets(){
		Point move;
		int x;
		int y;
		
		for (Bullet bullet : firedBullets) {
			
			// PROVERITI DA LI RADI U LEVO, POSTO NECE METKOVI DA SE KRECU U LEVO!!!
			if (bullet != null){
				if (bullet.isLeft() == true){ // kad metkovi idu u levo
					move = new Point(bullet.getBulletPosition());
					x = (int)move.getX();
					y = (int)move.getY();
					
					if (x - Bullet.BULLET_SPEED >= 0){
						move.setLocation(x - Bullet.BULLET_SPEED , y);
					}
					
					bullet.getBulletPosition().setLocation(move);
					
				}else{	// kad metkovi idu u desno
					
					move = new Point(bullet.getBulletPosition());
					x = (int)move.getX();
					y = (int)move.getY();
					
					if (x + Bullet.BULLET_SPEED + Bullet.BULLET_WIDTH <= getWidth()){   
						move.setLocation(x + Bullet.BULLET_SPEED, y);
					}
					
					bullet.getBulletPosition().setLocation(move);
				}
			}
		}
	}
	*/
	
	public List<Bullet> getFiredBullets() {
		return firedBullets;
	}
	
	public int getWidthOfPlayer() {
		return widthOfPlayer;
	}

	public int getHeightOfPlayer() {
		return heightOfPlayer;
	}
	
	
}
