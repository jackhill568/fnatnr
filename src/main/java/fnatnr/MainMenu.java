package fnatnr;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainMenu extends JPanel implements MouseListener {

	private static final int TOTAL_NIGHTS = 5;

	private int nightsUnlocked = 1;

	private Image backgroundImage = new ImageIcon("assets/menu_background.png").getImage();

	private Rectangle[] nightButtons = new Rectangle[TOTAL_NIGHTS];
	private Rectangle settingsButton;
	private Rectangle exitButton;

	private int hoveredButton = -1; // -1 = none, 0-4 = nights, 5 = settings, 6 = exit

	public interface MenuListener {
		void onNightSelected(int night);

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

	private void layoutButtons() {
		int btnW = (int) (getWidth() * 0.25);
		int btnH = (int) (getHeight() * 0.07);
		int btnX = (int) (getWidth() * 0.375);

		int startY = (int) (getHeight() * 0.25);
		int gapY = (int) (getHeight() * 0.1);
		for (int i = 0; i < TOTAL_NIGHTS; i++) {
			nightButtons[i] = new Rectangle(btnX, startY + i * gapY, btnW, btnH);
		}

		int bottomY = (int) (getHeight() * 0.82);
		int halfW = (int) (getWidth() * 0.12);
		settingsButton = new Rectangle((int) (getWidth() * 0.33) - halfW, bottomY, halfW * 2, btnH);
		exitButton = new Rectangle((int) (getWidth() * 0.67) - halfW, bottomY, halfW * 2, btnH);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		layoutButtons();

		if (backgroundImage != null) {
			g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
		} else {
			g.setColor(new Color(10, 5, 5));
			g.fillRect(0, 0, getWidth(), getHeight());
		}

		g.setColor(new Color(0, 0, 0, 120));
		g.fillRect(0, 0, getWidth(), getHeight());

		g.setColor(Color.WHITE);
		g.setFont(new Font("Monospaced", Font.BOLD, (int) (getHeight() * 0.03)));
		String title = "SELECT NIGHT";
		int tw = g.getFontMetrics().stringWidth(title);
		g.drawString(title, (getWidth() - tw) / 2, (int) (getHeight() * 0.18));

		for (int i = 0; i < TOTAL_NIGHTS; i++) {
			drawNightButton(g, i);
		}

		drawButton(g, settingsButton, "SETTINGS", 5, false);

		drawButton(g, exitButton, "EXIT", 6, false);
	}

	private void drawNightButton(Graphics g, int index) {
		boolean unlocked = index < nightsUnlocked;
		boolean hovered = hoveredButton == index;
		Rectangle btn = nightButtons[index];

		if (!unlocked) {
			g.setColor(new Color(40, 40, 40, 180));
		} else if (hovered) {
			g.setColor(new Color(180, 120, 0, 220));
		} else {
			g.setColor(new Color(100, 60, 0, 180));
		}
		g.fillRoundRect(btn.x, btn.y, btn.width, btn.height, 12, 12);

		g.setColor(unlocked ? Color.ORANGE : Color.DARK_GRAY);
		g.drawRoundRect(btn.x, btn.y, btn.width, btn.height, 12, 12);

		int fontSize = (int) (btn.height * 0.30);
		g.setFont(new Font("Monospaced", Font.BOLD, fontSize));
		String label = unlocked ? "Night " + (index + 1) : "Night " + (index + 1) + "  [LOCKED]";
		g.setColor(unlocked ? Color.WHITE : Color.GRAY);
		int lw = g.getFontMetrics().stringWidth(label);
		g.drawString(label, btn.x + (btn.width - lw) / 2, btn.y + (btn.height + fontSize) / 2 - 2);
	}

	private void drawButton(Graphics g, Rectangle btn, String label, int id, boolean destructive) {
		boolean hovered = hoveredButton == id;

		if (hovered) {
			g.setColor(destructive ? new Color(160, 30, 30, 220) : new Color(60, 60, 60, 220));
		} else {
			g.setColor(destructive ? new Color(80, 20, 20, 180) : new Color(30, 30, 30, 180));
		}
		g.fillRoundRect(btn.x, btn.y, btn.width, btn.height, 12, 12);

		g.setColor(hovered ? Color.WHITE : Color.GRAY);
		g.drawRoundRect(btn.x, btn.y, btn.width, btn.height, 12, 12);

		int fontSize = (int) (getHeight() * 0.025);
		g.setFont(new Font("Monospaced", Font.BOLD, fontSize));
		g.setColor(hovered ? Color.WHITE : Color.LIGHT_GRAY);
		int lw = g.getFontMetrics().stringWidth(label);
		g.drawString(label, btn.x + (btn.width - lw) / 2, btn.y + (btn.height + fontSize) / 2 - 2);
	}

	private void updateHover(int mx, int my) {
		int prev = hoveredButton;
		hoveredButton = -1;

		for (int i = 0; i < TOTAL_NIGHTS; i++) {
			if (nightButtons[i] != null && nightButtons[i].contains(mx, my)) {
				hoveredButton = i;
				break;
			}
		}
		if (settingsButton != null && settingsButton.contains(mx, my))
			hoveredButton = 5;
		if (exitButton != null && exitButton.contains(mx, my))
			hoveredButton = 6;

		if (hoveredButton != prev)
			repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int mx = e.getX(), my = e.getY();

		for (int i = 0; i < TOTAL_NIGHTS; i++) {
			if (nightButtons[i] != null && nightButtons[i].contains(mx, my)) {
				if (i < nightsUnlocked) {
					listener.onNightSelected(i + 1);
				}
				return;
			}
		}

		if (settingsButton != null && settingsButton.contains(mx, my)) {
			listener.onSettingsClicked();
		}
		if (exitButton != null && exitButton.contains(mx, my)) {
			listener.onExitClicked();
		}
	}

	public void unlockNextNight() {
		if (nightsUnlocked < TOTAL_NIGHTS) {
			nightsUnlocked++;
		}
		repaint();
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
