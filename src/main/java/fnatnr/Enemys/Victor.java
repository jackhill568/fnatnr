package fnatnr.Enemys;


import javax.swing.ImageIcon;
import fnatnr.Room;
import fnatnr.Sink;


public class Victor extends Enemy {

	private Sink sink;
	private int time = 0;
		
	public Victor(String name, Sink sink, Room room) {
		this.name = name;
		this.sink = sink;
		this.currentRoom = room;
	}
	

	@Override
	public void chanceMove() {
		sink.getLevel();

		if (sink.getStatus()) {
			time++;
			if (time > 4) {
				time = 0;
			}

		}

	}
}


