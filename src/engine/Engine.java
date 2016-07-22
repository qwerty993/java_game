package engine;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import brick_ladder.Brick;
import bullet.Bullet;
import characters.Character;
import characters.Enemy;
import characters.Enemy2;
import characters.Enemy3;
import characters.Player;

public class Engine {
	private static final int PLAYER_START_X = 0;
	private static final int PLAYER_START_Y = 500 - 68; //600 - 68 - 32; //68 = heightOfPlayer & 32 = height of footer	
	
	
	// ovaj deo da uzimam iz brick klase
	private static final int brickWidth = Character.IMG_WIDTH;
	private static final int brickHeight = Character.IMG_HEIGHT;
	
	private static Point move = null;	// za kretanje levo, desno, gore, dole
	
	private Player player;
	private List<Enemy> enemies;
	private List<Brick> levelObstacles;
	private ArrayList<Thread> threads;
	private Random r;
	private boolean endOfGame;
	private int level;
	private Brick tmpBrick; 
	
	public Engine(int lvl){
		r = new Random();
		level = lvl;
		
		player = new Player(new Point(PLAYER_START_X, PLAYER_START_Y), 100, 4f, 3, false);
		enemies = new ArrayList<Enemy>();
		levelObstacles = new ArrayList<Brick>(); 
		threads = new ArrayList<Thread>();
		
		init(level);
	}
	
	
	
	// CITOVANJE :D
	
	public void killAllEnemies(){
		enemies.clear();
	}
	
	public void killAllThreads(){
		threads.clear();
	}
	
	
	
	
	
	public void init(int level){
		System.out.println("Inicijalizujem nivo: " + level);
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
	
	public void baseInitForObstacles(){
		int k = Character.SCREEN_WIDTH / Character.IMG_WIDTH + 1;
		levelObstacles.clear();
		levelObstacles = Brick.matrixOfEmptyBricks();
		
		for (int i = 0; i < k; i++) {
			for (Brick brick : levelObstacles) {
				if (brick.getX() == i * brickWidth && brick.getY() == 500){
					//brick.setIsBrick(true);
					brick.setIsBrickAndIsLadder(true, false, true);
				}
			}
			/*  FOR UMESTO OVOGA, valjda je brze jer manje opterecuje memoriju  
			Rectangle tmpRectangle = new Rectangle(new Point(i*brickWidth, 500), new Dimension(brickWidth, brickHeight));
			Brick tmpBrick = new Brick(tmpRectangle, true);
			levelObstacles.add(tmpBrick);
			*/
		}
	}
	
	private void init1() {
		player.setCurrentPosition(new Point(PLAYER_START_X, PLAYER_START_Y));
		player.getFiredBullets().clear();
		player.setHealth(100);
		player.setNumberOfLives(3);
		endOfGame = false;
		level = 1;
		
		enemies.clear();
		
		enemies.add(new Enemy(new Point(300, 500-68))); // zeleni 
		enemies.get(enemies.size()-1).setEnemyMoveBorders(0, 800);
		
		enemies.add(new Enemy(new Point(600, 500-68)));	// zeleni
		enemies.get(enemies.size()-1).setEnemyMoveBorders(0, 800);
		
		enemies.add(new Enemy2(new Point(448, 500 - 68*4))); // crveni
		enemies.get(enemies.size()-1).setEnemyMoveBorders(448-64, 576 - 64);
		
		enemies.add(new Enemy(new Point(128, 500 - 68*5))); 	// zeleni
		enemies.get(enemies.size()-1).setEnemyMoveBorders(64 + 64, 192);
		
		enemies.add(new Enemy3(new Point(670, 500 - 68*7)));	// plavi
		enemies.get(enemies.size()-1).setEnemyMoveBorders(512 + 20, 750);
		
		initObstaclesLevelOne();
		
		startThreads();
	}

	public void initObstaclesLevelOne(){
		baseInitForObstacles();
		
		for (int i = 1; i < 4; i++) {
			for (Brick brick : levelObstacles) {
				if (brick.getX() == i * brickWidth && brick.getY() == 500 - brickHeight * 4){
					brick.setIsBrickAndIsLadder(true, false, true);  // horizontala

					if (i == 1){
						tmpBrick = null; 
						for (int j = 1; j < 4; j++) {
							tmpBrick = brick.getBrick(i * brickWidth, 500 - brickHeight * j);						
							tmpBrick.setIsBrickAndIsLadder(false, true, true); // vertikala

							if (j == 3){
								tmpBrick = brick.getBrick(i * brickWidth, 500 - brickHeight * (j + 1));
								tmpBrick.setIsBrickAndIsLadder(true, true, true); // horizontala presek vertikala
							}
							//System.out.println("INIT_11 ( "+ tmpBrick.x +", "+ tmpBrick.y+" ): isLadder == " + tmpBrick.isLadderIMG() + ", isBrickIMG == " + tmpBrick.isBrickIMG() + ", isBrick == " + tmpBrick.isBrick());
						}	
					}	
				}
			}
		}
		
		for (int i = 5; i < 9; i++) {
			for (Brick brick : levelObstacles) {
				if (brick.getX() == i * brickWidth && brick.getY() == 500 - brickHeight * 3){
					brick.setIsBrickAndIsLadder(true, false, true); 
					
					if (i == 5){
						tmpBrick = null; 
						for (int j = 1; j < 3; j++) {
							tmpBrick = brick.getBrick(i * brickWidth, 500 - brickHeight * j);						
							tmpBrick.setIsBrickAndIsLadder(false, true, true); // vertikala
							
							if (j == 2){
								tmpBrick = brick.getBrick(i * brickWidth, 500 - brickHeight * (j + 1));
								tmpBrick.setIsBrickAndIsLadder(true, true, true); // horizontala presek vertikala
							}
							//System.out.println("INIT_12 ( "+ tmpBrick.x +", "+ tmpBrick.y+" ): isLadder == " + tmpBrick.isLadderIMG() + ", isBrickIMG == " + tmpBrick.isBrickIMG() + ", isBrick == " + tmpBrick.isBrick());
						}	
					}	
				}
			}
		}
		
		for (int i = 8; i < 13; i++) {
			for (Brick brick : levelObstacles) {
				if (brick.getX() == i * brickWidth && brick.getY() == 500 - brickHeight * 6){
					brick.setIsBrickAndIsLadder(true, false, true);
					
					if (i == 8){
						tmpBrick = null; 
						for (int j = 4; j < 6; j++) {
							tmpBrick = brick.getBrick(i * brickWidth, 500 - brickHeight * j);						
							tmpBrick.setIsBrickAndIsLadder(false, true, true); // vertikala
							
							if (j == 5){
								tmpBrick = brick.getBrick(i * brickWidth, 500 - brickHeight * (j + 1));
								tmpBrick.setIsBrickAndIsLadder(true, true, true); // horizontala presek vertikala
							}
							//System.out.println("INIT_13 ( "+ tmpBrick.x +", "+ tmpBrick.y+" ): isLadder == " + tmpBrick.isLadderIMG() + ", isBrickIMG == " + tmpBrick.isBrickIMG() + ", isBrick == " + tmpBrick.isBrick());
						}	
					}	
				}
			}
		}
		
		tmpBrick = null;
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
		
		startThreads();
	}
	
	private void initObstaclesLevelTwo() { 
		levelObstacles.clear();
		baseInitForObstacles();
				
		for (int i = 0; i < 3; i++) {
			for (Brick brick : levelObstacles) {
				if (brick.getX() == i * brickWidth && brick.getY() == 500 - brickHeight * 2){
					brick.setIsBrickAndIsLadder(true, false, true);
				}
			}
		}
		
		for (int i = 9; i < 12; i++) {
			for (Brick brick : levelObstacles) {
				if (brick.getX() == i * brickWidth && brick.getY() == 500 - brickHeight * 3){
					brick.setIsBrickAndIsLadder(true, false, true);

					if (i == 9){
						tmpBrick = null; 
						for (int j = 1; j < 3; j++) {
							tmpBrick = brick.getBrick(i * brickWidth, 500 - brickHeight * j);						
							tmpBrick.setIsBrickAndIsLadder(false, true, true); // vertikala

							if (j == 2){
								tmpBrick = brick.getBrick(i * brickWidth, 500 - brickHeight * (j + 1));
								tmpBrick.setIsBrickAndIsLadder(true, true, true); // horizontala presek vertikala
							}
							System.out.println("INIT_2 ( "+ tmpBrick.x +", "+ tmpBrick.y+" ): isLadder == " + tmpBrick.isLadderIMG() + ", isBrickIMG == " + tmpBrick.isBrickIMG() + ", isBrick == " + tmpBrick.isBrick());
						}	
					}
				}
			}
		}
		
		for (int i = 3; i < 8; i++) {
			for (Brick brick : levelObstacles) {
				if (brick.getX() == i * brickWidth && brick.getY() == 500 - brickHeight * 4){
					brick.setIsBrickAndIsLadder(true, false, true);
				}
			}
		}
		
		for (int i = 0; i < 4; i++) {			
			for (Brick brick : levelObstacles) {
				if (brick.getX() == i * brickWidth && brick.getY() == 500 - brickHeight * 6){
					brick.setIsBrickAndIsLadder(true, false, true);
					
					if (i == 3){
						tmpBrick = null; 
						for (int j = 5; j < 6; j++) {
							tmpBrick = brick.getBrick(i * brickWidth, 500 - brickHeight * j);						
							tmpBrick.setIsBrickAndIsLadder(false, true, true); // vertikala
							
							if (j == 5){
								tmpBrick = brick.getBrick(i * brickWidth, 500 - brickHeight * (j + 1));
								tmpBrick.setIsBrickAndIsLadder(true, true, true); // horizontala presek vertikala
							}
							System.out.println("INIT_2 ( "+ tmpBrick.x +", "+ tmpBrick.y+" ): isLadder == " + tmpBrick.isLadderIMG() + ", isBrickIMG == " + tmpBrick.isBrickIMG() + ", isBrick == " + tmpBrick.isBrick());
						}	
					}
				}
			}
		}
		
		for (int i = 7; i < 11; i++) {			
			for (Brick brick : levelObstacles) {
				if (brick.getX() == i * brickWidth && brick.getY() == 500 - brickHeight * 6){
					brick.setIsBrickAndIsLadder(true, false, true);
					
					if (i == 7){
						tmpBrick = null; 
						for (int j = 5; j < 6; j++) {
							tmpBrick = brick.getBrick(i * brickWidth, 500 - brickHeight * j);						
							tmpBrick.setIsBrickAndIsLadder(false, true, true); // vertikala
							
							if (j == 5){
								tmpBrick = brick.getBrick(i * brickWidth, 500 - brickHeight * (j + 1));
								tmpBrick.setIsBrickAndIsLadder(true, true, true); // horizontala presek vertikala
							}
							System.out.println("INIT_2 ( "+ tmpBrick.x +", "+ tmpBrick.y+" ): isLadder == " + tmpBrick.isLadderIMG() + ", isBrickIMG == " + tmpBrick.isBrickIMG() + ", isBrick == " + tmpBrick.isBrick());
						}	
					}
					
					if (i == 10){
						tmpBrick = null; 
						for (int j = 4; j < 6; j++) {
							tmpBrick = brick.getBrick(i * brickWidth, 500 - brickHeight * j);						
							tmpBrick.setIsBrickAndIsLadder(false, true, true); // vertikala
							
							if (j == 5){
								tmpBrick = brick.getBrick(i * brickWidth, 500 - brickHeight * (j + 1));
								tmpBrick.setIsBrickAndIsLadder(true, true, true); // horizontala presek vertikala
							}
							System.out.println("INIT_2 ( "+ tmpBrick.x +", "+ tmpBrick.y+" ): isLadder == " + tmpBrick.isLadderIMG() + ", isBrickIMG == " + tmpBrick.isBrickIMG() + ", isBrick == " + tmpBrick.isBrick());
						}	
					}	
				}
			}
		}
		
		tmpBrick = null;
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
		
		startThreads();
		
	}
		
	private void initObstaclesLevelThree() { // dodati merdevine
		levelObstacles.clear();
		baseInitForObstacles();
		
		for (int i = 1; i < 12; i++) {
			if (i < 4 || i > 7){
				for (Brick brick : levelObstacles) {
					if (brick.getX() == i * brickWidth && brick.getY() == 500 - brickHeight * 2){
						brick.setIsBrickAndIsLadder(true, false, true);
					}
				}
			}
		}
		
		for (int i = 0; i < 7; i++) {
			if (i == 0 || i > 3){
				for (Brick brick : levelObstacles) {
					if (brick.getX() == i * brickWidth && brick.getY() == 500 - brickHeight * 3){
						brick.setIsBrickAndIsLadder(true, false, true);
					}
				}
			}
		}
		
		for (int i = 1; i < 8; i++) {
			for (Brick brick : levelObstacles) {
				if (brick.getX() == i * brickWidth && brick.getY() == 500 - brickHeight * 5){
					brick.setIsBrickAndIsLadder(true, false, true);
				}
			}
		}
		
		for (int i = 1; i < 11; i++) {
			if (i == 1 || i > 7){
				for (Brick brick : levelObstacles) {
					if (brick.getX() == i * brickWidth && brick.getY() == 500 - brickHeight * 6){
						brick.setIsBrickAndIsLadder(true, false, true);
					}
				}
			}
		}
		
		tmpBrick = null;
	}

	private void startThreads(){
		threads.clear();
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
			level = 4;
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
					if (levelObstacles.get(i) != null && levelObstacles.get(i).isBrick() && !levelObstacles.get(i).isLadderIMG()){
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
		if (numberOfKills == enemies.size()) return false; 
		return true; 
	}

	public void keepPlayerWithinBorders(Direction direction) {
		switch (direction) {
		case LEFT:
			player.setImageByString("playerLeft");
			moveLeft();
			break;
		case RIGHT:
			player.setImageByString("playerRight");
			moveRight();
			break;
		case UP:
			moveUp();
			break;
		case DOWN:
			moveDown();
			break;
		default:
			break;
		}
	}
	
	private void moveUp(){
		move = player.getCurrentPosition();
		for (Brick brick : levelObstacles) {			
			if (brick.getX() == move.getX() && brick.getY() == move.getY()){
				Brick tmpBrick = brick;
				System.out.println("UP ( "+ tmpBrick.x +", "+ tmpBrick.y+" ): isLadder == " + tmpBrick.isLadderIMG() + ", isBrickIMG == " + tmpBrick.isBrickIMG() + ", isBrick == " + tmpBrick.isBrick());
					if (brick.isBrick() == true && brick.isLadderIMG() == true){
						player.moveUp();
						System.out.println("gore");
					}else{
						System.out.println("Neces gore!");
					}
					break;
					
				
			} 
		}
	}
	
	private void moveDown(){
		move = player.getCurrentPosition();
		for (Brick brick : levelObstacles) {
			if (brick.getX() == move.getX() && brick.getY()  == move.getY() + brickHeight){
				Brick tmpBrick = brick;
				System.out.println("DOWN ( "+ tmpBrick.x +", "+ tmpBrick.y+" ): isLadder == " + tmpBrick.isLadderIMG() + ", isBrickIMG == " + tmpBrick.isBrickIMG() + ", isBrick == " + tmpBrick.isBrick());
				
				if (brick.isBrick() == true && brick.isLadderIMG() == true){
					player.moveDown();
					System.out.println("dole");
				}else{
					System.out.println("Neces dole!");
				}
				break;
			}
		}
	}
	
	private void moveLeft() {
		move = player.getCurrentPosition();
		for (Brick brick : levelObstacles) {
			if (brick.getX() >= move.getX() - brickWidth  && brick.getY() == move.getY() + brickHeight){
				Brick tmpBrick = brick;
				System.out.println("LEFT ( "+ tmpBrick.x +", "+ tmpBrick.y+" ): isLadder == " + tmpBrick.isLadderIMG() + ", isBrickIMG == " + tmpBrick.isBrickIMG() + ", isBrick == " + tmpBrick.isBrick());
				
				if (brick.isBrick() == true && (brick.isBrickIMG() == true || brick.isLadderIMG() == true)){
					player.moveLeft();
					System.out.println("levo");
				}else{
					System.out.println("Nije cigla LEVO");
				}
				break;
			}
		}
	}
	
	private void moveRight() {
		move = player.getCurrentPosition();
		for (Brick brick : levelObstacles) {
			if (brick.getX() >= move.getX() + brickWidth - Player.STEP && brick.getY() == move.getY() + brickHeight){
				Brick tmpBrick = brick;
				System.out.println("RIGHT ( "+ tmpBrick.x +", "+ tmpBrick.y+" ): isLadder == " + tmpBrick.isLadderIMG() + ", isBrickIMG == " + tmpBrick.isBrickIMG() + ", isBrick == " + tmpBrick.isBrick());
				
				if (brick.isBrick() == true && (brick.isBrickIMG() == true || brick.isLadderIMG() == true)){
					System.out.println("desno");
					player.moveRight();
				}else{
					System.out.println("Nije cigla DESNO");
				}
				break;
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
	
	
	public void fireBullets(boolean space, boolean left, boolean right){
		if (space == true){
			if (right == true){
				player.fire(Direction.RIGHT);
			}
			if (left == true){
				player.fire(Direction.LEFT);
			}
		}
		player.destroyBullets();
	}
	
	public void removeAll(){
		hidePlayer();
		enemies.clear();
		levelObstacles.clear();
		player.getFiredBullets().clear();
	}
	
	public void hidePlayer(){
		player.hide();
	}
	
	public void moveBullets(){
		player.moveBullets();
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
	
	public void setLevel(int level){
		this.level = level;
	}
	
	public List<Brick> getLevelObstacles() {
		return levelObstacles;
	}
	
}
