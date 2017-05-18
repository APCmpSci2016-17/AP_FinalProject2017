import java.util.HashMap;
import java.util.Stack;

public class Evaluator {
	
	private static HashMap<Integer, Integer> prec = new HashMap<Integer, Integer>() {
		private static final long serialVersionUID = 1283576132598L; //HashMap is serializable, requires this variable (or else it gives a really annoying warning)
	{ 
		//HashMap of all operations with corresponding precedence (higher takes place before lower)
		//Note: Close Parentheses [')'] are not included here as it is handled uniquely
		put(0,0);  // ( Open Parentheses
		put(1,2);  // + Plus
		put(2,2);  // - Minus
		put(3,3);  // * Multiply
		put(4,3);  // / Divide
		put(5,3);  // % Modulus
		put(6,5);  // ^ Exponentiate
		put(7,6);  // ! Factorial
		put(8,4);  // - Unary Negation
		put(9,0);  // ( Function parenthesis
		put(10,1); // , Argument separation
	}};
	
	private static String binaries   = " +-*/%^   ,";  //List of all binary operators. Index important
	private static String prefixes   = "(       -( ";  //List of all prefix operators. Index important
	private static String postfixes  = "       !   "; //List of all postfix operators. Index important
	//Indices of these lists map to corresponding positions in the HashMap
	
	private static boolean isOp(char c) {
		return binaries.indexOf(c) >= 0 || prefixes.indexOf(c) >= 0 || postfixes.indexOf(c) >= 0;
	}
	
	public static void main(String[] args) {
//		System.out.println(stringEval("0+30/4*(1-3!)") + "\n");
//		System.out.println(stringEval("5*(3-1)") + "\n");
//		System.out.println(stringEval("5*3!") + "\n");
//		System.out.println(stringEval("5*(-3)") + "\n");
//		System.out.println(stringEval("5*-3") + "\n");
//		System.out.println(stringEval("5!*-3") + "\n");
//		System.out.println(stringEval("-3*5") + "\n");
//		System.out.println(stringEval("11!") + "\n");
//		System.out.println(stringEval("(((2+3))") + "\n"); //improper parentheses: should fail
//		System.out.println(stringEval("((2+3)))") + "\n"); //improper parentheses: should fail
//		System.out.println(stringEval("3/0+1") + "\n"); //Divide by zero: this case should fail
//		System.out.println(stringEval("4*double(4!-5)") + "\n");
//		System.out.println(stringEval("max(sin(6/5),cos(6/5))") + "\n");
//		System.out.println(stringEval("max(sin(1.2),cos(1.2),1)") + "\n");
//		System.out.println(stringEval("max(1,21,3,4,2,1,2)") + "\n");
//		System.out.println(stringEval("zeta(2)") + "\n");
//		System.out.println(stringEval("5(3)") + "\n");
		boolean exit = false;
		do {
			System.out.println(stringEval(ReadInput.readIn("Input a calculation: ")) + "\n");
			switch(ReadInput.readIn("Would you like to input another calculation? (Y/N): ").toUpperCase().charAt(0)) {
			case 'Y': break;
			case 'N':
			default: exit = true;
			break;
			}
		} while (!exit);
	}
	
	private static void applyFunc(String str, Stack<Double> num, int numArgs) throws ArithmeticException {
		switch (str) {
		case "double": //multiplies the number inputted
			num.push(num.pop()*2);
			break;
		case "sin": //Sine function
			num.push(Math.sin(num.pop()));
			break;
		case "cos": //Cosine function
			num.push(Math.cos(num.pop()));
			break;
		case "max": //Takes the max of n numbers where n >= 2
			double m = num.pop();
			for (int i = 1; i < numArgs; i++) m = Math.max(m, num.pop());
			num.push(m);
			break;
		case "tan": //Tangent function
			num.push(Math.tan(num.pop()));
			break;
		case "csc": //Cosecant function
			num.push(1/Math.sin(num.pop()));
			break;
		case "sec": //Secant function
			num.push(1/Math.cos(num.pop()));
			break;
		case "cot": //Cotanget function
			num.push(1/Math.tan(num.pop()));
			break;
		case "arctan": //inverse tangent function
			num.push(Math.atan(num.pop()));
			break;
		case "arcsin": //inverse sine function
			num.push(Math.asin(num.pop()));
			break;
		case "arccos": //inverse cosine function
			num.push(Math.acos(num.pop()));
			break;
		case "zeta": //Approximation of the Riemann Zeta function
			double n = num.pop();
			double a = 0;
			for (int i = 1; i < 10000; i++) a += 1/Math.pow(i,n);
			num.push(a);
			break;
		default: //If the function is invalid (not on the list)
			throw new ArithmeticException();
		}
	}
	
	public static double stringEval(String expr) {
		try { //yeah, yeah, I know. This is bad convention. Deal with it https://www.vignette3.wikia.nocookie.net/feed-the-beast/images/d/dd/Dealwithit.jpg/revision/latest?cb=20130809112807
		Stack<Integer> sym = new Stack<>(); //Symbol stack
		Stack<Double> num = new Stack<>(); //Number stack
		Stack<String> func = new Stack<>(); //Function stack
		Stack<Integer> args = new Stack<>(); //Holds the number of arguments in every function
		int decimals = -1; //Number of integers read in after a decimal separator
		double numBuff = 0; //Number buffer (for multiple digit numbers)
		boolean numFlag = false; //True when current character read is a number
		boolean stackFlag = false; //True when last stack pushed to was the number stack
		String strBuff = null; //String buffer (for functions)
		
		System.out.println("(" + expr + ")");
		
		for (char c : ("(" + expr + ")").toCharArray()) { //Loop over the entire String padded with parentheses
			if (c == ' ') { //do nothing if current character is a space
			} else if (c == '.') { //if the current character is a period (decimal separator)...
				if (!numFlag) { //if the last character read was not a number
					numFlag = true; //set numFlag to true
					numBuff = 0; //clear the number buffer
				}
				
				decimals = 0; //initialize decimal tracker
			} else if (Character.isAlphabetic(c)) { //else if the current character is in the set [A,a-Z,z]
				if (strBuff == null) { //if the string buffer is null
					strBuff = Character.toString(c); //initialize the string buffer to the current character
				} else { //else add onto the string buffer
					strBuff += Character.toString(c);
				}
			} else if (Character.isDigit(c)) { //If the character is a digit...
				if (!numFlag) { //If the last character read was not a number
					if (stackFlag) { //if the last stacked modified was the number stack
						sym.push(3); //implicit multiplication1 i.e. (3)5 => (3)*5
					}
					numFlag = true; //last character read was a number
					numBuff = 0; //initialize the number buffer
				}
				
				if (decimals >= 0) decimals++; //if decimals has been initialized, increment
				numBuff *= 10; //Shift digits to the left i.e.: 101 -> 1010
				numBuff += Character.digit(c, 10); //Add next digit i.e: 1010, 5 -> 1015
			} else { //If the character is not a digit...
				if (numFlag) { //If the last character read was a number
					numFlag = false; //Reset the flag
					if (decimals >= 0) //if decimals was initialized
						numBuff /= Math.pow(10, decimals); //the number buffer is divided by 10^decimals OR (numBuff)*10^(-decimals)
					num.push(numBuff); //push the number buffer to the stack
					decimals = -1; //reset decimal tracker
					stackFlag = true; //Last stack modified was the number stack
				}
				
				if (c == '(') { //If the current character is '('
					if (strBuff != null) { //if the string buffer isn't empty, we are dealing with a function
						func.push(strBuff); //push the string buffer to the function stack
						strBuff = null; //reset the string buffer
						sym.push(9); //push the Function-begin operation to the symbol stack
						args.push(1); //Push 1 to the arg stack. Unknown is there is more than one argument yet, but there must be at least 1
					} else { //if the string buffer is empty
						if (stackFlag) { //if the last stack modified was the number stack
							sym.push(3); //implicit multiplication2 i.e. 3(5) => 3*(5)
						}
						sym.push(0); //push '(' to the symbol stack
					}
					stackFlag = false; //the last stack modified was the symbol stack
				} else if (isOp(c) || c == ')') { //If the symbol is a valid symbol or is ')' ...
					int cop;
					if (stackFlag) { //if the last stack modified was the number stack
						int bin = binaries.indexOf(c); //binary index of the current character
						if (bin >= 0) {
							cop = bin;
							stackFlag = false;
						} else {
							cop = postfixes.indexOf(c); //postfix index ot the current character
							stackFlag = true;
						}
					} else {
						cop = prefixes.indexOf(c);
						stackFlag = false;
					}
					
					while (c == ')' || (!sym.isEmpty() && prec.get(sym.peek()) >= prec.get(cop))) {
						/* if: the current symbol is ')'
						 * 		collapse the stack until you reach the nearest '('
						 * else if: the stack isn't empty and the precedence of the symbol on top of the stack is greater than the precedence of the current symbol
						 * 		collapse the stack until you reach a symbol with a 
						 * 		lower precedence than the current symbol
						 */
						boolean stopEval = false;
						
						double op1; //number 1
						double op2; //number 2
						
						switch (sym.pop()) { //{Pop the symbol from the stack and operate
						case 0: // ( (normal)
							stopEval = true;
							break;
						case 1: // +
							op2 = num.pop();
							op1 = num.pop();
							num.push(op1+op2);
							break;
						case 2: // -
							op2 = num.pop();
							op1 = num.pop();
							num.push(op1-op2);
							break;
						case 3: // *
							op2 = num.pop();
							op1 = num.pop();
							num.push(op1*op2);
							break;
						case 4: // /
							op2 = num.pop();
							op1 = num.pop();
							if (op2 != 0) { //checking Divide-by-zero
								num.push(op1/op2);
							} else {
								throw new ArithmeticException();
							}
							break;
						case 5: // %
							op2 = num.pop();
							op1 = num.pop();
							num.push(op1%op2);
							break;
						case 6: // ^
							op2 = num.pop();
							op1 = num.pop();
							num.push(Math.pow(op1,op2));
							break;
						case 7: // !
							op1 = num.pop();
							num.push((double)factorial((int)op1));
							break;
						case 8: // - (unary)
							op1 = num.pop();
							num.push(-op1);
							break;
						case 9: // ( (function)
							applyFunc(func.pop(), num, args.pop());
							stopEval = true;
							break;
						case 10: // ,
							break;
						}
						
						if (stopEval) break;
						
					}
					
					if (c != ')') { //If the current character isn't ')'
						sym.push(cop);
					}
					if (c == ',') {
						args.push(args.pop()+1);
					}
				}
			}
			System.out.print(c + " " + sym + " " + num + " " + args);
			System.out.println(" " + numFlag); //append the state of the numFlag to the end of the debug message
		}
		Double out = num.pop();
		if (!sym.isEmpty()) { throw new ArithmeticException(); } //if there are still symbols in the stack, something is seriously wrong (throw an exception)
		
		return out; //pop the last number off the number stack (the answer)
		} catch (Exception e) { //Failing gracefully to allow all problems to run
			e.printStackTrace();
			System.out.println("\n*WARNING*\nAn error occurred. Moving to next problem (if it exists)"); //error message
			return Double.POSITIVE_INFINITY; //return a CLEARLY WRONG answer to satisfy return type
		}
	}
	
	private static int factorial(int n) { //basic recursive factorial method
		if (n == 1) { return 1; }
		return factorial(n-1) * n;
	}

}