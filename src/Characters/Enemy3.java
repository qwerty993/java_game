package Characters;

import java.awt.Point;

public class Enemy3 extends Enemy implements Runnable {

	public Enemy3(Point startPosition){
		this(startPosition, Difficulty.HARD.getDiffLevel(), 10f, 1, false);
	}
	
	public Enemy3(Point startPosition, int health, double speed, int numberOfLives, boolean collision) {
		this(startPosition, health, speed, numberOfLives, collision, "enemy31", "enemy32");
	}
	
	
	public Enemy3(Point startPosition, int health, double speed, int numberOfLives, boolean collision, String firstIMG, String secondIMG) {
		super(startPosition, health, speed, numberOfLives, collision, firstIMG, secondIMG);
	}

	@Override
	public void run() {
		super.run();
	}

	

}
