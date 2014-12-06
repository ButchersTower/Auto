package Auto.pression;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class ReadAndWrite {
	static ArrayList<String> strings;

	public void readLines() throws IOException {
		BufferedReader inputStream = null;
		strings = new ArrayList<String>();
		try {
			InputStream is = new FileInputStream("PressionSave1.txt");
			inputStream = new BufferedReader(new InputStreamReader(is));
			String l;
			while ((l = inputStream.readLine()) != null) {
				strings.add(l);
			}
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}
	}

	public ArrayList<String> getstrings() {
		return strings;
	}

	public void saveLines(String[] st) {
		try {
			BufferedWriter writer = null;
			String prives = "PressionSave1.txt";
			OutputStream os = new FileOutputStream(prives);
			writer = new BufferedWriter(new OutputStreamWriter(os));

			for (int s = 0; s < st.length; s++) {
				writer.write(st[s]);
				writer.newLine();
			}
			writer.flush();
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		}
	}
}
