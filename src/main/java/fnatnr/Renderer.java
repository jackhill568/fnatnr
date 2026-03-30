package fnatnr;

import fnatnr.Enemys.*;

import fnatnr.NightTools.NightTimer;

import java.awt.Image;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.ImageIcon;
import java.awt.Font;

public class Renderer {

	private Image tableImage = new ImageIcon("assets/table.png").getImage();
	private Image sinkImage = new ImageIcon("assets/sink.png").getImage();
	private Image doorImage = new ImageIcon("assets/door.png").getImage();

	private GamePanel gamePanel;

	public Renderer(GamePanel panel) {
		this.gamePanel = panel;
	}

	private void rectFill(Graphics g, Color color) {
		g.setColor(new Color(30, 20, 10));
		g.fillRect(0, 0, gamePanel.getWidth(), gamePanel.getHeight());
	}

	private void drawBackground(Graphics g, Image image) {
		g.drawImage(image, 0, 0, gamePanel.getWidth(), gamePanel.getHeight(), gamePanel);
	}

	private Font scaledFont(int style, int size) {
		float[] sizes = { 0.012f, 0.018f, 0.022f, 0.028f, 0.034f, 0.04f, 0.05f, 0.06f, 0.07f, 0.08f };
		float scale = sizes[Math.max(0, Math.min(size - 1, sizes.length - 1))];
		return new Font("Monospaced", style, (int) (gamePanel.getHeight() * scale));
	}

	private void drawCentred(Graphics g, String text, float yPercent) {
		int fw = g.getFontMetrics().stringWidth(text);
		int x = (gamePanel.getWidth() - fw) / 2;
		int y = (int) (gamePanel.getHeight() * yPercent);
		g.drawString(text, x, y);
	}

	private void drawAt(Graphics g, String text, float xPercent, float yPercent) {
		g.drawString(text,
				(int) (gamePanel.getWidth() * xPercent),
				(int) (gamePanel.getHeight() * yPercent));
	}

	private void drawTime(Graphics g) {
		g.setColor(Color.WHITE);
		g.setFont(scaledFont(Font.BOLD, 3));
		String time = NightTimer.getInstance().getTimeString();
		drawAt(g, time, 0.90f, 0.98f);
	}

	public void drawTable(Graphics g) {
		rectFill(g, new Color(30, 20, 10));
		drawBackground(g, tableImage);
	}

	public void drawSink(Graphics g) {
		rectFill(g, new Color(30, 20, 10));
		drawBackground(g, sinkImage);
	}

	public void drawDoorway(Graphics g) {
		rectFill(g, new Color(30, 20, 10));
		drawBackground(g, doorImage);

		for (Enemy enemy : gamePanel.getEnemies()) {
			if (enemy.getRoom().getName().equals("Doorway")) {
				enemy.drawSprite(g);
			}
		}

		g.setColor(gamePanel.getDoorClosed() ? Color.RED : Color.GREEN);
		String status = gamePanel.getDoorClosed() ? "DOOR CLOSED" : "DOOR OPEN";
		g.setFont(scaledFont(Font.BOLD, 5));
		drawCentred(g, status, 0.1f);
	}

	public void drawCameras(Graphics g) {
		rectFill(g, new Color(10, 30, 10));

		Room room = gamePanel.getSelectedCamera();
		drawBackground(g, room.getImage());

		g.setColor(Color.GREEN);
		g.setFont(scaledFont(Font.BOLD, 4));
		drawAt(g, room.getName(), 0.1f, 0.1f);

		drawEnemies(g);
	}

	public void drawStatusBar(Graphics g) {
		int barX = (int) (gamePanel.getWidth() * 0.75);
		int barY = (int) (gamePanel.getHeight() * 0.05);
		int barW = (int) (gamePanel.getWidth() * 0.2);
		int barH = (int) (gamePanel.getHeight() * 0.03);
		int pad = (int) (gamePanel.getWidth() * 0.01);
		int fontSize = (int) (gamePanel.getHeight() * 0.018);

		g.setColor(new Color(0, 0, 0, 180));
		g.fillRoundRect(barX - pad, barY - fontSize - pad, barW + pad * 2, barH + fontSize + pad * 3, 10, 10);

		g.setColor(Color.WHITE);
		g.setFont(scaledFont(Font.BOLD, 3));
		drawAt(g, "Sink Shame", 0.75f, 0.043f);

		float pct = Math.min(1.0f, Math.max(0.0f, gamePanel.getSink().getLevel()));
		int red = (int) (255 * pct);
		int green = (int) (255 * (1 - pct));
		g.setColor(new Color(red, green, 0));
		g.fillRect(barX, barY, (int) (barW * pct), barH);

		g.setColor(Color.WHITE);
		g.drawRect(barX, barY, barW, barH);

	}

	public void drawHUD(Graphics g) {
		int hudH = (int) (gamePanel.getHeight() * 0.05);

		g.setColor(new Color(0, 0, 0, 160));
		g.fillRect(0, gamePanel.getHeight() - hudH, gamePanel.getWidth(), hudH);

		g.setColor(Color.WHITE);
		g.setFont(scaledFont(Font.PLAIN, 2));

		switch (gamePanel.getCurrentScreen()) {
			case TABLE:
				drawAt(g, "[C] Cameras   [D] Doorway   [S] Sink", 0.005f, 0.98f);
				break;
			case SINK:
				drawAt(g, "[F] Clean   [T] Back to Table", 0.005f, 0.98f);
				break;
			case DOOR:
				drawAt(g, "[Q] Toggle Door   [T] Back to Table", 0.005f, 0.98f);
				break;
			case CAMERAS:
				drawAt(g, "[C] Put Away Camera   [LEFT] [RIGHT] Switch Room", 0.005f, 0.98f);
				break;
		}

		drawTime(g);
	}

	public void drawGameOver(Graphics g) {
		rectFill(g, Color.BLACK);

		g.setColor(Color.RED);
		g.setFont(scaledFont(Font.BOLD, 9));
		String title = "GAME OVER";
		drawCentred(g, title, 0.4f);

		g.setColor(Color.GRAY);
		g.setFont(scaledFont(Font.BOLD, 4));
		String prompt = "Click to continue";
		drawCentred(g, prompt	, 0.6f);
	}

	public void drawGameWin(Graphics g) {
		rectFill(g, Color.BLACK);

		g.setColor(Color.YELLOW);
		g.setFont(scaledFont(Font.BOLD, 9));
		String title = "SURVIVED";
		drawCentred(g, title, 0.4f);

		g.setColor(Color.WHITE);
		g.setFont(scaledFont(Font.PLAIN, 5));
		String sub = "Night " + gamePanel.getNight() + " Complete";
		drawCentred(g, sub, 0.5f);

		g.setColor(Color.GRAY);
		g.setFont(scaledFont(Font.BOLD, 4));
		String prompt = "Click to continue";
		drawCentred(g, prompt	, 0.6f);
	}

	public void drawEnemies(Graphics g) {
		for (Enemy enemy : gamePanel.getEnemies()) {
			if (gamePanel.getSelectedCamera() == enemy.getRoom()) {
				enemy.drawSprite(g);
			}
		}
	}

}
