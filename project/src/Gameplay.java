import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Gameplay extends JPanel implements KeyListener, ActionListener {

	private final int ballRadius;
	private final int ballSpeed;
	private final int paddlePositionY;
	private final int paddleLength;
	private final int paddleHeight;
	private final int paddleSpeed;
	private boolean playing;
	private int score;
	private int ballPositionX;
	private int ballPositionY;
	private int ballDirectionX;
	private int ballDirectionY;
	private int paddlePositionX;
	private int numBricks;
	private Map map;

	public Gameplay() {
		ballRadius = 20;
		ballSpeed = 3;
		paddleLength = 90;
		paddleHeight = 10;
		paddlePositionY = 530;
		paddleSpeed = 20;
		startGame();
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		Timer timer = new Timer(1, this);
		timer.start();
	}

	@Override
	public void paint(Graphics graphics) {

		graphics.setColor(App.white);
		graphics.fillRect(0, 0, App.windowWidth, App.windowHeight);

		graphics.setColor(App.green);
		graphics.fillOval(ballPositionX, ballPositionY, ballRadius, ballRadius);

		graphics.setColor(App.gray);
		graphics.fillRoundRect(paddlePositionX, paddlePositionY, paddleLength, paddleHeight, paddleLength / 10, paddleLength / 10);

		drawLabel(graphics, String.valueOf(score), App.gray, 25, App.windowHeight / 20);

		map.draw((Graphics2D) graphics);

		if (numBricks <= 0) {
			endGame(graphics, "Win", App.green);
		}

		if (ballPositionY >= App.windowHeight) {
			endGame(graphics, "Lose", App.red);
		}

		graphics.dispose();

	}

	@Override
	public void keyPressed(KeyEvent event) {

		if (event.getKeyCode() == KeyEvent.VK_RIGHT) {
			if (paddlePositionX >= App.windowWidth - paddleLength - 25) {
				paddlePositionX = App.windowWidth - paddleLength - 25;
			} else {
				moveRight();
			}
		}

		if (event.getKeyCode() == KeyEvent.VK_LEFT) {
			if (paddlePositionX <= 5) {
				paddlePositionX = 5;
			} else {
				moveLeft();
			}
		}

		if ((event.getKeyCode() == KeyEvent.VK_ENTER) && (!playing)) {
			startGame();
			repaint();
		}

	}

	@Override
	public void actionPerformed(ActionEvent event) {

		if (!playing) {
			return;
		}

		if (new Rectangle(ballPositionX, ballPositionY, ballRadius, ballRadius).intersects(new Rectangle(paddlePositionX, paddlePositionY, paddleLength / 3, paddleHeight))) {
			ballDirectionY = -ballDirectionY;
			ballDirectionX = -ballSpeed;
		} else if (new Rectangle(ballPositionX, ballPositionY, ballRadius, ballRadius).intersects(new Rectangle(paddlePositionX + (paddleLength / 3), paddlePositionY, paddleLength / 3, paddleHeight))) {
			ballDirectionY = -ballDirectionY;
			ballDirectionX = ballSpeed;
		} else if (new Rectangle(ballPositionX, ballPositionY, ballRadius, ballRadius).intersects(new Rectangle(paddlePositionX + (paddleLength / 3) + (paddleLength / 3), paddlePositionY, paddleLength / 3, paddleHeight))) {
			ballDirectionY = -ballDirectionY;
		}

		check:
		for (int row = 0; row < map.map.length; row++) {
			for (int column = 0; column < map.map[0].length; column++) {
				if (map.map[row][column] > 0) {
					int brickX = column * map.brickWidth + 80;
					int brickY = row * map.brickHeight + 50;
					Rectangle brick = new Rectangle(brickX, brickY, map.brickWidth, map.brickHeight);
					Rectangle ball = new Rectangle(ballPositionX, ballPositionY, ballRadius, ballRadius);
					if (ball.intersects(brick)) {
						map.setBrickValue(row, column, 0);
						score = score + 10;
						numBricks--;
						if ((ballPositionX <= brick.x) || (ballPositionX + ballRadius >= brick.x + brick.width)) {
							ballDirectionX = -ballDirectionX;
						} else {
							ballDirectionY = -ballDirectionY;
						}
						break check;
					}
				}
			}
		}

		ballPositionX += ballDirectionX;
		ballPositionY += ballDirectionY;

		if (ballPositionX <= 0) {
			ballDirectionX = -ballDirectionX;
		}

		if (ballPositionY <= 0) {
			ballDirectionY = -ballDirectionY;
		}

		if (ballPositionX >= 670) {
			ballDirectionX = -ballDirectionX;
		}

		repaint();

	}

	private void startGame() {

		playing = true;
		score = 0;

		int minBallPositionX = 50;
		int maxBallPositionX = App.windowWidth - ballRadius - minBallPositionX;
		ballPositionX = minBallPositionX + (int) (Math.random() * ((maxBallPositionX - minBallPositionX) + 1));
		ballPositionY = (App.windowHeight / 2) + 50;
		int direction = Math.random() < 0.5 ? 1 : -1;
		ballDirectionX = ballSpeed * direction;
		ballDirectionY = -ballSpeed;
		paddlePositionX = (App.windowWidth / 2) - (paddleLength / 2);

		int minRows = 3;
		int maxRows = 5;
		int minColumns = 10;
		int maxColumns = 13;
		int rows = minRows + (int) (Math.random() * ((maxRows - minRows) + 1));
		int columns = minColumns + (int) (Math.random() * ((maxColumns - minColumns) + 1));
		numBricks = rows * columns;
		map = new Map(rows, columns);

	}

	private void endGame(Graphics graphics, String result, Color color) {
		playing = false;
		ballDirectionX = ballDirectionY = 0;
		drawLabel(graphics, "You " + result, color, 30, App.windowHeight / 2);
		drawLabel(graphics, "Press Enter to Restart", App.gray, 20, (App.windowHeight / 2) + 50);
	}

	public void moveRight() {
		playing = true;
		paddlePositionX += paddleSpeed;
	}

	public void moveLeft() {
		playing = true;
		paddlePositionX -= paddleSpeed;
	}

	public void drawLabel(Graphics graphics, String text, Color color, int size, int y) {
		Font font = new Font("SansSerif", Font.BOLD, size);
		graphics.setColor(color);
		graphics.setFont(font);
		FontMetrics metrics = graphics.getFontMetrics(font);
		Rectangle rectangle = new Rectangle(0, 0, App.windowWidth, App.windowHeight);
		int x = rectangle.x + (rectangle.width - metrics.stringWidth(text)) / 2;
		graphics.setFont(font);
		graphics.drawString(text, x, y);
	}

	@Override
	public void keyReleased(KeyEvent event) {

	}

	@Override
	public void keyTyped(KeyEvent event) {

	}

}
