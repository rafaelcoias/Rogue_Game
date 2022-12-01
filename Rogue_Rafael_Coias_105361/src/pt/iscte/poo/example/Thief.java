package pt.iscte.poo.example;

import java.util.ArrayList;

import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Thief extends GameElement implements Mob {

	private Room room;
	private int life = 5;
	private boolean stole = false;
	
	private final static int KILLVALUE = 10;
	private final static int CAPACITY = 5;
	
	private ArrayList<Point2D> scapePoints = new ArrayList<>();
	private ArrayList<GameElement> itemsBar = new ArrayList<>();
	private int items = 0;
	
	public Thief(Point2D position, Room room) {
		super(position, LAYER, "Thief");
		this.room = room;
		scapePoints.add(new Point2D(0,0));
		scapePoints.add(new Point2D(9,0));
		scapePoints.add(new Point2D(0,9));
		scapePoints.add(new Point2D(9,9));
	}

	@Override
	public void move(Vector2D v) {
		Vector2D stoleVector = new Vector2D(v.getX() * -1, v.getY() * -1);
		GameElement objInFront = room.getObject(getPosition().plus(stoleVector));
		if (stole && canMove(objInFront))
			setPosition(getPosition().plus(stoleVector));
		else
			setPosition(getPosition().plus(v));
	}

	@Override
	public boolean canMove(GameElement e) {
		return e instanceof Item || e instanceof Floor || e == null;
	}

	@Override
	public void attack(Vector2D moveVector) {
		GameElement item = ((Hero)room.getObject(getPosition().plus(moveVector))).stealItem();
		if (item == null || items == CAPACITY)
			return ;
		stole = true;
		itemsBar.add(item);
		items++;
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
	
	public void dropItems() {
		for (GameElement item : itemsBar) {
			item.setPosition(getPosition());
			Engine.addObject(item);
		}
	}

}
