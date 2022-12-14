package pt.iscte.poo.example;

import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Bat extends GameElement implements Mob {
	
	private Room room;
	private int life = 3;
	
	private final static int KILLVALUE = 5;
	private final static int DAMAGE = -1;
	
	public Bat(Point2D position, Room room) {
		super(position, LAYER, "Bat");
		this.room = room;
	}
	
// Mob Interface	
		
	@Override
	public void attack(Vector2D moveVector) {
		if (random(50))
			return ;
		((Mob)room.getObject(getPosition().plus(moveVector))).setLife(DAMAGE);
		setLife(1);
	}
	
	@Override
	public void move(Vector2D v) {
		GameElement e = room.getObject(getPosition().plus(v));
		if (random(50) && (e == null || (canMove(e) && !(e instanceof Door)))) {
			setPosition(getPosition().plus(v));
			return ;
		}
		Vector2D randomVector = randomVector();
		e = room.getObject(getPosition().plus(randomVector));
		if (e == null || (canMove(e) && !(e instanceof Door)))
			setPosition(getPosition().plus(randomVector));
	}
	
	@Override
	public boolean canMove(GameElement e) {
		return e.getLayer() < getLayer();
	}
	
	@Override
	public void setLife(int DAMAGE) {
		if (DAMAGE > 0 && getLife() >= 3)
			return ;
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
