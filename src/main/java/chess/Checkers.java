package chess;

import java.util.List;
import java.util.Scanner;

import chess.Pawn.PawnColor;

public class Checkers {

	private PawnColor playerColorTurn;
	private Scanner scanner;

	public Checkers(PawnColor firstColor) {
		this.playerColorTurn = firstColor;
		this.scanner = new Scanner(System.in);
	}

	public static void main(String[] args) {

		Checkers checkers = new Checkers(PawnColor.WHITE);
		checkers.playGame();
	}

	public Location formatInputPlayer(String text) {
		String[] split = text.split("|");

		if (split.length != 2) {
			System.out.println("Le format n'est pas le bon");
			return null;
		}

		try {
			String letter = split[0].toUpperCase();

			int y = Integer.parseInt(split[1]) - 1;
			int x = Board.letter.indexOf(letter);

			return new Location(x, y);
		} catch (NumberFormatException e) {
			System.out.println("L'entré doit être une lettre suivi d'un chiffre");
			return null;
		}
	}

	public Location getMove(Board board, boolean isFirst, String title) {
		Location l = null;
		boolean isPawnCase = true;

		do {
			System.out.print(title + " : ");
			String fromPos = scanner.nextLine();

			l = formatInputPlayer(fromPos);

			if (l != null && isFirst) {
				isPawnCase = !board.isFreeCase(l);

				if (!isPawnCase) {
					title = "Cette case n'a pas de pion veuillez en choisir une autre";
				}
			}


		} while (l == null || !isPawnCase);

		return l;
	}

	public void playGame() {
		Board board = new Board();
		board.initBoard();

		PawnColor player = playerColorTurn;
		board.showBoard();

		while (!board.isGameOver()) {

			if (player.equals(PawnColor.WHITE)) {


				Location from = getMove(board, true, "Choisis ton coup");
				Location to = getMove(board, false, "Choisis une destination");

				System.out.println("from : " + from);
				System.out.println("to : " + to);

				board.applyMove(new Move(from, to));

			} else {

				System.out.println("Tour de l'IA");
				EncapsuleMove bestMove = getBestMove(board, player, 3);

				if (bestMove != null) {
					for (Move move : bestMove) {
						board.applyMove(move);
					}

					System.out.println("L'IA à joué " + bestMove);
				} else {

					System.out.println("L'IA ne peux plus jouer");
					break;
				}

			}

			board.showBoard();
			player = player.equals(PawnColor.BLACK) ? PawnColor.WHITE : PawnColor.BLACK;
		}

		System.out.println("La partie est terminé");

	}

	public EncapsuleMove getBestMove(Board board, PawnColor pawnColor, int depth) {

		List<EncapsuleMove> possibleMoves = board.getAllMoves(pawnColor);
		System.out.println("possibleMoves : " + possibleMoves);
		EncapsuleMove bestMove = null;
		int bestScore = Integer.MIN_VALUE;

		for (EncapsuleMove encapsuleMove : possibleMoves) {
			Board newBoard = board.copy();

			System.out.println("encapsuleMove : " + encapsuleMove);
			for (Move move : encapsuleMove) {
				newBoard.applyMove(move);
			}

			int score = minMax(newBoard, depth, false);

			if (score > bestScore) {
				bestScore = score;
				bestMove = encapsuleMove;
			}
		}

		System.out.println("bestMove : " + bestMove);
		return bestMove;
	}

	public int evaluate(Board board) {

		int score = 0;

		for (int y = 0; y < Board.BOARD_SIZE; y++) {
			for (int x = 0; x < Board.BOARD_SIZE; x++) {

				Pawn pawn = board.getPawn(x, y);

				if (pawn != null) {
					int factor = pawn.getPawnColor() == PawnColor.BLACK ? 1 : -1;
					score += pawn.isQueen() ? factor * 3 : factor * 1;
				}
			}
		}

		return score;
	}

	public int minMax(Board board, int depth, boolean isIA) {

		if (depth == 0 || board.isGameOver()) {
			return evaluate(board);
		}

		List<EncapsuleMove> moves = board.getAllMoves(isIA ? PawnColor.BLACK : PawnColor.WHITE);

		// Aucun mouvement défaite du joueur
		if (moves.isEmpty()) {
			return isIA ? Integer.MIN_VALUE : Integer.MAX_VALUE;
		}

		int bestScore = isIA ? Integer.MIN_VALUE : Integer.MAX_VALUE;

		for (EncapsuleMove encapsuleMove : moves) {

			Board newBoard = board.copy();

			for (Move move : encapsuleMove) {
				newBoard.applyMove(move);
			}

			int score = minMax(newBoard, depth - 1, !isIA);

			if (isIA) {
				bestScore = Math.max(bestScore, score);
			} else {
				bestScore = Math.min(bestScore, score);
			}
		}

		return bestScore;
	}



}
