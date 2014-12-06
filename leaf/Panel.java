package Auto.leaf;

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

public class Panel extends JPanel implements MouseListener, KeyListener {
	// Clicking mouse draws the screen.

	int width = 360;
	int height = 420;

	Thread thread;
	Image image;
	Graphics g;

	// Vars for gLoop Above

	Image[] txtAr;

	int[][] fonts = { { 12, 16 } };
	ArrayList<String> st = new ArrayList<String>();

	String[][] bigTree = { { "Verb" }, { "Noun" }, { "Pronoun" },
			{ "Question" }, { "Adverb" }, { "Article" }, { "Adj" }, { "Noun" },
			{ "Verb" }, { "Noun" }, { "Pronoun" }, { "Verb" }, { "Verb" },
			{ "Adj" }, { "Noun" }, { "Adj" }, { "Verb" }, { "Adj" },
			{ "Verb" }, { "Noun" }, { "Adverb" }, { "Adj" }, { "Noun" },
			{ "Preposition" }, { "Noun" } };
	int[][][] treeGoesTo = { { { 4, 13, 5, 14, 18 } }, { { 0 } }, { { 0 } },
			{ { 8 } }, { { -1 } }, { { 6 } }, { { 7 } }, { { -1 } },
			{ { 10, 9 } }, { { 12 } }, { { 11 } }, { { -1 } }, { { -1 } },
			{ { -1 } }, { { 15 } }, { { 16 } }, { { 17 } }, { { -1 } },
			{ { 19, 20, 21 } }, { { -1 } }, { { -1 } }, { { 22 } }, { { 23 } },
			{ { 24 } }, { { -1 } } };
	int[] endWhen = { -1, -1, -1, -1, -4, -1, -1, -8, -1, -1, -1, -5, -5, -2,
			-3, -1, -1, -6, -1, -7, -1, -7, -1, -1, -11 };

	ArrayList<String[]> words;
	ArrayList<String[]> Verbs;

	int[][] singleButton = { { 10, 392, 340, 18, 0 } };

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
	 */

	// Want to save plurality of pronouns, don't need yet but seems important in
	// trying to identify pronouns
	void initWords() {
		words = new ArrayList<String[]>();
		words.add(new String[] { "hello", "Interjection", "Greeting" });
		words.add(new String[] { "welcome", "Interjection", "Greeting" });

		words.add(new String[] { "i", "Pronoun", "First person" });
		words.add(new String[] { "me", "Pronoun", "First person" });
		words.add(new String[] { "my", "Pronoun", "First person" });
		words.add(new String[] { "you", "Pronoun", "Second person" });
		words.add(new String[] { "it", "Pronoun", "Third person" });
		words.add(new String[] { "he", "Pronoun", "Third person" });
		words.add(new String[] { "she", "Pronoun", "Third person" });
		words.add(new String[] { "they", "Pronoun", "Third person" });

		words.add(new String[] { "a", "Article" });
		words.add(new String[] { "an", "Article" });
		words.add(new String[] { "the", "Article" });

		words.add(new String[] { "to", "Preposition" });
		words.add(new String[] { "at", "Preposition" });
		words.add(new String[] { "after", "Preposition" });
		words.add(new String[] { "on", "Preposition" });
		// But is a preposition and conjunction depending on the sentence
		words.add(new String[] { "but", "Preposition" });

		words.add(new String[] { "and", "Conjunction", "and" });

		words.add(new String[] { "head", "Noun", "head" });
		words.add(new String[] { "chest", "Noun", "chest" });
		words.add(new String[] { "arm", "Noun", "arm" });
		words.add(new String[] { "arms", "Noun", "arms" });
		words.add(new String[] { "leg", "Noun", "leg" });
		words.add(new String[] { "legs", "Noun", "legs" });
		words.add(new String[] { "ear", "Noun", "ear" });
		words.add(new String[] { "ears", "Noun", "ears" });
		words.add(new String[] { "cat", "Noun", "cat" });
		words.add(new String[] { "dog", "Noun", "dog" });
		words.add(new String[] { "meat", "Noun", "meat" });
		words.add(new String[] { "home", "Noun", "home" });
		words.add(new String[] { "house", "Noun", "house" });
		words.add(new String[] { "name", "Noun", "name" });
		words.add(new String[] { "bottle", "Noun", "bottle" });
		words.add(new String[] { "drugs", "Noun", "drugs" });
		words.add(new String[] { "tavern", "Noun", "tavern" });
		words.add(new String[] { "life", "Noun", "life" });

		words.add(new String[] { "Scourge", "Noun", "Scourge" });
		words.add(new String[] { "Azeroth", "Noun", "Azeroth" });
		words.add(new String[] { "undead", "Adjective", "undead" });

		words.add(new String[] { "ugly", "Adjective", "ugly" });
		words.add(new String[] { "pretty", "Adjective", "pretty" });
		words.add(new String[] { "good", "Adjective", "good" });
		words.add(new String[] { "bad", "Adjective", "bad" });
		words.add(new String[] { "tired", "Adjective", "tired" });
		words.add(new String[] { "energetic", "Adjective", "energetic" });
		words.add(new String[] { "sick", "Adjective", "sick" });
		words.add(new String[] { "healthy", "Adjective", "healthy" });
		words.add(new String[] { "cool", "Adjective", "cool" });
		words.add(new String[] { "uncool", "Adjective", "uncool" });
		words.add(new String[] { "blue", "Adjective", "blue" });
		words.add(new String[] { "empty", "Adjective", "empty" });
		words.add(new String[] { "all", "Adjective", "all" });

		words.add(new String[] { "well", "Adverb", "well" });
		// "How" is confusing
		words.add(new String[] { "how", "Adverb", "how" });

		words.add(new String[] { "have", "Verb", "to have", "Infinitive" });
		words.add(new String[] { "own", "Verb", "to own", "Infinitive" });
		words.add(new String[] { "possess", "Verb", "to possess", "Infinitive" });
		words.add(new String[] { "do", "Verb", "to do", "Infinitive" });
		words.add(new String[] { "doing", "Verb", "to do", "Progressive" });
		words.add(new String[] { "want", "Verb", "to want", "Infinitive" });
		words.add(new String[] { "go", "Verb", "to go", "Infinitive" });
		words.add(new String[] { "seek", "Verb", "to seek", "Infinitive" });

		words.add(new String[] { "be", "Verb", "to be", "Infinitive" });
		words.add(new String[] { "am", "Verb", "to be", "First" });
		words.add(new String[] { "are", "Verb", "to be", "Second",
				"Plural First", "Plural Second", "Plural Third" });
		words.add(new String[] { "is", "Verb", "to be", "Third" });
		words.add(new String[] { "sell", "Verb", "to sell", "Infinitive" });

		words.add(new String[] { "going", "Verb", "to go", "Progressive" });
		words.add(new String[] { "feel", "Verb", "to feel", "Infinitive" });
		words.add(new String[] { "eradicate", "Verb", "To eradicate",
				"Infinitive" });

		// Will
		words.add(new String[] { "will", "Verb", "will", "Auxilary" });
		// Questions work
		words.add(new String[] { "possessions", "noun", "to possess" });
		// what is an adjective (i think sometimes)
		words.add(new String[] { "what", "Question", "what" });
		// Confusing
		words.add(new String[] { "that", "Adjective", "that" });
	}

	void initVerbs() {
		// .get(x)[0] = (Infinitive with no “to”)
		// .get(x)[1] = (First)
		// .get(x)[2] = (Second)
		// .get(x)[3] = (Third)
		// .get(x)[4] = (Plural First)
		// .get(x)[5] = (Plural Second)
		// .get(x)[6] = (Plural Third)
		Verbs = new ArrayList<String[]>();
		Verbs.add(new String[] { "be", "am", "are", "is", "are", "are", "are" });
		Verbs.add(new String[] { "do", "do", "do", "does", "do", "do", "do" });
		Verbs.add(new String[] { "eradicate", "eradicate", "eradicate",
				"eradicates", "eradicate", "eradicate", "eradicate" });
		Verbs.add(new String[] { "feel", "feel", "feel", "feels", "feel",
				"feel", "feel" });
		Verbs.add(new String[] { "go", "go", "go", "goes", "go", "go", "go" });
		Verbs.add(new String[] { "have", "have", "have", "has", "have", "have",
				"have" });
		Verbs.add(new String[] { "own", "own", "own", "owns", "own", "own",
				"own" });
		Verbs.add(new String[] { "possess", "possess", "possesses", "possess",
				"possess", "possess", "possess" });
		Verbs.add(new String[] { "seek", "seek", "seek", "seeks", "seek",
				"seek", "seek" });
		Verbs.add(new String[] { "sell", "sell", "sell", "sells", "sell",
				"sell", "sell" });
		Verbs.add(new String[] { "want", "want", "want", "wants", "want",
				"want", "want" });
	}

	public void pStart() {
		imageInit();
		st.add("Ice wagon flew");
		st.add("Witho Yoh Glow");
		drawAll();
	}

	void drawAll() {
		// have text box read from a string[]
		newTextBox(st, 10, 10, 340, 0);

		g.setColor(new Color(224, 224, 224));
		g.fillRect(singleButton[0][0], singleButton[0][1], singleButton[0][2],
				singleButton[0][3]);

		int[] stNums = converter(sb.toString());
		// System.out.println("stNums.l: " + stNums.length);
		int xIndent = 0;
		for (int le = 0; le < stNums.length; le++) {
			g.drawImage(txtAr[stNums[le]], singleButton[0][0] + xIndent,
					singleButton[0][1] + 1, null);
			xIndent += fonts[0][0];
		}

		boolean allGood = false;
		for (int c = 0; c < cursor.length; c++) {
			if (cursor[c] != -1) {
				allGood = true;
			}
		}
		if (allGood) {
			g.setColor(Color.BLACK);
			g.fillRect(singleButton[0][0] + (fonts[0][0] * cursor[1]) - 1,
					singleButton[0][1] + 2, 2, 14);
		}
	}

	// draw a single text box
	void newTextBox(ArrayList<String> stAl, int x, int y, int width, int font) {
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
						lineS = appendIntAr(lineS, lettersThisLine);
						lettersThisLine = 0;
						stCollect.append(words[w] + " ");
						lettersThisLine += wordL + 1;
					}
				}
				row += 1;
				lineS = appendIntAr(lineS, lettersThisLine);
				lettersThisLine = 0;
			}
			// draws box.
			g.setColor(new Color(224, 224, 224));
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
					g.drawImage(txtAr[stNums[le + letsDrawn]], x + xIndent, y
							+ (r * (fonts[font][1] + spaceBetweenRows)), null);

					xIndent += fonts[font][0];
				}
				letsDrawn += lineS[r];
			}
			y += row * 18;
		}
	}

	// leaves a period before and after each string
	String[] parseString(String st) {
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
			}

		}
		return nw;
	}

	int[] appendIntAr(int[] st, int appendage) {
		int[] temp = new int[st.length + 1];
		for (int a = 0; a < st.length; a++) {
			temp[a] = st[a];
		}
		temp[temp.length - 1] = appendage;
		return temp;
	}

	void inputTheBar() {
		String app = sb.toString();
		st.add(app);
		sb.delete(0, sb.length());
		cursor[1] = 0;
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
		txtAr = new Image[70];
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
	}

	// [0] = input bar
	// [1] = letter at (x).
	int[] cursor = { -1 };

	StringBuilder sb = new StringBuilder();

	@Override
	public void mousePressed(MouseEvent me) {
		if (me.getX() > singleButton[0][0]
				&& me.getX() < singleButton[0][0] + singleButton[0][2]
				&& me.getY() > singleButton[0][1]
				&& me.getY() < singleButton[0][1] + singleButton[0][2]) {
			System.out.println("oneButton");
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

	boolean shiftP;

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
				} else if (ke.getKeyCode() == KeyEvent.VK_SLASH) {
					sb.insert(cursor[1], "?");
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
				} else if (ke.getKeyCode() == KeyEvent.VK_SLASH) {
					sb.insert(cursor[1], "/");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_COMMA) {
					sb.insert(cursor[1], ",");
					cursor[1]++;
				} else if (ke.getKeyCode() == KeyEvent.VK_PERIOD) {
					sb.insert(cursor[1], ".");
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
