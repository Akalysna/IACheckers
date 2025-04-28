package util;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import chess.Board;
import chess.EncapsuleMove;
import chess.Location;

public class ConsoleUtils {

	public static final boolean showLog = false;
	private Scanner scanner = new Scanner(System.in);

	public static void log(String str) {
		if (showLog)
			System.out.println(str);
	}

	/**
	 * 
	 * @param location
	 * @return
	 */
	public String locationToUserInput(Location location) {

		if (location == null)
			return "";

		// Récupère la coordonnée X de la case
		// et la convertit en lettre correspondante
		String x = Board.letter.get(location.getX());

		// On ajoute 1 à la coordonnée Y pour l'affichage
		// car les coordonnées de l'utilisateur commencent à 1
		return x + (location.getY() + 1);
	}

	/**
	 * Parse l'entrée de l'utilisateur pour obtenir une position sur le plateau de
	 * jeu. La position doit être au format lettre-chiffre (ex: A1).
	 * 
	 * @param text
	 * @return Location ou null si l'entrée est invalide
	 * @throws NumberFormatException          si le format de l'entrée est incorrect
	 * @throws ArrayIndexOutOfBoundsException si la lettre ou le chiffre est en
	 *                                        dehors des limites du tableau
	 */
	public Location parsePlayerInput(String text) {

		// Sépare la chaîne de caractères en deux parties : une lettre et un chiffre
		String[] split = text.split("|");

		// Si la chaîne ne contient pas exactement deux parties, affiche un message
		// d'erreur
		if (split.length != 2) {
			System.out.println("Le format n'est pas le bon");
			return null;
		}

		try {
			// Récupère la lettre et la convertit en majuscule pour la normaliser
			String letter = split[0].toUpperCase();

			// Récupère le chiffre et le convertit en entier, puis le décrémente de 1 pour
			// l'adapter à l'indexation
			// des tableaux (qui commencent à 0)
			int y = Integer.parseInt(split[1]) - 1;
			int x = Board.letter.indexOf(letter);

			return new Location(x, y);

		} catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
			System.out.println("Les coordonnées doivent être au format lettre-chiffre (ex: A1)");
			return null;
		}
	}

	public String formatChainedLocations(EncapsuleMove encapsuleMove) {

		List<String> list = encapsuleMove.getChainedLocations().stream().map(this::locationToUserInput)
				.collect(Collectors.toList());
		
		return String.join(" - ", list);
	}

	/**
	 * 
	 * @param board
	 * @param isFirst
	 * @param title
	 * @return
	 */
	public Location obtainMoveFromPlayer(Board board, boolean isFirst, String title) {
		Location l = null;
		boolean isPawnCase = true;

		do {

			// Affiche un message d'invite pour l'utilisateur
			System.out.print(title + " : ");

			// Lit l'entrée de l'utilisateur
			String fromPos = scanner.nextLine();

			l = parsePlayerInput(fromPos);

			if (l != null && isFirst) {
				isPawnCase = !board.isFreeCase(l);

				if (!isPawnCase) {
					title = "Cette case n'a pas de pion veuillez en choisir une autre";
				}
			}


		} while (l == null || !isPawnCase);

		return l;
	}
    
}
