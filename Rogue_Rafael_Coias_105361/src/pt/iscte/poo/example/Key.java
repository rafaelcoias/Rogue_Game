package pt.iscte.poo.example;

import pt.iscte.poo.utils.Point2D;

public class Key extends GameElement implements Item {
	private Point2D position;
	private final int ID;
	private final static int LAYER = 1;
	
	public Key(Point2D position, int ID) {
		super(position, LAYER);
		this.position = position;
		this.ID = ID;
	}
	
	@Override
	public String getName() {
		return "Key";
	}

	@Override
	public Point2D getPosition() {
		return position;
	}

	@Override
	public int getLayer() {
		return LAYER;
	}
	
	@Override
	public void setPosition(Point2D p) {
		position = p;
	}
	
// Only Key
	
	public int getID() {
		return ID;
	}
}
