package Auto.pression;

import java.awt.Graphics;

public class Branch {
	int[] goesTo;
	String word;
	int x;
	int y;
	int width;
	int height;
	int endWhen = -1;

	public Branch(int x, int y, String word) {
		this.x = x;
		this.y = y;
		this.word = word;

		goesTo = new int[0];

		width = (word.length() * 12) + 12;
		height = 30;
	}

	public Branch(int x, int y, String word, int[] goesTo) {
		this.x = x;
		this.y = y;
		this.word = word;
		this.goesTo = goesTo;

		goesTo = new int[0];

		width = (word.length() * 12) + 12;
		height = 30;
	}

	public Branch(int x, int y, String word, int[] goesTo, int endWhen) {
		this.x = x;
		this.y = y;
		this.word = word;
		this.goesTo = goesTo;
		this.endWhen = endWhen;

		goesTo = new int[0];

		width = (word.length() * 12) + 12;
		height = 30;
	}

	void addLetter(String cha) {
		StringBuilder builder = new StringBuilder();
		for (int l = 0; l < word.length(); l++) {
			builder.append(word.charAt(l));
		}
		builder.append(cha);
		word = builder.toString();
		width += 12;
	}

	void addInt(int in) {
		endWhen *= 10;
		endWhen -= in;
	}

	void removeInt() {
		endWhen -= endWhen % 10;
		endWhen /= 10;
	}

	void removeLetter() {
		if (word.length() > 0) {
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < word.length() - 1; i++) {
				builder.append(word.charAt(i));
			}
			word = builder.toString();
			width -= 12;
		}
	}

	void drawLetters(Graphics g) {

	}

	int getX() {
		return x;
	}

	int getY() {
		return y;
	}

	int getW() {
		return width;
	}

	int getH() {
		return height;
	}

	String getWord() {
		return word;
	}

	void setX(int x) {
		this.x = x;

	}

	void setY(int y) {
		this.y = y;
	}

	void addGoTo(int in) {
		System.out.println("append");
		goesTo = Panel.appendIntAr(goesTo, in);
	}

	int[] getGoesTo() {
		return goesTo;
	}

	void setGoesTo(int[] ngt) {
		goesTo = ngt;
	}

	void setGoesToInt(int place, int newInt) {
		goesTo[place] = newInt;
	}

	int getCentX() {
		int centX = ((2 * x) + width) / 2;
		return centX;
	}

	int getCentY() {
		int centY = ((2 * y) + height) / 2;
		return centY;
	}

	int[] getCent() {
		int centX = ((2 * x) + width) / 2;
		int centY = ((2 * y) + height) / 2;
		return new int[] { centX, centY };
	}

	int getEndWhen() {
		return endWhen;
	}
}
