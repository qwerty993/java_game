package Engine;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Sound.Sounds;
import Bullet.Bullet;
import Characters.Character;
import Characters.Enemy;
import Characters.Enemy2;
import Characters.Enemy3;
import Characters.Player;

public class Engine {
	private static final int PLAYER_START_X = 0;
	private static final int PLAYER_START_Y = 600 - 68 - 32; //68 = heightOfPlayer & 32 = height of footer	
	
	Random r;
	Player player;
	List<Enemy> enemies;
	boolean endOfGame;
	int level;
	
	public Engine(int level){
		r = new Random();
		endOfGame = false;
		this.level = level;
		
		init(level);
	}
	
	public void init(int level){
		if (level == 1){
			init1();
		}else if (level == 2){
			init2();
		}else if (level == 3){
			init3();
		}else{
			System.out.println("END OF GAME");
		}
	}
	
	
	private void init1() {
		this.player = new Player(new Point(PLAYER_START_X, PLAYER_START_Y), 100, 4f, 3, false);
		
		enemies = new ArrayList<Enemy>();
		enemies.add(new Enemy(new Point(800 - 64 - 100, 500-68)));
		enemies.add(new Enemy2(new Point(800 - 64*2 -100 + 60*2, 500 - 68*2)));
		enemies.add(new Enemy3(new Point(800 - 64*3 - 100, 500 - 68*3)));
		enemies.add(new Enemy(new Point(800 - 64*4 - 100, 500 - 68*4)));
		
		for (Enemy enemy : enemies) {
			Thread enemyThreads = new Thread(enemy);
			enemyThreads.start();
			System.out.println("Enemy activated!");
		}
		
	}

	private void init2() {
		// TODO Auto-generated method stub
		
	}

	
	private void init3() {
		// TODO Auto-generated method stub
		
	}
	
	public boolean isWin(){
		if (level > 3) return true;
		return false;
	}
	
	public boolean isEnd(){
		if (endOfGame == true) return true;
		return false;
	}
	
	public boolean isNextLevel(){
		if (moreToKill() == true) return false;
		else{
			level++;
			if (isWin() == true){
				System.out.println("Pobedio si!");
				// MOZE DA SE UBACI WINER SCREEN I CESTITKE, NOVA PARTIJA DA/NE?
				return false;
			}
			return true;
		}
	}
	
	public boolean overlaps(Rectangle player, Rectangle enemy){
		return player.getX() < enemy.getX() + enemy.getWidth() && player.getX() + player.getWidth() > enemy.getX() && player.getY() < enemy.getY() + enemy.getHeight() && player.getY() + player.getHeight() > enemy.getY();  
	}
	
	public boolean isCollisionWithBullet(){ // KOLIZIJA IZMEDJU ENEMY-ja i BULLET-a 
		Rectangle bulletRectangle;
		Rectangle enemyRectangle;
		List<Bullet> firedBullets = player.getFiredBullets();
		Enemy enemy;
		Bullet bullet;
		boolean collisionHappend = false;
		
		for (int i = 0; i < enemies.size(); i++) {
			enemy = null;
			enemy = enemies.get(i);
			
			if (enemy != null){
				enemyRectangle = new Rectangle(new Point(enemy.getCurrentPosition()), new Dimension(Character.IMG_WIDTH, Character.IMG_HEIGHT));
				
				for (int j = 0; j < firedBullets.size(); j++) {
					bullet = null;
					bullet = firedBullets.get(j);
					
					if (bullet != null){	
						bulletRectangle = new Rectangle(new Point(bullet.getBulletPosition()), new Dimension(Bullet.BULLET_WIDTH, Bullet.BULLET_HEIGHT));
						if (overlaps(bulletRectangle, enemyRectangle) == true){
							//Sounds.HIT.play();
							collisionHappend = true;
							bullet.setBulletImage(null);
							enemy.collisonWithBullet(true);
													
							// OVAJ DEO UNISTI BULLET I ENEMYA (metkovi nisu mogli da predju mesto gde je nekada
							// bio enemy. sa ovim delom sam to resio.. sad metkovi dodju do kraja prozora)
							if (enemy.isEnemyKilled() == true){
								bullet = null;
								firedBullets.set(j, null);
								enemy = null;
								enemies.set(i, null);
								// dodat naknadno, jer su izlazile greske!
								isCollisionWithBullet();
							}
						}
					}
				}
			}
		}
		if (collisionHappend == true) return true; // desio se sudar bulleta i enemyja
		return false;
	}
	
	
	
	
	
	public boolean isCollision(){	// KOLIZIJA IZMEDJU PLAYER-A I ENEMY-JA 
		Rectangle rectanglePlayer = new Rectangle(player.getCurrentX(), player.getCurrentY(), player.getWidthOfPlayer(), player.getHeightOfPlayer());
		Rectangle enemyRectangle;
		boolean collisionHappend = false;
		
		for (int i = 0; i < enemies.size(); i++) {
			Enemy tmpEnemy = enemies.get(i);
			if (tmpEnemy != null){
				enemyRectangle = new Rectangle(tmpEnemy.getCurrentX(), tmpEnemy.getCurrentY(), tmpEnemy.getWidthOfEnemy(), tmpEnemy.getHeightOfEnemy());
				if (overlaps(rectanglePlayer, enemyRectangle) == true){
					collisionHappend = true;
					
					tmpEnemy.collisonWithPlayer(collisionHappend);
					
					if (tmpEnemy.isEnemyKilled() == true){
						enemies.remove(i); // izbacujem ubijenog enemy-ja iz liste
						
						// MOZDA UMESTO ENEMIES.REMOVE(I) DA PROBAM SA enemies.set(i, null);
						
						
						/* POKUSAO SAM DA PREKINEM TREAD KAD GA IZBACIM
						try {
							((Thread)enemies).join();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						*/
					}
					
					// preklapaju se enemy i player, player-u se smanjuje health 20%;
					player.setHealth(player.getHealth() - (int)(player.getHealth() * 0.2) - 1);
					
					if (player.getHealth() <= 0){
						int numOfLives = player.getNumberOfLives();
						System.out.println("Ostalo zivota: " + numOfLives);
						if ( numOfLives > 0){
							player.setHealth(100); // ponovo ima 100% health-a
							--numOfLives;
							player.setNumberOfLives(numOfLives); // smanjio mu se jedan zivot
						}else{
							endOfGame = true;
						}
					}
					/* OVAJ DEO SAM ISPOZIVAO U GUI-ju
					if (isEnd() == true){
						// Igrac nema vise zivota i health mu je 0%
						System.exit(0);
					}
					if (isWin() == true){
						// DORADITI
						System.out.println("Pobedio si!");
						System.exit(0);
					}
					*/
				}
			}
		}	
		if (collisionHappend == true) return true; // desio se sudar player-a i enemy-ja
		return false;
	}
	
	public boolean moreToKill(){
		int numberOfKills = 0;
		
		for (int i = 0; i < enemies.size(); i++) {
			if (enemies.get(i) == null){
				numberOfKills++;
			}else{
				Enemy tmpEnemy = enemies.get(i);
				
				if (r.nextInt(2) == 1){ // idi desno ako moze
					if (tmpEnemy.getCurrentX() + Enemy.SPEED <= Enemy.SCREEN_WIDTH)
						tmpEnemy.getCurrentPosition().setLocation(tmpEnemy.getCurrentX() + Enemy.SPEED, tmpEnemy.getCurrentY());
				}else{	// idi levo ako moze
					if (tmpEnemy.getCurrentX() - Enemy.SPEED >= 0){
						tmpEnemy.getCurrentPosition().setLocation(tmpEnemy.getCurrentX() - Enemy.SPEED, tmpEnemy.getCurrentY());
					}
				}
			}
		}
		
		if (numberOfKills == enemies.size()) return false; // nema vise enemy-ja (moze boss da se pojavi) ili odmah na nextLevel
		return true; // imas jos da teglis :D
	}

	
	public Player getPlayer() {
		return player;
	}

	public List<Enemy> getEnemies() {
		return enemies;
	}

	public int getLevel() {
		return level;
	}
	
	
	
}
