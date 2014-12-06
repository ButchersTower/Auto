package Auto.stem;

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
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class PanelOne extends JPanel implements Runnable, KeyListener,
		MouseListener {

	// Want to add cursor to the input line.
	// Input line needs to have capitals and such.

	// "// UNNECESSARY" marks some of the shit that is unnecessary

	// Make a method like numOfWord() that returns the part of speech of the
	// word

	// If there is a greeting with more after then just cut off the greeting and
	// analyze them separately

	// if (words.get(plugNums[0])[0].equals("i")) {
	// firstNoun = "player";
	// } Should set to the (String name = "player";)

	// Any verb that dignified possession should be saved under the same
	// category.

	// Use the height of each ArrayList<String> of the text box to make sure it
	// doesn't go below 410.

	// When asking what they want list all of the things they want.

	// / Have memory and reciting pretty good.

	int width = 420;
	int height = 450;

	Image[] txtAr;;

	Thread thread;
	Image image;
	Graphics g;

	// Vars for gLoop Below
	public int tps = 20;
	public int milps = 1000 / tps;
	long lastTick = 0;
	int sleepTime = 0;
	long lastSec = 0;
	int ticks = 0;
	long startTime;
	long runTime;
	private long nextTick = 0;
	private boolean running = false;

	// Vars for gLoop Above

	int[][] butts = { { 10, 420, 400, 17, 2, 0 } };

	int selectedZone = 0;

	ArrayList<String> consolChat = new ArrayList<String>();

	int convoChartStep = -1;

	String[] convoChart = { "Greeting", "Greeting" };

	String[] possVerbs = { "to have", "to own", "to possess" };

	String[] conversers = { "player", "computer" };

	ArrayList<Character> chatLine = new ArrayList<Character>();

	public Panel() {
		super();

		addKeyListener(this);
		addMouseListener(this);

		setPreferredSize(new Dimension(width, height));
		setFocusable(true);
		requestFocus();
	}

	public void addNotify() {
		super.addNotify();
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}

	public void run() {
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		this.setSize(new Dimension(width, height));

		startTime = System.currentTimeMillis();

		gStart();
	}

	/**
	 * Methods go below here.
	 * 
	 */

	public void gStart() {
		imageInit();

		initWords();
		initVerbs();
		initCharacters();
		createChatTrees();

		outPutText("(Sys) only use capital letters for proper nouns");
		// running = true;
		// gLoop();
	}

	public void gLoop() {
		while (running) {
			// Do the things you want the gLoop to do below here

			// And above here.
			drwGm();

			ticks++;
			// Runs once a second and keeps track of ticks;
			// 1000 ms since last output
			if (timer() - lastSec > 1000) {
				if (ticks < tps - 1 || ticks > tps + 1) {
					if (timer() - startTime < 2000) {
						System.out.println("Ticks this second: " + ticks);
						System.out.println("timer(): " + timer());
						System.out.println("nextTick: " + nextTick);
					}
				}

				ticks = 0;
				lastSec = (System.currentTimeMillis() - startTime);
			}

			// Used to protect the game from falling beind.
			if (nextTick < timer()) {
				nextTick = timer() + milps;
			}

			// Limits the ticks per second
			if (timer() - nextTick < 0) {
				sleepTime = (int) (nextTick - timer());
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
				}

				nextTick += milps;
			}
		}
	}

	// // actual memory
	// // .get(x)[0] = StatusTypes[0];
	// // .get(x)[1] = Conditions[0];
	// ArrayList<int[]> Statuses;
	// // databases
	// // .get(x)[0] = Condition;
	// // .get(x)[1] = definition;
	// ArrayList<String[]> Conditions;
	// // .get(x)[0] = Status Type Name;
	// // .get(x)[1] = Explanation;
	// ArrayList<String[]> StatusTypes;

	String[] bigTree = { "Noun", "Verb", "To be", "Noun", "Verb", "To be",
			"Adjective", "Adjective", "Adjective", "Adjective", "Noun",
			"Preposition", "Noun" };

	// 0 is start
	// int[][] treeGoesTo = { { 1 }, { 2, 3, 4 }, { 6 }, { 7 }, { 5, 8 }, { 9 },
	// { -1 }, { -1 }, { 10 }, { 11 }, { 12 }, { -1 } };
	int[][] treeGoesTo = { { 1 }, { 2, 3, 4 }, { 6 }, { 7 }, { 5, 8 }, { 9 },
			{ -1 }, { -1 }, { 10 }, { -1 }, { 11 }, { 12 }, { -1 } };

	// If there is a (-1) then if the tree ends there there is something to do.

	ArrayList<String[]> words;

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

	ArrayList<String[]> Verbs;

	void initVerbs() {
		// .get(x)[0] = (Infinitive with no “to”)
		// .get(x)[1] = (First)
		// .get(x)[2] = (Second)
		// .get(x)[3] = (Third)
		// .get(x)[4] = (Plural First)
		// .get(x)[5] = (Plural Second)
		// .get(x)[6] = (Plural Third)
		Verbs = new ArrayList<String[]>();
		Verbs.add(new String[] { "possess", "possess", "possesses", "possess",
				"possess", "possess", "possess" });
		Verbs.add(new String[] { "want", "want", "want", "wants", "want",
				"want", "want" });
		Verbs.add(new String[] { "have", "have", "have", "has", "have", "have",
				"have" });
		Verbs.add(new String[] { "be", "am", "are", "is", "are", "are", "are" });
		Verbs.add(new String[] { "own", "own", "own", "owns", "own", "own",
				"own" });
		Verbs.add(new String[] { "do", "do", "do", "does", "do", "do", "do" });
		Verbs.add(new String[] { "go", "go", "go", "goes", "go", "go", "go" });
		Verbs.add(new String[] { "feel", "feel", "feel", "feels", "feel",
				"feel", "feel" });
		Verbs.add(new String[] { "go", "go", "go", "goes", "go", "go", "go" });
		Verbs.add(new String[] { "sell", "sell", "sell", "sells", "sell",
				"sell", "sell" });
	}

	public void txtBox(Graphics g, int wi, int hi, int font, int xl, int yl,
			ArrayList<String> st, boolean drwBkg) {
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

	void botBar() {
		String werds = getStringRepresentation(chatLine);
		ArrayList<String> tempChatBar = new ArrayList<String>();
		tempChatBar.add(werds);
	}

	// Converts ArrayList<Character> to String
	String getStringRepresentation(ArrayList<Character> list) {
		StringBuilder builder = new StringBuilder(list.size());
		for (Character ch : list) {
			builder.append(ch);
		}
		return builder.toString();
	}

	void interpretChars() {
		if (consolChat.size() + 1 > ((400 - (400 % 16)) / 16)) {
			consolChat.remove(0);
		}
		String werds = getStringRepresentation(chatLine);
		ArrayList<String> tempChatBar = new ArrayList<String>();
		consolChat.add("(P) " + werds);
		chatLine.clear();
		computerIn(werds);
	}

	void newInterpret() {
		if (consolChat.size() + 1 > ((400 - (400 % 16)) / 16)) {
			consolChat.remove(0);
		}
		String werds = getStringRepresentation(chatLine);
		ArrayList<String> tempChatBar = new ArrayList<String>();
		consolChat.add("(P) " + werds);
		chatLine.clear();
		contInterpret(werds);
	}

	void contInterpret(String st) {
		// Break st into words by the spaces.
		String[] wordsIn = st.split("[ ]");

		// int[][] nums = [numberOfWords][2]
		// [x][0] = number of word in (wordsAL)
		// [x][1] = tense.

		// For tense (0 = infinitive) (1 - 6) normal (7 == future)
		int[] nums = new int[wordsIn.length];

		// This run through nums and sees if it follows the newTree

		// Forget about (Article) if it prefaces a (Noun)
		int y = -1;
		for (int z = 0; z < nums.length; z++) {
			y++;
			nums[z] = numOfWord(wordsIn[y]);
			// If there is an infinitive verb following "to" then it removes
			// "to" and keeps the verb in the infinitive
			// ("to be" + "infinitive verb") -> verb
			// (Art + Noun) -> Noun
			if (z > 0 && nums[z] != -1 && nums[z - 1] != -1) {
				if (words.get(nums[z])[1].equals("Verb")) {
					if (words.get(nums[z])[3].equals("Infinitive")) {
						if (nums[z - 1] == numOfWord("to")) {
							// leave this word the same but remove (nums[z-1])
							nums = shortenIntAR(nums, z - 1);
							z--;
						}
					}
				} else if (words.get(nums[z])[1].equals("Noun")) {
					// If this one is a (N) and the one before is an (Art)
					if (words.get(nums[z - 1])[1].equals("Article")) {
						nums = shortenIntAR(nums, z - 1);
						z--;
					}
				}
			}
		}
		boolean understandAll = true;
		for (int i = 0; i < nums.length; i++) {
			if (nums[i] != -1) {
				// UNNECESSARY
				System.out.print("(" + words.get(nums[i])[1] + ")");
			} else {
				understandAll = false;
			}
		}
		System.out.println();
		if (!understandAll) {
			outPutText("(C) MisUnderstood Negative One");
			return;
		}
		// If it does not find a match for that 'i' then end the tree search.
		// Follows the tree
		int[] possableNext = { 0 };
		int lastBranch = -1;
		loopa: for (int i = 0; i < nums.length; i++) {
			boolean matchHere = false;
			// Runs through the possable next and sees if it matches
			loopp: for (int p = 0; p < possableNext.length; p++) {
				if (possableNext[p] != -1) {
					// System.out.println("bigTre[" + possableNext[p] + "[" + p
					// + "]]: " + bigTree[possableNext[p]] + "  |  words[" + i +
					// "] " + words.get(nums[i])[1]);
					if (words.get(nums[i])[1].equals(bigTree[possableNext[p]])) {
						System.out.println("pNp: " + possableNext[p]);
						lastBranch = possableNext[p];
						possableNext = treeGoesTo[possableNext[p]];
						matchHere = true;
						break loopp;
					}
				}
			}
			if (!matchHere) {
				System.out.println("noMatch, word " + i);
				break loopa;
			}
		}
		System.out.println("lASTbRANCH: " + lastBranch);
	}

	void outPutText(String st) {
		if (consolChat.size() + 1 > ((400 - (400 % 16)) / 16)) {
			consolChat.remove(0);
		}
		consolChat.add(st);
	}

	void drawAll() {
		String werds = getStringRepresentation(chatLine);
		ArrayList<String> tempChatBar = new ArrayList<String>();
		tempChatBar.add(werds);
		g.setColor(Color.BLUE);
		txtBox(g, 400, 400, 0, 10, 10, consolChat, true);
		g.setColor(Color.CYAN);
		txtBox(g, 400, 17, 0, 10, 420, tempChatBar, true);
		drwGm();
	}

	void computerIn(String st) {
		// Break st into words by the spaces.
		String[] wordsIn = st.split("[ ]");

		int[] nums = new int[wordsIn.length];
		// When analyzing a line find the parts of speech so you can
		// appropriately edit variable like who has or wants what.

		// If there is an infinitive verb following "to" then it removes
		// "to" and keeps the verb in the infinitive
		int y = -1;
		for (int z = 0; z < nums.length; z++) {
			y++;
			nums[z] = numOfWord(wordsIn[y]);
			// If there is an infinitive verb following "to" then it removes
			// "to" and keeps the verb in the infinitive
			if (z > 0 && nums[z] != -1 && nums[z - 1] != -1) {
				if (words.get(nums[z])[1].equals("Verb")) {
					if (words.get(nums[z])[3].equals("Infinitive")) {
						if (nums[z - 1] == numOfWord("to")) {
							// leave this word the same but remove (nums[z-1])
							nums = shortenIntAR(nums, z - 1);
							z--;
						}
					}
				}
			}
		}
		// UNNECESSARY
		for (int z = 0; z < nums.length; z++) {
			System.out.println("wordNum[" + z + "]: " + nums[z]);
		}
		// Has interpreted the input into an array of its definitions
		// take the first word
		// IT should be like: If there is only one word and it is a greeting
		// then respond with a greeting
		boolean respond = false;
		if (nums[0] != -1) {
			if (words.get(nums[0])[1].equals("Interjection")) {
				if (words.get(nums[0])[2].equals("Greeting")) {
					// 0 in nums because it is the first word
					// respond with greeting
					outPutText("(C) Hi");
					respond = true;
				}
			}
		}
		// this analizes the text
		// if it doesnt understand it doesnt output "MisUnderstood" unless it
		// didnt output anything else.
		boolean wordsMakeSense = true;
		for (int i = 0; i < nums.length; i++) {
			if (nums[i] == -1) {
				wordsMakeSense = false;
			}
		}
		if (wordsMakeSense) {
			if (analizeText(nums)) {
				respond = true;
			}
		}
		if (!respond) {
			outPutText("(C) MisUnderstood");
		}
	}

	int numOfWord(String st) {
		// This find the number (from wordsAL) of the string plugged
		int numb = -1;
		for (int a = 0; a < words.size(); a++) {
			// System.out.println("wordPlugged: |" + st + "|    |" +
			// words.get(a)[0] + "|");
			if (st.equals(words.get(a)[0])) {
				numb = a;
			}
		}
		return numb;
	}

	ArrayList<String[]> sentenceTrees;

	void createChatTrees() {
		sentenceTrees = new ArrayList<String[]>();
		sentenceTrees.add(new String[] { "Noun", "Verb", "Adjective" });
		sentenceTrees.add(new String[] { "Pronoun", "Verb", "Adjective" });
		sentenceTrees.add(new String[] { "Noun", "Verb", "Noun" });
		sentenceTrees.add(new String[] { "Pronoun", "Verb", "Noun" });
		sentenceTrees.add(new String[] { "Noun", "Verb", "Article", "Noun" });
		sentenceTrees
				.add(new String[] { "Pronoun", "Verb", "Article", "Noun" });
		sentenceTrees.add(new String[] { "Noun", "Verb", "Adverb" });
		sentenceTrees.add(new String[] { "Pronoun", "Verb", "Adverb" });
		sentenceTrees.add(new String[] { "Question", "Verb", "Noun", "Verb" });
		sentenceTrees
				.add(new String[] { "Question", "Verb", "Pronoun", "Verb" });
		// Extension of tree 2 through 5.
		sentenceTrees.add(new String[] { "Noun", "Verb", "Article", "Noun",
				"Adjective", "Verb", "Adjective" });
		sentenceTrees.add(new String[] { "Pronoun", "Verb", "Article", "Noun",
				"Adjective", "Verb", "Adjective" });

		sentenceTrees.add(new String[] { "Noun", "Verb", "Verb", "Noun" });
		sentenceTrees.add(new String[] { "Pronoun", "Verb", "Verb", "Noun" });
		sentenceTrees.add(new String[] { "Noun", "Verb", "Verb", "Adjective" });
		sentenceTrees
				.add(new String[] { "Pronoun", "Verb", "Verb", "Adjective" });

		// Extension of tree 2 through 5. and 10 and 11.
		sentenceTrees.add(new String[] { "Noun", "Verb", "Article",
				"Adjective", "Noun" });
		sentenceTrees.add(new String[] { "Pronoun", "Verb", "Article",
				"Adjective", "Noun" });

	}

	boolean analizeText(int[] plugNums) {
		// Run through all the sentenceTrees, test if they match up one word at
		// a time. If they do then react the way the tree tells you to.
		int tree = -1;
		int bee = -1;

		for (int a = 0; a < sentenceTrees.size(); a++) {
			if (plugNums.length == sentenceTrees.get(a).length) {
				// if they have the same number of words
				// and if the words are the same
				boolean allSame = true;
				// runs through all the words and sees if their part of speach
				// matches.
				for (int b = 0; b < plugNums.length; b++) {
					if (words.get(plugNums[b])[1]
							.equals(sentenceTrees.get(a)[b])) {
					} else {
						// if they don't match
						allSame = false;
					}
				}
				if (allSame) {
					tree = a;
				}
			}
		}
		// UNNECESSARY
		System.out.print("tree[" + tree + "]\t");
		for (int i = 0; i < plugNums.length; i++) {
			System.out.print("(" + words.get(plugNums[i])[1] + ")");
		}
		System.out.println();
		if (tree != -1) {
			// If the first word is a pronoun then you need to figure out how to
			// specifically organize it based on the converstaion that has been
			// going on
			// how to remember what to do according to each tree;
			String firstNoun = words.get(plugNums[0])[0];
			if (tree == 0 || tree == 1) {
				tree0n1(plugNums);
			} else if (tree == 2 || tree == 3) {
				tree2n3(plugNums);
			} else if (tree == 4 || tree == 5) {
				tree4n5(plugNums);
			} else if (tree == 6 || tree == 7) {
				if (words.get(plugNums[0])[0].equals("i")) {
					firstNoun = "player";
				}
				// If the second word is a possVerbs then set possession
				characters.get(1).appendMemory(firstNoun,
						words.get(plugNums[1])[2], words.get(plugNums[2])[2]);
			} else if (tree == 8 || tree == 9) {
				tree8n9(plugNums);
			} else if (tree == 10 || tree == 11) {
				tree10n11(plugNums);
			} else if (tree == 12 || tree == 13 || tree == 14 || tree == 15) {
				tree12n13(plugNums);
			} else if (tree == 16 || tree == 17) {
				tree16n17(plugNums);
			}
			return true;
		} else {
			return false;
		}
	}

	void tree0n1(int[] plugNums) {
		// (Noun) (Verb) (Adjective)
		// (Pronoun) (Verb) (Adjective)
		String firstNoun = words.get(plugNums[0])[0];
		String verb = words.get(plugNums[1])[2];
		String[] save = { words.get(plugNums[2])[2] };
		// This doesn't need to worry about possession, (subject, verb,
		// adjective) "I have tired" doesn't make sense.
		// If the Verb is not "to be" then it doesn't make sense
		if (words.get(plugNums[0])[0].equals("i")) {
			firstNoun = "player";
		}
		characters.get(1).newSave(firstNoun, verb, save);
	}

	void tree2n3(int[] plugNums) {
		// (Noun) (Verb) (Noun)
		// (Pronoun) (Verb) (Noun)
		String firstNoun = words.get(plugNums[0])[0];
		if (words.get(plugNums[0])[0].equals("i")) {
			firstNoun = "player";
		} else if (words.get(plugNums[0])[0].equals("you")) {
			firstNoun = "computer";
		}
		String verb = words.get(plugNums[1])[2];
		String[] save = { words.get(plugNums[2])[2] };
		characters.get(1).newSave(firstNoun, verb, save);
		// boolean possession = false;
		// for (int a = 0; a < possVerbs.length; a++) {
		// if (words.get(plugNums[1])[2].equals(possVerbs[a])) {
		// characters.get(1).addToList(firstNoun, "to possess",
		// words.get(plugNums[2])[2]);
		// possession = true;
		// } }
		// if (!possession) {
		// characters.get(1).appendMemory(firstNoun,
		// words.get(plugNums[1])[2], words.get(plugNums[2])[2]);
		// }
	}

	void tree4n5(int[] plugNums) {
		// (Noun) (Verb) (Article) (Noun)
		// (Pronoun) (Verb) (Article) (Noun)
		String firstNoun = words.get(plugNums[0])[0];
		if (words.get(plugNums[0])[0].equals("i")) {
			firstNoun = "player";
		} else if (words.get(plugNums[0])[0].equals("you")) {
			firstNoun = "computer";
		}
		String verb = words.get(plugNums[1])[2];
		String[] save = { words.get(plugNums[3])[2] };
		characters.get(1).newSave(firstNoun, verb, save);

		// boolean possession = false;
		// for (int a = 0; a < possVerbs.length; a++) {
		// if (words.get(plugNums[1])[2].equals(possVerbs[a])) {
		// characters.get(1).addToList(firstNoun, "to possess",
		// words.get(plugNums[3])[2]);
		// possession = true;
		// }
		// }
		// if (!possession) {
		// characters.get(1).appendMemory(firstNoun,
		// words.get(plugNums[1])[2], words.get(plugNums[3])[2]);
		// }
	}

	void tree8n9(int[] plugNums) {
		// Break down (Question) (Verb) (Pronoun) (Verb).
		String person = words.get(plugNums[2])[0];
		if (words.get(plugNums[2])[0].equals("i")) {
			person = "player";
		} else if (words.get(plugNums[2])[0].equals("you")) {
			person = "computer";
		}
		if (words.get(plugNums[0])[0].equals("what")) {
			if (words.get(plugNums[1])[2].equals("to do")) {
				// System.out.println("|" + person + "|    |"
				// + words.get(plugNums[3])[2] + "|");
				// Don't need to check the pronoun because it will just make a
				// new 'database if it does not know the person yet.
				// characters.get(1)
				// .tellMemmory(person, words.get(plugNums[3])[2]);
				characters.get(1).tellNew(person, words.get(plugNums[3])[2]);
			}
		}
	}

	void tree10n11(int[] plugNums) {
		// (Pronoun) (Verb) (Article) (Noun) (Adjective) (Verb) (Adjective)
		// I want a cat that is cool

		String firstNoun = words.get(plugNums[0])[0];
		// if the verb is a possession verb then set possession
		if (words.get(plugNums[0])[0].equals("i")) {
			firstNoun = "player";
		} else if (words.get(plugNums[0])[0].equals("you")) {
			firstNoun = "computer";
		}

		String verb = words.get(plugNums[1])[2];
		String noun = words.get(plugNums[3])[2];
		String adjective = words.get(plugNums[6])[2];

		if (words.get(plugNums[4])[2].equals("that")) {
			if (words.get(plugNums[5])[2].equals("to be")) {
				System.out.println("|" + firstNoun + "| |" + verb + "| |"
						+ noun + "| |" + adjective + "|");
				String[] tempAr1 = { noun, adjective };
				characters.get(1).newSave(firstNoun, verb, tempAr1);
			}
		}

		// // If the second word is a possVerbs then set possession
		// boolean possession = false;
		// for (int a = 0; a < possVerbs.length; a++) {
		// if (words.get(plugNums[1])[2].equals(possVerbs[a])) {
		// // characters.get(1).addToList(firstNoun, "to possess",
		// // words.get(plugNums[2])[2]);
		// possession = true;
		// }
		// }
		// if (!possession) {
		// characters.get(1).appendMemory(firstNoun,
		// words.get(plugNums[1])[2], words.get(plugNums[2])[2]);
		// }
	}

	void tree12n13(int[] plugNums) {
		// (Noun) (Verb) (Verb) (Noun)
		// (Pronoun) (Verb) (Verb) (Noun)
		// "i want to go home" -> (player) (to want) (to go) (home)
		// Double verb, second one should be checked make sure it was infinitive
		// "what do i want"?
		// -"you want to go home"
		// "how do i feel about home"?
		// "what is my opinion about home"?
		// -"you want to go there"
		// "where do i want to go"

		// When saving (verb) (verb) assume the second one is infinitive
		String firstNoun = words.get(plugNums[0])[0];
		// if the verb is a possession verb then set possession
		if (words.get(plugNums[0])[0].equals("i")) {
			firstNoun = "player";
		} else if (words.get(plugNums[0])[0].equals("you")) {
			firstNoun = "computer";
		}
		String verb1 = words.get(plugNums[1])[2];
		String verb2 = words.get(plugNums[2])[2];
		String noun = words.get(plugNums[3])[2];
		String[] save = { verb2, noun };
		characters.get(1).newSave(firstNoun, verb1, save);
		// System.out.println(firstNoun + " " + verb1 + " " + verb2 + " " + noun
		// + ".");
	}

	void tree16n17(int[] plugNums) {
		// (Noun) (Verb) (Article) (Adjective) (Noun)
		// (Pronoun) (Verb) (Article) (Adjective) (Noun)
		// "i want a cool cat"
		String firstNoun = words.get(plugNums[0])[0];
		// if the verb is a possession verb then set possession
		if (words.get(plugNums[0])[0].equals("i")) {
			firstNoun = "player";
		} else if (words.get(plugNums[0])[0].equals("you")) {
			firstNoun = "computer";
		}

		String verb = words.get(plugNums[1])[2];
		String noun = words.get(plugNums[4])[2];
		String adjective = words.get(plugNums[3])[2];

		// System.out.println("|" + firstNoun + "| |" + verb + "| |" + noun
		// + "| |" + adjective + "|");
		String[] tempAr1 = { noun, adjective };
		characters.get(1).newSave(firstNoun, verb, tempAr1);

	}

	ArrayList<GameCharacter> characters;

	// This version remembers characters by their names, but indepth ones could
	// recognizes thins based on their traits and appearances.
	void initCharacters() {
		characters = new ArrayList<GameCharacter>();
		characters.add(new GameCharacter("player", this));
		characters.add(new GameCharacter("computer", this));
	}

	void lastBranch12(){
		
	}
	
	// This works with any type of array.
	int[] appendIntAR(int[] in, int appendage) {
		int[] temp = new int[in.length + 1];
		for (int a = 0; a < in.length; a++) {
			temp[a] = in[a];
		}
		temp[temp.length - 1] = appendage;
		return temp;
	}

	// This removed the [numToRemove] variable from an AR and compresses
	int[] shortenIntAR(int[] in, int numToRemove) {
		int[] temp = new int[in.length - 1];
		boolean reachedYet = false;
		for (int a = 0; a < in.length; a++) {
			// System.out.println("a: " + a);
			if (a == numToRemove) {
				reachedYet = true;
				a++;
				// System.out.println("newA: " + a);
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

	String[] appendStringAR(String[] in, String appendage) {
		String[] temp = new String[in.length + 1];
		for (int a = 0; a < in.length; a++) {
			temp[a] = in[a];
		}
		temp[temp.length - 1] = appendage;
		return temp;
	}

	// This removed the [numToRemove] variable from an AR and compresses
	String[] shortenStrAR(String[] in, int numToRemove) {
		String[] temp = new String[in.length - 1];
		boolean reachedYet = false;
		for (int a = 0; a < in.length; a++) {
			// System.out.println("a: " + a);
			if (a == numToRemove) {
				reachedYet = true;
				a++;
				// System.out.println("newA: " + a);
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

	/**
	 * Methods go above here.
	 * 
	 */

	public long timer() {
		return System.currentTimeMillis() - startTime;

	}

	public void drwGm() {
		Graphics g2 = this.getGraphics();
		g2.drawImage(image, 0, 0, null);
		g2.dispose();
	}

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
	}

	boolean shiftP = false;
	boolean ctrlP = false;

	@Override
	public void keyReleased(KeyEvent ke) {
		if (ke.getKeyCode() == KeyEvent.VK_SHIFT) {
			shiftP = false;
		} else if (ke.getKeyCode() == KeyEvent.VK_CONTROL) {
			ctrlP = false;
		}
		if (selectedZone == 1) {
			if (shiftP) {
				// Shift pressed
				if (ke.getKeyCode() == KeyEvent.VK_A) {
					chatLine.add('A');
				} else if (ke.getKeyCode() == KeyEvent.VK_B) {
					chatLine.add('B');
				} else if (ke.getKeyCode() == KeyEvent.VK_C) {
					chatLine.add('C');
				} else if (ke.getKeyCode() == KeyEvent.VK_D) {
					chatLine.add('D');
				} else if (ke.getKeyCode() == KeyEvent.VK_E) {
					chatLine.add('E');
				} else if (ke.getKeyCode() == KeyEvent.VK_F) {
					chatLine.add('F');
				} else if (ke.getKeyCode() == KeyEvent.VK_G) {
					chatLine.add('G');
				} else if (ke.getKeyCode() == KeyEvent.VK_H) {
					chatLine.add('H');
				} else if (ke.getKeyCode() == KeyEvent.VK_I) {
					chatLine.add('I');
				} else if (ke.getKeyCode() == KeyEvent.VK_J) {
					chatLine.add('J');
				} else if (ke.getKeyCode() == KeyEvent.VK_K) {
					chatLine.add('K');
				} else if (ke.getKeyCode() == KeyEvent.VK_L) {
					chatLine.add('L');
				} else if (ke.getKeyCode() == KeyEvent.VK_M) {
					chatLine.add('M');
				} else if (ke.getKeyCode() == KeyEvent.VK_N) {
					chatLine.add('N');
				} else if (ke.getKeyCode() == KeyEvent.VK_O) {
					chatLine.add('O');
				} else if (ke.getKeyCode() == KeyEvent.VK_P) {
					chatLine.add('P');
				} else if (ke.getKeyCode() == KeyEvent.VK_Q) {
					chatLine.add('Q');
				} else if (ke.getKeyCode() == KeyEvent.VK_R) {
					chatLine.add('R');
				} else if (ke.getKeyCode() == KeyEvent.VK_S) {
					chatLine.add('S');
				} else if (ke.getKeyCode() == KeyEvent.VK_T) {
					chatLine.add('T');
				} else if (ke.getKeyCode() == KeyEvent.VK_U) {
					chatLine.add('U');
				} else if (ke.getKeyCode() == KeyEvent.VK_V) {
					chatLine.add('V');
				} else if (ke.getKeyCode() == KeyEvent.VK_W) {
					chatLine.add('W');
				} else if (ke.getKeyCode() == KeyEvent.VK_X) {
					chatLine.add('X');
				} else if (ke.getKeyCode() == KeyEvent.VK_Y) {
					chatLine.add('Y');
				} else if (ke.getKeyCode() == KeyEvent.VK_Z) {
					chatLine.add('Z');
				}
			} else if (ctrlP) {
				if (ke.getKeyCode() == KeyEvent.VK_B) {
					characters.get(1).outputNew();
				}
			} else {
				if (ke.getKeyCode() == KeyEvent.VK_A) {
					chatLine.add('a');
				} else if (ke.getKeyCode() == KeyEvent.VK_B) {
					chatLine.add('b');
				} else if (ke.getKeyCode() == KeyEvent.VK_C) {
					chatLine.add('c');
				} else if (ke.getKeyCode() == KeyEvent.VK_D) {
					chatLine.add('d');
				} else if (ke.getKeyCode() == KeyEvent.VK_E) {
					chatLine.add('e');
				} else if (ke.getKeyCode() == KeyEvent.VK_F) {
					chatLine.add('f');
				} else if (ke.getKeyCode() == KeyEvent.VK_G) {
					chatLine.add('g');
				} else if (ke.getKeyCode() == KeyEvent.VK_H) {
					chatLine.add('h');
				} else if (ke.getKeyCode() == KeyEvent.VK_I) {
					chatLine.add('i');
				} else if (ke.getKeyCode() == KeyEvent.VK_J) {
					chatLine.add('j');
				} else if (ke.getKeyCode() == KeyEvent.VK_K) {
					chatLine.add('k');
				} else if (ke.getKeyCode() == KeyEvent.VK_L) {
					chatLine.add('l');
				} else if (ke.getKeyCode() == KeyEvent.VK_M) {
					chatLine.add('m');
				} else if (ke.getKeyCode() == KeyEvent.VK_N) {
					chatLine.add('n');
				} else if (ke.getKeyCode() == KeyEvent.VK_O) {
					chatLine.add('o');
				} else if (ke.getKeyCode() == KeyEvent.VK_P) {
					chatLine.add('p');
				} else if (ke.getKeyCode() == KeyEvent.VK_Q) {
					chatLine.add('q');
				} else if (ke.getKeyCode() == KeyEvent.VK_R) {
					chatLine.add('r');
				} else if (ke.getKeyCode() == KeyEvent.VK_S) {
					chatLine.add('s');
				} else if (ke.getKeyCode() == KeyEvent.VK_T) {
					chatLine.add('t');
				} else if (ke.getKeyCode() == KeyEvent.VK_U) {
					chatLine.add('u');
				} else if (ke.getKeyCode() == KeyEvent.VK_V) {
					chatLine.add('v');
				} else if (ke.getKeyCode() == KeyEvent.VK_W) {
					chatLine.add('w');
				} else if (ke.getKeyCode() == KeyEvent.VK_X) {
					chatLine.add('x');
				} else if (ke.getKeyCode() == KeyEvent.VK_Y) {
					chatLine.add('y');
				} else if (ke.getKeyCode() == KeyEvent.VK_Z) {
					chatLine.add('z');
				} else if (ke.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
					if (chatLine.size() > 0) {
						chatLine.remove(chatLine.size() - 1);
					}
				} else if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
					// Take the charAL and display it on screen and input it for
					// the computer to interpret
					// interpretChars();
					newInterpret();
				} else if (ke.getKeyCode() == KeyEvent.VK_QUOTE) {
					// Non shift so quote is apostrophe
					chatLine.add('\'');
				} else if (ke.getKeyCode() == KeyEvent.VK_SPACE) {
					chatLine.add(' ');
				} else if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
					selectedZone = 0;
				}
			}
			System.out.print("chatLine: ");
			for (int i = 0; i < chatLine.size(); i++) {
				System.out.print(chatLine.get(i));
			}
			System.out.println("");
			drawAll();
		} else {
			if (ke.getKeyCode() == KeyEvent.VK_SPACE) {
				g.setColor(Color.CYAN);
				g.fillRect(butts[0][0], butts[0][1], butts[0][2], butts[0][3]);
				drwGm();
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent ke) {
		if (ke.getKeyCode() == KeyEvent.VK_SHIFT) {
			shiftP = true;
		}
		if (ke.getKeyCode() == KeyEvent.VK_CONTROL) {
			ctrlP = true;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent me) {
		if (me.getX() > butts[0][0]) {
			if (me.getX() < butts[0][0] + butts[0][2]) {
				if (me.getY() > butts[0][1]) {
					if (me.getY() < butts[0][1] + butts[0][3]) {
						System.out.println("butClick");
						selectedZone = 1;
					}
				}
			}
		}
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
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

}
