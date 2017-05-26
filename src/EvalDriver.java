
public class EvalDriver {

	public static void main(String[] args) {
		boolean exit = false;
		do {
			try {
			Parser eval = new Parser(ReadInput.readIn("Input a calculation: "));
			System.out.println(eval.getResult().toString());
			} catch(ArithmeticException e) {
				e.printStackTrace();
				System.out.println("\nA fatal error has occurred.");
			}
			switch(ReadInput.readIn("Would you like to input another calculation? (Y/N): ").toUpperCase().charAt(0)) {
			case 'Y': break;
			case 'N':
			default: exit = true;
			break;
			}
		} while (!exit);
	}

}
