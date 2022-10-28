package pt.iscte.poo.example;

import pt.iscte.poo.utils.Point2D;

public class Square extends GameElement {

	private Point2D position;
	private final String NAME;
	private final static int LAYER = 0;
	
	public Square(Point2D position, String name) {
		super(position, 0);
		this.position = position;
		this.NAME = name;
	}
	
	@Override
	public String getName() {
		return NAME;
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
