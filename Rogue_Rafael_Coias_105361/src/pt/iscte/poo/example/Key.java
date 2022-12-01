package pt.iscte.poo.example;

import pt.iscte.poo.utils.Point2D;

public class Key extends GameElement implements Item {

	private final int ID;
	
	public Key(Point2D position, int ID) {
		super(position, LAYER, "Key");
		this.ID = ID;
	}
	
	public int getID() {
		return ID;
	}
}
