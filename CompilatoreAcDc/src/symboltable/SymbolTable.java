package symboltable;

import java.util.HashMap;
import java.util.Map.Entry;

/**
 *<p>{@link SymbolTable} class.</p>
 * 
 * <p>Represents a symboltable with {@link String} and {@link Attributes}.</p>
 */
public class SymbolTable {
    private static HashMap<String, Attributes> table;

	private SymbolTable() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * Symbol table initialization.
	 */
	public static void init() {
		table = new HashMap<>();
	}

	/**
	 * <p>Tries to insert a value in the map.</p>
	 * <p>Adds it if it's not present and returns {@code true}, otherwise {@code false}.</p>
	 * @param id the variable name (or id).
	 * @param entry the {@link Attributes} value (containing e.g.: variable type).
	 * @return {@code true} if id is not already in the map, otherwise {@code false}.
	 */
	public static boolean enter(String id, Attributes entry) {
		Attributes value = table.get(id);
		if (value != null)
			return false;
		table.put(id, entry);
		return true;
	}

	/**
	 * <p>Searchs for a given id.</p>
	 * <p>Returns an {@link Attributes} value containing variable info, otherwise {@code null}.</p>
	 * @param id the id to search for.
	 * @return an {@link Attributes} value containing variable info, otherwise {@code null}.
	 */
	public static Attributes lookup(String id) {
		return table.get(id);
	}

	/**
	 * Returns a string representing the whole symbol table.
	 * @return a string representing the whole symbol table.
	 */
	public static String toStr() {
		StringBuilder res = new StringBuilder("symbol table\n=============\n");

		for (Entry<String, Attributes> entry : table.entrySet())
			res.append(String.format("%s   \t%s%n", entry.getKey(), entry.getValue()));

		return res.toString();
	}

	/**
	 * Returns the map size.
	 * @return the map size.
	 */
	public static int size() {
		return (table.size());
	}
}
