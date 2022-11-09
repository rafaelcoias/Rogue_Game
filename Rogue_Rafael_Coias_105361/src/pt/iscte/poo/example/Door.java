package pt.iscte.poo.example;

import pt.iscte.poo.utils.Point2D;

public class Door extends GameElement {
	
	private Room nextRoom;
	private Point2D nextPosition;
	
	private final int ID;
	private boolean opened;
	private final static int LAYER = 0;
	
	public Door(Point2D position, Room nextRoom, Point2D nextPosition, int ID) {
		super(position, LAYER);
		this.nextRoom = nextRoom;
		this.nextPosition = nextPosition;
		this.ID = ID;
		opened = false;
	}
	
	public Door(Point2D position, Room nextRoom, Point2D nextPosition) {
		super(position, LAYER);
		this.nextRoom = nextRoom;
		this.nextPosition = nextPosition;
		ID = -1;
		opened = true;
	}
	
// ImageTile Interface
	
	@Override
	public String getName() {
		if (opened)
			return "DoorOpen";
		return "DoorClosed";
	}

	@Override
	public Point2D getPosition() {
		return super.getPosition();
	}

	@Override
	public int getLayer() {
		return super.getLayer();
	}
	
// Only Door
	
	public void openDoor() {
		opened = true;
	}
	
	public int getID() {
		return ID;
	}
	
	public Room getNextRoom() {
		return nextRoom;
	}
	
	public Point2D getNextPosition() {
		return nextPosition;
	}
}
