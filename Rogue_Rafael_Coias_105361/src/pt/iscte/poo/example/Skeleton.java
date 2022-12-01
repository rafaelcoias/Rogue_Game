package pt.iscte.poo.example;

import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Skeleton extends GameElement implements Mob {
	
	private Room room;
	private int life = 5;
	
	private boolean move = false;
	
	private final static int KILLVALUE = 15;
	private final static int DAMAGE = -1;
	private final static int LAYER = 2;
	
	public Skeleton(Point2D position, Room room) {
		super(position, LAYER, "Skeleton");
		this.room = room;
	}
	
// Mob Interface
	
	@Override
	public void attack(Vector2D moveVector) {
		((Mob)room.getObject(getPosition().plus(moveVector))).setLife(DAMAGE);
	}
	
	@Override
	public void move(Vector2D v) {
		if (!move) {
			move = true;
			return ;
		}
		move = false;
		setPosition(getPosition().plus(v));
	}
	
	@Override
	public boolean canMove(GameElement e) {
		return e instanceof Item || e instanceof Floor;
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
