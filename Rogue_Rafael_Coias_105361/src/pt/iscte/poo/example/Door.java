package pt.iscte.poo.example;

import pt.iscte.poo.utils.Point2D;

public class Door extends GameElement {
	
	private Room nextRoom;
	private Point2D nextPosition;
	
	private final int ID;
	private final static int LAYER = 0;
	
	public Door(Point2D position, Room nextRoom, Point2D nextPosition, int ID) {
		super(position, LAYER, "DoorClosed");
		this.nextRoom = nextRoom;
		this.nextPosition = nextPosition;
		this.ID = ID;
	}
	
	public Door(Point2D position, Room nextRoom, Point2D nextPosition) {
		super(position, LAYER, "DoorClosed");
		this.nextRoom = nextRoom;
		this.nextPosition = nextPosition;
		ID = -1;
	}
	
// Only Door
	
	public void openDoor() {
		setName("DoorOpen");
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
	
	public boolean isOpen() {
		return getName().equals("DoorOpen");
	}
}
