package Auto.stem;

public class OldMethods {
	boolean analizeTextOld(int[] plugNums) {
		System.out.println("GET her\t1");
		// this needs to return a boolean of true if it outputs text for the
		// computer.
		// Should like start with if first word is noun and the second is a
		// verb then remember the status of the noun of the according verb is
		// the following adjective
		// first word noun, second is verb, third is
		// for now it is going to run through chat tree and if it hits all of it
		// then set memory.
		boolean wordsMakeSense = true;
		for (int i = 0; i < plugNums.length; i++) {
			if (plugNums[i] == -1) {
				wordsMakeSense = false;
			}
		}
		boolean understood = false;
		if (wordsMakeSense) {
			// Want to change this to follow trees.
			if (words.get(plugNums[0])[1].equals("Noun")
					|| words.get(plugNums[0])[1].equals("Pronoun")) {
				// Need to accommodate naming pronouns.
				// if the first word is a noun
				if (words.get(plugNums[1])[1].equals("Verb")) {
					// if the second word is a verb
					if (words.get(plugNums[2])[1].equals("Noun")) {
						// if the third word is an adjective
						// set memory
						// When it sets a memory it should display about how
						// what is
						// saved
						// this only takes in the int[] plugNums and!!!!!
						// It does not know how to identify what object "I" is.
						// Computer needs to know who is speaking to it.
						// Need to labke all texts with what object it is coming
						// from

						System.out.println("GET HERE");

						/**
						 * NEED to get "player" from somewhere.
						 */
						// System.out.println("wordNum  (" +
						// words.get(plugNums[1])[2] + ", " +
						// words.get(plugNums[2])[2] + ")");
						characters.get(1).appendMemory("player",
								words.get(plugNums[1])[2],
								words.get(plugNums[2])[2]);

						understood = true;
					}
				}
			}
		}
		/**
		 * for (int a = 0; a < plugNums.length; a+"+) { if
		 * (words.get(plugNums[a])[1].equals("Noun")) {
		 */
		// first word equals a noun. It needs to follow a tree now how
		// do i work out a tree. I have one branch currently but how do
		// i structure it so i can have different top branches and they
		// split off very similarly to foldText.
		// Needs to intake the name of the character speaking, once it
		// gets it then it searches for a memory of that character and
		// creates/sets the status of that character according to the
		// adjective.
		// } }
		return understood;
	}
}
