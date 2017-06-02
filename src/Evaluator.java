
public class Evaluator {

	private Integer factorial(Integer i) {
		if (i == 1) return 1;
		return i * factorial(i - 1);
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
			return new Expr("" + Math.sin(one.val));
		}
	}
}
