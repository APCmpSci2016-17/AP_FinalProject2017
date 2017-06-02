
public class Corner {

	private int r;
	private int c;
	private double magnitude;
	
	public Corner(int r, int c, double magnitude){
		this.r = r;
		this.c = c;
		this.magnitude = magnitude;
	}
	
	public void nonMaxSuppression(int radius, double[][] arr) {
		//radius = 39;
		for(int i = 0; i < radius; i++) {
			for(int j = 0; j < radius; j++) {
				//System.out.print(i -(radius/2) + r + ", " + (j - radius/2 + c) + "\n");
				if(i -(radius/2) + r > 0 && i -(radius/2) + r < arr.length && j - radius/2 + c > 0 && j - radius/2 + c < arr[0].length) {
					arr[i -(radius/2) + r][j - radius/2 + c] = 0;
				}
			}
		}
	}
	
	public void markup(int radius, double [][] arr){
		for(int i = 0; i < radius; i++){
			for(int j = 0; j < radius; j++){
				arr[i -(radius/2) + r][j - radius/2 + c] = 255;
			}
		}
	}

	public int getR() {
		return r;
	}

	public void setR(int r) {
		this.r = r;
	}

	public int getC() {
		return c;
	}

	public void setC(int c) {
		this.c = c;
	}

	public double getMagnitude() {
		return magnitude;
	}

	public void setMagnitude(double magnitude) {
		this.magnitude = magnitude;
	}
	
}

