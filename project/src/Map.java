import java.awt.*;

public class Map {

	public int[][] map;
	public int brickWidth;
	public int brickHeight;

	public Map(int rows, int columns) {
		map = new int[rows][columns];
		for (int row = 0; row < map.length; row++) {
			for (int column = 0; column < map[0].length; column++) {
				map[row][column] = 1;
			}
		}
		brickWidth = 540 / columns;
		brickHeight = 150 / rows;
	}

	public void draw(Graphics2D graphics) {
		for (int row = 0; row < map.length; row++) {
			for (int column = 0; column < map[0].length; column++) {
				if (map[row][column] > 0) {
					graphics.setColor(App.blue);
					graphics.fillRoundRect(column * brickWidth + 80, row * brickHeight + 50, brickWidth, brickHeight, 10, 10);
					graphics.setStroke(new BasicStroke(3));
					graphics.setColor(App.white);
					graphics.drawRoundRect(column * brickWidth + 80, row * brickHeight + 50, brickWidth, brickHeight, 10, 10);
				}
			}
		}
	}

	public void setBrickValue(int row, int column, int value) {
		map[row][column] = value;
	}

}
