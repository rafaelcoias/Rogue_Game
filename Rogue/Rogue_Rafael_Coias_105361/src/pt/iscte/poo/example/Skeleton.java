package pt.iscte.poo.example;

import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Skeleton extends GameElement implements Mob {
	private Point2D position;
	private Room room;
	private int life = 5;
	
	private boolean move = false;
	
	private final static int DAMAGE = -1;
	private final static int LAYER = 2;
	
	public Skeleton(Point2D position, Room room) {
		super(position, LAYER);
		this.position = position;
		this.room = room;
	}
	
// ImageTile Interface	
	
	@Override
	public String getName() {
		return "Skeleton";
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
		Mob mob = (Mob)room.getObject(position.plus(moveVector));
		mob.setLife(DAMAGE);
		if (mob.getLife() > 0)
			return ;
		Engine.endGame();
	}
	
	@Override
	public void move(Vector2D v) {
		if (!move) {
			move = true;
			return ;
		}
		move = false;
		position = position.plus(v);
	}
	
	@Override
	public boolean canMove(GameElement e) {
		return e.getLayer() < getLayer();
	}
	
	@Override
	public void setLife(int DAMAGE) {
		life = life + DAMAGE;
	}
	
	@Override
	public int getLife() {
		return life;
	}
}
