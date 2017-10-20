package nl.han.ica.icss.checker;

import java.util.HashMap;

import nl.han.ica.icss.ast.*;

import static nl.han.ica.icss.ast.Operation.Operator.*;

public class Checker {

    public enum ExpressionType {
        PIXELVALUE,
        PERCENTAGE,
        COLORVALUE,
        BOOLVALUE,
        UNDEFINED
    }

    private HashMap<String,Assignment> symboltable;

	public void check(AST ast) {
	    //Clear symbol table
        symboltable = new HashMap<>();

		//Save the symbol table.
        ast.symboltable = symboltable;
        //Save the verdict
		if(ast.getErrors().isEmpty()) {
            ast.checked = true;
        }
	}
}
