package chess;

import java.awt.Point;

public class Move {
    private int xFrom, yFrom, xTo, yTo;

	public Move(int xFrom, int yFrom, int xTo, int yTo) {
		this.xFrom = xFrom;
		this.yFrom = yFrom;
		this.xTo = xTo;
		this.yTo = yTo;
	}
	
	public Move(Location from, Location to) {
		this(from.getX(), from.getY(), to.getX(), to.getY());
	}
	
	public Location getMiddle() {

		int xMiddle = (xFrom + xTo) / 2;
		int yMiddle = (yFrom + yTo) / 2;
		
		double distance = new Point(xFrom, yFrom).distance(new Point(xTo,yTo)); 
		if(Math.floor(distance) == 1) {
			return null;
		}
		
		return new Location(xMiddle, yMiddle);
	}
	
	public static Location getMiddle(int xFrom, int yFrom, int xTo, int yTo) {
		int xMiddle = (xFrom + xTo) / 2;
		int yMiddle = (yFrom + yTo) / 2;
		
		return new Location(xMiddle, yMiddle);
	}
	
	public Location getFrom() {
		return new Location(xFrom, yFrom);
	}
	
	public Location getTo() {
		return new Location(xTo, yTo);
	}

	public int getXFrom() {
		return xFrom;
	}

	public void setXFrom(int xfrom) {
		this.xFrom = xfrom;
	}

	public int getYFrom() {
		return yFrom;
	}

	public void setYFrom(int yFrom) {
		this.yFrom = yFrom;
	}

	public int getXTo() {
		return xTo;
	}

	public void setXTo(int xTo) {
		this.xTo = xTo;
	}

	public int getYTo() {
		return yTo;
	}

	public void setYTo(int yTo) {
		this.yTo = yTo;
	}

	@Override
	public String toString() {
		return "Move [xFrom=" + xFrom + ", yFrom=" + yFrom + ", xTo=" + xTo + ", yTo=" + yTo + "]";
	}
	
	
}
