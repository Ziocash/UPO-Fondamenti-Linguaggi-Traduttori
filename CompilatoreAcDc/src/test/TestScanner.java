package test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;

import org.junit.Test;

import scanner.Scanner;
import token.Token;
import token.TokenType;

public class TestScanner {

	// @Test
	// public void testScanEOF() throws IOException {
	// 	String path = "C:\\Users\\Simone Gattini\\source\\repos\\UPO-Fondamenti-Linguaggi-Traduttori\\CompilatoreAcDc\\src\\test\\data\\testEOF.txt";
	// 	Scanner scanner = new Scanner(path);
	// 	assertNull(scanner.nextToken());
	// 	fail("Unexpected value");

	// }

	@Test
	public void testScanId() throws IOException {
		String path = "src/test/data/testIdKw.txt";
		Scanner scanner = new Scanner(path);
	}

}