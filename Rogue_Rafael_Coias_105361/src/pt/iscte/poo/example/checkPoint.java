package pt.iscte.poo.example;

import java.util.ArrayList;

import pt.iscte.poo.utils.Point2D;

public class checkPoint {
	public static ArrayList<Room> rooms = new ArrayList<>();
	public static ArrayList<Point2D> doors = new ArrayList<>();
	public static ArrayList<GameElement> heroItems = new ArrayList<>();
	public static Room room;
	public static int initPositionX;
	public static int initPositionY;
	public static int heroLife;
	public static int heroScore;
	public static int turns;
	
	public static void saveOpenDoors(Room room) {
		doors.clear();
		checkPoint.doors.clear();
		for (GameElement e : room.getObjects()) {
			if (e instanceof Door && e.getName().equals("DoorOpen"))
				doors.add(e.getPosition());
		}
	}
}
