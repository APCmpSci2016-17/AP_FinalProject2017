import java.util.HashMap;

public class Rewrite {
	private Expr from;
	private Expr to;
	
	public Rewrite(String from, String to) {
		this.from = new Expr(from);
		this.to = new Expr(to);
	}
	
	public Rewrite(Expr from, Expr to) {
		this.from = from;
		this.to = to;
	}
	
	private static HashMap<String, Expr> match(Expr expr, Expr pattern) {
		if (pattern.type.equals(Expr.Type.VARIABLE) && pattern.name.startsWith("$")) {
			return new HashMap<String, Expr>() {{ put(pattern.name,expr); }};
		}
		
		if (pattern.type.equals(Expr.Type.CONSTANT) && expr.type.equals(Expr.Type.CONSTANT) && expr.val.equals(pattern.val)
				|| expr.type.equals(Expr.Type.VARIABLE) && pattern.type.equals(Expr.Type.VARIABLE) && expr.name.equals(pattern.name)) {
			return new HashMap<String, Expr>();
		}
		
		if (pattern.type.equals(Expr.Type.FUNCTION) &&
				expr.type.equals(Expr.Type.FUNCTION) &&
				pattern.args.length == expr.args.length) {
			HashMap<String, Expr> total = new HashMap<String, Expr>();
			for (int i = 0; i < pattern.args.length; i++) {
				HashMap<String, Expr> submatch = match(expr.args[i], pattern.args[i]);
				if (submatch == null) return null;
				total.putAll(submatch);
			}
			return total;
		}
		
		return null;
	}
	
	public static Expr replace(Expr to, HashMap<String, Expr> vars) {
		if (to.type.equals(Expr.Type.VARIABLE) && to.name.startsWith("$")) {
			return vars.getOrDefault(to.name, to);
		} else if (to.type.equals(Expr.Type.CONSTANT) || to.type.equals(Expr.Type.VARIABLE)) {
			return to;
		} else {
			Expr e = new Expr(to.name, to.args);
			for (int i = 0; i < e.args.length; i++) {
				e.args[i] = replace(to.args[i], vars);
			}
			return e;
		}
	}
	
	public Expr run(Expr e) {
		HashMap<String, Expr> vars = match(e, from);
		if (vars != null) {
			return replace(to, vars);
		} else if (e.type.equals(Expr.Type.FUNCTION)) {
			Expr top = new Expr(e.name, e.args);
			
			for (int i = 0; i < e.args.length; i++) {
				top.args[i] = run(e.args[i]);
			}
			
			return top;
		}
		
		return e;
	}
}
