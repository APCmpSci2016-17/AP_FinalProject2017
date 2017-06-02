/**
 * AP CmpSci 2016-2017
 * pixLab
 * Works with pixel arrays to edit images
 * Contains static methods allowing the testing of methods in the picture class
 * @author Connor Boham
 * @version August 20th, 2016
 **/

/**
 * This class contains class (static) methods that will help you test the
 * Picture class methods. Uncomment the methods and the code in the main to
 * test.
 * 
 * @author Barbara Ericson
 */
public class PictureTester {
	/** Method to test zeroBlue */
	public static void testZeroBlue() {
		Picture beach = new Picture("beach.jpg");
		beach.explore();
		beach.zeroBlue();
		beach.explore();
	}

	/** Method to test KeepOnlyBlue */
	public static void testKeepOnlyBlue() {
		Picture beach = new Picture("beach.jpg");
		beach.explore();
		beach.keepOnlyBlue();
		beach.explore();
	}

	/**
	 * Method to test FixUnderwater
	 */
	public static void testFixUnderwater() {
		Picture water = new Picture("water.jpg");
		water.explore();
		water.fixUnderwater();
		water.explore();
	}

	/**
	 * Method to test negate
	 */
	public static void testNegate() {
		Picture beach = new Picture("beach.jpg");
		beach.explore();
		beach.negate();
		beach.explore();
	}

	/**
	 * Method to test grayscale
	 */
	public static void testGrayscale() {
		Picture beach = new Picture("beach.jpg");
		beach.explore();
		beach.grayscale();
		beach.explore();
	}

	/** Method to test mirrorVertical */
	public static void testMirrorVertical() {
		Picture caterpillar = new Picture("caterpillar.jpg");
		caterpillar.explore();
		caterpillar.mirrorVertical();
		caterpillar.explore();
	}

	/**
	 * Method to test mirrorHorizontal
	 */
	public static void testMirrorHorizontal() {
		Picture motorcycle = new Picture("redMotorcycle.jpg");
		motorcycle.explore();
		motorcycle.mirrorHorizontal();
		motorcycle.explore();
	}

	/**
	 * Method to test mirrorHorizontalBotToTop
	 */
	public static void testMirrorHorizontalBotToTop() {
		Picture motorcycle = new Picture("redMotorcycle.jpg");
		motorcycle.explore();
		motorcycle.mirrorHorizontalBotToTop();
		motorcycle.explore();
	}

	/**
	 * Method to test MirrorVerticalRightToLeft
	 */
	public static void testMirrorVerticalRightToLeft() {
		Picture caterpillar = new Picture("caterpillar.jpg");
		caterpillar.explore();
		caterpillar.mirrorVerticalRightToLeft();
		caterpillar.explore();
	}

	/**
	 * Method to test mirrorDiagonal
	 */
	public static void testMirrorDiagonal() {
		Picture beach = new Picture("beach.jpg");
		beach.explore();
		beach.mirrorDiagonal();
		beach.explore();
	}

	/**
	 * Method to test mirrorTemple
	 */
	public static void testMirrorTemple() {
		Picture temple = new Picture("temple.jpg");
		temple.explore();
		temple.mirrorTemple();
		temple.explore();
	}

	/**
	 * Method to test mirrorArms
	 */
	public static void testMirrorArms() {
		Picture snowman = new Picture("snowman.jpg");
		snowman.explore();
		snowman.mirrorArms();
		snowman.explore();
	}

	/**
	 * Method to test mirrorGull
	 */
	public static void testMirrorGull() {
		Picture seagull = new Picture("seagull.jpg");
		seagull.explore();
		seagull.mirrorGull();
		seagull.explore();
	}

	/**
	 * Method to test copyTwo
	 */
	public static void testCopyTwo() {
		Picture snowman = new Picture("snowman.jpg");
		Picture flower = new Picture("flower1.jpg");
		snowman.copyTwo(flower, 0, 0, 0, 0, 100, 50);
		snowman.explore();
	}

	/** Method to test the collage method */
	public static void testCollage() {
		Picture canvas = new Picture("640x480.jpg");
		canvas.createCollage();
		canvas.explore();
	}

	/**
	 * Method to test myCollage
	 */
	public static void testMyCollage() {
		Picture canvas = new Picture("640x480.jpg");
		canvas.myCollage();
		Picture robot = new Picture("robot.jpg");
		canvas.explore();
	}

	/** Method to test edgeDetection */
	public static void testEdgeDetection() {
		Picture swan = new Picture("swan.jpg");
		swan.edgeDetectionY(10);
		swan.explore();
	}

	/**
	 * Method to test Encode
	 */
	public static void testEncode() {
		Picture beach = new Picture("beach.jpg");
		Picture msg = new Picture("msg.jpg");
		beach.encode();
		beach.explore();
	}

	/**
	 * Method to test Decode
	 */
	public static void testDecode() {
		Picture encoded = new Picture("encoded.bmp");
		encoded.explore();
		encoded.decode();
		encoded.explore();
	}

	/**
	 * Main method for testing. Every class can have a main method in Java
	 */
	public static void main(String[] args) {
		// uncomment a call here to run a test
		// and comment out the ones you don't want
		// to run
		// testZeroBlue();
		// testKeepOnlyBlue();
		// testKeepOnlyRed();
		// testKeepOnlyGreen();
		// testNegate();
		// testGrayscale();
		// testFixUnderwater();
		// testMirrorVertical();
		// testMirrorVerticalRightToLeft();
		// testMirrorHorizontal();
		// testMirrorHorizontalBotToTop();
		// testMirrorDiagonal();
		// testMirrorTemple();
		// testMirrorArms();
		// testMirrorGull();
		// testMirrorDiagonal();
		// testCollage();
		// testMyCollage();
		// testEdgeDetection();
		// testEdgeDetection2();
		// testChromakey();
		testDecode();
		// testGetCountRedOverValue(250);
		// testSetRedToHalfValueInTopHalf();
		// testClearBlueOverValue(200);
		// testGetAverageForColumn(0);
	}
}