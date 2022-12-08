package pt.iscte.poo.example;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class Main {
	
	private static String[] menuOptions = {"Play", "Best Scores", "How To Play", "Exit"};
	private static String[] returnOption = {"Return"};
	
	// The program starts here.
	
	public static void main(String[] args) {
		while (true)
			if (initMenu())
				break ;
	}
	
	public static boolean initMenu() {
		int op = JOptionPane.showOptionDialog(null, "Choose an Option", "MENU", 0, 1, null, menuOptions, null);
		if (op == 0)
			Engine.getInstance().start();
		else if (op == 1) {
			showScores();
			return false;
		}
		else if (op == 2) {
			howToPlay();
			return false;
		}
		return true;
	}
	
	private static void howToPlay() {
		String result = "";
		result = "1 | Drop Items using 1, 2 and 3\n";
		result += "2 | To heal your life use H\n";
		result += "3 | Walk around rooms using keys\n";
		result += "4 | Goal - Find the treasure!";
		JOptionPane.showOptionDialog(null, result, "HOW TO PLAY", 0, 1, null, returnOption, null);
	}
	
	private static void showScores() {
		try {
			String result = "";
			Scanner sc = new Scanner(new File("./saveGames.txt"));
			while (sc.hasNextLine())
				result = result + sc.nextLine() + "\n";
			sc.close();
			JOptionPane.showOptionDialog(null, result, "SCORES", 0, 1, null, returnOption, null);
		} catch (IOException e) {
			System.err.println("Erro ao ler o ficheiro.");
		}
	}
}
