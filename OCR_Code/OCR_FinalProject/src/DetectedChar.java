
import java.util.ArrayList;

public class DetectedChar {
	// Corners
	private int cornersTop;
	private int[] position; //[r, c]
	private int cornersRight;
	private int cornersBot;
	private int cornersLeft;
	private int cornersTopRight;
	private int cornersBotRight;
	private int cornersTopLeft;
	private int cornersBotLeft;

	// Calculated data
	private int detectedCharId;
	private double pixelDensity; 

	// Data for stuff

	public DetectedChar(double[][] imgData, int[] position) {
		this.position[0] = position[0];
		this.position[1] = position[1];
		ArrayList<Corner> corners = cornerDetection(imgData);
		pixelDensity = pixelDensity(imgData);
		int[] mid = { imgData.length / 2, imgData[0].length / 2 }; // [r, c]
		// //*Connor
		// hey Ryan
		// why is it
		// x,y
		// instead
		// of r, c
		for (int i = 0; i < corners.size(); i++) {
			int c = corners.get(i).getC();
			int r = corners.get(i).getR();
			boolean left = false;
			boolean right = false;
			boolean top = false;
			boolean bot = false;

			if (c < mid[1]) {
				cornersLeft++;
				left = true;
			} else if (c > mid[1]) {
				cornersRight++;
				right = true;
			}
			if (r > mid[0]) {
				cornersBot++;
				bot = true;
			} else if (r < mid[0]) {
				cornersTop++;
				top = true;
			}

			if (top && right) {
				cornersTopRight++;
			} else if (top && left) {
				cornersTopLeft++;
			} else if (bot && right) {
				cornersBotRight++;
			} else if (bot && left) {
				cornersBotLeft++;
			}

		}
		
		
		
		//printData();
	}

	private double pixelDensity(double[][] imgData) {
		int sum = 0;
		for(int i = 0; i < imgData.length; i++){
			for(int j = 0; j < imgData[0].length; j++){
				if(imgData[i][j] == 0){
					sum++;
				}
			}
		}
		return ((double)sum) / (imgData.length * imgData[0].length);
	}

	/*
	 * Returns ArrayList of corners locations
	 */
	private ArrayList<Corner> cornerDetection(double[][] imgData) {
		ArrayList<Corner> corners = new ArrayList<Corner>();
		double[][] rmap = TemplateMatching.findRMAP(imgData);
		TemplateMatching.displayImg(rmap);
		double[][] nonMax = new double[rmap.length][rmap[0].length];
		for (int i = 0; i < rmap.length; i++) {
			for (int j = 0; j < rmap[0].length; j++) {
				nonMax[i][j] = rmap[i][j];// copies array
			}
		}

		double max = nonMax[0][0];
		int r = 0;
		int c = 0;
		boolean maxThreshold = true;
		do {

			// find maximum
			max = nonMax[0][0];
			r = 0;
			c = 0;
			for (int i = 0; i < rmap.length; i++) {
				for (int j = 0; j < rmap[0].length; j++) {
					if (nonMax[i][j] > max) {
						max = nonMax[i][j]; // finds the maximum then finds its
						// position
						r = i;
						c = j;
					}
				}
			}

			if (max > TemplateMatching.threshold) {
				Corner myCorner = new Corner(r, c, nonMax[r][c]);
				myCorner.nonMaxSuppression((imgData.length + imgData[0].length) / 7, nonMax);
				corners.add(myCorner);
			} else {
				maxThreshold = false;
				break;
			}

		} while (maxThreshold);

		// TemplateMatching.displayImg(nonMax);
		return corners;
	}

	public int getDetectedCharId() {
		return detectedCharId;
	}

	public void printData() {
		System.out.println("Pixel density: " + pixelDensity);
		System.out.println("__Corners__");
		System.out.println("Top " + cornersTop);
		System.out.println("Right " + cornersRight);
		System.out.println("Bot " + cornersBot);
		System.out.println("Left " + cornersLeft);
		System.out.println("Top Right " + cornersTopRight);
		System.out.println("Bot Right " + cornersBotRight);
		System.out.println("Top Left " + cornersTopLeft);
		System.out.println("Bot Left " + cornersBotLeft);
	}

}