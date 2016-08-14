package characters;

import java.awt.Point;

public class Enemy2 extends Enemy implements Runnable {

	public Enemy2(Point startPosition){
		this(startPosition, Difficulty.MEDIUM.getDiffLevel(), 1, false);
	}
	
	public Enemy2(Point startPosition, int health, int numberOfLives, boolean collision) {
		this(startPosition, health, numberOfLives, collision, "enemy21", "enemy22");
	}
	
	public Enemy2(Point startPosition, int health, int numberOfLives, boolean collision, String firstIMG, String secondIMG) {
		super(startPosition, health, numberOfLives, collision, firstIMG, secondIMG);
	}
	
	@Override
	public void run() {
		super.run();		
	}
	
}
