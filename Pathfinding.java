package Pathfinding;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Pathfinding extends JFrame {
	// I'll be using a 5x5 grid to practice, x = start, y = finish, z = move, 1 = blocked, 0 = movable

	// F = G + H
	// G = Cost from starting position to current square
	// H = Estimated total cost to destination square
	
	// openList = Potential next moves
	// closedList = Moves we have already tried
	// deadEnds = Moves that lead us to a dead-end

	private static final long serialVersionUID = 1L;
	
	public static ArrayList<Point> openList = new ArrayList<Point>();
	public static ArrayList<Point> closedList = new ArrayList<Point>();
	public static ArrayList<Point> deadEnds = new ArrayList<Point>();
	
	public static Pathfinding p;
	public static int length = 5, width = 5;
	
	static Point pts[][] = new Point[5][5];
	
	public static Point startPoint;
	public static Point endPoint;

	public Pathfinding() {
		initGui();
	}
	
	private void initGui() {
		setTitle("Pathfinder");
		setSize(460, 360);
		add(new Gui());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
	}
	
	public static void main(String[] args) {

		setMap( "x0010"
			  + "00010"
			  + "00001"
			  + "01101"
			  + "0y100");

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				p = new Pathfinding();
				p.setVisible(true);
			}
			
		});
	}
	
	public static void findPath() {
		/*
		 * 1.  Add the original position to the closed list
		 * 2.  Search for all movable surrounding squares (0)
		 * 2a. Calculate the F-score for each movable square
		 * 2b. If no movable squares, backstep and add it to deadend list
		 * 3.  Choose lowest F-score and set it as next move
		 * 3a. Return to step 2, repeat until destination reached
		 */
		
		System.out.println("Start point: " + startPoint.getCoords());
		System.out.println("End   point: " + endPoint.getCoords());
		System.out.println("Distance: " + startPoint.getDistance(endPoint));
		
		closedList.add(startPoint);
		int g = 0, h = 0, f = 0;
		Point currentPoint;
		
		int steps = 0;

		try {
			// Our main loop. It keeps cycling until a path is successfully found, or more than
			// 99 steps have been attempted (to prevent potential crashing/no path found)
			do {
				if (closedList.size() > 0) {
					currentPoint = closedList.get(closedList.size() - 1);
				} else {
					currentPoint = closedList.get(closedList.size());

				}
				Point upper = getUpperPoint(currentPoint);
				Point left = getLeftPoint(currentPoint);
				Point right = getRightPoint(currentPoint);
				Point bottom = getBottomPoint(currentPoint);
			
				g = steps;
				// We go through the surrounding tiles to see if they can be moved to
				// We check if it's movable (not a wall or solid object), not in the closed list, and not a dead end
				if (upper != null && upper.isMovable() && !closedList.contains(upper) && !deadEnds.contains(upper)) {
					openList.add(upper);
					h = upper.getDistance(endPoint);
					f = g + h;
					upper.setF(f);
				}
			
				if (left != null && left.isMovable() && !closedList.contains(left) && !deadEnds.contains(left)) {
					openList.add(left);

					h = left.getDistance(endPoint);
					f = g + h;
					left.setF(f);
				}
			
				if (right != null && right.isMovable() && !closedList.contains(right) && !deadEnds.contains(right)) {
					openList.add(right);

					h = right.getDistance(endPoint);
					f = g + h;
					right.setF(f);
				}
			
				if (bottom != null && bottom.isMovable() && !closedList.contains(bottom) && !deadEnds.contains(bottom)) {
					openList.add(bottom);

					h = bottom.getDistance(endPoint);
					f = g + h;
					bottom.setF(f);
				}
			
				// If we don't have any movable squares, backtrack 2 steps and add it to the restricted list
				// so that we don't try to go there again
				if (openList.size() == 0) {
					deadEnds.add(currentPoint);
					if (closedList.size() > 1) {
						currentPoint = closedList.get(closedList.size() - 2);
					} else {
						currentPoint = closedList.get(closedList.size() - 1);

					}
					
					closedList.remove(closedList.size() - 1);
					continue;
				}
			
				Point bestMove = findLowestScoredPoint();
				closedList.add(bestMove);
				openList.clear();

				steps++;
			} while (!reachedDestination() && steps < 99);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		outputPath();
	}
	
	// using my 5x5 grid above, setting up points that have (x, y) coordinates
	public static void setMap(String mapString) {
		
		openList.clear(); closedList.clear(); deadEnds.clear();
		String name = "";
		System.out.println("Map Size: " + mapString.length());

		int count = 0;
		System.out.println("length: " + length + " width: " + width);
		pts = new Point[length][width];
		
		for (int x = 0; x < length; x++) {
			for (int y = 0; y < width; y++) {
				name = mapString.substring(count, 1 + count);
				pts[x][y] = new Point(x, y, name);
				if (name.compareTo("x") == 0) startPoint = new Point(x, y, name);
				if (name.compareTo("y") == 0) endPoint = new Point(x, y, name);
				count++;
			}
		}
	}

	public static Point[][] getMapString() {
		return pts;
	}
	
	// The lowest score point is likely the next best move, so let's find it.
	// We start with 999 because nothing will ever (likely) exceed that, meaning the
	// first time we iterate it will be the "lowest"
	public static Point findLowestScoredPoint() {
		int low = 999;
		Point lowest = null;
		
		for (int x = 0; x < openList.size(); x++) {
			System.out.println(openList.get(x).getCoords());
			if (openList.get(x).getF() < low) {
				lowest = openList.get(x);
				low = openList.get(x).getF();
			} else if (openList.get(x).getF() == low) {
				lowest = openList.get(x);
				low = openList.get(x).getF();
			}
		}
		System.out.println("Chose: " + lowest.getCoords());
		return lowest;
	}
	
	// Checks to see if we've reached the destination, which is if the last item on the closed
	// list is "y"
	public static boolean reachedDestination() {
		if (closedList.size() > 0) {
			return closedList.get(closedList.size() - 1).getDetails().compareTo("y") == 0;
		}
		return false;
	}
	
	// Print out the path
	public static void outputPath() {
		for (int x = 1; x < closedList.size(); x++) {
			System.out.println("Move " + x + ": " + closedList.get(x).getCoords());
		}
	}
	
	// This is for easily getting the surrounding points
	public static Point getUpperPoint(Point startPoint) {
		// x + 0, y - 1
		int pointX = startPoint.getX();
		int pointY = startPoint.getY() - 1;
		
		if (pointY < 0) return null;
		return pts[pointX][pointY];
	}
	public static Point getRightPoint(Point startPoint) {
		// x + 1, y + 0
		int pointX = startPoint.getX() + 1;
		int pointY = startPoint.getY();
		
		if (pointX >= length) return null;
		return pts[pointX][pointY];
	}
	public static Point getBottomPoint(Point startPoint) {
		// x + 0, y + 1
		int pointX = startPoint.getX();
		int pointY = startPoint.getY() + 1;
		
		if (pointY >= width) return null;
		return pts[pointX][pointY];
	}
	public static Point getLeftPoint(Point startPoint) {
		// x - 1, y + 0
		int pointX = startPoint.getX() - 1;
		int pointY = startPoint.getY();
		
		if (pointX < 0) return null;
		return pts[pointX][pointY];
	}
}
