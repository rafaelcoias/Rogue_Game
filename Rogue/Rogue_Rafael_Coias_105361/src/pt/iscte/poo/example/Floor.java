package pt.iscte.poo.example;

import pt.iscte.poo.utils.Point2D;

public class Floor extends GameElement {

	private Point2D position;
	private final static int LAYER = 0;

	public Floor(Point2D position) {
		super(position, LAYER);
		this.position = position;
	}

	@Override
	public String getName() {
		return "Floor";
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