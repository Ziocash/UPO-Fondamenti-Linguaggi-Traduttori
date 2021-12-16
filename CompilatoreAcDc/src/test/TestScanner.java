package test;

import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileNotFoundException;
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
		assertEquals(token.getRow(), 1);
		token = scanner.nextToken();
		assertTrue(token.getType() == TokenType.TYFLOAT);
		assertEquals(token.getRow(), 2);
		token = scanner.nextToken();
		assertFalse(token.getType() == TokenType.TYFLOAT);
		assertTrue(token.getType() == TokenType.ID);
		assertEquals(token.getRow(), 2);
		token = scanner.nextToken();
		assertTrue(token.getType() == TokenType.PRINT);
		assertEquals(token.getRow(), 3);
		token = scanner.nextToken();
		assertFalse(token.getType() == TokenType.PRINT);
		assertTrue(token.getType() == TokenType.ID);
		assertEquals(token.getRow(), 3);
		token = scanner.nextToken();
		assertTrue(token.getType() == TokenType.ID);
		assertEquals(token.getRow(), 4);
		token = scanner.nextToken();
		assertFalse(token.getType() == TokenType.TYINT);
		assertTrue(token.getType() == TokenType.ID);
		assertEquals(token.getRow(), 5);
		token = scanner.nextToken();
		assertTrue(token.getType() == TokenType.TYINT);
		assertEquals(token.getRow(), 6);
		token = scanner.nextToken();
		assertTrue(token.getType() == TokenType.ID);
		assertEquals(token.getRow(), 6);
	}

	@Test
	public void testScanNumber() throws IOException, LexicalException {
		String path = "C:\\Users\\Simone Gattini\\source\\repos\\UPO-Fondamenti-Linguaggi-Traduttori\\CompilatoreAcDc\\src\\test\\data\\testNumbers.txt";
		Scanner scanner = new Scanner(path);
		Token token = scanner.nextToken();
		assertTrue(token.getType() == TokenType.INT);
		assertEquals(token.getValue(), "30000");
		assertEquals(token.getRow(), 1);
		assertThrows(LexicalException.class, () -> {
			scanner.nextToken();
		});
		token = scanner.nextToken();
		assertTrue(token.getType() == TokenType.FLOAT);
		assertEquals(token.getValue(), "13.454");
		assertEquals(token.getRow(), 4);
		token = scanner.nextToken();
		assertTrue(token.getType() == TokenType.FLOAT);
		assertEquals(token.getValue(), "098.895");
		assertEquals(token.getRow(), 4);
		token = scanner.nextToken();
		assertTrue(token.getType() == TokenType.INT);
		assertEquals(token.getValue(), "45668");
		assertEquals(token.getRow(), 5);
		assertThrows(LexicalException.class, () -> {
			scanner.nextToken();
		});
		assertThrows(LexicalException.class, () -> {
			scanner.nextToken();
		});
		token = scanner.nextToken();
		assertTrue(token.getType() == TokenType.EOF);
		assertEquals(token.getRow(), 8);
	}

	@Test
	public void testScanEOF() throws IOException, LexicalException {
		String path = "C:\\Users\\Simone Gattini\\source\\repos\\UPO-Fondamenti-Linguaggi-Traduttori\\CompilatoreAcDc\\src\\test\\data\\testEOF.txt";
		Scanner scanner = new Scanner(path);
		Token token = scanner.nextToken();
		assertTrue(token.getType() == TokenType.EOF);
		assertEquals(token.getRow(), 3);
	}

	@Test
	public void testPeekToken() throws IOException, LexicalException {
		String path = "C:\\Users\\Simone Gattini\\source\\repos\\UPO-Fondamenti-Linguaggi-Traduttori\\CompilatoreAcDc\\src\\test\\data\\testNumbers.txt";
		Scanner scanner = new Scanner(path);
		Token token = scanner.nextToken();
		assertEquals(token, scanner.peekToken());
		token = scanner.nextToken();
		assertEquals(token, scanner.peekToken());
	}

}