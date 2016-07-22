package gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import brick_ladder.Brick;
import bullet.Bullet;
import characters.Enemy;
import characters.Player;
import engine.Engine;

public class Canvas extends JComponent {
	private static final long serialVersionUID = 1L;
	
	private Image backgroundImage;
	private List<Brick> obstacles;
	private Engine engine;

	
	public Canvas(Engine engine){
		setPreferredSize(new Dimension(800, 600 - 32));
		this.engine = engine;
	}
		
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);  					// paint parent's background
		g.drawImage(backgroundImage, 0, 0, null); 	// setujem background

		paintLevel(g);	
		paintPlayer(g);
		paintEnemies(g);
		paintBullets(g);
		
		repaint();
	}
	
	private void paintEnemies(Graphics g) {
		for (int i = 0; i < engine.getEnemies().size(); i++) {
			Enemy enemy = engine.getEnemies().get(i);
			if (enemy != null){
				enemy.setDisplayedFirst(!enemy.isDisplayedFirst()); // konstantno smenjujem 2 slike
	            enemy.setImage(enemy.isDisplayedFirst());
	            if (enemy.isEnemyAlive() == true){
	            	g.drawImage(enemy.getImage(), enemy.getCurrentX(), enemy.getCurrentY(), enemy.getWidthOfEnemy(), enemy.getHeightOfEnemy(), null);
	            }
			}
		}
	}

	private void paintPlayer(Graphics g) {
		g.drawImage(engine.getPlayer().getImage(), engine.getPlayer().getCurrentX(), engine.getPlayer().getCurrentY(), Player.getWidthOfPlayer(), Player.getHeightOfPlayer(), null);
	}

	private void paintLevel(Graphics g){
		obstacles = engine.getLevelObstacles();
		int level = engine.getLevel();
		readBackgroundImage(level);
		
		if (level < 4){
			for (Brick rectangle : obstacles) {
				if (rectangle.isBrick())
					g.drawImage(rectangle.getBrickImage(), (int)rectangle.getX(), (int)rectangle.getY(), null);
			}
		}
	}	
	
	private void paintBullets(Graphics g){
		Bullet tmpBullet = null;
		int k = engine.getPlayer().getFiredBullets().size();
				
		for (int i = 0; i < k; i++){
			tmpBullet = engine.getPlayer().getFiredBullets().get(i);
			if (tmpBullet != null){	
				if (tmpBullet.isLeft() == false){
					g.drawImage(tmpBullet.getBulletImage(), (int)tmpBullet.getBulletPosition().getX(), (int)tmpBullet.getBulletPosition().getY(), Bullet.BULLET_WIDTH, Bullet.BULLET_HEIGHT, null);
				}else{
					//g.drawImage(image, x + width, y, -width, height, null); 
					//  vertical flip:
					g.drawImage(tmpBullet.getBulletImage(), (int)tmpBullet.getBulletPosition().getX() + Bullet.BULLET_WIDTH, (int)tmpBullet.getBulletPosition().getY(), - Bullet.BULLET_WIDTH, Bullet.BULLET_HEIGHT, null);
				}
			}
		}
		repaint();		
	}	
	
	private void readBackgroundImage(int level){
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
}



