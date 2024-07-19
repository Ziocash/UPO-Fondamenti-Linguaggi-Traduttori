package token;

/**
 * Representing token types
 */
public enum TokenType {
	/**
	 * Representing {@code float} keyword
	 */
	TYFLOAT,
	/**
	 * Representing {@code int} keyword
	 */
	TYINT,
	/**
	 * Representing {@code print} keyword
	 */
	PRINT,
	/**
	 * Representing an identifier (variable name)
	 */
	ID,
	/**
	 * Integer type value
	 */
	INT,
	/**
	 * Float type (max 5 decimals) value
	 */
	FLOAT,
	/**
	 * Assign token to match "="
	 */
	ASSIGN,
	/**
	 * Assign token to match "+"
	 */
	PLUS,
	/**
	 * Assign token to match "-"
	 */
	MINUS,
	/**
	 * Assign token to match "*"
	 */
	TIMES,
	/**
	 * Assign token to match "/"
	 */
	DIV,
	/**
	 * Assign token to match ";"
	 */
	SEMI,
	/**
	 * Assign token to match end of file
	 */
	EOF;
}
