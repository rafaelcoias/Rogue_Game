package pt.iscte.poo.example;

import pt.iscte.poo.utils.Point2D;

public class Treasure extends GameElement implements Item {
	private Point2D position;
	private final static int LAYER = 1;

	public Treasure(Point2D position) {
		super(position, LAYER);
		this.position = position;
	}
	
	@Override
	public String getName() {
		return "Treasure";
	}

	@Override
	public Point2D getPosition() {
		return position;
	}
	
	@Override
	public void setPosition(Point2D p) {
		position = p;
	}

	@Override
	public int getLayer() {
		return LAYER;
	}
}
