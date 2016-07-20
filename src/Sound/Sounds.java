package Sound;

import java.applet.Applet;
import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

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
		
		//Applet.newAudioClip(this.getClass().getResource("/Resources/Sounds/" + fileName)).play();
		
		String path = System.getProperty("user.dir") + "/bin/Resources/Sounds/" + fileName;
		System.out.println("path: " + path);			
			try {
				URL url = this.getClass().getResource("/Resources/Sounds/" + fileName);
				AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
				clip = AudioSystem.getClip();
				clip.open(audioInputStream);
			} catch (UnsupportedAudioFileException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (LineUnavailableException e) {
		         e.printStackTrace();
			}
					
	}
	
	
    public void play() {
    	

    	clip.setFramePosition(0);
    	clip.start();
	}
    
}
