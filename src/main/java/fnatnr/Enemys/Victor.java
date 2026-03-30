package fnatnr.Enemys;


import java.awt.Panel;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import fnatnr.GamePanel;
import fnatnr.Room;
import fnatnr.Sink;


public class Victor extends Enemy {

	private Sink sink;
	private int time = 0;
	private GamePanel gamePanel;
		
	public Victor(String name, Sink sink, Room room, GamePanel gamePanel) {
		this.name = name;
		this.sink = sink;
		this.currentRoom = room;
		this.gamePanel = gamePanel;

		this.spriteData.put(room, new EnemyRoomData(50, 50, 100, 100, new ImageIcon("assets/cheese.png").getImage()));
	}
	

	@Override
	public void chanceMove() {
		sink.getLevel();

 	if (sink.getStatus()) {
			time++;
			if (time > 4) {
				gamePanel.triggerGameOver(this.name);
				time = 0;
			}

		}

	}
}


