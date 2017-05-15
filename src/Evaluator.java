import java.util.HashMap;
import java.util.Stack;

public class Evaluator {
	
	private static HashMap<Integer, Integer> prec = new HashMap<Integer, Integer>() {{
		put(0,0); // (
		put(1,1); // +
		put(2,1); // - (Minus)
		put(3,2); // *
		put(4,2); // /
		put(5,2); // %
		put(6,3); // ^
	}};
	
	private static String symbols = "(+-*/%^";
	
	public static void main(String[] args) {
//		System.out.println(stringEval("0+30/4*(1-2)") + "\n");
//		System.out.println(stringEval("5*(-1)") + "\n");
		System.out.println(stringEval("5*(3-1)") + "\n");
	}
	
	public static double stringEval(String expr) {
		Stack<Integer> sym = new Stack<>(); //Symbol stack
		Stack<Double> num = new Stack<>(); //Number stack
		double numBuff = 0; //Number buffer (for multiple digit numbers)
		boolean numFlag = false; //True when last character read is a number
		
		System.out.println("(" + expr + ")");
		
		for (char c : ("(" + expr + ")").toCharArray()) { //Loop over the entire String
			System.out.print(c + " " + sym + " " + num); //Print out in format <current char> <symbol stack> <number stack>
			if (Character.isDigit(c)) { //If the character is a digit...
				if (!numFlag) { //If the last character read was not a number, 
					numFlag = true;
					numBuff = 0;
				}
				
				numBuff *= 10;
				numBuff += Character.digit(c, 10);
			} else {
				if (numFlag) {
					numFlag = false;
					num.push(numBuff);
				}
				
				if (c == '(') {
					sym.push(0);
				} else if (symbols.indexOf(c) > 0 || c == ')') {
					while (c == ')' || (!sym.isEmpty() && prec.get(sym.peek()) >= prec.get(symbols.indexOf(c)))) {
						
						if (sym.peek() == 0) {
							sym.pop();
							break;
						}
						
						double op2 = num.pop();
						double op1 = num.pop();
						
						switch (sym.pop()) {
						case 1: // +
							num.push(op1+op2);
							break;
						case 2: // -
							num.push(op1-op2);
							break;
						case 3: // *
							num.push(op1*op2);
							break;
						case 4: // /
							num.push(op1/op2);
							break;
						case 5: // %
							num.push(op1%op2);
							break;
						case 6: // ^
							num.push(Math.pow(op1,op2));
							break;
						}
					}
					if (c != ')') sym.push(symbols.indexOf(c));
				}
			}
			System.out.println(" " + numFlag);
		}
		
		return num.pop();
	}

}
