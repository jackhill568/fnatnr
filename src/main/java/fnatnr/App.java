package fnatnr;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import fnatnr.Enemys.*;

public class App {

	private Enemy[] enemys;

	public static void main(final String[] args) {

		SwingUtilities.invokeLater(() -> {
			final JFrame frame = new JFrame("Five Nights at 290 Renfrew");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			GamePanel gamePanel = new GamePanel();
			frame.add(gamePanel);
			frame.pack(); 
			frame.setLocationRelativeTo(null); 
			frame.setVisible(true);

			gamePanel.startGame();

		});
	}
}
