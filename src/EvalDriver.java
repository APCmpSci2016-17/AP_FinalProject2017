
public class EvalDriver {

	public static void main(String[] args) {
		boolean exit = false;
		do {
			Evaluator eval = new Evaluator();
			System.out.println(eval.stringEval(ReadInput.readIn("Input a calculation: ")) + "\n");
			switch(ReadInput.readIn("Would you like to input another calculation? (Y/N): ").toUpperCase().charAt(0)) {
			case 'Y': break;
			case 'N':
			default: exit = true;
			break;
			}
		} while (!exit);
	}

}
