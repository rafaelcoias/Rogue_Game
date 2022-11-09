package pt.iscte.poo.example;

import pt.iscte.poo.utils.Point2D;

public class Wall  extends GameElement {

	private final static int LAYER = 3;

	public Wall(Point2D position) {
		super(position, LAYER);
	}
	
	@Override
	public String getName() {
		return "Wall";
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
