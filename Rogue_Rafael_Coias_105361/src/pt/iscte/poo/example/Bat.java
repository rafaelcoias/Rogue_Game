package pt.iscte.poo.example;

import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Bat extends GameElement implements Mob {
	
	private Point2D position;
	private Room room;
	private int life = 3;
	
	private final static int KILLVALUE = 5;
	private final static int DAMAGE = -1;
	private final static int LAYER = 2;
	
	public Bat(Point2D position, Room room) {
		super(position, LAYER);
		this.position = position;
		this.room = room;
	}
	
// ImageTile Interface	
	
	@Override
	public String getName() {
		return "Bat";
	}

	@Override
	public Point2D getPosition() {
		return position;
	}

	@Override
	public int getLayer() {
		return LAYER;
	}
	
// Mob Interface	
		
	@Override
	public void attack(Vector2D moveVector) {
		if (random(50))
			return ;
		Mob mob = (Mob)room.getObject(position.plus(moveVector));
		mob.setLife(DAMAGE);
		setLife(1);
		if (mob.getLife() > 0)
			return ;
		Engine.endGame(false);
	}
	
	@Override
	public void move(Vector2D v) {
		GameElement e = room.getObject(getPosition().plus(v));
		if (random(50) && (e == null || (canMove(e) && !e.getName().equals("DoorClosed") && !e.getName().equals("DoorOpen")))) {
			position = position.plus(v);
			return ;
		}
		Vector2D randomVector = randomVector();
		e = room.getObject(getPosition().plus(randomVector));
		if (e == null || (canMove(e) && !e.getName().equals("DoorClosed") && !e.getName().equals("DoorOpen")))
			position = position.plus(randomVector);
	}
	
	@Override
	public boolean canMove(GameElement e) {
		return e.getLayer() < getLayer();
	}
	
	@Override
	public void setLife(int DAMAGE) {
		if (DAMAGE > 0 && getLife() == 3)
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
