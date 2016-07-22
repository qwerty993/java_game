package bullet;

import engine.Direction;

public interface BulletInterface {
	public static final int BULLET_SPEED = 10; 
	public static final int BULLET_DAMAGE = 5;
	public static final int BULLET_WIDTH = 41;
	public static final int BULLET_HEIGHT = 9;
	
	public void spawnBullet(Direction direction);
}
