package chess;

import java.util.List;

public class EatingResult {
	public List<Location> eatingPawn;
	public List<Location> lastPawnPos;

	public EatingResult(List<Location> eatingPawn, List<Location> lastPawnPos) {
		super();
		this.eatingPawn = eatingPawn;
		this.lastPawnPos = lastPawnPos;
	}
}
