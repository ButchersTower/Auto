package Auto.pression;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Panel extends JPanel implements MouseListener, KeyListener,
		MouseMotionListener {
	// Clicking mouse draws the screen.

	int width = 600;
	int height = 400;

	Image[] txtAr;

	Thread thread;
	Image image;
	Graphics g;

	ArrayList<Branch> bList;

	int selectedBox = -1;

	boolean shiftP = false;
	boolean ctrlP = false;

	ReadAndWrite RnR;

	public Panel() {
		super();

		setPreferredSize(new Dimension(width, height));
		setFocusable(true);
		requestFocus();

		addKeyListener(this);
		addMouseListener(this);

		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		this.setSize(new Dimension(width, height));

		pStart();
	}

	/**
	 * Methods go below here.
	 * 
	 */

	public void pStart() {
		imageInit();
		bList = new ArrayList<Branch>();

		// Register for mouse events on blankArea and panel.
		addMouseMotionListener(this);
		addMouseMotionListener(this);

		RnR = new ReadAndWrite();

		System.out.println("CTRL + D: Deleted the selected box");
		System.out.println("CTRL + S: Moves selected box to .get(0)");
		System.out
				.println("CTRL + E: Edit the (endWhen) varaible of selected box");
		System.out.println("CTRL + B: Display coppyable code.");
		System.out.println("CTRL + A: Saves the tree to a .txt file");
		System.out.println("CTRL + L: Loads a tree from a .txt file");
	}

	void addBox(int x, int y) {
		bList.add(new Branch(x, y, "a"));
	}

	void drawTree() {
		System.out.println("drawTree");
		/**
		 * IF i move the drawing over to Branch() and this method calls them
		 * then the boxes would probably overlap better.
		 */
		// Draws the background
		g.setColor(Color.ORANGE);
		g.fillRect(0, 0, width, height);

		g.setColor(Color.BLACK);
		// Draw all lines
		for (int i = 0; i < bList.size(); i++) {
			// If they go anywhere.
			// Draw a line from the center of this box to the center the box
			// it goes to.
			// System.out.println("goesToLength: " +
			// bList.get(i).getGoesTo().length);
			for (int b = 0; b < bList.get(i).getGoesTo().length; b++) {
				g.drawLine(bList.get(i).getCentX(), bList.get(i).getCentY(),
						bList.get(bList.get(i).getGoesTo()[b]).getCentX(),
						bList.get(bList.get(i).getGoesTo()[b]).getCentY());
			}
		}

		// Draws the boxes and words inside
		for (int i = 0; i < bList.size(); i++) {
			g.setColor(Color.WHITE);
			g.fillRect(bList.get(i).getX(), bList.get(i).getY(), bList.get(i)
					.getW(), bList.get(i).getH());
			int wi = bList.get(i).getW();
			int hi = bList.get(i).getH();
			int xl = bList.get(i).getX();
			int yl = bList.get(i).getY();
			ArrayList<String> st = new ArrayList<String>();
			st.add(bList.get(i).getWord());
			txtBox(g, wi, hi, 0, xl + 2, yl + 6, st, false);
			ArrayList<String> numb = new ArrayList<String>();
			String number = Integer.toString(bList.get(i).getEndWhen());
			numb.add(number);
			txtBox(g, 1, xl + wi - 14, yl + 2, number);
		}
		// Draw an outline at the selectedBox
		if (selectedBox != -1) {
			g.setColor(Color.CYAN);
			g.drawRect(bList.get(selectedBox).getX() + 1, bList
					.get(selectedBox).getY() + 1,
					bList.get(selectedBox).getW() - 3, bList.get(selectedBox)
							.getH() - 3);
		}
		int numW = 1;
		if (selectedBox != -1) {
			if (bList.get(selectedBox).getEndWhen() > 9
					|| bList.get(selectedBox).getEndWhen() < 0) {
				numW = 2;
				if (bList.get(selectedBox).getEndWhen() > 99
						|| bList.get(selectedBox).getEndWhen() < -9) {
					numW = 3;
					if (bList.get(selectedBox).getEndWhen() > 999
							|| bList.get(selectedBox).getEndWhen() < -99) {
						numW = 4;
					}
				}
			}
		}

		if (editNum) {
			// draw a cyan rect around the numb.
			g.setColor(Color.BLUE);
			g.drawRect(bList.get(selectedBox).getX()
					+ bList.get(selectedBox).getW() - 16, bList
					.get(selectedBox).getY() + 1, (numW * 8), 11);
		}
	}

	public void txtBox(Graphics g, int wi, int hi, int font, int xl, int yl,
			ArrayList<String> st, boolean drwBkg) {
		// Need to code in minecraft font.
		// Needs a seperate converter.
		int[] lastLetterLoc = new int[2];
		int SRSBUISNESS = 0;
		if (drwBkg) {
			g.fillRect(xl, yl, wi, hi);
		} else {
		}
		int lineDrawnOn = 0;
		for (int stl = 0; stl < st.size(); stl++) {
			String[] words = st.get(stl).split("[ ]");

			int twi = 0, thi = 0;
			if (font == 0) {
				twi = 12;
				thi = 16;
			}
			if (font == 1) {
				twi = 6;
				thi = 8;
			}

			int lettersPerRow = (wi - (wi % twi)) / twi;

			int numLines = 0;
			int ghe = 0;
			while (ghe < words.length) {
				ghe = repeat(ghe, 0, words, lettersPerRow);
				numLines++;
			}
			// System.out.println("(" + a + ", " + b + ", " + c + ", " + d +
			// ")");
			int[] figures = new int[numLines];
			for (int i = 0; i < figures.length; i++) {
				if (i == 0) {
					figures[0] = repeat(0, 0, words, lettersPerRow);
				} else {
					figures[i] = repeat(figures[i - 1], 0, words, lettersPerRow);
				}
			}
			for (int i = 0; i < figures.length; i++) {
				for (int ii = 0; ii < i; ii++) {
					figures[i] -= figures[ii];
				}
			}
			int lettersDrawnOfThisTextBox = 0;
			int drawPlace = xl;
			int drawnWords = 0;
			// If figures.length = 1 and if figures[ig] == null then save the
			// letLoc as the left side of the text box.
			if (figures.length == 1) {
				// System.out.println("figures[0]: " + figures[0]);
			}
			// System.out.println("figures.l: " + figures.length);
			// this goes through all of the lines
			for (int ig = 0; ig < figures.length; ig++) {
				// System.out.println("figures[" + ig + "]: " + figures[ig]);
				// this ques up the words of a line
				for (int ih = 0; ih < figures[ig]; ih++) {
					// this draws individual words
					if (words[drawnWords].length() == 0) {
						// add letLoc.
						// System.out.println("ZERO LINE: ig[" + ig + "]   ih["
						// + ih + "]");
					}
					int[] j = converter(words[drawnWords]);
					for (int i = 0; i < j.length; i++) {
						// System.out.println("(" + a + ", " + b + ", " + c +
						// ", " + 0 + ")" + "lettersDrawn: " +
						// lettersDrawnOfThisTextBox);
						g.drawImage(txtAr[j[i]], drawPlace, yl + (ig * thi)
								+ (lineDrawnOn * thi), null);
						lettersDrawnOfThisTextBox++;
						SRSBUISNESS++;
						drawPlace += twi;
					}
					int countLetter = 0;
					for (int w = 0; w < words.length; w++) {
						countLetter += words[w].length() + 1;
					}
					countLetter--;
					if (SRSBUISNESS < countLetter) {
						SRSBUISNESS++;
						lettersDrawnOfThisTextBox++;
					}
					lastLetterLoc[0] = drawPlace;
					lastLetterLoc[1] = (yl + (ig * thi) + (lineDrawnOn * thi));
					drawPlace += twi;
					drawnWords++;
				}
				drawPlace = xl;
			}
			lineDrawnOn += figures.length;
			String removeSpaces = st.get(st.size() - 1);
			String opposite = new StringBuilder(removeSpaces).reverse()
					.toString();
			int endSpaces = 0;
			aLoop: for (char cee : opposite.toCharArray()) {
				if (cee == ' ') {
					endSpaces++;
				} else {
					break aLoop;
				}
			}
		}
	}

	public void txtBox(Graphics g, int font, int xl, int yl, String st) {
		// Need to code in minecraft font.
		// Needs a seperate converter.
		int twi = 0, thi = 0;
		if (font == 0) {
			twi = 12;
			thi = 16;
		}
		if (font == 1) {
			twi = 6;
			thi = 8;
		}

		int currentxl = xl;
		for (int l = 0; l < st.length(); l++) {
			if (font == 1) {
				int letter = McConverter(st.charAt(l));
				g.drawImage(mcNum[letter], currentxl, yl, null);
				currentxl += 6;
			}
		}

	}

	int McConverter(char cha) {
		int letter = -1;
		if (cha == '0') {
			letter = 0;
		} else if (cha == '1') {
			letter = 1;
		} else if (cha == '2') {
			letter = 2;
		} else if (cha == '3') {
			letter = 3;
		} else if (cha == '4') {
			letter = 4;
		} else if (cha == '5') {
			letter = 5;
		} else if (cha == '6') {
			letter = 6;
		} else if (cha == '7') {
			letter = 7;
		} else if (cha == '8') {
			letter = 8;
		} else if (cha == '9') {
			letter = 9;
		} else if (cha == '-') {
			letter = 10;
		}
		return letter;
	}

	int repeat(int a, int b, String[] words, int lettersPerRow) {
		if (a >= words.length) {
			return a;
		}
		if (b + words[a].length() <= lettersPerRow) {
			if (b + words[a].length() + 1 <= lettersPerRow) {
				b += words[a].length() + 1;
			} else {
				b += words[a].length();
			}
			a++;
			return repeat(a, b, words, lettersPerRow);
		} else {
			return a;
		}

	}

	public static int[] converter(String st) {
		int a = st.length();
		int[] nw = new int[a];

		for (int b = 0; b < a; b++) {
			if (st.charAt(b) == 'a') {
				nw[b] = 26;
			} else if (st.charAt(b) == 'A') {
				nw[b] = 0;
			} else if (st.charAt(b) == 'b') {
				nw[b] = 27;
			} else if (st.charAt(b) == 'B') {
				nw[b] = 1;
			} else if (st.charAt(b) == 'c') {
				nw[b] = 28;
			} else if (st.charAt(b) == 'C') {
				nw[b] = 2;
			} else if (st.charAt(b) == 'd') {
				nw[b] = 29;
			} else if (st.charAt(b) == 'D') {
				nw[b] = 3;
			} else if (st.charAt(b) == 'e') {
				nw[b] = 30;
			} else if (st.charAt(b) == 'E') {
				nw[b] = 4;
			} else if (st.charAt(b) == 'f') {
				nw[b] = 31;
			} else if (st.charAt(b) == 'F') {
				nw[b] = 5;
			} else if (st.charAt(b) == 'g') {
				nw[b] = 32;
			} else if (st.charAt(b) == 'G') {
				nw[b] = 6;
			} else if (st.charAt(b) == 'h') {
				nw[b] = 33;
			} else if (st.charAt(b) == 'H') {
				nw[b] = 7;
			} else if (st.charAt(b) == 'i') {
				nw[b] = 34;
			} else if (st.charAt(b) == 'I') {
				nw[b] = 8;
			} else if (st.charAt(b) == 'j') {
				nw[b] = 35;
			} else if (st.charAt(b) == 'J') {
				nw[b] = 9;
			} else if (st.charAt(b) == 'k') {
				nw[b] = 36;
			} else if (st.charAt(b) == 'K') {
				nw[b] = 10;
			} else if (st.charAt(b) == 'l') {
				nw[b] = 37;
			} else if (st.charAt(b) == 'L') {
				nw[b] = 11;
			} else if (st.charAt(b) == 'm') {
				nw[b] = 38;
			} else if (st.charAt(b) == 'M') {
				nw[b] = 12;
			} else if (st.charAt(b) == 'n') {
				nw[b] = 39;
			} else if (st.charAt(b) == 'N') {
				nw[b] = 13;
			} else if (st.charAt(b) == 'o') {
				nw[b] = 40;
			} else if (st.charAt(b) == 'O') {
				nw[b] = 14;
			} else if (st.charAt(b) == 'p') {
				nw[b] = 41;
			} else if (st.charAt(b) == 'P') {
				nw[b] = 15;
			} else if (st.charAt(b) == 'q') {
				nw[b] = 42;
			} else if (st.charAt(b) == 'Q') {
				nw[b] = 16;
			} else if (st.charAt(b) == 'r') {
				nw[b] = 43;
			} else if (st.charAt(b) == 'R') {
				nw[b] = 17;
			} else if (st.charAt(b) == 's') {
				nw[b] = 44;
			} else if (st.charAt(b) == 'S') {
				nw[b] = 18;
			} else if (st.charAt(b) == 't') {
				nw[b] = 45;
			} else if (st.charAt(b) == 'T') {
				nw[b] = 19;
			} else if (st.charAt(b) == 'u') {
				nw[b] = 46;
			} else if (st.charAt(b) == 'U') {
				nw[b] = 20;
			} else if (st.charAt(b) == 'v') {
				nw[b] = 47;
			} else if (st.charAt(b) == 'V') {
				nw[b] = 21;
			} else if (st.charAt(b) == 'w') {
				nw[b] = 48;
			} else if (st.charAt(b) == 'W') {
				nw[b] = 22;
			} else if (st.charAt(b) == 'x') {
				nw[b] = 49;
			} else if (st.charAt(b) == 'X') {
				nw[b] = 23;
			} else if (st.charAt(b) == 'y') {
				nw[b] = 50;
			} else if (st.charAt(b) == 'Y') {
				nw[b] = 24;
			} else if (st.charAt(b) == 'z') {
				nw[b] = 51;
			} else if (st.charAt(b) == 'Z') {
				nw[b] = 25;
			} else if (st.charAt(b) == ' ') {
				nw[b] = 52;
			} else if (st.charAt(b) == '0') {
				nw[b] = 53;
			} else if (st.charAt(b) == '1') {
				nw[b] = 54;
			} else if (st.charAt(b) == '2') {
				nw[b] = 55;
			} else if (st.charAt(b) == '3') {
				nw[b] = 56;
			} else if (st.charAt(b) == '4') {
				nw[b] = 57;
			} else if (st.charAt(b) == '5') {
				nw[b] = 58;
			} else if (st.charAt(b) == '6') {
				nw[b] = 59;
			} else if (st.charAt(b) == '7') {
				nw[b] = 60;
			} else if (st.charAt(b) == '8') {
				nw[b] = 61;
			} else if (st.charAt(b) == '9') {
				nw[b] = 62;
			} else if (st.charAt(b) == '/') {
				nw[b] = 63;
			} else if (st.charAt(b) == '?') {
				nw[b] = 64;
			} else if (st.charAt(b) == '¿') {
				nw[b] = 65;
			} else if (st.charAt(b) == '(') {
				nw[b] = 66;
			} else if (st.charAt(b) == ')') {
				nw[b] = 67;
			} else if (st.charAt(b) == 'é') {
				nw[b] = 4;
			} else if (st.charAt(b) == 'á') {
				nw[b] = 0;
			} else if (st.charAt(b) == 'ó') {
				nw[b] = 14;
			} else if (st.charAt(b) == 'í') {
				nw[b] = 8;
			} else if (st.charAt(b) == '.') {
				nw[b] = 68;
			} else if (st.charAt(b) == ',') {
				nw[b] = 69;
			} else if (st.charAt(b) == '\'') {
				nw[b] = 70;
			}

		}
		return nw;
	}

	int[] startingSaves = { 0 };

	int[] added;

	int[][] finalInts;
	String[] finalStrings;

	// This runs throught the branches of the tree from the begining.
	void compress() {
		// this gets set to the original number of that word. Need to take the
		// updated number.
		finalInts = new int[0][];
		finalStrings = new String[0];
		added = new int[0];
		// This runs thought all the starters.
		for (int a = 0; a < startingSaves.length; a++) {
			// if it has not been added yet then.
			// add it. save it to the StringAr and intAr
			addAndUpdate(startingSaves[a]);
		}
		// Save this shit to a text box.
		// And make a save of the boxes locations so i can reload the program.
		// Ideally load the program from the copy pastable text.
		for (int a = 0; a < finalStrings.length; a++) {
			System.out.print("finalString: " + finalStrings[a] + "  \t");
			for (int b = 0; b < finalInts[a].length; b++) {
				System.out
						.print("int[" + a + "][" + b + "] " + finalInts[a][b]);
			}
			System.out.println();
		}
		String[][] bigTree = new String[finalStrings.length][1];
		int[][][] treeGoesTo = new int[finalInts.length][1][];
		for (int a = 0; a < finalStrings.length; a++) {
			bigTree[a][0] = finalStrings[a];
			// if (finalInts[a].length == 0) then it should set treeGoesTo[a][0]
			// equal to -1;
			if (finalInts[a].length == 0) {
				treeGoesTo[a][0] = new int[] { -1 };
			}
			for (int b = 0; b < finalInts[a].length; b++) {
				treeGoesTo[a][0] = finalInts[a];
			}
		}
		// System.out.print("String[][] bigTree = {");
		// for (int a = 0; a < finalStrings.length; a++) {
		// if (a == 0) {
		// System.out.print(" \"" + finalStrings[a] + "\"");
		// } else {
		// System.out.print(", \"" + finalStrings[a] + "\"");
		// }
		// }
		// System.out.println(" };");

		System.out.print("String[][] bigTree = {");
		for (int a = 0; a < bigTree.length; a++) {
			if (a == 0) {
				System.out.print(" \"" + bigTree[a][0] + "\"");
			} else {
				System.out.print(", \"" + bigTree[a][0] + "\"");
			}
		}
		System.out.println(" };");

		// // System.out.print("int[][][] treeGoesTo = {");
		// System.out.println("tre.l: " + treeGoesTo.length);
		// for (int a = 0; a < treeGoesTo.length; a++) {
		// // System.out.print(" {");
		// System.out.println("tre" + a + "].l: " + treeGoesTo[a][0].length);
		// for (int b = 0; b < treeGoesTo[a][0].length; b++) {
		// System.out.println("tre[" + a + "][" + b + "]: "
		// + treeGoesTo[a][0][b]);
		// if (b == 0) {
		// // System.out.print(" " + treeGoesTo[a][b] + "");
		// } else {
		// // System.out.print(", " + treeGoesTo[a][b] + "");
		// }
		// // System.out.print(" }");
		// }
		// }
		// // System.out.println(" };");

		// Outputs tree
		System.out.print("int[][][] treeGoesTo = {");
		for (int a = 0; a < treeGoesTo.length; a++) {
			for (int b = 0; b < treeGoesTo[a].length; b++) {
				// System.out.print("tre[" + a + "][" + b + "] {");
				System.out.print(" {");
				for (int c = 0; c < treeGoesTo[a][b].length; c++) {
					// System.out.println("tre[" + a + "][" + b + "][" + c +
					// "]: " + treeGoesTo[a][b][c]);
					if (c == 0) {
						System.out.print(" " + treeGoesTo[a][b][c]);
					} else {
						System.out.print(", " + treeGoesTo[a][b][c]);
					}
				}
				System.out.print(" }");
			}
			if (a + 1 < treeGoesTo.length) {
				System.out.print(", ");
			}
		}
		System.out.println(" }");
	}

	// String[][] bigTree =
	// int[][][] treeGoesTo =

	void addAndUpdate(int box) {
		System.out.println("addBox:  " + box);
		boolean notAdded = true;
		for (int z = 0; z < added.length; z++) {
			if (added[z] == box) {
				notAdded = false;
			}
		}
		if (notAdded) {
			finalStrings = appendStringAr(finalStrings, bList.get(box)
					.getWord());
			finalInts = appendIntArAr(finalInts, bList.get(box).getGoesTo());
			added = appendIntAr(added, box);
			// Now run through the boxes added and add them.
			for (int c = 0; c < bList.get(box).getGoesTo().length; c++) {
				addAndUpdate(bList.get(box).getGoesTo()[c]);
			}
		}
	}

	void newCompress() {
		// This just loads all the info from the branches into a string
		System.out.print("String[][] bigTree = {");
		for (int a = 0; a < bList.size(); a++) {
			if (a == 0) {
				System.out.print(" {\"" + bList.get(a).getWord() + "\"}");
			} else {
				System.out.print(", {\"" + bList.get(a).getWord() + "\"}");
			}
		}
		System.out.println(" };");
		System.out.print("int[][][] treeGoesTo = {");
		for (int a = 0; a < bList.size(); a++) {
			int[] goTo = bList.get(a).getGoesTo();
			if (goTo.length == 0) {
				goTo = appendIntAr(goTo, -1);
			}
			System.out.print(" {{");
			for (int b = 0; b < goTo.length; b++) {
				if (b == 0) {
					System.out.print(" " + goTo[b]);
				} else {
					System.out.print(", " + goTo[b]);
				}
			}
			if (a + 1 < bList.size()) {
				System.out.print(" }},");
			}
		}
		System.out.println(" }}};");

		System.out.print("int[] endWhen = {");
		for (int a = 0; a < bList.size(); a++) {
			if (a == 0) {
				System.out.print("" + bList.get(a).getEndWhen());
			} else {
				System.out.print("," + bList.get(a).getEndWhen());
			}
		}
		System.out.println("};");
	}

	void removeBranch(int branch) {
		bList.remove(branch);
		for (int a = 0; a < bList.size(); a++) {
			for (int b = 0; b < bList.get(a).getGoesTo().length; b++) {
				if (bList.get(a).getGoesTo()[b] == branch) {
					bList.get(a).setGoesTo(
							shortenIntAr(bList.get(a).getGoesTo(), b));
				} else if (bList.get(a).getGoesTo()[b] > branch) {
					bList.get(a).setGoesToInt(b,
							bList.get(a).getGoesTo()[b] - 1);
				}
			}
		}
	}

	void addBranch() {

	}

	// Should this forget about all of its old connections?
	// No, this should keep everything the same just move it to the front of the
	// arrayList. Useful for compressing because then zero and up are the
	// starters.
	void shiftBranch(int oldSpot, int newSpot) {
		// This needs to update all the connections.
		// Run throught every branch and update the goesTo
		// Move the branches between (old and new) goesTo up or
		boolean shiftUp;
		if (newSpot < oldSpot) {
			shiftUp = false;
		} else if (newSpot > oldSpot) {
			shiftUp = true;
		} else {
			return;
		}
		System.out.println("(old, new): (" + oldSpot + ", " + newSpot + ")");
		for (int a = 0; a < bList.size(); a++) {
			// Run through its goesTo and if they are between old and new then
			// move it in the correct direction.
			for (int b = 0; b < bList.get(a).getGoesTo().length; b++) {
				if (shiftUp) {
					if (bList.get(a).getGoesTo()[b] == oldSpot) {
						bList.get(a).getGoesTo()[b] = newSpot;
					} else if (bList.get(a).getGoesTo()[b] > oldSpot
					// else so it doesn't move the same number twice
							&& bList.get(a).getGoesTo()[b] <= newSpot) {
						bList.get(a).getGoesTo()[b] -= 1;
					}
				} else {
					if (bList.get(a).getGoesTo()[b] == oldSpot) {
						bList.get(a).getGoesTo()[b] = newSpot;
					} else if (bList.get(a).getGoesTo()[b] >= newSpot
							&& bList.get(a).getGoesTo()[b] < oldSpot) {
						bList.get(a).getGoesTo()[b] += 1;
					}
				}
			}
		}
		bList.add(newSpot, bList.get(oldSpot));
		bList.remove(oldSpot + 1);
	}

	// This works with any type of array.
	static int[] appendIntAr(int[] st, int appendage) {
		int[] temp = new int[st.length + 1];
		for (int a = 0; a < st.length; a++) {
			temp[a] = st[a];
		}
		temp[temp.length - 1] = appendage;
		return temp;
	}

	// This removed the [numToRemove] variable from an AR and compresses
	int[] shortenIntAr(int[] in, int numToRemove) {
		int[] temp = new int[in.length - 1];
		boolean reachedYet = false;
		for (int a = 0; a < in.length; a++) {
			System.out.println("a: " + a);
			if (a == numToRemove) {
				reachedYet = true;
				a++;
				System.out.println("newA: " + a);
			}
			if (a < in.length) {
				if (reachedYet) {
					temp[a - 1] = in[a];
				} else {
					temp[a] = in[a];
				}
			}
		}
		return temp;
	}

	static int[][] appendIntArAr(int[][] st, int appendage[]) {
		int[][] temp = new int[st.length + 1][];
		for (int a = 0; a < st.length; a++) {
			temp[a] = st[a];
		}
		temp[temp.length - 1] = appendage;
		return temp;
	}

	static String[] appendStringAr(String[] st, String appendage) {
		String[] temp = new String[st.length + 1];
		for (int a = 0; a < st.length; a++) {
			temp[a] = st[a];
		}
		temp[temp.length - 1] = appendage;
		return temp;
	}

	/**
	 * Saving below
	 */

	String saveNums;
	String saveWords;
	String saveGoes;

	// I want it to compress to a string. Save all variables from all of the
	// boxes.
	void save() {
		// DONT NEED TO REMEBER 'WORD' OR 'GOESTO'
		// Can use newCompress
		// But still need to save (w, h, xl, yl, goesTo)
		// ! w, h, xl, yl | goesTo[0], goesTo[1], goesTo[2] !
		StringBuilder longLine = new StringBuilder();
		for (int i = 0; i < bList.size(); i++) {
			String appendage = bList.get(i).getW() + "," + bList.get(i).getH()
					+ "," + bList.get(i).getX() + "," + bList.get(i).getY()
					+ "," + bList.get(i).getEndWhen();
			longLine.append(appendage);
			if (i + 1 < bList.size()) {
				longLine.append("!");
			}
		}
		saveNums = longLine.toString();
		System.out.println("save: " + saveNums);

		StringBuilder bigTreeSave = new StringBuilder();
		// This just loads all the info from the branches into a string
		// System.out.print("String[][] bigTree = {");

		for (int a = 0; a < bList.size(); a++) {
			if (a == 0) {
				bigTreeSave.append(bList.get(a).getWord());
			} else {
				bigTreeSave.append("!" + bList.get(a).getWord());
			}
		}
		saveWords = bigTreeSave.toString();
		System.out.println("btSave: " + bigTreeSave.toString());
		StringBuilder goesToSave = new StringBuilder();
		// goesToSave.append("!");
		// System.out.println(" }");
		// System.out.print("int[][][] treeGoesTo = {");
		for (int a = 0; a < bList.size(); a++) {
			int[] goTo = bList.get(a).getGoesTo();
			if (goTo.length == 0) {
				goTo = appendIntAr(goTo, -1);
			}
			// System.out.print(" {{");
			for (int b = 0; b < goTo.length; b++) {
				if (b == 0) {
					goesToSave.append(goTo[b]);
					// System.out.print(" " + goTo[b]);
				} else {
					goesToSave.append("," + goTo[b]);
					// System.out.print(", " + goTo[b]);
				}
			}
			if (a + 1 < bList.size()) {
				goesToSave.append("!");
			}
		}
		saveGoes = goesToSave.toString();
		System.out.println("gtSave: " + goesToSave.toString());

		String[] allLines = { saveNums, saveWords, saveGoes };

		RnR.saveLines(allLines);
	}

	void load() {
		System.out.println("loading");
		// save:
		// !24,30,168,78!24,30,281,100!24,30,364,123!24,30,399,225!24,30,317,200!
		// btSave: !a!b!c!d!e!
		// gtSave: !1!2!4,3!-1!-1!

		// Breaks saveString into, each box.
		// Each box into
		// Break vars by ',' and then set them.

		try {
			RnR.readLines();
			ArrayList<String> allLines = RnR.getstrings();
			saveNums = allLines.get(0);
			saveWords = allLines.get(1);
			saveGoes = allLines.get(2);
		} catch (IOException e) {
			System.out.println("cantLoadFrom.Txt");
		}

		bList = new ArrayList<Branch>();

		// Run through the words and save that first.
		String[] boxes = saveNums.split("[!]");
		System.out.println("saveGoes: " + saveGoes);
		String[] goes = saveGoes.split("[!]");
		String[] words = saveWords.split("[!]");
		// bList = new ArrayList<Branch>();
		for (int a = 0; a < boxes.length; a++) {
			String[] nums = boxes[a].split("[,]");
			int x = Integer.parseInt(nums[2]);
			int y = Integer.parseInt(nums[3]);
			int endWhen = Integer.parseInt(nums[4]);
			String word = words[a];
			String[] goesToA = goes[a].split("[,]");
			int[] goesToF = new int[goesToA.length];
			for (int b = 0; b < goesToA.length; b++) {
				if (Integer.parseInt(goesToA[b]) == -1) {
					goesToF = new int[0];
				} else {
					goesToF[b] = Integer.parseInt(goesToA[b]);
				}
			}
			bList.add(new Branch(x, y, word, goesToF, endWhen));
			System.out.print("(x: " + x + ")   (y: " + y + ")   (word: " + word
					+ ")   (goesTo: ");
			for (int b = 0; b < goesToF.length; b++) {
				if (b == 0) {
					System.out.print(goesToF[b]);
				} else {
					System.out.print("," + goesToF[b]);
				}
			}
			System.out.println(")   (endWhen: " + endWhen + ")");
		}
	}

	void loadOld() {
		System.out.println("loading");
		// save:
		// !24,30,168,78!24,30,281,100!24,30,364,123!24,30,399,225!24,30,317,200!
		// btSave: !a!b!c!d!e!
		// gtSave: !1!2!4,3!-1!-1!

		// Breaks saveString into, each box.
		// Each box into
		// Break vars by ',' and then set them.

		try {
			RnR.readLines();
			ArrayList<String> allLines = RnR.getstrings();
			saveNums = allLines.get(0);
			saveWords = allLines.get(1);
			saveGoes = allLines.get(2);
		} catch (IOException e) {
			System.out.println("cantLoadFrom.Txt");
		}

		// Run through the words and save that first.
		String[] boxes = saveNums.split("[!]");
		System.out.println("saveGoes: " + saveGoes);
		String[] goes = saveGoes.split("[!]");
		String[] words = saveWords.split("[!]");
		// bList = new ArrayList<Branch>();
		System.out.println("boxes.l: " + boxes.length);
		for (int a = 0; a < boxes.length; a++) {
			System.out.println("boxes[" + a + "]: " + boxes[a]);
			System.out.println("goes[" + a + "]: " + goes[a]);
			String[] nums = boxes[a].split("[,]");
			int x = Integer.parseInt(nums[0]);
			int y = Integer.parseInt(nums[1]);
			String word = words[a];
			String[] goesToA = goes[a].split("[,]");
			int[] goesToF = new int[goesToA.length];
			for (int b = 0; b < goesToA.length; b++) {
				System.out.println("goesToA[" + b + "]: " + goesToA[b]);
				goesToF[b] = Integer.parseInt(goesToA[b]);
			}
			// bList.add(new Branch(x, y, word));
			System.out.print("(x: " + x + ")   (y: " + y + ")   (word: " + word
					+ ")   (goesTo: ");
			for (int b = 0; b < goesToF.length; b++) {
				if (b == 0) {
					System.out.print(goesToF[b]);
				} else {
					System.out.print("," + goesToF[b]);
				}
			}
			System.out.println(")");
		}
	}

	void saveCompress() {

	}

	/**
	 * Methods go above here.
	 * 
	 */

	public void drwGm() {
		Graphics g2 = this.getGraphics();
		g2.drawImage(image, 0, 0, null);
		g2.dispose();
	}

	Image[] mcNum;

	public void imageInit() {
		txtAr = new Image[71];
		ImageIcon ii = new ImageIcon(this.getClass().getResource(
				"res/font/tx/cA.png"));
		txtAr[0] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource("res/font/tx/cB.png"));
		txtAr[1] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource("res/font/tx/cC.png"));
		txtAr[2] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource("res/font/tx/cD.png"));
		txtAr[3] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource("res/font/tx/cE.png"));
		txtAr[4] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource("res/font/tx/cF.png"));
		txtAr[5] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource("res/font/tx/cG.png"));
		txtAr[6] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource("res/font/tx/cH.png"));
		txtAr[7] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource("res/font/tx/cI.png"));
		txtAr[8] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource("res/font/tx/cJ.png"));
		txtAr[9] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource("res/font/tx/cK.png"));
		txtAr[10] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource("res/font/tx/cL.png"));
		txtAr[11] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource("res/font/tx/cM.png"));
		txtAr[12] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource("res/font/tx/cN.png"));
		txtAr[13] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource("res/font/tx/cO.png"));
		txtAr[14] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource("res/font/tx/cP.png"));
		txtAr[15] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource("res/font/tx/cQ.png"));
		txtAr[16] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource("res/font/tx/cR.png"));
		txtAr[17] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource("res/font/tx/cS.png"));
		txtAr[18] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource("res/font/tx/cT.png"));
		txtAr[19] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource("res/font/tx/cU.png"));
		txtAr[20] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource("res/font/tx/cV.png"));
		txtAr[21] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource("res/font/tx/cW.png"));
		txtAr[22] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource("res/font/tx/cX.png"));
		txtAr[23] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource("res/font/tx/cY.png"));
		txtAr[24] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource("res/font/tx/cZ.png"));
		txtAr[25] = ii.getImage();

		ii = new ImageIcon(this.getClass().getResource("res/font/tx/La.png"));
		txtAr[26] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource("res/font/tx/Lb.png"));
		txtAr[27] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource("res/font/tx/Lc.png"));
		txtAr[28] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource("res/font/tx/Ld.png"));
		txtAr[29] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource("res/font/tx/Le.png"));
		txtAr[30] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource("res/font/tx/Lf.png"));
		txtAr[31] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource("res/font/tx/Lg.png"));
		txtAr[32] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource("res/font/tx/Lh.png"));
		txtAr[33] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource("res/font/tx/Li.png"));
		txtAr[34] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource("res/font/tx/Lj.png"));
		txtAr[35] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource("res/font/tx/Lk.png"));
		txtAr[36] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource("res/font/tx/Ll.png"));
		txtAr[37] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource("res/font/tx/Lm.png"));
		txtAr[38] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource("res/font/tx/Ln.png"));
		txtAr[39] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource("res/font/tx/Lo.png"));
		txtAr[40] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource("res/font/tx/Lp.png"));
		txtAr[41] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource("res/font/tx/Lq.png"));
		txtAr[42] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource("res/font/tx/Lr.png"));
		txtAr[43] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource("res/font/tx/Ls.png"));
		txtAr[44] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource("res/font/tx/Lt.png"));
		txtAr[45] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource("res/font/tx/Lu.png"));
		txtAr[46] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource("res/font/tx/Lv.png"));
		txtAr[47] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource("res/font/tx/Lw.png"));
		txtAr[48] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource("res/font/tx/Lx.png"));
		txtAr[49] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource("res/font/tx/Ly.png"));
		txtAr[50] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource("res/font/tx/Lz.png"));
		txtAr[51] = ii.getImage();

		ii = new ImageIcon(this.getClass()
				.getResource("res/font/tx/cSpace.png"));
		txtAr[52] = ii.getImage();

		ii = new ImageIcon(this.getClass().getResource("res/font/tx/n0.png"));
		txtAr[53] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource("res/font/tx/n1.png"));
		txtAr[54] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource("res/font/tx/n2.png"));
		txtAr[55] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource("res/font/tx/n3.png"));
		txtAr[56] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource("res/font/tx/n4.png"));
		txtAr[57] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource("res/font/tx/n5.png"));
		txtAr[58] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource("res/font/tx/n6.png"));
		txtAr[59] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource("res/font/tx/n7.png"));
		txtAr[60] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource("res/font/tx/n8.png"));
		txtAr[61] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource("res/font/tx/n9.png"));
		txtAr[62] = ii.getImage();
		ii = new ImageIcon(this.getClass()
				.getResource("res/font/tx/zslash.png"));
		txtAr[63] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource("res/font/tx/qMark.png"));
		txtAr[64] = ii.getImage();
		ii = new ImageIcon(this.getClass()
				.getResource("res/font/tx/qMarkI.png"));
		txtAr[65] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource("res/font/tx/z(.png"));
		txtAr[66] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource("res/font/tx/z).png"));
		txtAr[67] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/tx/zPeriod.png"));
		txtAr[68] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource("res/font/tx/zComa.png"));
		txtAr[69] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/tx/zApostrophe.png"));
		txtAr[70] = ii.getImage();

		mcNum = new Image[11];
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/Minecraft/0.png"));
		mcNum[0] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/Minecraft/1.png"));
		mcNum[1] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/Minecraft/2.png"));
		mcNum[2] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/Minecraft/3.png"));
		mcNum[3] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/Minecraft/4.png"));
		mcNum[4] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/Minecraft/5.png"));
		mcNum[5] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/Minecraft/6.png"));
		mcNum[6] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/Minecraft/7.png"));
		mcNum[7] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/Minecraft/8.png"));
		mcNum[8] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/Minecraft/9.png"));
		mcNum[9] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/Minecraft/zDash.png"));
		mcNum[10] = ii.getImage();
	}

	int deltaX = 0;
	int deltaY = 0;
	boolean dragging = false;
	int initialBox = -1;
	boolean connecting = false;

	@Override
	public void mousePressed(MouseEvent me) {
		dragging = false;
		initialBox = -1;
		connecting = false;
		editNum = false;
		if (shiftP) {
			if (me.getButton() == MouseEvent.BUTTON1) {
				boolean foundBox = false;
				for (int i = 0; i < bList.size(); i++) {
					if (me.getX() > bList.get(i).getX()
							&& me.getX() < bList.get(i).getX()
									+ bList.get(i).getW()) {
						if (me.getY() > bList.get(i).getY()
								&& me.getY() < bList.get(i).getY()
										+ bList.get(i).getH()) {
							System.out.println("click on box " + i);
							selectedBox = i;
							foundBox = true;
							dragging = true;
						}
					}
				}
				if (foundBox) {
					// Find the delta (x, y)
					// Click is going to be greater than the corner.
					deltaX = bList.get(selectedBox).getX() - me.getX();
					deltaY = bList.get(selectedBox).getY() - me.getY();
				}
			}
			if (me.getButton() == MouseEvent.BUTTON3) {
				// If I right click and drag from one box to another then
				// set the initial branch to go to the latter branch.
				boolean foundBox = false;
				for (int i = 0; i < bList.size(); i++) {
					if (me.getX() > bList.get(i).getX()
							&& me.getX() < bList.get(i).getX()
									+ bList.get(i).getW()) {
						if (me.getY() > bList.get(i).getY()
								&& me.getY() < bList.get(i).getY()
										+ bList.get(i).getH()) {
							System.out.println("click on box " + i);
							initialBox = i;
							foundBox = true;
							connecting = true;
						}
					}
				}
			}
		} else {
			if (me.getButton() == MouseEvent.BUTTON1) {
				for (int i = 0; i < bList.size(); i++) {
					if (me.getX() > bList.get(i).getX()
							&& me.getX() < bList.get(i).getX()
									+ bList.get(i).getW()) {
						if (me.getY() > bList.get(i).getY()
								&& me.getY() < bList.get(i).getY()
										+ bList.get(i).getH()) {
							System.out.println("click on box " + i);
							selectedBox = i;
						}
					}
				}
			} else if (me.getButton() == MouseEvent.BUTTON3) {
				addBox(me.getX(), me.getY());
			}
			drawTree();
		}
	}

	@Override
	public void mouseReleased(MouseEvent me) {
		int finalBox = -1;
		if (connecting) {
			for (int i = 0; i < bList.size(); i++) {
				if (me.getX() > bList.get(i).getX()
						&& me.getX() < bList.get(i).getX()
								+ bList.get(i).getW()) {
					if (me.getY() > bList.get(i).getY()
							&& me.getY() < bList.get(i).getY()
									+ bList.get(i).getH()) {
						finalBox = i;
					}
				}
			}
		}
		if (finalBox != -1) {
			bList.get(initialBox).addGoTo(finalBox);
		}
		connecting = false;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		drwGm();
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
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void mouseDragged(MouseEvent me) {
		if (dragging) {
			bList.get(selectedBox).setX(me.getX() + deltaX);
			bList.get(selectedBox).setY(me.getY() + deltaY);
		}
		drawTree();
		drwGm();
	}

	boolean editNum = false;

	@Override
	public void keyPressed(KeyEvent ke) {
		if (ke.getKeyCode() == KeyEvent.VK_SHIFT) {
			shiftP = true;
		} else if (ke.getKeyCode() == KeyEvent.VK_CONTROL) {
			ctrlP = true;
		}
		if (selectedBox != -1) {
			if (!ctrlP) {
				if (shiftP) {
					// Shift pressed
					if (ke.getKeyCode() == KeyEvent.VK_A) {
						bList.get(selectedBox).addLetter("A");
					} else if (ke.getKeyCode() == KeyEvent.VK_B) {
						bList.get(selectedBox).addLetter("B");
					} else if (ke.getKeyCode() == KeyEvent.VK_C) {
						bList.get(selectedBox).addLetter("C");
					} else if (ke.getKeyCode() == KeyEvent.VK_D) {
						bList.get(selectedBox).addLetter("D");
					} else if (ke.getKeyCode() == KeyEvent.VK_E) {
						bList.get(selectedBox).addLetter("E");
					} else if (ke.getKeyCode() == KeyEvent.VK_F) {
						bList.get(selectedBox).addLetter("F");
					} else if (ke.getKeyCode() == KeyEvent.VK_G) {
						bList.get(selectedBox).addLetter("G");
					} else if (ke.getKeyCode() == KeyEvent.VK_H) {
						bList.get(selectedBox).addLetter("H");
					} else if (ke.getKeyCode() == KeyEvent.VK_I) {
						bList.get(selectedBox).addLetter("I");
					} else if (ke.getKeyCode() == KeyEvent.VK_J) {
						bList.get(selectedBox).addLetter("J");
					} else if (ke.getKeyCode() == KeyEvent.VK_K) {
						bList.get(selectedBox).addLetter("K");
					} else if (ke.getKeyCode() == KeyEvent.VK_L) {
						bList.get(selectedBox).addLetter("L");
					} else if (ke.getKeyCode() == KeyEvent.VK_M) {
						bList.get(selectedBox).addLetter("M");
					} else if (ke.getKeyCode() == KeyEvent.VK_N) {
						bList.get(selectedBox).addLetter("N");
					} else if (ke.getKeyCode() == KeyEvent.VK_O) {
						bList.get(selectedBox).addLetter("O");
					} else if (ke.getKeyCode() == KeyEvent.VK_P) {
						bList.get(selectedBox).addLetter("P");
					} else if (ke.getKeyCode() == KeyEvent.VK_Q) {
						bList.get(selectedBox).addLetter("Q");
					} else if (ke.getKeyCode() == KeyEvent.VK_R) {
						bList.get(selectedBox).addLetter("R");
					} else if (ke.getKeyCode() == KeyEvent.VK_S) {
						bList.get(selectedBox).addLetter("S");
					} else if (ke.getKeyCode() == KeyEvent.VK_T) {
						bList.get(selectedBox).addLetter("T");
					} else if (ke.getKeyCode() == KeyEvent.VK_U) {
						bList.get(selectedBox).addLetter("U");
					} else if (ke.getKeyCode() == KeyEvent.VK_V) {
						bList.get(selectedBox).addLetter("V");
					} else if (ke.getKeyCode() == KeyEvent.VK_W) {
						bList.get(selectedBox).addLetter("W");
					} else if (ke.getKeyCode() == KeyEvent.VK_X) {
						bList.get(selectedBox).addLetter("X");
					} else if (ke.getKeyCode() == KeyEvent.VK_Y) {
						bList.get(selectedBox).addLetter("Y");
					} else if (ke.getKeyCode() == KeyEvent.VK_Z) {
						bList.get(selectedBox).addLetter("Z");
					}
				} else {
					if (ke.getKeyCode() == KeyEvent.VK_A) {
						System.out.println("here");
						bList.get(selectedBox).addLetter("a");
					} else if (ke.getKeyCode() == KeyEvent.VK_B) {
						bList.get(selectedBox).addLetter("b");
					} else if (ke.getKeyCode() == KeyEvent.VK_C) {
						bList.get(selectedBox).addLetter("c");
					} else if (ke.getKeyCode() == KeyEvent.VK_D) {
						bList.get(selectedBox).addLetter("d");
					} else if (ke.getKeyCode() == KeyEvent.VK_E) {
						bList.get(selectedBox).addLetter("e");
					} else if (ke.getKeyCode() == KeyEvent.VK_F) {
						bList.get(selectedBox).addLetter("f");
					} else if (ke.getKeyCode() == KeyEvent.VK_G) {
						bList.get(selectedBox).addLetter("g");
					} else if (ke.getKeyCode() == KeyEvent.VK_H) {
						bList.get(selectedBox).addLetter("h");
					} else if (ke.getKeyCode() == KeyEvent.VK_I) {
						bList.get(selectedBox).addLetter("i");
					} else if (ke.getKeyCode() == KeyEvent.VK_J) {
						bList.get(selectedBox).addLetter("j");
					} else if (ke.getKeyCode() == KeyEvent.VK_K) {
						bList.get(selectedBox).addLetter("k");
					} else if (ke.getKeyCode() == KeyEvent.VK_L) {
						bList.get(selectedBox).addLetter("l");
					} else if (ke.getKeyCode() == KeyEvent.VK_M) {
						bList.get(selectedBox).addLetter("m");
					} else if (ke.getKeyCode() == KeyEvent.VK_N) {
						bList.get(selectedBox).addLetter("n");
					} else if (ke.getKeyCode() == KeyEvent.VK_O) {
						bList.get(selectedBox).addLetter("o");
					} else if (ke.getKeyCode() == KeyEvent.VK_P) {
						bList.get(selectedBox).addLetter("p");
					} else if (ke.getKeyCode() == KeyEvent.VK_Q) {
						bList.get(selectedBox).addLetter("q");
					} else if (ke.getKeyCode() == KeyEvent.VK_R) {
						bList.get(selectedBox).addLetter("r");
					} else if (ke.getKeyCode() == KeyEvent.VK_S) {
						bList.get(selectedBox).addLetter("s");
					} else if (ke.getKeyCode() == KeyEvent.VK_T) {
						bList.get(selectedBox).addLetter("t");
					} else if (ke.getKeyCode() == KeyEvent.VK_U) {
						bList.get(selectedBox).addLetter("u");
					} else if (ke.getKeyCode() == KeyEvent.VK_V) {
						bList.get(selectedBox).addLetter("v");
					} else if (ke.getKeyCode() == KeyEvent.VK_W) {
						bList.get(selectedBox).addLetter("w");
					} else if (ke.getKeyCode() == KeyEvent.VK_X) {
						bList.get(selectedBox).addLetter("x");
					} else if (ke.getKeyCode() == KeyEvent.VK_Y) {
						bList.get(selectedBox).addLetter("y");
					} else if (ke.getKeyCode() == KeyEvent.VK_Z) {
						bList.get(selectedBox).addLetter("z");
					} else if (ke.getKeyCode() == KeyEvent.VK_0) {
						if (editNum) {
							bList.get(selectedBox).addInt(0);
						}
					} else if (ke.getKeyCode() == KeyEvent.VK_1) {
						if (editNum) {
							bList.get(selectedBox).addInt(1);
						}
					} else if (ke.getKeyCode() == KeyEvent.VK_2) {
						if (editNum) {
							bList.get(selectedBox).addInt(2);
						}
					} else if (ke.getKeyCode() == KeyEvent.VK_3) {
						if (editNum) {
							bList.get(selectedBox).addInt(3);
						}
					} else if (ke.getKeyCode() == KeyEvent.VK_4) {
						if (editNum) {
							bList.get(selectedBox).addInt(4);
						}
					} else if (ke.getKeyCode() == KeyEvent.VK_5) {
						if (editNum) {
							bList.get(selectedBox).addInt(5);
						}
					} else if (ke.getKeyCode() == KeyEvent.VK_6) {
						if (editNum) {
							bList.get(selectedBox).addInt(6);
						}
					} else if (ke.getKeyCode() == KeyEvent.VK_7) {
						if (editNum) {
							bList.get(selectedBox).addInt(7);
						}
					} else if (ke.getKeyCode() == KeyEvent.VK_8) {
						if (editNum) {
							bList.get(selectedBox).addInt(8);
						}
					} else if (ke.getKeyCode() == KeyEvent.VK_9) {
						if (editNum) {
							bList.get(selectedBox).addInt(9);
						}
					} else if (ke.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
						if (editNum) {
							bList.get(selectedBox).removeInt();
						} else {
							bList.get(selectedBox).removeLetter();
						}
					} else if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
						// Take the charAL and display it on screen and input it
						// for
						// the computer to interpret
						// interpretChars();
						// newInterpret();
					} else if (ke.getKeyCode() == KeyEvent.VK_QUOTE) {
						// Non shift so quote is apostrophe
						bList.get(selectedBox).addLetter("''");
					} else if (ke.getKeyCode() == KeyEvent.VK_SPACE) {
						bList.get(selectedBox).addLetter(" ");
					}
				}
			} else {
				if (ke.getKeyCode() == KeyEvent.VK_D) {
					removeBranch(selectedBox);
				} else if (ke.getKeyCode() == KeyEvent.VK_S) {
					shiftBranch(selectedBox, 0);
					selectedBox = 0;
				} else if (ke.getKeyCode() == KeyEvent.VK_E) {
					editNum = true;
				}
			}
		}
		if (ctrlP) {
			if (ke.getKeyCode() == KeyEvent.VK_B) {
				System.out.println("ctrl + B");
				newCompress();
			} else if (ke.getKeyCode() == KeyEvent.VK_A) {
				save();
			} else if (ke.getKeyCode() == KeyEvent.VK_L) {
				load();
			}
		}
		if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
			selectedBox = -1;
			editNum = false;
		}
		drawTree();
		drwGm();

	}

	@Override
	public void keyReleased(KeyEvent ke) {
		if (ke.getKeyCode() == KeyEvent.VK_SHIFT) {
			shiftP = false;
		} else if (ke.getKeyCode() == KeyEvent.VK_CONTROL) {
			ctrlP = false;
		}

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}
}
