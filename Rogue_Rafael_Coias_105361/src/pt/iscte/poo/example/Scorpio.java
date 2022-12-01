package pt.iscte.poo.example;

import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Scorpio extends GameElement implements Mob {

	private Room room;
	private int life = 2;
	
	private final static int KILLVALUE = 15;
	
	public Scorpio(Point2D position, Room room) {
		super(position, LAYER, "Scorpio");
		this.room = room;
	}

	@Override
	public void move(Vector2D v) {
		setPosition(getPosition().plus(v));
	}

	@Override
	public boolean canMove(GameElement e) {
		return e instanceof Item || e instanceof Floor;
	}

	@Override
	public void attack(Vector2D moveVector) {
		((Hero)room.getObject(getPosition().plus(moveVector))).poisened();
	}

	@Override
	public void setLife(int damage) {
		life = life + damage;
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
