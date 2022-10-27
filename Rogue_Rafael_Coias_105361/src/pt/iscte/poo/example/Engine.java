package pt.iscte.poo.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

import pt.iscte.poo.gui.ImageMatrixGUI;
import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.observer.Observed;
import pt.iscte.poo.observer.Observer;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Engine implements Observer {

	public static final int GRID_HEIGHT = 12;
	public static final int GRID_WIDTH = 10;
	
	private static Engine INSTANCE = null;
	private static ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
	
	private static Hero hero;
	private int turns;
	
	private static Room currentRoom;
	private static List<Room> rooms = new ArrayList<>();
	
	public static Engine getInstance() {
		if (INSTANCE == null)
			INSTANCE = new Engine();
		return INSTANCE;
	}

	private Engine() {		
		gui.registerObserver(this);
		gui.setSize(GRID_WIDTH, GRID_HEIGHT);
		gui.go();
	}

	// Starts the game, adds floor, walls and
	// then waits for a key to be pressed
	
	public void start() {
		Room room = new Room(new File("./rooms/room0.txt"));
		addRoom(room);
		addFloor();
		currentRoom = room;
		hero = new Hero(new Point2D(1,1), currentRoom);
		addObjectToGame(hero);
		createMap(room);
		gui.setStatusMessage("   ROGUE                           Moves: " 
				+ turns + "            Score: " + hero.getScore());
		gui.update();
	}
	
	// Adds the floor to the game
	
	private void addFloor() {
		List<ImageTile> tileList = new ArrayList<>();
		for (int x=0; x!=GRID_WIDTH; x++)
			for (int y=0; y!=GRID_HEIGHT; y++)
				tileList.add(new Floor(new Point2D(x,y)));
		gui.addImages(tileList);
	}
	
	// Reads the file "roomX" 
	// Returns an error if there is no file found
	
	public static void createMap(Room room) {
		currentRoom = room;
		try {
			readMap(room.getFile());
		} catch (FileNotFoundException e) {
			System.err.println("Erro na abertura do ficheiro");
		}
	}
	
	// Reads the file "room0" and adds a wall
	// whenever a "#" is found.
	// Adds every Object to the map and to the List of Objects
	// Then it shows the life and items of the hero
	
	
	private static void readMap(File file) throws FileNotFoundException {
		Scanner sc = new Scanner(file);
		for (int lineNumb = 0; lineNumb != GRID_HEIGHT - 1; lineNumb++) {
			String line = sc.nextLine();
			for (int i = 0; i != line.length(); i++)
				if (line.charAt(i) == '#')
					addObjectToGame(new Wall(new Point2D(i, lineNumb)));
		}
		addObjects(sc);
		addStatsBar();
		sc.close();
	}
	
	// Adds life and items of the hero 
	
	private static void addStatsBar() {
		for (int i = 0; i != GRID_WIDTH; i++)
			gui.addImage(new Square(new Point2D(i, GRID_HEIGHT - 2), "Black"));
		gui.addImages(hero.getLifeBar());
	}
	
	// Reads the end of the file and adds
	// every object in its position
	
	private static void addObjects(Scanner sc) {
		while (sc.hasNextLine()) {
			String allLine = sc.nextLine();
			String line[] = allLine.split(",");
			String object = line[0];
			int i = Integer.parseInt(line[1]);
			int j = Integer.parseInt(line[2]);
			
			if (object.equals("Skeleton"))
				addObjectToGame(new Skeleton(new Point2D(i, j), currentRoom));
			else if (object.equals("Bat"))
				addObjectToGame(new Bat(new Point2D(i, j), currentRoom));
			else if (object.equals("Thug"))
				addObjectToGame(new Thug(new Point2D(i, j), currentRoom));
			else if (object.equals("Armor"))
				addObjectToGame(new Armor(new Point2D(i, j)));
			else if (object.equals("HealingPotion"))
				addObjectToGame(new HealingPotion(new Point2D(i, j)));
			else if (object.equals("Sword"))
				addObjectToGame(new Sword(new Point2D(i, j)));
			else if (object.equals("Treasure"))
				addObjectToGame(new Treasure(new Point2D(i, j)));
			else if (object.equals("Key"))
				addObjectToGame(new Key(new Point2D(i, j), Integer.parseInt(allLine.substring(allLine.length() - 1))));
			else if (object.equals("Door"))
				addObjectToGame(new Door(new Point2D(i, j), new Room(doFile(line[3])), new Point2D(Integer.parseInt(line[4]), Integer.parseInt(line[5])), Integer.parseInt(allLine.substring(allLine.length() - 1))));
		}
	}
	
	private static void addObjectToGame(GameElement e) {
		currentRoom.addObject(e);
		gui.addImage(e);
	}
	
	private static File doFile(String name) {
		return new File ("./rooms/" + name + ".txt");
	}
	
	// Awaits for a key to be pressed and verifies
	// if it is a direction
	// If it is, the hero moves and so do the enemies
	
	@Override
	public void update(Observed source) {
		int key = ((ImageMatrixGUI) source).keyPressed();
		
		if (isTurn(key)) {
			if (Direction.isDirection(key))
				hero.moveHero(key);
			else if (key >= '1' && key <= '9')
				hero.dropItem(key);
			else if (key == 'h' || key == 'H')
				hero.heal();
			moveMobs();
			turns++;
		}
		gui.setStatusMessage("   ROGUE                           Moves: " 
				+ turns + "            Score: " + hero.getScore());
		gui.addImages(hero.getItems());
		gui.addImages(hero.getLifeBar());
		gui.update();
	}
	
	private boolean isTurn(int key) {
		return Direction.isDirection(key) || (key >= '1' && key <= '3') || key == 'h' || key == 'H';
	}
	
	// Move every enemy (if they can)
	// If the hero is in front of them, it attacks
	
	private void moveMobs() {
		for (GameElement e : currentRoom.getObjects()) {
			if (e.getLayer() == 2 && !e.getName().equals(hero.getName())) {
				Vector2D moveVector = e.getPosition().vectorTo(hero.getPosition());
				GameElement objInFront = currentRoom.getObject(e.getPosition().plus(moveVector));
				Mob mob = (Mob)e;
				if (objInFront == null || mob.canMove(objInFront))
					mob.move(moveVector);
				else if (objInFront.getName().equals(hero.getName()))
					mob.attack(moveVector);
				else if (e.getName().equals("Bat"))
					mob.move(moveVector);
			}
			//System.out.println(e.getName() + " - " + e.getPosition());
		}
	}
	
// Static References	
	
	//	Re do the existing room
	
	public static void reDoMap(Room room) {
		currentRoom = room;
		for (GameElement e : room.getObjects())
			gui.addImage(e);
	}
	
	// Remove object from the game and from the Object List
	
	public static void removeObject(ImageTile e) {
		gui.removeImage(e);
		currentRoom.removeObject((GameElement)e);
	}
	
	// Clear Every object in the game
	
	public static void clearObjects(Room room) {
		for (GameElement e : room.getObjects())
			if (!e.getName().equals("Hero"))
				gui.removeImage(e);
	}
	
	// Get the current room
	
	public static Room getCurrentRoom() {
		return currentRoom;
	}
	
	// Checks if there is a room in the rooms list
	
	public static boolean roomExists(Room room) {
		for (Room r : rooms)
			if (r.getFile().equals(room.getFile()))
				return true;
		return false;
	}
	
	// Adds a room to the game
	
	public static void addRoom(Room room) {
		rooms.add(room);
	}
	
	// Ends the game when hero dies or when it finds the treasure
	
	public static void endGame() {
		System.exit(0);
	}
}
