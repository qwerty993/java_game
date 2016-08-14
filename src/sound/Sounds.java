package sound;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Base64.Decoder;

import javax.sound.sampled.AudioFormat;
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
	File sound;
	
	Sounds(String fileName) {
		try{
			System.out.println(getClass().getResource("/Resources/Sounds/" + fileName));
			AudioInputStream ais = AudioSystem.getAudioInputStream(getClass().getResourceAsStream(fileName));
			AudioFormat baseFormat = ais.getFormat();
			AudioFormat decodeFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
			AudioInputStream dais = AudioSystem.getAudioInputStream(decodeFormat, ais);
			clip = AudioSystem.getClip();
			clip.open(dais);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	/*
	public void playSound(){
		if (clip == null) return;
		stop();
		clip.setFramePosition(0);
		clip.start();
	}
	*/
	public void stop(){
		if (clip.isRunning()) clip.stop();
	}
	
	public void close(){
		stop();
		clip.close();
	}
	
	
	
	
	/*
	Sounds(String fileName){
		this.fileName = fileName;
		URL url = this.getClass().getResource("/Resources/Sounds/" + fileName);
		//sound = new File(url);
		//new File(new URL("http://static1.grsites.com/archive/sounds/cartoon/cartoon003.mp3"));
		
		File soundFile = new File("/Resources/Sounds/" + fileName);
		System.out.println(soundFile);
		AudioInputStream audioInputStream;
		try {
			audioInputStream = AudioSystem.getAudioInputStream( soundFile );
			clip = AudioSystem.getClip();
			clip.open(audioInputStream);
		} catch (UnsupportedAudioFileException | IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
		
	}
	
	public void playSound(){
		clip.start();//This plays the audio 
	}
	*/
	/*
    public void playSound() {
    	clip.setFramePosition(0); // mora uvek da se premota
    	clip.start();
	}   
	*/
	/*
	public void playSound(){
		try {
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(sound));
			clip.start();
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
	}
	*/
	/*
	public static synchronized void playSound(final String url) {
		  new Thread(new Runnable() {
		  // The wrapper thread is unnecessary, unless it blocks on the
		  // Clip finishing; see comments.
		    public void run() {
		    	 System.out.println(this.getClass().getResource("/Resources/Sounds/" + url));
		      try {
		        Clip clip = AudioSystem.getClip();
		        URL pathURL = this.getClass().getResource("/Resources/Sounds/Collect_Point.wav");
		        AudioInputStream inputStream = AudioSystem.getAudioInputStream(pathURL);
		       
		        clip.open(inputStream);
		        clip.start(); 
		      } catch (Exception e) {
		        System.err.println(e.getMessage());
		      }
		    }
		  }).start();
		}
	*/
	/*
	public void playSound(){
		String path = System.getProperty("user.dir") + "/bin/Resources/Sounds/" + fileName;
		System.out.println("path: " + path);

			
			try {
				clip = AudioSystem.getClip();
				File file = new File(path);
				//AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(path).getAbsoluteFile());
				AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(Sounds.class.getResourceAsStream("/Resources/Sounds/" + fileName));
				if (file.exists()) {
					//AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
					clip.open(audioInputStream);			    	
				}
				clip.open(audioInputStream);
			} catch (Exception ex) {
				System.out.println("Greska prilikom pustanja zvuka.");
				//ex.printStackTrace();
			}
	}
	*/
    /*
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
    */
    

    /*
    
    public synchronized void playSound() {
    	  new Thread(new Runnable() {
    	  // The wrapper thread is unnecessary, unless it blocks on the
    	  // Clip finishing; see comments.
    	    public void run() {
    	      try {
    	        Clip clip = AudioSystem.getClip();
    	        AudioInputStream inputStream = AudioSystem.getAudioInputStream(
    	          Sounds.class.getResourceAsStream("/Resources/Sounds/" + fileName));
    	        clip.open(inputStream);
    	        clip.start(); 
    	      } catch (Exception e) {
    	        System.err.println(e.getMessage());
    	      }
    	    }
    	  }).start();
    	}
    */
	
}

