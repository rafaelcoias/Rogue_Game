package pt.iscte.poo.example;

import pt.iscte.poo.utils.Point2D;

public class HealingPotion extends GameElement implements Item {
	
	private Point2D position;
	private final static int LAYER = 1;
	private final int HEAL = 5;
	
	public HealingPotion(Point2D position) {
		super(position, LAYER);
		this.position = position;
	}
	
	@Override
	public String getName() {
		return "HealingPotion";
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
	
	public int getHeal() {
		return HEAL;
	}
}
