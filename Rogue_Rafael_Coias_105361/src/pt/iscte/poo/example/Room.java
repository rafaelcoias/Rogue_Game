package pt.iscte.poo.example;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pt.iscte.poo.utils.Point2D;

public class Room {
	
	private File file;
	private List<GameElement> objects = new ArrayList<>();
	
	public Room(File file) {
		this.file = file;
	}

	public File getFile() {
		return file;
	}
	
	public void addObject(GameElement e) {
		objects.add(e);
	}
	
	public void removeObject(GameElement e) {
		objects.remove(e);
	}
	
	public List<GameElement> getObjects() {
		return objects;
	}
	
	// Returns the Object which is at the position p
	
	public GameElement getObject(Point2D p) {
		for (GameElement e : objects)
			if (e.getPosition().equals(p)) 
				return e;
		return null;
	}
}
