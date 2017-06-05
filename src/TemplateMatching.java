import java.util.ArrayList;
public class TemplateMatching {

	static double alpha = 0.05;
	static double threshold = 150;
	static int blackWhite = 100;
	static double [][] kernel = createKernel();
	static ArrayList<DetectedChar> detectedCharacters = new ArrayList<DetectedChar>();

	public static void main(String[] args) {
		//tuning parameters and instantiation of image and arrays
		Picture image = new Picture("minus.jpg");
		double[][][] colored = toMatrix(image.getPixels2D());
		image.explore();

		double[][] grayed = colorToGray(colored); 
		displayImg(findRMAP(grayed));
		double[][] blackWhiteArr = makeBlackAndWhite(grayed, blackWhite); //thresholds the grayed image into black and white only (0 or 255)
		displayImg(blackWhiteArr);
		search(blackWhiteArr);
		displayImg(grayed);
//		for(int i = 0; i < detectedCharacters.size(); i++){
//			System.out.println(detectedCharacters.get(i).toString());
//		}
		
	}
	public static double[][] findRMAP(double[][] grayed) {
		double[][] bordered = createBorderReflect(grayed);
		double[][] filtered = filter(bordered, kernel);
		double[][] gradientX = calcGradientX(filtered);
		double[][] gradientX2 = new double[gradientX.length][gradientX[0].length];
		double[][] gradientY = calcGradientY(filtered);
		double[][] gradientY2 = new double[gradientX.length][gradientX[0].length];
		double[][] gradientXY = new double[gradientX.length][gradientX[0].length];
		double[][] rmap = new double[gradientY.length][gradientY[0].length];



		for(int i = 0; i < gradientX.length; i++){
			for (int j = 0; j < gradientX[0].length; j++){
				gradientXY[i][j] = (gradientX[i][j] * gradientY[i][j]);
			}
		}

		for(int i = 0; i < gradientX.length; i++){
			for (int j = 0; j < gradientX[0].length; j++){
				gradientX2[i][j] = (gradientX[i][j] * gradientX[i][j]);
			}
		}

		for(int i = 0; i < gradientY.length; i++){
			for (int j = 0; j < gradientY[0].length; j++){
				gradientY2[i][j] = (gradientY[i][j] * gradientY[i][j]);
			}
		}
		bordered = createBorderReflect(gradientXY);
		gradientXY = filter(bordered, kernel);
		bordered = createBorderReflect(gradientX2);
		gradientX2 = filter(bordered, kernel);
		bordered = createBorderReflect(gradientY2);
		gradientY2 = filter(bordered, kernel);

		bordered = createBorderReflect(gradientX);
		gradientX = filter(bordered, kernel);
		bordered = createBorderReflect(gradientY);
		gradientY = filter(bordered, kernel);


		double[][] trace = new double[gradientY.length][gradientY[0].length];
		for(int i = 0; i < gradientY.length; i++){
			for (int j = 0; j < gradientY[0].length; j++){
				trace[i][j] = gradientX2[i][j] + gradientY2[i][j];
			}
		}

		for(int i = 0; i < gradientY.length; i++){
			for (int j = 0; j < gradientY[0].length; j++){
				rmap[i][j] = ((gradientX2[i][j] * gradientY2[i][j]) - (gradientXY[i][j] * gradientXY[i][j])) - (alpha * (trace[i][j] * trace[i][j])); //determinant - scaled trace^2

			}
		}
		rmap = normalizeImage(rmap, 0, 255);
		return rmap;

	}
	public static double[][] makeBlackAndWhite(double[][] grayed, int blackWhite){
		double [][] arr = new double[grayed.length][grayed[0].length];
		for(int i = 0; i < grayed.length; i++){
			for (int j = 0; j < grayed[0].length; j++){
				if(grayed[i][j] < blackWhite){
					arr[i][j] = 0;
				}
				else{
					arr[i][j] = 255;
				}
			}
		}
		return arr;
	}


	public static void separateImage(double[][] imageArr, int r, int c) {
		ArrayList<int[]> points = new ArrayList<int[]>(); // Arraylist for all pixels that contain info

		recurseChar(points, imageArr, r, c); 
		if( points.size() >= 15){ //all this breaks up the image into "characters"
			int rMin = points.get(0)[0];
			int cMin = points.get(0)[1];
			int rMax = points.get(0)[0];
			int cMax = points.get(0)[1];

			for(int i = 1; i < points.size(); i++) {
				if(points.get(i)[0] < rMin){
					rMin = points.get(i)[0];
				}
				if(points.get(i)[0] > rMax){
					rMax = points.get(i)[0];
				}
				if(points.get(i)[1] < cMin){
					cMin = points.get(i)[1];
				}
				if(points.get(i)[1] > cMax){
					cMax = points.get(i)[1];
				}
			}

			double [][] arrEval = new double[rMax - rMin + 5][cMax - cMin + 5]; //it's not detecting the corners because of the edges of the array
			for(int i = 0; i < points.size(); i++){
				arrEval[points.get(i)[0] - rMin + 2][points.get(i)[1] - cMin + 2] = 255; //set black points to white then invert later
			}
			arrEval = invert(arrEval);
			
			displayImg(arrEval);
			detectedCharacters.add(new DetectedChar(arrEval, new int [] {r, c} ));
		}
	}

	public static void recurseChar(ArrayList<int[]> points, double[][] arr, int r, int c) {
		points.add(new int[]{r, c});
		arr[r][c] = 255;

		if(r < arr.length - 1 && arr[r+1][c] == 0) {
			recurseChar(points, arr, r+1, c);
		}
		if(r!= 0 && arr[r-1][c] == 0) {
			recurseChar(points, arr, r-1, c);
		}
		if(c != arr[0].length - 1 && arr[r][c + 1] == 0) {
			recurseChar(points, arr, r, c + 1);
		}
		if(c != 0 && arr[r][c - 1] == 0) {
			recurseChar(points, arr, r, c - 1);
		}
		if(r != arr.length - 1 && c!= arr[0].length - 1 && arr[r+1][c+1] == 0) {
			recurseChar(points, arr, r + 1, c + 1);
		}
		if(r!= 0 && c!= 0 && arr[r-1][c-1] == 0) {
			recurseChar(points, arr, r - 1, c - 1);
		}
		if(r != arr.length - 1 && c!= 0 && arr[r+1][c-1] == 0) {
			recurseChar(points, arr, r + 1, c - 1);
		}
		if(r != 0 && c!= arr[0].length - 1 && arr[r-1][c+1] == 0) {
			recurseChar(points, arr, r - 1, c + 1);
		}

		//				for (int i = -1; i <= 1; i++) {
		//					for (int j = -1; j <= 1; j++) {
		//						if (i != 0 || j != 0) {
		//							if (arr[r+i][c+j] == 0) {
		//								recurseChar(points, arr, r+i, c+i);
		//							}
		//						}
		//					}
		//				}


	}

	/**
	 * Method that takes a pixel object 2D array and converts it into a 3D
	 * integer array
	 * 
	 * @param pixelArray
	 *            - the array of pixel objects 2D
	 * @return int [][][] 3D matrix - the outcoming 3D int matrix
	 */
	public static double[][][] toMatrix(Pixel[][] pixelArray) {
		double[][][] matrix = new double[pixelArray.length][pixelArray[0].length][3]; // [x][y][z]
		// or
		// [row][column][depth]
		for (int z = 0; z <= 2; z++) { // depth
			for (int y = 0; y < pixelArray[0].length; y++) { // column
				for (int x = 0; x < pixelArray.length; x++) { // row
					if (z == 0) {
						matrix[x][y][z] = pixelArray[x][y].getRed();
					} // end if
					else if (z == 1) {
						matrix[x][y][z] = pixelArray[x][y].getGreen();
					} // end if
					else {
						matrix[x][y][z] = pixelArray[x][y].getBlue();
					} // end else
				} // end third loop
			} // end second loop
		} // end first loop
		return matrix;
	} // end method

	/**
	 * an overloaded method that takes in a 3D integer matrix and returns a
	 * picture object
	 * 
	 * @param matrix
	 * @return a picture made from the matrix
	 */
	public static Picture fromMatrix(int[][][] matrix) {
		Picture pic = new Picture(matrix.length, matrix[0].length);
		Pixel[][] pixelArray = pic.getPixels2D();
		for (int z = 0; z <= 2; z++) { // depth
			for (int y = 0; y < pixelArray[0].length; y++) { // column
				for (int x = 0; x < pixelArray.length; x++) { // row
					if (z == 0) {
						pixelArray[x][y].setRed(matrix[x][y][z]);
					} else if (z == 1) {
						pixelArray[x][y].setGreen(matrix[x][y][z]);
					} else {
						pixelArray[x][y].setBlue(matrix[x][y][z]);
					} // end else
				} // end first loop
			} // end second loop
		} // end third loop
		return pic;
	} // end method

	/**
	 * a method that takes a 2D integer array and returns a Picture object
	 * 
	 * @param 2D
	 *            matrix (method overloaded)
	 * @return a picture
	 */
	public static Picture fromMatrix(int[][] matrix) {
		Picture pic = new Picture(matrix.length, matrix[0].length);
		Pixel[][] pixelArray = pic.getPixels2D();
		for (int z = 0; z <= 2; z++) { // depth
			for (int y = 0; y < pixelArray[0].length; y++) { // column
				for (int x = 0; x < pixelArray.length; x++) { // row
					if (z == 0) {
						pixelArray[x][y].setRed(matrix[x][y]);
					} else if (z == 1) {
						pixelArray[x][y].setGreen(matrix[x][y]);
					} else {
						pixelArray[x][y].setBlue(matrix[x][y]);
					} // end else
				} // end first loop
			} // end second loop
		} // end third loop
		return pic;
	} // end method

	/**
	 * an overloaded method that takes a 2D double array and returns a Picture
	 * object
	 * 
	 * @param matrix
	 *            - double 2D matrix from which a picture will be put back out
	 * @return pic - a Picture object made from the double matrix
	 */
	public static Picture fromMatrix(double[][] matrix) {
		Picture pic = new Picture(matrix.length, matrix[0].length);
		Pixel[][] pixelArray = pic.getPixels2D();
		for (int z = 0; z <= 2; z++) { // depth
			for (int y = 0; y < pixelArray[0].length; y++) { // column
				for (int x = 0; x < pixelArray.length; x++) { // row
					if (z == 0) {
						pixelArray[x][y].setRed((int) matrix[x][y]);
					} else if (z == 1) {
						pixelArray[x][y].setGreen((int) matrix[x][y]);
					} else {
						pixelArray[x][y].setBlue((int) matrix[x][y]);
					} // end else
				} // end first loop
			} // end second loop
		} // end third loop
		return pic;
	} // end method

	/**
	 * a method that takes in a color 3D array and makes it a 2D grayscale array
	 * 
	 * @param colorMatrix
	 * @return 2D int[][] in grayscale
	 */
	public static double[][] colorToGray(double[][][] colorMatrix) {
		double[][] gray = new double[colorMatrix.length][colorMatrix[0].length];
		for (int x = 0; x < colorMatrix.length; x++) {
			for (int y = 0; y < colorMatrix[0].length; y++) {
				gray[x][y] = (int) (.21 * colorMatrix[x][y][0] + .72 * colorMatrix[x][y][1]
						+ .07 * colorMatrix[x][y][2]);
			}
		}

		return gray;
	}

	/**
	 * a method that creates a 2 layer border around the picture including the
	 * corners through mirroring techniques
	 * 
	 * @param smallMatrix
	 * @return a 2 element bordered int[][]
	 */
	public static double[][] createBorderReflect(double[][] smallMatrix) {
		double[][] bordered = new double[smallMatrix.length + 4][smallMatrix[0].length + 4];

		for (int j = 0; j < smallMatrix.length; j++) {
			for (int i = 0; i < smallMatrix[0].length; i++) {
				bordered[j + 2][i + 2] = smallMatrix[j][i];
			}
		}

		for (int j = 2; j < smallMatrix.length + 2; j++) {
			bordered[j][0] = smallMatrix[j - 2][2];
		} // left outer
		for (int j = 2; j < smallMatrix.length + 2; j++) {
			bordered[j][1] = smallMatrix[j - 2][1];
		} // left inner

		for (int j = 2; j < smallMatrix.length + 2; j++) {
			bordered[j][bordered[0].length - 1] = smallMatrix[j - 2][bordered[0].length - 7];
		} // right outer

		for (int j = 2; j < smallMatrix.length + 2; j++) {
			bordered[j][bordered[0].length - 2] = smallMatrix[j - 2][bordered[0].length - 6];
		} // right outer

		for (int i = 0; i < bordered[0].length; i++) {
			bordered[0][i] = bordered[4][i];
		} // top outer
		for (int i = 0; i < bordered[0].length; i++) {
			bordered[1][i] = bordered[3][i];
		} // top inner
		for (int i = 0; i < bordered[0].length; i++) {
			bordered[bordered.length - 1][i] = bordered[bordered.length - 5][i];
		} // bottom outer
		for (int i = 0; i < bordered[0].length; i++) {
			bordered[bordered.length - 2][i] = bordered[bordered.length - 4][i];
		} // bottom outer

		return bordered;
	}

	/**
	 * a method that creates a one layer border around the image
	 * 
	 * @param image
	 *            - the image from which a border will be created
	 * @return bordered - the image that is bordered
	 */
	public static int[][] createBorderReflectOne(int[][] image) {

		int[][] bordered = new int[image.length + 2][image[0].length + 2];

		for (int j = 0; j < image.length; j++) {
			for (int i = 0; i < image[0].length; i++) {
				bordered[j + 1][i + 1] = image[j][i];
			}
		}

		for (int j = 1; j < image.length + 1; j++) {
			bordered[j][0] = image[j - 1][1];
		} // left outer

		for (int j = 2; j < image.length + 1; j++) {
			bordered[j][bordered[0].length - 1] = image[j - 1][image[0].length - 2];
		} // right outer

		for (int i = 0; i < bordered[0].length; i++) {
			bordered[0][i] = bordered[2][i];
		} // top outer
		for (int i = 0; i < bordered[0].length; i++) {
			bordered[bordered.length - 1][i] = bordered[bordered.length - 3][i];
		} // bottom outer

		return bordered;
	}

	/**
	 * a method that filters a picture with a Gaussian filter
	 * 
	 * @param image
	 *            - incoming image, 2D
	 * @param kernel
	 *            - double 2D 5x5 array with fractional values
	 * @return - image with Gaussian filter
	 */

	public static double[][] filter(double[][] image, double[][] kernel) {
		double[][] filtered = new double[image.length - 4][image[0].length - 4];
		double newValue = 0;
		for (int i = 2; i <= filtered.length + 1; i++) {
			for (int j = 2; j <= filtered[0].length + 1; j++) {
				for (int y = -2 /* ((int)((kernel.length)/2))*-1 */; y <= 2; y++) { // -2
					// to
					// 2;
					for (int x = -2/* (int)((kernel.length)/2))*-1 */; x <= 2; x++) {
						newValue += (image[i + y][j + x]) * (kernel[y + 2][x + 2]);
						filtered[i - 2][j - 2] = (int) (newValue);
					}
				}
				newValue = 0;
			}
		}

		return filtered;
	}

	/**
	 * a method that calculates the horizontal gradient
	 * 
	 * @param image
	 *            - the image from which the gradient will be calculated
	 * @return gradX - a 2D array of integers with the X gradients in
	 */
	public static double[][] calcGradientX(double[][] image) {
		double[][] gradX = new double[image.length - 1][image[0].length - 1];
		for (int j = 0; j < gradX.length; j++) {
			for (int i = 0; i < gradX[0].length - 1; i++) {
				gradX[j][i] = Math.abs(image[j][i] - image[j][i + 1]);
			}
		}
		return gradX;
	}

	/**
	 * a method that calculates the vertical gradient
	 * 
	 * @param image
	 *            - the image from which the gradient will be calculated
	 * @return gradY - a 2D array of integers with the Y gradients in
	 */
	public static double[][] calcGradientY(double[][] image) {
		double[][] gradY = new double[image.length - 1][image[0].length - 1];
		for (int j = 0; j < gradY.length; j++) {
			for (int i = 0; i < gradY[0].length; i++) {
				gradY[j][i] = Math.abs(image[j][i] - image[j + 1][i]);
			}
		}
		return gradY;
	}

	/**
	 * a method that takes in the gradient arrays and returns the vector
	 * addition of the gradients
	 * 
	 * @param gradX
	 *            - array of X integer gradients
	 * @param gradY
	 *            - array of Y integer gradients
	 * @return gradMag - an array of the vector additions of the gradient
	 *         magnitudes
	 */
	public static double[][] calcGradientMag(double[][] gradX, double[][] gradY) {
		double[][] gradMag = new double[gradX.length + 1][gradX[0].length + 1];
		for (int i = 0; i < gradMag.length - 1; i++) {
			for (int j = 0; j < gradMag[0].length - 1; j++) {
				gradMag[i][j] = Math.sqrt(Math.pow(gradX[i][j], 2) + Math.pow(gradY[i][j], 2));
			}
		}
		return gradMag;

	}

	/**
	 * a method that calculates the gradient angles in the array
	 * 
	 * @param gradX
	 *            - an array filled with magnitudes in the X direction
	 * @param gradY
	 *            - an array filled with magnitudes in the Y direction
	 * @return gradAngle - the angle formed between the vector sum of gradX and
	 *         gradY as elements in the array
	 */
	public static double[][] calcGradientAngle(int[][] gradX, int[][] gradY) {
		double[][] gradAngle = new double[gradX.length][gradX[0].length];
		for (int i = 0; i < gradAngle.length; i++) {
			for (int j = 0; j < gradAngle[0].length; j++) {
				gradAngle[i][j] = Math.abs(Math.atan2(gradY[i][j], gradX[i][j]));
			}
		}
		return gradAngle;

	}

	/**
	 * a method that adjusts the gradient angle array into a one of four
	 * different angles in the primary and secondary directions
	 * 
	 * @param gradAngle
	 *            - the array filled with double values of angles in the top
	 *            half of the unit circle
	 * @return gradAngleAdj - an array filled with adjusted values into one of
	 *         four different angles
	 */
	public static double[][] adjustGradAngle(double[][] gradAngle) {
		double[][] gradAngleAdj = new double[gradAngle.length][gradAngle[0].length];
		for (int i = 0; i < gradAngle.length; i++) {
			for (int j = 0; j < gradAngle[0].length; j++) {

				if (gradAngle[i][j] < (Math.PI / 8) || gradAngle[i][j] > (3 * (Math.PI) / 4 + Math.PI / 8)) {
					gradAngleAdj[i][j] = 0;
				} else if (gradAngle[i][j] >= (Math.PI / 8) && gradAngle[i][j] < Math.PI / 4 + Math.PI / 8) {
					gradAngleAdj[i][j] = Math.PI / 4;
				} else if (gradAngle[i][j] >= Math.PI / 4 + Math.PI / 8
						&& gradAngle[i][j] < 3 * Math.PI / 4 - Math.PI / 8) {
					gradAngleAdj[i][j] = Math.PI / 2;
				} else {
					gradAngleAdj[i][j] = 3 * Math.PI / 4;
				}
			}
		}
		return gradAngleAdj;
	}

	/**
	 * method that returns non maximum values as zero in a new array
	 * 
	 * @param gradMag
	 *            - the 2D int array of magnitudes
	 * @param gradAngleAdj
	 *            - the angle array with one of four angles
	 * @return nonMaxSupp - the resulting values in a new array in which each
	 *         non maximum is suppressed
	 */
	public static double[][] nonMaxSupp(double[][] gradMag, double[][] gradAngleAdj) {
		double[][] nonMaxSupp = new double[gradMag.length][gradMag[0].length];
		double tolerance = .005;
		double angle = 0.00;
		double angleOne = Math.PI / 4;
		double angleTwo = Math.PI / 2;
		double angleThree = 3 * Math.PI / 4;
		double magOne = 0;
		double magTwo = 0;
		double magThree = 0;
		for (int i = 1; i < gradAngleAdj.length - 1; i++) {
			for (int j = 1; j < gradAngleAdj[0].length - 1; j++) {
				angle = gradAngleAdj[i][j];
				if (Math.abs(angle - angleOne) < tolerance) { // diagonal from
					// bottom left
					// to upper
					// right
					magOne = gradMag[i + 1][j - 1];
					magTwo = gradMag[i][j];
					magThree = gradMag[i - 1][j + 1];
					if (magTwo >= magOne && magTwo >= magThree) {
						nonMaxSupp[i][j] = magTwo;
					} else {
						nonMaxSupp[i][j] = 0;
					}
				} else if (Math.abs(angle - angleTwo) < tolerance) { // vertical
					magOne = gradMag[i - 1][j];
					magTwo = gradMag[i][j];
					magThree = gradMag[i + 1][j];
					if (magTwo >= magOne && magTwo >= magThree) {
						nonMaxSupp[i][j] = magTwo;
					} else {
						nonMaxSupp[i][j] = 0;
					}
				} else if (Math.abs(angle - angleThree) < tolerance) { // diagonal
					// to
					// bottom
					// right
					magOne = gradMag[i - 1][j - 1];
					magTwo = gradMag[i][j];
					magThree = gradMag[i + 1][j + 1];
					if (magTwo >= magOne && magTwo >= magThree) {
						nonMaxSupp[i][j] = magTwo;
					} else {
						nonMaxSupp[i][j] = 0;
					}
				} else { // horizontal
					magOne = gradMag[i][j - 1];
					magTwo = gradMag[i][j];
					magThree = gradMag[i][j + 1];
					if (magTwo >= magOne && magTwo >= magThree) {
						nonMaxSupp[i][j] = magTwo;
					} else {
						nonMaxSupp[i][j] = 0;
					}
				}
			}
		}
		return nonMaxSupp;
	}

	/**
	 * a method that produces an image array of one of three integer values: 0
	 * 127 or 255.
	 * 
	 * @param nonMaxSupp
	 * @param threshOne
	 * @param threshTwo
	 */
	public static int[][] doubleThresh(double[][] nonMaxSupp, int threshOne, int threshTwo) {
		int[][] dThresh = new int[nonMaxSupp.length][nonMaxSupp[0].length];
		for (int i = 0; i < nonMaxSupp.length; i++) {
			for (int j = 0; j < nonMaxSupp[0].length; j++) {
				if (nonMaxSupp[i][j] <= threshOne) {
					dThresh[i][j] = 0;
				} else if (nonMaxSupp[i][j] > threshOne && nonMaxSupp[i][j] <= threshTwo) {
					dThresh[i][j] = 127;
				} else {
					dThresh[i][j] = 255;
				}
			}
		}
		return dThresh;
	}

	/**
	 * a method that performs edge tracking by hysteresis
	 * 
	 * @param dThresh
	 *            - double thresholded image
	 */
	public static int[][] edge(int[][] dThresh) {
		int[][] edge = new int[dThresh.length][dThresh[0].length];
		for (int i = 0; i < dThresh.length; i++) {
			for (int j = 0; j < dThresh[0].length; j++) {
				if (dThresh[i][j] == 255) {
					edge[i][j] = 255;
				} else {
					edge[i][j] = 0;
				}
			}
		}
		for (int i = 1; i < dThresh.length; i++) {
			for (int j = 1; j < dThresh[0].length; j++) {
				if (dThresh[i][j] == 127) {
					if (dThresh[i - 1][j - 1] == 255 || dThresh[i - 1][j] == 255 || dThresh[i - 1][j + 1] == 255
							|| dThresh[i][j - 1] == 255 || dThresh[i][j + 1] == 255 || dThresh[i + 1][j - 1] == 255
							|| dThresh[i + 1][j] == 255 || dThresh[i + 1][j + 1] == 255) {
						edge[i][j] = 255;
					} // end if
				} // end if
			} // end inner loop
		} // end outer loop
		return edge;
	} // end method

	/**
	 * a method to display the image after edge detection
	 * 
	 * @param arr
	 *            - the image after edge detection
	 */
	public static void displayImg(double[][] arr) {
		Picture image = new Picture(arr.length, arr[0].length);
		image = fromMatrix(arr);
		image.explore();
	}

	/**
	 * a method to invert the values (black to white, white to black)
	 * 
	 * @param before
	 *            - the array before inversion
	 * @return after - the array after inversion
	 */
	public static double[][] invert(double[][] before) {
		double[][] after = new double[before.length][before[0].length];
		for (int i = 0; i < after.length; i++) {
			for (int j = 0; j < after[0].length; j++) {
				after[i][j] = 255 - before[i][j];
			}
		}
		return after;
	}

	/**
	 * a method to normalize an array of values to 0-255
	 * 
	 * @param arr
	 *            - an incoming 2D array of type double
	 * @return - a 2D array of type double with normalized values
	 */
	public static double[][] normalizeImage(double[][] arr, double min, double max) {

		double[][] out = new double[arr.length][arr[0].length];
		double imgMin = 0.0;
		double imgMax = 0.0;
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[0].length; j++) {
				if (arr[i][j] > imgMax) {
					imgMax = arr[i][j];
				} else if (arr[i][j] < imgMin) {
					imgMin = arr[i][j];
				}
			}
		}
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[0].length; j++) {
				out[i][j] = min + (((arr[i][j] - imgMin)*(max-min))/(imgMax-imgMin));
			}
		}
		return out;
	}

	public static double[][] renormalizeImage(double[][] arr) {

		double[][] out = new double[arr.length][arr[0].length];
		double imgMin = 0.0;
		double imgMax = 0.0;
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[0].length; j++) {
				if (arr[i][j] > imgMax) {
					imgMax = arr[i][j];
				} else if (arr[i][j] < imgMin) {
					imgMin = arr[i][j];
				}
			}
		}
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[0].length; j++) {
				out[i][j] = (arr[i][j] - imgMin) * (255.0 / imgMax);
			}
		}
		return out;
	}

	/**
	 * a method to normalize an array of values to 0-255
	 * 
	 * @param arr
	 *            - an incoming 2D array of type int
	 * @return - a 2D array of type int with normalized values
	 */
	public static int[][] normalizeImage(int[][] arr) {

		int[][] out = new int[arr.length][arr[0].length];
		int imgMin = 0;
		int imgMax = 0;
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[0].length; j++) {
				if (arr[i][j] > imgMax) {
					imgMax = arr[i][j];
				} else if (arr[i][j] < imgMin) {
					imgMin = arr[i][j];
				}
			}
		}
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[0].length; j++) {
				out[i][j] = (int) ((arr[i][j] - imgMin) * (255.0 / imgMax));
			}
		}
		return out;
	}

	public static double[][] createKernel(){
		double [][] kernel = new double [5][5];
		for(int i = 0; i < kernel.length; i++){
			for (int j = 0; j < kernel[0].length; j++){
				kernel[i][j] = 1.0/25.0;
			}
		}
		return kernel;
	}

	public static void search(double[][] arr){
		boolean go = true;
		for(int i = 0; i < arr.length; i++){ //loops through the array (main function)
			for(int j = 0; j < arr[0].length && go; j++){
				if(arr[i][j] == 0){
					separateImage(arr, i, j);
				}
			}
		}
		//displayImg(arr);
	}

}
