import java.util.HashMap;
import java.util.Stack;

public class Evaluator {
	
	private static HashMap<Integer, Integer> prec = new HashMap<Integer, Integer>() {{
		put(0,0); // ( Open Parenthese
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
	
	public static void main(String[] args) {
		System.out.println(stringEval("0+30/4*(1-3!)") + "\n");
		System.out.println(stringEval("5*(3-1)") + "\n");
		System.out.println(stringEval("5*3!") + "\n");
		System.out.println(stringEval("5*(-3)") + "\n");
//		System.out.println(stringEval("5*-3"));
		System.out.println(stringEval("-3*5"));
	}
	
	public static double stringEval(String expr) {
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
					
					if (!sym.isEmpty() && sym.peek() == 8) { //If the symbol stack isn't empty and 
															 //there is a unary negation at the top of the stack 
						sym.pop();
						numBuff *= -1; //Negate the buffer
					}
					
					num.push(numBuff);
					stackFlag = true; //Last stack modified was the number stack
				}
				
				if (c == '(') { //If the current character is ( push 0 to the symbol stack and modify the flag
					sym.push(0);
					stackFlag = false;
				} else if (binaries.indexOf(c) > 0 || c == ')') { //If the symbol is in the binary list or is )...
					
					while (c == ')' || (!sym.isEmpty() && prec.get(sym.peek()) >= prec.get(binaries.indexOf(c)))) {
						/* if the current symbol is )
						 * 		collapse the stack until you reach the nearest (
						 * else if the stack isn't empty and the precedence of the symbol on top of the stack is greater than the precedence of the current symbol
						 * 		collapse the stack until you reach a symbol with a 
						 * 		lower precedence than the current symbol
						 */
						if (sym.peek() == 0) {
							sym.pop();
							break;
						}
						
						double op2;
						double op1;
						
						switch (sym.pop()) {
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
							num.push(op1/op2);
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
							break;
						}
						
						
						
					}
					if (c != ')') {
						if (stackFlag) sym.push(binaries.indexOf(c));
						else sym.push(unaries.indexOf(c));
						stackFlag = false;
					} else {
						stackFlag = true;
					}
				}
			}
			System.out.println();
		}
		
		return num.pop();
	}
	
	private static int factorial(int n) {
		if (n == 1) { return 1; }
		return factorial(n-1) * n;
	}

}
