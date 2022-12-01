package pt.iscte.poo.example;

import pt.iscte.poo.utils.Point2D;

public class Sword extends GameElement implements Item {
	
	public Sword(Point2D position) {
		super(position, LAYER, "Sword");
	}
}
