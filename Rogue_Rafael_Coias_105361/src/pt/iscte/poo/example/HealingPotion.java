package pt.iscte.poo.example;

import pt.iscte.poo.utils.Point2D;

public class HealingPotion extends GameElement implements Item {
	
	private final int HEAL = 5;
	
	public HealingPotion(Point2D position) {
		super(position, LAYER, "HealingPotion");
	}
	
	public int getHeal() {
		return HEAL;
	}
}
