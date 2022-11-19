package pt.iscte.poo.example;

import java.util.ArrayList;

import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Hero extends GameElement implements Mob {

	private Room room;

	private int life;
	private int score = 0;
	
	private final static int MAXLIFE = 10;
	private final static int SWORD = 2;
	private final static int DAMAGE = -10;
	private final static int LAYER = 2;
	private final static int CAPACITY = 3;
	
	private ArrayList<GameElement> itemsBar = new ArrayList<>();
	private int items = 0;
	
	private ArrayList<Square> lifeBar = new ArrayList<>();

	public Hero(Point2D position, Room room) {
		super(position, LAYER);
		for (int i = 0; i != Engine.GRID_WIDTH; i++)
			lifeBar.add(new Square(new Point2D(i, Engine.GRID_HEIGHT - 1), "Green"));
		this.room = room;
		life = MAXLIFE;
	}
	
	
// ImageTile Interface
	

	@Override
	public String getName() {
		return "Hero";
	}
	
	@Override
	public Point2D getPosition() {
		return super.getPosition();
	}

	@Override
	public int getLayer() {
		return super.getLayer();
	}

	
// Mob Interface
	
	
	@Override
	public void move(Vector2D v) {
		setPosition(super.getPosition().plus(v));
	}
	
	@Override
	public boolean canMove(GameElement e) {
		return true;
	}

	@Override
	public void setLife(int DAMAGE) {
		if ((DAMAGE > 0 && getLife() == MAXLIFE) || (hasArmor() && random(50)))
			return ;
		life = life + DAMAGE < 0 ? 0 : life + DAMAGE;
		life = life > MAXLIFE ? MAXLIFE : life;
		for (int i = 0; i != life; i++)
			lifeBar.get(i).setName("Green");
		for (int i = life; i != MAXLIFE; i++)
			lifeBar.get(i).setName("Red");
	}
	
	@Override
	public int getLife() {
		return life;
	}
	
	@Override
	public void attack(Vector2D moveVector) {
		Mob mob = (Mob)room.getObject(getPosition().plus(moveVector));
		mob.setLife(DAMAGE * hasSword());
		if (mob.getLife() > 0)
			return ;
		Engine.removeObject(room.getObject(getPosition().plus(moveVector)));
		score += mob.getKillValue();
	}
	
	@Override
	public int getKillValue() {
		return 0;
	}
	
	
// Only Hero

	// Checks if the hero can move.
	// Then, if there is a monster in the direction moved to,
	// the hero attacks it;
	// If there is an item, picks it;
	// 

	public void moveHero(int key) {
		Direction move = Direction.directionFor(key);
		Vector2D moveVector = move.asVector();
		GameElement objInFront = room.getObject(getPosition().plus(moveVector));
		if (isMapLimit(moveVector))
			return ;
		if (isFloor(objInFront))
			move(moveVector);
		else if (objInFront.getLayer() == getLayer())
			attack(moveVector);
		else if (objInFront.getLayer() == 1)
			pickItem(objInFront, moveVector);
		else if (objInFront.getName().equals("DoorOpen"))
			moveToNextRoom((Door)objInFront);
		else if (objInFront.getName().equals("DoorClosed")) {
			Door door = (Door)objInFront;
			if (hasKey(door))
				door.openDoor();
		}
	}
	
	// Checks if the object to move to is floor
	
	private boolean isFloor(GameElement e) {
		return e == null;
	}
	
	// Checks if the hero moves to the map edge
	
	private boolean isMapLimit(Vector2D v) {
		Point2D p = getPosition().plus(v);
		if (p.getX() < 0 || p.getY() < 0 || p.getX() >= Engine.GRID_WIDTH || p.getY() >= Engine.GRID_HEIGHT - 2)
			return true;
		return false;
	}
	
	// Picks an Object and checks if it is a
	// Sword or an Armor.
	// It changes the position of the object to
	// put it on the items bar
	// Increments number of current items
	
	private void pickItem(GameElement item, Vector2D v) {
		move(v);
		if (items == CAPACITY)
			return ;
		item.setPosition(new Point2D(items, Engine.GRID_HEIGHT - 2));
		itemsBar.add(items, item);
		room.removeObject(item);
		items++;
		if (item.getName().equals("Treasure")) {
			score += 100;
			Engine.endGame(true);
		}
	}
	
	// Drops an Object in the index specified
	// It checks if the dropped object is a
	// Sword or an Armor.
	// Then it puts the object in the hero's position.
	// Finally, organizes the items bar and removes that
	// Object from it.
	// Decrements number of current items
	
	public void dropItem(int index) {
		index = index - '1';
		if (index >= items)
			 return ;
		GameElement item = itemsBar.get(index);
		item.setPosition(getPosition());
		itemsBar.remove(index);
		room.addObject(item);
		sortItemsBar(index);
		items--;
	}
	
	// Checks if the hero has an HealingPotion on his
	// item list.
	// If there is it heals the hero.
	// Then removes the HealingPotion from the game
	// Decrements the number of current items
	
	public void heal() {
		for (GameElement item : itemsBar) {
			if (item.getName().equals("HealingPotion")) {
				HealingPotion p = (HealingPotion)item;
				int index = itemsBar.indexOf(item);
				setLife(p.getHeal());
				Engine.removeObject(item);
				itemsBar.remove(item);
				sortItemsBar(index);
				items--;
				break ;
			}
		}
	}
	
	// Sorts item bar by moving every item to the left
	
	private void sortItemsBar(int index) {
		for (int i = 0; i != items - 1; i++)
			itemsBar.get(i).setPosition(new Point2D(i, Engine.GRID_HEIGHT - 2));
	}
	
	private boolean hasKey(Door door) {
		if  (door.getID() == -1)
			return true;
		for (GameElement item : itemsBar) {
			if (item.getName().equals("Key")) {
				Key k = (Key)item;
				if (k.getID() == door.getID()) {
					itemsBar.remove(item);
					sortItemsBar(itemsBar.indexOf(item));
					items--;
					Engine.removeObject(item);
					return true;
				}	
			}
		}
		return false;
	}
	
	private int hasSword() {
		for (GameElement item : itemsBar)
			if (item.getName().equals("Sword"))
				return SWORD;
		return 1;
	}
	
	private boolean hasArmor() {
		for (GameElement item : itemsBar)
			if (item.getName().equals("Armor"))
				return true;
		return false;
	}
	
	// Move hero to the Room "room"
	// If there isn't any Room like "room" it adds it
	// to the list of rooms in the Engine and then
	// moves the hero into it.
	// Opens the next room's door
	
	public void moveToNextRoom(Door d) {
		Engine.clearObjects(room);
		if (!Engine.roomExists(d.getNextRoom())) {
			Engine.addRoom(d.getNextRoom());
			Engine.createMap(d.getNextRoom());
			score += 10;
		} 
		else
			Engine.reDoMap(d.getNextRoom());
		Door door = (Door)Engine.getRoom(d.getNextRoom()).getObject(d.getNextPosition());
		door.openDoor();
		room.removeObject(this);
		room = Engine.getCurrentRoom();
		setPosition(d.getNextPosition());
	}
	
	// Set the current room
	
	public void setRoom(Room room) {
		this.room = room;
	}
	
// Resets
	
	public void clearItems() {
		itemsBar.clear();
	}
	
	public void resetScore() {
		score = 0;
	}
	
// Get Functions
	
	public int getScore() {
		return score;
	}
	
	public ArrayList<ImageTile> getLifeBar() {
		ArrayList<ImageTile> result = new ArrayList<>();
		for (Square s : lifeBar)
			result.add((ImageTile)s);
		return result;
	}	
	
	public ArrayList<ImageTile> getItems() {
		ArrayList<ImageTile> list = new ArrayList<>();
		for (GameElement item : itemsBar)
			list.add((ImageTile)item);
		return list;
	}	 
}
