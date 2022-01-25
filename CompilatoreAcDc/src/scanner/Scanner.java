package scanner;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import exception.LexicalException;
import token.Token;
import token.TokenType;

public class Scanner {
	private static final char EOF = (char) -1; // int 65535
	private int row = 1;
	private PushbackReader buffer;

	private Token token = null;

	private List<Character> skipChars; // ' ', '\n', '\t', '\r', EOF
	private List<Character> letters; // 'a',...'z'
	private List<Character> numbers; // '0',...'9'

	private HashMap<Character, TokenType> operatorsMap; // '+', '-', '*', '/', '=', ';'
	private HashMap<String, TokenType> keyWordsMap; // "print", "float", "int"

	/**
	 * Creates a new {@code Scanner} and set up methods to tokenize given file 
	 * 
	 * @param fileName The name of file to be read
	 * @throws FileNotFoundException If the named file does not exist, is a directory rather than a regular file, or for some other reason cannot be opened for reading.
	 */
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

	/**
	 * Returns the actual token, if token is {@code null} returns the next one 
	 * 
	 * @return The actual token, if token is {@code null} returns the next one
	 * @throws IOException If an I/O error occurs
	 * @throws LexicalException If code is lexically incorrect
	 */
	public Token peekToken() throws IOException, LexicalException {
		if (token == null)
			token = nextToken();
		return token;
	}

	/**
	 * Returns the next token 
	 * 
	 * @return The next token
	 * @throws IOException If an I/O error occurs
	 * @throws LexicalException If code is lexically incorrect
	 */
	public Token nextToken() throws IOException, LexicalException {
		// Avanza nel buffer leggendo i carattere in skipChars
		// incrementando riga se leggi '\n'.
		// Se raggiungi la fine del file ritorna il Token EOF
		while (skipChars.contains(peekChar())) {
			if (peekChar() == '\n')
				row++;
			if (peekChar() == EOF) {
				readChar();
				token = new Token(TokenType.EOF, row);
				return token;
			}
			readChar();
		}

		while (peekChar() != '\n') {

			// Se nextChar e' in numbers
			// return scanNumber()
			// che legge sia un intero che un float e ritorna il Token INUM o FNUM
			// i caratteri che leggete devono essere accumulati in una stringa
			// che verra' assegnata al campo valore del Token
			if (numbers.contains(peekChar()) || peekChar() == '.') {
				token = scanNumber();
				return token;
			}

			// Se nextChar e' in operators
			// ritorna il Token associato con l'operatore o il delimitatore
			if (letters.contains(peekChar())) {
				token = scanId();
				return token;
			}

			if (operatorsMap.containsKey(peekChar())) {
				token = new Token(operatorsMap.get(peekChar()), row);
				readChar();
				return token;
			}
			readChar();
		}
		// Altrimenti il carattere NON E' UN CARATTERE LEGALE
		throw new LexicalException("Illegal character in row " + row);
	}

	/**
	 * Returns a token that represents a numeric value (int or float with max 5 decimals).
	 * 
	 * @return The token representing a numeric value (int or float with 5 decimals).
	 * @throws IOException If an I/O error occurs
	 * @throws LexicalException If code is lexically incorrect
	 */
	private Token scanNumber() throws IOException, LexicalException {
		StringBuilder result = new StringBuilder();
		while (numbers.contains(peekChar())) {
			result.append(readChar());
		}
		if (peekChar() != '.')
			return new Token(TokenType.INT, row, result.toString());

		int count = 0;
		result.append(readChar());
		while (numbers.contains(peekChar())) {
			result.append(readChar());
			count++;
		}

		if (count >= 1 && count <= 5)
			return new Token(TokenType.FLOAT, row, result.toString());

		throw new LexicalException("Uncorrect decimals in row " + row);

	}

	/**
	 * Returns a token that represents an Id (variable or keyword)
	 * 
	 * @return The token that represents a Id (variable or keyword)
	 * @throws IOException If an I/O error occurs
	 */
	private Token scanId() throws IOException {
		StringBuilder sb = new StringBuilder();
		while (letters.contains(peekChar())) {
			sb.append(readChar());
		}

		if (keyWordsMap.containsKey(sb.toString()))
			return new Token(keyWordsMap.get(sb.toString()), row);
		return new Token(TokenType.ID, row, sb.toString());
	}

	/**
	 * Reads a char and consumes it
	 * 
	 * @return The read character
	 * @throws IOException If an I/O error occurs
	 */
	private char readChar() throws IOException {
		return ((char) this.buffer.read());
	}

	/**
	 * Reads a char without consuming it
	 * 
	 * @return The read character
	 * @throws IOException If an I/O error occurs
	 */
	private char peekChar() throws IOException {
		char c = (char) buffer.read();
		buffer.unread(c);
		return c;
	}
}
