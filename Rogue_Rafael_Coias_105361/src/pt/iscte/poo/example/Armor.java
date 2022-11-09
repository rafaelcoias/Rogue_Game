package pt.iscte.poo.example;

import pt.iscte.poo.utils.Point2D;

public class Armor extends GameElement implements Item {

	private final static int LAYER = 1;
	
	public Armor(Point2D position) {
		super(position, LAYER);
	}

	@Override
	public String getName() {
		return "Armor";
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
