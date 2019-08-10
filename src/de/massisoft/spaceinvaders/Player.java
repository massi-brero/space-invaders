package de.massisoft.spaceinvaders;

import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;

public class Player extends Sprite implements Constants {
	private final int START_X = 280;
	private final int START_Y = 270;

	private final String playerImg = "src/images/ship.png";
	private int width;

	public Player() {
		initPlayer();
	}

	private void initPlayer() {
		ImageIcon ii = new ImageIcon(playerImg);

		width = ii.getImage().getWidth(null);

		setImage(ii.getImage());
		setX(START_X);
		setY(START_Y);
	}

	public void act() {
		setX(getX() + getDx());

		x = x <= 2 ? 2 : x;
		x = x >= BOARD_WIDTH - 2 * width ? BOARD_WIDTH - 2 * width : x;
	}

	/**
	 * 
	 * @param e
	 */
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();

		if (key == KeyEvent.VK_LEFT) {
			setDx(-2);
		}

		if (key == KeyEvent.VK_RIGHT) {
			setDx(2);
		}
	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();

		if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) {
			setDx(0);
		}

	}

}
