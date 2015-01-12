package Auto.leaf;

import java.util.ArrayList;

public class Decypher {

	private static String[][] bigTree = { { "Noun" }, { "Pronoun" },
			{ "Question" }, { "Verb" }, { "Adverb" }, { "Article" },
			{ "Adjective" }, { "Noun" }, { "Verb" }, { "Noun" }, { "Pronoun" },
			{ "Verb" }, { "Verb" }, { "Adjective" }, { "Noun" },
			{ "Adjective" }, { "Verb" }, { "Adjective" }, { "Verb" },
			{ "Noun" }, { "Adverb" }, { "Adjective" }, { "Noun" },
			{ "Preposition" }, { "Noun" }, { "Pronoun" } };
	private static int[][][] treeGoesTo = { { { 3 } }, { { 3 } }, { { 8 } },
			{ { 4, 13, 5, 14, 18, 25 } }, { { -1 } }, { { 6 } }, { { 7 } },
			{ { -1 } }, { { 10, 9 } }, { { 12 } }, { { 11 } }, { { -1 } },
			{ { -1 } }, { { -1 } }, { { 15 } }, { { 16 } }, { { 17 } },
			{ { -1 } }, { { 19, 20, 21 } }, { { -1 } }, { { -1 } }, { { 22 } },
			{ { 23 } }, { { 24 } }, { { -1 } }, { { 15 } } };
	private static int[] endWhen = { -1, -1, -1, -1, -4, -1, -1, -8, -1, -1,
			-1, -5, -5, -2, -3, -1, -1, -6, -1, -7, -1, -7, -1, -1, -11, -3 };

	private static int[] treeStart = { 0, 1, 2 };
	private static int[] intTree = { 0, 1, 7, 2, 4, 5, 3, 0, 2, 0, 1, 2, 2, 3,
			0, 3, 2, 3, 2, 0, 4, 3, 0, 6, 0, 1 };
	private static int[][] treeGoesToD = { { 3 }, { 3 }, { 8 },
			{ 4, 13, 5, 14, 18, 25 }, { -1 }, { 6 }, { 7 }, { -1 }, { 10, 9 },
			{ 12 }, { 11 }, { -1 }, { -1 }, { -1 }, { 15 }, { 16 }, { 17 },
			{ -1 }, { 19, 20, 21 }, { -1 }, { -1 }, { 22 }, { 23 }, { 24 },
			{ -1 }, { 15 } };

	// Part of speech.
	// 0 = Noun
	// 1 = Pronoun
	// 2 = Verb
	// 3 = Adjective
	// 4 = Adverb
	// 5 = Article
	// 6 = Preposition
	// 7 = Question
	// 8 = Proper noun

	public static void decypher(String st) {
		// Split into words by spaces.
		String[] words = st.split("[ ]");
		// .get(x)[0] = pos int
		ArrayList<int[]> pos = new ArrayList<int[]>();
		// Run through each word.
		int currentPos = -1;
		for (int w = 0; w < words.length; w++) {
			// Identify each part of speech.
			// Save to an int[].
			// Do I want to combine words now or later?
			// I think now because the pos adds ambiguity at this point.
			pos.add(givePos(words[w]));
			currentPos += 1;
			if (pos.get(currentPos)[0] == -1) {
				System.out.print("[" + words[w] + "] ");
			} else {
				System.out.print("(" + words[w] + ") ");
			}
			// If the last word was "to" and the new word is a lemma of a verb.
			// Then combine those two words into a verb.
			// If the last word was an article and the new word is a noun.
			// Then combine those two words into a noun.
			if (w > 0) {
				// Check to see if the verb is in the infinitive.
				// if this pos is a verb and the last pos is "to" and this verb
				// is potentially in the infinitive
				if (pos.get(currentPos)[0] == 2 && words[w - 1].equals("to")
						&& pos.get(currentPos)[1] == 0) {
					// if (Panel.words.get(nums[z])[3].equals("Infinitive")) {
					pos.remove(currentPos - 1);
					currentPos -= 1;
				}
				// If this pos is a noun, last pos is an article
				if (pos.get(currentPos)[0] == 0
						&& pos.get(currentPos - 1)[0] == 5) {
					pos.remove(currentPos - 1);
					currentPos -= 1;
				}
			}
		}
		System.out.println();
		for (int a = 0; a < pos.size(); a++) {
			System.out.print("pos(" + a + ")");
			for (int b = 0; b < pos.get(a).length; b++) {
				System.out.print(" [" + pos.get(a)[b] + "]");
			}
			System.out.println("");
		}
		// Broke the words into their part of speech.
		// Now what?
		// Follow tree.

		// Run through tree start and check to see if it follows any branches
		// I need to convert the string POS to ints.

		int lastBranch = -1;
		int[] nextBranches = treeStart;
		for (int w = 0; w < pos.size(); w++) {
			// If the branch continues on or reaches its end.
			// boolean up = false;
			loopb: for (int i = 0; i < nextBranches.length; i++) {
				if (nextBranches[i] != -1) {
					if (intTree[nextBranches[i]] == pos.get(w)[0]) {
						lastBranch = nextBranches[i];
						nextBranches = treeGoesToD[nextBranches[i]];
						break loopb;
						// Break should be unnecessary if the tree does not have
						// bugs.
						// Not necessary but reduces iterations.
					}
				}
			}
		}
		if (lastBranch != -1) {
			System.out.println("Ending: " + endWhen[lastBranch]);
		}
		// for (int i = 0; i < nextBranches.length; i++) {
		// System.out.println(i + ": " + nextBranches[i]);
		// }
	}

	// Bug. It compares the POS of the current word to the nextBranch. POS is -1
	// if the word is unknown. nextBranch is -1 if there is no branch.

	private static int[] givePos(String st) {
		// [0] = the part of speech.
		// [1] = the first tense it satisfies.
		// [2] = the second tense
		// [3] = etc... These are set from 0 to 6
		// If the first letter is upper case then return it as a proper noun.
		if (st.length() > 0) {
			if (Character.isUpperCase(st.charAt(0))) {
				return new int[] { 8 };
			}
		}
		// Searches through all the words
		for (int a = 0; a < Panel.words.size(); a++) {
			// If this iteration is the word
			if (st.equals(Panel.words.get(a)[0])) {
				// Return an int according to the part of speech key.
				switch (Panel.words.get(a)[1]) {
				case "Noun":
					return new int[] { 0 };
				case "Pronoun":
					return new int[] { 1 };
				case "Verb":
					return new int[] { 2 };
				case "Adjective":
					return new int[] { 3 };
				case "Adverb":
					return new int[] { 4 };
				case "Article":
					return new int[] { 5 };
				case "Preposition":
					return new int[] { 6 };
				case "Question":
					return new int[] { 7 };
				case "Proper Noun":
					return new int[] { 8 };
				}
			}
		}
		boolean[] tensesSatisfied = new boolean[7];
		for (int a = 0; a < Panel.verbs.size(); a++) {
			boolean thisLemma = false;
			for (int b = 0; b < Panel.verbs.get(a).length; b++) {
				if (st.equals(Panel.verbs.get(a)[b])) {
					thisLemma = true;
					tensesSatisfied[b] = true;
					// return new int[] { a };

					// Check how many tenses it satisfies. and make an int with
					// a length of that +1.
					// [0] = a;
					// [1...] = the tenses it satisfies

					// b is the tense.
					// Now I know theis word is under this lemma so continue on
					// until the end of b and count/ save which tenses match.
					// Either save the data into an ArrayList<Integer>. OR
					// a boolean[] of 6 length that saves for each tense. OR
					// an int[] that get appended and a new one created with
					// each addition.
				}
			}
			// After it runs throught the tenses if the lemma is found then
			// compile the POS correct tense to an int[] to be returned.
			if (thisLemma) {
				// This a is found to be correct lemma.
				int totalTrue = 0;
				for (boolean t : tensesSatisfied) {
					if (t) {
						totalTrue += 1;
					}
				}
				int[] ret = new int[totalTrue + 1];
				ret[0] = 2;
				int at = 1;
				for (int t = 0; t < tensesSatisfied.length; t++) {
					if (tensesSatisfied[t]) {
						ret[at] = t;
						at += 1;
					}
				}
				return ret;
			}
		}
		return new int[] { -1 };
	}

	static int[] numOfWordNew(String st) {
		// {-2, -1} proper noun
		// {-1, -1) unknown
		// {-1, x} words.get(x)
		// {a, b} verbs.get(a)[b]

		// If its not a non-verb then check through the verbs.

		// This find the number (from wordsAL) of the string plugged
		int numb = -1;
		if (st.length() > 0) {
			if (Character.isUpperCase(st.charAt(0))) {
				System.out.println("Cap: " + st);
				return new int[] { -2, -1 };
			}
		}
		for (int a = 0; a < Panel.words.size(); a++) {
			// System.out.println("wordPlugged: |" + st + "|    |" +
			// words.get(a)[0] + "|");
			if (st.equals(Panel.words.get(a)[0])) {
				// numb = a;
				return new int[] { -1, a };
			}
		}
		if (numb == -1) {
			// search through verbs
			verbSearch(st);
			return new int[] { -2, -1 };
		}
		// return numb;
		return new int[] { -1, -1 };
	}

	static int numOfWord(String st) {
		// {-2, -1} proper noun
		// {-1, -1) unknown
		// {-1, x} words.get(x)
		// {a, b} verbs.get(a)[b]

		// If its not a non-verb then check through the verbs.

		// This find the number (from wordsAL) of the string plugged
		int numb = -1;
		if (st.length() > 0) {
			if (Character.isUpperCase(st.charAt(0))) {
				System.out.println("Cap: " + st);
				return -2;
			}
		}
		for (int a = 0; a < Panel.words.size(); a++) {
			// System.out.println("wordPlugged: |" + st + "|    |" +
			// words.get(a)[0] + "|");
			if (st.equals(Panel.words.get(a)[0])) {
				numb = a;
			}
		}
		if (numb == -1) {
			// search through verbs
			verbSearch(st);
		}
		return numb;
	}

	static int[] verbSearch(String st) {
		for (int a = 0; a < Panel.verbs.size(); a++) {
			for (int b = 0; b < Panel.verbs.get(a).length; b++) {
				if (st.equals(Panel.verbs.get(a)[b])) {
					return new int[] { a, b };
				}
			}
		}
		return new int[] { -1, -1 };
	}

}
