package characters;

import java.awt.Point;

public class Enemy3 extends Enemy implements Runnable {

	public Enemy3(Point startPosition){
		this(startPosition, Difficulty.HARD.getDiffLevel(), 1, false);
	}
	
	public Enemy3(Point startPosition, int health, int numberOfLives, boolean collision) {
		this(startPosition, health, numberOfLives, collision, "enemy31", "enemy32");
	}
	
	
	public Enemy3(Point startPosition, int health, int numberOfLives, boolean collision, String firstIMG, String secondIMG) {
		super(startPosition, health, numberOfLives, collision, firstIMG, secondIMG);
	}

	@Override
	public void run() {
		super.run();
	}

	

}
