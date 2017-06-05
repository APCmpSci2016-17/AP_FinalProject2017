import java.util.ArrayList;
import java.util.Stack;

public class Expr {
	public enum Type {
		CONSTANT,
		FUNCTION,
		VARIABLE
	}
	
	public Type type; // The type of the expression
	public String name = null; // Name of function or variable
	public Expr[] args = null; // Arguments to function
	public Double val = null; // Value of constant
	
	public Expr(String name, Expr... args) {
		this.type = Type.FUNCTION;
		this.name = name;
		this.args = args;
	}
	
	public Expr(String name) {
		this.type = Type.VARIABLE;
		this.name = name;
	}
	
	public Expr(double val) {
		this.type = Type.CONSTANT;
		this.val = val;
	}
	
	public String toString() {
		String r = "";
		switch (type) {
			case CONSTANT:
				r = val.toString();
				break;
			case VARIABLE:
				r = name;
				break;
			case FUNCTION:
				r = name + "(";
				boolean doComma = false;
				for (Expr e : args) {
					if (doComma) r += ", ";
					doComma = true;
					r += e.toString();
				}
				r += ")";
				break;
		}
		return r;
	}
	
	public String[] findVars() {
		ArrayList<String> vars = new ArrayList<String>();
		Stack<Expr> toTry = new Stack<Expr>();
		
		while (!toTry.isEmpty()) {
			Expr cur = toTry.pop();
			switch (cur.type) {
				case CONSTANT:
					break;
				case VARIABLE:
					vars.add(cur.name);
					break;
				case FUNCTION:
					for (Expr e : cur.args) {
						toTry.push(e);
					}
					break;
			}
		}
		
		return (String[])vars.toArray();
	}
	
	public boolean equals(Object other) {
		Expr e = (Expr)other;
		
		if (this.type.equals(Type.CONSTANT) && e.type.equals(Type.CONSTANT) && this.val.equals(e.val) ||
			this.type.equals(Type.VARIABLE) && e.type.equals(Type.VARIABLE) && this.name.equals(e.name)) {
			return true;
		} else if (this.type.equals(Type.FUNCTION) && e.type.equals(Type.FUNCTION) && this.name.equals(e.name) && this.args.length == e.args.length) {
			for (int i = 0; i < this.args.length; i++) {
				if (!this.args[i].equals(e.args[i])) return false;
			}
			return true;
		} else {
			return false;
		}
	}
}
