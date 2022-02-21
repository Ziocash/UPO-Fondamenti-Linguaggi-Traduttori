package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.Test;

import exception.LexicalException;
import scanner.Scanner;
import token.Token;
import token.TokenType;

public class TestScanner {

	@Test
	public void testScanId() throws IOException, LexicalException {
		String path = "C:\\Users\\Simone Gattini\\source\\repos\\UPO-Fondamenti-Linguaggi-Traduttori\\CompilatoreAcDc\\src\\test\\data\\testIdKw.txt";
		Scanner scanner = new Scanner(path);
		Token token = scanner.nextToken();
		assertTrue(token.getType() == TokenType.TYINT);
		assertEquals(1, token.getRow());
		token = scanner.nextToken();
		assertTrue(token.getType() == TokenType.TYFLOAT);
		assertEquals(2, token.getRow());
		token = scanner.nextToken();
		assertFalse(token.getType() == TokenType.TYFLOAT);
		assertTrue(token.getType() == TokenType.ID);
		assertEquals(2, token.getRow());
		token = scanner.nextToken();
		assertTrue(token.getType() == TokenType.PRINT);
		assertEquals(3, token.getRow());
		token = scanner.nextToken();
		assertFalse(token.getType() == TokenType.PRINT);
		assertTrue(token.getType() == TokenType.ID);
		assertEquals(3, token.getRow());
		token = scanner.nextToken();
		assertTrue(token.getType() == TokenType.ID);
		assertEquals(4, token.getRow());
		token = scanner.nextToken();
		assertFalse(token.getType() == TokenType.TYINT);
		assertTrue(token.getType() == TokenType.ID);
		assertEquals(5, token.getRow());
		token = scanner.nextToken();
		assertTrue(token.getType() == TokenType.TYINT);
		assertEquals(6, token.getRow());
		token = scanner.nextToken();
		assertTrue(token.getType() == TokenType.ID);
		assertEquals(6, token.getRow());
	}

	@Test
	public void testScanNumber() throws IOException, LexicalException {
		String path = "C:\\Users\\Simone Gattini\\source\\repos\\UPO-Fondamenti-Linguaggi-Traduttori\\CompilatoreAcDc\\src\\test\\data\\testNumbers.txt";
		Scanner scanner = new Scanner(path);
		Token token = scanner.nextToken();
		assertTrue(token.getType() == TokenType.INT);
		assertEquals("30000", token.getValue());
		assertEquals(1, token.getRow());
		assertThrows(LexicalException.class, () -> {
			scanner.nextToken();
		});
		token = scanner.nextToken();
		assertTrue(token.getType() == TokenType.FLOAT);
		assertEquals("13.454", token.getValue());
		assertEquals(4, token.getRow());
		token = scanner.nextToken();
		assertTrue(token.getType() == TokenType.FLOAT);
		assertEquals("098.895", token.getValue());
		assertEquals(4, token.getRow());
		token = scanner.nextToken();
		assertTrue(token.getType() == TokenType.INT);
		assertEquals("45668", token.getValue());
		assertEquals(5, token.getRow());
		assertThrows(LexicalException.class, () -> {
			scanner.nextToken();
		});
		assertThrows(LexicalException.class, () -> {
			scanner.nextToken();
		});
		token = scanner.nextToken();
		assertTrue(token.getType() == TokenType.EOF);
		assertEquals(8, token.getRow());
	}

	@Test
	public void testScanEOF() throws IOException, LexicalException {
		String path = "C:\\Users\\Simone Gattini\\source\\repos\\UPO-Fondamenti-Linguaggi-Traduttori\\CompilatoreAcDc\\src\\test\\data\\testEOF.txt";
		Scanner scanner = new Scanner(path);
		Token token = scanner.nextToken();
		assertTrue(token.getType() == TokenType.EOF);
		assertEquals(3, token.getRow());
	}

	@Test
	public void testPeekToken() throws IOException, LexicalException {
		String path = "C:\\Users\\Simone Gattini\\source\\repos\\UPO-Fondamenti-Linguaggi-Traduttori\\CompilatoreAcDc\\src\\test\\data\\testPeek.txt";
		Scanner scanner = new Scanner(path);
		Token token = scanner.nextToken();
		assertEquals(token, scanner.peekToken());
		assertEquals(1, token.getRow());
		assertEquals("15", token.getValue());
		token = scanner.nextToken();
		assertEquals(token, scanner.peekToken());
		assertEquals(1, token.getRow());
		assertEquals("14", token.getValue());
	}

	@Test
	public void testOperators() throws IOException, LexicalException {
		String path = "C:\\Users\\Simone Gattini\\source\\repos\\UPO-Fondamenti-Linguaggi-Traduttori\\CompilatoreAcDc\\src\\test\\data\\testOperators.txt";
		Scanner scanner = new Scanner(path);
		Token token = scanner.nextToken();
		assertTrue(token.getType() == TokenType.PLUS);
		assertEquals(1, token.getRow());
		token = scanner.nextToken();
		assertTrue(token.getType() == TokenType.MINUS);
		assertEquals(2, token.getRow());
		token = scanner.nextToken();
		assertTrue(token.getType() == TokenType.TIMES);
		assertEquals(2, token.getRow());
		token = scanner.nextToken();
		assertTrue(token.getType() == TokenType.DIV);
		assertEquals(3, token.getRow());
		token = scanner.nextToken();
		assertTrue(token.getType() == TokenType.ASSIGN);
		assertEquals(8, token.getRow());
		token = scanner.nextToken();
		assertTrue(token.getType() == TokenType.SEMI);
		assertEquals(10, token.getRow());
	}

	@Test
	public void testAllTokens() throws IOException, LexicalException {
		String path = "C:\\Users\\Simone Gattini\\source\\repos\\UPO-Fondamenti-Linguaggi-Traduttori\\CompilatoreAcDc\\src\\test\\data\\testGeneral.txt";
		Scanner scanner = new Scanner(path);
		Token token = scanner.nextToken();
		assertTrue(token.getType() == TokenType.TYINT);
		assertEquals(2, token.getRow());
		token = scanner.nextToken();
		assertTrue(token.getType() == TokenType.ID);
		assertEquals(2, token.getRow());
		token = scanner.nextToken();
		assertTrue(token.getType() == TokenType.SEMI);
		assertEquals(2, token.getRow());
		//
		token = scanner.nextToken();
		assertTrue(token.getType() == TokenType.ID);
		assertEquals(3, token.getRow());
		token = scanner.nextToken();
		assertTrue(token.getType() == TokenType.ASSIGN);
		assertEquals(3, token.getRow());
		assertThrows(LexicalException.class, () -> {
			scanner.nextToken();
		});
		token = scanner.nextToken();
		assertTrue(token.getType() == TokenType.SEMI);
		assertEquals(3, token.getRow());
		//
		token = scanner.nextToken();
		assertTrue(token.getType() == TokenType.TYFLOAT);
		assertEquals(5, token.getRow());
		token = scanner.nextToken();
		assertTrue(token.getType() == TokenType.ID);
		assertEquals(5, token.getRow());
		token = scanner.nextToken();
		assertTrue(token.getType() == TokenType.SEMI);
		assertEquals(5, token.getRow());
		//
		token = scanner.nextToken();
		assertTrue(token.getType() == TokenType.ID);
		assertEquals(6, token.getRow());
		token = scanner.nextToken();
		assertTrue(token.getType() == TokenType.ASSIGN);
		assertEquals(6, token.getRow());
		token = scanner.nextToken();
		assertTrue(token.getType() == TokenType.ID);
		assertEquals(6, token.getRow());
		token = scanner.nextToken();
		assertTrue(token.getType() == TokenType.PLUS);
		assertEquals(6, token.getRow());
		token = scanner.nextToken();
		assertTrue(token.getType() == TokenType.FLOAT);
		assertEquals(6, token.getRow());
		token = scanner.nextToken();
		assertTrue(token.getType() == TokenType.SEMI);
		assertEquals(6, token.getRow());
		//
		token = scanner.nextToken();
		assertTrue(token.getType() == TokenType.PRINT);
		assertEquals(7, token.getRow());
		token = scanner.nextToken();
		assertTrue(token.getType() == TokenType.ID);
		assertEquals(7, token.getRow());
		token = scanner.nextToken();
		assertTrue(token.getType() == TokenType.SEMI);
		assertEquals(7, token.getRow());

	}
}