import java.util.HashMap;
import java.util.Stack;

public class Evaluator {
	
	private static HashMap<Integer, Integer> prec = new HashMap<Integer, Integer>() {
		private static final long serialVersionUID = 1L; //HashMap is serializable, requires this variable (or else it gives a really annoying warning)
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
	
	private class Func {
		public String name;
		public int args;
		
		public Func(String n) {
			name = n;
			args = 1;
		}
		
		public String toString() {
			return name;
		}
	}
	
	private Stack<Integer> sym; //Symbol stack
	private Stack<Double> num; //Number stack
	private Stack<Func> func; //Function stack
	private int decimals; //Number of integers read in after a decimal separator
	private double numBuff; //Number buffer (for multiple digit numbers)
	private boolean stackFlag; //True when last stack pushed to was the number stack
	private String strBuff; //String buffer (for functions)
	
	public Evaluator() {
		sym = new Stack<Integer>();
		num = new Stack<Double>();
		func = new Stack<Func>();
		decimals = -1;
		numBuff = -1;
		stackFlag = false;
		strBuff = null;
	}
	
	public double stringEval(String expr)   {
		for (char c : ("(" + expr + ")").toCharArray()) readChar(c);
		return num.pop();
	}
	
	private void readChar(char c)   {
		if (c == ' ') {
			// Do nothing
		} else if (c == '.') {
			if (numBuff == -1) {
				numBuff = 0;
			}
			
			decimals = 0;
		} else if (Character.isAlphabetic(c)) {
			if (strBuff == null)
				strBuff = Character.toString(c);
			else
				strBuff += Character.toString(c);
		} else if (Character.isDigit(c)) {
			putNumBuff(c);
		} else {
			if (numBuff >= 0) {
				if (decimals >= 0)
					numBuff /= Math.pow(10, decimals);
				num.push(numBuff);
				decimals = -1;
				numBuff = -1;
				stackFlag = true;
			}
			
			if (c == '(') {
				if (strBuff != null) {
					func.push(new Func(strBuff));
					strBuff = null;
					sym.push(9);
				} else {
					if (stackFlag) {
						putSymbol('*');
					}
					sym.push(0);
				}
				stackFlag = false;
			} else if (isOp(c) || c == ')') {
				if (strBuff != null && func.peek().name != "sum")
					applyConst(strBuff);
				putSymbol(c);
			}
		}
		System.out.println(c + " " + sym + " " + num + " " + func);
	}
	
	private void eval(int index)   {
		while (index == -1 || (!sym.isEmpty() && prec.get(sym.peek()) >= prec.get(index))) {
			
			boolean stopEval = false;
			
			double op1;
			double op2;
			
			switch (sym.pop()) {
			default: throw new ArithmeticException();
			case 0: // ( (normal)
				stopEval = true;
				break;
			case 1: // +
				op2 = num.pop();
				op1 = num.pop();
				num.push(op1 + op2);
				break;
			case 2: // -
				op2 = num.pop();
				op1 = num.pop();
				num.push(op1 - op2);
				break;
			case 3: // *
				op2 = num.pop();
				op1 = num.pop();
				num.push(op1 * op2);
				break;
			case 4: // /
				op2 = num.pop();
				op1 = num.pop();
				if (op2 != 0) { // checking Divide-by-zero
					num.push(op1 / op2);
				} else {
					throw new ArithmeticException();
				}
				break;
			case 5: // %
				op2 = num.pop();
				op1 = num.pop();
				num.push(op1 % op2);
				break;
			case 6: // ^
				op2 = num.pop();
				op1 = num.pop();
				num.push(Math.pow(op1, op2));
				break;
			case 7: // !
				op1 = num.pop();
				num.push((double) factorial((int) op1));
				break;
			case 8: // - (unary)
				op1 = num.pop();
				num.push(-op1);
				break;
			case 9: // ( (function)
				applyFunc(func.pop());
				stopEval = true;
				break;
			case 10: // ,
				break;
			}
			
			if (stopEval) break;
		}
	}
	
	private void applyConst(String constant) {
		switch (constant) {
		default: throw new ArithmeticException();
		case "pi": //Pi
			num.push(Math.PI);
			break;
		case "e": //Eulers number
			num.push(Math.E);
			break;
		case "tau": //Tau
			num.push(2 * Math.PI);
			break;
		case "gr": //Golden Ratio
			num.push(1.618033988749895);
			break;
		case "c": //Speed of light in m/s
			num.push(299792458d);
			break;
		case "G": //Gravitational Constant
			num.push(6.674e-11);
			break;
		}
		
		strBuff = null;
		stackFlag = true;
	}
	
	private void putSymbol(char c)   {
		int symIndex = -1;
		if (stackFlag) { //if the last stack modified was the number stack
			int bin = binaries.indexOf(c); //binary index of the current character
			if (bin >= 0) { //if the character is in the binary list
				symIndex = bin; //set cop = bin
				stackFlag = false; //last stack modified will be the symbol stack
			} else { //if the character isn't in the binary list
				symIndex = postfixes.indexOf(c); //set cop = postfix index of the current character
				stackFlag = true; //last stack modified will be the number stack
			}
		} else { //if the last stack modified was the symbol stack
			symIndex = prefixes.indexOf(c); //set cop = prefix index of the current character
			stackFlag = false; //last stack modified will be the symbol stack
		}
		
		eval(symIndex);
		
		if (c != ')')
			sym.push(symIndex);
		if (c == ',') {
			Func f = func.pop();
			f.args ++;
			func.push(f);
		}
	}
	
	private void putNumBuff(char c) {
		if (numBuff == -1) {
			if (stackFlag) {
				putSymbol('*');
			}
			numBuff = 0;
		}
		
		if (decimals >= 0) decimals ++;
		numBuff *= 10;
		numBuff += Character.digit(c, 10);
	}
	
	private void applyFunc(Func f)   {
		int numArgs = f.args;
		String str = f.name;
		
		switch (str) {
		default: throw new ArithmeticException();
		case "double": //multiplies the number input by 2
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
			if (num.peek() == Double.NaN)
				throw new ArithmeticException();
			break;
		case "arccos": //inverse cosine function
			num.push(Math.acos(num.pop()));
			if (num.peek().equals(Double.NaN))
				throw new ArithmeticException();
			break;
		case "zeta": //Approximation of the Riemann Zeta function
			double n = num.pop();
			double a = 0;
			for (int i = 1; i < 10000; i++) a += 1/Math.pow(i,n);
			num.push(a);
			break;
		case "abs": //absolute value function
			num.push(Math.abs(num.pop()));
			break;
		case "ln": //natural log function
			num.push(Math.log(num.pop()));
			break;
		case "log": //log function
			double number = num.pop();
			double base = num.pop();
			num.push(Math.log(number)/Math.log(base));
			break;
		case "floor": //floor function
			num.push(Math.floor(num.pop()));
			break;
		case "ceiling": //ceiling function
			num.push(Math.ceil(num.pop()));
			break;
		case "nPr": //Permutation function
			double y = num.pop();
			double x = num.pop();
			num.push((double) nPr((int) x, (int) y));
			break;
		case "nCr": //Combination function
			double R = num.pop();
			double N = num.pop();
			num.push((double) (nCr((int) N, (int) R)));
			break;
		case "sum":
			Func var = func.pop();
			Func expr = func.pop();
			double end = num.pop();
			double start = num.pop();
			num.push(summation(expr.name, var.name, start, end));
		}
	}

	private int nPr(int n, int r) {
		return factorial(n) / factorial(n-r);
	}
	
	private int nCr(int n, int r) {
		return factorial(n) / (factorial (n-r) * factorial(r));
	}
	
	private int factorial(int n) { //basic recursive factorial method
		if (n == 1) { return 1; }
		else if (n < 1) { throw new ArithmeticException(); }
		return factorial(n-1) * n;
	}
	
	private double summation(String expr, String var, double start, double end) {
		double sum = 0;
		for (int i = (int) Math.floor(start); i <= end; i ++) {
			String cur = expr.substring(0);
			cur.replaceAll(var, "" + i);
			sum += stringEval(cur);
		}
		return sum;
	}

}