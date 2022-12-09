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
	private boolean poisened = false;
	
	private final static int MAXLIFE = 10;
	private final static int SWORD = 2;
	private final static int DAMAGE = -1;
	private final static int CAPACITY = 3;
	
	private ArrayList<GameElement> itemsBar = new ArrayList<>();
	
	private ArrayList<Square> lifeBar = new ArrayList<>();

	public Hero(Point2D position, Room room) {
		super(position, LAYER, "Hero");
		for (int i = 0; i != Engine.GRID_WIDTH; i++)
			lifeBar.add(new Square(new Point2D(i, Engine.GRID_HEIGHT - 1), "Green"));
		this.room = room;
		life = MAXLIFE;
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
		if (getLife() <= 0)
			Engine.endGame(false);
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
		if (((GameElement)mob) instanceof Thief)
			((Thief)mob).dropItems();
		Engine.removeObject((GameElement)mob);
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
		else if (objInFront instanceof Mob)
			attack(moveVector);
		else if (objInFront instanceof Item)
			pickItem(objInFront.getPosition(), moveVector);
		else if (objInFront instanceof Door && ((Door)objInFront).isOpen())
			moveToNextRoom((Door)objInFront);
		else if (objInFront instanceof Door && !((Door)objInFront).isOpen())
			if (hasKey((Door)objInFront))
				((Door)objInFront).openDoor();
	}
	
	public boolean isFloor(GameElement e) {
		return e == null;
	}
	
	// Checks if the hero moves to the map edge
	
	private boolean isMapLimit(Vector2D v) {
		Point2D p = getPosition().plus(v);
		if (p.getX() < 0 || p.getY() < 0 || p.getX() >= Engine.GRID_WIDTH || p.getY() >= Engine.GRID_HEIGHT - 2)
			return true;
		return false;
	}
	
	// Picks all Object on that position.
	// It changes the position of the object to
	// put it on the items bar
	// Increments number of current items
	// If it picks the treasure, wins the game
	
	private void pickItem(Point2D p, Vector2D v) {
		while (room.getObject(p) != null) {
			if (itemsBar.size() == CAPACITY)
				break ;
			GameElement item = room.getObject(p);
			item.setPosition(new Point2D(itemsBar.size(), Engine.GRID_HEIGHT - 2));
			itemsBar.add(item);
			room.removeObject(item);
			if (item instanceof Treasure) {
				score += 100;
				Engine.endGame(true);
			}
		}
		move(v);
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
		if (index >= itemsBar.size())
			 return ;
		GameElement item = itemsBar.get(index);
		item.setPosition(getPosition());
		itemsBar.remove(index);
		room.addObject(item);
		sortItemsBar(index);
	}
	
	// Checks if the hero has an HealingPotion on his
	// item list.
	// If there is it heals the hero.
	// Then removes the HealingPotion from the game
	// Decrements the number of current items
	
	public void heal() {
		for (GameElement item : itemsBar) {
			if (item instanceof HealingPotion) {
				int index = itemsBar.indexOf(item);
				setLife(((HealingPotion)item).getHeal());
				Engine.removeObject(item);
				itemsBar.remove(item);
				sortItemsBar(index);
				poisened = false;
				break ;
			}
		}
	}
	
	// Sorts item bar by moving every item to the left
	
	private void sortItemsBar(int index) {
		for (int i = 0; i != itemsBar.size(); i++)
			itemsBar.get(i).setPosition(new Point2D(i, Engine.GRID_HEIGHT - 2));
	}
	
	public void poisened() {
		poisened = true;
	}
	
	public boolean isPoisened() {
		return poisened;
	}
	
	public GameElement stealItem() {
		if (itemsBar.size() == 0)
			return null;
		int index = (int)(Math.random() * itemsBar.size());
		GameElement e = itemsBar.get(index);
		Engine.removeObject(e);
		itemsBar.remove(e);
		sortItemsBar(index);
		return e;
	}
	
	private boolean hasKey(Door door) {
		if  (door.getID() == -1)
			return true;
		for (GameElement item : itemsBar) {
			if (item instanceof Key) {
				if (((Key)item).getID() == door.getID()) {
					itemsBar.remove(item);
					sortItemsBar(itemsBar.indexOf(item));
					Engine.removeObject(item);
					return true;
				}	
			}
		}
		return false;
	}
	
	private int hasSword() {
		for (GameElement item : itemsBar)
			if (item instanceof Sword)
				return SWORD;
		return 1;
	}
	
	private boolean hasArmor() {
		for (GameElement item : itemsBar)
			if (item instanceof Armor)
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
			((Door)Engine.getRoom(d.getNextRoom()).getObject(d.getNextPosition())).openDoor();
			room.removeObject(this);
			room = Engine.getCurrentRoom();
			setPosition(d.getNextPosition());
			Engine.checkPoint();
			return ;
		} 
		Engine.reDoMap(d.getNextRoom());
		((Door)Engine.getRoom(d.getNextRoom()).getObject(d.getNextPosition())).openDoor();
		room.removeObject(this);
		room = Engine.getCurrentRoom();
		setPosition(d.getNextPosition());
		Engine.checkPoint();
	}
	
// Set Functions
	
	public void setRoom(Room room) {
		this.room = room;
	}
	
	public void setHeroLife(int life) {
		this.life = life;
	}
	
	public void setHeroScore(int score) {
		this.score = score;
	}
	
	@SuppressWarnings("unchecked")
	public void setItems(ArrayList<GameElement> itemsBar) {
		this.itemsBar = (ArrayList<GameElement>)itemsBar.clone();
		for (GameElement e : itemsBar)
			Engine.addObjectImage(e);
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
	
	public ArrayList<GameElement> getItems() {
		return itemsBar;
	} 
}
