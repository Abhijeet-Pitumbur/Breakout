import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class App extends JFrame {

	public static Color white;
	public static Color red;
	public static Color green;
	public static Color blue;
	public static Color gray;
	public static int windowWidth;
	public static int windowHeight;

	public App() {

		white = Color.decode("#FFFFFF");
		red = Color.decode("#DB5860");
		green = Color.decode("#59A869");
		blue = Color.decode("#4D8AC9");
		gray = Color.decode("#6E6E6E");

		windowWidth = 700;
		windowHeight = 600;

		add(new Gameplay());
		setBounds(0, 0, windowWidth, windowHeight);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getResource("icon.png"))).getImage());
		setLocationRelativeTo(null);
		setResizable(false);
		setTitle("Breakout");
		setVisible(true);

	}

	public static void main(String[] args) {
		new App();
	}

}
