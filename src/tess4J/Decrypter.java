package tess4J;

import java.io.File;
import java.util.ArrayList;

public class Decrypter {

	public void decryptEnglishArrayList(ArrayList<String> encryptedArrayList) {
		
		// Replace all zeroes with o's, 5’s with s’s, and 6’s with G’s
		// Create ArrayList with replaced 1’s with i’s
		// Create ArrayList with replaced 1’s with L’s 
		
		
		// Reads in encryptedArrayList
		
		
// Variable: JSONObject kKey
		// Generates a 'K' key
		// String of alphabet letters
		// 'K' key is a JSON object
		//		for each letter in 'K', pick a letter from the string of alphabet letters and subtract the letter from the string
		//		{"a" : "some randomly selected letter", etc. }
		
		
// Variable: ArrayList<String> languagesDictionary
// Variable: Integer minimumPercentageToStopDecrypting
		// Try to decrypt using instance of 'K' key
		// 		match as many words as possible to the language's dictionary. 
		//		set a percentage to stop generating 'K' keys
		
// Variable: Boolean isDecrpyted //set to true will stop the decrypter and print out 'K' key and decrypted text in an output file
		// while not decrypted, continue generating 'K' keys and trying to decrypt
		// if decrypted then output file of decrypted text and 'K' key
// Variable: File outputFile //containing 'K' key and decrypted text
	}
	
	public ArrayList<String> fileToArrayList(File encryptedText) {
		// reads in an encrypted file to arrayList here
		
		ArrayList<String> exampleArrayList = null;
		ArrayList<String> encryptedTextArrayList = exampleArrayList;
		
		
		return encryptedTextArrayList;
	}
	
	public void decrypt(File encryptedText) {
// Variable: File encryptedText
		
		// ArrayList<String> encryptedArrayList = fileToArrayList(encryptedText);
		
		// decryptEnglishArrayList(encryptedArrayList);
		
	}
	
	
}
