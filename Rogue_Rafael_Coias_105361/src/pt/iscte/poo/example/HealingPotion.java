package pt.iscte.poo.example;

import pt.iscte.poo.utils.Point2D;

public class HealingPotion extends GameElement {
	
	private final static int LAYER = 1;
	private final int HEAL = 5;
	
	public HealingPotion(Point2D position) {
		super(position, LAYER);
	}
	
	@Override
	public String getName() {
		return "HealingPotion";
	}

	@Override
	public Point2D getPosition() {
		return super.getPosition();
	}

	@Override
	public int getLayer() {
		return super.getLayer();
	}
	
	public int getHeal() {
		return HEAL;
	}
}
