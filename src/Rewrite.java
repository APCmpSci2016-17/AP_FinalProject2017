import java.util.HashMap;

public class Rewrite {
	private Expr from;
	private Expr to;
	
	public Rewrite(String all) {
		String[] parts = all.split("=>", 2);
		this.from = new Parser(parts[0]).getResult();
		this.to = new Parser(parts[1]).getResult();
	}
	
	public Rewrite(String from, String to) {
		this.from = new Parser(from).getResult();
		this.to = new Parser(to).getResult();
	}
	
	public Rewrite(Expr from, Expr to) {
		this.from = from;
		this.to = to;
	}
	
	public static class RewriteResult {
		public Expr result;
		public boolean changed;
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
				pattern.args.length == expr.args.length &&
				pattern.name.equals(expr.name)) {
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
	
	private static Expr replace(Expr to, HashMap<String, Expr> vars) {
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
		return this.runWithChanged(e).result;
	}
	
	public RewriteResult runWithChanged(Expr e) {
		HashMap<String, Expr> vars = match(e, from);
		if (vars != null) {
			return new RewriteResult() {{ result = replace(to, vars); changed = true; }};
		} else if (e.type.equals(Expr.Type.FUNCTION)) {
			Expr top = new Expr(e.name, e.args);
			boolean change = false;
			for (int i = 0; i < e.args.length; i++) {
				RewriteResult r = runWithChanged(e.args[i]);
				top.args[i] = r.result;
				change = change || r.changed;
			}
			final boolean thanksjava = change; // THANKS JAVA
			return new RewriteResult() {{ result = top; changed = thanksjava; }};
		}
		
		return new RewriteResult() {{ result = e; changed = false; }};
	}
	
	public String toString() {
		return "FROM: " + from.toString() + "\nTO: " + to.toString();
	}
	
	public static Rewrite[] readArray(String s) {
		String lines[] = s.split("\\r?\\n");
		Rewrite[] r = new Rewrite[lines.length];
		
		for (int i = 0; i < lines.length; i++) {
			r[i] = new Rewrite(lines[i]);
		}
		
		return r;
	}
	
	public static Expr runAll(Expr e, Rewrite[] rs) {
		Expr q = e;
		
		for (Rewrite r : rs) {
			boolean changed = false;
			while (!changed) {
				RewriteResult res = r.runWithChanged(q);
				q = res.result;
				changed = changed || res.changed;
			}
		}
		
		return q;
	}
}
