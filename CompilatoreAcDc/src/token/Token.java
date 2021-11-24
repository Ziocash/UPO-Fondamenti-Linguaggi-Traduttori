package token;

public class Token {

	private int row;
	private TokenType type;
	private String value;
	
	public Token(TokenType type, int row, String value) {
		this.type = type;
		this.row = row;
		this.value = value;
	}
	
	public Token(TokenType type, int row) {
		this.type = type;
		this.row = row;
	}

    public int getRow() {
		return row;
	}
	
	public void setRow(int row) {
		this.row = row;
	}

	public TokenType getType() {
		return type;
	}

	public void setType(TokenType type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
    
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("<").append(type).append(",").append("r:"+row);
		builder = value != null ? builder.append(","+value+">") : builder.append(">");
		return builder.toString();
	}

     

}
