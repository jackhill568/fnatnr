package fnatnr;

import javax.swing.*;
import javax.swing.Timer;

import fnatnr.Enemys.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class GamePanel extends JPanel implements ActionListener {

	public static final int WIDTH = 1024;
	public static final int HEIGHT = 768;

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
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setBackground(Color.BLACK);
		setFocusable(true);

		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				handleKeyPress(e.getKeyCode());
			}
		});

		this.sink = new Sink(50, 1, 7);

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

	private void updateEnemies() {

		for (Enemy enemy : enemys) {
			enemy.chanceMove();
		}

	}

	private void drawEnemies(Graphics g) {
		for (Enemy enemy : enemys) {
			if (rooms[selectedCamera] == enemy.getRoom()) {
				enemy.drawSprite(g);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (gameOver)
			return;

		updateEnemies();
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
				sink.clean();
				drawStatusBar(g);
				break;
			case DOOR:
				drawDoorway(g);
				break;
			case CAMERAS:
				drawCameras(g);
				break;
		}

		drawHUD(g);
	}

	private void drawTable(Graphics g) {
		g.setColor(new Color(30, 20, 10));
		g.fillRect(0, 0, WIDTH, HEIGHT);

		g.setColor(Color.WHITE);
		g.setFont(new Font("Monospaced", Font.PLAIN, 16));
		g.drawString("TABLE VIEW", 20, 30);

		g.drawImage(tableImage, 0, 0, WIDTH, HEIGHT, this);
	}

	private void drawSink(Graphics g) {
		g.setColor(new Color(30, 20, 10));
		g.fillRect(0, 0, WIDTH, HEIGHT);

		g.setColor(Color.WHITE);
		g.setFont(new Font("Monospaced", Font.PLAIN, 16));
		g.drawString("SINK VIEW", 20, 30);

		g.drawImage(sinkImage, 0, 0, WIDTH, HEIGHT, this);
	}

	private void drawDoorway(Graphics g) {
		g.setColor(new Color(30, 20, 10));
		g.fillRect(0, 0, WIDTH, HEIGHT);

		g.setColor(Color.WHITE);
		g.setFont(new Font("Monospaced", Font.PLAIN, 16));
		g.drawString("DOOR VIEW", 20, 30);

		g.drawImage(doorImage, 0, 0, WIDTH, HEIGHT, this);
	}

	private void drawCameras(Graphics g) {
		g.setColor(new Color(10, 30, 10));
		g.fillRect(0, 0, WIDTH, HEIGHT);

		Room room = rooms[selectedCamera];

		g.drawImage(room.getImage(), 0, 0, WIDTH, HEIGHT, this);

		g.setColor(Color.GREEN);
		g.setFont(new Font("Monospaced", Font.BOLD, 18));
		g.drawString(room.getName(), 20, 30);

		g.setColor(new Color(0, 0, 0, 180));
		g.fillRect(0, HEIGHT - 50, WIDTH, 50);
		g.setColor(Color.WHITE);
		g.setFont(new Font("Monospaced", Font.PLAIN, 13));

		for (int i = 0; i < rooms.length; i++) {
			int x = 10 + i * 100;
			g.setColor(i == selectedCamera ? Color.GREEN : Color.GRAY);
			g.drawString("[" + (i + 1) + "] " + rooms[i].getName().substring(0, Math.min(7, rooms[i].getName().length())), x,
					HEIGHT - 40);
		}

		drawEnemies(g);
	}

	private void drawStatusBar(Graphics g) {
		int barY = (int) (getHeight() * 0.05);
		int barX = (int) (getWidth() * 0.75);
		int barW = (int) (getWidth() * 0.2);
		int barH = (int) (getHeight() * 0.03);

		g.setColor(new Color(0, 0, 0, 180));
		g.fillRoundRect(barX - 10, barY - 20, barW + 20, barH + 30, 10, 10);

		g.setColor(Color.WHITE);
		g.setFont(new Font("Monospaced", Font.BOLD, (int) (getHeight() * 0.018)));
		g.drawString("SINK", barX, barY - 4);

		g.setColor(Color.DARK_GRAY);
		g.fillRect(barX, barY, barW, barH);

		float pct = Math.min(1.0f, Math.max(0.0f, sink.getLevel()));
		g.setColor(new Color(pct, 1 - pct, 0f));
		g.fillRect(barX, barY, (int) (barW * pct), barH);

		g.setColor(Color.WHITE);
		g.drawRect(barX, barY, barW, barH);

		if (sink.getStatus()) {
			if ((System.currentTimeMillis() / 500) % 2 == 0) {
				g.setColor(Color.RED);
				g.drawString("CLEAN SINK!", barX, barY + barH + 16);
			}
		}
	}

	private void drawHUD(Graphics g) {
		g.setColor(new Color(0, 0, 0, 160));
		g.fillRect(0, HEIGHT - 40, WIDTH, 40);

		g.setColor(Color.WHITE);
		g.setFont(new Font("Monospaced", Font.PLAIN, 13));

		switch (currentScreen) {
			case TABLE:
				g.drawString("[C] Cameras [D] Doorway [S] Clean Sink [T] Table", 10,
						HEIGHT - 14);
				break;
			case SINK:
				g.drawString("[T] Table", 10,
						HEIGHT - 14);
				break;
			case DOOR:
				g.drawString("[Q] Close Door [T] Table", 10,
						HEIGHT - 14);
				break;
			case CAMERAS:
				g.drawString("[C] Put away camera", 10,
						HEIGHT - 14);
				break;
		}
	}

	private void drawGameOver(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		g.setColor(Color.RED);
		g.setFont(new Font("Monospaced", Font.BOLD, 60));
		g.drawString("GAME OVER", WIDTH / 2 - 200, HEIGHT / 2);
		g.setFont(new Font("Monospaced", Font.PLAIN, 24));
		g.drawString("Got you: " + lastKiller, WIDTH / 2 - 120, HEIGHT / 2 + 50);
	}

	private void handleKeyPress(int key) {
		switch (key) {
			case KeyEvent.VK_C:
				if (currentScreen == Screen.TABLE)
					currentScreen = Screen.CAMERAS;
				else if (currentScreen == Screen.CAMERAS) {
					currentScreen = Screen.TABLE;
				}
				break;
			case KeyEvent.VK_Q:
				if (currentScreen == Screen.DOOR)
					leftDoorClosed = !leftDoorClosed;
				break;
			case KeyEvent.VK_S:
				if (currentScreen == Screen.TABLE) {
					currentScreen = Screen.SINK;
				}
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
		repaint();
	}

	public boolean isTaskComplete() {
		return taskComplete;
	}
}
