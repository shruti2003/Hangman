
/**
 * CS312 Assignment 6.
 * 
 * On my honor, Shruti Patel, this programming assignment is my own work and I have
 * not shared my solution with any other student in the class.
 *
 * A program to play Hangman.
 *
 * Email Address: shrutipatel@utexas.edu 
 * UTEID: sp44725 
 * Section 5 digit ID: 52550 
 * Grader name: Nathan 
 * Number of slip days used on this assignment: 0
 */

import java.util.Scanner;

public class Hangman {

	public static final char START = 'A';
	public static final char END = 'Z';
	public static final int GUESS_LIMIT = 5;

	public static void main(String[] args) {
		intro();
		PhraseBank phrases = buildPhraseBank(args);
		// CS312 Students -- Do not create any additional Scanners.
		Scanner keyboard = new Scanner(System.in);

		// CS312 students: add your code here

		// Create boolean if user wants to play
		boolean playGame = true;
		startGame(phrases);
		checkGuess(phrases, keyboard, phrases.getNextPhrase(), playGame);
	}

	// CS312 students: add your methods here.
	// Create a method that starts the game and introduces the topic
	public static void startGame(PhraseBank phrases) {
		System.out.println();
		System.out.println("I am thinking of a " + phrases.getTopic() + " ...");
		System.out.println();
	}

	// Create a method that creates the star(*) string for the phrase
	public static String createStarString(String phrase) {
		String displayPhrase = "";
		for (int star = 0; star < phrase.length(); star++) {
			if (phrase.charAt(star) == '_') {
				displayPhrase += "_";
			} else {
				displayPhrase += "*";
			}
		}
		System.out.println("The current phrase is " + displayPhrase);
		System.out.println();
		return displayPhrase;
	}

	// create a method that gets the current phrase and eliminates correctly guessed
	// letters
	public static String getCurrentPhrase(String guess, String displayPhrase, String phrase) {
		//check to see if guess matches a chracter in the phrase and then reveal a star
		for (int character = 0; character < phrase.length(); character++) {
			if (phrase.charAt(character) == guess.charAt(0)) {
				displayPhrase = displayPhrase.substring(0, character) + 
						guess + displayPhrase.substring(character + 1);
			}
		}
		// only print current phrase if guess is valid and display phrase has *
		if (isValidGuess(guess, displayPhrase) && displayPhrase.contains("*")) {
			System.out.println("The current phrase is " + displayPhrase);
			System.out.println();
		}
		return displayPhrase;
	}

	// create a method that returns string of the alphabet options at the start
	public static String alphabet() {
		String alphabet = "";
		for (char remainingChar = START; remainingChar < END; remainingChar++) {
			alphabet += remainingChar + "--";
		}
		String remainingLetter = alphabet + "Z";
		return remainingLetter;
	}

	// create a method that accounts for the letters already guessed and removes
	// them from the alphabet
	public static String letterRemaining(boolean valid, 
			String guess, String remainingLetter) {
		if (guess != " " && valid) {
			final int THREE = 3;
			final int TWO = 2;
			int indexOfGuess = remainingLetter.indexOf(guess);
			int length = remainingLetter.length();
			//eliminate the guessed words
			if (indexOfGuess <= length / THREE / TWO) {
				remainingLetter = remainingLetter.substring(0, indexOfGuess)
						+ remainingLetter.substring(indexOfGuess + THREE);
			} else {
				remainingLetter = remainingLetter.substring(0, indexOfGuess - TWO)
						+ remainingLetter.substring(indexOfGuess + 1);
			}
		}
		return remainingLetter;
	}

	// create a method that checks to see if the guess the user inputted is valid
	public static boolean isValidGuess(String guess, String remainingLetter) {
		boolean guessValid = true;
		String firstLetter = guess.substring(0, 1);
		//if input is not a letter
		if (!(guess.charAt(0) >= START && guess.charAt(0) <= END)) {
			guessValid = false;
		}
		//if input has already been guessed
		if (!remainingLetter.contains(firstLetter)) {
			guessValid = false;
		}
		//if input is not valid
		if (!guessValid) {
			guess = firstLetter.toLowerCase() + guess.substring(1);
			System.out.println(guess + " is not a valid guess.");
		}
		return guessValid;
	}

	// create a method which runs the game while a condition is true and calls other
	// methods initialized above
	public static boolean checkGuess(PhraseBank phrases, 
			Scanner key, String phrase, boolean playGame) {
		int guessCount = 0;
		String guess = "";
		boolean valid = true;
		String letterLeft = letterRemaining(valid, " ", alphabet());
		String currentPhrase = createStarString(phrase);
		//while game continues keep updating the availble letters and current phrase
		while (guessCount < GUESS_LIMIT && currentPhrase.contains("*") && playGame) {
			System.out.println("The letters you have not guessed yet are: \n" + letterLeft);
			guess = getGuess(letterLeft, key, guess);
			valid = isValidGuess(guess, letterLeft);
			if (valid) {
				guess = printGuess(guess);
				if (phrase.contains(guess)) {
					present(guessCount);
					currentPhrase = getCurrentPhrase(guess, currentPhrase, phrase);
				} else {
					guessCount = notPresent(guessCount, currentPhrase);
				}
			}
			letterLeft = letterRemaining(valid, guess, letterLeft);
		}
		playGame = winOrLose(guessCount, key, playGame, currentPhrase, phrase, phrases);
		return playGame;
	}

	// create a method that gets the guess and outputs the result
	public static String getGuess(String letterleft, Scanner key, String guess) {
		System.out.println();
		System.out.print("Enter your next guess: ");
		guess = key.nextLine().toUpperCase();
		System.out.println();
		return guess;
	}

	// print user guess
	public static String printGuess(String guess) {
		guess = guess.substring(0, 1);
		System.out.print("You guessed " + guess + ".");
		System.out.println();
		System.out.println();
		return guess;
	}

	// create a method that accounts for when the guess is present in the phrase
	public static void present(int guessCount) {
		System.out.println("That is present in the secret phrase.");
		System.out.println();
		System.out.println("Number of wrong guesses so far: " + guessCount);
	}

	// create a method that accounts for when the guess is not present in the phrase
	public static int notPresent(int guessCount, String currentPhrase) {
		System.out.println("That is not present in the secret phrase.");
		System.out.println();
		guessCount++;
		System.out.println("Number of wrong guesses so far: " + guessCount);
		if (guessCount <= GUESS_LIMIT - 1) {
			System.out.println("The current phrase is " + currentPhrase);
			System.out.println();
		}
		return guessCount;
	}

	// determine if the user is a winner or loser
	public static boolean winOrLose(int guessCount, Scanner key, 
			boolean playGame, String currentPhrase, String phrase,
			PhraseBank phrases) {
		if (!currentPhrase.contains("*")) {
			System.out.println("The phrase is " + currentPhrase + ".");
			System.out.println("You win!!");
			end(phrase, currentPhrase, phrases, playGame, key);
		} else if (guessCount >= GUESS_LIMIT) {
			System.out.println("You lose. The secret phrase was " + phrase);
			end(phrase, currentPhrase, phrases, playGame, key);
		}
		return playGame;
	}
	// end the game based on the users input
	public static void end(String phrase, String currentPhrase,
			PhraseBank phrases, boolean playGame, Scanner key) {
		System.out.println("Do you want to play again?");
		System.out.print("Enter 'Y' or 'y' to play again: ");
		String yesOrNo = key.nextLine();
		yesOrNo = yesOrNo.toLowerCase();
		if (yesOrNo.charAt(0) == 'n') {
			playGame = false;
		} else if (yesOrNo.charAt(0) == 'y') {
			startGame(phrases);
			checkGuess(phrases, key, phrases.getNextPhrase(), playGame);
		}
	}

	// Build the PhraseBank.
	// If args is empty or null, build the default phrase bank.
	// If args is not null and has a length greater than 0
	// then the first elements is assumed to be the name of the
	// file to build the PhraseBank from.
	public static PhraseBank buildPhraseBank(String[] args) {
		PhraseBank result;
		if (args == null || args.length == 0 || args[0] == null || args[0].length() == 0) {
			result = new PhraseBank();
			// CS312 students, uncomment this line if you would like less predictable
			// behavior
			// from the PhraseBank during testing. NOTE, this line MUST be commented out
			// in the version of the program you turn in or your will fail all tests.
			// THE FOLLOWING LINE MUST BE COMMENTED OUT IN THE VERSION YOU SUBMIT!!!
			// result = new PhraseBank("HangmanMovies.txt", true);
		} else {
			result = new PhraseBank(args[0]);
		}
		return result;
	}

	// show the intro to the program
	public static void intro() {
		System.out.println("This program plays the game of hangman.");
		System.out.println();
		System.out.println("The computer will pick a random phrase.");
		System.out.println("Enter letters for your guess.");
		System.out.println("After 5 wrong guesses you lose.");
	}
}