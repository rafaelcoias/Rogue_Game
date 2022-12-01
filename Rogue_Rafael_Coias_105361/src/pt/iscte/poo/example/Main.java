package pt.iscte.poo.example;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class Main {
	
	private static String[] menuOptions = {"Play", "Best Scores", "Exit"};
	private static String[] scoreOptions = {"Return"};
	
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
		return true;
	}
	
	private static void showScores() {
		try {
			String result = "";
			Scanner sc = new Scanner(new File("./saveGames.txt"));
			while (sc.hasNextLine())
				result = result + sc.nextLine() + "\n";
			sc.close();
			JOptionPane.showOptionDialog(null, result, "SCORES", 0, 1, null, scoreOptions, null);
		} catch (IOException e) {
			System.err.println("Erro ao ler o ficheiro.");
		}
	}
}
