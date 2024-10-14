
public class Position {
	private int x, y; 
	private int degres; 
	private char startSide; //b ou g pour blueSide ou greenSide
	
	
	public Position(int x, int y, char startSide) {
		this.x=x; 
		this.y=y;
		degres = 0;
		this.startSide=startSide; 
	}
	
	
	
}
