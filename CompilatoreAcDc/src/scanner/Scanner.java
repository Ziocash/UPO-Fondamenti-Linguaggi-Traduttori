package scanner;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import token.*;

public class Scanner {
	final char EOF = (char) -1; // int 65535
	private int row;
	private PushbackReader buffer;
	private String log;

	private List<Character> skipChars; // ' ', '\n', '\t', '\r', EOF
	private List<Character> letters; // 'a',...'z'
	private List<Character> numbers; // '0',...'9'

	private HashMap<Character, TokenType> operatorsMap; // '+', '-', '*', '/', '=', ';'
	private HashMap<String, TokenType> keyWordsMap; // "print", "float", "int"

	public Scanner(String fileName) throws FileNotFoundException {
		this.buffer = new PushbackReader(new FileReader(fileName));
		row = 1;
		skipChars = Arrays.asList(' ', '\n', '\t', '\r', EOF);
		letters = Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
				'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z');
		numbers = Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9');
		operatorsMap = new HashMap<Character, TokenType>() {
			{
				put('+', TokenType.PLUS);
				put('-', TokenType.MINUS);
				put('*', TokenType.TIMES);
				put('/', TokenType.DIV);
				put('=', TokenType.ASSIGN);
				put(';', TokenType.SEMI);
			}
		};

		keyWordsMap = new HashMap<String, TokenType>() {
			{
				put("print", TokenType.PRINT);
				put("float", TokenType.TYFLOAT);
				put("int", TokenType.TYINT);
			}
		};
	}

	/*
	 * private Token peekToken() throws IOException { ..... }
	 */

	public Token nextToken() throws IOException {

		// nextChar contiene il prossimo carattere dell'input.
		char nextChar = peekChar();

		// Avanza nel buffer leggendo i carattere in skipChars
		// incrementando riga se leggi '\n'.
		// Se raggiungi la fine del file ritorna il Token EOF

		while (nextChar != '\n') {
			if (skipChars.contains(nextChar))
				readChar();

			if (numbers.contains(nextChar))
				return scanNumber();

			if (letters.contains(nextChar))
				return scanId();

			if (nextChar == EOF)
				return new Token(TokenType.EOF, row);
		}

		// Se nextChar e' in numbers
		// return scanNumber()
		// che legge sia un intero che un float e ritorna il Token INUM o FNUM
		// i caratteri che leggete devono essere accumulati in una stringa
		// che verra' assegnata al campo valore del Token

		// Se nextChar e' in letters
		// return scanId()
		// che legge tutte le lettere minuscole e ritorna un Token ID o
		// il Token associato Parola Chiave (per generare i Token per le
		// parole chiave usate l'HaskMap di corrispondenza

		// Se nextChar e' in operators
		// ritorna il Token associato con l'operatore o il delimitatore

		// Altrimenti il carattere NON E' UN CARATTERE LEGALE

		return null;

	}

	private Token scanNumber() throws IOException {
		char nextChar = peekChar();
		StringBuilder sb = new StringBuilder();
		Token numberToken = new Token(TokenType.INT, row);
		while (numbers.contains(nextChar)) {
			if (nextChar == '.')
				numberToken.setType(TokenType.FLOAT);
			sb.append(readChar());
		}
		return numberToken;

	}

	private Token scanId() throws IOException {
		char nextChar = peekChar();
		StringBuilder sb = new StringBuilder();
		Token idToken = new Token(TokenType.ID, row);
		while (letters.contains(nextChar)) {
			sb.append(readChar());
		}
		return null;
	}

	private char readChar() throws IOException {
		return ((char) this.buffer.read());
	}

	private char peekChar() throws IOException {
		char c = (char) buffer.read();
		buffer.unread(c);
		return c;
	}
}
