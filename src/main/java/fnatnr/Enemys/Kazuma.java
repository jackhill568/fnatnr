package fnatnr.Enemys;

import fnatnr.Room;
import fnatnr.GamePanel;
import fnatnr.GameRandom;

import java.awt.Image;
import javax.swing.ImageIcon;

public class Kazuma extends Enemy {

	private int state = 0; // 5 states

	private int tickFrame = 0;
	private int watchFrame = 0;

	private static final int ADVANCE_BLOCK = 10;
	private static final int WATCH_BLOCK = 8;

	private boolean beingWatched = false;

	public Kazuma(String name, int agression, Room currentRoom,  GamePanel gamePanel) {
		this.name = name;
		this.aggression = agression;
		this.currentRoom = currentRoom;
		this.gamePanel = gamePanel;
		
		this.spriteData.put(currentRoom, new EnemyRoomData(50, 50, 100, 100, new ImageIcon("assets/cheese.png").getImage()));
	}

	public int getState() {
		return state;
	}

	@Override
	public void chanceMove() {

		if (beingWatched) {
			watchFrame++;
			if (watchFrame >= WATCH_BLOCK) {
				watchFrame = 0;
				tickFrame = 0; 
				state = Math.max(0, state - 1);
			}
			beingWatched = false;
			return; 
		}

		watchFrame = 0; 
		tickFrame++;

		if (tickFrame >= ADVANCE_BLOCK) {
			tickFrame = 0;
			int rand = GameRandom.getInstance().nextInt(20);
			if (rand < aggression) {
				state = Math.min(4, state + 1);
			}
		}
	}

}
