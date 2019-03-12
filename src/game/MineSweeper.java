package game;

import javax.swing.JFrame;

public class MineSweeper extends JFrame {
	private static final long serialVersionUID = 8506978258133167008L;

	private Grid _grid;

	public MineSweeper() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(600, 600);
		this._grid = new Grid(10, 10);
		this.setContentPane(_grid);
	}
}