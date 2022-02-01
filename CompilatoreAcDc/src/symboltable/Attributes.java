package symboltable;

import ast.LangType;

/**
 * <p>{@link SymbolTable} integration class.</p>
 * 
 * <p>Used to describe or enrich attributes to a variable inserted into the HashMap of the symbol table.</p>
 */
public class Attributes {
	
	/**
	 * Variable type.
	 */
	private LangType type;
	
	/**
	 * Class constructor.
	 * @param type the variable type.
	 */
	public Attributes(LangType type) {
		this.type = type;
	}

	/**
	 * Returns the variable type.
	 * @return the variable type.
	 */
	public LangType getType() {
		return type;
	}
	
	/**
	 * Sets the variable type.
	 * @param type the variable value to be set.
	 */
	public void setType(LangType type) {
		this.type = type;
	}

	/**
	 * Returns the variable type.
	 * 
	 * @return the variable type.
	 */
	@Override
	public String toString() {
		return type.toString();
	}
}