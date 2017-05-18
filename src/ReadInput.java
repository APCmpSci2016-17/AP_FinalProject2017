import java.util.Scanner;
import java.util.GregorianCalendar;

/**
 * This class is designed to read input of different types from the user and 
 * perform error checking on the value for correct data type expected and for
 * valid range of the expected value. It was designed to simplify the error
 * checking and validation process for other classes and methods. The methods
 * included are static so they can be called directly.
 * 
 * This was created for simplification and consolidation of error checking 
 * for code readability and for practicing associations.
 * 
 * @author Tricia Flynn
 */
public class ReadInput
{
	/** Scanner to gather user input from the keyboard */
	private static Scanner reader = new Scanner(System.in);

	//-------------------------------------------------------------------------------- 
	/** This method performs error checking on user input when 
	 *  the expected value is an integer. It re-prompts user via 
	 *  recursion until a double is entered.
	 *  
	 * @param prompt String prompt to use when re-prompting user for input
	 * 
	 * @return int valid integer value
	 */
	public static int readInt(String prompt) {
		System.out.println(prompt);
		if (reader.hasNextInt()) {
			return reader.nextInt();
		}
		else {
			reader.next();
			System.out.println("That input is invalid.");
			return readInt(prompt);
		}
	}

	//-------------------------------------------------------------------------------- 
	/** This method performs error checking on user input when 
	 *  the expected value is a double. It re-prompts user via 
	 *  recursion until a double is entered. Note that integer
	 *  input would be considered valid doubles, but the value
	 *  returned would be a double version of it.  
	 *  
	 * @param prompt String prompt to use when re-prompting user for input
	 * 
	 * @return double valid double value
	 */
	public static double readDouble(String prompt) {
		System.out.println(prompt);
		if (reader.hasNextDouble()) {
			return reader.nextDouble();
		}
		else {
			reader.next();
			System.out.println("That input is invalid.");
			return readDouble(prompt);
		}
	} 

	//-------------------------------------------------------------------------------- 
	/** This method performs error checking on user input when 
	 *  the expected value is a double greater than 0. It re-prompts user until a 
	 *  double value greater than 0 is entered.
	 *  
	 * @param prompt String prompt to use when re-prompting user for input
	 * 
	 * @return double valid value greater than 0 after error checking
	 */
	public static double positiveDouble(String prompt){

		double checkValue = -1;

		do {

			System.out.print(prompt);

			if (reader.hasNextDouble()){
				checkValue = reader.nextDouble();
			} else {
				System.out.println("\nInvalid entry. Please try again.");
				reader.next(); // move Scanner pointer forward if input not a double
			}
		} while (checkValue <= 0);

		reader.nextLine();      //consume leftover newline character

		return checkValue;
	} 

	//-------------------------------------------------------------------------------- 
	/** This method performs error checking on user input when 
	 *  the expected value is an int greater than 0. It re-prompts user until a 
	 *  int value greater than 0 is entered.
	 * 
	 * @param prompt String prompt to use when re-prompting user for input
	 * 
	 * @return int valid value greater than 0 after error checking
	 */
	public static int positiveInt(String prompt){

		int checkValue = -1;

		do {

			System.out.print(prompt);

			if (reader.hasNextInt()){
				checkValue = reader.nextInt();
			} else {
				System.out.println("\nInvalid entry. Please try again.");
				reader.next(); // move Scanner pointer forward if input not an integer
			}

		} while (checkValue <= 0);

		reader.nextLine();      //consume leftover newline character

		return checkValue;
	} 

	//--------------------------------------------------------------------------------

	/** This overloaded method performs error checking on user input when 
	 *  the expected value is not within a specified range (inclusive of 
	 *  min and inclusive of max. It re-prompts user until a 
	 *  int value within the range is entered.
	 *  
	 * @param min int the minimum of the valid range 
	 * @param max int the maximum of the valid range
	 * @param prompt String prompt to use when re-prompting user for input
	 * 
	 * @return int valid value greater than 0 after error checking
	 */
	public static int range(int min, int max, String prompt){

		int checkValue = min - 1;

		do {

			System.out.print(prompt);

			if (reader.hasNextInt()){
				checkValue = reader.nextInt();
			} else {
				System.out.println("\nInvalid entry. Please try again.");
				reader.next(); // move Scanner pointer forward if input not an integer
			} 

		} while (checkValue < min || checkValue > max);

		reader.nextLine();      //consume leftover newline character

		return checkValue;
	} 

	//--------------------------------------------------------------------------------  
	/** This overloaded method performs error checking on user input when 
	 *  the expected value is not within a specified range inclusive of min 
	 *  but exclusive of max. It re-prompts user until a 
	 *  double value within the range is entered.
	 *  
	 * @param min double the minimum of the valid range 
	 * @param max double the maximum of the valid range
	 * @param prompt String prompt to use when re-prompting user for input
	 * 
	 * @return double valid value greater than 0 after error checking
	 */
	public static double range(double min, double max, String prompt){

		double checkValue = min -1;

		do {

			System.out.print(prompt);

			if (reader.hasNextDouble()){
				checkValue = reader.nextDouble();
			} else {
				System.out.println("\nInvalid entry. Please try again.");
				reader.next(); // move Scanner pointer forward if input not a double
			}
		} while (checkValue < min || checkValue >= max);

		reader.nextLine();      //consume leftover newline character

		return checkValue;
	}  

	//--------------------------------------------------------------------------------   

	/** This method includes the error checking necessary when creating 
	 *  a GregorianCalendar object using inputs for the year,
	 *  month, and day.
	 *  
	 * @param prompt String prompt to use when prompting user for input
	 * 
	 * @return GregorianCalendar GregorianCalendar object created
	 */
	public static GregorianCalendar readGregCalMDY(String prompt) {
		int maxDays = 0;
		System.out.println(prompt);
		int year = positiveInt("Enter your year: ");
		int month = range(1, 12, "Enter your month: ");
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			maxDays = 31;
			break;
		case 4:
		case 6:
		case 9:
		case 11:
			maxDays = 30;
			break;
		case 2:
			if (((year % 4 == 0) && !(year % 100 == 0)) || (year % 400 == 0))
				maxDays = 29;
			else
				maxDays = 28;
			break;
		default:
			System.out.println("Invalid month.");
			break;
		}
		
		int day = range(1, maxDays, "Enter your day: ");
		
		GregorianCalendar temp = new GregorianCalendar(year, month - 1, day);
		return temp;
	}

	public static String readIn(String prompt) {
		System.out.println(prompt);
		String out = reader.nextLine();
		
		return out;
	}  

}
