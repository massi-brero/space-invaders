package de.massisoft.spaceinvaders;

import javax.swing.ImageIcon;

public class Alien extends Sprite {
	private Bomb bomb;
	private final String alienImg = "src/mages/alien.png";
	
	public Alien(int x, int y) {
		initAlient(x, y);
	}

	private void initAlient(int x, int y) {
		setX(x);
		setY(y);
		
		bomb = new Bomb();
		setImage(new ImageIcon(alienImg).getImage());
	}
	
	public void act(int direction) {
		setX(getX() + direction);
	}
	
	public class Bomb extends Sprite {
		
	}
}
