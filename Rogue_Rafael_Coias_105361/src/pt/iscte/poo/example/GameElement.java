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
	
	public void setPosition(Point2D position) {
		this.position = position;
	}
	
	public int getLayer() {
		return LAYER;
	}
	
	// Creates a Random number between 1 - 100 and
	// returns true if it is between 1 - percentage
	
	public boolean random(int percentage) {
		return (int)(Math.random() * 100) + 1 <= percentage ? true : false;
	}
	
	// Returns a Random Vector 
	
	public Vector2D randomVector() {
		Direction randDirection = Direction.random();
		return randDirection.asVector();
	}
}
