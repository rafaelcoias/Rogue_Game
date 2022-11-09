package pt.iscte.poo.example;

import pt.iscte.poo.utils.Point2D;

public class Square extends GameElement {

	private String name;
	private final static int LAYER = 0;
	
	public Square(Point2D position, String name) {
		super(position, LAYER);
		this.name = name;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public Point2D getPosition() {
		return super.getPosition();
	}

	@Override
	public int getLayer() {
		return super.getLayer();
	}
	
	public void setName(String color) {
		name = color;
	}
	
}
