package fnatnr;

import java.awt.Image;

import javax.swing.ImageIcon;

public class Room {

	private String name;
	private Room[] neighbours;
	private Image image;

	public Room(String name, String imagePath) {
		this.name = name;
		this.image = new ImageIcon(imagePath).getImage();
	}

	public void setNeighbours(Room[] neigbours) {
		this.neighbours = neigbours;
	}

	public Room[] getNeighbours() {
		return neighbours;
	}

	public Image getImage() {
		return this.image;
	}

	public String getName() {
		return this.name;
	}


	public void drawSprite() {

	}

}
