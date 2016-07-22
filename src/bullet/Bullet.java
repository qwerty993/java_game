package bullet;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import characters.Character;
import engine.Direction;

public class Bullet implements BulletInterface{
	public BufferedImage bulletImage;
	private Point bulletPosition;
	private boolean left;
	
	public Bullet(Point startPosition){
		bulletPosition = new Point(startPosition.x + 60, startPosition.y + 46);
		setBulletImage("bullet");
	}
	
	public void setBulletImage(String imageStr) {
		if (imageStr == null){
			bulletImage = null;
		}else{
			try {
		    	String path = System.getProperty("user.dir") + "/bin/Resources/Images/" + imageStr + ".png";
		    	File file = new File(path);
		    	bulletImage = ImageIO.read(file);	    	
		    } catch (IOException ioe) {
		    	ioe.printStackTrace();
		    }	
		}
	}

	@Override
	public void spawnBullet(Direction direction) {
		if (direction == Direction.RIGHT){
			if (bulletPosition.getX() + BULLET_SPEED <= Character.SCREEN_WIDTH){
				bulletPosition.setLocation((int)bulletPosition.getX() + BULLET_SPEED, (int)bulletPosition.getY());
				left = false;
			}
		}else if (direction == Direction.LEFT){
			if (bulletPosition.getX() - BULLET_SPEED >= 0){
				bulletPosition.setLocation((int)bulletPosition.getX() - BULLET_SPEED - BULLET_WIDTH - Character.IMG_WIDTH + 10, (int)bulletPosition.getY());
				left = true;
			}
		}
	}	

	public boolean equals(Bullet bullet) {
		if (this.getBulletPosition().getX() == bullet.getBulletPosition().getX() && this.getBulletPosition().getY() == bullet.getBulletPosition().getY())
			return true;
		return false;
	}

	public BufferedImage getBulletImage() {
		return bulletImage;
	}

	public int getWidthOfBullet() {
		return BULLET_WIDTH;
	}

	public int getHeightOfBullet() {
		return BULLET_HEIGHT;
	}

	public Point getBulletPosition() {
		return bulletPosition;
	}

	public boolean isLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}
	
}
