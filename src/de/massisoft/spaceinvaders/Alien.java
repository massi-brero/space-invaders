package de.massisoft.spaceinvaders;

import javax.swing.ImageIcon;

public class Alien extends Sprite {
	private Bomb bomb;
	private final String alienImg = "src/images/alien_small.png";
	
	public Alien() {
	}
	
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
	
	public Bomb getBomb() {
		return bomb;
	}
	
	public String getAlienImg() {
		return alienImg;
	}
	
	
	public class Bomb extends Sprite {
		private final String bombImg = "src/images/bomb_small.png";
		private boolean destroyed;
		
		public Bomb() {
			initBomb();
		}

		private void initBomb() {
			
			setDestroyed(true);
			this.setX(x);
			this.setY(y);
			
			ImageIcon ii = new ImageIcon(bombImg);
			setImage(ii.getImage());			
		}

		public boolean isDestroyed() {
			return destroyed;
		}

		public void setDestroyed(boolean destroyed) {
			this.destroyed = destroyed;
		}
		
	}
}
