package pt.iscte.poo.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JOptionPane;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import pt.iscte.poo.gui.ImageMatrixGUI;
import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.observer.Observed;
import pt.iscte.poo.observer.Observer;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Engine implements Observer {

// Attributes
	
	// Constants
	public static final int GRID_HEIGHT = 12;
	public static final int GRID_WIDTH = 10;
	public static final int ENTER = 10;
	public static final int ESCAPE = 27;
	
	// Game
	private static Engine INSTANCE = null;
	private static ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
	
	// Hero
	private static String username = "";
	private static Hero hero;
	private static int turns;
	
	// End Game
	private static String[] endOptions = {"Play Again", "Exit"};
	private static boolean win = false;
	private static boolean ended = false;
	
	// Game Rooms
	private static Room currentRoom;
	private static List<Room> rooms = new ArrayList<>();
	
// Project
	
	public static Engine getInstance() {
		if (INSTANCE == null)
			INSTANCE = new Engine();
		return INSTANCE;
	}

	private Engine() {
		while (username.length() == 0) {
			username = gui.askUser("Enter your name :");
			if (username == null)
				return ;
		}
		gui.registerObserver(this);
		gui.setSize(GRID_WIDTH, GRID_HEIGHT);
		gui.go();
	}

	// Starts the game, adds floor, walls and
	// then waits for a key to be pressed
	
	public void start() {
		gui.setName("ROGUE");
		Room room = new Room(new File("./rooms/room0.txt"));
		currentRoom = room;
		addRoom(room);
		addFloor();
		hero = new Hero(new Point2D(1,1), currentRoom);
		addObjectToGame(hero);
		createMap(room);
		gui.setStatusMessage("  Moves: " + turns + "                                                                                     "
				+ "Score: " + hero.getScore());
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
		currentRoom.removeObject(hero);
		currentRoom = room;
		currentRoom.addObject(hero);
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
				addObjectToGame(new Door(new Point2D(i, j), new Room(doFile(line[3])), new Point2D(Integer.parseInt(line[4]), Integer.parseInt(line[5])), line.length == 7 ? Integer.parseInt(allLine.substring(allLine.length() - 1)) : -1));
		}
	}
	
	// Adds object to the game and to the object list
	
	private static void addObjectToGame(GameElement e) {
		currentRoom.addObject(e);
		gui.addImage(e);
	}
	
	// Returns a room file
	
	private static File doFile(String name) {
		return new File ("./rooms/" + name + ".txt");
	}
	
	// Awaits for a key to be pressed and verifies
	// if it is a direction
	// If it is, the hero moves and so do the enemies
	
	@Override
	public void update(Observed source) {
		int key = ((ImageMatrixGUI) source).keyPressed();

		if (isTurn(key) && !ended) {
			if (Direction.isDirection(key))
				hero.moveHero(key);
			else if (key >= '1' && key <= '9')
				hero.dropItem(key);
			else if (key == 'h' || key == 'H')
				hero.heal();
			else if (key == ESCAPE)
				System.exit(0);
			moveMobs();
			turns++;
		}
		gui.setStatusMessage("  Moves: " + turns + "                                                                                     "
				+ "Score: " + hero.getScore());
		if (ended)
			checkEndGame();
		gui.update();
	}
	
	private boolean isTurn(int key) {
		return Direction.isDirection(key) || (key >= '1' && key <= '9') || key == 'h' || key == 'H' || key == ESCAPE;
	}
	
	// Move every enemy (if they can)
	// If the hero is in front of them, it attacks
	
	private void moveMobs() {
		for (GameElement e : currentRoom.getObjects()) {
			if (isMob(e) && !e.getName().equals(hero.getName())) {
				Vector2D moveVector = e.getPosition().vectorTo(hero.getPosition());
				GameElement objInFront = currentRoom.getObject(e.getPosition().plus(moveVector));
				Mob mob = (Mob)e;
				if (objInFront == null || (mob.canMove(objInFront) && !objInFront.getName().equals("DoorClosed") && !objInFront.getName().equals("DoorOpen")))
					mob.move(moveVector);
				else if (objInFront.getName().equals(hero.getName()))
					mob.attack(moveVector);
				else if (e.getName().equals("Bat"))
					mob.move(moveVector);
			}
		}
	}
	
	private boolean isMob(GameElement e) {
		return e.getLayer() == 2;
	}
	
// Static References	
	
	public static void reDoMap(Room room) {
		currentRoom = rooms.get(getRoomIndex(room));
		for (GameElement e : currentRoom.getObjects())
			gui.addImage(e);
		currentRoom.addObject(hero);
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
	
	// Returns the index of a certain room
	
	private static int getRoomIndex(Room room) {
		for (Room r : rooms)
			if (r.getFile().equals(room.getFile()))
				return rooms.indexOf(r);
		return -1;
	}
	
	// Returns a certain Room
	
	public static Room getRoom(Room room) {
		for (Room r : rooms)
			if (r.getFile().getName().equals(room.getFile().getName()))
				return r;
		return null;
	}
	
	// Returns the current room
	
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
	
// Game's end
	
	// Ends the game when hero dies or when it finds the treasure
	// and tries to save the game statistics
	
	public static void endGame(boolean won) {
		if (won)
			win = true;
		ended = true;
		try {
			saveGame();
		} catch (FileNotFoundException e) {
			System.err.println("Erro na abertura do ficheiro");
		}
	}
	
	// Checks if the game the user clicks ENTER
	// or ESCAPE :
	// Enter - retry
	// Escape - exit
	
	private void checkEndGame() {
		boolean exit = JOptionPane.showOptionDialog(null, "Choose an option, " + username + " :", win == true ? "You Won!" : "Game Over", 0, 3, null, endOptions, null) == 0 ? false : true;
		if (exit) {
			gui.dispose();
			System.exit(0);
		}
		else {
			resetAll();
			start();
		}
	}
	
	// Resets all game statistics and objects
	
	private static void resetAll() {
		for (Room r : rooms) {
			for (GameElement e : r.getObjects())
				gui.removeImage(e);
			r.getObjects().clear();
		}
		gui.removeImages(hero.getItems());
		gui.removeImages(hero.getLifeBar());
		hero.clearItems();
		gui.removeImage(hero);
		hero.resetScore();
		rooms.clear();
		turns = 0;
		win = false;
		ended = false;
	}
	
	// Saves the game and sorts the 5 games with the best score.
	// Compares the score with a lambda expression.
	
	private static void saveGame() throws FileNotFoundException {
		ArrayList<String> info = readFile();
		DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		LocalDateTime localDate = LocalDateTime.now();
		info.add(username + " " + hero.getScore() + " " + date.format(localDate));
		info.sort((s1, s2) -> Integer.parseInt(s2.split(" ")[1]) - Integer.parseInt(s1.split(" ")[1]));
		writeToFile(info);
	}
	
	private static ArrayList<String> readFile() throws FileNotFoundException {
		ArrayList<String> info = new ArrayList<>();
		Scanner sc = new Scanner(new File("./saveGames.txt"));
		sc.nextLine();
		sc.nextLine();
		while (sc.hasNextLine())
			info.add(sc.nextLine());
		sc.close();
		return info;
	}
	
	private static void writeToFile(ArrayList<String> info) throws FileNotFoundException {
		PrintWriter write = new PrintWriter(new File("./saveGames.txt"));
		write.println("USERNAME - SCORE - DATE");
		write.println();
		for (int i = 0; i != info.size() && i != 5; i++)
			write.println(info.get(i));
		write.close();
	}
}
