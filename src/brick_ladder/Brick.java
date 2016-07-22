package brick_ladder;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import characters.Character;

public class Brick extends Rectangle {
	private static final long serialVersionUID = 1L;
	private static final int BRICK_WIDTH = Character.IMG_WIDTH;
	private static final int BRICK_HEIGHT = Character.IMG_HEIGHT;
	
	private static String path = System.getProperty("user.dir") + "/bin/Resources/Images/";
	private static File file; 
	private static List<Brick> levelObstacles;
	
	private boolean brick;
	private boolean isLadderIMG;
	private boolean isBrickIMG;
	private BufferedImage brickImage;
	private String fileName;
	
	public Brick(Rectangle rectangle){
		super(rectangle);
		this.brick = false;
		this.isBrickIMG = false;
		this.isLadderIMG = false;
	}
	
	private void readImage(boolean isBrickIMG, boolean isLadderIMG){
		fileName = null;
		boolean flag = false;
		
		if (isBrickIMG){
			fileName = "brick.jpg";
			flag = true;
		}
		if (isLadderIMG){
			fileName = "ladder.png";
			flag = true;
		}
		if (isBrickIMG && isLadderIMG){
			fileName = "brickAndLadder.jpg";
			flag = true;
		}
		
		if (flag){
			try {
				file = new File(path + fileName);
				brickImage = ImageIO.read(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void setBrick(boolean isBrick){
		brick = isBrick;
	}
	
	public boolean isBrick(){
		return brick;
	}
	
	public boolean isBrickIMG() {
		return isBrickIMG;
	}

	public boolean isLadderIMG() {
		return isLadderIMG;
	}

	public BufferedImage getBrickImage() {
		return brickImage;
	}	
	
	public static List<Brick> matrixOfEmptyBricks(){		
		levelObstacles = new ArrayList<Brick>();
		
		for (int i = 0; i <= 800; ) {
			for (int j = 500; j >=0; ) {
				levelObstacles.add(new Brick(new Rectangle(new Point(i, j), new Dimension(BRICK_WIDTH, BRICK_HEIGHT))));	
				j -= BRICK_HEIGHT;
			}
			i += BRICK_WIDTH;
		}
		return levelObstacles;
	}
	
	public Brick getBrick(int x, int y){
		for (Brick brick : levelObstacles) {
			if (brick.getX() == x && brick.getY() == y)
				return brick;
		}
		return null;
	}

	public void setIsBrickAndIsLadder(boolean isBrickIMG, boolean isLadderIMG, boolean brick){
		this.isBrickIMG = isBrickIMG;
		this.isLadderIMG = isLadderIMG;
		this.brick = brick;
		if (brick == true) readImage(isBrickIMG, isLadderIMG);
	}

	public String getFileName() {
		return fileName;
	}
	
	
}
