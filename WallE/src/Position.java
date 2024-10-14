
public class Position {
	private int x, y; 
	private int degres;
	
	public Position() {
		this(0,0);
	}
	
	public Position(int x, int y) {
		this.x=x; 
		this.y=y;
		degres = 0;
	}
	public double getX() {
		return this.x;
		
	}
	
	public double getY() {
		return this.y;
		
	}
	
	public double getDegres() {
		return this.degres;
		
	}
	
	
	public void setX (double a) {
		x = a;
	}
	
	public void setY (double b) {
		y = b;
	}
	
	
	public void setDegres (double d) {
	 this.degres=d ;
	}
	
	public void  UpdatePosition( int d ) {
		double a,o;
		double degresrad= degres*Math.PI/180;
		a= Math.cos(degresrad) * d ;
		o= Math.sin(degresrad) * d ;
		this.setX(a);
		this.setY(o);
		
		
		}
	
	public static void main(String[] args) {

	


	}
}
