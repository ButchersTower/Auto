package Auto.pression;

import javax.swing.JFrame;

public class Pression extends JFrame {
	public Pression() {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(new Panel());
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setTitle("Pression");
	}

	public static void main(String[] args) {
		new Pression();
	}
}
