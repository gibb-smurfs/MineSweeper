package game;

import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * The playfield component of MineSweeper
 * 
 * @author Matteias
 */
public class Grid extends JPanel implements MouseListener {
	private static final long serialVersionUID = -1774185334835330786L;

	/**
	 * The grid cells
	 */
	private Cell[][] _cells;

	/**
	 * The grid height
	 */
	private int _height;

	/**
	 * The grid width
	 */
	private int _width;

	/**
	 * The grid lock for the mouse listener
	 */
	private ReentrantLock lock = new ReentrantLock();

	/**
	 * RNG
	 */
	private Random _rand = new Random();

	/**
	 * Generates a grid with the specified height and width
	 * 
	 * @param height The grids height
	 * @param width  The grid width
	 */
	public Grid(int height, int width) {
		this._height = height;
		this._width = width;
		this.setLayout(new GridLayout(this._height, this._width));
		this.generateGrid();
	}

	/**
	 * Generates the playfield
	 */
	private void generateGrid() {
		this._cells = new Cell[this._height][this._width];
		boolean[][] mines = this.generateMineGrid(this._height, this._width);

		for (int i = 0; i < this._height; i++) {
			for (int j = 0; j < this._width; j++) {
				if (mines[i][j]) {
					this._cells[i][j] = new Cell(CellType.Mine, i, j);
				} else {
					int neighborCount = this.countNeighboringMines(mines, i, j);
					if (neighborCount > 0) {
						this._cells[i][j] = new Cell(CellType.Hint, i, j);
						this._cells[i][j].setHintValue(neighborCount);
					} else {
						this._cells[i][j] = new Cell(CellType.Void, i, j);
					}
				}

				this._cells[i][j].addMouseListener(this);
				this.add(this._cells[i][j]);
			}
		}
	}

	/**
	 * Recursively activates the directly neighbouring cells if a void cell is
	 * activated
	 * 
	 * @param c The cell whose neighbours are checked
	 */
	private void updateNeighbors(Cell c) {
		if (c.getCellType() == CellType.Void) {

			Cell[] neighborCells = this.getDirectNeighborCells(c);

			for (int i = 0; i < neighborCells.length; i++) {
				if (!neighborCells[i].isActive()) {
					neighborCells[i].setActive();
					if (neighborCells[i].getCellType() == CellType.Void) {
						this.updateNeighbors(neighborCells[i]);
					}
				}
			}
		}
	}

	/**
	 * Gets an array of directly neighbouring cells (edge-to-edge)
	 * 
	 * @param c The cell
	 * @return Returns the array of directly neighbouring cells
	 */
	private Cell[] getDirectNeighborCells(Cell c) {
		ArrayList<Cell> cellList = new ArrayList<Cell>();
		int coordX = c.getCoordinateX();
		int coordY = c.getCoordinateY();

		if (coordX > 0)
			cellList.add(this._cells[coordX - 1][coordY]);
		if (coordX < this._cells.length - 1)
			cellList.add(this._cells[coordX + 1][coordY]);
		if (coordY > 0)
			cellList.add(this._cells[coordX][coordY - 1]);
		if (coordY < this._cells[coordX].length - 1)
			cellList.add(this._cells[coordX][coordY + 1]);

		return cellList.toArray(new Cell[cellList.size()]);
	}

	/**
	 * Generates a grid of Mines, where true is considered to be a mine
	 * 
	 * @param height The grid height
	 * @param width  The grid width
	 * @return Returns a grid of mines, where true is considered to be a mine
	 */
	private boolean[][] generateMineGrid(int height, int width) {
		boolean[][] mineGrid = new boolean[height][width];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				mineGrid[i][j] = this.getRandomBoolean(10);
			}
		}

		return mineGrid;
	}

	/**
	 * Randomly returns true or false
	 * 
	 * @param truePercentage The probability of the method returning true
	 * @return Returns the random boolean
	 */
	private boolean getRandomBoolean(int truePercentage) {
		if (truePercentage < 1 || truePercentage > 99)
			throw new IllegalArgumentException("truePercentage must be in the range 1-99");
		return this._rand.nextInt(1000) <= truePercentage * 10;
	}

	/**
	 * Count how many mines are neighbouring the specified cell
	 * 
	 * @param grid   The mine grid
	 * @param cell_x The cells x coordinates
	 * @param cell_y The cells y coordinates
	 * @return Returns the number of neighbouring mines
	 */
	private int countNeighboringMines(boolean[][] grid, int cell_x, int cell_y) {
		int count = 0;

		for (int i = cell_x - 1; i < cell_x + 2; i++) {
			for (int j = cell_y - 1; j < cell_y + 2; j++) {
				if (i >= 0 && grid.length > i && j >= 0 && grid[i].length > j && grid[i][j]
						&& !(cell_y == i && cell_x == j)) {
					count++;
				}
			}
		}

		return count;
	}

	/**
	 * Shows a 'Game Over' message and activates all cells
	 */
	private void gameOver() {
		JOptionPane.showMessageDialog(null, "Boom! Game Over!");
		this.activateAllCells();
	}

	/**
	 * Shows a 'Game Won' message and activates all cells
	 */
	private void gameWon() {
		JOptionPane.showMessageDialog(null, "Congratulations! You Won!");
		this.activateAllCells();
	}

	/**
	 * Activates all cells on the grid
	 */
	private void activateAllCells() {
		for (int i = 0; i < this._cells.length; i++) {
			for (int j = 0; j < this._cells[i].length; j++) {
				this._cells[i][j].setActive();
				this._cells[i][j].removeMouseListener(this);
			}
		}
	}

	/**
	 * Checks if there are any non-mine cells left to activate
	 * 
	 * @return Returns true if there is at least one non-mine cell left to activate
	 */
	private boolean areCellsLeft() {
		for (int i = 0; i < this._cells.length; i++) {
			for (int j = 0; j < this._cells.length; j++) {
				if (!this._cells[i][j].isActive() && !(this._cells[i][j].getCellType() == CellType.Mine)) {
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		if (arg0.getButton() == 1) {
			lock.lock();

			Cell c = (Cell) arg0.getSource();
			if (c.getCellType() == CellType.Mine) {
				this.gameOver();
			} else {
				this.updateNeighbors(c);

				if (!this.areCellsLeft())
					this.gameWon();
			}
			c.removeMouseListener(this);

			lock.unlock();
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

}
