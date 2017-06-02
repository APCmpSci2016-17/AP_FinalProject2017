/**
 * AP CmpSci 2016-2017
 * pixLab
 * Works with pixel arrays to edit images
 * Class full of methods used to edit pictures
 * @author Connor Boham
 * @version August 20th, 2016
 **/
import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.text.*;
import java.util.*;
import java.util.List; // resolves problem with java.awt.List and java.util.List

/**
 * A class that represents a picture. This class inherits from SimplePicture and
 * allows the student to add functionality to the Picture class.
 * 
 * @author Barbara Ericson ericson@cc.gatech.edu
 */
public class Picture extends SimplePicture {
	///////////////////// constructors //////////////////////////////////

	/**
	 * Constructor that takes no arguments
	 */
	public Picture() {
		/*
		 * not needed but use it to show students the implicit call to super()
		 * child constructors always call a parent constructor
		 */
		super();
	}

	/**
	 * Constructor that takes a file name and creates the picture
	 * 
	 * @param fileName
	 *            the name of the file to create the picture from
	 */
	public Picture(String fileName) {
		// let the parent class handle this fileName
		super(fileName);
	}

	/**
	 * Constructor that takes the width and height
	 * 
	 * @param height
	 *            the height of the desired picture
	 * @param width
	 *            the width of the desired picture
	 */
	public Picture(int height, int width) {
		// let the parent class handle this width and height
		super(width, height);
	}

	/**
	 * Constructor that takes a picture and creates a copy of that picture
	 * 
	 * @param copyPicture
	 *            the picture to copy
	 */
	public Picture(Picture copyPicture) {
		// let the parent class do the copy
		super(copyPicture);
	}

	/**
	 * Constructor that takes a buffered image
	 * 
	 * @param image
	 *            the buffered image to use
	 */
	public Picture(BufferedImage image) {
		super(image);
	}

	////////////////////// methods ///////////////////////////////////////

	/**
	 * Method to return a string with information about this picture.
	 * 
	 * @return a string with information about the picture such as fileName,
	 *         height and width.
	 */
	public String toString() {
		String output = "Picture, filename " + getFileName() + " height " + getHeight() + " width " + getWidth();
		return output;

	}

	/** Method to set the blue to 0 */
	public void zeroBlue() {
		Pixel[][] pixels = this.getPixels2D();
		for (Pixel[] rowArray : pixels) {
			for (Pixel pixelObj : rowArray) {
				pixelObj.setBlue(0);
			}
		}
	}

	/**
	 * Method that mirrors the picture around a vertical mirror in the center of
	 * the picture from left to right
	 */
	public void mirrorVertical() {
		Pixel[][] pixels = this.getPixels2D();
		Pixel leftPixel = null;
		Pixel rightPixel = null;
		int width = pixels[0].length;
		for (int row = 0; row < pixels.length; row++) {
			for (int col = 0; col < width / 2; col++) {
				leftPixel = pixels[row][col];
				rightPixel = pixels[row][width - 1 - col];
				rightPixel.setColor(leftPixel.getColor());
			}
		}
	}

	/**
	 * Method that mirrors the picture around a vertical mirror in the center of
	 * the picture from right to left
	 */
	public void mirrorVerticalRightToLeft() {
		Pixel[][] pixels = this.getPixels2D();
		Pixel leftPixel = null;
		Pixel rightPixel = null;
		int width = pixels.length;
		for (int row = 0; row < pixels.length; row++) {
			for (int col = 0; col < width / 2; col++) {
				leftPixel = pixels[row][col];
				rightPixel = pixels[row][width - 1 - col];
				leftPixel.setColor(rightPixel.getColor());
			}
		}
	}

	/**
	 * Method that mirrors the picture horizontally from top to bottom
	 */
	public void mirrorHorizontal() {
		Pixel[][] pixels = this.getPixels2D();
		Pixel topPixel = null;
		Pixel bottomPixel = null;
		int height = pixels.length;
		for (int row = 0; row < height / 2; row++) {
			for (int col = 0; col < pixels[0].length; col++) {
				topPixel = pixels[row][col];
				bottomPixel = pixels[height - 1 - row][col];
				bottomPixel.setColor(topPixel.getColor());
			}
		}
	}

	/**
	 * Method that mirrors the picture horizontally from bottom to top
	 */
	public void mirrorHorizontalBotToTop() {
		Pixel[][] pixels = this.getPixels2D();
		Pixel topPixel = null;
		Pixel bottomPixel = null;
		int height = pixels.length;
		for (int row = 0; row < height / 2; row++) {
			for (int col = 0; col < pixels[0].length; col++) {
				topPixel = pixels[row][col];
				bottomPixel = pixels[height - 1 - row][col];
				topPixel.setColor(bottomPixel.getColor());
			}
		}
	}

	/**
	 * Method that mirrors the picture diagonally from bottom left to top right
	 */
	public void mirrorDiagonal() {
		Pixel[][] pixels = this.getPixels2D();
		Pixel topPixel = null;
		Pixel bottomPixel = null;
		int height = pixels.length;
		int width = pixels[0].length;
		int squareLength = 0;
		if (height <= width) {
			squareLength = height;
		} else {
			squareLength = width;
		}
		for (int row = 0; row < squareLength; row++) {
			for (int col = 0; col < squareLength; col++) {
				topPixel = pixels[row][col];
				bottomPixel = pixels[col][row];
				topPixel.setColor(bottomPixel.getColor());
			}
		}
	}

	/**
	 * Method that mirrors a section of the snowman picture such that the
	 * resulting picture has 4 arms instead of 2
	 */
	public void mirrorArms() {

		Pixel[][] pixels = this.getPixels2D();
		Pixel topPixel = null;
		Pixel bottomPixel = null;
		int mirrorPoint = 194;
		for (int row = 166; row < mirrorPoint; row++) {
			for (int col = 104; col < 294; col++) {
				topPixel = pixels[row][col];
				bottomPixel = pixels[mirrorPoint - row + mirrorPoint][col];
				bottomPixel.setColor(topPixel.getColor());
			}
		}

	}

	/**
	 * Method that mirrors a section of a seagull picture from left to right
	 * such that the seagull has an identical buddy
	 */
	public void mirrorGull() {
		int mirrorPoint = 354;
		Pixel leftPixel = null;
		Pixel rightPixel = null;
		Pixel[][] pixels = this.getPixels2D();

		// loop through the rows
		for (int row = 233; row < 338; row++) {
			// loop from 13 to just before the mirror point
			for (int col = 230; col < mirrorPoint; col++) {

				leftPixel = pixels[row][col];
				rightPixel = pixels[row][mirrorPoint - col + mirrorPoint];
				rightPixel.setColor(leftPixel.getColor());
			}
		}
	}

	/** Mirror just part of a picture of a temple */
	public void mirrorTemple() {
		int mirrorPoint = 276;
		Pixel leftPixel = null;
		Pixel rightPixel = null;
		int count = 0;
		Pixel[][] pixels = this.getPixels2D();

		// loop through the rows
		for (int row = 27; row < 97; row++) {
			// loop from 13 to just before the mirror point
			for (int col = 13; col < mirrorPoint; col++) {

				leftPixel = pixels[row][col];
				rightPixel = pixels[row][mirrorPoint - col + mirrorPoint];
				rightPixel.setColor(leftPixel.getColor());
				count += 1;
			}
		}
		System.out.println("The number of times the loop executed is: " + count + ".");
	}

	/**
	 * copy from the passed fromPic to the specified startRow and startCol in
	 * the current picture
	 * 
	 * @param fromPic
	 *            the picture to copy from
	 * @param startRow
	 *            the start row to copy to
	 * @param startCol
	 *            the start col to copy to
	 */
	public void copy(Picture fromPic, int startRow, int startCol) {
		Pixel fromPixel = null;
		Pixel toPixel = null;
		Pixel[][] toPixels = this.getPixels2D();
		Pixel[][] fromPixels = fromPic.getPixels2D();
		for (int fromRow = 0, toRow = startRow; fromRow < fromPixels.length
				&& toRow < toPixels.length; fromRow++, toRow++) {
			for (int fromCol = 0, toCol = startCol; fromCol < fromPixels[0].length
					&& toCol < toPixels[0].length; fromCol++, toCol++) {
				fromPixel = fromPixels[fromRow][fromCol];
				toPixel = toPixels[toRow][toCol];
				toPixel.setColor(fromPixel.getColor());
			}
		}
	}

	/**
	 * copy a section of the selected picture to another picture's sections
	 * 
	 * @param fromPic
	 *            the picture to copy from
	 * @param startToRow
	 *            the start row on the to picture
	 * @param startToCol
	 *            the start column on the to picture
	 * @param startFromRow
	 *            the start row on the from picture
	 * @param startFromCol
	 *            the start column on the from picture
	 * @param endFromRow
	 *            the end row on the from picture
	 * @param endFromCol
	 *            the end column on the from picture
	 */
	public void copyTwo(Picture fromPic, int startToRow, int startToCol, int startFromRow, int startFromCol,
			int endFromRow, int endFromCol) {
		Pixel fromPixel = null;
		Pixel toPixel = null;
		Pixel[][] toPixels = this.getPixels2D();
		Pixel[][] fromPixels = fromPic.getPixels2D();
		for (int fromRow = startFromRow, toRow = startToRow; fromRow < endFromRow
				&& toRow < toPixels.length; fromRow++, toRow++) {
			for (int fromCol = startFromCol, toCol = startToCol; fromCol < endFromCol
					&& toCol < toPixels[0].length; fromCol++, toCol++) {
				fromPixel = fromPixels[fromRow][fromCol];
				toPixel = toPixels[toRow][toCol];
				toPixel.setColor(fromPixel.getColor());
			}
		}
	}

	/** Method to create a collage of several pictures */
	public void createCollage() {
		Picture flower1 = new Picture("flower1.jpg");
		Picture flower2 = new Picture("flower2.jpg");
		this.copy(flower1, 0, 0);
		this.copy(flower2, 100, 0);
		this.copy(flower1, 200, 0);
		Picture flowerNoBlue = new Picture(flower2);
		flowerNoBlue.zeroBlue();
		this.copy(flowerNoBlue, 300, 0);
		this.copy(flower1, 400, 0);
		this.copy(flower2, 500, 0);
		this.mirrorVertical();
		this.write("collage.jpg");
	}

	/**
	 * Method to create a second collage. not Connor's best work in terms or
	 * artistic ability
	 */
	public void myCollage() {
		Picture robot = new Picture("robot.jpg");
		Picture kitten = new Picture("kitten2.jpg");
		this.copy(robot, 0, 0);
		this.copyTwo(kitten, 90, 0, 50, 90, 300, 400);
		kitten.mirrorDiagonal();
		kitten.grayscale();
		this.copyTwo(kitten, 90, 300, 50, 90, 300, 400);
		this.zeroBlue();

	}

	/**
	 * Method to show large changes in color
	 * 
	 * @param edgeDist
	 *            the distance for finding edges
	 */
	public void edgeDetection(int edgeDist) {
		Pixel leftPixel = null;
		Pixel rightPixel = null;
		Pixel[][] pixels = this.getPixels2D();
		Color rightColor = null;
		for (int row = 0; row < pixels.length; row++) {
			for (int col = 0; col < pixels[0].length - 1; col++) {
				leftPixel = pixels[row][col];
				rightPixel = pixels[row][col + 1];
				rightColor = rightPixel.getColor();
				if (leftPixel.colorDistance(rightColor) > edgeDist) {
					leftPixel.setColor(Color.BLACK);
				} else {
					leftPixel.setColor(Color.WHITE);
				}
			}
		}

	}

	/**
	 * Method to show large changes in color from top to bottom pixels
	 * 
	 * @param edgeDist
	 *            the distance for finding edges
	 */
	public void edgeDetectionY(int edgeDist) {
		Pixel topPixel = null;
		Pixel bottomPixel = null;
		Color bottomColor = null;
		Pixel[][] pixels = this.getPixels2D();

		for (int row = 0; row < pixels.length - 1; row++) {
			for (int col = 0; col < pixels[0].length; col++) {
				topPixel = pixels[row][col];
				bottomPixel = pixels[row + 1][col];
				bottomColor = bottomPixel.getColor();
				if (topPixel.colorDistance(bottomColor) > edgeDist) {
					topPixel.setColor(Color.BLACK);
				} else {
					topPixel.setColor(Color.WHITE);
				}
			}
		}
	}

	/*
	 * Main method for testing - each class in Java can have a main method
	 */
	public static void main(String[] args) {
		Picture beach = new Picture("beach.jpg");
		beach.explore();
		beach.zeroBlue();
		beach.explore();
	}

	/**
	 * Method to set the green and red color channels to 0
	 */
	public void keepOnlyBlue() {
		Pixel[][] pixels = this.getPixels2D();
		for (Pixel[] rowArray : pixels) {
			for (Pixel pixelObj : rowArray) {
				pixelObj.setRed(0);
				pixelObj.setGreen(0);
			}
		}
	}

	/**
	 * Method to negate the color channels of each pixel in an image
	 */
	public void negate() {
		Pixel[][] pixels = this.getPixels2D();
		for (Pixel[] rowArray : pixels) {
			for (Pixel pixelObj : rowArray) {
				pixelObj.setRed(255 - pixelObj.getRed());
				pixelObj.setBlue(255 - pixelObj.getBlue());
				pixelObj.setGreen(255 - pixelObj.getGreen());
			}
		}
	}

	/**
	 * Method to turn the picture into a grayscale image
	 */
	public void grayscale() {
		Pixel[][] pixels = this.getPixels2D();
		for (Pixel[] rowArray : pixels) {
			for (Pixel pixelObj : rowArray) {
				pixelObj.setRed((int) (.21 * pixelObj.getRed() + .72 * pixelObj.getGreen() + .07 * pixelObj.getBlue()));
				pixelObj.setBlue(
						(int) (.21 * pixelObj.getRed() + .72 * pixelObj.getGreen() + .07 * pixelObj.getBlue()));
				pixelObj.setGreen(
						(int) (.21 * pixelObj.getRed() + .72 * pixelObj.getGreen() + .07 * pixelObj.getBlue()));
			}
		}
	}

	/**
	 * Method to make an image of fish clearer to see the fish, also not
	 * Connor's best work
	 */
	public void fixUnderwater() {
		Pixel[][] pixels = this.getPixels2D();
		for (Pixel[] rowArray : pixels) {
			for (Pixel pixelObj : rowArray) {

				pixelObj.setRed(pixelObj.getRed() + 190);
				pixelObj.setGreen(pixelObj.getGreen() + 50);
				pixelObj.setBlue(pixelObj.getBlue() + 70);
				// tried my best, couldn't find anything that worked well
				;
			}
		}
	}

	/**
	 * Encodes a secret message into the beach image and writes the resulting
	 * image as a bitmap file type
	 */
	public void encode() {
		Pixel[][] image = this.getPixels2D();
		for (Pixel[] rowArray : image) {
			for (Pixel pixelObj : rowArray) {
				if (pixelObj.getRed() % 2 != 0) {
					pixelObj.setRed(pixelObj.getRed() - 1);
				}
			}
		}

		Picture msg = new Picture("msg.jpg");
		Pixel[][] message = msg.getPixels2D();
		for (int row = 0; row < message.length; row++) {
			for (int col = 0; col < message[0].length; col++) {
				if (message[row][col].colorDistance(Color.BLACK) < 10) {
					image[row][col].setRed(image[row][col].getRed() + 1);
				}
			}
		}
		this.write("encoded.bmp");
	}

	/**
	 * Decodes the message from the encoded beach image
	 */
	public void decode() {
		Pixel[][] image = this.getPixels2D();
		for (Pixel[] rowArray : image) {
			for (Pixel pixelObj : rowArray) {
				if (pixelObj.getRed() % 2 != 0) {
					pixelObj.setColor(Color.BLACK);
				} else {
					pixelObj.setColor(Color.WHITE);
				}
			}
		}
	}

} // this } is the end of class Picture, put all new methods before this
