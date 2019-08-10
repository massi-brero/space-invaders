package de.massisoft.spaceinvaders;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JPanel;

public class Board extends JPanel implements Runnable, Constants {

	private Dimension dim;
	private ArrayList<Alien> aliens;
	private Player player;
	private Shot shot;
	
	private final int ALIEN_INIT_X = 150;
	private final int ALIEN_INIT_Y = 5;
	private int direction = 1;
	private int deaths = 0;
	
	private boolean running = true;
	private final String explImg = "src/images/explosion.png";
	private String mesaage = "GAME OVER";

	private Thread animator;
	
	private static final long serialVersionUID = 6433999049281448757L;

	public Board() {
		initBoard();
	}

	private void initBoard() {
		addKeyListener(new TAdapter());
		setFocusable(true);
		dim = new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
		setBackground(Color.black);
		
		gameInit();
		setDoubleBuffered(true);
	}
	
	@Override
	public void addNotify() {
		super.addNotify();
		gameInit();
	}
	
	private void gameInit() {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 6; j++) {
				Alien alien = new Alien(ALIEN_INIT_X + 18 * j, ALIEN_INIT_Y + 18 *i);
				aliens.add(alien);
			}
		}
		
		player = new Player();
		shot = new Shot();
		
		if (animator == null || !running) {
			animator = new Thread(this);
			animator.start();
		}
	}

	private class TAdapter extends KeyAdapter {
		
		@Override
		public void keyReleased(KeyEvent e) {
			player.keyReleased(e);
		}
		
		@Override
		public void keyPressed(KeyEvent e) {
			player.keyPressed(e);
			
			int x = player.getX();
			int y = player.getY();
			
			int key = e.getKeyCode();
			
			if (key == KeyEvent.VK_SPACE) {
				if (running) {
					if (!shot.isVisible()) {
						shot = new Shot(x, y);
					}
				}
			}
		}
		
	}
	
}
