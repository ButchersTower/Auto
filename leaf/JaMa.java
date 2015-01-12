package Auto.leaf;

public class JaMa {
	// This removed the [numToRemove] variable from an AR and compresses
	static int[] shortenIntAR(int[] in, int numToRemove) {
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

	public static int[] appendIntAr(int[] st, int appendage) {
		int[] temp = new int[st.length + 1];
		for (int a = 0; a < st.length; a++) {
			temp[a] = st[a];
		}
		temp[temp.length - 1] = appendage;
		return temp;
	}

	public static String[] appendStringAr(String[] st, String appendage) {
		String[] temp = new String[st.length + 1];
		for (int a = 0; a < st.length; a++) {
			temp[a] = st[a];
		}
		temp[temp.length - 1] = appendage;
		return temp;
	}
}
