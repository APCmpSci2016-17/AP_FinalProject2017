
public class Evaluator {

	private Integer factorial(Integer i) {
		if (i == 1) return 1;
		return i * factorial(i - 1);
	}
	
	private Integer nPr(int n, int r) {
		return factorial(n) / factorial(n-r);
	}
	
	private Integer gcd (int a, int b) {
		if (b == 0)
			return a;
		else
			return gcd(b, a % b);
	}
	
	private Integer nCr(int n, int r) {
		return factorial(n) / (factorial (n-r) * factorial(r));
	}
	
	private Integer isPrime(int n) {
		if(n < 2) return 0;
	    if(n == 2 || n == 3) return 1;
	    if(n%2 == 0 || n%3 == 0) return 0;
	    long sqrtN = (long)Math.sqrt(n)+1;
	    for(long i = 6L; i <= sqrtN; i += 6) {
	        if(n%(i-1) == 0 || n%(i+1) == 0) return 0;
	    }
	    return 1;
	}
	
	public Expr run(Expr expr) throws ArithmeticException {
		if (expr.args.length == 0 || expr.type == Expr.Type.CONSTANT) {
			return expr;
		}
		
		Expr one = null;
		Expr two = null;
		
		if (expr.args.length >= 1 && expr.args[0].type == Expr.Type.FUNCTION) {
			one = run(expr.args[0]);
		} else if (expr.args.length != 0) {
			one = expr.args[0];
		}
		
		if (expr.args.length > 1 && expr.args[1].type == Expr.Type.FUNCTION) {
			two = run(expr.args[1]);
		} else if (expr.args.length > 1) {
			two = expr.args[1];
		}
		
//		System.out.println(one.val + " " + two.val);
		
		switch(expr.name) {
		default: throw new ArithmeticException();
		case "+": 
			return new Expr(one.val + two.val);
		case "-":
			if (expr.args.length == 1)
				return new Expr(-one.val);
				else
				return new Expr(one.val - two.val);
		case "*":
			return new Expr(one.val * two.val);
		case "/":
			return new Expr(one.val/two.val);
		case "^":
			return new Expr(Math.pow(one.val, two.val));
		case "%":
			return new Expr(one.val % two.val);
		case "!":
			return new Expr(factorial((int) Math.round(one.val)));
		case "sin":
			return new Expr(Math.sin(one.val));
		case "cos":
			return new Expr(Math.cos(one.val));
		case "tan":
			return new Expr(Math.tan(one.val));
		case "sec":
			return new Expr(1.0 / Math.cos(one.val));
		case "csc":
			return new Expr(1.0 / Math.sin(one.val));
		case "cot":
			return new Expr(1.0 / Math.tan(one.val));
		case "arctan":
			return new Expr(Math.atan(one.val));
		case "arcsin":
			Expr e = new Expr(Math.asin(one.val));
			if (e.val.equals(Double.NaN)) throw new ArithmeticException();
			return e;
		case "arccos":
			e = new Expr(Math.acos(one.val));
			if (e.val.equals(Double.NaN)) throw new ArithmeticException();
			return e;
		case "zeta":
			int count = 0;
			for (int i = 1; i < 10000; i ++) count += 1/Math.pow(i, one.val);
			return new Expr(count);
		case "abs":
			return new Expr(Math.abs(one.val));
		case "ln":
			return new Expr(Math.log(one.val));
		case "log":
			return new Expr(Math.log(one.val)/Math.log(two.val));
		case "floor":
			return new Expr(Math.floor(one.val));
		case "ceil":
			return new Expr(Math.ceil(one.val));
		case "nPr":
			return new Expr(nPr((int) Math.round(one.val), (int) Math.round(two.val)));
		case "nCr":
			return new Expr(nCr((int) Math.round(one.val), (int) Math.round(two.val)));
		case "lcm":
			return new Expr((one.val * two.val) / gcd((int) Math.round(one.val), (int) Math.round(two.val)));
		case "gcd":
			return new Expr(gcd((int) Math.round(one.val), (int) Math.round(two.val)));
		case "rand":
			return new Expr(Math.random()*(one.val - two.val) + two.val);
		case "prime":
			return new Expr(isPrime(one.val.intValue()));
		case "signum":
			return new Expr(Math.signum(one.val));
		case "sqrt":
			return new Expr(Math.sqrt(one.val));
		case "cbrt":
			return new Expr(Math.cbrt(one.val));
		case "max":
			e = new Expr(one.val);
			for (int i = 1; i < expr.args.length; i ++) e.val = Math.max(e.val, expr.args[i].val);
			return e;
		}
	}
}
