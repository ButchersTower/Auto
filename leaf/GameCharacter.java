package Auto.leaf;

import java.util.ArrayList;
import java.util.Random;

public class GameCharacter {
	// "// REDUNDANT" is a thing

	String name = "computer";
	// actual memory
	// .get(x)[0] = StatusTypes[0];
	// .get(x)[1] = Conditions[0];
	ArrayList<int[]> Statuses;
	// databases
	// .get(x)[0] = Condition;
	// .get(x)[1] = definition;
	ArrayList<String[]> Conditions;
	// .get(x)[0] = Status Type Name;
	// .get(x)[1] = Explanation;
	ArrayList<String[]> StatusTypes;

	// goes .get(xxx).get(a)[b]
	// xx > 1 if its searching for condition
	// .get(0).get(0)[0] = "nameOfCharacter"
	// .get(a)[0] = statuses number.
	// .get(a)[1] = adjective/ information.
	// xxx needs to be unique, but i need a way of representing names.
	ArrayList<ArrayList<String[]>> otherChars;

	Panel panel;

	Random rand;

	public GameCharacter(String name, Panel panel) {
		this.name = name;
		this.panel = panel;
		rand = new Random();
		otherChars = new ArrayList<ArrayList<String[]>>();

		newChars = new ArrayList<ArrayList<ArrayList<String[]>>>();

		ArrayList<String[]> thisChar = new ArrayList<String[]>();
		thisChar.add(new String[] { name });
		otherChars.add(thisChar);
	}

	// This sets variables of objects, I can't perceive how this could
	// accommodate possession of items.
	void appendMemory(String person, String condition, String adjective) {
		// find the nameALAL (else make on)
		// find conditionAL in name (else make one)
		// set the status to adjective.
		boolean nameFound = false;
		for (int a = 0; a < otherChars.size(); a++) {
			// this runs through the characters
			if (otherChars.get(a).get(0)[0].equals(person)) {
				// This find the character with the appropriate name
				nameFound = true;
				// this looks for the condition of the character
				boolean conditionFound = false;
				for (int b = 1; b < otherChars.get(a).size(); b++) {
					if (otherChars.get(a).get(b)[0].equals(condition)) {
						conditionFound = true;
						// condition found so set it
						otherChars.get(a).get(b)[1] = adjective;
						panel.takeIn("(Cmem) (" + otherChars.get(a).get(0)[0]
								+ " " + otherChars.get(a).get(b)[0] + " "
								+ otherChars.get(a).get(b)[1] + ")");

					}
				}
				if (!conditionFound) {
					// create the condition and set status
					otherChars.get(a)
							.add(new String[] { condition, adjective });
					panel.takeIn("(Cmem) ("
							+ otherChars.get(a).get(0)[0]
							+ " "
							+ otherChars.get(a).get(
									otherChars.get(a).size() - 1)[0]
							+ " "
							+ otherChars.get(a).get(
									otherChars.get(a).size() - 1)[1] + ")");
				}
				// run through the conditions looking for this one
			}
		}
		if (!nameFound) {
			ArrayList<String[]> thisChar = new ArrayList<String[]>();
			thisChar.add(new String[] { person });
			thisChar.add(new String[] { condition, adjective });
			otherChars.add(thisChar);
			panel.takeIn("(Cmem) ("
					+ otherChars.get(otherChars.size() - 1).get(0)[0] + " "
					+ otherChars.get(otherChars.size() - 1).get(1)[0] + " "
					+ otherChars.get(otherChars.size() - 1).get(1)[1] + ")");
			// create a character with that name, add status, and set condtion
		}
	}

	void addToList(String person, String verb, String object) {
		// find the person, then look for the verb "To possess" and add the
		// object onto it
		boolean nameFound = false;
		for (int a = 0; a < otherChars.size(); a++) {
			// this runs through the characters
			if (otherChars.get(a).get(0)[0].equals(person)) {
				// This find the character with the appropriate name
				nameFound = true;
				boolean conditionFound = false;
				for (int b = 1; b < otherChars.get(a).size(); b++) {
					if (otherChars.get(a).get(b)[0].equals(verb)) {
						conditionFound = true;
						// condition found so add object

						// This just adds the object to the end
						// Should search the String[] to make sure it doesn't
						// already have the item.
						boolean alreadyThere = false;
						String[] temp = new String[otherChars.get(a).get(b).length + 1];
						for (int x = 0; x < temp.length - 1; x++) {
							// This if loop ignores x == 0 because
							// .get(a).get(b)[x] = the infinitive verb
							if (x != 0 && object == otherChars.get(a).get(b)[x]) {
								System.out.println("alreadyThere");
								alreadyThere = true;
							}
							temp[x] = otherChars.get(a).get(b)[x];
							System.out.println("otherChars.get(a).get(b)[a]: "
									+ otherChars.get(a).get(b)[x]);
						}
						if (!alreadyThere) {
							otherChars.get(a).set(b, temp);
							panel.takeIn("(Cmem) ("
									+ otherChars.get(a).get(0)[0]
									+ " "
									+ otherChars.get(a).get(b)[0]
									+ " "
									+ otherChars.get(a).get(b)[otherChars
											.get(a).get(b).length - 1] + ")");

						} else {
							panel.takeIn("(Cmem) ("
									+ otherChars.get(a).get(0)[0]
									+ " already possess)");
						}

					}
				}
				if (!conditionFound) {
					// create possession and add object
					otherChars.get(a).add(new String[] { verb, object });
					panel.takeIn("(Cmem) ("
							+ otherChars.get(a).get(0)[0]
							+ " "
							+ otherChars.get(a).get(
									otherChars.get(a).size() - 1)[0]
							+ " "
							+ otherChars.get(a).get(
									otherChars.get(a).size() - 1)[1] + ")");
				}
			}
		}
		if (!nameFound) {
			// create a character with that name, add status, and set condtion
			ArrayList<String[]> thisChar = new ArrayList<String[]>();
			thisChar.add(new String[] { person });
			thisChar.add(new String[] { verb, object });
			otherChars.add(thisChar);
			panel.takeIn("(Cmem) ("
					+ otherChars.get(otherChars.size() - 1).get(0)[0] + " "
					+ otherChars.get(otherChars.size() - 1).get(1)[0] + " "
					+ otherChars.get(otherChars.size() - 1).get(1)[1] + ")");
		}
	}

	ArrayList<ArrayList<ArrayList<String[]>>> newChars;

	void newSave(String person, String verb, String[] save) {
		// (0)(0)[0] = name
		// (0)(1)[0] = possess
		// (0)(1)[1] = bottle
		// (0)(1)[2] = empty

		// (0)(0)[0][0] = name
		// (0)(1)[0][0] = possess
		// (0)(1)[1][0] = bottle
		// (0)(1)[1][1] = empty

		// (0)(0)[0][0] = Scourge
		// (0)(1)[0][0] = seek
		// (0)(1)[1][0] = to eradicate
		// (0)(1)[1][1] = all
		// (0)(1)[1][2] = life
		// (0)(1)[1][3] = on
		// (0)(1)[1][4] = Azeroth

		// (0)(0)[0][0] = player
		// (0)(1)[0][0] = want
		// (0)(1)[1][0] = go
		// (0)(1)[1][1] = home

		System.out.println("here");

		boolean nameFound = false;
		for (int a = 0; a < newChars.size(); a++) {
			if (newChars.get(a).get(0).get(0)[0].equals(person)) {
				nameFound = true;
				boolean verbFound = false;
				for (int b = 0; b < newChars.get(a).size(); b++) {
					if (newChars.get(a).get(b).get(0)[0].equals(verb)) {
						verbFound = true;
						boolean objFound = false;
						for (int c = 0; c < newChars.get(a).get(b).size(); c++) {
							if (newChars.get(a).get(b).get(c)[0]
									.equals(save[0])) {
								objFound = true;
								// For now add the new adjectives to the end.
								// Find the adjective of "save" and add it.
								String newAdj = save[1];
								// If this newAdj is already there?
								boolean alreadyAdded = false;
								for (int z = 1; z < newChars.get(a).get(b)
										.get(c).length; z++) {
									if (newChars.get(a).get(b).get(c)[z]
											.equals(newAdj)) {
										alreadyAdded = true;

									}
								}
								if (!alreadyAdded) {
									System.out.println("AKREADY ADDED");
									// appendStringAR
									String[] oldStr = newChars.get(a).get(b)
											.get(c);
									String[] newStr = panel.appendStringAr(
											oldStr, newAdj);
									newChars.get(a).get(b).set(c, newStr);

									panel.takeIn("(Cmem) ("
											+ newChars.get(a).get(0).get(0)[0]
											+ " "
											+ newChars.get(a).get(b).get(0)[0]
											+ " "
											+ newChars
													.get(a)
													.get(b)
													.get(newChars.get(a).get(b)
															.size() - 1)[0]
											+ " "
											+ newChars
													.get(a)
													.get(b)
													.get(newChars.get(a).get(b)
															.size() - 1)[newChars
													.get(a)
													.get(b)
													.get(newChars.get(a).get(b)
															.size() - 1).length - 1]
											+ ")");
								}
							}
						}
						if (!objFound) {
							System.out.println("new OBJ");
							// set condition
							newChars.get(a).get(b).add(save);

							// ArrayList<String[]> thisChar = new
							// ArrayList<String[]>();
							// thisChar.add(new String[] { person });
							// thisChar.add(new String[] { verb });
							// newChars.add(thisChar);

							int second = newChars.get(a).size() - 1;
							int third = newChars.get(a).get(second).size() - 1;
							String outPut = "(Cmem) (";
							outPut += newChars.get(a).get(0).get(0)[0] + " "
									+ newChars.get(a).get(second).get(0)[0];

							for (int g = 0; g < newChars.get(a).get(second)
									.get(third).length; g++) {
								outPut += " "
										+ newChars.get(a).get(second)
												.get(third)[g];
							}
							outPut += ")";
							panel.takeIn(outPut);

							// panel.takeIn("(Cmem) (" +
							// newChars.get(a).get(0).get(0)[0] + " " +
							// newChars.get(a).get(b).get(0)[0] + " " + newChars
							// .get(a) .get(b)
							// .get(newChars.get(a).get(b).size() - 1)[0] + " "
							// + newChars .get(a) .get(b)
							// .get(newChars.get(a).get(b).size() - 1)[1] +
							// ")");
						}
					}
				}
				if (!verbFound) {
					System.out.println("new VERB");
					// create verb and add condition
					ArrayList<String[]> temp2 = new ArrayList<String[]>();
					temp2.add(new String[] { verb });
					temp2.add(save);
					newChars.get(a).add(temp2);

					// ArrayList<String[]> thisChar = new ArrayList<String[]>();
					// thisChar.add(new String[] { person });
					// thisChar.add(new String[] { verb });
					// newChars.add(thisChar);

					int second = newChars.get(a).size() - 1;
					String outPut = "(Cmem) (";
					outPut += newChars.get(a).get(0).get(0)[0] + " "
							+ newChars.get(a).get(second).get(0)[0];

					for (int g = 0; g < newChars.get(a).get(second).get(1).length; g++) {
						outPut += " " + newChars.get(a).get(second).get(1)[g];
					}
					outPut += ")";
					panel.takeIn(outPut);

					// System.out.println("verb: "
					// + newChars.get(a).get(second).get(0)[0]);
					// panel.takeIn("(Cmem) ("
					// + newChars.get(a).get(0).get(0)[0] + " "
					// + newChars.get(a).get(second).get(0)[0] + " "
					// + newChars.get(a).get(second).get(1)[0] + " "
					// + newChars.get(a).get(second).get(1)[1] + ")");
				}
			}
		}
		if (!nameFound) {
			System.out.println("new PER");
			// create a character with that name, add status, and set condtion
			ArrayList<String[]> temp1 = new ArrayList<String[]>();
			temp1.add(new String[] { person });
			ArrayList<String[]> temp2 = new ArrayList<String[]>();
			temp2.add(new String[] { verb });
			temp2.add(save);
			ArrayList<ArrayList<String[]>> inner1 = new ArrayList<ArrayList<String[]>>();
			inner1.add(temp1);
			inner1.add(temp2);

			newChars.add(inner1);

			// ArrayList<String[]> thisChar = new ArrayList<String[]>();
			// thisChar.add(new String[] { person });
			// thisChar.add(new String[] { verb });
			// newChars.add(thisChar);

			int first = newChars.size() - 1;
			int second = newChars.get(first).size() - 1;

			String outPut = "(Cmem) (";
			outPut += newChars.get(first).get(0).get(0)[0] + " "
					+ newChars.get(first).get(second).get(0)[0];
			for (int a = 0; a < newChars.get(first).get(second).get(1).length; a++) {
				outPut += " " + newChars.get(first).get(second).get(1)[a];
			}
			outPut += ")";
			panel.takeIn(outPut);
			// panel.takeIn("(Cmem) (" +
			// newChars.get(first).get(0).get(0)[0] + " " +
			// newChars.get(first).get(second).get(0)[0] + " " +
			// newChars.get(first).get(second).get(1)[0] + " " +
			// newChars.get(first).get(second).get(1)[1] + ")");
		}
	}

	// newChars.get(a).get(b).get(0)[0].equals(verb)

	void tellNew(String person, String verb) {
		// Find person, find verb... EASY
		// Find if they have shit under the verb and

		// "i have a cat that is cool"
		// "i have a bottle"
		// "what do i have"
		// "you have a cool cat
		// you have a bottle"
		boolean nameFound = false;
		for (int a = 0; a < newChars.size(); a++) {
			if (newChars.get(a).get(0).get(0)[0].equals(person)) {
				nameFound = true;
				boolean verbFound = false;
				for (int b = 0; b < newChars.get(a).size(); b++) {
					if (newChars.get(a).get(b).get(0)[0].equals(verb)) {
						verbFound = true;
						String otherPerson = person;
						boolean findSpeaker = false;
						boolean findSubject = false;
						int tenseOfVerb = 3;
						for (int c = 0; c < panel.conversers.length; c++) {
							// If one of the names are computer and the other is
							// player then set formality
							if (panel.conversers[c].equals(name)) {
								// computer is in the converstaion
								// REDUNDANT
								findSpeaker = true;
							}
							if (panel.conversers[c].equals(person)) {
								findSubject = true;
							}
						}
						if (findSubject && findSpeaker) {
							if (person == name) {
								otherPerson = "i";
								tenseOfVerb = 1;
							} else {
								otherPerson = "you";
								tenseOfVerb = 2;
							}
						}
						// Removes the "to" from the verb by isolating the
						// second word
						String newVerb = verb.split("[ ]")[1];
						// Search through VerbsAl and find the infinitive then
						// find the tense.
						for (int v = 0; v < panel.Verbs.size(); v++) {
							if (panel.Verbs.get(v)[0].equals(newVerb)) {
								newVerb = panel.Verbs.get(v)[tenseOfVerb];
							}
						}
						System.out.println("NewVerb: " + newVerb);
						// if (panel.words.get(adjNum)[1]) { }
						// What do i have?
						// "player" "to have"
						// String subject = person;
						// String newVerb = verb;
						String adj = "";
						// If there is a word to check.

						// If after the object there is an adjective
						if (newChars.get(a).get(b).get(1).length > 1) {
							int nextWordNum = panel.numOfWord(newChars.get(a)
									.get(b).get(1)[1]);
							System.out.println("This Word: "
									+ panel.words.get(nextWordNum)[0]);
							if (panel.words.get(nextWordNum)[1]
									.equals("Adjective")) {
								System.out.println("next is Adj");
								if (rand.nextInt(2) == 0) {
									// (N) (V) "a" (Adj) (N)
									String adjective = newChars.get(a).get(b)
											.get(1)[1];
									adj = " that is ";

									// This should go thought the different
									// additional words in the memory
									for (int z = 1; z < newChars.get(a).get(b)
											.get(1).length; z++) {
										if (z == 1) {
											adj += newChars.get(a).get(b)
													.get(1)[z];
										} else {
											adj += " and "
													+ newChars.get(a).get(b)
															.get(1)[z];
										}
									}
									String noun = newChars.get(a).get(b).get(1)[0];
									String article = "a";
									if (noun.charAt(0) == 'a'
											|| noun.charAt(0) == 'e'
											|| noun.charAt(0) == 'i'
											|| noun.charAt(0) == 'o'
											|| noun.charAt(0) == 'u') {
										article = "an";
									}
									panel.takeIn("(C) " + otherPerson + " "
											+ newVerb + " " + article + " "
											+ noun + adj + ".");
								} else {
									// (N) (V) (N) "that" "is" (Adj)
									for (int z = 1; z < newChars.get(a).get(b)
											.get(1).length; z++) {

										if (z == 1) {
											adj += newChars.get(a).get(b)
													.get(1)[z];
										} else {
											adj += ", "
													+ newChars.get(a).get(b)
															.get(1)[z];
										}

									}
									String article = "a";
									if (adj.charAt(0) == 'a'
											|| adj.charAt(0) == 'e'
											|| adj.charAt(0) == 'i'
											|| adj.charAt(0) == 'o'
											|| adj.charAt(0) == 'u') {
										article = "an";
									}
									panel.takeIn("(C) " + otherPerson + " "
											+ newVerb + " " + article + " "
											+ adj + " "
											+ newChars.get(a).get(b).get(1)[0]
											+ ".");
								}
							} else if (panel.words.get(nextWordNum)[1]
									.equals("Verb")) {
								System.out.println("next is Verb");
								// This is a double verb
								// Have (noun) (verb)
								// need, (infinitive verb)
								// then the following (word)
								// .get(1) because for now it only shows the
								// first thing you want
								// This should like run through the saveAr
								String nextVerb = "to "
										+ newChars.get(a).get(b).get(1)[0];
								String finalWord = newChars.get(a).get(b)
										.get(1)[1];
								panel.takeIn("(C) " + otherPerson + " "
										+ newVerb + " " + nextVerb + " "
										+ finalWord + ".");
							}
						} else {
							// If there are no adjectives or additional
							// explanations...
							String noun = newChars.get(a).get(b).get(1)[0];
							String article = "a";
							if (noun.charAt(0) == 'a' || noun.charAt(0) == 'e'
									|| noun.charAt(0) == 'i'
									|| noun.charAt(0) == 'o'
									|| noun.charAt(0) == 'u') {
								article = "an";
							}
							panel.takeIn("(C) " + otherPerson + " " + newVerb
									+ " " + article + " " + noun + ".");

						}
						// If the word after the noun is an adjective then
						// EITHER:
						// (N) (V) "a" (Adj) (N)
						// (N) (V) (N) "that" "is" (Adj)
						// Pick at random???
					}
				}
				if (!verbFound) {
					String otherPerson = person;
					boolean findSpeaker = false;
					boolean findSubject = false;
					int tenseOfVerb = 3;
					for (int c = 0; c < panel.conversers.length; c++) {
						// If one of the names are computer and the other is
						// player then set formality
						if (panel.conversers[c].equals(name)) {
							// computer is in the conversation
							// REDUNDANT
							findSpeaker = true;
						}
						if (panel.conversers[c].equals(person)) {
							findSubject = true;
						}
					}
					if (findSubject && findSpeaker) {
						if (person == name) {
							otherPerson = "i";
							tenseOfVerb = 1;
						} else {
							otherPerson = "you";
							tenseOfVerb = 2;
						}
					}
					// Removes the "to" from the verb by isolating the
					// second word
					String newVerb = verb.split("[ ]")[1];
					// Search through VerbsAl and find the infinitive then
					// find the tense.
					for (int v = 0; v < panel.Verbs.size(); v++) {
						if (panel.Verbs.get(v)[0].equals(newVerb)) {
							newVerb = panel.Verbs.get(v)[tenseOfVerb];
						}
					}
					panel.takeIn("(C) i do not know what " + otherPerson + " "
							+ newVerb + ".");
				}
			}
		}
		if (!nameFound) {
			String otherPerson = person;
			boolean findSpeaker = false;
			boolean findSubject = false;
			int tenseOfVerb = 3;
			for (int c = 0; c < panel.conversers.length; c++) {
				// If one of the names are computer and the other is
				// player then set formality
				if (panel.conversers[c].equals(name)) {
					// computer is in the converstaion
					// REDUNDANT
					findSpeaker = true;
				}
				if (panel.conversers[c].equals(person)) {
					findSubject = true;
				}
			}
			if (findSubject && findSpeaker) {
				if (person == name) {
					otherPerson = "i";
					tenseOfVerb = 1;
				} else {
					otherPerson = "you";
					tenseOfVerb = 2;
				}
			}
			// Removes the "to" from the verb by isolating the
			// second word
			String newVerb = "be";
			// Search through VerbsAl and find the infinitive then
			// find the tense.
			for (int v = 0; v < panel.Verbs.size(); v++) {
				if (panel.Verbs.get(v)[0].equals(newVerb)) {
					newVerb = panel.Verbs.get(v)[tenseOfVerb];
				}
			}
			panel.takeIn("(C) i do not know who " + otherPerson + " " + newVerb
					+ ".");
		}
	}

	void outputNew() {
		System.out.println("outPutNew");
		for (int a = 0; a < newChars.size(); a++) {
			for (int b = 0; b < newChars.get(a).size(); b++) {
				for (int c = 0; c < newChars.get(a).get(b).size(); c++) {
					for (int d = 0; d < newChars.get(a).get(b).get(c).length; d++) {
						System.out.println("(" + a + ", " + b + ", " + c + ", "
								+ d + ")" + newChars.get(a).get(b).get(c)[d]);
					}
				}
			}
		}
	}

	void tellMemmory(String person, String verb) {
		// find the person
		boolean findPerson = false;
		for (int a = 0; a < otherChars.size(); a++) {
			if (otherChars.get(a).get(0)[0].equals(person)) {
				findPerson = true;
				boolean findVerb = false;
				System.out.println("gethere2");
				for (int b = 0; b < otherChars.get(a).size(); b++) {
					if (otherChars.get(a).get(b)[0].equals(verb)) {
						findVerb = true;
						System.out.println("gethere1");
						// This is the person and verb, tell them what you know
						// about this condition
						// run through the memories.

						// try to be formal
						// If the two speakers are "computer" and the subject of
						// the computers sentence then change the noun to "you".
						String otherPerson = person;
						System.out.println("c0: |" + panel.conversers[0]
								+ "|     |" + panel.conversers[1] + "|");
						System.out.println("nam: |" + name + "|    |" + person
								+ "|");
						boolean findSpeaker = false;
						boolean findSubject = false;
						int tenseOfVerb = 3;
						for (int c = 0; c < panel.conversers.length; c++) {
							// If one of the names are computer and the other is
							// player then set formality
							if (panel.conversers[c].equals(name)) {
								// computer is in the converstaion
								// REDUNDANT
								findSpeaker = true;
							}
							if (panel.conversers[c].equals(person)) {
								findSubject = true;
							}
						}
						if (findSubject && findSpeaker) {
							if (person == name) {
								otherPerson = "i";
								tenseOfVerb = 1;
							} else {
								otherPerson = "you";
								tenseOfVerb = 2;
							}
						}
						// Removes the "to" from the verb by isolating the
						// second word
						String newVerb = verb.split("[ ]")[1];
						// Search through VerbsAl and find the infinitive then
						// find the tense.
						for (int v = 0; v < panel.Verbs.size(); v++) {
							if (panel.Verbs.get(v)[0].equals(newVerb)) {
								newVerb = panel.Verbs.get(v)[tenseOfVerb];
							}
						}
						// "you possess cat"
						// Find the number of cats that I have, if it is one
						// then put "a" infront of noun, else put the number.
						panel.takeIn("(C) " + otherPerson + " " + newVerb + " "
								+ otherChars.get(a).get(b)[1] + ".");
					}
				}
				if (!findVerb) {
					String otherPerson = person;
					System.out.println("c0: |" + panel.conversers[0]
							+ "|     |" + panel.conversers[1] + "|");
					System.out.println("nam: |" + name + "|    |" + person
							+ "|");
					boolean findSpeaker = false;
					boolean findSubject = false;
					int tenseOfVerb = 3;
					for (int c = 0; c < panel.conversers.length; c++) {
						if (panel.conversers[c].equals(name)) {
							findSpeaker = true;
						}
						if (panel.conversers[c].equals(person)) {
							findSubject = true;
						}
					}
					if (findSubject && findSpeaker) {
						if (person == name) {
							otherPerson = "i";
							tenseOfVerb = 1;
						} else {
							otherPerson = "you";
							tenseOfVerb = 2;
						}
					}

					String newVerb = verb.split("[ ]")[1];
					for (int v = 0; v < panel.Verbs.size(); v++) {
						if (panel.Verbs.get(v)[0].equals(newVerb)) {
							System.out.println("find be");
							newVerb = panel.Verbs.get(v)[tenseOfVerb];
						}
					}
					panel.takeIn("(C) i do not know what " + otherPerson + " "
							+ newVerb);
				}
			}
		}
		if (!findPerson) {

			String otherPerson = person;
			System.out.println("c0: |" + panel.conversers[0] + "|     |"
					+ panel.conversers[1] + "|");
			System.out.println("nam: |" + name + "|    |" + person + "|");
			boolean findSpeaker = false;
			boolean findSubject = false;
			int tenseOfVerb = 3;
			for (int c = 0; c < panel.conversers.length; c++) {
				if (panel.conversers[c].equals(name)) {
					findSpeaker = true;
				}
				if (panel.conversers[c].equals(person)) {
					findSubject = true;
				}
			}
			System.out.println("findSub: " + findSubject + "   findSpe: "
					+ findSpeaker);
			if (findSubject && findSpeaker) {
				if (person == name) {
					otherPerson = "i";
					tenseOfVerb = 1;
				} else {
					otherPerson = "you";
					tenseOfVerb = 2;
				}
			}

			String newVerb = "be";
			for (int v = 0; v < panel.Verbs.size(); v++) {
				if (panel.Verbs.get(v)[0].equals(newVerb)) {
					System.out.println("find be");
					newVerb = panel.Verbs.get(v)[tenseOfVerb];
				}
			}

			panel.takeIn("(C) i do not know who " + otherPerson + " " + newVerb);

		}
	}

	void outputMem() {
		// When outputting if there are multiple descriptions (like with
		// possession) then output them all. Do i want it on one line or
		// Individual ones?
		for (int a = 0; a < otherChars.size(); a++) {
			// this runs through all characters
			for (int b = 1; b < otherChars.get(a).size(); b++) {
				// b == 1 because .get(x).get(0)[0] = name
				// this runs through all the conditions

				// c == 1 because .get(x)[0] == name.
				for (int c = 1; c < otherChars.get(a).get(b).length; c++) {
					System.out
							.println("ABC: (" + a + ", " + b + ", " + c + ")");
					panel.takeIn("(Cmem) (" + otherChars.get(a).get(0)[0] + " "
							+ otherChars.get(a).get(b)[0] + " "
							+ otherChars.get(a).get(b)[c] + ")");
				}
			}
		}
	}

	void initVars() {
		// All of these are player conditions.
		Conditions.add(new String[] { "gCondition", "General condition" });
		Conditions.add(new String[] { "Hair", "General condition of hair" });
	}

	String getName() {
		return name;
	}
}
