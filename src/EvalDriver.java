import java.nio.file.Files;
import java.nio.file.Paths;

public class EvalDriver {

	public static void main(String[] args) throws Exception {
		boolean exit = false;
		Rewrite[] rs = Rewrite.readArray(new String(Files.readAllBytes(Paths.get("rules.txt" /* TEMP FILE */))));
		System.out.println(rs);
		do {
			try {
			Expr e = Parser.parse(ReadInput.readIn("Input a calculation: "));
			Expr r = Rewrite.runAll(e, rs);
			System.out.println(r);
			System.out.println(Evaluator.eval(r));
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
