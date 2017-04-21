
public enum Player {
	
	COMPUTER("X"), USER("O"), NONE("-");
	private final String text;
	
	private Player(String text){
		this.text = text;
	}
	
	public String toString(){
		return text;
	}
}
