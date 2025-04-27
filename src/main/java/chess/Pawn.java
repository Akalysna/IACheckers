package chess;

public class Pawn {

	enum PawnColor {
		WHITE("O"), BLACK("X");

		private String sign;

		private PawnColor(String sign) {
			this.sign = sign;
		}

		public String getSign() {
			return sign;
		}
	}

	private Location location;
	private boolean isQueen;
	private PawnColor pawnColor;

	public Pawn(Location location, PawnColor color) {
		super();
		this.location = location;
		this.isQueen = false;
		this.pawnColor = color;
	}

	public PawnColor getPawnColor() {
		return pawnColor;
	}

	public Location getLocation() {
		return location;
	}
	
	public boolean isQueen() {
		return isQueen;
	}

	@Override
	public String toString() {
		return "Pawn [location=" + location + ", isQueen=" + isQueen + ", isWhite=" + pawnColor + "]";
	}


}
