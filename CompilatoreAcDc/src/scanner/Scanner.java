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
	final char EOF = (char) -1; // int 65535
	private int row = 1;
	private PushbackReader buffer;
	private String log;

	private Token token = null;

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

	public Token peekToken() throws IOException, LexicalException {
		if (token == null)
			token = nextToken();
		return token;
	}

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

	// Se nextChar e' in letters
	// return scanId()
	// che legge tutte le lettere minuscole e ritorna un Token ID o
	// il Token associato Parola Chiave (per generare i Token per le
	// parole chiave usate l'HashMap di corrispondenza
	private Token scanId() throws IOException {
		StringBuilder sb = new StringBuilder();
		while (letters.contains(peekChar())) {
			sb.append(readChar());
		}

		if (keyWordsMap.containsKey(sb.toString()))
			return new Token(keyWordsMap.get(sb.toString()), row);
		return new Token(TokenType.ID, row, sb.toString());
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
