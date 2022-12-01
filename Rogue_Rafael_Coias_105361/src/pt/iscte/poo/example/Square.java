package pt.iscte.poo.example;

import pt.iscte.poo.utils.Point2D;

public class Square extends GameElement {

	private final static int LAYER = 0;
	
	public Square(Point2D position, String name) {
		super(position, LAYER, name);
	}
	
}
