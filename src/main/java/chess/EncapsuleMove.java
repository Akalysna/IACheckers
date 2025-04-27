package chess;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EncapsuleMove implements Iterable<Move>{
	public List<Move> moves = new ArrayList<>(); 
	
	public EncapsuleMove(Move move) {
		moves.add(move);
	}
	
	public EncapsuleMove(List<Move> moves) {
		this.moves.addAll(moves);
	}
	
	public EncapsuleMove() {
	}

	@Override
	public Iterator<Move> iterator() {
		return moves.iterator();
	}

	@Override
	public String toString() {
		return "EncapsuleMove [moves=" + moves + "]";
	}
	
	
}
