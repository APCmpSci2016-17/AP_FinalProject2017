import java.util.ArrayList;
import java.util.Stack;

public class Expr {
	private enum Type {
		CONSTANT,
		FUNCTION,
		VARIABLE
	}
	
	private Type type; // The type of the expression
	private String name = null; // Name of function or variable
	private Expr[] args = null; // Arguments to function
	private Integer val = null; // Value of constant
	
	public Expr(String name, Expr... args) {
		this.type = Type.FUNCTION;
		this.name = name;
		this.args = args;
	}
	
	public Expr(String name) {
		this.type = Type.VARIABLE;
		this.name = name;
	}
	
	public Expr(int val) {
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
						toTry.add(e);
					}
					break;
			}
		}
		
		return (String[])vars.toArray();
	}
}
