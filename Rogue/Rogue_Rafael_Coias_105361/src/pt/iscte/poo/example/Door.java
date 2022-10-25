package pt.iscte.poo.example;

import pt.iscte.poo.utils.Point2D;

public class Door extends GameElement {
	private Point2D position;
	private Room nextRoom;
	private Point2D nextPosition;
	
	private final int ID;
	private boolean opened;
	private static int layer = 3;
	
	public Door(Point2D position, Room nextRoom, Point2D nextPosition, int ID) {
		super(position, layer);
		this.position = position;
		this.nextRoom = nextRoom;
		this.nextPosition = nextPosition;
		this.ID = ID;
		opened = false;
	}
	
	public Door(Point2D position, Room nextRoom, Point2D nextPosition, int ID, boolean opened) {
		super(position, layer);
		this.position = position;
		this.nextRoom = nextRoom;
		this.nextPosition = nextPosition;
		this.ID = ID;
		this.opened = opened;
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
		return position;
	}

	@Override
	public int getLayer() {
		return layer;
	}
	
// Only Door
	
	public void openDoor() {
		opened = true;
		layer = 0;
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
