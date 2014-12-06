package Auto.stem;

import javax.swing.JFrame;


public class Stem extends JFrame {
	public Stem() {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(new Panel());
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setTitle("Stem");
	}

	public static void main(String[] args) {
		new Stem();
	}
}
