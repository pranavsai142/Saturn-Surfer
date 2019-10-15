import javax.swing.*;
import java.awt.*;

public class saturnSurfer {
	public static void main(String[] args) {
		//Create jpeg canvas
		JFrame frame = new JFrame();
		frame.setTitle("saturn surfer");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Game game = new Game();
		game.setFocusable(true);
		game.requestFocusInWindow();
		
		frame.setLayout(new BorderLayout());
		frame.add(game, BorderLayout.CENTER);
		
		
		frame.pack();
		frame.setResizable(true);
		frame.setVisible(true);
	}
}