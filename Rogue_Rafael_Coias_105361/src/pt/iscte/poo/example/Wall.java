package pt.iscte.poo.example;

import pt.iscte.poo.utils.Point2D;

public class Wall  extends GameElement implements Imovel {

	public Wall(Point2D position) {
		super(position, LAYER, "Wall");
	}
}
