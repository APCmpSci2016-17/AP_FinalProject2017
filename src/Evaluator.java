public class Evaluator {

	private static long factorial(int i) {
		if (i == 1) return 1l;
		return i * factorial(i - 1);
	}
	
	private static long nPr(int n, int r) {
		return factorial(n) / factorial(n-r);
	}
	
	private static int gcd (int a, int b) {
		if (b == 0)
			return a;
		else
			return gcd(b, a % b);
	}
	
	private static long nCr(int n, int r) {
		return factorial(n) / (factorial (n-r) * factorial(r));
	}
	
	private static int isPrime(int n) {
		if(n < 2) return 0;
	    if(n == 2 || n == 3) return 1;
	    if(n%2 == 0 || n%3 == 0) return 0;
	    long sqrtN = (long)Math.sqrt(n)+1;
	    for(long i = 6L; i <= sqrtN; i += 6) {
	        if(n%(i-1) == 0 || n%(i+1) == 0) return 0;
	    }
	    return 1;
	}
	
	public static double eval(Expr e) {	
		switch (e.type) {
			case CONSTANT:
				return constant(e);
			case FUNCTION:
				double[] as = new double[e.args.length];
				for (int i = 0; i < as.length; i++)
					as[i] = eval(e.args[i]);
				return runFunc(e.name, as);
			default:
				throw new ArithmeticException();
		}
	}
	
	private static Double constant(Expr expr) {
		switch (expr.name) {
		default: throw new ArithmeticException();
		case "pi": return Math.PI;
		case "e": return Math.E;
		case "tau": return 2 * Math.PI;
		case "phi": return 1.618033988749895;
		case "c": return 299792458d;
		case "G": return 6.674e-11;
		}
	}
	
	public static double runFunc(String op, double[] a) throws ArithmeticException {
		double e;
		switch(op) {
		default: throw new ArithmeticException();
		case "+": 
			return a[0] + a[1];
		case "-":
			if (a.length == 1)
				return -a[0];
			else
				return a[0]-a[1];
		case "*":
			return a[0] * a[1];
		case "/":
			return a[0] / a[1];
		case "^":
			return Math.pow(a[0], a[1]);
		case "%":
			return a[0] % a[1];
		case "!":
			return factorial((int)a[0]);
		case "sin":
			return Math.sin(a[0]);
		case "cos":
			return Math.cos(a[0]);
		case "tan":
			return Math.tan(a[0]);
		case "sec":
			return 1.0 / Math.cos(a[0]);
		case "csc":
			return 1.0 / Math.sin(a[0]);
		case "cot":
			return 1.0 / Math.tan(a[0]);
		case "arctan":
			return Math.atan(a[0]);
		case "arcsin":
			e = Math.asin(a[0]);
			if (e == Double.NaN) throw new ArithmeticException();
			return e;
		case "arccos":
			e = Math.acos(a[0]);
			if (e == Double.NaN) throw new ArithmeticException();
			return e;
		case "zeta":
			e = 0;
			for (int i = 1; i < 10000; i ++) e += 1/Math.pow(i, a[0]);
			return e;
		case "abs":
			return Math.abs(a[0]);
		case "ln":
			return Math.log(a[0]);
		case "log":
			return Math.log(a[0])/Math.log(a[1]);
		case "floor":
			return Math.floor(a[0]);
		case "ceil":
			return Math.ceil(a[0]);
		case "nPr":
			return nPr((int)a[0], (int)a[1]);
		case "nCr":
			return nCr((int)a[0], (int)a[1]);
		case "lcm":
			return (a[0]*a[1]) / gcd((int)a[0], (int)a[1]);
		case "gcd":
			return gcd((int)a[0], (int)a[1]);
		case "rand":
			return (int)(Math.random()*(a[1]-a[0]))+a[0];
		case "prime":
			return isPrime((int)a[0]);
		case "signum":
			return Math.signum(a[0]);
		case "sqrt":
			return Math.sqrt(a[0]);
		case "cbrt":
			return Math.cbrt(a[0]);
		case "max":
			e = a[0];
			for (int i = 1; i < a.length; i ++) e = Math.max(e, a[i]);
			return e;
		}
	}
}
