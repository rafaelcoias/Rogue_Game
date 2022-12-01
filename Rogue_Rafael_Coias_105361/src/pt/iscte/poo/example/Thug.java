package pt.iscte.poo.example;

import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Thug extends GameElement implements Mob {

	private Room room;
	private int life = 10;
	
	private final static int KILLVALUE = 30;
	private final static int DAMAGE = -1;
	
	
	public Thug(Point2D position, Room room) {
		super(position, LAYER, "Thug");
		this.room = room;
	}
	
// Mob Interface	
	
	@Override
	public void attack(Vector2D moveVector) {
		Mob mob = (Mob)room.getObject(getPosition().plus(moveVector));
		if (random(30))
			mob.setLife(DAMAGE * 3);
		else
			mob.setLife(DAMAGE);
	}
	
	@Override
	public boolean canMove(GameElement e) {
		return e instanceof Item || e instanceof Floor;
	}
	
	@Override
	public void move(Vector2D v) {
		setPosition(getPosition().plus(v));
	}
	
	@Override
	public void setLife(int DAMAGE) {
		life = life + DAMAGE;
	}
	
	@Override
	public int getLife() {
		return life;
	}
	
	@Override
	public int getKillValue() {
		return KILLVALUE;
	}
}
