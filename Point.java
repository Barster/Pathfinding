public class Point {

	private int x;
	private int y;
	private String details;
	
	private int g, h, f;
	
	public Point(int x, int y, String details) {
		this.x = x;
		this.y = y;
		this.details = details;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public void setF(int f) {
		this.f = f;
	}
	public int getF() {
		return this.f;
	}
	
	public void setG(int g) {
		this.g = g;
	}
	public int getG() {
		return this.g;
	}
	
	public void setH(int h) {
		this.h = h;
	}
	public int getH() {
		return this.h;
	}
	
	public String getCoords() {
		return "(" + this.getX() + ", " + this.getY() + ")";
	}
	public String getDetails() {
		return this.details;
	}
	public void setDetails(String s) {
		this.details = s;
	}
	// just a simple solution for now. 0 mean movable, 1 means blocked
	public boolean isMovable() {
		return this.details.contains("0") || this.details.contains("y");
	}
	
	// d = sqrt([x2 - x2]^2 + [y2 - y1]^2
	public int getDistance(Point pointB) {
		double x = pointB.getX() - this.getX();
		double y = pointB.getY() - this.getY();
		
		return (int) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	}
}
