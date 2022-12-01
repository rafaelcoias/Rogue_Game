package pt.iscte.poo.example;
import pt.iscte.poo.utils.Vector2D;

public interface Mob {
	final static int LAYER = 2;
	public void move(Vector2D v);
	public boolean canMove(GameElement e);
	public void attack(Vector2D moveVector);
	public void setLife(int damage);
	public int getLife();
	public int getKillValue();
}
