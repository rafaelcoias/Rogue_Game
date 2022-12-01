package pt.iscte.poo.example;

import pt.iscte.poo.utils.Point2D;

public class Floor extends GameElement {

	private final static int LAYER = 0;

	public Floor(Point2D position) {
		super(position, LAYER, "Floor");
	}
}