package Auto.leaf;

import java.util.ArrayList;

public class Words {
	// Want to save plurality of pronouns, don't need yet but seems important in
	// trying to identify pronouns
	public static ArrayList<String[]> initWords() {
		ArrayList<String[]> words;
		words = new ArrayList<String[]>();
		words.add(new String[] { "hello", "Interjection", "Greeting" });
		words.add(new String[] { "welcome", "Interjection", "Greeting" });
		words.add(new String[] { "hey", "Interjection", "Greeting" });

		words.add(new String[] { "this", "Adverb", "Determiner" });
		words.add(new String[] { "would", "Verb", "Modal Auxilary" });

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
		words.add(new String[] { "process", "Noun", "process" });
		words.add(new String[] { "decisions", "Noun", "decisions" });

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
		words.add(new String[] { "less", "Adjective", "less" });

		words.add(new String[] { "well", "Adverb", "well" });
		// "How" is confusing
		words.add(new String[] { "how", "Adverb", "how" });

		// Questions work
		words.add(new String[] { "possessions", "noun", "to possess" });
		// what is an adjective (i think sometimes)
		words.add(new String[] { "what", "Question", "what" });
		// Confusing
		words.add(new String[] { "that", "Adjective", "that" });

		words.add(new String[] { "right", "Adverb", "right" });
		words.add(new String[] { "now", "Adverb", "now" });
		return words;
	}

	public static ArrayList<String[]> initVerbs() {
		ArrayList<String[]> verbs;
		// .get(x)[0] = (Infinitive with no “to”)
		// .get(x)[1] = (First) I
		// .get(x)[2] = (Second) You
		// .get(x)[3] = (Third) He/ She
		// .get(x)[4] = (Plural First) We
		// .get(x)[5] = (Plural Second) You all
		// .get(x)[6] = (Plural Third) They
		verbs = new ArrayList<String[]>();
		verbs.add(new String[] { "be", "am", "are", "is", "are", "are", "are" });
		verbs.add(new String[] { "do", "do", "do", "does", "do", "do", "do" });
		verbs.add(new String[] { "eradicate", "eradicate", "eradicate",
				"eradicates", "eradicate", "eradicate", "eradicate" });
		verbs.add(new String[] { "feel", "feel", "feel", "feels", "feel",
				"feel", "feel" });
		verbs.add(new String[] { "go", "go", "go", "goes", "go", "go", "go" });
		verbs.add(new String[] { "have", "have", "have", "has", "have", "have",
				"have" });
		verbs.add(new String[] { "own", "own", "own", "owns", "own", "own",
				"own" });
		verbs.add(new String[] { "possess", "possess", "possesses", "possess",
				"possess", "possess", "possess" });
		verbs.add(new String[] { "seek", "seek", "seek", "seeks", "seek",
				"seek", "seek" });
		verbs.add(new String[] { "sell", "sell", "sell", "sells", "sell",
				"sell", "sell" });
		verbs.add(new String[] { "want", "want", "want", "wants", "want",
				"want", "want" });
		verbs.add(new String[] { "require", "require", "require", "requires",
				"require", "require", "require" });
		verbs.add(new String[] { "wish", "wish", "wish", "wishes", "wish",
				"wish", "wish" });

		return verbs;
	}
}
