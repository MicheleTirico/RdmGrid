package test;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
 
public class SquareGrid extends JPanel {
    SquareRx[][] squares;
    final int PAD = 20;
    final int ROWS = 10;
    final int COLS = 10;
 
    public SquareGrid() {
        addMouseListener(ml);
    }
 
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        if(squares == null) {
            initSquares();
        }
        // Draw squares.
        g2.setPaint(Color.blue);
        for(int i = 0; i < ROWS; i++) {
            for(int j = 0; j < COLS; j++) {
                squares[i][j].draw(g2);
            }
        }
    }
 
    private void initSquares() {
        squares = new SquareRx[ROWS][COLS];
        int w = getWidth();
        int h = getHeight();
        double xInc = (double)(w - 2*PAD)/COLS;
        double yInc = (double)(h - 2*PAD)/ROWS;
        for(int i = 0; i < ROWS; i++) {
            double y = PAD + i*yInc;
            for(int j = 0; j < COLS; j++) {
                double x = PAD + j*xInc;
                Rectangle2D.Double r =
                    new Rectangle2D.Double(x, y, xInc, yInc);
                squares[i][j] = new SquareRx(i, j, r);
            }
        }
    }
 
    private MouseListener ml = new MouseAdapter() {
        public void mousePressed(MouseEvent e) {
            Point p = e.getPoint();
            if(!isInGrid(p)) return;
            double xInc = (double)(getWidth() - 2*PAD)/COLS;
            double yInc = (double)(getHeight() - 2*PAD)/ROWS;
            int row = (int)((p.y-PAD)/yInc);
            int col = (int)((p.x-PAD)/xInc);
            System.out.println(squares[row][col]);
            boolean isSelected = squares[row][col].isSelected();
            squares[row][col].setSelected(!isSelected);
            repaint();
        }
    };
 
    private boolean isInGrid(Point p) {
        Rectangle r = getBounds();
        r.grow(-PAD, -PAD);
        return r.contains(p);
    }
 
    public static void main(String[] args) {
        SquareGrid test = new SquareGrid();
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(test);
        f.setSize(400,400);
        f.setLocation(100,100);
        f.setVisible(true);
        test.addComponentListener(test.cl);
    }
 
    private ComponentListener cl = new ComponentAdapter() {
        public void componentResized(ComponentEvent e) {
            squares = null;
            repaint();
        }
    };
}
 
class SquareRx {
    private final int row;
    private final int col;
    Rectangle2D.Double rect;
    Color color = new Color(140,200,160);
    Color bgColor = Color.orange;
    Color selColor = Color.red;
    private boolean selected = false;
 
    public SquareRx(int r, int c, Rectangle2D.Double rect) {
        row = r;
        col = c;
        this.rect = rect;
    }
 
    public void draw(Graphics2D g2) {
        g2.setPaint(selected ? selColor : bgColor);
        g2.fill(rect);
        g2.setPaint(color);
        g2.draw(rect);
    }
 
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
 
    public boolean isSelected() { return selected; }
 
    public String toString() {
        return "SQUARE[row:" + row + ", col:" + col +
                    ", selected:" + selected + "]";
    }
}