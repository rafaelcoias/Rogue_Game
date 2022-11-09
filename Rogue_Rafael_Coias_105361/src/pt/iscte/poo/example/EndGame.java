package pt.iscte.poo.example;

import pt.iscte.poo.utils.Point2D;

public class EndGame extends GameElement {
	
	private final static int LAYER = 5;
	private boolean win;
	
	public EndGame(boolean win) {
		super(new Point2D(0,0), LAYER);
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
		return super.getPosition();
	}

	@Override
	public int getLayer() {
		return super.getLayer();
	}
}
