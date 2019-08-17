package de.massisoft.spaceinvaders;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Board extends JPanel implements Runnable, Constants {

	private Dimension dim;
	private ArrayList<Alien> aliens;
	private Player player;
	private Shot shot;

	private final int ALIEN_INIT_X = 150;
	private final int ALIEN_INIT_Y = 5;
	private int direction = -1;
	private int deaths = 0;

	private boolean running = false;
	private final String explImg = "src/images/explosion_small.png";
	private final String alienImg = "src/images/alien_counter.png";
	private final String logo = "src/images/logo.png";
	private String messageGameOver = "GAME OVER";

	private Thread animator;
	private boolean splashScreen = true;

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
		aliens = new ArrayList<Alien>();

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
		// Iterator<Alien> it = aliens.iterator();

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

	public void drawCounter(Graphics g) {
		String message = deaths + " / " + NUMBER_OF_ALIENS_TO_DESTROY;
		ImageIcon ii = new ImageIcon(alienImg);
		int fontSize = 10;
		int height = (int) Math.floor(BOARD_HEIGHT - ALIEN_HEIGHT * 4.5);
		g.drawImage(ii.getImage(), 10, height, this);

		Font counterFont = new Font("Courier", Font.BOLD, fontSize);
		g.setFont(counterFont);
		g.drawString(message, 30, height + fontSize - 1);
	}

	public void showSplashScreen(Graphics g) {
		ImageIcon ii = new ImageIcon(logo);
		g.drawImage(ii.getImage(), (BOARD_WIDTH - ii.getIconWidth()) / 2, 40, this);
		
		String message = "Press Space To Start!";
		drawText(g, message);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.setColor(Color.black);
		g.fillRect(0, 0, dim.width, dim.height);
		g.setColor(Color.green);
		
		if (splashScreen) {
			showSplashScreen(g);
		}

		if (running) {
			g.drawLine(0, GROUND, BOARD_WIDTH, GROUND);
			drawAliens(g);
			drawPlayer(g);
			drawShot(g);
			drawBombing(g);
			drawCounter(g);
		}

		Toolkit.getDefaultToolkit().sync();
		g.dispose();
	}

	public void gameOver() {
		Graphics g = this.getGraphics();

		g.setColor(Color.black);
		g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);

		g.setColor(new Color(0, 32, 48));
		g.fillRect(50, BOARD_WIDTH / 2 - 30, BOARD_WIDTH - 100, 50);

		g.setColor(Color.white);
		g.drawRect(50, BOARD_WIDTH / 2 - 30, BOARD_WIDTH - 100, 50);

		drawText(g, messageGameOver);
	}

	/**
	 * Draws a string in the middle of the screen
	 * 
	 * @param Graphics g
	 */
	private void drawText(Graphics g, String message) {
		Font small = new Font("HELVETICA", Font.BOLD, 14);
		FontMetrics metrics = this.getFontMetrics(small);

		g.setColor(Color.white);
		g.setFont(small);
		g.drawString(message, (BOARD_WIDTH - metrics.stringWidth(message)) / 2, BOARD_WIDTH / 2);
	}

	public void animationCycle() {
		if (deaths == NUMBER_OF_ALIENS_TO_DESTROY) {
			running = false;
			messageGameOver = "GAME WON!";
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

				if (alien.isVisible() && shot.isVisible()) {
					if (shotX >= alienX && shotX <= alienX + ALIEN_WIDTH && shotY <= alienY
							&& shotY >= alienY - ALIEN_HEIGHT) {
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

			if (alien.getX() <= BORDER_LEFT && direction != 1) {
				direction = 1;
				Iterator<Alien> i2 = aliens.iterator();

				while (i2.hasNext()) {
					Alien a2 = (Alien) i2.next();
					a2.setY(a2.getY() + GO_DOWN);
				}

			}
		}

		Iterator<Alien> it = aliens.iterator();
		while (it.hasNext()) {
			Alien alien = (Alien) it.next();

			if (alien.isVisible()) {
				if (alien.getY() > GROUND - ALIEN_HEIGHT) {
					running = false;
					messageGameOver = "INVASION!";
				}

				alien.act(direction);
			}
		}

		/*
		 * Bomb
		 */
		Random generator = new Random();

		for (Alien alien : aliens) {
			int shotChance = generator.nextInt(15);
			Alien.Bomb b = alien.getBomb();

			if (shotChance == CHANCE && alien.isVisible() && b.isDestroyed()) {
				b.setDestroyed(false);
				b.setX(alien.getX());
				b.setY(alien.getY());
			}

			if (player.isVisible() && !b.isDestroyed()) {
				if (b.getX() >= player.getX() && b.getX() <= player.getX() + PLAYER_WIDTH && b.getY() >= player.getY()
						&& b.getY() <= player.getY() + PLAYER_HEIGHT) {
					ImageIcon ii = new ImageIcon(explImg);
					player.setImage(ii.getImage());
					player.setDying(true);
					b.setDestroyed(true);
				}
			}

			if (!b.isDestroyed()) {
				b.setY(b.getY() + 1);

				if (b.getY() >= GROUND - BOMB_HEIGHT) {
					b.setDestroyed(true);
				}
			}
		}
	}

	@Override
	public void run() {

		long beforeTime, timeDiff, sleep;
		beforeTime = System.currentTimeMillis();
		
		while (splashScreen) {
			repaint();
		}
		
		while (running) {
			repaint();
			animationCycle();

			timeDiff = System.currentTimeMillis() - beforeTime;
			sleep = DELAY - timeDiff;

			if (sleep < 0) {
				sleep = 2;
			}

			try {
				Thread.sleep(sleep);
			} catch (InterruptedException e) {
				System.out.println("interrupted");
			}

			beforeTime = System.currentTimeMillis();
		}

		gameOver();

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
				if (running) { // game is running
					if (!shot.isVisible()) {
						shot = new Shot(x, y);
					}
				} else if (splashScreen) { // we are on the start screen
					splashScreen = false;
					running = true;
				} else {
					splashScreen = true;
					gameInit();
				}
			}
		}
	}
}
