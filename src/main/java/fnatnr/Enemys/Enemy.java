package fnatnr.Enemys;

import fnatnr.Room;

import java.util.HashMap;
import java.util.Map;

import fnatnr.GamePanel;
import fnatnr.GameRandom;

import java.awt.Graphics;

public abstract class Enemy {

	protected Room currentRoom;
	protected int agression; // 0 - 20
	protected String name;

	protected Room homeRoom;

	protected GamePanel gamePanel;

	protected Map<Room, EnemyRoomData> spriteData = new HashMap<>();

	protected int tickFrame = 0;

	public void nextRoom() {
		Room[] neighbours = currentRoom.getNeighbours();

		if (neighbours.length == 0) {
			System.out.println("TRIED TO MOVE IN A ROOM WITH NO NEIGHBOURS");
			return;
		}

		if (GameRandom.getInstance().nextInt(20) < agression - 5) {
			currentRoom = neighbours[0];
		} else {
			currentRoom = neighbours[GameRandom.getInstance().nextInt(neighbours.length)];
		}

		if (currentRoom.getName().equals("Kitchen")) {
			attack();
		}
	}

	public void chanceMove() {
		if (agression == 0) {
			return;
		}

		int rand = GameRandom.getInstance().nextInt(20);
		tickFrame++;

		if (currentRoom.getName().equals("Doorway") && gamePanel.getDoorClosed()) {
			if (tickFrame > 10) { // after 10 frames of closing door go home + play aduio que here
				currentRoom = homeRoom;
			}
		} else if (agression > rand && tickFrame > (40 - agression)) {
			tickFrame = 0;
			nextRoom();
		}

	}

	public void attack() {
		this.gamePanel.triggerGameOver(this.name);
	}

	public Room getRoom() {
		return currentRoom;
	}

	public void setRoom(Room room) {
		this.currentRoom = room;
	}

	public String getName() {
		return this.name;
	}

	public void drawSprite(Graphics g) {
		EnemyRoomData data = this.spriteData.get(currentRoom);
		g.drawImage(data.sprite, data.x, data.y, data.width, data.height, null);
	}
}
