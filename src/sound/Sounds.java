package sound;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public enum Sounds {
	COLLECT_POINT("Collect_Point.wav"),
	EXPLOSION("Explosion.wav"),
	HERO_DEATH("Hero_Death.wav"),
	HIT("Hit.wav"),
	JINGLE_LOSE("Jingle_Lose.wav"),
	SHOOT("Shoot.wav");
	
	private Clip clip;
	String fileName;
	
	Sounds(String fileName){
		this.fileName = fileName;
		
		//String path = System.getProperty("user.dir") + "/bin/Resources/Sounds/" + fileName;
		//System.out.println("path: " + path);

			/*
			try {
				clip = AudioSystem.getClip();
				File file = new File(path);
				//AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(path).getAbsoluteFile());
				AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(Sounds.class.getResourceAsStream("/Resources/Sounds/" + fileName));
				if (file.exists()) {
					AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
					clip.open(audioInputStream);			    	
				}
				clip.open(audioInputStream);
			} catch (Exception ex) {
				System.out.println("Greska prilikom pustanja zvuka.");
				//ex.printStackTrace();
			}
				*/	
	}
	

    
    public void playSound() {
		try {
			//Clip clip;
			AudioInputStream audioInputStream = AudioSystem
					.getAudioInputStream(new File(System.getProperty("user.dir") + "/bin/Resources/Sounds/Collect_Point.wav"));
			clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
		} catch (Exception ex) {
			System.out.println("Greska prilikom pustanja zvuka. --> " + fileName);
			ex.printStackTrace();
		}
	}
    
    /*
    public void playSound() {
    	clip.setFramePosition(0); // mora uvek da se premota
    	clip.start();
	}   
    */
}

