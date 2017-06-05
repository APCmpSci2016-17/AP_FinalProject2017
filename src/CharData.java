public class CharData {
	private int charID;
	private int pixelDensity;
	private int cornersTop;
	private int cornersRight;
	private int cornersBot;
	private int cornersLeft;
	private int cornersTopRight;
	private int cornersBotRight;
	private int cornersTopLeft;
	private int cornersBotLeft;
	
	public CharData(int charID, int pixelDensity, int cornersTop, int cornersRight, int cornersBot, int cornersLeft,
					int cornersTopRight, int cornersTopLeft, int cornersBotRight, int cornersBotLeft) {
		this.charID = charID;
		this.pixelDensity = pixelDensity;
		this.cornersTop = cornersTop;
		this.cornersRight = cornersRight;
		this.cornersBot = cornersBot;
		this.cornersLeft = cornersLeft;
		this.cornersTopRight = cornersTopRight;
		this.cornersBotRight = cornersBotRight;
		this.cornersTopLeft = cornersTopLeft;
		this.cornersBotLeft = cornersBotLeft;
	}
	
	public int getPixelDensity() {
		return pixelDensity;
	}
	public int getCharID() {
		return charID;
	}
	public int getCornersTop() {
		return cornersTop;
	}
	public int getCornersRight() {
		return cornersRight;
	}
	public int getCornersBot() {
		return cornersBot;
	}
	public int getCornersLeft() {
		return cornersLeft;
	}
	public int getCornersTopRight() {
		return cornersTopRight;
	}
	public int getCornersBotRight() {
		return cornersBotRight;
	}
	public int getCornersTopLeft() {
		return cornersTopLeft;
	}
	public int getCornersBotLeft() {
		return cornersBotLeft;
	}
	
	
}