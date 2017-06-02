public class EvalDriver {

	public static void main(String[] args) throws Exception {
		boolean exit = false;
		Rewrite r = new Rewrite("$a*($b+$c) => $a*$b+$a*$c");
		System.out.println(r);
		do {
			try {
			Parser eval = new Parser(ReadInput.readIn("Input a calculation: "));
			System.out.println(r.run(eval.getResult()));
			} catch(ArithmeticException e) {
				e.printStackTrace();
				System.out.println("\nA fatal error has occurred.");
			}
			switch(ReadInput.readIn("Would you like to input another calculation? (y/N): ").toUpperCase().charAt(0)) {
			case 'Y': break;
			case 'N':
			default: exit = true;
			break;
			}
		} while (!exit);
	}
}
