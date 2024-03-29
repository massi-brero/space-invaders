package de.massisoft.spaceinvaders;

import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;

public class App extends JFrame implements Constants {
	
	private static final long serialVersionUID = 1L;

	public App() {
		initUI();
	}
	
	public void initUI(	) {
			add(new Board());
			setTitle(GAME_NAME);
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			setSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
			setLocationRelativeTo(null);
			setResizable(false);
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			App app = new App();
			app.setVisible(true);
		});
	}
	
}
