package game;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;

/**
 * The play cell component of MineSweeper
 * 
 * @author Matteias
 */
public class Cell extends JButton implements MouseListener {
	private static final long serialVersionUID = 8826412260036077288L;

	/**
	 * The cells type
	 */
	private CellType _cellType;

	/**
	 * The hint value stored in the cell
	 */
	private int _hintValue;

	/**
	 * True if the cell is active
	 */
	private boolean _isActive = false;

	/**
	 * True if the cell is flagged
	 */
	private boolean _isFlagged = false;

	/**
	 * The cells x-coordinate on the grid
	 */
	private int _coordinateX;

	/**
	 * The cells y-coordinate on the grid
	 */
	private int _coordinateY;

	/**
	 * Generates a cell with the specified properties
	 * 
	 * @param cellType The cell type
	 * @param coordX   The cells x-coordinates on the grid
	 * @param coordY   The cells y-coordinates on the grid
	 */
	public Cell(CellType cellType, int coordX, int coordY) {
		this._coordinateX = coordX;
		this._coordinateY = coordY;
		this._cellType = cellType;
		this.addMouseListener(this);
	}

	/**
	 * Sets the hint value for hint cells
	 * 
	 * @param value The hint value
	 */
	public void setHintValue(int value) {
		_hintValue = value;
	}

	/**
	 * Flips the cell and shows the hidden values
	 */
	public void setActive() {
		if (!this._isActive) {
			this.setEnabled(false);
			this.removeMouseListener(this);
			this._isActive = true;
			this._isFlagged = false;
			this.setBackground(this.getNextBackgroundColor());

			if (this._hintValue > 0) {
				this.setText(Integer.toString(this._hintValue));
			}

			this._isActive = true;

		}
	}

	/**
	 * Checks if the cell is active
	 * 
	 * @return Returns true if the cell is active
	 */
	public boolean isActive() {
		return this._isActive;
	}

	/**
	 * Toggles the flag on an inactive cell
	 */
	public void toggleFlag() {
		if (!this._isActive) {
			this._isFlagged = !this._isFlagged;
			this.setBackground(this.getNextBackgroundColor());
		}
	}

	/**
	 * Gets the background color for the current state
	 * 
	 * @return Returns the background color
	 */
	private Color getNextBackgroundColor() {
		if (this._isActive) {
			switch (this._cellType) {
			case Void:
				return Color.blue;
			case Mine:
				return Color.red;
			case Hint:
				return Color.yellow;
			default:
				throw new IllegalStateException("Invalid Cell Type");
			}
		} else {
			if (_isFlagged)
				return Color.green;
			else
				return null;
		}
	}

	/**
	 * Gets the x-coordinate of the cell on the grid
	 * 
	 * @return Returns the x-coordinate of the cell on the grid
	 */
	public int getCoordinateX() {
		return this._coordinateX;
	}

	/**
	 * Gets the y-coordinate of the cell on the grid
	 * 
	 * @return Returns the y-coordinate of the cell on the grid
	 */
	public int getCoordinateY() {
		return this._coordinateY;
	}

	/**
	 * Gets the cell type
	 * 
	 * @return Returns the cell type
	 */
	public CellType getCellType() {
		return this._cellType;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getButton() == 1) {
			this.setActive();
		} else if (e.getButton() == 3) {
			this.toggleFlag();
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
