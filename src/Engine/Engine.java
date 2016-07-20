package Engine;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Bullet.Bullet;
import Characters.Character;
import Characters.Enemy;
import Characters.Enemy2;
import Characters.Enemy3;
import Characters.Player;

public class Engine {
	private static final int PLAYER_START_X = 0;
	private static final int PLAYER_START_Y = 500 - 68; //600 - 68 - 32; //68 = heightOfPlayer & 32 = height of footer	
	
	private static final int brickWidth = Character.IMG_WIDTH;
	private static final int brickHeight = Character.IMG_HEIGHT;
	
	private Random r;
	private static Player player;
	private List<Enemy> enemies;
	private boolean endOfGame;
	private static int level;
	private static List<Rectangle> levelObstacles;
	
	private ArrayList<Thread> threads;
	
	public Engine(int lvl){
		r = new Random();
		level = lvl;
		levelObstacles = new ArrayList<Rectangle>(); 
		init(level);
	}
		

	// CITOVANJE :D
	
	public void killAllEnemies(){
		enemies.clear();
	}
	
	public void killAllThreads(){
		threads.clear();
	}
	
	
	/// ------------------ KEEP PLAYER WITHIN BORDERS ------------
		
	public void keepPlayerWithinBorders(Character player){  /// BAGUJE MASU 
		
		System.out.println("player.getCurrentY() = " + player.getCurrentY());
		
		Rectangle playerRectanle = new Rectangle(player.getCurrentPosition(), new Dimension(Character.getImgWidth(), Character.getImgWidth()));
		for (Rectangle obstacle : levelObstacles) {
			
			Direction direction = overlapsFromLeftOrRight(playerRectanle, obstacle);
			if (direction != Direction.NOTHING){
				if (direction == Direction.LEFT){
					if ((int)obstacle.getX() - Character.IMG_WIDTH >= 0 )
						player.setCurrentPosition(new Point((int)obstacle.getX() - Character.getImgWidth(), player.getCurrentY()));
					System.out.println("obstacle.getX() - Character.getImgWidth() = " + ((int)obstacle.getX() - Character.getImgWidth()) + " player.getCurrentY() = " + player.getCurrentY());
				}else if(direction == Direction.RIGHT){
					if ((int)obstacle.getX() + brickHeight + Character.IMG_WIDTH <= Character.SCREEN_WIDTH)
						player.setCurrentPosition(new Point((int)obstacle.getX() + brickWidth, player.getCurrentY()));
				}else{
					//System.out.println("Sve regularno!");
				}
			}
		
			
			
			//if (player instanceof Player){  /// ZATO STO ENEMIES MOGU SAMO LEVO I DESNO DA IDU, NEMA POTREBE da se ovo izvrsava
				direction = overlapsFromUpOrDown(playerRectanle, obstacle);
				if (direction != Direction.NOTHING){
										
					if (direction == Direction.DOWN){
						// MOZDA TREBA NEKI IF za proveru da li je izzasao van okvira ekrana 800 * 568
						
						if ((int)obstacle.getY() + brickHeight <= PLAYER_START_Y)
							player.setCurrentPosition(new Point(player.getCurrentX(), (int)obstacle.getY() + Character.getImgWidth()));
						
					}else if(direction == Direction.UP){
						if ((int)obstacle.getY() >= 0)
							player.setCurrentPosition(new Point(player.getCurrentX(), (int)obstacle.getY() - Character.getImgWidth()));
					}else{
						//System.out.println("Sve regularno!");
					}
				}
			//}
		
		}
	}
	
	private Direction overlapsFromUpOrDown(Rectangle player, Rectangle obstacle){ // obstacles && player
		// plejer prilazi dodozdo prepreci
		// O
		// ^
		// |
		// P
		
		if ((int)player.getY() <= (int)obstacle.getY() + brickHeight){
			if (player.intersects(obstacle)){
				System.out.println("Direction.DOWN");
				return Direction.DOWN;
			}
		}
		// plejer prilazi odozgo prepreci
		// P
		// |
		// -
		// O
		
		if ((int)player.getY() + Player.getHeightOfPlayer() >= (int)obstacle.getY()){
			if (player.intersects(obstacle)){
				System.out.println("Direction.UP");
				return Direction.UP;
			}
		}
		
		return Direction.NOTHING;
	}
	
	private Direction overlapsFromLeftOrRight(Rectangle player, Rectangle obstacle){ // obstacles && player
		// plejer prilazi prepreci sa leve strane 
		// P -> O
		if ((int)player.getX() + Character.IMG_WIDTH >= (int)obstacle.getX()){
			if (player.intersects(obstacle)){
				System.out.println("preseca sa leve1");
				return Direction.LEFT;
			}
		}
		// plejer prilazi prepreci sa desne strane 
		// O <- P
		if ((int)player.getX() <= (int)obstacle.getX() + brickWidth){
			if (player.intersects(obstacle)){
				System.out.println("preseca sa desne 2");
				return Direction.RIGHT;
			}
		}
		return Direction.NOTHING;
	}
	

	
	/// ------------------ END OF KEEP PLAYER WITHIN BORDERS ------------
	

	
	class POKUSAJ_____JUMP {
		//----------------------JUMP--------------------------------------------------------
			Point velocity = new Point(10, 10);
			Point gravity = new Point(0, 10);
			double previousTime = 0;
			double currentTime = getCurrentTime();
			boolean onGround = true;
					
			public void jump(){
				onGround = true;
				while (onGround == true){
					/*
					previousTime = currentTime;
					currentTime = getCurrentTime();
					
					System.out.println(previousTime - currentTime);
					
					double dt = currentTime - previousTime;
					
					if (dt > 0.15f){
						dt = 0.15f;
					}
					*/
					updateJump(0.03f);
				}
			}
			
			public void updateJump(double dt){
				double x = player.getCurrentX() + velocity.getX() * dt;
				double y = player.getCurrentY() + velocity.getY() * dt;
				
				if (player.getCurrentY() + (int)y >= 800){
					onGround = true;
				}
				velocity.translate(gravity.x, gravity.y);
				player.getCurrentPosition().setLocation(x, y);
				
				System.out.println("(x,y) = ( " + x + ", " + y + ")");
				
				keepPlayerWithinBorders(player);
				System.out.println("izasao");
				
			}
			
			public double getCurrentTime(){
				return System.currentTimeMillis();
			}
			
			
			
			public boolean isOnGround() {
				return onGround;
			}
			
			//-----------------------END OF JUMP------------------------------------------
	
	}

	
	
	
	public void init(int level){
		if (level == 1){
			endOfGame = false;
			init1();
		}else if (level == 2){
			init2();
		}else if (level == 3){
			init3();
		}else{
			System.out.println("END OF GAME");
		}
	}
	
	public void baseInitForObstacles(){
		int k = Character.SCREEN_WIDTH / Character.IMG_WIDTH + 1;
		
		for (int i = 0; i < k; i++) {
			Rectangle tmpRectangle = null;
			tmpRectangle = new Rectangle(new Point(i*brickWidth, 500), new Dimension(brickWidth, brickHeight));
			levelObstacles.add(tmpRectangle);
		}
		
	}
	
	private void init1() {
		player = new Player(new Point(PLAYER_START_X, PLAYER_START_Y), 100, 4f, 3, false);
		
		enemies = new ArrayList<Enemy>();
		
		enemies.add(new Enemy(new Point(300, 500-68))); // zeleni 
		enemies.get(enemies.size()-1).setEnemyMoveBorders(0, 800);
		
		enemies.add(new Enemy(new Point(600, 500-68)));	// zeleni
		enemies.get(enemies.size()-1).setEnemyMoveBorders(0, 800);
		
		enemies.add(new Enemy2(new Point(416, 500 - 68*4))); // crveni
		enemies.get(enemies.size()-1).setEnemyMoveBorders(320, 512);
		
		enemies.add(new Enemy(new Point(128, 500 - 68*5))); 	// zeleni
		enemies.get(enemies.size()-1).setEnemyMoveBorders(64, 192);
		
		enemies.add(new Enemy3(new Point(656, 500 - 68*7)));	// plavi
		enemies.get(enemies.size()-1).setEnemyMoveBorders(512, 750);
		
		initObstaclesLevelOne();
		
		threads = new ArrayList<Thread>();
	
		startThreads();
	}

	public void initObstaclesLevelOne(){
		levelObstacles.clear();
		baseInitForObstacles();
		
		for (int i = 1; i < 4; i++) {
			Rectangle tmpRectangle = null;
			tmpRectangle = new Rectangle(new Point(i*brickWidth, 500 - brickHeight *4), new Dimension(brickWidth, brickHeight));
			levelObstacles.add(tmpRectangle);
		}
		
		for (int i = 5; i < 9; i++) {
			Rectangle tmpRectangle = null;
			tmpRectangle = new Rectangle(new Point(i*brickWidth, 500 - brickHeight *3), new Dimension(brickWidth, brickHeight));
			levelObstacles.add(tmpRectangle);
		}
		
		for (int i = 8; i < 13; i++) {
			Rectangle tmpRectangle = null;
			tmpRectangle = new Rectangle(new Point(i*brickWidth, 500 - brickHeight *6), new Dimension(brickWidth, brickHeight));
			levelObstacles.add(tmpRectangle);
		}
	}
	
	private void init2() {
		player.setCurrentPosition(new Point(PLAYER_START_X, PLAYER_START_Y));
		player.getFiredBullets().clear();
		
		enemies.clear();
		
		enemies.add(new Enemy2(new Point(450, 500-68))); // crveni
		enemies.get(enemies.size()-1).setEnemyMoveBorders(0, 600);
		
		enemies.add(new Enemy3(new Point(700, 500-68))); // plavi
		enemies.get(enemies.size()-1).setEnemyMoveBorders(50, 750);
		
		enemies.add(new Enemy2(new Point(0, 500 - 68*3))); // crveni
		enemies.get(enemies.size()-1).setEnemyMoveBorders(0, 192);
		
		enemies.add(new Enemy2(new Point(620, 500 - 68*4))); // crveni
		enemies.get(enemies.size()-1).setEnemyMoveBorders(576, 704);
		
		enemies.add(new Enemy(new Point(192, 500 - 68*5))); // zeleni
		enemies.get(enemies.size()-1).setEnemyMoveBorders(192, 320);
		
		enemies.add(new Enemy3(new Point(45, 500 - 68*7))); // plavi
		enemies.get(enemies.size()-1).setEnemyMoveBorders(0, 192);
		
		enemies.add(new Enemy2(new Point(448, 500 - 68*7))); // crveni
		enemies.get(enemies.size()-1).setEnemyMoveBorders(448, 640);
		
		initObstaclesLevelTwo();
		
		threads.clear();
		
		startThreads();
		
	}
	
	private void initObstaclesLevelTwo() { 
		levelObstacles.clear();
		baseInitForObstacles();
		
		for (int i = 0; i < 3; i++) {
			Rectangle tmpRectangle = null;
			tmpRectangle = new Rectangle(new Point(i*brickWidth, 500 - brickHeight *2), new Dimension(brickWidth, brickHeight));
			levelObstacles.add(tmpRectangle);
		}
		
		for (int i = 9; i < 12; i++) {
			Rectangle tmpRectangle = null;
			tmpRectangle = new Rectangle(new Point(i*brickWidth, 500 - brickHeight *3), new Dimension(brickWidth, brickHeight));
			levelObstacles.add(tmpRectangle);
		}
		
		for (int i = 3; i < 6; i++) {
			Rectangle tmpRectangle = null;
			tmpRectangle = new Rectangle(new Point(i*brickWidth, 500 - brickHeight *4), new Dimension(brickWidth, brickHeight));
			levelObstacles.add(tmpRectangle);
		}
		
		for (int i = 0; i < 4; i++) {
			Rectangle tmpRectangle = null;
			tmpRectangle = new Rectangle(new Point(i*brickWidth, 500 - brickHeight *6), new Dimension(brickWidth, brickHeight));
			levelObstacles.add(tmpRectangle);
		}
		
		for (int i = 7; i < 11; i++) {
			Rectangle tmpRectangle = null;
			tmpRectangle = new Rectangle(new Point(i*brickWidth, 500 - brickHeight *6), new Dimension(brickWidth, brickHeight));
			levelObstacles.add(tmpRectangle);
		}
		
	}

	private void init3() {
		player.setCurrentPosition(new Point(5 * 64, PLAYER_START_Y));
		player.getFiredBullets().clear();
		
		enemies.clear();
		
		enemies.add(new Enemy2(new Point(128, 500 - 68))); // crveni
		enemies.get(enemies.size()-1).setEnemyMoveBorders(64, 750);
		
		enemies.add(new Enemy3(new Point(600, 500 - 68*3))); // plavi
		enemies.get(enemies.size()-1).setEnemyMoveBorders(512, 640);
		
		enemies.add(new Enemy2(new Point(690, 500 - 68*3))); // crveni
		enemies.get(enemies.size()-1).setEnemyMoveBorders(640, 704);
		
		enemies.add(new Enemy3(new Point(64, 500 - 68*3))); // plavi
		enemies.get(enemies.size()-1).setEnemyMoveBorders(64, 192);
		
		enemies.add(new Enemy(new Point(0, 500 - 68*4))); // zeleni
		enemies.get(enemies.size()-1).setEnemyMoveBorders(0, 0);
		
		enemies.add(new Enemy2(new Point(320, 500 - 68*4))); // crveni
		enemies.get(enemies.size()-1).setEnemyMoveBorders(256, 384);
		
		enemies.add(new Enemy3(new Point(250, 500 - 68*6))); // plavi
		enemies.get(enemies.size()-1).setEnemyMoveBorders(128, 448);
		
		enemies.add(new Enemy(new Point(140, 500 - 68*6))); // zeleni
		enemies.get(enemies.size()-1).setEnemyMoveBorders(128, 250);
		
		enemies.add(new Enemy3(new Point(64, 500 - 68*7))); // plavi
		enemies.get(enemies.size()-1).setEnemyMoveBorders(64, 64);
		
		enemies.add(new Enemy2(new Point(530, 500 - 68*7))); // crveni
		enemies.get(enemies.size()-1).setEnemyMoveBorders(512, 640);
		
		initObstaclesLevelThree();
		
		threads.clear();
		
		startThreads();
		
	}
		
	private void initObstaclesLevelThree() {
		levelObstacles.clear();
		baseInitForObstacles();
		
		for (int i = 1; i < 12; i++) {
			if (i < 4 || i > 7){
				Rectangle tmpRectangle = null;
				tmpRectangle = new Rectangle(new Point(i*brickWidth, 500 - brickHeight *2), new Dimension(brickWidth, brickHeight));
				levelObstacles.add(tmpRectangle);
			}
		}
		
		for (int i = 0; i < 7; i++) {
			if (i == 0 || i > 3){
				Rectangle tmpRectangle = null;
				tmpRectangle = new Rectangle(new Point(i*brickWidth, 500 - brickHeight *3), new Dimension(brickWidth, brickHeight));
				levelObstacles.add(tmpRectangle);
			}
		}
		
		for (int i = 1; i < 8; i++) {
			Rectangle tmpRectangle = null;
			tmpRectangle = new Rectangle(new Point(i*brickWidth, 500 - brickHeight *5), new Dimension(brickWidth, brickHeight));
			levelObstacles.add(tmpRectangle);
		}
		
		for (int i = 1; i < 11; i++) {
			if (i == 1 || i > 7){
				Rectangle tmpRectangle = null;
				tmpRectangle = new Rectangle(new Point(i*brickWidth, 500 - brickHeight *6), new Dimension(brickWidth, brickHeight));
				levelObstacles.add(tmpRectangle);
			}
		}
	}

	private void startThreads(){
		for (int i = 0; i < enemies.size(); i++) {
		    Thread enemyThread = new Thread(enemies.get(i));
		    threads.add(enemyThread);
		    enemyThread.start();
		 }
	}
	
	public void stopThreads(){ 	/// PROVERITI DA LI RADI
		for (Thread thread : threads) {
			if (thread.isAlive()){
				Thread.currentThread().interrupt();
				thread.interrupt();
			}
		}
	}
	
	
	public boolean isWin(){
		if (level > 3) return true;
		return false;
	}
	
	public boolean isEnd(){
		if (endOfGame == true){
			return true;
		}
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
								return isCollisionWithBullet();
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
		Rectangle rectanglePlayer = new Rectangle(player.getCurrentX(), player.getCurrentY(), Player.getWidthOfPlayer(), Player.getHeightOfPlayer());
		Rectangle enemyRectangle;
		boolean collisionHappend = false;
		
		for (int i = 0; i < enemies.size(); i++) {
			Enemy tmpEnemy = enemies.get(i);
			if (tmpEnemy != null){
				enemyRectangle = new Rectangle(tmpEnemy.getCurrentX(), tmpEnemy.getCurrentY(), tmpEnemy.getWidthOfEnemy(), tmpEnemy.getHeightOfEnemy());
				if (overlaps(rectanglePlayer, enemyRectangle) == true){
					collisionHappend = true;
					
					tmpEnemy.collisonWithPlayer(collisionHappend);  // skida enemy-ju helth
					
					if (tmpEnemy.isEnemyKilled() == true){
				
						enemies.set(i, null);   // setujem ubijenog enemy-ja na null
						threads.get(i).interrupt();
						System.out.println("enemies.set(i,null); --> " + enemies.get(i));
						System.out.println("threads.get(i).interrupt(); --> " + threads.get(i).isInterrupted());
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
				}
			}
		}	
		if (collisionHappend == true) return true; // desio se sudar player-a i enemy-ja
		return false;
	}
	

	public void bulletOverlapsWithObstacle(){
		List<Bullet> firedBullets = player.getFiredBullets();

		for (int j = 0; j < firedBullets.size(); j++) {
			Bullet bullet = firedBullets.get(j);
			if (bullet != null){
				Rectangle bulletRectangle = new Rectangle(new Point(bullet.getBulletPosition().getLocation()), new Dimension(Bullet.BULLET_WIDTH, Bullet.BULLET_HEIGHT));					
				for (int i = 0; i < levelObstacles.size(); i++) {
					if (levelObstacles.get(i) != null){
						Rectangle obstacleRectangle = new Rectangle(levelObstacles.get(i).getLocation(), new Dimension(brickWidth, brickHeight));
						if (overlapsFromLeftOrRight(bulletRectangle, obstacleRectangle) != Direction.NOTHING){
							//System.out.println("Metak udario u zid!");
							player.destroyBulletWhenHitObstacle(j);
						}
					}
				}
			}
		}
		
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
		
		//System.out.println("moreToKill() = " + numberOfKills);
		
		if (numberOfKills == enemies.size()) return false; // nema vise enemy-ja (moze boss da se pojavi) ili odmah na nextLevel
		return true; // imas jos da teglis :D
	}

	public static Player getPlayer() {
		return player;
	}

	public List<Enemy> getEnemies() {
		return enemies;
	}

	public static int getLevel() {
		return level;
	}
	
	public static List<Rectangle> getLevelObstacles() {
		return levelObstacles;
	}
	
	
}
