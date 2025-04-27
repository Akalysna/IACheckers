package chess;

import java.util.ArrayList;
import java.util.Arrays;

import chess.Board.AskException;

public class InputResult {
	private String from;
	private String to;

	private ArrayList<String> convert;

	public InputResult(String from, String to) {
		super();
		this.from = from;
		this.to = to;

		this.convert.addAll(Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h", "i", "j"));
	}

	public String getFrom() {
		return from;
	}

	public String getTo() {
		return to;
	}

	public Location getFromLocation() {
		try {
			return getLocationFromWritter(from);
		} catch (AskException e) {
			return null;
		}
	}

	public Location getToLocation() {
		try {
			return getLocationFromWritter(to);
		} catch (AskException e) {
			return null;
		}
	}

	private Location getLocationFromWritter(String str) throws AskException {

		String[] splitText = str.split("|");

		// Si le nombre n'est pas correcte
		if (splitText.length != 2)
			throw new AskException("La longueur n'est pas bonne");

		String letter = splitText[0];
		int number = 0;

		// Si la lettre entre n'en est pas un ou ne fait pas parti de la liste
		if (!convert.contains(letter))
			throw new AskException("La lettre écrite n'est pas une lettre autorisé");

		try {
			number = Integer.parseInt(splitText[1]);
		} catch (NumberFormatException e) {
			throw new AskException("La deuxième valeur n'est pas un nombre");
		}

		return new Location(convert.indexOf(letter), number);

	}
}
