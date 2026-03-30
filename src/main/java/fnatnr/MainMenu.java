package fnatnr;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class MainMenu extends JPanel implements MouseListener {

	private static final int TOTAL_NIGHTS = 5;

	public enum MenuState {
		MAIN,
		NIGHT_SELECT
	}

	private MenuState state = MenuState.MAIN;
	private int nightsUnlocked;
	private int hoveredButton = -1;

	private Image backgroundImage = new ImageIcon("assets/menu_background.png").getImage();

	private Rectangle btnContinue;
	private Rectangle btnNewGame;
	private Rectangle btnSettings;
	private Rectangle btnExit;

	private Rectangle[] nightButtons = new Rectangle[TOTAL_NIGHTS];
	private Rectangle btnBack;

	public interface MenuListener {
		void onNewGame();

		void onContinue(int night);

		void onSettingsClicked();

		void onExitClicked();
	}

	private MenuListener listener;

	public MainMenu(MenuListener listener, int nightsUnlocked) {
		this.listener = listener;
		this.nightsUnlocked = nightsUnlocked;

		setPreferredSize(new Dimension(1024, 768));
		setBackground(Color.BLACK);
		addMouseListener(this);
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				updateHover(e.getX(), e.getY());
			}
		});
	}

	private void layoutMain() {
		int btnW = (int) (getWidth() * 0.22);
		int btnH = (int) (getHeight() * 0.07);
		int btnX = (int) (getWidth() * 0.06);
		int gap = (int) (getHeight() * 0.03);

		int startY = (int) (getHeight() * 0.35);
		btnContinue = new Rectangle(btnX, startY, btnW, btnH);
		btnNewGame = new Rectangle(btnX, startY + (btnH + gap), btnW, btnH);
		btnSettings = new Rectangle(btnX, startY + (btnH + gap) * 2, btnW, btnH);
		btnExit = new Rectangle(btnX, startY + (btnH + gap) * 3, btnW, btnH);
	}

	private void layoutNightSelect() {
		int btnW = (int) (getWidth() * 0.22);
		int btnH = (int) (getHeight() * 0.07);
		int btnX = (int) (getWidth() * 0.06);
		int gap = (int) (getHeight() * 0.025);

		int startY = (int) (getHeight() * 0.2);
		for (int i = 0; i < TOTAL_NIGHTS; i++) {
			nightButtons[i] = new Rectangle(btnX, startY + i * (btnH + gap), btnW, btnH);
		}
		btnBack = new Rectangle(btnX, (int) (getHeight() * 0.85), btnW, btnH);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

		g.setColor(new Color(0, 0, 0, 160));
		g.fillRect(0, 0, getWidth(), getHeight());

		g.setColor(Color.WHITE);
		g.setFont(new Font("Monospaced", Font.BOLD, (int) (getHeight() * 0.06)));
		String title = "Five Nights at 290 Renfrew";
		g.drawString(title, (int) (getWidth() * 0.06), (int) (getHeight() * 0.22));

		g.setColor(new Color(200, 200, 200, 120));
		g.fillRect((int) (getWidth() * 0.06), (int) (getHeight() * 0.26), (int) (getWidth() * 0.4), 2);

		switch (state) {
			case MAIN:
				drawMain(g);
				break;
			case NIGHT_SELECT:
				drawNightSelect(g);
				break;
		}
	}

	private void drawMain(Graphics g) {
		layoutMain();

		boolean hasSave = nightsUnlocked > 1;

		drawMenuButton(g, btnContinue, "Continue", 0, !hasSave); // greyed if no save
		drawMenuButton(g, btnNewGame, "New Game", 1, false);
		drawMenuButton(g, btnSettings, "Settings", 2, false);
		drawMenuButton(g, btnExit, "Exit", 3, false);

		if (!hasSave) {
			g.setColor(Color.GRAY);
			g.setFont(new Font("Monospaced", Font.PLAIN, (int) (getHeight() * 0.018)));
			g.drawString("No save found", btnContinue.x, btnContinue.y + btnContinue.height + (int) (getHeight() * 0.025));
		}
	}

	private void drawNightSelect(Graphics g) {
		layoutNightSelect();

		g.setColor(Color.WHITE);
		g.setFont(new Font("Monospaced", Font.BOLD, (int) (getHeight() * 0.035)));
		g.drawString("Select Night", (int) (getWidth() * 0.06), (int) (getHeight() * 0.16));

		for (int i = 0; i < TOTAL_NIGHTS; i++) {
			boolean unlocked = i < nightsUnlocked;
			boolean hovered = hoveredButton == 10 + i;
			Rectangle btn = nightButtons[i];

			if (!unlocked)
				g.setColor(new Color(30, 30, 30, 180));
			else if (hovered)
				g.setColor(new Color(180, 120, 0, 220));
			else
				g.setColor(new Color(80, 55, 0, 180));
			g.fillRoundRect(btn.x, btn.y, btn.width, btn.height, 10, 10);

			g.setColor(unlocked ? Color.ORANGE : Color.DARK_GRAY);
			g.drawRoundRect(btn.x, btn.y, btn.width, btn.height, 10, 10);

			int fontSize = (int) (btn.height * 0.38);
			g.setFont(new Font("Monospaced", Font.BOLD, fontSize));
			FontMetrics fm = g.getFontMetrics();
			String label = "Night " + (i + 1) + (unlocked ? "" : "  [LOCKED]");
			while (fm.stringWidth(label) > btn.width - 16 && fontSize > 6) {
				fontSize--;
				g.setFont(new Font("Monospaced", Font.BOLD, fontSize));
				fm = g.getFontMetrics();
			}
			g.setColor(unlocked ? Color.WHITE : Color.GRAY);
			g.drawString(label, btn.x + 10, btn.y + (btn.height + fontSize) / 2 - 2);
		}

		drawMenuButton(g, btnBack, "Back", 20, false);
	}

	private void drawMenuButton(Graphics g, Rectangle btn, String label, int id, boolean disabled) {
		boolean hovered = hoveredButton == id && !disabled;

		if (disabled)
			g.setColor(new Color(30, 30, 30, 150));
		else if (hovered)
			g.setColor(new Color(80, 80, 80, 220));
		else
			g.setColor(new Color(20, 20, 20, 180));
		g.fillRoundRect(btn.x, btn.y, btn.width, btn.height, 10, 10);

		g.setColor(disabled ? Color.DARK_GRAY : hovered ? Color.WHITE : new Color(150, 150, 150));
		g.drawRoundRect(btn.x, btn.y, btn.width, btn.height, 10, 10);

		int fontSize = (int) (btn.height * 0.38);
		g.setFont(new Font("Monospaced", Font.BOLD, fontSize));
		g.setColor(disabled ? Color.DARK_GRAY : hovered ? Color.WHITE : Color.LIGHT_GRAY);
		g.drawString(label, btn.x + (int) (btn.width * 0.1), btn.y + (btn.height + fontSize) / 2 - 2);
	}

	private void updateHover(int mx, int my) {
		int prev = hoveredButton;
		hoveredButton = -1;

		if (state == MenuState.MAIN) {
			layoutMain();
			if (btnContinue != null && btnContinue.contains(mx, my) && nightsUnlocked > 1)
				hoveredButton = 0;
			if (btnNewGame != null && btnNewGame.contains(mx, my))
				hoveredButton = 1;
			if (btnSettings != null && btnSettings.contains(mx, my))
				hoveredButton = 2;
			if (btnExit != null && btnExit.contains(mx, my))
				hoveredButton = 3;
		} else {
			layoutNightSelect();
			for (int i = 0; i < TOTAL_NIGHTS; i++) {
				if (nightButtons[i] != null && nightButtons[i].contains(mx, my) && i < nightsUnlocked)
					hoveredButton = 10 + i;
			}
			if (btnBack != null && btnBack.contains(mx, my))
				hoveredButton = 20;
		}

		if (hoveredButton != prev)
			repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int mx = e.getX(), my = e.getY();

		if (state == MenuState.MAIN) {
			layoutMain();
			if (btnContinue.contains(mx, my) && nightsUnlocked > 1) {
				state = MenuState.NIGHT_SELECT;
				hoveredButton = -1;
				repaint();
				return;
			}
			if (btnNewGame.contains(mx, my)) {
				listener.onNewGame();
				return;
			}
			if (btnSettings.contains(mx, my)) {
				listener.onSettingsClicked();
				return;
			}
			if (btnExit.contains(mx, my)) {
				listener.onExitClicked();
				return;
			}
		} else {
			layoutNightSelect();
			for (int i = 0; i < TOTAL_NIGHTS; i++) {
				if (nightButtons[i].contains(mx, my) && i < nightsUnlocked) {
					listener.onContinue(i + 1);
					return;
				}
			}
			if (btnBack.contains(mx, my)) {
				state = MenuState.MAIN;
				hoveredButton = -1;
				repaint();
			}
		}
	}

	public void setNightsUnlocked(int n) {
		this.nightsUnlocked = Math.min(n, TOTAL_NIGHTS);
		repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
}
