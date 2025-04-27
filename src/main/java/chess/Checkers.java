package chess;

import java.util.Scanner;

import chess.Pawn.PawnColor;

public class Checkers {
	
	PawnColor playerColorTurn;
	private boolean endGame = false;
	
	public Checkers(PawnColor firstColor) {
		this.playerColorTurn = firstColor;
	}

	public static void main(String[] args) {
		
		Board board = new Board(PawnColor.WHITE); 
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
	
	

}
