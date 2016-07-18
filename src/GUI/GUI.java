package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.GridLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import Engine.Engine;

public class GUI extends JFrame implements KeyEventDispatcher {
	private static final long serialVersionUID = 1L;

	private static final int width = 800; // SVGA resolution 800x600
	private static final int height = 600;
	
	
	// Uzimam dimenzije ekrana da bih pozicionirao prozor na centar ekrana.
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private double widthScr = screenSize.getWidth();
	private double heightScr = screenSize.getHeight();

	// FOOTER PART
	private JPanel footer;
	private JLabel lblHealth;
	private JLabel lblLives;	
	private JLabel lblCurrentLevel;
	// END OF FOOTER PART
	

	
	Timer timer;
	Canvas canvas;
	Engine engine;
	
	public GUI() {
		setTitle("Kill them all!");
		
		setBounds((int) ((widthScr - width) / 2), (int) ((heightScr - height) / 2), width, height);

		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		manager.addKeyEventDispatcher(this);

		engine = new Engine(1);
			
		canvas = new Canvas(engine.getPlayer(), engine.getEnemies());

		
		timer = new Timer(20, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
		        engine.getPlayer().moveBullets();
		        refreshGUI();
				canvas.repaint();
			}			
		});

		timer.start();

		getContentPane().add(canvas);
		
		setFooter();
		//pack();
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}

	public void setFooter() {
		footer = new JPanel(new GridLayout(1, 3, 4, 4));
		footer.setPreferredSize(new Dimension(width, 32));
		
		lblHealth = new JLabel("HEALTH: 100%", SwingConstants.CENTER);
		lblLives = new JLabel("LIVES: 3", SwingConstants.CENTER); 
		lblCurrentLevel = new JLabel("LEVEL: 1", SwingConstants.CENTER);
		/* 	
		  	Dodam u konstruktor SwingConstants.CENTER i onda nema potrebe za podesavanjem  
		 	lblLives.setVerticalAlignment(SwingConstants.CENTER);
			lblLives.setHorizontalAlignment(SwingConstants.CENTER);
		*/
		
		footer.add(lblHealth);
		footer.add(lblLives);
		footer.add(lblCurrentLevel);
		
		getContentPane().add(footer, BorderLayout.SOUTH);
	}

	private void refreshFooter(){
		lblHealth.setText("HEALTH: " + engine.getPlayer().getHealth());
		lblLives.setText("LIVES: " + engine.getPlayer().getNumberOfLives());
		lblCurrentLevel.setText("LEVEL: " + engine.getLevel());
	}
	
	public void refreshGUI(){	
		if (engine.isEnd()){
			dialogBoxHandler("Thank you for playing!\nNew game?");
		}
		
		if (engine.isNextLevel()){
			engine.init(engine.getLevel());
			System.out.println("Next level activated!");
			canvas.removeAll();
			canvas.revalidate();
			canvas.repaint();
		}
		
		if (engine.isWin()){
			dialogBoxHandler("Congratulations!\nYou win!\nNew game?");
		}
		
		if (engine.isCollision()) 
			refreshFooter();
		
		if (engine.isCollisionWithBullet()) 
			refreshFooter();
	}
	
	private void dialogBoxHandler(String message){
		int response = JOptionPane.showConfirmDialog(this, message, "Game over!", JOptionPane.YES_NO_OPTION);
		timer.stop();
		
		if (response == JOptionPane.YES_OPTION){
			timer.start();
			engine.init(1);
			canvas.removeAll();
			canvas.revalidate();
			canvas.repaint();
		}else{
			dispose();
		}
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent e) {
		boolean space = false;
		boolean left = false;
		boolean right = false;
		
		if (e.getID() == Event.KEY_RELEASE) {
			if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT){
				// kad se puste strlice levo i desno onda se postavi player front image
				engine.getPlayer().setImageByString("player");
				
				if (e.getKeyCode() == KeyEvent.VK_LEFT){
					left = false;
				}else{
					right = false;
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_SPACE){
				space = false;
			}
						
		}
		
		if (e.getID() == Event.KEY_PRESS) {
			switch (e.getKeyCode()) {
			
				case KeyEvent.VK_LEFT:
					engine.getPlayer().setImageByString("playerLeft");
					engine.getPlayer().moveLeft();
					left = true;
					break;
					
				case KeyEvent.VK_RIGHT:
					engine.getPlayer().setImageByString("playerRight");
					engine.getPlayer().moveRight();
					right = true;
					break;
					
				case KeyEvent.VK_UP:
					engine.getPlayer().moveUp();
					break;
				
				case KeyEvent.VK_DOWN:
					engine.getPlayer().moveDown();
					break;
					
				case KeyEvent.VK_SPACE:
					space = true;
					break;
										
				default:
					break;
			}
			
			if (space == true){
				if (left == true){
					engine.getPlayer().fire("left");
				}else{
					engine.getPlayer().fire("right");
				}
			}
			
			refreshGUI();
			/*	OVAJ DEO JE UBACEN U REFRESHGUI();
			if (engine.isCollision()) 
				refreshFooter();
			
			if (engine.isCollisionWithBullet()) 
				refreshFooter();
			*/
		}
		
		
		canvas.repaint();
		return true;
	}

}
