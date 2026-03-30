package fnatnr.Enemys;

import fnatnr.Room;
import fnatnr.NightTools.NightTimer;

import java.util.HashMap;
import java.util.Map;

import fnatnr.GamePanel;
import fnatnr.GameRandom;

import java.awt.Graphics;

public abstract class Enemy {

	protected Room currentRoom;
	protected int aggression; 
	protected String name;

	protected Room homeRoom;

	protected GamePanel gamePanel;

	protected Map<Room, EnemyRoomData> spriteData = new HashMap<>();

	protected int tickFrame = GameRandom.getInstance().nextInt(20);

public void nextRoom() {
    Room[] neighbours = currentRoom.getNeighbours();
    if (neighbours.length == 0) return;

    if (GameRandom.getInstance().nextInt(20) < aggression - 5) {
        currentRoom = neighbours[0];
    } else {
        currentRoom = neighbours[GameRandom.getInstance().nextInt(neighbours.length)]; 
    }
}

public void chanceMove() {
    if (aggression == 0) return;

    tickFrame++;

    float nightProgress = NightTimer.getInstance().getProgress(); 
    int effectiveAggression = (int)(aggression + (nightProgress * 8));
    effectiveAggression = Math.min(20, effectiveAggression); 

    if (currentRoom.getName().equals("Doorway") && gamePanel.getDoorClosed()) {
        if (tickFrame > 10) {
            tickFrame = 0;
            currentRoom = homeRoom;
        }
        return;
    }

    int moveThreshold = Math.max(1, 40 - (effectiveAggression * 2));
    if (tickFrame >= moveThreshold) {
        tickFrame = 0;
        nextRoom();
        checkAttack();
    }
}

protected void checkAttack() {
    if (currentRoom.getName().equals("Kitchen")) {
        attack();
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
