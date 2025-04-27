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
	
	private boolean isQueen;
	private PawnColor pawnColor;

	public Pawn(PawnColor color) {
		super();
		this.isQueen = false;
		this.pawnColor = color;
	}

	public PawnColor getPawnColor() {
		return pawnColor;
	}

	public boolean isQueen() {
		return isQueen;
	}

	@Override
	public String toString() {
		return "Pawn [isQueen=" + isQueen + ", isWhite=" + pawnColor + "]";
	}


}
