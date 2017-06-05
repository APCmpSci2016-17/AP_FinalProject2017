import java.util.Stack;

public class Parser {
	private static int[] prec = {
			0, // ( Open Parentheses
			2, // + Plus
			2, // - Minus
			3, // * Multiply
			3, // / Divide
			3, // % Modulus
			5, // ^ Exponentiate
			6, // ! Factorial
			4, // - Unary Negation
			0, // ( Function parenthesis
			1  // , Argument separation
		};
		
		private enum AppType {
			UNARY,
			BINARY,
			FUNCPAREN,
			COMMA,
			OPEN,
		}
		
		private static AppType[] appType = {
			AppType.OPEN, // ( Open Parentheses
			AppType.BINARY, // + Plus
			AppType.BINARY, // - Minus
			AppType.BINARY, // * Multiply
			AppType.BINARY, // / Divide
			AppType.BINARY, // % Modulus
			AppType.BINARY, // ^ Exponentiate
			AppType.UNARY, // ! Factorial
			AppType.UNARY, // - Unary Negation
			AppType.FUNCPAREN, // ( Function parenthesis
			AppType.COMMA, // , Argument separation
		}; 

		private static String binaries   = " +-*/%^   ,";  //List of all binary operators. Index important
		private static String prefixes   = "(       -( ";  //List of all prefix operators. Index important
		private static String postfixes  = "       !   "; //List of all postfix operators. Index important
		private static String allOps     = "(+-*/%^!-(,";
		//Indices of these lists map to corresponding positions in the HashMap
		
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
		
		public Parser(String inp) {
			sym = new Stack<Integer>();
			expr = new Stack<Expr>();
			func = new Stack<Func>();
			decimals = -1;
			numBuff = -1;
			stackFlag = false;
			strBuff = null;
			for (char c : ("(" + inp + ")").toCharArray()) readChar(c);
		}
		
		public Expr getResult() {
			return expr.peek();
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
			} else if (allOps.indexOf(c) >= 0 || c == ')') {
				applyPossibleConst(strBuff);
				putSymbol(c);
			}
		}
		
		private void readChar(char c)   {
			if (c == ' ') {
				// Do nothing
			} else if (c == '.') {
				processDecimal();
			} else if (Character.isAlphabetic(c) || c == '$') {
				processAlpha(c);
			} else if (Character.isDigit(c)) {
				putNumBuff(c);
			} else {
				processSymbol(c);
			}
		}
		
		private void eval(int index) {
			boolean stop = false;
			while (!sym.isEmpty() && !stop && (index == -1 || prec[sym.peek()] >= prec[index])) {
				int op = sym.pop();
				if (op == 0) {
					break;
				} else {
					switch (appType[op]) {
						case UNARY:
							expr.push(new Expr(allOps.substring(op, op+1),expr.pop()));
							break;
						case BINARY:
							Expr op2 = expr.pop();
							Expr op1 = expr.pop();
							expr.push(new Expr(allOps.substring(op, op+1),op1,op2));
							break;
						case FUNCPAREN:
							applyFunction();
							stop = true;
							break;
						case COMMA:
							break;
						case OPEN:
							stop = true;
							break;
					}
				}
			}
		}
		
		private void applyPossibleConst(String constant) {
			if (constant != null) {
				expr.push(new Expr(constant));
				strBuff = null;
				stackFlag = true;
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
				f.args++;
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
		
		private void applyFunction() {
			Func f = func.pop();
			System.out.println(f.name + ":" + f.args + ":" + expr);
			Expr[] args = new Expr[f.args];
			for (int i = f.args-1; i >= 0; i--) {
				args[i] = expr.pop();
			}
			expr.push(new Expr(f.name, args));
		}
}
