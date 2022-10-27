package pt.iscte.poo.example;

import pt.iscte.poo.utils.Point2D;

public class Wall  extends GameElement {

	private Point2D position;
	private final static int LAYER = 3;

	public Wall(Point2D position) {
		super(position, LAYER);
		this.position = position;
	}
	
	@Override
	public String getName() {
		return "Wall";
	}

	@Override
	public Point2D getPosition() {
		return position;
	}

	@Override
	public int getLayer() {
		return LAYER;
	}
}
