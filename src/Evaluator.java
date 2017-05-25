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
	
	private static HashMap<Integer, Integer> appType = new HashMap<Integer, Integer>() {
		private static final long serialVersionUID = 1L;
	{
		put(0,1);  // ( Open Parentheses
		put(1,2);  // + Plus
		put(2,2);  // - Minus
		put(3,2);  // * Multiply
		put(4,2);  // / Divide
		put(5,2);  // % Modulus
		put(6,2);  // ^ Exponentiate
		put(7,1);  // ! Factorial
		put(8,1);  // - Unary Negation
		put(9,-1);  // ( Function parenthesis
		put(10,2); // , Argument separation
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
	private Stack<Expr> expr; //Number stack
	private Stack<Func> func; //Function stack
	private int decimals; //Number of integers read in after a decimal separator
	private int numBuff; //Number buffer (for multiple digit numbers)
	private boolean stackFlag; //True when last stack pushed to was the number stack
	private String strBuff; //String buffer (for functions)
	
	public Evaluator() {
		sym = new Stack<Integer>();
		expr = new Stack<Expr>();
		func = new Stack<Func>();
		decimals = -1;
		numBuff = -1;
		stackFlag = false;
		strBuff = null;
	}
	
	public Expr stringConvert(String inp)   {
		for (char c : ("(" + inp + ")").toCharArray()) readChar(c);
		return expr.pop();
	}
	
	private void processAlpha(char c) {
		if (strBuff == null) {
			strBuff = Character.toString(c);
		} else {
			strBuff += Character.toString(c);
		}
	}
	
	private void processDecimal() {
		if (numBuff == -1) {
			numBuff = 0;
		}
		
		decimals = 0;
	}
	
	private void addPossibleNumber() {
		if (numBuff >= 0) {
			if (decimals > 0) {
				expr.push(
					new Expr("/",
						new Expr(numBuff),
						new Expr("^",
							new Expr(10),
							new Expr(decimals)
						)
					)
				);
			} else {
				expr.push(new Expr(numBuff));
			}
			decimals = -1;
			numBuff = -1;
			stackFlag = true;
		}
	}
	
	private void processOpen() {
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
	}
	
	private void processSymbol(char c) {
		addPossibleNumber();
		
		if (c == '(') {
			processOpen();
		} else if (isOp(c) || c == ')') {
			applyPossibleConst(strBuff);
			putSymbol(c);
		}
	}
	
	private void readChar(char c)   {
		if (c == ' ') {
			// Do nothing
		} else if (c == '.') {
			processDecimal();
		} else if (Character.isAlphabetic(c)) {
			processAlpha(c);
		} else if (Character.isDigit(c)) {
			putNumBuff(c);
		} else {
			processSymbol(c);
		}
		System.out.println(c + " " + sym + " " + expr + " " + func);
	}
	
	private void eval(int index)   {
		while (index == -1 || (!sym.isEmpty() && prec.get(sym.peek()) >= prec.get(index))) {
			int op = sym.pop();
			// TODO
		}
	}
	
	private void applyPossibleConst(String constant) {
		if (constant != null) {
			expr.push(new Expr(constant));
		}
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
		
		if (decimals >= 0) decimals++;
		numBuff *= 10;
		numBuff += Character.digit(c, 10);
	}
	
	private void applyFunc(Func f)   {
		Expr[] args = new Expr[f.args];
		for (int i = f.args-1; i >= 0; i--) {
			args[i] = expr.pop();
		}
		expr.push(new Expr(f.name, args));
	}
}