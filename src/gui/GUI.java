package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
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

import engine.Direction;
import engine.Engine;

public class GUI extends JFrame implements KeyEventDispatcher {
	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 800;		// SVGA resolution 800x600
	private static final int HEIGHT = 600;
	
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();		// DIMENZIJE EKRANA (MONITORA)
	private final double widthScr = screenSize.getWidth();
	private final double heightScr = screenSize.getHeight();

	private JPanel footer;			// FOOTER 
	private JLabel lblHealth;
	private JLabel lblLives;	
	private JLabel lblCurrentLevel;
	private Font fontSize20;
	private Font fontSize18;
	
	private boolean space = false;	// KEYPRESSED COMBINATON LOGIC
	private boolean left = false;
	private boolean right = false;

	private Timer timer;
	private Canvas canvas;
	private Engine engine;

	public GUI() {
		setTitle("Kill them all!");
		
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		manager.addKeyEventDispatcher(this);

		engine = new Engine(1); 
		canvas = new Canvas(engine);
		
		timer = new Timer(16, new ActionListener() {	// 60 fps
			@Override
			public void actionPerformed(ActionEvent e) {
		        engine.moveBullets();
		        engine.bulletOverlapsWithObstacle();
		        refreshGUI();
				canvas.repaint();
			}			
		});

		timer.start();
		
		
		getContentPane().add(canvas);
		setFooter();
		
		pack();
		setVisible(true);
		setFocusable(true);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setLocation(new Point((int) ((widthScr - WIDTH) / 2), (int) ((heightScr - HEIGHT) / 2)));
	}

	public void setFooter() {
		fontSize18 = new  Font("Comic Sans MS", Font.BOLD, 18);
		fontSize20 = new Font("Comic Sans MS", Font.BOLD, 20);
		
		footer = new JPanel(new GridLayout(1, 3, 4, 4));
		
		footer.setPreferredSize(new Dimension(WIDTH, 32));
		footer.setBackground(new Color(51, 153, 255));
		
		lblHealth = new JLabel("HEALTH: 100%", SwingConstants.CENTER);
		lblHealth.setForeground(Color.WHITE);
		lblHealth.setFont(fontSize20);
		
		lblLives = new JLabel("LIVES: 3", SwingConstants.CENTER); 
		lblLives.setForeground(Color.WHITE);
		lblLives.setFont(fontSize20);
		
		lblCurrentLevel = new JLabel("LEVEL: 1", SwingConstants.CENTER);
		lblCurrentLevel.setForeground(Color.WHITE);
		lblCurrentLevel.setFont(fontSize20);
		
		footer.add(lblHealth);
		footer.add(lblLives);
		footer.add(lblCurrentLevel);
			
		getContentPane().add(footer, BorderLayout.SOUTH);
	}

	private void refreshFooter(){
		if (engine.isCollision()){
			lblHealth.setForeground(Color.RED);
			lblHealth.setFont(fontSize18);
		}else{
			lblHealth.setForeground(Color.WHITE);
			lblHealth.setFont(fontSize20);
		}
		lblHealth.setText("HEALTH: " + engine.getPlayer().getHealth() + "%");
		lblLives.setText("LIVES: " + engine.getPlayer().getNumberOfLives());
		lblCurrentLevel.setText("LEVEL: " + engine.getLevel());
	}
	
	public void refreshGUI(){	
		if (engine.isEnd()){
			newGame("Thank you for playing!\nNew game?");
		}
		
		if (engine.isNextLevel()){
			engine.init(engine.getLevel());
			// DEBUG
			//System.out.println("Next level activated! " + engine.getLevel());
			refreshFooter();
			canvas.repaint();
		}
		
		if (engine.isWin()){
			newGame("Congratulations!\nYou win!\nNew game?");
		}
		
		if (engine.isCollision()) 
			refreshFooter();
		
		if (engine.isCollisionWithBullet()) 
			refreshFooter();
		
	}
	
	private void newGame(String message){
		engine.removeAll();
		canvas.repaint();
		dialogBoxHandler(message);
	}
	
	private void dialogBoxHandler(String message){
		timer.stop();
		int response = JOptionPane.showConfirmDialog(this, message, "Game over!", JOptionPane.YES_NO_OPTION);
		
		if (response == JOptionPane.YES_OPTION){
			engine.setLevel(1);
			engine.init(engine.getLevel());
			
			if (engine.getLevel() != 1) engine.removeAll();
			
			refreshFooter();			
			timer.start();
			
		}else{
			dispose();
			System.exit(0);
		}
	}
	
		
	@Override
	public boolean dispatchKeyEvent(KeyEvent e) {
		if (e.getID() == Event.KEY_RELEASE) {
			
			if (e.getKeyCode() == KeyEvent.VK_SPACE){
				space = false;
			}
			
			if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT){
				// kad se puste strlice levo i desno onda se postavi player front image
				engine.getPlayer().setImageByString("player");
				
				if (e.getKeyCode() == KeyEvent.VK_LEFT){
					left = false;
				}else if (e.getKeyCode() == KeyEvent.VK_RIGHT){
					right = false;
				}
			}						
		}
		
		if (e.getID() == Event.KEY_PRESS) {
			switch (e.getKeyCode()) {
			
				case KeyEvent.VK_LEFT:
					engine.keepPlayerWithinBorders(Direction.LEFT);
					left = true;
					break;
					
				case KeyEvent.VK_RIGHT:
					engine.keepPlayerWithinBorders(Direction.RIGHT);
					right = true;
					break;
					
				case KeyEvent.VK_UP:
					engine.keepPlayerWithinBorders(Direction.UP);
					break;
				
				case KeyEvent.VK_DOWN:
					engine.keepPlayerWithinBorders(Direction.DOWN);
					break;
					
				case KeyEvent.VK_SPACE:
					space = true;
					break;
					
				case KeyEvent.VK_TAB:
					engine.killAllEnemies();	// za brzi prelazak nivo-a
					break;
					
				default:
					break;
			}
						
			engine.fireBullets(space, left, right);
			refreshGUI();
		}
		
		canvas.repaint();
		return true;
	}

}
