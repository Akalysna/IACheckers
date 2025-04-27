package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import chess.Pawn.PawnColor;

public class Checkers {

	private PawnColor playerColorTurn;
	private Scanner scanner;
	public static final boolean showLog = false;

	public Checkers(PawnColor firstColor) {
		this.playerColorTurn = firstColor;
		this.scanner = new Scanner(System.in);
	}

	public static void log(String str) {
		if (showLog)
			System.out.println(str);
	}

	public static void main(String[] args) {

		Checkers checkers = new Checkers(PawnColor.WHITE);
		checkers.playGame(3);
	}

	public String formatLocationToText(Location l) {

		if (l == null)
			return "";

		String x = Board.letter.get(l.getX());
		return x + (l.getY() + 1);
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

	public void playGame(int depth) {
		Board board = new Board();
		board.initBoard();

		PawnColor player = playerColorTurn;
		board.showBoard();

		while (!board.isGameOver()) {

			if (player.equals(PawnColor.WHITE)) {

				Location from = getMove(board, true, "Choisis ton coup");
				Location to = getMove(board, false, "Choisis une destination");
				
				System.out.println("");
				Checkers.log("from : " + from);
				Checkers.log("to : " + to);

				Move move = new Move(from, to);
				board.applyMove(move);

			} else {

				System.out.println("Tour de l'IA");
				EncapsuleMove bestMove = getBestMove(board, player, depth);

				if (bestMove != null) {
					System.out.print("L'IA à joué : ");
					for (Move move : bestMove) {
						board.applyMove(move);

						String from = formatLocationToText(move.getFrom());
						String to = formatLocationToText(move.getTo());
						System.out.println(String.format("(%s-%s)", from, to));
					}

					System.out.println("");

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

		Checkers.log(pawnColor.toString());
		List<EncapsuleMove> possibleMoves = board.getAllMoves(pawnColor);
		Checkers.log("possibleMoves : " + possibleMoves);
		EncapsuleMove bestMove = null;
		int bestScore = Integer.MIN_VALUE;

		for (EncapsuleMove encapsuleMove : possibleMoves) {
			Board newBoard = board.copy();

			Checkers.log("_________________________");
//			newBoard.showBoard();

			Checkers.log("encapsuleMove : " + encapsuleMove);
			for (Move move : encapsuleMove) {
				newBoard.applyMove(move);
			}

			int score = minMax(newBoard, depth, false);
			Checkers.log("Score : " + score);

			if (score > bestScore) {
				bestScore = score;
				bestMove = encapsuleMove;
			}
		}

		Checkers.log("bestMove : " + bestMove);
		return bestMove;
	}

	public int evaluate(Board board) {

		int score = 0;

		for (int y = 0; y < Board.BOARD_SIZE; y++) {
			for (int x = 0; x < Board.BOARD_SIZE; x++) {

				Pawn pawn = board.getPawn(x, y);

				if (pawn != null) {
					List<Move> listCapture = board.getCaptureMove(x, y, new ArrayList<>());
					if (pawn.getPawnColor() == PawnColor.BLACK) {
						if (pawn.isQueen())
							score += 3;
						else
							score += 1;

						score += listCapture.size();
					} else {
						if (pawn.isQueen())
							score -= 3;
						else
							score -= 1;

						score -= listCapture.size();
					}
				}
			}
		}

		return score;
	}

	public int minMax(Board board, int depth, boolean isMaxPlayer) {

		Checkers.log("Depth : " + depth);
//		board.showBoard();
		if (depth == 0 || board.isGameOver()) {
			int result = evaluate(board);
			Checkers.log("Result : " + result);
			return result;
		}

		List<EncapsuleMove> moves = board.getAllMoves(isMaxPlayer ? PawnColor.BLACK : PawnColor.WHITE);

		// Aucun mouvement défaite du joueur
		if (moves.isEmpty()) {
			return isMaxPlayer ? Integer.MIN_VALUE : Integer.MAX_VALUE;
		}

		int bestScore = isMaxPlayer ? Integer.MIN_VALUE : Integer.MAX_VALUE;

		for (EncapsuleMove encapsuleMove : moves) {

			Board newBoard = board.copy();

			for (Move move : encapsuleMove) {
				newBoard.applyMove(move);
			}

			int score = minMax(newBoard, depth - 1, !isMaxPlayer);

			if (isMaxPlayer) {
				bestScore = Math.max(bestScore, score);
			} else {
				bestScore = Math.min(bestScore, score);
			}
		}

		return bestScore;
	}



}
