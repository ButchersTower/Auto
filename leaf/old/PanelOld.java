package Auto.leaf.old;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import Auto.leaf.Decypher;
import Auto.leaf.JaMa;
import Auto.leaf.Words;

public class PanelOld extends JPanel implements MouseListener, KeyListener {
	// Clicking mouse draws the screen.

	private static int width = 360;
	private static int height = 420;

	private Image image;
	private Graphics g;
	private Image[] txtAr;

	private static int[][] fonts = { { 12, 16 }, { 10, 14 } };

	// Button/ Text box info.
	private int[][] singleButton = { { 10, 392, 340, 18, 0 } };

	// Chat box strings.
	private static ArrayList<String[]> calib = new ArrayList<String[]>();

	// Name of characters?
	public String[] conversers = { "player", "computer" };

	// Word databases
	public static ArrayList<String[]> words;
	public static ArrayList<String[]> verbs;

	// Text box vars
	private static int xMargin = 2;
	private static int yMargin = 2;
	private static int XPadding = 2;
	private static int yPadding = 2;
	private static int charWidth = 10;
	private static int botMargin = 4;

	// [0] = input bar
	// [1] = letter at (x).
	private int[] cursor = { -1 };

	private StringBuilder sb = new StringBuilder();

	private boolean shiftP;

	// Where data is saved to.
	public static ArrayList<GameCharacter> characters;

	// This version remembers characters by their names, but indepth ones could
	// recognizes thins based on their traits and appearances.
	void initCharacters() {
		characters = new ArrayList<GameCharacter>();
		characters.add(new GameCharacter("player", this));
		characters.add(new GameCharacter("computer", this));
	}

	public PanelOld() {
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
	 */

	private void pStart() {
		imageInit();
		initCharacters();
		words = Words.initWords();
		verbs = Words.initVerbs();
		// st.add("Ice wagon flew");
		// st.add("Witho Yoh Glow");
		drawAll();
	}

	// draw a single text box
	private void newTextBox(ArrayList<String> stAl, int x, int y, int width,
			int font) {
		for (int al = 0; al < stAl.size(); al++) {
			String st = stAl.get(al);
			int spaceBetweenRows = 2;
			int[][] attempt;
			// returns int[rows][letters]
			StringBuilder stCollect = new StringBuilder();
			int[] lineS = new int[0];
			// construct a long string of all the letters drawn to this txtBox
			// and
			// draw it later in the method.
			int row = 0;
			String[] lines = parseString(st);
			int[][] letLoc = new int[lines.length][];
			// between <br>'s
			for (int l = 0; l < lines.length; l++) {
				int totLettsLine = (width - (width % fonts[font][0]))
						/ fonts[font][0];
				// int widthLeft = width;
				// parse by spaces into words.
				// System.out.println("lines[" + l + "]: " + lines[l]);
				String[] words = lines[l].split("[ ]");

				// take a letter (period) off the beginning first and end of
				// last word.
				if (words.length == 1) {
					StringBuilder stB = new StringBuilder();
					stB.append(words[0]);
					stB.deleteCharAt(0);
					stB.deleteCharAt(stB.length() - 1);
					words[0] = stB.toString();
				} else if (words.length > 1) {
					StringBuilder stB = new StringBuilder();
					stB.append(words[0]);
					stB.deleteCharAt(0);
					words[0] = stB.toString();

					stB.delete(0, stB.length());

					stB.append(words[words.length - 1]);
					stB.deleteCharAt(stB.length() - 1);
					words[words.length - 1] = stB.toString();
				}

				int lettersThisLine = 0;

				// figure out with of each word and if it can fit onto this
				// line.
				for (int w = 0; w < words.length; w++) {
					int wordL = words[w].length();
					if (lettersThisLine + wordL <= totLettsLine) {
						stCollect.append(words[w] + " ");
						lettersThisLine += wordL + 1;
					} else {
						// move to next line
						row += 1;
						lineS = JaMa.appendIntAr(lineS, lettersThisLine);
						lettersThisLine = 0;
						stCollect.append(words[w] + " ");
						lettersThisLine += wordL + 1;
					}
				}
				row += 1;
				lineS = JaMa.appendIntAr(lineS, lettersThisLine);
				lettersThisLine = 0;
			}
			// draws text box back.
			g.setColor(Color.WHITE);
			g.fillRect(x, y, width, row * (fonts[font][1] + spaceBetweenRows));
			// draws letters.
			int letsDrawn = 0;
			int[] stNums = converter(stCollect.toString());
			// System.out.println("stNums.l: " + stNums.length);
			// System.out.println("lineS.l:  " + lineS.length);
			for (int r = 0; r < lineS.length; r++) {
				// draw the correct number of letters on each line.
				int xIndent = 0;
				// System.out.println("lineS[" + r + "]: " + lineS[r]);
				for (int le = 0; le < lineS[r]; le++) {
					g.drawImage(txtAr[stNums[le + letsDrawn]], x + xIndent + 2,
							y + (r * (fonts[font][1] + spaceBetweenRows)) + 2,
							null);

					xIndent += fonts[font][0];
				}
				letsDrawn += lineS[r];
			}
			y += row * 18;
			y += 2;
		}

	}

	// leaves a period before and after each string
	private String[] parseString(String st) {
		// split by "<br>"
		StringBuilder stringB = new StringBuilder();
		stringB.append(st);
		// add "a"'s so the program does not forget about arrows or breaks that
		// come before all other text.
		stringB.insert(0, ".");
		stringB.append(".");
		String[] strngs = stringB.toString().split("<br>");
		stringB.delete(0, stringB.length());
		for (int s = 0; s < strngs.length; s++) {
			// System.out.println("strngs[" + s + "]: " + strngs[s]);
			// decompile greater than.
			String[] a1 = strngs[s].split("<gt>");
			// recomple with correct sign.

			stringB.append(strngs[s]);
			stringB.insert(0, ".");
			stringB.append(".");
			for (int a = 0; a < a1.length; a++) {
				// take out and reinsure greater than and greater than.
				// if there are
				String[] a2 = stringB.toString().split("<lt>");
				stringB.delete(0, stringB.length());
				for (int b = 0; b < a2.length; b++) {
					if (b != 0) {
						stringB.append("<");
					}
					stringB.append(a2[b]);
				}
				// System.out.println("stB.ts1: " + stringB.toString());
				String[] a3 = stringB.toString().split("<gt>");
				stringB.delete(0, stringB.length());
				// System.out.println("stb.l: " + stringB.length());
				for (int b = 0; b < a3.length; b++) {
					if (b != 0) {
						stringB.append(">");
					}
					stringB.append(a3[b]);
				}
			}
			// gets rid of the string before and after each string
			// stringB.deleteCharAt(0);
			// stringB.deleteCharAt(stringB.length() - 1);
			strngs[s] = stringB.toString();
			stringB.delete(0, stringB.length());
			// System.out.println("strngsf[" + s + "]: " + strngs[s]);
		}
		// gets rid of the period before and after the first and last strings.
		if (strngs.length == 1) {
			StringBuilder stB = new StringBuilder();
			stB.append(strngs[0]);
			stB.deleteCharAt(0);
			stB.deleteCharAt(stB.length() - 1);
			strngs[0] = stB.toString();
		} else if (strngs.length > 1) {
			StringBuilder stB = new StringBuilder();
			stB.append(strngs[0]);
			stB.deleteCharAt(0);
			strngs[0] = stB.toString();

			stB.delete(0, stB.length());

			stB.append(strngs[strngs.length - 1]);
			stB.deleteCharAt(stB.length() - 1);
			strngs[strngs.length - 1] = stB.toString();
		}
		return strngs;
	}

	private static int[] converter(String st) {
		int a = st.length();
		int[] nw = new int[a];

		for (int b = 0; b < a; b++) {
			if (st.charAt(b) == ' ') {
				nw[b] = 0;
			} else if (st.charAt(b) == 'a') {
				nw[b] = 27;
			} else if (st.charAt(b) == 'A') {
				nw[b] = 1;
			} else if (st.charAt(b) == 'b') {
				nw[b] = 28;
			} else if (st.charAt(b) == 'B') {
				nw[b] = 2;
			} else if (st.charAt(b) == 'c') {
				nw[b] = 29;
			} else if (st.charAt(b) == 'C') {
				nw[b] = 3;
			} else if (st.charAt(b) == 'd') {
				nw[b] = 30;
			} else if (st.charAt(b) == 'D') {
				nw[b] = 4;
			} else if (st.charAt(b) == 'e') {
				nw[b] = 31;
			} else if (st.charAt(b) == 'E') {
				nw[b] = 5;
			} else if (st.charAt(b) == 'f') {
				nw[b] = 32;
			} else if (st.charAt(b) == 'F') {
				nw[b] = 6;
			} else if (st.charAt(b) == 'g') {
				nw[b] = 33;
			} else if (st.charAt(b) == 'G') {
				nw[b] = 7;
			} else if (st.charAt(b) == 'h') {
				nw[b] = 34;
			} else if (st.charAt(b) == 'H') {
				nw[b] = 8;
			} else if (st.charAt(b) == 'i') {
				nw[b] = 35;
			} else if (st.charAt(b) == 'I') {
				nw[b] = 9;
			} else if (st.charAt(b) == 'j') {
				nw[b] = 36;
			} else if (st.charAt(b) == 'J') {
				nw[b] = 10;
			} else if (st.charAt(b) == 'k') {
				nw[b] = 37;
			} else if (st.charAt(b) == 'K') {
				nw[b] = 11;
			} else if (st.charAt(b) == 'l') {
				nw[b] = 38;
			} else if (st.charAt(b) == 'L') {
				nw[b] = 12;
			} else if (st.charAt(b) == 'm') {
				nw[b] = 39;
			} else if (st.charAt(b) == 'M') {
				nw[b] = 13;
			} else if (st.charAt(b) == 'n') {
				nw[b] = 40;
			} else if (st.charAt(b) == 'N') {
				nw[b] = 14;
			} else if (st.charAt(b) == 'o') {
				nw[b] = 41;
			} else if (st.charAt(b) == 'O') {
				nw[b] = 15;
			} else if (st.charAt(b) == 'p') {
				nw[b] = 42;
			} else if (st.charAt(b) == 'P') {
				nw[b] = 16;
			} else if (st.charAt(b) == 'q') {
				nw[b] = 43;
			} else if (st.charAt(b) == 'Q') {
				nw[b] = 17;
			} else if (st.charAt(b) == 'r') {
				nw[b] = 44;
			} else if (st.charAt(b) == 'R') {
				nw[b] = 18;
			} else if (st.charAt(b) == 's') {
				nw[b] = 45;
			} else if (st.charAt(b) == 'S') {
				nw[b] = 19;
			} else if (st.charAt(b) == 't') {
				nw[b] = 46;
			} else if (st.charAt(b) == 'T') {
				nw[b] = 20;
			} else if (st.charAt(b) == 'u') {
				nw[b] = 47;
			} else if (st.charAt(b) == 'U') {
				nw[b] = 21;
			} else if (st.charAt(b) == 'v') {
				nw[b] = 48;
			} else if (st.charAt(b) == 'V') {
				nw[b] = 22;
			} else if (st.charAt(b) == 'w') {
				nw[b] = 49;
			} else if (st.charAt(b) == 'W') {
				nw[b] = 23;
			} else if (st.charAt(b) == 'x') {
				nw[b] = 50;
			} else if (st.charAt(b) == 'X') {
				nw[b] = 24;
			} else if (st.charAt(b) == 'y') {
				nw[b] = 51;
			} else if (st.charAt(b) == 'Y') {
				nw[b] = 25;
			} else if (st.charAt(b) == 'z') {
				nw[b] = 52;
			} else if (st.charAt(b) == 'Z') {
				nw[b] = 26;
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

			} else if (st.charAt(b) == '`') {
				nw[b] = 63;
			} else if (st.charAt(b) == '-') {
				nw[b] = 64;
			} else if (st.charAt(b) == '=') {
				nw[b] = 65;
			} else if (st.charAt(b) == '~') {
				nw[b] = 66;
			} else if (st.charAt(b) == '!') {
				nw[b] = 67;
			} else if (st.charAt(b) == '@') {
				nw[b] = 68;
			} else if (st.charAt(b) == '#') {
				nw[b] = 69;
			} else if (st.charAt(b) == '$') {
				nw[b] = 70;
			} else if (st.charAt(b) == '%') {
				nw[b] = 71;
			} else if (st.charAt(b) == '^') {
				nw[b] = 72;
			} else if (st.charAt(b) == '&') {
				nw[b] = 73;
			} else if (st.charAt(b) == '*') {
				nw[b] = 74;
			} else if (st.charAt(b) == '(') {
				nw[b] = 75;
			} else if (st.charAt(b) == ')') {
				nw[b] = 76;
			} else if (st.charAt(b) == '+') {
				nw[b] = 77;
			} else if (st.charAt(b) == ',') {
				nw[b] = 78;
			} else if (st.charAt(b) == '.') {
				nw[b] = 79;
			} else if (st.charAt(b) == '/') {
				nw[b] = 80;
			} else if (st.charAt(b) == ';') {
				nw[b] = 81;
			} else if (st.charAt(b) == '[') {
				nw[b] = 82;
			} else if (st.charAt(b) == ']') {
				nw[b] = 83;
			} else if (st.charAt(b) == '\'') {
				nw[b] = 84;
			} else if (st.charAt(b) == '<') {
				nw[b] = 85;
			} else if (st.charAt(b) == '>') {
				nw[b] = 86;
			} else if (st.charAt(b) == '?') {
				nw[b] = 87;
			} else if (st.charAt(b) == ':') {
				nw[b] = 88;
			} else if (st.charAt(b) == '{') {
				nw[b] = 89;
			} else if (st.charAt(b) == '}') {
				nw[b] = 90;
			} else if (st.charAt(b) == '|') {
				nw[b] = 91;

			} else if (st.charAt(b) == 'é') {
				nw[b] = 5;
			} else if (st.charAt(b) == 'á') {
				nw[b] = 1;
			} else if (st.charAt(b) == 'ó') {
				nw[b] = 15;
			} else if (st.charAt(b) == 'í') {
				nw[b] = 9;
			}
		}
		return nw;
	}

	private void inputTheBar() {
		String app = sb.toString();
		// add the text box to the chat box.
		takeIn("(P) " + app);
		// Send text into computer AI.
		// Decypher.decypherOld(app);
		Decypher.decypher(app);
		// Clear the text box.
		sb.delete(0, sb.length());
		// Set the cursor to the beginning.
		cursor[1] = 0;
	}

	// Adds the string to the visual chat box (not input box)
	public static void takeIn(String st) {
		int charsPerRow = (width - xMargin - XPadding) / fonts[1][0];

		// break into words
		String[] words = st.split("[ ]");
		String[] lines = new String[0];
		String line = words[0];
		int charsLeft = charsPerRow - words[0].length();
		for (int w = 1; w < words.length; w++) {
			// If this word can fit add it, else move onto next line.
			if (words[w].length() < charsLeft) {
				// line = line.concat(" " + words[w]);
				line = line + " " + words[w];
				charsLeft -= (words[w].length() + 1);
			} else {
				// System.out.println("newLine");
				lines = JaMa.appendStringAr(lines, line);
				charsLeft = charsPerRow - words[w].length();
				line = words[w];
			}
		}
		lines = JaMa.appendStringAr(lines, line);
		calib.add(lines);
	}

	/**
	 * Draw
	 */

	private void drawAll() {
		int font = 1;
		g.setColor(new Color(216, 216, 216));
		g.fillRect(0, 0, width, height);
		// have text box read from a string[]
		// newTextBox(st, 10, 10, 340, 0);
		drawLines(4, 4, 1);

		// Bottom Input Bar Background
		g.setColor(Color.WHITE);
		g.fillRect(singleButton[0][0], singleButton[0][1], singleButton[0][2],
				singleButton[0][3]);

		// Bottom Input Bar Text
		int[] stNums = converter(sb.toString());
		// System.out.println("stNums.l: " + stNums.length);
		int xIndent = 0;
		for (int le = 0; le < stNums.length; le++) {
			g.drawImage(txtAr[stNums[le]], singleButton[0][0] + xIndent,
					singleButton[0][1] + 1, null);
			xIndent += fonts[font][0];
		}

		boolean allGood = false;
		for (int c = 0; c < cursor.length; c++) {
			if (cursor[c] != -1) {
				allGood = true;
			}
		}
		if (allGood) {
			g.setColor(Color.BLACK);
			g.fillRect(singleButton[0][0] + (fonts[font][0] * cursor[1]) - 1,
					singleButton[0][1] + 2, 2, 14);
		}
	}

	private void drawLines(int x, int y, int font) {
		// Draws all lines
		int yIndent = 0;
		for (int i = 0; i < calib.size(); i++) {
			for (int l = 0; l < calib.get(i).length; l++) {
				int[] stNums = converter(calib.get(i)[l]);
				int xIndent = 0;
				for (int le = 0; le < stNums.length; le++) {
					g.drawImage(txtAr[stNums[le]], x + xIndent + 2, y + yIndent
							+ 2, null);
					xIndent += fonts[font][0];
				}
				yIndent += fonts[font][1];
			}
			yIndent += botMargin;
		}
	}

	/**
	 * Methods go above here.
	 */

	public void drwGm() {
		Graphics g2 = this.getGraphics();
		g2.drawImage(image, 0, 0, null);
		g2.dispose();
	}

	public void imageInit() {
		String folder = "eclipseAlpha";
		txtAr = new Image[92];

		ImageIcon ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/Sspace.png"));
		txtAr[0] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/cA.png"));
		txtAr[1] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/cB.png"));
		txtAr[2] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/cC.png"));
		txtAr[3] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/cD.png"));
		txtAr[4] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/cE.png"));
		txtAr[5] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/cF.png"));
		txtAr[6] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/cG.png"));
		txtAr[7] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/cH.png"));
		txtAr[8] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/cI.png"));
		txtAr[9] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/cJ.png"));
		txtAr[10] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/cK.png"));
		txtAr[11] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/cL.png"));
		txtAr[12] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/cM.png"));
		txtAr[13] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/cN.png"));
		txtAr[14] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/cO.png"));
		txtAr[15] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/cP.png"));
		txtAr[16] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/cQ.png"));
		txtAr[17] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/cR.png"));
		txtAr[18] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/cS.png"));
		txtAr[19] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/cT.png"));
		txtAr[20] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/cU.png"));
		txtAr[21] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/cV.png"));
		txtAr[22] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/cW.png"));
		txtAr[23] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/cX.png"));
		txtAr[24] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/cY.png"));
		txtAr[25] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/cZ.png"));
		txtAr[26] = ii.getImage();

		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/La.png"));
		txtAr[27] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/Lb.png"));
		txtAr[28] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/Lc.png"));
		txtAr[29] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/Ld.png"));
		txtAr[30] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/Le.png"));
		txtAr[31] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/Lf.png"));
		txtAr[32] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/Lg.png"));
		txtAr[33] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/Lh.png"));
		txtAr[34] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/Li.png"));
		txtAr[35] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/Lj.png"));
		txtAr[36] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/Lk.png"));
		txtAr[37] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/Ll.png"));
		txtAr[38] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/Lm.png"));
		txtAr[39] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/Ln.png"));
		txtAr[40] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/Lo.png"));
		txtAr[41] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/Lp.png"));
		txtAr[42] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/Lq.png"));
		txtAr[43] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/Lr.png"));
		txtAr[44] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/Ls.png"));
		txtAr[45] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/Lt.png"));
		txtAr[46] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/Lu.png"));
		txtAr[47] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/Lv.png"));
		txtAr[48] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/Lw.png"));
		txtAr[49] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/Lx.png"));
		txtAr[50] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/Ly.png"));
		txtAr[51] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/Lz.png"));
		txtAr[52] = ii.getImage();

		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/n0.png"));
		txtAr[53] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/n1.png"));
		txtAr[54] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/n2.png"));
		txtAr[55] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/n3.png"));
		txtAr[56] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/n4.png"));
		txtAr[57] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/n5.png"));
		txtAr[58] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/n6.png"));
		txtAr[59] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/n7.png"));
		txtAr[60] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/n8.png"));
		txtAr[61] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/n9.png"));
		txtAr[62] = ii.getImage();

		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/S`.png"));
		txtAr[63] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/S-.png"));
		txtAr[64] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/S=.png"));
		txtAr[65] = ii.getImage();

		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/S~.png"));
		txtAr[66] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/S!.png"));
		txtAr[67] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/S@.png"));
		txtAr[68] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/S#.png"));
		txtAr[69] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/S$.png"));
		txtAr[70] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/S%.png"));
		txtAr[71] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/S^.png"));
		txtAr[72] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/S&.png"));
		txtAr[73] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/S*.png"));
		txtAr[74] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/S(.png"));
		txtAr[75] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/S).png"));
		txtAr[76] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/S+.png"));
		txtAr[77] = ii.getImage();

		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/S,.png"));
		txtAr[78] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/S..png"));
		txtAr[79] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/SslashF.png"));
		txtAr[80] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/S;.png"));
		txtAr[81] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/S[.png"));
		txtAr[82] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/S].png"));
		txtAr[83] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/SslashB.png"));
		txtAr[84] = ii.getImage();

		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/S<.png"));
		txtAr[85] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/S>.png"));
		txtAr[86] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/S?.png"));
		txtAr[87] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/S:.png"));
		txtAr[88] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/S{.png"));
		txtAr[89] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/S}.png"));
		txtAr[90] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"res/font/" + folder + "/S|.png"));
		txtAr[91] = ii.getImage();
	}

	@Override
	public void mousePressed(MouseEvent me) {
		if (me.getX() > singleButton[0][0]
				&& me.getX() < singleButton[0][0] + singleButton[0][2]
				&& me.getY() > singleButton[0][1]
				&& me.getY() < singleButton[0][1] + singleButton[0][2]) {
			// System.out.println("oneButton");
			cursor = new int[] { 0, sb.length() };
		}
		drawAll();
		drwGm();
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
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
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent ke) {
		boolean allGood = true;
		for (int c = 0; c < cursor.length; c++) {
			if (cursor[c] == -1) {
				allGood = false;
			}
		}
		if (allGood) {
			if (ke.getKeyCode() == KeyEvent.VK_SHIFT) {
				shiftP = true;
			} else if (ke.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
				if (cursor[1] != 0) {
					sb.delete(cursor[1] - 1, cursor[1]);
					cursor[1]--;
				}
			} else if (ke.getKeyCode() == KeyEvent.VK_DELETE) {
				if (cursor[1] != sb.length()) {
					sb.delete(cursor[1], cursor[1] + 1);
				}
			} else if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
				inputTheBar();
			} else if (ke.getKeyCode() == KeyEvent.VK_LEFT) {
				if (cursor[1] != 0) {
					cursor[1]--;
				}
			} else if (ke.getKeyCode() == KeyEvent.VK_RIGHT) {
				if (cursor[1] != sb.length()) {
					cursor[1]++;
				}
			} else if (shiftP) {
				if (ke.getKeyCode() == KeyEvent.VK_A) {
					sb.insert(cursor[1], "A");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_B) {
					sb.insert(cursor[1], "B");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_C) {
					sb.insert(cursor[1], "C");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_D) {
					sb.insert(cursor[1], "D");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_E) {
					sb.insert(cursor[1], "E");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_F) {
					sb.insert(cursor[1], "F");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_G) {
					sb.insert(cursor[1], "G");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_H) {
					sb.insert(cursor[1], "H");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_I) {
					sb.insert(cursor[1], "I");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_J) {
					sb.insert(cursor[1], "J");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_K) {
					sb.insert(cursor[1], "K");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_L) {
					sb.insert(cursor[1], "L");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_M) {
					sb.insert(cursor[1], "M");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_N) {
					sb.insert(cursor[1], "N");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_O) {
					sb.insert(cursor[1], "O");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_P) {
					sb.insert(cursor[1], "P");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_Q) {
					sb.insert(cursor[1], "Q");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_R) {
					sb.insert(cursor[1], "R");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_S) {
					sb.insert(cursor[1], "S");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_T) {
					sb.insert(cursor[1], "T");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_U) {
					sb.insert(cursor[1], "U");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_V) {
					sb.insert(cursor[1], "V");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_W) {
					sb.insert(cursor[1], "W");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_X) {
					sb.insert(cursor[1], "X");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_Y) {
					sb.insert(cursor[1], "Y");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_Z) {
					sb.insert(cursor[1], "Z");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_0) {
					sb.insert(cursor[1], ")");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_1) {
					sb.insert(cursor[1], "!");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_2) {
					sb.insert(cursor[1], "@");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_3) {
					sb.insert(cursor[1], "#");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_4) {
					sb.insert(cursor[1], "$");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_5) {
					sb.insert(cursor[1], "%");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_6) {
					sb.insert(cursor[1], "^");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_7) {
					sb.insert(cursor[1], "&");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_8) {
					sb.insert(cursor[1], "*");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_9) {
					sb.insert(cursor[1], "(");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_EQUALS) {
					sb.insert(cursor[1], "+");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_COMMA) {
					sb.insert(cursor[1], "<");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_PERIOD) {
					sb.insert(cursor[1], ">");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_SLASH) {
					sb.insert(cursor[1], "?");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_SEMICOLON) {
					sb.insert(cursor[1], ":");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_OPEN_BRACKET) {
					sb.insert(cursor[1], "{");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_CLOSE_BRACKET) {
					sb.insert(cursor[1], "}");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_BACK_SLASH) {
					sb.insert(cursor[1], "|");
					cursor[1]++;
				}
			} else {
				if (ke.getKeyCode() == KeyEvent.VK_A) {
					sb.insert(cursor[1], "a");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_B) {
					sb.insert(cursor[1], "b");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_C) {
					sb.insert(cursor[1], "c");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_D) {
					sb.insert(cursor[1], "d");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_E) {
					sb.insert(cursor[1], "e");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_F) {
					sb.insert(cursor[1], "f");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_G) {
					sb.insert(cursor[1], "g");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_H) {
					sb.insert(cursor[1], "h");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_I) {
					sb.insert(cursor[1], "i");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_J) {
					sb.insert(cursor[1], "j");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_K) {
					sb.insert(cursor[1], "k");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_L) {
					sb.insert(cursor[1], "l");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_M) {
					sb.insert(cursor[1], "m");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_N) {
					sb.insert(cursor[1], "n");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_O) {
					sb.insert(cursor[1], "o");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_P) {
					sb.insert(cursor[1], "p");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_Q) {
					sb.insert(cursor[1], "q");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_R) {
					sb.insert(cursor[1], "r");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_S) {
					sb.insert(cursor[1], "s");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_T) {
					sb.insert(cursor[1], "t");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_U) {
					sb.insert(cursor[1], "u");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_V) {
					sb.insert(cursor[1], "v");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_W) {
					sb.insert(cursor[1], "w");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_X) {
					sb.insert(cursor[1], "x");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_Y) {
					sb.insert(cursor[1], "y");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_Z) {
					sb.insert(cursor[1], "z");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_0) {
					sb.insert(cursor[1], "0");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_1) {
					sb.insert(cursor[1], "1");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_2) {
					sb.insert(cursor[1], "2");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_3) {
					sb.insert(cursor[1], "3");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_4) {
					sb.insert(cursor[1], "4");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_5) {
					sb.insert(cursor[1], "5");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_6) {
					sb.insert(cursor[1], "6");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_7) {
					sb.insert(cursor[1], "7");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_8) {
					sb.insert(cursor[1], "8");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_9) {
					sb.insert(cursor[1], "9");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_MINUS) {
					sb.insert(cursor[1], "-");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_EQUALS) {
					sb.insert(cursor[1], "=");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_COMMA) {
					sb.insert(cursor[1], ",");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_PERIOD) {
					sb.insert(cursor[1], ".");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_SLASH) {
					sb.insert(cursor[1], "/");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_SEMICOLON) {
					sb.insert(cursor[1], ";");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_OPEN_BRACKET) {
					sb.insert(cursor[1], "[");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_CLOSE_BRACKET) {
					sb.insert(cursor[1], "]");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_BACK_SLASH) {
					sb.insert(cursor[1], "\'");
					cursor[1]++;
				}
			}
			if (ke.getKeyCode() == KeyEvent.VK_SPACE) {
				sb.insert(cursor[1], " ");
				cursor[1]++;
			}
		}
		drawAll();
		drwGm();
	}

	@Override
	public void keyReleased(KeyEvent ke) {
		if (ke.getKeyCode() == KeyEvent.VK_SHIFT) {
			shiftP = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}
}
