package de.massisoft.spaceinvaders;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
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
	private String message = "GAME OVER";

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
				Alien alien = new Alien(ALIEN_INIT_X + 18 * j, ALIEN_INIT_Y + 18 * i);
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

	public void drawAliens(Graphics g) {
		Iterator<Alien> it = aliens.iterator();

		for (Alien alien : aliens) {
			if (alien.isVisible()) {
				g.drawImage(alien.getImage(), alien.getX(), alien.getY(), this);
			}

			if (alien.isDying()) {
				alien.die();
			}
		}
	}

	public void drawPlayer(Graphics g) {

		if (player.isVisible()) {
			g.drawImage(player.getImage(), player.getX(), player.getY(), this);
		}

		if (player.isDying()) {
			player.die();
			running = false;
		}
	}

	public void drawShot(Graphics g) {
		if (shot.isVisible()) {
			g.drawImage(shot.getImage(), shot.getX(), shot.getY(), this);
		}
	}

	public void drawBombing(Graphics g) {
		for (Alien alien : aliens) {
			Alien.Bomb b = alien.getBomb();

			if (!b.isDestroyed()) {
				g.drawImage(b.getImage(), b.getX(), b.getY(), this);
			}
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(Color.black);
		g.fillRect(0, 0, dim.width, dim.height);
		g.setColor(Color.green);
		
		if (running) {
			g.drawLine(0, 0, dim.width, dim.height);
			drawAliens(g);
			drawPlayer(g);
			drawShot(g);
			drawBombing(g);
		}
		
		Toolkit.getDefaultToolkit().sync();
		g.dispose();
		
	}
	
	public void gameOver() {
		Graphics g = this.getGraphics();
		
		g.setColor(Color.black);
		g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);
		
		g.setColor(new Color(0, 32, 48));
		g.fillRect(50, BOARD_WIDTH / 2 - 30, BOARD_HEIGHT- 50, 50);
		
		g.setColor(Color.white);
		g.fillRect(50, BOARD_WIDTH / 2 - 30, BOARD_HEIGHT - 50, 50);
		
		Font small = new Font("HELVETICA", Font.BOLD, 14);
		FontMetrics metrics = this.getFontMetrics(small);
		
		g.setColor(Color.white);
		g.setFont(small);
		g.drawString(message, (BOARD_WIDTH  - metrics.stringWidth(message) / 2 ), BOARD_HEIGHT / 2);
	}
	
	public void animationCycle() {
		if (deaths == NUMBER_OF_ALIENS_TO_DESTROY) {
			running = false;
			message = "GAME WON!";
		}
		
		/**
		 * Player
		 */
		player.act();
		
		/**
		 * Shot
		 */
		if (shot.isVisible()) {
			int shotX = shot.getX();
			int shotY = shot.getY();
			
			for (Alien alien : aliens) {
				int alienX = alien.getX();
				int alienY = alien.getY();
				
				if (alien.isVisible()) {
					if (shotX >= alienX
						&& shotX <= alienX + ALIEN_WIDTH
						&& shotY >= alienY
						&& shotY <= alienY - ALIEN_HEIGHT
						) {
						ImageIcon ii = new ImageIcon(explImg);
						alien.setImage(ii.getImage());
						alien.setDying(true);
						deaths++;
						shot.die();
					}
					
				}
			}

			// shot moves upwards
			if (shot.getY() < 0) {
				shot.die();
			} else {
				shot.setY(shot.getY() - 4);
			}
			
			/*
			 * Alien
			 */
			for (Alien alien : aliens) {
				if (alien.getX() >= BOARD_WIDTH - BORDER_RIGHT && direction != -1) {
					direction = -1;
					Iterator<Alien> i1 = aliens.iterator();
					
					while (i1.hasNext()) {
						Alien a2 = (Alien) i1.next();
						a2.setY(a2.getY() + GO_DOWN);
					}
				}
				
				if(alien.getX() <= BORDER_LEFT && direction == 1) {
					direction = 1;
					Iterator<Alien> i2 = aliens.iterator();
					
					while (i2.hasNext()) {
						Alien a2 = (Alien) i2.next();
						a2.setY(a2.getY() + GO_DOWN);
					}
					
				}
			}
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
