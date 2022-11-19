package pt.iscte.poo.example;

import pt.iscte.poo.utils.Point2D;

public class Key extends GameElement implements Item {

	private final int ID;
	private final static int LAYER = 1;
	
	public Key(Point2D position, int ID) {
		super(position, LAYER);
		this.ID = ID;
	}
	
	@Override
	public String getName() {
		return "Key";
	}

	@Override
	public Point2D getPosition() {
		return super.getPosition();
	}

	@Override
	public int getLayer() {
		return super.getLayer();
	}
	
// Only Key
	
	public int getID() {
		return ID;
	}
}
