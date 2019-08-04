package de.massisoft.spaceinvaders;

import java.awt.Image;

public class Sprite {
	private boolean visible;
	private boolean dying;
	private Image image;
	protected int x;
	protected int y;
	protected int dx;
	
	public Sprite() {
		setVisible(true);
	}
	
	public void die() {
		setVisible(false);
	}
	
	public boolean isVisible() {
		return visible;
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	public boolean isDying() {
		return dying;
	}
	public void setDying(boolean dying) {
		this.dying = dying;
	}
	public Image getImage() {
		return image;
	}
	public void setImage(Image image) {
		this.image = image;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getDx() {
		return dx;
	}
	public void setDx(int dx) {
		this.dx = dx;
	}
}
