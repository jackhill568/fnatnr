package fnatnr;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.ImageIcon;

import fnatnr.Enemys.*;
import fnatnr.NightTools.NightTimer;
import fnatnr.NightTools.NightData;
import fnatnr.NightTools.NightRegister;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class GamePanel extends JPanel implements ActionListener {

	private static final int TICK_RATE_MS = 1000;
	private javax.swing.Timer gameLoop = new Timer(500, this);
	private javax.swing.Timer renderLoop = new Timer(32, e -> repaint());

	private Enemy lastKiller;

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
	private Room[] cameraRooms;
	private int selectedCamera = 0;
	private Renderer renderer;

	public int night;
	public Runnable onNightComplete;
	public Runnable onGameOver;

	private NightTimer nightTimer = NightTimer.getInstance();
	private boolean nightComplete = false;

	public GamePanel(int night, Runnable onComplete, Runnable onGameOver) {
		this.night = night;
		this.onNightComplete = onComplete;
		this.onGameOver = onGameOver;
		
		this.renderer = new Renderer(this);

		setPreferredSize(new Dimension(1024, 768));
		setBackground(Color.BLACK);
		setFocusable(true);

		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				handleKeyPress(e.getKeyCode());
			}
		});

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				handleMouseClick(e.getX(), e.getY());
			}
		});

		NightData data = NightRegister.get(night);

		this.sink = new Sink(data.sink[0], data.sink[1], data.sink[2]);

		createRooms();
		createEnemys(data, sink);
		gameLoop.start();
		renderLoop.start();
	}

	public boolean getDoorClosed() {
		return leftDoorClosed;
	}

	public List<Enemy> getEnemies() {
		return enemys;
	}

	public Room getSelectedCamera() {
		return cameraRooms[selectedCamera];
	}

	public Sink getSink() {
		return sink;
	}

	public Screen getCurrentScreen() {
		return currentScreen;
	}

	public int getNight() {
		return night;
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
		Room doorway = new Room("Doorway", "assets/rooms/doorwayOpen.png");
		Room kitchen = new Room("Kitchen", "assets/rooms/table.png");

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
		cameraRooms = new Room[] { victor, cayden, josh, nathan, jack, kazuma, hall, stairs };
	}

	private void createEnemys(NightData data, Sink sink) {

		enemys = new ArrayList<>();

		Enemy jack = new ClassicEnemy("Jack", data.enemyAgressions[0], rooms[4], rooms, this, "assets/Jack.png");
		Enemy nathan = new ClassicEnemy("Nathan", data.enemyAgressions[1], rooms[3], rooms, this, "assets/Nathan.png");

		Enemy cayden = new PairEnemy("Cayden", data.enemyAgressions[2], rooms[1], rooms, this, "assets/Cayden.png");
		Enemy josh = new PairEnemy("Josh", data.enemyAgressions[3], rooms[2], rooms, this, "assets/Josh.png");

		Enemy kazuma = new Kazuma("Kazuma", data.enemyAgressions[4], rooms[5], this, "assets/Kazuma.png");

		Enemy victor = new Victor("Victor", sink, rooms[9], this, "assets/Victor.png");

		enemys.add(jack);
		enemys.add(nathan);
		enemys.add(cayden);
		enemys.add(josh);
		enemys.add(kazuma);
		enemys.add(victor);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (gameOver)
			return;

		sink.update();
		for (Enemy enemy : enemys) {
			enemy.chanceMove();
		}

		nightTimer.update();

		if (nightTimer.isComplete()) {
			triggerWin();
			return;
		}

		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (gameOver) {
			renderer.drawGameOver(g, lastKiller);
			return;
		} else if (nightComplete) {
			renderer.drawGameWin(g);
			return;
		}

		switch (currentScreen) {
			case TABLE:
				renderer.drawTable(g);
				renderer.drawStatusBar(g);
				break;
			case SINK:
				renderer.drawSink(g);
				renderer.drawStatusBar(g);
				break;
			case DOOR:
				renderer.drawDoorway(g, leftDoorClosed);
				break;
			case CAMERAS:
				renderer.drawCameras(g);
				break;
		}

		renderer.drawHUD(g);
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
					selectedCamera = (selectedCamera - 1 + cameraRooms.length) % cameraRooms.length;
				break;
			case KeyEvent.VK_RIGHT:
				if (currentScreen == Screen.CAMERAS)
					selectedCamera = (selectedCamera + 1) % cameraRooms.length;
				break;
		}
		repaint();
	}

	private void handleMouseClick(int mx, int my) {
		if (gameOver || nightComplete) {
			if (gameOver)
				SwingUtilities.invokeLater(onGameOver::run);
			else if (nightComplete)
				SwingUtilities.invokeLater(onNightComplete::run);
			return;
		}
	}

	public void startGame() {
		requestFocusInWindow();
	}


	public void triggerGameOver(Enemy killer) {
		gameOver = true;
		lastKiller = killer;
		gameLoop.stop();
		renderLoop.stop();
		repaint();
	}

	private void triggerWin() {
		nightComplete = true;
		gameLoop.stop();
		renderLoop.stop();
		repaint();
	}

}
