package fnatnr;

import javax.swing.*;
import javax.swing.Timer;

import fnatnr.Enemys.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class GamePanel extends JPanel implements ActionListener {

	private static final int TICK_RATE_MS = 1000;
	private javax.swing.Timer gameLoop = new Timer(500, this);

	public enum Screen {
		TABLE, CAMERAS, SINK, DOOR
	}

	private Screen currentScreen = Screen.TABLE;

	private boolean leftDoorClosed = false;

	private boolean gameOver = false;
	private boolean taskComplete = false;

	private List<Enemy> enemys;
	private Sink sink;

	private Room[] rooms;
	private int selectedCamera = 0;

	private Image tableImage = new ImageIcon("assets/table.png").getImage();
	private Image sinkImage = new ImageIcon("assets/sink.png").getImage();
	private Image doorImage = new ImageIcon("assets/door.png").getImage();

	public GamePanel() {
		setPreferredSize(new Dimension(1024, 768));
		setBackground(Color.BLACK);
		setFocusable(true);

		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				handleKeyPress(e.getKeyCode());
			}
		});

		this.sink = new Sink(50, 1, 1);

		createRooms();
		createEnemys(sink);
		gameLoop.start();
	}

	private void createRooms() {
		Room victor = new Room("Victor Room", "assets/rooms/victorRoom.png");
		Room cayden = new Room("Cayden Room", "assets/rooms/caydenRoom.png");
		Room josh = new Room("Josh Room", "assets/rooms/joshRoom.png");
		Room nathan = new Room("Nathan Room", "assets/rooms/nathanRoom.png");
		Room jack = new Room("Jack Room", "assets/rooms/jackRoom.png");
		Room kazuma = new Room("Kazuma Cove", "assets/rooms/kazumaRoom.png");
		Room hall = new Room("Hall", "assets/rooms/hall.png");
		Room stairs = new Room("Stairs", "assets/rooms/stairs.png");
		Room doorway = new Room("Doorway", "assets/rooms/doorway.png");
		Room kitchen = new Room("Kitchen", "assets/rooms/kitchen.png");

		hall.setNeighbours(new Room[] { stairs, victor, cayden, josh, nathan, jack });
		stairs.setNeighbours(new Room[] { doorway, hall, kazuma });
		doorway.setNeighbours(new Room[] { kitchen, stairs, kazuma });
		kitchen.setNeighbours(new Room[] { doorway, hall });
		victor.setNeighbours(new Room[] { hall });
		cayden.setNeighbours(new Room[] { hall });
		josh.setNeighbours(new Room[] { hall });
		nathan.setNeighbours(new Room[] { hall });
		jack.setNeighbours(new Room[] { hall });
		kazuma.setNeighbours(new Room[] { doorway, stairs });

		rooms = new Room[] { victor, cayden, josh, nathan, jack, kazuma, hall, stairs, doorway, kitchen };
	}

	private void createEnemys(Sink sink) {
		enemys = new ArrayList<>();
		Enemy test = new ClassicEnemy("cheese", 10, rooms[2], rooms);
		enemys.add(test);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (gameOver)
			return;

		for (Enemy enemy : enemys) {
			enemy.chanceMove();
		}
		sink.update();

		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (gameOver) {
			drawGameOver(g);
			return;
		}

		switch (currentScreen) {
			case TABLE:
				drawTable(g);
				drawStatusBar(g);
				break;
			case SINK:
				drawSink(g);
				drawStatusBar(g);
				break;
			case DOOR:
				drawDoorway(g);
				drawStatusBar(g);
				break;
			case CAMERAS:
				drawCameras(g);
				break;
		}

		drawHUD(g);
	}

	private void drawTable(Graphics g) {
		g.setColor(new Color(30, 20, 10));
		g.fillRect(0, 0, getWidth(), getHeight());
		g.drawImage(tableImage, 0, 0, getWidth(), getHeight(), this);
	}

	private void drawSink(Graphics g) {
		g.setColor(new Color(30, 20, 10));
		g.fillRect(0, 0, getWidth(), getHeight());
		g.drawImage(sinkImage, 0, 0, getWidth(), getHeight(), this);
	}

	private void drawDoorway(Graphics g) {
		g.setColor(new Color(30, 20, 10));
		g.fillRect(0, 0, getWidth(), getHeight());
		g.drawImage(doorImage, 0, 0, getWidth(), getHeight(), this);

		g.setColor(leftDoorClosed ? Color.RED : Color.GREEN);
		g.setFont(new Font("Monospaced", Font.BOLD, (int) (getHeight() * 0.04)));
		String status = leftDoorClosed ? "DOOR CLOSED" : "DOOR OPEN";
		int sw = g.getFontMetrics().stringWidth(status);
		g.drawString(status, (getWidth() - sw) / 2, (int) (getHeight() * 0.1));
	}

	private void drawCameras(Graphics g) {
		g.setColor(new Color(10, 30, 10));
		g.fillRect(0, 0, getWidth(), getHeight());

		Room room = rooms[selectedCamera];
		g.drawImage(room.getImage(), 0, 0, getWidth(), getHeight(), this);

		g.setColor(Color.GREEN);
		g.setFont(new Font("Monospaced", Font.BOLD, (int) (getHeight() * 0.025)));
		g.drawString(room.getName(), (int) (getWidth() * 0.02), (int) (getHeight() * 0.05));

		drawEnemies(g);
	}

	private void drawStatusBar(Graphics g) {
		int barX = (int) (getWidth() * 0.75);
		int barY = (int) (getHeight() * 0.05);
		int barW = (int) (getWidth() * 0.2);
		int barH = (int) (getHeight() * 0.03);
		int pad = (int) (getWidth() * 0.01);
		int fontSize = (int) (getHeight() * 0.018);

		g.setColor(new Color(0, 0, 0, 180));
		g.fillRoundRect(barX - pad, barY - fontSize - pad, barW + pad * 2, barH + fontSize + pad * 3, 10, 10);

		g.setColor(Color.WHITE);
		g.setFont(new Font("Monospaced", Font.BOLD, fontSize));
		g.drawString("SINK", barX, barY - (int) (pad * 0.5));


		float pct = Math.min(1.0f, Math.max(0.0f, sink.getLevel()));
		int red = (int) (255 * pct);
		int green = (int) (255 * (1 - pct));
		g.setColor(new Color(red, green, 0));
		g.fillRect(barX, barY, (int) (barW * pct), barH);

		g.setColor(Color.WHITE);
		g.drawRect(barX, barY, barW, barH);

		if (sink.getStatus()) {
			if ((System.currentTimeMillis() / 500) % 2 == 0) {
				g.setColor(Color.RED);
				g.drawString("CLEAN SINK!", barX, barY + barH + fontSize + pad);
			}
		}
	}

	private void drawHUD(Graphics g) {
		int hudH = (int) (getHeight() * 0.05);
		int fontSize = (int) (getHeight() * 0.018);
		int textY = getHeight() - (int) ((hudH - fontSize) * 0.5);

		g.setColor(new Color(0, 0, 0, 160));
		g.fillRect(0, getHeight() - hudH, getWidth(), hudH);

		g.setColor(Color.WHITE);
		g.setFont(new Font("Monospaced", Font.PLAIN, fontSize));

		switch (currentScreen) {
			case TABLE:
				g.drawString("[C] Cameras   [D] Doorway   [S] Sink", (int) (getWidth() * 0.01), textY);
				break;
			case SINK:
				g.drawString("[F] Clean   [T] Back to Table", (int) (getWidth() * 0.01), textY);
				break;
			case DOOR:
				g.drawString("[Q] Toggle Door   [T] Back to Table", (int) (getWidth() * 0.01), textY);
				break;
			case CAMERAS:
				g.drawString("[C] Put Away Camera   [LEFT] [RIGHT] Switch Room", (int) (getWidth() * 0.01), textY);
				break;
		}
	}

	private void drawGameOver(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());

		g.setColor(Color.RED);
		g.setFont(new Font("Monospaced", Font.BOLD, (int) (getHeight() * 0.08)));
		String title = "GAME OVER";
		int tw = g.getFontMetrics().stringWidth(title);
		g.drawString(title, (getWidth() - tw) / 2, (int) (getHeight() * 0.45));

		g.setColor(Color.WHITE);
		g.setFont(new Font("Monospaced", Font.PLAIN, (int) (getHeight() * 0.03)));
		String sub = "Got you: " + lastKiller;
		int sw = g.getFontMetrics().stringWidth(sub);
		g.drawString(sub, (getWidth() - sw) / 2, (int) (getHeight() * 0.55));
	}

	private void drawEnemies(Graphics g) {
		for (Enemy enemy : enemys) {
			if (rooms[selectedCamera] == enemy.getRoom()) {
				enemy.drawSprite(g);
			}
		}
	}

	private void handleKeyPress(int key) {
		leftDoorClosed = false;
		switch (key) {
			case KeyEvent.VK_C:
				if (currentScreen == Screen.TABLE)
					currentScreen = Screen.CAMERAS;
				else if (currentScreen == Screen.CAMERAS)
					currentScreen = Screen.TABLE;
				break;
			case KeyEvent.VK_Q:
				if (currentScreen == Screen.DOOR)
					leftDoorClosed = !leftDoorClosed;
				break;
			case KeyEvent.VK_S:
				if (currentScreen == Screen.TABLE)
					currentScreen = Screen.SINK;
				break;
			case KeyEvent.VK_F:
				if (currentScreen == Screen.SINK)
					sink.clean();
				break;
			case KeyEvent.VK_T:
				currentScreen = Screen.TABLE;
				break;
			case KeyEvent.VK_D:
				if (currentScreen == Screen.TABLE)
					currentScreen = Screen.DOOR;
				break;
			case KeyEvent.VK_LEFT:
				if (currentScreen == Screen.CAMERAS)
					selectedCamera = (selectedCamera - 1 + rooms.length) % rooms.length;
				break;
			case KeyEvent.VK_RIGHT:
				if (currentScreen == Screen.CAMERAS)
					selectedCamera = (selectedCamera + 1) % rooms.length;
				break;
		}
		repaint();
	}

	public void startGame() {
		requestFocusInWindow();
	}

	private String lastKiller = "";

	private void triggerGameOver(String killerName) {
		gameOver = true;
		lastKiller = killerName;
		gameLoop.stop();
		repaint();
	}

	public boolean isTaskComplete() {
		return taskComplete;
	}
}
