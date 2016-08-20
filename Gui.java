import java.awt.Button;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Gui extends JPanel {
	private static final long serialVersionUID = 1L;

	// All the possible images we could represent in the GUI
	private BufferedImage blank, blocked, start, finish, move;
	
	private Point[][] map;
	
	public Gui() {
		initComponents();
		loadImages();
		map = Pathfinding.getMapString();
	}
	
	// Creating our load and solve buttons on the GUI
	private void initComponents() {
		setLayout(null);
		
		Button load = new Button("Load");
		load.setBounds(350, 10, 50, 30);
		load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				readMapFromFile();
				map = Pathfinding.getMapString();
			}
			
		});
		add(load);
		
		Button solve = new Button("Solve");
		solve.setBounds(350, 50, 50, 30);
		solve.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Pathfinding.findPath();
				updateMap();
			}
			
		});
		add(solve);
		
	}
	
	// Reads the map from a text file called map.txt and sets the information
	// such as length and width of the map
	private void readMapFromFile() {
		File in = new File("map.txt");
		String s = "";
		int l = 0, w = 0;
		try {
			Scanner input = new Scanner(in);
			while (input.hasNextLine()) {
				String next = input.nextLine();
				s += next;
				w++;
				if (w == 1) l = next.length();
			}
			input.close();
			System.out.println(s);
			System.out.println("Length: " + l + " width: " + w);
		} catch (FileNotFoundException e) {
			System.out.println("Map file not found!");
		}
		
		Pathfinding.pts = new Point[l][w];
		Pathfinding.length = w; Pathfinding.width = l;
		Pathfinding.setMap(s);
	}

	// Load all our images into the BufferedReaders
	private void loadImages() {
		try {
			blank = ImageIO.read(new File("blank.png"));
			blocked = ImageIO.read(new File("blocked.png"));
			start = ImageIO.read(new File("start.png"));
			finish = ImageIO.read(new File("finish.png"));
			move = ImageIO.read(new File("move.png"));
			
			System.out.println("Successfully loaded images!");
		} catch (IOException e) {
			Logger.getLogger(Gui.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	private void updateMap() {
		for (int i = 0; i < Pathfinding.closedList.size(); i++) {
			map[Pathfinding.closedList.get(i).getX()][Pathfinding.closedList.get(i).getY()].setDetails("z");
		}
	}
	// THE PAINT COMPONENTS
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		repaint();
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		drawMap(g);
	}
	
	// Draw the map onto the screen. We check to see what each point is and draw the respective
	// image in that position.
	private void drawMap(Graphics g) throws ArrayIndexOutOfBoundsException {
		Graphics2D g2d = (Graphics2D)g;
		
		int drawX = 10, drawY = 10;
		
		for (int i = 0; i < Pathfinding.length; i++) {
			for (int j = 0; j < Pathfinding.width; j++) {
				if (map[i][j].getDetails().compareTo("x") == 0) {
					g2d.drawImage(start, drawX, drawY, 30, 30, null, null);
				} else if (map[i][j].getDetails().compareTo("y") == 0) {
					g2d.drawImage(finish, drawX, drawY, 30, 30, null, null);
				} else if (map[i][j].getDetails().compareTo("1") == 0) {
					g2d.drawImage(blocked, drawX, drawY, 30, 30, null, null);
				} else if (map[i][j].getDetails().compareTo("z") == 0) {
					g2d.drawImage(move, drawX, drawY, 30, 30, null, null);
				} else {
					g2d.drawImage(blank, drawX, drawY, 30, 30, null, null);
				}
				drawX += 30;
			}
				
    		drawY += 30;
    		drawX = 10;
		}
	}
}
