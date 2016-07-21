package characters;

import java.awt.Point;

public class Enemy2 extends Enemy implements Runnable {

	public Enemy2(Point startPosition){
		this(startPosition, Difficulty.MEDIUM.getDiffLevel(), 6f, 1, false);
	}
	
	public Enemy2(Point startPosition, int health, double speed, int numberOfLives, boolean collision) {
		this(startPosition, health, speed, numberOfLives, collision, "enemy21", "enemy22");
	}
	
	public Enemy2(Point startPosition, int health, double speed, int numberOfLives, boolean collision, String firstIMG, String secondIMG) {
		super(startPosition, health, speed, numberOfLives, collision, firstIMG, secondIMG);
	}
	
	@Override
	public void run() {
		super.run();		
	}
	
}
