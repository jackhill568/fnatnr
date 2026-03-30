package fnatnr;

import javax.swing.*;

public class GameWindow implements MainMenu.MenuListener {

	private JFrame frame;
	private MainMenu mainMenu;
	private int nightsUnlocked = 1;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new GameWindow().start());
	}

	public void start() {
		frame = new JFrame("Five Nights at 290 Renfrew");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);

		showMainMenu();

		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	private void showMainMenu() {
		mainMenu = new MainMenu(this, nightsUnlocked);
		frame.setContentPane(mainMenu);
		frame.pack();
		mainMenu.requestFocusInWindow();
		frame.revalidate();
	}

	private void startNight(int night) {
		GamePanel gamePanel = new GamePanel(night, () -> onNightComplete(night), () -> onGameOver());
		frame.setContentPane(gamePanel);
		frame.pack();
		gamePanel.startGame();
		frame.revalidate();
	}

	private void onNightComplete(int night) {
		if (night >= nightsUnlocked) {
			nightsUnlocked = night + 1;
		}
		SwingUtilities.invokeLater(() -> {
			mainMenu.setNightsUnlocked(nightsUnlocked);
			showMainMenu();
		});
	}

	private void onGameOver() {
		SwingUtilities.invokeLater(() -> showMainMenu());
	}

	@Override
	public void onNightSelected(int night) {
		startNight(night);
	}

	@Override
	public void onSettingsClicked() {
		JOptionPane.showMessageDialog(frame, "No Settings", "Settings", JOptionPane.INFORMATION_MESSAGE);
	}

	@Override
	public void onExitClicked() {
		System.exit(0);
	}
}
