package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import Bullet.Bullet;
import Characters.Enemy;
import Characters.Player;

public class Canvas extends JComponent {
	private static final long serialVersionUID = 1L;
	
	Image backgroundImage;
	Player player;
	List<Enemy> enemies;

	public Canvas(Player player, List<Enemy> enemies){
		setPreferredSize(new Dimension(800, 600 - 32));
		this.player = player;
		this.enemies = enemies;
		
		try{
			String path = System.getProperty("user.dir") + "/bin/Resources/Images/background_lvl1.jpg";
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
		
		paintLevelOne(g);
		
		// prikazujem player-a
		g2.drawImage(player.getImage(), player.getCurrentX(), player.getCurrentY(), player.getWidthOfPlayer(), player.getHeightOfPlayer(), null);
		
		//paintBullet(g);
		
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
	
	public void paintLevelOne(Graphics g){
		g.setColor(Color.RED);
		g.fillRect(0, player.getHeightOfPlayer(), 450, player.getHeightOfPlayer() - Player.JUMP); // player.getHeightOfPlayer() - Player.JUMP = 18
	}
	
	
	public void paintBullet(Graphics g){
		// prikazujem metak
		Graphics2D g2 = (Graphics2D) g;
		Bullet tmpBullet = null;
		
		for (int i = 0; i < player.getFiredBullets().size(); i++){
			tmpBullet = player.getFiredBullets().get(i);
			if (tmpBullet != null){	
				if (tmpBullet.isLeft() == false){
					g2.drawImage(tmpBullet.getBulletImage(), (int)tmpBullet.getBulletPosition().getX(), (int)tmpBullet.getBulletPosition().getY(), Bullet.BULLET_WIDTH, Bullet.BULLET_HEIGHT, null);
				}else{
					
					// OVAJ DEO NE RADI KAD TREBA DA IDE U LEVO 
					
					//g2.drawImage(image, x + width, y, -width, height, null); //  vertical flip:
					g2.drawImage(tmpBullet.getBulletImage(), (int)tmpBullet.getBulletPosition().getX() + Bullet.BULLET_WIDTH, (int)tmpBullet.getBulletPosition().getY(), - Bullet.BULLET_WIDTH, Bullet.BULLET_HEIGHT, null);
					System.out.println("idem u levo");
				}
			}
		}
		
		repaint();		
	}
}



