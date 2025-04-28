package chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import chess.Pawn.PawnColor;

public class Board {

	public static final List<String> letter = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J");

	private Pawn[][] board;
	public static final int BOARD_SIZE = 10;

	public Board() {
		this.board = new Pawn[BOARD_SIZE][BOARD_SIZE];

		for (int y = 0; y < BOARD_SIZE; y++) {
			for (int x = 0; x < BOARD_SIZE; x++) {
				board[x][y] = null;
			}
		}
	}

	/**
	 * Initialise le plateau de jeu avec les pions noirs et blancs
	 */
	public void initBoard() {

		for (int y = 0; y < BOARD_SIZE; y++) {
			for (int x = 0; x < BOARD_SIZE; x++) {

				// Black
				if (y < 4 && (x + y) % 2 != 0) {
					board[x][y] = new Pawn(PawnColor.BLACK);
				}

				// White
				if (y >= 6 && (x + y) % 2 != 0) {
					board[x][y] = new Pawn(PawnColor.WHITE);
				}
			}
		}

		board[2][5] = new Pawn(PawnColor.BLACK);
		board[5][2] = null;

	}

	public boolean isValid(int x, int y) {
		return inBound(x, y) && isFreeCase(new Location(x, y));
	}

	public boolean isFreeCase(Location to) {
		return board[to.getX()][to.getY()] == null;
	}

	public boolean inBound(int x, int y) {
		return x >= 0 && x < BOARD_SIZE && y >= 0 && y < BOARD_SIZE;
	}

	public boolean inBound(Location l) {
		return l.getX() >= 0 && l.getX() < BOARD_SIZE && l.getY() >= 0 && l.getY() < BOARD_SIZE;
	}

	public Pawn getPawn(int x, int y) {
		return board[x][y];
	}


	// ____________________________________________________

	public boolean isGameOver() {

		boolean hasWhitePawn = false;
		boolean hasBlackPawn = false;

		for (int y = 0; y < Board.BOARD_SIZE; y++) {
			for (int x = 0; x < Board.BOARD_SIZE; x++) {

				Pawn pawn = getPawn(x, y);
				if (pawn != null) {
					hasWhitePawn |= pawn.getPawnColor().equals(PawnColor.WHITE);
					hasBlackPawn |= pawn.getPawnColor().equals(PawnColor.BLACK);
				}

				if (hasWhitePawn && hasBlackPawn)
					return false;
			}
		}

		if (getAllPlayerMoves(PawnColor.BLACK).isEmpty())
			return true;
		if (getAllPlayerMoves(PawnColor.WHITE).isEmpty())
			return true;

		return false;
	}

	public void setPawn(Pawn pawn, int x, int y) {
		board[x][y] = pawn;
	}

	public void applyMove(Move move) {

		if (!inBound(move.getFrom()) || !inBound(move.getTo()))
			return;

		Pawn pawn = board[move.getXFrom()][move.getYFrom()];
		board[move.getXTo()][move.getYTo()] = pawn;
		board[move.getXFrom()][move.getYFrom()] = null;

		// Capture d'un pion adverse 
		Location location = move.getMiddle();
	
		if (location == null)
			return;
	
		setPawn(null, location.getX(), location.getY());
	}

	public List<EncapsuleMove> getAllCaptureMove(PawnColor pawnColor) {

		List<EncapsuleMove> captureMoves = new ArrayList<>();

		for (int y = 0; y < BOARD_SIZE; y++) {
			for (int x = 0; x < BOARD_SIZE; x++) {

				Pawn pawn = getPawn(x, y);

				// Passer si la case est vide
				if (pawn == null)
					continue;

				// Passer si le pion n'est pas celui du joueur
				if (!pawn.getPawnColor().equals(pawnColor))
					continue;

				if (!pawn.isQueen()) {
					List<Move> listCapMove = findBestCapturePath(pawnColor, x, y, new ArrayList<>());
					if (!listCapMove.isEmpty())
						captureMoves.add(new EncapsuleMove(listCapMove));
				}
			}
		}
		
		// Régle qui obliga a capture si la capture est possible
		return getMaxCaptureLength(captureMoves);

	}
	
	public List<EncapsuleMove> getMaxCaptureLength(List<EncapsuleMove> list){
		int maxLength = 0;
		for (EncapsuleMove encapsuleMove : list) {
			maxLength = Math.max(maxLength, encapsuleMove.moves.size());
		}
		
		List<EncapsuleMove> newList = new ArrayList<>(); 
		
		for (EncapsuleMove encapsuleMove : list) {
			if(encapsuleMove.moves.size() == maxLength) {
				newList.add(encapsuleMove);
			}
		}
		
		return newList;
	}


	public List<EncapsuleMove> getAllPlayerMoves(PawnColor pawnColor) {

		List<EncapsuleMove> simpleMoves = new ArrayList<>();
		List<EncapsuleMove> captureMoves = new ArrayList<>();

		for (int y = 0; y < BOARD_SIZE; y++) {
			for (int x = 0; x < BOARD_SIZE; x++) {

				Pawn pawn = getPawn(x, y);

				// Passer si la case est vide
				if (pawn == null)
					continue;

				// Passer si le pion n'est pas celui du joueur
				if (!pawn.getPawnColor().equals(pawnColor))
					continue;

				if (!pawn.isQueen()) {
					simpleMoves.addAll(getSimpleMoves(pawnColor, x, y));

					List<Move> listCapMove = findBestCapturePath(pawnColor, x, y, new ArrayList<>());
					if (!listCapMove.isEmpty())
						captureMoves.add(new EncapsuleMove(listCapMove));
				}
			}
		}

		// Régle qui oblige a capture si la capture est possible
		return captureMoves.isEmpty() ? simpleMoves : getMaxCaptureLength(captureMoves);
	}

	public List<EncapsuleMove> getSimpleMoves(PawnColor pawnColor, int x, int y) {
		List<EncapsuleMove> moves = new ArrayList<>();

		// Connaitre le sens de déplacement selon la nature de la pièce
		int direction = pawnColor.equals(PawnColor.BLACK) ? 1 : -1;

		// Droite
		if (isValid(x - 1, y + direction)) {
			Move move = new Move(x, y, x - 1, y + direction);
			moves.add(new EncapsuleMove(move));
		}

		// Gauche
		if (isValid(x + 1, y + direction)) {
			Move move = new Move(x, y, x + 1, y + direction);
			moves.add(new EncapsuleMove(move));
		}

		return moves;
	}

	public Board copy() {
		Board duplicateBoard = new Board();

		for (int y = 0; y < BOARD_SIZE; y++) {
			for (int x = 0; x < BOARD_SIZE; x++) {
				duplicateBoard.setPawn(getPawn(x, y), x, y);
			}
		}

		return duplicateBoard;
	}


	/**
	 * Récupère le plus long chemin de capture disponible depuis une position donnée
	 * @param pawnColor
	 * @param x
	 * @param y
	 * @param moves
	 * @return Retourne une liste vide s'il n'y a pas de capture
	 */
	public List<Move> findBestCapturePath(PawnColor pawnColor, int x, int y, List<Move> moves) {

		// Meilleur chemin
		List<Move> bestWay = new ArrayList<>(moves);

		// Les directions
		int[] xX = { 2, 2, -2, -2 };
		int[] yY = { -2, 2, 2, -2 };

		// Pour chaque direction
		for (int i = 0; i < 4; i++) {
			
			int xTo = x + xX[i];
			int yTo = y + yY[i];

			// Vérifie si le mouvement est une capture
			if (canCapture(pawnColor, x, y, xTo, yTo)) {
				
				Location middleLocation = Move.getMiddle(x, y, xTo, yTo);

				// Vérifie si le pion à déjà été capturer
				if (isEating(middleLocation, moves))
					continue;

				Move move = new Move(x, y, xTo, yTo);
				moves.add(move);

				List<Move> newMoves = findBestCapturePath(pawnColor, xTo, yTo, moves);

				if (newMoves.size() > bestWay.size()) {
					bestWay = newMoves;
				}

			}

		}

		return bestWay;
	}

	public boolean isEating(Location l, List<Move> moves) {
		for (Move m : moves) {
			Location middleLocation = m.getMiddle(); 
			if (middleLocation.equals(l)) {
				return true;
			}
		}
		
		return false;
	}

	public boolean canCapture(PawnColor pawnColor, int xFrom, int yFrom, int xTo, int yTo) {

		if (isValid(xTo, yTo)) {

			int xMiddle = xFrom + (xTo - xFrom) / 2;
			int yMiddle = yFrom + (yTo - yFrom) / 2;

			Pawn middlePawn = getPawn(xMiddle, yMiddle);
			
			if (middlePawn == null)
				return false;

			return !middlePawn.getPawnColor().equals(pawnColor);
		}

		return false;
	}


	public void showBoard() {

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
			Thread.currentThread().interrupt();
		}

		System.out.print("   ");
		System.out.println(String.join(" ", letter));

		for (int y = 0; y < BOARD_SIZE; y++) {

			System.out.print((y != 9 ? " " : "") + String.valueOf(y + 1) + (" "));

			for (int x = 0; x < BOARD_SIZE; x++) {
				Pawn pawn = board[x][y];
				System.out.print(pawn == null ? ". " : pawn.getPawnColor().getSign() + " ");
			}

			System.out.println();
		}

		System.out.println("\n");
	}
}