package pt.iscte.poo.example;

import pt.iscte.poo.utils.Point2D;

public class Sword extends GameElement{

	private final static int LAYER = 1;
	
	public Sword(Point2D position) {
		super(position, LAYER);
	}
	
	@Override
	public String getName() {
		return "Sword";
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
