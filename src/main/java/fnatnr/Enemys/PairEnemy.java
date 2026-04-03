package fnatnr.Enemys;

import fnatnr.Room;
import fnatnr.GamePanel;
import fnatnr.GameRandom;

import java.awt.Image;

import javax.swing.ImageIcon;

public class PairEnemy extends Enemy {

	private boolean paried = false;

	public PairEnemy(String name, int agression, Room currentRoom, Room[] rooms, GamePanel gamePanel, String killFrame) {
		this.name = name;
		this.aggression = agression;
		this.currentRoom = currentRoom;
		this.homeRoom = currentRoom;
		this.gamePanel = gamePanel;

		this.killFrame = new ImageIcon(killFrame).getImage();

		for (Room room : rooms) {
			this.spriteData.put(room, new EnemyRoomData(50, 50, 100, 100, new ImageIcon("assets/cheese.png").getImage()));
		}

	}

	@Override
	public void nextRoom() {
		Room[] neighbours = currentRoom.getNeighbours();

		if (neighbours.length == 0) {
			System.out.println("TRIED TO MOVE IN A ROOM WITH NO NEIGHBOURS");
			return;
		}
		if (paried) {
			currentRoom = neighbours[0];
		} else {
			currentRoom = neighbours[GameRandom.getInstance().nextInt(neighbours.length)];
		}
	}

}
