package Auto.leaf.old;

import java.util.ArrayList;

import Auto.leaf.JaMa;
import Auto.leaf.Panel;

public class DecypherOld {

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

	private static int givePosOld(String st) {
		// This one does not hold the tense of the verb. What if there are
		// multiple tenses for that same word. Do i need to have an Ar of the
		// different tenses this word satisfies.
		// If the first letter is upper case then return it as a proper noun.
		if (st.length() > 0) {
			if (Character.isUpperCase(st.charAt(0))) {
				return 8;
			}
		}
		// Searches through all the words
		for (int a = 0; a < Panel.words.size(); a++) {
			// If this iteration is the word
			if (st.equals(Panel.words.get(a)[0])) {
				// Return an int according to the part of speech key.
				switch (Panel.words.get(a)[1]) {
				case "noun":
					return 0;
				case "Pronoun":
					return 1;
				case "Verb":
					return 2;
				case "Adjective":
					return 3;
				case "Adverb":
					return 4;
				case "Article":
					return 5;
				case "Preposition":
					return 6;
				case "Question":
					return 7;
				case "Proper noun":
					return 8;
				}
			}
		}
		for (int a = 0; a < Panel.verbs.size(); a++) {
			for (int b = 0; b < Panel.verbs.size(); b++) {
				if (st.equals(Panel.verbs.get(a)[b])) {
					return a;
					// b is the tense.
				}
			}
		}
		return -1;
	}

	static void decypherOld(String st) {
		// Break st into words by the spaces.
		String[] wordsIn = st.split("[ ]");

		// int[][] nums = [numberOfWords][2]
		// [x][0] = number of word in (wordsAL)
		// [x][1] = tense.
		// For tense (0 = infinitive) (1 - 6) normal (7 == future)

		// If the word is a proper noun then set it the nums equal to -2.
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
			if (nums[z] == -2) {
				// word starts with a capital letter so count it as a proper
				// noun.

			}
			// Combines infinitive verbs with "to" and articles with nouns.
			if (z > 0 && nums[z] != -1 && nums[z - 1] != -1) {
				if (Panel.words.get(nums[z])[1].equals("Verb")) {
					if (Panel.words.get(nums[z])[3].equals("Infinitive")) {
						if (nums[z - 1] == numOfWord("to")) {
							// leave this word the same but remove (nums[z-1])
							nums = JaMa.shortenIntAR(nums, z - 1);
							z--;
						}
					}
				} else if (Panel.words.get(nums[z])[1].equals("Noun")) {
					// If this one is a (N) and the one before is an (Art)
					if (Panel.words.get(nums[z - 1])[1].equals("Article")) {
						nums = JaMa.shortenIntAR(nums, z - 1);
						z--;
					}
				}
			}
		}
		boolean understandAll = true;
		for (int i = 0; i < nums.length; i++) {
			if (nums[i] != -1) {
				// UNNECESSARY
				System.out.print("(" + wordsIn[i] + ")");
			} else {
				System.out.print(" [Misunderstand (" + wordsIn[i] + ")]");
				understandAll = false;
			}
		}

		System.out.println();
		if (!understandAll) {
			// outPutText("(C) MisUnderstood Negative One");
			Panel.takeIn("(C) MisUnderstood Negative One");
			return;
		}

		// If it does not find a match for that 'i' then end the tree search.
		// Follows the tree
		// 0 and 1 are starting points
		int[] possableNext = { 0, 1, 2 };
		int lastBranch = -1;
		int sub = -1;
		// runs through each word
		loopa: for (int i = 0; i < nums.length; i++) {
			boolean matchHere = false;
			// Runs through the possable next and sees if it matches
			loopp: for (int p = 0; p < possableNext.length; p++) {
				if (possableNext[p] != -1) {
					// System.out.println("bigTre[" + possableNext[p] + "[" + p
					// + "]]: " + bigTree[possableNext[p]] + "  |  words[" + i +
					// "] " + words.get(nums[i])[1]);
					// If the word is noun, this allows it to be noun or
					// pronoun. This is only for the first because i don't know
					// how to make it work for either, WAIT.

					// System.out.println("word: " + words.get(nums[i])[1]
					// + "     tree[" + possableNext[p] + "]"
					// + bigTree[possableNext[p]][0] + "    word: "
					// + words.get(nums[i])[0]);

					boolean isWord = false;
					if ((nums[i]) == -2) {
						if (bigTree[possableNext[p]][0].equals("noun")) {
							isWord = true;
						}
					} else {
						if ((nums[i]) != -1
								&& Panel.words.get(nums[i])[1]
										.equals(bigTree[possableNext[p]][0])) {
							isWord = true;
						}
					}

					System.out.println("nums[" + i + "]: " + nums[i]);
					if (isWord) {
						// The first word equals, if the sub word equals too...
						// Runs through the sub words, if they fit.
						boolean haveSub = false;
						for (int s = 1; s < bigTree[possableNext[p]].length; s++) {
							System.out.println("sub: "
									+ bigTree[possableNext[p]][s]
									+ "     .equal: "
									+ Panel.words.get(nums[i])[2]);
							if (Panel.words.get(nums[i])[2]
									.equals(bigTree[possableNext[p]][s])) {
								haveSub = true;
								// System.out.println("pNp: " +
								// possableNext[p]);
								lastBranch = possableNext[p];
								sub = s;
								possableNext = treeGoesTo[possableNext[p]][s];
								matchHere = true;
								// System.out.println("BREAK P");
								break loopp;
							}
						}
						if (!haveSub) {
							// System.out.println("pNp: " + possableNext[p]);
							lastBranch = possableNext[p];
							sub = 0;
							possableNext = treeGoesTo[possableNext[p]][0];
							matchHere = true;
							// System.out.println("BREAK P");
							break loopp;
						}
					}
				}
			}
			if (!matchHere) {
				System.out.println("noMatch, word " + i);
				System.out.println("BREAK A");
				break loopa;
			}
		}
		if (lastBranch == 3) {
			tree2n3(nums);
		} else if (lastBranch == 5) {
			// (Pro)(V)(Adj)
			tree0n1(nums);
		} else if (lastBranch == 11) {
			lastBranch11(nums);
		}
		System.out.println("lASTbRANCH: " + lastBranch + "    sub: " + sub);
		// Find the endWhen of last branch and execute that code.
		if (lastBranch == -1) {
			System.out.println("NO FOLLOW PATH");
		} else {
			endWhenCode(endWhen[lastBranch], nums);
		}
	}

	/**
	 * Endings are handled below
	 */

	private static void tree0n1(int[] plugNums) {
		// (Noun) (Verb) (Adjective)
		// (Pronoun) (Verb) (Adjective)
		String firstNoun = Panel.words.get(plugNums[0])[0];
		String verb = Panel.words.get(plugNums[1])[2];
		String[] save = { Panel.words.get(plugNums[2])[2] };
		// This doesn't need to worry about possession, (subject, verb,
		// adjective) "I have tired" doesn't make sense.
		// If the Verb is not "to be" then it doesn't make sense
		if (Panel.words.get(plugNums[0])[0].equals("i")) {
			firstNoun = "player";
		} else if (Panel.words.get(plugNums[0])[0].equals("you")) {
			firstNoun = "computer";
		}
		Panel.characters.get(1).newSave(firstNoun, verb, save);
	}

	private static void tree2n3(int[] plugNums) {
		// (Noun) (Verb) (Noun)
		// (Pronoun) (Verb) (Noun)
		String firstNoun = Panel.words.get(plugNums[0])[0];
		if (Panel.words.get(plugNums[0])[0].equals("i")) {
			firstNoun = "player";
		} else if (Panel.words.get(plugNums[0])[0].equals("you")) {
			firstNoun = "computer";
		}
		String verb = Panel.words.get(plugNums[1])[2];
		String[] save = { Panel.words.get(plugNums[2])[2] };
		Panel.characters.get(1).newSave(firstNoun, verb, save);
		// boolean possession = false;
		// for (int a = 0; a < possPanel.verbs.length; a++) {
		// if (Panel.words.get(plugNums[1])[2].equals(possPanel.verbs[a])) {
		// Panel.characters.get(1).addToList(firstNoun, "to possess",
		// Panel.words.get(plugNums[2])[2]);
		// possession = true;
		// } }
		// if (!possession) {
		// Panel.characters.get(1).appendMemory(firstNoun,
		// Panel.words.get(plugNums[1])[2], Panel.words.get(plugNums[2])[2]);
		// }
	}

	private static void lastBranch11(int[] plugNums) {
		// (Noun)(Verb)(Verb)(Adjective)(Noun)(Preposition)(Noun)

		// (0)(0)[0][0] = the Scourge
		// (0)(1)[0][0] = seek
		// (0)(1)[1][0] = to eradicate
		// (0)(1)[1][1] = all
		// (0)(1)[1][2] = life
		// (0)(1)[1][3] = on
		// (0)(1)[1][4] = Azeroth

		// "what do the Scourge seek?"
		// "to eradicate all life on Azeroth"

		// "Do the Scourge seek to eradicate all life?"
		// (Q) (N) (V) (V) (Adj) (N)
		// "the scourge", "seek", "to eradicate", "all", "life"
		// "yes, all life on Azeroth?"

		// (String person, String verb, String[] save)

		// Classic find player.
		// plug in "seek"
		//

		String firstNoun = Panel.words.get(plugNums[0])[0];
		// if the verb is a possession verb then set possession
		if (Panel.words.get(plugNums[0])[0].equals("i")) {
			firstNoun = "player";
		} else if (Panel.words.get(plugNums[0])[0].equals("you")) {
			firstNoun = "computer";
		}

		String verb = Panel.words.get(plugNums[1])[0];
		System.out.println("plusNums.l: " + plugNums.length);
		String[] save = { Panel.words.get(plugNums[2])[0],
				Panel.words.get(plugNums[3])[0],
				Panel.words.get(plugNums[4])[0],
				Panel.words.get(plugNums[5])[0],
				Panel.words.get(plugNums[6])[0] };
		Panel.characters.get(1).newSave(firstNoun, verb, save);
	}

	private static void endWhenCode(int endWhen, int[] plugNums) {
		System.out.println("endWhen: " + endWhen);
		if (endWhen == -2) {
			tree2n3(plugNums);
		} else if (endWhen == -3) {
			tree0n1(plugNums);
		} else if (endWhen == -4) {
			tree6n7(plugNums);
		} else if (endWhen == -5) {
			tree8n9(plugNums);
		} else if (endWhen == -6) {
			tree10n11(plugNums);
		} else if (endWhen == -7) {
			tree12n13(plugNums);
		} else if (endWhen == -8) {
			tree16n17(plugNums);
		} else if (endWhen == -11) {
			lastBranch11(plugNums);
		}
	}

	private static void tree4n5(int[] plugNums) {
		// (Noun) (Verb) (Article) (Noun)
		// (Pronoun) (Verb) (Article) (Noun)
		String firstNoun = Panel.words.get(plugNums[0])[0];
		if (Panel.words.get(plugNums[0])[0].equals("i")) {
			firstNoun = "player";
		} else if (Panel.words.get(plugNums[0])[0].equals("you")) {
			firstNoun = "computer";
		}
		String verb = Panel.words.get(plugNums[1])[2];
		String[] save = { Panel.words.get(plugNums[3])[2] };
		Panel.characters.get(1).newSave(firstNoun, verb, save);

		// boolean possession = false;
		// for (int a = 0; a < possPanel.verbs.length; a++) {
		// if (Panel.words.get(plugNums[1])[2].equals(possPanel.verbs[a])) {
		// Panel.characters.get(1).addToList(firstNoun, "to possess",
		// Panel.words.get(plugNums[3])[2]);
		// possession = true;
		// }
		// }
		// if (!possession) {
		// Panel.characters.get(1).appendMemory(firstNoun,
		// Panel.words.get(plugNums[1])[2], Panel.words.get(plugNums[3])[2]);
		// }
	}

	private static void tree6n7(int[] plugNums) {
		// (Noun) (Verb) (Adverb)
		String firstNoun = Panel.words.get(plugNums[0])[0];
		if (Panel.words.get(plugNums[0])[0].equals("i")) {
			firstNoun = "player";
		} else if (Panel.words.get(plugNums[0])[0].equals("you")) {
			firstNoun = "computer";
		}
		// If the second word is a possPanel.verbs then set possession
		// Panel.characters.get(1).appendMemory(firstNoun,
		// Panel.words.get(plugNums[1])[2],
		// Panel.words.get(plugNums[2])[2]);
		String[] save = { Panel.words.get(plugNums[2])[2] };
		Panel.characters.get(1).newSave(firstNoun,
				Panel.words.get(plugNums[1])[2], save);
	}

	private static void tree8n9(int[] plugNums) {
		// Break down (Question) (Verb) (Pronoun) (Verb).
		String person = Panel.words.get(plugNums[2])[0];
		if (Panel.words.get(plugNums[2])[0].equals("i")) {
			person = "player";
		} else if (Panel.words.get(plugNums[2])[0].equals("you")) {
			person = "computer";
		}
		if (Panel.words.get(plugNums[0])[0].equals("what")) {
			if (Panel.words.get(plugNums[1])[2].equals("to do")) {
				// System.out.println("|" + person + "|    |"
				// + Panel.words.get(plugNums[3])[2] + "|");
				// Don't need to check the pronoun because it will just make a
				// new 'database if it does not know the person yet.
				// Panel.characters.get(1)
				// .tellMemmory(person, Panel.words.get(plugNums[3])[2]);
				Panel.characters.get(1).tellNew(person,
						Panel.words.get(plugNums[3])[2]);
			}
		}
	}

	private static void tree10n11(int[] plugNums) {
		// (Pronoun) (Verb) (Article) (Noun) (Adjective) (Verb) (Adjective)
		// I want a cat that is cool

		String firstNoun = Panel.words.get(plugNums[0])[0];
		// if the verb is a possession verb then set possession
		if (Panel.words.get(plugNums[0])[0].equals("i")) {
			firstNoun = "player";
		} else if (Panel.words.get(plugNums[0])[0].equals("you")) {
			firstNoun = "computer";
		}

		String verb = Panel.words.get(plugNums[1])[2];
		String noun = Panel.words.get(plugNums[2])[2];
		// NEW PROGRAM COMBINES (ART) AND (NOUN)
		String adjective = Panel.words.get(plugNums[5])[2];

		if (Panel.words.get(plugNums[3])[2].equals("that")) {
			if (Panel.words.get(plugNums[4])[2].equals("to be")) {
				System.out.println("|" + firstNoun + "| |" + verb + "| |"
						+ noun + "| |" + adjective + "|");
				String[] tempAr1 = { noun, adjective };
				Panel.characters.get(1).newSave(firstNoun, verb, tempAr1);
			}
		}

		// // If the second word is a possPanel.verbs then set possession
		// boolean possession = false;
		// for (int a = 0; a < possPanel.verbs.length; a++) {
		// if (Panel.words.get(plugNums[1])[2].equals(possPanel.verbs[a])) {
		// // Panel.characters.get(1).addToList(firstNoun, "to possess",
		// // Panel.words.get(plugNums[2])[2]);
		// possession = true;
		// }
		// }
		// if (!possession) {
		// Panel.characters.get(1).appendMemory(firstNoun,
		// Panel.words.get(plugNums[1])[2], Panel.words.get(plugNums[2])[2]);
		// }
	}

	private static void tree12n13(int[] plugNums) {
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
		String firstNoun = Panel.words.get(plugNums[0])[0];
		// if the verb is a possession verb then set possession
		if (Panel.words.get(plugNums[0])[0].equals("i")) {
			firstNoun = "player";
		} else if (Panel.words.get(plugNums[0])[0].equals("you")) {
			firstNoun = "computer";
		}
		String verb1 = Panel.words.get(plugNums[1])[2];
		String verb2 = Panel.words.get(plugNums[2])[2];
		String noun = Panel.words.get(plugNums[3])[2];
		String[] save = { verb2, noun };
		Panel.characters.get(1).newSave(firstNoun, verb1, save);
		// System.out.println(firstNoun + " " + verb1 + " " + verb2 + " " + noun
		// + ".");
	}

	private static void tree16n17(int[] plugNums) {
		// (Noun) (Verb) (Article) (Adjective) (Noun)
		// (Pronoun) (Verb) (Article) (Adjective) (Noun)
		// "i want a cool cat"
		String firstNoun = Panel.words.get(plugNums[0])[0];
		// if the verb is a possession verb then set possession
		if (Panel.words.get(plugNums[0])[0].equals("i")) {
			firstNoun = "player";
		} else if (Panel.words.get(plugNums[0])[0].equals("you")) {
			firstNoun = "computer";
		}

		String verb = Panel.words.get(plugNums[1])[2];
		String noun = Panel.words.get(plugNums[4])[2];
		String adjective = Panel.words.get(plugNums[3])[2];

		// System.out.println("|" + firstNoun + "| |" + verb + "| |" + noun
		// + "| |" + adjective + "|");
		String[] tempAr1 = { noun, adjective };
		Panel.characters.get(1).newSave(firstNoun, verb, tempAr1);

	}

	/**
	 * Endings are handled above
	 */

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
