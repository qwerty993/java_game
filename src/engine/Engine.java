package engine;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import bullet.Bullet;
import characters.Character;
import characters.Enemy;
import characters.Enemy2;
import characters.Enemy3;
import characters.Player;

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
				
				keepPlayerWithinBorders(Direction.NOTHING);
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
		
		//ExecutorService service = Executors.newCachedThreadPool();
		//Future future;

		for (Thread thread : threads) {
			if (thread.isAlive()){
				Thread.currentThread().interrupt();
				thread.interrupt();
			//	future = service.submit(thread);
			//	future.cancel(true);
			}
		}
		//service.shutdown();
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
													
							// Metak svako unistavam ako je plejer pogodjen
							bullet = null;
							firedBullets.set(j, null);
							
							// OVAJ DEO UNISTI BULLET I ENEMYA (metkovi nisu mogli da predju mesto gde je nekada
							// bio enemy. sa ovim delom sam to resio.. sad metkovi dodju do kraja prozora)
							if (enemy.isEnemyKilled() == true){
								
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
		if (numberOfKills == enemies.size()) return false; 
		return true; 
	}

	/// ------------------ KEEP PLAYER WITHIN BORDERS ------------
	

	public void keepPlayerWithinBorders(Direction direction) {
		switch (direction) {
		case LEFT:
			overlapsFromLEFT();
			break;
		case RIGHT:
			overlapsFromRIGHT();
			break;
		case UP:
			overlapsFromUP();
			break;
		case DOWN:
			overlapsFromDOWN();
			break;
		default:
			break;
		}
	}
		
	private void overlapsFromLEFT() {
		Rectangle playerRectangle = new Rectangle(player.getCurrentPosition(), new Dimension(Character.getImgWidth(), Character.getImgWidth()));
		for (Rectangle obstacle : levelObstacles) {
			if ((int) playerRectangle.getY() <= (int) obstacle.getY() + brickHeight) {
				if (playerRectangle.intersects(obstacle)) {
					if (obstacle.x - brickWidth >= 0) {
						player.setCurrentPosition(new Point(obstacle.x + brickWidth, obstacle.y));
						System.out.println("Direction.LEFT");
						return;
					}
				}
			}
		}
	}
		
	private void overlapsFromRIGHT() {
		Rectangle playerRectangle = new Rectangle(player.getCurrentPosition(), new Dimension(Character.getImgWidth(), Character.getImgWidth()));
		for (Rectangle obstacle : levelObstacles) {
			if ((int) playerRectangle.getY() + Player.getHeightOfPlayer() >= (int) obstacle.getY()) {
				if (playerRectangle.intersects(obstacle)) {
					if (obstacle.x + brickWidth <= 800 - brickWidth) {
						player.setCurrentPosition(new Point(obstacle.x - brickWidth, obstacle.y));
						System.out.println("Direction.RIGHT");
						return;
					}
				}
			}
		}
	}
		
	private void overlapsFromUP() {
		Rectangle playerRectangle = new Rectangle(player.getCurrentPosition(), new Dimension(Character.getImgWidth(), Character.getImgWidth()));
		for (Rectangle obstacle : levelObstacles) {
			if ((int) playerRectangle.getY() + Player.getHeightOfPlayer() >= (int) obstacle.getY()) {
				if (playerRectangle.intersects(obstacle)) {
					player.setCurrentPosition(new Point(obstacle.x, obstacle.y + brickHeight));
					System.out.println("Direction.UP");
					return;
				}
			}
		}
	}	
		
	private void overlapsFromDOWN() {
		Rectangle playerRectangle = new Rectangle(player.getCurrentPosition(),new Dimension(Character.getImgWidth(), Character.getImgWidth()));
		
		for (Rectangle obstacle : levelObstacles) {
			if ((int) playerRectangle.getY() <= (int) obstacle.getY() + brickHeight) {
				if (playerRectangle.intersects(obstacle)) {
					if (obstacle.y + brickHeight <= 568) {
						player.setCurrentPosition(new Point(obstacle.x, obstacle.y - brickHeight));
						System.out.println("Direction.DOWN");
						return;
					}
				}
			}
		}
	}
			
	private Direction overlapsFromLeftOrRight(Rectangle bullet, Rectangle obstacle){ // obstacles && bullet
		
		if ((int)bullet.getX() + Character.IMG_WIDTH >= (int)obstacle.getX()){ 
			if (bullet.intersects(obstacle)){
				System.out.println("Direction.LEFT");
				return Direction.LEFT;
			}
		}
		
		if ((int)bullet.getX() <= (int)obstacle.getX() + brickWidth){ 
			if (bullet.intersects(obstacle)){
				System.out.println("Direction.RIGHT");
				return Direction.RIGHT;
			}
		}
		return Direction.NOTHING;
	}
			
	/// ------------------ END OF KEEP PLAYER WITHIN BORDERS ------------
	
	
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
