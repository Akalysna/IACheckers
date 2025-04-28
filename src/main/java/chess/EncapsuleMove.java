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
	
	/**
	 * Transforme la liste de mouvements en une liste de locations chainées.
	 * @return
	 */
	public List<Location> getChainedLocations(){
		List<Location> locations = new ArrayList<>();
		for(int i = 0; i < moves.size(); i++) {
			Move move = moves.get(i);
			locations.add(move.getFrom());
			if(i == moves.size() - 1) {
				locations.add(move.getTo());
			}
		}
		return locations;
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
