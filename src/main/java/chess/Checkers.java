package chess;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import chess.Pawn.PawnColor;

public class Checkers {

	PawnColor playerColorTurn;
	private boolean endGame = false;

	public Checkers(PawnColor firstColor) {
		this.playerColorTurn = firstColor;
	}

	public static void main(String[] args) {

		Board board = new Board();
//		board.showBoard();

		board.turn(new Location(1, 6), new Location(2, 5));
		board.turn(new Location(2, 3), new Location(3, 4));
		board.turn(new Location(7, 6), new Location(6, 5));
		board.turn(new Location(3, 4), new Location(3, 4));

	}


	public void gameLoop() {
		while (!endGame) {

		}
	}

	public void ask() {
		System.out.println("Au tour du joueur " + playerColorTurn.toString() + ".");
		System.out.println("Qu'elle pion déplacé ?");
		Scanner scanner = new Scanner(System.in);
		String fromPawnPos = scanner.nextLine();
		System.out.println("Où le déplacer ?");
		String toPos = scanner.nextLine();

	}
	
	public Move getBestMove(Board board, PawnColor pawnColor, int depth) {
		
		List<Move> possibleMoves = board.getAllMoves(pawnColor);
		
		Move bestMove = null; 
		int bestScore = Integer.MIN_VALUE; 
		
		for(Move move : possibleMoves) {
			Board newBoard = board.copy(); 
			newBoard.applyMove(move);
			
			int score = minMax(newBoard, depth, false);
			
			if(score > bestScore) {
				bestScore = score; 
				bestMove = move;
			}
		}
		
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

		List<Move> moves = board.getAllMoves(isIA ? PawnColor.BLACK : PawnColor.WHITE);

		// Aucun mouvement défaite du joueur
		if (moves.isEmpty()) {
			return isIA ? Integer.MIN_VALUE : Integer.MAX_VALUE;
		}

		int bestScore = isIA ? Integer.MIN_VALUE : Integer.MAX_VALUE;

		for (Move move : moves) {

			Board newBoard = board.copy();
			newBoard.applyMove(move);

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
