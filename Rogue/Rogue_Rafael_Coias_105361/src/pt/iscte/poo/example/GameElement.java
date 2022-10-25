package pt.iscte.poo.example;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;
import pt.iscte.poo.gui.ImageTile;

public abstract class GameElement implements ImageTile {

	private Point2D position;
	private final int LAYER;
	
	public GameElement(Point2D position, int layer) {
		this.position = position;
		this.LAYER = layer;
	}
	
	public Point2D getPosition() {
		return position;
	}
	
	public int getLayer() {
		return LAYER;
	}
	
	// Returns a Random number between 1 - 100
	
	public boolean random(int perc) {
		return (int)(Math.random() * 100) + 1 <= perc ? true : false;
	}
	
	// Returns a Random Vector 
	
	public Vector2D randomVector() {
		Direction randDirection = Direction.random();
		return randDirection.asVector();
	}
}
