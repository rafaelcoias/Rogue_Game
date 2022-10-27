package pt.iscte.poo.example;

import java.util.ArrayList;

import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Hero extends GameElement implements Mob {

	private Point2D position;
	private int currentRoom;
	private Room room;
	private String name = "User1";

	private int life = 10;
	private int score = 0;
	
	private final static int SWORD = 2;
	private final static int DAMAGE = -1;
	private final static int LAYER = 2;
	
	private ArrayList<Item> itemsBar = new ArrayList<>();
	private final int CAPACITY = 3;
	private int items = 0;
	
	private ArrayList<ImageTile> lifeBar = new ArrayList<>();

	public Hero(Point2D position, Room room) {
		super(position, LAYER);
		this.position = position;
		for (int i = 0; i != Engine.GRID_WIDTH; i++)
			lifeBar.add(new Square(new Point2D(i, Engine.GRID_HEIGHT - 1), "Green"));
		this.room = room;
	}
	
	
// ImageTile Interface
	

	@Override
	public String getName() {
		return "Hero";
	}
	
	@Override
	public Point2D getPosition() {
		return position;
	}

	@Override
	public int getLayer() {
		return LAYER;
	}

	
// Mob Interface
	
	
	@Override
	public void move(Vector2D v) {
		position = position.plus(v);
	}
	
	@Override
	public boolean canMove(GameElement e) {
		return e.getLayer() < getLayer();
	}

	@Override
	public void setLife(int DAMAGE) {
		if ((DAMAGE > 0 && getLife() == 10) || (hasArmor() && random(50)))
			return ;
		life = life + DAMAGE < 0 ? 0 : life + DAMAGE;
		life = life > 10 ? 10 : life;
		for (int i = 0; i != life; i++)
			lifeBar.set(i, new Square(new Point2D(i, Engine.GRID_HEIGHT - 1), "Green"));
		for (int i = life; i != 10; i++)
			lifeBar.set(i, new Square(new Point2D(i, Engine.GRID_HEIGHT - 1), "Red"));
	}
	
	@Override
	public int getLife() {
		return life;
	}
	
	@Override
	public void attack(Vector2D moveVector) {
		Mob mob = (Mob)room.getObject(position.plus(moveVector));
		mob.setLife(DAMAGE * hasSword());
		if (mob.getLife() > 0)
			return ;
		Engine.removeObject(room.getObject(position.plus(moveVector)));
	}
	
	
// Only Hero

	// Checks if the hero can move.
	// Then, if there is a monster in the direction moved to,
	// the hero attacks it, if not the hero just move

	public void moveHero(int key) {
		Direction move = Direction.directionFor(key);
		Vector2D moveVector = move.asVector();
		GameElement objInFront = room.getObject(position.plus(moveVector));
		if (objInFront == null)
			move(moveVector);
		else if (objInFront.getLayer() == getLayer())
			attack(moveVector);
		else if (objInFront.getLayer() == 1 && items < CAPACITY)
			pickItem((Item)objInFront, moveVector);
		else if (objInFront.getName().equals("DoorOpen"))
			moveToNextRoom((Door)objInFront);
		else if (canMove(objInFront))
			move(moveVector);
		else if (objInFront.getName().equals("DoorClosed")) {
			Door door = (Door)objInFront;
			if (hasKey(door))
				door.openDoor();
		}
	}
	
	// Picks an Object and checks if it is a
	// Sword or an Armor.
	// It changes the position of the object to
	// put it on the items bar
	// Increments number of current items
	
	private void pickItem(Item i, Vector2D v) {
		move(v);
		i.setPosition(new Point2D(items, Engine.GRID_HEIGHT - 2));
		itemsBar.add(items, i);
		items++;
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
		Item i = itemsBar.get(index);
		i.setPosition(getPosition());
		itemsBar.remove(index);
		sortItemsBar(index);
		items--;
	}
	
	// Checks if the hero has an HealingPotion on his
	// item list.
	// If there is it heals the hero.
	// Then removes the HealingPotion from the game
	// Decrements the number of current items
	
	public void heal() {
		for (Item i : itemsBar) {
			if (i.getName().equals("HealingPotion")) {
				HealingPotion p = (HealingPotion)i;
				int index = itemsBar.indexOf(i);
				setLife(p.getHeal());
				i.setPosition(new Point2D(-1,-1));
				//Engine.removeObject(i);
				itemsBar.remove(i);
				sortItemsBar(index);
				items--;
				break ;
			}
		}
	}
	
	// Sorts item bar by moving every item to the left
	
	private void sortItemsBar(int index) {
		for (int i = 0; i != items - 1; i++) {
			Item item = itemsBar.get(i);
			item.setPosition(new Point2D(i, Engine.GRID_HEIGHT - 2));
		}
	}
	
	private boolean hasKey(Door door) {
		for (Item i : itemsBar) {
			if (i.getName().equals("Key")) {
				Key k = (Key)i;
				if (k.getID() == door.getID())
					return true;
			}
		}	
		return false;
	}
	
	private int hasSword() {
		for (Item i : itemsBar)
			if (i.getName().equals("Sword"))
				return SWORD;
		return 1;
	}
	
	private boolean hasArmor() {
		for (Item i : itemsBar)
			if (i.getName().equals("Armor"))
				return true;
		return false;
	}
	
	// Move hero to the Room "room"
	// If there isn't any Room like "room" it adds it
	// to the list of rooms in the Engine and then
	// moves the hero into it
	
	public void moveToNextRoom(Door d) {
		Engine.clearObjects(room);
		if (!Engine.roomExists(d.getNextRoom())) {
			Engine.addRoom(d.getNextRoom());
			Engine.createMap(d.getNextRoom());
			Door door = (Door)d.getNextRoom().getObject(d.getNextPosition());
			door.openDoor();
		} 
		else
			Engine.reDoMap(d.getNextRoom());
		room = Engine.getCurrentRoom();
		position = d.getNextPosition();
	}
	
	// Set current room
	
	public void setRoom(Room room) {
		this.room = room;
	}
	
	// Changes user's name

	public void setName(String name) {
		this.name = name;
		if (name == null)
			this.name = "User1";
	}
	
// Get Functions
	
	public String getUserName() {
		return name;
	}
	
	public int getScore() {
		return score;
	}
	
	public ArrayList<ImageTile> getLifeBar() {
		return lifeBar;
	}	
	
	public ArrayList<ImageTile> getItems() {
		ArrayList<ImageTile> list = new ArrayList<>();
		for (Item i : itemsBar)
			list.add((ImageTile)i);
		return list;
	}	 
}
