package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import chess.Pawn.PawnColor;
import util.ConsoleUtils;

public class Checkers {

	private PawnColor playerColorTurn;
	private Scanner scanner;
	ConsoleUtils consoleUtils;

	public Checkers(PawnColor firstColor) {
		this.playerColorTurn = firstColor;
		this.scanner = new Scanner(System.in);
		this.consoleUtils = new ConsoleUtils();
	}

	public static void main(String[] args) {

		Checkers checkers = new Checkers(PawnColor.WHITE);
		checkers.playGame(3);
	}

	/**
	 * Méthode pour jouer une partie de dames
	 * La méthode permet à un joueur humain et à une IA de jouer à tour de rôle.
	 * Le joueur humain joue avec la couleur spécifiée lors de la création de
	 * l'objet
	 * Checkers.
	 * 
	 * @param depth Profondeur de recherche pour l'IA
	 */
	public void playGame(int depth) {

		// Initialisation du plateau de jeu
		Board board = new Board();
		board.initBoard();

		// Affichage du plateau de jeu
		PawnColor player = playerColorTurn;
		board.showBoard();

		// Boucle de jeu
		// Le jeu continue tant que la partie n'est pas terminée
		while (!board.isGameOver()) {

			if (player.equals(PawnColor.WHITE)) {

				// Le joueur humain joue avec la couleur blanche
				playHumainMove(board, player);

			} else {

				// L'IA joue avec la couleur noire
				boolean isEndGame = playAIMove(board, player, depth);
				if (isEndGame)
					break;

			}

			// On affiche le plateau de jeu après chaque coup joué 
			// et on change de joueur
			board.showBoard();
			player = player.equals(PawnColor.BLACK) ? PawnColor.WHITE : PawnColor.BLACK;
		}

		System.out.println("La partie est terminé");

	}

	// ________________________________________________________

	/**
	 * Méthode pour jouer un coup de l'humain
	 * La méthode demande à l'utilisateur de choisir un mouvement
	 * et applique le mouvement sur le plateau de jeu.
	 * 
	 * @param board  Plateau de jeu
	 * @param player Couleur du joueur
	 * @return true si le mouvement a été appliqué, false sinon
	 */
	public boolean playHumainMove(Board board, PawnColor player) {

		// Le joueur humain joue avec la couleur blanche
		// On vérifie si le joueur humain a des mouvements obligatoires
		// Si le joueur humain a des mouvements obligatoires, on lui demande de choisir
		// parmi les mouvements possibles
		List<EncapsuleMove> list = board.getAllCaptureMove(player);

		if (!list.isEmpty()) {

			System.out.println("Choisi parmi les chemins suivant: ");

			// Affichage des mouvements possibles
			// On affiche les mouvements possibles sous forme de liste
			// Chaque mouvement est affiché sous la forme (a1-b2)
			// où a1 est la case de départ et b2 est la case d'arrivée
			for (int i = 0; i < list.size(); i++) {
				String format = consoleUtils.formatChainedLocations(list.get(i));
				System.out.println(String.format("%d: %s", i + 1, format));
			}

			int index = Integer.parseInt(scanner.nextLine()) - 1;

			for (Move move : list.get(index)) {
				board.applyMove(move);
			}

		} else {

			Location from = consoleUtils.obtainMoveFromPlayer(board, true, "Choisis ton coup");
			Location to = consoleUtils.obtainMoveFromPlayer(board, false, "Choisis une destination");
			System.out.println("");

			Move move = new Move(from, to);
			board.applyMove(move);
		}

		return true;
	}

	/**
	 * Méthode pour jouer un coup de l'IA
	 * La méthode utilise l'algorithme MinMax pour évaluer les coups possibles
	 * 
	 * @param board  Plateau de jeu
	 * @param player Couleur du joueur
	 * @param depth  Profondeur de recherche
	 * @return
	 */
	public boolean playAIMove(Board board, PawnColor player, int depth) {

		System.out.println("Tour de l'IA");
		EncapsuleMove bestMove = getBestMove(board, player, depth);

		if (bestMove != null) {
			System.out.print("L'IA à joué : ");
			for (Move move : bestMove) {
				board.applyMove(move);
			}

			System.out.println(consoleUtils.formatChainedLocations(bestMove));
			System.out.println("");

			return false;

		} else {

			System.out.println("L'IA ne peux plus jouer");
			return true;
		}
	}

	// ________________________________________________________

	/**
	 * Méthode pour obtenir le meilleur coup possible pour l'IA
	 * La méthode utilise l'algorithme MinMax pour évaluer les coups possibles
	 * et choisir le meilleur coup pour l'IA.
	 * 
	 * @param board     Plateau de jeu
	 * @param pawnColor Couleur du pion de l'IA
	 * @param depth     Profondeur de recherche
	 * @return Meilleur coup possible
	 */
	public EncapsuleMove getBestMove(Board board, PawnColor pawnColor, int depth) {

		// Récupérer tous les mouvements possibles pour le joueur actuel
		List<EncapsuleMove> possibleMoves = board.getAllPlayerMoves(pawnColor);

		// Si aucun mouvement n'est possible, retourner null
		EncapsuleMove bestMove = null;
		int bestScore = Integer.MIN_VALUE;

		// Parcourir tous les mouvements possibles
		for (EncapsuleMove encapsuleMove : possibleMoves) {

			// Pour chaque mouvement, créer un nouveau plateau de jeu
			// et appliquer le mouvement sur le nouveau plateau
			Board newBoard = board.copy();
			for (Move move : encapsuleMove) {
				newBoard.applyMove(move);
			}

			// Appeler la méthode MinMax sur le nouveau plateau avec une profondeur de
			// recherche
			// On inverse le joueur pour la recherche MinMax car on cherche à évaluer le
			// coup
			// du joueur adverse
			int score = minMax(newBoard, depth, false);

			// Mettre à jour le meilleur score et le meilleur mouvement
			// Si le score est meilleur que le meilleur score actuel, mettre à jour le
			// meilleur score
			if (score > bestScore) {
				bestScore = score;
				bestMove = encapsuleMove;
			}
		}

		// Retourner le meilleur mouvement trouvé
		return bestMove;
	}

	/**
	 * Méthode d'évaluation du score du plateau de jeu
	 * Le score est calculé en fonction du nombre de pions et de reines
	 * capturés par chaque joueur. Le score est positif pour le joueur noir et
	 * négatif pour le joueur blanc.
	 * 
	 * @param board Plateau du jeu
	 * @return
	 */
	public int evaluateScore(Board board) {

		int score = 0;

		// Parcours du plateau
		for (int y = 0; y < Board.BOARD_SIZE; y++) {
			for (int x = 0; x < Board.BOARD_SIZE; x++) {

				// Récupération d'un pion
				Pawn pawn = board.getPawn(x, y);

				if (pawn != null) {

					// Le facteur permettra d'additionné ou de soustraire au score selon la couleur
					// du pion
					// Si le pion n'est pas celui de l'IA alors le score diminue
					int factor = pawn.getPawnColor().equals(PawnColor.BLACK) ? 1 : -1;

					// Chaque pièces encore sur le plateau valent 1, les reines valent 3
					score += pawn.isQueen() ? factor * 3 : factor * 1;

					// Liste des pions pouvant être capturer par un pion
					// Chaque movement correspont a un pion capturé
					List<Move> listCapture = board.findBestCapturePath(pawn.getPawnColor(), x, y, new ArrayList<>());

					// On ajoute le nombre de pion capturer au score
					score += factor * listCapture.size();
				}
			}
		}

		return score;
	}

	/**
	 * Méthode de recherche du meilleur coup possible
	 * La méthode utilise l'algorithme MinMax pour évaluer les coups possibles
	 * et choisir le meilleur coup pour l'IA.
	 * 
	 * @param board Plateau de jeu
	 * @param depth Profondeur de recherche
	 * @return Score du meilleur coup
	 */
	public int minMax(Board board, int depth, boolean isMaxPlayer) {

		// Si la profondeur est nulle ou si le jeu est terminé, évaluer le score
		if (depth == 0 || board.isGameOver()) {
			return evaluateScore(board);
		}

		// Récupérer tous les mouvements possibles pour le joueur actuel
		// Si le joueur est le joueur max, on cherche les mouvements du joueur noir
		List<EncapsuleMove> moves = board.getAllPlayerMoves(isMaxPlayer ? PawnColor.BLACK : PawnColor.WHITE);

		// Aucun mouvement défaite du joueur
		if (moves.isEmpty()) {
			// Si le joueur est le joueur max, il a perdu
			// Sinon, il a gagné
			return isMaxPlayer ? Integer.MIN_VALUE : Integer.MAX_VALUE;
		}

		// Initialiser le meilleur score
		// Si le joueur est le joueur max, on cherche à maximiser le score
		int bestScore = isMaxPlayer ? Integer.MIN_VALUE : Integer.MAX_VALUE;

		// Parcourir tous les mouvements possibles
		for (EncapsuleMove encapsuleMove : moves) {

			// Pour chaque mouvement, créer un nouveau plateau de jeu
			Board newBoard = board.copy();

			// Appliquer le mouvement sur le nouveau plateau
			for (Move move : encapsuleMove) {
				newBoard.applyMove(move);
			}

			// Appeler la méthode MinMax sur le nouveau plateau avec une profondeur réduite
			// et inverser le joueur
			int score = minMax(newBoard, depth - 1, !isMaxPlayer);

			// Mettre à jour le meilleur score en fonction du joueur
			// Si le joueur est le joueur max, on cherche à maximiser le score
			// Sinon, on cherche à minimiser le score
			if (isMaxPlayer) {
				bestScore = Math.max(bestScore, score);
			} else {
				bestScore = Math.min(bestScore, score);
			}
		}

		// Retourner le meilleur score
		return bestScore;
	}

}
