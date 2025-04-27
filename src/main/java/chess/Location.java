package chess;

public class Location {

	private int x;
	private int y;

	public Location() {
		// TODO Auto-generated constructor stub
	}

	public Location(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	@Override
	public boolean equals(Object obj) {
		Location point = (Location) obj;
		return super.equals(obj) && x == point.x && y == point.y;
	}

	@Override
	public String toString() {
		return "[x=" + x + ", y=" + y + "]";
	}
	
	
}
