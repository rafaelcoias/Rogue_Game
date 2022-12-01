package pt.iscte.poo.example;

import pt.iscte.poo.utils.Point2D;

public class Treasure extends GameElement implements Item {

	public Treasure(Point2D position) {
		super(position, LAYER, "Treasure");
	}
}
