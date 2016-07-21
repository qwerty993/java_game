package gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import bullet.Bullet;
import characters.Enemy;
import characters.Player;
import engine.Engine;

public class Canvas extends JComponent {
	private static final long serialVersionUID = 1L;
	
	Image backgroundImage;
	Player player;
	List<Enemy> enemies;
	
	// BRICK IMAGE
	BufferedImage image;
	String path = System.getProperty("user.dir") + "/bin/Resources/Images/brick.jpg";
	File file = new File(path);
	// END OF BRICK IMAGE
	List<Rectangle> obstacles;
	
	
	public Canvas(Player player, List<Enemy> enemies){
		setPreferredSize(new Dimension(800, 600 - 32));
		this.player = player;
		this.enemies = enemies;
		
		readBrickImage();
		readBackgroundImage(1);
		
	}
	

	
	public void readBackgroundImage(int level){
		String fileName = null;
		
		switch (level) {
		case 1:
			fileName = "background_1.jpg";
			break;
		case 2:
			fileName = "background_2.png";
			break;
		case 3:
			fileName = "background_3.png";
			break;
		default:
			fileName = "gameOver.jpeg";
			break;
		}
		
		try{
			String path = System.getProperty("user.dir") + "/bin/Resources/Images/" + fileName;
			File file = new File(path);
			backgroundImage = ImageIO.read(file);
		}catch (IOException ioe) {
	    	ioe.printStackTrace();
	    }
	}

	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);  // paint parent's background

		g.drawImage(backgroundImage, 0, 0, null); // setujem background
	
		Graphics2D g2 = (Graphics2D) g;

		paintLevel(g);	// crta prepreke
		
		// prikazujem player-a
		g2.drawImage(player.getImage(), player.getCurrentX(), player.getCurrentY(), Player.getWidthOfPlayer(), Player.getHeightOfPlayer(), null);
				
		// prikazujem enemies
		for (int i = 0; i < enemies.size(); i++) {
			Enemy enemy = enemies.get(i);
			if (enemy != null){
				enemy.setDisplayedFirst(!enemy.isDisplayedFirst()); // konstantno smenjujem 2 slike
	            enemy.setImage(enemy.isDisplayedFirst());
				
	            if (enemy.isEnemyAlive() == true){
	            	g2.drawImage(enemy.getImage(), enemy.getCurrentX(), enemy.getCurrentY(), enemy.getWidthOfEnemy(), enemy.getHeightOfEnemy(), null);
	            }
			}
		}
		paintBullet(g);
		repaint();
	}
	
	public void paintLevel(Graphics g){
		int level = Engine.getLevel();
		obstacles = Engine.getLevelObstacles();
		
		if (level < 4){
			for (Rectangle rectangle : obstacles) {
				g.drawImage(image, (int)rectangle.getX(), (int)rectangle.getY(), null);
			}
		}else{
			// MOZE POBEDNICKI SCREEEN
		}
	}	
	
	public void paintBullet(Graphics g){ // prikazujem sve bullet-e
		Graphics2D g2 = (Graphics2D) g;
		Bullet tmpBullet = null;
		
		for (int i = 0; i < player.getFiredBullets().size(); i++){
			tmpBullet = player.getFiredBullets().get(i);
			if (tmpBullet != null){	
				if (tmpBullet.isLeft() == false){
					g2.drawImage(tmpBullet.getBulletImage(), (int)tmpBullet.getBulletPosition().getX(), (int)tmpBullet.getBulletPosition().getY(), Bullet.BULLET_WIDTH, Bullet.BULLET_HEIGHT, null);
				}else{
					//g2.drawImage(image, x + width, y, -width, height, null); //  vertical flip:
					g2.drawImage(tmpBullet.getBulletImage(), (int)tmpBullet.getBulletPosition().getX() + Bullet.BULLET_WIDTH, (int)tmpBullet.getBulletPosition().getY(), - Bullet.BULLET_WIDTH, Bullet.BULLET_HEIGHT, null);
				}
			}
		}
		repaint();		
	}
	
	
	
	public void readBrickImage(){
		try {
			image = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		
	
}



