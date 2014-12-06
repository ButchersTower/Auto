package Auto.leaf;

import javax.swing.JFrame;

public class Leaf {
	public Leaf() {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(new Panel());
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setTitle("Leaf");
	}

	public static void main(String[] args) {
		new Leaf();
	}
}
