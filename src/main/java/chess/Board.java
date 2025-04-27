package chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.IntConsumer;

import chess.Pawn.PawnColor;

public class Board {

	private Pawn[][] board;
	public static final int BOARD_SIZE = 10;


	public Board(PawnColor firstColor) {
		initBoard();
	}

	public Board() {
		this(PawnColor.BLACK);
	}


	private void loopBoard(BiConsumer<Integer, Integer> row, IntConsumer column, IntConsumer afterColumn) {

//		Pour chaque colonne
		for (int y = 0; y < BOARD_SIZE; y++) {

			column.accept(y);

			// Pour chaque ligne
			for (int x = 0; x < BOARD_SIZE; x++) {
				row.accept(x, y);
			}

			afterColumn.accept(y);
		}
	}

	public void loopBoard(BiConsumer<Integer, Integer> row) {
		loopBoard(row, y -> {}, y -> {});
	}

	private void loopBoard(BiConsumer<Integer, Integer> row, IntConsumer column) {
		loopBoard(row, column, y -> {});
	}

	private void initBoard() {
		this.board = new Pawn[BOARD_SIZE][BOARD_SIZE];
		loopBoard((x, y) -> {

			board[x][y] = null;

			// Black
			if (y < 4 && (x + y) % 2 != 0) {
				board[x][y] = new Pawn(new Location(x, y), PawnColor.BLACK);
			}

			// White
			if (y >= 6 && (x + y) % 2 != 0) {
				board[x][y] = new Pawn(new Location(x, y), PawnColor.WHITE);
			}
		});

		board[3][8] = null;
		board[5][6] = null;

	}

	class EatingResult {
		private List<Location> eatingPawn;
		private List<Location> lastPawnPos;

		public EatingResult(List<Location> eatingPawn, List<Location> lastPawnPos) {
			super();
			this.eatingPawn = eatingPawn;
			this.lastPawnPos = lastPawnPos;
		}
	}

	public EatingResult eat(List<Location> from, List<Location> eatingPawn) {

//		System.out.println();
//		System.out.println("From : " + from);

		// Meilleur chemin
		List<Location> bestWay = new ArrayList<>(eatingPawn);
		List<Location> lastPos = new ArrayList<>(from);

		// Les directions
		int[] xX = { 1, 1, -1, -1 };
		int[] yY = { -1, 1, 1, -1 };

		// Pour chaque direction
		for (int i = 0; i < 4; i++) {

//			System.out.println("Position : " + i);

			// Case adjacentes
			int xNearby = from.get(from.size() - 1).getX() + xX[i];
			int yNearby = from.get(from.size() - 1).getY() + yY[i];
			boolean nearbyInBound = inBound(xNearby, yNearby);

			// Case suivante dans l'adjacence
			int xNextCase = xNearby + xX[i];
			int yNextCase = yNearby + yY[i];
			boolean nextInBound = inBound(xNextCase, yNextCase);

//			System.out.println("Nearby : " + new Location(xNearby, yNearby));
//			System.out.println("Next : " + new Location(xNextCase, yNextCase));

			if (!nearbyInBound || !nextInBound)
				continue;

			// Pion potentiel de la case adjacente
			Pawn nearbyPawn = board[xNearby][yNearby];
//			System.out.println("nearbyPawn : " + nearbyPawn);

			boolean isAdversary = nearbyPawn != null && nearbyPawn.getPawnColor() != playerColorTurn;
			boolean isAlreadyEat = false;

			for (Location l : eatingPawn) {
				isAlreadyEat |= l.getX() == xNearby && l.getY() == yNearby;
			}
//			System.out.println("isAlreadyEat : " + isAlreadyEat);

//			System.out.println("eatingPawn : " + eatingPawn);

			// Vérifier si un adversaire est autour
			if (isAdversary && !isAlreadyEat) {

				boolean isFree = isFreeCase(new Location(xNextCase, yNextCase));
//				System.out.println("isFree : " + isFree);

				// Si la case de destination est vide alors mangé le pion adverse
				if (isFree) {

					List<Location> newEating = new ArrayList<>(eatingPawn);
					newEating.add(new Location(xNearby, yNearby));

					List<Location> newPawnPos = new ArrayList<>(from);
					newPawnPos.add(new Location(xNextCase, yNextCase));

					EatingResult result = eat(newPawnPos, newEating);
					List<Location> newWay = result.eatingPawn;
					List<Location> newFrom = result.lastPawnPos;

					if (newWay.size() > bestWay.size()) {
						bestWay = newWay;
						lastPos = newFrom;
					}
				}
			}
		}

		return new EatingResult(bestWay, lastPos);
	}

	public boolean inBound(int x, int y) {
		return x >= 0 && x < BOARD_SIZE && y >= 0 && y < BOARD_SIZE;
	}

	public boolean inBound(Location l) {
		return l.getX() >= 0 && l.getX() < BOARD_SIZE && l.getY() >= 0 && l.getY() < BOARD_SIZE;
	}



	public void turn(Location from, Location to) {

		// Les coordonnées ne sont pas dans la limite du plateau
		if (!inBound(from) || !inBound(to)) {
			System.out.println("OutOfBound");
			return;
		}

		// Vérifier si le from est de la bonne couleur
		if (!isOwnPawn(from, playerColorTurn)) {
			System.out.println("Not your Pawn");
			return;
		}

		// Vérifie si un coup obligatoire doit être jouer
		if (mandatoryMoveAvailable(from))
			return;

		if (inBound(to) && isFreeCase(to)) { // Vérifier si le pion peux allez sur la case demander

			// Déplacé le pion du joueur
			movePawn(from, to);

		} else {
			// Si aucun coup possible la parti est terminé
			// TODO COMPLETER
		}

		showBoard();

		// Changement de joueur
		playerColorTurn = playerColorTurn.equals(PawnColor.BLACK) ? PawnColor.WHITE : PawnColor.BLACK;
	}

	public void movePawn(Location from, Location to) {

		if (!inBound(from) || !inBound(to)) {
//			System.out.println("[movePawn] OutofBoard");
			return;
		}

		Pawn pawn = board[from.getX()][from.getY()];
		board[to.getX()][to.getY()] = pawn;
		board[from.getX()][from.getY()] = null;
	}

	public boolean isOwnPawn(Location l, PawnColor color) {

		// Si la position de la case reste dans la bordure
		if (inBound(l)) {

			// Récuperer le pion potentiel
			Pawn pawn = board[l.getX()][l.getY()];

			// Récupérer et comparer la couleur du pion avec le joueur courant s'il existe
			if (pawn != null)
				return pawn.getPawnColor().equals(color);

			return false;
		}
		return false;
	}

	public boolean mandatoryMoveAvailable(Location from) {

		// Vérifier si des pions peuvents être obligatoirement mangé
		EatingResult result = eat(new ArrayList<>(Arrays.asList(from)), new ArrayList<>());

		List<Location> eaten = result.eatingPawn;
//		System.out.println(eaten);
		List<Location> lastPos = result.lastPawnPos;
//		System.out.println(lastPos);

		// Si non vide supprimé les pions du plateau et déplacer le pion
		if (!eaten.isEmpty()) {

			for (int i = 0; i < lastPos.size(); i++) {
				if (i + 1 >= lastPos.size())
					continue;
				movePawn(lastPos.get(i), lastPos.get(i + 1));
				showBoard();
			}

			// Récupere le chemin et supprimer les pions adverse mangé
			for (Location location : eaten) {
				board[location.getX()][location.getY()] = null;
			}

			showBoard();
			return true;
		}
		return false;
	}

	public boolean isFreeCase(Location to) {
		return board[to.getX()][to.getY()] == null;
	}

	public void showBoard() {

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
			Thread.currentThread().interrupt();
		}

		loopBoard((x, y) -> {
			Pawn pawn = board[x][y];
			System.out.print(pawn == null ? ". " : pawn.getPawnColor().getSign() + " ");

		}, y -> System.out.println());

		System.out.println("\n");
	}
}
