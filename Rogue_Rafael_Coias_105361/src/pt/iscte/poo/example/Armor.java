package pt.iscte.poo.example;

import pt.iscte.poo.utils.Point2D;

public class Armor extends GameElement implements Item {
	
	public Armor(Point2D position) {
		super(position, LAYER, "Armor");
	}
}
