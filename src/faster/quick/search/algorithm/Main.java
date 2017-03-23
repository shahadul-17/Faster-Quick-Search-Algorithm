package faster.quick.search.algorithm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeSet;

public class Main {
	
	public static void main(String[] args) {
		char[] alphabets, pattern, text;
		
		Main main = new Main();
		Scanner scanner = new Scanner(System.in);		// initializing scanner...
		
		System.out.print("Enter the pattern = ");
		
		pattern = scanner.nextLine().trim().toUpperCase().toCharArray();		// taking pattern as input from user...
		
		System.out.print("Enter the text = ");
		
		text = scanner.nextLine().trim().toUpperCase().toCharArray();		// taking text as input from user...
		
		scanner.close();		// closing scanner...
		
		alphabets = main.getAlphabets(pattern);
		
		main.print(alphabets, "\nAlphabets\t\t\t");
		main.print(main.fasterQuickSearch(alphabets, pattern, text), "Match found at indices\t\t");
	}
	
	private void print(int[] array, String text) {
		text += "= [ ";
		
		for (int i = 0; i < array.length; i++) {
			text += array[i];
			
			if (i == array.length - 1) {
				text += " ]";
			}
			else {
				text += ", ";
			}
		}
		
		System.out.println(text);
	}
	
	private void print(char[] array, String text) {
		text += "= [ ";
		
		for (int i = 0; i < array.length; i++) {
			text += array[i];
			
			if (i == array.length - 1) {
				text += " ]";
			}
			else {
				text += ", ";
			}
		}
		
		System.out.println(text);
	}
	
	private char[] getAlphabets(char[] pattern) {
		int i;
		char[] alphabets;

		Iterator<Character> iterator;
		SortedSet<Character> sortedSet = new TreeSet<Character>();

		for (i = 0; i < pattern.length; i++) {
			sortedSet.add(pattern[i]);
		}

		i = 0;
		alphabets = new char[sortedSet.size()];
		iterator = sortedSet.iterator();

		while (iterator.hasNext()) {
			alphabets[i] = iterator.next();
			
			i++;
		}

		return alphabets;
	}
	
	private int getIndexOf(char character, char[] alphabets) {
		for (int i = 0; i < alphabets.length; i++) {
			if (alphabets[i] == character) {
				return i;
			}
		}
		
		return -1;
	}
	
	private int[] getBadCharacterShifts(int patternLength, char[] alphabets, char[] pattern) {
		int i;
		int[] badCharacterShiftArray = new int[alphabets.length];
		
		for (i = 0; i < alphabets.length; i++) {
			badCharacterShiftArray[i] = patternLength;
		}
		
		for (i = 0; i < patternLength; i++) {
			badCharacterShiftArray[getIndexOf(pattern[i], alphabets)] = patternLength - i;
		}

		return badCharacterShiftArray;
	}

	private int getMaximalExpectedShiftPosition(char[] alphabets, char[] pattern) {
		int i, indexOfCharacter, expectedSkip = 0, maximalExpectedShiftValue = 0, maximalExpectedShiftPosition = 0;
		int[] previousPositionOfCharacters = new int[alphabets.length];

		for (i = 0; i < previousPositionOfCharacters.length; i++) {
			previousPositionOfCharacters[i] = -1;
		}

		for (i = 0; i < pattern.length; i++) {
			indexOfCharacter = getIndexOf(pattern[i], alphabets);
			expectedSkip = expectedSkip + alphabets.length - (i - previousPositionOfCharacters[indexOfCharacter]);
			previousPositionOfCharacters[indexOfCharacter] = i;

			if (expectedSkip >= maximalExpectedShiftValue) {
				maximalExpectedShiftValue = expectedSkip;
				maximalExpectedShiftPosition = i;
			}
		}

		return maximalExpectedShiftPosition;
	}

	private boolean isMatchFound(int offset, char[] pattern, char[] text) {
		for (int i = 0; i < pattern.length; i++) {
			if (pattern[i] != text[i + offset]) {
				return false;
			}
		}

		return true;
	}
	
	private int[] getIntegerArray(ArrayList<Integer> arrayList) {
		int[] integerArray = new int[arrayList.size()];
		
		for (int i = 0; i < integerArray.length; i++) {
			integerArray[i] = arrayList.get(i);
		}
		
		return integerArray;
	}
	
	private int[] fasterQuickSearch(char[] alphabets, char[] pattern, char[] text) {
		int i = 0, maximalExpectedShiftPosition = getMaximalExpectedShiftPosition(alphabets, pattern);
		int[] nextBadCharacterShifts = getBadCharacterShifts(maximalExpectedShiftPosition, alphabets, pattern),
				badCharacterShifts = getBadCharacterShifts(pattern.length, alphabets, pattern);
		
		ArrayList<Integer> indices = new ArrayList<Integer>();
		
		System.out.println("Maximal expected shift position\t= " + maximalExpectedShiftPosition);
		print(nextBadCharacterShifts, "Next\t\t\t\t");
		print(badCharacterShifts, "Bad character shifts\t\t");
		
		while (i <= text.length - pattern.length) {
			while (pattern[maximalExpectedShiftPosition] != text[i + maximalExpectedShiftPosition]) {
				i += nextBadCharacterShifts[getIndexOf(text[i + maximalExpectedShiftPosition], alphabets)];
				
				if (i >= text.length - pattern.length) {
					return null;
				}
			}
			
			if (isMatchFound(i, pattern, text)) {
				indices.add(i);
			}
			
			i += badCharacterShifts[getIndexOf(text[i + pattern.length], alphabets)];
		}
		
		return getIntegerArray(indices);
	}
	
}