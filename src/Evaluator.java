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
		put(10,1); //, Argument separation
	}};
	
	private static String binaries   = " +-*/%^   ,"; //List of all binary operators and post-operators. Index important
	private static String prefixes   = "(       -( ";
	private static String postfixes  = "       !   "; //List of all unary pre-operators. Parallel to the binaries list
	//Indices of these lists map to corresponding positions in the HashMap
	
	private static boolean isOp(char c) {
		return binaries.indexOf(c) >= 0 || prefixes.indexOf(c) >= 0 || postfixes.indexOf(c) >= 0;
	}
	
	public static void main(String[] args) {
		System.out.println(stringEval("0+30/4*(1-3!)") + "\n");
//		System.out.println(stringEval("5*(3-1)") + "\n");
//		System.out.println(stringEval("5*3!") + "\n");
//		System.out.println(stringEval("5*(-3)") + "\n");
		System.out.println(stringEval("5*-3") + "\n");
		System.out.println(stringEval("5!*-3") + "\n");
//		System.out.println(stringEval("-3*5") + "\n");
//		System.out.println(stringEval("11!") + "\n");
//		System.out.println(stringEval("(((2+3))") + "\n"); //improper parentheses: should fail
//		System.out.println(stringEval("((2+3)))") + "\n"); //improper parentheses: should fail
//		System.out.println(stringEval("3/0+1") + "\n"); //Divide by zero: this case should fail
		System.out.println(stringEval("4*double(4!-5)") + "\n");
		System.out.println(stringEval("max(sin(6/5),cos(6/5))") + "\n");
		System.out.println(stringEval("max(sin(1.2),cos(1.2))") + "\n");
	}
	
	private static void applyFunc(String str, Stack<Double> num) {
		switch (str) {
		case "double":
			num.push(num.pop()*2);
			break;
		case "sin":
			num.push(Math.sin(num.pop()));
			break;
		case "cos":
			num.push(Math.cos(num.pop()));
			break;
		case "max":
			num.push(Math.max(num.pop(), num.pop()));
			break;
		}
	}
	
	public static double stringEval(String expr) {
		try { //yeah, yeah, I know. This is bad convention. Deal with it https://www.vignette3.wikia.nocookie.net/feed-the-beast/images/d/dd/Dealwithit.jpg/revision/latest?cb=20130809112807
		Stack<Integer> sym = new Stack<>(); //Symbol stack
		Stack<Double> num = new Stack<>(); //Number stack
		Stack<String> func = new Stack<>(); //Number stack
		int decimals = -1;
		double numBuff = 0; //Number buffer (for multiple digit numbers)
		boolean numFlag = false; //True when current character read is a number
		boolean stackFlag = false; //True when last stack pushed to was the number stack
		String strBuff = null;
		
		System.out.println("(" + expr + ")");
		
		for (char c : ("(" + expr + ")").toCharArray()) { //Loop over the entire String
//			System.out.print(c + " " + sym + " " + num); //Print out in format <current char> <symbol stack> <number stack>
			if (c == ' ') { //do nothing if space
			} else if (c == '.') {
				if (!numFlag) {
					numFlag = true;
					numBuff = 0;
				}
				
				decimals = 0;
			} else if (Character.isAlphabetic(c)) {
				if (strBuff == null) {
					strBuff = Character.toString(c);
				} else {
					strBuff += Character.toString(c);
				}
			} else if (Character.isDigit(c)) { //If the character is a digit...
				
				if (!numFlag) { //If the last character read was not a number clear the buffer
					numFlag = true;
					numBuff = 0;
				}
				
				if (decimals >= 0) decimals++;
				numBuff *= 10; //Shift digits to the left i.e.: 101 -> 1010
				numBuff += Character.digit(c, 10); //Add next digit i.e: 1010, 5 -> 1015
			} else { //If the character is not a digit...
				if (numFlag) { //If the last character read was a number
					numFlag = false; //Reset the flag
					if (decimals >= 0) numBuff /= Math.pow(10, decimals);
					num.push(numBuff);
					decimals = -1;
					stackFlag = true; //Last stack modified was the number stack
				}
				
				if (c == '(') { //If the current character is ( push 0 to the symbol stack and modify the flag
					if (strBuff != null) {
						func.push(strBuff);
						strBuff = null;
						sym.push(9);
					} else {
						sym.push(0);
					}
					stackFlag = false;
				} else if (isOp(c) || c == ')') { //If the symbol is in the binary list or is )...
					int cop;
					if (stackFlag) {
						int bin = binaries.indexOf(c);
						if (bin >= 0) {
							cop = bin;
							stackFlag = false;
						} else {
							cop = postfixes.indexOf(c);
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
							applyFunc(func.pop(), num);
							stopEval = true;
							break;
						case 10: // ,
							break;
						}
						
						if (stopEval) break;
						
					}
					if (c != ')') { //If the current character isn't ')'
						sym.push(cop);
					} else {
					}
				}
			}
			System.out.print(c + " " + sym + " " + num);
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