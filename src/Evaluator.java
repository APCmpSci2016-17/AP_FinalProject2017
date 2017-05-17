import java.util.HashMap;
import java.util.Stack;

public class Evaluator {
	
	private static HashMap<Integer, Integer> prec = new HashMap<Integer, Integer>() {
		private static final long serialVersionUID = 1283576132598L; //HashMap is serializable, requires this variable (or else it gives a really annoying warning)
	{ 
		//HashMap of all operations with corresponding precedence (higher takes place before lower)
		//Note: Close Parentheses [')'] are not included here as it is handled uniquely
		put(0,0); // ( Open Parentheses
		put(1,1); // + Plus
		put(2,1); // - Minus
		put(3,2); // * Multiply
		put(4,2); // / Divide
		put(5,2); // % Modulus
		put(6,4); // ^ Exponentiate
		put(7,5); // ! Factorial
		put(8,3); // - Unary Negation
	}};
	
	private static String binaries = "(+-*/%^!_"; //List of all binary operators and post-operators. Index important
	private static String unaries  = "________-"; //List of all unary pre-operators. Parallel to the binaries list
	//Indices of these lists map to corresponding positions in the HashMap
	
	public static void main(String[] args) {
		System.out.println(stringEval("0+30/4*(1-3!)") + "\n");
		System.out.println(stringEval("5*(3-1)") + "\n");
		System.out.println(stringEval("5*3!") + "\n");
		System.out.println(stringEval("5*(-3)") + "\n");
		System.out.println(stringEval("5*-3") + "\n"); //This case fails
		System.out.println(stringEval("-3*5") + "\n");
		System.out.println(stringEval("11!") + "\n");
		System.out.println(stringEval("(((2+3))") + "\n"); //improper parentheses: should fail
		System.out.println(stringEval("((2+3)))") + "\n"); //improper parentheses: should fail
		System.out.println(stringEval("3/0+1") + "\n"); //Divide by zero: this case should fail
	}
	
	public static double stringEval(String expr) {
		try { //yeah, yeah, I know. This is bad convention. Deal with it https://www.vignette3.wikia.nocookie.net/feed-the-beast/images/d/dd/Dealwithit.jpg/revision/latest?cb=20130809112807
		Stack<Integer> sym = new Stack<>(); //Symbol stack
		Stack<Double> num = new Stack<>(); //Number stack
		double numBuff = 0; //Number buffer (for multiple digit numbers)
		boolean numFlag = false; //True when current character read is a number
		boolean stackFlag = false; //True when last stack pushed to was the number stack
		
		System.out.println("(" + expr + ")");
		
		for (char c : ("(" + expr + ")").toCharArray()) { //Loop over the entire String
			System.out.print(c + " " + sym + " " + num); //Print out in format <current char> <symbol stack> <number stack>
			if (Character.isDigit(c)) { //If the character is a digit...
				
				if (!numFlag) { //If the last character read was not a number clear the buffer
					numFlag = true;
					numBuff = 0;
				}
				
				numBuff *= 10; //Shift digits to the left i.e.: 101 -> 1010
				numBuff += Character.digit(c, 10); //Add next digit i.e: 1010, 5 -> 1015
			} else { //If the character is not a digit...
				if (numFlag) { //If the last character read was a number
					numFlag = false; //Reset the flag
					num.push(numBuff);
					stackFlag = true; //Last stack modified was the number stack
				}
				
				if (c == '(') { //If the current character is ( push 0 to the symbol stack and modify the flag
					sym.push(0);
					stackFlag = false;
				} else if (binaries.indexOf(c) > 0 || c == ')') { //If the symbol is in the binary list or is )...
					
					while (c == ')' || (!sym.isEmpty() && prec.get(sym.peek()) >= prec.get(binaries.indexOf(c)))) {
						/* if: the current symbol is ')'
						 * 		collapse the stack until you reach the nearest '('
						 * else if: the stack isn't empty and the precedence of the symbol on top of the stack is greater than the precedence of the current symbol
						 * 		collapse the stack until you reach a symbol with a 
						 * 		lower precedence than the current symbol
						 */
						if (sym.peek() == 0) { //if the symbol at the top of the stack is ) remove it from the stack and finish the collapse
							sym.pop();
							break;
						}
						
						double op1; //number 1
						double op2; //number 2
						
						switch (sym.pop()) { //{Pop the symbol from the stack and operate
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
								int a = 10/0; //throws a random divide-by-zero exception
											  //doubles default to Infinity instead of throwing
											  //an exception.
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
						}
						
					}
					if (c != ')') { //If the current character isn't ')'
						if (stackFlag) sym.push(binaries.indexOf(c)); //if the last stack modified was the number stack push from the binary symbols
						else sym.push(unaries.indexOf(c)); //else push from the unary symbols
						stackFlag = false; //last stack modified was the symbol stack
					} else {
						stackFlag = true; //special case, effectively anything in parentheses is a number therefore writing to the number stack
					}
				}
			}
			System.out.println(" " + numFlag); //append the state of the numFlag to the end of the debug message
		}
		Double out = num.pop();
		if (!sym.isEmpty()) { int a = 10/0; } //if there are still symbols in the stack, something is seriously wrong (throw an exception)
		
		return out; //pop the last number off the number stack (the answer)
		} catch (Exception e) { //Failing gracefully to allow all problems to run
			System.out.println("\n*WARNING*\nAn error occurred. Moving to next problem (if it exists)"); //error message
			return Double.POSITIVE_INFINITY; //return a CLEARLY WRONG answer to satisfy return type
		}
	}
	
	private static int factorial(int n) { //basic recursive factorial method
		if (n == 1) { return 1; }
		return factorial(n-1) * n;
	}

}
