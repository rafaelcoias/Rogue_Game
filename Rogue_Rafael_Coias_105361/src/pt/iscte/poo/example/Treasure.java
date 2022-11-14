package pt.iscte.poo.example;

import pt.iscte.poo.utils.Point2D;

public class Treasure extends GameElement {

	private final static int LAYER = 1;

	public Treasure(Point2D position) {
		super(position, LAYER);
	}
	
	@Override
	public String getName() {
		return "Treasure";
	}

	@Override
	public Point2D getPosition() {
		return super.getPosition();
	}

	@Override
	public int getLayer() {
		return super.getLayer();
	}
}
