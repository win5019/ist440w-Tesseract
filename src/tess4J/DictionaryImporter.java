// Sources: 	https://stackoverflow.com/questions/42175805/reading-a-text-file-line-by-line-using-java-eclipse
//				https://www.dreamincode.net/forums/topic/84941-read-a-text-file-in-java-using-eclipse/
//				https://github.com/dwyl/english-words



package tess4J;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

// This currently only imports 1 language
// 		To import multiple languages, a file path to a folder should be set and 
//		then all of the language text files should be read into different ArrayLists

public class DictionaryImporter {

	public ArrayList<String> getEnglishArrayList() {
		
		ArrayList<String> englishArrayList = new ArrayList<>();
		String wordListFileName = "english.txt";
		
		try{
			System.out.println(System.getProperty("user.dir"));
			
			FileInputStream fstream = new FileInputStream(System.getProperty("user.dir") + "\\" + wordListFileName);
			
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String txtFileLine;
			
			//Read File Line By Line
			while ((txtFileLine = br.readLine()) != null)   {
				// Print the content on the console
				englishArrayList.add(txtFileLine);
			}
			
			//Close the input stream
			in.close();
		
		} catch(IOException e){ //Catch exception if any
			e.printStackTrace();
		}
		
		return englishArrayList;
	}
	
}
