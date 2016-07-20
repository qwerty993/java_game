package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Event;
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

import Engine.Engine;

public class GUI extends JFrame implements KeyEventDispatcher {
	private static final long serialVersionUID = 1L;

	private static final int WIDTH = 800; // SVGA resolution 800x600
	private static final int HEIGHT = 600;
	
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
	

	// KEYPRESSED COMBINATON LOGIC
	private boolean space = false;
	private boolean left = false;
	private boolean right = false;
	//
	
	
	Timer timer;
	Canvas canvas;
	Engine engine;
	
	public GUI() {
		setTitle("Kill them all!");
		
		//setBounds((int) ((widthScr - WIDTH) / 2), (int) ((heightScr - HEIGHT) / 2), WIDTH, HEIGHT);
		//setBounds(0, 0, WIDTH, HEIGHT);
		
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		manager.addKeyEventDispatcher(this);

		engine = new Engine(1); 
		
		canvas = new Canvas(Engine.getPlayer(), engine.getEnemies());

		
		timer = new Timer(20, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
		        Engine.getPlayer().moveBullets();
		        engine.bulletOverlapsWithObstacle();
		        
		        engine.keepPlayerWithinBorders(Engine.getPlayer());
		        refreshGUI();
				canvas.repaint();
			}			
		});

		timer.start();

		getContentPane().add(canvas);
		
		setFooter();
		pack();
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		//setLocationRelativeTo(null);
		
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setLocation(new Point((int) ((widthScr - WIDTH) / 2), (int) ((heightScr - HEIGHT) / 2)));
	}

	public void setFooter() {
		footer = new JPanel(new GridLayout(1, 3, 4, 4));
		footer.setPreferredSize(new Dimension(WIDTH, 32));
		
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
		lblHealth.setText("HEALTH: " + Engine.getPlayer().getHealth() + "%");
		lblLives.setText("LIVES: " + Engine.getPlayer().getNumberOfLives());
		lblCurrentLevel.setText("LEVEL: " + Engine.getLevel());
	}
	
	
	
	public void refreshGUI(){	
		if (engine.isEnd()){
			dialogBoxHandler("Thank you for playing!\nNew game?");
		}
		
		if (engine.isNextLevel()){
			engine.init(Engine.getLevel());
			System.out.println("Next level activated!");
			refreshFooter();	// dodao refreshFooter() jer je mnogo kasnio sa promenom LVL-a 
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
	
	private synchronized void dialogBoxHandler(String message){
		int response = JOptionPane.showConfirmDialog(this, message, "Game over!", JOptionPane.YES_NO_OPTION);
		timer.stop();
		
		//engine.stopThreads();
		
		//engine.killAllThreads();
		
		// stopiraj sve tredove
		// engine.stopThreads();
		// MNOGO PUTA SE POJAVI DIALOG BOX, TO NE SME DA SE DESAVA, PROVERITI U ENGINU STOP_THREADS() !!!!
		
		if (response == JOptionPane.YES_OPTION){
			System.out.println("ovde puca");
			engine.init(1);
			timer.start();
			canvas.removeAll();
			canvas.revalidate();
			canvas.repaint();
			refreshGUI();
		}else{
			engine.stopThreads();
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
				Engine.getPlayer().setImageByString("player");
				
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
					Engine.getPlayer().setImageByString("playerLeft");
					Engine.getPlayer().moveLeft();
					left = true;
					break;
					
				case KeyEvent.VK_RIGHT:
					Engine.getPlayer().setImageByString("playerRight");
					Engine.getPlayer().moveRight();
					right = true;
					break;
					
				case KeyEvent.VK_UP:
					Engine.getPlayer().moveUp();
					break;
				
				case KeyEvent.VK_DOWN:
					Engine.getPlayer().moveDown();
					break;
					
				case KeyEvent.VK_SPACE:
					space = true;
					break;
				// ---------------- JUMP --------------
				case KeyEvent.VK_SHIFT:
					System.out.println("SAD BI TREBAO DA SKOCIS :(");
					break;
				// --------------- END OF JUMP --------
					
					
				case KeyEvent.VK_TAB:
					engine.killAllEnemies();
					break;
				default:
					break;
			}
			
			
			/// FIRST TRY ------------------- >> MNOGO SPOR ODZIV, MORA TO BOLJE
			if (space == true){
				if (right == true){
					Engine.getPlayer().fire("right");
				}
				if (left == true){
					Engine.getPlayer().fire("left");
				}
			}
			
			
			Engine.getPlayer().destroyBullets();
			engine.keepPlayerWithinBorders(Engine.getPlayer()); // ------------------ KEEP PLAYER WITHIN BORDERS ------------
			refreshGUI();
		}
		
		canvas.repaint();
		return true;
	}

}
