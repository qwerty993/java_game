package Sound;

import java.io.BufferedInputStream;
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
	
	private Clip sound, clip;
	private String fileName;
	
	
	Sounds(String fileName){
		this.fileName = fileName;
		/*
		try {
			sound = AudioSystem.getClip();
			sound.open(AudioSystem.getAudioInputStream(getClass().getResource("/Resources/Sounds/" + this.fileName)));
		} 
		catch (UnsupportedAudioFileException e1) { }
		catch (IOException e2) { } 
		catch (LineUnavailableException e3) { }
		*/
	}
	
	/*
    public void play() {
    	//sound.setFramePosition(0);
    	System.out.println(fileName + ": " + sound.getMicrosecondLength());
    	sound.start();
	}
    */
	
	
	public void play(){
		
		try {
			
			String path = System.getProperty("user.dir") + "/bin/Resources/Sounds/" + fileName;
			System.out.println(path);
			
			BufferedInputStream bufStream = new BufferedInputStream(new URL(path).openStream());
	        //AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(path).getAbsoluteFile());
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bufStream);
	        clip = AudioSystem.getClip();
	        clip.open(audioInputStream);
	        clip.start();
	    } catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}
	
}
