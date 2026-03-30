package fnatnr.Enemys;

import fnatnr.Room;
import fnatnr.GamePanel;
import fnatnr.GameRandom;

public class PairEnemy extends Enemy {

	private boolean paried = false;

	public PairEnemy(String name, int agression, Room currentRoom, GamePanel gamePanel) {
		this.name = name;
		this.agression = agression;
		this.currentRoom = currentRoom;
		this.homeRoom = currentRoom;
		this.gamePanel = gamePanel; 
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
