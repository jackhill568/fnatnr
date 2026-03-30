package fnatnr.Enemys;

import fnatnr.GamePanel;
import fnatnr.Room;

import java.awt.Graphics;

import javax.swing.ImageIcon;

public class ClassicEnemy extends Enemy {

	public ClassicEnemy(String name, int agression, Room currentRoom, Room[] rooms, GamePanel gamePanel) {
	this.name = name;
	this.agression = agression;
	this.currentRoom = currentRoom;
	this.homeRoom = currentRoom;
	this.gamePanel = gamePanel;

	for (Room room : rooms)  {
		this.spriteData.put(room, new EnemyRoomData(50, 50, 100, 100, new ImageIcon("assets/cheese.png").getImage()));
	}

 }

}
