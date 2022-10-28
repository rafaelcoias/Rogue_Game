package pt.iscte.poo.example;

import pt.iscte.poo.utils.Point2D;

public class EndGame extends GameElement {
	private final static Point2D POSITION = new Point2D(0,0);
	private final static int LAYER = 5;
	private boolean win;
	
	public EndGame(boolean win) {
		super(POSITION, LAYER);
		this.win = win;
	}
	
	@Override
	public String getName() {
		if (win)
			return "YouWon";
		return "GameOver";
	}

	@Override
	public Point2D getPosition() {
		return POSITION;
	}

	@Override
	public int getLayer() {
		return LAYER;
	}
}
