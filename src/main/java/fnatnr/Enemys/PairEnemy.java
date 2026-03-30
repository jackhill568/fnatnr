package fnatnr.Enemys;

import fnatnr.Room;
import fnatnr.GamePanel;
import fnatnr.GameRandom;

import javax.swing.ImageIcon;
public class PairEnemy extends Enemy {

	private boolean paried = false;

	public PairEnemy(String name, int agression, Room currentRoom, Room[] rooms, GamePanel gamePanel) {
		this.name = name;
		this.aggression = agression;
		this.currentRoom = currentRoom;
		this.homeRoom = currentRoom;
		this.gamePanel = gamePanel;

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
