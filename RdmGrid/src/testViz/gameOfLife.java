package testViz;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;


public class gameOfLife extends JApplet{
	private static final long serialVersionUID = 1L;
	
	cellClass cell;
	
	
	public void run() {
		Container contentWindow = getContentPane();
		cell = new cellClass();
		contentWindow.add(cell);
		
	}

}


class grid extends JComponent{ 
	private static final long serialVersionUID = 2L;
	
	int XSIZE = 500;
	int YSIZE = 500;
	private int row;
	private int col;
	private int size = 5;
	private cellClass c;
	private Dimension preferredSize = new Dimension(XSIZE, YSIZE);
	
	public void paint(Graphics a) {
		int x, y;
		for(x=0; x<row; x++){
			for(y=0; y<col; y++){
				if(c.grid[x][y] != 0){
					a.drawRect(x * size, y * size, 5, 5);
				}
			}
		}
		a.drawRect(0, 0, XSIZE, YSIZE);
		
	}
	
	public grid(cellClass newGrid, int newRow, int newCol, int newSize) {
		setMinimumSize(preferredSize);
		setMaximumSize(preferredSize);
		setPreferredSize(preferredSize);
		this.row = newRow;
		this.col = newCol;
		this.size = newSize;
		this.c = newGrid;
		
	}
	
	
}


class cellClass extends JPanel implements ActionListener{
	private static final long serialVersionUID = 3L;
	
	static final int ROW = 100;
	static final int COL = 100;
	static final int SIZE = 5;
	static final int min = 2;
	static final int max = 3;
	static final int birth = 3;
	public int genCount = 0;
	
	public int[][] grid;
	private int[][] nextGrid;
	
	private GridBagLayout gridBag = new GridBagLayout();
	private GridBagConstraints c = new GridBagConstraints();
	
	JLabel title;
	JLabel genCounter;
	JButton oneGen;
	JButton contPlay;
	JButton stop;
	public grid board;
	public boolean paused = true;
	public boolean canChange = true;
	
	
	
	cellClass() {
		grid = new int [ROW][COL];
		nextGrid = new int[ROW][COL];
		
		makeGrid(grid);
		
		setLayout(gridBag);
		
		title = new JLabel("Game of Life Applet");
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		c.insets = new Insets(2,0,0,0);
		c.anchor = GridBagConstraints.WEST;
		add(title);
		
		board = new grid(this,ROW,COL,SIZE);
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 1;
		gridBag.setConstraints(board, c);
		add(board);
		
		oneGen = new JButton("Move one Generation.");
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 1;
		gridBag.setConstraints(oneGen, c);
		add(oneGen);
		
		contPlay = new JButton("Play");
		c.gridx = 1;
		c.gridy = 3;
		c.gridwidth = 1;
		gridBag.setConstraints(contPlay, c);
		add(contPlay);
		
		stop = new JButton("Stop.");
		c.gridx = 2;
		c.gridy = 3;
		c.gridwidth = 1;
		gridBag.setConstraints(stop, c);
		add(stop);
		
		genCounter = new JLabel("Generation: 0");
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		gridBag.setConstraints(genCounter, c);
		add(genCounter);
		
		
	}
	
	class ButtonListener {
		public void addActionListener(ActionEvent e) throws InterruptedException {
			JButton source = (JButton)e.getSource();
			
			if(source == oneGen){
				nextGen();
			}
			if(source == contPlay){
				paused = false;
				canChange = false;
				while (paused = false) {
					nextGen();
					Thread.sleep(1000);
				}
			}
			if(source == stop) {
				paused = true;
				canChange = false;
			}
		}
	}
	
	public void mouseClicked(MouseEvent e){
		int xco = e.getX();
		int yco = e.getY();
		if((e.getComponent() == board) && (paused == true)){
			grid[xco/5][yco/5] = 1;
		}
	}
	
	public void makeGrid(int[][] emptyGrid) {
		int x, y;
		for(x = 0; x < ROW; x++){
			for(y = 0; y < COL; y++){
				emptyGrid[x][y] = 0;
			}
		}
	}
	
	public void nextGen() {
		getNextGen();
		board.repaint();
		genCount++;
		genCounter.setText("Generation: " + Integer.toString(genCount));		
	}
	
	public void getNextGen() {
		int x, y, neighbor;
		makeGrid(nextGrid);
		for(x = 0; x < ROW; x++){
			for(y=0; y<COL; y++){
				neighbor = calculate(x,y);
				
				if(grid[x][y] != 0){
					if((neighbor >= min) && (neighbor <= max)) {
						nextGrid[x][y] = neighbor;
					}
				}else {
					if(neighbor == birth){
						nextGrid[x][y] = birth;
					}
				}
			}
		}
		makeGrid(grid);
		copyGrid(nextGrid,grid);
	}
	
	public void copyGrid(int[][] source, int[][] newGrid) {
		int x, y;
		for(x=0; x<ROW; x++){
			for(y=0; y<COL; y++){
				newGrid[x][y] = source[x][y];
			}
		}
	}
	
	private int calculate(int x, int y){
		int a, b, total;
		
		total = (grid[x][y]);
		for (a = -1; a<= 1; a++) {
			for (b = -1; b <= 1; b++){
				if(grid[(ROW + (x + a)) % ROW][(COL + (y + b)) % COL] != 0) {
					total++;
				}
			}
		}
		return total;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}